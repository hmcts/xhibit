//
//package uk.gov.courtservice.xhibit.test.business.services.charge;
//
//import junit.framework.*;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.*;
//import uk.gov.courtservice.xhibit.business.services.charge.DeleteJoinderIndictmentHelper;
//import uk.gov.courtservice.framework.test.TestUtils;
//import java.util.Vector;
//
//public class DeleteJoinderIndictmentHelperTest extends TestCase
//{
//  DeleteJoinderIndictmentHelper delJoinderIndictmentHelper;
//
//  public DeleteJoinderIndictmentHelperTest(String s)
//  {
//    super(s);
//    delJoinderIndictmentHelper = new DeleteJoinderIndictmentHelper();
//  }
//
//  protected void setUp() throws Exception
//  {
//    TestUtils.execSql("insert into xhb_joinder (joinder_id, ref_judge_id) values (1, 953)");
//    TestUtils.execSql("insert into xhb_joinder_charge (joinder_charge_id, joinder_id, charge_id) values (1, 1, 1)");
//    TestUtils.execSql("insert into xhb_joinder_charge (joinder_charge_id, joinder_id, charge_id) values (2, 1, 2)");
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testDeleteCheckJoinderCharge()
//  {
//    // check a non-joinder charge
//    DelChargeValue delChargeVal1 =  new DelChargeValue();
//    delChargeVal1.setChargeID(new Integer(3));
//    delChargeVal1.setDeleteResults(true);
//    delChargeVal1.setInCourt(false);
//
//    DelChargeValue[] delChargeValueRet = delJoinderIndictmentHelper.deleteCheckJoinderCharge(delChargeVal1);
//    assertEquals(1, delChargeValueRet.length);
//    DelChargeValue delChargeValueRet1 = delChargeValueRet[0];
//    assertEquals(3, delChargeValueRet1.getChargeID().intValue());
//
//    // check a joinder charge
//    DelChargeValue delChargeVal2 =  new DelChargeValue();
//    delChargeVal2.setChargeID(new Integer(1));
//    delChargeVal2.setDeleteResults(true);
//    delChargeVal2.setInCourt(false);
//
//    DelChargeValue[] delChargeValueJRet = delJoinderIndictmentHelper.deleteCheckJoinderCharge(delChargeVal2);
//    assertEquals(2, delChargeValueJRet.length);
//    Vector chargeIds = new Vector();
//    DelChargeValue delChargeValueRetJ1 = delChargeValueJRet[0];
//    chargeIds.add(delChargeValueRetJ1.getChargeID());
//    DelChargeValue delChargeValueRetJ2 = delChargeValueJRet[1];
//    chargeIds.add(delChargeValueRetJ2.getChargeID());
//
//    assertTrue(chargeIds.contains(new Integer(1)));
//    assertTrue(chargeIds.contains(new Integer(2)));
//  }
//}
//