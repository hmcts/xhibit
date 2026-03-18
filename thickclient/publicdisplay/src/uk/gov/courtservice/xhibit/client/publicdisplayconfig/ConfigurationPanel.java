package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Outer Panel for Public Display Configurator
 * </p>
 * <p>
 * Description: Panel that displays the maintain screens and maintain rotation
 * set panels.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: ConfigurationPanel.java,v 1.5 2006/06/05 12:32:06 bzjrnl Exp $
 */

public class ConfigurationPanel extends XPanel {
    private MaintainRotationSetPanel rotationSetPanel;

    private MaintainScreensPanel screenPanel;

    /**
     * Create instances of the maintain rotation set panel and the maintain
     * screen panel. Includes a listener from the rotation set panel to listen
     * for changes to the screens
     * 
     * @throws CSRecoverableException
     */
    public ConfigurationPanel() throws CSRecoverableException {
        super(new GridBagLayout());
        rotationSetPanel = new MaintainRotationSetPanel();
        screenPanel = new MaintainScreensPanel();
        screenPanel.addPropertyChangeListener(MaintainScreensPanel.NOTIFY_SCREEN_CHANGE,
                new MaintainScreenChangeListener(rotationSetPanel));
        this.add(screenPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        this.add(rotationSetPanel, new GridBagConstraints(1, 0, 1, 1, 0.5, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
    }

    /**
     * Empty Implementation
     */
    public void stepInitialise() {
        // No implementation
    }

    /**
     * Delegates the stepDeactivate calls to the rotation set panel and the
     * screen panel
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        rotationSetPanel.stepDeactivate();
        screenPanel.stepDeactivate();
    }

    /**
     * Delegates the stepValidate calls to the rotation set panel and the screen
     * panel
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        rotationSetPanel.stepValidate();
        screenPanel.stepValidate();
    }

    /**
     * Delegates the stepUpdateViewState calls to the rotation set panel and the
     * screen panel
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        rotationSetPanel.stepUpdateViewState();
        screenPanel.stepUpdateViewState();
    }

    /**
     * Delegates the stepDeinitialise calls to the rotation set panel and the
     * screen panel
     * 
     * @param save
     *            Will always be false as we have removed the OK button
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean save) throws CSRecoverableException {
        rotationSetPanel.stepDeinitialise(save);
        screenPanel.stepDeinitialise(save);
    }

    /**
     * Delegates the stepActivate calls to the rotation set panel and the screen
     * panel
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        rotationSetPanel.stepActivate();
        screenPanel.stepActivate();
    }
}