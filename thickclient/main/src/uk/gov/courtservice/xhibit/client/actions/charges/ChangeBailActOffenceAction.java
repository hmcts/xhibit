package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.event.ActionEvent;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChangeBailActOffenceDialog;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: ChangeBailActOffenceAction
 * </p>
 * <p>
 * Description: Action for changing Bail act offences on the Breach Charge
 * that is associated with the offence.
 * 
 * </p>
 * Company: Logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
public class ChangeBailActOffenceAction extends XAction  {

    private static final long serialVersionUID = 1L;

    public ChangeBailActOffenceAction() {
        populateFromBundle("ChangeBailActOffence");
    }
   
    public void xActionPerformed(ActionEvent e) throws CSRecoverableException, Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        ChangeBailActOffenceDialog dialog = new ChangeBailActOffenceDialog(xac);
        dialog.setVisible(true);
    }
}