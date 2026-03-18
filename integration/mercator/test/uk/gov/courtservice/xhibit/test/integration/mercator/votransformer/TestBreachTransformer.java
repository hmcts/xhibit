//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.BreachTransformer;
//
//import java.util.Calendar;
//
//
//
//public class TestBreachTransformer extends TestCase
//{
//
//  public TestBreachTransformer(String s)
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
//    BreachTransformer breachtransformer = new BreachTransformer();
//
//    Calendar cal = Calendar.getInstance();
//
//
//    uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breachValue = new uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue(
//        new Integer(1),
//        new Integer(2),
//        "origSent",
//        cal,
//        "origCourtType",
//        new Integer(343),
//        "Crown court at Chelmsford",
//        cal,
//        "BT",
//        "BB",
//        "HO_code",
//        "HO_Desc",
//        true,
//        "NG",
//        new Integer(999),
//        new Integer(3838));
//    breachValue.setDirty(true);
//    breachValue.setUpdateCount(99);
//
//    uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breachValue2 = new uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue(
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    null,
//    false,
//    null,
//    null,
//    null);
//    breachValue.setDirty(false);
//    breachValue.setUpdateCount(-1);
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue1 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(1),
//      new Integer(2), "Marie", "Ulrika", "Holmberg", "UE",
//      cal, new Integer(2), cal, new Integer(1), null);
//
//    uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breachValue4 = null;
//
//    CSValueObject valueObject1=  breachValue;
//    CSValueObject csvalueobjectRet = (CSValueObject)breachtransformer.transformVO(valueObject1);
//    System.out.println("*********** RESULT 1 *********** ");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  breachValue2;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)breachtransformer.transformVO(valueObject2);
//    System.out.println("*********** RESULT 2 *********** ");
//    System.out.println(csvalueobjectRet2.toString());
//
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  defendantValue1;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)breachtransformer.transformVO(valueObject3);
//      System.out.println(csvalueobjectRet3.toString());
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//
//    System.out.println("*********** RESULT 4 *********** ");
//    try
//    {
//        CSValueObject valueObject4=  breachValue4;
//        CSValueObject csvalueobjectRet4 = (CSValueObject)breachtransformer.transformVO(valueObject4);
//        if(csvalueobjectRet4 != null)
//        {
//          System.out.println(csvalueobjectRet4.toString());
//        }else{
//          System.out.println("object is null");
//        }
//      }catch(CSConfigurationException e)
//      {
//        System.out.println("error : " + e.getMessage());
//      }
//
//  }
//}
//