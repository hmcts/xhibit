//package uk.gov.courtservice.xhibit.test.business.services.defendant;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseStatusValue;
//import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
//
//public class TestRemoteDefendantControllerBean extends TestCase {
//    DefendantControllerBeanBusinessDelegate delegate = DefendantControllerBeanBusinessDelegate.DelegateFactory.getInstance();
//
//    public TestRemoteDefendantControllerBean(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testGetDefendantDetails() {
//        try {
//            Integer defID = new Integer(1);
//            Integer caseID = new Integer(6);
//            DefendantValue defValue = delegate.getDefendantDetails(defID, caseID);
//            assertNotNull(defValue);
//            System.out.println("defValue = " + defValue);
//            /**@todo Change this to use set up file instead of hardcoded*/
//
//            assertEquals(defValue.getCourtID(), new Integer(1));
//            assertEquals(defValue.getCrestDefendantID(), new Integer(28259));
//            assertEquals(defValue.getDefendantID(), new Integer(1));
//            assertEquals(defValue.getFirstName(), new String("ZMWIVQ"));
//            assertEquals(defValue.getGender(), new Integer(1));
//            assertEquals(defValue.getInitials(), new String("A"));
////      assertEquals(defValue.getIsJuvenile(),new String("N"));
////      assertEquals(defValue.getIsMasked(),new String("N"));
////      assertEquals(defValue.getMaskedName(), null );
//            assertEquals(defValue.getMiddleName(), new String(""));
//            assertEquals(defValue.getSurName(), new String("TFBVILE"));
////      assertEquals(defValue.getVersion(),new Integer(56));
//            /*
//            AddressValue addressValue = defValue.getAddressValue();
//            System.out.println("addressValue = " + addressValue);
//            assertEquals(addressValue.getAddress1(),"A1");
//            assertEquals(addressValue.getAddress2(),"A2");
//            assertEquals(addressValue.getAddress3(),"A3");
//            assertEquals(addressValue.getAddress4(),"A4");
//            assertEquals(addressValue.getAddressID(),new Integer(41));
//            assertEquals(addressValue.getCountry(),"C");
//            assertEquals(addressValue.getCounty(),"C");
//            assertEquals(addressValue.getPostcode(),"P");
//            assertEquals(addressValue.getTown(),"T");
//            */
//
//            // testing update in same test method tut tut...
//
//            defValue.setIsMasked("Y");
//            defValue.setMaskedName("MASKED_NAME");
//
//            System.out.println("*******************************");
//            CaseStatusValue csv = new CaseStatusValue(new Integer(6), true);
//
//            delegate.updateDefendant(defValue, csv);
//            System.out.println("*******************************");
//            // then do another get and see if the values have changed!
//
//            DefendantValue defValueSecond = delegate.getDefendantDetails(defID, caseID);
//
//            System.out.println("defValueSecond = " + defValueSecond);
//            assertEquals(defValueSecond.getIsMasked(), new String("Y"));
//            assertEquals(defValueSecond.getMaskedName(), "MASKED_NAME");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
//