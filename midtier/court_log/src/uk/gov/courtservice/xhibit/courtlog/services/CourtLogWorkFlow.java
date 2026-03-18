package uk.gov.courtservice.xhibit.courtlog.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category.XhbCourtLogCategory;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_category_desc.XhbCourtLogCategoryDesc;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntryBeanHelper2;
import uk.gov.courtservice.xhibit.courtlog.exceptions.CourtLogBusinessException;
import uk.gov.courtservice.xhibit.courtlog.flr.CourtLogEntryQueries;
import uk.gov.courtservice.xhibit.courtlog.helpers.CrudValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.helpers.EntityHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.ViewValueAssembler;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.SubscriberChain;
import uk.gov.courtservice.xhibit.courtlog.subscriptions.SubscriberChainFactory;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogScheduledHearingValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * The class provides the business interface for the court log functionality
 * 
 * @author pznwc5
 * @version $Id: CourtLogWorkFlow.java,v 1.33 2010/01/25 15:35:36 pokalak Exp $
 */
public class CourtLogWorkFlow {
    private static final Logger log = CSServices.getLogger(CourtLogWorkFlow.class);

    private CourtLogWorkFlow() {
        // prevent external instantiation...
    }

    /**
     * Adds a new court log entry
     * 
     * @param crudVal
     *            The CRUD value
     * @return View value
     * @throws CourtLogBusinessException
     */
    public static CourtLogViewValue newEntry(CourtLogCRUDValue crudVal) throws CourtLogBusinessException {
    	log.debug("newEntry(caseId="+crudVal.getCaseId()+",eventType="+crudVal.getEventType()+")");
    	return (getSubscriberChain(crudVal).processCreate())[0];	// this does gui update too
    }
    
    public static CourtLogViewValue newEntry(CourtLogCRUDValue crudVal, boolean origin) throws CourtLogBusinessException {
        log.debug("newEntry(caseId="+crudVal.getCaseId()+",eventType="+crudVal.getEventType()+",origin="+ (origin ? "True" : "False")+")");
    	// CTX-502: only create court log events if XHB_CASE.CASE_LISTED='Y'  	
    	XhbCase caseDetails = XhbCaseBeanHelper2.findByPrimaryKey(crudVal.getCaseId());
    	String caseListed = caseDetails.getCaseListed(); 	// can return NULL
    	CourtLogViewValue[] returnCourtLogViewVal = new CourtLogViewValue[1]; 	// blank return
    	
    	// check for null, doing .equals("Y") when caseListed is null results in null pointer exception
    	if(caseListed != null && !caseListed.isEmpty()) { 
        	if(caseListed.equals("Y")) {
                // only return the main entry...
                return (getSubscriberChain(crudVal).processCreate())[0];	// this does gui update too
        	} else {
        		return returnCourtLogViewVal[0];	// blank return 
        	}
    	} else {
    		return returnCourtLogViewVal[0];	// blank return 
    	}    	
    }

    /**
     * Deletes the court log entry
     * 
     * @param logEntryId
     *            Log entry id
     * @param inCourt
     *            "In court" indicator
     * @throws CourtLogBusinessException
     */
    public static void deleteEntry(Long logEntryId, boolean inCourt) throws CourtLogBusinessException {
    	log.debug("deleteEntry(logEntryId="+logEntryId+",inCourt="+(inCourt?"True":"False")+")");
    	getSubscriberChain(getEntry(logEntryId)).processDelete();
    }

    /**
     * Updates the court log entry
     * 
     * @param crudVal
     *            CRUD value
     * @return View value
     * @throws CourtLogBusinessException
     */
    public static CourtLogViewValue updateEntry(CourtLogCRUDValue crudVal) throws CourtLogBusinessException {
    	log.debug("updateEntry(caseId="+crudVal.getCaseId()+",eventType="+crudVal.getEventType()+")");
    	return (getSubscriberChain(crudVal).processUpdate())[0];
    }

    /**
     * Gets all the entries for the case
     * 
     * @param caseId
     *            Case id
     * @return Array of court log view values
     */
    public static CourtLogViewValue[] getCourtLog(Integer caseId) {
        log.debug("getCourtLog(Integer) - caseId = " + caseId);

        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        final XhbCourtLogEntryBasicValue[] cle = cleq.getCourtLogEntries(caseId);
        final CourtLogViewValue[] values = ViewValueAssembler.createCourtLogViewValues(cle);

        log.debug("getCourtLog(Integer) - Returning " + values.length + " entries");
        return values;
    }

    /**
     * Gets court log for the case and date range
     * 
     * @param caseId
     *            Case id
     * @param fromDate
     *            Start date
     * @param toDate
     *            End date
     * @return Array of court log view values
     */
    public static CourtLogViewValue[] getCourtLog(Integer caseId, Date fromDate, Date toDate) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLog(Integer, Date, Date) - caseId = " + caseId + "; fromDate = " + fromDate
                    + "; toDate = " + toDate);
        }

        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        final XhbCourtLogEntryBasicValue[] cle = cleq.getCourtLogEntries(caseId, fromDate, toDate);
        final CourtLogViewValue[] values = ViewValueAssembler.createCourtLogViewValues(cle);

        log.debug("getCourtLog(Integer, Date, Date) - Returning " + values.length + " entries");
        return values;
    }

    /**
     * Gets an individual entry
     * 
     * @param logEntryId
     *            Log entry id
     */
    public static CourtLogCRUDValue getEntry(Long logEntryId) {
        log.debug("getEntry(Long) - logEntryId = " + logEntryId);

        final XhbCourtLogEntryBasicValue xhbCourtLogEntry = EntityHelper.getXhbCourtLogEntryBasicValue(logEntryId);
        return CrudValueAssembler.createCourtLogCRUDValue(xhbCourtLogEntry);
    }

    /**
     * Gets a given category of court log for a given case
     * 
     * @param caseId
     *            Case id
     * @param categoryDesc
     *            Description of the category for events to find
     * @return Array of court log view values
     */
    public static CourtLogViewValue[] getCourtLog(Integer caseId, String categoryDesc) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLog(Integer, String) - caseId = " + caseId + "; categoryDesc = " + categoryDesc);
        }

        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        final XhbCourtLogEntryBasicValue[] cle = cleq.getCourtLogEntries(caseId, categoryDesc);
        final CourtLogViewValue[] values = ViewValueAssembler.createCourtLogViewValues(cle);

        log.debug("getCourtLog(Integer, String) - Returning " + values.length + " entries");
        return values;
    }

    /**
     * Find out if there are many more recent events of a particular type than
     * the passed in date.
     * 
     * @param caseId
     *            Case id
     * @param eventType
     * @param fromDate
     * @return A boolean indicating <i>true</i> if there are more recent
     *         events, <i>false</i> otherwise.
     */
    public static boolean hasMoreEvents(Integer caseId, Integer eventType, Date fromDate) {
        if (log.isDebugEnabled()) {
            log.debug("hasMoreEvents(Integer, Integer, Date) - caseId = " + caseId + "; eventType = " + eventType
                    + "; fromDate = " + fromDate);
        }

        // Check the parameters
        if ((caseId == null) || (eventType == null) || (fromDate == null)) {
            throw new IllegalArgumentException("null parameter supplied");
        }

        final Collection courtLogEntries = XhbCourtLogEntryBeanHelper2.findByCaseIdEventTypeDate(caseId, eventType,
                new Timestamp(fromDate.getTime()));

        final boolean value = (courtLogEntries.size() > 0);
        log.debug("hasMoreEvents(Integer, Integer, Date) - Returning " + value);
        return value;
    }

    /**
     * Gets a given category of court log for a given case beteen the date range
     * specified
     * 
     * @param caseId
     *            Case id
     * @param categoryDesc
     *            Description of the category for events to find
     * @param fromDate
     *            Start date
     * @param toDate
     *            End date
     * @return Array of court log view values
     */
    public static CourtLogViewValue[] getCourtLog(Integer caseId, String categoryDesc, Date fromDate, Date toDate) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLog(Integer, String, Date, Date) - caseId = " + caseId + "; categoryDesc = "
                    + categoryDesc + "; fromDate = " + fromDate + "; toDate = " + toDate);
        }

        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        final XhbCourtLogEntryBasicValue[] cle = cleq.getCourtLogEntries(caseId, categoryDesc, fromDate, toDate);
        final CourtLogViewValue[] values = ViewValueAssembler.createCourtLogViewValues(cle);

        log.debug("getCourtLog(Integer, String, Date, Date) - Returning " + values.length + " entries");
        return values;
    }

    /**
     * Private utility method to acquire the <code>SubscriberChain</code> for
     * the passed in <code>CourtLogCRUDValue</code>.
     * 
     * @param crudVal
     *            The <code>CourtLogCRUDValue</code> we want to acquire the
     *            chain for
     * @return The <code>SubscriberChain</code>
     */
    private static SubscriberChain getSubscriberChain(CourtLogCRUDValue crudVal) {
        return SubscriberChainFactory.getInstance().getSubscriberChain(crudVal);
    }

    /**
     * Gets custom properties for the scheduled hearings associated with the
     * case passed in
     * 
     * @param caseId
     * @return
     */
    public static CourtLogScheduledHearingValue[] getCourtLogScheduledHearingValuesForCase(Integer caseId) {
        if (log.isDebugEnabled()) {
            log.debug("getCourtLogScheduledHearingValuesForCase(Integer) - caseId = " + caseId);
        }

        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        return cleq.getCourtLogScheduledHearingValues(caseId);
    }

    /**
     * TODO
     * 
     * @param categoryDescs
     * @return
     */
    public static Integer[] getEventTypesByCategoryDesc(String[] categoryDescs) {
        log.debug("getEventTypesByCategoryDesc(String[]) - no. of categories =" + categoryDescs.length);

        final Collection returnTypes = new ArrayList();

        for (int i = 0; i < categoryDescs.length; i++) {
            final XhbCourtLogCategoryDesc desc = EntityHelper.getXhbCourtLogCategoryDesc(categoryDescs[i]);

            final Iterator it = desc.getXhbCourtLogCategories().iterator();
            while (it.hasNext()) {
                XhbCourtLogCategory clc = (XhbCourtLogCategory) it.next();
                returnTypes.add(clc.getXhbCourtLogEventDesc().getEventType());
            }
        }

        final Integer[] values = (Integer[]) returnTypes.toArray(new Integer[returnTypes.size()]);
        log.debug("getEventTypesByCategoryDesc(String[]) - Returning " + values.length + " event types");
        return values;
    }

    /**
     * Retrieves the last event of this type for this case, or null if no event
     * of this type is found
     * 
     * @param caseId
     *            The case we are checking on
     * @param eventType
     *            The event type to retrieve
     * @return The last event of this type, or null if none found
     */
    public static XhbCourtLogEntryBasicValue getLastEventOfType(Integer caseId, Integer eventType) {
        final CourtLogEntryQueries cleq = new CourtLogEntryQueries();
        return cleq.getLastCourtLogEntry(caseId, eventType);
    }
}
