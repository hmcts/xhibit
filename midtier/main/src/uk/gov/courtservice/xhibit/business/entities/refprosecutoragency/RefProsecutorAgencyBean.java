package uk.gov.courtservice.xhibit.business.entities.refprosecutoragency;

import java.util.Date;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class RefProsecutorAgencyBean extends CSEntityBean {

	private static final long serialVersionUID = 1L;
	
	public Integer ejbCreate(Integer refProsecutorAgencyId, String title, String prosecutorName1, String prosecutorName2, String prosecutorName3, String initials, Integer addressId, String crestOpposerId, Integer courtId, String cpsCode, String dxRef, String obsInd, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
        setRefProsecutorAgencyId(refProsecutorAgencyId);
        setTitle(title);
        setProsecutorName1(prosecutorName1);
        setProsecutorName2(prosecutorName2);
        setProsecutorName3(prosecutorName3);
        setInitials(initials);
        setAddressId(addressId);
        setCrestOpposerId(crestOpposerId);
        setCourtId(courtId);
        setCpsCode(cpsCode);
        setDxRef(dxRef);
        setLastUpdateDate(lastUpdateDate);
        setCreationDate(creationDate);
        setCreatedBy(createdBy);
        setLastUpdatedBy(lastUpdatedBy);
        setObsInd(obsInd);
        setVersion(version);
        return null;
    }

    public void ejbPostCreate(Integer refProsecutorAgencyId, String title, String prosecutorName1, String prosecutorName2, String prosecutorName3, String initials, Integer addressId, String crestOpposerId, Integer courtId, String cpsCode, String dxRef, String obsInd, Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
    }

    public abstract Integer getRefProsecutorAgencyId();
	public abstract void setRefProsecutorAgencyId(Integer refProsecutorAgencyId);
	
	public abstract String getTitle();
	public abstract void setTitle(String title);
	
	public abstract String getProsecutorName1();
	public abstract void setProsecutorName1(String prosecutorName1);
	
	public abstract String getProsecutorName2();
	public abstract void setProsecutorName2(String prosecutorName2);
	
	public abstract String getProsecutorName3();
	public abstract void setProsecutorName3(String prosecutorName3);
	
	public abstract String getInitials();
	public abstract void setInitials(String initials);
	
	public abstract Integer getAddressId();
	public abstract void setAddressId(Integer addressId);
	
	public abstract String getCrestOpposerId();
	public abstract void setCrestOpposerId(String crestOpposerId);
	
	public abstract Integer getCourtId();
	public abstract void setCourtId(Integer courtId);
	
	public abstract String getCpsCode();
	public abstract void setCpsCode(String cpsCode);
	
	public abstract String getDxRef();
	public abstract void setDxRef(String dxRef);
	
	public abstract String getObsInd();
	public abstract void setObsInd(String obsInd);
	
	public abstract Date getLastUpdateDate();
	public abstract void setLastUpdateDate(Date lastUpdateDate);
	
	public abstract Date getCreationDate();
	public abstract void setCreationDate(Date creationDate);
	
	public abstract String getCreatedBy();
	public abstract void setCreatedBy(String createdBy);
	
	public abstract String getLastUpdatedBy();
	public abstract void setLastUpdatedBy(String lastUpdatedBy);
	
	public abstract Integer getVersion();
	public abstract void setVersion(Integer version);
	
    public abstract uk.gov.courtservice.xhibit.business.entities.address.Address getAddress();
    public abstract void setAddress(uk.gov.courtservice.xhibit.business.entities.address.Address address);
}
