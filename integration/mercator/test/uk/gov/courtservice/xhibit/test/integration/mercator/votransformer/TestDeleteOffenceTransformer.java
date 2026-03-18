//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.DeleteOffenceTransformer;
//
//import java.util.Vector;
//
//
//
//public class TestDeleteOffenceTransformer extends TestCase
//{
//
//  public TestDeleteOffenceTransformer(String s)
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
// // public void testTransformOutput() {
//  //  DeleteOffenceTransformer deleteoffencetransformer = new DeleteOffenceTransformer();
// //   Object result1=  null  /** @todo fill in non-null value */;
//  //  Object objectRet = deleteoffencetransformer.transformOutput(result1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
// // }
//
//
//  public void testTransformVO() {
//    DeleteOffenceTransformer deleteoffencetransformer = new DeleteOffenceTransformer();
//
//    DelOffenceValue delOff = new DelOffenceValue();
//
//    Vector v = new Vector();
//    v.addElement(new Integer(111));
//    v.addElement(new Integer(222));
//    v.addElement(new Integer(444));
//    v.addElement(new Integer(666));
//
//    delOff.setCaseID(new Integer(122));
//    delOff.setChargeID(new Integer(222));
//    delOff.setCourtID(new Integer(322));
//    delOff.setDefendantIDs(v);
//    delOff.setOffenceID(new Integer(99));
//    delOff.setDeleteResults(true);
//    delOff.setInCourt(true);
//    delOff.setUpdateCount(8);
//
//    DelOffenceValue delOff2 = new DelOffenceValue();
//    delOff2.setCaseID(null);
//    delOff2.setChargeID(null);
//    delOff2.setCourtID(null);
//    delOff2.setDefendantIDs(null);
//    delOff2.setOffenceID(null);
//    delOff2.setDeleteResults(false);
//    delOff2.setInCourt(false);
//    delOff2.setUpdateCount(-1);
//
//    CSValueObject valueObject1=  delOff  /** @todo fill in non-null value */;
//    CSValueObject csvalueobjectRet = (CSValueObject)deleteoffencetransformer.transformVO(valueObject1);
//    System.out.println("**** RESULT 1*****");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  delOff2  /** @todo fill in non-null value */;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)deleteoffencetransformer.transformVO(valueObject2);
//    System.out.println("**** RESULT 2*****");
//    System.out.println(csvalueobjectRet2.toString());
//
//    AddressValue address = new AddressValue();
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  address;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)deleteoffencetransformer.transformVO(valueObject3);
//      System.out.println(csvalueobjectRet3.toString());
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//
//      DelChargeValue delNull = null;
//      System.out.println("*********** RESULT 4 *********** ");
//    try
//    {
//        CSValueObject valueObject4=  delNull;
//        CSValueObject csvalueobjectRet4 = (CSValueObject)deleteoffencetransformer.transformVO(valueObject4);
//        if(csvalueobjectRet4 != null)
//        {
//          System.out.println(csvalueobjectRet4.toString());
//        }else{
//          System.out.println("object is null");
//        }
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//  }
//}
//