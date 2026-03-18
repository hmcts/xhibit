package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.Date;

import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

public class AddJudgeUsageModel {

	private static final String EMPTY_STRING = "";
	private Date sittingDate = new Date();
	private Integer courtSiteId;
	private XhibitApplicationController xac;
	private RefJudgeBasicValue refJudge;
	
	public AddJudgeUsageModel(XhibitApplicationController xac) {
		setXac(xac);
	}
	
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	public Integer getCourtSiteId() {
		return courtSiteId;
	}
	public void setCourtSiteId(Integer courtSiteId) {
		this.courtSiteId = courtSiteId;
	}
	public XhibitApplicationController getXac() {
		return xac;
	}
	public void setXac(XhibitApplicationController xac) {
		this.xac = xac;
	}
	public RefJudgeBasicValue getRefJudge() {
		return refJudge;
	}

	public void setRefJudge(RefJudgeBasicValue refJudge) {
		this.refJudge = refJudge;
	}
	public String getReqJudge() {
		return getRefJudge() != null ? getFullName(
				getRefJudge().getJudgeType() != null ? getRefJudge().getJudgeType() : EMPTY_STRING, 
				getRefJudge().getTitle() != null ? getRefJudge().getTitle() : EMPTY_STRING, 
			    getRefJudge().getFirstName() != null ? getRefJudge().getFirstName() : EMPTY_STRING, 
				getRefJudge().getSurname() != null ? getRefJudge().getSurname() : EMPTY_STRING) : null;
	}
	
	private String getFullName(String judgeType, String title, String firstName, String surname) {
		return judgeType.concat(" ").concat(title).concat(" ").concat(firstName).concat(" ").concat(surname);
	}
}