package uk.gov.courtservice.xhibit.business.xmlmerge;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.business.services.formatting.AbstractXMLMergeUtils;
import uk.gov.courtservice.xhibit.business.services.formatting.FormattingServices;
import uk.gov.courtservice.xhibit.business.services.formatting.IWPXMLMergeUtils;

/**
 * Test class to test the IWP merge process.
 * @author waltersn
 *
 */
public class TestIWPXMLMergeUtils extends TestCase {
	private IWPXMLMergeUtils xmlUtils ;
	private static final String NEW_LINE = "\\n";
	private static final String RETURN_C ="\\r";
	protected final Logger log = Logger.getLogger(AbstractTestXMLMergeUtils.class);

	public TestIWPXMLMergeUtils() throws XPathExpressionException {
		xmlUtils = new IWPXMLMergeUtils();
	}	
	
	/** 
	 * Test a sucessful merge of two documents. 
	 */
	public void testSuccess()  {

		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+ CPPXMLConstants.COURT_NAME_SNARESBROOK + CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_SNARESBROOK + CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE + CPPXMLConstants.DEFAULT_THREE_DEFENDANTS
				+ CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT + CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_1
				+ CPPXMLConstants.COURT_ROOM_END_TAG
				+ generateBlankCourtRoomsWithinRange(2, 26)
				+ generateBlankCourtRoomsWithinRange(49, 51)
				+ generateBlankCourtRoom(CPPXMLConstants.EIGHTY_EIGHT) + generateBlankCourtRoom(CPPXMLConstants.NINETY_NINE) + CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITE_END_TAG 
				+ CPPXMLConstants.COURT_SITE_THE_NEW_SITE + CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.TWELVE) + CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITE_END_TAG 
				+ CPPXMLConstants.COURT_SITE_EST + CPPXMLConstants.COURT_ROOMS_ENTRY_TAG + generateBlankCourtRoom(CPPXMLConstants.ONE)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG + CPPXMLConstants.COURT_SITE_SNARESBROOK
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG + CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_THREE_DEFENDANTS + CPPXMLConstants.DEFAULT_EVENT_EARLY_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		String expectedXmlMinusDateTime = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+ CPPXMLConstants.COURT_NAME_SNARESBROOK + CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_SNARESBROOK + CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE + CPPXMLConstants.DEFAULT_THREE_DEFENDANTS
				+ CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT + CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_1
				+ CPPXMLConstants.COURT_ROOM_END_TAG + CPPXMLConstants.COURT_ROOM_ENTRY_TAG + CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_THREE_DEFENDANTS + CPPXMLConstants.DEFAULT_EVENT_EARLY_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_2
				+ CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoomsWithinRange(3, 26)
				+generateBlankCourtRoomsWithinRange(49, 51)
				+generateBlankCourtRoom(CPPXMLConstants.EIGHTY_EIGHT) + generateBlankCourtRoom(CPPXMLConstants.NINETY_NINE)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_SITE_THE_NEW_SITE + CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.TWELVE) + CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITE_END_TAG + CPPXMLConstants.COURT_SITE_EST
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.ONE)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG 
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(expectedXmlMinusDateTime)>=0);
			assertNotSame(s1,xml1);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}

	/**
	 * Returns a document with externals not allowed.
	 * This was flagged up by sonarqube.
	 * @param xml1 
	 * @return Document
	 * @throws ParserConfigurationException
	 * @throws IOException 
	 * @throws SAXException 
	 */
	private Document getDocBuilderWithNoExternals(String xml1) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(new StringReader(xml1)));
	}

	/**
	 * Test merging in a different courtsite into xml1. This
	 * should do nothing as if courtsite doesn't match it doesn't get merged in.
	 * Xml1 has 1 courtsite - courtsite1
	 * Xml2 has 1 courtsite courtsite2.
	 * Expected should be xml1 without courtsite2 from xml2 merged in
	 */
	public void testMergingCourtSiteFrom2Into1()  {

		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG	
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.COURT_ROOM_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG	
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.COURT_ROOM_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		String expectedXmlMinusDateTime = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+ CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG	
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.COURT_ROOM_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG;
		
		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(expectedXmlMinusDateTime)>=0);
			assertNotSame(s1,xml1);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * Test when the courtroom doesn't exist in xml2 then it doesn't get inserted into xml 1
	 * Xml1 has 1 courtroomnumber 13
	 * Xml2 has 1 courtrooomnumber 3
	 * Expected should be the same as xml1 as courtroom 3 doesn't exist in xml1
	 */
	public void testUnknownCourtRoomNotMerged()  {

		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG	
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+ CPPXMLConstants.COURT_ROOM_COURT_13
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG	
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+ CPPXMLConstants.COURT_ROOM_COURT_3
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		try {
			String s1 = mergeDocuments(xml1,xml2);
			assertTrue(s1.indexOf(xml1)>=0);			
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * Test if crown court isn't in initial xml it doesn't get merged in
	 * Xml1 has 1 courtroomnumber 13
	 * Xml2 has 1 courtrooomnumber 3
	 * Expected should be the same as xml1 as courtroomnumber 3 doesn't exist in xml1
	 */
	public void testCrownCourtNotMergedIn()  {

		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+ CPPXMLConstants.CROWN_COURT_ROOM_13 
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_1
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO 
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+ CPPXMLConstants.CROWN_COURT_ROOM_3
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		try {
			String s1 = mergeDocuments(xml1,xml2);
			
			assertTrue(removeDate(s1).equals(removeDate(xml1)));
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}


	
	/**
	 * Test where xml1 is empty and xml2 has nonempty for that courtroom. 
	 * Documents are identical apart from that so merged should be equal to xml2.
	 */
	public void testWithxml1empty2Not() {
		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.DEFAULT_EVENT_LATE_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		
		String xml2WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.DEFAULT_EVENT_LATE_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG;

		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml2WithNoDate)>=0);
			assertNotSame(s1,xml1);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * Test where both are empty then its just equal to the xhibit one.
	 */
	public void testWithxml1And2empty() {
		String xml1 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+generateBlankCourtRoom("Court 1")
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		
		String xml2WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG ;

		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml2WithNoDate)>=0);
			assertNotSame(s1,xml1);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * This test tests that if both xml files have the same non empty courtroom that it 
	 * only shows the latest one which is file1.
	 */
	public void testWithSameNonEmptyCourtRoomXhibit() {
		String xml1=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_LATE_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST+CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	
		
		String xml2=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_EARLY_CPP+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 
	
		
		String xml1WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_LATE_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG;
		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml1WithNoDate)>=0);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * This test tests that if both xml files have the same non empty courtroom that it 
	 * only shows the latest one. If both have the same date and time it should only show file1.
	 */
	public void testWithSameNonEmptyCourtRoomEqual() {
		String xml1=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_LATE_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST+CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	
		
		String xml2=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_LATE_CPP+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST
				+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 
	
		
		String xml1WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_LATE_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG;
		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml1WithNoDate)>=0);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}

	
	/**
	 * This test tests that if both xml files have the same non empty courtroom that it 
	 * only shows the latest one which is file2.
	 */
	public void testWithSameNonEmptyCourtRoomCPPLatest() {
		String xml1=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET+CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	
		
		String xml2=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_LATE_CPP+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST
				+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 
	
		
		String xml1WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_LATE_CPP+CPPXMLConstants.DEFAULT_TIMESTATUS_SET_LATEST+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG;
		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml1WithNoDate)>=0);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}

	
	/**
	 * Test where the original xml has a non empty courtroom and 
	 * second one is empty (everything else in the file is identical so when 
	 * merging the merged should match xml1).
	 */
	public void testWithxml2empty1Not() {
		String xml1=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET+CPPXMLConstants.COURT_ROOM_COURT_2+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		
		String xml1WithNoDate = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+ CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_ONE_DEFENDANT + CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_COURT_2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG;

		try {
			String s1 = mergeDocuments(xml1,xml2);

			assertTrue(s1.indexOf(xml1WithNoDate)>=0);
			assertNotSame(s1,xml2);

		} catch(Exception e){
			TestCase.fail();
		}
	}
	
	/**
	 * Test where one of the xml files have a missing component which should
	 * make it fail with a SAXParseException.
	 */
	public void testFailureWithInvalidXml() {

		//xml1 doesn't have <currentcourtstatus> so the merge should throw an exception
		String xml1=CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET+CPPXMLConstants.COURT_ROOM_COURT_2
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG+CPPXMLConstants.COURT_END_TAG
						+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME;
		
		
		String xml2 = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+ CPPXMLConstants.COURT_SITE_TEST2
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.COURT_TWO)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		try {
			mergeDocuments(xml1,xml2);

			//we should not get here
			TestCase.fail();

		} catch(Exception e){
			assertEquals(e.getClass(), SAXParseException.class);
		}
	}
	
	/**
	 * Test to ensure the replace date time works correctly.
	 */
	public void testReplaceDateTime(){
		String xml1 =CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_THREE_DEFENDANTS
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoomsWithinRange(2,25)
				+generateBlankCourtRoomsWithinRange(49,51)
				+generateBlankCourtRoom(CPPXMLConstants.EIGHTY_EIGHT)
				+generateBlankCourtRoom(CPPXMLConstants.NINETY_NINE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_THE_NEW_SITE
				+CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+generateBlankCourtRoom(CPPXMLConstants.TWELVE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_EST
				+CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+generateBlankCourtRoom(CPPXMLConstants.ONE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 
		
		
		
		
		String xml2=CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.COURT_ROOM_2
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_SITE_Z
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT+CPPXMLConstants.COURT_ROOM_2
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.DEFAULT_DATE_TIMESTAMP_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		try {
			Document doc = getDocBuilderWithNoExternals(xml1);
			Document doc2 = getDocBuilderWithNoExternals(xml2);
			
			Document mergedDocument = xmlUtils.merge(doc,doc2);

			Calendar cal = Calendar.getInstance();
			String dayofweek = AbstractXMLMergeUtils.returnDay(cal);
			String date = String.format("%02d", cal.get(Calendar.DAY_OF_MONTH));
			String month = AbstractXMLMergeUtils.returnMonth(cal);
			String hour = String.format("%02d",cal.get(Calendar.HOUR_OF_DAY));
			String min = String.format("%02d",cal.get(Calendar.MINUTE));
			String year = Integer.toString(cal.get(Calendar.YEAR));
						
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList nodes = (NodeList)xpath.evaluate("//datetimestamp",mergedDocument,XPathConstants.NODESET);
			for(int j=0; j<nodes.getLength();j++) {
				NodeList childNodes = nodes.item(j).getChildNodes();
				for(int i=0; i<childNodes.getLength();i++) {
					Element node = (Element)childNodes.item(i);
					if(node.getNodeName().equals("dayofweek")) {
						assertTrue(node.getTextContent().equals(dayofweek));			
					} else if(node.getNodeName().equals("date")) {
						assertTrue(node.getTextContent().equals(date));
					} else if(node.getNodeName().equals("month")) {
						assertTrue(node.getTextContent().equals(month));
					} else if(node.getNodeName().equals("year")) {
						assertTrue(node.getTextContent().equals(year));
					} else if(node.getNodeName().equals("hour")) {
						assertTrue(node.getTextContent().equals(hour));
					} else if(node.getNodeName().equals("min")) {
						assertTrue(node.getTextContent().equals(min));
					}					
				}
			}			

		} catch(Exception e){
			TestCase.fail();
		}
		
	}
	
	public void testExtractCourtSites() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException, TransformerException {
		
		String xhibitXml = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG + CPPXMLConstants.COURT_SITE_SNARESBROOK
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoomsWithinRange(1,25)
				+ generateBlankCourtRoom("49")
				+ CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG
				+ CPPXMLConstants.SNARESBROOK_PAGENAME + CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
		
		
		String cppXml = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG + CPPXMLConstants.COURT_SITE_SNARESBROOK
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG + CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_THREE_DEFENDANTS + CPPXMLConstants.DEFAULT_EVENT_EARLY_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_1
				+ CPPXMLConstants.COURT_ROOM_END_TAG
		+generateBlankCourtRoomsWithinRange(2,25)
		+generateBlankCourtRoomsWithinRange(49,51)
		+generateBlankCourtRoom(CPPXMLConstants.EIGHTY_EIGHT)
		+generateBlankCourtRoom(CPPXMLConstants.NINETY_NINE)
		+CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_SITE_THE_NEW_SITE
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.TWELVE)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG 
				+ CPPXMLConstants.COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_SITE_EST 
				+ CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+ generateBlankCourtRoom(CPPXMLConstants.ONE)
				+ CPPXMLConstants.COURT_ROOMS_END_TAG
				+ CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG + CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 
	
		
		String expectedXml = CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG + CPPXMLConstants.COURT_NAME_SNARESBROOK
				+ CPPXMLConstants.COURT_SITES_ENTRY_TAG + CPPXMLConstants.COURT_SITE_SNARESBROOK
				+ CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG + CPPXMLConstants.DEFAULT_T_CASE
				+ CPPXMLConstants.DEFAULT_THREE_DEFENDANTS + CPPXMLConstants.DEFAULT_EVENT_EARLY_CPP
				+ CPPXMLConstants.DEFAULT_TIMESTATUS_SET + CPPXMLConstants.COURT_ROOM_1
				+ CPPXMLConstants.COURT_ROOM_END_TAG 
		+generateBlankCourtRoomsWithinRange(2,25)
		+generateBlankCourtRoomsWithinRange(49,50)
		+CPPXMLConstants.COURT_ROOMS_END_TAG + CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+ CPPXMLConstants.COURT_END_TAG + CPPXMLConstants.SNARESBROOK_PAGENAME
				+ CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;

		String actual = mergeDocuments(xhibitXml,cppXml);		
		assertTrue(actual.equals(expectedXml));		
	}
	
	/**
	 * Testing the get courtsites functionality works correctly.
	 * @throws XPathExpressionException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void testGetCourtSites() throws XPathExpressionException, ParserConfigurationException, SAXException, IOException {
		String xml1 =
				CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_COURT_SITE_ENTRY_TAG
				+ "\n<courtsitename>\nSNARESBROOK mu</courtsitename>"
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE+CPPXMLConstants.DEFAULT_THREE_DEFENDANTS
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1+CPPXMLConstants.COURT_ROOM_END_TAG
		+ generateBlankCourtRoomsWithinRange(2,25)
		+ generateBlankCourtRoomsWithinRange(49,51)
		+ generateBlankCourtRoom(CPPXMLConstants.EIGHTY_EIGHT)+generateBlankCourtRoom(CPPXMLConstants.NINETY_NINE)
		+ CPPXMLConstants.COURT_ROOMS_END_TAG+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_THE_NEW_SITE
				+CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+generateBlankCourtRoom(CPPXMLConstants.TWELVE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_EST
				+CPPXMLConstants.COURT_ROOMS_ENTRY_TAG
				+generateBlankCourtRoom(CPPXMLConstants.ONE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG; 

		Document xhibitDoc = getDocBuilderWithNoExternals(xml1);
		
		ArrayList<Node> n = (ArrayList<Node>) FormattingServices.getCourtSites(xhibitDoc);
		assertTrue(n.size()==3);
		assertTrue(n.get(0).getNodeValue().replaceAll("\n", "").replace("\r", "").equalsIgnoreCase("snaresbrook mu"));
		assertTrue(n.get(1).getNodeValue().equalsIgnoreCase("THENEWSITE"));
		assertTrue(n.get(2).getNodeValue().equalsIgnoreCase("est vs"));

	}
	
	/**
	 * Test an unknown courtroom doesn't get merged in
	 * xml1 has snaresbrook with courtroom1 and courtroom2 and courtroom3 (courtroom1 not empty and 2 and 3 empty)
	 * xml2 has snaresbrook with courtroom1 and courtroom 3 and courtroom 4 and test1 with courtroom1
	 * expected xml should have snaresbrook with courtroom1 from xml1, blank courtroom2 and courtroom3 from xml2
	 */
	public void testUnknownCourtRoomAndKnownCourtroom() {
		try {
		String xhibitXml = get1CourtroomSite2CourtRooms();
		String cppXml = get2CourtSite4CourtRooms ();
		String expectedMinusTime = getMergedXmlOf1and2();
		
		String actual = mergeDocuments(xhibitXml,cppXml);

		actual = removeDate(actual);
		assertTrue(actual.equals(expectedMinusTime));	
		} catch(Exception e) {
			TestCase.fail();
		}
		
	}
	
	/**
	 * Test an unknown courtroom doesn't get merged in and neither does an invalid one
	 * xml1 has snaresbrook with courtroom1 and courtroom2 and courtroom3 (courtroom1 not empty and 2 and 3 empty)
	 * xml2 has snaresbrook with courtroom1 invalid name and courtroom 3 and courtroom 4 and invalid nameand test1 with courtroom1
	 * expected xml should have snaresbrook with courtroom1 from xml1, blank courtroom2 and courtroom3 from xml2
	 */
	public void testUnknownCourtRoomAndInvalidCourtroom() {
		try {
		String xhibitXml = get1CourtroomSite2CourtRooms();
		String cppXml = get2CourtSite5CourtRoomsWithInvalidName ();
		String expectedMinusTime = getMergedXmlOf1and2();
		
		String actual = mergeDocuments(xhibitXml,cppXml);
		actual = removeDate(actual);
		assertTrue(actual.equals(expectedMinusTime));	
		} catch(Exception e) {
			TestCase.fail();
		}
		
	}
	
	/**
	 * Method to remove the date part of an xml
	 * @param xml to remove from
	 * @return string without the datetimestamp element
	 */
	private String removeDate(String xml) {
		xml= xml.replaceAll("<datetimestamp>.*?</datetimestamp>", "");
		
		xml = xml.replaceAll(NEW_LINE,"");
		xml = xml.replaceAll(RETURN_C, "");	
		xml =xml.substring(xml.indexOf(CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG),xml.indexOf(CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG)+21);
		return xml;
	}

	/**
	 * Method used in this test class to transform the Document to pure String so that we can use it to compare.
	 */
	private String transformXml(Document doc) throws TransformerException {
		String s1 =xmlUtils.docToString(doc);
		s1 = s1.replaceAll(NEW_LINE,"");
		s1 = s1.replaceAll(RETURN_C, "");	
		s1 =s1.substring(s1.indexOf(CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG),s1.indexOf(CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG)+21);
		return s1;
	}
	
	/**
	 * Xml that includes:
	 * snaresbrook with courtroom 1 filled and courtroom 2 and 3 empty;
	 * @return String xml ;
	 */
	private String get1CourtroomSite2CourtRooms() {
		return CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoom(CPPXMLConstants.TWO)
				+generateBlankCourtRoom(CPPXMLConstants.THREE)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	}
	
	/**
	 * Xml that includes:
	 * snaresbrook with courtroom1 filled and courtroom 3 filled and courtroom 4 blank
	 * test1 with courtroom1 filled
	 * @return String xml 
	 */
	private String get2CourtSite4CourtRooms() {
		return CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+CPPXMLConstants.COURT_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_3
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoom(CPPXMLConstants.FOUR)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_TEST1
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	}
	
	/**
	 * Xml that includes:
	 * snaresbrook with courtroom1 incorrectly named filled and courtroom 3 filled and courtroom 4 blank and incorrectly named
	 * test1 with courtroom1 filled
	 * @return String xml 
	 */
	private String get2CourtSite5CourtRoomsWithInvalidName() {
		return CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+CPPXMLConstants.COURT_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_3
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoom(CPPXMLConstants.FOUR)
				+CPPXMLConstants.COURT_ROOMS_END_TAG
				+CPPXMLConstants.COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_SITE_TEST1
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	}
	
	/**
	 * Merger of the two above.
	 * @return String Xml that includes:
	 * snaresbrook with courtroom 1 filled and courtroom 2 empty and 3 filled with a cpp one;
	 * @return String xml 
	 */
	public String getMergedXmlOf1and2() {
		return CPPXMLConstants.CURRENT_COURT_STATUS_ENTRY_TAG
				+CPPXMLConstants.COURT_NAME_SNARESBROOK
				+CPPXMLConstants.COURT_SITES_ENTRY_TAG
				+CPPXMLConstants.COURT_SITE_SNARESBROOK
				+CPPXMLConstants.COURT_ROOMS_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_1
				+CPPXMLConstants.COURT_ROOM_END_TAG
				+generateBlankCourtRoom(CPPXMLConstants.TWO)
				+CPPXMLConstants.COURT_ROOM_ENTRY_TAG
				+CPPXMLConstants.DEFAULT_T_CASE_CPP_INFO
				+CPPXMLConstants.DEFAULT_ONE_DEFENDANT
				+CPPXMLConstants.DEFAULT_EVENT_EARLY_XHIBIT
				+CPPXMLConstants.DEFAULT_TIMESTATUS_SET
				+CPPXMLConstants.COURT_ROOM_3
				+CPPXMLConstants.COURT_ROOMS_ROOM_END_TAG
				+CPPXMLConstants.COURT_SITES_COURT_SITE_END_TAG
				+CPPXMLConstants.COURT_END_TAG
				+CPPXMLConstants.SNARESBROOK_PAGENAME
				+CPPXMLConstants.CURRENT_COURT_STATUS_END_TAG;
	}
	
	private String generateBlankCourtRoom(String number) {
		return CPPXMLConstants.BLANK_COURT_ROOM_ENTRY_TAG +number+CPPXMLConstants.BLANK_COURT_ROOM_END_TAG;
	}
	
	private String generateBlankCourtRoomsWithinRange(int start, int end) {
		StringBuilder buff = new StringBuilder();
		for(int i=start;i<end;i++) {
			buff.append(generateBlankCourtRoom(Integer.toString(i)));
		}
		return buff.toString();
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
		
		Document mergedDocument = xmlUtils.merge(doc,doc2);
		return transformXml(mergedDocument);
	}
}