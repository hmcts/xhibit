package uk.gov.courtservice.xhibit.business.entities.refhearingtype;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeComplexValue;

/**
 * Maintainer for Reference Hearing Type.
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
public class RefHearingTypeMaintainer extends ReferenceDataMaintainer {

    private RefHearingTypeHome home = null;

    /**
     * Default constructor.
     */
    public RefHearingTypeMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefHearingType findByPrimaryKey(Integer key) throws ObjectNotFoundException {

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
     * Find the entities using the courtId and category
     * 
     * @param Integer
     *            Id of the court
     * @param String 
     *            Category           
     * @return The local interface of the returned entities
     * @throws FinderException
     */
    public Collection findByCourtIdAndCategory(Integer courtId, String category) throws FinderException {
    	try {
            log.debug(ENTER_METHOD + "findByCourtIdAndCategory");
            return this.getHome().findByCourtIdAndCategory(courtId, category);
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
     *            RefHearingType
     * @return RefHearingTypeBasicValue
     */
    public RefHearingTypeBasicValue getBasicValue(RefHearingType local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefHearingTypeBasicValue value = new RefHearingTypeBasicValue(local.getRefHearingTypeId(), local.getVersion());
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
     *            RefHearingType
     * @return RefHearingTypeComplexValue
     */
    public RefHearingTypeComplexValue getComplexValue(RefHearingType local) {
        log.debug(ENTER_METHOD + "getComplexValue");
        RefHearingTypeComplexValue value = new RefHearingTypeComplexValue(local.getRefHearingTypeId(), local
                .getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<RefHearingTypeBasicValue>
     */
    public Collection<RefHearingTypeBasicValue> getBasicValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(false, locals);
    }

    /**
     * Create and return a Collection given a local entity.
     * 
     * @param locals
     *            Collection
     * @return Collection<RefHearingTypeComplexValue>
     */
    public Collection<RefHearingTypeComplexValue> getComplexValues(Collection locals) {
        log.debug("Entered: getBasicValues"); 
        return getCollectionValues(true, locals);
    }

	@SuppressWarnings("unchecked")
	private <T> Collection<T> getCollectionValues(boolean complexValue, Collection locals) {
        log.debug("Entered: getCollectionValues");
        if (locals == null)
            return null;

        Collection<T> values = new ArrayList<T>();
        Iterator it = locals.iterator();
        T rowValue = null;
        RefHearingType element = null;
        while (it.hasNext()) {
        	element = (RefHearingType) it.next();
        	if (complexValue) {
        		rowValue = (T) getComplexValue(element);
        	} else {
        		rowValue = (T) getBasicValue(element);	
        	}        	
        	values.add(rowValue);
        }
        return values;
    }
    /**
     * Home help ;o) No casting required - now public.
     */
    public RefHearingTypeHome getHome() {
        if (this.home == null) {
            log.debug("lazy initialise RefHearingTypeHome");
            this.home = (RefHearingTypeHome) CSServices.getServiceLocator().getLocalHome(RefHearingTypeHome.class);
        }
        return this.home;
    }

    /**
     * Load the given value object with the data from the local reference.
     * 
     * @param RefHearingTypeBasicValue
     *            value
     * @param RefHearingType
     *            local
     */
    private void loadValue(RefHearingTypeBasicValue value, RefHearingType local) {
        value.setCategory(local.getCategory());
        value.setHearingTypeCode(local.getHearingTypeCode());
        value.setHearingTypeDesc(local.getHearingTypeDesc());
        value.setListSequence(local.getListSequence());
        value.setObsInd(local.getObsInd());
        value.setSeqNo(local.getSeqNo());
    }
}