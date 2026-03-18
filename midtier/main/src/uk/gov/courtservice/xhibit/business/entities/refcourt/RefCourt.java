package uk.gov.courtservice.xhibit.business.entities.refcourt;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.court.Court;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefCourt extends CSEntityLocal {

    public Integer getRefCourtId();

    /*
     * public Integer getCourtId();
     */
    public String getCourtFullName();

    public String getCourtShortName();

    public String getCourtType();

    public String getCrestCode();

    public String getDxRef();

    public String getIsPsd();

    /** @todo Surely this should be named isPsd? */
    public String getNamePrefix();

    public String getObsInd();

    public Integer getAddressId();
    
    public Integer getCourtId();

    /* public String getXhbVersion(); */

    public void setCourtFullName(String courtFullName);

    public void setCourtShortName(String courtShortName);

    public void setCourtType(String courtType);

    public void setCrestCode(String crestCode);

    /* public void setCourtId(Integer courtId); */
    public void setDxRef(String dxRef);

    public void setIsPsd(String isPsd);

    public void setNamePrefix(String namePrefix);

    public void setObsInd(String obsInd);

    /*
     * public void setXhbVersion(String xhbVersion);
     * 
     * public Integer getAddressId();
     */
    public void setAddressId(Integer addressId);
    
    public void setCourtId(Integer courtId);

    public Court getCourt();

    public void setCourt(Court court);

    public Address getAddress();

    public void setAddress(Address address);

}