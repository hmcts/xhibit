package uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria;

import java.io.Serializable;

/**
 * Search criteria for the [System] Reference data type, Court.
 * <p>
 * NB. There is also a *Business* Reference Type named RefCourt!
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
public class CourtCriteria extends AbstractUrnSearchCriteria implements Serializable, Obsoletable {
	private static final long serialVersionUID = 7607562703230480750L;
	private static final String TABLE_NAME = "Court";

    protected interface AttributeNames {
        public static final String CIRCUIT = "circuit";

        public static final String COURT_SITE_ID = "courtSite.courtSiteId";

        public static final String COURT_NAME = "courtName";

        public static final String COURT_PREFIX = "courtPrefix";

        public static final String COURT_TYPE = "courtType";

        public static final String CREST_ID = "crestCourtId";

        public static final String SHORT_NAME = "shortName";

        public static final String COURT_CODE = "courtCode";
    }

    /**
     * Default constructor.
     */
    public CourtCriteria() {
    }

    public String getTableName() {
        return TABLE_NAME;
    }

    public String getCircuit() {
        return this.getAttribute(AttributeNames.CIRCUIT);
    }

    public Integer getCourtSiteId() {
        String courtSiteId = getAttribute(AttributeNames.COURT_SITE_ID);
        if (courtSiteId == null)
            return null;
        else
            return Integer.valueOf(courtSiteId);
    }

    public String getCourtName() {
        return this.getAttribute(AttributeNames.COURT_NAME);
    }

    public String getCourtType() {
        return this.getAttribute(AttributeNames.COURT_TYPE);
    }

    public String getCrestCourtId() {
        return this.getAttribute(AttributeNames.CREST_ID);
    }

    public String getPrefix() {
        return this.getAttribute(AttributeNames.COURT_PREFIX);
    }

    public String getShortName() {
        return this.getAttribute(AttributeNames.SHORT_NAME);
    }

    public String getCourtCode() {
        return this.getAttribute(AttributeNames.COURT_CODE);
    }

    /**
     * Courts are only bothered with identifying the 'short name' from the URN.
     * <p>
     * For a URN of "//CHELMS/A/01", we extract 'CHELMS'.
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
            this.debug("URN has been set to null - therefore setting ShortName to null.");
            this.setShortName(null);
        } else {
            String shortName = extractCourtShortName();
            if (shortName != null)
                setShortName(shortName);
        }
    }

    public void setCircuit(String newValue) {
        this.setWildAttribute(AttributeNames.CIRCUIT, newValue);
    }

    public void setCourtName(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_NAME, newValue);
    }

    /**
     * Temporary overload.
     * 
     * @deprecated Please use String version.
     */
    public void setCourtSiteId(Integer newValue) {
        String stringValue = null;
        if (newValue != null) {
            stringValue = newValue.toString();
        }
        this.setCourtSiteId(stringValue);
    }

    public void setCourtSiteId(String newValue) {
        this.setAttribute(AttributeNames.CREST_ID, newValue);
    }

    public void setCourtType(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_TYPE, newValue);
    }

    public void setCrestCourtId(String newValue) {
        this.setAttribute(AttributeNames.COURT_SITE_ID, newValue);
    }

    public void setPrefix(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_PREFIX, newValue);
    }

    public void setShortName(String newValue) {
        this.setWildAttribute(AttributeNames.SHORT_NAME, newValue);
    }

    public void setCourtCode(String newValue) {
        this.setWildAttribute(AttributeNames.COURT_CODE, newValue);
    }

    public void setObsInd(String newValue) {
        this.setAttribute(ATTRIBUTE_NAME_OBSIND, newValue);
    }

    public String getObsInd() {
        return this.getAttribute(ATTRIBUTE_NAME_OBSIND);
    }

    /**
     * Returns the ordered list of arguments for court criteria.
     * 
     * @return
     */
    public Object[] getArgs() {
        return new Object[] { getCircuit(), getCourtSiteId(), getCourtName(), getPrefix(), getCourtType(),
                getCrestCourtId(), getShortName() };
    }
}