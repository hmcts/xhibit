//package uk.gov.courtservice.business.services.witness.test;
//
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSessionSelector;
//import javax.naming.NamingException;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.8 $
// *
// */
//public class TestWitnessSessionSelector extends AbstractTestClass
//{
//    private WitnessSessionSelector witnessSessionSelector;
//    protected static final int WEEK_NUMBER = 1;
//    protected static final int WITNESSES_IN_WEEK = 13;
//
//
//    public TestWitnessSessionSelector(String s) throws NamingException
//    {
//        super(s);
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        witnessSessionSelector = WitnessFactory.getInstance().getWitnessSessionSelector();
//    }
//
//
//    public void testGetWitnessesForWeek()
//    {
//        System.out.println("Classloader is: "+ getClass().getClassLoader());
//        WitnessSession[] witnessesForWeek =
//                witnessSessionSelector.getWitnessesForWeek(CASE_ID, WEEK_NUMBER);
//      //  assertEquals("Number of witnesses the wrong size",
//             //        WITNESSES_IN_WEEK, witnessesForWeek.length);
//    }
//
//
//    public void testAreWitnessesInWeek()
//    {
//        boolean b = witnessSessionSelector.areWitnessesInWeek(CASE_ID, WEEK_NUMBER);
//        assertTrue("Witnesses should be in the week", b);
//    }
//
//
//    public void testAreWitnessesOnCase()
//    {
//        boolean b = witnessSessionSelector.areWitnessesOnCase(CASE_ID);
//        assertTrue("Witnesses should be on case", b);
//    }
//}
//