package uk.gov.courtservice.framework.testutils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

/**
 * <p>
 * Title: XMLMarshaller
 * </p>
 * <p>
 * Description: Class that provides utilities to serialise and deserialise
 * objects to XML for testing purposes.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author AW Daley
 * @version 1.0
 */

public class XMLMarshaller {

    private XMLMarshaller() {
        // Reduce Permisions
    }

    /**
     * Generates XML representation of the object. The object should have a zero
     * agrument constructor and getters/setters for each attribute.
     * 
     * @param objToMarshall
     *            object to marshall to XML.
     * @param fileName
     *            file to where the XML representing the object will be stored
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     */
    public static void marshall(Object objToMarshall, String fileName) throws IOException, ValidationException,
            MarshalException {

        Marshaller.marshal(objToMarshall, new FileWriter(fileName));

    }

    /**
     * Generates an instance of an object of the type of class specified. The
     * class should define a zero agrument constructor and getters/setters for
     * each attribute.
     * 
     * @param classToUnmarshall
     *            class of the object to instansiate from the XML.
     * @param fileName
     *            file where the XML representing the object is stored
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     */
    public static Object unMarshall(Class classToUnmarshall, String fileName) throws FileNotFoundException,
            ValidationException, MarshalException {
        Object obj;

        obj = Unmarshaller.unmarshal(classToUnmarshall, new FileReader(fileName));
        return obj;
    }

    /**
     * Generates an instance of an object of the type of class specified. The
     * class should define a zero agrument constructor and getters/setters for
     * each attribute.
     * 
     * @param classToUnmarshall
     *            class of the object to instansiate from the XML.
     * @param url
     *            url where the XML representing the object is stored
     * @throws IOException
     * @throws ValidationException
     * @throws MarshalException
     */
    public static Object unMarshall(Class classToUnmarshall, URL url) throws IOException, ValidationException,
            MarshalException {
        Object obj;

        // get the contents of the file
        URLConnection urlConnection = url.openConnection();
        urlConnection.setDoInput(true);
        InputStream urlInputStream = urlConnection.getInputStream();

        obj = Unmarshaller.unmarshal(classToUnmarshall, new InputStreamReader(urlInputStream));
        return obj;
    }
}