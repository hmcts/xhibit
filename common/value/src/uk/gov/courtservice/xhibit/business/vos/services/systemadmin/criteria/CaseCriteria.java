package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Search criteria for the Case
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class CaseCriteria extends AbstractSearchCriteria implements Obsoletable {

    private static final String TABLE_NAME = "Case";

    protected interface AttributeNames {
        public static final String CASE_NUMBER = "case.caseNumber";

    }

    /**
     * Default constructor.
     */
    public CaseCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCaseNumber() {
        return this.getAttribute(AttributeNames.CASE_NUMBER);
    }

    public void setCaseNumber(String newValue) {
        this.setAttribute(AttributeNames.CASE_NUMBER, newValue);
    }
     
    /**
     * Returns the ordered list of arguments for case criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCaseNumber() };
    }
}