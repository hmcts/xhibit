package uk.gov.courtservice.xhibit.business.entities.orders;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RemandReasonsHome extends EJBLocalHome {
	public RemandReasons create(Integer remandReasonsId, Integer defendantOnCaseId, Integer orderId, 
			Integer remandReasonDescriptionId, String additionalInformation,
			String obsInd, String userDisplayName) throws CreateException;

	public RemandReasons findByPrimaryKey(Integer remandReasonsId) throws FinderException;
}