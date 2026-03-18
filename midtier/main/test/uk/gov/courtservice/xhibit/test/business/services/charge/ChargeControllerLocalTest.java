//package uk.gov.courtservice.xhibit.test.business.services.charge;
//
//import junit.framework.TestCase;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.test.TestUtils;
//import uk.gov.courtservice.xhibit.business.entities.defendantonoffence.DefendantOnOffence;
//import uk.gov.courtservice.xhibit.business.entities.defendantonoffence.DefendantOnOffenceMaintainer;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeController;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerHome;
//import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
//
//import javax.ejb.ObjectNotFoundException;
//
///**
// * <p>Title: ChargeControllerLocalTest</p>
// * <p>Description: Using local tests so I can directly call the maintainers
// * to check that the entities have been updated as expected.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Sarah Tong
// * @version 1.0
// */
//public class ChargeControllerLocalTest extends TestCase {
//    private Logger log = CSServices.getLogger(getClass());
//    ChargeController chargeController = (ChargeController) CSServices.getEJBServices()
//            .createLocalSession(ChargeControllerHome.class);
//
//    public ChargeControllerLocalTest(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() throws Exception {
//        TestUtils testUtils = new TestUtils();
//        testUtils.execSql("update xhb_defendant_on_offence set is_stayed = null");
//    }
//
//    /**
//     * Before running test, need to modify input data to ensure the charge exists before it can be deleted.
//     */
//    public void testDeleteCharge() {
//        try {
//            Integer chargeID = new Integer(13);
//            Integer courtID = new Integer(1);
//            Integer caseID = new Integer(18);
//            Integer crestChargeSeqNo = new Integer(1);
//            Integer crestChargeID = new Integer(44269);
//            boolean inCourt = false;
//            DelChargeValue delChargeValue = new DelChargeValue(chargeID, courtID, caseID, crestChargeSeqNo, crestChargeID, inCourt);
//            chargeController.deleteCharge(delChargeValue);
//            //assertTrue(true);
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    public void testUpdateDefendantOnCountStatus() {
//        DefendantOnOffenceBasicValue[] defOnOffenceBasicValues = new DefendantOnOffenceBasicValue[2];
//
//        // Get some DefendantOnOffenceBasicValues
//        DefendantOnOffenceMaintainer dooMaintainer = new DefendantOnOffenceMaintainer();
//        DefendantOnOffence defOnOffence1 = null;
//        DefendantOnOffence defOnOffence2 = null;
//
//        try {
//            defOnOffence1 = dooMaintainer.findByPrimaryKey(new Integer(1));
//            defOnOffence2 = dooMaintainer.findByPrimaryKey(new Integer(23));
//        } catch (ObjectNotFoundException ex) {
//            //fail since the object was not found.
//            ex.printStackTrace();
//            fail();
//        }
//        DefendantOnOffenceBasicValue dooBasicValue1 = dooMaintainer.getDefendantOnOffenceBasicValue(defOnOffence1);
//        DefendantOnOffenceBasicValue dooBasicValue2 = dooMaintainer.getDefendantOnOffenceBasicValue(defOnOffence2);
//        defOnOffenceBasicValues[0] = dooBasicValue1;
//        defOnOffenceBasicValues[1] = dooBasicValue2;
//        String newStatus2 = "Stay";
//
//        // set to stayed
//        // TODO : update next statement in line with the new method definition
//        //chargeController.updateDefendantOnCountStatus(defOnOffenceBasicValues, newStatus2);
//        DefendantOnOffence defOnOffence3 = null;
//        DefendantOnOffence defOnOffence4 = null;
//
//        try {
//            // find the DefendantOnOffence records and check they are stayed
//            defOnOffence3 = dooMaintainer.findByPrimaryKey(new Integer(1));
//            defOnOffence4 = dooMaintainer.findByPrimaryKey(new Integer(23));
//            assertEquals("Y", defOnOffence3.getIsStayed());
//            assertEquals("Y", defOnOffence4.getIsStayed());
//        } catch (ObjectNotFoundException ex) {
//            //fail since the object was not found.
//            ex.printStackTrace();
//            fail();
//        }
//
//    }
//}
//