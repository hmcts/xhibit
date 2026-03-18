//package uk.gov.courtservice.business.services.witness.test;
//
//import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
//import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
//import javax.naming.NamingException;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.4 $
// *
// */
//
//public class TestWitnessFactory extends AbstractTestClass
//{
//
//    public void testGetWitnessDetail()
//    {
//        try
//        {
//            WitnessFactory.getInstance().getWitnessDetail(WITNESS_ID);
//        }
//        catch (WitnessNotFoundException e)
//        {
//            handleException(e);
//        }
//    }
//
//
//    public void testGetWitnessDetailSelector()
//    {
//        try
//        {
//            WitnessFactory.getInstance().getWitnessDetailSelector();
//
//        }
//        catch (Exception e)
//        {
//            handleException(e);
//        }
//    }
//
//
//    public void testGetWitnessSummarySelector()
//    {
//        try
//        {
//            WitnessFactory.getInstance().getWitnessSummarySelector();
//
//        }
//        catch (Exception e)
//        {
//            handleException(e);
//        }
//    }
//
//
//    public void testGetWitnessSessionSelector()
//    {
//        try
//        {
//            WitnessFactory.getInstance().getWitnessSessionSelector();
//
//        }
//        catch (Exception e)
//        {
//            handleException(e);
//        }
//    }
//
//
//    public TestWitnessFactory(String s) throws NamingException
//    {
//        super(s);
//    }
//}
//