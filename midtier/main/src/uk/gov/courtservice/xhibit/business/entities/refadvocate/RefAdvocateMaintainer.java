package uk.gov.courtservice.xhibit.business.entities.refadvocate;

import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;

/**
 * Maintainer for Advocate refeence data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.7 $
 */
public class RefAdvocateMaintainer extends ReferenceDataMaintainer {
    private RefAdvocateHome home = null;

    /**
     * Default constructor.
     */
    public RefAdvocateMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefAdvocate findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the entity using the supplied surname, initials and court ID.
     * 
     * @param surname
     *            Counsel surname
     * @param initials
     *            Counsel initials
     * @param courtId
     *            Court ID
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findCounselBySurnameInitialsCourtId(String surname, String initials, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findCounselBySurnameInitialsCourtId");
            return this.getHome().findCounselBySurnameInitialsCourtId(surname, initials, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }
    
    /**
     * Find the entity using the supplied bar number and court ID.
     * 
     * @param barNumber
     *            Bar Number
     * @param courtId
     *            Court ID
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public Collection findCounselByBarNumberCourtId(Integer barNumber, Integer courtId) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findCounselByBarNumberCourtId");
            return this.getHome().findCounselByBarNumberCourtId(barNumber, courtId);
        } catch (ObjectNotFoundException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        } catch (FinderException anException) {
            throw new EJBException(anException);
        }
    }

    /**
     * Find the entity using the refLegalRepresentativeId.
     * 
     * @param refLegalRepresentativeId
     * @return RefAdvocate
     * @throws ObjectNotFoundException
     */
    public RefAdvocate findByRefLegalRepresentativeId(Integer refLegalRepresentativeId) {
        String methodName = "findByRefLegalRepresentativeId() - ";

        log.debug(methodName + "called - refLegalRepresentativeId: " + refLegalRepresentativeId);
        try {
            RefAdvocate refAdvocate = 
                this.getHome().findByRefLegalRepresentativeId(refLegalRepresentativeId);
            log.debug(methodName + "exited - OK");
            return refAdvocate;
        } catch (FinderException e) {
            throw new EJBException(e);
        }
    }
    
    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefAdvocate
     * @return RefAdvocateBasicValue
     */
    public RefAdvocateBasicValue getBasicValue(RefAdvocate local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefAdvocateBasicValue value = new RefAdvocateBasicValue(local.getRefAdvocateId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Complex Value given a local entity.
     * 
     * @param local
     *            RefAdvocate
     * @return RefAdvocateComplexValue
     */
    public RefAdvocateComplexValue getComplexValue(RefAdvocate local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefAdvocateComplexValue value = new RefAdvocateComplexValue(local.getRefAdvocateId(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefAdvocate Home.
     * 
     * @return RefAdvocateHome
     */
    public RefAdvocateHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefAdvocateHome");
            this.home = (RefAdvocateHome) CSServices.getServiceLocator().getLocalHome(RefAdvocateHome.class);
        }
        return this.home;
    }

    private void loadValue(RefAdvocateBasicValue value, RefAdvocate local) {
        value.setAdvTypeInd(local.getAdvTypeInd());
        value.setbarNo(local.getBarNo());
        value.setCrestAdvocateId(local.getCrestAdvocateId());
        value.setCrestChamberId(local.getCrestChamberId());
        value.setHonours(local.getHonours());
        value.setIsGlobal(local.getIsGlobal());
        value.setObsInd(local.getObsInd());
        value.setVatNo(local.getVatNo());
        value.setyearOfCall(local.getYearOfCall());
        value.setLegalRepId(local.getLegalRepId());
        value.setRefChamberId(local.getRefChamberId());
        /** @todo Do this here or use the RefLegalRep Maintainer? */
        value.setFirstName(local.getRefLegalRepresentative().getFirstName());
        value.setInitials(local.getRefLegalRepresentative().getInitials());
        value.setLegalRepType(local.getRefLegalRepresentative().getLegalRepType());
        value.setMiddleName(local.getRefLegalRepresentative().getMiddleName());
        value.setSurname(local.getRefLegalRepresentative().getSurname());
        value.setTitle(local.getRefLegalRepresentative().getTitle());
    }
}