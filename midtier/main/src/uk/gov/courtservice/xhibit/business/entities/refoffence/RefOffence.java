package uk.gov.courtservice.xhibit.business.entities.refoffence;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;

public interface RefOffence extends CSEntityLocal {

    public Integer getRefOffenceId();

    public String getActSection();

    public Integer getCourtId();

    public String getDvlcCode();

    public String getHoClass();

    public String getHoProcType();

    public String getHoSubclass();

    public String getObsInd();

    public String getOffenceClass();

    public String getOffenceCode();

    public String getOffenceDesc();

    public String getOffenceDesc2();

    public String getOffenceGroup();

    // public String getOffenceType();
    public String getStatute();

    // public String getXhbVersion();

    public void setActSection(String actSection);

    public void setCourtId(Integer courtId);

    public void setDvlcCode(String dvlcCode);

    public void setHoClass(String hoClass);

    public void setHoProcType(String hoProcType);

    public void setHoSubclass(String hoSubclass);

    public void setObsInd(String obsInd);

    public void setOffenceClass(String offenceClass);

    public void setOffenceCode(String offenceCode);

    public void setOffenceDesc(String offenceDesc);

    public void setOffenceDesc2(String offenceDesc2);

    public void setOffenceGroup(String offenceGroup);

    // public void setOffenceType(String offenceType);
    public void setStatute(String statute);
    // public void setXhbVersion(String xhbVersion);

}