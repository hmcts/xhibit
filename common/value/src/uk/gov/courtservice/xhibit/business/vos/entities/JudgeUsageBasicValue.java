package uk.gov.courtservice.xhibit.business.vos.entities;

import java.util.Date;

import uk.gov.courtservice.framework.business.vos.CSAbstractValue;

public class JudgeUsageBasicValue extends CSAbstractValue {


	private static final long serialVersionUID = -8479507367109583629L;
	
	private Integer judgeUsageId;
	private Integer courtRoomId;
	private String courtChambersInd;
	private Integer refJudgeId;
	private Date sittingDate;
	
	public final Integer getJudgeUsageId() {
		return judgeUsageId;
	}
	public final void setJudgeUsageId(Integer judgeUsageId) {
		this.judgeUsageId = judgeUsageId;
	}
	public final Integer getCourtRoomId() {
		return courtRoomId;
	}
	public final void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}
	public final String getCourtChambersInd() {
		return courtChambersInd;
	}
	public final void setCourtChambersInd(String courtChambersInd) {
		this.courtChambersInd = courtChambersInd;
	}
	
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	public final Integer getRefJudgeId() {
		return refJudgeId;
	}
	public final void setRefJudgeId(Integer refJudgeId) {
		this.refJudgeId = refJudgeId;
	}

}
