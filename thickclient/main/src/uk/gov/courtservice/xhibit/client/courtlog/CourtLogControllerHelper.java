package uk.gov.courtservice.xhibit.client.courtlog;

import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.caze.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue.CourtLogControllerPrintCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue.CourtLogControllerPrintValue;
import uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue.HearingCourtLogEntriesPrintValue;
import uk.gov.courtservice.xhibit.business.vos.services.courtlog.printvalue.RelatedCourtLogEntriesPrintValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.PersonValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.courtlog.util.EventConstants;
import uk.gov.courtservice.xhibit.client.util.HearingHeaderValueHelper;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.CourtLogXslHelper;
import uk.gov.courtservice.xhibit.courtlog.helpers.xsl.TranslationType;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: CourtLogControllerHelper
 * </p>
 * <p>
 * Description: Provides functionality for transfomring the court log for
 * printing
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell Xdevelopemnt (2003)
 * @version 1.0
 */
public class CourtLogControllerHelper {
    /**
     * The log4j logger
     */
    private static final Logger LOG = CSServices.getLogger(CourtLogControllerHelper.class);

    /**
     * Format the court log for printing using the parameters specified in the
     * model
     * 
     * @param xac
     *            the application controller
     * @param clcm
     *            the court log model
     * @return the formated xml-fo docuemnt ready for printing
     */
    public String[] formatCourtLogForPrinting(XhibitApplicationController xac, CourtLogControllerModel clcm)
            throws UserCancelException, CSRecoverableException {
        return new String[] { formatCourtLogControllerPrintValue(createCourtLogControllerPrintValue(xac, clcm)) };
    }

    /**
     * Display the print dialog to determine which part of the court log to
     * display
     * 
     * @param xac
     *            the application controller
     * @param clcm
     *            the court log model
     * @return a CourtLogControllerPrintValue containing the court log data to
     *         be printed
     */
    public CourtLogControllerPrintValue createCourtLogControllerPrintValue(XhibitApplicationController xac,
            CourtLogControllerModel clcm) throws UserCancelException, CSRecoverableException {
        CourtLogControllerPrintValue clcpv = new CourtLogControllerPrintValue();

        PrintCourtLogModel pclm = displayPrintDialog(xac);
        if (pclm.printAllCourtLog()) {
            clcpv.setCourtLogControllerPrintCompositeValue(createCourtLogEntriesPrintValues(clcm, true, null, null));
        } else if (pclm.printCourtLogRange()){
            clcpv.setCourtLogControllerPrintCompositeValue(createCourtLogEntriesPrintValues(clcm, false, pclm.getSelectedDateFrom(), pclm.getSelectedDateTo()));
        } else {
            clcpv.setCourtLogControllerPrintCompositeValue(createCourtLogEntriesPrintValues(clcm, pclm.getSelectedDate()));
        }

        return clcpv;
    }

    /**
     * Create the court log print entries for the hearings
     * 
     * @param clcm
     *            the court log model
     * @param hearingDate
     *            the date of the hearings to retrieve
     * @return the collection of HearingCourtLogEntriesPrintValues
     */
    public Collection createCourtLogEntriesPrintValues(CourtLogControllerModel clcm, boolean allDates, Date fromDate, Date toDate) throws CSRecoverableException {
        LOG.debug("Creating Court log print values for all/selected dates.");

        ArrayList courtLogEntriesPrintValueList = new ArrayList();

        Collection scheduledHearingCollection;
        if (allDates) {
        	scheduledHearingCollection = getScheduledHearings(clcm);
        } else {
        	scheduledHearingCollection = getScheduledHearingsRange(clcm, fromDate, toDate);
        }
        Iterator scheduledHearings = scheduledHearingCollection.iterator();
        while (scheduledHearings.hasNext()) {
            courtLogEntriesPrintValueList.add(createHearingCourtLogEntriesPrintValue((ScheduledHearingValue) scheduledHearings.next()));
        }

        // Loop over the events adding them to the hearing print values (can be
        // multiple), if a hearing (or a related) print value is not found
        // add a related print value to hold it.
        // Note: This could be optimised by taking advantage of the events and
        // print values being sorted by date
        CourtLogViewValue[] courtLogEntries;
        if (allDates) {
        	courtLogEntries = getCourtLogEntries(clcm); // already sorted!
        } else {
        	courtLogEntries = getCourtLogEntries(clcm, fromDate, toDate);
        }

        for (int i = 0; i < courtLogEntries.length; i++) {
            CourtLogViewValue courtLogEntry = courtLogEntries[i];
            Date courtLogEntryDate = getLowerBound(courtLogEntry.getEntryDate());

            boolean neadRelatedCourtLogEntriesPrintValue = true;

            Iterator courtLogEntriesPrintValues = courtLogEntriesPrintValueList.iterator();
            while (courtLogEntriesPrintValues.hasNext()) {
                CourtLogControllerPrintCompositeValue courtLogEntriesPrintValue = (CourtLogControllerPrintCompositeValue) courtLogEntriesPrintValues.next();
                if (courtLogEntryDate.equals(getLowerBound(courtLogEntriesPrintValue.getDate()))) {
                    courtLogEntriesPrintValue.addChargeLogItem(courtLogEntry);
                    neadRelatedCourtLogEntriesPrintValue = false;
                }
            }

            if (neadRelatedCourtLogEntriesPrintValue) {
                RelatedCourtLogEntriesPrintValue relatedCourtLogEntriesPrintValue = new RelatedCourtLogEntriesPrintValue(courtLogEntryDate);
                relatedCourtLogEntriesPrintValue.addChargeLogItem(courtLogEntry);
                courtLogEntriesPrintValueList.add(relatedCourtLogEntriesPrintValue);
            }
        }

        // Having put the court log entries into their 'boxs' we will now
        // compress the list by combining any RelatedCourtLogEntriesPrintValue
        // into a single (the first) RelatedCourtLogEntriesPrintValue. Spliting
        // the two stages like This is very ineficient but it makes it a lot
        // easier to understand what is happening
        Collection sortedCourtLogEntriesPrintValueCollection = sortCourtLogEntriesPrintValues(courtLogEntriesPrintValueList);

        RelatedCourtLogEntriesPrintValue previousRelatedCourtLogEntriesPrintValue = null;
        Iterator sortedCourtLogEntriesPrintValues = sortedCourtLogEntriesPrintValueCollection.iterator();
        while (sortedCourtLogEntriesPrintValues.hasNext()) {
            Object next = sortedCourtLogEntriesPrintValues.next();
            if (next instanceof RelatedCourtLogEntriesPrintValue) {
                RelatedCourtLogEntriesPrintValue relatedCourtLogEntriesPrintValue = (RelatedCourtLogEntriesPrintValue) next;
                if (previousRelatedCourtLogEntriesPrintValue == null) {
                    previousRelatedCourtLogEntriesPrintValue = relatedCourtLogEntriesPrintValue;
                } else {
                    previousRelatedCourtLogEntriesPrintValue.addChargeLogItems(relatedCourtLogEntriesPrintValue.getChargeLogItems());
                    sortedCourtLogEntriesPrintValues.remove();
                }
            } else { // must be instance of HearingCourtLogEntriesPrintValue
                previousRelatedCourtLogEntriesPrintValue = null;
            }
        }

        return sortedCourtLogEntriesPrintValueCollection;
    }

    /**
     * Create the court log print entries for the hearings on the specific date
     * 
     * @param clcm
     *            the court log model
     * @param hearingDate
     *            the date of the hearings to retrieve
     * @return the collection of HearingCourtLogEntriesPrintValues
     */
    public Collection createCourtLogEntriesPrintValues(CourtLogControllerModel clcm, Date hearingDate)
            throws CSRecoverableException {
        LOG.debug("Creating Court log print values for " + hearingDate + ".");
        ArrayList courtLogEntriesPrintValueList = new ArrayList();

        Collection scheduledHearingCollection = getScheduledHearings(clcm, hearingDate);
        CourtLogViewValue[] courtLogEntries = getCourtLogEntries(clcm, hearingDate, null);

        Iterator scheduledHearings = scheduledHearingCollection.iterator();
        while (scheduledHearings.hasNext()) {
            HearingCourtLogEntriesPrintValue hclepv = createHearingCourtLogEntriesPrintValue((ScheduledHearingValue) scheduledHearings
                    .next());
            hclepv.setChargeLogItems(Arrays.asList(courtLogEntries));
            courtLogEntriesPrintValueList.add(hclepv);
        }

        return sortCourtLogEntriesPrintValues(courtLogEntriesPrintValueList);
    }

    /**
     * Sort the court log entries print values
     * 
     * @param courtLogEntriesPrintValueCollection
     *            the collection to be sorted
     * @return the sorted collection
     */
    public Collection sortCourtLogEntriesPrintValues(Collection courtLogEntriesPrintValueCollection) {
        ArrayList sortedCourtLogEntriesPrintValueList = new ArrayList(courtLogEntriesPrintValueCollection);
        Collections.sort(sortedCourtLogEntriesPrintValueList);
        return sortedCourtLogEntriesPrintValueList;
    }

    /**
     * Create a HearingCourtLogEntriesPrintValue object (with completed header)
     * 
     * @param scheduledHearing
     *            the scheduled hearing to populate from
     * @return the new HearingCourtLogEntriesPrintValue object
     */
    public HearingCourtLogEntriesPrintValue createHearingCourtLogEntriesPrintValue(
            ScheduledHearingValue scheduledHearing) throws CSRecoverableException {
        HearingCourtLogEntriesPrintValue hclepv = new HearingCourtLogEntriesPrintValue(scheduledHearing.getScheduledHearingDate().getTime());
        HearingHeaderValue hhv = XhibitDelegateHelper.getHearingDelegate().getHearingHeader(
                scheduledHearing.getScheduledHearingID(),
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        hclepv.setHearingHeaderValue(hhv);
        populateHearingHeaderData(hclepv, hhv);
        return hclepv;
    }

    /**
     * Display the print dialog and return the model
     * 
     * @param the
     *            PrintCourtLogModel
     */
    public PrintCourtLogModel displayPrintDialog(XhibitApplicationController xac) throws UserCancelException,
            CSRecoverableException {
        try {
            PrintCourtLogModel pclm = new PrintCourtLogModel(xac);
            PrintCourtLogDialog pcld = new PrintCourtLogDialog(xac, pclm);
            pcld.setVisible(true);
            if (pcld.isCancelClicked()) {
                throw new UserCancelException();
            }
            return pclm;
        } catch (UserCancelException uce) {
            throw uce;
        } catch (Exception e) {
            throw new CSRecoverableException("Court_Log.printCourtLog.printCourtLogDialog", null,
                    "An error occurred in the Print Court Log Dialog", e);
        }
    }

    /**
     * Convert the value object into xml-fo using castor and xsl through Pritn
     * Services
     * 
     * @return the formated xml-fo docuemnt ready for printing
     */
    public String formatCourtLogControllerPrintValue(CourtLogControllerPrintValue value) throws CSRecoverableException {
        try {
            return CSServices.getPrintServices().getFormattedDocument(value, Locale.getDefault());
        } catch (Exception e) {
            throw new CSRecoverableException("Court_Log.printCourtLog.formatting", null,
                    "An error whilst formatting data to print the court log", e);

        }
    }

    //
    // Utility Methods
    //

    /**
     * Return a collection of the scheduled hearings for the case on that
     * particular day, there can be more than one scheduled hearing on a given
     * day.
     * 
     * @param pclm
     *            the print court log model
     * @reuturn iterator for collection of ScheduledHearingValue objecsts
     */
    // This method can obviously be optimised by adding a new finder method
    private static Collection getScheduledHearings(CourtLogControllerModel model, Date date)
            throws CSRecoverableException {
        Date maskedDate = getLowerBound(date);

        List list = new ArrayList();

        Iterator scheduledHearings = getScheduledHearings(model).iterator();
        while (scheduledHearings.hasNext()) {
            ScheduledHearingValue scheduledHearing = ((ScheduledHearingValue) scheduledHearings.next());
            if (maskedDate.equals(getLowerBound(scheduledHearing.getScheduledHearingDate()))) {
                list.add(scheduledHearing);
            }
        }

        return list;
    }

    /**
     * Return a collection of all the scheduled hearings for the case
     * 
     * @param pclm
     *            the print court log model
     * @return collection of ScheduledHearingValue objecsts
     */
    private static Collection getScheduledHearings(CourtLogControllerModel model) throws CSRecoverableException {
        return model.getAcm().getAllScheduledHearingForCase(true);
    }
    
    /**
     * Return a collection of all the scheduled hearings for the case
     * 
     * @param pclm
     *            the print court log model
     * @return collection of ScheduledHearingValue objects
     */
    private static Collection getScheduledHearingsRange(CourtLogControllerModel model, Date fromDate, Date toDate) throws CSRecoverableException {
        return model.getAcm().getScheduledHearingRangeForCase(fromDate, toDate);
    }

    /**
     * Return a collection of all the court log entries for the given case
     * 
     * @param pclm
     *            the print court log model
     * @return Array of court log entries for the case
     */
    private CourtLogViewValue[] getCourtLogEntries(CourtLogControllerModel model) {
        final CourtLogViewValue[] courtLogEntries = XhibitDelegateHelper.getCourtLogDelegate2().getCourtLog(
                model.getAcm().getCaseId());

        translateCourtLogViewValues(courtLogEntries);
        return courtLogEntries;
    }

    /**
     * Return a collection of all the court log entries for the given case on
     * the specified date or range of dates
     * 
     * @param pclm
     *            the print court log model
     * @param date
     *            the specified date
     * @return collection of court log entries for the case
     */
    private CourtLogViewValue[] getCourtLogEntries(CourtLogControllerModel model, Date date1, Date date2) {
        Date lowerBound = getLowerBound(date1);
        Date upperBound;
        if (date2 == null) {
        	upperBound = getUpperBound(date1);
        } else {
        	upperBound = getUpperBound(date2);
        }

        LOG.debug("date1: " + date1 + " lower: " + lowerBound + " upper " + upperBound);

        final CourtLogViewValue[] courtLogEntries = XhibitDelegateHelper.getCourtLogDelegate2().getCourtLog(
                model.getAcm().getCaseId(), lowerBound, upperBound);
        translateCourtLogViewValues(courtLogEntries);
        return courtLogEntries;
    }

    /**
     * Get a date for the specified calendar with the time set to 00:00:00.000
     * 
     * @param calendar
     *            the calendar to mask
     * @return the new calendar
     */
    public static Date getLowerBound(Calendar calendar) {
        return getLowerBound(calendar.getTime());
    }

    /**
     * Get a date for the specified calendar with the time set to 23:59:59.999
     * 
     * @param calendar
     *            the calendar to mask
     * @return the new calendar
     */
    public static Date getUpperBound(Calendar calendar) {
        return getUpperBound(calendar.getTime());
    }

    /**
     * Get a date for the specified timestamp with the time set to 00:00:00.000
     * 
     * @param timestamp
     *            the timestamp to mask
     * @return the new calendar
     */
    public static Date getLowerBound(Timestamp timestamp) {
        return getLowerBound(new Date(timestamp.getTime()));
    }

    /**
     * Get a date for the specified timestamp with the time set to 23:59:59.999
     * 
     * @param timestamp
     *            the timestamp to mask
     * @return the new calendar
     */
    public static Date getUpperBound(Timestamp timestamp) {
        return getUpperBound(new Date(timestamp.getTime()));
    }

    /**
     * Get a date for the specified date with the time set to 00:00:00.000
     * 
     * @param date
     *            the date
     * @return the new calendar
     */
    public static Date getLowerBound(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * Get a date for the specified date with the time set to 23:59:59.999
     * 
     * @param date
     *            the date
     * @return the new calendar
     */
    private static Date getUpperBound(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * Translate the xml in the CourtLogViewValue objects
     * 
     * @param the
     *            courtLogViewValues collection that is to be translated (note
     *            these object are mutated by this method)
     */
    private void translateCourtLogViewValues(CourtLogViewValue[] courtLogEntries) {
        for (int i = 0; i < courtLogEntries.length; i++) {
            translateCourtLogViewValue(courtLogEntries[i]);
        }
    }

    /**
     * Translate the xml in the CourtLogViewValue object
     * 
     * @param the
     *            courtLogViewValue that is translated (note this object is
     *            mutated by this method)
     */
    public void translateCourtLogViewValue(CourtLogViewValue courtLogViewValue) {
        String xml;

        try {
            xml = CourtLogXslHelper.translateEvent(courtLogViewValue, Locale.getDefault(), TranslationType.GUI);
        } catch (Exception e) {
            // Add HTML tags to the log entry and dump out the error message
            // xml = "<b>" + XHIBITConstant.getResource(
            // XhibitBundles.CourtLogResources, "translationError" ) + " " +
            // courtLogViewValue.getEventType( ).toString( ) +
            // "</b><br></td>";
        	
                 try {           	
                	 InputSource iSource = new InputSource();
                     iSource.setCharacterStream(new StringReader(courtLogViewValue.getLogEntry()));
                     Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(iSource); 
					//if its bench warrant print out in style of bench warrant 
					if(courtLogViewValue.getEventType().equals(20100)){
						xml = getBenchWarrantVal(document);
						if(xml==null) {
							xml = getTranslationError(courtLogViewValue.getEventType());
						}
					} else {
						xml = getTranslationError(courtLogViewValue.getEventType());
					}
				} catch (Exception e1) {
					xml = getTranslationError(courtLogViewValue.getEventType());
				}

                
        	
        }

        courtLogViewValue.setLogEntry(xml);
    }

	//
    // Start Of Old Code Block, This code needs commenting and improving
    //

    private void populateHearingHeaderData(HearingCourtLogEntriesPrintValue clcpcv, HearingHeaderValue hhv) {

        HearingHeaderValueHelper myHelper = new HearingHeaderValueHelper(hhv);
        // hhv.getStaffValues()

        String ccNames = getNames(myHelper.getStaffNamesByType(PersonValue.COURT_CLERK), false);
        clcpcv.setCourtClerk(ccNames);

        clcpcv.setCaseNumber(getCaseTypeAndNumber(hhv.getHhCase().getCaseType(), hhv.getHhCase().getCaseNumber()));
        clcpcv.setCourtReporter(getNames(myHelper.getStaffNamesByType(PersonValue.COURT_REPORTER), false));
        clcpcv.setDefAdvocate(getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.DEFENCE), false));
        clcpcv.setDefendants(getNames(myHelper.getDefendantNames(), false));
        clcpcv.setHearingType(hhv.getHearingType());
        clcpcv.setJudge(getNames(myHelper.getStaffNamesByType(PersonValue.JUDGE), false));
        Vector staffNames = (Vector) myHelper.getStaffNamesByType(PersonValue.JUSTICE);
        clcpcv.setJustice01(getPersonNameByPosition(staffNames, 0));
        clcpcv.setJustice02(getPersonNameByPosition(staffNames, 1));
        clcpcv.setJustice03(getPersonNameByPosition(staffNames, 2));
        clcpcv.setJustice04(getPersonNameByPosition(staffNames, 3));
        clcpcv.setLinkedCases(getLinkedCases(hhv.getCaseSchedHearingValues()));
        clcpcv.setProsAdvocate(getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.PROSECUTION), false));
        clcpcv.setRespondent(getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.RESPONDENT), false));
        clcpcv.setTimeListed(XDateFormat.format(hhv.getTimeListed(), XDateFormat.TIMEFORMAT));
        clcpcv.setObjAdvocate(getNames(myHelper.getLegalRepresentativesNamesByType(PersonValue.OBJECTOR), false));

        try {
            clcpcv.setTypeOfCase(CaseTypeHelper.determineCaseType(hhv.getHhCase().getCaseType(), hhv.getHhCase()
                    .getCaseSubType()));
        } catch (UnknownCaseTypeException ucte) {
            clcpcv.setTypeOfCase(CaseTypeHelper.Undefined_CaseType);
        }
    }

    private String getCaseTypeAndNumber(String caseType, Integer caseNumber) {
        StringBuffer buf = new StringBuffer();

        if (caseType == null) { /* Do nothing */
        } else {
            buf.append(caseType);
        }

        if (caseNumber == null) { /* Do nothing */
        } else {
            buf.append(caseNumber.toString());
        }

        return buf.toString();
    }

    private String getNames(Collection param, boolean withWordWrap) {
        StringBuffer buf = new StringBuffer();
        boolean firstName = true;

        Iterator iter = param.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
            if (firstName) {
                firstName = false;
            } else {
                buf.append(withWordWrap ? "\n" : ", ");
            }
            buf.append(item);
        }

        return buf.toString();
    }

    private String getPersonNameByPosition(Vector param, int id) {
        if (param == null) {
            return "";
        } else {
            if (param.size() > id) {
                return (String) param.get(id);
            } else {
                return "";
            }
        }
    }

    private String getLinkedCases(CaseSchedHearingValue[] array) {
        StringBuffer result = new StringBuffer();
        boolean firstItem = true;

        for (int x = 0; x < array.length; x++) {
            if (firstItem) {
                firstItem = false;
            } else {
                result.append(", ");
            }
            result.append(array[x].getCaseType() + array[x].getCaseNumber().toString());
        }

        return result.toString();
    }
    
    /**
     * Used to display an error in the court log if it can't translate
     * @param eventType the EventType
     * @return xml string with the header translation error 
     */
    private String getTranslationError(Integer eventType) {
    	return EventConstants.EVENT_HEADER_BEGIN
                + ResourceBundleHelper.getResource(XhibitBundles.CourtLogResources, "translationError") + " "
                + eventType.toString() + EventConstants.EVENT_HEADER_END;
    }
    
    /**
     * Used to return the bench warrant value if it's old and has been replaced
     * in the format of 
     * <event_header> defendant_name; valueFromPropFile </event_header>
     * <event_text>free text</event_text> Note, this is optional
     * @param document to process
     * @return xml string for bench warrant if it exists in the old file
     */
    private String getBenchWarrantVal(Document document) {
    	
    	ResourceBundle fileLookUps = CSServices.getConfigServices().getBundle("XHIBITPreviousEventsResources");
       
		if(document.getElementsByTagName("E21000_BWO_Type")!=null) {
			String text = document.getElementsByTagName("E21000_BWO_Type").item(0).getTextContent();
			String valToPrint = fileLookUps.getString(text);
			if(valToPrint !=null) {    	
		    	String xml = EventConstants.EVENT_HEADER_BEGIN;
		    	if(document.getElementsByTagName("defendant_name")!=null) {
		    		xml= xml+ document.getElementsByTagName("defendant_name").item(0).getTextContent()+"; ";
		    	}
		    	xml = xml +valToPrint+EventConstants.EVENT_HEADER_END;
				if(document.getElementsByTagName("freetext")!=null) {
					xml = xml +EventConstants.EVENT_TEXT_BEGIN
							+document.getElementsByTagName("free_text").item(0).getTextContent()
							+EventConstants.EVENT_TEXT_END;
				}
				return xml;	
			}
		}
    return null;
    }

    //
    // End Of Old Code Block
    //
}
