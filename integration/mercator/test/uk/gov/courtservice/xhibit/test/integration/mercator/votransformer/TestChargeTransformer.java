//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.ChargeTransformer;
//
//import java.util.Calendar;
//import java.util.Vector;
//
//
//public class TestChargeTransformer extends TestCase
//{
//
//  public TestChargeTransformer(String s)
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
//    ChargeTransformer chargetransformer = new ChargeTransformer();
//
//    Calendar cal = Calendar.getInstance();
//
//    //Creating a BreachValue
//    BreachValue breachValue = new BreachValue(
//        new Integer(1),
//        new Integer(2),
//        "origSent",
//        cal,
//        "origCourtType",
//        new Integer(343),
//        "Crown court at Liverpool",
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
//    BreachValue breachValue2 = new BreachValue(
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
//    //set vectors with DefIDs
//    Vector v = new Vector();
//    v.addElement(new Integer(1111));
//    v.addElement(new Integer(2222));
//    v.addElement(new Integer(3333));
//    v.addElement(new Integer(4444));
//
//    Vector v2 = new Vector();
//    v2.addElement(new Integer(5555));
//    v2.addElement(new Integer(6666));
//    v2.addElement(new Integer(7777));
//    v2.addElement(new Integer(8888));
//
//    //address and defendant
//    AddressValue address = new AddressValue(new Integer(11), "add1", "add2", "add3",
//      "add4", "town", "county", "postcode", "country");
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue1 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(1),
//      new Integer(2), "Marie", "Ulrika", "Holmberg", "UE",
//      cal, new Integer(2), cal, new Integer(1), address);
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue2 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(2),
//      new Integer(4), "Rich", "Anthony", "Naisby", "RA",
//      cal, new Integer(6), cal, new Integer(8), address);
//
//    Vector v3 = new Vector();
//    v3.addElement(defendantValue1);
//    v3.addElement(defendantValue2);
//
//    Vector v4 = new Vector();
//    v4.addElement(defendantValue2);
//
//    //Creating several OffenceValues
//    OffenceValue offence1 = new OffenceValue(new Integer(1),
//        new Integer(2), new Integer(3), v, "crestofftext111",
//        new Integer(4), new Integer(5), new Integer(6),
//        "offDesc111");
//
//    offence1.setDefendantValues(v3);
//    offence1.setDirty(true);
//    offence1.setInCourt(true);
//    offence1.setPlea("G");
//    offence1.setRefSystemCodeID(new Integer(99));
//    offence1.setCourtID(new Integer(88));
//    offence1.setCaseID(new Integer(20028938));
//
//    OffenceValue offence2 = new OffenceValue(new Integer(11),
//        new Integer(22), new Integer(33), v2, "crestofftext2222",
//        new Integer(44), new Integer(55), new Integer(66),
//        "offDesc222");
//
//    offence2.setDefendantValues(v3);
//    offence2.setDirty(false);
//    offence2.setInCourt(true);
//    offence2.setPlea("NG");
//    offence2.setRefSystemCodeID(new Integer(9339));
//    offence2.setCourtID(new Integer(8338));
//    offence2.setCaseID(new Integer(2002893833));
//
//    Vector offences = new Vector();
//    offences.addElement(offence1);
//    offences.addElement(offence2);
//
//    //this is a work around and have to change the ChargeType constructor to be public
//    //or you cannot test this or get nullpointer exception.
//    //ChargeType type = new ChargeType("B", "Breach");
//    //ChargeType type2 = null;
//
//    ChargeValue charge = new ChargeValue(new Integer(11),
//        new Integer(22),
//        //type,
//        null,
//        new Integer(33),
//        new Integer(44),
//        cal,
//        breachValue,
//        offences,
//        new Integer(55),
//        new Integer(66),
//        cal,
//        cal,
//        "indResp....");
//    charge.setUpdateCount(77);
//    charge.setDirty(true);
//
//    ChargeValue charge2 = new ChargeValue(
//        null,
//        null,
//        //type,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null,
//        null);
//    charge.setUpdateCount(-1);
//    charge.setDirty(false);
//
//    CSValueObject valueObject1=  charge;
//    CSValueObject csvalueobjectRet = (CSValueObject)chargetransformer.transformVO(valueObject1);
//    System.out.println("***** RESULT 1 *******");
//    System.out.println(csvalueobjectRet.toString());
//
//    CSValueObject valueObject2=  charge2;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)chargetransformer.transformVO(valueObject2);
//    System.out.println("***** RESULT 2 *******");
//    System.out.println(csvalueobjectRet2.toString());
//
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue3 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(1),
//      new Integer(2), "Marie", "Ulrika", "Holmberg", "UE",
//      cal, new Integer(2), cal, new Integer(1), null);
//
//    BreachValue breachValue4 = null;
//
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  defendantValue3;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)chargetransformer.transformVO(valueObject3);
//      System.out.println(csvalueobjectRet3.toString());
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//
//      System.out.println("*********** RESULT 4 *********** ");
//      try
//      {
//        CSValueObject valueObject4=  breachValue4;
//        CSValueObject csvalueobjectRet4 = (CSValueObject)chargetransformer.transformVO(valueObject4);
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