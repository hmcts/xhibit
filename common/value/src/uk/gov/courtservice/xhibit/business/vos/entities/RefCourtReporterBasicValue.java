package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

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
public class RefCourtReporterBasicValue extends CSAbstractValue {

	private static final long serialVersionUID = -2690903251208876660L;
	private Integer courtFirmId = null;

    private Integer courtId = null;

    private String firstName = null;

    private String initials = null;

    private String middleName = null;

    private String obsInd = null;

    private Integer refCrestCourtReporterId = null;

    private String reportMethod = null;

    private String surname = null;

    /**
     * Default constructor.
     */
    public RefCourtReporterBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefCourtReporterBasicValue(Integer id, Integer version) {
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
     *            Integer
     * @param firstName
     *            String
     * @param middleName
     *            String
     * @param surname
     *            String
     * @param initials
     *            String
     * @param reportMethod
     *            String
     * @param obsInd
     *            String
     * @param courtFirmId
     *            Integer
     * @param courtId
     *            Integer
     */
    public RefCourtReporterBasicValue(Integer id, Integer version, Integer crestCourtReporterId, String firstName,
            String middleName, String surname, String initials, String reportMethod, String obsInd,
            Integer courtFirmId, Integer courtId) {

        this(id, version);
        this.firstName = firstName;
        this.middleName = middleName;
        this.surname = surname;
        this.refCrestCourtReporterId = crestCourtReporterId;
        this.initials = initials;
        this.reportMethod = reportMethod;
        this.obsInd = obsInd;
        this.courtFirmId = courtFirmId;
        this.courtId = courtId;
    }

    public Integer getCrestCourtReporterId() {
        return refCrestCourtReporterId;
    }

    public void setCrestCourtReporterId(Integer refCrestCourtReporterId) {
        this.refCrestCourtReporterId = refCrestCourtReporterId;
    }

    public Integer getRefCourtReporterFirmId() {
        return courtFirmId;
    }

    public void setRefCourtReporterFirmId(Integer courtFirmId) {
        this.courtFirmId = courtFirmId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newValue) {
        this.firstName = newValue;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String newValue) {
        this.middleName = newValue;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getReportMethod() {
        return reportMethod;
    }

    public void setReportMethod(String reportMethod) {
        this.reportMethod = reportMethod;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }
}
