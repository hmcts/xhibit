package uk.gov.courtservice.xhibit.business.services.exiss.inbound;

public enum InboundMessagePayloadType {
     EXCEPTION("EXCEPTION")
    ,MESSAGE("MESSAGE")
    ,DELIVERERROR("DELIVERERROR");

     private final String payloadType;
     
     private InboundMessagePayloadType(String payloadType) {
         this.payloadType = payloadType;
     }
     
     public String toString() {
         return payloadType;
     }
}
