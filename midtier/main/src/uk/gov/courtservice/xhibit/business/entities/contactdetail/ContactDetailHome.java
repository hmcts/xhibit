package uk.gov.courtservice.xhibit.business.entities.contactdetail;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ContactDetailHome extends javax.ejb.EJBLocalHome {
	public ContactDetail create(Integer contactId, String contactType, String contactValue, String emailFormat,
			String pagerNet, Integer addressId, java.util.Date lastUpdateDate, java.util.Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version) throws CreateException;

	public ContactDetail findByPrimaryKey(Integer contactId) throws FinderException;

	public Collection findByAddressId(java.lang.Integer addressId) throws FinderException;


}
