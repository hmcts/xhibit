package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSiteSummary;

/**
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP 2004
 * 
 * $Revision: 1.3 $
 */
public class CourtSiteSummaryImpl implements CourtSiteSummary {
    private final String courtName;

    private final Integer courtId;

    private final String crestCourtId;

    private final String courtSiteName;

    private final Integer courtSiteId;

    private static final long serialVersionUID = 7342425606147074651L;
    /**
     * Construct a new summary object from the data provided
     */
    public CourtSiteSummaryImpl(String courtName, Integer courtId, String crestCourtId, String courtSiteName,
            Integer courtSiteId) {
        this.courtName = courtName;
        this.courtId = courtId;
        this.crestCourtId = crestCourtId;
        this.courtSiteName = courtSiteName;
        this.courtSiteId = courtSiteId;
    }

    /**
     * CourtSiteSummary Implementation
     */
    public String getCourtName() {
        return courtName;
    }

    /**
     * CourtSiteSummary Implementation
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * CourtSiteSummary Implementation
     */
    public String getCrestCourtId() {
        return crestCourtId;
    }

    /**
     * CourtSiteSummary Implementation
     */
    public String getCourtSiteName() {
        return courtSiteName;
    }

    /**
     * CourtSiteSummary Implementation
     */
    public Integer getCourtSiteId() {
        return courtSiteId;
    }

}
