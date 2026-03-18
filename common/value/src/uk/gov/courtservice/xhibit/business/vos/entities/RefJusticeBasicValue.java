package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * RefJusticeBasicValue.
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
public class RefJusticeBasicValue extends CSAbstractValue {

	static final long serialVersionUID = 8729456088832468358L;

	private Integer crestJusticeId = null;

    private Integer courtId = null;

    private String initials = null;

    private String justiceName = null;

    private String obsInd = null;

    private String psdCourtCode = null;

    private String title = null;

    /**
     * Default constructor.
     */
    public RefJusticeBasicValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefJusticeBasicValue(Integer id, Integer version) {

        super(id, version);
    }

    /**
     * Parameter constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     * @param crestJusticeId
     * @param courtId
     * @param sittingId
     * @param justiceName
     */
    public RefJusticeBasicValue(Integer id, Integer version, Integer crestJusticeId, Integer courtId,
            String justiceName, String obsInd, String initials, String psdCourtCode, String title) {

        this(id, version);
        this.crestJusticeId = crestJusticeId;
        this.courtId = courtId;
        this.justiceName = justiceName;
        this.obsInd = obsInd;
        this.initials = initials;
        this.psdCourtCode = psdCourtCode;
        this.title = title;
    }

    public Integer getCrestJusticeId() {
        return crestJusticeId;
    }

    public void setCrestJusticeId(Integer crestJusticeId) {
        this.crestJusticeId = crestJusticeId;
    }

    public Integer getCourtId() {
        return courtId;
    }

    public void setCourtID(Integer courtId) {
        this.courtId = courtId;
    }

    public String getJusticeName() {
        return justiceName;
    }

    public void setJusticeName(String justiceName) {
        this.justiceName = justiceName;
    }

    public void setObsInd(String obsInd) {
        this.obsInd = obsInd;
    }

    public String getObsInd() {
        return obsInd;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getInitials() {
        return initials;
    }

    public void setPsdCourtCode(String psdCourtCode) {
        this.psdCourtCode = psdCourtCode;
    }

    public String getPsdCourtCode() {
        return psdCourtCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}