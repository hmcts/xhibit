package uk.gov.courtservice.xhibit.client.xhibitapplication;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: XHIBIT Client Framework
 * </p>
 * <p>
 * Description: Run implementation of this to login and start a Xhibit Session
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public interface XhibitInterface {
    public XhibitApplicationController newXhibitApplication();

    public void exitXhibitApplication(java.awt.Frame callingFrame) throws CSRecoverableException;

    public void closeXhibitApplication(XhibitApplicationController xac) throws CSRecoverableException;

    public void roamingTerminalHotSwitch(java.awt.Frame callingFrame) throws CSRecoverableException;
}