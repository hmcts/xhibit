package uk.gov.courtservice.xhibit.business.services.publicdisplay.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.impl.GenericPublicDisplayDataSource;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.AbstractCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.AllCaseStatusCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.AllCourtStatusCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.CourtDetailCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.CourtListCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.DailyListCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.JuryCurrentStatusCppToPublicDisplay;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit.SummaryByNameCppToPublicDisplay;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;

public final class CppDataSourceFactory {
	
	public static interface DataType {
        public static final String COURTDETAIL_TYPE = "CourtDetail";
        public static final String COURTLIST_TYPE = "CourtList";
        public static final String DAILYLIST_TYPE = "DailyList";
        public static final String ALLCOURTSTATUS_TYPE = "AllCourtStatus";    
        public static final String SUMMARYBYNAME_TYPE = "SummaryByName";    
        public static final String ALLCASETSTATUS_TYPE = "AllCaseStatus";    
        public static final String JURYCURRENTSTATUS_TYPE = "JuryCurrentStatus";    
    } 

	 /**
     * Returns a DataSource for the document specified.
     * 
     * @param uri
     *            the uri of the document we need the data for.
     * 
     * @return a DataSource.
     * 
     * @post return != null
     * @pre uri != null
     * @pre uri.getDocumentType() != null
     */
    public static AbstractCppToPublicDisplay getDataSource(String shortName, Date date, int courtId, int[] courtRoomIds){
    	
    	final Logger log = CSServices.getLogger(GenericPublicDisplayDataSource.class);
    	
    	AbstractCppToPublicDisplay cppToPubDisp = null;
    	
    	if(shortName != null && shortName.equals(DataType.COURTDETAIL_TYPE)){
    		cppToPubDisp = new CourtDetailCppToPublicDisplay(date, courtId, courtRoomIds);
    	}
    	else if(shortName != null && shortName.equals(DataType.COURTLIST_TYPE)){
    		cppToPubDisp = new CourtListCppToPublicDisplay(date, courtId, courtRoomIds);
    	}
    	else if(shortName != null && shortName.equals(DataType.DAILYLIST_TYPE)){
        	cppToPubDisp = new DailyListCppToPublicDisplay(date, courtId, courtRoomIds);
        }
    	else if(shortName != null && shortName.equals(DataType.ALLCOURTSTATUS_TYPE)){
        	cppToPubDisp = new AllCourtStatusCppToPublicDisplay(date, courtId, courtRoomIds);
        }
    	else if(shortName != null && shortName.equals(DataType.SUMMARYBYNAME_TYPE)){
        	cppToPubDisp = new SummaryByNameCppToPublicDisplay(date, courtId, courtRoomIds);
        }
    	else if(shortName != null && shortName.equals(DataType.ALLCASETSTATUS_TYPE)){
        	cppToPubDisp = new AllCaseStatusCppToPublicDisplay(date, courtId, courtRoomIds);
        }
    	else if(shortName != null && shortName.equals(DataType.JURYCURRENTSTATUS_TYPE)){
        	cppToPubDisp = new JuryCurrentStatusCppToPublicDisplay(date, courtId, courtRoomIds);
        }
    	else{
    		// document type is either null or not known
    		log.error("AbstractCppToPublicDisplay.getDataSource - " + shortName + " - not known as a document type.");
    	}
    	
    	return cppToPubDisp;
    }
    
    @SuppressWarnings("unchecked")
	public static Collection postProcessing(String shortName, Collection data) {
    	
    	final Logger log = CSServices.getLogger(GenericPublicDisplayDataSource.class);
    	
    	if(shortName != null && shortName.equals(DataType.COURTDETAIL_TYPE)){
    		// Sort the collection by court site, court room and then event time descending
        	Collections.sort((List<CourtDetailValue>) data);
        	
        	// Remove Duplicates
        	ArrayList<CourtDetailValue> newList = new ArrayList<CourtDetailValue>();
        	int previousRoomNo = -1;
        	String previousCourtSiteCode = "";
        	boolean matchingFound;
        	for ( CourtDetailValue value : (ArrayList<CourtDetailValue>) data ) {
        		// First check we're not processing the same court room at the same court site multiple times
        		if ( previousRoomNo != value.getCrestCourtRoomNo() || 
        			 (previousRoomNo == value.getCrestCourtRoomNo() && !previousCourtSiteCode.equals(value.getCourtSiteCode())) ) {

	        		// Get all matching elements
	        		ArrayList<CourtDetailValue> matchingList = (ArrayList<CourtDetailValue>) findAllObjects(value, (ArrayList<CourtDetailValue>) data);
	        		if ( matchingList.size() == 1 ) {
	        			newList.add(value);
	        		}
	        		else if ( matchingList.size() > 1 ) {
	        			// Multiple matching courtroom records found. The collection is sorted to have the most recent first
	        			// Find the most recent record with information to display
	        			matchingFound = false; 
	        			for ( CourtDetailValue matchingValue : matchingList ) {
	        				if ( matchingValue.hasInformationForDisplay() ) {
	        					newList.add(matchingValue);
	        					matchingFound = true;
	        					break;
	        				}
	        			}
	        			
	        			// If no matching record with information to display, then just use the first in the list
	        			if ( !matchingFound ) {
	        				newList.add(matchingList.get(0));
	        			}
	        		}
	        		
        			// Update the previous room number and court site code
        			previousRoomNo = value.getCrestCourtRoomNo();
        			previousCourtSiteCode = value.getCourtSiteCode();
        		}
        	}
        	return newList;
    	}
    	else if(shortName != null && shortName.equals(DataType.COURTLIST_TYPE)){
    		// Sort the collection by not before time
    		if( null != data && data.size() > 1){
    			// Sort the collection by Not Before Time
            	Collections.sort((List<CourtListValue>) data);
            	// ensure the order is latest time last
            	return data;
    			
    		}
    		else{
    			//return the list as nothing to do
    			return data;
    		}        	
    	}
    	else if(shortName != null && shortName.equals(DataType.DAILYLIST_TYPE)){
    		// Sort the collection by not before time
    		if( null != data && data.size() > 1){
    			// Sort the collection by Not Before Time
            	Collections.sort((List<JuryStatusDailyListValue>) data);
            	// ensure the order is latest time last
            	return data;   			
    		}
    		else{
    			//return the list as nothing to do
    			return data;
    		} 
        }
    	else if(shortName != null && shortName.equals(DataType.ALLCOURTSTATUS_TYPE)){
    		// Sort the collection by court site, court room and then event time descending
        	Collections.sort((List<AllCourtStatusValue>) data);
        	
        	// Remove Duplicates
        	ArrayList<AllCourtStatusValue> newList = new ArrayList<AllCourtStatusValue>();
        	int previousRoomNo = -1;
        	String previousCourtSiteCode = "";
        	boolean matchingFound;
        	for ( AllCourtStatusValue value : (ArrayList<AllCourtStatusValue>) data ) {
        		// First check we're not processing the same court room at the same court site multiple times
        		if ( previousRoomNo != value.getCrestCourtRoomNo() || 
        			 (previousRoomNo == value.getCrestCourtRoomNo() && !previousCourtSiteCode.equals(value.getCourtSiteCode())) ) {

	        		// Get all matching elements
	        		ArrayList<AllCourtStatusValue> matchingList = (ArrayList<AllCourtStatusValue>) findAllObjects(value, (ArrayList<AllCourtStatusValue>) data);
	        		if ( matchingList.size() == 1 ) {
	        			newList.add(value);
	        		}
	        		else if ( matchingList.size() > 1 ) {
	        			// Multiple matching courtroom records found. The collection is sorted to have the most recent first
	        			// Find the most recent record with information to display
	        			matchingFound = false; 
	        			for ( AllCourtStatusValue matchingValue : matchingList ) {
	        				if ( matchingValue.hasInformationForDisplay() ) {
	        					newList.add(matchingValue);
	        					matchingFound = true;
	        					break;
	        				}
	        			}
	        			
	        			// If no matching record with information to display, then just use the first in the list
	        			if ( !matchingFound ) {
	        				newList.add(matchingList.get(0));
	        			}
	        		}
	        		
        			// Update the previous room number and court site code
        			previousRoomNo = value.getCrestCourtRoomNo();
        			previousCourtSiteCode = value.getCourtSiteCode();
        		}
        	}
        	return newList;
        }
    	else if(shortName != null && shortName.equals(DataType.SUMMARYBYNAME_TYPE)){
    		// Sort the collection by defendant names
        	Collections.sort((List<SummaryByNameValue>) data);
    		return data;
        }
    	else if(shortName != null && shortName.equals(DataType.ALLCASETSTATUS_TYPE)){
    		// Sort the collection by court site, then court room then defendant names
        	Collections.sort((List<AllCaseStatusValue>) data);
    		return data;
        }
    	else if(shortName != null && shortName.equals(DataType.JURYCURRENTSTATUS_TYPE)){
    		// Sort the collection by not before time
    		if( null != data && data.size() > 1){
    			// Sort the collection by Not Before Time
            	Collections.sort((List<JuryStatusDailyListValue>) data);
            	// ensure the order is latest time last
            	return data;   			
    		}
    		else{
    			//return the list as nothing to do
    			return data;
    		}        	
        }
    	else{
    		// document type is either null or not known
    		log.error("AbstractCppToPublicDisplay.getDataSource - " + shortName + " - not known as a document type.");
    	}
    	return null;
    }
    
    private static <T> List<T> findAllObjects(T obj, List<T> list) {
    	final List<T> matchingList = new ArrayList<T>();
    	for ( int i=0; i<list.size(); i++ ) {
    		if ( obj.equals(list.get(i)) ) {
    			matchingList.add(list.get(i));
    		}
    	}
    	return matchingList;
    }
}
