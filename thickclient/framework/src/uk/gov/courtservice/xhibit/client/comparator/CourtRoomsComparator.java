package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;

/**
 * Used to sort court rooms based upon the crest court room number
 * 
 * @author vincentc
 *
 */
public class CourtRoomsComparator implements Comparator<CourtRoomBasicValue>{

	@Override
	public int compare(CourtRoomBasicValue o1, CourtRoomBasicValue o2) {
		return o1.getCrestCourtRoomNo().compareTo(o2.getCrestCourtRoomNo());
	}

}