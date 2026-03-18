package uk.gov.courtservice.xhibit.client.actions.search;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: This action ensures after calling the xActionPerformed method on
 * OpenSearchJudgeAction, the caller stepUpdateViewState method is called to
 * refresh calling panel.
 * </p>
 * <p>
 * Description: The action extends OpenSearchJudgeAction and only performs an
 * additional call to the calling object
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */

public class OpenSearchJudgeUpdateParentAction extends OpenSearchJudgeAction {
    public void xActionPerformed(ActionEvent e) {
        try {
            super.xActionPerformed(e);
            if (getCaller() instanceof XPanel) {
                ((XPanel) getCaller()).stepUpdateViewState();
            }
        } catch (CSRecoverableException ex) {
            XHIBITConstant.handleError(ex);
        }
    }
}