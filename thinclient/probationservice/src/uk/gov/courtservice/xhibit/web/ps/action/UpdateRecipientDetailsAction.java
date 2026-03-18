package uk.gov.courtservice.xhibit.web.ps.action;

import java.util.List;

import uk.gov.courtservice.xhibit.business.ps.services.PSControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.KeyFactory;
import uk.gov.courtservice.xhibit.web.ps.bean.AddressBean;
import uk.gov.courtservice.xhibit.web.ps.bean.PSRRecipientDetailsBean;
import uk.gov.courtservice.xhibit.web.ps.util.ActionUtil;

/**
 * <p>
 * Title: UpdateRecipientDetailsAction
 * </p>
 * <p>
 * Description: The the action to update recipient details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.13 $ $Log:
 *         UpdateRecipientDetailsAction.java,v $ Revision 1.11 2006/04/26
 *         09:01:53 bzjrnl Change: TI901 Comment: Weblogic Upgrade
 * 
 * Revision 1.10 2003/12/10 14:35:07 xzmw8n Implemented check token
 * functionality
 * 
 * Revision 1.9 2003/08/11 08:22:14 bzw8gp Jon Powell
 * 
 * organise imports (remove unused)
 * 
 * Revision 1.8 2003/03/21 19:48:51 fz0n8j Late changes
 * 
 * Revision 1.7 2003/03/19 21:14:56 fz0n8j Modified for new mappings.
 * 
 * Revision 1.6 2003/03/18 13:25:09 fz0n8j Moved action utils
 * 
 * Revision 1.5 2003/03/17 11:32:19 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.4 2003/03/14 20:54:41 fz0n8j Now commnicates with the database via
 * EJBs. ecawley
 * 
 * Revision 1.3 2003/03/14 11:17:48 fz0n8j Changed setParameter and getParameter
 * to setRequestParameter and getRequestParameter.
 * 
 * Revision 1.2 2003/03/10 18:28:39 fz0n8j Added code to go back to summary
 * after
 * 
 * Revision 1.1 2003/03/07 16:40:07 fz0n8j Added files to CVS
 * 
 */
public class UpdateRecipientDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public UpdateRecipientDetailsAction() {
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

            AddressBean addressBean = new AddressBean((String) actionEnvironment.getRequestParameter("line1"),
                    (String) actionEnvironment.getRequestParameter("line2"), (String) actionEnvironment
                            .getRequestParameter("line3"), (String) actionEnvironment.getRequestParameter("line4"),
                    (String) actionEnvironment.getRequestParameter("town"), (String) actionEnvironment
                            .getRequestParameter("county"), (String) actionEnvironment.getRequestParameter("postcode"),
                    (String) actionEnvironment.getRequestParameter("country"));
            PSRRecipientDetailsBean psrRecipientDetailsBean = new PSRRecipientDetailsBean(0, (String) actionEnvironment
                    .getRequestParameter("officeName"), addressBean, (String) actionEnvironment
                    .getRequestParameter("telephone"), (String) actionEnvironment.getRequestParameter("fax"),
                    (String) actionEnvironment.getRequestParameter("email"), (String) actionEnvironment
                            .getRequestParameter("method"));
            if (psrRecipientDetailsBean.isValid()) {
                PSControllerBeanBusinessDelegate delegate = PSControllerBeanBusinessDelegate.DelegateFactory
                        .getInstance();
                PSRRecipientValueSet recipientValue = (PSRRecipientValueSet) actionEnvironment
                        .getSessionParameter((String) actionEnvironment.getRequestParameter("objectid")); // get
                // the
                // value
                // objects
                // back
                // from
                // the
                // session
                recipientValue = ActionUtil.getModifiedRecipientValue(recipientValue, psrRecipientDetailsBean);
                delegate.updateRecipientValues(recipientValue);
                List recipientvalues = delegate.getAllRecipients(); // set
                // the
                // list
                // for
                // the
                // summary
                // jsp
                // which
                // we go
                // back
                // to
                String id = KeyFactory.getInstance().nextKey();
                actionEnvironment.setSessionParameter(id, recipientvalues);
                actionEnvironment.setRequestParameter("objectid", id);
                List beans = ActionUtil.getRecipientSummaries(recipientvalues);
                if (!beans.isEmpty()) {
                    actionEnvironment.setRequestParameter("recipientsummaries", beans);
                }
                actionEnvironment.setResponseName("updaterecipientdetailscomplete");
            } else {
                actionEnvironment.setRequestParameter("psrRecipientDetailsBean", psrRecipientDetailsBean); // set
                // the
                // bean
                // back
                // with
                // errors
                // . .
                // .
                actionEnvironment.setRequestParameter("objectid", (String) actionEnvironment
                        .getRequestParameter("objectid")); // set
                // the
                // object
                // id
                // back
                // into
                // the
                // html
                actionEnvironment.setResponseName("updaterecipientdetailsinvalid");
            }
        }
    }

}
