
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCases complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCases">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="courthouse" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="courtroom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCases", propOrder = {
    "courthouse",
    "courtroom",
    "date"
})
public class GetCases {

    protected String courthouse;
    protected String courtroom;
    protected String date;

    /**
     * Gets the value of the courthouse property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourthouse() {
        return courthouse;
    }

    /**
     * Sets the value of the courthouse property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourthouse(String value) {
        this.courthouse = value;
    }

    /**
     * Gets the value of the courtroom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCourtroom() {
        return courtroom;
    }

    /**
     * Sets the value of the courtroom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCourtroom(String value) {
        this.courtroom = value;
    }

    /**
     * Gets the value of the date property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDate(String value) {
        this.date = value;
    }

}
