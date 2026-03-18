package uk.gov.courtservice.xhibit.business.services.witness.interfaces;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Abstract State Managed Object
 * </p>
 * <p>
 * Description: A class that provides covenience methods to state-managed
 * objects.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 * 
 */
public abstract class AbstractStateManagedObject implements StateManaged {
    private static final Logger log = CSServices.getLogger(AbstractStateManagedObject.class);

    public int metaState = NEW;

    /**
     * @pre metaState == UNSAVED || metaState == NEW || metaState == UPDATED ||
     *      metaState == MODIFIED || metaState == REMOVED
     * @param metaState
     */
    public void setMetaState(final int metaState) {
        log.debug("Meta state set to:" + metaState);
        this.metaState = metaState;
    }

    /**
     * @post return == UNSAVED || return == NEW || return == UPDATED || return ==
     *       MODIFIED || return == REMOVED
     * @return
     */
    public int getMetaState() {
        log.debug("Meta state is:" + metaState);
        return metaState;
    }

    public void markAsModified() {
        if (getMetaState() == NEW) {
            setMetaState(UNSAVED);
        } else if (getMetaState() == UPDATED) {
            setMetaState(MODIFIED);
        }
        log.debug("Meta marked as:" + metaState);
    }

    public void checkForRemoval() {
        checkForUpdate();
    }

    public void checkForUpdate() {
        if (getMetaState() == REMOVED) {
            throw new IllegalStateException(
                    "Persisted instance of this object has been removed, this object is not valid.");
        }
        if (getMetaState() == UNSAVEABLE) {
            throw new IllegalStateException(
                    "Persisted instance of this object is not allowed it is marked as unsaveable.");
        }
    }

    /**
     * @post (getMetaState() == UNSAVED || getMetaState() == MODIFIED) implies
     *       return == true
     * 
     */
    public boolean isModified() {
        return getMetaState() == UNSAVED || getMetaState() == MODIFIED;
    }

}
