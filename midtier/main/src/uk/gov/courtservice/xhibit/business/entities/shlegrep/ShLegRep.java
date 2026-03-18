package uk.gov.courtservice.xhibit.business.entities.shlegrep;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.ccinfo.CcInfo;
import uk.gov.courtservice.xhibit.business.entities.schedhearingdefendant.SchedHearingDefendant;
import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;

public interface ShLegRep extends CSEntityLocal {
    public Integer getShLegRepId();

    public void setCrestSequenceNo(Integer crestSequenceNo);

    public Integer getCrestSequenceNo();

    public void setLegalRole(String legalRole);

    public String getLegalRole();

    public void setIsSignedIn(String isSignedIn);

    public String getIsSignedIn();

    public void setSolFirmOrRefLegalRep(String solFirmOrRefLegalRep);

    public String getSolFirmOrRefLegalRep();

    public void setSchedHearDefId(Integer schedHearDefId);

    public Integer getSchedHearDefId();

    public void setRefLegalRepId(Integer refLegalRepId);

    public Integer getRefLegalRepId();

    public void setSubstitutedRefLegalRepId(Integer substitutedRefLegalRepId);
    
    public Integer getSubstitutedRefLegalRepId();
    
    public void setSubInst(String subInst);
    
    public String getSubInst();
    
    public abstract void setScheduledHearingId(Integer scheduledHearingId);

    public abstract Integer getScheduledHearingId();

    public void setCcInfoId(Integer ccInfoId);

    public Integer getCcInfoId();

    public void setRefDefenceCategoryId(Integer refDefenceCategoryId);

    public Integer getRefDefenceCategoryId();

    public void setRefSolicitorFirmId(Integer refSolicitorFirmId);

    public Integer getRefSolicitorFirmId();

    public CcInfo getCcInfo();

    public void setCcInfo(CcInfo ccInfo);

    public SchedHearingDefendant getSchedHearingDefendant();

    public void setSchedHearingDefendant(SchedHearingDefendant schedHearingDefendant);

    public void setScheduledHearing(ScheduledHearing scheduledHearing);

    public ScheduledHearing getScheduledHearing();
}