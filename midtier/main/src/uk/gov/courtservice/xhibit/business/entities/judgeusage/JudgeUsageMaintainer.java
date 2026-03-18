package uk.gov.courtservice.xhibit.business.entities.judgeusage;

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
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.JudgeUsageComplexValue;


public class JudgeUsageMaintainer extends AbstractEntityMaintainer {

    private JudgeUsageHome home = null;

    public JudgeUsageMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public JudgeUsage findByPrimaryKey(final Integer key) throws ObjectNotFoundException {
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
	public Collection<JudgeUsage> findBySittingDateAndCourtRoom(final Integer courtRoomId, final Date sittingStartDate, final Date sittingEndDate) throws FinderException {
            log.debug(ENTER_METHOD + "findBySittingDateAndCourtRoom");
            final Collection<JudgeUsage>  locals = this.getHome().findBySittingDateAndCourtRoom(courtRoomId,sittingStartDate, sittingEndDate);
            return locals;
    }

    /**
     * The JudgeUsageHome Home.
     * 
     * @return JudgeUsageResultHome
     */
    public JudgeUsageHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise JudgeUsageHome");
            this.home = (JudgeUsageHome) CSServices.getServiceLocator().getLocalHome(JudgeUsageHome.class);
        }
        return this.home;
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     */
    public JudgeUsageBasicValue getBasicValue(JudgeUsage local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        JudgeUsageBasicValue value = new JudgeUsageBasicValue();
        this.loadValue(value, local);
        return value;
    }
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     */
    public JudgeUsageComplexValue getComplexValue(JudgeUsage local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        JudgeUsageComplexValue value = new JudgeUsageComplexValue();
        this.loadValue(value, local);
        return value;
    }
    

    protected void loadValue(JudgeUsageBasicValue value, JudgeUsage local) {
    	
    	value.setCourtChambersInd(local.getCourtChambersInd());
    	value.setCourtRoomId(local.getCourtRoomId());
    	value.setId(local.getJudgeUsageId());
    	value.setJudgeUsageId(local.getJudgeUsageId());
    	value.setRefJudgeId(local.getRefJudgeId());
    	value.setSittingDate(local.getSittingDate());
    	
    }
    
	public void updateJudgeUsageCourtChambersInd(final JudgeUsageComplexValue complexValue, final String userName)
			throws ObjectNotFoundException {

		final JudgeUsage local = findByPrimaryKey(complexValue.getJudgeUsageId());
		local.setCourtChambersInd(complexValue.getCourtChambersInd());
		local.setLastUpdatedBy(userName);
	}
	
	public void delete(final JudgeUsage local) {
		try {
			local.remove();
		} catch (final RemoveException r) {
			CSServices.getDefaultErrorHandler().handleError(r, getClass(), r.toString());
			throw new EJBException(r);
		}
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        if (!(value instanceof JudgeUsageBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }
		try {
			JudgeUsageBasicValue basicValue = (JudgeUsageBasicValue) value;
			
			JudgeUsage result = getHome().create(
					basicValue.getCourtRoomId(), basicValue.getCourtChambersInd(),
					basicValue.getRefJudgeId(), basicValue.getSittingDate(), userDisplayName);
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