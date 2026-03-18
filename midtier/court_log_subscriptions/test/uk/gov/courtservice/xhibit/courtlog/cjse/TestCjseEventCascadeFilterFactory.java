//package uk.gov.courtservice.xhibit.courtlog.cjse;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//
//import junit.framework.TestCase;
//
///**
// * <p>Title: TestCjseEventCascadeFilterFactory</p>
// * <p>Description: Test the parsing of the cascade.xml file. Consequently tests
// * both <code>TestCjseEventCascadeFilterFactory</code> and
// * <code>TestCjseEventCascadeHandler</code> classes</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author Sarah Tong
// * @version $Id: TestCjseEventCascadeFilterFactory.java,v 1.3 2006/07/13 12:58:00 xzfdtb Exp $
// */
//public class TestCjseEventCascadeFilterFactory extends TestCase
//{
//
//  public TestCjseEventCascadeFilterFactory(String s)
//  {
//    super(s);
//  }
//
//  protected void setUp()
//  {
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  /**
//   * Tests the parsing of the cascade.xml file
//   */
//  public void testGetEventCascadeFilters()
//  {
//    Map mapRet = CjseEventCascadeFilterFactory.getEventCascadeFilters();
//    assertEquals("Did not retrieve the correct number of cascades from the xml",
//                 2, mapRet.size());
//    assertNotNull("Did not find cascade for event 40701",
//                  mapRet.get(new Integer(40701)));
//
//    // check the cascade (sub events) for event 40701
//    Integer[] event40701 = (Integer[])mapRet.get(new Integer(40701));
//    assertEquals("Incorrect number of sub events for event 40701",
//                 3, event40701.length);
//    List subevents40701 = Arrays.asList(event40701);
//    assertTrue("Sub event 407011 not retrived for event 40701",
//               subevents40701.contains(new Integer(407011)));
//    assertTrue("Sub event 407012 not retrived for event 40701",
//               subevents40701.contains(new Integer(407012)));
//    assertTrue("Sub event 407013 not retrived for event 40701",
//               subevents40701.contains(new Integer(407013)));
//
//    // check the cascade (sub events) for event 40702
//    Integer[] event40702 = (Integer[])mapRet.get(new Integer(40702));
//    assertEquals("Incorrect number of sub events for event 40702",
//                 2, event40702.length);
//    List subevents40702 = Arrays.asList(event40702);
//    assertTrue("Sub event 407021 not retrived for event 40702",
//               subevents40702.contains(new Integer(407021)));
//    assertTrue("Sub event 407022 not retrived for event 40702",
//               subevents40702.contains(new Integer(407022)));
//  }
//}
//