package uk.gov.courtservice.xhibit.business.xmlmerge;


/**
 * Constants used in the xml tests.
 * @author waltersn
 *
 */
public class CPPXMLConstants {
	
	//Stop other classes instantiating this class
	private CPPXMLConstants(){}

	//Numbers used for courtrooms
	public static final String ONE = "1";
	public static final String TWO = "2";
	public static final String THREE = "3";
	public static final String FOUR = "4";
	public static final String TWELVE = "12";
	public static final String EIGHTY_EIGHT = "88";
	public static final String NINETY_NINE = "99";
	public static final String COURT_TWO = "Court 2";

	//Single tags
	public static final String CURRENT_COURT_STATUS_ENTRY_TAG="<currentcourtstatus xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
	public static final String CURRENT_COURT_STATUS_END_TAG="</currentcourtstatus>";
		
	public static final String COURT_ROOMS_ENTRY_TAG = "<courtrooms>";
	public static final String COURT_ROOMS_END_TAG = "</courtrooms>";

	public static final String COURT_SITES_ENTRY_TAG = "<courtsites>";
	public static final String COURT_SITES_END_TAG="</courtsites>";
		
	public static final String COURT_SITE_ENTRY_TAG = "<courtsite>";
	public static final String COURT_SITE_END_TAG="</courtsite>";
		
	public static final String BLANK_STATUS_TAG = "<currentstatus/>";

	public static final String COURT_ROOM_NAME_ENTRY_TAG = "<courtroomname>";
	public static final String COURT_ROOM_NAME_END_TAG = "</courtroomname>";

	public static final String COURT_ROOM_ENTRY_TAG ="<courtroom>";
	public static final String COURT_ROOM_END_TAG = "</courtroom>";
	
	public static final String COURT_SITE_NAME_ENTRY_TAG ="<courtsitename>";
	public static final String COURT_SITE_NAME_END_TAG = "</courtsitename>";
	
	public static final String DEFENDANTS_ENTRY_TAG ="<defendants>";
	public static final String DEFENDANTS_END_TAG = "</defendants>";	

	public static final String DEFENDANT_ENTRY_TAG ="<defendant>";
	public static final String DEFENDANT_END_TAG = "</defendant>";	

	public static final String CASES_ENTRY_TAG ="<cases>";
	public static final String CASES_END_TAG = "</cases>";	

	
	public static final String COURT_END_TAG = "</court>";

	//default values
	public static final String DEFAULT_TIMESTATUS_SET = "<timestatusset>11:49</timestatusset>";
	public static final String DEFAULT_TIMESTATUS_SET_LATEST = "<timestatusset>13:49</timestatusset>";

	public static final String DEFAULT_ONE_DEFENDANT = DEFENDANTS_ENTRY_TAG+DEFENDANT_ENTRY_TAG+"<firstname>first2</firstname><middlename>middle2</middlename><lastname>last2</lastname></defendant></defendants>";
	public static final String DEFAULT_THREE_DEFENDANTS = DEFENDANTS_ENTRY_TAG+DEFENDANT_ENTRY_TAG+"<firstname>FIRST1</firstname><middlename>MIDDLE1</middlename><lastname>LAST1</lastname>"+DEFENDANT_END_TAG+DEFENDANT_ENTRY_TAG+"<firstname>FIRST2</firstname><middlename>MIDDLE2</middlename><lastname>LAST2</lastname>"+DEFENDANT_END_TAG+DEFENDANT_ENTRY_TAG+"<firstname>FIRST3</firstname><lastname>LAST3</lastname>"+DEFENDANT_END_TAG+DEFENDANTS_END_TAG;
	public static final String DEFAULT_T_CASE_CPP_INFO = CASES_ENTRY_TAG+"<caseDetails><cppurn>cppcase2</cppurn><casenumber>2</casenumber><casetype>T</casetype><hearingtype>For Trial (Backer)</hearingtype></caseDetails></cases>";
	public static final String DEFAULT_T_CASE = CASES_ENTRY_TAG+"<caseDetails><casenumber>20187229</casenumber><casetype>T</casetype><hearingtype>For Trial (Backer)</hearingtype></caseDetails></cases>";
	public static final String SNARESBROOK_PAGENAME = "<pagename>snaresbrook</pagename>";
	public static final String COURT_ROOM_1 = COURT_ROOM_NAME_ENTRY_TAG+"1"+COURT_ROOM_NAME_END_TAG;
	public static final String COURT_ROOM_2 = COURT_ROOM_NAME_ENTRY_TAG+"2"+COURT_ROOM_NAME_END_TAG;
	public static final String COURT_ROOM_COURT_2 = COURT_ROOM_NAME_ENTRY_TAG+COURT_TWO+COURT_ROOM_NAME_END_TAG;
	public static final String COURT_ROOM_3 = COURT_ROOM_NAME_ENTRY_TAG+"3"+COURT_ROOM_NAME_END_TAG;
	public static final String COURT_ROOM_COURT_3 = COURT_ROOM_NAME_ENTRY_TAG+"Court 3"+COURT_ROOM_NAME_END_TAG;
	public static final String COURT_ROOM_COURT_13 = COURT_ROOM_NAME_ENTRY_TAG+"Court 13"+COURT_ROOM_NAME_END_TAG;

	public static final String CROWN_COURT_ROOM_13 = COURT_ROOM_NAME_ENTRY_TAG+"Crown Court 13"+COURT_ROOM_NAME_END_TAG;
	public static final String CROWN_COURT_ROOM_3 = COURT_ROOM_NAME_ENTRY_TAG+"Crown Court 3"+COURT_ROOM_NAME_END_TAG;

	public static final String COURT_NAME_SNARESBROOK = "<court><courtname>SNARESBROOK</courtname>";
    public static final String DEFAULT_EVENT_LATE_XHIBIT = "<currentstatus><event><time>13:49</time><date>08/11/19</date><free_text/><E30200_Long_Adjourn_Options><E30200_LAO_Name>MY XHIBIT LATE</E30200_LAO_Name><E30200_LAO_PSR_Required>true</E30200_LAO_PSR_Required><E30200_LAO_Date>29-Nov-2019</E30200_LAO_Date><E30200_LAO_PSR_Deft_ID>133802</E30200_LAO_PSR_Deft_ID><E30200_LAO_Type>E30200_Case_to_be_listed_for_Sentence</E30200_LAO_Type></E30200_Long_Adjourn_Options><defendant_on_case_id>956915</defendant_on_case_id><type>30200</type></event></currentstatus>";
    public static final String DEFAULT_EVENT_EARLY_XHIBIT = "<currentstatus><event><time>11:49</time><date>08/11/19</date><free_text/><E30200_Long_Adjourn_Options><E30200_LAO_Name>MY XHIBIT EARLY</E30200_LAO_Name><E30200_LAO_PSR_Required>true</E30200_LAO_PSR_Required><E30200_LAO_Date>29-Nov-2019</E30200_LAO_Date><E30200_LAO_PSR_Deft_ID>133802</E30200_LAO_PSR_Deft_ID><E30200_LAO_Type>E30200_Case_to_be_listed_for_Sentence</E30200_LAO_Type></E30200_Long_Adjourn_Options><defendant_on_case_id>956915</defendant_on_case_id><type>30200</type></event></currentstatus>";
    public static final String DEFAULT_EVENT_LATE_CPP = "<currentstatus><event><time>13:49</time><date>08/11/19</date><free_text/><E30200_Long_Adjourn_Options><E30200_LAO_Name>MY CPP EVENT LATE</E30200_LAO_Name><E30200_LAO_PSR_Required>true</E30200_LAO_PSR_Required><E30200_LAO_Date>29-Nov-2019</E30200_LAO_Date><E30200_LAO_PSR_Deft_ID>133802</E30200_LAO_PSR_Deft_ID><E30200_LAO_Type>E30200_Case_to_be_listed_for_Sentence</E30200_LAO_Type></E30200_Long_Adjourn_Options><defendant_on_case_id>956915</defendant_on_case_id><type>30200</type></event></currentstatus>";
    public static final String DEFAULT_EVENT_EARLY_CPP = "<currentstatus><event><time>11:49</time><date>08/11/19</date><free_text/><E30200_Long_Adjourn_Options><E30200_LAO_Name>MY CPP EVENT EARLY</E30200_LAO_Name><E30200_LAO_PSR_Required>true</E30200_LAO_PSR_Required><E30200_LAO_Date>29-Nov-2019</E30200_LAO_Date><E30200_LAO_PSR_Deft_ID>133802</E30200_LAO_PSR_Deft_ID><E30200_LAO_Type>E30200_Case_to_be_listed_for_Sentence</E30200_LAO_Type></E30200_Long_Adjourn_Options><defendant_on_case_id>956915</defendant_on_case_id><type>30200</type></event></currentstatus>";
    public static final String DEFAULT_DATE_TIMESTAMP_TAG = "<datetimestamp><dayofweek>Friday</dayofweek><date>08</date><month>November</month><year>2019</year><hour>11</hour><min>59</min></datetimestamp>";

	
	//court sites
	public static final String COURT_SITE_SNARESBROOK = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"SNARESBROOK mu"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_THE_NEW_SITE = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"THENEWSITE"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_TEST1 = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"test1"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_TEST2 = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"test2"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_Z = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"z"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_EST = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"est vs"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_1 = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"CourtSite 1"+COURT_SITE_NAME_END_TAG;
	public static final String COURT_SITE_2 = COURT_SITE_ENTRY_TAG+COURT_SITE_NAME_ENTRY_TAG+"CourtSite 2"+COURT_SITE_NAME_END_TAG;

	//multitag	
	public static final String COURT_ROOMS_ROOM_ENTRY_TAG = COURT_ROOMS_ENTRY_TAG+COURT_ROOM_ENTRY_TAG;
	public static final String COURT_ROOMS_ROOM_END_TAG = COURT_ROOM_END_TAG+COURT_ROOMS_END_TAG;

	public static final String COURT_SITES_COURT_SITE_ENTRY_TAG = COURT_SITES_ENTRY_TAG+COURT_SITE_ENTRY_TAG;
	public static final String COURT_SITES_COURT_SITE_END_TAG = COURT_SITE_END_TAG+COURT_SITES_END_TAG;
	
	public static final String BLANK_COURT_ROOM_ENTRY_TAG =COURT_ROOM_ENTRY_TAG+BLANK_STATUS_TAG+COURT_ROOM_NAME_ENTRY_TAG;
	public static final String BLANK_COURT_ROOM_END_TAG =COURT_ROOM_NAME_END_TAG+COURT_ROOM_END_TAG;

	


}