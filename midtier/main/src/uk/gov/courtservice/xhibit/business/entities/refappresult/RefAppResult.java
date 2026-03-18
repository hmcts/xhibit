package uk.gov.courtservice.xhibit.business.entities.refappresult;

import uk.gov.courtservice.xhibit.business.entities.court.Court;

public interface RefAppResult extends javax.ejb.EJBLocalObject {
    public void setAppResultCode(String appResultCode);

    public String getAppResultCode();

    public void setAppResultDescr1(String appResultDescr1);

    public String getAppResultDescr1();

    public void setAppResultDescr2(String appResultDescr2);

    public String getAppResultDescr2();

    public Integer getCourtId();

    public void setHoCode(Integer hoCode);

    public Integer getHoCode();

    public void setLesserOffInd(String lesserOffInd);

    public String getLesserOffInd();

    public void setObsInd(String obsInd);

    public String getObsInd();

    public void setRefAppResultId(Integer refAppResultId);

    public Integer getRefAppResultId();

    public void setVarySentence(String varySentence);

    public String getVarySentence();

    public Integer getVersion();

    public void setCourt(Court court);

    public Court getCourt();

    public void setUpdated(String userDisplayName);
}