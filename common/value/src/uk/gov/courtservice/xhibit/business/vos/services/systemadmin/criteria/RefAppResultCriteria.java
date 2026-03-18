package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class RefAppResultCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -6771886354708062740L;
	private static final String TABLE_NAME = "RefAppResult";

    protected interface AttributeNames {
        public static final String COURT_ID = "court.courtId";

        public static final String APP_RESULT_CODE = "appResultCode";

        public static final String HO_CODE = "hoCode";

        public static final String VARY_SENTENCE = "varySentence";

        public static final String LESSER_OFF_IND = "lesserOffInd";
    }

    public RefAppResultCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getAppResultCode() {
        return this.getAttribute(AttributeNames.APP_RESULT_CODE);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getHoCode() {
        return this.getAttribute(AttributeNames.HO_CODE);
    }

    public String getVarySentence() {
        return this.getAttribute(AttributeNames.VARY_SENTENCE);
    }

    public String getLesserOffInd() {
        return this.getAttribute(AttributeNames.LESSER_OFF_IND);
    }

    public void setAppResultCode(String newValue) {
        this.setAttribute(AttributeNames.APP_RESULT_CODE, newValue);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setHoCode(String newValue) {
        this.setAttribute(AttributeNames.HO_CODE, newValue);
    }

    public void setVarySentence(String newValue) {
        this.setAttribute(AttributeNames.VARY_SENTENCE, newValue);
    }

    public void setLesserOffInd(String newValue) {
        this.setAttribute(AttributeNames.LESSER_OFF_IND, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref app result criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getAppResultCode(), getHoCode(), getVarySentence(), getLesserOffInd() };
    }
}