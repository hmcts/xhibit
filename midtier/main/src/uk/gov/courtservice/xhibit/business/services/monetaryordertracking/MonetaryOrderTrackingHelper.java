package uk.gov.courtservice.xhibit.business.services.monetaryordertracking;

import java.util.Collection;
import java.util.Date;

import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;
import java.math.BigDecimal;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.monetaryordertracking.MonetaryOrderTracking;
import uk.gov.courtservice.xhibit.business.entities.monetaryordertracking.MonetaryOrderTrackingHome;
import uk.gov.courtservice.xhibit.business.entities.monetaryordertracking.MonetaryOrderTrackingMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.AbstractHelper;
import uk.gov.courtservice.xhibit.business.vos.entities.MonetaryOrderTrackingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.monetaryordertracking.MonetaryOrderTrackingValue;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import javax.ejb.EJBException;
/**
 * <p>
 * Title: MonetaryOrderTrackingHelper
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
 * @author Wen the Eternally Surprised
 * @version 1.0
 */
public class MonetaryOrderTrackingHelper extends AbstractHelper {
	private static final Logger log = CSServices.getLogger(MonetaryOrderTrackingHelper.class);
	private MonetaryOrderTrackingMaintainer monetaryOrderTrackingMaintainer;

	/**
	 * Default constructor that instantiate the necessary maintainers.
	 */
	public MonetaryOrderTrackingHelper() {
		monetaryOrderTrackingMaintainer = new MonetaryOrderTrackingMaintainer();
	}

	public MonetaryOrderTrackingValue create(MonetaryOrderTrackingValue monetaryOrderTrackingValue) {
		MonetaryOrderTrackingValue rc = null;
		try {
			Date acknowledgementDate = null;
			Integer caseId = null;
			Integer collectMagistratesCourtId = null;
			BigDecimal compensation = null;
			BigDecimal costs = null;
			String createdBy = null;
			Date creationDate = null;
			Integer defendantOnCaseId = null;
			BigDecimal fined = null;
			String lastUpdatedBy = null;
			Date lastUpdateDate = null;
			Integer monetaryOrderTrackingId = null;
			String obsInd = null;
			Date orderDate = null;
			Integer version = null;

			//--- Setup local variables ---
			acknowledgementDate = monetaryOrderTrackingValue.getAcknowledgementDate();
			caseId = monetaryOrderTrackingValue.getCaseId();
			collectMagistratesCourtId = monetaryOrderTrackingValue.getCollectMagistratesCourtId();
			compensation = monetaryOrderTrackingValue.getCompensation();
			costs = monetaryOrderTrackingValue.getCosts();
			createdBy = monetaryOrderTrackingValue.getCreatedBy();
			creationDate = monetaryOrderTrackingValue.getCreationDate();
			defendantOnCaseId = monetaryOrderTrackingValue.getDefendantOnCaseId();
			fined = monetaryOrderTrackingValue.getFined();
			lastUpdatedBy = monetaryOrderTrackingValue.getLastUpdatedBy();
			lastUpdateDate = monetaryOrderTrackingValue.getLastUpdateDate();
			monetaryOrderTrackingId = monetaryOrderTrackingValue.getMonetaryOrderTrackingId();
			obsInd = monetaryOrderTrackingValue.getObsInd();
			orderDate = monetaryOrderTrackingValue.getOrderDate();
			version = monetaryOrderTrackingValue.getVersion();
			//---
			MonetaryOrderTrackingHome home = (MonetaryOrderTrackingHome)CSServices.getServiceLocator()
					.getLocalHome(MonetaryOrderTrackingHome.class);
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			DefendantOnCaseMaintainer defendantMaintainer = new DefendantOnCaseMaintainer();
			
			Case caze = caseMaintainer.findByPrimaryKey(caseId);
			DefendantOnCase defOnCase = defendantMaintainer.findByPrimaryKey(defendantOnCaseId);
			MonetaryOrderTracking monetaryOrderTracking = home.create(acknowledgementDate, caze, collectMagistratesCourtId,
					compensation, costs, createdBy, creationDate, defOnCase, fined, lastUpdatedBy, lastUpdateDate,
					monetaryOrderTrackingId, obsInd, orderDate, version);
			//---
			if (null != monetaryOrderTracking) {
				rc = new MonetaryOrderTrackingValue();
				if (null != monetaryOrderTracking.getAcknowledgementDate()) {
					rc.setAcknowledgementDate(monetaryOrderTracking.getAcknowledgementDate());
				}
				if (null != monetaryOrderTrackingValue.getCaseId()) {
					rc.setCaseId(monetaryOrderTrackingValue.getCaseId());
				}
				if (null != monetaryOrderTrackingValue.getCollectMagistratesCourtId()) {
					rc.setCollectMagistratesCourtId(monetaryOrderTrackingValue.getCollectMagistratesCourtId());
				}
				if (null != monetaryOrderTrackingValue.getCompensation()) {
					rc.setCompensation(monetaryOrderTrackingValue.getCompensation());
				}
				if (null != monetaryOrderTrackingValue.getCosts()) {
					rc.setCosts(monetaryOrderTrackingValue.getCosts());
				}
				if (null != monetaryOrderTrackingValue.getCreatedBy()) {
					rc.setCreatedBy(monetaryOrderTrackingValue.getCreatedBy());
				}
				if (null != monetaryOrderTrackingValue.getCreationDate()) {
					rc.setCreationDate(monetaryOrderTrackingValue.getCreationDate());
				}
				if (null != monetaryOrderTrackingValue.getDefendantOnCaseId()) {
					rc.setDefendantOnCaseId(monetaryOrderTrackingValue.getDefendantOnCaseId());
				}
				if (null != monetaryOrderTrackingValue.getFined()) {
					rc.setFined(monetaryOrderTrackingValue.getFined());
				}
				if (null != monetaryOrderTrackingValue.getLastUpdatedBy()) {
					rc.setLastUpdatedBy(monetaryOrderTrackingValue.getLastUpdatedBy());
				}
				if (null != monetaryOrderTrackingValue.getLastUpdateDate()) {
					rc.setLastUpdateDate(monetaryOrderTrackingValue.getLastUpdateDate());
				}
				if (null != monetaryOrderTrackingValue.getMonetaryOrderTrackingId()) {
					rc.setMonetaryOrderTrackingId(monetaryOrderTrackingValue.getMonetaryOrderTrackingId());
				}
				if (null != monetaryOrderTrackingValue.getObsInd()) {
					rc.setObsInd(monetaryOrderTrackingValue.getObsInd());
				}
				if (null != monetaryOrderTrackingValue.getOrderDate()) {
					rc.setOrderDate(monetaryOrderTrackingValue.getOrderDate());
				}
			}
		}
		catch(Exception ex) {
      CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	    throw new EJBException(ex);
		}
		return rc;
	}


  /**
   * Call update method in maintainer and return true, if errors occur then it'll pass the errors back up
   */
	public boolean update(MonetaryOrderTrackingBasicValue monetaryOrderTrackingBasicValue, String userDisplayName) {
    try {
      monetaryOrderTrackingMaintainer.update(monetaryOrderTrackingBasicValue, userDisplayName);
      return true;
		}
		catch (ObjectNotFoundException ex) {
       CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	     throw new EJBException(ex);
		}
	}


	public MonetaryOrderTrackingBasicValue findByPrimaryKey(Integer monetaryOrderTrackingId) throws FinderException {
		try {
			MonetaryOrderTracking local = monetaryOrderTrackingMaintainer.findByPrimaryKey(monetaryOrderTrackingId);
			MonetaryOrderTrackingBasicValue result = monetaryOrderTrackingMaintainer.getBasicValue(local);
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}


	public Collection findByCaseId(Integer caseId, Integer courtId) throws FinderException {
		try {
			Collection result = newCollection();
			Collection locals = monetaryOrderTrackingMaintainer.findByCaseId(caseId, courtId);
			for (MonetaryOrderTracking local : (Collection<MonetaryOrderTracking>) locals) {
				MonetaryOrderTrackingBasicValue value = monetaryOrderTrackingMaintainer.getBasicValue(local);
				result.add(value);
			}
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}


	public Collection findByDefendantOnCaseId(Integer defendantOnCaseId, Integer courtId) throws FinderException {
		try {
			Collection result = newCollection();
			Collection locals = monetaryOrderTrackingMaintainer.findByDefendantOnCaseId(defendantOnCaseId, courtId);
			for (MonetaryOrderTracking local : (Collection<MonetaryOrderTracking>) locals) {
				MonetaryOrderTrackingBasicValue value = monetaryOrderTrackingMaintainer.getBasicValue(local);
				result.add(value);
			}
			return result;
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
    	}
	}

}