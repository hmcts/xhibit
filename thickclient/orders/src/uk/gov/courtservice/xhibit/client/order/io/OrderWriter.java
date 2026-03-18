/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 9:53:06 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.io;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.OrderFactory;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;

/**
 * <p>
 * Title: The abstract class from which the OrderWriter implementation extends.
 * </p>
 * <p>
 * Description: An OrderWriter is responsible for writing out an
 * 
 * @see uk.gov.courtservice.xhibit.client.order.xml.XMLOrderWriter
 *      </p>
 *      <p>
 *      Copyright: Copyright (c) 2002
 *      </p>
 *      <p>
 *      Company: EDS
 *      </p>
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public abstract class OrderWriter {
    // an output stream of bytes.
    private OutputStream os;

    // an OrderTransform used to transform data using xslt.
    private OrderTransform orderTransform;

    /**
     * Construct an OrderWriter used to write the modified order data after the
     * data has been transformed.
     * 
     * @param os
     *            an output stream.
     * @throws OrderWriterException
     *             thrown if an attempt to write the order data fails.
     */
    public OrderWriter(OutputStream os) throws OrderWriterException {
        try {
            orderTransform = OrderFactory.getTransform();
        } catch (OrderTransformException e) {
            throw new OrderWriterException(e);
        }
        this.os = os;

    }

    /**
     * @deprecated use the OutputStream constructor instead.
     * @param url
     * @throws OrderWriterException
     *             thrown if an attempt to write the order data fails.
     */
    public OrderWriter(URL url) throws OrderWriterException {
        try {

            orderTransform = OrderFactory.getTransform();
            URLConnection connection = url.openConnection();
            // connection.connect();
            connection.setDoOutput(true);
            os = connection.getOutputStream();

        } catch (IOException e) {
            throw new OrderWriterException(e);
        } catch (OrderTransformException e) {
            throw new OrderWriterException(e);
        }
    }

    /**
     * Write data after performing transform.
     * 
     * @param data
     *            OrderData
     * @throws OrderWriterException
     *             thrown if an attempt to write the order data fails.
     */
    public void write(OrderData data) throws OrderWriterException {

        try {
            orderTransform.transform(data, os);
        } catch (OrderTransformException e) {
            throw new OrderWriterException(e);
        }

    }

}
