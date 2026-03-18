
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCourtLogResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCourtLogResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://response.dfs.moj.synapps.com/}DARTSResponse">
 *       &lt;sequence>
 *         &lt;element name="court_log" type="{http://response.dfs.moj.synapps.com/}court_log" minOccurs="0"/>
 *         &lt;element name="entries" type="{http://response.dfs.moj.synapps.com/}CourtLogEntry" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCourtLogResponse", propOrder = {
    "courtLog",
    "entries"
})
public class GetCourtLogResponse
    extends DARTSResponse
{

    @XmlElement(name = "court_log")
    protected CourtLog courtLog;
    @XmlElement(nillable = true)
    protected List<CourtLogEntry> entries;

    /**
     * Gets the value of the courtLog property.
     * 
     * @return
     *     possible object is
     *     {@link CourtLog }
     *     
     */
    public CourtLog getCourtLog() {
        return courtLog;
    }

    /**
     * Sets the value of the courtLog property.
     * 
     * @param value
     *     allowed object is
     *     {@link CourtLog }
     *     
     */
    public void setCourtLog(CourtLog value) {
        this.courtLog = value;
    }

    /**
     * Gets the value of the entries property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the entries property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEntries().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CourtLogEntry }
     * 
     * 
     */
    public List<CourtLogEntry> getEntries() {
        if (entries == null) {
            entries = new ArrayList<CourtLogEntry>();
        }
        return this.entries;
    }

}
