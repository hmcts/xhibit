package uk.gov.courtservice.xhibit.business.entities.contactdetail;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: ContactDetailMaintainer
 * </p>
 * <p>
 * Description:
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
 * 
 */

public class ContactDetailMaintainer { // extends AbstractEntityMaintainer {

	private static ContactDetailHome home = null;

	private static Logger log = CSServices.getLogger(ContactDetailMaintainer.class);

	public ContactDetailMaintainer() {
		if (home == null) {
			home = (ContactDetailHome) CSServices.getServiceLocator().getLocalHome(ContactDetailHome.class);
		}
	}
	
	/**
	 * Find contact details given address id, 
	 * @param addressId
	 * @return collection (Fax/Email/Phone)
	 */
	public Collection findByAddressId(java.lang.Integer addressId) {
		try {
			return home.findByAddressId(addressId);
		} catch(FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
		}
	}


}