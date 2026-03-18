package uk.gov.courtservice.xhibit.business.entities.refoffence;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefOffenceBean extends CSEntityBean {

    public Integer ejbCreate(String actSection, String dvlcCode, String hoClass, String hoProcType, String hoSubclass,
            String obsInd, String offenceClass, String offenceCode, String offenceDesc, String offenceDesc2,
            String offenceGroup, String statute, Integer courtId, String userDisplayName) throws CreateException {

        setActSection(actSection);
        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setDvlcCode(dvlcCode);
        setHoClass(hoClass);
        setHoProcType(hoProcType);
        setHoSubclass(hoSubclass);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setOffenceClass(offenceClass);
        setOffenceCode(offenceCode);
        setOffenceDesc(offenceDesc);
        setOffenceDesc2(offenceDesc2);
        setOffenceGroup(offenceGroup);
        setStatute(statute);
        return null;
    }

    public void ejbPostCreate(String actSection, String dvlcCode, String hoClass, String hoProcType, String hoSubclass,
            String obsInd, String offenceClass, String offenceCode, String offenceDesc, String offenceDesc2,
            String offenceGroup, String statute, Integer courtId, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------
    public abstract Integer getRefOffenceId();

    public abstract String getActSection();

    public abstract String getDvlcCode();

    public abstract String getHoClass();

    public abstract String getHoProcType();

    public abstract String getHoSubclass();

    public abstract String getObsInd();

    public abstract String getOffenceClass();

    public abstract String getOffenceCode();

    public abstract String getOffenceDesc();

    public abstract String getOffenceDesc2();

    public abstract String getOffenceGroup();

    public abstract String getStatute();

    public abstract void setActSection(String actSection);

    public abstract void setDvlcCode(String dvlcCode);

    public abstract void setHoClass(String hoClass);

    public abstract void setHoProcType(String hoProcType);

    public abstract void setHoSubclass(String hoSubclass);

    public abstract void setObsInd(String obsInd);

    public abstract void setOffenceClass(String offenceClass);

    public abstract void setOffenceCode(String offenceCode);

    public abstract void setOffenceDesc(String offenceDesc);

    public abstract void setOffenceDesc2(String offenceDesc2);

    public abstract void setOffenceGroup(String offenceGroup);

    public abstract void setRefOffenceId(Integer refOffenceId);

    public abstract void setStatute(String statute);

    public abstract Integer getCourtId();

    public abstract void setCourtId(Integer courtId);

}