//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.SignIndTransformer;
//
//import java.util.Calendar;
//
//
//
//public class TestSignIndTransformer extends TestCase
//{
//
//  public TestSignIndTransformer(String s)
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
//  public void testTransformVO() {
//    SignIndTransformer signindtransformer = new SignIndTransformer();
//
//    Calendar cal = Calendar.getInstance();
//
//    SignIndValue signIndValue = new SignIndValue();
//    signIndValue.setCaseID(new Integer(122));
//    signIndValue.setChargeID(new Integer(222));
//    signIndValue.setCourtID(new Integer(322));
//    signIndValue.setInCourt(true);
//    signIndValue.setIndSignedDate(cal);
//    signIndValue.setNumberOfDays(new Integer(88));
//    signIndValue.setSignOutOfTime(true);
//    signIndValue.setUpdateCount(9);
//
//    SignIndValue signIndValue2 = new SignIndValue();
//    signIndValue2.setCaseID(null);
//    signIndValue2.setChargeID(null);
//    signIndValue2.setCourtID(null);
//    signIndValue2.setInCourt(false);
//    signIndValue2.setIndSignedDate(null);
//    signIndValue2.setNumberOfDays(null);
//    signIndValue2.setSignOutOfTime(false);
//    signIndValue2.setUpdateCount(-1);
//
//    CSValueObject valueObject1=  signIndValue  /** @todo fill in non-null value */;
//    CSValueObject csvalueobjectRet = (CSValueObject)signindtransformer.transformVO(valueObject1);
//    System.out.println("**** RESULT 1*****");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  signIndValue2  /** @todo fill in non-null value */;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)signindtransformer.transformVO(valueObject2);
//    System.out.println("**** RESULT 2*****");
//    System.out.println(csvalueobjectRet2.toString());
//
//    AddressValue address = new AddressValue();
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  address;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)signindtransformer.transformVO(valueObject3);
//      System.out.println(csvalueobjectRet3.toString());
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//
//    SignIndValue signNull = null;
//    System.out.println("*********** RESULT 4 *********** ");
//    try
//    {
//      CSValueObject valueObject4=  signNull;
//      CSValueObject csvalueobjectRet4 = (CSValueObject)signindtransformer.transformVO(valueObject4);
//      if(csvalueobjectRet4 != null)
//      {
//        System.out.println(csvalueobjectRet4.toString());
//      }else{
//        System.out.println("object is null");
//      }
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//  }
// // public void testTransformOutput() {
// //   SignIndTransformer signindtransformer = new SignIndTransformer();
//  //  Object result1=  null  /** @todo fill in non-null value */;
// //   Object objectRet = signindtransformer.transformOutput(result1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
// // }
//}
//