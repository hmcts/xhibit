/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 29, 2002
 * Time: 4:38:23 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.xml;

import java.io.OutputStream;

import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
import uk.gov.courtservice.xhibit.client.order.io.OrderWriter;

/**
 * This class is used to persist an instance of OrderData. It is instatiated
 * from OrderFactory.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class XMLOrderWriter extends OrderWriter {

    public XMLOrderWriter(OutputStream os) throws OrderWriterException {
        super(os);
    }

    // public XMLOrderWriter(URL url) throws OrderWriterException
    // {
    // super(url);
    // }
    //
}
