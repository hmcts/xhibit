//package uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.test;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.web.publicdisplay.messaging.work.ThreadPool;
//
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestThreadPool extends TestCase
//{
//
//	public TestThreadPool(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//	public void testDoWork()
//		throws Exception
//	{
//		CSServices.getConfigServices();
//		ThreadPool threadPool = new ThreadPool(10);
//		assertEquals(10, threadPool.getNumFreeWorkers());
//
//		TestWork[] testWorks = new TestWork[10];
//		for(int i = 0;i < testWorks.length;i++)
//		{
//			testWorks[i] = new TestWork();
//			assertEquals(10 - i, threadPool.getNumFreeWorkers());
//			threadPool.scheduleWork(testWorks[i]);
//		}
//
//		threadPool.shutdown();
//		assertEquals(0, threadPool.getNumFreeWorkers());
//	}
//
//	private static class TestWork implements Runnable
//	{
//		private static int count;
//
//		private TestWork()
//		{
//			count++;
//		}
//
//		public void run()
//		{
//			while(count != 10)
//			{
//			}
//		}
//	}
//
//}
//