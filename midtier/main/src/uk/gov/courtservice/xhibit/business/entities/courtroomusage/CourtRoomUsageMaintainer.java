package uk.gov.courtservice.xhibit.business.entities.courtroomusage;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;
import javax.ejb.RemoveException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomUsageComplexValue;


public class CourtRoomUsageMaintainer extends AbstractEntityMaintainer {

    private CourtRoomUsageHome home = null;

    public CourtRoomUsageMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public CourtRoomUsage findByPrimaryKey(final Integer key) throws ObjectNotFoundException {
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
    
    @SuppressWarnings("unchecked")
	public Collection<CourtRoomUsage> findBySittingDateAndCourtRoom(final Integer courtRoomId, final Date sittingStartDate, final Date sittingEndDate) throws FinderException {
            log.debug(ENTER_METHOD + "findBySittingDateAndCourtRoom");
            final Collection<CourtRoomUsage>  locals = this.getHome().findBySittingDateAndCourtRoom(courtRoomId,sittingStartDate, sittingEndDate);
            return locals;
    }

    /**
     * The JudgeUsageHome Home.
     * 
     * @return JudgeUsageResultHome
     */
    public CourtRoomUsageHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise JudgeUsageHome");
            this.home = (CourtRoomUsageHome) CSServices.getServiceLocator().getLocalHome(CourtRoomUsageHome.class);
        }
        return this.home;
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     */
    public CourtRoomUsageBasicValue getBasicValue(CourtRoomUsage local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        CourtRoomUsageBasicValue value = new CourtRoomUsageBasicValue();
        this.loadValue(value, local);
        return value;
    }
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     */
    public CourtRoomUsageComplexValue getComplexValue(CourtRoomUsage local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        CourtRoomUsageComplexValue value = new CourtRoomUsageComplexValue();
        this.loadValue(value, local);
        return value;
    }
    

    protected void loadValue(CourtRoomUsageBasicValue value, CourtRoomUsage local) {
    	
    	value.setAmTimeHours(local.getAmTimeHours());
    	value.setAmTimeMins(local.getAmTimeMins());
    	value.setCourtRoomId(local.getCourtRoomId());
    	value.setPmTimeHours(local.getPmTimeHours());
    	value.setPmTimeMins(local.getPmTimeMins());
    	value.setCourtRoomUsageId((Integer) local.getPrimaryKey());
    	value.setSittingDate(local.getSittingDate());
    	value.setVersion(local.getVersion());
    	
    }
    
    public void updateCourtRoomUsageTime(final CourtRoomUsageComplexValue complexValue, final String userName) throws ObjectNotFoundException{
    	
    	CourtRoomUsage local = findByPrimaryKey(complexValue.getCourtRoomUsageId());
    	local.setAmTimeHours(complexValue.getAmTimeHours());
    	local.setAmTimeMins(complexValue.getAmTimeMins());
    	local.setPmTimeHours(complexValue.getPmTimeHours());
    	local.setPmTimeMins(complexValue.getPmTimeMins());
    	local.setLastUpdatedBy(userName);
    }
    
	public void delete(final CourtRoomUsage local) {
		try {
			local.remove();
		} catch (final RemoveException r) {
			CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
			throw new EJBException(r);
		}
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof CourtRoomUsageBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			CourtRoomUsageBasicValue basicValue = (CourtRoomUsageBasicValue) value;
			
			CourtRoomUsage result = getHome().create(
					basicValue.getCourtRoomId(), basicValue.getAmTimeHours(),
					basicValue.getAmTimeMins(), basicValue.getPmTimeHours(), basicValue.getPmTimeMins(),
					basicValue.getSittingDate(), userDisplayName);
			return result;
		} catch (CreateException e) {
		       CSServices.getDefaultErrorHandler().handleError(e, getClass());
	            throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		// TODO Auto-generated method stub
		
	}
}