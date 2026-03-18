//package uk.gov.courtservice.business.services.witness.test;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessDetail;
//
//public class TestWitnessSelector extends AbstractTestClass
//{
//    public TestWitnessSelector(String s) throws NamingException
//    {
//        super(s);
//    }
//
//    public void testGetAllWitnessDetails() throws Exception
//    {
//        WitnessDetail[] allWitnessesDetails = WitnessFactory.getInstance()
//                .getWitnessDetailSelector().getAllWitnessDetails(CASE_ID);
//        assertNotNull("Error - there are witnesses on the case", allWitnessesDetails);
//        for (int i = 0; i < allWitnessesDetails.length; i++)
//        {
//            WitnessDetail witnessesDetail = allWitnessesDetails[i];
//            log.debug("Witness Detail " + i + " : " + witnessesDetail);
//            assertNotNull("Error - Witness Id is null", witnessesDetail.getId());
//            assertNotNull("Error - Expected time of arrival is null", witnessesDetail.getExpected());
//            assertNotNull("Error - Witness type is null", witnessesDetail.getType());
//        }
//    }
//}