package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

public enum MdiResponseCode {
    SUCCESS(1),
    ERROR(200),
    TEMPORARY_PROBLEM(201),
    SERVER_FAILURE(202),
    REQUEST_ID_MISSING(306),
    REQUEST_ID_INVALID(306),
    SOURCE_ID_MISSING(304),
    SOURCE_ID_INVALID(304),
    SOURCE_ID_COURT_SERVICE_HUB(304),
    DESTINATION_ID_MISSING(305),
    DESTINATION_ID_INVALID(305),
    DESTINATION_ID_MANY(305),
    EXEC_MODE_ID_MISSING(307),
    EXEC_MODE_ID_INVALID(307),
    MESSAGE_MISSING(300),
    FATAL_MISSING(300);

    private final int code;

    MdiResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
