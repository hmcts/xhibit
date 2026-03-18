package uk.gov.courtservice.xhibit.business.vos.entities;

/**
 * RefJusticeComplexValue.
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
public class RefJusticeComplexValue extends RefJusticeBasicValue {

    static final long serialVersionUID = -7737120075678804035L;
	
	private CourtBasicValue court = null;

    /**
     * Default constructor.
     */
    public RefJusticeComplexValue() {
    }

    /**
     * Key constructor.
     * 
     * @param id
     *            Integer
     * @param version
     *            Integer
     */
    public RefJusticeComplexValue(Integer id, Integer version) {

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
     *            Integer
     * @param courtId
     *            Integer
     * @param justiceName
     *            String
     * @param obsInd
     *            String
     * @param initials
     *            String
     * @param psdCourtCode
     *            String
     * @param title
     *            String
     */
    public RefJusticeComplexValue(Integer id, Integer version, Integer crestJusticeId, Integer courtId,
            String justiceName, String obsInd, String initials, String psdCourtCode, String title) {

        super(id, version, crestJusticeId, courtId, justiceName, obsInd, initials, psdCourtCode, title);
    }

    public CourtBasicValue getCourt() {
        return this.court;
    }

    public void setCourt(CourtBasicValue newValue) {
        this.court = newValue;
    }

}