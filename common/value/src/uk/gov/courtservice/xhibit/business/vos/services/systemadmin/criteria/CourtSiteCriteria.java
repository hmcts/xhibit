package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.io.Serializable;

/**
 * Court Site search criteria.
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
public class CourtSiteCriteria extends AbstractUrnSearchCriteria implements Serializable, Obsoletable {
	private static final long serialVersionUID = 4824577299835410226L;
    private static final String TABLE_NAME = "CourtSite";

    protected interface AttributeNames {
        public static final String COURT_SITE_CODE = "courtSiteCode";

        public static final String COURT_ID = "court.courtId";

        public static final String COURT_SHORT_NAME = "court.shortName";

        public static final String COURT_SITE_NAME = "courtSiteName";
    }

    /**
     * Default constructor.
     */
    public CourtSiteCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    /**
     * The Primary Key of the related Court.
     */
    public String getCourtId() {
        return this.getAttribute(AttributeNames.COURT_ID);
    }

    /**
     * The Short Name of the related Court.
     */
    public String getCourtShortName() {
        return this.getAttribute(AttributeNames.COURT_SHORT_NAME);
    }

    public String getCourtSiteCode() {
        return this.getAttribute(AttributeNames.COURT_SITE_CODE);
    }

    public String getCourtSiteName() {
        return this.getAttribute(AttributeNames.COURT_SITE_NAME);
    }

    /**
     * CourtRooms are only bothered with identifying the 'site code' from the
     * URN.
     * <p>
     * For a URN of "//CHELMS/A/01", we extract Name = 'CHELMS' & Code = 'A'.
     * </P>
     */
    protected void parseUrn() {
        String newUrn = this.getUrn();
        if (newUrn == null) {
            /**
             * @todo Check this assumption: It might have been set to null, in
             *       which case do nothing - or set the relevant criteria to
             *       null? I've opted for the latter.
             */
            this.debug("URN has been set to null - therefore setting [CourtSite]Code to null.");
            this.setCourtShortName(null);
            this.setCourtSiteCode(null);
        } else {
            String shortName = extractCourtShortName();
            if (shortName != null)
                setCourtShortName(shortName);
            String courtSiteCode = extractCourtSiteCode();
            if (courtSiteCode != null)
                setCourtSiteCode(courtSiteCode);
        }
    }

    /**
     * Set the Primary Key of the related Court to be searched.
     */
    public void setCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_ID, newValue);
    }

    /**
     * The Short Name of the related Court.
     */
    public void setCourtShortName(String newValue) {
        this.setAttribute(AttributeNames.COURT_SHORT_NAME, newValue);
    }

    public void setCourtSiteCode(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_SITE_CODE, newValue);
    }

    public void setCourtSiteName(String newValue) {
        this.setAttribute(AttributeNames.COURT_SITE_NAME, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for contact site criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtSiteCode(), getCourtId(), getCourtShortName(), getCourtSiteName() };
    }
}