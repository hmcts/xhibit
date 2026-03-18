package uk.gov.courtservice.xhibit.integration.mercator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: MercatorURL
 * </p>
 * <p>
 * Description: Holds a URL for a HTTP connection
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author GJS
 * @version 1.0
 */
public class MercatorURL {
    private String mercatorAgentUrl = null;

    private static Logger log = CSServices.getLogger(MercatorURL.class);

    /**
     * Zero parm constructor
     * 
     * @roseuid 3DDBB1460363
     */
    private MercatorURL() {
    }

    /**
     * Constructor setting the MercatorAgentUrl
     * 
     * @param String
     */
    public MercatorURL(String newMercatorAgentUrl) {
        this.mercatorAgentUrl = newMercatorAgentUrl;
    }

    /**
     * Method to return the Mercator Agent URL.
     * 
     * The format of the URL is as follows http://%IP_ADDRESS%:%PORT%/%NAME eg
     * http://130.177.3.216:8081/appcomm
     * 
     * @return String representing the Mercator Agent URL
     */
    public String getMercatorAgentUrl() {
        log.debug("<< mercatorAgentUrl: " + mercatorAgentUrl + " >>");
        return mercatorAgentUrl;
    }
}