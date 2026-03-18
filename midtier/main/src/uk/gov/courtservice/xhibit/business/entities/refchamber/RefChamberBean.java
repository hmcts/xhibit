package uk.gov.courtservice.xhibit.business.entities.refchamber;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefChamberBean extends CSEntityBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7254264052485048198L;

	public Integer ejbCreate(Integer crestChamberId, String dxRef, String firmName, String isGlobal,
            String locationCode, String obsInd, String userDisplayName, String clerkName) throws CreateException {

        setObsInd(obsInd);
        setIsGlobal(isGlobal);
        setDxRef(dxRef);
        setLocationCode(locationCode);
        setCrestChamberId(crestChamberId);
        setFirmName(firmName);
        setCreatedBy(userDisplayName);
        setLastUpdatedBy(userDisplayName);
        setClerkName(clerkName);
        return null;
    }
    
    public void ejbPostCreate(Integer crestChamberId, String dxRef, String firmName, String isGlobal,
            String locationCode, String obsInd, String userDisplayName, String clerkName) throws CreateException {
    }

    public abstract Integer getCrestChamberId();

    public abstract String getDxRef();

    public abstract String getFirmName();

    public abstract String getIsGlobal();

    public abstract String getLocationCode();

    public abstract String getObsInd();

    public abstract Integer getRefChamberId();
    
    public abstract String getClerkName();

    public abstract void setCrestChamberId(Integer crestChamberId);

    public abstract void setDxRef(String dxRef);

    public abstract void setFirmName(String firmName);

    public abstract void setIsGlobal(String isGlobal);

    public abstract void setLocationCode(String locationCode);

    public abstract void setObsInd(String obsInd);

    public abstract void setRefChamberId(Integer refChamberId);
    
    public abstract void setClerkName(String clerkName);

    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address); 
    
    public abstract uk.gov.courtservice.xhibit.business.entities.court.Court getCourt();

    public abstract void setCourt(uk.gov.courtservice.xhibit.business.entities.court.Court court);

}