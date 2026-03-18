package uk.gov.courtservice.xhibit.common.results.vos;

// jdk
import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

import uk.gov.courtservice.framework.business.vos.Parameterizeable;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;

/**
 * <p>
 * Title: DisposalValue
 * </p>
 * <p>
 * Description: This object represents the disposal data.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author: William Fardell, Xdevelopment (2004)
 * @version: 1.0
 */
public class DisposalValue extends ResultValue implements Parameterizeable, Serializable, Comparable {

    private static final long serialVersionUID = 988905974729231757L;

    private static final String COURT_TYPE_CROWN = "C";

    private static final String COURT_TYPE_MAGISTRATE = "M";

    /**
     * Map of XhbDisposalLineBasicValue objects or lists of
     * XhbDisposalLineBasicValue keyed against the ref_disposal_line_id.
     */
    private final SortedListMap lineMap = new SortedListMap(new DisposalLineComparator());

    /**
     * The basic value object we are wrapping
     */
    private XhbDisposal2BasicValue disposal;

    /**
     * Construct a new disposal value
     */
    public DisposalValue(int refDisposalTypeId, Integer defendantOnOffenceId, Integer defendantOnCaseId,
            String courtType, Integer psdDisposal2Id) {
        disposal = new XhbDisposal2BasicValue();

        setObsInd(false);

        setRefDisposalTypeId(refDisposalTypeId);

        // defendantOnOffenceId and defendantOnCaseId are mutually exclusive
        if (defendantOnOffenceId == null) {
            if (defendantOnCaseId == null) {
                throw new IllegalArgumentException("defendantOnCaseId: null defendantOnOffenceId: null");
            } else {
                setDefendantOnCaseId(defendantOnCaseId.intValue());
            }
        } else {
            if (defendantOnCaseId == null) {
                setDefendantOnOffenceId(defendantOnOffenceId.intValue());
            } else {
                throw new IllegalArgumentException("defendantOnCaseId: " + defendantOnCaseId
                        + " defendantOnOffenceId: " + defendantOnOffenceId);
            }
        }

        setCourtType(courtType);

        if (psdDisposal2Id != null) {
            setPsdDisposal2Id(psdDisposal2Id.intValue());
        }

    }

    /**
     * Construct a value to wrap the basic (gened) disposal values
     */
    public DisposalValue(XhbDisposal2BasicValue disposal) {
        setDisposal(disposal);
    }

    /**
     * Get the basic disposal value, used for persistence
     */
    public XhbDisposal2BasicValue getDisposal() {
        return disposal;
    }

    /**
     * Set the basic disposal object (cant be final as we need to reset after a
     * create to get the pk and version.
     */
    public void setDisposal(XhbDisposal2BasicValue disposal) {
        if (disposal == null) {
            throw new IllegalArgumentException("disposal: null");
        }
        this.disposal = disposal;
    }

    /**
     * Remove all lines from the disposal
     */
    public void clearLines() {
        lineMap.clear();
    }

    /**
     * Add Line, stores the line in the map if only one or in a list in the map
     * if multiple entries externally this should appear as a list of maps. The
     * implementation optimizes the common case where there is only one entry.
     * In particular serialization overhead is reduced.
     */
    public void addLine(XhbDisposalLineBasicValue line) {
        if (line == null || !equals(line.getDisposal2Id(), getDisposal2Id())) {
            throw new IllegalArgumentException("line: " + line);
        }
        lineMap.add(line.getRefDisposalLineId(), line);
    }

    /**
     * Set Line, replaces the line in the map if only one or in a list in the
     * map if multiple entries externally this should appear as a list of maps.
     * The implementation optimizes the common case where there is only one
     * entry. In particular serialization overhead is reduced.
     */
    public void setLine(XhbDisposalLineBasicValue line) {
        if (line != null && equals(line.getDisposal2Id(), getDisposal2Id())) {
            // Find and replace the existing line
            Integer refDisposalLineId = line.getRefDisposalLineId();
            for (int i = 0, c = getLineCount(refDisposalLineId); i < c; i++) {
                if (getLine(refDisposalLineId, i).getLineNumber().equals(line.getLineNumber())) {
                    lineMap.set(refDisposalLineId, i, line);
                    return; // Our Work Here Is Done!
                }
            }
        }
        throw new IllegalArgumentException("line: " + line);
    }

    /**
     * Get the refDisposalLineId with the specified index!
     */
    public XhbDisposalLineBasicValue getLine(int refDisposalLineId, int index) {
        return getLine(new Integer(refDisposalLineId), index);
    }

    public XhbDisposalLineBasicValue getLine(Integer refDisposalLineId, int index) {
        if (refDisposalLineId == null) {
            throw new IllegalArgumentException("refDisposalLineId: null");
        }
        return (XhbDisposalLineBasicValue) lineMap.get(refDisposalLineId, index);
    }

    /**
     * Get the number of lines for the ref line
     */
    public int getLineCount(int refDisposalLineId) {
        return getLineCount(new Integer(refDisposalLineId));
    }

    public int getLineCount(Integer refDisposalLineId) {
        if (refDisposalLineId == null) {
            throw new IllegalArgumentException("refDisposalLineId: null");
        }
        return lineMap.getCount(refDisposalLineId);
    }

    //
    // Accessors
    //
    public Integer getDisposal2Id() {
        return disposal.getDisposal2Id();
    }

    public Date getCreationDate() {
        return disposal.getCreationDate();
    }

    // Use isUnRelatedDisposal before calling
    public int getDefendantOnCaseId() {
        Integer defendantOnCaseId = disposal.getDefendantOnCaseId();
        if (defendantOnCaseId == null) {
            throw new IllegalStateException("defendantOnCaseId: null");
        }
        return defendantOnCaseId.intValue();
    }

    // Use isRelatedDisposal before calling
    public int getDefendantOnOffenceId() {
        Integer defendantOnOffenceId = disposal.getDefendantOnOffenceId();
        if (defendantOnOffenceId == null) {
            throw new IllegalStateException("defendantOnOffenceId: null");
        }
        return defendantOnOffenceId.intValue();
    }

    // Error if not set
    public int getRefDisposalTypeId() {
        Integer refDisposalTypeId = disposal.getRefDisposalTypeId();
        if (refDisposalTypeId == null) {
            throw new IllegalStateException("refDisposalTypeId: null");
        }
        return refDisposalTypeId.intValue();
    }

    public void setRefDisposalTypeId(int refDisposalTypeId) {
        disposal.setRefDisposalTypeId(new Integer(refDisposalTypeId));
    }

    public void setDefendantOnCaseId(int defendantOnCaseId) {
        disposal.setDefendantOnCaseId(new Integer(defendantOnCaseId));
        disposal.setDefendantOnOffenceId(null);
    }

    public void setDefendantOnOffenceId(int defendantOnOffenceId) {
        disposal.setDefendantOnCaseId(null);
        disposal.setDefendantOnOffenceId(new Integer(defendantOnOffenceId));
    }

    public void setCourtType(String courtType) {
        if (!isCourtType(courtType)) {
            throw new IllegalArgumentException("courtType: " + courtType);
        }
        disposal.setCourtType(courtType);
    }

    public void setPsdDisposal2Id(int psdDisposal2Id) {
        disposal.setPsdDisposal2Id(new Integer(psdDisposal2Id));
    }

    public void setObsInd(boolean obsInd) {
        disposal.setObsInd(obsInd ? "Y" : "N");
    }

    public boolean getObsInd() {
        return "Y".equals(disposal.getObsInd());
    }

    // wrapper support
    public Integer getDisId() {
        return disposal.getDisId();
    }

    public void setDisId(Integer disId) {
        disposal.setDisId(disId);
    }

    public String getCourtType() {
        return disposal.getCourtType();
    }

    public Integer getPsdDisposal2Id() {
        return disposal.getPsdDisposal2Id();
    }

    public void setPsdDisposal2Id(Integer psdDisposal2Id) {
        disposal.setPsdDisposal2Id(psdDisposal2Id);
    }

    //
    // Business
    //

    public boolean isCriminalDisposal() {
        return COURT_TYPE_CROWN.equals(disposal.getCourtType());
    }

    public boolean isMagistrateDisposal() {
        return COURT_TYPE_MAGISTRATE.equals(disposal.getCourtType());
    }

    public boolean isMagistrateGeneralDisposal() {
        return isMagistrateDisposal() && isUnrelatedDisposal();
    }

    public boolean isVariationDisposal() {
        return disposal.getPsdDisposal2Id() != null;
    }

    public boolean isUnrelatedDisposal() {
        return !isRelatedDisposal();
    }

    public boolean isRelatedDisposal() {
        return disposal.getDefendantOnOffenceId() != null;
    }

    private static boolean isCourtType(String courtType) {
        return COURT_TYPE_CROWN.equals(courtType) || COURT_TYPE_MAGISTRATE.equals(courtType);
    }

    /**
     * Parameterizeable Implementation
     */
    public Object[] getMessageParameters() {
        /**
         * @todo decide what parameters to pass in message
         */
        return new Object[0];
    }

    /**
     * Comparable implementation (sort into creation order)
     */
    public int compareTo(Object other) {
        Date thisDate = getCreationDate();
        Date otherDate = ((DisposalValue) other).getCreationDate();
        // null is latest!
        return thisDate == null ? (otherDate == null ? 0 : 1)
                : (otherDate == null ? -1 : thisDate.compareTo(otherDate));
    }

    /**
     * Append debug information to the buffer
     */
    public void appendDebug(StringBuffer buffer, int indent) {
        buffer.append(DisposalValue.class.getName());
        buffer.append(" {disposal=");
        buffer.append(disposal);
        buffer.append(",");
        indent += 1;
        appendLine(buffer, indent);
        buffer.append("lineMap=");
        lineMap.appendDebug(buffer, indent);
        indent -= 1;
        appendLine(buffer, indent);
        buffer.append("}");
    }

    /**
     * Utility to compare two objects symetrically (ie handle nulls correctly)
     */
    private static boolean equals(Object obj1, Object obj2) {
        return obj1 == null ? obj2 == null : obj2 != null && obj1.equals(obj2);
    }

    /**
     * Comparator used to sort the lines into order on line number!
     */
    private static class DisposalLineComparator implements Comparator, Serializable {

        private static final long serialVersionUID = 1L;

        public int compare(Object obj1, Object obj2) {
            int value1 = getLineNumber(obj1);
            int value2 = getLineNumber(obj2);
            return (value1 < value2 ? -1 : (value1 == value2 ? 0 : 1));
        }
    };

    private static int getLineNumber(Object obj) {
        return obj instanceof XhbDisposalLineBasicValue ? getLineNumber((XhbDisposalLineBasicValue) obj) : 0;
    }

    private static int getLineNumber(XhbDisposalLineBasicValue value) {
        Integer number = value.getLineNumber();
        return number != null ? number.intValue() : 0;
    }

    public SortedListMap getMap() {
    	return lineMap;
    }
}
