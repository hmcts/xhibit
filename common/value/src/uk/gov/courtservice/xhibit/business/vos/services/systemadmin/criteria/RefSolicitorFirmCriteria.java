package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * 'Solicitor Firm' search criteria.
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
public class RefSolicitorFirmCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = -2199702122630607066L;
    private static final String TABLE_NAME = "RefSolicitorFirm";

    protected interface AttributeNames {
        public static final String SOLICITOR_FIRM_NAME = "solicitorFirmName";

        public static final String CREST_SOLICITOR_FIRM_ID = "crestSofId";

        public static final String COURT_ID = "courtId";
    }

    public RefSolicitorFirmCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getSolicitorFirmName() {
        return this.getAttribute(AttributeNames.SOLICITOR_FIRM_NAME);
    }

    public void setSolicitorFirmName(String newValue) {
        this.setAttribute(AttributeNames.SOLICITOR_FIRM_NAME, newValue);
    }

    public String getCrestSolicitorFirmId() {
        return this.getAttribute(AttributeNames.CREST_SOLICITOR_FIRM_ID);
    }

    public void setCrestSolicitorFirmId(String newValue) {
        this.setAttribute(AttributeNames.CREST_SOLICITOR_FIRM_ID, newValue);
    }

    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for ref solictor firm criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getSolicitorFirmName(), getCrestSolicitorFirmId(), getCourtId() };
    }
}