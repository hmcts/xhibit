package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listdistribution.MaintainListLetterRecipientsPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: MaintainListLetterRecipientsAction
 * </p>
 * <p>
 * Description: Maintain List Letter Recipients Action
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: MaintainListLetterRecipientsAction.java,v 1.2 2006/05/10
 *          08:01:41 bzjrnl Exp $
 */
public class MaintainListLetterRecipientsAction extends XAction {
    public MaintainListLetterRecipientsAction() {
        super("MaintainListLetterRecipients");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        xac.open(new MaintainListLetterRecipientsPanel(xac));
    }
}
