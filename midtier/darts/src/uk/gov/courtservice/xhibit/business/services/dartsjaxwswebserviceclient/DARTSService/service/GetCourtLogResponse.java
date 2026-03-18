
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for getCourtLogResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="getCourtLogResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://response.dfs.moj.synapps.com/}GetCourtLogResponse" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getCourtLogResponse", propOrder = {
    "_return"
})
public class GetCourtLogResponse {

    @XmlElement(name = "return")
    protected uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.GetCourtLogResponse _return;

    /**
     * Gets the value of the return property.
     * 
     * @return
     *     possible object is
     *     {@link com.synapps.moj.dfs.response.GetCourtLogResponse }
     *     
     */
    public uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.GetCourtLogResponse getReturn() {
        return _return;
    }

    /**
     * Sets the value of the return property.
     * 
     * @param value
     *     allowed object is
     *     {@link com.synapps.moj.dfs.response.GetCourtLogResponse }
     *     
     */
    public void setReturn(uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.GetCourtLogResponse value) {
        this._return = value;
    }

}
