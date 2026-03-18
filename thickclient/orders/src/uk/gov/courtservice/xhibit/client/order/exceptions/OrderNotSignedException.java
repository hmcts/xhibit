package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

/**
 * <p>
 * Title: OrderNotSignedException
 * </p>
 * <p>
 * Description: Thrown if all order signed details are not captured
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

public class OrderNotSignedException extends OrdersGenericException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    public OrderNotSignedException(String s) {
        super(ERROR_KEY, s);
    }

    public OrderNotSignedException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

}