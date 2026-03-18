package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.Collections;
import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: ViewRecipientSummariesAction
 * </p>
 * <p>
 * Description: The the action for the summary for psrrecipients.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.16 $ $Log:
 *         ViewRecipientSummariesAction.java,v $ Revision 1.14 2006/04/26
 *         09:01:54 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.13 2003/12/10 14:35:07 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.12 2003/08/11 08:22:15 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.11 2003/03/26 16:54:51 fz0n8j Bug fixes.
 * 
 * Revision 1.10 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.9 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.8 2003/03/17 11:32:19 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.7 2003/03/14 20:54:41 fz0n8j Now commnicates with the database via
 * EJBs. ecawley
 * 
 * Revision 1.6 2003/03/14 12:48:55 fz0n8j Now gets data from the middle tier!
 * 
 * Revision 1.5 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.4 2003/03/11 16:12:15 fz0n8j Deals with empty results more
 * correctly, added CVS Log, ecawley
 * 
 */
public class ViewRecipientSummariesAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public ViewRecipientSummariesAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) {

        PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
        List recipientvalues = delegate.getAllRecipients();
        String id = KeyFactory.getInstance().nextKey();
        actionEnvironment.setSessionParameter(id, recipientvalues);
        actionEnvironment.setRequestParameter("objectid", id);
        List beans = ActionUtil.getRecipientSummaries(recipientvalues);
        Collections.sort(beans);
        if (!beans.isEmpty()) {
            actionEnvironment.setRequestParameter("recipientsummaries", beans);
        }
        actionEnvironment.setResponseName("viewrecipientsummariescomplete");
    }
}
