package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.query.search.RefCourtQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefCourtReporterQuery;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourt;
import uk.gov.courtservice.xhibit.business.entities.refcourt.RefCourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refcourtreporter.RefCourtReporter;
import uk.gov.courtservice.xhibit.business.entities.refcourtreporter.RefCourtReporterMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm.RefCourtReporterFirmMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;

/**
 * This [BizRef] Helper channels all Ref Court & Court Reporter related queries.
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.14 $
 */
public class RefCourtHelper extends AbstractHelper {
    protected static final String ILLEGAL_ARGUMENT_MSG_ID = "sysadmin.bisref.illegalargument";

    private RefCourtMaintainer refCourtMaintainer = null;

    private RefCourtReporterMaintainer refCourtReporterMaintainer = null;

    private RefCourtReporterFirmMaintainer refCourtReporterFirmMaintainer = null;

    private AddressMaintainer addressMaintainer = null;

    /**
     * Default constructor.
     */
    public RefCourtHelper() {
    }
    
	/**
	 * Return court details by Court Ref ID
	 * 
	 * @param Integer
	 *            courtRefId
	 * @return Collection of RefCourtBasicValue
	 * @throws BisRefControllerException
	 */
	public RefCourtBasicValue findCourtByRefId(Integer refCourtId) throws BisRefControllerException {
		final String METHOD_NAME = "::findRefCourtByRefId ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		RefCourtBasicValue refCourtBasicVals = new RefCourtBasicValue();
		try {
			RefCourt ref = this.getRefCourtMaintainer().findByRefCourtId(refCourtId);
			refCourtBasicVals = getRefCourtBasicValue(ref);
		} catch (ObjectNotFoundException anException) {
			CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
			throw new BisRefControllerException("ERR_NO", "Object with key [" + refCourtId + "] not found",
					anException);
		}

		log.debug(METHOD_NAME + METHOD_EXIT);
		return refCourtBasicVals;
	}
    
	/**
	 * This method populates RefCourtBasicValue
	 * 
	 * @param ref
	 *            the current ref from refcourt
	 * @return RefCourtValue a composite value object
	 */
	private RefCourtBasicValue getRefCourtBasicValue(RefCourt ref) {
		final String METHOD_NAME = "::getRefCourtBasicValue ";
		log.debug(METHOD_NAME + METHOD_ENTER);
		RefCourtBasicValue refCourtBasicValue = new RefCourtBasicValue(ref.getCourtFullName(), ref.getCourtShortName(),
				ref.getNamePrefix(), ref.getCourtType(), ref.getCrestCode(), ref.getObsInd(), ref.getDxRef(),
				ref.getAddressId(), ref.getRefCourtId());
		log.debug(METHOD_NAME + METHOD_EXIT);
		return refCourtBasicValue;
	}

    /**
     * If the criteria contains a PrimaryKey, find Court Reference data using
     * that, otherwise create and execute a query.
     * 
     * @param RefCourtCriteria
     *            criteria
     * @return Collection of RefCourtBasicValue
     * @throws BisRefControllerException
     */
    public Collection findCourts(RefCourtCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findRefCourts ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findCourtsByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefCourt localRef = this.getRefCourtMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefCourtComplexValue value = this.getRefCourtMaintainer().getComplexValue(localRef);
                results = this.newCollection();
                if (!criteria.isBasicRequest()) {
                    AddressBasicValue addressBasicValue = getAddressMaintainer().getAddressBasicValue(
                            localRef.getAddress());
                    value.setAddress(addressBasicValue);
                }
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw new BisRefControllerException("ERR_NO", "Object with key [" + criteria.getPrimaryKey()
                        + "] not found", anException);
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    }

    /**
     * If the criteria contains a PrimaryKey, find Court Reporters using that,
     * otherwise create and execute a query.
     * 
     * @param RefCourtReporterCriteria
     *            criteria
     * @return Collection of RefCourtReporterBasicValue
     * @throws BisRefControllerException
     */
    public Collection findCourtReporters(RefCourtReporterCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findCourtReporters ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findCourtReportersByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefCourtReporter localRef = this.getRefCourtReporterMaintainer().findByPrimaryKey(
                        criteria.getPrimaryKey());
                RefCourtReporterComplexValue value = this.getRefCourtReporterMaintainer().getComplexValue(localRef);
                results = this.newCollection();
                if (!criteria.isBasicRequest()) {
                    RefCourtReporterFirmComplexValue refCourtReporterFirmComplexValue = getRefCourtReporterFirmMaintainer()
                            .getComplexValue(localRef.getRefCourtReporterFirm());
                    if (criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS))
                        refCourtReporterFirmComplexValue.setAddress(getAddressMaintainer().getAddressBasicValue(
                                localRef.getRefCourtReporterFirm().getAddress()));
                    value.setRefCourtReporterFirm(refCourtReporterFirmComplexValue);
                }

                results.add(value);
            } catch (ObjectNotFoundException anException) {
                CSServices.getDefaultErrorHandler().handleError(anException, getClass(), anException.toString());
                throw new BisRefControllerException("ERR_NO", "Object with key [" + criteria.getPrimaryKey()
                        + "] not found", anException);
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    }

    /*---------------------------------- Private Find-by-Query Methods ------------------------------------------------*/

    /**
     * Find Court Reporters matching the given the criteria using an EJB QL
     * Query.
     * <p>
     * This ignores the primary key criterion.
     * </p>
     * 
     * @param criteria
     *            RefCourtReporterCriteria
     * @return java.util.Collection
     */
    private Collection findCourtReportersByQuery(RefCourtReporterCriteria criteria) {
    	final String METHOD_NAME = "::findCourtReportersByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        // modified to use the fast lane readers
        final Collection basicVOs = (new RefCourtReporterQuery()).search(criteria);

        // this one we always need to convert to complex VOs
        final Collection complexVOs = new ArrayList(basicVOs.size());
        final Iterator it = basicVOs.iterator();

        while (it.hasNext()) {
            // convert the basic value objects to complex ones
            final RefCourtReporterBasicValue basicVO = (RefCourtReporterBasicValue) it.next();
            final RefCourtReporterComplexValue complexVO = new RefCourtReporterComplexValue();

            // now actually copy the value object to the complex type
            copyVO(basicVO, complexVO);

            // if not a basic request, need extra details
            if (criteria.isBasicRequest() == false) {
                try {
                    final RefCourtReporterFirmComplexValue rcrfCVO = getRefCourtReporterFirmMaintainer()
                            .getComplexValue(
                                    getRefCourtReporterFirmMaintainer().findByPrimaryKey(
                                            complexVO.getRefCourtReporterFirmId()));

                    // put the address in if applicable
                    if (criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS)) {
                        rcrfCVO.setAddress(getAddressMaintainer().getAddressBasicValue(
                                getAddressMaintainer().findByPK(rcrfCVO.getAddressId())));
                    }

                    complexVO.setRefCourtReporterFirm(rcrfCVO);
                } catch (final ObjectNotFoundException e) {
                    String errMsg = "Unable to find a bean in findCourtReporters";
                    throw handleException(errMsg, e);
                }
            }

            complexVOs.add(complexVO);
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        // always return the complex type
        return complexVOs;
    }

    /**
     * Find 'Ref' Courts matching the given the criteria using an EJB QL Query.
     * <p>
     * This ignores the primary key criterion.
     * </p>
     * 
     * @param criteria
     *            RefCourtCriteria
     * @return java.util.Collection
     */
    private Collection findCourtsByQuery(RefCourtCriteria criteria) {
    	final String METHOD_NAME = "::findCourtsByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        // modified to use the fast lane readers
        final Collection basicVOs = (new RefCourtQuery()).search(criteria);

        // if we are just after a basic request, return the basicVOs
        if (criteria.isBasicRequest()) {
            return basicVOs;
        }

        try {
            // convert the basic value objects to complex ones
            final Collection complexVOs = new ArrayList(basicVOs.size());
            final Iterator it = basicVOs.iterator();

            while (it.hasNext()) {
                final RefCourtBasicValue basicVO = (RefCourtBasicValue) it.next();
                final RefCourtComplexValue complexVO = new RefCourtComplexValue();

                // now actually copy the value object to the complex type
                copyVO(basicVO, complexVO);

                // and add the address details to it
                final AddressBasicValue addressBasicValue = getAddressMaintainer().createBasicVO(
                        getAddressMaintainer().findByPK(complexVO.getAddressId()));
                complexVO.setAddress(addressBasicValue);

                complexVOs.add(complexVO);
            }
            log.debug(METHOD_NAME + METHOD_EXIT);
            return complexVOs;
        } catch (final ObjectNotFoundException e) {
            String errMsg = "Unable to find address bean for findCourtsByQuery";
            throw handleException(errMsg, e);
        }
    }
    
    public String findByPSDCTCodeAndCourtId(String psdCode, Integer courtId){
    	
    	final String METHOD_NAME = "::findByPSDCTCodeAndCourtId ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        try {
                 String magName = this.getRefCourtMaintainer().findByPSDCTCodeAndCourtId(psdCode, courtId);
                 log.debug(METHOD_NAME + METHOD_EXIT);
                 return magName;
        } catch (ObjectNotFoundException anException) {
        	log.debug("Unable to find court name for "+psdCode);
        	return "UNKNOWN";
        }
    	
    }
    
    public ArrayList<RefCourtBasicValue> findByCourtIdAndIsPSD(String isPSD, Integer courtId) {
    	final String METHOD_NAME = "::findByPSDCTCodeAndCourtId ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return this.getRefCourtMaintainer().findByCourtIdAndIsPSD(isPSD, courtId);    	
    }

    /*------------------------------------ Private Helper Methods ------------------------------------------------------*/

    private RefCourtMaintainer getRefCourtMaintainer() {

        if (this.refCourtMaintainer == null) {
            log.debug(": Lazy initialise this.refCourtMaintainer");
            this.refCourtMaintainer = new RefCourtMaintainer();
        }
        return this.refCourtMaintainer;
    }

    private RefCourtReporterMaintainer getRefCourtReporterMaintainer() {

        if (this.refCourtReporterMaintainer == null) {
            log.debug(": Lazy initialise this.refCourtReporterMaintainer");
            this.refCourtReporterMaintainer = new RefCourtReporterMaintainer();
        }
        return this.refCourtReporterMaintainer;
    }

    private RefCourtReporterFirmMaintainer getRefCourtReporterFirmMaintainer() {

        if (this.refCourtReporterFirmMaintainer == null) {
            log.debug(": Lazy initialise this.refCourtReporterFirmMaintainer");
            this.refCourtReporterFirmMaintainer = new RefCourtReporterFirmMaintainer();
        }
        return this.refCourtReporterFirmMaintainer;
    }

    private AddressMaintainer getAddressMaintainer() {

        if (addressMaintainer == null) {
            log.debug(": Lazy initialise this.addressMaintainer");
            this.addressMaintainer = new AddressMaintainer();
        }
        return this.addressMaintainer;
    }
}
