package uk.gov.courtservice.xhibit.client.actions.admin.courtroomstats;

import java.util.Date;

/**
 * POJO to represent a Judge Usage Row
 * @author westalll
 *
 */
public class JudgeUsageRow {
	
	private Integer judgeUsageId;
	private Integer courtRoomId;
	private String courtRoomName;
	private String courtChambersInd;
	private Integer judgeId;
	private Date sittingDate;
	private String judgeName;
	private boolean dirty = false;
	
	
	public final Integer getCourtRoomId() {
		return courtRoomId;
	}
	public final void setCourtRoomId(Integer courtRoomId) {
		this.courtRoomId = courtRoomId;
	}
	public final String getCourtRoomName() {
		return courtRoomName;
	}
	public final void setCourtRoomName(String courtRoomName) {
		this.courtRoomName = courtRoomName;
	}
	public final String getCourtChambersInd() {
		return courtChambersInd;
	}
	public final void setCourtChambersInd(String courtChambersInd) {
		this.courtChambersInd = courtChambersInd;
	}
	public final Integer getJudgeId() {
		return judgeId;
	}
	public final void setJudgeId(Integer judgeId) {
		this.judgeId = judgeId;
	}
	public final Date getSittingDate() {
		return sittingDate;
	}
	public final void setSittingDate(Date sittingDate) {
		this.sittingDate = sittingDate;
	}
	public String getJudgeName() {
		return judgeName;
	}
	public void setJudgeName(String judgeName) {
		this.judgeName = judgeName;
	}
	public boolean isDirty() {
		return dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
	public Integer getJudgeUsageId() {
		return judgeUsageId;
	}
	public void setJudgeUsageId(Integer judgeUsageId) {
		this.judgeUsageId = judgeUsageId;
	}
}
