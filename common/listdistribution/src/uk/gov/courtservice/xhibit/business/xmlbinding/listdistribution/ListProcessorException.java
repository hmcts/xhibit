package uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution;

import uk.gov.courtservice.xhibit.business.exceptions.listdistribution.ListDistributionException;

/**
 * <p>
 * Title: ListProcessorException
 * </p>
 * <p>
 * Description: Thrown by the ListProcessor when an error occurs.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListProcessorException.java,v 1.1 2005/02/23 14:55:36 bzjrnl
 *          Exp $
 */
public class ListProcessorException extends ListDistributionException {
	
	static final long serialVersionUID = -8218095653514679151L;
	
    /**
     * Construct a new ListProcessorException with the specified cause.
     */
    public ListProcessorException(Throwable cause) {
        super(cause);
    }

    /**
     * Construct a new ListProcessorException with the specified message and
     * cause.
     */
    public ListProcessorException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Construct a new ListProcessorException with the specified message and
     * cause.
     */
    public ListProcessorException(String msg) {
        super(msg);
    }
}