//package uk.gov.courtservice.xhibit.test.business.entities.shlegrep;
//
////jdk
//import java.util.ArrayList;
//import java.util.Collection;
//
//import javax.naming.Context;
//import javax.rmi.PortableRemoteObject;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRep;
//import uk.gov.courtservice.xhibit.business.entities.shlegrep.ShLegRepHome;
//
///**
// * <p>Title: StaffTestt</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Faisal Shoukat
// * @version 1.0
// */
//
//public class ShLegRepTest extends TestCase
//{
//
//    private Logger log =  CSServices.getLogger(ShLegRepTest.class);
//    Collection ccInfo = new ArrayList();
//
//    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
//
//    public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
//    +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
//
//    public static String sittingSQL = "INSERT INTO XHB_SITTING (SITTING_ID, SITTING_TIME, IS_FLOATING, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'y', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//
//    public static String refCourtSQL = "INSERT INTO XHB_REF_COURT (REF_COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, COURT_ID) VALUES ( 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//    public static String caseSQL = "INSERT INTO XHB_CASE (CASE_ID, REF_COURT_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//    public static String refHearingSQL = "INSERT INTO XHB_REF_HEARING_TYPE (REF_HEARING_TYPE_ID, HEARING_TYPE_CODE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES (1, 'co', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//    public static String hearingSQL = "INSERT INTO XHB_HEARING (HEARING_ID, CASE_ID, REF_HEARING_TYPE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, MP_HEARING_TYPE) VALUES (1, 1, 1,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
//    public  static String linkSHSQL= "INSERT INTO XHB_LINKED_SH ( LINKED_SH_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//
//    public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (1, 1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
//
//    public static String schedAttendeeSQL = "INSERT INTO XHB_SCHED_HEARING_ATTENDEE (SH_ATTENDEE_ID, ATTENDEE_TYPE, SCHEDULED_HEARING_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES (1, 'j', 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//    public  static String ccSQL= "INSERT INTO XHB_CC_INFO ( CC_INFO_ID, CC_INFO_TEXT, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 'ccInfo txt', 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
//    public static String schedHearDefSQL = "INSERT INTO XHB_SCHED_HEARING_DEFENDANT (SCHED_HEAR_DEF_ID, SCHEDULED_HEARING_ID, DEFENDANT_ON_CASE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String shLegRepSQl = "INSERT INTO XHB_SH_LEG_REP (SH_LEG_REP_ID, SCHED_HEAR_DEF_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE, CC_INFO_ID, REF_SOLICITOR_FIRM_ID) VALUES (1, 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 1, 1)";
//    public static String defOnCaseSQL = "INSERT INTO XHB_DEFENDANT_ON_CASE (DEFENDANT_ON_CASE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//    public static String refSolFirmSQL = "INSERT INTO XHB_REF_SOLICITOR_FIRM(REF_SOLICITOR_FIRM_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
//
//    public ShLegRepTest(String s)
//    {
//	super(s);
//    }
//
//    protected void setUp() throws Exception
//    {
//
//	log.debug("setUp()");
//	log.debug("deleting data");
//
//	TestUtils.execSql("Delete from XHB_SH_LEG_REP");
//	TestUtils.execSql("Delete from XHB_REF_SOLICITOR_FIRM");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_DEFENDANT");
//	TestUtils.execSql("Delete from XHB_DEFENDANT_ON_CASE");
//	TestUtils.execSql("Delete from XHB_CC_INFO");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//
//	log.debug("Insert attendee record");
//	TestUtils.execSql(addrSQL);
//	TestUtils.execSql(courtSQL);
//	TestUtils.execSql(refCourtSQL);
//	TestUtils.execSql(caseSQL);
//	TestUtils.execSql(sittingSQL);
//	TestUtils.execSql(linkSHSQL);
//	TestUtils.execSql(refHearingSQL);
//	TestUtils.execSql(hearingSQL);
//	TestUtils.execSql(schedHearingSQL);
//	TestUtils.execSql(schedAttendeeSQL);
//	TestUtils.execSql(ccSQL);
//	TestUtils.execSql(defOnCaseSQL);
//	TestUtils.execSql(schedHearDefSQL);
//	TestUtils.execSql(refSolFirmSQL);
//	TestUtils.execSql(shLegRepSQl);
//
//
//
//
//    }
//
//    protected void tearDown()throws Exception
//    {
//	log.debug("tearDown()");
//	log.debug("deleting data");
//	TestUtils.execSql("Delete from XHB_SH_LEG_REP");
//	TestUtils.execSql("Delete from XHB_REF_SOLICITOR_FIRM");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_DEFENDANT");
//	TestUtils.execSql("Delete from XHB_DEFENDANT_ON_CASE");
//	TestUtils.execSql("Delete from XHB_CC_INFO");
//	TestUtils.execSql("Delete from XHB_SCHED_HEARING_ATTENDEE");
//	TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
//	TestUtils.execSql("Delete from XHB_LINKED_SH");
//	TestUtils.execSql("Delete from XHB_HEARING");
//	TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
//	TestUtils.execSql("Delete from XHB_SITTING");
//	TestUtils.execSql("Delete from XHB_CASE");
//	TestUtils.execSql("Delete from XHB_REF_COURT");
//	TestUtils.execSql("Delete from XHB_COURT");
//	TestUtils.execSql("Delete from XHB_ADDRESS");
//
//
//    }
//
//
//    public void testFindByPrimaryKey()
//    {
//	try
//	{
//	    ShLegRepHome home = lookupHome();
//	    log.debug("testFindByPrimaryKey() - Got legrep");
//	    ShLegRep local = home.findByPrimaryKey(new Integer(1));
//	    log.debug("ccinfoID : " + local.getCcInfoId());
//
//	    assertEquals(1, local.getSchedHearDefId().intValue());
//
//	    //this.assertEquals(1, local.getShAttendeeId().intValue());
//
//
////For this test to work leg rep field in sttendee table must be popu;lated
///*	    log.debug("testing CMR");
//	    SchedHearingAttendee attendee = local.getScheduledHearingAttendee();
//	    log.debug("got the attendee bean");
//	    log.debug("cmr field = " + attendee.getAttendeeType());
//	    this.assertEquals(1, attendee.getScheduledHearingId().intValue());*/
//	}
//	catch(Exception e)
//	{
//	    log.debug("findByPrimaryKey() is failed");
//	    e.printStackTrace();
//	    fail();
//	}
//    }
//
//
//    private ShLegRepHome lookupHome() throws Exception
//    {
//	Context ctx = CSServices.getServiceLocator().getInitialContext();
//	Object home = (ShLegRepHome) ctx.lookup("ShLegRepHome");
//	return (ShLegRepHome) PortableRemoteObject.narrow(home, ShLegRepHome.class);
//    }
//}