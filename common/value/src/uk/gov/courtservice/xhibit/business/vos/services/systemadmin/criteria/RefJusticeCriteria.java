package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Justice search criteria.
 * <p>
 * Business Reference Data Type - hence 'Ref' prefix.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public class RefJusticeCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -4262490728061850012L;
    private static final String TABLE_NAME = "RefJustice";

    protected interface AttributeNames {
        public static final String JUSTICE_NAME = "justiceName";

        public static final String COURT_ID = "courtId";
    }

    public RefJusticeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getJusticeName() {
        return this.getAttribute(AttributeNames.JUSTICE_NAME);
    }

    public void setJusticeName(String newValue) {
        this.setAttribute(AttributeNames.JUSTICE_NAME, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    /**
     * Returns the ordered list of arguments for ref justice criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getJusticeName(), getCourtId() };
    }
}
