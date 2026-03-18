///*
// * Created on 17-Feb-2004
// *
// * To change the template for this generated file go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//package uk.gov.courtservice.xhibit.courtlog.subscriptions;
//
//import java.util.List;
//
//import uk.gov.courtservice.framework.services.CSServices;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestSubscriptionDom extends CourtLogTestCase
//{
//
//	public TestSubscriptionDom(String name)
//	{
//		super(name);
//		CSServices.getConfigServices();
//	}
//
//    public void testGetInstance()
//    {
//    	SubscriptionDom dom = new SubscriptionDom(TEST_CONFIG);
//    	assertNotNull(dom);
//    }
//
//    public void testGetSubscribers()
//    {
//    	SubscriptionDom dom = new SubscriptionDom(TEST_CONFIG);
//    	List subscribers = dom.getSubscribers(
//    		new String[] {"Test_Category_1", "Test_Category_2"},
//			"Test_Event");
//		assertEquals(4, subscribers.size());
//    }
//
//}
//