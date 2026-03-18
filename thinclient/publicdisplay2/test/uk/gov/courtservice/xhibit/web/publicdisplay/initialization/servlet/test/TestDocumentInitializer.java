//package uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.test;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.web.publicdisplay.configuration.DisplayConfigurationReader;
//import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.DocumentInitializer;
///**
// * @author pznwc5
// *
// * To change the template for this generated type comment go to
// * Window - Preferences - Java - Code Generation - Code and Comments
// */
//public class TestDocumentInitializer extends TestCase
//{
//
//	private static final long JOIN_TIMEOUT = 300L;
//
//	private DocumentInitializer initializer;
//
//	public TestDocumentInitializer(String s)
//		throws Exception
//	{
//		super(s);
//	}
//
//	public void setUp()
//	{
//		int courtIds[] = DisplayConfigurationReader.getInstance().getConfiguredCourtIds();
//		initializer = new DocumentInitializer(courtIds, 1, 0);
//	}
//
//
//	public void testInitialize()
//		throws Exception
//	{
//		initializer.initialize();
///**		boolean initialized = false;
//		for(int i = 0;i < 6;i++)
//		{
//			Thread.sleep(2*1000l);
//			initialized = service.isInitialized();
//			if(initialized) break;
//		}
//		assertTrue(initialized);**/
//	}
//
//}
//