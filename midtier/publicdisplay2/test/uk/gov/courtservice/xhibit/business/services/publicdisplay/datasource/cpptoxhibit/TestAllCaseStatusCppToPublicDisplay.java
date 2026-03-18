package uk.gov.courtservice.xhibit.business.services.publicdisplay.datasource.cpptoxhibit;

import org.powermock.modules.junit4.PowerMockRunner;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.gov.courtservice.xhibit.business.entities.xhb_clob.XhbClobBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.cppformatting.CppFormattingHelper;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.CppDataSourceFactory;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.AllCaseStatusValue;
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

public class TestAllCaseStatusCppToPublicDisplay extends TestSummaryByNameCppToPublicDisplay {
	
	protected static int[] courtRoomIds = {8122,8123,8124};
	protected static int courtId = 81;
	
	@Mock
	protected XhbCourtBeanHelper2 courtHelper;
	@Mock
	protected CppFormattingHelper cppFormattingHelper;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(TestAllCaseStatusCppToPublicDisplay.class);	
	}

	public TestAllCaseStatusCppToPublicDisplay() {
		super(new AllCaseStatusCppToPublicDisplay(new Date(), courtId, courtRoomIds) );
	}

	/**
	 * Tests the fields are set as expected in the constructor
	 * @throws Exception
	 */
	@Override
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
	 * Tests that for a valid single courtroom with one case and two defendants, the data is assigned to the AllCaseStatusValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
	public void testSingleCourtroomXML() throws Exception {
		
		// Setup the SummaryByNameCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<currentstatus><event><time>10:15</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"<listcourtroomname>Court 12</listcourtroomname><movedfromcourtsitename>Court Site B</movedfromcourtsitename><movedfromcourtroomname>Court Room 22</movedfromcourtroomname>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom></courtrooms>" +
				"<floating><cases><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases></floating></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<AllCaseStatusValue> cppData = (List<AllCaseStatusValue>) cppToPublicDisplay.getCppData();
		
		// Check the collection of AllCaseStatusValue CPP data
		assertEquals("Error in cppData size", 4, cppData.size());
		// First record
		assertEquals("Error AllCaseStatusValue1.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue1.getNotBeforeTimeAsString()", "10:00", cppData.get(0).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue1.getDefendantName().getName()", "SAD KEN", cppData.get(0).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue1.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());
		assertEquals("Error AllCaseStatusValue1.getHearingDescription()", "Plea and Trial Preparation", cppData.get(0).getHearingDescription());
		assertEquals("Error AllCaseStatusValue1.getHearingProgress()", 0, cppData.get(0).getHearingProgress());
		assertEquals("Error AllCaseStatusValue1.getEventTimeAsString()", "10:15", cppData.get(0).getEventTimeAsString());
		assertEquals("Error AllCaseStatusValue1.isFloating()", false, cppData.get(0).isFloating());
		assertEquals("Error AllCaseStatusValue1.getListCourtRoomId()", 8123, cppData.get(0).getListCourtRoomId());
		assertEquals("Error AllCaseStatusValue1.getMovedFromCourtSiteShortName()", "Court Site B", cppData.get(0).getMovedFromCourtSiteShortName());
		assertEquals("Error AllCaseStatusValue1.getMovedFromCourtRoomName()", "Court Room 22", cppData.get(0).getMovedFromCourtRoomName());
		
		// Second record
		assertEquals("Error AllCaseStatusValue2.getCourtRoomName()", "Court Room 11", cppData.get(1).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue2.getNotBeforeTimeAsString()", "10:00", cppData.get(1).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue2.getDefendantName().getName()", "NOGGIN THE NOG", cppData.get(1).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue2.getCaseNumber()", "PDCase123", cppData.get(1).getCaseNumber());
		assertEquals("Error AllCaseStatusValue2.getHearingDescription()", "Plea and Trial Preparation", cppData.get(1).getHearingDescription());
		assertEquals("Error AllCaseStatusValue2.getHearingProgress()", 0, cppData.get(1).getHearingProgress());
		assertEquals("Error AllCaseStatusValue2.getEventTimeAsString()", "10:15", cppData.get(1).getEventTimeAsString());
		assertEquals("Error AllCaseStatusValue2.isFloating()", false, cppData.get(1).isFloating());
		assertEquals("Error AllCaseStatusValue2.getListCourtRoomId()", 8123, cppData.get(1).getListCourtRoomId());
		assertEquals("Error AllCaseStatusValue2.getMovedFromCourtSiteShortName()", "Court Site B", cppData.get(1).getMovedFromCourtSiteShortName());
		assertEquals("Error AllCaseStatusValue2.getMovedFromCourtRoomName()", "Court Room 22", cppData.get(1).getMovedFromCourtRoomName());
		
		// Third record
		assertEquals("Error AllCaseStatusValue3.getCourtRoomName()", "", cppData.get(2).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue3.getCourtSiteCode()", "A", cppData.get(2).getCourtSiteCode());
		assertEquals("Error AllCaseStatusValue3.getNotBeforeTimeAsString()", "14:00", cppData.get(2).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue3.getDefendantName().getName()", "HOMER SIMPSON", cppData.get(2).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue3.getCaseNumber()", "PDCase456", cppData.get(2).getCaseNumber());
		assertEquals("Error AllCaseStatusValue3.getHearingDescription()", "For Mention", cppData.get(2).getHearingDescription());
		assertEquals("Error AllCaseStatusValue3.getHearingProgress()", 0, cppData.get(2).getHearingProgress()); // If no Hearing Progress set, should default to 0
		assertEquals("Error AllCaseStatusValue3.getEventTimeAsString()", "09:30", cppData.get(2).getEventTimeAsString());
		assertEquals("Error AllCaseStatusValue3.isFloating()", true, cppData.get(2).isFloating());
		assertEquals("Error AllCaseStatusValue3.getListCourtRoomId()", 0, cppData.get(2).getListCourtRoomId());
		assertEquals("Error AllCaseStatusValue3.getMovedFromCourtSiteShortName()", null, cppData.get(2).getMovedFromCourtSiteShortName());
		assertEquals("Error AllCaseStatusValue3.getMovedFromCourtRoomName()", null, cppData.get(2).getMovedFromCourtRoomName());
		
		// Fourth record
		assertEquals("Error AllCaseStatusValue4.getCourtRoomName()", "", cppData.get(3).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue4.getCourtSiteCode()", "A", cppData.get(3).getCourtSiteCode());
		assertEquals("Error AllCaseStatusValue4.getNotBeforeTimeAsString()", "14:00", cppData.get(3).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue4.getDefendantName().getName()", "MARGE SIMPSON", cppData.get(3).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue4.getCaseNumber()", "PDCase456", cppData.get(3).getCaseNumber());
		assertEquals("Error AllCaseStatusValue4.getHearingDescription()", "For Mention", cppData.get(3).getHearingDescription());
		assertEquals("Error AllCaseStatusValue4.getHearingProgress()", 0, cppData.get(3).getHearingProgress()); // If no Hearing Progress set, should default to 0
		assertEquals("Error AllCaseStatusValue4.getEventTimeAsString()", "09:30", cppData.get(3).getEventTimeAsString());
		assertEquals("Error AllCaseStatusValue4.isFloating()", true, cppData.get(3).isFloating());
		assertEquals("Error AllCaseStatusValue4.getListCourtRoomId()", 0, cppData.get(3).getListCourtRoomId());
		assertEquals("Error AllCaseStatusValue4.getMovedFromCourtSiteShortName()", null, cppData.get(3).getMovedFromCourtSiteShortName());
		assertEquals("Error AllCaseStatusValue4.getMovedFromCourtRoomName()", null, cppData.get(3).getMovedFromCourtRoomName());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that for a valid multiple courtroom CPP XML CLOB including multiple case sittings, the data is assigned to the AllCaseStatusValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
	public void testMultipleCourtroomXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases>" +
				"<caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>CHARLES</firstname><middlename>MONTGOMERY</middlename><lastname>BURNS</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MOE</firstname><middlename></middlename><lastname>SZYSLAK</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><hearingtype>For Execution of Bench Warrant</hearingtype>" +
				"<hearingprogress>1</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>WAYLON</firstname><lastname>SMITHERS</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>11:15</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase789</cppurn><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>2</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>HANS</firstname><lastname>MOLEMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>12:45</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails>"
				+ "</cases><timestatusset>09:30</timestatusset><courtroomname>Court 13</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<AllCaseStatusValue> cppData = (List<AllCaseStatusValue>) cppToPublicDisplay.getCppData();
		
		// Check the collection of AllCaseStatusValue CPP data
		assertEquals("Error in cppData size", 4, cppData.size());
		// First record
		assertEquals("Error AllCaseStatusValue1.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue1.getNotBeforeTimeAsString()", "10:00", cppData.get(0).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue1.getDefendantName().getName()", "CHARLES MONTGOMERY BURNS", cppData.get(0).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue1.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());
		assertEquals("Error AllCaseStatusValue1.getHearingDescription()", "Plea and Trial Preparation", cppData.get(0).getHearingDescription());
		assertEquals("Error AllCaseStatusValue1.getHearingProgress()", 0, cppData.get(0).getHearingProgress());
		assertEquals("Error AllCaseStatusValue1.getEventTimeAsString()", "10:00", cppData.get(0).getEventTimeAsString());
		
		// Second record
		assertEquals("Error AllCaseStatusValue2.getCourtRoomName()", "Court Room 11", cppData.get(1).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue2.getNotBeforeTimeAsString()", "10:00", cppData.get(1).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue2.getDefendantName().getName()", "MOE SZYSLAK", cppData.get(1).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue2.getCaseNumber()", "PDCase123", cppData.get(1).getCaseNumber());
		assertEquals("Error AllCaseStatusValue2.getHearingDescription()", "Plea and Trial Preparation", cppData.get(1).getHearingDescription());
		assertEquals("Error AllCaseStatusValue2.getHearingProgress()", 0, cppData.get(1).getHearingProgress());
		assertEquals("Error AllCaseStatusValue2.getEventTimeAsString()", "10:00", cppData.get(1).getEventTimeAsString());
		
		// Third record
		assertEquals("Error AllCaseStatusValue3.getCourtRoomName()", "Court Room 13", cppData.get(2).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue3.getNotBeforeTimeAsString()", "11:00", cppData.get(2).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue3.getDefendantName().getName()", "WAYLON SMITHERS", cppData.get(2).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue3.getCaseNumber()", "PDCase456", cppData.get(2).getCaseNumber());
		assertEquals("Error AllCaseStatusValue3.getHearingDescription()", "For Execution of Bench Warrant", cppData.get(2).getHearingDescription());
		assertEquals("Error AllCaseStatusValue3.getHearingProgress()", 1, cppData.get(2).getHearingProgress());
		assertEquals("Error AllCaseStatusValue3.getEventTimeAsString()", "11:15", cppData.get(2).getEventTimeAsString());
		
		// Fourth record
		assertEquals("Error AllCaseStatusValue4.getCourtRoomName()", "Court Room 13", cppData.get(3).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue4.getNotBeforeTimeAsString()", "14:00", cppData.get(3).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue4.getDefendantName().getName()", "HANS MOLEMAN", cppData.get(3).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue4.getCaseNumber()", "PDCase789", cppData.get(3).getCaseNumber());
		assertEquals("Error AllCaseStatusValue4.getHearingDescription()", "For Mention", cppData.get(3).getHearingDescription());
		assertEquals("Error AllCaseStatusValue4.getHearingProgress()", 2, cppData.get(3).getHearingProgress());
		assertEquals("Error AllCaseStatusValue4.getEventTimeAsString()", "12:45", cppData.get(3).getEventTimeAsString());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data is combined with Xhibit data and then the collection is sorted and duplicates 
	 * removed, the resulting collection of AllCaseStatusValue values is the correct size and has been sorted
	 * as expected.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
	public void testSortData() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases>" +
				"<caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>CHARLES</firstname><middlename>MONTGOMERY</middlename><lastname>BURNS</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>MOE</firstname><middlename></middlename><lastname>SZYSLAK</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices><publicnotice>No Trousers on a Thursday</publicnotice><publicnotice>Rubber Chickens are Strictly Prohibited</publicnotice></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom>" +
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><hearingtype>For Execution of Bench Warrant</hearingtype>" +
				"<hearingprogress>1</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>WAYLON</firstname><lastname>SMITHERS</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>11:15</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase789</cppurn><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>2</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>HANS</firstname><lastname>MOLEMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>12:45</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails>" +
				"</cases><timestatusset>09:30</timestatusset><courtroomname>Court 13</courtroomname></courtroom></courtrooms>" +
				"<floating><cases><caseDetails><cppurn>PDCase999</cppurn><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:25</timestatusset><defendants>" +
				"<defendant><firstname>KENT</firstname><lastname>BROCKMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/>" +
				"<currentstatus></currentstatus>" +
				"</caseDetails></cases></floating>" +
				"</courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		// Retrieve a mock set of Xhibit Data (5 defendants in court room 12 plus 1 more on a floating case)
		@SuppressWarnings("unchecked")
		List<AllCaseStatusValue> xhibitData = (List<AllCaseStatusValue>)getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<AllCaseStatusValue> cppData = (List<AllCaseStatusValue>) cppToPublicDisplay.getCppData();
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 11 objects in the list (6 from Xhibit and 5 from CPP)
		assertEquals("Error in cppData size", 11, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<AllCaseStatusValue> processedData = (List<AllCaseStatusValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.ALLCASETSTATUS_TYPE, xhibitData);
		
		// Check basic information after post processing, should still be 9 objects in the list
		assertEquals("Error in cppData size", 11, processedData.size());
		
		// First record
		int index = 0;
		assertEquals("Error AllCaseStatusValue1.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue1.getNotBeforeTimeAsString()", "", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue1.getDefendantName().getName()", "CHARLES MONTGOMERY BURNS", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue1.getCaseNumber()", "PDCase123", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue1.getHearingDescription()", "Plea and Trial Preparation", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue1.getHearingProgress()", 0, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue1.getEventTimeAsString()", "10:00", processedData.get(index).getEventTimeAsString());
		// Second record
		index = 1;
		assertEquals("Error AllCaseStatusValue2.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue2.getNotBeforeTimeAsString()", "", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue2.getDefendantName().getName()", "MOE SZYSLAK", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue2.getCaseNumber()", "PDCase123", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue2.getHearingDescription()", "Plea and Trial Preparation", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue2.getHearingProgress()", 0, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue2.getEventTimeAsString()", "10:00", processedData.get(index).getEventTimeAsString());
		// Third record
		index = 2;
		assertEquals("Error AllCaseStatusValue3.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue3.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue3.getDefendantName().getName()", "NED FLANDERS", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue3.getCaseNumber()", "T20190013", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue3.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue3.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue3.getEventTimeAsString()", "09:35", processedData.get(index).getEventTimeAsString());
		// Fourth record
		index = 3;
		assertEquals("Error AllCaseStatusValue4.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue4.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue4.getDefendantName().getName()", "APU NAHASAPEEMAPETILION", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue4.getCaseNumber()", "T20190013", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue4.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue4.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue4.getEventTimeAsString()", "09:35", processedData.get(index).getEventTimeAsString());
		// Fifth record
		index = 4;
		assertEquals("Error AllCaseStatusValue5.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue5.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue5.getDefendantName().getName()", "HOMER SIMPSON", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue5.getCaseNumber()", "T20190013", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue5.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue5.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue5.getEventTimeAsString()", "09:35", processedData.get(index).getEventTimeAsString());
		// Sixth record
		index = 5;
		assertEquals("Error AllCaseStatusValue6.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue6.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue6.getDefendantName().getName()", "BARNEY GUMBLE", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue6.getCaseNumber()", "S20190040", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue6.getHearingDescription()", "Trial (Part Heard)", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue6.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue6.getEventTimeAsString()", "09:39", processedData.get(index).getEventTimeAsString());
		// Seventh record
		index = 6;
		assertEquals("Error AllCaseStatusValue7.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue7.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue7.getDefendantName().getName()", "GIL GUNDERSON", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue7.getCaseNumber()", "S20190040", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue7.getHearingDescription()", "Trial (Part Heard)", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue7.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue7.getEventTimeAsString()", "09:39", processedData.get(index).getEventTimeAsString());
		// Eighth record
		index = 7;
		assertEquals("Error AllCaseStatusValue8.getCourtRoomName()", "Court Room 13", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue8.getNotBeforeTimeAsString()", "11:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue8.getDefendantName().getName()", "WAYLON SMITHERS", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue8.getCaseNumber()", "PDCase456", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue8.getHearingDescription()", "For Execution of Bench Warrant", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue8.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue8.getEventTimeAsString()", "11:15", processedData.get(index).getEventTimeAsString());
		// Ninth record
		index = 8;
		assertEquals("Error AllCaseStatusValue9.getCourtRoomName()", "Court Room 13", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue9.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue9.getDefendantName().getName()", "HANS MOLEMAN", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue9.getCaseNumber()", "PDCase789", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue9.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue9.getHearingProgress()", 2, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue9.getEventTimeAsString()", "12:45", processedData.get(index).getEventTimeAsString());
		// Ninth record
		index = 9;
		assertEquals("Error AllCaseStatusValue10.getCourtRoomName()", "", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue10.getNotBeforeTimeAsString()", "11:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue10.getDefendantName().getName()", "KENT BROCKMAN", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue10.getCaseNumber()", "PDCase999", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue10.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue10.getHearingProgress()", 0, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue10.getEventTimeAsString()", "09:25", processedData.get(index).getEventTimeAsString());
		// Ninth record
		index = 10;
		assertEquals("Error AllCaseStatusValue11.getCourtRoomName()", "", processedData.get(index).getCourtRoomName());
		assertEquals("Error AllCaseStatusValue11.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error AllCaseStatusValue11.getDefendantName().getName()", "CARL CARLSON", processedData.get(index).getDefendantName().getName());
		assertEquals("Error AllCaseStatusValue11.getCaseNumber()", "A20190055", processedData.get(index).getCaseNumber());
		assertEquals("Error AllCaseStatusValue11.getHearingDescription()", "For Mention", processedData.get(index).getHearingDescription());
		assertEquals("Error AllCaseStatusValue11.getHearingProgress()", 1, processedData.get(index).getHearingProgress());
		assertEquals("Error AllCaseStatusValue11.getEventTimeAsString()", "09:37", processedData.get(index).getEventTimeAsString());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Protected method that returns a mock list of Xhibit AllCaseStatusValue objects
	 * @return
	 */
	@Override
	protected List<?> getXhibitData() {
		List<AllCaseStatusValue> data = new ArrayList<AllCaseStatusValue>();
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String today = df.format(new Date());
		
		AllCaseStatusValue s1 = new AllCaseStatusValue();
		s1.setCourtRoomName("Court Room 11");
		s1.setCourtSiteCode("A");
		s1.setCourtSiteName("SNARESBROOK");
		s1.setCrestCourtRoomNo(11);
		s1.setNotBeforeTime(Timestamp.valueOf(today + " 10:00:00.0"));
		s1.setDefendantName( new DefendantName(
				"HOMER",
				"", 
				"SIMPSON", 
				false)	// Hide from Public Display
		);
		s1.setReportingRestricted(false);
		s1.setFloating("0");
		s1.setCaseNumber("T20190013");
		s1.setEventTime(Timestamp.valueOf(today + " 09:35:00.0"));
		s1.setHearingDescription("For Mention");
		s1.setHearingProgress(1);
		data.add(s1);
		
		AllCaseStatusValue s2 = new AllCaseStatusValue();
		s2.setCourtRoomName("Court Room 11");
		s2.setCourtSiteCode("A");
		s2.setCourtSiteName("SNARESBROOK");
		s2.setCrestCourtRoomNo(11);
		s2.setNotBeforeTime(Timestamp.valueOf(today + " 10:00:00.0"));
		s2.setDefendantName( new DefendantName(
				"APU",
				"", 
				"NAHASAPEEMAPETILION", 
				false)	// Hide from Public Display
		);
		s2.setReportingRestricted(false);
		s2.setFloating("0");
		s2.setCaseNumber("T20190013");
		s2.setEventTime(Timestamp.valueOf(today + " 09:35:00.0"));
		s2.setHearingDescription("For Mention");
		s2.setHearingProgress(1);
		data.add(s2);
		
		AllCaseStatusValue s3 = new AllCaseStatusValue();
		s3.setCourtRoomName("Court Room 11");
		s3.setCourtSiteCode("A");
		s3.setCourtSiteName("SNARESBROOK");
		s3.setCrestCourtRoomNo(11);
		s3.setNotBeforeTime(Timestamp.valueOf(today + " 10:00:00.0"));
		s3.setDefendantName( new DefendantName(
				"NED",
				"", 
				"FLANDERS", 
				false)	// Hide from Public Display
		);
		s3.setReportingRestricted(false);
		s3.setFloating("0");
		s3.setCaseNumber("T20190013");
		s3.setEventTime(Timestamp.valueOf(today + " 09:35:00.0"));
		s3.setHearingDescription("For Mention");
		s3.setHearingProgress(1);
		data.add(s3);
		
		AllCaseStatusValue s4 = new AllCaseStatusValue();
		s4.setCourtRoomName("Court Room 12");
		s4.setCourtSiteCode("A");
		s4.setCourtSiteName("SNARESBROOK");
		s4.setCrestCourtRoomNo(12);
		s4.setNotBeforeTime(Timestamp.valueOf(today + " 14:00:00.0"));
		s4.setDefendantName( new DefendantName(
				"BARNEY",
				"", 
				"GUMBLE", 
				false)	// Hide from Public Display
		);
		s4.setReportingRestricted(false);
		s4.setFloating("0");
		s4.setCaseNumber("S20190040");
		s4.setEventTime(Timestamp.valueOf(today + " 09:39:00.0"));
		s4.setHearingDescription("Trial (Part Heard)");
		s4.setHearingProgress(1);
		data.add(s4);
		
		AllCaseStatusValue s5 = new AllCaseStatusValue();
		s5.setCourtRoomName("Court Room 12");
		s5.setCourtSiteCode("A");
		s5.setCourtSiteName("SNARESBROOK");
		s5.setCrestCourtRoomNo(12);
		s5.setNotBeforeTime(Timestamp.valueOf(today + " 14:00:00.0"));
		s5.setDefendantName( new DefendantName(
				"GIL",
				"", 
				"GUNDERSON", 
				false)	// Hide from Public Display
		);
		s5.setReportingRestricted(false);
		s5.setFloating("0");
		s5.setCaseNumber("S20190040");
		s5.setEventTime(Timestamp.valueOf(today + " 09:39:00.0"));
		s5.setHearingDescription("Trial (Part Heard)");
		s5.setHearingProgress(1);
		data.add(s5);
		
		AllCaseStatusValue s6 = new AllCaseStatusValue();
		s6.setCourtRoomName("");
		s6.setCourtSiteCode("A");
		s6.setCourtSiteName("SNARESBROOK");
		s6.setCrestCourtRoomNo(99);
		s6.setNotBeforeTime(Timestamp.valueOf(today + " 14:00:00.0"));
		s6.setDefendantName( new DefendantName(
				"CARL",
				"", 
				"CARLSON", 
				false)	// Hide from Public Display
		);
		s6.setReportingRestricted(false);
		s6.setFloating("1");
		s6.setCaseNumber("A20190055");
		s6.setEventTime(Timestamp.valueOf(today + " 09:37:00.0"));
		s6.setHearingDescription("For Mention");
		s6.setHearingProgress(1);
		data.add(s6);
		
		return data;
	}
	
}