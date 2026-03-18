//
//package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.linkhearing;
//
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.linkhearing.LinkHearingsHelper;
//import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
//import uk.gov.courtservice.xhibit.business.entities.linkedhearing.*;
//import uk.gov.courtservice.xhibit.business.entities.hearing.*;
//
//import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.*;
//
//import uk.gov.courtservice.xhibit.test.business.services.hearingschedule.hearingrecord.TestConstants;
//
//import uk.gov.courtservice.framework.business.vos.*;
//import uk.gov.courtservice.framework.business.entities.*;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//
//import javax.transaction.UserTransaction;
//import junit.framework.*;
//import org.apache.log4j.Logger;
//import javax.naming.InitialContext;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Vector;
//
//
///**
// * <p>Title: LinkHearingsHelperTest</p>
// * <p>Description: Test the linkage of hearings </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class LinkHearingsHelperTest extends TestCase {
//
//  private static Logger log  = CSServices.getLogger(LinkHearingsHelperTest.class);
//
//
//  public LinkHearingsHelperTest(String s) {
//    super(s);
//  }
//
//
//
//  protected void setUp()
//  {
//    log.debug(">>>>>>>>>>>> setUp Starts <<<<<<<<<<<<<");
//    try
//    {
//      //delete the data
//      TestUtils.execSql(TestConstantsLinkHearing.removeAllExportAs);
//      TestUtils.execSql(TestConstants.delExportA);
//      TestUtils.execSql(TestConstants.DEL_SOLICITOR);
//      TestUtils.execSql(TestConstants.DEL_SOL_FIRM);
//      TestUtils.execSql(TestConstants.DEL_ADV);
//      TestUtils.execSql(TestConstants.DEL_CHAMBER);
//      TestUtils.execSql(TestConstants.delShLegRep);
//      TestUtils.execSql(TestConstants.delSchedHearDef);
//      TestUtils.execSql(TestConstants.delRefLegRep);
//      TestUtils.execSql(TestConstants.delShJudge);
//      TestUtils.execSql(TestConstants.delShAtt);
//      TestUtils.execSql(TestConstants.delJudge);
//      TestUtils.execSql(TestConstants.DEL_SH_ATT_CR);
//      TestUtils.execSql(TestConstants.DEL_SH_JUSTICE);
//      TestUtils.execSql(TestConstants.delShHrg1);
//      TestUtils.execSql(TestConstants.delSitting);
//      TestUtils.execSql(TestConstants.delHrgList);
//      TestUtils.execSql(TestConstants.delDefHrgRec);
//      TestUtils.execSql(TestConstants.delHrg);
//      TestUtils.execSql(TestConstants.delRefHrgType);
//      TestUtils.execSql(TestConstants.delDefOnCase);
//      TestUtils.execSql(TestConstants.DEL_CR);
//      TestUtils.execSql(TestConstants.DEL_CR_FIRM);
//
//      /*TestUtils.execSql(TestConstants.delCase);
//      TestUtils.execSql(TestConstants.DEL_CASES);
//      TestUtils.execSql(TestConstants.DEL_DEF_REF);//
//      TestUtils.execSql(TestConstants.delDef);
//      TestUtils.execSql(TestConstants.delSolFirm);
//      TestUtils.execSql(TestConstants.delCCInfo);
//      TestUtils.execSql(TestConstants.delRefCourt);
//      TestUtils.execSql(TestConstants.delCourtRoom);
//      TestUtils.execSql(TestConstants.delCourtSite);
//      TestUtils.execSql(TestConstants.delAddr);
//      TestUtils.execSql(TestConstants.delCourt);
//      */
//
//      //remove
//      TestUtils.execSql(TestConstantsLinkHearing.removeAllExportAs);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_HRG);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_L_HRG);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_REF_HRG_TYPE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_CASE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_REF_COURT);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT_ROOM);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT_SITE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_ADDR);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT);
//
//      //insert data
//      TestUtils.execSql(TestConstantsLinkHearing.INS_COURT);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_ADDR);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_COURT_SITE);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_COURT_ROOM);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_REF_COURT);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_REF_HRG_TYPE);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_REF_HRG_TYPE2);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_REF_HRG_TYPE3);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_CASE);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_CASE2);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_CASE3);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_L_HRG_1);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_L_HRG_2);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_L_HRG_3);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_1);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_2);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_3);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_4);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_5);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_6);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_7);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_8);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_9);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_10);
//      TestUtils.execSql(TestConstantsLinkHearing.INS_HRG_11);
//      TestUtils.execSql(TestConstantsLinkHearing.insertExportAReadyforExport_HRG);
//      TestUtils.execSql(TestConstantsLinkHearing.insertExportAReadyforExport_L_HRG);
//
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    log.debug(">>>>>>>>>>>> setUp Ends <<<<<<<<<<<<<");
//
//  }
//
//
//
//  protected void tearDown()
//  {
//    log.debug(">>>>>>>>>>>> tearDown Starts <<<<<<<<<<<<<");
//    try
//    {
//      //remove data
//      TestUtils.execSql(TestConstantsLinkHearing.removeAllExportAs);
//      TestUtils.execSql(TestConstants.delExportA);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_HRG);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_L_HRG);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_REF_HRG_TYPE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_CASE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_REF_COURT);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT_ROOM);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT_SITE);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_ADDR);
//      TestUtils.execSql(TestConstantsLinkHearing.DEL_COURT);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    log.debug(">>>>>>>>>>>> tearDown Ends <<<<<<<<<<<<<");
//  }
//
//
//
//  /**
//   * Test to unlink a single hearing - only 1 hearing exists with this linked id.
//   */
//  public void testUnlinkHearing1()
//  {
//    log.debug(">>>>>>>>>>>> testUnlinkHearing1 Starts <<<<<<<<<<<<<");
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    try
//    {
//      linkhearingshelper.unlinkHearing(TestConstantsLinkHearing.HRG_3_ID);
//      Hearing entity = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_3_ID);
//
//      Collection all = (Collection)maintainer.findByLinkedHearingId(TestConstantsLinkHearing.L_HRG_2_ID);
//      if(all != null)
//        log.debug("There are "+all.size()+" linked hearings left");
//      else
//        log.debug("There are no other linked hearings");
//
//      log.debug("Hearing id: " + entity.getHearingId());
//      log.debug("Hearing linked id: " + entity.getLinkedHearingId());
//      this.assertEquals(entity.getLinkedHearingId(), null);
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUnlinkHearing1 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//
//  /**
//   * Test to unlink hearings where there are only 2 linked hearings. Both
//   * should be unlinked.
//   */
//  public void testUnlinkHearing2()
//  {
//    log.debug(">>>>>>>>>>>> testUnlinkHearing2 Starts <<<<<<<<<<<<<");
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    try
//    {
//      linkhearingshelper.unlinkHearing(TestConstantsLinkHearing.HRG_2_ID);
//      Hearing entity = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_2_ID);
//
//      Collection all = (Collection)maintainer.findByLinkedHearingId(TestConstantsLinkHearing.L_HRG_1_ID);
//      if(all != null)
//        log.debug("There are "+all.size()+" linked hearings left");
//      else
//        log.debug("There are no other linked hearings");
//
//      log.debug("Hearing id: " + entity.getHearingId());
//      log.debug("Hearing linked id: " + entity.getLinkedHearingId());
//      this.assertEquals(entity.getLinkedHearingId(), null);
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUnlinkHearing2 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * Test to unlink a single hearing where there are 3 linked hearings.
//   * only the one should be unlinked.
//   */
//  public void testUnlinkHearing3()
//  {
//    log.debug(">>>>>>>>>>>> testUnlinkHearing3 Starts <<<<<<<<<<<<<");
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    try
//    {
//      linkhearingshelper.unlinkHearing(TestConstantsLinkHearing.HRG_5_ID);
//      Hearing entity = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_5_ID);
//
//      Collection all = (Collection)maintainer.findByLinkedHearingId(TestConstantsLinkHearing.L_HRG_3_ID);
//
//      if(all != null)
//        log.debug("There are "+all.size()+" linked hearings left");
//      else
//        log.debug("There are no other linked hearings");
//
//      log.debug("Hearing id: " + entity.getHearingId());
//      log.debug("Hearing linked id: " + entity.getLinkedHearingId());
//      this.assertEquals(entity.getLinkedHearingId(), null);
//
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUnlinkHearing3 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * Test to unlink a hearing that has not been linked.
//   */
//  public void testUnlinkHearing4()
//  {
//    log.debug(">>>>>>>>>>>> testUnlinkHearing4 Starts <<<<<<<<<<<<<");
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    try
//    {
//      linkhearingshelper.unlinkHearing(TestConstantsLinkHearing.HRG_7_ID);
//      Hearing entity = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_7_ID);
//
//      log.debug("Hearing id: " + entity.getHearingId());
//      log.debug("Hearing linked id: " + entity.getLinkedHearingId());
//      this.assertEquals(entity.getLinkedHearingId(), null);
//
//    }
//    catch(HearingScheduleException ex)
//    {
//      //this exception is expected here since there are no hearings to unlink....
//      this.assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUnlinkHearing4 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//
//
//  /**
//   * Test where the lead hearing has a linked id already.
//   */
//  public void testLinkHearings1()
//  {
//    log.debug(">>>>>>>>>>>> testLinkHearings1 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings1 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings1 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings1 Starts <<<<<<<<<<<<<");
//
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    Integer leadHearingID1= TestConstantsLinkHearing.HRG_1_ID;
//
//    Vector hearingIDs = new Vector();
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_2_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_3_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_4_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_5_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_6_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_7_ID);
//
//    Collection hearingIDs2= hearingIDs;
//
//    try {
//      linkhearingshelper.linkHearings(leadHearingID1, hearingIDs2);
//
//      Hearing entityUpdated = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_7_ID);
//      Hearing entityLead = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_1_ID);
//
//      log.debug("updated hearing");
//      log.debug("Hearing id: " + entityUpdated.getHearingId());
//      log.debug("Hearing linked id: " + entityUpdated.getLinkedHearingId());
//
//      log.debug("lead hearing");
//      log.debug("Hearing id: " + entityLead.getHearingId());
//      log.debug("Hearing linked id: " + entityLead.getLinkedHearingId());
//      this.assertEquals(entityUpdated.getLinkedHearingId(), entityLead.getLinkedHearingId());
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testLinkHearings1 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//
//  /**
//   * Test where the lead hearing's linked id is null.
//   * this will have to create a new linked hearing
//   */
//  public void testLinkHearings2()
//  {
//    log.debug(">>>>>>>>>>>> testLinkHearings2 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings2 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings2 Starts <<<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>> testLinkHearings2 Starts <<<<<<<<<<<<<");
//
//    LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//    HearingMaintainer maintainer = new HearingMaintainer();
//
//    Integer leadHearingID1= TestConstantsLinkHearing.HRG_7_ID;
//
//    Vector hearingIDs = new Vector();
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_2_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_3_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_4_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_5_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_6_ID);
//    hearingIDs.addElement(TestConstantsLinkHearing.HRG_1_ID);
//
//    Collection hearingIDs2= hearingIDs;
//
//    try {
//      linkhearingshelper.linkHearings(leadHearingID1, hearingIDs2);
//
//      Hearing entityUpdated = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_1_ID);
//      Hearing entityLead = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_7_ID);
//
//      log.debug("updated hearing");
//      log.debug("Hearing id: " + entityUpdated.getHearingId());
//      log.debug("Hearing linked id: " + entityUpdated.getLinkedHearingId());
//
//      log.debug("lead hearing");
//      log.debug("Hearing id: " + entityLead.getHearingId());
//      log.debug("Hearing linked id: " + entityLead.getLinkedHearingId());
//      this.assertEquals(entityUpdated.getLinkedHearingId(), entityLead.getLinkedHearingId());
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testLinkHearings2 Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * testListCaseWithHearings
//   */
//  public void testListCaseWithHearings()
//    {
//      log.debug(">>>>>>>>>>>> testListCaseWithHearings Starts <<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>> testListCaseWithHearings Starts <<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>> testListCaseWithHearings Starts <<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>> testListCaseWithHearings Starts <<<<<<<<<<<<<");
//      LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//
//      String caseTypeAndNumber = "S2003222";
//      Integer courtID = TestConstantsLinkHearing.COURT_ID;
//      Integer leadHearingID = TestConstantsLinkHearing.HRG_8_ID;
//
//      try
//      {
//        CaseHearingValue value = linkhearingshelper.listCaseWithHearings(caseTypeAndNumber,
//            courtID, leadHearingID);
//
//        log.debug("Case id :" + value.getCaseID());
//        log.debug("getCaseNumber :" + value.getCaseNumber());
//        log.debug("getCaseSubType :" + value.getCaseSubType());
//        log.debug("getCaseType :" + value.getCaseType());
//        log.debug("getCourtID :" + value.getCourtID());
//
//        Iterator it = value.getHearingValues().iterator();
//        while(it.hasNext())
//        {
//          HearingValue hearing = (HearingValue)it.next();
//          log.debug("hearingid : " + hearing.getHearingID());
//          log.debug("getEndDate : " + hearing.getEndDate());
//          log.debug("getHearingTypeCode : " + hearing.getHearingTypeCode());
//          log.debug("getHearingTypeDesc : " + hearing.getHearingTypeDesc());
//          log.debug("getRefHearingTypeID : " + hearing.getRefHearingTypeID());
//          log.debug("getStartDate : " + hearing.getStartDate());
//        }
//
//        this.assertTrue(true);
//      }
//      catch(Exception e)
//      {
//        log.debug(e.toString());
//        e.printStackTrace();
//        fail();
//      }
//      finally
//      {
//        log.debug(">>>>>>>>>>>> testListCaseWithHearings Ends <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>> testListCaseWithHearings Ends <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>> testListCaseWithHearings Ends <<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>> testListCaseWithHearings Ends <<<<<<<<<<<<<");
//      }
//    }
//
//
//
//       /**
//         * test where the hearing has been exported
//         */
//        public void testLinkHearings3()
//        {
//          log.debug(">>>>>>>>>>>> testLinkHearings3 Starts <<<<<<<<<<<<<");
//          log.debug(">>>>>>>>>>>> testLinkHearings3 Starts <<<<<<<<<<<<<");
//          log.debug(">>>>>>>>>>>> testLinkHearings3 Starts <<<<<<<<<<<<<");
//          log.debug(">>>>>>>>>>>> testLinkHearings3 Starts <<<<<<<<<<<<<");
//
//          LinkHearingsHelper linkhearingshelper = new LinkHearingsHelper();
//          HearingMaintainer maintainer = new HearingMaintainer();
//
//          Integer leadHearingID1= TestConstantsLinkHearing.HRG_8_ID;
//
//          Vector hearingIDs = new Vector();
//          hearingIDs.addElement(TestConstantsLinkHearing.HRG_9_ID);
//          hearingIDs.addElement(TestConstantsLinkHearing.HRG_10_ID);
//          hearingIDs.addElement(TestConstantsLinkHearing.HRG_11_ID);
//
//
//          Collection hearingIDs2= hearingIDs;
//
//          try {
//            linkhearingshelper.linkHearings(leadHearingID1, hearingIDs2);
//
//            Hearing entityUpdated = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_10_ID);
//            Hearing entityLead = (Hearing)maintainer.findByPK(TestConstantsLinkHearing.HRG_8_ID);
//
//            log.debug("updated hearing");
//            log.debug("Hearing id: " + entityUpdated.getHearingId());
//            log.debug("Hearing linked id: " + entityUpdated.getLinkedHearingId());
//
//            log.debug("lead hearing");
//            log.debug("Hearing id: " + entityLead.getHearingId());
//            log.debug("Hearing linked id: " + entityLead.getLinkedHearingId());
//           // this.assertEquals(entityUpdated.getLinkedHearingId(), entityLead.getLinkedHearingId());
//          }
//          catch(Exception e)
//          {
//            log.debug(">>>>>>>>>>>>>>>>>>> THIS SHOULD HAVE FAILED SINCE HEARING IS EXPORTED <<<<<<<<<<<<<");
//            assertTrue(true);
//            log.debug(e.toString());
//            e.printStackTrace();
//            //fail();
//          }
//          finally
//          {
//            log.debug(">>>>>>>>>>>> testLinkHearings2 Ends <<<<<<<<<<<<<");
//          }
//  }
//}