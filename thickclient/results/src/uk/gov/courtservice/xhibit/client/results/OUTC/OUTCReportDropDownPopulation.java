package uk.gov.courtservice.xhibit.client.results.OUTC;

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

import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.ComboHelperVO;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCalendarBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefHearingTypeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefListingDataBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCalendarCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: OUTCReportDropDownPopulation
 * </p>
 * <p>
 * Description: Class used by Reports to populate the dropdowns
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Ervin P
 * @version 1.0
 */

public class OUTCReportDropDownPopulation {
	 /** Event 40711 */
    private static final Integer CASE_TRIALTIME = new Integer(40711);
	
	private static final String pdLookup = "E" + CASE_TRIALTIME + "_Time_Estimate_Options";
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
        for (String crestCode  : getDisplayStrings(displayStrings, codeType).keySet()) {
        	codes.add((crestCode).toUpperCase());
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

	@SuppressWarnings("unchecked")
	public static ArrayList<RefHearingTypeBasicValue> getHearingTypes() {
		ArrayList<RefHearingTypeBasicValue> results =  new ArrayList<RefHearingTypeBasicValue>();
		RefHearingTypeBasicValue defValue = new RefHearingTypeBasicValue();
		defValue.setHearingTypeDesc(
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_dropdowndefault_title"));
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
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_dropdowndefault_title"),
				new GenericSystemRefComparator());
		return results;
	}
	
	/**
	 * Returns the Case Type.
	 * 
	 * @return case types
	 */
	public static ArrayList<RefSystemCodeBasicValue> getCaseTypes() {
		ArrayList<RefSystemCodeBasicValue> results = getGenericSystemCodes(RefSystemCodeCriteria.CodeType.CASE_TYPE,
				XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "OUTCReport.panel_dropdowndefault_title"),
				new GenericSystemRefComparator());
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
	 * Returns the B/C Status.
	 * 
	 * @return b/c status
	 */
	public static ArrayList<DropdownCodeStringValue> getBcStatus() {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		results.add(new DropdownCodeStringValue("Any","", null));
		results.add(new DropdownCodeStringValue("Bail", "B","B"));
		results.add(new DropdownCodeStringValue("In Custody", "C","C")); 
		results.add(new DropdownCodeStringValue("Not Applicable", "N","N/A"));
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
	 * Returns the system codes for the passed in type.
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
	 * Returns the time estimate units.
	 * 
	 * @return time estimate units
	 */
    @SuppressWarnings("unchecked")
	public static Vector getTimeEstimateUnits() {
    	// Get the restrictions from 40711.xsd file
    	 /** Event 40711 */
		Collection restrictions = RestrictionFinder
				.getRestrictingValues(CASE_TRIALTIME.toString() + ".xsd", pdLookup);
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
				new GenericSystemRefComparator());
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
}

