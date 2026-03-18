package uk.gov.courtservice.xhibit.courtlog.darts;

//jdk, j2ee
import java.io.IOException;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ejb.EJBException;
import javax.ejb.FinderException;

import org.apache.log4j.Logger;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.ValidationException;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_config_prop.XhbConfigPropBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoom;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBeanNotFoundException;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffence;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_offence.XhbDefendantOnOffenceBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.darts.DartsMessageFactory;
import uk.gov.courtservice.xhibit.business.services.darts.XhibitDartsControllerLocal;
import uk.gov.courtservice.xhibit.business.services.darts.XhibitDartsControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.entities.DarRetentionPolicyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDarRetentionPoliciesBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefDispRetentionPolicyBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventPopulator;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventlevel.EventLevelAndIdentifier;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.CaseNumbers;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.DartsEvent;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.EventParameters;
import uk.gov.courtservice.xhibit.xmlbinding.generated.cjse.entities.RetentionPolicy;

/**
 * <p>
 * Title: DartsClHelper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Provides utility methods to support the business process in DartsSubscriber.  The darts
 * messaging process uses many classes provided via the uk.gov.courtservice.xhibit.courtlog.cjse
 * package, it's functionality in almost identical apart from the structure of the final event 
 * message sent to darts.  
 * </p>
 *
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20081010 
 */

public class DartsClHelper {
    private static final Logger log = CSServices.getLogger(DartsClHelper.class);

    private static final String DARTS_COURTLOG_DELAY = "DARTS_COURTLOG_DELAY";
    private static final String CASE_CLOSED_DESC = "Case_Closed";
    private static final Integer CASE_CLOSED_EVENT = Integer.valueOf(30300);
    private static final DateFormat DATEFORMAT = new SimpleDateFormat("dd/MM/yyyy");
    private static final DateFormat DATETIMEFORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private static final DateFormat TIMEFORMAT = new SimpleDateFormat("HH:mm:ss");
    private Map _eventPopulators;
    private Map _eventCascades;
    private XhibitDartsControllerLocal dartsController; 
    
    /**
     * No arguments constructor used to create an instance of DartsClHelper.
     */
    public DartsClHelper() 
    {
        /* Get the populators. */
        _eventPopulators = DartsEventPopulatorMapFactory.getEventPopulators();
        /* Get the cascades from Singleton instance so config read only Once. */
        _eventCascades = DartsEventCascadeFilterFactory.getInstance().getEventCascadeFilters();
        
        dartsController = (XhibitDartsControllerLocal) CSServices.getEJBServices().createLocalSession(
        		XhibitDartsControllerLocalHome.class);
    }


    /**
     * This method takes a CourtLogSubscriptionValue representing a Court Log
     * Event and uses it to produce a DARTS event.
     * 
     * @param obj : CourtLogSubscriptionValue
     *            the court log event.
     * @param freeText : Integer
     *            the free text associated with the original CRUD value.
     * @param eventID
     *            the id of the original court log entry.                         
     * @throws NumberFormatException
     *             when the event type id is not a number.
     */
    public void transformClEvent(CourtLogSubscriptionValue obj, String freeText, Integer eventID) throws NumberFormatException 
    {
        String methodName = "transformClEvent()";
        log.debug(methodName+" - Start");
   
        delayCourtLogEvent();
        Integer courtRoomId = obj.getCourtRoomId();
        int courtRoomNumber = 0;
        XhbCourtRoom theCourtRoom = null;
        String courtSiteName = null;
        XhbCase theCase = null;
        Integer caseId = obj.getCourtLogViewValue().getCaseId();
        Integer defendantOnCaseId = obj.getCourtLogViewValue().getDefendantOnCaseId();
        Integer defendantOnOffenceId = obj.getCourtLogViewValue().getDefendantOnOffenceId();
  
        boolean inCourt = true;
        if (courtRoomId == null) {
            /* no court room, this event must have been created out of court */
            inCourt = false;
        }

        /*
         * Access to the EJB entities that represent the case information.
         */
        
        /* Get the case Entity */
        try {
            theCase = XhbCaseBeanHelper2.findByPrimaryKey(caseId);
        } catch (XhbCaseBeanNotFoundException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            log.error("Could not get case information for case id : " + caseId);
            return;
        }
        String caseIndentifier = theCase.getCaseType() + theCase.getCaseNumber();
        log.debug("caseIdentifier="+caseIndentifier);

        /* Get the courtRoom Entity */
        if( inCourt == true) {
            try {
                theCourtRoom = XhbCourtRoomBeanHelper2.findByPrimaryKey(courtRoomId);
            } catch (XhbCourtRoomBeanNotFoundException  ex) {
                CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
                log.error("Could not get courtRoom information for courtRoom id : " + courtRoomId);
                return;
            }
            courtRoomNumber = theCourtRoom.getCrestCourtRoomNo().intValue();
            courtSiteName = theCourtRoom.getXhbCourtSite().getCourtSiteName();
            theCourtRoom = null;
        } else {
            courtSiteName = theCase.getXhbCourt().getCourtName();
        }
        log.debug("courtSiteName="+courtSiteName);

        /* Get the relevant data from the value object and the database. */
        CourtLogViewValue objViewValue = obj.getCourtLogViewValue();
        Integer xhibitEventType = objViewValue.getEventType();
        log.debug("xhibitEventType="+xhibitEventType);
        
        /* Get the retention policy */
        RetentionPolicy retentionPolicy = null;
        if (dartsController.isSendOutGoingRetentionPolicyRequired(xhibitEventType)) {
	        try {
	        	String userDisplayName = null; 
	        	Integer caseDarRetentionPolicyId = dartsController.updateRetentionPolicyOnMsgSend(xhibitEventType, theCase.getDarRetentionPolicyId(), caseId, 
	        			theCase.getCaseType(), userDisplayName);
	        	retentionPolicy = getRetentionPolicy(caseDarRetentionPolicyId, defendantOnCaseId, defendantOnOffenceId);
	        	log.debug(methodName+" Case Total = "+retentionPolicy.getCaseTotalSentence());
	        } catch (RuntimeException ex) {
	        	log.error("Error updating RetentionPolicy: "+ex.getMessage());
	        	throw ex;
	        } catch (FinderException ex) {
	        	log.error("Error updating RetentionPolicy"+ex.getMessage());
	        	return;
			}
        }
        
        /* check for event cascades. */
        Integer[] xhibtEventIds = (Integer[]) _eventCascades.get(xhibitEventType);
        if (xhibtEventIds == null) {
            xhibtEventIds = new Integer[] { xhibitEventType };
        }

        /* for each sub event, or for the single event if no cascade found... */
        for (int i = 0; i < xhibtEventIds.length; i++) {
            if (log.isDebugEnabled()) {
                log.debug(methodName + "eventIds[" + i + "] = " + xhibtEventIds[i]);
            }
            /* Find an event populator to create the cjse event */
            CjseEventPopulator populator = (CjseEventPopulator) _eventPopulators.get(xhibtEventIds[i]);
            if (populator != null) {
                /* Use the event populator to generate the output XML and
                 * Set up the event parameters for population.              */
                EventParameters eventParameters = new EventParameters();
                /* Populate the event. */
                EventLevelAndIdentifier levelAndIdentifier = populator.populateDartsEvent(obj, theCase, eventParameters,
                                                                                          xhibtEventIds[i]);
                if (levelAndIdentifier == null) {
                    /* this event is not going to DARTS */
                    log.debug("createDartsEvent - levelAndIdentifier == null; returning");
                    continue;
                }
                
                Integer exissEventType = Integer.valueOf(eventParameters.getEventTypeID());
                createDartsEvent( obj, courtSiteName, courtRoomNumber, caseId, caseIndentifier, freeText, 
                        eventID, xhibtEventIds[i].intValue(), exissEventType, eventParameters,
                        retentionPolicy);
            } else {
                
                if (log.isDebugEnabled()) {
                    log.debug("There is no populator configured for the Xhibit event type: " + objViewValue.getEventType()
                            + " this event will not be sent " + "to DARTS");
                }
                continue;
            }
        }
        log.debug(methodName + " - Finished");
    } //  end of transformClEvent()

    /**
     * Delete the Courtlog event logic 
     **/
    public void deleteClEvent(final Integer xhibitEventType, final Integer caseId) {    	
    	if (dartsController.isSendOutGoingRetentionPolicyRequired(xhibitEventType)) {
    		log.debug("deleteClEvent("+xhibitEventType+","+caseId+")");
    		try {
	        	String userDisplayName = null; 
	        	// Recalculate the retention policy
	        	dartsController.recalculateCaseRetentionPolicy(caseId, userDisplayName);	        	
	        } catch (FinderException ex) {
	        	log.error("PostDelete - Failed to recalculate retention policy", ex);
	        	return;
			}
        }
    }
    
    /**
     * This method takes all relevant information and constructs the
     * new DARTS event ready to be sent to the DARTS processing.
     *
     * @param obj
     *            The CourtLogSubscriptionValue for an XHIBIT message.
     * @param theCase
     *            The case entity for the case the event is created on.
     * @param courtSiteName
     *            The name of the court site where the event occured.
     * @param courtRoomNumber
     *            The crest court room number where the event occured.
     * @param caseIndentifier
     *            The identifier of the case that the event is associated with.
     * @param freeText
     *            The free text entered with the event.
     * @param eventID
     *            the id of the original court log entry.
     * @param retentionPolicy
     *            the retention policy of the darts event   
     *            
     * @throws NumberFormatException
     *             When the event type id is not a number.
     */
    public void createDartsEvent( CourtLogSubscriptionValue obj, 
            String courtSiteName, int courtRoomNumber, Integer caseId, String caseIndentifier, String  freeText, 
            Integer eventID, int xhibitEventId, Integer exissEventId, EventParameters eventParamenters,
            RetentionPolicy retentionPolicy) throws NumberFormatException {
        
        int eventNo = 0;
        if(eventID != null ){
            eventNo = eventID.intValue();
        }
        if (log.isDebugEnabled()) {
            log.debug("createDartsEvent - Start: Darts Event: " + courtSiteName + " : Case Ident" 
                       + caseIndentifier + " : event No  " + eventNo);
        }
                
        /* Build the Darts Event */
        DartsEvent dartsEvent = new DartsEvent();
        dartsEvent.setID(eventNo);
        Calendar eventTime = getEventTime( eventParamenters.getEventTime() , xhibitEventId, caseId); 
        dartsEvent.setY(eventTime.get(Calendar.YEAR)); 
        dartsEvent.setM(eventTime.get(Calendar.MONTH)+ 1);
        dartsEvent.setD(eventTime.get(Calendar.DAY_OF_MONTH));
        dartsEvent.setH(eventTime.get(Calendar.HOUR_OF_DAY));
        dartsEvent.setMIN(eventTime.get(Calendar.MINUTE));
        dartsEvent.setS(eventTime.get(Calendar.SECOND));
        dartsEvent.setCourtHouse(courtSiteName);
        dartsEvent.setCourtRoom(courtRoomNumber);
        CaseNumbers currentCases = new CaseNumbers();
        currentCases.addCaseNumber(caseIndentifier);
        dartsEvent.setCaseNumbers(currentCases);
        dartsEvent.setRetentionPolicy(retentionPolicy);
        /* MessageText for Darts Message will only consist of Defendant names if relevant to type of event */
        if (freeText != null)
        {
                dartsEvent.setEventText(eventParamenters.getMessageText() +  " " + freeText);
        }
        else
        {
                dartsEvent.setEventText(eventParamenters.getMessageText());
        }
        log.debug("createDartsEvent - event populated: " + dartsEvent);
 
        /* Marshal the event back to XML */
        Marshaller marshaller = null;
        StringWriter xmlOutput = new StringWriter();
        try {
            marshaller = new Marshaller(xmlOutput);
            marshaller.setNamespaceMapping("be", "urn:integration-cjsonline-gov-uk:pilot:entities");
            marshaller.marshal(dartsEvent);
            log.debug("createDartsEvent - event marshalled: " + courtSiteName);
        } catch (ValidationException ex) {
            log.error("Invalid XML generated by populator.", ex);
            return;
        } catch (MarshalException ex) {
            log.error("Could no marshal XML generated by populator.", ex);
            return;
        } catch (IOException ex) {
            log.error("Insane exception due to a string not being successfully " + "wrapped in a StringWriter.", ex);
            return;
        }

        String logEntry = xmlOutput.toString();
        logEntry = stripXMLHeading(logEntry);

        /* Send the event to the DartsMessageOutboundQueue. */
        saveDARTSEvent( xhibitEventId, exissEventId, logEntry);
        log.debug("createDartsEvent - Finished");

    } // end of createDartsEvent()

    private Calendar getEventTime(Date eventDateTime, Integer eventType, Integer caseId) {
    	log.debug("getEventTime()");
    	Calendar eventTime = Calendar.getInstance();
    	if (CASE_CLOSED_EVENT.equals(eventType)) {
    		// Get all the case closed events
    		@SuppressWarnings("unchecked")
			Collection<XhbCourtLogEntry> caseClosedEvents = XhbCourtLogEntryBeanHelper2.findByCaseIdEventDesc(caseId, CASE_CLOSED_DESC);
    		
    		// Get the count of case closed events (already includes this one)
    		Integer caseClosedEventCount = (caseClosedEvents != null && !caseClosedEvents.isEmpty() 
    				? caseClosedEvents.size() : 1 );
    		log.debug("caseClosedEventCount = "+caseClosedEventCount);
    		
    		// Get the latest time of day for the case closed event
    		Date latestEventTime = getLatestEventTime(caseClosedEvents);
    		log.debug("latestEventTime = "+getDateAsString(latestEventTime, TIMEFORMAT));
    		
    		// Get the latest date for the hearings
    		Date latestHearingDate = getLatestHearingDate(caseId);
    		log.debug("latestHearingDate = "+getDateAsString(latestHearingDate,DATEFORMAT));
    		
    		// Start with the passed in date time
    		eventTime.setTime( eventDateTime );
    		
    		// Set the date to the latest hearing date
    		if (latestHearingDate != null) {
    			Calendar latestHearingCal = DateTimeUtilities.convertToCalendar(latestHearingDate);
    			eventTime = setDate(eventTime, latestHearingCal);
    		}
    		
    		// Set the time to the latest event dates time
    		if (latestEventTime != null) {
    			Calendar latestEventCal = DateTimeUtilities.convertToCalendar(latestEventTime);
    			eventTime = setTime(eventTime, latestEventCal, caseClosedEventCount);
    		}
    	} else {
    		eventTime.setTime( eventDateTime );
    	}
    	log.debug("New EventTime="+getDateAsString(eventTime.getTime(), DATETIMEFORMAT));
        return eventTime;
    }
    
    private String getDateAsString(Date date, DateFormat dateFormat) {
    	if (date != null) {
    		return dateFormat.format(date);
    	}
    	return null;
    }
    
    private Calendar setDate(Calendar eventTime, Calendar date) {
    	Calendar result = eventTime;
    	result.set(Calendar.YEAR, date.get(Calendar.YEAR));
		result.set(Calendar.MONTH, date.get(Calendar.MONTH));
		result.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));
		return result;
    }
    
    private Calendar setTime(Calendar eventTime, Calendar time, Integer additionalSeconds) {
    	Calendar result = eventTime;
    	result.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
    	result.set(Calendar.MINUTE, time.get(Calendar.MINUTE));
		result.set(Calendar.SECOND, time.get(Calendar.SECOND) + additionalSeconds);
		return result;
    }
    
    private Date getLatestEventTime(Collection<XhbCourtLogEntry> events) {
    	log.debug("getLatestEventTime() - Start");
    	Date result = null;
    	// Already includes this court log, so count must be higher than 1
    	if (events != null && !events.isEmpty() && events.size() > 1) {
    		for (XhbCourtLogEntry event : events) {
    			if (event.getDateTime() != null) {
    				if (result == null) {
    					result = event.getDateTime();
    				} else {
    					Integer resultInt = getTimeAsInt(result);
    					Integer eventTimeInt = getTimeAsInt(event.getDateTime());
    					if (resultInt < eventTimeInt) {
    						result = event.getDateTime();
    					}
    				}
    			}
    		}
    	}
    	log.debug("getLatestEventTime() - End");
		return result;
    }
    
    @SuppressWarnings("deprecation")
	private Integer getTimeAsInt(Date date) {
    	Integer result = date.getHours() * 10000;
    	result = result + date.getMinutes() * 100;
    	result = result + date.getSeconds();
    	return result;
    }
    
    private Date getLatestHearingDate(Integer caseId) {
    	log.debug("getLatestHearingDate(caseId="+caseId+")");
    	Date result = null;
    	try {
			@SuppressWarnings("unchecked")
			Collection<ScheduledHearingValue> schedHearings = dartsController.getScheduledHearings(caseId);
			if (schedHearings != null && !schedHearings.isEmpty()) {
				for (ScheduledHearingValue hearing : schedHearings) {
					if (hearing.getScheduledHearingDate() != null) { 
						if (result == null || result.before(hearing.getScheduledHearingDate().getTime())) {
							result = hearing.getScheduledHearingDate().getTime();
						}
					}
				}
			}
		} catch (EJBException e) {
			log.debug("No hearings for caseId "+caseId);
		}
    	// This covers the old CREST data where there is no hearing data. 
    	if (result == null) {
    		result = new Date();
    	}
    	log.debug("getLatestHearingDate() - Finished");
        return result;
    }
    
    /**
     * <p>
     * This method is used to send the event Message to the JMS queue DartsMessageOutboundQueue.
     * A MDB is resposible for removing the message from the queue and persisting it into the
     * DAR_MESSAGE_STORE table in the DARTS schema.
     * </p>
     * @param xhibitEventId : int
     *            The xhibit event id.
     * @param exissEventId : Integer
     *            The Exiss event id.
     * @param logEntry : String
     *            The XML content
     */
    public void saveDARTSEvent( int xhibitEventId, Integer exissEventId, String logEntry) {
        log.debug("saveDARTSEvent Start: xhibitEventId = " + xhibitEventId);
        Map<String, String> propertyMap = new HashMap<String, String>();
        propertyMap.put("xhibitMessageCode", String.valueOf(xhibitEventId));
        propertyMap.put("exissMessageCode", String.valueOf(exissEventId));
             
        if (log.isDebugEnabled()) {
            log.debug( "Sending event msg to Darts DartsMessageOutboundQueue: " + xhibitEventId +" : " + exissEventId );
        }
        CSServices.getJMSServices().send(
                           new DartsMessageFactory("jms/darts/DartsMessageOutboundQueue", propertyMap, logEntry)); 
        log.debug("save_DARTS_Event - Finished");
    } // end of saveDARTSEvent()

    private String stripXMLHeading(String logEntry) {
        int endTagPos = logEntry.indexOf("?>");
        return logEntry.substring(endTagPos + 2);
    } // end of stripXMLHeading
    
    
    private RetentionPolicy getRetentionPolicy(final Integer caseDarRetentionPolicyId,
    		final Integer defendantOnCaseId, final Integer defendantOnOffenceId) throws RuntimeException, FinderException{
    	log.debug("getRetentionPolicy("+caseDarRetentionPolicyId+","+defendantOnCaseId+","+defendantOnOffenceId);
    	DarRetentionPolicyBasicValue caseDarRetentionPolicy = null;
    	RefDarRetentionPoliciesBasicValue refDarRetentionPolicy = null;
    	
    	// Get the darRetentionPolicyId for the defendantOnCaseId,defendantOnOffenceId or the case
    	Integer darRetentionPolicyId = getDarRetentionPolicyId(defendantOnCaseId,defendantOnOffenceId);
    	Integer refDarRetentionPolicyId = null;
    	
    	// Get the disposal policy (if one is set)
    	if (darRetentionPolicyId != null) {
        	log.debug("darRetentionPolicyId="+darRetentionPolicyId);
    		// Get the disposal darRetentionPolicy
        	DarRetentionPolicyBasicValue darRetentionPolicy = getDarRetentionPolicy(darRetentionPolicyId);
    		if (darRetentionPolicy != null) {
            	// Get the refDispRetentionPolicy
    			RefDispRetentionPolicyBasicValue refDispRetentionPolicy = getRefDispRetentionPolicy(darRetentionPolicy.getRefDispRetentionPolicyId());   	
                if (refDispRetentionPolicy != null) {
                	// Get the refDarRetentionPolicies
                	refDarRetentionPolicyId = refDispRetentionPolicy.getRefDarRetentionPolicyId();
                }
            }
    	} 
    	
    	// Get the case totals / policy
    	if (caseDarRetentionPolicyId != null) {
    		log.debug("caseDarRetentionPolicyId="+caseDarRetentionPolicyId);
    		// Get the case darRetentionPolicy
    		caseDarRetentionPolicy = getDarRetentionPolicy(caseDarRetentionPolicyId);
    		if (caseDarRetentionPolicy != null) {
    			// Get the refDarRetentionPolicies
    			refDarRetentionPolicyId = caseDarRetentionPolicy.getRefDarRetentionPolicyId();
    		}
    	}
    	
    	// If no refDarRetentionPolicyId is populated then use the default
    	if (refDarRetentionPolicyId == null) {
    		refDarRetentionPolicy = dartsController.getDefaultRefDarPolicy();
    		refDarRetentionPolicyId = refDarRetentionPolicy != null ? refDarRetentionPolicy.getRefDarRetentionPolicyId() : null;
    	}
    	
    	// Get the refDarRetentionPolicy
    	if (refDarRetentionPolicyId != null && refDarRetentionPolicy == null) {
        	refDarRetentionPolicy = getRefDarRetentionPolicy(refDarRetentionPolicyId);
    	}
    	
    	// Populate the Retention Policy
    	if (caseDarRetentionPolicy != null && refDarRetentionPolicy != null) {
    		RetentionPolicy result = new RetentionPolicy();
    		String duration = getDurationString(caseDarRetentionPolicy.getDurationDays(),
    				caseDarRetentionPolicy.getDurationMonths(), caseDarRetentionPolicy.getDurationYears());
    		result.setCaseRetentionFixedPolicy(refDarRetentionPolicy.getPolicyNo().toString());
    		result.setCaseTotalSentence(duration);
    		return result;
    	}
    	return null;
    }
    
    private String getDurationString(final Integer durationDays, final Integer durationMonths, final Integer durationYears) {
    	String years = (durationYears != null ? durationYears.toString() : "0") + "Y";
    	String months = (durationMonths != null ? durationMonths.toString() : "0") + "M";
    	String days = (durationDays != null ? durationDays.toString() : "0") + "D";
    	return years + months + days;
    }
    
    private Integer getDarRetentionPolicyId(
    		final Integer defendantOnCaseId, final Integer defendantOnOffenceId) throws RuntimeException{
    	log.debug("getDarRetentionPolicyId("+defendantOnCaseId+","+defendantOnOffenceId+")");
    	// Get the retentionPolicy for the defendantOnCase
    	if (defendantOnCaseId != null) {
    		XhbDefendantOnCase defendantOnCase = getDefendantOnCase(defendantOnCaseId);
    		if (defendantOnCase != null && defendantOnCase.getDarRetentionPolicyId() != null) {
    			return defendantOnCase.getDarRetentionPolicyId();
    		}
    	}
    	// Get the darRetentionPolicy for the defendantOnOffence
    	if (defendantOnOffenceId != null) {
    		XhbDefendantOnOffence defendantOnOffence = getDefendantOnOffence(defendantOnOffenceId);
    		if (defendantOnOffence != null && defendantOnOffence.getDarRetentionPolicyId() != null) {
    			return defendantOnOffence.getDarRetentionPolicyId();
    		}
    	}
    	return null;
    }
    
    private RefDarRetentionPoliciesBasicValue getRefDarRetentionPolicy(Integer refDarRetentionPoliciesId) throws FinderException {
    	RefDarRetentionPoliciesBasicValue result = null;
    	try {
    		result = dartsController.findRefDarRetentionPolicy(refDarRetentionPoliciesId); 
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            log.error("Could not get refDarRetentionPolicies information for id : " + refDarRetentionPoliciesId);
            throw ex;
        }
    	return result;
    }
    
    private RefDispRetentionPolicyBasicValue getRefDispRetentionPolicy(Integer refDispRetentionPolicyId) throws FinderException {
    	RefDispRetentionPolicyBasicValue result = null;
    	try {
    		result = dartsController.findRefDispRetentionPolicy(refDispRetentionPolicyId);
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            log.error("Could not get refDispRetentionPolicy information for id : " + refDispRetentionPolicyId);
            throw ex;
        }
    	return result;
    }
    
    private DarRetentionPolicyBasicValue getDarRetentionPolicy(Integer darRetentionPolicyId) throws FinderException {
    	DarRetentionPolicyBasicValue result = null;
    	try {
    		result = dartsController.findDarRetentionPolicy(darRetentionPolicyId);
        } catch (FinderException ex) {
            CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
            log.error("Could not get darRetentionPolicy information for id : " + darRetentionPolicyId);
            throw ex;
        }
    	return result;
    }
    
    private XhbDefendantOnCase getDefendantOnCase(Integer defendantOnCaseId) throws RuntimeException {
    	log.debug("getDefendantOnCase("+defendantOnCaseId+")");
    	XhbDefendantOnCase result = null;
    	try {
    		result = XhbDefendantOnCaseBeanHelper2.findByPrimaryKey(defendantOnCaseId);
        } catch (RuntimeException ex) {
            log.error("Could not get defendantOnCase information for id : " + defendantOnCaseId);
        }
    	return result;
    }
    
    private XhbDefendantOnOffence getDefendantOnOffence(Integer defendantOnOffenceId) throws RuntimeException {
    	log.debug("getDefendantOnOffence("+defendantOnOffenceId+")");
    	XhbDefendantOnOffence result = null;
    	try {
    		result = XhbDefendantOnOffenceBeanHelper2.findByPrimaryKey(defendantOnOffenceId);
        } catch (RuntimeException ex) {
            log.error("Could not get defendantOnOffence information for id : " + defendantOnOffenceId);
        }
    	return result;
    }
    
    /*
     * Delay the courtlog event until the main transaction has completed.
     */
    private void delayCourtLogEvent() {
    	Integer delay = getDelay();
    	if (delay != null) {
    		sleep(delay);
    	}
    }
    
    private void sleep(Integer milliseconds) {
    	log.debug("Sleep("+milliseconds+")");
    	try {
			TimeUnit.MILLISECONDS.sleep(milliseconds);
		} catch (InterruptedException e) {
			log.debug("Sleep Interrupted");
		}
    }
    
    private Integer getDelay() { 
    	XhbConfigPropBasicValue configPropBasicValue = getConfigPropBasicValue(DARTS_COURTLOG_DELAY);
    	Integer result = null;
    	if (configPropBasicValue != null) {
    		try {
    		result = Integer.parseInt(configPropBasicValue.getPropertyValue());
    		} catch (NumberFormatException ex) {
    			log.error(DARTS_COURTLOG_DELAY +" contains invalid intger "+configPropBasicValue.getPropertyValue());
    		}
    	}
    	log.debug("getDelay() - "+result);
    	return result;
    }
    
    private XhbConfigPropBasicValue getConfigPropBasicValue(String propertyName) {
		XhbConfigPropBasicValue[] properties = XhbConfigPropBeanHelper2.findByPropertyNameValue(propertyName);
		if (properties != null) {
			for( XhbConfigPropBasicValue basicValue : properties){
				return basicValue;
			}
		}
		return null;
	}
} //  end of class 