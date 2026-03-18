package uk.gov.courtservice.xhibit.business.xmlmerge;


import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.formatting.DailyListXMLMergeUtils;

public class TestDailyListXMLMergeUtils extends AbstractTestXMLMergeUtils {
 		
	public TestDailyListXMLMergeUtils() throws XPathExpressionException {
		super(new DailyListXMLMergeUtils());
	}	

	//------------- Additional Tests -------------
	
	/**
	 * Test that when xhibit is blank then the Court Lists from cpp get merged in
	 *  correctly but the rest of the file e.g. doc name remain as xhibit.
	 */
	public void testBlankXhibitDailyList()  {

			String xml1 =CPPListConstants.DAILY_LIST_ENTRY_TAG
					+ CPPListConstants.DL_XHIBIT_DOC_DEFAULT
					+ CPPListConstants.FL_DEFAULT_LIST_HEADER_XHIBIT_TAGS
					+ CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
					+ CPPListConstants.COURT_LISTS_BLANK_TAG
					+CPPListConstants.DAILY_LIST_END_TAG;
			
						
			String xml2 =CPPListConstants.DAILY_LIST_ENTRY_TAG
					+ CPPListConstants.DL_CPP_DOC_DEFAULT
					+ CPPListConstants.FL_DEFAULT_LIST_HEADER_XHIBIT_TAGS
					+ CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
					+ CPPListConstants.COURT_LISTS_ENTRY_TAG
							+ CPPListConstants.COURT_LIST_DEFAULT_SITTING_06_01
							+ CPPListConstants.COURT_HOUSE_TAGS
							+ CPPListConstants.SITTINGS_ENTRY_TAG
							+ CPPListConstants.SITTING_ENTRY_TAG
							+ CPPListConstants.COURT_ROOM_NUMBER_ENTRY_TAG+"1"+CPPListConstants.COURT_ROOM_NUMBER_END_TAG
							+ CPPListConstants.SEQ_NO_1
							+ CPPListConstants.SITTING_AT_09
							+ CPPListConstants.PRIORITY_T
							+ CPPListConstants.DEFAULT_JUDICIARY
							+ CPPListConstants.HEARINGS_ENTRY_TAG
							+ CPPListConstants.HEARING_1
							+CPPListConstants.HEARINGS_END_TAG
						+ CPPListConstants.SITTING_END_TAG
						+ CPPListConstants.SITTINGS_END_TAG
						+CPPListConstants.COURT_LIST_END_TAG
						+ CPPListConstants.COURT_LISTS_END_TAG
						+ CPPListConstants.DAILY_LIST_END_TAG;
								
			
			String expected = CPPListConstants.DAILY_LIST_ENTRY_TAG
					+ CPPListConstants.DL_XHIBIT_DOC_DEFAULT
					+ CPPListConstants.FL_DEFAULT_LIST_HEADER_XHIBIT_TAGS
					+ CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
					+ CPPListConstants.COURT_LISTS_ENTRY_TAG
					+ CPPListConstants.COURT_LIST_DEFAULT_SITTING_06_01
					+ CPPListConstants.COURT_HOUSE_TAGS
					+ CPPListConstants.SITTINGS_ENTRY_TAG
					+ CPPListConstants.SITTING_ENTRY_TAG
					+ CPPListConstants.COURT_ROOM_NUMBER_ENTRY_TAG+"1"+CPPListConstants.COURT_ROOM_NUMBER_END_TAG
					+ CPPListConstants.SEQ_NO_1
					+ CPPListConstants.SITTING_AT_09
					+ CPPListConstants.PRIORITY_T
					+ CPPListConstants.DEFAULT_JUDICIARY
					+ CPPListConstants.HEARINGS_ENTRY_TAG
					+ CPPListConstants.HEARING_1
					+CPPListConstants.HEARINGS_END_TAG
				+ CPPListConstants.SITTING_END_TAG
				+ CPPListConstants.SITTINGS_END_TAG
				+CPPListConstants.COURT_LIST_END_TAG
				+ CPPListConstants.COURT_LISTS_END_TAG
					+CPPListConstants.DAILY_LIST_END_TAG;
					
		try {
			Document doc = getDocBuilderWithNoExternals(xml1);
			Document doc2 = getDocBuilderWithNoExternals(xml2);
			
			DailyListXMLMergeUtils xmlUtils1 = new DailyListXMLMergeUtils();

			Document mergedDocument = xmlUtils1.merge(doc,doc2);
			String s1 = transformXml(mergedDocument);

			assertTrue(s1.indexOf(expected)>=0);
			assertNotSame(s1,xml1);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}
}
