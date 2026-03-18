package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * Advocate Refererence search criteria.
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

public class RefAdvocateCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -1404854419232323136L;
    private static final String TABLE_NAME = "RefAdvocate";

    protected interface AttributeNames {
        public static final String ADVOCATE_TYPE_IND = "advTypeInd";

        public static final String CREST_ID = "crestAdvocateId";

        public static final String COURT_ID = "court.courtId";

        public static final String CHAMBER_ADDRESS = "refChamber.address";

        public static final String CHAMBER_FIRM_NAME = "refChamber.firmName";

        public static final String INITIALS = "refLegalRepresentative.initials";

        public static final String FIRST_NAME = "refLegalRepresentative.firstName";

        public static final String MIDDLE_NAME = "refLegalRepresentative.middleName";

        public static final String SURNAME = "refLegalRepresentative.surname";

        public static final String LEGAL_REP_ID = "legalRepId";
        
        public static final String DEFENDANT_ID = "defendantId";
        
        public static final String CASE_ID = "court.caseId";
    }

    /**
     * Default constructor.
     */
    public RefAdvocateCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getAdvocateTypeIndicator() {
        return this.getAttribute(AttributeNames.ADVOCATE_TYPE_IND);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public String getCrestId() {
        return this.getAttribute(AttributeNames.CREST_ID);
    }

    public String getChamberAddress() {
        return this.getAttribute(AttributeNames.CHAMBER_ADDRESS);
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

    public String getLegalRepId() {
        return this.getAttribute(AttributeNames.LEGAL_REP_ID);
    }

    public String getChamberFirmName() {
        return this.getAttribute(AttributeNames.CHAMBER_FIRM_NAME);
    }
    
    public String getDefendantId() {
        return this.getAttribute(AttributeNames.DEFENDANT_ID);
    }

    public String getCaseId() {
        return this.getAttribute(AttributeNames.CASE_ID);
    }
    
    public void setAdvocateTypeIndicator(String newValue) {
        this.setAttribute(AttributeNames.ADVOCATE_TYPE_IND, newValue);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setCrestId(String newValue) {
        this.setAttribute(AttributeNames.CREST_ID, newValue);
    }

    public void setChamberAddress(String newValue) {
        this.setAttribute(AttributeNames.CHAMBER_ADDRESS, newValue);
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

    public void setLegalRepId(String newValue) {
        this.setAttribute(AttributeNames.LEGAL_REP_ID, newValue);
    }

    public void setChamberFirmName(String newValue) {
        this.setAttribute(AttributeNames.CHAMBER_FIRM_NAME, newValue);
    }

    public void setDefendantId(String newValue) {
        this.setAttribute(AttributeNames.DEFENDANT_ID, newValue);
    }

    public void setCaseId(String newValue) {
        this.setAttribute(AttributeNames.CASE_ID, newValue);
    }
    /*
     * // No longer required as all searches are performed by the // fast lane
     * readers. public String queryString() throws QueryUtilsException { String
     * query = super.queryString(); query += " orderBy o." +
     * AttributeNames.SURNAME; return query; }
     */

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref advocate.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getAdvocateTypeIndicator(), getInitials(), getFirstName(), getMiddleName(), getSurname(),
                getLegalRepId(), getChamberFirmName(), getCourtId(), getDefendantId(), getCaseId()
        // the other details defined in the top
        // interface are never actually used by
        // anything that calls this, and I cannot
        // find them on the database, so leaving
        // out for now
        };
    }
}