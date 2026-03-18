package uk.gov.courtservice.xhibit.business.services.contactdetail;

import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;

/**
 * <p>
 * Title: ContactDetailControllerBean
 * </p>
 * <p>
 * Description: Session bean for manipulating contact details
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="ContactDetailController" 
 * 			 description="Contact detail Session Bean" 
 *           type="Stateless" view-type="both"
 *           jndi-name="ContactDetailControllerHome"
 *           local-jndi-name="ContactDetailLocalControllerHome"
 * @ejb.transaction type="Required"
 * 
 * 
 * @author Chris Kudzin
 * 
 * @version 2.0
 */
public class ContactDetailControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;

	private ContactDetailHelper contactDetailHelper = new ContactDetailHelper();

	/**
	 * Returns a RefSolicitorFirmComplexValue
	 * 
	 * @param Integer
	 *            addressId
	 * 
	 * @ejb.interface-method view-type="both"
	 * 
	 * @throws FinderException
	 */
	public RefSolicitorFirmComplexValue findByAddressId(Integer addressId) throws FinderException {
		RefSolicitorFirmComplexValue r = contactDetailHelper.findByAddressId(addressId);
		return r;
	}

}