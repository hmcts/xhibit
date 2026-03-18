package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

/**
 * 'Hearing Type' search criteria.
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
public class RefHearingTypeCriteria extends AbstractSearchCriteria implements Obsoletable {
	private static final long serialVersionUID = 3867596807509049975L;
    private static final String TABLE_NAME = "RefHearingType";

    protected interface AttributeNames {
        public static final String HEARING_CODE = "hearingTypeCode";

        public static final String COURT_ID = "courtId";
    }

    public RefHearingTypeCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getHearingCode() {
        return this.getAttribute(AttributeNames.HEARING_CODE);
    }

    public void setHearingCode(String newValue) {
        this.setAttribute(AttributeNames.HEARING_CODE, newValue);
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
     * Returns the ordered list of arguments for ref hearing type criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getHearingCode(), getCourtId() };
    }
}