package uk.gov.courtservice.xhibit.business.entities.solicitor;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;

/**
 * Maintainer for Legal Representative reference data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version 1.0
 */
public class SolicitorMaintainer extends ReferenceDataMaintainer {
    private SolicitorHome home = null;

    /**
     * Default constructor.
     */
    public SolicitorMaintainer() {
    }

    public Solicitor createSolicitor(SolicitorBasicValue solicitorBasicValue) throws CreateException {
        String solicitorName = solicitorBasicValue.getCrestSolicitorName();
        String isInCrest = solicitorBasicValue.getIsInCrest();
        String obsInd = solicitorBasicValue.getObsInd();
        Solicitor solicitor = null;
        try {
            solicitor = getHome().create(solicitorName, isInCrest, obsInd);
        } catch (CreateException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        }

        if (log.isDebugEnabled()) {
            log.debug("***************************************");
            log.debug("create solicitor with values: Solicitor name" + solicitor.getCrestSolicitorName()
                    + "reflegalrepid:" + solicitor.getRefLegalRepId() + "IsInCrest:" + solicitor.getIsInCrest()
                    + "ObsInd:" + solicitor.getIsInCrest() + "FirmId:" + solicitor.getFirmId() + "solicitorID:"
                    + solicitor.getSolicitorId());
        }

        return solicitor;
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Solicitor findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        final String METHOD = "findByPrimaryKey ";
        log.debug(ENTER_METHOD + METHOD + "[" + key + "]");
        try {
            Solicitor object = this.getHome().findByPrimaryKey(key);
            log.debug(METHOD + "- found Solicitor, Crest Name = " + object.getCrestSolicitorName());
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
     *            Solicitor
     * @return SolicitorBasicValue
     */
    public SolicitorBasicValue getBasicValue(Solicitor local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        SolicitorBasicValue value = new SolicitorBasicValue(local.getSolicitorId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            Solicitor
     * @return SolicitorComplexValue
     */
    public SolicitorComplexValue getComplexValue(Solicitor local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        SolicitorComplexValue value = new SolicitorComplexValue(local.getSolicitorId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * The Solicitor Home.
     * 
     * @return SolicitorHome
     */
    public SolicitorHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise SolicitorHome");
            this.home = (SolicitorHome) CSServices.getServiceLocator().getLocalHome(SolicitorHome.class);
        }
        return this.home;
    }

    protected void loadValue(SolicitorBasicValue value, Solicitor local) {
        value.setCrestSolicitorName(local.getCrestSolicitorName());
        value.setInCrest(local.getIsInCrest());
        value.setObsInd(local.getObsInd());
        value.setFirmId(local.getFirmId());
        value.setLegalRepId(local.getRefLegalRepId());
    }
}