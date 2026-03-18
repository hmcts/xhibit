package uk.gov.courtservice.xhibit.webservice.scjsestub.cjseservice.server;

import uk.gov.courtservice.xhibit.services.scjsestub.outbound.ScjseOutboundProcessorStub;
import uk.gov.courtservice.framework.services.CSServices;
import org.apache.log4j.Logger;
import java.util.HashMap;

/**
 * <p>
 * Title: Helper for inbound message testing
 * </p>
 * <p>
 * Description:
 *
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: InboundStubHelper.java,v 1.3 2007/03/06 11:59:06 qz4rwx Exp $ Exp $
 */

public class InboundStubHelper {

    private static final Logger log = CSServices.getLogger(InboundStubHelper.class);

    public InboundStubHelper() {}

    /**
     * Sends the Deliver Message to the inbound web service
     * All errors logged and suppressed
     *
     * @param String requestId
     * @param String messageType
     * @param String deliverMessage
     */
    protected void sendDeliverMessages(String requestId, String messageType, String deliverMessage) throws Exception
    {
        try {

            HashMap<String,String> propertiesAndValuesMap = new HashMap<String,String>();

            propertiesAndValuesMap = getDeliverMessageProperties();

            if(requestId!=null)
            {
                log.debug("requestId passed in as part of deliver properties so override properties file value to:" + requestId);
                propertiesAndValuesMap.put("XHBMessageIdentifier",requestId);
                propertiesAndValuesMap.put("DeliverRequest",requestId);
            }
            
            if(messageType!=null)
            {
                log.debug("messageType passed in as part of deliver properties so override properties file value to:" + messageType);
                propertiesAndValuesMap.put("XHBMessageTypeType",messageType);
            }
            
            //Force create of the stub version of the outbound processor which is designed for Deliver Requests
            //Do not go via the Factory as we will always require the stub version in this scenario
            //as the impl version is not designed to handle Deliver Requests
            ScjseOutboundProcessorStub scjseOutboundProcessor = new ScjseOutboundProcessorStub();

            scjseOutboundProcessor.processRequest(propertiesAndValuesMap, deliverMessage);
        }
        catch(Exception e)
        {
            log.warn("Warning: An error occured sending the deliver message to the remote inbound web service. Throw the exception");
            //e.printStackTrace(System.out);
            throw e;
        }
    }

    /**
     * Gets the Deliver Message Properties from file
     * @return HashMap
     */
    protected HashMap<String,String> getDeliverMessageProperties() throws Exception
    {
        return WebServiceStubProperties.getStubProperties(WebServiceStubProperties.DELIVER_PROPERTIES_FILE);
    }

}