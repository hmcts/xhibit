//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.LinkCountDefTransformer;
//
//import java.util.Vector;
//
//
//
//public class TestLinkCountDefTransformer extends TestCase
//{
//
//  public TestLinkCountDefTransformer(String s)
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
//  public void testTransformOutput() {
//
//    LinkCountDefTransformer linkcountdeftransformer = new LinkCountDefTransformer();
//
//    Vector v = new Vector();
//    Integer[] pair1 = {new Integer(111), new Integer(222)};
//    Integer[] pair2 = {new Integer(122), new Integer(322)};
//    Integer[] pair3 = {new Integer(111), new Integer(322)};
//    Integer[] pair4 = {new Integer(122), new Integer(222)};
//    Integer[] pair5 = {new Integer(111), new Integer(444)};
//    v.addElement(pair1);
//    v.addElement(pair2);
//    v.addElement(pair3);
//    v.addElement(pair4);
//    v.addElement(pair5);
//
//    LinkCountDefValue links = new LinkCountDefValue();
//    links.setCaseID(new Integer(111));
//    links.setCourtID(new Integer(222));
//    links.setIsInCourt(true);
//    links.setUpdateCount(99);
//    //links.setCountDefPairs(v);
//
//    LinkCountDefValue links2 = new LinkCountDefValue();
//    links2.setCaseID(null);
//    links2.setCourtID(null);
//    links2.setIsInCourt(false);
//    links2.setUpdateCount(-1);
//    //links2.setCountDefPairs(null);
//
//    CSValueObject valueObject1=  links;
//    CSValueObject csvalueobjectRet = (CSValueObject)linkcountdeftransformer.transformVO(valueObject1);
//    System.out.println("****RESULT 1****");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  links2;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)linkcountdeftransformer.transformVO(valueObject2);
//    System.out.println("****RESULT 2****");
//    System.out.println(csvalueobjectRet2.toString());
//
//    AddressValue address = new AddressValue();
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  address;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)linkcountdeftransformer.transformVO(valueObject3);
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
//        CSValueObject csvalueobjectRet4 = (CSValueObject)linkcountdeftransformer.transformVO(valueObject4);
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
//
//
//
//  public void testTransformVO() {
//    LinkCountDefTransformer linkcountdeftransformer = new LinkCountDefTransformer();
//    CSValueObject valueObject1=  null  /** @todo fill in non-null value */;
//    CSValueObject csvalueobjectRet = (CSValueObject)linkcountdeftransformer.transformVO(valueObject1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//  }
//}
//