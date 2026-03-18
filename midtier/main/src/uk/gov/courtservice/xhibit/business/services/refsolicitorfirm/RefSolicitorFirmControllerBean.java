package uk.gov.courtservice.xhibit.business.services.refsolicitorfirm;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetail;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetailMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

/**
 * <p>
 * Title: RefSolicitorFirmControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating defendant details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="RefSolicitorFirmController" description="Ref Solicitor Information from Session Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="RefSolicitorFirmControllerHome"
 *           local-jndi-name="RefSolicitorFirmControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Chris Kudzin
 * 
 * @version 2.0
 */
public class RefSolicitorFirmControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private RefSolicitorFirmMaintainer refSolicitorMaintainer = new RefSolicitorFirmMaintainer();
	private AddressMaintainer addressMaintainer = new AddressMaintainer();
	private ContactDetailMaintainer contactMaintainer = new ContactDetailMaintainer();

	
	/**
	 * Returns a RefSolicitorFirmComplexValue
	 * 
	 * @param Integer
	 *            refSolicitorFirmId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 */
	public RefSolicitorFirmComplexValue findByPK(Integer refSolicitorFirmId) {
		try {
			RefSolicitorFirmComplexValue r =  refSolicitorMaintainer.getComplexValue(refSolicitorMaintainer.findByPrimaryKey(refSolicitorFirmId));
			if(r.getAddressId()!=null) {
				r.populateFromAddress(addressMaintainer.getAddressBasicValue(addressMaintainer.findByPK(r.getAddressId())));
				Collection contactDetails = contactMaintainer.findByAddressId(r.getAddressId());
				for (ContactDetail contactDetail : (ArrayList<ContactDetail>) contactDetails) {
					String contactType = contactDetail.getContactType();
					if (contactType != null) {
						if (contactType.equals("Fax")) {
							r.setFaxNumber(contactDetail.getContactValue());
						} else if (contactType.equals("Phone")) {
							r.setTelephoneNumber(contactDetail.getContactValue());
						} else if (contactType.equals("Non Secure Email")) {
							r.setNonsecureEmailAddress(contactDetail.getContactValue());
						} else if (contactType.equals("Secure Email")) {
							r.setSecureEmailAddress(contactDetail.getContactValue());
						}
					}
				}
			}
			return r;
		} catch(FinderException e) {
			throw new EJBException(e);
		}
	}
}