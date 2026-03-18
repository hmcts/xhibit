package uk.gov.courtservice.xhibit.common.results.vos;

// jdk
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title: ResultsReferenceValue
 * </p>
 * <p>
 * Description: Contains the results reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class ResultsReferenceValue implements Serializable {
	
	static final long serialVersionUID = 7917674047854573324L;
	
    /**
     * Formating Constant
     */
    private static String NL = System.getProperty("line.separator", "\n");

    /**
     * Formating Constant
     */
    private static String TAB = System.getProperty("line.tab", "    ");

    /**
     * The court Id of the reference data we are loading
     */
    private Integer courtId;

    /**
     * The root of the disposal menu (not orders!)
     */
    private DisposalMenuReferenceValue recordSheetDisposalMenuRoot;

    /**
     * The record sheet disposals (not orders!)
     */
    private DisposalReferenceValue[] recordSheetDisposals;

    /**
     * A map containing record sheet disposals keyed against there primary key
     * refDisposalTypeId this is generated when first accessed it is a cache of
     * the data in recordSheetDisposals. DO NOT Access diectly use
     * getRecordSheetDisposalMap to ensure creation is threadsafe
     */
    private transient Map recordSheetDisposalMap;

    /**
     * A map containing record sheet disposals keyed against DisposalCode it
     * only includes the latest template versions this is generated when first
     * accessed it is a cache of the data in recordSheetDisposals. DO NOT Access
     * diectly use getLatestRecordSheetDisposalMap to ensure creation is
     * threadsafe
     */
    private transient Map latestRecordSheetDisposalMap;

    /**
     * Construct an object to be populated with the specified data
     */
    public ResultsReferenceValue(Integer courtId) {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: " + courtId);
        }
        this.courtId = courtId;
    }

    /**
     * Get the court id the reference data is for
     * 
     * @return the court id
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * Initialise the record disposal menu and disposal data
     * 
     * @param rootDisposalMenuValue
     */
    public void setRecordSheetDisposalData(DisposalMenuReferenceValue recordSheetDisposalMenuRoot,
            DisposalReferenceValue[] recordSheetDisposals) {
        if (recordSheetDisposalMenuRoot == null || !recordSheetDisposalMenuRoot.isRoot()) {
            throw new ResultsValueException("recordSheetDisposalMenuRoot: " + recordSheetDisposalMenuRoot);
        }
        if (recordSheetDisposals == null) {
            throw new IllegalArgumentException("recordSheetDisposals: null");
        }
        this.recordSheetDisposalMenuRoot = recordSheetDisposalMenuRoot;
        this.recordSheetDisposals = recordSheetDisposals;
    }

    /**
     * Get the root of the disposal menu data
     * 
     * @return the root of the result disposal menu
     * @throws IllegalStateException
     *             if the record sheet disposal data has not been initialised
     */
    public DisposalMenuReferenceValue getRecordSheetDisposalMenuRoot() throws IllegalStateException {
        if (recordSheetDisposalMenuRoot == null) {
            throw new IllegalStateException("recordSheetDisposalMenuRoot: null");
        }
        return recordSheetDisposalMenuRoot;
    }

    /**
     * Get the latest result sheet disposal
     * 
     * @param disposalCode
     *            the code of the disposal to lookup
     * @return the DisposalReferenceValue or null if not found
     * @throws IllegalArgumentException
     *             if the code is null
     * @throws IllegalStateException
     *             if the recordSheetDisposals have not been initialised
     * @throws ResultsValueException
     *             if the requested disposal can not be found
     */
    public DisposalReferenceValue getLatestRecordSheetDisposal(String disposalCode) {
        if (disposalCode == null) {
            throw new IllegalArgumentException("disposalCode: null");
        }
        if (recordSheetDisposals == null) {
            throw new IllegalStateException("recordSheetDisposals: null");
        }

        DisposalReferenceValue value = (DisposalReferenceValue) getLatestRecordSheetDisposalMap().get(disposalCode);
        if (value != null) {
            return value;
        } else {
            throw new ResultsValueException("Could not find disposal reference data for code " + disposalCode + ".");
        }
    }

    private synchronized Map getLatestRecordSheetDisposalMap() {
        if (latestRecordSheetDisposalMap == null) {
            latestRecordSheetDisposalMap = createLatestDisposalMap(recordSheetDisposals);
        }
        return latestRecordSheetDisposalMap;
    }

    private static Map createLatestDisposalMap(DisposalReferenceValue[] disposals) {
        Map map = new HashMap();
        for (int i = 0; i < disposals.length; i++) {
            String disposalCode = disposals[i].getDisposalCode();
            DisposalReferenceValue latestValue = (DisposalReferenceValue) map.get(disposalCode);
            if (latestValue == null || latestValue.getTemplateVersion() < disposals[i].getTemplateVersion()) {
                map.put(disposalCode, disposals[i]);
            }
        }
        return map;
    }

    /**
     * Get a disposal from the record sheet map
     * 
     * @param refDisposalTypeId
     *            the pk of the disposal to lookup
     * @return the DisposalReferenceValue or null if not found
     * @throws IllegalArgumentException
     *             if either is null
     * @throws IllegalStateException
     *             if the recordSheetDisposals have not been initialised
     * @throws ResultsValueException
     *             if the requested disposal can not be found
     */
    public DisposalReferenceValue getRecordSheetDisposal(int refDisposalTypeId) {
        return getRecordSheetDisposal(new Integer(refDisposalTypeId));
    }

    public DisposalReferenceValue getRecordSheetDisposal(Integer refDisposalTypeId) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }

        if (recordSheetDisposals == null) {
            throw new IllegalStateException("recordSheetDisposals: null");
        }

        DisposalReferenceValue value = (DisposalReferenceValue) getRecordSheetDisposalMap().get(refDisposalTypeId);
        if (value != null) {
            return value;
        } else {
            throw new ResultsValueException("Could not find disposal reference data for id " + refDisposalTypeId + ".");
        }
    }

    /**
     * Determine whether a reference Disposal is of type recordsheet by
     * searching the record sheet map.
     * 
     * @param refDisposalTypeId
     * @return
     */
    public boolean isRecordSheetDisposal(int refDisposalTypeId) {
        return isRecordSheetDisposal(new Integer(refDisposalTypeId));
    }

    public boolean isRecordSheetDisposal(Integer refDisposalTypeId) {
        if (refDisposalTypeId == null) {
            throw new IllegalArgumentException("refDisposalTypeId: null");
        }

        if (recordSheetDisposals == null) {
            throw new IllegalStateException("recordSheetDisposals: null");
        }
        return getRecordSheetDisposalMap().containsKey(refDisposalTypeId);
    }

    private synchronized Map getRecordSheetDisposalMap() {
        if (recordSheetDisposalMap == null) {
            recordSheetDisposalMap = createDisposalMap(recordSheetDisposals);
        }
        return recordSheetDisposalMap;
    }

    private static Map createDisposalMap(DisposalReferenceValue[] disposals) {
        Map map = new HashMap();
        for (int i = 0; i < disposals.length; i++) {
            map.put(new Integer(disposals[i].getRefDisposalTypeId()), disposals[i]);
        }
        return map;
    }

    /**
     * Get the number of of record sheet disposals
     * 
     * @return the number of record sheet disposals
     * @throws IllegalStateException
     *             if the recordSheetDisposals have not been initialised
     */
    public int getRecordSheetDisposalCount() {
        if (recordSheetDisposals == null) {
            throw new IllegalStateException("recordSheetDisposals: null");
        }
        return recordSheetDisposals.length;
    }

    /**
     * Get the record sheet disposal with the specified index
     * 
     * @return the record sheet with the specified index
     */
    public DisposalReferenceValue getRecordSheetDisposalAt(int index) {
        // Assume getRecordSheetDisposalCount will have been called first so
        // very unlikely
        // recordSheetDisposals will be null.
        try {
            return recordSheetDisposals[index];
        } catch (NullPointerException npe) {
            throw new IllegalStateException("recordSheetDisposals: null");
        } catch (ArrayIndexOutOfBoundsException aiobe) {
            throw new IllegalArgumentException("index: " + index);
        }
    }

    /**
     * Return a String representation of this object
     * 
     * @return a string containing a summary of this object
     */
    public String toString() {
        return ResultsReferenceValue.class.getName()
                + "{recordSheetDisposalMenuRoot="
                + (recordSheetDisposalMenuRoot == null ? "null" : String.valueOf(recordSheetDisposalMenuRoot
                        .getMenuItemId())) + ", recordSheetDisposals="
                + (recordSheetDisposals == null ? "null" : String.valueOf(recordSheetDisposals.length)) + "}";
    }

    /**
     * Return a string containing debug infromation (note this can be very
     * verbose)
     * 
     * @return the string containg full details of this object
     */
    public String toDebug() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(ResultsReferenceValue.class.getName());
        buffer.append(" {");
        buffer.append(NL);

        // Record Sheet Disposal Menu
        buffer.append(TAB);
        buffer.append("recordSheetDisposalMenuRoot {");
        buffer.append(NL);
        if (recordSheetDisposalMenuRoot == null) {
            buffer.append(TAB);
            buffer.append("null");
            buffer.append(NL);
        } else {
            setDebug(buffer, recordSheetDisposalMenuRoot, 2);
        }
        buffer.append(TAB);
        buffer.append("}");
        buffer.append(NL);

        // Record Sheet Disposals
        buffer.append(TAB);
        buffer.append("recordSheetDisposals {");
        buffer.append(NL);
        if (recordSheetDisposals == null) {
            buffer.append(TAB);
            buffer.append("null");
            buffer.append(NL);
        } else {
            if (0 < recordSheetDisposals.length) {
                setDebug(buffer, recordSheetDisposals[0]);
                for (int i = 1; i < recordSheetDisposals.length; i++) {
                    setDebug(buffer, recordSheetDisposals[i]);
                }
            } else {
                buffer.append(TAB);
                buffer.append("empty");
                buffer.append(NL);
            }
        }
        buffer.append(TAB);
        buffer.append("}");
        buffer.append(NL);

        buffer.append("}");
        buffer.append(NL);

        return buffer.toString();
    }

    private static void setDebug(StringBuffer buffer, DisposalReferenceValue value) {
        buffer.append(TAB);
        buffer.append(TAB);
        buffer.append(value);
        buffer.append(NL);
        for (int i = 0, c = value.getLineCount(); i < c; i++) {
            buffer.append(TAB);
            buffer.append(TAB);
            buffer.append(TAB);
            buffer.append(value.getLine(i));
            buffer.append(NL);
        }
    }

    private static void setDebug(StringBuffer buffer, DisposalMenuReferenceValue value, int level) {
        for (int i = 0; i < level; i++) {
            buffer.append(TAB);
        }
        buffer.append(value);
        buffer.append(NL);
        for (int i = 0, c = value.getChildValueCount(); i < c; i++) {
            setDebug(buffer, value.getChildValue(i), level + 1);
        }
    }

}
