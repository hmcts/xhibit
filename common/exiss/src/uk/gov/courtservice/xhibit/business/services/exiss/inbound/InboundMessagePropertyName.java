package uk.gov.courtservice.xhibit.business.services.exiss.inbound;

public enum InboundMessagePropertyName {
     REQUEST_ID("SCJRequestId")
    ,SOURCE_ID("SCJSourceId")
    ,DESTINATION_ID("SCJDestinationId")
    ,EXEC_MODE("SCJExecMode")
    ,TIMESTAMP("SCJTimestamp")
    ,PAYLOAD_TYPE("SCJType")
    ,ACK_REQUESTED("SCJAckRequested")
    ,MESSAGE_IDENTIFIER("SCJMessageIdentifier")
    ,REQUESTING_SYSTEM_ENVIRONMENT("SCJRequestingSystemEnvironment")
    ,REQUESTING_SYSTEM_NAME("SCJRequestingSystemName")
    ,REQUESTING_SYSTEM_ORG_UNIT_CODE("SCJRequestingSystemOrgUnitCode")
    ,CREATION_DATE_TIME("SCJCreationDateTime")
    ,EXPIRY_DATE_TIME("SCJExpiryDateTime")
    ,ORIGINATING_SYSTEM_NAME("SCJOriginatingSystemName")
    ,ORIGINATING_SYSTEM_ORG_UNIT_CODE("SCJOriginatingSystemOrgUnitCode")
    ,ORIGINATING_SYSTEM_ENVIRONMENT("SCJOriginatingSystemEnvironment")
    ,MESSAGE_SCHEMA_IDENTIFIER("SCJMessageSchemaIdentifier")
    ,MESSAGE_SCHEMA_NAMESPACE("SCJMessageSchemaNamespace")
    ,MESSAGE_SCHEMA_VERSION("SCJMessageSchemaVersion")
    ,MESSAGE_TYPE_TYPE("SCJMessageTypeType")
    ,MESSAGE_TYPE_VERSION("SCJMessageTypeVersion")
    ,CORRELATION_ID("SCJCorrelationId");

     private final String internalName;
     
     private InboundMessagePropertyName(String internalName) {
         this.internalName = internalName;
     }
     
     public String toString() {
         return internalName;
     }
}
