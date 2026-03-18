package uk.gov.courtservice.xhibit.webservice.scjseecho.cjseservice.server;

import java.io.StringWriter;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

/**
 * This class is used for help translating documents to Strings
 */
public class DocumentHelper
{
    public static String getDocumentAsXml(Document doc)
            throws TransformerConfigurationException, TransformerException
    {
        DOMSource domSource = new DOMSource(doc);
        String xmlString = getDocumentAsXml(domSource);
        return xmlString;
    }

    public static String getDocumentAsXml(DOMSource domSource)
            throws TransformerConfigurationException, TransformerException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        //transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
        //transformer.setOutputProperty(OutputKeys.ENCODING,"ISO-8859-1");
        transformer.setOutputProperty(OutputKeys.ENCODING,"utf-8");
        // we want to pretty format the XML output
        // note : this is broken in jdk1.5 beta!
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        //
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(domSource, sr);
        return sw.toString();
    }
}
