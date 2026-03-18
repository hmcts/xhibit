package uk.gov.courtservice.xhibit.business.entities.reflistingdata;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.systemadmin.ReferenceDataMaintainer;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;

/**
 * Maintainer for listing data reference data entity.
 * 
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version $Revision: 1.0 $
 */
public class RefListingDataMaintainer extends ReferenceDataMaintainer {
    private RefListingDataHome home = null;

    /**
     * Default constructor.
     */
    public RefListingDataMaintainer() {
    }

    /**
     * Find the entity using the supplied primary key.
     * 
     * @param id
     *            Primary key to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
    public RefListingData findByPrimaryKey(Integer key) throws ObjectNotFoundException {
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
     * Find the collection using the ref data type.
     * 
     * @param string
     *            Ref Listing Data Type to use when performing the search
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection<RefListingData> findByRefDataType(String refDataType) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByRefDataType");
            return this.getHome().findByRefDataType(refDataType);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }

    /**
     * Find the collection using the ref data type and data value.
     * 
     * @param string
     *            Ref Listing Data Type to use when performing the search
     * @param string
     *            Ref Listing Data Value to use when performing the search           
     * @return The local interface of the returned collection
     * @throws ObjectNotFoundException
     */
    public Collection<RefListingData> findByRefDataTypeAndDataValue(String refDataType, String refDataValue) throws ObjectNotFoundException {
        try {
			log.debug(ENTER_METHOD + "findByRefDataTypeAndDataValue");
            return this.getHome().findByRefDataTypeAndDataValue(refDataType, refDataValue);
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        } catch (FinderException f) {
        	CSServices.getDefaultErrorHandler().handleError(f, getClass());
        	throw new EJBException(f);
        }	
    }
    
    /**
     * Find the entity using the ref data value.
     * 
     * @param string
     *            Ref Listing Data Value to use when performing the search
     * @return The local interface of the returned entity
     * @throws ObjectNotFoundException
     */
	public RefListingData findNoteTypeByDataValue(String dataValue) throws FinderException {
    	final String methodName = "findNoteTypeByDataValue(" + dataValue + ")";
        log.debug(methodName + " entered");           
        
        try {     	
			Collection<RefListingData> locals = getHome().findByRefDataTypeAndDataValue(
					RefListingDataBasicValue.DataType.NOTE_TYPE, dataValue);    
			return !locals.isEmpty() ? ((List<RefListingData>) locals).get(0) : null;
        } catch (ObjectNotFoundException e) {
            CSServices.getDefaultErrorHandler().handleError(e, getClass());
            throw new EJBException(e);
        }   
    }
	
    /**
     * Create and return a Basic Value given a local entity.
     * 
     * @param local
     *            RefListingData
     * @return RefListingDataBasicValue
     */
    public RefListingDataBasicValue getBasicValue(RefListingData local) {
        log.debug(ENTER_METHOD + "getBasicValue");
        RefListingDataBasicValue value = new RefListingDataBasicValue((Integer) local.getPrimaryKey(), local.getVersion());
        this.loadValue(value, local);
        return value;
    }

    /**
     * The RefListingData Home.
     * 
     * @return RefListingDataHome
     */
    public RefListingDataHome getHome() {
        if (this.home == null) {
            log.debug("::getHome: lazy initialise RefListingDataHome");
            this.home = (RefListingDataHome) CSServices.getServiceLocator().getLocalHome(RefListingDataHome.class);
        }
        return this.home;
    }

    protected void loadValue(RefListingDataBasicValue value, RefListingData local) {
        value.setRefListingDataId(local.getRefListingDataId());
    	value.setRefDataType(local.getRefDataType());
        value.setRefDataValue(local.getRefDataValue());
        value.setObsInd(local.getObsInd());
    }
}