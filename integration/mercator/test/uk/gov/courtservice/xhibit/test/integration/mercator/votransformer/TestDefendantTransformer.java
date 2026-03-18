//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.DefendantTransformer;
//import uk.gov.courtservice.xhibit.integration.vos.services.defendant.DefendantMVO;
//
//import java.util.Calendar;
//
//public class TestDefendantTransformer extends TestCase
//{
//
//  public TestDefendantTransformer(String s)
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
//
//    DefendantTransformer defendanttransformer = new DefendantTransformer();
//
//    Calendar cal = Calendar.getInstance();
//
//    AddressValue address = new AddressValue(new Integer(11), "add1", "add2", "add3",
//        "add4", "town", "county", "postcode", "country");
//
//   uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(1),
//        new Integer(2), "Marie", "Ulrika", "Holmberg", "UE",
//        cal, new Integer(2), cal, new Integer(1), address);
//
//   uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue2 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(null,null,null,null,null,null,null,null,null, null, null);
//
//   CSValueObject csvalueobjectRet = (CSValueObject)defendanttransformer.transformVO(defendantValue);
//   System.out.println("***** RESULT 1 *****  : \n" + csvalueobjectRet.toString());
//
//   if(csvalueobjectRet instanceof DefendantMVO){
//     System.out.println("DefendantMVO is true");
//   }
//
//   if(csvalueobjectRet instanceof uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue){
//     System.out.println("DefendantValue is true");
//   }
//
//   CSValueObject csvalueobjectRet2 = (CSValueObject)defendanttransformer.transformVO(defendantValue2);
//   System.out.println("***** RESULT 2 *****  : \n" + csvalueobjectRet2.toString());
//
//
//   System.out.println("*********** RESULT 3 *********** ");
//   try
//   {
//     CSValueObject valueObject3=  address;
//     CSValueObject csvalueobjectRet3 = (CSValueObject)defendanttransformer.transformVO(valueObject3);
//     System.out.println(csvalueobjectRet3.toString());
//   }catch(CSConfigurationException e)
//   {
//     System.out.println("error : " + e.getMessage());
//   }
//
//     uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defNull = null;
//     System.out.println("*********** RESULT 4 *********** ");
//   try
//   {
//     CSValueObject valueObject4=  defNull;
//     CSValueObject csvalueobjectRet4 = (CSValueObject)defendanttransformer.transformVO(valueObject4);
//     if(csvalueobjectRet4 != null)
//     {
//       System.out.println(csvalueobjectRet4.toString());
//     }else{
//       System.out.println("object is null");
//     }
//   }catch(CSConfigurationException e)
//   {
//     System.out.println("error : " + e.getMessage());
//   }
//
//
//
//
//  }
//}
//