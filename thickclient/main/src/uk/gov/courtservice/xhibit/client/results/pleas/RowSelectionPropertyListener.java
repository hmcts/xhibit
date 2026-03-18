package uk.gov.courtservice.xhibit.client.results.pleas;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;

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

public class RowSelectionPropertyListener implements PropertyChangeListener, Serializable {
    XPanel panel;

    public RowSelectionPropertyListener(XPanel panel) {
        this.panel = panel;
    }

    public void propertyChange(PropertyChangeEvent ev) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("[RowSelectionPropertyListener] Property changed");
        try {
            panel.stepUpdateViewState();
        } catch (CSRecoverableException csre) {
            XHIBITConstant.handleError(csre);
        }
    }
}