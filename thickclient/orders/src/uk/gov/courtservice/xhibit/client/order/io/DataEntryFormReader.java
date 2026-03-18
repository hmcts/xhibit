/**
 * Created by IntelliJ IDEA.
 * User: EDS
 * Date: Nov 27, 2002
 * Time: 6:28:44 PM
 * To change this template use Options | File Templates.
 */
package uk.gov.courtservice.xhibit.client.order.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.DataEntryReaderException;
import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.components.OrderSection;
import uk.gov.courtservice.xhibit.client.order.xml.sax.DataEntryFormSAXHandler;

// Constructor should take the InputStream and data, could this all be
// refactored into
// DataEntryPanelAnyway ?????? Static ???

/**
 * The DataEntryFormReader is used to read a DataEntryTemplate from an
 * InputStream.
 * 
 * @author Neil Ellis & Neil Entwistle
 */
public class DataEntryFormReader {
    private static final Logger log = CSServices.getLogger(DataEntryFormReader.class);

    private OrderData data;

    public DataEntryFormReader(OrderData data) {
        this.data = data;
    }

    public DataEntryPanel read(InputStream i) throws DataEntryReaderException {
        try {
            XMLReader parser = XMLReaderFactory.createXMLReader();
            DataEntryFormSAXHandler entryTemplateSAXHandlerInstance = new DataEntryFormSAXHandler(data);
            parser.setContentHandler(entryTemplateSAXHandlerInstance);
            
            // The following lines will need to be reintroduced later
            // parser.setFeature("http://xml.org/sax/features/validation",
            // true);
            // parser.setFeature("http://apache.org/xml/features/validation/schema",
            // true);
            parser.parse(new InputSource(i));
            return entryTemplateSAXHandlerInstance.getEntryTemplate();
        } catch (SAXException e) {
            log.error("SAX error when parsing dataEntryFormInputStream:" + e.getMessage());
            e.printStackTrace();
            throw new DataEntryReaderException(e);
        } catch (IOException e) {
            log.error("IO error when parsing dataEntryFormInputStream:" + e.getMessage());
            e.printStackTrace();
            throw new DataEntryReaderException(e);
        }

    }

    public DataEntryPanel read(URL url) throws DataEntryReaderException {
        try {
            InputStream i = url.openStream();
            if (i == null) {
                throw new DataEntryReaderException("Unable to open the URL '" + url.toExternalForm() + "'");
            }
            return read(i);
        } catch (IOException e) {
            throw new DataEntryReaderException(e);
        }
    }

}
