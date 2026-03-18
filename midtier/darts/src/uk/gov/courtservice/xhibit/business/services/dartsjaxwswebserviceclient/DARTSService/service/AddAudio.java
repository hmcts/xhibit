
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.datamodel.core.DataPackage;


/**
 * <p>Java class for addAudio complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addAudio">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="document" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataPackage" type="{http://core.datamodel.fs.documentum.emc.com/}DataPackage" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addAudio", propOrder = {
    "document",
    "dataPackage"
})
public class AddAudio {

    protected String document;
    protected DataPackage dataPackage;

    /**
     * Gets the value of the document property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocument() {
        return document;
    }

    /**
     * Sets the value of the document property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocument(String value) {
        this.document = value;
    }

    /**
     * Gets the value of the dataPackage property.
     * 
     * @return
     *     possible object is
     *     {@link DataPackage }
     *     
     */
    public DataPackage getDataPackage() {
        return dataPackage;
    }

    /**
     * Sets the value of the dataPackage property.
     * 
     * @param value
     *     allowed object is
     *     {@link DataPackage }
     *     
     */
    public void setDataPackage(DataPackage value) {
        this.dataPackage = value;
    }

}
