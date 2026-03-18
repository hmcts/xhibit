package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.CppDataSourceFactory;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCourtStatusValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XhbCourtBeanHelper2.class,CppFormattingHelper.class})
@SuppressStaticInitializationFor("uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2")

public class TestAllCourtStatusCppToPublicDisplay extends TestAbstractCppToPublicDisplay {
	
	protected static int[] courtRoomIds = {8122,8123,8124};
	protected static int courtId = 81;
	
	@Mock
	protected XhbCourtBeanHelper2 courtHelper;
	@Mock
	protected CppFormattingHelper cppFormattingHelper;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(TestAllCourtStatusCppToPublicDisplay.class);	
	}

	public TestAllCourtStatusCppToPublicDisplay() {
		super(new AllCourtStatusCppToPublicDisplay(new Date(), courtId, courtRoomIds) );
	}
	
	public TestAllCourtStatusCppToPublicDisplay(CourtDetailCppToPublicDisplay courtDetailCppToPublicDisplay) {
		super(courtDetailCppToPublicDisplay);
	}

	/**
	 * Tests the fields are set as expected in the constructor
	 * @throws Exception
	 */
	public void testConstructor() throws Exception {
		// Test the Court Id set in the constructor
		assertEquals("Error in field CourtId", courtId, cppToPublicDisplay.getCourtId());
		
		// Test the Date set in the constructor
		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		assertEquals("Error in field Date", df.format(new Date()), df.format(cppToPublicDisplay.getDate()));
		
		// Test the Court Room Id array set in the constructor
		int[] testArray = cppToPublicDisplay.getCourtRoomIds();
		assertEquals("Error in field CourtRoomId length", courtRoomIds.length, testArray.length);
		for (int i=0; i<courtRoomIds.length; i++) {
			assertEquals("Error in field CourtRoomId, index: " + i, courtRoomIds[i], testArray[i]);
		}
	}
	
	/**
	 * Tests that for a valid single courtroom CPP XML CLOB, the data is assigned to the AllCourtStatusValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSingleCourtroomXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>0</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>Z</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>Anyone called Mark must wear a clown costume</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		assertEquals("Error in cppData size", 1, cppData.size());
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "PDCase456", cppData.get(0).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getEventTimeAsString()", "09:30", cppData.get(0).getEventTimeAsString());
		
		// Check Defendants
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 2, cppData.get(0).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(0).getDefendantNames();
		assertEquals("Error First DefendantName", "HOMER SIMPSON", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "MARGE SIMPSON", defNames.get(1).getName());
		assertEquals("Error AllCourtStatusValue.isReportingRestricted()", false, cppData.get(0).isReportingRestricted());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that for a valid multiple courtroom CPP XML CLOB, the data is assigned to the AllCourtStatusValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testMultipleCourtroomXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>1</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>5</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>1</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><casenumber>20190013</casenumber><casetype>T</casetype><activecase>1</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>MR</firstname><lastname>SMEG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 13</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		assertEquals("Error in cppData size", 2, cppData.size());
		
		// First AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.isReportingRestricted()", true, cppData.get(0).isReportingRestricted());

		// Second AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 13", cppData.get(1).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "T20190013", cppData.get(1).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 1, cppData.get(1).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(1).getDefendantNames();
		assertEquals("Error First DefendantName", "MR SMEG", defNames.get(0).getName());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data is combined with Xhibit data and then the collection is sorted and duplicates 
	 * removed, the resulting collection of AllCourtStatusValue values is the correct size and has been sorted
	 * as expected.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSortAndDuplicateXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Peter Rabbit</judgename><notbeforetime>11:00</notbeforetime><timestatusset>11:00</timestatusset>" +
				"<defendants><defendant><firstname>BENJAMIN</firstname><lastname>BUNNY</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>11:00</time><date>18/12/19</date><free_text>Judge is scratching his bottom</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>11:00</timestatusset><courtroomname>Court 13</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>1</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>09:30</notbeforetime><timestatusset>13:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>-1</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>3</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>13:30</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>13:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"</courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		// Retrieve a mock set of Xhibit Data (rooms 11 and 12 empty but room 13 with a hearing)
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> xhibitData = (List<AllCourtStatusValue>)getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData();
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 5 objects in the list (3 from Xhibit and 2 from CPP)
		assertEquals("Error in cppData size", 5, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> processedData = (List<AllCourtStatusValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.ALLCOURTSTATUS_TYPE, xhibitData);
		
		// Check basic information after post processing, should be 3 objects in the list now after sorting and removal of duplicates
		assertEquals("Error in cppData size", 3, processedData.size());
		
		// First AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 11", processedData.get(0).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "PDCase123", processedData.get(0).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 2, processedData.get(0).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) processedData.get(0).getDefendantNames();
		assertEquals("Error First DefendantName", "SAD KEN", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "NOGGIN THE NOG", defNames.get(1).getName());
		assertEquals("Error AllCourtStatusValue.isReportingRestricted()", false, processedData.get(0).isReportingRestricted());
		assertEquals("Error AllCourtStatusValue.hasInformationForDisplay()", true, processedData.get(0).hasInformationForDisplay());
		
		// Second AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 12", processedData.get(1).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "", processedData.get(1).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 0, processedData.get(1).getDefendantNames().size());
		assertEquals("Error AllCourtStatusValue.hasInformationForDisplay()", false, processedData.get(1).hasInformationForDisplay());

		// Third AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 13", processedData.get(2).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "PDCase456", processedData.get(2).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 1, processedData.get(2).getDefendantNames().size());
		defNames = (ArrayList<DefendantName>) processedData.get(2).getDefendantNames();
		assertEquals("Error First DefendantName", "BENJAMIN BUNNY", defNames.get(0).getName());
		assertEquals("Error AllCourtStatusValue.hasInformationForDisplay()", true, processedData.get(2).hasInformationForDisplay());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data has no active cases that no information is returned 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testNoActiveCases() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><activecase>0</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Peter Rabbit</judgename><notbeforetime>11:00</notbeforetime><timestatusset>11:00</timestatusset>" +
				"<defendants><defendant><firstname>BENJAMIN</firstname><lastname>BUNNY</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>11:00</time><date>18/12/19</date><free_text>Judge is scratching his bottom</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>11:00</timestatusset><courtroomname>Court 13</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>09:30</notbeforetime><timestatusset>13:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>-1</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>3</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>13:30</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>13:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"</courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData();
		
		// Check that although two courtrooms have cases, none are active so the list should be empty
		assertEquals("Error in cppData size", 0, cppData.size());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when bad XML is passed in, the correct Exception is thrown
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testBadXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtnamef>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom>" +
				"<timestatusset>09:30</timestatusset><courtroomname>Court 15</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		try {
			@SuppressWarnings({ "unchecked", "unused" })
			List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData();
		}
		catch (Exception e) {
			assertEquals(e.getClass(), XPathExpressionException.class);
		}
		
		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when there is no CPP data then no exceptions are thrown and only the Xhibit data is used 
	 * NOTE - NOT CURRENTLY WORKING USING MOCKITO - NEEDS LOOKING INTO
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	/**
	public void testNoCPPData() throws Exception {
		
		// Mocked methods
		PowerMockito.when(cppFormattingHelper.getLatestPublicDisplayDocument(courtId)).thenReturn(getNullCppFormattingBasicValue());
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		// Retrieve a mock set of Xhibit Data (rooms 11 and 12 empty but room 13 with a hearing)
		List<AllCourtStatusValue> xhibitData = getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> cppData = (List<AllCourtStatusValue>) cppToPublicDisplay.getCppData(); // EXCEPTION THROWN HERE
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 3 objects in the list (3 from Xhibit and 0 from CPP)
		assertEquals("Error in cppData size", 3, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<AllCourtStatusValue> processedData = (List<AllCourtStatusValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.ALLCOURTSTATUS_TYPE, xhibitData);
		
		// Check basic information after post processing, should be 3 objects in the list now after sorting and removal of duplicates
		assertEquals("Error in cppData size", 3, processedData.size());
		
		// First AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 11", processedData.get(0).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "", processedData.get(0).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 0, processedData.get(0).getDefendantNames().size());
		
		// Second AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 12", processedData.get(1).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "", processedData.get(1).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 0, processedData.get(1).getDefendantNames().size());

		// Third AllCourtStatusValue object
		assertEquals("Error AllCourtStatusValue.getCourtRoomName()", "Court Room 13", processedData.get(2).getCourtRoomName());
		assertEquals("Error AllCourtStatusValue.getCaseNumber()", "T20190013", processedData.get(2).getCaseNumber());
		assertEquals("Error AllCourtStatusValue.getDefendantNames()", 1, processedData.get(2).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) processedData.get(2).getDefendantNames();
		assertEquals("Error First DefendantName", "MR SMEG", defNames.get(0).getName());

		// Verify that the courtHelper and cppFormattingHelper have been invoked
		Mockito.verify(courtHelper);
		Mockito.verify(cppFormattingHelper);
	}*/
	
	/**
	 * Returns a null CppFormattingBasicValue object
	 * @return CppFormattingBasicValue
	 */
	protected CppFormattingBasicValue getNullCppFormattingBasicValue() {
		CppFormattingBasicValue o = null;
		return o;
	}
	
	/**
	 * Protected method to generate a mock XhbCourtBasicValue for Snaresbrook
	 * @return XhbCourtBasicValue
	 */
	protected XhbCourtBasicValue getXhbCourtObject() {
		XhbCourtBasicValue xhbCourt = new XhbCourtBasicValue();
		xhbCourt.setCourtId(courtId);
		xhbCourt.setCourtName("SNARESBROOK");
		xhbCourt.setCppCourt("Y");
		return xhbCourt;
	}
	
	/**
	 * Protected method to generate a mock CourtStructureValue for Snaresbrook
	 * @return CourtStructureValue
	 */
	protected CourtStructureValue getCourtStructureValue() {
		CourtStructureValue courtStructure = new CourtStructureValue();
		// Set the Court object
		courtStructure.setCourt( getXhbCourtObject() );
		
		// Set the Court Sites
		XhbCourtSiteBasicValue siteA = new XhbCourtSiteBasicValue();
		siteA.setCourtSiteId(1610);
		siteA.setCourtSiteCode("A");
		siteA.setCourtSiteName("SNARESBROOK");
		XhbCourtSiteBasicValue[] siteArray = {siteA};
		courtStructure.setCourtSites( siteArray );
		
		// Set the Court Rooms
		XhbCourtRoomBasicValue room11 = new XhbCourtRoomBasicValue();
		room11.setCourtRoomId(8122);
		room11.setCourtRoomName("Court 11");
		room11.setDisplayName("Court Room 11");
		room11.setCrestCourtRoomNo((byte)11);
		room11.setCourtSiteId(1610);
		XhbCourtRoomBasicValue room12 = new XhbCourtRoomBasicValue();
		room12.setCourtRoomId(8123);
		room12.setCourtRoomName("Court 12");
		room12.setDisplayName("Court Room 12");
		room12.setCourtSiteId(1610);
		room12.setCrestCourtRoomNo((byte)12);
		XhbCourtRoomBasicValue room13 = new XhbCourtRoomBasicValue();
		room13.setCourtRoomId(8124);
		room13.setCourtRoomName("Court 13");
		room13.setDisplayName("Court Room 13");
		room13.setCourtSiteId(1610);
		room13.setCrestCourtRoomNo((byte)13);
		XhbCourtRoomBasicValue[] roomArray = {room11,room12,room13};
		courtStructure.addCourtRooms(1610, roomArray);
		
		return courtStructure;
	}
	
	/**
	 * Protected method that returns a mock list of Xhibit AllCourtStatusValue objects
	 * @return
	 */
	protected List<?> getXhibitData() {
		List<AllCourtStatusValue> data = new ArrayList<AllCourtStatusValue>();
		
		AllCourtStatusValue room11 = new AllCourtStatusValue();
		room11.setCourtRoomName("Court Room 11");
		room11.setCourtSiteCode("A");
		room11.setCourtSiteName("SNARESBROOK");
		room11.setCrestCourtRoomNo(11);
		room11.setEventTime(Timestamp.valueOf("2019-12-18 18:00:00.0"));
		room11.setMovedFromCourtRoomId(0);
		room11.setReportingRestricted(false);
		data.add(room11);
		
		AllCourtStatusValue room12 = new AllCourtStatusValue();
		room12.setCourtRoomName("Court Room 12");
		room12.setCourtSiteCode("A");
		room12.setCourtSiteName("SNARESBROOK");
		room12.setCrestCourtRoomNo(12);
		room12.setEventTime(Timestamp.valueOf("2019-12-18 18:00:00.0"));
		room12.setMovedFromCourtRoomId(0);
		room12.setReportingRestricted(false);
		data.add(room12);
		
		AllCourtStatusValue room13 = new AllCourtStatusValue();
		room13.setCourtRoomName("Court Room 13");
		room13.setCourtSiteCode("A");
		room13.setCourtSiteName("SNARESBROOK");
		room13.setCrestCourtRoomNo(13);
		room13.setEventTime(Timestamp.valueOf("2019-12-18 10:00:00.0"));
		room13.setMovedFromCourtRoomId(0);
		room13.setReportingRestricted(false);
		room13.setCaseNumber("T20190013");
		room13.addDefendantName(new DefendantName("MR", null, "SMEG", false));
		data.add(room13);
		
		return data;
	}
	
}