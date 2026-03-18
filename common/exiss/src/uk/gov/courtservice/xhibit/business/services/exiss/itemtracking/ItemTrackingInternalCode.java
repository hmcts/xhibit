package uk.gov.courtservice.xhibit.business.services.exiss.itemtracking;

public enum ItemTrackingInternalCode {
    DISCARDED("DISCARDED"),
    NEW_ITEM("NEW_ITEM"),
    PROCESSING_MESSAGE("PROCESSING_MESSAGE"),
    SENT_TO_EXISS("SENT_TO_EXISS"),
    SENT_TO_WS_BRIDGE("SENT_TO_WS_BRIDGE"),
    UNDELIVERABLE("UNDELIVERABLE"),
    EXCEPTION("EXCEPTION"),
    SCJSE_MESSAGE_SENT_OK("SCJSE_MESSAGE_SENT_OK"),
    SCJSE_MESSAGE_SENT_ERROR("SCJSE_MESSAGE_SENT_ERROR"),
    SCJSE_MESSAGE_SENT_FATAL("SCJSE_MESSAGE_SENT_FATAL"),
    SCJSE_MESSAGE_GDDB_OK("SCJSE_MESSAGE_GDDB_OK"),
    SCJSE_MESSAGE_GDDB_ERROR("SCJSE_MESSAGE_GDDB_ERROR");

    private final String internalCode;
    
    private ItemTrackingInternalCode(String internalCode) {
        this.internalCode = internalCode;
    }
    
    public String getInternalCode() {
        return internalCode;
    }
}
