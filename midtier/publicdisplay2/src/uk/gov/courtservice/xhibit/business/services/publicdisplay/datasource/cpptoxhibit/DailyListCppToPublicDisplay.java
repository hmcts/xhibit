package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.util.Collection;
import java.util.Date;

public class DailyListCppToPublicDisplay extends JuryCurrentStatusCppToPublicDisplay {
	
	public DailyListCppToPublicDisplay(Date date, int courtId, int[] courtRoomIds) {
		super(date, courtId, courtRoomIds);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Collection getCppData() {						
		return super.getCppData();		
	}
}