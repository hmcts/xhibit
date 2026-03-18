package uk.gov.courtservice.xhibit.common.publicdisplay.events.pdda;

import uk.gov.courtservice.xhibit.common.publicdisplay.events.PublicDisplayEvent;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;

/**
 * An event to be used for updating the hearing progress on PDDA when it changes in XHIBIT.
 * @author d196492
 */
public class PddaHearingProgressEvent implements PublicDisplayEvent {
	
	private static final long serialVersionUID = -8013651434648029138L;
	
	private Integer hearingProgressIndicator;
	private String courtName;
	private String caseType;
	private Integer caseNumber;
	private String courtRoomName;
	private String isCaseActive;
	private Integer courtId;
	
	/**
	 * Deliberately returning nothing here as event type is not needed for PDDA
	 */
	@Override
	public EventType getEventType() {
		return null;
	}
	
	public Integer getCourtId() {
		return courtId;
	}

	public Integer getHearingProgressIndicator() {
		return hearingProgressIndicator;
	}


	public void setHearingProgressIndicator(Integer hearingProgressIndicator) {
		this.hearingProgressIndicator = hearingProgressIndicator;
	}


	public String getCourtName() {
		return courtName;
	}


	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}


	public String getCaseType() {
		return caseType;
	}


	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}


	public Integer getCaseNumber() {
		return caseNumber;
	}


	public void setCaseNumber(Integer caseNumber) {
		this.caseNumber = caseNumber;
	}
	
	public String getCourtRoomName() {
		return courtRoomName;
	}

	public void setCourtRoomName(String courtRoomName) {
		this.courtRoomName = courtRoomName;
	}
	
	public String getIsCaseActive() {
		return isCaseActive;
	}

	public void setIsCaseActive(String isCaseActive) {
		this.isCaseActive = isCaseActive;
	}
	
	public void setCourtId(Integer courtId) {
		this.courtId = courtId;
	}
	
	@Override
	public String toString() {
		return "PddaHearingProgressEvent [hearingProgressIndicator=" + hearingProgressIndicator + ", courtName="
				+ courtName + ", caseType=" + caseType + ", caseNumber=" + caseNumber + ", courtRoomName="
				+ courtRoomName + ", isCaseActive=" + isCaseActive + ", courtId=" + courtId + "]";
	}

}
