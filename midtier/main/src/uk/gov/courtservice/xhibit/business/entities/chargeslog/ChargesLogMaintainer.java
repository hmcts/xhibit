package uk.gov.courtservice.xhibit.business.entities.chargeslog;

import java.util.Collection;

//EJB
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.ChargesLogBasicValue;

/**
 * Maintainer for new Charges Log table in xhibit 8.7.0.3
 * @author waltersna
 */

public class ChargesLogMaintainer extends AbstractEntityMaintainer {
    private ChargesLogHome home = null;
    private String ENTER_METHOD = "Entering method : ";
	
	/**
	 * Default constructor
	 **/
	public ChargesLogMaintainer() {
	}
	
	/**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public ChargesLog findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     *            ChargesLog
     * @return ChargesLogBasicValue
     */
    public ChargesLogBasicValue getBasicValue(ChargesLog local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        ChargesLogBasicValue value = new ChargesLogBasicValue();
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }
	
	/**
     * The ChargesLog Home.
     * 
     * @return ChargesLogHome
     */
    public ChargesLogHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise ChargesLogHome");
            this.home = (ChargesLogHome)CSServices.getServiceLocator().getLocalHome(ChargesLogHome.class);
        }
        return this.home;
    }
	
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		 ChargesLogBasicValue chargeLog = (ChargesLogBasicValue)value;
			log.debug("about to create entry in charge log for "+chargeLog.getCaseId());
			
				try {
					ChargesLog charge = this.getHome().create(chargeLog.getCaseId(), chargeLog.getSequenceNo(), chargeLog.getChargesInfo(),
							userDisplayName);
					log.debug("successfully added to charge log db");
					return charge;
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
	
	protected void loadValue(ChargesLogBasicValue value, ChargesLog local) {    	
	    	value.setCaseId(local.getCaseId());
	    	value.setChargesInfo(local.getChargesInfo());
	    	value.setSequenceNo(local.getSequenceNo());
	    	value.setObsInd(local.getObsInd());
	}  
}