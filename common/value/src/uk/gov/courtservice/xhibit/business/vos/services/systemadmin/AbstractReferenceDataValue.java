package uk.gov.courtservice.xhibit.business.vos.services.systemadmin;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * Common superclass for ALL Reference Data Value Objects.
 * <p>
 * Implements the Reducible Interface, but as we are abstract, it is up to
 * concrete classes to implement.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.1
 */
public abstract class AbstractReferenceDataValue extends CSAbstractValue {
	private static final long serialVersionUID = 7498049166832769322L;
	protected static final Logger logger = CSServices.getLogger(AbstractReferenceDataValue.class);

    public AbstractReferenceDataValue() {
    }

    public AbstractReferenceDataValue(Integer key) {
        super(key);
    }

    public AbstractReferenceDataValue(Integer key, Integer version) {
        super(key, version);
    }

    /**
     * If the logger is allowing debug messages, write one.
     * <p>
     * If used by sub-classes, I think it will log to *this* class, but that is
     * better than everyone implementing the same method.
     * </p>
     * 
     * @param message
     *            String
     */
    protected void debug(String message) {

        if (logger.isDebugEnabled()) {
            logger.debug(message);
        }
    }
}