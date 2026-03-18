package uk.gov.courtservice.xhibit.courtlog.vos;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: Value Object used to create/update/delete Court Log Entries.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Joseph Babad / Paul Fitton
 * @version $Id: CourtLogCRUDValue.java,v 1.6 2006/06/05 12:28:20 bzjrnl Exp $
 */
public class CourtLogCRUDValue extends CourtLogAbstractValue {

	private static final long serialVersionUID = 371289955235964063L;

	/** Standard tags used in the XML generated from the Value Object. */
    public static final String ENTRY_DATE = "date";

    public static final String ENTRY_TIME = "time";

    public static final String EVENT_TYPE = "type";

    public static final String ENTRY_FREE_TEXT = "free_text";

    /** Property for processing linked cases */
    public static final String PROCESS_LINKED_CASES_PROPERTY = "process_linked_cases";

    /**
     * Used to hold the event-specific properties, which are converted to XML.
     */
    private Map propertyMap = new HashMap();

    /** Indicates whether the Court Log entry has been made in court. */
    private boolean inCourt;

    /** Free text associated with the Court Log Entry. */
    private String entryFreeText;

    /** Indicates whether we should process linked cases */
    private boolean processLinkedCases = true;

    public CourtLogCRUDValue() {
        super();
    }

    public CourtLogCRUDValue(Integer version) {
        super(version);
    }

    public void setPropertyMap(Map propertyMap) {
        this.propertyMap = propertyMap;
    }

    public Map getPropertyMap() {
        return this.propertyMap;
    }

    /**
     * Set an event property.
     * 
     * @param key
     *            Name of property
     * @param value
     *            Value of property.
     */
    public void setProperty(Object key, Object value) {
        this.propertyMap.put(key, value);
    }

    /**
     * Retrieve an event property
     * 
     * @param key
     *            The name of the property.
     * @return
     */
    public Object getProperty(Object key) {
        return this.propertyMap.get(key);
    }

    public void setInCourt(boolean inCourt) {
        this.inCourt = inCourt;
    }

    public boolean isInCourt() {
        return this.inCourt;
    }

    /**
     * Set the Free text for the entry.
     * 
     * @param entryFreeText
     */
    public void setEntryFreeText(String entryFreeText) {
        this.entryFreeText = entryFreeText;
    }

    /**
     * Get the entry free text.
     * 
     * @return
     */
    public String getEntryFreeText() {
        return this.entryFreeText;
    }

    /**
     * By default all linked cases should be processed unless the property on
     * the CRUD has been set, or this method is called with <i>false</i> as a
     * parameter.
     * 
     * @param processLinkedCases
     *            <code>boolean</code> to indicate if linked cases should be
     *            processed, if set to <i>true</i> then the event must also
     *            pass the remainder of the tests to see if the linked cases
     *            should be processed.
     */
    public void setProcessLinkedCases(boolean processLinkedCases) {
        this.processLinkedCases = processLinkedCases;
    }

    /**
     * @return
     */
    public boolean processLinkedCases() {
        // if the CRUD value contains a DefendantOnOffenceId then this is a
        // CRN level event and applies only to the case it was created on
        if (processLinkedCases && (getDefendantOnOffenceId() == null)) {
            Object prop = this.getProperty(PROCESS_LINKED_CASES_PROPERTY);
            return ((prop == null) || !("false".equalsIgnoreCase((String) prop)));
        }

        return false;
    }
}
