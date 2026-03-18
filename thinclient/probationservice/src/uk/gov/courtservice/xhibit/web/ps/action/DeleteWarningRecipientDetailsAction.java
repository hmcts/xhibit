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
 * Title: DeleteWarningRecipientDetailsAction
 * </p>
 * <p>
 * Description: The the action to warn before deleting recipient details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.12 $ $Log:
 *         DeleteWarningRecipientDetailsAction.java,v $ Revision 1.10 2006/04/26
 *         09:01:51 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.9 2003/12/10 14:35:05 xzmw8n Implemented check token functionality
 * 
 * Revision 1.8 2003/08/11 08:22:11 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.7 2003/03/19 21:14:55 fz0n8j Modified for new mappings.
 * 
 * Revision 1.6 2003/03/18 13:25:08 fz0n8j Moved action utils
 * 
 * Revision 1.5 2003/03/17 11:32:18 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.4 2003/03/14 20:55:13 fz0n8j Uses delegate code. ecawley
 * 
 * Revision 1.3 2003/03/14 11:17:47 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.2 2003/03/11 16:07:51 fz0n8j Deals with empty recults more
 * correctly - ecawley
 * 
 * Revision 1.1 2003/03/10 18:19:55 fz0n8j Added to cvs
 * 
 * 
 */
public class DeleteWarningRecipientDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public DeleteWarningRecipientDetailsAction() {
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
            actionEnvironment.setResponseName("deletewarningrecipientdetailscomplete");
        }
    }

}
