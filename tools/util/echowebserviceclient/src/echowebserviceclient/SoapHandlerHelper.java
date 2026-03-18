package echowebserviceclient;

import java.io.File;
import java.io.FileOutputStream;
import javax.xml.soap.SOAPMessage;

/**
 * This class is used by SOAP Handler classes to print Soap messages into external files etc..
 */
public class SoapHandlerHelper
{
    private static final String SOAP_TEST_FOLDER = "." + File.separatorChar + "sjcse_ws_echo_test";
    private static final String SOAP_ERROR_FOLDER = "." + File.separatorChar + "sjcse_ws_echo_error";

    private static final String WS_XML_FILE_LOGGING = "WS_XML_FILE_LOGGING";
    private static final String DEFAULT_WS_XML_FILE_LOGGING = "TRUE";

    private static final String WS_XML_ERROR_FILE_LOGGING = "WS_XML_ERROR_FILE_LOGGING";
    private static final String DEFAULT_WS_XML_ERROR_FILE_LOGGING = "TRUE";

	/**
     * <p>Title: createXmlFile</p>
     * <p>Description: Creates a SOAP file named after the SOAP message on the import
     * and containing the contents of that message</p>
     * @param SOAPMessage message
     */
	public static void createSoapFile(SOAPMessage message, String transactionName)
	{
		String fileName  = null;
		String directoryName  = null;
		File   directory = null;
		File   file      = null;

		FileOutputStream fileOS = null;

		try
		{
			if(transactionName.toLowerCase().indexOf("fault") != -1 ||
			   transactionName.toLowerCase().indexOf("error") != -1)
			{
				out("<< Directory is the Error Folder >>");
			    directoryName = SOAP_ERROR_FOLDER;
		    }
		    else
		    {
				out("<< Directory is the Test Folder >>");
				directoryName = SOAP_TEST_FOLDER;
			}


            fileName = FileHelper.getXmlFileName(transactionName, null, null);

	        out("<< Writing Soap message to an XML file called >>: " + fileName);

		    directory = FileHelper.createXmlDirectory(transactionName, directoryName, null);

			file = new File(directory, fileName);

	        out("<< Create FileOS >>");
	        fileOS = new FileOutputStream (file);

	        out("<< Write SOAP message to fileOS >>");
	        message.writeTo(fileOS);

	        out("<< Completed Writing SOAP message to fileOS >>");
		}
		catch(Exception e)
		{
			out("<< createSoapFile Exception >>: " + e);
		}
		finally
		{
			try
			{
				if (fileOS!=null)
			    {
					out("<< Close file os >>: ");
					fileOS.close();
			    }
			}
			catch(Exception e)
			{
				out("<< createSoapFile FileOutputStream close Exception >>: " + e);
			}
		}
	}


    /**
     * Return true if we require the deserialized XML from the
     * SOAP Request and Response to be logged to the hard drive for testing
     * Will be TRUE by default for the WS Stub
     *
     * @return boolean
     */
    public static boolean isWSFileLogging()
    {
    	String isWSFileLogging =
            System.getProperty(WS_XML_FILE_LOGGING,DEFAULT_WS_XML_FILE_LOGGING);

        out("<< isWSFileLogging: " + isWSFileLogging + " >>");

        return isWSFileLogging != null && isWSFileLogging.equalsIgnoreCase("TRUE");
    }

    /**
     * Return true if we require the deserialized XML from the
     * SOAP Request and Response to be logged to the hard drive when an error occurs
     * Will be TRUE by default for the WS Stub
     *
     * @return boolean
     */
    public static boolean isWSFileErrorLogging()
    {
    	String isWSFileErrorLogging =
            System.getProperty(WS_XML_ERROR_FILE_LOGGING,DEFAULT_WS_XML_ERROR_FILE_LOGGING);

        out("<< isWSFileErrorLogging: " + isWSFileErrorLogging + " >>");

        return isWSFileErrorLogging != null && isWSFileErrorLogging.equalsIgnoreCase("TRUE");
    }

    private static void out(String in)
    {
        System.out.println(in+"\n\n");
    }
}