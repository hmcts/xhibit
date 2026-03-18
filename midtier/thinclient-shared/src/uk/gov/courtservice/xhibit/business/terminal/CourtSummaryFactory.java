package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.CourtSummary;
import uk.gov.courtservice.xhibit.business.terminal.services.TcsControllerBeanBusinessDelegate;

/**
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment 2003
 * 
 * $Revision: 1.5 $
 * 
 * $Log: CourtSummaryFactory.java,v $
 * Revision 1.5  2006/06/05 12:29:57  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:22:53 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.3 2003/08/15 09:01:29 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.2 2003/04/29 16:54:44 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.1.2.1 2003/04/29 15:01:50 fz0n8j Added classes used for selecting
 * terminals.
 * 
 * 
 */
public class CourtSummaryFactory {
    private static CourtSummaryFactory instance;

    private TcsControllerBeanBusinessDelegate delegate = TcsControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    public synchronized static CourtSummaryFactory getInstance() {
        if (instance == null) {
            instance = new CourtSummaryFactory();
        }
        return instance;
    }

    private CourtSummaryFactory() {
    }

    /**
     * @return all court summaries.
     */
    public CourtSummary[] getCourtSummaries() {
        return delegate.getAllCourts();
    }

}
