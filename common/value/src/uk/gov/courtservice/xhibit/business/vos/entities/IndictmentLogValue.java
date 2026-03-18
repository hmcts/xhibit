package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: IndictmentLogValue
 * </p>
 * <p>
 * Description: A Value Object where the attributes map one to one with the
 * IndictmentLog enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2011
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Brian Hingston
 * @version 1.0
 * 
 */

public class IndictmentLogValue extends CSAbstractValue {

	private static final long serialVersionUID = -1045752974065379440L;
	
	// private Integer indictmentLogID;
    private Integer caseID;

    private Integer sequenceNo;

    private String indictmentInfo;

    

    public IndictmentLogValue() {
        super();
    }

    public IndictmentLogValue(Integer version) {
        super(version);
    }

    public IndictmentLogValue(Integer id, Integer version) {
        super(id, version);
    }

    public IndictmentLogValue(Integer indictmentLogID, Integer caseID, Integer sequenceNo, String indictmentInfo, Integer version) {
        this(indictmentLogID, version);
        this.caseID = caseID;
        this.sequenceNo = sequenceNo;
        this.indictmentInfo = indictmentInfo;
        
    }


    public String getIndictmentInfo() {
        return indictmentInfo;
    }

    /*
     * use super getID() instead. public Integer getIndictmentLogID() { return
     * indictmentLogID; }
     */
    public Integer getCaseID() {
        return caseID;
    }

    public Integer getSequenceNo() {
        return sequenceNo;
    }

 

    public void setIndictmentInfo(String indictmentInfo) {
        this.indictmentInfo = indictmentInfo;
    }

    /*
     * use super setID() instead. public void setIndictmentLogID(Integer indictmentLogID) {
     * this.indictmentLogID = indictmentLogID; }
     */
    public void setSequenceNo(Integer sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public void setCaseID(Integer caseID) {
        this.caseID = caseID;
    }

    
}