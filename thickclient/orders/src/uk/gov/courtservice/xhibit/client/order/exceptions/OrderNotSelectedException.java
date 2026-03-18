package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

/**
 * <p>
 * Title: OrderNotSelectedException
 * </p>
 * <p>
 * Description: Exception thrown when order not selected from list
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

public class OrderNotSelectedException extends OrdersGenericException {

    private static final String ERROR_KEY = "ORDERS_XXX";

    public OrderNotSelectedException(String s) {
        super(ERROR_KEY, s);
    }

    public OrderNotSelectedException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }
}