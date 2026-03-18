package uk.gov.courtservice.xhibit.client.im.screens;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: IMDisplayPanel
 * </p>
 * <p>
 * Description: XPanel to hold the Instant Messaging screen components. This is
 * required to be added to an XDialog
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class IMDisplayPanel extends XPanel {
    private static final Logger log = CSServices.getLogger(IMDisplayPanel.class);

    /**
     * Constructor
     */
    public IMDisplayPanel() {
        super();
    }

    /**
     * Framework method
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepInitialise()");
    }

    /**
     * Framework method
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepDeactivate()");
    }

    /**
     * Framework method
     * 
     * @throws
     * uk.gov.courtservice.framework.services.validation.CSValidationException
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException,
            uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepValidate()");
    }

    /**
     * Framework method
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepUpdateViewState() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepUpdateViewState()");
    }

    /**
     * Framework method
     * 
     * @param parm1
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepDeinitialise() " + parm1);
        // stepDeactivate is called when the panel is closed - at this point
    }

    /**
     * Framework method
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
        log.debug("IMDisplayPanel:stepActivate()");
    }

}