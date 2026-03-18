package uk.gov.courtservice.xhibit.business.entities.contactdetail;

import javax.ejb.CreateException;

import uk.gov.courtservice.framework.business.entities.CSEntityBean;

abstract public class ContactDetailBean extends CSEntityBean {

	public Integer ejbCreate(Integer contactId, String contactType, String contactValue, String emailFormat,
			String pagerNet, Integer addressId, java.util.Date lastUpdateDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
		setContactId(contactId);
		setContactType(contactType);
		setContactValue(contactValue);
		setEmailFormat(emailFormat);
		setPagerNet(pagerNet);
		setAddressId(addressId);
		setLastUpdateDate(lastUpdateDate);
		setCreationDate(creationDate);
		setCreatedBy(createdBy);
		setLastUpdatedBy(lastUpdatedBy);
		setVersion(version);
		return null;
	}

	public void ejbPostCreate(Integer contactId, String contactType, String contactValue, String emailFormat,
			String pagerNet, Integer addressId, java.util.Date lastUpdateDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version) throws CreateException {
	}

	// ------------------------------CMP
	// Fields------------------------------------
	public abstract Integer getContactId();

	public abstract void setContactId(Integer contactId);

	public abstract String getContactType();

	public abstract void setContactType(String contactType);

	public abstract String getContactValue();

	public abstract void setContactValue(String contactValue);

	public abstract String getEmailFormat();

	public abstract void setEmailFormat(String emailFormat);

	public abstract String getPagerNet();

	public abstract void setPagerNet(String pagerNet);

	public abstract Integer getAddressId();

	public abstract void setAddressId(Integer addressId);

	public abstract java.util.Date getLastUpdateDate();

	public abstract void setLastUpdateDate(java.util.Date lastUpdateDate);

	public abstract java.util.Date getCreationDate();

	public abstract void setCreationDate(java.util.Date creationDate);

	public abstract String getCreatedBy();

	public abstract void setCreatedBy(String createdBy);

	public abstract String getLastUpdatedBy();

	public abstract void setLastUpdatedBy(String lastUpdatedBy);

	public abstract Integer getVersion();

	public abstract void setVersion(Integer version);
}