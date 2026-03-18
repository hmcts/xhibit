package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.io.Serializable;

/**
 * Court Room search criteria.
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
 * @version 1.8
 */
public class CourtRoomCriteria extends AbstractUrnSearchCriteria implements Serializable, Obsoletable {
	private static final long serialVersionUID = -8409608494926803117L;
    private static final String TABLE_NAME = "CourtRoom";

    protected interface AttributeNames {
        public static final String COURT_ROOM_NAME = "courtRoomName";

        public static final String COURT_SITE_CODE = "courtSite.courtSiteCode";

        public static final String COURT_SITE_ID = "courtSite.courtSiteId";

        public static final String CREST_COURT_ROOM_NO = "crestCourtRoomNo";

        public static final String LOCATION = "location";

        public static final String SHORT_NAME = "courtSite.court.shortName";
    }

    /**
     * Default constructor.
     */
    public CourtRoomCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCourtRoomName() {
        return this.getAttribute(AttributeNames.COURT_ROOM_NAME);
    }

    public String getCourtSiteCode() {
        return this.getAttribute(AttributeNames.COURT_SITE_CODE);
    }

    public String getCourtSiteId() {
        return this.getAttribute(AttributeNames.COURT_SITE_ID);
    }

    public String getCrestCourtRoomNo() {
        return this.getAttribute(AttributeNames.CREST_COURT_ROOM_NO);
    }

    public String getLocation() {
        return this.getAttribute(AttributeNames.LOCATION);
    }

    public String getShortName() {
        return this.getAttribute(AttributeNames.SHORT_NAME);
    }

    /**
     * CourtRooms are identified by ALL URN fields.
     */
    protected void parseUrn() {
        String newUrn = this.getUrn();
        if (newUrn == null) {
            /**
             * @todo Check this assumption: It might have been set to null, in
             *       which case do nothing - or set the relevant criteria to
             *       null? I've opted for the latter.
             */
            this
                    .debug("URN has been set to null - therefore setting Crest Court Room No, Court Site Code & Short Name to null.");
            this.setCrestCourtRoomNo(null);
            this.setCourtSiteCode(null);
            this.setShortName(null);
        } else {
            String shortName = extractCourtShortName();
            if (shortName != null)
                setShortName(shortName);
            String courtRoomNo = extractCourtRoomNo();
            if (courtRoomNo != null)
                setCrestCourtRoomNo(courtRoomNo);
            String courtSiteCode = extractCourtSiteCode();
            if (courtSiteCode != null)
                setCourtSiteCode(courtSiteCode);
        }
    }

    public void setCourtRoomName(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_ROOM_NAME, newValue);
    }

    public void setCourtSiteCode(String newValue) {
        this.setAttribute(AttributeNames.COURT_SITE_CODE, newValue);
    }

    public void setCourtSiteId(String newValue) {
        this.setAttribute(AttributeNames.COURT_SITE_ID, newValue);
    }

    public void setCrestCourtRoomNo(String newValue) {
        this.setWildAttribute(AttributeNames.CREST_COURT_ROOM_NO, newValue);
    }

    public void setLocation(String newValue) {
        this.setWildAttribute(AttributeNames.LOCATION, newValue);
    }

    public void setShortName(String newValue) {
        this.setWildAttribute(AttributeNames.SHORT_NAME, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for court room criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCourtRoomName(), getCourtSiteCode(), getCourtSiteId(), getCrestCourtRoomNo(),
                getShortName() };
    }
}