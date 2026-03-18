package uk.gov.courtservice.xhibit.client.comparator;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;

/**
 * Used to sort receiving sites in alphabetical order of their court site code
 * 
 * @author waltersn
 *
 */
public class ReceivingSitesComparator implements Comparator<CourtSiteBasicValue>{

	@Override
	public int compare(CourtSiteBasicValue o1, CourtSiteBasicValue o2) {
		
		//Deal with null values.
		final String s1 = o1 != null &&  o1.getCourtSiteCode() !=null  ? o1.getCourtSiteCode() : "";
		final String s2 = o2 != null &&  o2.getCourtSiteCode() != null ? o2.getCourtSiteCode() : "";
		
		return s1.compareTo(s2);
	}

}
