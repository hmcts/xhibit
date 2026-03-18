package uk.gov.courtservice.xhibit.client.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Property change listener for menu item Edit/Select All
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class SelectAllPropertyListener implements PropertyChangeListener, Serializable {
    private XhibitApplicationController xac = null;

    public SelectAllPropertyListener(XhibitApplicationController myController) {
        xac = myController;
    }

    public void propertyChange(PropertyChangeEvent pce) {
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Property
        // changed");
        XAction selectAllAction = XhibitActions.getAction(xac, XhibitActions.EditSelectAll);
        selectAllAction.setEnabled(((Boolean) pce.getNewValue()).booleanValue());
    }
}