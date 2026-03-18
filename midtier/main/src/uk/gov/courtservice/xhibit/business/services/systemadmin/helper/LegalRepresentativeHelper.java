package uk.gov.courtservice.xhibit.business.services.systemadmin.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.ObjectNotFoundException;

import uk.gov.courtservice.xhibit.business.database.query.search.RefAdvocateQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefJudgeQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefJusticeQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefLegalRepresentativeQuery;
//import uk.gov.courtservice.xhibit.business.database.query.search.RefProsecutorAgencyQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.RefSolicitorFirmQuery;
import uk.gov.courtservice.xhibit.business.database.query.search.SolicitorQuery;
import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
import uk.gov.courtservice.xhibit.business.entities.court.Court;
import uk.gov.courtservice.xhibit.business.entities.court.CourtMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocate;
import uk.gov.courtservice.xhibit.business.entities.refadvocate.RefAdvocateMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refchamber.RefChamberMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudge;
import uk.gov.courtservice.xhibit.business.entities.refjudge.RefJudgeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicket;
import uk.gov.courtservice.xhibit.business.entities.refjudgeticket.RefJudgeTicketMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refjustice.RefJustice;
import uk.gov.courtservice.xhibit.business.entities.refjustice.RefJusticeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentative;
import uk.gov.courtservice.xhibit.business.entities.reflegalrepresentative.RefLegalRepresentativeMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgency;
import uk.gov.courtservice.xhibit.business.entities.refprosecutoragency.RefProsecutorAgencyMaintainer;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirm;
import uk.gov.courtservice.xhibit.business.entities.refsolicitorfirm.RefSolicitorFirmMaintainer;
import uk.gov.courtservice.xhibit.business.entities.solicitor.Solicitor;
import uk.gov.courtservice.xhibit.business.entities.solicitor.SolicitorMaintainer;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefChamberComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefLegalRepresentativeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefProsecutorAgencyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJusticeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefLegalRepresentativeCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefProsecutorAgencyCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;

/**
 * This [BizRef] Helper channels all Legal Representative related queries.
 * <p>
 * A Legal Representative might be an Advocate, Judge, Justice,
 * ProsecutorAgancy, Solicitor (and Firms) and, of course, Legal Representative
 * (the superclass of Advocate & Solicitor.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Jem Marsh
 * @version $Revision: 1.23 $
 */
public class LegalRepresentativeHelper extends AbstractHelper {
    private CourtMaintainer courtMaintainer = null;

    private RefAdvocateMaintainer refAdvocateMaintainer = null;

    private RefChamberMaintainer refChamberMaintainer = null;

    private RefJudgeMaintainer refJudgeMaintainer = null;
    
    private RefJudgeTicketMaintainer refJudgeTicketMaintainer = null;

    private RefJusticeMaintainer refJusticeMaintainer = null;

    private RefLegalRepresentativeMaintainer refLegalRepresentativeMaintainer = null;
    
    private RefProsecutorAgencyMaintainer refProsecutorAgencyMaintainer = null;

    private RefSolicitorFirmMaintainer refSolicitorFirmMaintainer = null;

    private SolicitorMaintainer solicitorMaintainer = null;

    private AddressMaintainer addressMaintainer = null;

    /**
     * Default constructor.
     */
    public LegalRepresentativeHelper() {
    }

    /**
     * If the criteria contains a PrimaryKey, find Advocate reference data using
     * that, otherwise create and execute a query.
     * 
     * @param RefAdvocateCriteria
     *            criteria
     * @return Collection of RefAdvocateBasicValue
     * @throws BisRefControllerException
     */
    public Collection findAdvocates(RefAdvocateCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findAdvocates ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findAdvocatesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefAdvocate localRef = this.getRefAdvocateMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefAdvocateComplexValue value = this.getRefAdvocateMaintainer().getComplexValue(localRef);
                results = this.newCollection();
                if (!criteria.isBasicRequest()) {
                    RefChamberComplexValue refChamberComplexValue = getRefChamberMaintainer().getComplexValue(
                            localRef.getRefChamber());
                    if (criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS))
                        refChamberComplexValue.setAddress(getAddressMaintainer().getAddressBasicValue(
                                localRef.getRefChamber().getAddress()));
                    value.populateFromRefChamber(refChamberComplexValue);
                }
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /**
     * If the criteria contains a PrimaryKey, find Judge reference data using
     * that, otherwise create and execute a query.
     * 
     * @param RefJudgeCriteria
     *            criteria
     * @return Collection of RefJudgeBasicValue
     * @throws BisRefControllerException
     */
    @SuppressWarnings("unchecked")
	public Collection findJudges(RefJudgeCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findAdvocates ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        try {
	        if (criteria.getPrimaryKey() == null) {
	            log.debug("- find using query using " + criteria.toString());
	            results = this.findJudgesByQuery(criteria);
	        } else {
	            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
                RefJudge localRef = this.getRefJudgeMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefJudgeBasicValue value = this.getRefJudgeMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
	        }
	        
	        if (!criteria.isBasicRequest()) {
	        	final String TICKET_DELIMITER = ", ";
	        	Collection values = this.newCollection();
	            for (Object basicValue : results) {
	            	RefJudgeComplexValue value = new RefJudgeComplexValue((RefJudgeBasicValue)basicValue);
	            	if (value.getCourtId() != null) {
	            		Court court = getCourtMaintainer().findByPrimaryKey(value.getCourtId());
	            		value.setCourt(getCourtMaintainer().getCourtBasicValue(court));
	            		
	            		Collection<RefJudgeTicket> refJudgeTickets = getRefJudgeTicketMaintainer().findJudgeTicketsByJudgeId(value.getId());
	            		value.setRefJudgeTickets(getRefJudgeTicketMaintainer().getBasicValues(refJudgeTickets));
	            		
	            		// Set the allTicketTypes
	            		List<String> ticketTypeArray = new ArrayList<String>();
	            		String allTicketTypes = "";
	            		if (refJudgeTickets != null && !refJudgeTickets.isEmpty()) {
            				for (RefJudgeTicket refJudgeTicket : refJudgeTickets) {
            					String ticketType = refJudgeTicket.getTicketType();
            					// If the ticket types not been processed yet
            		        	if (!ticketTypeArray.contains(ticketType)) {
            		        		//Add to array to be ignored next time round
            		        		ticketTypeArray.add(ticketType);
            		        		//Add to allTicketTypes string
            		        		if (allTicketTypes.length() > 0) {       		
            		            		allTicketTypes = allTicketTypes.concat(TICKET_DELIMITER);
            		            	}
            		            	allTicketTypes = allTicketTypes.concat(ticketType);
            		        	}
            		        }
	            		}
	            		value.setAllTicketTypes(allTicketTypes);
	            		
	            		// Set the fullListTitle
	            		final String TITLE_DELIMITER = " ";
	            		String fullListTitle = "";
	            		if (value.getFullListTitle1() != null) {
	            			fullListTitle = fullListTitle.concat(value.getFullListTitle1());
	            		}
	            		if (value.getFullListTitle2() != null) {
	            			if (!fullListTitle.isEmpty()) {
	            				fullListTitle = fullListTitle.concat(TITLE_DELIMITER);
	            			}	
	            			fullListTitle = fullListTitle.concat(value.getFullListTitle2());
	            		}
	            		if (value.getFullListTitle3() != null) {
	            			if (!fullListTitle.isEmpty()) {
	            				fullListTitle = fullListTitle.concat(TITLE_DELIMITER);
	            			}	
	            			fullListTitle = fullListTitle.concat(value.getFullListTitle3());
	            		}
	            		value.setFullListTitle(fullListTitle);
	            	}
	            	values.add(value);
	            }
	            results = values;
	        }
        } catch (ObjectNotFoundException anException) {
            throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException); // @TODO
                                                                                            // Create
                                                                                            // an
                                                                                            // external
                                                                                            // error
                                                                                            // code
                                                                                            // for this situation.
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /**
     * Find Judge by id. Also loads the JudgeTickets associated.
     * 
     * @param refJudgeId
     * @return
     * @throws BisRefControllerException
     * @throws ObjectNotFoundException
     */
    public RefJudgeComplexValue findJudgeByPK(Integer refJudgeId) throws BisRefControllerException, ObjectNotFoundException {
    	String METHOD_NAME = "findJudgeByPK";
		log.debug(METHOD_ENTER + METHOD_NAME);
		RefJudge refJudge = this.getRefJudgeMaintainer().findByPrimaryKey(refJudgeId);
		RefJudgeComplexValue complexVal = this.getRefJudgeMaintainer().getComplexValue(refJudge);
		
		// Set JudgeTickets
		Collection<RefJudgeTicket> refJudgeTickets = getRefJudgeTicketMaintainer().findJudgeTicketsByJudgeId(refJudgeId);
		complexVal.setRefJudgeTickets(getRefJudgeTicketMaintainer().getBasicValues(refJudgeTickets));
		log.debug(METHOD_NAME + METHOD_EXIT);
		return complexVal;
    }
    /**
     * If the criteria contains a PrimaryKey, find Justice reference data using
     * that, otherwise create and execute a query.
     * 
     * @param RefJusticeCriteria
     *            criteria
     * @return Collection of RefJusticeBasicValue
     * @throws BisRefControllerException
     */
    public Collection findJustices(RefJusticeCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findJustices ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findJusticesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefJustice localRef = this.getRefJusticeMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefJusticeBasicValue value = this.getRefJusticeMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /**
     * If the criteria contains a PrimaryKey, find Legal Representative
     * reference data using that, otherwise create and execute a query.
     * 
     * @param RefLegalRepresentativeCriteria
     *            criteria
     * @return Collection of RefLegalRepresentativeBasicValue
     * @throws BisRefControllerException
     */
    public Collection findLegalRepresentatives(RefLegalRepresentativeCriteria criteria)
            throws BisRefControllerException {

        final String METHOD_NAME = "::findRefLegalRepresentatives ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findLegalRepresentativesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefLegalRepresentative localRef = this.getRefLegalRepresentativeMaintainer().findByPrimaryKey(
                        criteria.getPrimaryKey());
                RefLegalRepresentativeBasicValue value = this.getRefLegalRepresentativeMaintainer().getBasicValue(
                        localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /**
     * If the criteria contains a PrimaryKey, find Judge reference data using
     * that, otherwise create and execute a query.
     * 
     * @param RefJudgeCriteria
     *            criteria
     * @return Collection of RefJudgeBasicValue
     * @throws BisRefControllerException
     */
    public Collection findProsecutorAgencies(RefProsecutorAgencyCriteria criteria) throws BisRefControllerException {
        final String METHOD_NAME = "::findProsecutorAgencies ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findProsecutorAgenciesByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
            	RefProsecutorAgency localRef = this.getRefProsecutorAgencyMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                RefProsecutorAgencyBasicValue value = this.getRefProsecutorAgencyMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException); // @TODO
                                                                                                // Create
                                                                                                // an
                                                                                                // external
                                                                                                // error
                                                                                                // code
                // for this situation.
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    }
    
    
    /**
     * If the criteria contains a PrimaryKey, find Solicitor Firm reference data
     * using that, otherwise create and execute a query. For find by primary key
     * searches, a basic value object is returned. For other searches, a complex
     * value object is returned.
     * 
     * @param RefSolicitorFirmCriteria
     *            criteria
     * @return Collection of SolicitorFirmBasicValue
     * @throws BisRefControllerException
     */
    public Collection findSolicitorFirms(RefSolicitorFirmCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findSolicitorFirms ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findSolicitorFirmsByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                RefSolicitorFirm localRef = this.getRefSolicitorFirmMaintainer().findByPrimaryKey(
                        criteria.getPrimaryKey());
                RefSolicitorFirmBasicValue value = this.getRefSolicitorFirmMaintainer().getBasicValue(localRef);
                results = this.newCollection();
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    /**
     * If the criteria contains a PrimaryKey, find Solicitor reference data
     * using that, otherwise create and execute a query.
     * 
     * @param SolicitorsCriteria
     *            criteria
     * @return Collection of SolicitorBasicValue
     * @throws BisRefControllerException
     */
    public Collection findSolicitors(SolicitorCriteria criteria) throws BisRefControllerException {

        final String METHOD_NAME = "::findSolicitors ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        Collection results = null;
        if (criteria.getPrimaryKey() == null) {
            log.debug("- find using query using " + criteria.toString());
            results = this.findSolicitorsByQuery(criteria);
        } else {
            log.debug("- find by primary key [" + criteria.getPrimaryKey() + "]");
            try {
                Solicitor localRef = this.getSolicitorMaintainer().findByPrimaryKey(criteria.getPrimaryKey());
                SolicitorComplexValue value = this.getSolicitorMaintainer().getComplexValue(localRef);
                results = this.newCollection();
                // for complex types get solicitor firms
                if (!criteria.isBasicRequest()) {
                    RefSolicitorFirmComplexValue refSolicitorFirmComplexValue = getRefSolicitorFirmMaintainer()
                            .getComplexValue(localRef.getRefSolicitorFirm());
                    if (criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS)) {
                        refSolicitorFirmComplexValue.populateFromAddress(getAddressMaintainer().getAddressBasicValue(
                                localRef.getRefSolicitorFirm().getAddress()));
                    }
                    value.setFirm(refSolicitorFirmComplexValue);
                }
                results.add(value);
            } catch (ObjectNotFoundException anException) {
                throw this.buildBisObjectNotFoundException("ERR_NO", criteria, anException);
                /** @TODO Create an external error code for this situation. */
            }
        }

        log.debug(METHOD_NAME + METHOD_EXIT);
        return results;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    private Collection findAdvocatesByQuery(RefAdvocateCriteria criteria) {
    	final String METHOD_NAME = "::findAdvocatesByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return (new RefAdvocateQuery()).search(criteria);
    }

    private Collection findJudgesByQuery(RefJudgeCriteria criteria) {
    	final String METHOD_NAME = "::findJudgesByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return (new RefJudgeQuery()).search(criteria);
    }

    private Collection findJusticesByQuery(RefJusticeCriteria criteria) {
    	final String METHOD_NAME = "::findJusticesByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return (new RefJusticeQuery()).search(criteria);
    }

    private Collection findLegalRepresentativesByQuery(RefLegalRepresentativeCriteria criteria) {
    	final String METHOD_NAME = "::findLegalRepresentativesByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return (new RefLegalRepresentativeQuery()).search(criteria);
    }
    
    private Collection findProsecutorAgenciesByQuery(RefProsecutorAgencyCriteria criteria) {
    	final String METHOD_NAME = "::findProsecutorAgenciesByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        //return (new RefProsecutorAgencyQuery()).search(criteria);
        return null;
    }

    private Collection findSolicitorFirmsByQuery(RefSolicitorFirmCriteria criteria) {
    	final String METHOD_NAME = "::findSolicitorFirmsByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        return (new RefSolicitorFirmQuery()).search(criteria);
    }

    private Collection findSolicitorsByQuery(SolicitorCriteria criteria) {
    	final String METHOD_NAME = "::findSolicitorsByQuery ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        // modified to use the fast lane readers
        final Collection basicVOs = (new SolicitorQuery()).search(criteria);

        // this one we always need to convert to complex VOs
        final Collection complexVOs = new ArrayList(basicVOs.size());
        final Iterator it = basicVOs.iterator();

        while (it.hasNext()) {
            // convert the basic value objects to complex ones
            final SolicitorBasicValue basicVO = (SolicitorBasicValue) it.next();
            final SolicitorComplexValue complexVO = new SolicitorComplexValue();

            // now actually copy the value object to the complex type
            copyVO(basicVO, complexVO);

            if (criteria.isBasicRequest() == false) {
                try {
                    final RefSolicitorFirmComplexValue refSolicitorFirmComplexValue = getRefSolicitorFirmMaintainer()
                            .getComplexValue(getRefSolicitorFirmMaintainer().findByPrimaryKey(complexVO.getFirmId()));

                    if (criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS)) {
                        refSolicitorFirmComplexValue.populateFromAddress(getAddressMaintainer().getAddressBasicValue(
                                getAddressMaintainer().findByPK(refSolicitorFirmComplexValue.getAddressId())));
                    }

                    complexVO.setFirm(refSolicitorFirmComplexValue);
                } catch (final ObjectNotFoundException e) {
                    String errMsg = "Unable to find a bean in findSolicitorsByQuery";
                    throw handleException(errMsg, e);
                }
            }

            complexVOs.add(complexVO);
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        // always return the complex type
        return complexVOs;
    } /*
         * End Method
         * ------------------------------------------------------------------------------------------------------------------------------------------------------------------
         */

    public SolicitorBasicValue createSolicitor(SolicitorBasicValue solicitorBasicValue, String userDisplayName) {
    	final String METHOD_NAME = "::createSolicitor ";
        log.debug(METHOD_NAME + METHOD_ENTER);
        String METHOD = "createSolicitor";
        Solicitor solicitor = null;
        try {
            solicitorBasicValue.setLegalRepType("S");
            solicitorBasicValue.setObsInd("N");

            Court court = getCourtMaintainer().findByPrimaryKey(solicitorBasicValue.getCourtId());
            log.debug(METHOD + "Found court with Id:" + court.getCourtId().toString());

            // we can't use solicitorBasicValue (using reflection this will
            // eventually be used for Solicitor lookups)
            // to hold Court so pass it in as a parameter

            RefLegalRepresentative refLegalRep = getRefLegalRepresentativeMaintainer().createLegalRep(
                    solicitorBasicValue, court, userDisplayName);

            solicitorBasicValue.setLegalRepId(refLegalRep.getRefLegalRepId());

            solicitor = getSolicitorMaintainer().createSolicitor(solicitorBasicValue);
            solicitor.setRefLegalRepresentative(refLegalRep);

            if (solicitorBasicValue.getFirmId() != null) {
                RefSolicitorFirm solicitorFirm = getRefSolicitorFirmMaintainer().findByPrimaryKey(
                        solicitorBasicValue.getFirmId());
                solicitor.setRefSolicitorFirm(solicitorFirm);
            }

            log.debug("solicitor created with ID:" + solicitor.getSolicitorId());
        } catch (CreateException createException) {
            String errMsg = "Problem creating solicitor.";
            throw handleException(errMsg, createException);
        } catch (ObjectNotFoundException objectNotFoundException) {
            String errMsg = "Problem creating solicitor.";
            throw handleException(errMsg, objectNotFoundException);
        }
        log.debug(METHOD_NAME + METHOD_EXIT);
        return getSolicitorMaintainer().getBasicValue(solicitor);
    }
    
    public RefLegalRepresentativeBasicValue createLegalRep(RefLegalRepresentativeBasicValue refLegalRepBasicValue, String userDisplayName) {
        String METHOD = "createLegalRep";
        log.debug(METHOD + METHOD_ENTER);
        RefLegalRepresentative refLegalRep;
        try {
        	refLegalRepBasicValue.setObsInd("N");

            Court court = getCourtMaintainer().findByPrimaryKey(refLegalRepBasicValue.getCourtId());
            log.debug(METHOD + "Found court with Id:" + court.getCourtId().toString());

            refLegalRep = getRefLegalRepresentativeMaintainer().createLegalRep(
            		refLegalRepBasicValue, court, userDisplayName);
        } catch (CreateException createException) {
            String errMsg = "Problem creating legal representative.";
            throw handleException(errMsg, createException);
        } catch (ObjectNotFoundException objectNotFoundException) {
            String errMsg = "Problem creating legal representative.";
            throw handleException(errMsg, objectNotFoundException);
        }
        log.debug(METHOD + METHOD_EXIT);
        return getRefLegalRepresentativeMaintainer().getBasicValue(refLegalRep);
    }

    /* Maintainer accessors */

    private RefAdvocateMaintainer getRefAdvocateMaintainer() {
        if (refAdvocateMaintainer == null) {
            log.debug(": Lazy initialise this.refAdvocateMaintainer");
            this.refAdvocateMaintainer = new RefAdvocateMaintainer();
        }
        return this.refAdvocateMaintainer;
    }

    private RefChamberMaintainer getRefChamberMaintainer() {

        if (refChamberMaintainer == null) {
            log.debug(": Lazy initialise this.refChamberMaintainer");
            this.refChamberMaintainer = new RefChamberMaintainer();
        }
        return this.refChamberMaintainer;
    }

    private RefJudgeMaintainer getRefJudgeMaintainer() {

        if (refJudgeMaintainer == null) {
            log.debug(": Lazy initialise this.refJudgeMaintainer");
            this.refJudgeMaintainer = new RefJudgeMaintainer();
        }
        return this.refJudgeMaintainer;
    }
    
    private RefJudgeTicketMaintainer getRefJudgeTicketMaintainer() {

        if (refJudgeTicketMaintainer == null) {
            log.debug(": Lazy initialise this.refJudgeTicketMaintainer");
            this.refJudgeTicketMaintainer = new RefJudgeTicketMaintainer();
        }
        return this.refJudgeTicketMaintainer;
    }

    private RefJusticeMaintainer getRefJusticeMaintainer() {

        if (refJusticeMaintainer == null) {
            log.debug(": Lazy initialise this.refJusticeMaintainer");
            this.refJusticeMaintainer = new RefJusticeMaintainer();
        }
        return this.refJusticeMaintainer;
    }
    
    private RefProsecutorAgencyMaintainer getRefProsecutorAgencyMaintainer() {

        if (refProsecutorAgencyMaintainer == null) {
            log.debug(": Lazy initialise this.refProsecutorAgencyMaintainer");
            this.refProsecutorAgencyMaintainer = new RefProsecutorAgencyMaintainer();
        }
        return this.refProsecutorAgencyMaintainer;
    }

    private RefLegalRepresentativeMaintainer getRefLegalRepresentativeMaintainer() {

        if (refLegalRepresentativeMaintainer == null) {
            log.debug(": Lazy initialise this.refLegalRepresentativeMaintainer");
            this.refLegalRepresentativeMaintainer = new RefLegalRepresentativeMaintainer();
        }
        return this.refLegalRepresentativeMaintainer;
    }

    private SolicitorMaintainer getSolicitorMaintainer() {

        if (solicitorMaintainer == null) {
            log.debug(": Lazy initialise this.solicitorMaintainer");
            this.solicitorMaintainer = new SolicitorMaintainer();
        }
        return this.solicitorMaintainer;
    }

    private RefSolicitorFirmMaintainer getRefSolicitorFirmMaintainer() {

        if (refSolicitorFirmMaintainer == null) {
            log.debug(": Lazy initialise this.refSolicitorFirmMaintainer");
            this.refSolicitorFirmMaintainer = new RefSolicitorFirmMaintainer();
        }
        return this.refSolicitorFirmMaintainer;
    }

    private AddressMaintainer getAddressMaintainer() {

        if (addressMaintainer == null) {
            log.debug(": Lazy initialise this.addressMaintainer");
            this.addressMaintainer = new AddressMaintainer();
        }
        return this.addressMaintainer;
    }

    private CourtMaintainer getCourtMaintainer() {
        if (courtMaintainer == null) {
            log.debug(": Lazy initialise this.courtMaintainer");
            this.courtMaintainer = new CourtMaintainer();
        }
        return this.courtMaintainer;
    }
}