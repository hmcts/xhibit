package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

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
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderInitialDataException extends OrdersGenericRuntimeException {

    /**
     * 
     * @param t
     */
    public OrderInitialDataException(Throwable t) {
        super(t);
    }

    /**
     * 
     * @param s
     */
    public OrderInitialDataException(String s) {
        super(s);
    }
}