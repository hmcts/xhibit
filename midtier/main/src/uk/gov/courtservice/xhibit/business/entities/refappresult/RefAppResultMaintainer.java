package uk.gov.courtservice.xhibit.business.entities.refappresult;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.5 $
 */
public class RefAppResultMaintainer extends ReferenceDataMaintainer {

    private RefAppResultHome home = null;

    public RefAppResultMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefAppResult findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * The RefAppResult Home.
     * 
     * @return RefAppResultHome
     */
    public RefAppResultHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefSolicitorFirmHome");
            this.home = (RefAppResultHome) CSServices.getServiceLocator().getLocalHome(RefAppResultHome.class);
        }
        return this.home;
    }

    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefSolicitorFirm
     * @return RefSolicitorFirmBasicValue
     */
    public RefAppResultBasicValue getBasicValue(RefAppResult local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefAppResultBasicValue value = new RefAppResultBasicValue(local.getRefAppResultId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    protected void loadValue(RefAppResultBasicValue value, RefAppResult local) {
        value.setRefAppResId(local.getRefAppResultId());
        value.setCode(local.getAppResultCode());
        value.setDescription1(local.getAppResultDescr1());
        value.setDescription2(local.getAppResultDescr2());
        value.setHoCode(local.getHoCode());
        value.setVarySentence(local.getVarySentence());
        value.setLesserOffInd(local.getLesserOffInd());
        value.setObsInd(local.getObsInd());
        value.setCourtId(local.getCourtId());
    }
}