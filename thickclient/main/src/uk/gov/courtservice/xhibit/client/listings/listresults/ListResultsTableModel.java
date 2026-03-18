package uk.gov.courtservice.xhibit.client.listings.listresults;

import uk.gov.courtservice.xhibit.business.vos.entities.ListingResultsInformation;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITTableModel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: List Results Table Model
 * </p>
 * <p>
 * Description: This class provides access to the data displayed in the table in
 * the List Results screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2018
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Chris Vincent
 * @version 1.0
 */
public class ListResultsTableModel extends XHIBITTableModel {

	private static final long serialVersionUID = 1L;
	
	private static final String YES = "Yes";
	private static final String NO = "No";
	private static final String CRACKED_INEFFECTIVE_NULL = "Not Applicable";
	private static final String CRACKED_INEFFECTIVE_MISSING = "Missing";
	private static final String HRG_TYPE_MISSING_TRL = "TRL";
	private static final String HRG_TYPE_MISSING_TFL = "TFL";
	private static final String HRG_TYPE_MISSING_TBK = "TBK";
	private static final String FLOATER_CASE_TEXT = "FLTR";
	private static final char CASE_TYPE_T = 'T';

	public ListResultsTableModel() {
        super();
        setupColumns();
    }

    /**
     * Configures the table headers
     */
    private void setupColumns() {
    	
    	String[] col = new String[] { 
    			XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColCaseNumber"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColCaseTitle"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColCourt"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColFutureHearings"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColCaseStatus"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColHearingType"),
                XHIBITConstant.getResource(XhibitBundles.Listings, "listResultsGridColEffective") };
        this.setColumnNames(col);

    }
    
    /**
     * Returns the data for a specific cell.
     * 
     * @param r
     * @param c
     * @return Object
     */
    public Object getValueAt(int r, int c) {
    	ListingResultsInformation myLR = (ListingResultsInformation) getDataAt(r);
        switch (c) {
	        case 0:
	            return myLR.getCaseNumber();
	        case 1:
	            return myLR.getCaseTitle();
	        case 2:
	            // Concatenate court site code with room number and sitting sequence
	        	String roomSequence;
	        	if ( myLR.getIsFloating() == 1 ) roomSequence = FLOATER_CASE_TEXT;
	        	else roomSequence = myLR.getCourtRoomNo() + " - " + myLR.getSittingSequenceNo();
	        	return myLR.getCourtSiteCode() + " / " + roomSequence;
	        case 3:
	        	// Future hearings if future records in XHB_CASE_ON_LIST or XHB_CASE_DIARY_FIXTURE
	        	String futureHearings = ( myLR.getFutureHearingsXcol().equals(YES) || myLR.getFutureHearingsXcdf().equals(YES) ) ? YES : NO;
	            return futureHearings;
	        case 4:
	            return myLR.getCaseStatus();
	        case 5:
	            return myLR.getHearingType();
	        case 6:
	        	/**
	        	 * If Cracked/Ineffective is NULL, then 'Not Applicable' will be returned
	        	 * by the database package, but if the case type is 'T' and the hearing type
	        	 * is 'TRL','TFL' or 'TBK', then 'Missing' should be displayed instead. 
	        	 */
	            String returnValue = myLR.getEffectiveCrackedIneffective();
	            if ( returnValue.equals(CRACKED_INEFFECTIVE_NULL) && 
	            	 myLR.getCaseNumber().charAt(0) == CASE_TYPE_T &&
	            	 	( myLR.getHearingType().equals(HRG_TYPE_MISSING_TRL) ||
	            	 	  myLR.getHearingType().equals(HRG_TYPE_MISSING_TFL) ||
	            	 	  myLR.getHearingType().equals(HRG_TYPE_MISSING_TBK) ) ) {
	            	returnValue = CRACKED_INEFFECTIVE_MISSING;
	            }
	            		
	        	return returnValue;
	        default:
	            return "";
        }
    }

}
