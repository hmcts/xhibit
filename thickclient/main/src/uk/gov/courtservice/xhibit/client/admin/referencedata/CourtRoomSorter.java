package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;

public class CourtRoomSorter implements Comparator<CourtRoomBasicValue> {
	public int compare(CourtRoomBasicValue a, CourtRoomBasicValue b) {
		return a.getCrestCourtRoomNo() - b.getCrestCourtRoomNo();
	}
}