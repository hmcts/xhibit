package uk.gov.courtservice.xhibit.services.scjsegateway.inbound;

import uk.gov.courtservice.framework.services.CSServices;

public class ScjseInboundGatewayFactory {

    private static final ScjseInboundGatewayFactory instance = new ScjseInboundGatewayFactory();

    public static ScjseInboundGatewayFactory getInstance() {
        return instance;
    }
    
    private ScjseInboundGatewayFactory() {        
    }

    public ScjseInboundGateway getScjseInboundGateway() {
        return (ScjseInboundGateway) CSServices.getDiscoveryServices().createInstance(ScjseInboundGateway.class);
    }
}
