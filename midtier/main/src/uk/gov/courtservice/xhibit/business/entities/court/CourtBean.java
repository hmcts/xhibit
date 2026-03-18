package uk.gov.courtservice.xhibit.business.entities.court;

// j2ee
import javax.ejb.CreateException;
import javax.ejb.EntityBean;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;
import uk.gov.courtservice.xhibit.business.entities.address.Address;

abstract public class CourtBean extends CSEntityBean implements EntityBean {

	public Integer ejbCreate(String courtType, String circuit, String courtName, String crestCourtId,
			String crestIpAddress, Address address, String courtPrefix, String shortName, String obsInd,
			String courtCode, String courtStartTime, String userDisplayName, String dxRef, String countyLocCode,
			String tier)

			throws CreateException {

		setCourtType(courtType);
		setCircuit(circuit);
		setCourtName(courtName);
		setCrestCourtId(crestCourtId);
		setCourtPrefix(courtPrefix);
		setShortName(shortName);
		setCreatedBy(userDisplayName);
		setLastUpdatedBy(userDisplayName);
		setCrestIpAddress(crestIpAddress);
		setObsInd(obsInd);
		setCourtCode(courtCode);
		setCourtStartTime(courtStartTime);
		setDxRef(dxRef);
		setCountyLocCode(countyLocCode);
		setTier(tier);
		return null;
	}

	public void ejbPostCreate(String courtType, String circuit, String courtName, String crestCourtId,
			String crestIpAddress, Address address, String courtPrefix, String shortName, String obsInd,
			String courtCode, String courtStartTime, String userDisplayName, String dxRef, String countyLocCode,
			String tier) throws CreateException {
		setAddress(address);
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract void setCourtId(Integer courtId);

	public abstract void setCourtType(String courtType);

	public abstract void setCircuit(String circuit);

	public abstract void setCourtName(String courtName);

	public abstract void setCrestCourtId(String crestCourtId);

	public abstract void setCrestIpAddress(String crestIpAddress);

	public abstract void setCourtPrefix(String courtPrefix);

	public abstract void setShortName(String shortName);

	public abstract void setDisplayName(String displayName);

	public abstract void setObsInd(String obsInd);

	public abstract void setCourtCode(String courtCode);

	public abstract void setPoliceForceCode(Integer policeForceCode);

	public abstract void setCourtStartTime(String courtStartTime);

	public abstract void setFlRepSort(String flRepSort);

	public abstract void setWlRepSort(String wlRepSort);

	public abstract void setWlRepPeriod(Integer wlRepPeriod);

	public abstract void setWlRepTime(String wlRepTime);

	public abstract void setWlFreeText(String wlFreeText);

	public abstract void setDxRef(String dxRef);

	public abstract void setCountyLocCode(String countyLocCode);

	public abstract void setTier(String tier);

	public abstract void setAddressId(Integer addressId);

	public abstract Integer getCourtId();

	public abstract String getCourtType();

	public abstract String getCircuit();

	public abstract String getCourtName();

	public abstract String getCrestCourtId();

	public abstract String getCrestIpAddress();

	public abstract String getCourtPrefix();

	public abstract String getShortName();

	public abstract String getDisplayName();

	public abstract String getObsInd();

	public abstract String getCourtCode();

	public abstract Integer getPoliceForceCode();

	public abstract String getFlRepSort();

	public abstract String getCourtStartTime();

	public abstract String getWlRepSort();

	public abstract Integer getWlRepPeriod();

	public abstract String getWlRepTime();

	public abstract String getWlFreeText();

	public abstract String getDxRef();

	public abstract String getCountyLocCode();

	public abstract String getTier();

	public abstract Integer getAddressId();

	// ------------------------------CMR
	// Fields------------------------------------
	public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();

	public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);

	public abstract java.util.Collection getCourtSites();

	public abstract void setCourtSites(java.util.Collection courtSites);

}