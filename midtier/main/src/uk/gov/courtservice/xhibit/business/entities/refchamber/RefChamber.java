package uk.gov.courtservice.xhibit.business.entities.refchamber;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.address.Address;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 */
public interface RefChamber extends CSEntityLocal {

    /*
     * - the following are now CMR fields... public Integer getAddressId();
     * public Integer getCourtId();
     */
    public Integer getCrestChamberId();

    public Integer getRefChamberId();

    public String getDxRef();

    public String getFirmName();

    public String getIsGlobal();

    public String getLocationCode();

    public String getObsInd();
    
    public String getClerkName();

    /*
     * public void setAddressId(Integer addressId); public void
     * setCourtId(Integer courtId);
     */
    public void setCrestChamberId(Integer crestChamberId);

    public void setDxRef(String dxRef);

    public void setFirmName(String firmName);

    public void setIsGlobal(String isGlobal);

    public void setLocationCode(String locationCode);

    public void setObsInd(String obsInd);

    public void setRefChamberId(Integer refChamberId);

    public Address getAddress();

    public void setAddress(Address newValue);
	
	//public abstract RefAdvocate getRefAdvocate(); 
	
	//public abstract void setRefAdvocate(RefAdvocate newValue);
	
    public Court getCourt();

    public void setCourt(Court newValue);
    
    public void setClerkName(String clerkName);

}