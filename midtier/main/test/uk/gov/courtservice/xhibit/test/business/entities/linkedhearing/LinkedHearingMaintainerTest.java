//
//package uk.gov.courtservice.xhibit.test.business.entities.linkedhearing;
//
//// 3RD PARTY
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.linkedhearing.LinkedHearing;
//import uk.gov.courtservice.xhibit.business.entities.linkedhearing.LinkedHearingMaintainer;
//import uk.gov.courtservice.xhibit.business.vos.entities.LinkedHearingBasicValue;
//
//
///**
// * <p>Title: LinkedHearingMaintainerTest</p>
// * <p>Description: Test the linked hearing maintainer</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//public class LinkedHearingMaintainerTest extends TestCase
//{
//
//  private static Logger log  = CSServices.getLogger(LinkedHearingMaintainerTest.class);
//
//  public LinkedHearingMaintainerTest(String s) {
//    super(s);
//  }
//
//  protected void setUp() {
//  }
//
//  protected void tearDown() {
//  }
//
//  /**
//   * Test to create
//   */
//  public void testCreate() {
//    log.debug(">>>>>>>>>>>> testCreate Starts <<<<<<<<<<<<<");
//    try
//    {
//      LinkedHearingMaintainer linkedhearingmaintainer = new LinkedHearingMaintainer();
//      LinkedHearingBasicValue value = new LinkedHearingBasicValue();
//      LinkedHearing entity = (LinkedHearing)linkedhearingmaintainer.create(value);
//      log.debug("Newly created linkedhearing id: " + entity.getLinkedHearingId());
//      assertNotNull(entity);
//    }
//    catch (Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testCreate Ends <<<<<<<<<<<<<");
//    }
//  }
//
//
//  /**
//   * delete
//   */
//  public void testDelete()
//  {
//    log.debug(">>>>>>>>>>>> testDelete Starts <<<<<<<<<<<<<");
//    try {
//      LinkedHearingMaintainer linkedhearingmaintainer = new LinkedHearingMaintainer();
//      LinkedHearingBasicValue value = new LinkedHearingBasicValue();
//      LinkedHearing entity = (LinkedHearing)linkedhearingmaintainer.create(value);
//      log.debug("Newly created linkedhearing id: " + entity.getLinkedHearingId());
//
//      log.debug("will try to delete linkedhearing id and version: " + entity.getLinkedHearingId()+", "+entity.getVersion());
//      linkedhearingmaintainer.delete(entity.getLinkedHearingId(), entity.getVersion());
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testDelete Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * find by primary key
//   */
//  public void testFindByPrimaryKey()
//  {
//    log.debug(">>>>>>>>>>>> testFindByPrimaryKey Starts <<<<<<<<<<<<<");
//    try
//    {
//      LinkedHearingMaintainer linkedhearingmaintainer = new LinkedHearingMaintainer();
//      LinkedHearingBasicValue value = new LinkedHearingBasicValue();
//      LinkedHearing entity = (LinkedHearing)linkedhearingmaintainer.create(value);
//      log.debug("Newly created that will be find by pk: " + entity.getLinkedHearingId());
//
//      LinkedHearing entity2 = (LinkedHearing)linkedhearingmaintainer.findByPrimaryKey(entity.getLinkedHearingId());
//      log.debug("Found by pk: " + entity2.getLinkedHearingId());
//      assertEquals(entity.getLinkedHearingId(), entity2.getLinkedHearingId());
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testFindByPrimaryKey Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * get a basicvalue
//   */
//  public void testGetLinkedHearingBasicValue()
//  {
//    log.debug(">>>>>>>>>>>> testGetLinkedHearingBasicValue Starts <<<<<<<<<<<<<");
//    LinkedHearingMaintainer maintainer = new LinkedHearingMaintainer();
//    try
//    {
//      LinkedHearingBasicValue actual = new LinkedHearingBasicValue();
//      LinkedHearing entity = (LinkedHearing)maintainer.create(actual);
//
//      Integer id = entity.getLinkedHearingId();
//
//      LinkedHearingBasicValue expected = maintainer.getLinkedSHBasicValue(entity);
//
//      log.debug("hearinglinked id: " + id +", expected : " + expected.getId());
//      assertEquals(id, expected.getId());
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testGetLinkedHearingBasicValue Ends <<<<<<<<<<<<<");
//    }
//  }
//
//  /**
//   * update
//   */
//  public void testUpdate()
//  {
//   log.debug(">>>>>>>>>>>> testUpdate Starts <<<<<<<<<<<<<");
//    try
//    {
//      LinkedHearingMaintainer linkedhearingmaintainer = new LinkedHearingMaintainer();
//      LinkedHearingBasicValue value = new LinkedHearingBasicValue();
//      LinkedHearing entity = (LinkedHearing)linkedhearingmaintainer.create(value);
//      log.debug("Newly created that will be updated: " + entity.getLinkedHearingId());
//
//      LinkedHearingBasicValue value2 = linkedhearingmaintainer.getLinkedSHBasicValue(entity);
//      linkedhearingmaintainer.update(value2);
//      log.debug("Newly updated: " + value2.getId());
//      assertTrue(true);
//    }
//    catch(Exception e)
//    {
//      log.debug(e.toString());
//      e.printStackTrace();
//      fail();
//    }
//    finally
//    {
//      log.debug(">>>>>>>>>>>> testUpdate Ends <<<<<<<<<<<<<");
//    }
//  }
//}
//