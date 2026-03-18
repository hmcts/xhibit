package uk.gov.courtservice.xhibit.courtlog;

/**
 * Operation type
 * 
 * @author pznwc5
 * @version $Revision: 1.7 $
 */
public class OperationType {
    public static final OperationType PRE_CREATE = new OperationType("PRE_CREATE");

    public static final OperationType POST_CREATE = new OperationType("POST_CREATE");

    public static final OperationType PRE_DELETE = new OperationType("PRE_DELETE");

    public static final OperationType POST_DELETE = new OperationType("POST_DELETE");

    public static final OperationType PRE_UPDATE = new OperationType("PRE_UPDATE");

    public static final OperationType POST_UPDATE = new OperationType("POST_UPDATE");

    public static final OperationType PRE_CREATE_LINKED = new OperationType("PRE_CREATE_LINKED");

    public static final OperationType POST_CREATE_LINKED = new OperationType("POST_CREATE_LINKED");

    public static final OperationType PRE_UPDATE_LINKED = new OperationType("PRE_UPDATE_LINKED");

    public static final OperationType POST_UPDATE_LINKED = new OperationType("POST_UPDATE_LINKED");

    private String type;

    private OperationType(String type) {
        this.type = type;
    }

    public String toString() {
        return type;
    }
}
