package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.soap.SOAPMessage;

import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;

/**
 * This class is used by SOAP Handler classes to print Soap messages into external files etc..
 */
public class SoapHandlerHelper
{
    protected static final String SOAP_TEST_FOLDER = "." + File.separatorChar + "sjcse_ws_stub_log";
    protected static final String SOAP_ERROR_FOLDER = "." + File.separatorChar + "sjcse_ws_stub_error";

    private static final String WS_XML_FILE_LOGGING = "WS_XML_FILE_LOGGING";
    private static final String DEFAULT_WS_XML_FILE_LOGGING = "FALSE";

    private static final String WS_XML_ERROR_FILE_LOGGING = "WS_XML_ERROR_FILE_LOGGING";
    private static final String DEFAULT_WS_XML_ERROR_FILE_LOGGING = "TRUE";

    private static final Logger log = CSServices.getLogger(SoapHandlerHelper.class);

	/**
     * <p>Title: createXmlFile</p>
     * <p>Description: Creates a SOAP file named after the SOAP message on the import
     * and containing the contents of that message</p>
     * @param SOAPMessage
     * @param String - transactionName
     * @param String - id
     */
    public static void createSoapFile(SOAPMessage message, String transactionName, String id)
	{
		String fileName  = null;
		String directoryName  = null;
		File   directory = null;
		File   file      = null;

		FileOutputStream fileOS = null;

		try
		{
            directoryName = determineDirectoryName(transactionName);

            log.debug("<< Filename will be determined using transactionName:" + transactionName + " and id:" + id + " >>");
            
            fileName = FileHelper.getXmlFileName(transactionName, null, id);

	        log.debug("<< Writing Soap message to an XML file called >>: " + fileName);

		    directory = FileHelper.createXmlDirectory(transactionName, directoryName, null);

			file = new File(directory, fileName);

	        log.debug("<< Create FileOS >>");
	        fileOS = new FileOutputStream (file);

	        log.debug("<< Write SOAP message to fileOS >>");
	        message.writeTo(fileOS);

	        log.debug("<< Completed Writing SOAP message to fileOS >>");
		}
		catch(Exception e)
		{
			log.warn("<< createSoapFile Exception >>: " + e);
		}
		finally
		{
			try
			{
				if (fileOS!=null)
			    {
					log.debug("<< Close file os >>: ");
					fileOS.close();
			    }
			}
			catch(Exception e)
			{
				log.warn("<< createSoapFile FileOutputStream close Exception >>: " + e);
			}
		}
	}


    /**
     * Determines whether the directory will be the stub or error folder
     *
     * @param String
     * @return String
     */
    public static String determineDirectoryName(String transactionName)
    {
        String directoryName = SOAP_TEST_FOLDER;
        
        if(transactionName.toLowerCase().indexOf("fault") != -1 ||
           transactionName.toLowerCase().indexOf("error") != -1)
        {
            log.debug("<< Directory is the Error Folder >>");
            directoryName = SOAP_ERROR_FOLDER;
        }        
        return directoryName;
    }
    
    /**
     * Return true if we require the deserialized XML from the
     * SOAP Request and Response to be logged to the hard drive for testing
     * Will be FALSE by default as debug should only be produced by the stub
     *
     * @return boolean
     */
    public static boolean isWSFileLogging()
    {
    	String isWSFileLogging =
            System.getProperty(WS_XML_FILE_LOGGING,DEFAULT_WS_XML_FILE_LOGGING);

        log.debug("<< isWSFileLogging: " + isWSFileLogging + " >>");

        return isWSFileLogging != null && isWSFileLogging.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require the deserialized XML from the
     * SOAP Request and Response to be logged to the hard drive when an error occurs
     * Will be TRUE by default to ensure errors are always logged
     *
     * @return boolean
     */
    public static boolean isWSFileErrorLogging()
    {
    	String isWSFileErrorLogging =
            System.getProperty(WS_XML_ERROR_FILE_LOGGING,DEFAULT_WS_XML_ERROR_FILE_LOGGING);

        log.debug("<< isWSFileErrorLogging: " + isWSFileErrorLogging + " >>");

        return isWSFileErrorLogging != null && isWSFileErrorLogging.equalsIgnoreCase("TRUE");
    }
}