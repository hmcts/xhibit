package uk.gov.courtservice.xhibit.business.vos.services.hearingrecord;

/**
 * Value object to store the court reporter information required on a CREST form
 * 'A'. This is a read-only object.
 * 
 * From System CMR, refCourtReporter
 * 
 * <Change History/>
 * 
 * <P>
 * 02/06/03 - MH - Added Initials.
 * </P>
 * 
 * 
 */
public class HRCourtReporterValue implements HRValueObject {

    private static final long serialVersionUID = 1L;

    private String firstName;

    private String middleName;

    private String surname;

    private Integer refCourtReporterID;
    
    private Integer refCrestCourtReporterId;

    private String initials;

    public HRCourtReporterValue(Integer rcrID) {
        this.refCourtReporterID = rcrID;
    }

    public Integer getCrestCourtReporterId() {
        return refCrestCourtReporterId;
    }
    
    public void setCrestCourtReporterId(Integer refCrestCourtReporterId) {
        this.refCrestCourtReporterId = refCrestCourtReporterId;
    }
    
    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getSurname() {
        return surname;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Integer getRefCourtReporterID() {
        return refCourtReporterID;
    }

    public void setRefCourtReporterID(Integer id) {
        this.refCourtReporterID = id;
    }
}
