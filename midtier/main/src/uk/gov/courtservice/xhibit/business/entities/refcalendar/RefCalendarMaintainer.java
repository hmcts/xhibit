package uk.gov.courtservice.xhibit.business.entities.refcalendar;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarComplexValue;

/**
 * Maintainer for Calendar reference data entity.

 * 
 * @author grewalg
 *
 */
public class RefCalendarMaintainer extends ReferenceDataMaintainer {
    private RefCalendarHome home = null;

    /**
     * Default constructor.
     */
    public RefCalendarMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByCourtIdAndCalDate(Integer courtId, Date fromDate, Date toDate) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findByCourtIdAndCalDate");
            return this.getHome().findByCourtIdAndCalDate(courtId, fromDate, toDate);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    public Collection findByCalDateAndCourtId(Date calDate, Integer courtId) throws ObjectNotFoundException {

        try {
            log.debug(ENTER_METHOD + "findByCalDateAndCourtId(" + calDate + courtId + ")");
            return this.getHome().findByCalDateAndCourtId(calDate, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefCalendar findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findByPrimaryKey");
            return this.getHome().findByPrimaryKey(key);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefCalendar
     * @return RefCalendarBasicValue
     */
    public RefCalendarBasicValue getRefCalendarBasicValue(RefCalendar local) {
        log.debug(ENTER_METHOD + "getBasicValue");

        RefCalendarBasicValue value = new RefCalendarBasicValue(local.getRefCalendarId(), local.getVersion());
        this.loadValue(value, local);
        log.debug(EXIT_METHOD + "getRefCalendarBasicValue");
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefCalendar
     * @return RefCalendarComplexValue
     */
    public RefCalendarComplexValue getRefCalendarComplexValue(RefCalendar local) {
        log.debug(ENTER_METHOD + "getRefCalendarComplexValue");
        RefCalendarComplexValue value = new RefCalendarComplexValue(local.getRefCalendarId(), local.getVersion());
        this.loadValue(value, local);
        log.debug(EXIT_METHOD + "getRefCalendarComplexValue");
        return value;
    }

    /**
     * The RefCalendar Home.
     * 
     * @return RefCalendarHome
     */
    public RefCalendarHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefCalendarHome");
            this.home = (RefCalendarHome) CSServices.getServiceLocator().getLocalHome(RefCalendarHome.class);
        }
        return this.home;
    }

    private void loadValue(RefCalendarBasicValue value, RefCalendar local) {
    	value.setAvail(local.getAvail());
    	value.setCalDate(local.getCalDate());
        value.setCourtId(local.getCourtId());
        value.setDescription(local.getDescription());
        value.setSysAcAvail(local.getSysAcAvail());
    }
    
    public void updateRefCalendar(List<RefCalendarBasicValue> updateList, String userDisplayName) {
		try {
			for (RefCalendarBasicValue rcbv : updateList) {
				RefCalendar refCalendar = getHome().findByPrimaryKey(rcbv.getId());
				
				// check version
	            if (refCalendar.getVersion() == null || rcbv.getVersion() == null || !refCalendar.getVersion().equals(rcbv.getVersion())) {
	                throw new OptimisticLockException("Optimistic Lock Error");
	            }
	            
				refCalendar.setAvail(rcbv.getAvail());
				refCalendar.setSysAcAvail(rcbv.getSysAcAvail());
				refCalendar.setDescription(rcbv.getDescription());
				refCalendar.setUpdated(userDisplayName);
			}
			log.debug("Number of RefCalendar records updated - " + updateList.size());
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw new EJBException(ex);
        }
    }
}