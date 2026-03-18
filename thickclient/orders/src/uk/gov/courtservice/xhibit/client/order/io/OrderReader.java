/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 9:53:06 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;

/**
 * An OrderReader is used to read in OrderData from an InputStrean.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public abstract class OrderReader {
    protected InputStream i;

    public OrderReader(InputStream i) {
        this.i = i;
    }

    public OrderReader(URL url) throws OrderReaderException {
        try {
            i = url.openStream();
        } catch (IOException e) {
            throw new OrderReaderException("Could not open url " + url.toExternalForm(), e);
        }

    }

    abstract public OrderData read() throws OrderReaderException;

}
