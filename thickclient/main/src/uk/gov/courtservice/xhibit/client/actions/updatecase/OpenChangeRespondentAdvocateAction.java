package uk.gov.courtservice.xhibit.client.actions.updatecase;

import java.awt.event.ActionEvent;

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

public class OpenChangeRespondentAdvocateAction extends XAction {
    public OpenChangeRespondentAdvocateAction() {
        populateFromBundle("OpenChangeRespondentAdvocateAction");
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("OpenChangeRespondentAdvocateAction.java: opening ChangeRespondentAdvocate dialog.");
        XHIBITConstant
                .debug("OpenChangeRespondentAdvocateAction.java: openend and closed ChangeRespondentAdvocate dialog.");
    }
}