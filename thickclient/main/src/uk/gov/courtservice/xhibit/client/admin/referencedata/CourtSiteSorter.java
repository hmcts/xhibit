package uk.gov.courtservice.xhibit.client.admin.referencedata;

import java.util.Comparator;

import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteComplexValue;

public class CourtSiteSorter implements Comparator<CourtSiteComplexValue>{
	public int compare(CourtSiteComplexValue a, CourtSiteComplexValue b) {
		return a.getCourtSiteCode().compareTo(b.getCourtSiteCode());
	}
}