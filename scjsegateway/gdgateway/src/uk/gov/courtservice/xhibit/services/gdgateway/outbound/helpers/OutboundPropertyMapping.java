package uk.gov.courtservice.xhibit.services.gdgateway.outbound.helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: Enum that contains mapping between JMS properties and the XSD generated classes
 * </p>
 * <p>
 * Description: Enum that contains mapping between outbound JMS properties and the XSD generated classes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS,
 * @version $Id: OutboundPropertyMapping.java,v 1.1 2006/11/07 14:12:18 qz4rwx Exp $
 */

/**
 * Initialise Enum with acceptable values and their associated codes.
 */
public enum OutboundPropertyMapping {
    ReceiveXHBMessageIdentifier("ReceiveRequest,setMessageIdentifier"),
    DeliverXHBMessageIdentifier("DeliverRequest,setMessageIdentifier"),
    XHBMessageTypeType("MessageType,setType"),
    XHBMessageTypeVersion("MessageType,setVersion"),
    XHBMessageSchemaNamespace("MessageSchemaStructureChoiceSequence,setNamespace"),
    XHBMessageSchemaVersion("MessageSchemaStructureChoiceSequence,setVersion"),
    XHBMessageSchemaIdentifier("MessageSchemaStructureChoice,setIdentifier"),
    XHBOriginatingSystemName("OriginatingSystem,setName"),
    XHBOriginatingSystemOrgUnitCode("OriginatingSystem,setOrgUnitCode"),
    XHBOriginatingSystemEnvironment("OriginatingSystem,setEnvironment"),
    XHBCreationDateTime("MessageMetadata,setCreationDateTime"),
    XHBExpiryDateTime("MessageMetadata,setExpiryDateTime"),
    XHBRequestingSystemName("RequestingSystem,setName"),
    XHBRequestingSystemOrgUnitCode("RequestingSystem,setOrgUnitCode"),
    XHBRequestingSystemEnvironment("RequestingSystem,setEnvironment");

    OutboundPropertyMapping(String code) {
        this.code = code;
    }

    /**
     * Create a hashmap mapping the code values against the associated enum.
     */
    public static final Map<String, OutboundPropertyMapping> OutboundPropertyMappingMap = new HashMap<String, OutboundPropertyMapping>();
    static {
        for (OutboundPropertyMapping outboundPropertyMapping : OutboundPropertyMapping.values()) {
            OutboundPropertyMappingMap.put(outboundPropertyMapping.code, outboundPropertyMapping);
        }
    }

    private final String code;

    public String getCode() {
        return code;
    }

    /**
     * Custom toString method return Enum name and value.
     */
    public String toString() {
        return this.name() + "(" + code + ")";
    }
}

