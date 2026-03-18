package uk.gov.courtservice.xhibit.client.util;

/**
 * <p>
 * Title: XHIBIT2
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
 * @author Frederik Vandendriessche
 * @version 1.0
 */

/*
 * This public interface sets the criteria for classes that wish to use the
 * XHIBIT Toolbar.
 */

public class XToolbarCommunicator {

    private XToolbarCommunicator() {

    }

    public XToolbarCommunicator(XDialog xDialog) {
        XToolbarCommunicator xtc = new XToolbarCommunicator();
        // xtc.

        // xDialog

        // XDialogs must be created by passing in their parents, or their root
        // XhibitApplicationController
    }

    public XToolbarCommunicator(XPanel xPanel) {
        XToolbarCommunicator xtc = new XToolbarCommunicator();
        // xtc.

        // xPanel

        // XPanels must be created by passing in their parents, or their root
        // XhibitApplicationController
    }

    // consumers of this class must override this method to have the correct
    // screencode displayed (using XHIBITConstant.getProperty?)
    public String getScreenCode() {
        return "######";
    }
}