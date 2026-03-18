package uk.gov.courtservice.xhibit.client.maintaincharges;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
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
 * @author Bal Bhamra
 * @version 1.0
 */

public interface BreachController {
    public void stepUpdateViewState() throws CSRecoverableException;

    public void stepDeinitialise() throws CSRecoverableException;

    public XhibitApplicationController getXac();
}
