package uk.gov.courtservice.xhibit.services.scjsegateway.outbound;

import java.util.Map;

/**
 * <p>
 * Title: RequestProcessor
 * </p>
 * <p>
 * Description: Interface for implementation of messaging used by the SCJSE
 * Gateway Services.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
public interface ScjseOutboundProcessor {
    public void processRequest(Map propertiesMap, String payload) throws Exception;
}