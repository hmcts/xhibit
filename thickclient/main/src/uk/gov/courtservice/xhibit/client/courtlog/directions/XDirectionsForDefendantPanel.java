package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.util.Enumeration;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

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
 * @version 1.0
 */

public abstract class XDirectionsForDefendantPanel extends XDirectionsPanel {

    JLabel warningLabel;

    protected JLabel getWarningLabel() {
        if (warningLabel == null) {
            warningLabel = DirectionsFactory.createWarningLabel(ResourceBundleHelper.getResource(
                    XhibitBundles.Directions, "MultipleDefendantWarning"));
        }
        return warningLabel;
    }

    protected void showWarning(ButtonGroup buttonGroup) {
        setWarningVisible(buttonGroup, true);
    }

    protected void clearWarning(ButtonGroup buttonGroup) {
        boolean showWarning = getWarningLabel().isVisible();
        if (showWarning) {
            setWarningVisible(buttonGroup, false);
        }
    }

    /**
     * This will hide/show the warning label and set the dispaly state of the
     * radio buttons (if the group is passed in). Should the panel require
     * additional processing this method should be overriden in the calling
     * panel, calling super();
     * 
     * @param buttonGroup
     * @param visible
     */
    protected void setWarningVisible(ButtonGroup buttonGroup, boolean visible) {
        getWarningLabel().setVisible(visible);
        if (buttonGroup != null) {
            Enumeration enumeration = buttonGroup.getElements();
            while (enumeration.hasMoreElements()) {
                JRadioButton item = (JRadioButton) enumeration.nextElement();
                item.setIcon(visible ? DirectionsFactory.rbDisabled : null);
                item.setSelectedIcon(visible ? DirectionsFactory.rbDisabledSelected : null);
            }
        }
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        super.stepDeactivate();
        setModified(false);
    }
}