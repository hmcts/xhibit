package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listdistribution.ViewDistributionStatusPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ViewDistributionStatusAction
 * </p>
 * <p>
 * Description: View Distribution Status
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Id: ViewDistributionStatusAction.java,v 1.2 2006/05/10 08:01:41
 *          bzjrnl Exp $
 */
public class ViewDistributionStatusAction extends XAction {
    public ViewDistributionStatusAction() {
        super("ViewDistributionStatus");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        xac.open(new ViewDistributionStatusPanel(xac));
    }
}
