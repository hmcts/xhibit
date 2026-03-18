package uk.gov.courtservice.xhibit.business.entities.legalaidorder;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidOrderBasicValue;

public class LegalAidOrderMaintainer extends AbstractEntityMaintainer {

    private LegalAidOrderHome home = null;
    protected static final String ENTER_METHOD = "Entered: ";
    protected static final String EXIT_METHOD = "Exited: ";
    
    public LegalAidOrderMaintainer() {
        getHome();
    }
    
    public LegalAidOrderBasicValue getBasicValue(LegalAidOrder order) {
    	LegalAidOrderBasicValue bv = new LegalAidOrderBasicValue(order.getLegalAidOrderId(), order.getVersion());
    	bv.setCrestLeoId(order.getCrestLeoId());
    	bv.setDateOfRevocation(order.getDateOfRevocation());
    	bv.setDefendantOnCaseId(order.getDefendantOnCaseId());
    	bv.setGrantedBy(order.getGrantedBy());
    	bv.setNumberOfAdvocates(order.getNumberOfAdvocates());
    	bv.setNumberOfQcs(order.getNumberOfQcs());
    	bv.setOrderDate(order.getOrderDate());
    	bv.setPsdRoRef(order.getPsdRoRef());
    	bv.setReasonForRevocationId(order.getReasonForRevocationId());
    	bv.setCaseProsAgencyId(order.getCaseProsAgencyId());
    	return bv;
    }
    public Collection getlegalAidValues(Collection locals) {
		if (locals == null)
			return null;
		List<LegalAidOrderBasicValue> legalValues = new ArrayList<LegalAidOrderBasicValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			legalValues.add(getBasicValue((LegalAidOrder) it.next()));
		}
		return legalValues;
	}
    
    public LegalAidOrder findByPrimaryKey(Integer key) throws ObjectNotFoundException {

        try {
            log.debug(ENTER_METHOD + "findByPrimaryKey");
            return this.getHome().findByPrimaryKey(key);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
        }
    }
    
    public Collection findByDefendantOnCaseId(Integer key) {

        try {
            log.debug(ENTER_METHOD + "findByDefendantOnCaseId"+key);
            return getlegalAidValues(this.getHome().findByDefendantOnCaseId(key));
        } catch (FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
        }
    }
    
    public Collection findAllByDefendantOnCaseId(Integer key) {

        try {
            log.debug(ENTER_METHOD + "findAllByDefendantOnCaseId"+key);
            return getlegalAidValues(this.getHome().findAllByDefendantOnCaseId(key));
        } catch (FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
        }
    }
    
    public Collection findByCaseProsAgencyId(Integer key) {

        try {
            log.debug(ENTER_METHOD + "findByCaseProsAgencyId"+key);
            return getlegalAidValues(this.getHome().findByCaseProsAgencyId(key));
        } catch (FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
        }
    }
    
    public Collection findAllByCaseProsAgencyId(Integer key) {

        try {
            log.debug(ENTER_METHOD + "findAllByCaseProsAgencyId"+key);
            return getlegalAidValues(this.getHome().findAllByCaseProsAgencyId(key));
        } catch (FinderException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, this.getClass());
            throw new EJBException(anException);
        }
    }
    
    
    public LegalAidOrderHome getHome() {

        if (this.home == null) {
            log.debug(": lazy initialise LegalAidOrderHome");
            this.home = (LegalAidOrderHome) CSServices.getServiceLocator().getLocalHome(LegalAidOrderHome.class);
        }
        return this.home;
    }
    
    
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
    	
    	log.debug(ENTER_METHOD + "create Legal aid order");
    	LegalAidOrderBasicValue val = (LegalAidOrderBasicValue)value;
    	try {
			return this.getHome().create(val.getDefendantOnCaseId(), val.getCaseProsAgencyId(), val.getOrderDate(), val.getGrantedBy(), val.getPsdRoRef(), val.getNumberOfAdvocates(), val.getNumberOfQcs(), val.getCrestLeoId(), userDisplayName);
		} catch (CreateException e) {
			  CSServices.getDefaultErrorHandler().handleError(e, getClass());
	          throw new EJBException(e);
		}    		
    }

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		 if (!(value instanceof LegalAidOrderBasicValue)) {
	            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
	     }
		 try {
			 // Get the current db values
			 LegalAidOrder order = getHome().findByPrimaryKey(value.getId());
		       
		     // Check the version
		     if (order.getVersion() == null || value.getVersion() == null || !order.getVersion().equals(value.getVersion())) {
		         throw new OptimisticLockException("Optimistic Lock Error");
		     }
		     LegalAidOrderBasicValue orderVal = (LegalAidOrderBasicValue) value;
		     order.setDateOfRevocation(orderVal.getDateOfRevocation());
		     order.setCrestLeoId(orderVal.getCrestLeoId());
		     order.setDefendantOnCaseId(orderVal.getDefendantOnCaseId());
		     order.setGrantedBy(orderVal.getGrantedBy());
		     order.setNumberOfAdvocates(orderVal.getNumberOfAdvocates());
		     order.setNumberOfQcs(orderVal.getNumberOfQcs());
		     order.setOrderDate(orderVal.getOrderDate());
		     order.setPsdRoRef(orderVal.getPsdRoRef());
		     order.setReasonForRevocationId(orderVal.getReasonForRevocationId());
		     order.setUpdated(userDisplayName);
		     order.setCaseProsAgencyId(orderVal.getCaseProsAgencyId());
		     order.setObsInd(orderVal.getObsInd());

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
        throw new java.lang.UnsupportedOperationException();
		
	}
	
	/**
	 * Set obs ind to y
	 * @param id
	 * @param version
	 * @param userDisplayName
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, Integer version, String userDisplayName) throws ObjectNotFoundException {
		try {
			LegalAidOrder order = home.findByPrimaryKey(id);
			if (!order.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				order.setObsInd("Y");
				order.setUpdated(userDisplayName);
			}
		} catch (ObjectNotFoundException ex) {
			//shouldn't be trying to delete if it doesn't exist so right to throw an exception
            CSServices.getDefaultErrorHandler().handleError(ex, getClass());
            throw ex;
        }
		catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
}
