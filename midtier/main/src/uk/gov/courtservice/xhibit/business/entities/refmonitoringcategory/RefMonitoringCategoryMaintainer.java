package uk.gov.courtservice.xhibit.business.entities.refmonitoringcategory;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;



public class RefMonitoringCategoryMaintainer extends ReferenceDataMaintainer {
    private RefMonitoringCategoryHome home = null;

    /**
     * Default constructor.
     */
    public RefMonitoringCategoryMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefMonitoringCategory findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     *            RefMonitoringCategory
     * @return RefMonitoringCategoryBasicValue
     */
    public RefMonitoringCategoryBasicValue getBasicValue(RefMonitoringCategory local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefMonitoringCategoryBasicValue value = new RefMonitoringCategoryBasicValue();
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }


    /**
     * The RefMonitoringCategory Home.
     * 
     * @return RefMonitoringCategoryHome
     */
    public RefMonitoringCategoryHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefMonitoringCategoryHome");
            this.home = (RefMonitoringCategoryHome)CSServices.getServiceLocator().getLocalHome(RefMonitoringCategoryHome.class);
        }
        return this.home;
    }

    
    protected void loadValue(RefMonitoringCategoryBasicValue value, RefMonitoringCategory local) {    	
    	value.setRefMonitoringCategoryId(local.getRefMonitoringCategoryId());
    	value.setMonitoringCategoryCode(local.getMonitoringCategoryCode());
    	value.setMonitoringCategoryName(local.getMonitoringCategoryName());
    }
}
