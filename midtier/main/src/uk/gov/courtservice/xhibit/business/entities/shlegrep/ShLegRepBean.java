package uk.gov.courtservice.xhibit.business.entities.shlegrep;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfo;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

abstract public class ShLegRepBean extends CSEntityBean {

    public Integer ejbCreate(
            Integer crestSequenceNo, 
            String legalRole, 
            String isSignedIn, 
            String solFirmOrRefLegalRep,
            Integer refLegalRepId, 
            Integer refDefenceCategoryId, 
            Integer refSolicitorFirmId,
            Integer substitutedRefLegalRepId, 
            String subInst, String userDisplayName) throws CreateException {
        setCrestSequenceNo(crestSequenceNo);
        setLegalRole(legalRole);
        setIsSignedIn(isSignedIn);
        setSolFirmOrRefLegalRep(solFirmOrRefLegalRep);
        setRefLegalRepId(refLegalRepId);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setRefDefenceCategoryId(refDefenceCategoryId);
        setRefSolicitorFirmId(refSolicitorFirmId);
        setSubstitutedRefLegalRepId(substitutedRefLegalRepId);
        setSubInst(subInst);

        return null;
    }

    public void ejbPostCreate(
            Integer crestSequenceNo, 
            String legalRole, 
            String isSignedIn,
            String solFirmOrRefLegalRep, 
            Integer refLegalRepId, 
            Integer refDefenceCategoryId, 
            Integer refSolicitorFirmId,
            Integer substitutedRefLegalRepId,
            String subInst, String userDisplayName)
            throws CreateException {
    }

    public abstract void setShLegRepId(Integer shLegRepId);

    public abstract void setCrestSequenceNo(Integer crestSequenceNo);

    public abstract void setLegalRole(String legalRole);

    public abstract void setIsSignedIn(String isSignedIn);

    public abstract void setSolFirmOrRefLegalRep(String solFirmOrRefLegalRep);

    public abstract void setSchedHearDefId(Integer schedHearDefId);

    public abstract void setRefLegalRepId(Integer refLegalRepId);
    
    public abstract void setSubstitutedRefLegalRepId(Integer substitutedRefLegalRepId);
    
    public abstract void setSubInst(String subInst);

    public abstract void setLastUpdatedBy(String lastUpdatedBy);

    public abstract void setCreatedBy(String createdBy);

    public abstract void setCcInfoId(Integer ccInfoId);

    public abstract void setRefDefenceCategoryId(Integer refDefenceCategoryId);

    public abstract void setRefSolicitorFirmId(Integer refSolicitorFirmId);

    public abstract void setScheduledHearingId(Integer scheduledHearingId);

    // ------------------------------------------------------------------------------------------------

    public abstract Integer getShLegRepId();

    public abstract Integer getCrestSequenceNo();

    public abstract String getLegalRole();

    public abstract String getIsSignedIn();

    public abstract String getSolFirmOrRefLegalRep();

    public abstract Integer getSchedHearDefId();

    public abstract Integer getRefLegalRepId();
    
    public abstract Integer getSubstitutedRefLegalRepId();
    
    public abstract String getSubInst();

    public abstract String getLastUpdatedBy();

    public abstract String getCreatedBy();

    public abstract Integer getCcInfoId();

    public abstract Integer getRefDefenceCategoryId();

    public abstract Integer getRefSolicitorFirmId();

    public abstract Integer getScheduledHearingId();

    // -----------------------------CMR-------------------------------------------------------------------
    public abstract void setCcInfo(CcInfo ccInfo);

    public abstract void setSchedHearingDefendant(SchedHearingDefendant schedHearingDefendant);

    public abstract void setScheduledHearing(ScheduledHearing scheduledHearing);

    public abstract CcInfo getCcInfo();

    public abstract SchedHearingDefendant getSchedHearingDefendant();

    public abstract ScheduledHearing getScheduledHearing();
}