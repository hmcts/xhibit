package uk.gov.courtservice.xhibit.business.vos.entities;

public class JudgeUsageComplexValue extends JudgeUsageBasicValue {
	
	private static final long serialVersionUID = -122045646713471564L;
	private RefJudgeBasicValue judge;
	private CourtRoomBasicValue courtRoom;
	
	public final RefJudgeBasicValue getJudge() {
		return judge;
	}
	public final void setJudge(final RefJudgeBasicValue judge) {
		
		if (judge !=null) {
			this.setRefJudgeId(judge.getId());
			this.judge = judge;
		}
	}
	public final CourtRoomBasicValue getCourtRoom() {
		return courtRoom;
	}

	public final void setCourtRoom(final CourtRoomBasicValue courtRoom) {
		
		if (courtRoom != null) {
			this.setCourtRoomId(courtRoom.getId());
			this.courtRoom = courtRoom;
		}
	}
}
