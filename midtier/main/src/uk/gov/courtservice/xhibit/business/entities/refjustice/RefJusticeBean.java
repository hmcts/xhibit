package uk.gov.courtservice.xhibit.business.entities.refjustice;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

public abstract class RefJusticeBean extends CSEntityBean {

    public Integer ejbCreate(String justiceName, Integer crestJusticeId, Integer courtId, String obsInd,
            String initials, String psdCourtCode, String title, String userDisplayName) throws CreateException {
        setCourtId(courtId);
        setCreatedBy(userDisplayName);
        setCrestJusticeId(crestJusticeId);
        setInitials(initials);
        setJusticeName(justiceName);
        setLastUpdatedBy(userDisplayName);
        setObsInd(obsInd);
        setPsdCourtCode(psdCourtCode);
        setTitle(title);
        return null;
    }

    public void ejbPostCreate(String justiceName, Integer crestJusticeId, Integer courtId, String obsInd,
            String initials, String psdCourtCode, String title, String userDisplayName) throws CreateException {
    }

    public abstract Integer getRefJusticeId(); // PK

    public abstract void setCourtId(Integer courtId);

    public abstract Integer getCourtId();

    public abstract Integer getCrestJusticeId();

    public abstract String getInitials();

    public abstract String getJusticeName();

    public abstract String getObsInd();

    public abstract String getPsdCourtCode();

    public abstract String getTitle();

    public abstract void setCrestJusticeId(Integer crestJusticeId);

    public abstract void setInitials(String initials);

    public abstract void setJusticeName(String justiceName);

    public abstract void setObsInd(String obsInd);

    public abstract void setPsdCourtCode(String psdCourtCode);

    public abstract void setRefJusticeId(Integer refJusticeId);

    public abstract void setTitle(String title);
    /*
     * public abstract Court getCourt(); public abstract void getCourt(Court
     * newValue);
     */
}