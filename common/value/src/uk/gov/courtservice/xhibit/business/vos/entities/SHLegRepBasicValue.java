package uk.gov.courtservice.xhibit.business.vos.entities;

//FRAMEWORK
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: SHLegRepBasicValue
 * </p>
 * <p>
 * Description: A Value Object whose attributes map one to one with the SHLegRep
 * enitity CMP fields.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class SHLegRepBasicValue extends CSAbstractValue {
	private static final long serialVersionUID = -7464217672419227851L;
	private String legalRole;

    private String isSignIn;

    private String solFirmOrRefLegalRep;

    private Integer crestSequenceNo;

    private Integer schedHearDefID;

    private Integer refLegalRepID;
    
    private Integer substitutedRefLegalRepID;
    
    private String subInst;

    private Integer ccInfoID;

    private Integer refDefenceCategoryID;

    private Integer refSolicitorFirmID;

    private Integer scheduledHearingID;

    public SHLegRepBasicValue() {
        super();
    }

    public SHLegRepBasicValue(Integer version) {
        super(version);
    }

    public SHLegRepBasicValue(Integer shLegRepID, Integer version) {
        super(shLegRepID, version);
    }

    public String getLegalRole() {
        return legalRole;
    }

    public void setLegalRole(String legalRole) {
        this.legalRole = legalRole;
    }

    public void setIsSignIn(String isSignIn) {
        this.isSignIn = isSignIn;
    }

    public String getIsSignIn() {
        return isSignIn;
    }

    public void setSolFirmOrRefLegalRep(String solFirmOrRefLegalRep) {
        this.solFirmOrRefLegalRep = solFirmOrRefLegalRep;
    }

    public String getSolFirmOrRefLegalRep() {
        return solFirmOrRefLegalRep;
    }

    public void setCrestSequenceNo(Integer crestSequenceNo) {
        this.crestSequenceNo = crestSequenceNo;
    }

    public Integer getCrestSequenceNo() {
        return crestSequenceNo;
    }

    public void setSchedHearDefID(Integer schedHearDefID) {
        this.schedHearDefID = schedHearDefID;
    }

    public Integer getSchedHearDefID() {
        return schedHearDefID;
    }

    public void setRefLegalRepID(Integer refLegalRepID) {
        this.refLegalRepID = refLegalRepID;
    }

    public Integer getRefLegalRepID() {
        return refLegalRepID;
    }

    public void setSubstitutedRefLegalRepID(Integer substitutedRefLegalRepID) {
        this.substitutedRefLegalRepID = substitutedRefLegalRepID;
    }
    
    public Integer getSubstitutedRefLegalRepID() {
        return this.substitutedRefLegalRepID;
    }
    
    public void setSubInst(String subInst) {
        this.subInst = subInst;
    }
    
    public String getSubInst() {
        return this.subInst;
    }
    
    public void setCcInfoID(Integer ccInfoID) {
        this.ccInfoID = ccInfoID;
    }

    public Integer getCcInfoID() {
        return ccInfoID;
    }

    public void setRefDefenceCategoryID(Integer refDefenceCategoryID) {
        this.refDefenceCategoryID = refDefenceCategoryID;
    }

    public Integer getRefDefenceCategoryID() {
        return refDefenceCategoryID;
    }

    public void setRefSolicitorFirmID(Integer refSolicitorFirmID) {
        this.refSolicitorFirmID = refSolicitorFirmID;
    }

    public Integer getRefSolicitorFirmID() {
        return refSolicitorFirmID;
    }

    public void setScheduledHearingID(Integer scheduledHearingID) {
        this.scheduledHearingID = scheduledHearingID;
    }

    public Integer getScheduledHearingID() {
        return scheduledHearingID;
    }
}