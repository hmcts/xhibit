//package uk.gov.courtservice.xhibit.test.business.entities.exporta;
//
//// jdk
//import java.util.ArrayList;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.exporta.ExportA;
//import uk.gov.courtservice.xhibit.business.entities.exporta.ExportAHome;
//
///**
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class ExportATest extends TestCase
//{
//  private static Logger log  = CSServices.getLogger(ExportATest.class);
//  private ExportAHome home = (ExportAHome)CSServices.getServiceLocator().getLocalHome(ExportAHome.class);
//  int exporta_id = 999;
//  int hearing_id = 900;
//  int linked_hearing_id = 950;
//
//  private String delete = "delete from xhb_exporta where export_a_id = "+exporta_id +
//                          " or hearing_id = " + hearing_id + " or linked_hearing_id = " +
//                          linked_hearing_id;
//  private String deleteHearing = "delete from xhb_hearing where hearing_id = "+hearing_id;
//
//  private String insertSingleHearing = "insert into xhb_hearing (hearing_id, case_id, ref_hearing_type_id," +
//                      " court_id, hearing_end_date, linked_hearing_id) values (" + hearing_id +
//                      ", 1, 1, 1, TO_Date( '02/21/2003 09:30:45 AM', 'MM/DD/YYYY HH:MI:SS AM') , null)";
//
//  private String insert1 = "insert into xhb_exporta (export_a_id, court_clerk_export, status_flag, " +
//                                        "hearing_id) values ("+exporta_id+", 'Marie', 'F', 1)";
//
//  private String insert2 = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                        "hearing_id) values ('Marie', 'F', "+hearing_id+")";
//
//  private String insert3 = "insert into xhb_exporta (court_clerk_export, status_flag, " +
//                                        "linked_hearing_id) values ('Marie', 'F', "+linked_hearing_id+")";
//
//  public ExportATest(String s)
//  {
//      super(s);
//  }
//
//  /**
//   *
//   * @throws Exception
//   */
//  protected void setUp() throws Exception
//  {
//    TestUtils.execSql(delete);
//    TestUtils.execSql(deleteHearing);
//    TestUtils.execSql(insertSingleHearing);
//    TestUtils.execSql(insert1);
//    TestUtils.execSql(insert2);
//    TestUtils.execSql(insert3);
//
//  }
//
//  /**
//   *
//   * @throws Exception
//   */
//  protected void tearDown() throws Exception
//  {
//    TestUtils.execSql(delete);
//    TestUtils.execSql(deleteHearing);
//  }
//
//  /**
//   * testFindByKeyAndVersion
//   * @throws Exception
//   */
//  public void testFindByKeyAndVersion() throws Exception
//  {
//    log.debug("################## testFindByKeyAndVersion() start ##################");
//    try
//    {
//      //need to find by pk before so that we can get a version number.
//      ExportA local = home.findByPrimaryKey(new Integer(exporta_id));
//
//      ExportA local2 = home.findByKeyAndVersion(local.getExportAId(), local.getVersion());
//
//      log.debug(">>>>>>>>>>>>>> Found an Object <<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>> Searched for <<<<<<<<<<<<<<<");
//      log.debug("exporta_id : " + exporta_id);
//      log.debug("version : " + local.getVersion());
//      log.debug(">>>>>>>>>>>>>> Found <<<<<<<<<<<<<<<");
//      log.debug("ExportAId : " + local2.getExportAId());
//      log.debug("Hearing id  : " + local2.getHearingId());
//      log.debug("linked hearingid : " + local2.getLinkedHearingId());
//      log.debug("status flag : " + local2.getStatusFlag());
//      log.debug("court clerk : " + local2.getCourtClerkExport());
//
//      log.debug("################## testFindByKeyAndVersion() finish ##################");
//      assertEquals(local2.getExportAId().intValue(), exporta_id);
//    }
//    catch(Exception e)
//    {
//        e.printStackTrace();
//        fail();
//    }
//  }
//
//  /**
//   * testFindByPrimaryKey
//   * @throws Exception
//   */
//  public void testFindByPrimaryKey() throws Exception
//  {
//    log.debug("################## testFindByPrimaryKey() start ##################");
//    try
//    {
//      ExportA local = home.findByPrimaryKey(new Integer(exporta_id));
//
//      log.debug(">>>>>>>>>>>>>> Found an Object <<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>> Searched for <<<<<<<<<<<<<<<");
//      log.debug("exporta_id : " + exporta_id);
//      log.debug(">>>>>>>>>>>>>> Found <<<<<<<<<<<<<<<");
//      log.debug("ExportAId : " + local.getExportAId());
//      log.debug("Hearing id  : " + local.getHearingId());
//      log.debug("linked hearingid : " + local.getLinkedHearingId());
//      log.debug("status flag : " + local.getStatusFlag());
//      log.debug("court clerk : " + local.getCourtClerkExport());
//
//      log.debug("################## testFindByPrimaryKey() finish ##################");
//      assertEquals(local.getExportAId().intValue(), exporta_id);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * testRemove
//   * @throws Exception
//   */
//  public void testRemove() throws Exception
//  {
//    log.debug("################## testRemove() start ##################");
//    try
//    {
//      //need to find the local to get the version before.
//      ExportA local = home.findByPrimaryKey(new Integer(exporta_id));
//
//      log.debug(">>>>>>>>>>>>>> Found an Object <<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>> Searched for <<<<<<<<<<<<<<<");
//      log.debug("exporta_id : " + exporta_id);
//      log.debug(">>>>>>>>>>>>>> Found <<<<<<<<<<<<<<<");
//      log.debug("ExportAId : " + local.getExportAId());
//      log.debug("Hearing id  : " + local.getHearingId());
//      log.debug("linked hearingid : " + local.getLinkedHearingId());
//      log.debug("status flag : " + local.getStatusFlag());
//      log.debug("court clerk : " + local.getCourtClerkExport());
//
//      local.remove();
//      log.debug("################## testRemove() finish ##################");
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * testFindByHearingLinkedHearingID where the hearing id is set
//   * @throws Exception
//   */
//  public void testFindByHearingLinkedHearingID1() throws Exception
//  {
//    log.debug("################## testFindByHearingLinkedHearingID() start - find hearing ##################");
//    try
//    {
//      ExportA local = null;
//      ArrayList locals = (ArrayList)home.findByHearingLinkedHearingID(new Integer(hearing_id), null);
//
//      for(int i = 0; i < locals.size(); i++)
//      {
//        local = (ExportA)locals.get(i);
//        log.debug(">>>>>>>>>>>>>> Found an Object <<<<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>> Searched for <<<<<<<<<<<<<<<");
//        log.debug("hearing_id : " + hearing_id);
//        log.debug(">>>>>>>>>>>>>> Found <<<<<<<<<<<<<<<");
//        log.debug("ExportAId : " + local.getExportAId());
//        log.debug("Hearing id  : " + local.getHearingId());
//        log.debug("linked hearingid : " + local.getLinkedHearingId());
//        log.debug("status flag : " + local.getStatusFlag());
//        log.debug("court clerk : " + local.getCourtClerkExport());
//      }
//      log.debug("################## testFindByHearingLinkedHearingID() finish ##################");
//      assertEquals(local.getHearingId().intValue(), hearing_id);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//   * testFindByHearingLinkedHearingID where the hearing id is set
//   * @throws Exception
//   */
//  public void testFindByHearingLinkedHearingID2() throws Exception
//  {
//    log.debug("################## testFindByHearingLinkedHearingID() start - find linked_hearing ##################");
//    try
//    {
//      ExportA local = null;
//      ArrayList locals = (ArrayList)home.findByHearingLinkedHearingID(null,
//          new Integer(linked_hearing_id));
//
//      for(int i = 0; i < locals.size(); i++)
//      {
//        local = (ExportA)locals.get(i);
//        log.debug(">>>>>>>>>>>>>> Found an Object <<<<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>> Searched for <<<<<<<<<<<<<<<");
//        log.debug("linked_hearing_id : " + linked_hearing_id);
//        log.debug(">>>>>>>>>>>>>> Found <<<<<<<<<<<<<<<");
//        log.debug("ExportAId : " + local.getExportAId());
//        log.debug("Hearing id  : " + local.getHearingId());
//        log.debug("linked hearingid : " + local.getLinkedHearingId());
//        log.debug("status flag : " + local.getStatusFlag());
//        log.debug("court clerk : " + local.getCourtClerkExport());
//      }
//      log.debug("################## testFindByHearingLinkedHearingID() finish ##################");
//      assertEquals(local.getLinkedHearingId().intValue(), linked_hearing_id);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//
//  /**
//   * testCreate
//   * @throws Exception
//   */
//  public void testCreate() throws Exception
//  {
//    log.debug("################## testCreate() start ##################");
//    try
//    {
//      //create one for linked hearings
//      ExportA local1 = home.create("Marie the exporter", "R", new Integer(linked_hearing_id), null);
//
//      log.debug(">>>>>>>>>>>>>> Created an Object <<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>> Created for <<<<<<<<<<<<<<<");
//      log.debug("linked_hearing_id : " + linked_hearing_id);
//      log.debug(">>>>>>>>>>>>>> Created <<<<<<<<<<<<<<<");
//      log.debug("ExportAId : " + local1.getExportAId());
//      log.debug("Hearing id  : " + local1.getHearingId());
//      log.debug("linked hearingid : " + local1.getLinkedHearingId());
//      log.debug("status flag : " + local1.getStatusFlag());
//      log.debug("court clerk : " + local1.getCourtClerkExport());
//
//      //create one for hearings
//      ExportA local2 = home.create("Marie the exporter", "R", new Integer(hearing_id), null);
//
//      log.debug(">>>>>>>>>>>>>> Created an Object <<<<<<<<<<<<<<<");
//      log.debug(">>>>>>>>>>>>>> Created for <<<<<<<<<<<<<<<");
//      log.debug("hearing_id : " + hearing_id);
//      log.debug(">>>>>>>>>>>>>> Created <<<<<<<<<<<<<<<");
//      log.debug("ExportAId : " + local2.getExportAId());
//      log.debug("Hearing id  : " + local2.getHearingId());
//      log.debug("linked hearingid : " + local2.getLinkedHearingId());
//      log.debug("status flag : " + local2.getStatusFlag());
//      log.debug("court clerk : " + local2.getCourtClerkExport());
//
//      log.debug("################## testCreate() finish ##################");
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//      fail();
//    }
//  }
//
//  /**
//     * testUpdate
//     * @throws Exception
//     */
//    public void testUpdate() throws Exception
//    {
//      log.debug("################## testUpdate() start ##################");
//      try
//      {
//        //create one for linked hearings
//        ExportA local = home.findByPrimaryKey(new Integer(exporta_id));
//
//        log.debug(">>>>>>>>>>>>>> Before update <<<<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>> Updated for <<<<<<<<<<<<<<<");
//        log.debug("exporta_id : " + exporta_id);
//        log.debug(">>>>>>>>>>>>>> Created <<<<<<<<<<<<<<<");
//        log.debug("ExportAId : " + local.getExportAId());
//        log.debug("Hearing id  : " + local.getHearingId());
//        log.debug("linked hearingid : " + local.getLinkedHearingId());
//        log.debug("status flag : " + local.getStatusFlag());
//        log.debug("court clerk : " + local.getCourtClerkExport());
//
//        local.setCourtClerkExport("somebody else to export");
//        local.setStatusFlag("S");
//        local.setLinkedHearingId(new Integer(linked_hearing_id));
//        local.setUpdated();
//
//        log.debug(">>>>>>>>>>>>>> After update <<<<<<<<<<<<<<<");
//        log.debug(">>>>>>>>>>>>>> Updated for <<<<<<<<<<<<<<<");
//        log.debug("exporta_id : " + exporta_id);
//        log.debug(">>>>>>>>>>>>>> Created <<<<<<<<<<<<<<<");
//        log.debug("ExportAId : " + local.getExportAId());
//        log.debug("Hearing id  : " + local.getHearingId());
//        log.debug("linked hearingid : " + local.getLinkedHearingId());
//        log.debug("status flag : " + local.getStatusFlag());
//        log.debug("court clerk : " + local.getCourtClerkExport());
//
//        log.debug("################## testUpdate() finish ##################");
//        assertTrue(true);
//      }
//      catch(Exception e)
//      {
//        e.printStackTrace();
//        fail();
//      }
//  }
//
//
//}