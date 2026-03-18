package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * The Reference Data Type, "Hearing Type".
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class RefHearingTypeComplexValue extends RefHearingTypeBasicValue {

	static final long serialVersionUID = -658600998253945727L;
	
	private RefCourtBasicValue refCourtValue = null;

    /**
     * Default constructor.
     */
    public RefHearingTypeComplexValue() {
    }

    /**
     * Standard constructor.
     * 
     * @param key
     *            Integer (refHearingTypeID)
     * @param version
     *            Integer (DB table row update counter)
     */
    public RefHearingTypeComplexValue(Integer key, Integer version) {
        super(key, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param key
     *            Integer (refHearingTypeID)
     * @param version
     *            Integer (DB table row update counter)
     * @param seqNo
     *            Integer
     * @param listSequence
     *            Integer
     * @param hearingTypeCode
     *            String
     * @param hearingTypeDesc
     *            String
     * @param category
     *            String
     * @param obsInd
     *            Integer
     * @param courtId
     *            Integer
     */
    public RefHearingTypeComplexValue(Integer key, Integer version, String category, Integer courtId, String code,
            String description, Integer listSequence, String obsoleteIndicator, Integer sequenceNo) {
        super(key, version, category, courtId, code, description, listSequence, obsoleteIndicator, sequenceNo);
    }

    public RefCourtBasicValue getRefCourtValue() {
        return this.refCourtValue;
    }

    public void setRefCourtValue(RefCourtBasicValue newValue) {
        this.refCourtValue = newValue;
    }
}