package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Offence search criteria.
 * <p>
 * System Reference Data Type.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 161 07-04 -2003 AW Daley Offence Type replaced with 52135 Offence
 * Description.
 */
public class RefOffenceCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = 4447308317876880626L;
    private static final String TABLE_NAME = "RefOffence";

    protected interface AttributeNames {
        public static final String ACT_SECTION = "actSection";

        public static final String COURT_ID = "courtId";

        /**
         * @deprecated Removed by Issue 161
         */
        public static final String OFFENCE_TYPE = "offenceType";

        public static final String OFFENCE_DESC = "offenceDesc";

        public static final String STATUTE = "statute";

        public static final String OFFENCE_CODE = "offenceCode";

        public static final String ATTRIBUTE_NAME_OBSIND = "obsInd"; // Is on
        // super
        // class
        // but
        // required
        // here
        // otherwise
        // failure
        // on
        // xhibitSearchCriteria
        
        public static final String BAIL_ACT = "bailAct";
    }

    public RefOffenceCriteria() {
        // Empty
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getActSection() {
        return this.getAttribute(AttributeNames.ACT_SECTION);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    /**
     * @deprecated Removed by Issue 161
     */
    public String getOffenceType() {
        return this.getAttribute(AttributeNames.OFFENCE_TYPE);
    }

    public String getOffenceDescription() {
        return this.getAttribute(AttributeNames.OFFENCE_DESC);
    }

    public String getStatute() {
        return this.getAttribute(AttributeNames.STATUTE);
    }

    public String getOffenceCode() {
        return this.getAttribute(AttributeNames.OFFENCE_CODE);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    public String getBailAct() {
        return this.getAttribute(AttributeNames.BAIL_ACT);
    }
    
    public void setActSection(String newValue) {
        this.setAttribute(AttributeNames.ACT_SECTION, newValue);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    /**
     * @deprecated Removed by Issue 161
     */
    public void setOffenceType(String newValue) {
        this.setAttribute(AttributeNames.OFFENCE_TYPE, newValue);
    }

    public void setOffenceDescription(String newValue) {
        this.setAttribute(AttributeNames.OFFENCE_DESC, newValue);
    }

    public void setStatute(String newValue) {
        this.setAttribute(AttributeNames.STATUTE, newValue);
    }

    public void setOffenceCode(String newValue) {
        this.setAttribute(AttributeNames.OFFENCE_CODE, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public void setBailAct(String newValue) {
        this.setAttribute(AttributeNames.BAIL_ACT, newValue);
    }

    /**
     * Returns the ordered list of arguments for ref offence criteria.
     * 
     * @return Object[]
     */
    public Object[] getArgs() {
        return new Object[] { getActSection(), getCourtId(), getOffenceDescription(), getStatute(), getOffenceCode(),
                getObsInd(), getBailAct(), };
    }

    /**
     * Returns the ordered list of arguments for ref offence criteria which
     * requires GUI validation.
     * 
     * @return Object[]
     */
    public Object[] getIncludeArgs() {
        return new Object[] { getActSection(), getOffenceDescription(), getStatute(), getOffenceCode(), };
    }
}