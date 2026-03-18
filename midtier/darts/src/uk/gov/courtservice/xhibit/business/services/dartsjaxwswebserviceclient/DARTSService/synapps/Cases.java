
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cases complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="cases">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="case" type="{http://response.dfs.moj.synapps.com/}Case" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="courthouse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="courtroom" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="Y" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="M" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="D" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "cases", propOrder = {
    "_case"
})
public class Cases {

    @XmlElement(name = "case")
    protected List<Case> _case;
    @XmlAttribute(name = "courthouse")
    protected String courthouse;
    @XmlAttribute(name = "courtroom")
    protected String courtroom;
    @XmlAttribute(name = "Y")
    protected String y;
    @XmlAttribute(name = "M")
    protected String m;
    @XmlAttribute(name = "D")
    protected String d;

    /**
     * Gets the value of the case property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the case property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCase().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Case }
     * 
     * 
     */
    public List<Case> getCase() {
        if (_case == null) {
            _case = new ArrayList<Case>();
        }
        return this._case;
    }

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
     * Gets the value of the y property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getY() {
        return y;
    }

    /**
     * Sets the value of the y property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setY(String value) {
        this.y = value;
    }

    /**
     * Gets the value of the m property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getM() {
        return m;
    }

    /**
     * Sets the value of the m property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setM(String value) {
        this.m = value;
    }

    /**
     * Gets the value of the d property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getD() {
        return d;
    }

    /**
     * Sets the value of the d property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setD(String value) {
        this.d = value;
    }

}
