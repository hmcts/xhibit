package uk.gov.courtservice.framework.services;

/**
 * <p>Title: ClasspathEntityResolver</p>
 * <p>Description: This class enables us to have the dtd referenced in the mapping.xml
 * file to be located on the classpath relative location</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EDS</p>
 * @author Faisal Shoukat
 * @version 1.0
 */

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class ClasspathEntityResolver implements EntityResolver {
    private static final ClasspathEntityResolver THIS = new ClasspathEntityResolver();

    private Map dtdMap;

    /**
     * constructor made private to enforce singleton
     */
    private ClasspathEntityResolver() {
        dtdMap = new HashMap();
        registerDefaultReferences();
    }

    /**
     * register the defaults dtd references here for key use the public id and
     * for the value use the classpath relative location
     */
    private void registerDefaultReferences() {
    }

    /**
     * register a new entity reference for key use the public id and for the
     * value use the classpath relative location
     */
    public void registerReference(String publicId, String location) {
        dtdMap.put(publicId, location);
    }

    /**
     * implement resolve entity method from super class
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        String dtdURL = null;
        if (publicId != null) {
            // find the registered dtd url
            dtdURL = (String) dtdMap.get(publicId);
        }
        if (dtdURL == null) {
            return null;
        }
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream(dtdURL);

        return (new InputSource(stream));
    }

    public static ClasspathEntityResolver getInstance() {
        return THIS;
    }

}
