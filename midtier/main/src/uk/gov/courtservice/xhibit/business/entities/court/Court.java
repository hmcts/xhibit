package uk.gov.courtservice.xhibit.business.entities.court;

import java.util.Collection;

import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.xhibit.business.entities.address.Address;

/**
 * Manually updated due to problems with the JBuilder EJB Designer.
 * 
 * @author Jem Marsh
 * @amended groenmg 02-07-18
 * CTX-1976 - default time marking and time listed correctly. Add court start time
 */
public interface Court extends CSEntityLocal {

	public Integer getCourtId();

	public String getCircuit();

	public String getCourtName();

	public String getCourtPrefix();

	public String getCourtType();

	public String getCrestCourtId();

	public String getCrestIpAddress();

	public String getShortName();

	public String getDisplayName();

	public String getObsInd();

	public String getCourtCode();	

	public Integer getPoliceForceCode();
	
	public String getFlRepSort();
    
    public String getCourtStartTime();
    
    public String getWlRepSort();
    
    public Integer getWlRepPeriod();
    
    public String getWlRepTime();
    
    public String getWlFreeText();
    
    public String getDxRef();
    
    public String getCountyLocCode();
    
    public String getTier();
    
	public Collection getCourtSites();
	
	public Address getAddress();

	public void setCircuit(String circuit);

	public void setCourtName(String courtName);

	public void setCourtPrefix(String courtPrefix);

	public void setCourtType(String courtType);

	public void setCrestCourtId(String crestCourtId);

	public void setCrestIpAddress(String crestIpAddress);

	public void setShortName(String shortName);

	public void setDisplayName(String displayName);

	public void setObsInd(String obsInd);

	public void setCourtCode(String courtCode);

	public void setPoliceForceCode(Integer policeForceCode);
	
    public void setFlRepSort(String flRepSort);
    
    public void setCourtStartTime(String courtStartTime);
    
    public void setWlRepSort(String wlRepSort); 
    
    public void setWlRepPeriod(Integer wlRepPeriod);
    
    public void setWlRepTime(String wlRepTime);
    
    public void setWlFreeText(String wlFreeText);
    
    public void setDxRef(String dxRef);
    
    public void setCountyLocCode(String countyLocCode);
    
    public void setTier(String tier);

	public void setCourtSites(Collection courtSites);

	public void setAddress(Address address);
}