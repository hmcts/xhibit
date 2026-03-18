package uk.gov.courtservice.xhibit.business.entities.contactdetail;

import javax.ejb.EJBLocalObject;

public interface ContactDetail extends EJBLocalObject {

	public Integer getContactId();

	public void setContactId(Integer contactId);

	public String getContactType();

	public void setContactType(String contactType);

	public String getContactValue();

	public void setContactValue(String contactValue);

	public String getEmailFormat();

	public void setEmailFormat(String emailFormat);

	public String getPagerNet();

	public void setPagerNet(String pagerNet);

	public Integer getAddressId();

	public void setAddressId(Integer addressId);

	public java.util.Date getLastUpdateDate();

	public void setLastUpdateDate(java.util.Date lastUpdateDate);

	public java.util.Date getCreationDate();

	public void setCreationDate(java.util.Date creationDate);

	public String getCreatedBy();

	public void setCreatedBy(String createdBy);

	public String getLastUpdatedBy();

	public void setLastUpdatedBy(String lastUpdatedBy);

	public Integer getVersion();

	public void setVersion(Integer version);
}