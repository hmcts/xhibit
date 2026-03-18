package echowebserviceclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileReader;

/**
 * This class is a helper class designed to read name/value pairs from the Web Service Clent
 * Echo Properties file
 * Each entry in the HashMap will be keyed on a unique name and value (separated by a space)
 */
public class EchoWSClientProperties {
    private static final String DEFAULT_ECHO_PROPERTIES_FILE = ".." + System.getProperty("file.separator", "/") + "properties" + System.getProperty("file.separator", "/") + "EchoWebServiceClientProperties.txt";

    private static HashMap echoProperties = null;

    /**
     * Helper Method to retrieve the stored Echo Properties from file
     *
     * @return HashMap
     */
    public static HashMap getEchoProperties() throws IOException {

		File propertiesFile = null;

		if(System.getProperty("echo.wsclient.properties") == null ||
		   System.getProperty("echo.wsclient.properties").trim().equals(""))
		{
			System.out.println("<< Getting echo properties from file (default location) called: >>: " + DEFAULT_ECHO_PROPERTIES_FILE);
			propertiesFile = new File("DEFAULT_ECHO_PROPERTIES_FILE");
		}
		else
		{
			System.out.println("<< Getting echo properties from file (location retrieved from echo.wsclient.properties)) called: >>: " + System.getProperty("echo.wsclient.properties"));
			propertiesFile = new File(System.getProperty("echo.wsclient.properties"));
		}

		echoProperties = getNameValueMap(propertiesFile,",");

        return echoProperties;
    }

    /**
     * Helper Method to retrieve the Web Service Echo Properties from file
     *
     * @param File
     * @param tokenizer
     * @return HashMap
     */
    private static HashMap<String,String> getNameValueMap(File file, String tokenizer) throws IOException {

        final HashMap<String,String> hashMap = new HashMap<String,String>();

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
                    propertyValue = st.nextToken();

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
}
