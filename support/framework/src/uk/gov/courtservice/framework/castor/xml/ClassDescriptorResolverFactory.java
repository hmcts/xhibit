package uk.gov.courtservice.framework.castor.xml;

import org.exolab.castor.xml.ClassDescriptorResolver;
import org.exolab.castor.xml.util.ClassDescriptorResolverImpl;

/**
 * <p>
 * Title: ClassDescriptorResolverFactory
 * </p>
 * <p>
 * Description: Factory for producing ClassDescriptorResolver.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ClassDescriptorResolverFactory.java,v 1.2 2005/01/18 11:33:04
 *          tz0d5m Exp $
 */
public class ClassDescriptorResolverFactory {

    /**
     * Stop unnecessary construction of this object
     */
    private ClassDescriptorResolverFactory() {
        // Change the permisions of the default constructor
    }

    /**
     * Get a ClassDescriptorResolver createing a new one if necessary
     */
    public static ClassDescriptorResolver getClassDescriptorResolver() {
        // Unsure if ClassDescriptorResolverImpl is thread safe so
        // create a new instance each time!
        return new ClassDescriptorResolverImpl();
    }
}
