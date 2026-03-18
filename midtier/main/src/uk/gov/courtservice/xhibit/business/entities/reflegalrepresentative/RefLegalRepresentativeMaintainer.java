package uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;

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
 * @version $Revision: 1.8 $
 */
public class RefLegalRepresentativeMaintainer extends ReferenceDataMaintainer {
    private RefLegalRepresentativeHome home = null;
    private String methodName;

    /**
     * Default constructor.
     */
    public RefLegalRepresentativeMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefLegalRepresentative findByPrimaryKey(Integer key) throws ObjectNotFoundException {
        try {
            log.debug(ENTER_METHOD + "findByPrimaryKey");
            RefLegalRepresentative object = this.getHome().findByPrimaryKey(key);
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
     *            RefLegalRepresentative
     * @return RefLegalRepresentativeBasicValue
     */
    public RefLegalRepresentativeBasicValue getBasicValue(RefLegalRepresentative local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefLegalRepresentativeBasicValue value = new RefLegalRepresentativeBasicValue(local.getRefLegalRepId(), local
                .getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefLegalRepresentative Home.
     * 
     * @return RefLegalRepresentativeHome
     */
    public RefLegalRepresentativeHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefLegalRepresentativeHome");
            this.home = (RefLegalRepresentativeHome) CSServices.getServiceLocator().getLocalHome(
                    RefLegalRepresentativeHome.class);
        }
        return this.home;
    }

    protected void loadValue(RefLegalRepresentativeBasicValue value, RefLegalRepresentative local) {
        value.setFirstName(local.getFirstName());
        value.setInitials(local.getInitials());
        value.setLegalRepType(local.getLegalRepType());
        value.setMiddleName(local.getMiddleName());
        value.setSurname(local.getSurname());
        value.setTitle(local.getTitle());
        value.setObsInd(local.getObsInd());
    }

    public RefLegalRepresentative createLegalRep(CSAbstractValue value, Court court, String userDisplayName) throws CreateException,
            ObjectNotFoundException {
        final String METHOD = "createLegalRep";

        if (!(value instanceof RefLegalRepresentativeBasicValue)) {
            if (log.isDebugEnabled())
                log.debug(METHOD + "Unexpected type:" + value.getClass());
            throw new IllegalArgumentException("Unexpected type:" + value.getClass());
        }

        RefLegalRepresentativeBasicValue refLegalRepBasicValue = (RefLegalRepresentativeBasicValue) value;
        RefLegalRepresentative refLegalRep = null;

        try {
            refLegalRep = this.getHome().create(refLegalRepBasicValue.getFirstName(),
                    refLegalRepBasicValue.getMiddleName(), refLegalRepBasicValue.getSurname(),
                    refLegalRepBasicValue.getTitle(), refLegalRepBasicValue.getInitials(),
                    refLegalRepBasicValue.getLegalRepType(), refLegalRepBasicValue.getObsInd(), court, userDisplayName);
        } catch (CreateException anException) {
            CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
            throw anException;
        }
        log.debug("Created legalRep with ID:" + refLegalRep.getRefLegalRepId());
        return refLegalRep;
    }

}