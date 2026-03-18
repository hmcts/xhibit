package uk.gov.courtservice.xhibit.business.vos.entities;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

/**
 * <p>
 * Title: ContactDetailBasicValue
 * </p>
 * <p>
 * Description: This object value is used to store a contact detail, as specified in
 * the Contact Detail table.
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class ContactDetailBasicValue extends CSAbstractValue {
	private Integer contactId;
	private String contactType;
	private String contactValue;
	private String emailFormat;
	private String pagerNet;
	private Integer addressId;
	private java.util.Date lastUpdateDate;
	private java.util.Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;

	public ContactDetailBasicValue() {
	}

	public ContactDetailBasicValue(Integer id, Integer version) {
		super(id, version);
	}

	public ContactDetailBasicValue(Integer contactId, String contactType, String contactValue, String emailFormat,
			String pagerNet, Integer addressId, java.util.Date lastUpdateDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version) {
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
	}

	public ContactDetailBasicValue(ContactDetailBasicValue otherData) {
		setContactId(otherData.getContactId());
		setContactType(otherData.getContactType());
		setContactValue(otherData.getContactValue());
		setEmailFormat(otherData.getEmailFormat());
		setPagerNet(otherData.getPagerNet());
		setAddressId(otherData.getAddressId());
		setLastUpdateDate(otherData.getLastUpdateDate());
		setCreationDate(otherData.getCreationDate());
		setCreatedBy(otherData.getCreatedBy());
		setLastUpdatedBy(otherData.getLastUpdatedBy());
		setVersion(otherData.getVersion());

	}

	public Integer getPrimaryKey() {
		return getContactId();
	}

	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getContactType() {
		return this.contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	public String getContactValue() {
		return this.contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}

	public String getEmailFormat() {
		return this.emailFormat;
	}

	public void setEmailFormat(String emailFormat) {
		this.emailFormat = emailFormat;
	}

	public String getPagerNet() {
		return this.pagerNet;
	}

	public void setPagerNet(String pagerNet) {
		this.pagerNet = pagerNet;
	}

	public Integer getAddressId() {
		return this.addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public java.util.Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(java.util.Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public java.util.Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(java.util.Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getLastUpdatedBy() {
		return this.lastUpdatedBy;
	}

	public void setLastUpdatedBy(String lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}

	public Integer getVersion() {
		return this.version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
