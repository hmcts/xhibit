package datamigration1745;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;
import java.io.File;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import datamigration1745.database.DataMigrationDatabaseFactory;


/**
 * This class is a helper class designed to read name/value pairs from the Data Migration initialisation file
 * Each entry in the HashMap will be keyed on a unique name and value (separated by a space)
 * which is then used to build the DataMigrationPropsStructure
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: DataMigrationProperties.java,v 1.8 2009/03/12 13:09:50 valenzul Exp $ Exp $
 */
public class DataMigrationProperties {
    protected static final String DEFAULT_DM_PROPERTIES_FILE = ".." + System.getProperty("file.separator", "/") + "properties" + System.getProperty("file.separator", "/") + "datamigration.ini";

    private static HashMap<String,String> dmProperties = null;

    private static DataMigrationPropsStructure dataMigrationPropsStructure = null;

    private static final String DEFAULT_MODE="R";

    private static final String DEFAULT_REPORT_DIRECTORY = ".." + System.getProperty("file.separator", "/") + "reportbox";
    private static final String DEFAULT_ERROR_DIRECTORY  = ".." + System.getProperty("file.separator", "/") + "errorbox";

    private static final String DEFAULT_REPORT_NAME   = getCurrentDate() + "_data_migration_report.xml";
    private static final String DEFAULT_ERROR_NAME    = getCurrentDate() + "_data_migration_error.xml";
    
    /**
     * Helper Method to retrieve the stored Data Migration initialisation Properties from an ini file
     * and then set up a DataMigrationPropsStructure
     *
     * @return DataMigrationPropsStructure
     */
    public static DataMigrationPropsStructure getProperties() throws IOException, Exception {

        if(dataMigrationPropsStructure==null)
        {
            System.out.println("<< dataMigrationPropsStructure is null will now be built >>");
            
            dataMigrationPropsStructure = new DataMigrationPropsStructure();
           
            if(System.getProperty("database.password") != null &&
               !System.getProperty("database.password").trim().equals(""))
            {
                System.out.println("<< Found DB Password >>");
                dataMigrationPropsStructure.setDatabasePassword(System.getProperty("database.password"));
            }
            else
            {
                System.out.println("***** ERROR, Database Password must be supplied on the command line");
                throw new Exception("ERROR, Database Password must be supplied on the command line");                
            }
            
		    File propertiesFile = null;
            
		    if(System.getProperty("datamigration.properties") == null ||
		       System.getProperty("datamigration.properties").trim().equals(""))
		    {
		    	System.out.println("<< Getting data migration properties from file (default location) called: >>: " + DEFAULT_DM_PROPERTIES_FILE);
		    	propertiesFile = new File(DEFAULT_DM_PROPERTIES_FILE);
		    }
	    	else
	    	{
	    		System.out.println("<< Getting data migration properties from file (location retrieved from datamigration.properties)) called: >>: " + System.getProperty("datamigration.properties"));
	    		propertiesFile = new File(System.getProperty("datamigration.properties"));
	    	}

            dmProperties = getNameValueMap(propertiesFile,";");

            getDataMigrationPropsStructure(dmProperties);
        }
        
        return dataMigrationPropsStructure;
    }

    public static void processArguments(String[] args) {
        System.out.println("Start processArguments: args.length:" + args.length);
        String arg = null;
        String property = null;
        
        for (int i = 0; i < args.length; i++) {
            System.out.println("Arg found:" + args[i]);
            arg = args[i].trim();
            property = null;
            
            if (arg.startsWith("-p")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    if (property.startsWith("-?") || property.startsWith("-h")) {
                        System.out.println("-? or -h (help) found");
                        help();
                    } 
                    else
                    {
                        System.setProperty("database.password", property);
                    }
                }
                else {
                    throw new IllegalArgumentException(
                            "-p but no mandatory database password specified.");
                }
            }
            else if (arg.startsWith("-t")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    System.setProperty("datamigration.databasetype", property);
                }
                else {
                    System.out.println("-t but no database type specified for optional argument.");
                }
            } 
            else if (arg.startsWith("-m")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    System.setProperty("connection.pool.maxsize", property);
                }
                else {
                    System.out.println("-m but no database connection pool max size specified for optional argument.");
                }
            }
            else if (arg.startsWith("-l")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    System.setProperty("datamigration.properties", property);
                }
                else {
                    System.out.println("-l but no data migration ini file location specified for optional argument.");
                }
            }
            else if (arg.startsWith("-s")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    System.setProperty("datamigration.statistics", property);
                }
                else {
                    System.out.println("-s but no stats enabled specified specified for optional argument.");
                }
            }
            else if (arg.startsWith("-f")) {
                System.out.println("Arg tag found:" + arg);
                property = arg.substring(2);
                System.out.println("Arg Property found:" + property);
                if(property!=null && !property.trim().equals(""))
                {
                    System.setProperty("datamigration.reportformat", property);
                }
                else {
                    System.out.println("-f but no report format specified specified for optional argument.");
                }
            }
            else if (arg.startsWith("-?") || arg.startsWith("-h")) {
                System.out.println("-? or -h (help) found");
                help();
            } 
            else {
                throw new IllegalArgumentException("Unrecognised argument \""
                    + args[i] + "\".");
            }
        } 
        
        if(System.getProperty("database.password")==null || System.getProperty("database.password").trim().equals(""))
        {
            throw new IllegalArgumentException(
                "No mandatory database password specified.");
        }
    }
    
    /**
     * Helper Method to retrieve the Data Migration Properties from file
     *
     * @param File
     * @param tokenizer
     * @return HashMap
     */
    private static HashMap<String,String> getNameValueMap(File file, String tokenizer) throws IOException {

        HashMap<String,String> hashMap = new HashMap<String,String>();

        BufferedReader bufferedReader = null;
        String line = null;
        String formatLine = null;
        StringTokenizer st = null;
        String propertyName = null;
        String propertyValue = null;

        try
        {
			bufferedReader = new BufferedReader(new FileReader(file), (int) file.length());

            System.out.println("getNameValueMap tokenizer: " + tokenizer);

            while ((line = bufferedReader.readLine()) != null) {
                formatLine = line.trim();

                if (formatLine != null && !formatLine.equals("")) {
                    if (tokenizer == null || tokenizer.trim().equals("")) {
                        st = new StringTokenizer(formatLine);
                    } else {
                        st = new StringTokenizer(formatLine, tokenizer);
                    }

                    propertyName = st.nextToken();
                    if(st.hasMoreTokens()) {
                        propertyValue = st.nextToken();
                    }
                    else {
                        propertyValue = null;
                    }

                    System.out.println("propertyName: " + propertyName + " propertyValue: " + propertyValue);

                    hashMap.put(propertyName, propertyValue);
                }
            }
            return hashMap;
        }
        catch (final IOException e)
        {
            System.out.println("getNameValueMap IOException on bufferedReader:" + e);
            e.printStackTrace();
            return hashMap;
        }
        finally
        {
            try
            {
                if (bufferedReader != null)
                {
                    bufferedReader.close();
                }
            }
            catch (final IOException ioe)
            {
                System.out.println("getSet IOException on closing bufferedReader: " + ioe);
                ioe.printStackTrace();
                return hashMap;
            }
        }
    }

    /**
     * Builds Data Migration structure using a HashMap of properties
     *
     * @param HashMap
     */
    private static void getDataMigrationPropsStructure(HashMap<String,String> properties) throws Exception {
        
        String propertyName = null;
        String propertyValue = null;

        Iterator it = properties.keySet().iterator();

        while (it.hasNext()) {
            propertyName = (String) it.next();
            System.out.println("Data Migration propertyName: " + propertyName);

            propertyValue = properties.get(propertyName);
            
            System.out.println("Data Migration propertyValue: " + propertyValue);

            if (propertyName.trim().equals("mode")) {
                System.out.println("***** Found the Mode for the Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setMode(propertyValue);
                }
                else
                {
                    System.out.println("***** Use Default MOde the Data Migration:" + DEFAULT_MODE);
                    dataMigrationPropsStructure.setMode(DEFAULT_MODE);
                }
            }
            else if (propertyName.trim().equals("directory")) {
                System.out.println("***** Found the Report Directory for the Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setReportDir(propertyValue);
                }
                else
                {
                    System.out.println("***** Use Default Report Directory the Data Migration:" + DEFAULT_REPORT_DIRECTORY);
                    dataMigrationPropsStructure.setReportDir(DEFAULT_REPORT_DIRECTORY);
                }
            }
            else if (propertyName.trim().equals("errordirectory")) {
                System.out.println("***** Found the Error Directory for the Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setErrorDir(propertyValue);
                }
                else
                {
                    System.out.println("***** Use Default Error Directory the Data Migration:" + DEFAULT_ERROR_DIRECTORY);
                    dataMigrationPropsStructure.setErrorDir(DEFAULT_ERROR_DIRECTORY);
                }
            }
            else if (propertyName.trim().equals("filename")) {
                System.out.println("***** Found the File Name for the Data Migration Report:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setReportFile(propertyValue);
                }
                else
                {                    
                    System.out.println("***** Use Default File Name the Data Migration Report:" + DEFAULT_REPORT_NAME);
                    dataMigrationPropsStructure.setReportFile(DEFAULT_REPORT_NAME);
                }
            }
            else if (propertyName.trim().equals("errorfilename")) {
                System.out.println("***** Found the File Name for the Data Migration Error File:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setErrorFile(propertyValue);
                }
                else
                {
                    System.out.println("***** Use Default File Name the Data Migration File:" + DEFAULT_ERROR_NAME);
                    dataMigrationPropsStructure.setErrorFile(DEFAULT_ERROR_NAME);
                }
            }
            else if (propertyName.trim().equals("courts")) {
                System.out.println("***** Found the list of Courts for Data Migration:" + propertyValue);
                
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    ArrayList<Integer> courtList = new ArrayList<Integer>();
                    
                    StringTokenizer st = new StringTokenizer(propertyValue, ",");
                    
                    String token = null;
     
                    try
                    {
                        while(st.hasMoreElements())
                        {
                            /* Added to allow ranges such as 2-50 */
                            token = st.nextToken().trim();
                            if (token.indexOf("-") > 0) {
                                StringTokenizer st2 = new StringTokenizer(token, "-");
                                Integer startNo = Integer.valueOf(st2.nextToken().trim());
                                Integer endNo = Integer.valueOf(st2.nextToken().trim());
                                if ( startNo > endNo)
                                {
                                    System.out.println("***** ERROR, The ranges must be of form x-y where x < y.");
                                    throw new Exception("ERROR, range incorrect.");
                                }
                                
                                for( int court_x = startNo; court_x < (endNo + 1); court_x++ )
                                {
                                    System.out.println("***** Court ID is:" + court_x);
                                    courtList.add(Integer.valueOf(court_x));
                                }
                            }
                            else
                            {
                                System.out.println("***** Court ID is:" + token);
                                if (token != null && !token.equals("")) {
                                    courtList.add(Integer.valueOf(token));
                                } else {
                                    System.out.println("***** ERROR, The Court ID must be not be null or spaces");
                                    throw new Exception("ERROR, The Court ID must be not be null or spaces");
                                }
                            }                            
                        }
                    }
                    catch(NumberFormatException nfe)
                    {
                        System.out.println("***** ERROR, The Court ID must be a numeric");
                        throw new Exception("ERROR, The Court ID must be a numeric");
                    }
                    
                    if(!courtList.isEmpty())
                    {
                        dataMigrationPropsStructure.setCourts(courtList);
                    }
                    else
                    {
                        System.out.println("***** ERROR, at least one Court ID must be specified by the courts property is a csl");
                        throw new Exception("ERROR, at least one Court ID must be specified by the courts property is a csl");
                    }
                }
                else
                {
                    System.out.println("***** ERROR, courts property must be specified with at least one court ID in a csl");
                    throw new Exception("ERROR, courts property must be specified with at least one Court ID in a csl");
                }
            }
            else if (propertyName.trim().equals("database.driver")) {
                System.out.println("***** Found the Database Driver for Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setDatabaseDriver(propertyValue);
                }
                else
                {                    
                    System.out.println("***** ERROR, The Database Driver must be specified");
                    throw new Exception("ERROR, The Database Driver must be specified");
                }
            }
            else if (propertyName.trim().equals("database.url")) {
                System.out.println("***** Found the Database URL for Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setDatabaseUrl(propertyValue);
                }
                else
                {                    
                    System.out.println("***** ERROR, The Database URL must be specified");
                    throw new Exception("ERROR, The Database URL must be specified");
                }
            }
            else if (propertyName.trim().equals("database.user")) {
                System.out.println("***** Found the Database User for Data Migration:" + propertyValue);
                if(propertyValue!=null && !propertyValue.trim().equals(""))
                {
                    dataMigrationPropsStructure.setDatabaseUser(propertyValue);
                }
                else
                {                    
                    System.out.println("***** ERROR, The Database User must be specified");
                    throw new Exception("ERROR, The Database User must be specified");
                }
            }
            else
            {
                System.out.println("***** ERROR, unknown data migration property specified");
                throw new Exception("ERROR, unknown data migration property specified");
            }
        }
    }
    
    private static String getCurrentDate()
    {
        String date = "";
        
        DateFormat sdf = SimpleDateFormat.getInstance();
        ((SimpleDateFormat)sdf).applyPattern("yyyyMMdd");
        date = ((SimpleDateFormat)sdf).format(Calendar.getInstance().getTime());
        return date;
    }   
    
    private static void help() {
        System.out.println("Usage: java datamigration1745.DataMigration [-options]");
        System.out.println("where options include:");
        System.out.println("    -p <Database Password: String. Mandatory>");
        System.out.println("    -s <Data Migration Statisics. Optional (true or false), default is: (" + DataMigrationStatistics.DEFAULT_IS_DM_STATS_LOGGED + ")>");
        System.out.println("    -f <Report Format. ALL will produce one report, COURT will produce a report per Court Optional, default is: (" + DataMigrationDatabaseFactory.DEFAULT_REPORT_FORMAT + ")>");
        System.out.println("    -t <Database Connection Management Type: Must be either FIXED (recommended as the same connection can be reused) or RECREATE (each transaction creates a new connection). Optional, default is: (" + DataMigrationDatabaseFactory.DEFAULT_DB_TYPE + ")>");
        System.out.println("    -l <Location of Data Migration initialization properties. Optional, default is (" + DataMigrationProperties.DEFAULT_DM_PROPERTIES_FILE + ")>");
        System.out.println("    -m <Connection Pool Max Size (FIXED DB Connection Management Type only): Numeric (recommend 1). Optional, default is: (" + DataMigrationDatabaseFactory.DEFAULT_MAX_POOL_SIZE + ")>");
        System.out.println("    -h -? <print help and exit>");
        System.exit(0);
    }
}
