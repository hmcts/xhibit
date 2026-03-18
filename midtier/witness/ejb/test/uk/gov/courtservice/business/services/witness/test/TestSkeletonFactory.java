//package uk.gov.courtservice.business.services.witness.test;
//
//import junit.framework.Assert;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.SkeletonSchedule;
//import uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome;
//import javax.naming.NamingException;
//
//
//public class TestSkeletonFactory extends AbstractTestClass
//{
//    private uk.gov.courtservice.xhibit.business.services.witness.SkeletonControllerHome home;
//    private uk.gov.courtservice.xhibit.business.services.witness.SkeletonController instance;
//    private SkeletonSchedule skeletonSchedule;
//    private WitnessReferenceData referenceData;
//
//
//    public TestSkeletonFactory(String s) throws NamingException
//    {
//        super(s);
//
//        home = (SkeletonControllerHome)
//               CSServices.getServiceLocator().getRemoteHome(SkeletonControllerHome.class);
//    }
//
//
//    public void testThing()
//    {
//        try
//        {
//
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//            Assert.fail(e.getMessage());
//        }
//
//    }
//
//
//}
//