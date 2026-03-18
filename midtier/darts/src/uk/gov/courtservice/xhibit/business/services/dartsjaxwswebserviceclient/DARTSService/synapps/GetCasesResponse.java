
package uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetCasesResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetCasesResponse">
 *   &lt;complexContent>
 *     &lt;extension base="{http://response.dfs.moj.synapps.com/}DARTSResponse">
 *       &lt;sequence>
 *         &lt;element name="cases" type="{http://response.dfs.moj.synapps.com/}cases" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetCasesResponse", propOrder = {
    "cases"
})
public class GetCasesResponse
    extends DARTSResponse
{

    protected Cases cases;

    /**
     * Gets the value of the cases property.
     * 
     * @return
     *     possible object is
     *     {@link Cases }
     *     
     */
    public Cases getCases() {
        return cases;
    }

    /**
     * Sets the value of the cases property.
     * 
     * @param value
     *     allowed object is
     *     {@link Cases }
     *     
     */
    public void setCases(Cases value) {
        this.cases = value;
    }

}
