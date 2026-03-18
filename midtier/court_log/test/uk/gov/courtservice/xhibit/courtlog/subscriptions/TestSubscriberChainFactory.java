///*
// * Created on 17-Feb-2004
// *
// * To change the template for this generated file go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//package uk.gov.courtservice.xhibit.courtlog.subscriptions;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestSubscriberChainFactory extends CourtLogTestCase
//{
//
//    /**
//     * Constructor for TestSubscriberChainFactory.
//     * @param arg0
//     */
//    public TestSubscriberChainFactory(String name)
//    {
//        super(name);
//		CSServices.getConfigServices();
//    }
//
//    public void testGetSubscriberChain() throws Exception
//    {
//		SubscriberChainFactory factory = SubscriberChainFactory.getInstance(TEST_CONFIG);
//    	factory.getSubscriberChain(
//    		new CourtLogCRUDValue()).processCreate();
//		factory.getSubscriberChain(
//			new CourtLogCRUDValue()).processDelete();
//		factory.getSubscriberChain(
//			new CourtLogCRUDValue()).processUpdate();
//    }
//
//}
//