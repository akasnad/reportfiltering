import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Mariner interview assignment
 * @author Jason Wright
 * @version 1.0
 */
public class MarinerAssignment {

	//Locks into strict file structure
	//TODO: optional imput file paths.
	static final String URL_CSV = "reports/reports.csv";
	static final String URL_JSON = "reports/reports.json";
	static final String URL_XML = "reports/reports.xml";
	
	/**
	 * Entry point calling report readers and writers.
	 * @param args Unused
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException, ParseException 
	{
		List<ReportLine> allReportLines = new ArrayList<ReportLine>();

		ReportReader reader = new ReportReader();
		allReportLines.addAll(reader.readReport(URL_CSV));
			
		allReportLines.addAll(reader.readReport(URL_JSON));
		System.out.println(allReportLines.size());
		
		allReportLines.addAll(reader.readReport(URL_XML));
		
		ReportWriter writer = new ReportWriter();
		writer.writeCSV(allReportLines);
		writer.writeSummary(allReportLines);
	}

}
