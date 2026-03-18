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
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.SummaryByNameValue;
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

public class TestSummaryByNameCppToPublicDisplay extends TestAbstractCppToPublicDisplay {
	
	protected static int[] courtRoomIds = {8122,8123,8124};
	protected static int courtId = 81;
	
	@Mock
	protected XhbCourtBeanHelper2 courtHelper;
	@Mock
	protected CppFormattingHelper cppFormattingHelper;
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(TestSummaryByNameCppToPublicDisplay.class);	
	}

	public TestSummaryByNameCppToPublicDisplay() {
		super(new SummaryByNameCppToPublicDisplay(new Date(), courtId, courtRoomIds) );
	}

	public TestSummaryByNameCppToPublicDisplay(AllCaseStatusCppToPublicDisplay allCaseStatusCppToPublicDisplay) {
		super(allCaseStatusCppToPublicDisplay);
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
	 * Tests that for a valid single courtroom with one case and two defendants, the data is assigned to the SummaryByNameValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSingleCourtroomXML() throws Exception {
		
		// Setup the SummaryByNameCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>1</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>The Judge has soiled himself</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name>SAD KEN</defendant_name></event></currentstatus>" +
				"<movedfromcourtsitename>Court Site B</movedfromcourtsitename><movedfromcourtroomname>Court Room 22</movedfromcourtroomname>" +
				"</caseDetails></cases><timestatusset>09:30</timestatusset><courtroomname>Court 11</courtroomname></courtroom></courtrooms>" +
				"<floating><cases><caseDetails><cppurn>PDCase456</cppurn><activecase>1</activecase><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>HOMER</firstname><lastname>SIMPSON</lastname></defendant>" +
				"<defendant><firstname>MARGE</firstname><lastname>SIMPSON</lastname><reportingrestrictions>A</reportingrestrictions></defendant></defendants><publicnotices/>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases></floating></courtsite></courtsites></court>" +
				"<datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<SummaryByNameValue> cppData = (List<SummaryByNameValue>) cppToPublicDisplay.getCppData();
		
		// Check the collection of SummaryByNameValue CPP data
		assertEquals("Error in cppData size", 4, cppData.size());
		// First record
		assertEquals("Error SummaryByNameValue1.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error SummaryByNameValue1.getCourtSiteCode()", "A", cppData.get(0).getCourtSiteCode());
		assertEquals("Error SummaryByNameValue1.getNotBeforeTimeAsString()", "10:00", cppData.get(0).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue1.getDefendantName().getName()", "SAD KEN", cppData.get(0).getDefendantName().getName());
		assertEquals("Error SummaryByNameValue1.isFloating()", false, cppData.get(0).isFloating());
		assertEquals("Error SummaryByNameValue1.getMovedFromCourtSiteShortName()", "Court Site B", cppData.get(0).getMovedFromCourtSiteShortName());
		assertEquals("Error SummaryByNameValue1.getMovedFromCourtRoomName()", "Court Room 22", cppData.get(0).getMovedFromCourtRoomName());
		assertEquals("Error SummaryByNameValue1.isReportingRestricted()", true, cppData.get(0).isReportingRestricted());
		// Second record
		assertEquals("Error SummaryByNameValue2.getCourtRoomName()", "Court Room 11", cppData.get(1).getCourtRoomName());
		assertEquals("Error SummaryByNameValue2.getCourtSiteCode()", "A", cppData.get(1).getCourtSiteCode());
		assertEquals("Error SummaryByNameValue2.getNotBeforeTimeAsString()", "10:00", cppData.get(1).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue2.getDefendantName().getName()", "NOGGIN THE NOG", cppData.get(1).getDefendantName().getName());
		assertEquals("Error SummaryByNameValue2.isFloating()", false, cppData.get(1).isFloating());
		assertEquals("Error SummaryByNameValue2.getMovedFromCourtSiteShortName()", "Court Site B", cppData.get(1).getMovedFromCourtSiteShortName());
		assertEquals("Error SummaryByNameValue2.getMovedFromCourtRoomName()", "Court Room 22", cppData.get(1).getMovedFromCourtRoomName());
		assertEquals("Error SummaryByNameValue2.isReportingRestricted()", false, cppData.get(1).isReportingRestricted());
		// Third record
		assertEquals("Error SummaryByNameValue3.getCourtRoomName()", "", cppData.get(2).getCourtRoomName());
		assertEquals("Error SummaryByNameValue3.getCourtSiteCode()", "A", cppData.get(2).getCourtSiteCode());
		assertEquals("Error SummaryByNameValue3.getNotBeforeTimeAsString()", "14:00", cppData.get(2).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue3.getDefendantName().getName()", "HOMER SIMPSON", cppData.get(2).getDefendantName().getName());
		assertEquals("Error SummaryByNameValue3.isFloating()", true, cppData.get(2).isFloating());
		assertEquals("Error SummaryByNameValue3.getMovedFromCourtSiteShortName()", null, cppData.get(2).getMovedFromCourtSiteShortName());
		assertEquals("Error SummaryByNameValue3.getMovedFromCourtRoomName()", null, cppData.get(2).getMovedFromCourtRoomName());
		assertEquals("Error SummaryByNameValue3.isReportingRestricted()", false, cppData.get(2).isReportingRestricted());
		// Fourth record
		assertEquals("Error SummaryByNameValue4.getCourtRoomName()", "", cppData.get(3).getCourtRoomName());
		assertEquals("Error SummaryByNameValue4.getCourtSiteCode()", "A", cppData.get(3).getCourtSiteCode());
		assertEquals("Error SummaryByNameValue4.getNotBeforeTimeAsString()", "14:00", cppData.get(3).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue4.getDefendantName().getName()", "MARGE SIMPSON", cppData.get(3).getDefendantName().getName());
		assertEquals("Error SummaryByNameValue4.isFloating()", true, cppData.get(3).isFloating());
		assertEquals("Error SummaryByNameValue4.getMovedFromCourtSiteShortName()", null, cppData.get(3).getMovedFromCourtSiteShortName());
		assertEquals("Error SummaryByNameValue4.getMovedFromCourtRoomName()", null, cppData.get(3).getMovedFromCourtRoomName());
		assertEquals("Error SummaryByNameValue4.isReportingRestricted()", false, cppData.get(3).isReportingRestricted());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that for a valid multiple courtroom CPP XML CLOB including multiple case sittings, the data is assigned to the SummaryByNameValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
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
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>WAYLON</firstname><lastname>SMITHERS</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase789</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>HANS</firstname><lastname>MOLEMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails>"
				+ "</cases><timestatusset>09:30</timestatusset><courtroomname>Court 13</courtroomname></courtroom></courtrooms></courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		@SuppressWarnings("unchecked")
		List<SummaryByNameValue> cppData = (List<SummaryByNameValue>) cppToPublicDisplay.getCppData();
		
		// Check the collection of SummaryByNameValue CPP data
		assertEquals("Error in cppData size", 4, cppData.size());
		// First record
		assertEquals("Error SummaryByNameValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error SummaryByNameValue.getNotBeforeTimeAsString()", "10:00", cppData.get(0).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue.getDefendantName().getName()", "CHARLES MONTGOMERY BURNS", cppData.get(0).getDefendantName().getName());
		// Second record
		assertEquals("Error SummaryByNameValue.getCourtRoomName()", "Court Room 11", cppData.get(1).getCourtRoomName());
		assertEquals("Error SummaryByNameValue.getNotBeforeTimeAsString()", "10:00", cppData.get(1).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue.getDefendantName().getName()", "MOE SZYSLAK", cppData.get(1).getDefendantName().getName());
		// Third record
		assertEquals("Error SummaryByNameValue.getCourtRoomName()", "Court Room 13", cppData.get(2).getCourtRoomName());
		assertEquals("Error SummaryByNameValue.getNotBeforeTimeAsString()", "11:00", cppData.get(2).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue.getDefendantName().getName()", "WAYLON SMITHERS", cppData.get(2).getDefendantName().getName());
		// Fourth record
		assertEquals("Error SummaryByNameValue.getCourtRoomName()", "Court Room 13", cppData.get(3).getCourtRoomName());
		assertEquals("Error SummaryByNameValue.getNotBeforeTimeAsString()", "14:00", cppData.get(3).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue.getDefendantName().getName()", "HANS MOLEMAN", cppData.get(3).getDefendantName().getName());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data is combined with Xhibit data and then the collection is sorted and duplicates 
	 * removed, the resulting collection of SummaryByNameValue values is the correct size and has been sorted
	 * as expected.
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	public void testSortData() throws Exception {
		
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
				"<courtroom><cases><caseDetails><cppurn>PDCase456</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>11:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>WAYLON</firstname><lastname>SMITHERS</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails><caseDetails><cppurn>PDCase789</cppurn><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Dave the Badger</judgename><notbeforetime>14:00</notbeforetime><timestatusset>09:30</timestatusset>" +
				"<defendants><defendant><firstname>HANS</firstname><lastname>MOLEMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
				"<publicnotices></publicnotices>" +
				"<currentstatus><event><time>10:00</time><date>18/12/19</date><free_text>Jury being sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails>" +
				"</cases><timestatusset>09:30</timestatusset><courtroomname>Court 13</courtroomname></courtroom></courtrooms>" +
				"<floating><cases><caseDetails><cppurn>PDCase999</cppurn><hearingtype>For Mention</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>15:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>KENT</firstname><lastname>BROCKMAN</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants><publicnotices/>" +
				"<currentstatus><event><time>09:30</time><date>18/12/19</date><free_text>Witness 1 Sworn in</free_text><process_linked_cases/><defendant_on_case_id/><type>CPP</type><defendant_name/></event></currentstatus>" +
				"</caseDetails></cases></floating>" +
				"</courtsite></courtsites></court><datetimestamp><dayofweek>Wednesday</dayofweek><date>18</date><month>December</month><year>2019</year><hour>10</hour><min>15</min></datetimestamp><pagename>snaresbrook</pagename></currentcourtstatus>");
		cppToPublicDisplay.setCppClob(clob);
		
		// Mocked methods
		PowerMockito.mockStatic(XhbCourtBeanHelper2.class);
		PowerMockito.when(courtHelper.findByPrimaryKeyValue(courtId)).thenReturn(getXhbCourtObject());
		PowerMockito.when(courtHelper.getCourtStructure(courtId)).thenReturn(getCourtStructureValue());
		
		// Retrieve a mock set of Xhibit Data (5 defendants in court room 12 plus 1 more on a floating case)
		@SuppressWarnings("unchecked")
		List<SummaryByNameValue> xhibitData = (List<SummaryByNameValue>)getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<SummaryByNameValue> cppData = (List<SummaryByNameValue>) cppToPublicDisplay.getCppData();
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 11 objects in the list (6 from Xhibit and 5 from CPP)
		assertEquals("Error in cppData size", 11, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<SummaryByNameValue> processedData = (List<SummaryByNameValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.SUMMARYBYNAME_TYPE, xhibitData);
		
		// Check basic information after post processing, should still be 11 objects in the list
		assertEquals("Error in cppData size", 11, processedData.size());
		
		// First record
		int index = 0;
		assertEquals("Error SummaryByNameValue1.getCourtRoomName()", "", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue1.getNotBeforeTimeAsString()", "15:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue1.getDefendantName().getName()", "KENT BROCKMAN", processedData.get(index).getDefendantName().getName());
		// Second record
		index = 1;
		assertEquals("Error SummaryByNameValue2.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue2.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue2.getDefendantName().getName()", "CHARLES MONTGOMERY BURNS", processedData.get(index).getDefendantName().getName());
		// Third record
		index = 2;
		assertEquals("Error SummaryByNameValue3.getCourtRoomName()", "", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue3.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue3.getDefendantName().getName()", "CARL CARLSON", processedData.get(index).getDefendantName().getName());
		// Fourth record
		index = 3;
		assertEquals("Error SummaryByNameValue4.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue4.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue4.getDefendantName().getName()", "NED FLANDERS", processedData.get(index).getDefendantName().getName());
		// Fifth record
		index = 4;
		assertEquals("Error SummaryByNameValue5.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue5.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue5.getDefendantName().getName()", "BARNEY GUMBLE", processedData.get(index).getDefendantName().getName());
		// Sixth record
		index = 5;
		assertEquals("Error SummaryByNameValue6.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue6.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue6.getDefendantName().getName()", "GIL GUNDERSON", processedData.get(index).getDefendantName().getName());
		// Seventh record
		index = 6;
		assertEquals("Error SummaryByNameValue7.getCourtRoomName()", "Court Room 13", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue7.getNotBeforeTimeAsString()", "14:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue7.getDefendantName().getName()", "HANS MOLEMAN", processedData.get(index).getDefendantName().getName());
		// Eighth record
		index = 7;
		assertEquals("Error SummaryByNameValue8.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue8.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue8.getDefendantName().getName()", "APU NAHASAPEEMAPETILION", processedData.get(index).getDefendantName().getName());
		// Ninth record
		index = 8;
		assertEquals("Error SummaryByNameValue9.getCourtRoomName()", "Court Room 12", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue9.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue9.getDefendantName().getName()", "HOMER SIMPSON", processedData.get(index).getDefendantName().getName());
		// Tenth record
		index = 9;
		assertEquals("Error SummaryByNameValue10.getCourtRoomName()", "Court Room 13", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue10.getNotBeforeTimeAsString()", "11:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue10.getDefendantName().getName()", "WAYLON SMITHERS", processedData.get(index).getDefendantName().getName());
		// Eleventh record
		index = 10;
		assertEquals("Error SummaryByNameValue11.getCourtRoomName()", "Court Room 11", processedData.get(index).getCourtRoomName());
		assertEquals("Error SummaryByNameValue11.getNotBeforeTimeAsString()", "10:00", processedData.get(index).getNotBeforeTimeAsString());
		assertEquals("Error SummaryByNameValue11.getDefendantName().getName()", "MOE SZYSLAK", processedData.get(index).getDefendantName().getName());

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
			List<SummaryByNameValue> cppData = (List<SummaryByNameValue>) cppToPublicDisplay.getCppData();
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
	 * Protected method that returns a mock list of Xhibit SummaryByNameValue objects
	 * @return
	 */
	protected List<?> getXhibitData() {
		List<SummaryByNameValue> data = new ArrayList<SummaryByNameValue>();
		
		SummaryByNameValue s1 = new SummaryByNameValue();
		s1.setCourtRoomName("Court Room 12");
		s1.setCourtSiteCode("A");
		s1.setCourtSiteName("SNARESBROOK");
		s1.setCrestCourtRoomNo(12);
		s1.setNotBeforeTime(Timestamp.valueOf("2020-01-10 10:00:00.0"));
		s1.setDefendantName( new DefendantName(
				"HOMER",
				"", 
				"SIMPSON", 
				false)	// Hide from Public Display
		);
		s1.setReportingRestricted(false);
		s1.setFloating("0");
		data.add(s1);
		
		SummaryByNameValue s2 = new SummaryByNameValue();
		s2.setCourtRoomName("Court Room 12");
		s2.setCourtSiteCode("A");
		s2.setCourtSiteName("SNARESBROOK");
		s2.setCrestCourtRoomNo(12);
		s2.setNotBeforeTime(Timestamp.valueOf("2020-01-10 10:00:00.0"));
		s2.setDefendantName( new DefendantName(
				"APU",
				"", 
				"NAHASAPEEMAPETILION", 
				false)	// Hide from Public Display
		);
		s2.setReportingRestricted(false);
		s2.setFloating("0");
		data.add(s2);
		
		SummaryByNameValue s3 = new SummaryByNameValue();
		s3.setCourtRoomName("Court Room 12");
		s3.setCourtSiteCode("A");
		s3.setCourtSiteName("SNARESBROOK");
		s3.setCrestCourtRoomNo(12);
		s3.setNotBeforeTime(Timestamp.valueOf("2020-01-10 10:00:00.0"));
		s3.setDefendantName( new DefendantName(
				"NED",
				"", 
				"FLANDERS", 
				false)	// Hide from Public Display
		);
		s3.setReportingRestricted(false);
		s3.setFloating("0");
		data.add(s3);
		
		SummaryByNameValue s4 = new SummaryByNameValue();
		s4.setCourtRoomName("Court Room 12");
		s4.setCourtSiteCode("A");
		s4.setCourtSiteName("SNARESBROOK");
		s4.setCrestCourtRoomNo(12);
		s4.setNotBeforeTime(Timestamp.valueOf("2020-01-10 14:00:00.0"));
		s4.setDefendantName( new DefendantName(
				"BARNEY",
				"", 
				"GUMBLE", 
				false)	// Hide from Public Display
		);
		s4.setReportingRestricted(false);
		s4.setFloating("0");
		data.add(s4);
		
		SummaryByNameValue s5 = new SummaryByNameValue();
		s5.setCourtRoomName("Court Room 12");
		s5.setCourtSiteCode("A");
		s5.setCourtSiteName("SNARESBROOK");
		s5.setCrestCourtRoomNo(12);
		s5.setNotBeforeTime(Timestamp.valueOf("2020-01-10 14:00:00.0"));
		s5.setDefendantName( new DefendantName(
				"GIL",
				"", 
				"GUNDERSON", 
				false)	// Hide from Public Display
		);
		s5.setReportingRestricted(false);
		s5.setFloating("0");
		data.add(s5);
		
		SummaryByNameValue s6 = new SummaryByNameValue();
		s6.setCourtRoomName("");
		s6.setCourtSiteCode("A");
		s6.setCourtSiteName("SNARESBROOK");
		s6.setCrestCourtRoomNo(99);
		s6.setNotBeforeTime(Timestamp.valueOf("2020-01-10 14:00:00.0"));
		s6.setDefendantName( new DefendantName(
				"CARL",
				"", 
				"CARLSON", 
				false)	// Hide from Public Display
		);
		s6.setReportingRestricted(false);
		s6.setFloating("1");
		data.add(s6);
		
		return data;
	}
	
}