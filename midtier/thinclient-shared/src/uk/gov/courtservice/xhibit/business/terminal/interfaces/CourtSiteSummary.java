package uk.gov.courtservice.xhibit.business.terminal.interfaces;

/**
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2004
 * 
 */
public interface CourtSiteSummary extends java.io.Serializable {
    /**
     * @return the court name.
     */
    public String getCourtName();

    /**
     * @return the court name.
     */
    public Integer getCourtId();

    /**
     * @return the crest court id.
     */
    public String getCrestCourtId();

    /**
     * @return the court site name
     */
    public String getCourtSiteName();

    /**
     * @return the court site id
     */
    public Integer getCourtSiteId();

}
