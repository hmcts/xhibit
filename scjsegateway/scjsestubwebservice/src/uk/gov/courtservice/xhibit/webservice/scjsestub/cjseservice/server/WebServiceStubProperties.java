package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.File;
import java.io.FileReader;

import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;

/**
 * This class is a helper class designed to read name/value pairs from the Web Service
 * Stub Properties file, which should be copied to the Weblogic logs directory
 * Each entry in the HashMap will be keyed on a unique name and value (separated by a space)
 */
public class WebServiceStubProperties {
    protected static final String STUB_PROPERTIES_FILE    = System.getProperty("webservice.stub.dir") + System.getProperty("file.separator", "/") + "WebServiceStubProperties.txt";
    protected static final String DELIVER_PROPERTIES_FILE = System.getProperty("webservice.stub.dir") + System.getProperty("file.separator", "/") + "WebServiceDeliverProperties.txt";

    private static HashMap stubProperties = null;

    private static final Logger log = CSServices.getLogger(WebServiceStubProperties.class);

    /**
     * Helper Method to retrieve the stored stubProperties
     *
     * @param String
     * @return HashMap
     */
    public static HashMap getStubProperties(String propertiesType) throws IOException {

        if(propertiesType.equals(STUB_PROPERTIES_FILE))
        {
		    log.debug("STUB_PROPERTIES_FILE: " + STUB_PROPERTIES_FILE);

		    stubProperties = getNameValueMap(new File(STUB_PROPERTIES_FILE),",");
        }
        else if(propertiesType.equals(DELIVER_PROPERTIES_FILE))
        {
            log.debug("DELIVER_PROPERTIES_FILE: " + DELIVER_PROPERTIES_FILE);

            stubProperties = getNameValueMap(new File(DELIVER_PROPERTIES_FILE),",");
        }

        return stubProperties;
    }

    /**
     * Helper Method to retrieve the Web Service Stub Properties from file
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

            log.debug("getNameValueMap tokenizer:" + tokenizer);

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

                    log.debug("propertyName: " + propertyName + " propertyValue: " + propertyValue);

                    hashMap.put(propertyName, propertyValue);
                }
            }
            return hashMap;
        }
        catch (final IOException e)
        {
            log.warn("getNameValueMap IOException on bufferedReader:" + e);
            e.printStackTrace(System.out);
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
                log.warn("getSet IOException on closing bufferedReader: " + ioe);
                ioe.printStackTrace(System.out);
                return hashMap;
            }
        }
    }
}
