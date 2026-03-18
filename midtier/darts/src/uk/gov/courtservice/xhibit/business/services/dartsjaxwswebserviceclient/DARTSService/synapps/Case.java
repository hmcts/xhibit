
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Case complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Case">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="case_number" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="scheduled_start" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="upload_priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defendants" type="{http://response.dfs.moj.synapps.com/}defendants" minOccurs="0"/>
 *         &lt;element name="judges" type="{http://response.dfs.moj.synapps.com/}judges" minOccurs="0"/>
 *         &lt;element name="prosecutors" type="{http://response.dfs.moj.synapps.com/}prosecutors" minOccurs="0"/>
 *         &lt;element name="defenders" type="{http://response.dfs.moj.synapps.com/}defenders" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Case", propOrder = {
    "caseNumber",
    "scheduledStart",
    "uploadPriority",
    "defendants",
    "judges",
    "prosecutors",
    "defenders"
})
public class Case {

    @XmlElement(name = "case_number")
    protected String caseNumber;
    @XmlElement(name = "scheduled_start")
    protected String scheduledStart;
    @XmlElement(name = "upload_priority")
    protected String uploadPriority;
    protected Defendants defendants;
    protected Judges judges;
    protected Prosecutors prosecutors;
    protected Defenders defenders;

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

    /**
     * Gets the value of the scheduledStart property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduledStart() {
        return scheduledStart;
    }

    /**
     * Sets the value of the scheduledStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduledStart(String value) {
        this.scheduledStart = value;
    }

    /**
     * Gets the value of the uploadPriority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadPriority() {
        return uploadPriority;
    }

    /**
     * Sets the value of the uploadPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadPriority(String value) {
        this.uploadPriority = value;
    }

    /**
     * Gets the value of the defendants property.
     * 
     * @return
     *     possible object is
     *     {@link Defendants }
     *     
     */
    public Defendants getDefendants() {
        return defendants;
    }

    /**
     * Sets the value of the defendants property.
     * 
     * @param value
     *     allowed object is
     *     {@link Defendants }
     *     
     */
    public void setDefendants(Defendants value) {
        this.defendants = value;
    }

    /**
     * Gets the value of the judges property.
     * 
     * @return
     *     possible object is
     *     {@link Judges }
     *     
     */
    public Judges getJudges() {
        return judges;
    }

    /**
     * Sets the value of the judges property.
     * 
     * @param value
     *     allowed object is
     *     {@link Judges }
     *     
     */
    public void setJudges(Judges value) {
        this.judges = value;
    }

    /**
     * Gets the value of the prosecutors property.
     * 
     * @return
     *     possible object is
     *     {@link Prosecutors }
     *     
     */
    public Prosecutors getProsecutors() {
        return prosecutors;
    }

    /**
     * Sets the value of the prosecutors property.
     * 
     * @param value
     *     allowed object is
     *     {@link Prosecutors }
     *     
     */
    public void setProsecutors(Prosecutors value) {
        this.prosecutors = value;
    }

    /**
     * Gets the value of the defenders property.
     * 
     * @return
     *     possible object is
     *     {@link Defenders }
     *     
     */
    public Defenders getDefenders() {
        return defenders;
    }

    /**
     * Sets the value of the defenders property.
     * 
     * @param value
     *     allowed object is
     *     {@link Defenders }
     *     
     */
    public void setDefenders(Defenders value) {
        this.defenders = value;
    }

}
