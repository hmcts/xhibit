package uk.gov.courtservice.xhibit.business.services.contactdetail;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.FinderException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetail;
import uk.gov.courtservice.xhibit.business.entities.contactdetail.ContactDetailHome;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

/**
 * <p>
 * Title: ContactDetailHelper
 * </p>
 * <p>
 * Description: Provides and abstract layer between the session facade and the
 * maintainer class. It is used to construct the necessary value objects and
 * contains any business logic.
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
public class ContactDetailHelper {
	private static final Logger LOG = CSServices.getLogger(ContactDetailHelper.class);

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public ContactDetailHelper() {

	}

	public RefSolicitorFirmComplexValue findByAddressId(Integer addressId) {
		ContactDetailHome cHome = (ContactDetailHome) CSServices.getServiceLocator()
				.getLocalHome(ContactDetailHome.class);
		RefSolicitorFirmComplexValue rValue = new RefSolicitorFirmComplexValue();
		try {
			Collection contactDetails;
			contactDetails = cHome.findByAddressId(addressId);
			for (ContactDetail contactDetail : (ArrayList<ContactDetail>) contactDetails) {
				rValue.setAddressId(contactDetail.getAddressId());
				String contactType = contactDetail.getContactType();
				if (contactType != null) {
					if (contactType.equals("Fax")) {
						rValue.setFaxNumber(contactDetail.getContactValue());
					} else if (contactType.equals("Phone")) {
						rValue.setTelephoneNumber(contactDetail.getContactValue());
					} else if (contactType.equals("NonsecureEmail")) {
						rValue.setNonsecureEmailAddress(contactDetail.getContactValue());
					} else if (contactType.equals("SecureEmail")) {
						rValue.setSecureEmailAddress(contactDetail.getContactValue());
					}
				}
			}
		} catch (FinderException e) {
			e.printStackTrace();
		}

		return rValue;
	}

}