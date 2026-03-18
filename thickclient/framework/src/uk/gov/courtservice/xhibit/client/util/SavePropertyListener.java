package uk.gov.courtservice.xhibit.client.util;

import java.beans.PropertyChangeEvent;

import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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

public class SavePropertyListener implements java.beans.PropertyChangeListener, java.io.Serializable {
    private XhibitApplicationController xac = null;

    public SavePropertyListener(XhibitApplicationController myController) {
        xac = myController;
    }

    public void propertyChange(PropertyChangeEvent ev) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("Property changed");
        XAction saveAction = XhibitActions.getAction(xac, XhibitActions.Save);
        saveAction.setEnabled(((Boolean) ev.getNewValue()).booleanValue());
    }
}