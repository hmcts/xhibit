////
////package uk.gov.courtservice.xhibit.test.business.entities.scheduledhearing;
////
////import java.util.Calendar;
////import java.util.Date;
////import java.util.GregorianCalendar;
////
////import javax.ejb.ObjectNotFoundException;
////import javax.naming.InitialContext;
////import javax.transaction.UserTransaction;
////
////import junit.framework.TestCase;
////
////import org.apache.log4j.Logger;
////
////import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
////import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
////import uk.gov.courtservice.framework.services.CSServices;
////import uk.gov.courtservice.framework.test.TestUtils;
////import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearing;
////import uk.gov.courtservice.xhibit.business.entities.scheduledhearing.ScheduledHearingMaintainer;
////import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingBasicValue;
////import uk.gov.courtservice.xhibit.business.vos.entities.ScheduledHearingComplexValue;
////
/////**
//// * <p>Title: ScheduledHearingMaintainerTest</p>
//// * <p>Description: ScheduledHearingMaintainer Test Class</p>
//// * <p>Copyright: Copyright (c) 2003</p>
//// * <p>Company: Electronic Data Systems</p>
//// * @author Joseph Babad
//// * @version $Id: ScheduledHearingMaintainerTest.java,v 1.9 2006/07/11 14:16:57 xzfdtb Exp $
//// *
//// * <Change History/>
//// *
//// * <P>13/02/03 - JB  - Created</P>
//// */
////public class ScheduledHearingMaintainerTest extends TestCase {
////
////    public  static String addrSQL= "INSERT INTO XHB_ADDRESS ( ADDRESS_ID, ADDRESS_1, ADDRESS_2, ADDRESS_3, ADDRESS_4, TOWN, COUNTY, POSTCODE, COUNTRY, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES ( 1, '12 Napier Rd', 'Chorlton', NULL, NULL, 'Manchester', 'GMB', 'M21 8AW', 'UK',  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'pete', 'pete', 1)";
////
////    public  static String courtSQL= "INSERT INTO XHB_COURT ( COURT_ID, COURT_TYPE, CIRCUIT, COURT_NAME, CREST_COURT_ID, COURT_PREFIX, SHORT_NAME, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, CREST_IP_ADDRESS) VALUES ( \n"
////                                  +"1, 'court_type', 'circuit', 'name', 'cid', 'cpfix', 'short', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'fez', 'fez', 1, 1, 'addr')";
////
////    public static String sittingSQL = "INSERT INTO XHB_SITTING (SITTING_ID, SITTING_TIME, IS_FLOATING, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'), 'y', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
////
////
////    public static String refCourtSQL = "INSERT INTO XHB_REF_COURT (REF_COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, ADDRESS_ID, COURT_ID) VALUES ( 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
////    public static String caseSQL = "INSERT INTO XHB_CASE (CASE_ID, REF_COURT_ID, COURT_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION) VALUES (1, 1, 1, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1)";
////
////    public static String refHearingSQL = "INSERT INTO XHB_REF_HEARING_TYPE (REF_HEARING_TYPE_ID, HEARING_TYPE_CODE, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID) VALUES (1, 'co', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
////    public static String hearingSQL = "INSERT INTO XHB_HEARING (HEARING_ID, CASE_ID, REF_HEARING_TYPE_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, COURT_ID, MP_HEARING_TYPE) VALUES (1, 1, 1,  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1, 1)";
////    public  static String linkSHSQL= "INSERT INTO XHB_LINKED_SH ( LINKED_SH_ID, VERSION, LAST_UPDATED_BY, CREATED_BY, CREATION_DATE, LAST_UPDATE_DATE) VALUES ( 1, 1, 'pete', 'pete', TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'))";
////
////    //  public static String schedHearingSQL = "INSERT INTO XHB_SCHEDULED_HEARING (SCHEDULED_HEARING_ID, SEQUENCE_NO, SITTING_ID, HEARING_ID, LAST_UPDATE_DATE, CREATION_DATE, CREATED_BY, LAST_UPDATED_BY, VERSION, LINKED_SH_ID) VALUES (17, 1, 3, 3, TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),  TO_Date( '11/15/2002 03:38:02 PM', 'MM/DD/YYYY HH:MI:SS AM'),'fez', 'fez', 1, 1)";
////
////
////    Calendar testCal = new GregorianCalendar( 2003, Calendar.FEBRUARY, 12, 7, 00, 00);
////    Calendar notBeforeCal = new GregorianCalendar( 2003, Calendar.FEBRUARY, 12, 11, 00, 00);
////    Calendar startCal = new GregorianCalendar( 2003, Calendar.FEBRUARY, 12, 11, 30, 00);
////    Calendar endCal = new GregorianCalendar( 2003, Calendar.FEBRUARY, 12, 12, 30, 00);
////    Calendar originalCal = new GregorianCalendar( 2003, Calendar.FEBRUARY, 12, 11, 15, 00);
////
////    private Date testDate;
////    private Date notBeforeTime;
////    private Date startTime;
////    private Date endTime;
////    private Date originalTime;
////
////    private Integer testHearingId = new Integer(1);
////    private Integer testSittingId = new Integer(1);
////
////    private ScheduledHearingBasicValue setUpShbv = new ScheduledHearingBasicValue();
////    private Integer setUpPK;
////    private Integer setUpVersion;
////
////    private static Logger log = CSServices.getLogger(ScheduledHearingMaintainerTest.class);
////
////    private InitialContext initContext;
////    private UserTransaction ut = null;
////
////    public ScheduledHearingMaintainerTest(String s) {
////        super(s);
////        testDate      = testCal.getTime();
////        notBeforeTime = notBeforeCal.getTime();
////        startTime     = startCal.getTime();
////        endTime       = endCal.getTime();
////        originalTime  = originalCal.getTime();
////
//////        testDate = new Date();
//////        notBeforeTime = new Date();
////
////        //startTime = new Date
////    }
////
////    protected void setUp() {
////        try
////        {
////            TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
////            TestUtils.execSql("Delete from XHB_LINKED_SH");
////            TestUtils.execSql("Delete from XHB_HEARING");
////            TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
////            TestUtils.execSql("Delete from XHB_SITTING");
////            TestUtils.execSql("Delete from XHB_CASE");
////            TestUtils.execSql("Delete from XHB_REF_COURT");
////            TestUtils.execSql("Delete from XHB_COURT");
////            TestUtils.execSql("Delete from XHB_ADDRESS");
////
////            log.debug("Insert attendee record");
////            TestUtils.execSql(addrSQL);
////            TestUtils.execSql(courtSQL);
////            TestUtils.execSql(refCourtSQL);
////            TestUtils.execSql(caseSQL);
////            TestUtils.execSql(sittingSQL);
////            TestUtils.execSql(linkSHSQL);
////            TestUtils.execSql(refHearingSQL);
////            TestUtils.execSql(hearingSQL);
////            //   TestUtils.execSql(schedHearingSQL);
////
////            initContext = new InitialContext();
////            ut = (UserTransaction)initContext.lookup("java:comp/UserTransaction");
////
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            setUpShbv = createBasicVO();
////            ScheduledHearing scheduledHearing = (ScheduledHearing)scheduledhearingmaintainer.create(setUpShbv);
////            this.setUpPK =  scheduledHearing.getScheduledHearingId();
////            this.setUpVersion = scheduledHearing.getVersion();
////        }
////        catch (Exception e)
////        {
////            log.debug ("setUp Error: " + e.toString( ));
////        }
////    }
////
////    protected void tearDown() {
////        try
////        {
////            TestUtils.execSql("Delete from XHB_SCHEDULED_HEARING");
////            TestUtils.execSql("Delete from XHB_LINKED_SH");
////            TestUtils.execSql("Delete from XHB_HEARING");
////            TestUtils.execSql("Delete from XHB_REF_HEARING_TYPE");
////            TestUtils.execSql("Delete from XHB_SITTING");
////            TestUtils.execSql("Delete from XHB_CASE");
////            TestUtils.execSql("Delete from XHB_REF_COURT");
////            TestUtils.execSql("Delete from XHB_COURT");
////            TestUtils.execSql("Delete from XHB_ADDRESS");
////
////        }
////        catch (Exception e)
////        {
////            log.debug ("setUp Error: " + e.toString( ));
////        }
////    }
////
////    public void testCreate() {
////
////        try
////        {
////            log.debug("testCreate called");
////
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            CSAbstractValue value1=  createBasicVO(); /** @todo fill in non-null value */;
////            log.debug("value 1 = " + value1.toString());
////            ut.begin();
////            CSEntityLocal csentitylocalRet = scheduledhearingmaintainer.create(value1);
////
////
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            ScheduledHearingBasicValue shbv = (ScheduledHearingBasicValue)value1;
////            ScheduledHearing scheduledHearing = (ScheduledHearing)csentitylocalRet;
////            log.debug("sittingID = " + scheduledHearing.getSittingId());
////            log.debug("hearingID = "+ scheduledHearing.getHearingId());
////            ut.commit();
////            assertEquals(shbv.getNotBeforeTime().getTime(), scheduledHearing.getNotBeforeTime().getTime() );
////        }
////        catch (Exception e)
////        {
////            fail (e.toString());
////        }
////
////    }
////
////    public void testDelete() {
////
////        try
////        {
////            log.debug("testDelete called");
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            Integer id1=  setUpPK;
////            log.debug("Pkey = " +id1);
////            Integer version2=  setUpVersion;
////            log.debug("version = "+ version2);
////            scheduledhearingmaintainer.delete(id1, version2);
////            log.debug("deleted");
////            assertTrue(true);
////        }
////        catch (Exception e)
////        {
////            fail(e.toString());
////        }
////    }
////    public void testGetScheduledHearingBasicValue()
////    {
////        try
////        {
////            log.debug("testGetScheduledHearingBasicValue called");
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            ScheduledHearing value1=  (ScheduledHearing)scheduledhearingmaintainer.findByPK(setUpPK) /** @todo fill in non-null value */;
////            log.debug("Value 1 = " + value1.toString());
////            ScheduledHearingBasicValue shbv = scheduledhearingmaintainer.getScheduledHearingBasicValue(value1);
////            log.debug("next value = "+ shbv.toString());
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            //assertEquals(shbv.getCurrentStatus(), value1.getCurrentStatus());
////            assertEquals(shbv.getId(), value1.getScheduledHearingId());
////        }
////        catch (ObjectNotFoundException e)
////        {
////            e.printStackTrace();
////            fail();
////        }
////    }
////
////    public void testGetScheduledHearingComplexValue()
////    {
////        try
////        {
////            log.debug("testGetScheduledHearingComplexValue called");
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            ScheduledHearing value1=  (ScheduledHearing)scheduledhearingmaintainer.findByPK(setUpPK) /** @todo fill in non-null value */;
////            ScheduledHearingComplexValue shbv = scheduledhearingmaintainer.getScheduledHearingComplexValue(value1);
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            //assertEquals(shbv.getCurrentStatus(), value1.getCurrentStatus());
////            assertEquals(shbv.getId(), value1.getScheduledHearingId());
////        }
////        catch (ObjectNotFoundException e)
////        {
////            e.printStackTrace();
////            fail();
////        }
////    }
////
//////    public void testGetScheduledHearings() {
//////        ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
//////        Collection localColl1=  null  /** @todo fill in non-null value */;
//////        Collection collectionRet = scheduledhearingmaintainer.getScheduledHearings(localColl1);
//////        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//////    }
////
////    public void testUpdate()
////    {
////        try
////        {
////            log.debug("testUpdate called");
////            ScheduledHearingMaintainer scheduledhearingmaintainer = new ScheduledHearingMaintainer();
////            ScheduledHearing value1=  (ScheduledHearing)scheduledhearingmaintainer.findByPK(setUpPK) /** @todo fill in non-null value */;
////            log.debug("value1 = " + value1.toString());
////            ScheduledHearingBasicValue shbv = scheduledhearingmaintainer.getScheduledHearingBasicValue(value1);
////            log.debug("shbv = " + shbv.toString());
////
////            shbv.setMovedFrom("TEST");
////            log.debug("updating new value");
////            scheduledhearingmaintainer.update(shbv);
////            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////            ScheduledHearing value2=  (ScheduledHearing)scheduledhearingmaintainer.findByPK(setUpPK) /** @todo fill in non-null value */;
////            log.debug("changed Entity = " + value2.getMovedFrom());
////            log.debug("value = " + shbv.getMovedFrom());
////            assertEquals(value2.getMovedFrom(), "TEST" );
////        }
////        catch (ObjectNotFoundException e)
////        {
////            e.printStackTrace();
////            fail();
////        }
////    }
////
////    private ScheduledHearingBasicValue createBasicVO()
////    {
////        ScheduledHearingBasicValue shbv = new ScheduledHearingBasicValue();
////        String currentStatus = "In Progress";
////        Integer hearingProgress = new Integer(1);
////        Boolean caseActive = new Boolean(true);
////        Integer linkedSHId = new Integer(1);
////        Integer refCourtReporterId = new Integer(1);
////        Integer sequenceNo = new Integer(5);
////
////        shbv.setHearingID(this.testHearingId);
////        //shbv.setCurrentStatus(currentStatus);
////        shbv.setDateOfHearing(testDate);
////        shbv.setEndTime(endTime);
////        shbv.setHearingProgress(hearingProgress);
////        shbv.setIsCaseActive(caseActive);
////        shbv.setLinkedSHID(linkedSHId);
////        shbv.setListingNote("Test");
////        shbv.setMovedFrom("Moved");
////        shbv.setNotBeforeTime(notBeforeTime);
////        shbv.setOriginalTime(originalTime);
////        //shbv.setRefCourtReporterID(refCourtReporterId);
////        shbv.setSequenceNo(sequenceNo);
////        shbv.setSittingID(testSittingId);
////        shbv.setStartTime(startTime);
////
////        return shbv;
////    }
////}