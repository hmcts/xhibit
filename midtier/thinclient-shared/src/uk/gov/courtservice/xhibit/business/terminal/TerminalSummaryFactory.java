package uk.gov.courtservice.xhibit.business.terminal;

import uk.gov.courtservice.xhibit.business.terminal.interfaces.TerminalSummary;
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
 * $Log: TerminalSummaryFactory.java,v $
 * Revision 1.5  2006/06/05 12:29:57  bzjrnl
 * Change: TI901
 * Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * Revision 1.4 2006/05/31 14:22:53 bzjrnl
 * Change: TI901 Comment: Weblogic Upgrade - Standadise code formatting Revision
 * 1.3 2006/05/10 08:01:40 bzjrnl Change: TI901 Comment: Weblogic Upgrade -
 * Security Fix & Orders Clobs
 * 
 * Revision 1.2 2003/04/29 16:54:44 fz0n8j Merged devBranch-2b-030404. (WDF)
 * 
 * Revision 1.1.2.1 2003/04/29 15:01:51 fz0n8j Added classes used for selecting
 * terminals.
 * 
 * 
 */

public class TerminalSummaryFactory {

    private static TerminalSummaryFactory instance;

    private TcsControllerBeanBusinessDelegate delegate = TcsControllerBeanBusinessDelegate.DelegateFactory
            .getInstance();

    public synchronized static TerminalSummaryFactory getInstance() {
        if (instance == null) {
            instance = new TerminalSummaryFactory();
        }
        return instance;
    }

    private TerminalSummaryFactory() {
    }

    /**
     * @return all terminal summaries for a court id.
     */
    public TerminalSummary[] getTerminalSummaries(Integer courtId) {
        return delegate.getTerminalsForCourt(courtId);
    }

    /**
     * @return the terminal by id.
     */
    public TerminalSummary getTerminalById(Integer terminalId) {
        return delegate.getTerminalById(terminalId);
    }

}
