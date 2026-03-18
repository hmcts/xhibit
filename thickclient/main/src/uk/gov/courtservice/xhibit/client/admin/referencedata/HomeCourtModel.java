package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.List;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;

/**
 * The model used for the fields on the Home Court screen.
 * @author toftn
 *
 */
public class HomeCourtModel {
	
	private XPanel callingClass;
	
	private CourtComplexValue court;
	
	private List<CourtSiteComplexValue> courtSites;
	
	public HomeCourtModel() {
		clearmodel();
	}
	
	public HomeCourtModel(XPanel callingClass) {
		setCallingClass(callingClass);
		clearmodel();
	}
	
	/**
	 * @return the courtSites
	 */
	public List<CourtSiteComplexValue> getCourtSites() {
		return courtSites;
	}

	/**
	 * @param courtSites the courtSites to set
	 */
	public void setCourtSites(List<CourtSiteComplexValue> courtSites) {
		this.courtSites = courtSites;
	}
	

	public XPanel getCallingClass() {
		return callingClass;
	}

	public void setCallingClass(XPanel callingClass) {
		this.callingClass = callingClass;
	}
	
	/**
	 * @return the court
	 */
	public CourtComplexValue getCourt() {
		return court;
	}

	/**
	 * @param court the court to set
	 */
	public void setCourt(CourtComplexValue court) {
		this.court = court;
	}

	public void clearmodel() {

    }	
}
