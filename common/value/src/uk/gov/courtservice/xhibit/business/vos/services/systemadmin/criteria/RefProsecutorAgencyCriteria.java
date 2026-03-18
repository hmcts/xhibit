package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Prosecutor Agency search criteria.
 * <p>
 * System Reference Data Type.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Scott Atwell
 * @version 1.0
 */
public class RefProsecutorAgencyCriteria extends AbstractSearchCriteria implements Obsoletable {
    private static final String TABLE_NAME = "RefProsecutorAgency";

    protected interface AttributeNames {
    	public static final Integer REF_PROSECUTOR_AGENCY_ID = 0;
    	public static final String TITLE = "title";
        public static final String PROSECUTOR_NAME_1 = "prosecutorName1";
        public static final String PROSECUTOR_NAME_2 = "prosecutorName2";
        public static final String PROSECUTOR_NAME_3 = "prosecutorName3";
        public static final String INITIALS = "initials";
        public static final Integer ADDRESS_ID = 0;
        public static final String CREST_OPPOSER_ID = "crestOpposerId";
        public static final String COURT_ID = "courtId";
        public static final String CPS_CODE = "cpsCode";
        public static final String DX_REF = "dxRef";
    }

    public RefProsecutorAgencyCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getProsecutorName1() {
        return this.getAttribute(AttributeNames.PROSECUTOR_NAME_1);
    }
    public void setProsecutorName1(String newValue) {
        this.setAttribute(AttributeNames.PROSECUTOR_NAME_1, newValue);
    }

    public String getProsecutorName2() {
        return this.getAttribute(AttributeNames.PROSECUTOR_NAME_2);
    }
    public void setProsecutorName2(String newValue) {
        this.setAttribute(AttributeNames.PROSECUTOR_NAME_2, newValue);
    }

    public String getProsecutorName3() {
        return this.getAttribute(AttributeNames.PROSECUTOR_NAME_3);
    }
    public void setProsecutorName3(String newValue) {
        this.setAttribute(AttributeNames.PROSECUTOR_NAME_3, newValue);
    }

    public String getInitials() {
        return this.getAttribute(AttributeNames.INITIALS);
    }
    public void setInitials(String newValue) {
        this.setAttribute(AttributeNames.INITIALS, newValue);
    }

    /*public Integer getAddressId() {
        return this.getAttribute(AttributeNames.ADDRESS_ID);
    }
    public void setAddressId(Integer newValue) {
        this.setAttribute(AttributeNames.ADDRESS_ID, newValue);
    }*/

    public String getCRESTOpposerID() {
        return this.getAttribute(AttributeNames.CREST_OPPOSER_ID);
    }
    public void setCRESTOpposerID(String newValue) {
        this.setAttribute(AttributeNames.CREST_OPPOSER_ID, newValue);
    }

    public String getCourtID() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }
    public void setCourtID(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public String getCpsCodeID() {
        return this.getAttribute(AttributeNames.CPS_CODE);
    }
    public void setCpsCode(String newValue) {
        this.setAttribute(AttributeNames.CPS_CODE, newValue);
    }

    public String getDxRef() {
        return this.getAttribute(AttributeNames.DX_REF);
    }
    public void setDxRef(String newValue) {
        this.setAttribute(AttributeNames.DX_REF, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }
    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }


    /**
     * Returns the ordered list of arguments for ref judge criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getProsecutorName1(), getCRESTOpposerID(), getCourtID() };
    }
}
