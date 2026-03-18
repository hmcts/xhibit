package uk.gov.courtservice.xhibit.business.xmlmerge;


import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.formatting.WarnedListXMLMergeUtils;

public class TestWarnedListXMLMergeUtils extends AbstractTestXMLMergeUtils {
 		
	private int expectedSortParentPosition = 2;
	private int expectedSortPosition = 0;
	public TestWarnedListXMLMergeUtils() throws XPathExpressionException {
		super(new WarnedListXMLMergeUtils());
	}
	
	//------------ Overridden Methods ------------
	
	@Override
	protected int expectedSortParentPosition() {
		return expectedSortParentPosition; 
	}
	
	@Override
	protected int expectedSortPosition() {
		return expectedSortPosition; 
	}
	
	//------------- Additional Tests -------------
	
	public void testSuccessWithoutFixedDateIntoWithFixedDate() throws Exception {
		TestDocument originalDocument = new WithFixedDateDocument();
		TestDocument documentToMerge = new WithoutFixedDateDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccessWithoutFixedDateIntoWithFixedDate", originalDocument.document, documentToMerge.document); 
		expectSuccess(originalDocument, documentToMerge);
	}

	public void testSuccessWithoutFixedDateIntoEmpty() throws Exception {
		TestDocument originalDocument = new EmptyDocument();
		TestDocument documentToMerge = new WithoutFixedDateDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccessWithoutFixedDateIntoEmpty", originalDocument.document, documentToMerge.document); 
		expectSuccess(originalDocument, documentToMerge);
	}
	
	public void testSuccessWithFixedDateIntoWithoutFixedDate() throws Exception {
		int tempExpectedSortParentPosition = expectedSortParentPosition;
		TestDocument originalDocument = new WithoutFixedDateDocument();
		TestDocument documentToMerge = new WithFixedDateDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccessWithFixedDateIntoWithoutFixedDate", originalDocument.document, documentToMerge.document);
		expectedSortParentPosition = 1;
		expectSuccess(originalDocument, documentToMerge);
		expectedSortParentPosition = tempExpectedSortParentPosition;
	}
	
	public void testSuccessWithFixedDateIntoEmpty() throws Exception {
		int tempExpectedSortParentPosition = expectedSortParentPosition;
		TestDocument originalDocument = new EmptyDocument();
		TestDocument documentToMerge = new WithFixedDateDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccessWithFixedDateIntoEmpty", originalDocument.document, documentToMerge.document);
		expectedSortParentPosition = 1;
		expectSuccess(originalDocument, documentToMerge);
		expectedSortParentPosition = tempExpectedSortParentPosition;
	}

	public void testSuccessEmptyIntoWithoutFixedDate() throws Exception {
		int tempExpectedSortParentPosition = expectedSortParentPosition;
		TestDocument originalDocument = new WithoutFixedDateDocument();
		TestDocument documentToMerge = new EmptyDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		debugDocuments("testSuccessEmptyIntoWithoutFixedDate", originalDocument.document, documentToMerge.document);
		expectedSortParentPosition = 1;
		documentToMerge.noOfCourtHouses--;
		expectSuccess(originalDocument, documentToMerge);
		expectedSortParentPosition = tempExpectedSortParentPosition;
	}

	public void testSuccessEmptyIntoWithFixedDate() throws Exception {
		TestDocument originalDocument = new WithFixedDateDocument();
		TestDocument documentToMerge = new EmptyDocumentToMerge(originalDocument.getNoOfSittings() + 1);
		
		documentToMerge.noOfCourtHouses--;
		debugDocuments("testSuccessEmptyIntoWithFixedDate", originalDocument.document, documentToMerge.document);
		expectSuccess(originalDocument, documentToMerge);
	}
	
	
	//--------------- Additional Test Documents ---------------

	
	private class WithFixedDateDocument extends TestDocument {
		protected String[] getChildNodeNames() {
			return new String[] {WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE};
		}
	}
	
	private class WithoutFixedDateDocument extends TestDocument {
		protected String[] getChildNodeNames() {
			return new String[] {WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE};
		}
	}
	
	private class WithFixedDateDocumentToMerge extends TestDocumentToMerge {
		public WithFixedDateDocumentToMerge(int startSittingNo) {
			super(startSittingNo);
		}	
		protected String[] getChildNodeNames() {
			return new String[] {WarnedListXMLMergeUtils.Tag.WITH_FIXED_DATE};
		}
	}
	
	private class WithoutFixedDateDocumentToMerge extends TestDocumentToMerge {
		public WithoutFixedDateDocumentToMerge(int startSittingNo) {
			super(startSittingNo);
		}	
		protected String[] getChildNodeNames() {
			return new String[] {WarnedListXMLMergeUtils.Tag.WITHOUT_FIXED_DATE};
		}
	}

	/**
	 * Test that when xhibit is blank then the Court Lists from cpp get merged in
	 *  correctly but the rest of the file e.g. doc name remain as xhibit.
	 */
	public void testBlankXhibitWarnedList()  {

			String xml1 =getBlankXhibitDoc();
			String xml2 =getCPPXhibitDoc();
			String expected = getExpectedXhibitDoc();
		
		try {
			String s1 = mergeDocuments(xml1, xml2);
			ensureSuccess(s1,xml1,xml2,expected);

		} catch(Exception e){
			TestCase.fail();
		}
	}

	/**
	 * Test where one court list is empty and one isn't 
	 */
	public void testMixOfBlankAndNotBlank() {
	
		try {
			String xml1 =getBlankAndNotBlankXhibitDoc();
			String xml2 =getCPPXhibitDocOnly1CourtSite();
			String expected = getExpectedXhibitDocMix();
			
			String s1 = mergeDocuments(xml1, xml2);
			ensureSuccess(s1,xml1,xml2,expected);
	
		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * test to make sure if the second doc passed in is blank then 
	 * we just return the first doc.
	 */
	public void testOriginalDocShownIfSecondBlank() {
		
		try {
			String xml1 =getCPPXhibitDoc();
			String xml2 =getBlankXhibitDoc();
			String s1 = mergeDocuments(xml1, xml2);
			assertEquals(s1,xml1);
			
		} catch(Exception e){
			TestCase.fail();
		}
	}
	/**
	 * Test that when xhibit is blank then the Court Lists from cpp get merged in
	 *  correctly but the rest of the file e.g. doc name remain as xhibit.
	 */
	public void testBlankXhibitWarnedListOnlyWithTag()  {
		
		try {
			String xml1 =getBlankXhibitDocWith1CourtList();
			String xml2 =getCPPXhibitDocOnlyWith();
			String expected = getExpectedXhibitDocOnlyWith();

			String s1 = mergeDocuments(xml1, xml2);
			ensureSuccess(s1,xml1,xml2,expected);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * return xhibit xml
	 * @return
	 */
	private String getBlankXhibitDoc() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.BLANK_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.BLANK_COURT_LIST_2_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	/**
	 * return xhibit xml
	 * @return
	 */
	private String getBlankXhibitDocWith1CourtList() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.BLANK_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	/**
	 * return xhibit xml
	 * courtsite1 - blank
	 * courtsite2 - with fixed date
	 * @return
	 */
	private String getBlankAndNotBlankXhibitDoc() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.BLANK_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.FILLED_COURT_LIST_2_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	/**
	 * return cpp xml
	 * court list 1 - with
	 * court list 2 - without 
	 * @return
	 */
	private String getCPPXhibitDoc() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_CPP_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_CPP_TAGS
				+CPPListConstants.CROWN_COURT_WITHOUT_ADDRESS_TAGS
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.CPP_COURT_LIST_1
				+CPPListConstants.CPP_COURT_LIST_2
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	
	/**
	 * return cpp xml
	 * court list 1 - with
	 * court list 2 - without 
	 * @return
	 */
	private String getCPPXhibitDocOnly1CourtSite() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_CPP_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_CPP_TAGS
				+CPPListConstants.CROWN_COURT_WITHOUT_ADDRESS_TAGS
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.CPP_COURT_LIST_1
				+CPPListConstants.CPP_COURT_LIST_2
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	/**
	 * return cpp xml
	 * @return
	 */
	private String getCPPXhibitDocOnlyWith() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_CPP_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_CPP_TAGS
				+CPPListConstants.CROWN_COURT_WITHOUT_ADDRESS_TAGS
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.CPP_COURT_LIST_1
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;

	}
	
	/**
	 * Return expected merge
	 * @return
	 */
	private String getExpectedXhibitDoc() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.MERGED_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.MERGED_COURT_LIST_2_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;
	}
	
	/**
	 * Return expected merge
	 * @return
	 */
	private String getExpectedXhibitDocOnlyWith() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.MERGED_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;
	}
	
	
	/**
	 * Return expected merge
	 * @return
	 */
	private String getExpectedXhibitDocMix() {
		return CPPListConstants.WARNED_LIST_ENTRY_TAG
				+CPPListConstants.DEFAULT_DOC_XHIBIT_ID_TAGS
				+CPPListConstants.DEFAULT_LIST_HEADER_XHIBIT_TAGS
				+CPPListConstants.CROWN_COURT_WITH_ADDRESS_TAGS
				+CPPListConstants.WARNED_LIST_DETAIL
				+CPPListConstants.COURT_LISTS_ENTRY_TAG
				+CPPListConstants.MERGED_COURT_LIST_1_WITH_ADDRESS_AND_ID
				+CPPListConstants.MERGED_COURT_LIST_3_WITH_ADDRESS_AND_ID
				+CPPListConstants.COURT_LISTS_END_TAG
				+CPPListConstants.WARNED_LIST_END_TAG;
	}
	
	/**
	 * As this was being done in 4 tests it makes sense to do this in one method and call that from the tests.
	 * @param xhibit
	 * @param cpp
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 * @throws XPathExpressionException
	 */
	public String mergeDocuments(String xhibit, String cpp) throws ParserConfigurationException, SAXException, IOException, TransformerException, XPathExpressionException {
		Document doc = getDocBuilderWithNoExternals(xhibit);
		Document doc2 = getDocBuilderWithNoExternals(cpp);
		
		WarnedListXMLMergeUtils xmlUtils1 = new WarnedListXMLMergeUtils();

		Document mergedDocument = xmlUtils1.merge(doc,doc2);
		return transformXml(mergedDocument);
	}
	
	/**
	 * Ensure that the following asserts are met.
	 * @param s1
	 * @param xml1
	 * @param xml2
	 * @param expected
	 */
	private void ensureSuccess(String s1, String xml1, String xml2, String expected) {
		assertTrue(s1.indexOf(expected)>=0);
		assertNotSame(s1,xml1);
		assertNotSame(s1,xml2);		
	}
}
