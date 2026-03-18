package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class RemoveRespondentAdvocateAction extends XAction {

    public RemoveRespondentAdvocateAction() {
        try {
            populateFromBundle("RemoveRespondentAdvocateAction");
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