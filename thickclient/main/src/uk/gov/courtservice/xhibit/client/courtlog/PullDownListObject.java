package uk.gov.courtservice.xhibit.client.courtlog;

/**
 * <p>
 * Title: PullDownListObject represents one line in a list or combo box
 * </p>
 * <p>
 * Description: Represents one line in a list or combo box for court log event
 * options.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class PullDownListObject {
    /**
     * The event option id. (The position of the object in a List)
     */
    private final int id;

    /**
     * The event option code. (E.g. E10200_Defendant_delayed)
     */
    private final String code;

    /**
     * The event option description. (E.g. Defendant delayed)
     */
    private final String desc;

    /**
     * Creates a PullDownListObject
     * 
     * @param id
     *            The event option id.
     * @param code
     *            The event option code.
     * @param desc
     *            The event option description.
     */
    public PullDownListObject(final int id, final String code, final String desc) {
        this.id = id;
        this.code = code;
        this.desc = desc;
    }

    /**
     * Gets the event option id.
     * 
     * @return The event option id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the event option code.
     * 
     * @return The event option code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the event option description.
     * 
     * @return The event option description.
     */
    public String getDesc() {
        return desc;
    }

    /**
     * A String representation of this object - the event option description.
     * 
     * @return A String representation of this object.
     */
    public String toString() {
        return desc;
    }
}