/**
 * <p>
 * Title: NonAvailableDaysModel
 * </p>
 * <p>
 * Description: NonAvailableDaysModel represents the model for the Non Available Days screen
 * CTX-1313
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Gurinder Brar
 * @version 1.0
 */

package uk.gov.courtservice.xhibit.client.listings.nonavailabledays;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import uk.gov.courtservice.xhibit.business.services.listing.ListingsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseListingEntryComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseNonAvailDaysBasicValue;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

public class NonAvailableDaysModel {
	
    public static interface ValidValues {
        public static final List<String> CASE_TYPES = CaseType.CaseListingCaseTypes();       
    }
	private Integer caseId;
	private String caseType;
	private Integer caseNumber;
	private Integer caseEntryId;
	private Collection<CaseNonAvailDaysBasicValue> hearings;
	
	/**
	 * Constructor with caseId, caseType, caseNumber and caseEntryId passed in
	 * @param caseId	Case Id
	 * @param caseType	Case Type
	 * @param caseNumber	Case Number
	 * @param caseEntryId	Case Listing Entry Id
	 */
	public NonAvailableDaysModel(Integer caseId, String caseType, Integer caseNumber, Integer caseEntryId) {
		clearmodel();
		setCaseId(caseId);
		setCaseType(caseType);
        setCaseNumber(caseNumber);
        setCaseEntryId(caseEntryId);
	}
	
	/**
	 * Constructor with just case id passed in so will query the database for the other values
	 * @param caseId
	 */
	public NonAvailableDaysModel(Integer caseId) {
		clearmodel();
		setCaseId(caseId);
		
		// Get the data from the database to populate the case type, case number and case listing entry id
		try {
			CaseListingEntryComplexValue caseListingEntry = XhibitDelegateHelper.getListingsDelegate()
					.getCaseListingEntryByCaseIdAndCourtId(caseId, XhibitSingleton.getInstance().getCourtId() );
			
			if ( caseListingEntry != null ) {  
				setCaseType( caseListingEntry.getCaseBasicValue().getCaseType() );
		        setCaseNumber( caseListingEntry.getCaseBasicValue().getCaseNumber() );
		        setCaseEntryId( caseListingEntry.getCaseListingEntryId() );
			}
			
		} catch (ListingsControllerException ex) {
			XHIBITConstant.handleError(ex);
		}
	}
	
	/**
	 * Reset data model
	 */
    public void clearmodel() {
    	setCaseId(null);
		setCaseType(null);
        setCaseNumber(null);
        setCaseEntryId(null);
        setHearings(null);
    }

    /**
     * Get Case Id
     * @return caseId
     */
	public Integer getCaseId() {
		return caseId;
	}

	/**
	 * Set Case Id
	 * @param caseId caseId to set
	 */
	public void setCaseId(Integer caseId) {
		this.caseId = caseId;
	}
	
	/**
	 * Get Case Type
	 * @return caseType
	 */
	public String getCaseType() {
        return caseType;
    }
    
	/**
	 * Set Case Type
	 * @param caseType caseType to set
	 */
    public void setCaseType(String caseType) {
    	this.caseType = caseType;
    }

    /**
     * Get Case Number
     * @return caseNumber
     */
	public Integer getCaseNumber() {
        return caseNumber;
    }
    
	/**
	 * Set Case Number
	 * @param caseNumber caseNumber to set
	 */
    public void setCaseNumber(Integer caseNumber) {
    	this.caseNumber = caseNumber;
    }
    
    /**
     * Get Case Entry Id
     * @return caseEntryId
     */
	public Integer getCaseEntryId() {
		return caseEntryId;
	}

	/**
	 * Set Case Entry Id
	 * @param caseEntryId caseEntryId to set
	 */
	public void setCaseEntryId(Integer caseEntryId) {
		this.caseEntryId = caseEntryId;
	}
    
    /**
     * Get Collection of hearings
     * @return hearings
     */
	public Collection<CaseNonAvailDaysBasicValue> getHearings() {
		return hearings;
	}
	
	/**
	 * Set the hearings and sort if not empty
	 * @param hearings Collection<CaseNonAvailDaysBasicValue>
	 */
	public void setHearings(Collection<CaseNonAvailDaysBasicValue> hearings) {
		if (hearings != null && !hearings.isEmpty()) {
			Collections.sort((List<CaseNonAvailDaysBasicValue>) hearings,sorter);
		}
		
		this.hearings = hearings;
	}

	/**
	 * Replace the object in the model with the updated object
	 */
	public void updateHearing(CaseNonAvailDaysBasicValue updatedBasicValue) {
		List<CaseNonAvailDaysBasicValue> hearings = (List<CaseNonAvailDaysBasicValue>) getHearings();
		ListIterator<CaseNonAvailDaysBasicValue> lit = hearings.listIterator();
		if (hearings != null) {
			while (lit.hasNext()) {
				if (lit.next().getId().equals(updatedBasicValue.getId())) {
					lit.set(updatedBasicValue);
					break;
				}
			}
		}	
		Collections.sort(hearings,sorter);
	}
	
	/**
	 * Add the object in the model with the new object
	 */
	public void addHearing(CaseNonAvailDaysBasicValue addedBasicValue) {
		List<CaseNonAvailDaysBasicValue> hearings = (List<CaseNonAvailDaysBasicValue>) getHearings();
		if (hearings != null) {
			hearings.add(addedBasicValue);
		}
		Collections.sort(hearings,sorter);
	}
	
	/**
	 * Delete the object in the model
	 */
	public void deleteHearing(CaseNonAvailDaysBasicValue addedBasicValue) {
		List<CaseNonAvailDaysBasicValue> hearings = (List<CaseNonAvailDaysBasicValue>) getHearings();
		if (hearings != null) {
			hearings.remove(addedBasicValue);
		}
		Collections.sort(hearings,sorter);
	}

	/**
	 * Comparator class for sorting non available days
	 */
	private static Comparator<CaseNonAvailDaysBasicValue> sorter = new Comparator<CaseNonAvailDaysBasicValue>() {
		
		public int compare(CaseNonAvailDaysBasicValue c1, CaseNonAvailDaysBasicValue c2) {
			int diff = c1.getStartDate().compareTo(c2.getStartDate());
			if (Integer.valueOf(0).equals(diff)) {
				diff = c1.getEndDate().compareTo(c2.getEndDate());
			}
			return diff;
		}
	};
}
