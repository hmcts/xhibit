package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 */

public class RemoveAppealAdvocateAction extends XAction {
    public RemoveAppealAdvocateAction() {
        try {
            populateFromBundle("RemoveAppealAdvocateAction");
        } catch (Exception e) {
            CSConfigurationException CSce = new CSConfigurationException(
                    "Exception whilst populateFromBundle(RemoveAppealAdvocateAction); ", e);
            XHIBITConstant.handleError(CSce);
        }
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("RemoveAppealAdvocateAction: opening remove defence advocate dialog.");
        XHIBITConstant.debug("RemoveAppealAdvocateAction: openend and closed remove defence advocate dialog.");
    }
}