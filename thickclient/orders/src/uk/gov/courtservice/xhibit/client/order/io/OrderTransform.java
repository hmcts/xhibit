/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Dec 4, 2002
 * Time: 2:05:55 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.io;

import java.io.OutputStream;

import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;

/**
 * OrderTransform provides functionality to transform OrderData to an
 * OutputStream.
 * 
 * @author Neil Ellis & Neil Entwistle
 */

public abstract class OrderTransform {
    public OrderTransform() {
    }

    public abstract void transform(OrderData data, OutputStream os) throws OrderTransformException;
}
