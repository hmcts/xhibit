package uk.gov.courtservice.xhibit.client.results.verdicts;

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
 * Title: Panel for the Conviction Date for Verdicts for Indictments
 * </p>
 * <p>
 * Description: Panel to allow the user to enter the Conviction Date for
 * Indictments on the Verdicts screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Revision: 1.13 $
 */
public class ConvictionDatePanel extends XPanel {
    private VerdictControllerModel model = null;

    private TitledBorder titledBorder1 = null;

    private XDatePanel verdictDate = null;

    /**
     * Constructor creates a ConvictionDatePanel
     * 
     * @param model
     *            VerdictControllerModel from the VerictsController
     */
    public ConvictionDatePanel(VerdictControllerModel model) {
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
     * Paints the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow), ResourceBundleHelper.getResource(XhibitBundles.Verdicts,
                "conviction.panel.border.title"));
        this.setBorder(titledBorder1);

        this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "conviction.panel.label")),
                new GridBagConstraints(0, 0, 4, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));

        this.add(new JLabel(ResourceBundleHelper.getResource(XhibitBundles.Verdicts, "conviction.label.date")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));

        this.add(getConvictionDate(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    /**
     * Lazy instantiates the conviction date.
     * 
     * @return the conviction date field.
     */
    private XDatePanel getConvictionDate() {
        if (verdictDate == null) {
            verdictDate = new XDatePanel(this);
            verdictDate.setRequired(true);
            verdictDate.setDateEditable(false);
            verdictDate.getDateComponent().getDisplay().addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    try {
                        // Ignore cursor keys.
                        if (!isACursorKeyReleased(e)) {
                            // The model only contains a date if the
                            // verdict
                            // date field contains a valid date.
                            if (verdictDate.isMandatoryFieldsCompleted()) {
                                moveScreenToModel();
                            } else {
                                model.setVerdictDate(null);
                            }
                        }
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
        }
        return verdictDate;
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
     * Move data from the VerdictController model to the screen.
     */
    private void moveModelToScreen() {
        verdictDate.setDate(model.getVerdictDate());
    }

    /**
     * Saves the conviction date to the VerdictController model.
     * 
     * @throws CSValidationException
     *             if an invalid date has been entered
     */
    private void moveScreenToModel() throws CSValidationException {
        model.setVerdictDate(getConvictionDate().getDate());
    }

    /**
     * XPanel implementation used to intialise or retrieve any data required by
     * this panel.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        model.setVerdictDate(Calendar.getInstance());
    }

    /**
     * XPanel implementation used to save data when this screen is left(closed).
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