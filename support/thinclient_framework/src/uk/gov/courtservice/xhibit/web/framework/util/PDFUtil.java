package uk.gov.courtservice.xhibit.web.framework.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.transform.Result;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.sax.SAXResult; 

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.MimeConstants;
import org.xml.sax.InputSource;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: PDFUtil
 * </p>
 * <p>
 * Description: A bunch of utilities for creating pdfs
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003)
 */
public class PDFUtil {

    /**
     * The log4j logger
     */

    private static final org.apache.log4j.Logger log4j = CSServices.getLogger(PDFUtil.class);
    
    private static final org.apache.fop.apps.FopFactory fobFactory = getFopFactory(new File(".").toURI());

    /**
     * The date format used in the xml
     */

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

    /**
     * The date format for time used in the xml
     */

    private static final SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm");

    /**
     * Stops this class being constructed unnecessarily
     */
    private PDFUtil() {
    }

    public static byte[] getPDF(String xslResource, HashMap data) throws FrameworkException {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append(resolveObject("data", data));
        return getPDF(xslResource, sb.toString());
    }

    public static byte[] getPDF(String xslResource, String xml) throws FrameworkException {
        log4j.debug("XML String to be passed to pdf generator : \n");
        log4j.debug("-----------------------------------------------------------------\n" + xml);
        log4j.debug("-----------------------------------------------------------------\n");
        return getPDF(ResourceUtil.getResourceAsStream(xslResource), new StringReader(xml));
    }

    public static byte[] getPDF(InputStream xsl, Reader xml) throws FrameworkException {
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();

            Templates myTemplates = tFactory.newTemplates(new StreamSource(xsl));

            Transformer transformer = myTemplates.newTransformer();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            transformer.transform(new StreamSource(xml), new StreamResult(baos));

            log4j.debug("Byte array output stream after transform : " + baos.toString());

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ByteArrayOutputStream PDFbaos = new ByteArrayOutputStream();
            
            FOUserAgent agent = fobFactory.newFOUserAgent();
            Fop fop = fobFactory.newFop(MimeConstants.MIME_PDF, agent, PDFbaos);
            
            Result res = new SAXResult(fop.getDefaultHandler());
            
            transformer.transform(new StreamSource(xml), res);
                                   

            return PDFbaos.toByteArray();
        } catch (Exception e) {
            throw new FrameworkException(e);
        }
    }

    private static String resolveObject(String name, Object o) {
        StringBuffer sb = new StringBuffer();
        if (o instanceof HashMap) {
            sb.append("<" + name + ">\n");
            HashMap map = (HashMap) o;
            Iterator i = map.keySet().iterator();
            while (i.hasNext()) {
                String mapName = (String) i.next();
                sb.append(resolveObject(mapName, map.get(mapName)));
            }
            sb.append("</" + name + ">\n");
        } else if (o instanceof List) {
            List list = (List) o;
            Iterator i = list.iterator();
            while (i.hasNext()) {
                sb.append(resolveObject(name, i.next()));
            }
        } else if (o instanceof Date) {
            sb.append("<" + name + ">\n");
            sb.append(sdf.format(o) + "\n");
            sb.append("</" + name + ">\n");
            sb.append("<" + name + "time>\n");
            sb.append(sdftime.format(o) + "\n");
            sb.append("</" + name + "time>\n");
        } else if (o instanceof String) {
            sb.append("<" + name + ">\n");
            o = PrimitiveUtil.convertSpecialChars(o.toString());
            sb.append(o + "\n");
            sb.append("</" + name + ">\n");
        } else if (o == null) // put a blank entry in for the moment . . .
        {
            sb.append("<" + name + ">\n");
            sb.append("\n");
            sb.append("</" + name + ">\n");
        } else {
            log4j.debug("Unknown object type for object resolve : " + name + " object " + o.getClass().getName());
        }
        return sb.toString();
    }

    /**
     * Returns the FopFactoryBuilder with backwards compatibility
     * 
     * @param Uri uri
     * @return The new FopFactoryBuilder
     */
    private static FopFactoryBuilder getFopFactoryBuilder(final URI uri) {
		FopFactoryBuilder fopFactoryBuilder = new FopFactoryBuilder(uri);
    	// Set the Fop to backwards compatibility validation
    	fopFactoryBuilder.setStrictFOValidation(false);
    	return fopFactoryBuilder;
    }
    
    /**
     * Returns the equivalent of new FopFactory.newInstance
     * 
     * @param Uri uri
     * @return The new FopFactory
     */
    private static FopFactory getFopFactory(final URI uri) {
    	return getFopFactoryBuilder(uri).build();
    }
}
