package uk.gov.courtservice.xhibit.client.order.exceptions;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author unascribed
 * @version 1.0
 */

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericException;

public class PanelNotInitialisedException extends OrdersGenericException {
    private static final String ERROR_KEY = "ORDERS_XXX";

    public PanelNotInitialisedException(String s) {
        super(ERROR_KEY, s);
    }

    public PanelNotInitialisedException(Throwable t) {
        super(ERROR_KEY, ERROR_KEY, t);
    }
}