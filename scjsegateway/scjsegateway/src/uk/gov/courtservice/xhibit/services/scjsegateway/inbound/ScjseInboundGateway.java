package uk.gov.courtservice.xhibit.services.scjsegateway.inbound;

import java.util.Map;

public interface ScjseInboundGateway {
    public void sendMessage(Map<String, String> headerMap, String body);
}
