//
//package uk.gov.courtservice.xhibit.test.integration.mercator;
//
//import junit.framework.*;
//import uk.gov.courtservice.xhibit.integration.mercator.MercatorWrapper;
//
//
///**
// *
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2002</p>
// * <p>Company: EDS</p>
// * @author Cag Onganer
// * @version 1.0
// */
//
//public class TestMercatorWrapper extends TestCase
//{
//  MercatorWrapper mw;
//  TestValueObject vObject1;
//
//  public TestMercatorWrapper(String s)
//  {
//    super(s);
//  }
//  private class TestValueObject{
//    int id;
//    String name;
//  }
//
//  protected void setUp()
//  {
//    vObject1 = new TestValueObject();
//    vObject1.id = 1;
//    vObject1.name = "Cag Onganer";
//  }
//
//  protected void tearDown()
//  {
//  }
//
//  public void testRunMap()
//  {
//    String logicalMapName = "addOffence";
//    TestValueObject vObject = null;
//
//    try
//    {
//      mw = new MercatorWrapper(true);
//      mw.runMap(logicalMapName,vObject1);
//    }
//    //catch (CSResourceUnavailableException ex)
//    catch (Exception ex)
//    {
//    }
//
//   //assertEquals("Value Object ID = ",vObject1.id,vObject.id);
//   //assertEquals("Value Object Name = ",vObject1.name,vObject.name);
//  }
//
//  public void testGetMapName()
//  {
//  }
//
//  public void examineReturnValue(int MapOverride[])
//  {
//  }
//
//}
//