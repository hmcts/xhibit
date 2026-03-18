package uk.gov.courtservice.xhibit.business.vos.entities;

public class CourtRoomUsageComplexValue extends CourtRoomUsageBasicValue {

	private static final long serialVersionUID = 1L;
	private CourtRoomBasicValue courtRoom;
	
	public final CourtRoomBasicValue getCourtRoom() {
		return courtRoom;
	}

	public final void setCourtRoom(CourtRoomBasicValue courtRoom) {
		this.courtRoom = courtRoom;
		if (courtRoom != null) {
			this.setCourtRoomId(courtRoom.getId());
		}
	}

}
