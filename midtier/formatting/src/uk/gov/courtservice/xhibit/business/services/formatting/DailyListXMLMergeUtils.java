package uk.gov.courtservice.xhibit.business.services.formatting;

import javax.xml.xpath.XPathExpressionException;

public class DailyListXMLMergeUtils extends AbstractListXMLMergeUtils {		
	
	public DailyListXMLMergeUtils() throws XPathExpressionException {
		super(new String[] {"DailyList/CourtLists/CourtList/Sittings"});
	}	
}
