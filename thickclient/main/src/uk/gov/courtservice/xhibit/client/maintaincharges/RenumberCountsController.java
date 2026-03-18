package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XWizardDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: XHIBIT 2 - RenumberCountsController
 * </p>
 * <p>
 * Description: Renumber Counts Controller
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Krishna Pokala
 * @version 1.0
 */

public class RenumberCountsController extends XWizardDialog {
    private static final long serialVersionUID = 1;

    /**
     * The model used to store the data.
     */
    private ChargesControllerModel model = null;

    /**
     * Reference to the joinder charge value.
     */
    private ChargeValue chargeValue = null;

    /**
     * Reference to the joidner case basic value.
     */
    private XhbCaseBasicValue caseValue = null;

    private static final Logger log = CSServices.getLogger(RenumberCountsController.class);

    /**
     * Default Constructor.
     */
    public RenumberCountsController(XhbCaseBasicValue caseValue, ChargesControllerModel model, String title) {
        super(model.getACM().getXhibitApplicationController(), title, true);

        this.chargeValue = model.getChargeValue();
        this.caseValue = caseValue;
        this.model=model;

        initDialog(model.getACM().getScheduledHearingValue(), model.getCCV(),model);
    }

    /**
     * Initialises the Dialog
     */
    private void initDialog(ScheduledHearingValue shv, ChargeCompositeValue ccv,ChargesControllerModel model) {
        ResourceBundle resources = ResourceBundleHelper.getResourceBundle(XhibitBundles.JoinderResources);

        // Clone so wizard does not affect case details if wizard is cancelled
        ChargeValue chargeValueClone = (ChargeValue) this.clone(chargeValue);
        XhbCaseBasicValue caseValueClone = (XhbCaseBasicValue) this.clone(caseValue);

        // Create the first panel, and set the model.
        final RenumberCountsListPanel panel = new RenumberCountsListPanel(model, null);

        // Add the panels to the stack.
        ArrayList<RenumberCountsListPanel> al = new ArrayList<RenumberCountsListPanel>();
        al.add(panel);

        // Disable the next & finish button by default.
        getButtonPanel().getNext().setEnabled(false);
        getButtonPanel().getFinish().setEnabled(false);

       // model.setWizardPanels(al);
        addBodyPanels(al);
        pack();
        setSize(750, 550);
        centreDialog();

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent e) {
                try {
                    if (currentPanel == 0) {
                        panel.stepActivate();
                    }
                } catch (CSRecoverableException csre) {
                    XHIBITErrorHandler.handleError(csre);
                }
            }

            // Reset the model references when the dialog closes.
            public void windowClosed(WindowEvent e) {
                //if (model != null) {
                    //model.reset();
                }
               // model = null;
           // }
        });
    }

    public Object clone(Object object) {
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(byteOut);
            objectOut.writeObject(object);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return in.readObject();
            // The error handling below is pants, I've at least added log
            // messages they were getting swallowed.
            // Please throw exceptions where appropriate -- Neil Ellis
        } catch (StreamCorruptedException e) {
            // XHIBITConstant.error ("Stream corrupted CLONE EX");
            log.warn(e, e);
        } catch (OptionalDataException e) {
            // XHIBITConstant.error ("optional data ex");
            log.warn(e, e);
        } catch (IOException e) {
            // XHIBITConstant.error ("IO CLONE EX");
            log.fatal(e, e);
        } catch (ClassNotFoundException e) {
            // XHIBITConstant.error ("Class not found");
            log.fatal(e, e);
        }
        return null;
    }

    /**
     * Overrides prev() in XWizardDialog. Move to the previous card in the
     * stack. No life cycle steps will be called on the current panel.<br>
     * Once the previous panel is displayed, if it is an XPanel it will call<br>
     * <code>stepUpdateViewState()</code>
     *
     * @throws CSRecoverableException
     */
    public void prev() throws CSRecoverableException {
        super.prev();

        if (xPanels[currentPanel] instanceof XPanel) {
            XPanel xp = (XPanel) xPanels[currentPanel];
            xp.stepActivate();
            xp.stepUpdateViewState();
        }
    }
}