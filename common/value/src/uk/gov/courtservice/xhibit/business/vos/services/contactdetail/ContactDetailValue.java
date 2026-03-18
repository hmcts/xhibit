package uk.gov.courtservice.xhibit.business.vos.services.contactdetail;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ContactDetailBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefOnCaseRefSolFirmBasicValue;

/**
 * <p>
 * Title: ContactDetailBasicValue
 * </p>
 * <p>
 * Description: This value object composes 3 value objects that contain
 * updatable data from XHIBIT. The internal value obejcts is
 * ContactDetailBasicValue.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Kudzin
 * @version 1.0
 */

public class ContactDetailValue extends CSAbstractValue {

	private static final long serialVersionUID = 1L;

	private ContactDetailBasicValue cdBV;

	private Integer contactId;
	private String contactValue;
	private String contactType;
	private String emailFormat;
	private String pagerNet;
	private Integer addressId;
	private Date lastUpdateDate;
	private Date creationDate;
	private String createdBy;
	private String lastUpdatedBy;
	private Integer version;

	public ContactDetailValue() {
	}

	
	public ContactDetailValue(Integer contactId, String contactType, String contactValue, String emailFormat,
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
	
	
	public Integer getPrimaryKey() {
		return getContactId();
	}

	public Integer getContactId() {
		return this.contactId;
	}

	public void setContactId(Integer contactId) {
		this.contactId = contactId;
	}

	public String getContactValue() {
		return this.contactValue;
	}

	public void setContactValue(String contactValue) {
		this.contactValue = contactValue;
	}
	
	public String getContactType() {
		return contactType;
	}


	public void setContactType(String contactType) {
		this.contactType = contactType;
	}


	public String getEmailFormat() {
		return emailFormat;
	}


	public void setEmailFormat(String emailFormat) {
		this.emailFormat = emailFormat;
	}


	public String getPagerNet() {
		return pagerNet;
	}


	public void setPagerNet(String pagerNet) {
		this.pagerNet = pagerNet;
	}


	public Integer getAddressId() {
		return addressId;
	}


	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}


	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
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

	public ContactDetailBasicValue getContactDetailBV() {
		return cdBV;
	}

	public void setContactDetailBV(ContactDetailBasicValue cdBV) {
		this.cdBV = cdBV;
	}

}