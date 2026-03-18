package uk.gov.courtservice.xhibit.services.scjsegateway.inbound;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.services.scjsegateway.common.ScjseGatewayMessageFactory;

public class ScjseInboundGatewayImpl implements ScjseInboundGateway {

    private static final Logger log = CSServices.getLogger(ScjseInboundGatewayImpl.class);

    public void sendMessage(Map<String, String> headerMap, String body) {
        if( log.isDebugEnabled() ) {
            logIt(headerMap, body);
        }
        CSServices.getJMSServices().send(
                new ScjseGatewayMessageFactory("scjsegateway/jms/ScjseInboundQueue", headerMap, body));
    }
    
    private void logIt(Map<String, String> headerMap, String body) {
        log.debug("Header properties and message body...");
        Set keySet = headerMap.keySet();
        Iterator iter = keySet.iterator();
        while(iter.hasNext()) {
            String name = (String)iter.next();
            String value = headerMap.get(name);
            log.debug("<" + name + ">/<" + value + ">");
        }
        log.debug("Message body:\n" + body);
    }
}
