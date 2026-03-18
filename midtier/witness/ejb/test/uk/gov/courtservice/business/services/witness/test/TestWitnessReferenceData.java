//package uk.gov.courtservice.business.services.witness.test;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.reference.WitnessReferenceDataFactory;
//import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;
//
//public class TestWitnessReferenceData extends TestCase
//{
//    private Logger log = CSServices.getLogger(TestWitnessReferenceData.class);
//    private WitnessReferenceData referenceData;
//
//    public TestWitnessReferenceData(String s)
//    {
//        super(s);
//    }
//
//    public void setUp() throws Exception
//    {
//        referenceData = WitnessReferenceDataFactory.getWitnessReferenceData();
//    }
//
//    public void testGetPagerNetworks()
//    {
//        String[] pagerNetworks = referenceData.getPagerNetworks();
//        for (int i = 0; i < pagerNetworks.length; i++)
//        {
//            String pagerNetwork = pagerNetworks[i];
//            log.debug(pagerNetwork);
//        }
//    }
//
//    public void testGetTrialSessionTypes()
//    {
//        String[] trialSessionTypes = referenceData.getTrialSessionTypes();
//        for (int i = 0; i < trialSessionTypes.length; i++)
//        {
//            String trialSessionType = trialSessionTypes[i];
//            log.debug(trialSessionType);
//        }
//    }
//
//    public void testGetWitnessStatuses()
//    {
//        String[] witnessStatuses = referenceData.getWitnessStatuses();
//        for (int i = 0; i < witnessStatuses.length; i++)
//        {
//            String witnessStatus = witnessStatuses[i];
//            log.debug(witnessStatus);
//        }
//    }
//
//    public void testGetWitnessTypes()
//    {
//        String[] witnessTypes = referenceData.getWitnessTypes();
//        for (int i = 0; i < witnessTypes.length; i++)
//        {
//            String witnessType = witnessTypes[i];
//            log.debug(witnessType);
//        }
//    }
//}
//