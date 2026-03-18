package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * A Business Reference search criteria - hence the Ref prefix.
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
public class RefCourtReporterCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = 7877356943329550756L;
    private static final String TABLE_NAME = "RefCourtReporter";

    protected interface AttributeNames {
        public static final String COURT_ID = "courtId";

        public static final String FIRM_NAME = "refCourtReporterFirm.firmName";

        public static final String INITIALS = "initials";

        public static final String FIRST_NAME = "firstName";

        public static final String MIDDLE_NAME = "middleName";

        public static final String SURNAME = "surname";
    }

    public RefCourtReporterCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getFirmName() {
        return this.getAttribute(AttributeNames.FIRM_NAME);
    }

    public String getInitials() {
        return this.getAttribute(AttributeNames.INITIALS);
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

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setFirmName(String newValue) {
        this.setAttribute(AttributeNames.FIRM_NAME, newValue);
    }

    public void setInitials(String newValue) {
        this.setAttribute(AttributeNames.INITIALS, newValue);
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

    /**
     * Returns the ordered list of arguments for ref court reporter criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtId(), getFirmName(), getInitials(), getFirstName(), getMiddleName(), getSurname() };
    }
}