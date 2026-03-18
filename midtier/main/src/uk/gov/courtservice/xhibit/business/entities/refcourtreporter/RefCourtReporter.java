package uk.gov.courtservice.xhibit.business.entities.refcourtreporter;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirm;

public interface RefCourtReporter extends CSEntityLocal {

    public Integer getCourtId();

    public Integer getCrestCourtReporterId();

    public String getFirstName();

    public String getInitials();

    public String getMiddleName();

    public String getObsInd();

    public Integer getRefCourtReporterFirmId();

    public Integer getRefCourtReporterId(); // PK

    public String getReportMethod();

    public String getSurname();

    public void setCourtId(Integer courtId);

    public void setCrestCourtReporterId(Integer crestCourtReporterId);

    public void setFirstName(String firstName);

    public void setInitials(String initials);

    public void setMiddleName(String middleName);

    public void setObsInd(String obsInd);

    public void setRefCourtReporterFirmId(Integer refCourtReporterFirmId);

    public void setReportMethod(String reportMethod);

    public void setSurname(String surname);

    public RefCourtReporterFirm getRefCourtReporterFirm();

    public void setRefCourtReporterFirm(RefCourtReporterFirm refCourtReporterFirm);

}