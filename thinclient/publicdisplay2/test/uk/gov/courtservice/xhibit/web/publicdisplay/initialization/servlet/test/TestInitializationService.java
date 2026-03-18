//package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.test;
//
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.InitializationService;
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestInitializationService extends TestCase
//{
//
//	private static final long JOIN_TIMEOUT = 300L;
//
//	private InitializationService service = InitializationService.getInstance();
//
//	public TestInitializationService(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//
//	public void testInitialize()
//		throws Exception
//	{
//		service.initialize();
//		boolean initialized = false;
//		for(int i = 0;i < 6;i++)
//		{
//			Thread.sleep(2*1000l);
//			initialized = service.isInitialized();
//			if(initialized) break;
//		}
//		assertTrue(initialized);
//	}
//
//	public void testDestroy()
//		throws Exception
//	{
//		service.destroy();
//	}
//
//}
//