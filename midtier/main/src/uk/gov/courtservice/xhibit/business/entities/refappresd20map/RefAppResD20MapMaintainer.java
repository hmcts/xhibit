package uk.gov.courtservice.xhibit.business.entities.refappresd20map;

import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResD20MapBasicValue;



public class RefAppResD20MapMaintainer extends ReferenceDataMaintainer {
    private RefAppResD20MapHome home = null;

    /**
     * Default constructor.
     */
    public RefAppResD20MapMaintainer() {
    }

    /**
     * Find the entity using the supplied appresultcode value
     * 
     * @param id
     *            App result code to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findByAppResultCode(String appResultCode) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findByAppResultCode");
            return this.getHome().findByAppResultCode(appResultCode);
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
    public RefAppResD20Map findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find all the live entities.
     * 
     * @return Collection of RefAppResD20MapBasicValue
     * @throws ObjectNotFoundException
     */
    public Collection<RefAppResD20MapBasicValue> findAll() throws ObjectNotFoundException {
    	Collection<RefAppResD20MapBasicValue> result = new ArrayList<RefAppResD20MapBasicValue>();
        try {
            log.debug(ENTER_METHOD + "findAll");
            Collection<RefAppResD20Map> locals = this.getHome().findAllMappings();
            for (RefAppResD20Map local : locals) {
            	result.add(getBasicValue(local));
            }
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
        return result;
    }


    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefAppResD20Map
     * @return RefAppResD20MapBasicValue
     */
    public RefAppResD20MapBasicValue getBasicValue(RefAppResD20Map local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefAppResD20MapBasicValue value = new RefAppResD20MapBasicValue();
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }


    /**
     * The RefAppResD20Map Home.
     * 
     * @return RefAppResD20MapHome
     */
    public RefAppResD20MapHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefAppResD20MapHome");
            this.home = (RefAppResD20MapHome)CSServices.getServiceLocator().getLocalHome(RefAppResD20MapHome.class);
        }
        return this.home;
    }

    
    protected void loadValue(RefAppResD20MapBasicValue value, RefAppResD20Map local) {    	
    	value.setRefAppResD20MapId(local.getRefAppResD20MapId());
    	value.setAppResultCode(local.getAppResultCode());
    	value.setD20Result(local.getD20Result());
    }
}
