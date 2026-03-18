package uk.gov.courtservice.xhibit.business.entities.legalaidamendment;


import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrder;
import uk.gov.courtservice.xhibit.business.vos.entities.LegalAidAmendmentBasicValue;



public class LegalAidAmendmentMaintainer extends AbstractEntityMaintainer {

    private LegalAidAmendmentHome home = null;
    protected static final String ENTER_METHOD = "Entered: ";
    protected static final String EXIT_METHOD = "Exited: ";
    
    public LegalAidAmendmentMaintainer() {
        getHome();
    }
    
    public LegalAidAmendmentBasicValue getBasicValue(LegalAidAmendment amendment) {
    	LegalAidAmendmentBasicValue bv = new LegalAidAmendmentBasicValue(amendment.getLegalAidAmendmentId(), amendment.getVersion());
    	bv.setAmendmentDate(amendment.getAmendmentDate());
    	bv.setAmendmentType(amendment.getAmendmentType());
    	return bv;
    }
    
    public LegalAidAmendment findByPrimaryKey(Integer key) throws ObjectNotFoundException {

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
    
    public Collection findByLegalAidOrderId(Integer legalAidOrderId) throws FinderException{
    		log.debug(ENTER_METHOD + "find by legal aid order id");
    		return this.getHome().findByLegalAidOrderId(legalAidOrderId);
 
    }
    
    public LegalAidAmendmentBasicValue latestLegalAidAmendmentByLegalAidOrderId(Integer legalAidOrderId) throws FinderException{
		log.debug(ENTER_METHOD + "find by legal aid order id");
		Collection c =this.getHome().findByLegalAidOrderId(legalAidOrderId);
		if(c!=null && c.size()>0) {
			return getBasicValue((LegalAidAmendment) c.iterator().next());
		} else {
			return null;
		}
}
    public LegalAidAmendmentHome getHome() {

        if (this.home == null) {
            log.debug(": lazy initialise LegalAidAmendmentHome");
            this.home = (LegalAidAmendmentHome) CSServices.getServiceLocator().getLocalHome(LegalAidAmendmentHome.class);
        }
        return this.home;
    }
    
    
    public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		 throw new java.lang.UnsupportedOperationException();    	
    }
	
	public void create(LegalAidAmendmentBasicValue val, LegalAidOrder order, String userDisplayName) {
	
		log.debug(ENTER_METHOD + "create Legal aid amendment");
    		try {
				this.getHome().create(val.getAmendmentDate(), 
				order, val.getAmendmentType(), userDisplayName);
			} catch (CreateException e) {
				  CSServices.getDefaultErrorHandler().handleError(e, getClass());
		          throw new EJBException(e);
			}    	
	}

	
	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		         throw new java.lang.UnsupportedOperationException();

		
	}
	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
        throw new java.lang.UnsupportedOperationException();
		
	}
	
	/**
	 * Set obs ind to Y for the amendment
	 * @param id
	 * @param version
	 * @param userDisplayName
	 * @throws ObjectNotFoundException
	 */
	public void delete(Integer id, Integer version, String userDisplayName) throws ObjectNotFoundException {
		LegalAidAmendment amendment;
		try {
			amendment = home.findByPrimaryKey(id);
			if (!amendment.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				amendment.setObsInd("Y");
				amendment.setUpdated(userDisplayName);
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
	}
}
