package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * 'System Code' search criteria.
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
public class RefSystemCodeCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -491763755695657462L;
	private static final String TABLE_NAME = "RefSystemCode";

    protected interface AttributeNames {
        public static final String COURT_ID = "court.courtId";

        public static final String CODE_TYPE = "codeType";

        public static final String DECODE = "decode";

        public static final String CODE = "code";

        public static final String CODE_TITLE = "codeTitle";

    }

    /**
     * @todo This is NOT an exhaustive list - I picked out the values that
     *       required for It.2 from column CODE_TYPE
     */
    public static interface CodeType {
        public static String DMI_ERROR_CODE = "DMI_ERROR_CODE";

        public static String HO_PROC_BREACH = "HO_PROC_BREACH";

        public static String PLEA = "PLEA";

        public static String VERDICT = "VERDICT";

        public static String ADVOCATE_TYPE = "ADVOCATE_TYPE";

        public static String DEFT_HRG_TYPE = "DEFT_HRG_TYPE";

        public static String PB_TYPE = "PB_TYPE";
        
        public static String TELE_APP_REFUSED = "TELE_APP_REFUSED";
        
        /**
         * Values for Defendant / appellant .
         */
        public static String ETHNIC_APPEARANCE = "ETHNIC_APPEARANCE";
        
        public static String ETHNIC_CLASSIFICATIN = "ETHNIC_CLASSIFICATIN";
        
        public static String HATE_CRIME = "HATE_TYPE";
        
        public static String PRISON_ID = "PRISON_ID";
        
        public static String BAIL_TYPE = "BAIL_TYPE";
        
        /**
         * Values for General tab.
         */
        public static String APPEAL = "CASE_APPEAL_TYPE";
        
        public static String MAG_COURT_HEARING_TYPE = "HO_PSD_HRG_TYPE";
        
        public static String POLICE_FORCE = "HO_POL_FORCE";
        
        public static String TICKET_TYPE = "JUDGE_TICKET";
        
        public static String RECEIPT_TYPE_T = "CASE_RECEIPT_TYPE_T" ;
        public static String RECEIPT_TYPE_S = "CASE_RECEIPT_TYPE_S" ;
        public static String APPEAL_TYPE = "CASE_APPEAL_TYPE" ;
        
        public static String CASE_TYPE = "CASE_TYPE";
        
        /**
         * Values for Listing.
         */
        public static String JUDGE_TYPE = "JUDGE_TYPE";
        
        public static String EXHIBIT_TIME_FORMAT = "EXHIBIT_TIME_FORMAT";
        
        /**
         * Values for Chamber and Advocate Details.
         */
        
        public static String LOCATION_CODE = "LOCATION_CODE";
        
        /**
         * Value for public representation
         */
        public static String REVOCATION_REASON="LA_INV_REASON";
        
        /**
         * Value for reason for vacating fixed hearing
         */
        public static String REASON_REMOVED = "REASON_REMOVED";
    }

    public RefSystemCodeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCodeType() {
        return this.getAttribute(AttributeNames.CODE_TYPE);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getDecode() {
        return this.getAttribute(AttributeNames.DECODE);
    }

    public String getCode() {
        return this.getAttribute(AttributeNames.CODE);
    }

    public String getCodeTitle() {
        return this.getAttribute(AttributeNames.CODE_TITLE);
    }

    public void setCodeType(String newValue) {
        this.setAttribute(AttributeNames.CODE_TYPE, newValue);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setDecode(String newValue) {
        this.setAttribute(AttributeNames.DECODE, newValue);
    }

    public void setCode(String newValue) {
        this.setAttribute(AttributeNames.CODE, newValue);
    }

    public void setCodeTitle(String newValue) {
        this.setAttribute(AttributeNames.CODE_TITLE, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref system code criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getCodeType(), getDecode(), getCode(), getCodeTitle() };
    }
}