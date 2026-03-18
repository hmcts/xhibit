package uk.gov.courtservice.xhibit.business.entities.refjudgeticket;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeTicketComplexValue;

/**
 * Maintainer for Calendar reference data entity.

 * 
 * @author grewalg
 *
 */
public class RefJudgeTicketMaintainer extends ReferenceDataMaintainer {
    private RefJudgeTicketHome home = null;

    /**
     * Default constructor.
     */
    public RefJudgeTicketMaintainer() {
    }

    /**
     * Find the entity using the supplied judge id.
     * 
     * @param judgeId
     *            judgeId to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findJudgeTicketsByJudgeId(Integer judgeId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findJudgeTicketsByJudgeId");
            return this.getHome().findJudgeTicketsByJudgeId(judgeId);
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
    public RefJudgeTicket findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
	 * Deletes a RefJudgeTicket by marking it as Obsolete.
	 * 
	 * @param refJudgeTicketBV
	 * @param userDisplayName
	 */
	public void deleteRefJudgeTicket(RefJudgeTicketBasicValue refJudgeTicketBV, String userDisplayName) {
		log.debug(ENTER_METHOD + "deleteRefJudgeTicket");
		try {
			RefJudgeTicket refJudgeTicket = this.getHome().findByPrimaryKey(refJudgeTicketBV.getId());

			// check version
			if (refJudgeTicket.getVersion() == null || refJudgeTicketBV.getVersion() == null
					|| !refJudgeTicket.getVersion().equals(refJudgeTicketBV.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				refJudgeTicket.setObsInd("Y");
				refJudgeTicket.setLastUpdatedBy(userDisplayName);
			}
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}
	
	/**
	 * Inserts RefJudgeTickets
	 * 
	 * @param judgeTickets
	 * @param userDisplayName
	 */
	public void createRefJudgeTickets(List<RefJudgeTicketBasicValue> judgeTickets, String userDisplayName)
			throws CreateException {
		log.debug(ENTER_METHOD + "createRefJudgeTickets");
		for (RefJudgeTicketBasicValue value : judgeTickets) {
			RefJudgeTicket refJudgeTicket = this.getHome().create(value.getJudgeId(), value.getTicketType(),
					userDisplayName, "N", value.getCourtId());
			Integer refJudgeTicketId = refJudgeTicket.getRefJudgeTicketId();
			log.debug("Created RefJudgeTicket with id - " + refJudgeTicketId);
		}
	}
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefJudgeTicket
     * @return RefJudgeTicketBasicValue
     */
    public RefJudgeTicketBasicValue getBasicValue(RefJudgeTicket local) {
        log.debug(ENTER_METHOD + "getBasicValue");

        RefJudgeTicketBasicValue value = new RefJudgeTicketBasicValue(local.getRefJudgeTicketId(), local.getVersion());
        this.loadValue(value, local);
        log.debug(EXIT_METHOD + "getBasicValue");
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefJudgeTicket
     * @return RefJudgeTicketComplexValue
     */
    public RefJudgeTicketComplexValue getComplexValue(RefJudgeTicket local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefJudgeTicketComplexValue value = new RefJudgeTicketComplexValue(local.getRefJudgeTicketId(), local.getVersion());
        this.loadValue(value, local);
        log.debug(EXIT_METHOD + "getComplexValue");
        return value;
    }
    
    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<RefJudgeTicketBasicValue>
     */
    public Collection<RefJudgeTicketBasicValue> getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(false, locals);
    }

    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<RefJudgeTicketComplexValue>
     */
    public Collection<RefJudgeTicketComplexValue> getComplexValues(Collection locals) {
        log.debug("Entered: getComplexValues"); 
        return getCollectionValues(true, locals);
    }

    /**
     * The RefJudge Home.
     * 
     * @return RefJudgeHome
     */
    public RefJudgeTicketHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefJudgeHome");
            this.home = (RefJudgeTicketHome) CSServices.getServiceLocator().getLocalHome(RefJudgeTicketHome.class);
        }
        return this.home;
    }

	@SuppressWarnings("unchecked")
	private <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        RefJudgeTicket element = null;
        while (it.hasNext()) {
        	element = (RefJudgeTicket) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }
	
    private void loadValue(RefJudgeTicketBasicValue value, RefJudgeTicket local) {
    	value.setJudgeId(local.getJudgeId());
    	value.setTicketType(local.getTicketType());
    	value.setObsInd(local.getObsInd());
        value.setCourtId(local.getCourtId());
        value.setVersion(local.getVersion());

    }
}