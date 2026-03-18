package uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterFirmComplexValue;

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
public class RefCourtReporterFirmMaintainer extends ReferenceDataMaintainer {

    private RefCourtReporterFirmHome home = null;

    /**
     * Default constructor.
     */
    public RefCourtReporterFirmMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param key
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefCourtReporterFirm findByPrimaryKey(Integer key) throws ObjectNotFoundException {

        try {
            log.debug(ENTER_METHOD + "findByPrimaryKey");
            RefCourtReporterFirm object = this.getHome().findByPrimaryKey(key);
            log.debug("findByPrimaryKey: Found it. Name = " + object.getFirmName());
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
     *            RefCourtReporterFirm
     * @return RefCourtReporterFirmBasicValue
     */
    public RefCourtReporterFirmBasicValue getBasicValue(RefCourtReporterFirm local) {

        log.debug(ENTER_METHOD + "getBasicValue");
        RefCourtReporterFirmBasicValue value = new RefCourtReporterFirmBasicValue(local.getRefCourtReporterFirmId(),
                local.getVersion());
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
     *            RefCourtReporterFirm
     * @return RefCourtReporterFirmComplexValue
     */
    public RefCourtReporterFirmComplexValue getComplexValue(RefCourtReporterFirm local) {

        log.debug(ENTER_METHOD + "getComplexValue");
        RefCourtReporterFirmComplexValue value = new RefCourtReporterFirmComplexValue(
                local.getRefCourtReporterFirmId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Home help ;o) No casting required.
     */
    public RefCourtReporterFirmHome getHome() {

        if (this.home == null) {
            log.debug(": lazy initialise RefCourtReporterFirmHome");
            this.home = (RefCourtReporterFirmHome) CSServices.getServiceLocator().getLocalHome(
                    RefCourtReporterFirmHome.class);
        }
        return this.home;
    }

    /**
     * Load the given value object with the data from the local reference.
     * 
     * @param RefCourtReporterFirmBasicValue
     *            value
     * @param RefCourtReporterFirm
     *            local
     */
    private void loadValue(RefCourtReporterFirmBasicValue value, RefCourtReporterFirm local) {

        value.setAddressId(local.getAddress().getAddressId());
        value.setCourtId(local.getCourtId());
        value.setCrestCourtReporterFirmId(local.getCrestCourtReporterFirmId());
        value.setDisplayFirst(local.getDisplayFirst());
        value.setDxRef(local.getDxRef());
        value.setFirmName(local.getFirmName());
        value.setObsInd(local.getObsInd());
        value.setVatNo(local.getVatNo());
    }
}