package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSiteSummary;
import uk.gov.courtservice.xhibit.business.terminal.services.TcsControllerBeanBusinessDelegate;

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
 * $Revision: 1.3 $
 */
public class CourtSiteSummaryFactory {
    private static CourtSiteSummaryFactory instance;

    private TcsControllerBeanBusinessDelegate delegate = TcsControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    public synchronized static CourtSiteSummaryFactory getInstance() {
        if (instance == null) {
            instance = new CourtSiteSummaryFactory();
        }
        return instance;
    }

    private CourtSiteSummaryFactory() {
    }

    /**
     * @return all court summaries.
     */
    public CourtSiteSummary[] getCourtSiteSummaries() {
        return delegate.getAllCourtSites();
    }

}
