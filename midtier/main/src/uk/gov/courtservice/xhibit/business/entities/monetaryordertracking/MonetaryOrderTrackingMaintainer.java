package uk.gov.courtservice.xhibit.business.entities.monetaryordertracking;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.listing.CaseOnList;
import uk.gov.courtservice.xhibit.business.entities.listing.List;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseOnListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.ListBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.MonetaryOrderTrackingBasicValue;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.defendantoncase.DefendantOnCaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;


public class MonetaryOrderTrackingMaintainer extends AbstractEntityMaintainer {
    private MonetaryOrderTrackingHome home = null;

    private static Logger log = CSServices.getLogger(MonetaryOrderTrackingHome.class);

    public MonetaryOrderTrackingMaintainer() {
        if (home == null) {
            home = (MonetaryOrderTrackingHome)CSServices.getServiceLocator().getLocalHome(MonetaryOrderTrackingHome.class);
        }
    }

    public MonetaryOrderTrackingBasicValue getMonetaryOrderTrackingBasicValue(MonetaryOrderTracking local) {
    	MonetaryOrderTrackingBasicValue value = new MonetaryOrderTrackingBasicValue(local.getCaseId(), local.getVersion());
        //setMonetaryOrderTrackingBasicValue(value, local);
        return value;
    }


    @Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		// TODO Auto-generated method stub
		return null;
	}


    @Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		if (!(value instanceof MonetaryOrderTrackingBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			//--- Get the current DB values ---
			MonetaryOrderTracking local = getHome().findByPrimaryKey(value.getId());
			//--- Check the version ---
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			MonetaryOrderTrackingBasicValue basicValue = (MonetaryOrderTrackingBasicValue)value;
			//--- Update the record ---
			local.setAcknowledgementDate(basicValue.getAcknowledgementDate());
			local.setCollectMagistratesCourtId(basicValue.getCollectMagistratesCourtId());
			local.setCompensation(basicValue.getCompensation());
			local.setCosts(basicValue.getCosts());
			CaseMaintainer caseMaintainer = new CaseMaintainer();
			DefendantOnCaseMaintainer defendantMaintainer = new DefendantOnCaseMaintainer();
			
			Case caze = caseMaintainer.findByPrimaryKey(basicValue.getCaseId());
			DefendantOnCase defOnCase = defendantMaintainer.findByPrimaryKey(basicValue.getDefendantOnCaseId());
			
			local.setCaze(caze);
			local.setDefendantOnCases(defOnCase);
			local.setFined(basicValue.getFined());
			local.setLastUpdatedBy(userDisplayName);
			local.setObsInd(basicValue.getObsInd());
			local.setOrderDate(basicValue.getOrderDate());

		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		} 	
	}


    @Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		
	}

    public MonetaryOrderTrackingHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise MonetaryOrderTrackingHome");
            this.home = (MonetaryOrderTrackingHome)CSServices.getServiceLocator().getLocalHome(MonetaryOrderTrackingHome.class);
        }
        return this.home;
    }


	/**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public MonetaryOrderTracking findByPrimaryKey(Integer pk) throws ObjectNotFoundException {
        try {
            return this.getHome().findByPrimaryKey(pk);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            throw new EJBException(ex);
        }
    }


    public Collection findByCaseId(Integer monetaryOrderTrackingId, Integer courtId) throws ObjectNotFoundException {
        try {
            return this.getHome().findByCaseId(monetaryOrderTrackingId, courtId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            throw new EJBException(ex);
        }
    }


	public Collection findByDefendantOnCaseId(Integer monetaryOrderTrackingId, Integer courtId) throws ObjectNotFoundException {
        try {
            return this.getHome().findByCaseId(monetaryOrderTrackingId, courtId);
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            throw new EJBException(ex);
        }
    }


	public MonetaryOrderTrackingBasicValue getBasicValue(MonetaryOrderTracking local) {
		MonetaryOrderTrackingBasicValue value = new MonetaryOrderTrackingBasicValue((Integer) local.getPrimaryKey(),
				local.getVersion());
		this.loadValue(value, local);
		return value;
	}

	
	protected void loadValue(MonetaryOrderTrackingBasicValue value, MonetaryOrderTracking local) {
		value.setAcknowledgementDate(local.getAcknowledgementDate());
		value.setCaseId(local.getCaseId());
		value.setCollectMagistratesCourtId(local.getCollectMagistratesCourtId());
		value.setCompensation(local.getCompensation());
		value.setCosts(local.getCosts());
		value.setCreatedBy(local.getCreatedBy());
		value.setCreationDate(local.getCreationDate());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setFined(local.getFined());
		value.setLastUpdatedBy(local.getLastUpdatedBy());
		value.setLastUpdateDate(local.getLastUpdateDate());
		value.setMonetaryOrderTrackingId(local.getMonetaryOrderTrackingId());
		value.setObsInd(local.getObsInd());
		value.setOrderDate(local.getOrderDate());
		value.setVersion(local.getVersion());
	}
	
}
