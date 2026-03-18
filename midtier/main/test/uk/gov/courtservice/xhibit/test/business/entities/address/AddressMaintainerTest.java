//
//package uk.gov.courtservice.xhibit.test.business.entities.address;
//
//import junit.framework.*;
//
//import uk.gov.courtservice.xhibit.business.entities.address.Address;
//import uk.gov.courtservice.xhibit.business.entities.address.AddressMaintainer;
//
//
///**
// *
// * <p>Title: AddressMaintainerTest</p>
// * <p>Description: Test Class for Address Maintainer</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Joseph Babad
// * @version $Id: AddressMaintainerTest.java,v 1.2 2006/07/13 12:58:01 xzfdtb Exp $
// */
///**@todo Add test cases for other maintainer methods. */
//public class AddressMaintainerTest extends TestCase {
//
//  public AddressMaintainerTest(String s) {
//    super(s);
//  }
//
//  protected void setUp() {
//  }
//
//  protected void tearDown() {
//  }
//
//  public void testFindByPK() {
//    AddressMaintainer addressmaintainer = new AddressMaintainer();
//    Integer id1=  new Integer(2730)  /** @todo fill in non-null value */;
//    try {
//      Address addressRet = addressmaintainer.findByPK(id1);
//      assertEquals( id1 , addressRet.getAddressId() );
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//
//  public void testFindByNonExistentPK() {
//    AddressMaintainer addressmaintainer = new AddressMaintainer();
//    Integer id1=  new Integer(2)  /* Non-existent in the test data*/;
//    try {
//      Address addressRet = addressmaintainer.findByPK(id1);
//      assertEquals( id1 , addressRet.getAddressId() );
//      fail("Should not have found this");
//    }
//    catch(Exception e) {
//        assertTrue(e.toString(), true );
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//}
//