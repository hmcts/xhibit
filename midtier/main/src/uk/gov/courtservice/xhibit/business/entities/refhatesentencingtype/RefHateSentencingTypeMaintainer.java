package uk.gov.courtservice.xhibit.business.entities.refhatesentencingtype;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCode;
import uk.gov.courtservice.xhibit.business.entities.refsystemcode.RefSystemCodeHome;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHateSentencingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHateSentencingTypeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeComplexValue;



public class RefHateSentencingTypeMaintainer extends ReferenceDataMaintainer {
    private RefHateSentencingTypeHome home = null;

    /**
     * Default constructor.
     */
    public RefHateSentencingTypeMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefHateSentencingType findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     *            RefHateSentencingType
     * @return RefHateSentencingTypeBasicValue
     */
    public RefHateSentencingTypeBasicValue getBasicValue(RefHateSentencingType local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefHateSentencingTypeBasicValue value = new RefHateSentencingTypeBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefHateSentencingType
     * @return RefHateSentencingTypeComplexValue
     */
    public RefHateSentencingTypeComplexValue getComplexValue(RefHateSentencingType local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefHateSentencingTypeComplexValue value = new RefHateSentencingTypeComplexValue((Integer) local.getPrimaryKey(), local
                .getVersion());
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefHateSentencingType Home.
     * 
     * @return RefHateSentencingTypeHome
     */
    public RefHateSentencingTypeHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefHateSentencingTypeHome");
            this.home = (RefHateSentencingTypeHome) CSServices.getServiceLocator().getLocalHome(RefHateSentencingTypeHome.class);
        }
        return this.home;
    }

    protected void loadValue(RefHateSentencingTypeBasicValue value, RefHateSentencingType local) {

    	value.setCjsQualifier(local.getCjsQualifier());
    	value.setCourtId(local.getCourtId());
    	value.setDescription(local.getDescription());
    	value.setHateSentType(local.getHateSentType());
    	value.setObsInd(local.getObsInd());


    }
}