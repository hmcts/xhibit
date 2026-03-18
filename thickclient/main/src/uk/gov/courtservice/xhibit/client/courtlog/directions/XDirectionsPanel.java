package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.util.HashMap;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * <p>
 * Title: Abstract panel for directions
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
 * @author Rakesh Lakhani
 * @version 1.0
 */

public abstract class XDirectionsPanel extends XPanel {
    private boolean activating = false;

    private boolean deactivating = false;

    abstract public void moveModelToScreen() throws CSRecoverableException;

    abstract public void moveScreenToModel() throws CSRecoverableException;

    public void stepInitialise() {
    }

    public void stepDeinitialise(boolean update) {
    }

    public void stepActivate() throws CSRecoverableException {
        activating = true;
        moveModelToScreen();
        stepUpdateViewState();
        activating = false;
    }

    public void stepDeactivate() throws CSRecoverableException {
        deactivating = true;
        moveScreenToModel();
        deactivating = false;
    }

    abstract public void populateCRUD(HashMap crud, Integer defOnCaseId);

    abstract public Integer getEventType();

    public boolean isActivating() {
        return activating;
    }

    public boolean isDeactivating() {
        return deactivating;
    }

    public void setModified(boolean modified) {
        if (!isActivating()) {
            super.setModified(modified);
        }
    }

}