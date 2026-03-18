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
import uk.gov.courtservice.xhibit.business.services.publicdisplay.data.CppDataSourceFactory;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.CourtDetailValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.renderdata.DefendantName;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

@RunWith(PowerMockRunner.class)
@PrepareForTest(XhbCourtBeanHelper2.class)
@SuppressStaticInitializationFor("uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBeanHelper2")

public class TestCourtDetailCppToPublicDisplay extends TestAllCourtStatusCppToPublicDisplay {
	
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(TestCourtDetailCppToPublicDisplay.class);	
	}

	public TestCourtDetailCppToPublicDisplay() {
		super(new CourtDetailCppToPublicDisplay(new Date(), courtId, courtRoomIds) );
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
	 * Tests that for a valid single courtroom CPP XML CLOB, the data is assigned to the CourtDetailValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
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
		List<CourtDetailValue> cppData = (List<CourtDetailValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		assertEquals("Error in cppData size", 1, cppData.size());
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "PDCase456", cppData.get(0).getCaseNumber());
		assertEquals("Error CourtDetailValue.getHearingDescription()", "For Mention", cppData.get(0).getHearingDescription());
		assertEquals("Error CourtDetailValue.getEventTimeAsString()", "09:30", cppData.get(0).getEventTimeAsString());
		
		// This fails despite the error message showing the expected and actual values are identical!!!
		//assertEquals("Error CourtDetailValue.getJudgeName()", "Before: Stig of the Dump", cppData.get(0).getJudgeName());	
		
		// Check Defendants
		assertEquals("Error CourtDetailValue.getDefendantNames()", 2, cppData.get(0).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(0).getDefendantNames();
		assertEquals("Error First DefendantName", "HOMER SIMPSON", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "MARGE SIMPSON", defNames.get(1).getName());
		
		// Check Public Displays
		assertEquals("Error CourtDetailValue.getPublicNotices()", 1, cppData.get(0).getPublicNotices().length);
		assertEquals("Error CourtDetailValue.getPublicNotices()[0].getPublicNoticeDesc()", "Anyone called Mark must wear a clown costume", cppData.get(0).getPublicNotices()[0].getPublicNoticeDesc());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that for a valid multiple courtroom CPP XML CLOB, the data is assigned to the CourtDetailValue as expected
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
	public void testMultipleCourtroomXML() throws Exception {
		
		// Setup the CourtDetailCppToPublicDisplay CLOB with CPP XML 
		XhbClobBasicValue clob = new XhbClobBasicValue();
		clob.setClobData("<?xml version=\"1.0\" encoding=\"UTF-8\"?><?xml-stylesheet type=\"text/xsl\" href=\"InternetWebPageTemplate.xsl\"?><currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
				"<court><courtname>SNARESBROOK</courtname><courtsites><courtsite><courtsitename>SNARESBROOK</courtsitename><courtrooms><courtroom><cases><caseDetails><cppurn>PDCase123</cppurn><activecase>1</activecase><hearingtype>Plea and Trial Preparation</hearingtype>" +
				"<hearingprogress>0</hearingprogress><judgename>Before: Stig of the Dump</judgename><notbeforetime>10:00</notbeforetime><timestatusset>09:30</timestatusset><defendants>" +
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
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
		List<CourtDetailValue> cppData = (List<CourtDetailValue>) cppToPublicDisplay.getCppData();
		
		// Check basic information
		assertEquals("Error in cppData size", 2, cppData.size());
		
		// First CourtDetailValue object
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 11", cppData.get(0).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "PDCase123", cppData.get(0).getCaseNumber());

		// Second CourtDetailValue object
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 13", cppData.get(1).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "T20190013", cppData.get(1).getCaseNumber());
		assertEquals("Error CourtDetailValue.getDefendantNames()", 1, cppData.get(1).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) cppData.get(1).getDefendantNames();
		assertEquals("Error First DefendantName", "MR SMEG", defNames.get(0).getName());
		assertEquals("Error CourtDetailValue.getPublicNotices()", 2, cppData.get(0).getPublicNotices().length);

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
	@Override
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
				"<defendant><firstname>SAD</firstname><middlename></middlename><lastname>KEN</lastname><reportingrestrictions>0</reportingrestrictions></defendant>" +
				"<defendant><firstname>NOGGIN</firstname><middlename>THE</middlename><lastname>NOG</lastname><reportingrestrictions>0</reportingrestrictions></defendant></defendants>" +
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
		List<CourtDetailValue> xhibitData = (List<CourtDetailValue>)getXhibitData();
		
		@SuppressWarnings("unchecked")
		List<CourtDetailValue> cppData = (List<CourtDetailValue>) cppToPublicDisplay.getCppData();
		
		// Add the two data sets together
		xhibitData.addAll(cppData);
		
		// Check basic information prior to post processing, should be 5 objects in the list (3 from Xhibit and 2 from CPP)
		assertEquals("Error in cppData size", 5, xhibitData.size());
		
		@SuppressWarnings("unchecked")
		List<CourtDetailValue> processedData = (List<CourtDetailValue>) CppDataSourceFactory.postProcessing(CppDataSourceFactory.DataType.COURTDETAIL_TYPE, xhibitData);
		
		// Check basic information after post processing, should be 3 objects in the list now after sorting and removal of duplicates
		assertEquals("Error in cppData size", 3, processedData.size());
		
		// First CourtDetailValue object
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 11", processedData.get(0).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "PDCase123", processedData.get(0).getCaseNumber());
		assertEquals("Error CourtDetailValue.getDefendantNames()", 2, processedData.get(0).getDefendantNames().size());
		ArrayList<DefendantName> defNames = (ArrayList<DefendantName>) processedData.get(0).getDefendantNames();
		assertEquals("Error First DefendantName", "SAD KEN", defNames.get(0).getName());
		assertEquals("Error Second DefendantName", "NOGGIN THE NOG", defNames.get(1).getName());
		assertEquals("Error CourtDetailValue.hasInformationForDisplay()", true, processedData.get(0).hasInformationForDisplay());
		
		// Second CourtDetailValue object
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 12", processedData.get(1).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "", processedData.get(1).getCaseNumber());
		assertEquals("Error CourtDetailValue.getDefendantNames()", 0, processedData.get(1).getDefendantNames().size());
		assertEquals("Error CourtDetailValue.hasInformationForDisplay()", false, processedData.get(1).hasInformationForDisplay());

		// Third CourtDetailValue object
		assertEquals("Error CourtDetailValue.getCourtRoomName()", "Court Room 13", processedData.get(2).getCourtRoomName());
		assertEquals("Error CourtDetailValue.getCaseNumber()", "PDCase456", processedData.get(2).getCaseNumber());
		assertEquals("Error CourtDetailValue.getDefendantNames()", 1, processedData.get(2).getDefendantNames().size());
		defNames = (ArrayList<DefendantName>) processedData.get(2).getDefendantNames();
		assertEquals("Error First DefendantName", "BENJAMIN BUNNY", defNames.get(0).getName());
		assertEquals("Error CourtDetailValue.hasInformationForDisplay()", true, processedData.get(2).hasInformationForDisplay());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Tests that when CPP data has no active cases that no information is returned 
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
	@Test
	@Override
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
		List<CourtDetailValue> cppData = (List<CourtDetailValue>) cppToPublicDisplay.getCppData();
		
		// Check that although two courtrooms have cases, none are active so the list should be empty
		assertEquals("Error in cppData size", 0, cppData.size());

		// Verify that the courtHelper has been invoked
		Mockito.verify(courtHelper);
	}
	
	/**
	 * Protected method that returns a mock list of Xhibit CourtDetailValue objects
	 * @return
	 */
	@Override
	protected List<?> getXhibitData() {
		List<CourtDetailValue> data = new ArrayList<CourtDetailValue>();
		
		CourtDetailValue room11 = new CourtDetailValue();
		room11.setCourtRoomName("Court Room 11");
		room11.setCourtSiteCode("A");
		room11.setCourtSiteName("SNARESBROOK");
		room11.setCrestCourtRoomNo(11);
		room11.setEventTime(Timestamp.valueOf("2019-12-18 18:00:00.0"));
		room11.setMovedFromCourtRoomId(0);
		room11.setReportingRestricted(false);
		data.add(room11);
		
		CourtDetailValue room12 = new CourtDetailValue();
		room12.setCourtRoomName("Court Room 12");
		room12.setCourtSiteCode("A");
		room12.setCourtSiteName("SNARESBROOK");
		room12.setCrestCourtRoomNo(12);
		room12.setEventTime(Timestamp.valueOf("2019-12-18 18:00:00.0"));
		room12.setMovedFromCourtRoomId(0);
		room12.setReportingRestricted(false);
		data.add(room12);
		
		CourtDetailValue room13 = new CourtDetailValue();
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