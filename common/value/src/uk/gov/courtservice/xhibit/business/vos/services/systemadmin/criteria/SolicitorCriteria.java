package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * 'Solicitor' search criteria [previously known as RefSolicitor].
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
public class SolicitorCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = 7626626174329508376L;
	private static final String TABLE_NAME = "Solicitor";

    protected interface AttributeNames {
        public static final String LEGAL_REP_ID = "refLegalRepId";

        public static final String INITIALS = "refLegalRepresentative.initials";

        public static final String FIRST_NAME = "refLegalRepresentative.firstName";

        public static final String MIDDLE_NAME = "refLegalRepresentative.middleName";

        public static final String SURNAME = "refLegalRepresentative.surname";

        public static final String CREST_SOLICITOR_NAME = "crestSolicitorName";

        public static final String SOLICITOR_FIRM_NAME = "refSolicitorFirm.solicitorFirmName";

        public static final String COURT_ID = "refLegalRepresentative.courtId";
    }

    public SolicitorCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getRefLegalRepId() {
        return this.getAttribute(AttributeNames.LEGAL_REP_ID);
    }

    public void setRefLegalRepId(String newValue) {
        this.setAttribute(AttributeNames.LEGAL_REP_ID, newValue);
    }

    public String getInitials() {
        return this.getAttribute(AttributeNames.INITIALS);
    }

    public void setInitials(String newValue) {
        this.setAttribute(AttributeNames.INITIALS, newValue);
    }

    public String getFirstName() {
        return this.getAttribute(AttributeNames.FIRST_NAME);
    }

    public void setFirstName(String newValue) {
        this.setAttribute(AttributeNames.FIRST_NAME, newValue);
    }

    public String getMiddleName() {
        return this.getAttribute(AttributeNames.MIDDLE_NAME);
    }

    public void setMiddleName(String newValue) {
        this.setAttribute(AttributeNames.MIDDLE_NAME, newValue);
    }

    public String getSurname() {
        return this.getAttribute(AttributeNames.SURNAME);
    }

    public void setSurname(String newValue) {
        this.setAttribute(AttributeNames.SURNAME, newValue);
    }

    public String getCrestSolicitorName() {
        return this.getAttribute(AttributeNames.CREST_SOLICITOR_NAME);
    }

    public void setCrestSolicitorName(String newValue) {
        this.setAttribute(AttributeNames.CREST_SOLICITOR_NAME, newValue);
    }

    public String getSolicitorFirmName() {
        return this.getAttribute(AttributeNames.SOLICITOR_FIRM_NAME);
    }

    public void setSolicitorFirmName(String newValue) {
        this.setAttribute(AttributeNames.SOLICITOR_FIRM_NAME, newValue);
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

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    /**
     * Returns the ordered list of arguments for solicitor criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getRefLegalRepId(), getInitials(), getFirstName(), getMiddleName(), getSurname(),
                this.getCrestSolicitorName(), this.getSolicitorFirmName(), getCourtId() };
    }
}
