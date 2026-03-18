package uk.gov.courtservice.xhibit.client.counselfacilities;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: FindRepresentativeWizardController
 * </p>
 * <p>
 * Description: Interface defining methods to used for the find representative
 * wizard life cycle.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public interface FindRepresentativeWizardController {
    public void stepUpdateViewState() throws CSRecoverableException;

    public void stepDeinitialise() throws CSRecoverableException;

    public XhibitApplicationController getXac();
}
