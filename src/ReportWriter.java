import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class ReportWriter {

	Date current = new Date();
	
	//Hard coded from reports.csv
	private static final String CLIENT_ADDRESS = "client-address";
	private static final String CLIENT_GUID = "client-guid";
	private static final String REQUEST_TIME = "request-time";
	private static final String SERVICE_GUID = "service-guid";
	private static final String RETRIES_REQUEST = "retries-request";
	private static final String PACKETS_REQUESTED = "packets-requested";
	private static final String PACKETS_SERVICED = "packets-serviced";
	private static final String MAX_HOLE_SIZE = "max-hole-size";
	
	//Hard coded from input reports.csv
	private static final String[] HEADER_ORDER = 
		{CLIENT_ADDRESS,CLIENT_GUID,REQUEST_TIME,SERVICE_GUID,RETRIES_REQUEST,PACKETS_REQUESTED,PACKETS_SERVICED,MAX_HOLE_SIZE};
	private static final char DELIMITER = ',';
	
	private static final String CSV_WRITE_PATH = "output/CombinedReport.csv";
	private static final String SUMMARY_WRITE_PATH = "output/CombinedSummary.txt";
	
	/**
	 * Writes the combined report.csv gathered from the three input report files.
	 * @param lines List of all ReportLine objects form all input report files. Call from main()
	 * @throws IOException
	 */
	//TODO: apply formatting dynamically based on input .csv formatting.
	public void writeCSV(List<ReportLine> lines) throws IOException
	{
		FileWriter fileWriter = new FileWriter(CSV_WRITE_PATH);

		CSVFormat writeFormat = CSVFormat.RFC4180.withHeader(HEADER_ORDER).withDelimiter(DELIMITER);
		CSVPrinter printer = new CSVPrinter(fileWriter, writeFormat);
		
		Collections.sort(lines);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzz");
		for(ReportLine line : lines) 
		{
			List<String> lineValues = new ArrayList<String>();
			lineValues.add(line.getClientAddress());
			lineValues.add(line.getClientGUID());
			lineValues.add(sdf.format(line.getRequestTime()));
			lineValues.add(line.getServiceGUID());
			lineValues.add(Integer.toString(line.getRetriesRequest()));
			lineValues.add(Integer.toString(line.getPacketsRequested()));
			lineValues.add(Integer.toString(line.getPacketsServiced()));
			lineValues.add(Integer.toString(line.getMaxHoleSize()));
			printer.printRecord(lineValues);
		}
		
		fileWriter.flush();
		fileWriter.close();
		printer.close();
	}
	
	/**
	 * Writes the summary. Counts distinct service-gui from all reports.
	 * @param lines List of all ReportLine objects form all input report files. Call from main()
	 * @throws IOException
	 */
	public void writeSummary(List<ReportLine> lines) throws IOException
	{
		FileWriter summaryWriter = new FileWriter(SUMMARY_WRITE_PATH);
		
		Set<String> serviceGUIDsDistinct = new HashSet<String>(); //used to grab all distinct service-gui
		List<String> serviceGUIDsAll = new ArrayList<String>(); //list of all service-gui
		
		for(ReportLine line : lines)
		{
			serviceGUIDsDistinct.add(line.getServiceGUID());
			serviceGUIDsAll.add(line.getServiceGUID());
		}
		
		summaryWriter.append("Total: " + Integer.toString(lines.size()));
		
		for(String s : serviceGUIDsAll)
		{
			int count = Collections.frequency(serviceGUIDsAll, s);
			//Read conflicting info about whether this is optimized by compiler or manual StringBuilder is required. 
			//Did not affect performance but larger reports probably will show slow downs
			summaryWriter.append("SERVICE-GUID: " + s + " COUNT: " + Integer.toString(count) + "\n"); 
		}
		
		summaryWriter.flush();
		summaryWriter.close();
	}
}
