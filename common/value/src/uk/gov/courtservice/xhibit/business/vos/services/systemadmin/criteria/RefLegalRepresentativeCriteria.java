package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Offence search criteria.
 * <p>
 * System Reference Data Type.
 * </p>
 * 
 * @todo Should this class be the super-class to Advocate & Solicitor criteria?
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * @author Jem Marsh
 * @version 1.1
 */
public class RefLegalRepresentativeCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = 8108852713705087863L;
	private static final String TABLE_NAME = "RefLegalRepresentative";

    protected interface AttributeNames {
        public static final String COURT_ID = "courtId";

        public static final String FIRST_NAME = "firstName";

        public static final String SURNAME = "surname";

        public static final String TYPE = "type";
    }

    /**
     * Default constructor.
     */
    public RefLegalRepresentativeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getFirstName() {
        return this.getAttribute(AttributeNames.FIRST_NAME);
    }

    public String getSurname() {
        return this.getAttribute(AttributeNames.SURNAME);
    }

    public String getType() {
        return this.getAttribute(AttributeNames.TYPE);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setFirstName(String newValue) {
        this.setAttribute(AttributeNames.FIRST_NAME, newValue);
    }

    public void setSurname(String newValue) {
        this.setAttribute(AttributeNames.SURNAME, newValue);
    }

    public void setType(String newValue) {
        this.setAttribute(AttributeNames.TYPE, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Return the ordered list of arguments for ref legal representative
     * criteria
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getFirstName(), getSurname(), getType() };
    }
}