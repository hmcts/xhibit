package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

/**
 * <p>
 * Title: Xhibit 2 OrderExistsException
 * </p>
 * <p>
 * Description: If an order of the selected type, for the selected defendant on
 * the current case already exists, throw this exception
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

public class OrderExistsException extends OrdersGenericException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    /**
     * 
     * @param t
     */
    public OrderExistsException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

    /**
     * 
     * @param s
     */
    public OrderExistsException(String s) {
        super(ERROR_KEY, s);
    }
}