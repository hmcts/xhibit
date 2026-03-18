package uk.gov.courtservice.framework.services.xml;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * @author pznwc5
 * 
 * Entity resolver based on system id
 */
public class SystemIdEntityResolver implements EntityResolver {

    /**
     * The method loads resourec from the classpath using the system is
     * 
     * @param publicId
     *            Public id
     * @param systemId
     *            System id
     */
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(systemId);
    }

}
