package uk.gov.courtservice.xhibit.common.results.vos;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class DRSRReportValue extends CSAbstractValue{
	
	private static final long serialVersionUID = 1L;
	
	private String caseNumber;
	private String listingDate;
	private String htyp;
	private String sittingSequenceNo;
	private String courtRoomName;
	private String site;
	public String getCaseNumber() {
		return caseNumber;
	}
	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}
	public String getListingDate() {
		return listingDate;
	}
	public void setListingDate(String listingDate) {
		this.listingDate = listingDate;
	}

	public String getSittingSequenceNo() {
		return sittingSequenceNo;
	}
	public void setSittingSequenceNo(String sittingSequenceNo) {
		this.sittingSequenceNo = sittingSequenceNo;
	}
	public String getCourtRoomName() {
		return courtRoomName;
	}
	public void setCourtRoomName(String courtRoomName) {
		this.courtRoomName = courtRoomName;
	}
	
	public String getHtyp() {
		return htyp;
	}
	public void setHtyp(String htyp) {
		this.htyp = htyp;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}


}
