package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_case.XhbDirectionsForCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsForCase;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

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
 * @version $Revision: 1.22 $
 */
public class CaseDirectionsDialog extends XDialog {
    private final XhibitApplicationController xac;

    private DirectionsForCase caseDirection;

    private TitledBorder caseBorder;

    private Border border1;

    public CaseDirectionsDialog(Frame frame) throws CSRecoverableException {
        super(frame, "", true);
        xac = (XhibitApplicationController) frame;

        DirectionsForCaseValue dirCaseVO = new DirectionsForCaseValue();
        dirCaseVO.setDirectionsForCaseBasicValue(new XhbDirectionsForCaseBasicValue());
        Integer shId = null;
        if (xac != null) {
            dirCaseVO.getDirectionsForCaseBasicValue().setCaseId(xac.getApplicationCaseModel().getCaseId());
            shId = xac.getApplicationCaseModel().getScheduledHearingId();
        }

        super.setTitle(XHIBITConstant.getResource(XhibitBundles.SimpleEvent, "titleBarLabelAdd"));
        caseDirection = new DirectionsForCase(dirCaseVO, shId);
        caseBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions, "DirectionsForCase"));
        border1 = BorderFactory.createCompoundBorder(caseBorder, BorderFactory.createEmptyBorder(4, 4, 4, 4));
        caseDirection.setBorder(border1);

        super.addBodyPanel(caseDirection);
        super.pack();
    }

    public void okClicked(ActionEvent ae) throws Exception {
        if (bodyPanel != null) {
            bodyPanel.stepValidate();
            bodyPanel.stepDeactivate();
            bodyPanel.stepDeinitialise(true);

            // This has been modified to use the Plea and Direction update
            // method so that as well as creating a court log event, it
            // updates the database.
            DirectionsForCase dfc = (DirectionsForCase) bodyPanel;
            if (dfc.getModified()) {
                DirectionsValue dv = new DirectionsValue();
                dv.setDirectionsForCaseValue(dfc.getModel());
                XhibitDelegateHelper.getDirectionsDelegate().saveDirections(dv);
            }
        }

        this.dispose();
    }
}
