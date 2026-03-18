
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.rt;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.emc.documentum.fs.rt package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ServiceException_QNAME = new QName("http://rt.fs.documentum.emc.com/", "ServiceException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.emc.documentum.fs.rt
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ServiceException }
     * 
     */
    public ServiceException createServiceException() {
        return new ServiceException();
    }

    /**
     * Create an instance of {@link DfsAttributeHolder }
     * 
     */
    public DfsAttributeHolder createDfsAttributeHolder() {
        return new DfsAttributeHolder();
    }

    /**
     * Create an instance of {@link DfsExceptionHolder }
     * 
     */
    public DfsExceptionHolder createDfsExceptionHolder() {
        return new DfsExceptionHolder();
    }

    /**
     * Create an instance of {@link StackTraceHolder }
     * 
     */
    public StackTraceHolder createStackTraceHolder() {
        return new StackTraceHolder();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ServiceException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rt.fs.documentum.emc.com/", name = "ServiceException")
    public JAXBElement<ServiceException> createServiceException(ServiceException value) {
        return new JAXBElement<ServiceException>(_ServiceException_QNAME, ServiceException.class, null, value);
    }

}
