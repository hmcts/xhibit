
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.datamodel.core.content;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.emc.documentum.fs.datamodel.core.content package. 
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

    private final static QName _ContentRenditionType_QNAME = new QName("http://content.core.datamodel.fs.documentum.emc.com/", "renditionType");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.emc.documentum.fs.datamodel.core.content
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataHandlerContent }
     * 
     */
    public DataHandlerContent createDataHandlerContent() {
        return new DataHandlerContent();
    }

    /**
     * Create an instance of {@link BinaryContent }
     * 
     */
    public BinaryContent createBinaryContent() {
        return new BinaryContent();
    }

    /**
     * Create an instance of {@link ActivityInfo }
     * 
     */
    public ActivityInfo createActivityInfo() {
        return new ActivityInfo();
    }

    /**
     * Create an instance of {@link UcfContent }
     * 
     */
    public UcfContent createUcfContent() {
        return new UcfContent();
    }

    /**
     * Create an instance of {@link UrlContent }
     * 
     */
    public UrlContent createUrlContent() {
        return new UrlContent();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RenditionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://content.core.datamodel.fs.documentum.emc.com/", name = "renditionType", scope = Content.class)
    public JAXBElement<RenditionType> createContentRenditionType(RenditionType value) {
        return new JAXBElement<RenditionType>(_ContentRenditionType_QNAME, RenditionType.class, Content.class, value);
    }

}
