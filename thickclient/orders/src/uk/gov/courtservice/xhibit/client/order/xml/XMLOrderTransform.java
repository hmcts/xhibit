package uk.gov.courtservice.xhibit.client.order.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.w3c.dom.Document;

import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
import uk.gov.courtservice.xhibit.client.order.io.OrderTransform;

/**
 * 
 * <p>
 * Title: XMLOrderTransform
 * </p>
 * <p>
 * Description: This class is used to perform transforms on XMLOrderData, it is
 * used as and OrderTransform and is instatiated from OrderFactory.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Ellis & Neil Entwistle
 * @version 1.0
 */
public class XMLOrderTransform extends OrderTransform {
    private Transformer transformer;

    private Source transformSource;

    // --Recycle Bin START (12/6/02 2:31 PM):
    // public Source getTransformSource()
    // {
    // return transformSource;
    // }
    // --Recycle Bin STOP (12/6/02 2:31 PM)

    private void setTransformSource(InputStream i) {
        this.transformSource = new StreamSource(i);
    }

    private void setTransformSource(URL url) throws OrderTransformException {
        try {
            this.transformSource = new StreamSource(url.openStream());
        } catch (IOException e) {
            throw new OrderTransformException(e);
        }
    }

    public XMLOrderTransform() throws OrderTransformException {
        try {
            initTransformer();
        } catch (TransformerConfigurationException e) {
            throw new OrderTransformException(e);
        }
    }

    public XMLOrderTransform(InputStream is) throws OrderTransformException {
        setTransformSource(is);
        try {
            initTransformer();
        } catch (TransformerConfigurationException e) {
            throw new OrderTransformException(e);
        }
    }

    public XMLOrderTransform(URL url) throws OrderTransformException {
        setTransformSource(url);
        try {
            initTransformer();
        } catch (TransformerConfigurationException e) {
            throw new OrderTransformException(e);
        }
    }

    /**
     * Transform the order data
     * 
     * @param data
     *            the order data
     * @param os
     *            the output stream to output the transform
     * @throws OrderTransformException
     */
    public void transform(OrderData data, OutputStream os) throws OrderTransformException {
        if (!(data instanceof XMLOrderData)) {
            throw new OrderTransformException("Order data was not in XML format.");
        }
        try {
            XMLOrderData xmlOrderData = (XMLOrderData) data;
            Document dom = xmlOrderData.getDom();
            transformer.transform(new DOMSource(dom), new StreamResult(os));

        } catch (TransformerFactoryConfigurationError transformerFactoryConfigurationError) {
            throw new OrderTransformException("Failed to transform due to a transformer factory configuration error.",
                    transformerFactoryConfigurationError);

        } catch (TransformerConfigurationException e) {
            throw new OrderTransformException("Failed to transform due to a transformer configuration exception.", e);
        } catch (TransformerException e) {
            throw new OrderTransformException("Failed to transform due to a general transform exception.", e);
        }
    }

    private void initTransformer() throws TransformerConfigurationException {
        TransformerFactory tFactory = TransformerFactory.newInstance();
        if (transformSource == null) {
            transformer = tFactory.newTransformer();
        } else {
            transformer = tFactory.newTransformer(transformSource);
        }

    }

}