package uk.gov.courtservice.xhibit.client.results.pleas;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XDatePanel;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title: Panel for the Arraingnment Date and Time for Pleas for Indictments
 * </p>
 * <p>
 * Description: Panel to allow the user to enter the Arraignment Date/Time for
 * Indictments on the Pleas screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.17 $
 */
public class ArraignmentPanel extends XPanel {
    private PleaControllerModel model = null;

    private XDatePanel arraignmentDate = null;

    /**
     * Constructor creates an ArraignmentPanel.
     * 
     * @param model
     *            PleaControllerModel from the PleaController
     */
    public ArraignmentPanel(PleaControllerModel model) {
        try {
            this.model = model;
            stepInitialise();
            jbInit();
            stepActivate();
        } catch (Exception ex) {
            XHIBITErrorHandler.handleError(ex);
        }
    }

    /**
     * Layout the widgets on the screen.
     */
    private void jbInit() {
        setLayout(new GridBagLayout());
        TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow), ResourceBundleHelper.getResource(XhibitBundles.Pleas,
                "arraignment.panel.border.title"));
        setBorder(titledBorder);

        add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "arraignment.panel.label")),
                new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.Pleas, "arraignment.label.date")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        add(getArraignmentDate(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    /**
     * Lazy instantiates the Arraignment date.
     * 
     * @return the Arraignment Date field.
     */
    public XDatePanel getArraignmentDate() {
        if (arraignmentDate == null) {
            arraignmentDate = new XDatePanel(this);
            arraignmentDate.setRequired(true);
            arraignmentDate.setDateEditable(false);
            arraignmentDate.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        // Ignore cursor keys.
                        if (!isACursorKeyReleased(e)) {
                            // The model only contains a date if the
                            // arraignment
                            // date field contains a valid date.
                            if (arraignmentDate.isMandatoryFieldsCompleted()) {
                                moveScreenToModel();
                            } else {
                                model.setArraignmentDate(null);
                            }
                        }
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
        }
        return arraignmentDate;
    }

    /**
     * Checks if a cursor key has been released.
     * 
     * @param ke
     *            the KeyEvent
     * @return true if a cursor key has been released.
     */
    private boolean isACursorKeyReleased(KeyEvent ke) {
        return (ke.getKeyCode() == KeyEvent.VK_LEFT || ke.getKeyCode() == KeyEvent.VK_RIGHT
                || ke.getKeyCode() == KeyEvent.VK_DOWN || ke.getKeyCode() == KeyEvent.VK_UP);
    }

    /**
     * Moves data from the Plea Controller model to the screen.
     */
    private void moveModelToScreen() {
        getArraignmentDate().setDate(model.getArraignmentDate());
    }

    /**
     * Saves the arraignment date/time to the PleaController model.
     * 
     * @throws CSValidationException
     *             if an invalid date or time has been entered
     */
    private void moveScreenToModel() throws CSValidationException {
        model.setArraignmentDate(getArraignmentDate().getDate());
    }

    /**
     * XPanel implementation used to intialise or retrieve any data required by
     * this panel.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        model.setArraignmentDate(Calendar.getInstance());
    }

    /**
     * XPanel implementation used to save data when this screen is left(closed).
     * Not used here
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * XPanel implementation used to validate data enterd on the panel.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     *             if the data on the screen in error.
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        moveScreenToModel();
    }

    /**
     * XPanel implementation used to update the state of controls on the screen
     * when some action or event take place.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        moveScreenToModel();
    }

    /**
     * XPanel implementation would be used to save any data when the panel is
     * closing.
     * 
     * @param update
     *            true is the screen is saving.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * XPanel implementation used to intialise the state of the panel when (each
     * time) it is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveModelToScreen();
    }
}