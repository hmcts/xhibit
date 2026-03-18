package uk.gov.courtservice.xhibit.business.services.cpplist;

import java.util.Date;

import javax.ejb.FinderException;
import javax.ejb.SessionBean;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CppListComplexValue;

/**
 * <p>
 * Title: CppListControllerBean
 * </p>
 * <p>
 * Description: Local interface to CPP List session facade.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2019
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @ejb.bean name="CppListController" description=
 *           "CPP List Session Bean" type="Stateless" 
 *           view-type="both" jndi-name="CppListControllerHome"
 *           local-jndi-name="CppListControllerLocalHome"
 * @ejb.transaction type="Required"
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class CppListControllerBean extends CSSessionBean implements SessionBean {

	private static final long serialVersionUID = 1L;
	
	// set up the helper class
	private CppListHelper cppListHelper = new CppListHelper();

	/**
	 * <p>
	 * Returns the latest unprocessed XHB_CPP_LIST
	 * </p>
	 * 
	 * @param courtId
	 *            ID of the court
	 * @param listType
	 * 			  List Type
	 * @param listStartDate 
	 *            List Start Date           
	 * @return CppListComplexValue
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public CppListComplexValue getLatestCPPList(final Integer courtId, final String listType, final Date listStartDate) {
		String methodName = "getLatestCPPList(" + courtId + "," + listType +"," + listStartDate + ") ";
		log.debug(methodName + " : entered");
		
		CppListComplexValue result = cppListHelper.getLatestCPPList(courtId, listType, listStartDate);
		return result;
	}

	/**
	 * <p>
	 * Update the XHB_CPP_LIST record
	 * </p>
	 * 
	 * @param cppListComplexValue
	 * @param username
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateCppList(final CppListComplexValue complexValue, final String userDisplayName) throws FinderException {
		String methodName = "updateCppList() ";
		log.debug(methodName + " : entered");
		
		try {
			cppListHelper.updateCppList(complexValue, userDisplayName);
		} catch (FinderException ex) {
			this.errorHandling(methodName, ex);
			throw ex;
		}
	}
	
	/**
	 * <p>
	 * Update the XHB_CPP_LIST record (basic update only)
	 * </p>
	 * 
	 * @param cppListBasicValue
	 * @param username
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public void updateCppList(final CppListBasicValue basicValue, final String userDisplayName) throws FinderException {
		String methodName = "updateCppList() ";
		log.debug(methodName + " : entered");
		
		try {
			cppListHelper.updateCppList(basicValue, userDisplayName);
		} catch (FinderException ex) {
			this.errorHandling(methodName, ex);
			throw ex;
		}
	}
	
	
	/**
	 * 
	 * @param courtId
	 * @param listType
	 * @param listStartDate
	 * @return
	 * 
	 * @ejb.interface-method view-type="both"
	 */
	public CppListComplexValue checkForExistingCppListRecord(int courtCode, String listType, Date listStartDate, Date listEndDate) {
		String methodName = "checkForExistingCppListRecord(" + courtCode + "," + listType + "," + listStartDate +"," + listEndDate + ") ";
		log.debug(methodName + " : entered");
		
		CppListComplexValue result = cppListHelper.getCPPListByCourtAndDate(courtCode, listType, listStartDate, listEndDate);
		return result;
	}
	
	/**
	 * Errorhandling method that will be used for all catch blocks where the
	 * CounselFacilitiesControllerException is being caught. This is used since
	 * all public methods in this class are handled in the same way.
	 * 
	 * @param methodName
	 *            String
	 * @param e
	 *            Exception
	 */
	private void errorHandling(String methodName, Exception e) {
		ctx.setRollbackOnly();
		CSServices.getDefaultErrorHandler().handleError(e, getClass());
		log.debug(methodName + " : failed! Transaction Rollback");
	}


}