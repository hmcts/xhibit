/**
 * Created by IntelliJ IDEA.
 * User: hzf3bb
 * Date: Jan 2, 2003
 * Time: 1:52:02 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.exceptions;

import uk.gov.courtservice.xhibit.client.exceptions.OrdersGenericRuntimeException;

public class IllegalOrderStateException extends OrdersGenericRuntimeException {

    public IllegalOrderStateException(String s) {
        super(s);
    }
}
