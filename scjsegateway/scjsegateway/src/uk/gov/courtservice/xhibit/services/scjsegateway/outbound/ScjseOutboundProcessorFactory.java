package uk.gov.courtservice.xhibit.services.scjsegateway.outbound;

import uk.gov.courtservice.framework.services.CSServices;

public class ScjseOutboundProcessorFactory {

    private static final ScjseOutboundProcessorFactory instance = new ScjseOutboundProcessorFactory();

    public static ScjseOutboundProcessorFactory getInstance() {
        return instance;
    }
    
    private ScjseOutboundProcessorFactory() {        
    }

    public ScjseOutboundProcessor getScjseOutboundProcessor() {
        return (ScjseOutboundProcessor) CSServices.getDiscoveryServices().createInstance(ScjseOutboundProcessor.class);
    }
}