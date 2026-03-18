
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for court_log complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="court_log">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="entry" type="{http://response.dfs.moj.synapps.com/}CourtLogEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="courthouse" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="case_number" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "court_log", propOrder = {
    "entry"
})
public class CourtLog {

    protected List<CourtLogEntry> entry;
    @XmlAttribute(name = "courthouse")
    protected String courthouse;
    @XmlAttribute(name = "case_number")
    protected String caseNumber;

    /**
     * Gets the value of the entry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourtLogEntry }
     * 
     * 
     */
    public List<CourtLogEntry> getEntry() {
        if (entry == null) {
            entry = new ArrayList<CourtLogEntry>();
        }
        return this.entry;
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
     * Gets the value of the caseNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCaseNumber() {
        return caseNumber;
    }

    /**
     * Sets the value of the caseNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCaseNumber(String value) {
        this.caseNumber = value;
    }

}
