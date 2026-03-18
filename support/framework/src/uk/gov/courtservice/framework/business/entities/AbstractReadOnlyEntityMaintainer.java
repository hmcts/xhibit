package uk.gov.courtservice.framework.business.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * Throws an exception for the methods Create, Update & Delete because these
 * types of object are read-only.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.9 $
 */
public abstract class AbstractReadOnlyEntityMaintainer extends AbstractEntityMaintainer {
    public AbstractReadOnlyEntityMaintainer() {
    }

    /**
     * We do not allow the creation of ReferenceData.
     * 
     * @param nonDeterminedValue
     * @return EJBLocalObject
     * 
     * @throws <code>UnsupportedOperationException</code> always.
     */
    public final CSEntityLocal create(CSAbstractValue nonDeterminedValue, String userDisplayName) {
        throw new UnsupportedOperationException("Creation of reference data not allowed:"
                + nonDeterminedValue.getClass());
    }

    /**
     * We do not allow the deletion of ReferenceData.
     * 
     * @param id
     * @param version
     * 
     * @throws <code>UnsupportedOperationException</code> always.
     */
    public final void delete(Integer id, Integer version) {
        throw new UnsupportedOperationException("Deletion of reference data not allowed. Object ID:" + id);
    }

    /**
     * We do not allow the update of ReferenceData.
     * 
     * @param nonDeterminedValue
     * 
     * @throws <code>UnsupportedOperationException</code> always.
     */
    public final void update(CSAbstractValue nonDeterminedValue, String userDisplayName) {
        throw new UnsupportedOperationException("Updates to reference data not allowed:"
                + nonDeterminedValue.getClass());
    }
}
