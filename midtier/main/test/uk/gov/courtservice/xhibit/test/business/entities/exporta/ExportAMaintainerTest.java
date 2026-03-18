//
//package uk.gov.courtservice.xhibit.test.business.entities.exporta;
//
//import java.util.ArrayList;
//
//import javax.ejb.ObjectNotFoundException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.exporta.ExportA;
//import uk.gov.courtservice.xhibit.business.entities.exporta.ExportAMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.ExportAValue;
//
///**
// *
// * <p>Title: ExportAMaintainerTest</p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//
//public class ExportAMaintainerTest extends TestCase {
//
//  private static Logger log  = CSServices.getLogger(ExportAMaintainerTest.class);
//  private Integer setUpPK;
//  private Integer setUpVersion;
//  private ExportA localEntity;
//  private String courtClerk1 = "Marie Holmberg - exporter pro1";
//  private String courtClerk2 = "Marie Holmberg - exporter pro2";
//  private Integer hearingID = new Integer(5);
//  private Integer linkedHearingID = new Integer(1);
//  private String statusFlag = "R";
//
//
//  public ExportAMaintainerTest(String s) {
//    super(s);
//  }
//
//
//  /**
//   * Set up!
//   */
//  protected void setUp()
//  {
//  }
//
//  /**
//   * tearDown
//   */
//  protected void tearDown()
//  {
//  }
//
//  /**
//   * testCreate
//   */
//  public void testCreate() {
//    log.debug("~~~~~~~~~~~~~~~~ testCreate called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//
//    log.debug("~~~~~~~~~~~~~~~~ testCreate finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//  public void testUpdate()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ testUpdate called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    ExportAValue value = maintainer.getExportAValue(exportA);
//    value.setCourtClerkName("Somebody else");
//    value.setStatusFlag("S");
//
//    log.debug("before update");
//    maintainer.update(value);
//
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//
//    log.debug("~~~~~~~~~~~~~~~~ testUpdate finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//  /**
//   * testDelete
//   */
//  public void testDelete()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ testDelete called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to delete");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//
//    log.debug("~~~~~~~~~~~~~~~~ testDelete finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//
//  /**
//   * testFindByPK
//   */
//  public void testFindByPK()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ testFindByPK called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to testFindByPK");
//    try
//    {
//      maintainer.findByPrimaryKey(exportA.getExportAId());
//    }
//    catch (ObjectNotFoundException e)
//    {
//      // did not find the entity so test fails...
//      e.printStackTrace();
//      fail();
//    }
//
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//    log.debug("~~~~~~~~~~~~~~~~ testFindByPK finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//
//  /**
//   * findByKeyAndVersion
//   */
//  public void testfindByKeyAndVersion()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ findByKeyAndVersion called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to findByKeyAndVersion");
//    try
//    {
//      maintainer.findByKeyAndVersion(exportA.getExportAId(), exportA.getVersion());
//    }
//    catch (ObjectNotFoundException e)
//    {
//      // did not find the entity so test fails...
//      e.printStackTrace();
//      fail();
//    }
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//    log.debug("~~~~~~~~~~~~~~~~ findByKeyAndVersion finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//
//  /**
//   * testFindByHearingOrLinkedHearingID1
//   * find with hearing id
//   */
//  public void testFindByHearingOrLinkedHearingID1()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ testFindByHearingOrLinkedHearingID1 called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportA());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    log.debug("getExportAId :" + exportA.getExportAId());
//    log.debug("getHearingId :" + exportA.getHearingId());
//    log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to findByHearingOrLinkedHearingID");
//    try
//    {
//      ArrayList founds = (ArrayList)maintainer.findByHearingOrLinkedHearingID(exportA.getHearingId(), exportA.getLinkedHearingId());
//
//     for(int i = 0; i < founds.size(); i++)
//     {
//       ExportA exporta = (ExportA)founds.get(i);
//
//       log.debug("-------ExportAID : " + exporta.getExportAId());
//       log.debug("-------HearingID : " + exporta.getHearingId());
//       log.debug("-------LinkedHearingID : " + exporta.getLinkedHearingId());
//       log.debug("-------Court clerk : " + exporta.getCourtClerkExport());
//       log.debug("-------Status : " + exporta.getStatusFlag());
//      }
//
//    }
//    catch (ObjectNotFoundException e)
//    {
//      // did not find the entity so test fails...
//      e.printStackTrace();
//      fail();
//    }
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//    log.debug("~~~~~~~~~~~~~~~~ testFindByHearingOrLinkedHearingID1 finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//
//  /**
//   * testFindByHearingOrLinkedHearingID2
//   * find with linked hearing id
//   */
//  public void testFindByHearingOrLinkedHearingID2()
//  {
//    log.debug("~~~~~~~~~~~~~~~~ testFindByHearingOrLinkedHearingID2 called ~~~~~~~~~~~~~~~~~~");
//    log.debug("get the maintainer");
//    ExportAMaintainer maintainer = new ExportAMaintainer();
//    CSEntityLocal local = maintainer.create(this.getExportALinkedHearing());
//    log.debug("has now created it");
//    ExportA exportA = (ExportA)local;
//
//    //log.debug("getExportAId :" + exportA.getExportAId());
//    //log.debug("getHearingId :" + exportA.getHearingId());
//    //log.debug("getLinkedHearingId :" + exportA.getLinkedHearingId());
//    //log.debug("getStatusFlag :" + exportA.getStatusFlag());
//
//    log.debug("try to findByHearingOrLinkedHearingID");
//    try
//    {
//      ArrayList founds = (ArrayList)maintainer.findByHearingOrLinkedHearingID(exportA.getHearingId(), exportA.getLinkedHearingId());
//
//      for(int i = 0; i < founds.size(); i++)
//      {
//        ExportA exporta = (ExportA)founds.get(i);
//
//        log.debug("-------ExportAID : " + exporta.getExportAId());
//        log.debug("-------HearingID : " + exporta.getHearingId());
//        log.debug("-------LinkedHearingID : " + exporta.getLinkedHearingId());
//        log.debug("-------Court clerk : " + exporta.getCourtClerkExport());
//        log.debug("-------Status : " + exporta.getStatusFlag());
//      }
//
//    }
//    catch (ObjectNotFoundException e)
//    {
//      // did not find the entity so test fails...
//      e.printStackTrace();
//      fail();
//    }
//    log.debug("try to delete/clean up");
//    maintainer.delete(exportA.getExportAId(), exportA.getVersion());
//    log.debug("~~~~~~~~~~~~~~~~ testFindByHearingOrLinkedHearingID2 finish ~~~~~~~~~~~~~~~~~~");
//    assertTrue(true);
//  }
//
//  /**
//   * getExportA value with hearing id set.
//   * @return ExportAValue
//   */
//  private ExportAValue getExportA()
//  {
//    ExportAValue value = new ExportAValue();
//    value.setCourtClerkName("Marie");
//    value.setHearingID(new Integer(1));
//    value.setLinkedHearingID(null);
//    value.setStatusFlag("R");
//    return value;
//  }
//
//  /**
//     * getExportA
//     * @return ExportAValue
//     */
//    private ExportAValue getExportALinkedHearing()
//    {
//      ExportAValue value = new ExportAValue();
//      value.setCourtClerkName("Marie");
//      value.setHearingID(null);
//      value.setLinkedHearingID(new Integer(1));
//      value.setStatusFlag("R");
//      return value;
//  }
//
//  /**
//   * Creates an ExportA entity
//   * @return
//   *
//  private ExportAValue createExportA()
//  {
//    log.debug("createExportA");
//    log.debug("#############################################");
//    ExportAValue value = new ExportAValue();
//    value.setCourtClerkName(courtClerk1);
//    value.setHearingID(hearingID);
//    value.setLinkedHearingID(null);
//    value.setStatusFlag(statusFlag);
//    log.debug("value.toString() : " + value.toString());
//    return value;
//  }*/
//}
//