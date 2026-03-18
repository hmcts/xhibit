package uk.gov.courtservice.xhibit.client.listings.list.outline;

import java.util.Calendar;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;

/**
 * Model class for data required for a court room in the outline control.
 * 
 * @author uphillj
 * @amend groenm - added the list date a room is associated with.
 *
 */
public class CourtRoomTreeNodeModel extends AbstractTreeNodeModel {

	private static final long serialVersionUID = 1L;
	
	private XhbCourtRoomBasicValue courtRoomBasicValue;
	
	private Calendar listDate;
	
	public XhbCourtRoomBasicValue getCourtRoom() {
		return courtRoomBasicValue;
	}

	public void setCourtRoom(XhbCourtRoomBasicValue courtRoomBasicValue) {
		this.courtRoomBasicValue = courtRoomBasicValue;
	}
	
	public Calendar getListDate() {
		return listDate;
	}
	
	public void setListDate(Calendar listDate) {
		this.listDate = listDate;
	}
	
	@Override
	public String getDisplayName() {
		return courtRoomBasicValue.getDisplayNameNoSite();
	}
	
}
