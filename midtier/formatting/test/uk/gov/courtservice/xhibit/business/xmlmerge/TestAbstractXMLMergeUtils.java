package uk.gov.courtservice.xhibit.business.xmlmerge;

import javax.xml.xpath.XPathExpressionException;

import uk.gov.courtservice.xhibit.business.services.formatting.AbstractListXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.AbstractXMLMergeUtils;

public class TestAbstractXMLMergeUtils extends AbstractTestXMLMergeUtils {
 		
	public TestAbstractXMLMergeUtils() throws XPathExpressionException {
		super(new TestXMLMergeUtils());
	}
	
	public static class TestXMLMergeUtils extends AbstractXMLMergeUtils {

		public TestXMLMergeUtils() throws XPathExpressionException {
			super(new String[] {"DailyList/CourtLists/CourtList/Sittings"});
		}

		@Override
		public String[] getNodeMatchArray() {
			return new String[] {AbstractListXMLMergeUtils.Tag.COURTHOUSE_CODE, AbstractListXMLMergeUtils.Tag.COURTHOUSE_NAME};
		}

		@Override
		public String[] getNodePositionArray() {
			return new String[] {AbstractListXMLMergeUtils.Tag.COURTROOM_NUMBER, AbstractListXMLMergeUtils.Tag.SITTING_AT};
		}

		@Override
		public String getMergeType() {
			return "LISTS";
		}	
	}
}
