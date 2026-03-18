package uk.gov.courtservice.xhibit.business.services.witness.interfaces;

import uk.gov.courtservice.xhibit.business.services.witness.exceptions.ModificationException;

/**
 * <p>
 * Title: A State Managed Object
 * </p>
 * <p>
 * Description: This interface exposes the state managed aspects of an object to
 * allow a consistent interface to persistance of the object. By checking it's
 * own state an object can have combined and optimized updates and can be aware
 * of it having been removed from the persistent store.
 * </p>
 * <p>
 * Also with the UNSAVEABLE state an object can guarantee it won't be persisted.
 * This obviously must be implemented in the update and remove methods.
 * <p>
 * <p>
 * The methods provided are just the <b>Up</b>date and <b>D</b>elete
 * <i>(although I prefer to use remove())</i> the <b>C</b>reate method should
 * be a factory and the <b>R</b>etrieve should either be on the factory or part
 * of a <i>Selector</i> class. The exception is in objects that have
 * hierachical relationships in which case the create and retrieve can logically
 * form part of the parent class.
 * 
 * <p>
 * <a href="http://gbspsiad002:8888/Xhibit/550">Swiki Page</a>
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.6 $
 * 
 * @invariant getMetaState() == NEW ||getMetaState() == UNSAVEABLE ||
 *            getMetaState() == UPDATED || getMetaState() == MODIFIED ||
 *            getMetaState() == REMOVED || getMetaState() == UNSAVED
 */
public interface StateManaged extends java.io.Serializable {
    int NEW = 1;

    int UPDATED = 2;

    int MODIFIED = 3;

    int REMOVED = 4;

    int UNSAVED = 5;

    int UNSAVEABLE = 6;

    /**
     * Persist the object. <i>Please note the pre/post conditions for this
     * method.</i>
     * 
     * @pre getMetaState() != UNSAVEABLE
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == UPDATED
     * @throws ModificationException
     */
    void update() throws ModificationException;

    /**
     * Remove the object from the persistent store. <i>Please note the pre/post
     * conditions for this method.</i>
     * 
     * @pre getMetaState() != UNSAVEABLE
     * @pre getMetaState() != REMOVED
     * @post getMetaState() == REMOVED
     * @throws ModificationException
     */
    void remove() throws ModificationException;

    /**
     * @return the current metaState of the object
     */
    int getMetaState();

    /**
     * If the meta state of the object is NEW/UPDATED then mark it as
     * UNSAVED/MODIFIED. Note if the object is UNSAVEABLE then no action is
     * taken but this is not an error condition.
     * 
     * @pre getMetaState() != REMOVED
     * @post getMetaState()@pre == NEW implies getMetaState() == UNSAVED
     * @post getMetaState()@pre == UPDATED implies getMetaState() == MODIFIED
     * 
     */
    void markAsModified();

    /**
     * Set the meta state to one of the constants defined by this interface.
     * 
     * @pre metaState == UNSAVED || metaState == NEW || metaState == UPDATED ||
     *      metaState == MODIFIED || metaState == REMOVED || metaState ==
     *      UNSAVEABLE
     * @param metaState
     *            the meta state to change the object to.
     */
    void setMetaState(int metaState);
}
