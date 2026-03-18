package uk.gov.courtservice.xhibit.business.entities.refsystemcode;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeComplexValue;

/**
 * Maintainer for SystemCode reference data entity.
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
public class RefSystemCodeMaintainer extends ReferenceDataMaintainer {
    private RefSystemCodeHome home = null;

    /**
     * Default constructor.
     */
    public RefSystemCodeMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefSystemCode findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the refSystemCode using the code type and court id.
     * 
     * @param codeType code type
     * @param courtId  parentCourtId
     * 
     * @return entity RefSystemCode 
     * @throws ObjectNotFoundException
     */
    public Collection findHOProcCodeType(String codeType, Integer courtId) throws FinderException {
        try {
            return this.getHome().findHOProcCodeType(codeType, courtId);
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
     *            RefSystemCode
     * @return RefSystemCodeBasicValue
     */
    public RefSystemCodeBasicValue getBasicValue(RefSystemCode local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefSystemCodeBasicValue value = new RefSystemCodeBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefSystemCode
     * @return RefSystemCodeComplexValue
     */
    public RefSystemCodeComplexValue getComplexValue(RefSystemCode local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefSystemCodeComplexValue value = new RefSystemCodeComplexValue((Integer) local.getPrimaryKey(), local
                .getVersion());
        /** @todo use getId() */
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefSystemCode Home.
     * 
     * @return RefSystemCodeHome
     */
    public RefSystemCodeHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefSystemCodeHome");
            this.home = (RefSystemCodeHome) CSServices.getServiceLocator().getLocalHome(RefSystemCodeHome.class);
        }
        return this.home;
    }

    protected void loadValue(RefSystemCodeBasicValue value, RefSystemCode local) {

        value.setCode(local.getCode());
        value.setCodeTitle(local.getCodeTitle());
        value.setCodeType(local.getCodeType());
        // value.setCourtId(local.getCourtId());
        value.setDecode(local.getDecode());
        value.setObsInd(local.getObsInd());
        value.setRefCodeOrder(local.getRefCodeOrder());
    }
}