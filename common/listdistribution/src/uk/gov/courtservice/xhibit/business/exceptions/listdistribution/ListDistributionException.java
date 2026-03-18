package uk.gov.courtservice.xhibit.business.exceptions.listdistribution;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * <p>
 * Title: ListDistributionException
 * </p>
 * <p>
 * Description: Thrown by the ListDistribution when an error occurs.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListDistributionException.java,v 1.1 2005/02/23 14:55:33 bzjrnl
 *          Exp $
 */
public class ListDistributionException extends CSUnrecoverableException {
	
	static final long serialVersionUID = -4424808752907264330L;
	
    /**
     * Construct a new ListDistributionException with the specified cause.
     */
    public ListDistributionException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new ListDistributionException with the specified message and
     * cause.
     */
    public ListDistributionException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Construct a new ListDistributionException with the specified message and
     * cause.
     */
    public ListDistributionException(String msg) {
        super(msg);
    }
}