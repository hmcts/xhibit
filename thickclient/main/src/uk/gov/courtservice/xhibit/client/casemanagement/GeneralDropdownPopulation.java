package uk.gov.courtservice.xhibit.client.casemanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.commons.lang.WordUtils;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.SysRefControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefMonitoringCategoryBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.comparator.MagistrateCourtDetailsComparator;
import uk.gov.courtservice.xhibit.client.comparator.MonitoringCategoryComparator;
import uk.gov.courtservice.xhibit.client.comparator.PoliceForceComparator;
import uk.gov.courtservice.xhibit.client.comparator.ReceivingSitesComparator;
import uk.gov.courtservice.xhibit.client.comparator.TicketTypeComparator;
import uk.gov.courtservice.xhibit.client.util.DropdownCodeStringValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
//import org.apache.commons.lang.WordUtils;

/**
 * Class used by general tabs to populate the dropdowns
 * @author waltersn
 *
 */
public class GeneralDropdownPopulation {

	 private static final String MAGISTRATES_CRESTCODE_MIN = "orders.magistrates.crestcode.min";

	    private static final String MAGISTRATES_CRESTCODE_MAX = "orders.magistrates.crestcode.max";
	    
	/**
	 * Sets drop down values
	 * @param codes to return the String values to display
	 * @param displayStrings vector of name,code type 
	 * @param codeType the codetype in the db to look up
	 * @return Vector of Strings to display in the drop down.
	 */
	static Vector<String> getCodes(Vector<String> codes, HashMap<String,String> displayStrings, String codeType) {
        if (codes != null) {
            return codes;
        }
        
        codes = new Vector<String>();

        if(codeType.equals("CASE_APPEAL_TYPE")) {
        	codes.add("Select Appeal Type");
        }
        else if(codeType.equals("HO_PSD_HRG_TYPE")) {
        	codes.add("Select Hearing Type");
        }
       
        else {
        	codes.add("Select value");
        }
        for (String crestCode : getDisplayStrings(displayStrings, codeType).keySet()) {
        	codes.add(WordUtils.capitalizeFully(crestCode));
        }
        
        return codes;
    }
	
	/**
	 * Returns the display strings for the dropdown value given the codetype passed in.
	 * @param displayStrings HashMap that will store the values
	 * @param codeType String representation of the code type
	 * @return display strings for drop down.
	 */
	static HashMap<String,String> getDisplayStrings(HashMap<String,String> displayStrings, String codeType) {
        if (displayStrings != null) {
            return displayStrings;
        }
        
        displayStrings = new HashMap<String,String>();

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
	 * Returns the dropdown value for ReceivedFrom
	 * @param codes to store values
	 * @return Hashmap of code, court full name
	 */
	static ArrayList<RefCourtBasicValue> getReceivedFrom () {
    	RefCourtBasicValue defValue = new RefCourtBasicValue();
    	defValue.setCourtFullName("Select Magistrates Court");

    	ArrayList<RefCourtBasicValue> tempVal = (ArrayList<RefCourtBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findByCourtIdAndIsPSD("Y", XhibitSingleton.getInstance().getCourtId()));
   		Collections.sort(tempVal, new MagistrateCourtDetailsComparator());
		
   		tempVal.add(0,defValue);
		return tempVal;

	}
	
	public static RefCourtBasicValue[] getCourts() {
		return getCourts(true);
	}
	
	public static RefCourtBasicValue[] getCourts(boolean removeCurrentCourt) {
		BisRefControllerBeanBusinessDelegate bisRefDelegate = XhibitDelegateHelper.getBizRefDelegate();
		
		RefCourtCriteria refCourtCriteria = new RefCourtCriteria();
		refCourtCriteria.setCourtPrefix("Crown Court");
		refCourtCriteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
		ArrayList<RefCourtBasicValue> courtList = null;
		try {
			courtList = (ArrayList<RefCourtBasicValue>) bisRefDelegate.findCourts(refCourtCriteria);
		} catch (BisRefControllerException e) {
			e.printStackTrace();
		}
		
		ArrayList<RefCourtBasicValue> courtListEdited = new ArrayList<RefCourtBasicValue>();
		
		int i = 0;
	
		// Add default value of "Select Crown Court"
		RefCourtBasicValue firstEntry = new RefCourtBasicValue();
		firstEntry.setCourtFullName("");
		courtListEdited.add(firstEntry);
		
		while(i < courtList.size()) {
			try {
				if(removeCurrentCourt) {
					if(courtList.get(i) != null && courtList.get(i).getCourtShortName() != null && 
							!courtList.get(i).getCourtShortName().equals(XhibitSingleton.getInstance().getCourtBasicValue().getShortName())) {
						courtListEdited.add(courtList.get(i));
					}
				} else {
					if(courtList.get(i) != null) {
						courtListEdited.add(courtList.get(i));
					}
				}
			} catch (CSRecoverableException e) {
				return null;
			}
			i++;
		}
		
		Sorter.sort(courtListEdited, new String[] { "courtFullName" }, Sorter.ASCENDING);
		return (RefCourtBasicValue[]) courtListEdited.toArray(new RefCourtBasicValue[courtListEdited.size()]);
	}
	
	static ArrayList<RefSystemCodeBasicValue> getTicketType() {
		ArrayList<RefSystemCodeBasicValue> ticketTypeCodes = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Ticket Type");
		//defValue.setId(-1);
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.TICKET_TYPE);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            ticketTypeCodes = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(ticketTypeCodes, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ticketTypeCodes.add(0, defValue);
		return ticketTypeCodes;
	}
	
	static ArrayList<RefSystemCodeBasicValue> getAppealType() {
		ArrayList<RefSystemCodeBasicValue> appealType = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Appeal Type");
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.APPEAL);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            appealType = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(appealType, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		appealType.add(0, defValue);
		return appealType;
	}

	
	static ArrayList<RefSystemCodeBasicValue> getHearingType() {
		ArrayList<RefSystemCodeBasicValue> hearingType = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Hearing Type");
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.MAG_COURT_HEARING_TYPE);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            hearingType = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(hearingType, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		hearingType.add(0, defValue);
		return hearingType;
	}
	

	public static ArrayList<RefSystemCodeBasicValue> getPoliceForceCode() {
		ArrayList<RefSystemCodeBasicValue> policeCodes = new ArrayList<RefSystemCodeBasicValue>();
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.POLICE_FORCE);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            policeCodes = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(policeCodes, new PoliceForceComparator());
        }
		catch (BisRefControllerException e) {
			e.printStackTrace();
		}
		return policeCodes;
	}

	
	/**
	 * Used on the public representation panel for revocation reason
	 * @return arraylist of values
	 */
	public static ArrayList<RefSystemCodeBasicValue> getReasonsForRevocation() {
		ArrayList<RefSystemCodeBasicValue> revocationType = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Revocation Type");
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.REVOCATION_REASON);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            revocationType = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(revocationType, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		revocationType.add(0, defValue);
		return revocationType;
	}


	/**
	 * Returns the receiving type in form of  Code - Site name. 
	 * @param site Vector to store the values in
	 * @return code - site
	 */
	static ArrayList<CourtSiteBasicValue> getReceivingSite() {
		ArrayList<CourtSiteBasicValue> courtSites = new ArrayList<CourtSiteBasicValue>();
		   try {
		    	courtSites = (ArrayList<CourtSiteBasicValue>)XhibitDelegateHelper.getBizRefDelegate().findHomeCourtAndSatellites(XhibitSingleton.getInstance().getCourtId());
		   } catch (Exception e) {
			   XHIBITConstant.handleError(e);
		   }
		   Collections.sort(courtSites, new ReceivingSitesComparator());
	    	return courtSites;

	}
	
	/**
	 * Returns the monitoring category. 
	 * @param Vector to store the values in
	 * @return monitoring category
	 */
	static ArrayList<RefMonitoringCategoryBasicValue> getMonitoringCategory() {
	    ArrayList<RefMonitoringCategoryBasicValue> monCategory = new ArrayList<RefMonitoringCategoryBasicValue>();
		RefMonitoringCategoryBasicValue defValue = new RefMonitoringCategoryBasicValue();
		defValue.setMonitoringCategoryName("Select Monitoring Category");
		try {
			monCategory = (ArrayList<RefMonitoringCategoryBasicValue>)XhibitDelegateHelper.getBizRefDelegate().findAllMonitoringCategories();
		}
		catch (SysRefControllerException e) {
			e.printStackTrace();
		}
		Collections.sort(monCategory,new MonitoringCategoryComparator());
		monCategory.add(0, defValue);
	    return monCategory;
	}
	

	@SuppressWarnings("unchecked")
	public static ArrayList<RefSystemCodeBasicValue> getReceiptTypes(String codeType) {
		ArrayList<RefSystemCodeBasicValue> receiptTypes = new ArrayList<RefSystemCodeBasicValue>();
		RefSystemCodeBasicValue defValue = new RefSystemCodeBasicValue();
		defValue.setDecode("Select Receipt Type");
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
		    criteria.setCodeType(codeType);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            receiptTypes = (ArrayList<RefSystemCodeBasicValue>)(XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(receiptTypes, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			e.printStackTrace();
		}
		receiptTypes.add(0, defValue);
		return receiptTypes;
	}
	
	@SuppressWarnings("unchecked")
	public static ArrayList<RefSystemCodeBasicValue> getBroadcastTypes() {
		ArrayList<RefSystemCodeBasicValue> revocationType = new ArrayList<RefSystemCodeBasicValue>();
		try {
		    RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.TELE_APP_REFUSED);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            revocationType = (ArrayList<RefSystemCodeBasicValue>) (XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria));
            Collections.sort(revocationType, new TicketTypeComparator());
        }
		catch (BisRefControllerException e) {
			XHIBITErrorHandler.handleError(e, null);
		}
		return revocationType;
	}
	
	public static ArrayList<DropdownCodeStringValue> getYesNo(boolean includeNull) {
		ArrayList<DropdownCodeStringValue> results = new ArrayList<DropdownCodeStringValue>();
		if (includeNull) {
			results.add(new DropdownCodeStringValue(" ", null, null));
		}
		results.add(new DropdownCodeStringValue("Yes", "Y","Y"));
		results.add(new DropdownCodeStringValue("No", "N","N"));
		return results;
	}
	
}
