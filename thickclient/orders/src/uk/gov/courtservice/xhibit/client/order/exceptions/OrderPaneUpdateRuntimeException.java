package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

/**
 * @author David Duncan & Neil Ellis
 */
public class OrderPaneUpdateRuntimeException extends OrdersGenericRuntimeException {
    public OrderPaneUpdateRuntimeException() {
    }

    public OrderPaneUpdateRuntimeException(Throwable t) {
        super(t);
    }

    public OrderPaneUpdateRuntimeException(String s, Throwable t) {
        super(s, t);
    }

    public OrderPaneUpdateRuntimeException(String s) {
        super(s);
    }
}
