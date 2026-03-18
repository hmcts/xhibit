package uk.gov.courtservice.xhibit.business.services.systemadmin;

import uk.gov.courtservice.framework.business.entities.AbstractReadOnlyEntityMaintainer;

/**
 * Implements the method 'createQuery' and handles logging for Ref Data
 * Maintainers.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.7 $
 */
public abstract class ReferenceDataMaintainer extends AbstractReadOnlyEntityMaintainer {
    protected static final String ENTER_METHOD = "Entered: ";

    protected static final String EXIT_METHOD = "Exited: ";

    public ReferenceDataMaintainer() {
    }
}