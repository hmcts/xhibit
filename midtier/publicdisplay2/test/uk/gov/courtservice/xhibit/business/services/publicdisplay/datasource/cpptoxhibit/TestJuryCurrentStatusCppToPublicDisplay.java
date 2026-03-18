package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_site.XhbCourtSiteBasicValue;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.CppDataSourceFactory;
import uk.gov.courtservice.xhibit.business.vos.entities.CppFormattingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.court.CourtStructureValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.JuryStatusDailyListValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

@RunWith(PowerMockRunner.class)
@PrepareForTest({XhbCourtBeanHelper2.class,CppFormattingHelper.class})
@SuppressStaticInitializationFor("uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2")

public class TestJuryCurrentStatusCppToPublicDisplay extends TestAbstractCppToPublicDisplay {
	
	protected static int[] courtRoomIds = {8122,8123,8124};
	protected static int courtId = 81;
	
	@Mock
	protected XhbCourtBeanHelper2 courtHelper;
	@Mock
	protected CppFormattingHelper cppFormattingHelper;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(TestJuryCurrentStatusCppToPublicDisplay.class);	
	}

	public TestJuryCurrentStatusCppToPublicDisplay() {
		super(new JuryCurrentStatusCppToPublicDisplay(new Date(), courtId, courtRoomIds) );
	}
	
	public TestJuryCurrentStatusCppToPublicDisplay(JuryCurrentStatusCppToPublicDisplay juryCurrentStatusCppToPublicDisplay) {
		super(juryCurrentStatusCppToPublicDisplay);
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
	 * Tests that for a valid single courtroom CPP XML CLOB, the data is assigned to the JuryStatusDailyListValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSingleCourtroomXML() throws Exception {
		
		// Setup the courtListCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>0</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>09:30</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>Anyone called Mark must wear a clown costume</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<JuryStatusDailyListValue> cppData = (List<JuryStatusDailyListValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		assertEquals("Error in cppData size", 2, cppData.size());
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());
		assertEquals("Error JuryStatusDailyListValue.getNotBeforeTimeAsString()", "09:30", cppData.get(0).getNotBeforeTimeAsString());
		
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", cppData.get(1).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase456", cppData.get(1).getCaseNumber());
		assertEquals("Error JuryStatusDailyListValue.getNotBeforeTimeAsString()", "10:00", cppData.get(1).getNotBeforeTimeAsString());
		
		// Check Defendants
		assertEquals("Error JuryStatusDailyListValue.getDefendantNames()", 2, cppData.get(1).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(1).getDefendantNames();
		assertEquals("Error First DefendantName", "HOMER SIMPSON", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "MARGE SIMPSON", defNames.get(1).getName());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that for a valid multiple courtroom CPP XML CLOB, the data is assigned to the JuryStatusDailyListValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testMultipleCasesinDifferentRoomXML() throws Exception {
		
		// Setup the courtListCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><casenumber>20190013</casenumber><casetype>T</casetype><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>MR</firstname><lastname>SMEG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 12</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<JuryStatusDailyListValue> cppData = (List<JuryStatusDailyListValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		// should only return data from court room 11 and 12 - the two records
		// record 1
		assertEquals("Error in cppData size", 2, cppData.size());
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());
		assertEquals("Error JuryStatusDailyListValue.getNotBeforeTimeAsString()", "10:00", cppData.get(0).getNotBeforeTimeAsString());
		
		// Check Defendants
		assertEquals("Error JuryStatusDailyListValue.getDefendantNames()", 2, cppData.get(0).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(0).getDefendantNames();
		assertEquals("Error First DefendantName", "SAD KEN", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "NOGGIN THE NOG", defNames.get(1).getName());
		
		// record 2
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 12", cppData.get(1).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "T20190013", cppData.get(1).getCaseNumber());
		assertEquals("Error JuryStatusDailyListValue.getNotBeforeTimeAsString()", "11:00", cppData.get(1).getNotBeforeTimeAsString());
		
		// Check Defendants
		assertEquals("Error JuryStatusDailyListValue.getDefendantNames()", 1, cppData.get(1).getDefendantNames().size());
		ArrayList<DefendantName> defNames2 = (ArrayList<DefendantName>) cppData.get(1).getDefendantNames();
		assertEquals("Error First DefendantName", "MR SMEG", defNames2.get(0).getName());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data is combined with Xhibit data and then the collection is sorted and duplicates 
	 * removed, the resulting collection of JuryStatusDailyListValue values is the correct size and has been sorted
	 * as expected.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSortXML() throws Exception {
		
		// Setup the courtListCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>0</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress></hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:30</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>09:55</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>Anyone called Mark must wear a clown costume</publicnotice></publicnotices>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom></courtrooms>" +
				"<floating><cases><caseDetails><cppurn>PDCase789</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases></floating></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		// Retrieve a mock set of Xhibit Data 
		@SuppressWarnings("unchecked")
		List<JuryStatusDailyListValue> xhibitData = (List<JuryStatusDailyListValue>)getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<JuryStatusDailyListValue> cppData = (List<JuryStatusDailyListValue>) cppToPublicDisplay.getCppData();
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 6 objects in the list (3 from Xhibit and 2 from CPP plus 1 a floater from xhibit)
		assertEquals("Error in cppData size", 6, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<JuryStatusDailyListValue> processedData = (List<JuryStatusDailyListValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.JURYCURRENTSTATUS_TYPE, xhibitData);
		
		// Check basic information after post processing, should be 6 objects in the list now after sorting and removal of duplicates
		assertEquals("Error in cppData size", 6, processedData.size());
		
		// First JuryStatusDailyListValue object		
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", processedData.get(0).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase456", processedData.get(0).getCaseNumber());			
		// Second JuryStatusDailyListValue object
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", processedData.get(1).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "T20190013", processedData.get(1).getCaseNumber());
		// Third JuryStatusDailyListValue object
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", processedData.get(2).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase123", processedData.get(2).getCaseNumber());
		// Fourth JuryStatusDailyListValue object
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 11", processedData.get(3).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "T20190015", processedData.get(3).getCaseNumber());
		// Fifth JuryStatusDailyListValue object
		assertEquals("Error JuryStatusDailyListValue.getCourtRoomName()", "Court Room 12", processedData.get(4).getCourtRoomName());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "T20190014", processedData.get(4).getCaseNumber());	
		// sixth JuryStatusDailyListValue object - floater
		assertEquals("Error JuryStatusDailyListValue.getFloating()", "1", processedData.get(5).getFloating());
		assertEquals("Error JuryStatusDailyListValue.getCaseNumber()", "PDCase789", processedData.get(5).getCaseNumber());	

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
		
		// Setup the courtListCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom>" +
				"<timestatusset>09:30</timestatusset><courtroomname>Court 15</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		try {
			@SuppressWarnings({ "unchecked", "unused" })
			List<JuryStatusDailyListValue> cppData = (List<JuryStatusDailyListValue>) cppToPublicDisplay.getCppData();
		}
		catch (Exception e) {
			assertEquals(e.getClass(), XPathExpressionException.class);
		}
		
		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	

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
		siteA.setDisplayName("Court Site A");
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
		room12.setCrestCourtRoomNo((byte)12);
		room12.setCourtSiteId(1610);
		
		XhbCourtRoomBasicValue[] roomArray = {room11, room12};
		courtStructure.addCourtRooms(1610, roomArray);
		
		return courtStructure;
	}
	
		
	/**
	 * Protected method that returns a mock list of Xhibit JuryStatusDailyListValue objects
	 * @return
	 */
	protected List<?> getXhibitData() {
		List<JuryStatusDailyListValue> data = new ArrayList<JuryStatusDailyListValue>();	
		
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    	Date today = Calendar.getInstance().getTime();
    	String todayString = dateFormat.format(today);	// Combine the time with the date to complete the timestamp otherwise uses 1970
		
		JuryStatusDailyListValue room11a = new JuryStatusDailyListValue();
		room11a.setCourtRoomName("Court Room 11");
		room11a.setCourtSiteCode("A");
		room11a.setCourtSiteName("SNARESBROOK");
		room11a.setCrestCourtRoomNo(11);
		room11a.setNotBeforeTime(Timestamp.valueOf(todayString + " 11:30:00.0"));
		room11a.setMovedFromCourtRoomId(0);
		room11a.setReportingRestricted(false);
		room11a.setCaseNumber("T20190015");
		room11a.setFloating("0");
		room11a.addDefendantName(new DefendantName("MR", null, "BOB", false));
		data.add(room11a);
		
		JuryStatusDailyListValue room11b = new JuryStatusDailyListValue();
		room11b.setCourtRoomName("Court Room 12");
		room11b.setCourtSiteCode("A");
		room11b.setCourtSiteName("SNARESBROOK");
		room11b.setCrestCourtRoomNo(12);
		room11b.setNotBeforeTime(Timestamp.valueOf(todayString + " 09:00:00.0"));
		room11b.setMovedFromCourtRoomId(0);
		room11b.setReportingRestricted(false);
		room11b.setCaseNumber("T20190014");
		room11b.setFloating("0");
		room11b.addDefendantName(new DefendantName("MR", null, "JONES", false));
		data.add(room11b);
		
		JuryStatusDailyListValue room11c = new JuryStatusDailyListValue();
		room11c.setCourtRoomName("Court Room 11");
		room11c.setCourtSiteCode("A");
		room11c.setCourtSiteName("SNARESBROOK");
		room11c.setCrestCourtRoomNo(11);
		room11c.setNotBeforeTime(Timestamp.valueOf(todayString + " 10:00:00.0"));
		room11c.setMovedFromCourtRoomId(0);
		room11c.setReportingRestricted(false);
		room11c.setCaseNumber("T20190013");
		room11c.setFloating("0");
		room11c.addDefendantName(new DefendantName("MR", null, "SMEG", false));
		data.add(room11c);
		
		return data;
	}
	
}