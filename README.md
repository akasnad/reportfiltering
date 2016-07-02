DESCRIPTION

Reads in three "report" files; a .csv file, a .json file, and a .xml file. These files all share equal values but different formatting and value order. 
Outputs single .csv file (concatenation of the 3 input files) with identical formatting to input .csv file.
Applies basic filtering to the final report. These filters are rigid and cannot be modified at runtime in this version.
	Remove records with 0 packets serviced.
	Sort by request time in ascending order.
Outputs a summary of total unique service guid as well as counts of duplicates for each.

INSTRUCTIONS

Run MarinerAssignment JAR.
Summary and combined report will be generated in the "output" folder.
JAR is required to be in the assignment root folder.
The three included reports are required to be in the "reports" folder.
Source included.

TOOLS/LIBRARIES

Maven

At first I decided to skip learning how to use Maven and opted for manually adding whatever .JARs I would need. Finding out how to do that for Jackson, especially it's XML addons proved too much a time sink. Turns out I also needed Woodstox to get the XML mapper working but that wasn't explicitly stated. 
Turns out Maven is built right into Eclipse and did not take more than 10 minutes to get set up with all the required dependencies. 

Apache Commons CSV

Simple methods to parse and write CSV files. CSV has so many edge cases it would be a waste of time to roll my own parser. The included csv looks simple but there is now the option to implement handling changes to the formatting or column order of reports.csv if these features were ever requested/more time to implement them. I was torn between a few different libraries but in the end I chose Apache Commons for ease of use and the other Commons components that had features I would need.

Apache Commons IO

FilenameUtils.getExtension() to grab file extension and branch to different reader methods.

Apache Commons Lang

Originally was going to use for the DateUtils but did not end up needing them. 
Used NumberUtils to handle the epoch date format within reports.json.
  
Jackson

Provided parsing for both JSON and XML direct to POJO. Annotations mapped the values from the input files directly to the matching object field. This removed having to worry about re ordering the values to match with reports.csv.

Woodstox Core and Stax

Required for the Jackson XML mapper. 


Original Instructions

#Data sorting and filtering

Read the 3 input files reports.json, reports.csv, reports.xml and output a combined CSV file with the following characteristics:

- The same column order and formatting as reports.csv
- All report records with packets-serviced equal to zero should be excluded
- records should be sorted by request-time in ascending order

Additionally, the application should print a summary showing the number of records in the output file associated with each service-guid.

Please provide source, documentation on how to run the program and an explanation on why you chose the tools/libraries used.

##Submission

You may fork this repo, commit your work and let us know of your project's location, or you may email us your project files in a zip file.
