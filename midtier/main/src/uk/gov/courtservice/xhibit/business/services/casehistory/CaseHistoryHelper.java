package uk.gov.courtservice.xhibit.business.services.casehistory;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.casehistory.CaseHistory;
import uk.gov.courtservice.xhibit.business.entities.casehistory.CaseHistoryHome;
import uk.gov.courtservice.xhibit.business.entities.casehistory.CaseHistoryMaintainer;
import uk.gov.courtservice.xhibit.business.vos.services.casehistory.CaseHistoryValue;

/**
 * <p>
 * Title: DeleteCaseHelper
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
public class CaseHistoryHelper {
	private static final Logger log = CSServices.getLogger(CaseHistoryHelper.class);

	private CaseHistoryMaintainer caseHistoryMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public CaseHistoryHelper() {
		caseHistoryMaintainer = new CaseHistoryMaintainer();

	}

	public CaseHistoryValue findByCaseHistoryId(Integer caseHistoryId) throws FinderException {
		CaseHistoryHome cHHome = (CaseHistoryHome) CSServices.getServiceLocator().getLocalHome(CaseHistoryHome.class);
		CaseHistory ch = cHHome.findByPrimaryKey(caseHistoryId);
		CaseHistoryValue chV = caseHistoryMaintainer.returnCaseHistoryValue(ch);
		return chV;
	}
	
	public CaseHistoryValue findByCaseNumberCaseTypeAndCourtId(String caseType, String caseNumber, Integer courtId) throws FinderException {
		log.debug("In Method : findByCaseNumberCaseTypeAndCourtId");
		try {
			CaseHistoryValue chV = caseHistoryMaintainer.findByCaseNumberCaseTypeAndCourtId(caseType, caseNumber, courtId);
			log.debug("Exiting method : findByCaseNumberCaseTypeAndCourtId");
			return chV;
		} catch(ObjectNotFoundException e) {
			return null;
		} 
	}

}