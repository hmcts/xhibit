package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: LeoAdvLinkBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the LeoAdvLink
 * enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */
public class LeoAdvLinkBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = 9097884866345527357L;
    private Integer legalAidOrderId;
    private Integer defendantOnCaseId;
    private Integer refAdvocateId;
    private String crestAdvCategory;
    private Integer crestPostNumber;
    private String available;
    private String newRowFlag;
    private String obsInd;

    public LeoAdvLinkBasicValue() {
        super();
    }

    public LeoAdvLinkBasicValue(Integer version) {
        super(version);
    }

    public LeoAdvLinkBasicValue(Integer leoAdvLinkId, Integer version) {
        super(leoAdvLinkId, version);
    }

    public LeoAdvLinkBasicValue(
            Integer legalAidOrderId, 
            Integer defendantOnCaseId,
            Integer refAdvocateId,
            String crestAdvCategory,
            Integer crestPostNumber,
            String available,
            String newRowFlag,
            String obsInd) {
        this.legalAidOrderId = legalAidOrderId;
        this.defendantOnCaseId = defendantOnCaseId;
        this.refAdvocateId = refAdvocateId;
        this.crestAdvCategory = crestAdvCategory;
        this.crestPostNumber = crestPostNumber;
        this.available = available;
        this.newRowFlag = newRowFlag;
        this.obsInd = obsInd;
    }

    public void setLegalAidOrderId(Integer legalAidOrderId) {
        this.legalAidOrderId = legalAidOrderId;
    }
    public Integer getLegalAidOrderId() {
        return legalAidOrderId;
    }
    
    public void setDefendantOnCaseId(Integer defendantOnCaseId) {
        this.defendantOnCaseId = defendantOnCaseId;
    }
    public Integer getDefendantOnCaseId() {
        return defendantOnCaseId;
    }
    
    public void setRefAdvocateId(Integer refAdvocateId) {
        this.refAdvocateId = refAdvocateId;
    }
    public Integer getRefAdvocateId() {
        return refAdvocateId;
    }
    
    public void setCrestAdvCategory(String crestAdvCategory) {
        this.crestAdvCategory = crestAdvCategory;
    }
    public String getCrestAdvCategory(){
        return crestAdvCategory;
    }
    
    public void setCrestPostNumber(Integer crestPostNumber) {
        this.crestPostNumber = crestPostNumber;
    }
    public Integer getCrestPostNumber() {
        return crestPostNumber;
    }
    
    public void setAvailable(String available) {
        this.available = available;
    }
    public String getAvailable() {
        return available;
    }
    
    public void setNewRowFlag(String newRowFlag) {
        this.newRowFlag = newRowFlag;
    }
    public String getNewRowFlag() {
        return newRowFlag;
    }
    
    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }
    public String getObsInd() {
        return obsInd;
    }
}

