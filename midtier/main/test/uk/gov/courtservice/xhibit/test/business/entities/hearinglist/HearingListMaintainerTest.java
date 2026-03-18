//package uk.gov.courtservice.xhibit.test.business.entities.hearinglist;
//
//import java.util.Date;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingList;
//import uk.gov.courtservice.xhibit.business.entities.hearinglist.HearingListMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingListBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.HearingListComplexValue;
//
//
//public class HearingListMaintainerTest extends TestCase {
//
//    private Integer primaryKeyTest = new Integer(1);
//    private Integer courtIdTest = new Integer(5);
//    private Integer setUpVersion;
//    private Integer setUpPK;
//    private static Logger log = CSServices.getLogger(HearingListMaintainerTest.class);
//
//    private Date testDate = new Date();
//
//    public HearingListMaintainerTest(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        log.debug("In setUp");
//
//        try
//        {
//            TestUtils.execSql("delete from xhb_hearing_list");
//            HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//            HearingListBasicValue value=  createBasicVO();
////            value.setListId( new Integer(1));
//            HearingList hearingList = (HearingList)hearinglistmaintainer.create(value);
//            setUpPK = hearingList.getListId();
//
//            setUpVersion = hearingList.getVersion();
//            //hearinglistmaintainer.update(value);
//        }
//        catch (Exception e)
//        {
//            log.debug("setUp error: " + e.toString());
//        }
//    }
//
//    protected void tearDown() {
//    }
//
////    public void testCreate() {
////        log.debug( "testCreate() " );
////        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
////        HearingListBasicValue value1=  createBasicVO();
////        EJBLocalObject ejblocalobjectRet = hearinglistmaintainer.create(value1);
////        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////    }
//    public void testDelete() {
//        try
//        {
//            log.debug( "testDelete() " );
//            HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//            Integer id1=  setUpPK  /** @todo fill in non-null value */;
//            Integer version2=  setUpVersion  /** @todo fill in non-null value */;
//            hearinglistmaintainer.delete(id1, version2);
//            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        }
//        catch (Exception e)
//        {
//            fail(e.toString());
//        }
//    }
//
//    public void testFindByPrimaryKey()
//    {
//        try
//        {
//            log.debug( "testFindByPrimaryKey() " );
//            HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//            HearingList hearinglistRet = hearinglistmaintainer.findByPrimaryKey(setUpPK);
//        }
//        catch (Exception e)
//        {
//            fail(e.toString());
//        }
//
//    }
//
//    public void testUpdate()
//    {
//      try
//      {
//        log.debug( "testUpdate() " );
//        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//
//        HearingList hearinglistRet = hearinglistmaintainer.findByPrimaryKey(setUpPK);
//        HearingListBasicValue value = hearinglistmaintainer.getHearingListBasicValue(hearinglistRet);
//        value.setListType("F");
//        hearinglistmaintainer.update(value);
//
//        assertEquals( hearinglistRet.getListType(), value.getListType() );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    public void testFindByCourtIdAndDate()
//    {
//      try
//      {
//        log.debug( "testFindByCourtIdAndDate() " );
//        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//        Integer courtId1 = courtIdTest ;
//        Date date2 =  testDate;
//        HearingList hearinglistRet = hearinglistmaintainer.findByCourtIdAndDate(courtId1, date2);
//
//        assertEquals( hearinglistRet.getCourtId(), courtIdTest );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    public void testFindByCourtIdDateAndListType()
//    {
//      try
//      {
//        log.debug( "testFindByCourtIdAndDate() " );
//        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//        Integer courtId1 = courtIdTest ;
//        Date date2 =  testDate;
//        String type = "D";
//        HearingList hearinglistRet = hearinglistmaintainer.findByCourtIdDateAndListType(courtId1, date2, type);
//
//        assertEquals( hearinglistRet.getCourtId(), courtIdTest );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//
//    }
//
//    public void testGetHearingListBasicValue()
//    {
//      try
//      {
//        log.debug( "testGetHearingListBasicValue() " );
//        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//        HearingList hearinglistRet = hearinglistmaintainer.findByPrimaryKey(setUpPK);
//
//        HearingListBasicValue hearinglistbasicvalueRet = hearinglistmaintainer.getHearingListBasicValue(hearinglistRet);         /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//
//        assertEquals( hearinglistbasicvalueRet.getCourtId(), hearinglistRet.getCourtId() );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    public void testGetHearingListComplexValue()
//    {
//      try
//      {
//        log.debug( "testGetHearingListComplexValue() " );
//
//        HearingListMaintainer hearinglistmaintainer = new HearingListMaintainer();
//        HearingList hearinglistRet = hearinglistmaintainer.findByPrimaryKey(setUpPK);
//
//        HearingListComplexValue hearinglistcomplexvalueRet = hearinglistmaintainer.getHearingListComplexValue(hearinglistRet);
//
//        assertEquals( hearinglistcomplexvalueRet.getCourtId(), hearinglistRet.getCourtId() );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    private HearingListBasicValue createBasicVO()
//    {
//        log.debug( "createBasicVO called");
//        HearingListBasicValue hlbv = new HearingListBasicValue(primaryKeyTest, new Integer(2));
//        hlbv.setCourtId( courtIdTest );
//        hlbv.setCrestListId( new Integer (10 ));
//        //hlbv.setDailyListXMLId( new Integer(15));
//        hlbv.setEditionNo( new Integer(20));
//        hlbv.setEndDate(testDate );
//        hlbv.setListCourtType("TE");
//        hlbv.setListType("D");
//        hlbv.setPrintReference("TEST_REF");
//        hlbv.setPublishedTime( testDate );
//        hlbv.setStartDate(testDate);
//        hlbv.setStatus("T");
//        return hlbv;
//    }
//}
//