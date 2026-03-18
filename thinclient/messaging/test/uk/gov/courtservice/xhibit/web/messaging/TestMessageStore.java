//package uk.gov.courtservice.xhibit.web.messaging;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * 
// * Empty test created for setting up JUnitEE 
// */
//public class TestMessageStore extends TestCase
//{
//  
//  // variables for testing
//  private MessageStore store = null;
//  
//  
//  /**
//   * Create new test class
//   * @param testName  will be passed in by JUnit runner
//   */
//  public TestMessageStore(String testName)
//  {
//      super(testName);
//  }
//  
//  
//  /**
//   * Have JUnit pick up all tests in this class
//   */
//  public static Test suite()
//  {
//      return new TestSuite(TestMessageStore.class);
//  }
//  
//  
//  /**
//   * Initialise variables common to each test. Re-run before each test.
//   */
//  protected void setUp()
//  {
//      store = new MessageStore();
//  }
//
//
//  /**
//   * Cleanup variables used by tests. Re-run after each test.
//   */
//  protected void tearDown()
//  {
//      store = null;
//  }
//
//
//  /**
//   * example test methods
//   */
//  public void testHasNewMessages()
//  {
//      // don't expect a new store to have messages ??
//      // (i don't know JMS .. perhaps it'll be given some on creation?)
//      boolean hasMessages = store.hasNewMessages();
//      assertEquals(false, hasMessages);
//  }
//  
//  public void testOnMessage() throws Exception
//  {
//      fail("method does not test anything");
//  }
//  
//}
//
//
//