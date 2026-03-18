/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Dec 2, 2002
 * Time: 5:55:41 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
import uk.gov.courtservice.xhibit.client.order.io.OrderTransform;
import uk.gov.courtservice.xhibit.client.order.io.OrderWriter;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderTransform;
import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderWriter;

/**
 * This class provides factory methods for the default implementation of the
 * OrderReader,OrderWriter and OrderTransform group. The OrderData
 * implementation is created by the OrderReader and so there is no factorey
 * metrhod for it.
 * 
 * @author Neil Ellis & Neil Entwistle
 * 
 * @see OrderReader
 * @see OrderWriter
 * @see OrderData
 * 
 */
public class OrderFactory {
    /**
     * Factory method to create an OrderReader.
     * 
     * @param is
     *            The InputStream to be used by the OrderReader.
     * @return An OrderReader.
     */
    public static OrderReader getReader(InputStream is) {
        return new XMLOrderReader(is);
    }

    /**
     * Factory method to create an OrderWriter.
     * 
     * @param os
     *            The OutputStream to be used by the OrderWriter.
     * @return An instance of an OrderWriter
     * @throws OrderWriterException
     */
    public static OrderWriter getWriter(OutputStream os) throws OrderWriterException {
        return new XMLOrderWriter(os);
    }

    /**
     * Factory method to create an OrderTransform.
     * 
     * @param is
     *            The InputStream used to build the OrderTransform
     * @return An instance of an OrderTransform
     * @throws OrderTransformException
     */
    public static OrderTransform getTransform(InputStream is) throws OrderTransformException {
        return new XMLOrderTransform(is);
    }

    /**
     * Factory method to create an OrderTransform.
     * 
     * @return An instance of an OrderTransform.
     * @throws OrderTransformException
     */
    public static OrderTransform getTransform() throws OrderTransformException {
        return new XMLOrderTransform();
    }

    /**
     * Factory method to create an OrderTransform.
     * 
     * @deprecated
     * @param url
     *            The URL of the transform to be used.
     * @return An instance of an OrderTransform.
     * @throws OrderTransformException
     */
    public static OrderTransform getTransform(URL url) throws OrderTransformException {
        return new XMLOrderTransform(url);
    }

}
