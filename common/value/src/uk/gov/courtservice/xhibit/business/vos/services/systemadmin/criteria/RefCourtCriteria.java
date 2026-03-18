package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Search criteria for the [Business] Reference data type, RefCourt [NOT the
 * System type 'Court'].
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.2
 */
public class RefCourtCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -4226846879789798347L;
    private static final String TABLE_NAME = "RefCourt";

    protected interface AttributeNames {
        public static final String COURT_ID = "court.courtId";

        public static final String CIRCUIT = "circuit";

        public static final String COURT_NAME = "courtFullName";

        public static final String COURT_PREFIX = "courtPrefix";

        public static final String COURT_TYPE = "courtType";

        public static final String CREST_COURT_ID = "crestCourtId";

        public static final String SHORT_NAME = "courtShortName";

        public static final String IS_PSD = "isPsd";
    }

    /**
     * Default constructor.
     */
    public RefCourtCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * This is the ID of the System Reference data type Court - NOT the primary
     * key of RefCourt.
     * 
     * @return String
     */
    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getCircuit() {
        return this.getAttribute(AttributeNames.CIRCUIT);
    }

    public String getCourtName() {
        return this.getAttribute(AttributeNames.COURT_NAME);
    }

    public String getCourtPrefix() {
        return this.getAttribute(AttributeNames.COURT_PREFIX);
    }

    public String getCourtType() {
        return this.getAttribute(AttributeNames.COURT_TYPE);
    }

    public String getCrestCourtId() {
        return this.getAttribute(AttributeNames.CREST_COURT_ID);
    }

    public String getShortName() {
        return this.getAttribute(AttributeNames.SHORT_NAME);
    }

    public String getIsPsd() {
        return this.getAttribute(AttributeNames.IS_PSD);
    }

    /**
     * This is the ID of the System Reference data type Court - NOT the primary
     * key of RefCourt.
     * 
     * @param String
     */
    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setCircuit(String newValue) {
        this.setAttribute(AttributeNames.CIRCUIT, newValue);
    }

    public void setCourtName(String newValue) {
        this.setAttribute(AttributeNames.COURT_NAME, newValue);
    }

    public void setCourtPrefix(String newValue) {
        this.setAttribute(AttributeNames.COURT_PREFIX, newValue);
    }

    public void setCourtType(String newValue) {
        this.setAttribute(AttributeNames.COURT_TYPE, newValue);
    }

    public void setCrestCourtId(String newValue) {
        this.setAttribute(AttributeNames.CREST_COURT_ID, newValue);
    }

    public void setShortName(String newValue) {
        this.setAttribute(AttributeNames.SHORT_NAME, newValue);
    }

    public void setIsPsd(String newValue) {
        this.setAttribute(AttributeNames.IS_PSD, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref court criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getCircuit(), getCourtName(), getCourtPrefix(), getCourtType(),
                getCrestCourtId(), getShortName(), getIsPsd() };
    }
}