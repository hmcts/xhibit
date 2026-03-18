//package uk.gov.courtservice.xhibit.test.business.entities.sitting;
//
//import java.util.Date;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
//import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.sitting.Sitting;
//import uk.gov.courtservice.xhibit.business.entities.sitting.SittingMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SittingComplexValue;
//
///**
// * <p>Title: SittingMaintainerTest</p>
// * <p>Description: Sitting Entity Maintainer Test Class</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Joseph Babad
// * @version $Id: SittingMaintainerTest.java,v 1.7 2006/07/11 14:16:57 xzfdtb Exp $
// * The test is only applicable when the correct test dat ahas been inserted I>E XHB V0.13 TEst Data
// */
//
//public class SittingMaintainerTest extends TestCase {
//
//    private static Logger log  = CSServices.getLogger(SittingMaintainerTest.class);
//    private Sitting localSittingEntity;
//    private Date sittingDate = new Date();
//    private Integer testCourtRoom1 = new Integer(6);
//    private Integer testCourtRoom2 = new Integer(7);
//
//    private Integer testCourtSite1 = new Integer(1);
//    private Integer testCourtSite2 = new Integer(1);
//    private Integer setUpPK;
//    private Integer setUpVersion;
//
//
//    public SittingMaintainerTest(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//        log.debug("setUp() called");
//        try
//        {
//            TestUtils.execSql("delete from xhb_sitting");
//
//            SittingMaintainer sittingmaintainer = new SittingMaintainer();
//            CSAbstractValue value1 =  createBV()  /** @todo fill in non-null value */;
//            CSEntityLocal csentitylocalRet = sittingmaintainer.create(value1);
//            localSittingEntity = (Sitting)csentitylocalRet;
//            setUpPK = localSittingEntity.getSittingId();
//            setUpVersion = localSittingEntity.getVersion();
//            log.debug("setUp - " + localSittingEntity.getSittingId() + " " + (Integer)localSittingEntity.getPrimaryKey());
//        }
//        catch (Exception e)
//        {
//            log.debug("setUp() Error: " + e.toString() );
//        }
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testCreate() {
//        log.debug("testCreate()");
//        SittingMaintainer sittingmaintainer = new SittingMaintainer();
//        CSAbstractValue value1 =  createBV()  /** @todo fill in non-null value */;
//        CSEntityLocal csentitylocalRet = sittingmaintainer.create(value1);
//        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        Sitting sitting = (Sitting)csentitylocalRet;
//        SittingBasicValue sbv = (SittingBasicValue)value1;
//        assertEquals( sitting.getCourtRoomId(), sbv.getCourtRoomID());
//    }
//
//    public void testDelete() {
//        log.debug("testDelete()");
//        SittingMaintainer sittingmaintainer = new SittingMaintainer();
//        Integer id1=  this.setUpPK; /** @todo fill in non-null value */;
//        Integer version2=  this.setUpVersion  /** @todo fill in non-null value */;
//        try
//        {
//            sittingmaintainer.delete(id1, version2);
//            /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//            assertTrue(true);
//        }
//        catch (Exception e)
//        {
//            fail(e.toString());
//        }
//    }
//
///*
//    public void testFindByDateAndCourt()
////    {
////      try
////      {
////        log.debug("testFindByDateAndCourt()");
////
////        SittingMaintainer sittingmaintainer = new SittingMaintainer();
////        // Create another one...
////        SittingBasicValue value1 =  (SittingBasicValue)createBV()   @todo fill in non-null value */;
////        value1.setCourtRoomID(this.testCourtRoom2);
////        value1.setCourtSiteID(this.testCourtSite2);
////        CSEntityLocal csentitylocalRet = sittingmaintainer.create(value1);
////        localSittingEntity = (Sitting)csentitylocalRet;
////
////        // Now do the find...
////        Date sittingTime1=  sittingDate  @todo fill in non-null value */;
////        Integer courtRoomId2=  testCourtRoom1   @todo fill in non-null value */;
////        Integer courtSiteId3=  testCourtSite1  @todo fill in non-null value */;
////        Sitting sittingRet = sittingmaintainer.findByDateAndCourt(sittingTime1, courtRoomId2, courtSiteId3);
////        / @todo:  Insert test code here.  Use assertEquals(), for example. */
////        assertEquals( sittingRet.getCourtSiteId(), testCourtSite1 );
////      }
////      catch (ObjectNotFoundException e)
////      {
////        e.printStackTrace();
////        fail();
////      }
////    }
////
////*/
////    public void testGetHome() {
////        SittingMaintainer sittingmaintainer = new SittingMaintainer();
////        SittingHome sittinghomeRet = sittingmaintainer.getHome();
////        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
////    }
//    public void testGetSittingBasicValue()
//    {
//      try
//      {
//        log.debug("testGetSittingBasicValue()");
//        SittingMaintainer sittingmaintainer = new SittingMaintainer();
//
//        Sitting sitting1 = sittingmaintainer.findByPK(localSittingEntity.getSittingId());
//
//        SittingBasicValue sittingbasicvalueRet = sittingmaintainer.getSittingBasicValue(sitting1);
//        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        assertEquals( sittingbasicvalueRet.getCourtRoomID(), sitting1.getCourtRoomId());
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    public void testGetSittingComplexValue()
//    {
//      try
//      {
//        log.debug("testGetSittingComplexValue()");
//        SittingMaintainer sittingmaintainer = new SittingMaintainer();
//
//        Sitting sitting1 = sittingmaintainer.findByPK(localSittingEntity.getSittingId());
//
//        SittingComplexValue sittingcomplexvalueRet = sittingmaintainer.getSittingComplexValue(sitting1);
//        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        assertEquals( sittingcomplexvalueRet.getCourtRoomID(), sitting1.getCourtRoomId());
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    public void testUpdate()
//    {
//      try
//      {
//        log.debug("testUpdate()" + setUpPK );
//        SittingMaintainer sittingMaintainer = new SittingMaintainer();
////        SittingBasicValue value1 =  (SittingBasicValue)createBV() /** @todo fill in non-null value */;
////        value1.setCourtRoomID(this.testCourtRoom2);
////        sittingMaintainer.update(value1);
//
//        // find byPK...
//        Sitting s1 = sittingMaintainer.findByPK(setUpPK);
//
//        SittingBasicValue sittingbasicvalueRet = sittingMaintainer.getSittingBasicValue(s1);
//        sittingbasicvalueRet.setCourtRoomID(this.testCourtRoom2);
//        sittingMaintainer.update(sittingbasicvalueRet);
//        // Find again...
//        Sitting s2 = sittingMaintainer.findByPK(setUpPK);
//        /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//        assertEquals( s2.getCourtRoomId(), this.testCourtRoom2 );
//      }
//      catch (ObjectNotFoundException e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//    }
//
//    private SittingBasicValue createBV()
//    {
//        SittingBasicValue sbv = new SittingBasicValue();
//        sbv.setCourtRoomID( testCourtRoom1 );
//        sbv.setCourtSiteID( testCourtSite1 );
//        sbv.setIsFloating("1");
//        sbv.setListID(new Integer(6));
//        sbv.setIsSittingJudge("1");
//        sbv.setJusticeName1("JUSTICE 1");
//        sbv.setJusticeName2("JUSTICE 1");
//        sbv.setJusticeName3("JUSTICE 1");
//        sbv.setJusticeName4("JUSTICE 1");
//        sbv.setRefJudgeID(new Integer(1006));
//        sbv.setRefJustice1ID(new Integer(649));
//        sbv.setRefJustice2ID(new Integer(650));
//        sbv.setRefJustice3ID(new Integer(651));
//        sbv.setRefJustice4ID(new Integer(652));
//        sbv.setSittingNote("Sitting Note");
//        sbv.setSittingSequenceNo(new Integer(9));
//        sbv.setSittingTime( sittingDate );
//        return sbv;
//    }
//
//}
//