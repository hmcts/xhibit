package uk.gov.courtservice.xhibit.business.entities.refusedbroadcastcase;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.legalaidamendment.LegalAidAmendment;
import uk.gov.courtservice.xhibit.business.vos.entities.RefusedBroadcastCaseBasicValue;

/**
 *
 *Refused broadcasting maintainer.
 *
 */

public class RefusedBroadcastCaseMaintainer extends AbstractEntityMaintainer {
	
    private static RefusedBroadcastCaseHome home = null;

    private static Logger log = CSServices.getLogger(RefusedBroadcastCaseMaintainer.class);


    public RefusedBroadcastCaseMaintainer() {
        if (home == null) {
            home = (RefusedBroadcastCaseHome) CSServices.getServiceLocator().getLocalHome(
            		RefusedBroadcastCaseHome.class);
        }
    }

    public RefusedBroadcastCaseBasicValue getRefusedBroadcastCaseHomeBasicValue(RefusedBroadcastCase local) {
        String methodName = "getRefusedBroadcastCaseHomeBasicValue() - ";
        log.debug(methodName + "called");

        RefusedBroadcastCaseBasicValue bv = createBasicVO(local);

        log.debug(methodName + "exited - OK");
        return bv;
    }


    /**
     * Not Used
     * 
     * @param value
     * @return
     * @deprecated Please use the overloaded method as Case object is passed through
     */
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
        throw new UnsupportedOperationException("Use create(CSAbstractValue, Case, userDisplayName)");
    }

    public CSEntityLocal create(CSAbstractValue value, Case thisCase, String userDisplayName) {
        String methodName = "create() - ";
        log.debug(methodName + "called");

        if (!(value instanceof RefusedBroadcastCaseBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        try {
        	RefusedBroadcastCaseBasicValue bv = (RefusedBroadcastCaseBasicValue) value;
            return home.create(thisCase, bv.getTeleAppRefusedReasonId(), userDisplayName);
        } catch (CreateException c) {
            CSServices.getDefaultErrorHandler().handleError(c, getClass(), c.toString());
            throw new EJBException(c);
        }
    }

    public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
        String methodName = "update() - ";
        log.debug(methodName + "called");

        if (!(value instanceof RefusedBroadcastCaseBasicValue)) {
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        RefusedBroadcastCaseBasicValue bv = (RefusedBroadcastCaseBasicValue) value;
        try {
            // Find home...
        	RefusedBroadcastCase local = home.findByPrimaryKey(bv.getId());

            // Locking check...
            if (!local.getVersion().equals(bv.getVersion())) {
                throw new OptimisticLockException("Optimistic Lock Error");
            } else {
                local.setTeleAppRefusedReasonId(bv.getTeleAppRefusedReasonId());
                local.setUpdated(userDisplayName);
            }

            log.debug(methodName + "exited - OK");
        } catch (ObjectNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw ex;
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, getClass(), ex.toString());
            throw new EJBException(ex);
        }
    }

    public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        throw new UnsupportedOperationException("Use delete(Integer, userDisplayName)");

    }

    public RefusedBroadcastCase findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        try {
            return home.findByPrimaryKey(id);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw e;
        } catch (FinderException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);
        }
    }

    private RefusedBroadcastCaseBasicValue createBasicVO(RefusedBroadcastCase local) {
        String methodName = "createBasicVO() - ";
        log.debug(methodName + "called");

        RefusedBroadcastCaseBasicValue bv = new RefusedBroadcastCaseBasicValue(local.getRefusedBroadcastCaseId(), local
                .getVersion());

        copyEntityPropsToVO(local, bv);
        return bv;
    }


    private void copyEntityPropsToVO(RefusedBroadcastCase local,
    		RefusedBroadcastCaseBasicValue broadcastCase) {
        String methodName = "copyEntityPropsToVO() - ";
        log.debug(methodName + "called");

        broadcastCase.setCaseId(local.getCaseId());
        broadcastCase.setTeleAppRefusedReasonId(local.getTeleAppRefusedReasonId());
        
    }

    /**
     * [0297.BRD.004]
     * Either created and entry or set it to obsolete
     * @param broadcastVals
     * @param userDisplayName
     * @param caze
     */
	public void updateRefusedBroadcast(List<RefusedBroadcastCaseBasicValue> broadcastVals, String userDisplayName, Case caze) {
		try {
			Collection<RefusedBroadcastCase> caseEntries =home.findByCaseId(caze.getCaseId());
			
			for(int i=0;i<broadcastVals.size();i++) {
				RefusedBroadcastCase ref = doesExistForCase(caseEntries, broadcastVals.get(i).getTeleAppRefusedReasonId());
			
				if(!"Y".equals(broadcastVals.get(i).getObsolete())&& ref==null) {
					create(broadcastVals.get(i), caze, userDisplayName);
				} else if("Y".equals(broadcastVals.get(i).getObsolete()) && ref!=null){
					delete(ref.getRefusedBroadcastCaseId(), userDisplayName);
				}
			}
			
		} catch(FinderException e) {
			 CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
	            throw new EJBException(e);	
		}
		
		
	}

	/**
	 * Set obs ind to Y for the entry
	 * @param id
	 * @param userDisplayName
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, String userDisplayName) throws ObjectNotFoundException {
		RefusedBroadcastCase refCase;
		try {
			refCase = home.findByPrimaryKey(id);
			refCase.setObsInd("Y");
			refCase.setUpdated(userDisplayName);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}

	/**
	 * Does a database entry already exist for that telerefusedid.
	 * @param fromdb collection retreived for that case from db
	 * @param telRefusedId to match on
	 * @return true if exists else false
	 */
	private RefusedBroadcastCase doesExistForCase(Collection<RefusedBroadcastCase> fromdb, Integer telRefusedId) {
		Iterator<RefusedBroadcastCase> iter = fromdb.iterator();
		RefusedBroadcastCase doesExist = null;
		while(iter.hasNext()) {
			RefusedBroadcastCase caseToRef = iter.next();
			if(caseToRef.getTeleAppRefusedReasonId().equals(telRefusedId)) {
				doesExist= caseToRef;
				break;
			}
		}
		return doesExist;	
	}
	 
	/**
	 * Find all non obsolete entries for case id.
	 * @param caseId
	 * @return arraylist of values
	 */
	public ArrayList<RefusedBroadcastCaseBasicValue> findByCaseId(Integer caseId)  {
		try {
			String methodName = "findByCaseId() - ";
	        log.debug(methodName + "called");
	        ArrayList<RefusedBroadcastCaseBasicValue> refusedBroadcastCaseBasicValues = new ArrayList<RefusedBroadcastCaseBasicValue>();
	        Collection<RefusedBroadcastCase> ref = home.findByCaseId(caseId);
			Iterator<RefusedBroadcastCase> iter = ref.iterator();
			while(iter.hasNext()) {
				refusedBroadcastCaseBasicValues.add(createBasicVO(iter.next()));
			}
			return refusedBroadcastCaseBasicValues;

	        
		}catch(FinderException e) {
			 CSServices.getDefaultErrorHandler().handleError(e, getClass(), e.toString());
            throw new EJBException(e);	
		}
	}
}