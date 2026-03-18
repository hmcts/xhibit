package uk.gov.courtservice.xhibit.webservice.gdgateway.cjseservice.client;

/**
 * <p>
 * Outbound constants
 * </p>
 * <p>
 * Description: Outbound constants
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: OutboundConstants.java,v 1.3 2006/12/07 17:28:38 qz4rwx Exp $ Exp $
 */

public class OutboundConstants {

    //WSDL property names  
    public static final String REQUEST_ID = "RequestId";
    public static final String SOURCE_IDENTIFIER = "SourceIdentifier";
    public static final String DESTINATION_IDENTIFIER = "DestinationIdentifier";
    public static final String EXEC_MODE = "ExecMode";
    public static final String REQUEST_TIMESTAMP = "RequestTimestamp";

    //URL property name of the remote Web Service
    public static final String SERVICE_URL = "ServiceUrl";

    //Web Service Timeouts Property names
    public static final String CONNECTION_TIMEOUT = "ConnectionTimeout";
    public static final String READ_TIMEOUT = "ReadTimeout";
    
    //Item tracking Queue
    public static final String ITEM_TRACKING_QUEUE = "scjsegateway/jms/ScjseItemTrackingQueue";
    
    //Proxy Password Lookup (this is done from the Weblogic startup properties as opposed to the database which is why it is not in ConfigPropertyCodes)  
    public static final String PROXY_PASSWORD_LOOKUP = "PROXY_PASSWORD";
    
    //Property names only used if the remote web service is accessed thru a firewall via a proxy    
    public static final String PROXY_HOST     = "ProxyHost";
    public static final String PROXY_PORT     = "ProxyPort";
    public static final String PROXY_USER     = "ProxyUser";
    public static final String PROXY_PASSWORD = "ProxyPassword";

    //Lookup properties from weblogic startup properties where the Database is not being used for config storage (eg testing)
    public static final String SCJSE_SERVER_START_ARGUMENTS_ENABLED = "scjse.serverstart.args.enabled";
}
