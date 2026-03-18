package uk.gov.courtservice.xhibit.business.vos.entities;

//framework
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: RefCourtReporterValue
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Khanh Tran
 * @version 1.0
 */

public class RefCourtReporterValue extends CSAbstractValue {

	private static final long serialVersionUID = 7557676517080719974L;
	
	private Integer refCrestCourtReporterId;

    private String firstName;

    private String midleName;

    private String surname;

    private String initials;

    private String reportMethod;

    private String obsInd;

    private Integer courtFirmId;

    private Integer courtId;

    private Integer refCourtReporterId;

    /**
     * 
     */
    public RefCourtReporterValue() {
    }

    /**
     * 
     * @param refCourtReporterId
     * @param crestCourtReporterId
     * @param firstName
     * @param midleName
     * @param surname
     * @param initials
     * @param reportMethod
     * @param obsInd
     */
    public RefCourtReporterValue(Integer refCourtReporterId, Integer crestCourtReporterId, String firstName,
            String midleName, String surname, String initials, String reportMethod, String obsInd, Integer courtFirmId,
            Integer courtId) {
        this.refCourtReporterId = refCourtReporterId;
        this.firstName = firstName;
        this.midleName = midleName;
        this.surname = surname;
        this.refCrestCourtReporterId = crestCourtReporterId;
        this.initials = initials;
        this.reportMethod = reportMethod;
        this.obsInd = obsInd;
        this.courtFirmId = courtFirmId;
        this.courtId = courtId;
    }

    /**
     * 
     * @return
     */
    public Integer getCrestCourtReporterId() {
        return refCrestCourtReporterId;
    }

    /**
     * 
     * @param refCourtReporterId
     */
    public void setCrestCourtReporterId(Integer refCrestCourtReporterId) {
        this.refCrestCourtReporterId = refCrestCourtReporterId;
    }

    /**
     * 
     * @return
     */
    public Integer getRefCourtReporterFirmId() {
        return courtFirmId;
    }

    /**
     * 
     * @param refCourtReporterId
     */
    public void setRefCourtReporterFirmId(Integer courtFirmId) {
        this.courtFirmId = courtFirmId;
    }

    /**
     * 
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * 
     * @return
     */
    public String getMidleName() {
        return midleName;
    }

    /**
     * 
     * @param midleName
     */
    public void setMidleName(String midleName) {
        this.midleName = midleName;
    }

    /**
     * 
     * @return
     */
    public String getSurname() {
        return surname;
    }

    /**
     * 
     * @param surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * 
     * @return
     */
    public String getInitials() {
        return initials;
    }

    /**
     * 
     * @param initials
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     * 
     * @return
     */
    public String getReportMethod() {
        return reportMethod;
    }

    /**
     * 
     * @param reportMethod
     */
    public void setReportMethod(String reportMethod) {
        this.reportMethod = reportMethod;
    }

    /**
     * 
     * @return
     */
    public String getObsInd() {
        return obsInd;
    }

    /**
     * 
     * @param obsInd
     */
    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public void setCourtId(Integer courtId) {
        this.courtId = courtId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setRefCourtReporterId(Integer refCourtReporterId) {
        this.refCourtReporterId = refCourtReporterId;
    }

    public Integer getRefCourtReporterId() {
        return refCourtReporterId;
    }

}
