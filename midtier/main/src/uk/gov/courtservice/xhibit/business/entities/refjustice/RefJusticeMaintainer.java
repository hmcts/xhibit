package uk.gov.courtservice.xhibit.business.entities.refjustice;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeComplexValue;

/**
 * Maintainer for Judge reference data type.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.8 $
 */
public class RefJusticeMaintainer extends ReferenceDataMaintainer {
    private RefJusticeHome home = null;

    /**
     * Find the entity using the supplied primary key
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     */
    public RefJustice findByPrimaryKey(Integer id) throws ObjectNotFoundException {
        log.debug(ENTER_METHOD + "findByPrimaryKey(" + id + ")");
        try {
            return this.getHome().findByPrimaryKey(id);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, RefJusticeMaintainer.class);
            throw new EJBException(anException);
        }
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefJustice
     * @return RefJusticeBasicValue
     */
    public RefJusticeBasicValue getBasicValue(RefJustice local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefJusticeBasicValue value = new RefJusticeBasicValue(local.getRefJusticeId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefHearingType
     * @return RefHearingTypeComplexValue
     */
    public RefJusticeComplexValue getComplexValue(RefJustice local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefJusticeComplexValue value = new RefJusticeComplexValue(local.getRefJusticeId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    public RefJusticeHome getHome() {
        if (this.home == null) {
            log.debug("getHome: lazy initialise RefJusticeHome");
            this.home = (RefJusticeHome) CSServices.getServiceLocator().getLocalHome(RefJusticeHome.class);
        }
        return this.home;
    }

    /**
     * Load the given value object with the data from the local reference.
     * 
     * @param RefJusticeBasicValue
     *            value
     * @param RefJustice
     *            local
     */
    private void loadValue(RefJusticeBasicValue value, RefJustice local) {
        value.setCourtID(local.getCourtId());
        value.setCrestJusticeId(local.getCrestJusticeId());
        value.setInitials(local.getInitials());
        value.setJusticeName(local.getJusticeName());
        value.setPsdCourtCode(local.getPsdCourtCode());
        value.setTitle(local.getTitle());

        /** @todo complete once attributes on RefJustice are ratified */
    }
}