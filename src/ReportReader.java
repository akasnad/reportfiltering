import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

//TODO: Filtering here is inflexible. Create separate filter/format methods.
//TODO: XML and JSON reports get mapped to the ReportLine object regardless of value ordering but the CSV file formatting cannot be changed (headers, separators, etc. are hardcoded)
public class ReportReader {


	private static final String CLIENT_ADDRESS = "client-address";
	private static final String CLIENT_GUID = "client-guid";
	private static final String REQUEST_TIME = "request-time";
	private static final String SERVICE_GUID = "service-guid";
	private static final String RETRIES_REQUEST = "retries-request";
	private static final String PACKETS_REQUESTED = "packets-requested";
	private static final String PACKETS_SERVICED = "packets-serviced";
	private static final String MAX_HOLE_SIZE = "max-hole-size";


	/**
	 * Determines which reader to use based on file extension.
	 * @param path Report file path to be read.
	 * @return Filtered list of ReportLine objects to parent list containing all records.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<ReportLine> readReport(String path) throws FileNotFoundException, IOException, ParseException
	{
		List<ReportLine> allLines = new ArrayList<ReportLine>();
		
		String fileExtension = FilenameUtils.getExtension(path);

		if(fileExtension.equals("csv"))
		{
			allLines.addAll(readCSVFiltered(path));
		} 
		else if (fileExtension.equals("json")) 
		{
			allLines.addAll(readJSONFiltered(path));
		} 
		else if (fileExtension.equals("xml")) 
		{
			allLines.addAll(readXMLFiltered(path));
		}
		
		return allLines;
	}
	
	/**
	 * Parse given .csv file. Creates ReportLine objects for each file line.
	 * Preemptively filters out records with packets-serviced equal to 0.
	 * @param path reports.csv file path to be read passed from readReport()
	 * @return Filtered list of ReportLine objects read from the .csv file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public List<ReportLine> readCSVFiltered(String path) throws FileNotFoundException, IOException, ParseException
	{
		CSVFormat readFormat = CSVFormat.RFC4180.withHeader().withDelimiter(',');
		
		CSVParser parser = new CSVParser(new FileReader(path), readFormat);
		
		List<ReportLine> lines = new ArrayList<ReportLine>();

		for(CSVRecord record : parser) 
		{
			if(Integer.parseInt(record.get(PACKETS_SERVICED)) != 0)
			{
				ReportLine reportLine = new ReportLine();
				reportLine.setClientAddress(record.get(CLIENT_ADDRESS));
				reportLine.setClientGUID(record.get(CLIENT_GUID));
				reportLine.setRequestTime(record.get(REQUEST_TIME));
				reportLine.setServiceGUID(record.get(SERVICE_GUID));
				reportLine.setRetriesRequest(record.get(RETRIES_REQUEST));
				reportLine.setPacketsRequested(record.get(PACKETS_REQUESTED));
				reportLine.setPacketsServiced(record.get(PACKETS_SERVICED));
				reportLine.setMaxHoleSize(record.get(MAX_HOLE_SIZE));
				lines.add(reportLine);
			}
		}
		
		parser.close();

		return lines;
	}
	
	/**
	 * Creates Jackson json mapper to pass to parser method.
	 * Mapper allows creation of objects independent of value ordering within file.
	 * @param path reports.json file path to be read passed from readReport().
	 * @return Filtered list of ReportLine objects read from the .json file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<ReportLine> readJSONFiltered(String path)  throws FileNotFoundException, IOException
	{
		ObjectMapper mapperJSON = new ObjectMapper();	
		return ReadLines(mapperJSON, path);
	}

	/**
	 * Creates Jackson xml mapper to pass to parser method. 
	 * Mapper allows creation of objects independent of value ordering within file.
	 * @param path reports.xml file path to be read passed from readReport().
	 * @return Filtered list of ReportLine objects read from the .xml file.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<ReportLine> readXMLFiltered(String path)  throws FileNotFoundException, IOException
	{
		XmlMapper mapperXML = new XmlMapper();
		return ReadLines(mapperXML, path);
	}
	
	/**
	 * Parse given report file. Handles both JSON and XML. Creates ReportLine objects for each file line.
	 * Preemptively filters out records with packets-serviced equal to 0.
	 * @param mapper Either a Jackson json mapper or Jackson xml mappper.
	 * @param path reports file path to be read passed down from readReport()
	 * @return Filtered list of ReportLine objects.
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public List<ReportLine> ReadLines(ObjectMapper mapper, String path) throws FileNotFoundException, IOException
	{
		 List<ReportLine> lines = Arrays.asList(mapper.readValue(new File(path), ReportLine[].class));
		 List<ReportLine> mutableLines = new ArrayList<ReportLine>(lines); //Cannot use removeIf() with asList() it creates non-mutable list.
		 mutableLines.removeIf(p -> p.getPacketsServiced() == 0); //Filter out all records with packets-serviced equal to 0
		 
		 return mutableLines;
	}
}
