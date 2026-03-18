package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: HRSHLegRepValue
 * </p>
 * <p>
 * Description: Value object to store the scheduled legal representative
 * information required on a CREST form 'A'. This is an updateable value object.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 */

public class HRSHLegRepValue extends CSAbstractValue {

    private Integer shLegRepID;

    private Integer refLegRepID;

    private Integer refDefenceCategoryID;

    private String refDefenceCategoryDesc;
    
    private String subInst;
    
    private Integer substitutedRefLegRepId;
    
    private static final long serialVersionUID = 2631833774760870346L;

    /**
     * Default constructor that takes in the unique value and the version.
     * 
     * @param shlrID
     * @param version
     */
    public HRSHLegRepValue(Integer shlrID, Integer version) {
        super(shlrID, version);
    }

    public HRSHLegRepValue() {
    }

    // shLegRepID
    public Integer getShLegRepID() {
        return this.shLegRepID;
    }

    public void setShLegRepID(Integer shLegRepID) {
        this.shLegRepID = shLegRepID;
    }

    // refLegRepID
    public Integer getRefLegRepID() {
        return refLegRepID;
    }

    public void setRefLegRepID(Integer refLegRepID) {
        this.refLegRepID = refLegRepID;
    }

    // refDefenceCategoryID
    public Integer getRefDefenceCategoryID() {
        return refDefenceCategoryID;
    }

    public void setRefDefenceCategoryID(Integer refDefenceCategoryID) {
        this.refDefenceCategoryID = refDefenceCategoryID;
    }

    // refDefenceCategoryDesc
    public String getRefDefenceCategoryDesc() {
        return refDefenceCategoryDesc;
    }

    public void setRefDefenceCategoryDesc(String refDefenceCategoryDesc) {
        this.refDefenceCategoryDesc = refDefenceCategoryDesc;
    }
    
    // subInst
    public String getSubInst() {
        return subInst;
    }
    
    public void setSubInst(String subInst) {
        this.subInst = subInst;
    }
    
    // substitutedRefLegRepId
    public Integer getSubstitutedRefLegRepId() {
        return substitutedRefLegRepId;
    }
    
    public void setSubstitutedRefLegRepId(Integer substitutedRefLegRepId) {
        this.substitutedRefLegRepId = substitutedRefLegRepId;
    }
}
