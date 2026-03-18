package uk.gov.courtservice.xhibit.business.entities.refcourtreporter;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefCourtReporterBean extends CSEntityBean {

    public Integer ejbCreate(String first, String middle, String surname, String initials, String reportMethod,
            String obsInd, Integer courtId, Integer reporterFirmId, Integer crestId, String userDisplayName) throws CreateException {
        setCourtId(courtId);
        setCrestCourtReporterId(crestId);
        setFirstName(first);
        setInitials(initials);
        setMiddleName(middle);
        setObsInd(obsInd);
        setRefCourtReporterFirmId(reporterFirmId);
        setReportMethod(reportMethod);
        setSurname(surname);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        return null;
    }

    public void ejbPostCreate(String first, String middle, String surname, String initials, String reportMethod,
            String obsInd, Integer courtId, Integer reporterFirmId, Integer crestId, String userDisplayName) throws CreateException {
    }

    // ------------------------------CMP
    // Fields------------------------------------

    public abstract Integer getCourtId();

    public abstract Integer getCrestCourtReporterId();

    public abstract String getFirstName();

    public abstract String getInitials();

    public abstract String getMiddleName();

    public abstract String getObsInd();

    public abstract Integer getRefCourtReporterFirmId();

    public abstract Integer getRefCourtReporterId(); // PK

    public abstract String getReportMethod();

    public abstract String getSurname();

    public abstract void setCourtId(Integer courtId);

    public abstract void setCrestCourtReporterId(Integer crestCourtReporterId);

    public abstract void setFirstName(String firstName);

    public abstract void setInitials(String initials);

    public abstract void setMiddleName(String middleName);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefCourtReporterFirmId(Integer refCourtReporterFirmId);

    public abstract void setRefCourtReporterId(Integer refCourtReporterId);

    public abstract void setReportMethod(String reportMethod);

    public abstract void setSurname(String surname);

    // cmr
    public abstract uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirm getRefCourtReporterFirm();

    public abstract void setRefCourtReporterFirm(
            uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirm refCourtReporterFirm);
}