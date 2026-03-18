package uk.gov.courtservice.xhibit.client.listings;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.commons.lang.WordUtils;

import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCalendarCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.comparator.CourtRoomsComparator;
import uk.gov.courtservice.xhibit.client.comparator.ReceivingSitesComparator;
import uk.gov.courtservice.xhibit.client.comparator.RefSystemCodeComparator;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * Class used by listings to populate the dropdowns
 * 
 * @author harrism
 *
 */
public class ListingDropdownPopulation {

	private static final String pdLookup = "E" + PDHConstants.CASE_TRIALTIME + "_Time_Estimate_Options";
	
	private static final String YES = "Y";
	private static final List<String> REQD_JUDGE_TYPES = Arrays.asList(new String[] {"HJ","CJ"});

	/**
	 * Sets drop down values
	 * 
	 * @param codes
	 *            to return the String values to display
	 * @param displayStrings
	 *            vector of name,code type
	 * @param codeType
	 *            the codetype in the db to look up
	 * @return Vector of Strings to display in the drop down.
	 */
	static Vector<String> getCodes(Vector<String> codes, HashMap<String, String> displayStrings, String codeType) {
        if (codes != null) {
            return codes;
        }

        codes = new Vector<String>();

		if (codeType.equals(RefSystemCodeCriteria.CodeType.TICKET_TYPE)) {
        	codes.add(XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectTicketType"));
		} else if (codeType.equals(RefSystemCodeCriteria.CodeType.JUDGE_TYPE)) {
        	codes.add(XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectJudgeType"));
		} else if (codeType.equals(RefListingDataBasicValue.DataType.PREDEFINED_LIST_NOTE)) {
            codes.add(XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectPreDefinedListNote"));
        } else {
        	codes.add("Select value");
        }
        for (String crestCode : getDisplayStrings(displayStrings, codeType).keySet()) {
        	codes.add(WordUtils.capitalizeFully(crestCode));
        }

        return codes;
    }

	/**
	 * Returns the display strings for the dropdown value given the codetype
	 * passed in.
	 * 
	 * @param displayStrings
	 *            HashMap that will store the values
	 * @param codeType
	 *            String representation of the code type
	 * @return display strings for drop down.
	 */
	static HashMap<String, String> getDisplayStrings(HashMap<String, String> displayStrings, String codeType) {
        if (displayStrings != null) {
            return displayStrings;
        }

		displayStrings = new HashMap<String, String>();

        try {
            RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(codeType);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            ArrayList defCatCodes = (ArrayList) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));

            for (int i = 0; i < defCatCodes.size(); i++) {
                String crestCode = ((RefSystemCodeBasicValue) (defCatCodes.get(i))).getCode();
                displayStrings.put(((RefSystemCodeBasicValue) (defCatCodes.get(i))).getDecode(), crestCode);
            }
        } catch (Exception e) {
            XHIBITConstant.handleError(e);
        }

        return displayStrings;
    }

	/**
	 * Returns the hearing types.
	 * 
	 * @return hearing types
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<RefHearingTypeBasicValue> getHearingTypes() {
		ArrayList<RefHearingTypeBasicValue> results =  new ArrayList<RefHearingTypeBasicValue>();
		RefHearingTypeBasicValue defValue = new RefHearingTypeBasicValue();
		defValue.setHearingTypeDesc(
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectHearingType"));
		try {
			results = (ArrayList<RefHearingTypeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate()
					.findHearingTypesByCourtId(XhibitSingleton.getInstance().getCourtId()));
            Sorter.sort((List) results, new String[] { "hearingTypeDesc" });
		} catch (BisRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		results.add(0, defValue);
		return results;
	}

	/**
	 * Returns the Case Type
	 * 
	 * @return case types
	 */
	public static ArrayList<DropdownCodeStringValue> getCaseTypeCodes() {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue("All", "", null));
		results.add(new DropdownCodeStringValue("T", "COMMITTAL FOR TRIAL","T"));
		results.add(new DropdownCodeStringValue("S", "COMMITTAL FOR SENTENCE","S"));
		results.add(new DropdownCodeStringValue("A", "CRIMINAL APPEAL ","A"));
		return results;
	}
	
	/**
	 * Returns the list type basic value for the list type enum.
	 * 
	 * @param listType list type enum
	 * @return list type basic value
	 */
	public static RefListingDataBasicValue getListType(ListTypeEnum listType) {
		RefListingDataBasicValue retVal = null;
		for (RefListingDataBasicValue value : getListTypes()) {
			if (listType.name().equals(value.getRefDataValue())) {
				retVal = value;
				break;
			}
		}
		return retVal;
	}

	/**
	 * Returns the list type enum for the list type id.
	 * 
	 * @param listTypeId list type id
	 * @return list type enum
	 */
	public static ListTypeEnum getListType(Integer listTypeId) {
		ListTypeEnum retVal = null;
		for (RefListingDataBasicValue value : getListTypes()) {
			if (listTypeId.equals(value.getRefListingDataId())) {
				retVal = ListTypeEnum.valueOf(value.getRefDataValue());
				break;
			}
		}
		return retVal;
	}
	
	/**
	 * Returns the list types for listings.
	 * 
	 * @return list types
	 */
	public static ArrayList<RefListingDataBasicValue> getListTypes() {
		ArrayList<RefListingDataBasicValue> results = getGenericListingData(
				RefListingDataBasicValue.DataType.LIST_TYPE, null,
				new GenericRefListingDataComparator());
		return results;
	}

	/**
	 * Returns the note classifications for listings.
	 * 
	 * @return note classifications
	 */
	public static ArrayList<RefListingDataBasicValue> getNoteClassifications() {
		ArrayList<RefListingDataBasicValue> results = getGenericListingData(
				RefListingDataBasicValue.DataType.NOTE_CLASSIFICATION,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectNoteClassification"),
				new GenericRefListingDataComparator());
		return results;
	}
	
	/**
	 * Returns the note types for listings.
	 * 
	 * @return note types
	 */
	public static ArrayList<RefListingDataBasicValue> getNoteTypes() {
		ArrayList<RefListingDataBasicValue> results = getGenericListingData(
				RefListingDataBasicValue.DataType.NOTE_TYPE,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectNoteType"),
				new GenericRefListingDataComparator());
		return results;
	}

	/**
	 * Returns the CalendarsDays.
	 * 
	 * @return code - refCalendars
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<RefCalendarBasicValue> getRefCalendar(Calendar startDate) {
		ArrayList<RefCalendarBasicValue> refCalendars = new ArrayList<RefCalendarBasicValue>();
		try {
			Timestamp startTimestamp = new Timestamp(startDate.getTime().getTime());
			RefCalendarCriteria criteria = new RefCalendarCriteria();
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			criteria.setStartDate(startTimestamp);
			refCalendars = (ArrayList<RefCalendarBasicValue>) XhibitDelegateHelper.getBizRefDelegate()
					.findCalendarDays(criteria);
		} catch (SysRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		Collections.sort(refCalendars, new RefCalendarComparator());
    	return refCalendars;
	}

	/**
	 * Returns the CalendarsDays.
	 * 
	 * @return code - refCalendars
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<RefCalendarBasicValue> getRefCalendar(Calendar startDate, Calendar endDate) {
		ArrayList<RefCalendarBasicValue> refCalendars = new ArrayList<RefCalendarBasicValue>();
		try {
			refCalendars = (ArrayList<RefCalendarBasicValue>) XhibitDelegateHelper.getBizRefDelegate()
								.getRefCalendarDatesByCourt(XhibitSingleton.getInstance().getCourtId(),
															startDate.getTime(), endDate.getTime());
		} catch (SysRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		Collections.sort(refCalendars, new RefCalendarComparator());
    	return refCalendars;
	}
	
    /**
     * Get the ref calendar for the particular date from the list
     * which may be null as it is possible for one not to exist.
     * 
     * @param refCalendars
     * @param calendar
     * @return
     */
    public static RefCalendarBasicValue getRefCalendar(List<RefCalendarBasicValue> refCalendars, Calendar calendar) {
    	RefCalendarBasicValue matchingDay = null;

    	// Ensure ref calendars are sorted by date ascending
		Collections.sort(refCalendars, new RefCalendarComparator());
		
		// Ensure supplied date has no time for comparison
		Calendar day = DateTimeUtilities.stripTimeToCalendar(calendar.getTime());
		
		// Search for ref calendar in list which matches supplied date
    	for (RefCalendarBasicValue refCalendar : refCalendars) {
    		Calendar refCal = DateTimeUtilities.convertToCalendar(refCalendar.getCalDate());
    		
    		// if the next day is the date, exit loop with matching day 
    		if (refCal.compareTo(day) == 0) {
    			matchingDay = refCalendar;
    			break;
    		}
    		// if the next day is after the date, exit loop with no matching day
    		else if (refCal.compareTo(day) > 0) {
    			break;
    		}
    	}
    	return matchingDay;
    }

	/**
	 * Return whether the date is a working day based on the supplied
	 * calendar days retrieved from one of the getRefCalendar methods.
	 * 
	 * @param refCalendars
	 * @param calendar
	 * @return
	 */
	public static boolean isWorkingDay(List<RefCalendarBasicValue> refCalendars, Calendar calendar) {
    	// Check list of ref calendars for a matching date
    	RefCalendarBasicValue matchingDay = getRefCalendar(refCalendars, calendar);

    	// Return whether date is a working date using following rules:
    	// 1) check matching day avail flag if matching day exists
    	// 2) weekends default to non working day if no matching day
    	// 3) weekdays default to working day if no matching day
    	boolean isWorkingDay = false;
    	if (matchingDay != null) {
    		isWorkingDay = YES.equals(matchingDay.getAvail());
    	} else {
    		isWorkingDay = !DateTimeUtilities.isWeekend(calendar);
    	}
    	
    	return isWorkingDay;
	}
	
	/*
	 * Ref comparator
	 */
	private static class RefCalendarComparator implements Comparator<RefCalendarBasicValue> {

		@Override
		public int compare(RefCalendarBasicValue o1, RefCalendarBasicValue o2) {
			return o1.getCalDate().compareTo(o2.getCalDate());
		}
	}

	/**
	 * Returns the pre-defined notes for listings.
	 * 
	 * @return pre-defined notes
	 */
	public static ArrayList<RefListingDataBasicValue> getPreDefinedNotes() {
		ArrayList<RefListingDataBasicValue> results = getGenericListingData(
				RefListingDataBasicValue.DataType.PREDEFINED_LIST_NOTE,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectPreDefinedListNote"),
				new GenericRefListingDataComparator());
		return results;
	}

	/**
	 * Returns the Court Sites.
	 * 
	 * @return code - site
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CourtSiteBasicValue> getCourtSites() {
		ArrayList<CourtSiteBasicValue> courtSites = new ArrayList<CourtSiteBasicValue>();
		try {
			CourtSiteCriteria criteria = new CourtSiteCriteria();
			criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			courtSites = (ArrayList<CourtSiteBasicValue>) XhibitDelegateHelper.getBizRefDelegate()
					.findCourtSites(criteria);
		} catch (SysRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		Collections.sort(courtSites, new ReceivingSitesComparator());
    	return courtSites;
	}
	
	/**
	 * Returns the Court Rooms for a given court site
	 * @param courtSiteId	Court site to retrieve court rooms for
	 * @return list of court rooms
	 */
	@SuppressWarnings("unchecked")
	public static ArrayList<CourtRoomBasicValue> getCourtRooms(final Integer courtSiteId) {
		ArrayList<CourtRoomBasicValue> courtRooms = new ArrayList<CourtRoomBasicValue>();
		try {
			CourtRoomCriteria crc = new CourtRoomCriteria();
			crc.setCourtSiteId(courtSiteId.toString());
            courtRooms = (ArrayList<CourtRoomBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findCourtRooms(crc));

		} catch (SysRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		Collections.sort(courtRooms, new CourtRoomsComparator());
		return courtRooms;
	}

	/**
	 * Returns the Required Judge Types.
	 * 
	 * @return judge types
	 */
	public static ArrayList<RefSystemCodeBasicValue> getRequiredJudgeTypes() {
		ArrayList<RefSystemCodeBasicValue> results = new ArrayList<RefSystemCodeBasicValue>();
		ArrayList<RefSystemCodeBasicValue> allJudgeTypes = getJudgeTypes();
		for (RefSystemCodeBasicValue judgeType : allJudgeTypes) {
			if (judgeType.getCode() == null || REQD_JUDGE_TYPES.contains(judgeType.getCode())) {
				results.add(judgeType);
			}
		}
		return results;
	}

	/**
	 * Returns the Judge Type.
	 * 
	 * @return judge types
	 */
	public static ArrayList<RefSystemCodeBasicValue> getJudgeTypes() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(RefSystemCodeCriteria.CodeType.JUDGE_TYPE,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectJudgeType"),
				new GenericSystemRefComparator());
		return results;
	}
	
	/**
	 * Returns the Judge Type with no inserted 'select' value.
	 * 
	 * @return judge types
	 */
	public static ArrayList<RefSystemCodeBasicValue> getRefJudgeTypes() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(	RefSystemCodeCriteria.CodeType.JUDGE_TYPE,
																			new GenericSystemRefComparator());
		return results;
	}
	
	/**
	 * Returns the Judge Ticket Types with no inserted 'select' value.
	 * 
	 * @return judge types
	 */
	public static ArrayList<RefSystemCodeBasicValue> getRefJudgeTicketTypes() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(	RefSystemCodeCriteria.CodeType.TICKET_TYPE,
																			new GenericSystemRefComparator());
		return results;
	}
	
	/**
	 * Returns the B/C Status.
	 * 
	 * @return b/c status
	 */
	public static ArrayList<DropdownCodeStringValue> getBcStatus() {
		return getBcStatus(false);
	}

	/**
	 * Returns the B/C Status. Limiting by report criteria (ie like OUTC report)
	 * 
	 * @return b/c status
	 */
	public static ArrayList<DropdownCodeStringValue> getBcStatus(boolean isLimitedReportingCriteria) {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue("Select B/C Status", ""));
		results.add(new DropdownCodeStringValue("Bail", "B"));
		results.add(new DropdownCodeStringValue("In Custody", "C"));
		if (!isLimitedReportingCriteria) {
			results.add(new DropdownCodeStringValue("In Care", "J")); 
		}
		results.add(new DropdownCodeStringValue("Not Applicable", "N"));
		return results;
	}
	
	/**
	 * Returns the ctl Applied.
	 * 
	 * @return ctl Applied values
	 */
	public static ArrayList<DropdownCodeStringValue> getCTLAppledValues() {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue(" ", ""));
		results.add(new DropdownCodeStringValue("Yes", "Y"));
		results.add(new DropdownCodeStringValue("No", "N"));
		
		return results;
	}
	
	/**
	 * Returns the Time formats.
	 * 
	 * @return time formats
	 */
	public static ArrayList<RefSystemCodeBasicValue> getTimeFormatTypes() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(RefSystemCodeCriteria.CodeType.EXHIBIT_TIME_FORMAT,
				XHIBITConstant.getResource(XhibitBundles.Listings, "listingDropdownSelectTimeMarking"),
				new GenericSystemRefComparator());
		return results;
	}

	/**
	 * Return the list of ref data values for the 
	 * @param refDataType
	 * @return ref listing data values
	 * @throws BisRefControllerException 
	 */
	public static ArrayList<String> getRefListingDataValues(final String refDataType) throws BisRefControllerException {
		@SuppressWarnings("unchecked")
		ArrayList<RefListingDataBasicValue> values = (ArrayList<RefListingDataBasicValue>)
					XhibitDelegateHelper.getBizRefDelegate().findRefListingData(refDataType);
		ArrayList<String> results = new ArrayList<String>();
		for (RefListingDataBasicValue value : values) {
			results.add(value.getRefDataValue());
		}
		return results;
	}

	/**
	 * Returns the system codes for the passed in type.
	 * Adds the supplies default value to the list
	 * 
	 * @return system codes
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RefSystemCodeBasicValue> getGenericSystemCodes(final String codeType,
			final String defaultValue, final Comparator<? super RefSystemCodeBasicValue> comparator) {
		ArrayList<RefSystemCodeBasicValue> results = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode(defaultValue);
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(codeType);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			results = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate()
					.findSystemCodes(criteria));
            Collections.sort(results, comparator);
		} catch (BisRefControllerException e) {
			XHIBITConstant.handleError(e);
		}

		results.add(0, defValue);
		
		return results;
	}
	
	/**
	 * Returns the system codes for the passed in type.
	 * 
	 * @return system codes
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RefSystemCodeBasicValue> getGenericSystemCodes(final String codeType, final Comparator<? super RefSystemCodeBasicValue> comparator) {
		ArrayList<RefSystemCodeBasicValue> results = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(codeType);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
			results = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate()
					.findSystemCodes(criteria));
            Collections.sort(results, comparator);
		} catch (BisRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		
		return results;
	}

	/**
	 * Returns the listing reference for the passed in type.
	 * 
	 * @return listing codes
	 */
	@SuppressWarnings("unchecked")
	private static ArrayList<RefListingDataBasicValue> getGenericListingData(final String codeType,
			final String defaultValue, final Comparator<? super RefListingDataBasicValue> comparator) {
		ArrayList<RefListingDataBasicValue> results = new ArrayList<RefListingDataBasicValue>();
		RefListingDataBasicValue defValue = new RefListingDataBasicValue();
		defValue.setRefDataValue(defaultValue);
		try {
			results = (ArrayList<RefListingDataBasicValue>) (XhibitDelegateHelper.getBizRefDelegate()
					.findRefListingData(codeType));
            Collections.sort(results, comparator);
		} catch (BisRefControllerException e) {
			XHIBITConstant.handleError(e);
		}
		results.add(0, defValue);
		return results;
	}

	/**
	 * Returns the time estimate units.
	 * 
	 * @return time estimate units
	 */
    @SuppressWarnings("unchecked")
	public static Vector getTimeEstimateUnits() {
    	// Get the restrictions from 40711.xsd file
		Collection restrictions = RestrictionFinder
				.getRestrictingValues(PDHConstants.CASE_TRIALTIME.toString() + ".xsd", pdLookup);
		// Build the vector
		Vector results = new Vector();
        Iterator iter = restrictions.iterator();
        while (iter.hasNext()) {
            String item = (String) iter.next();
			ComboHelperVO cvo = new ComboHelperVO(item,
					XHIBITConstant.getResource(XhibitBundles.Directions, item + "_DB"),
					XHIBITConstant.getResource(XhibitBundles.Directions, item));
            results.add(cvo);
        }
        Collections.sort(results, new TimeEstimateUnitComparator());
        return results; 
    }
    
    /**
	 * Returns a list of reasons for vacating fixed hearing
	 * 
	 * @return reasons for vacating fixed hearing
	 */
	public static ArrayList<RefSystemCodeBasicValue> getFixtureVacationReasons() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(RefSystemCodeCriteria.CodeType.REASON_REMOVED,
				" ",
				new RefSystemCodeComparator());
		return results;
	}

	/*
	 * Generic System Ref comparator
	 */
	private static class GenericSystemRefComparator implements Comparator<RefSystemCodeBasicValue> {

		@Override
		public int compare(RefSystemCodeBasicValue o1, RefSystemCodeBasicValue o2) {
			return o1.getDecode().compareTo(o2.getDecode());
		}
	}

	/*
	 * Generic Reference Listing Data comparator
	 */
	private static class GenericRefListingDataComparator implements Comparator<RefListingDataBasicValue> {

		@Override
		public int compare(RefListingDataBasicValue o1, RefListingDataBasicValue o2) {
			return o1.getRefDataValue().compareTo(o2.getRefDataValue());
		}
	}

	/*
	 * Time Estimate comparator
	 */
	private static class TimeEstimateUnitComparator implements Comparator<ComboHelperVO> {

		private static final String DAYS_DBVALUE = "2"; 
		
		private Integer getPriority(ComboHelperVO object) {
			Integer result = Integer.valueOf(99);
			if (DAYS_DBVALUE.equals(object.getDbValue())) {
				result = Integer.valueOf(1);
			}  
			return result;
		}
		@Override
		public int compare(ComboHelperVO o1, ComboHelperVO o2) {
			// Sort by priority...
			Integer o1Priority = getPriority(o1);
			Integer o2Priority = getPriority(o2);
			Integer diff = o1Priority.compareTo(o2Priority);
			// ...then Sort by db value order
			if (Integer.valueOf(0).equals(diff)) {
				diff = o1.getDbValue().compareTo(o2.getDbValue());
			}
			return diff;
		}
	}
}