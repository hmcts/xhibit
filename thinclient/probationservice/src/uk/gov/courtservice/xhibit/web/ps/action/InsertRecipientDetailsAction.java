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
 * Title: InsertRecipientDetailsAction
 * </p>
 * <p>
 * Description: The the action to insert recipient details.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003)
 * @version $Id: InsertRecipientDetailsAction.java,v 1.12 2006/04/26 09:01:52
 *          bzjrnl Exp $
 */
public class InsertRecipientDetailsAction extends AbstractAction {

    /**
     * Empty default constructor
     */
    public InsertRecipientDetailsAction() {
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
                PSRRecipientValueSet recipientValue = ActionUtil.getModifiedRecipientValue(null,
                        psrRecipientDetailsBean);
                delegate.insertRecipientValues(recipientValue);
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
                actionEnvironment.setResponseName("insertrecipientdetailscomplete");
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
                actionEnvironment.setResponseName("insertrecipientdetailsinvalid");
            }
        }
    }
}
