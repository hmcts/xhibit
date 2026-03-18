package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Judge search criteria.
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
 * @version 1.1
 */
public class RefJudgeCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -124437660623434790L;
	private static final String TABLE_NAME = "RefJudge";

    protected interface AttributeNames {
        public static final String FIRST_NAME = "firstName";

        public static final String MIDDLE_NAME = "middleName";

        public static final String SURNAME = "surname";

        public static final String COURT_ID = "courtId";
    }

    public RefJudgeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getFirstName() {
        return this.getAttribute(AttributeNames.FIRST_NAME);
    }

    public String getMiddleName() {
        return this.getAttribute(AttributeNames.MIDDLE_NAME);
    }

    public String getSurname() {
        return this.getAttribute(AttributeNames.SURNAME);
    }

    public void setFirstName(String newValue) {
        this.setAttribute(AttributeNames.FIRST_NAME, newValue);
    }

    public void setMiddleName(String newValue) {
        this.setAttribute(AttributeNames.MIDDLE_NAME, newValue);
    }

    public void setSurname(String newValue) {
        this.setAttribute(AttributeNames.SURNAME, newValue);
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
     * Returns the ordered list of arguments for ref judge criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getFirstName(), getMiddleName(), getSurname(), getCourtId() };
    }
}
