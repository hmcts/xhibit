package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.AbstractReferenceDataValue;

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
public class RefHearingTypeBasicValue extends AbstractReferenceDataValue {
	private static final long serialVersionUID = -1832711325936148293L;
    private String category = null;

    private Integer courtId = null;

    private String hearingTypeCode = null;

    private String hearingTypeDesc = null;

    private Integer listSequence = null;

    private String obsInd = null;

    private Integer seqNo = null;

    /**
     * Default constructor.
     */
    public RefHearingTypeBasicValue() {
    }

    /**
     * Standard constructor.
     * 
     * @param key
     *            Integer (refHearingTypeID)
     * @param version
     *            Integer (DB table row update counter)
     */
    public RefHearingTypeBasicValue(Integer key, Integer version) {
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
     * 
     * @todo Find out what seqNo & listSequence are
     */
    public RefHearingTypeBasicValue(Integer key, Integer version, String category, Integer courtId, String code,
            String description, Integer listSequence, String obsoleteIndicator, Integer sequenceNo) {

        this(key, version);
        this.category = category;
        this.courtId = courtId;
        this.hearingTypeCode = code;
        this.hearingTypeDesc = description;
        this.listSequence = listSequence;
        this.obsInd = obsoleteIndicator;
        this.seqNo = sequenceNo;
    }

    /*------------------------------------- Accessors -------------------------------------*/

    public String getCategory() {
        return this.category;
    }

    public Integer getCourtId() {
        return this.courtId;
    }

    public String getHearingTypeCode() {
        return this.hearingTypeCode;
    }

    public String getHearingTypeDesc() {
        return this.hearingTypeDesc;
    }

    public Integer getListSequence() {
        return this.listSequence;
    }

    public String getObsInd() {
        return this.obsInd;
    }

    public Integer getSeqNo() {
        return this.seqNo;
    }

    /*------------------------------------- Setters -------------------------------------*/

    public void setCategory(String newValue) {
        this.category = newValue;
    }

    public void setCourtId(Integer newValue) {
        this.courtId = newValue;
    }

    public void setHearingTypeCode(String newValue) {
        this.hearingTypeCode = newValue;
    }

    public void setHearingTypeDesc(String newValue) {
        this.hearingTypeDesc = newValue;
    }

    public void setListSequence(Integer newValue) {
        this.listSequence = newValue;
    }

    public void setObsInd(String newValue) {
        this.obsInd = newValue;
    }

    public void setSeqNo(Integer newValue) {
        this.seqNo = newValue;
    }

    /**
     * The summary description of this Value.
     * <p>
     * I have taken a guess at the values to use here.
     * </p>
     * 
     * @return String
     */
    public String summaryInformation() {

        /** @todo Identify the corect summary values. */
        return this.getHearingTypeCode() + " - " + this.getHearingTypeDesc();
    }
}