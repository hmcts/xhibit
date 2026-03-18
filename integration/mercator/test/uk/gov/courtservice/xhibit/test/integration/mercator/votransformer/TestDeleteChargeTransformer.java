//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.DeleteChargeTransformer;
//
//
//
//public class TestDeleteChargeTransformer extends TestCase
//{
//
//  public TestDeleteChargeTransformer(String s)
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
//    DeleteChargeTransformer deletechargetransformer = new DeleteChargeTransformer();
//
//    DelChargeValue delcharge = new DelChargeValue(new Integer(11),
//        new Integer(22),new Integer(33),new Integer(44),new Integer(55),true);
//
//    delcharge.setCaseID(new Integer(122));
//    delcharge.setChargeID(new Integer(222));
//    delcharge.setCourtID(new Integer(322));
//    delcharge.setCrestChargeID(new Integer(422));
//    delcharge.setCrestChargeSeqNo(new Integer(522));
//    delcharge.setDeleteResults(true);
//    delcharge.setInCourt(true);
//    delcharge.setUpdateCount(8);
//
//
//    DelChargeValue delcharge2 = new DelChargeValue(null,
//       null,null,null,null,false);
//   delcharge2.setCaseID(null);
//   delcharge2.setChargeID(null);
//   delcharge2.setCourtID(null);
//   delcharge2.setCrestChargeID(null);
//   delcharge2.setCrestChargeSeqNo(null);
//   delcharge2.setDeleteResults(false);
//   delcharge2.setInCourt(false);
//   delcharge2.setUpdateCount(-1);
//
//    CSValueObject valueObject1=  delcharge;
//    CSValueObject csvalueobjectRet = (CSValueObject)deletechargetransformer.transformVO(valueObject1);
//    System.out.println("**** RESULT 1*****");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  delcharge2;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)deletechargetransformer.transformVO(valueObject2);
//    System.out.println("**** RESULT 2*****");
//    System.out.println(csvalueobjectRet2.toString());
//
//
//    AddressValue address = new AddressValue();
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  address;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)deletechargetransformer.transformVO(valueObject3);
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
//        CSValueObject csvalueobjectRet4 = (CSValueObject)deletechargetransformer.transformVO(valueObject4);
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
//
//
//  }
//
//  //public void testTransformOutput() {
//   // DeleteChargeTransformer deletechargetransformer = new DeleteChargeTransformer();
//    //Object result1=  null  /** @todo fill in non-null value */;
//    //Object objectRet = deletechargetransformer.transformOutput(result1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//  //}
//}
//