package uk.gov.courtservice.xhibit.business.entities.refcourtreporter;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterComplexValue;

/**
 * Maintainer for Court Reporter reference data.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.5 $
 */
public class RefCourtReporterMaintainer extends ReferenceDataMaintainer {

    private RefCourtReporterHome home = null;

    /**
     * Default constructor.
     */
    public RefCourtReporterMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param key
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefCourtReporter findByPrimaryKey(Integer key) throws ObjectNotFoundException {

        try {
            log.debug(ENTER_METHOD + "findByPrimaryKey");
            RefCourtReporter object = this.getHome().findByPrimaryKey(key);
            log.debug("findByPrimaryKey: Found it. Name = " + object.getFirstName());
            return object;
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
     *            RefCourtReporter
     * @return RefCourtReporterBasicValue
     */
    public RefCourtReporterBasicValue getBasicValue(RefCourtReporter local) {

        log.debug(ENTER_METHOD + "getBasicValue");
        RefCourtReporterBasicValue value = new RefCourtReporterBasicValue(local.getRefCourtReporterId(), local
                .getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * <p>
     * NB. The complex object is returned with null relationships.
     * </p>
     * 
     * @param local
     *            RefCourtReporter
     * @return RefCourtReporterComplexValue
     */
    public RefCourtReporterComplexValue getComplexValue(RefCourtReporter local) {

        log.debug(ENTER_METHOD + "getComplexValue");
        RefCourtReporterComplexValue value = new RefCourtReporterComplexValue(local.getRefCourtReporterId(), local
                .getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Home help ;o) No casting required.
     */
    public RefCourtReporterHome getHome() {

        if (this.home == null) {
            log.debug("lazy initialise RefCourtReporterHome");
            this.home = (RefCourtReporterHome) CSServices.getServiceLocator().getLocalHome(RefCourtReporterHome.class);
        }
        return this.home;
    }

    /**
     * Load the given value object with the data from the local reference.
     * 
     * @param RefCourtReporterBasicValue
     *            value
     * @param RefCourtReporter
     *            local
     */
    private void loadValue(RefCourtReporterBasicValue value, RefCourtReporter local) {

        value.setCourtId(local.getCourtId());
        value.setCrestCourtReporterId(local.getCrestCourtReporterId());
        value.setFirstName(local.getFirstName());
        value.setInitials(local.getInitials());
        value.setMiddleName(local.getMiddleName());
        value.setObsInd(local.getObsInd());
        value.setRefCourtReporterFirmId(local.getRefCourtReporterFirmId());
        value.setReportMethod(local.getReportMethod());
        value.setSurname(local.getSurname());
    }
}