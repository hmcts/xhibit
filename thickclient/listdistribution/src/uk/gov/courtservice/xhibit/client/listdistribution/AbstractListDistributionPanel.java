package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.GridBagLayout;
import java.util.Properties;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: AbstractListDistributionPanel
 * </p>
 * <p>
 * Description: Common Functionality for list distribution panels.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: Will Fardell, Xdevelopment (2004)
 * </p>
 * 
 * @version $Id: AbstractListDistributionPanel.java,v 1.3 2006/05/10 08:01:41
 *          bzjrnl Exp $
 */
public abstract class AbstractListDistributionPanel extends XPanel {
    // The appication controller
    protected final XhibitApplicationController xac;

    /**
     * Construct a new panel for maintaining the recipients
     */
    public AbstractListDistributionPanel(XhibitApplicationController xac) throws CSRecoverableException {
        super(new GridBagLayout());

        if (xac == null) {
            throw new IllegalArgumentException("xac: null");
        }
        this.xac = xac;
    }

    // Framework

    /**
     * XPanel Implementaion: Called by the framework when the panel is made
     * visible.
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepActivate() throws CSRecoverableException {
        stepUpdateViewState();
    }

    /**
     * XPanel Implementaion: Called by the framework when the ok button is
     * pressed.
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // Framework Implementaion
    }

    /**
     * XPanel Implementaion: Called by the framework when the panel is made
     * invisible.
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepDeactivate() throws CSRecoverableException {
        // Framework Implementaion
    }

    /**
     * XPanel Implementaion: Called by the framework when the panel is closed
     * 
     * @throws CSRecoverableException
     *             if an error occures
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // Framework Implementaion
    }

    // Environment Utilities

    /**
     * Get the specifed resource from the list distribution resources
     */
    protected static String getResource(String key) {
        return ResourceBundleHelper.getResource(XhibitBundles.ListDistribution, key);
    }

    /**
     * Get a property from the listdistribution component properties
     */
    protected static String getProperty(String key) throws IllegalArgumentException {
        if (key != null) {
            String property = getProperties().getProperty(key);
            if (property != null) {
                return property;
            }
        }
        throw new IllegalArgumentException("key: " + key);
    }

    /**
     * Get properties collection for listdistribution (consider caching)
     */
    protected static Properties getProperties() {
        return CSServices.getConfigServices().getProperties("listdistribution");
    }

    /**
     * Get the court id of the current user
     */
    protected static Integer getCourtId() {
        return XhibitSingleton.getInstance().getCourtId();
    }

}
