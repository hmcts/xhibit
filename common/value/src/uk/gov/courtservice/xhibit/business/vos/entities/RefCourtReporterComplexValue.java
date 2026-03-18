package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefCourtReporterBasicValue
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 83,52539 28-04-2003 AW Daley Search mechanism requires fields and their
 * accessor methods to follow java bean spec. courtReporterFirm changes to
 * refCourtReporterFirm to comply with spec.
 * 
 */
public class RefCourtReporterComplexValue extends RefCourtReporterBasicValue {

	private static final long serialVersionUID = 9021775945856999636L;
	private RefCourtReporterFirmComplexValue refCourtReporterFirm = null;

    private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefCourtReporterComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefCourtReporterComplexValue(Integer id, Integer version) {
        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param crestCourtReporterId
     * @param firstName
     * @param midleName
     * @param surname
     * @param initials
     * @param reportMethod
     * @param obsInd
     */
    public RefCourtReporterComplexValue(Integer id, Integer version, Integer crestCourtReporterId, String firstName,
            String middleName, String surname, String initials, String reportMethod, String obsInd,
            Integer courtFirmId, Integer courtId) {
        super(id, version, crestCourtReporterId, firstName, middleName, surname, initials, reportMethod, obsInd,
                courtFirmId, courtId);
    }

    public CourtBasicValue getCourt() {
        return court;
    }

    public RefCourtReporterFirmComplexValue getRefCourtReporterFirm() {
        return refCourtReporterFirm;
    }

    // to be used so that the GUI can use reflection on this.
    public RefCourtReporterFirmComplexValue getCourtReporterFirm() {
        return refCourtReporterFirm;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }

    public void setRefCourtReporterFirm(RefCourtReporterFirmComplexValue newValue) {
        this.refCourtReporterFirm = newValue;
    }

    // to be used so that the GUI can use reflection on this.
    public void setCourtReporterFirm(RefCourtReporterFirmComplexValue newValue) {
        this.refCourtReporterFirm = newValue;
    }
}
