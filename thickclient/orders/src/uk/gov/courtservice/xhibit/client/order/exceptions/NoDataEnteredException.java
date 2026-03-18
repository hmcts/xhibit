package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

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
 * @author unascribed
 * @version 1.0
 */

public class NoDataEnteredException extends OrdersGenericException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    /**
     * 
     * @param t
     */
    public NoDataEnteredException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }

    /**
     * 
     * @param s
     */
    public NoDataEnteredException(String s) {
        super(ERROR_KEY, s);
    }

}
