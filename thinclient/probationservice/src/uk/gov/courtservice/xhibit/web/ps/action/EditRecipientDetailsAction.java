package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRecipientDetailsBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: EditRecipientDetailsAction
 * </p>
 * <p>
 * Description: The the action for showing the recipient details to edit.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.15 $ $Log:
 *         EditRecipientDetailsAction.java,v $ Revision 1.13 2006/04/26 09:01:52
 *         bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.12 2003/12/10 14:35:05 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.11 2003/08/11 08:22:12 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.10 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.9 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.8 2003/03/17 11:32:18 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.7 2003/03/17 11:15:17 fz0n8j *** empty log message ***
 * 
 * Revision 1.6 2003/03/14 20:55:46 fz0n8j Uses new delegate code
 * 
 * Revision 1.5 2003/03/14 11:17:47 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.4 2003/03/11 16:31:45 fz0n8j Added CVS log comments - ecawley
 * 
 * 
 */
public class EditRecipientDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public EditRecipientDetailsAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void internalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("loggedout");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory.getInstance();
            List recipientValues = (List) actionEnvironment.getSessionParameter((String) actionEnvironment
                    .getRequestParameter("objectid"));
            PSRRecipientValueSet recipientValueFromSession = (PSRRecipientValueSet) recipientValues.get(Integer
                    .parseInt((String) actionEnvironment.getRequestParameter("id")));
            PSRRecipientValueSet recipientValue = delegate.findRecipientByPrimaryKey(recipientValueFromSession
                    .getRecipient().getPrimaryKey());
            PSRRecipientDetailsBean bean = ActionUtil.getRecipientDetails(recipientValue);
            actionEnvironment.setRequestParameter("psrRecipientDetailsBean", bean);
            String id = KeyFactory.getInstance().nextKey();
            actionEnvironment.setSessionParameter(id, recipientValue); // put
            // value
            // objects
            // into
            // session
            // . . .
            actionEnvironment.setRequestParameter("objectid", id);
            actionEnvironment.setResponseName("editrecipientdetailscomplete");
        }
    }

}
