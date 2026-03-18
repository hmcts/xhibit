/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 9:46:49 AM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.xml;

import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
import uk.gov.courtservice.xhibit.client.order.io.OrderReader;

/**
 * This class is used to read an XMLOrderData from an InputStream or a URL. It
 * is used as an OrderReader and is instatiated from OrderFactory.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class XMLOrderReader extends OrderReader {

    public XMLOrderReader(InputStream i) {
        super(i);
    }

    public XMLOrderReader(URL url) throws OrderReaderException {
        super(url);
    }

    public OrderData read() throws OrderReaderException {
        Document result = null;
        try {
            // Get Document Builder Factory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            // Turn on validation, and turn off namespaces
            factory.setValidating(false);
            factory.setNamespaceAware(true);

            DocumentBuilder builder = null;

            builder = factory.newDocumentBuilder();
            result = builder.parse(i);
        } catch (Exception e) {
            throw new OrderReaderException(e);
        }

        return new XMLOrderData(result);
    }

}
