package uk.gov.courtservice.xhibit.business.entities.refoffence;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceComplexValue;

/**
 * Maintainer for Offence reference data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.6 $
 */
public class RefOffenceMaintainer extends ReferenceDataMaintainer {
    private RefOffenceHome home = null;

    /**
     * Default constructor.
     */
    public RefOffenceMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefOffence findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     *            RefOffence
     * @return RefOffenceBasicValue
     */
    public RefOffenceBasicValue getBasicValue(RefOffence local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefOffenceBasicValue value = new RefOffenceBasicValue(local.getRefOffenceId(), local.getVersion());
        /** @todo getRefOffenceId() should be getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefOffence
     * @return RefOffenceComplexValue
     */
    public RefOffenceComplexValue getComplexValue(RefOffence local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefOffenceComplexValue value = new RefOffenceComplexValue(local.getRefOffenceId(), local.getVersion());
        /** @todo getRefOffenceId() should be getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefOffence Home.
     * 
     * @return RefOffenceHome
     */
    public RefOffenceHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefOffenceHome");
            this.home = (RefOffenceHome) CSServices.getServiceLocator().getLocalHome(RefOffenceHome.class);
        }
        return this.home;
    }

    protected void loadValue(RefOffenceBasicValue value, RefOffence local) {
        value.setActSection(local.getActSection());
        value.setCourtId(local.getCourtId());
        value.setDvlcCode(local.getDvlcCode());
        value.setHoClass(local.getHoClass());
        value.setHoProcType(local.getHoProcType());
        value.setHoSubClass(local.getHoSubclass());
        value.setObsInd(local.getObsInd());
        value.setOffenceClass(local.getOffenceClass());
        value.setOffenceCode(local.getOffenceCode());
        value.setOffenceDesc(local.getOffenceDesc());
        value.setOffenceDesc2(local.getOffenceDesc2());
        value.setOffenceGroup(local.getOffenceGroup());
        // value.setOffenceType(local.getOffenceType());
        value.setStatute(local.getStatute());
    }
}