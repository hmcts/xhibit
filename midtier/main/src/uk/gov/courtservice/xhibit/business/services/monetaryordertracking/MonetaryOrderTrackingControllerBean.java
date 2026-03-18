package uk.gov.courtservice.xhibit.business.services.monetaryordertracking;

import java.lang.String;
import java.util.Collection;

import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingHelper;
import uk.gov.courtservice.xhibit.business.services.monetaryordertracking.MonetaryOrderTrackingControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.MonetaryOrderTrackingBasicValue;

/**
 * <p>
 * Title: MonetaryOrdersControllerBean
 * </p>
 * <p>
 * Description: Session Bean providing functionality for accessing and updating
 * monetary orders
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="MonetaryOrderTrackingController" description="Monetary Order Tracking Controller Bean"
 *           type="Stateless" view-type="both"
 *           jndi-name="MonetaryOrderTrackingControllerHome"
 *           local-jndi-name="MonetaryOrderTrackingControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Wen the Eternally Surprised
 * @version 1.0
 */
public class MonetaryOrderTrackingControllerBean extends CSSessionBean implements SessionBean {
	private static final long serialVersionUID = 1L;
	private MonetaryOrderTrackingHelper monetaryOrderTrackingHelper = new MonetaryOrderTrackingHelper();

	/**
	 * Returns a MonetaryOrderTrackingValue
	 * 
	 * @param pk Integer
	 * @return MonetaryOrderTrackingValue
	 * @ejb.interface-method view-type="both"
	 * @throws MonetaryOrderTrackingControllerException
	 * @throws FinderException
	 */
	public MonetaryOrderTrackingBasicValue findByPrimaryKey(Integer pk)
			throws MonetaryOrderTrackingControllerException, FinderException {
		log.debug("MonetaryOrderTrackingControllerBean::findByPrimaryKey(" + pk + ")");
		return monetaryOrderTrackingHelper.findByPrimaryKey(pk);
	}
	

	/**
	 * Returns a Collection<MonetaryOrderTrackingValue>
	 * 
	 * @param caseId Integer
	 * @param courtId Integer
	 * @return Collection<MonetaryOrderTrackingValue>
	 * @ejb.interface-method view-type="both"
	 * @throws MonetaryOrderTrackingControllerException
	 * @throws FinderException
	 */
	public Collection findByCaseId(Integer caseId, Integer courtId)
			throws MonetaryOrderTrackingControllerException, FinderException {
		log.debug("MonetaryOrderTrackingControllerBean::findByCaseId(" + caseId.toString() + ", " + courtId.toString() + ")");
		Collection rc = monetaryOrderTrackingHelper.findByCaseId(caseId, courtId);
    	return rc;
	}


	/**
	 * Returns a Collection<MonetaryOrderTrackingValue>
	 * 
	 * @param defendantOnCaseId Integer
	 * @param courtId Integer
	 * @return Collection<MonetaryOrderTrackingValue>
	 * @ejb.interface-method view-type="both"
	 * @throws MonetaryOrderTrackingControllerException
	 * @throws FinderException
	 */
    public Collection findByDefendantOnCaseId(Integer defendantOnCaseId, Integer courtId)
    		throws MonetaryOrderTrackingControllerException, FinderException {
		log.debug("MonetaryOrderTrackingControllerBean::findByDefendantOnCaseId(" + defendantOnCaseId.toString() + ")");
    	return monetaryOrderTrackingHelper.findByDefendantOnCaseId(defendantOnCaseId, courtId);
    }


    /**
	 * Returns a boolean
	 * 
	 * @param MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue
	 * @param String userDisplayName
	 * @return Boolean: success/failure of update
	 * @ejb.interface-method view-type="both"
	 * @throws MonetaryOrderTrackingControllerException 
	 */
	public boolean update(MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue, String userDisplayName)
			throws MonetaryOrderTrackingControllerException {
		log.debug("START: update");
			return monetaryOrderTrackingHelper.update(monetaryOrderTrackingBasicValue, userDisplayName);
	}

}
