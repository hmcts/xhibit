//
//package uk.gov.courtservice.xhibit.test.integration.mercator.votransformer;
//
//import junit.framework.*;
//import uk.gov.courtservice.framework.business.vos.CSValueObject;
//import uk.gov.courtservice.framework.exception.CSConfigurationException;
//import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
//import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OffenceTransformer;
//import uk.gov.courtservice.xhibit.integration.vos.services.prehearing.OffenceMVO;
//
//import java.util.Calendar;
//import java.util.Vector;
//
//
//public class TestOffenceTransformer extends TestCase
//{
//
//  public TestOffenceTransformer(String s)
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
//    OffenceTransformer offencetransformer = new OffenceTransformer();
//
//    Vector v = new Vector();
//    v.addElement(new Integer(1111));
//    v.addElement(new Integer(2222));
//    v.addElement(new Integer(3333));
//    v.addElement(new Integer(4444));
//
//    Calendar cal = Calendar.getInstance();
//
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
//
//    Vector v2 = new Vector();
//    v2.addElement(defendantValue1);
//    v2.addElement(defendantValue2);
//
//    OffenceValue offence = new OffenceValue(new Integer(1),
//        new Integer(2), new Integer(3), v, "crestofftext",
//        new Integer(4), new Integer(5), new Integer(6),
//        "offDesc");
//
//    offence.setDefendantValues(v2);
//    offence.setDirty(true);
//    offence.setInCourt(true);
//    offence.setPlea("G");
//    offence.setRefSystemCodeID(new Integer(99));
//    offence.setCourtID(new Integer(88));
//    offence.setCaseID(new Integer(20028938));
//
//    OffenceValue offence2 = new OffenceValue(null,
//        null, null, null, null,
//        null, null, null, null);
//
//    offence2.setDefendantValues(null);
//    offence2.setDirty(false);
//    offence2.setInCourt(false);
//    offence2.setPlea(null);
//    offence2.setRefSystemCodeID(null);
//    offence2.setCourtID(null);
//    offence2.setCaseID(null);
//
//    CSValueObject valueObject1=  offence;
//    CSValueObject csvalueobjectRet = (CSValueObject)offencetransformer.transformVO(valueObject1);
//    System.out.println("****** RESULT 1********* \n" + csvalueobjectRet.toString());
//    if(csvalueobjectRet instanceof OffenceMVO){
//      System.out.println("OffenceMVO is true");
//    }
//    if(csvalueobjectRet instanceof OffenceValue){
//      System.out.println("OffenceValue is true");
//    }
//
//    CSValueObject valueObject2=  offence2;
//    CSValueObject csvalueobjectRet2 = (CSValueObject)offencetransformer.transformVO(valueObject2);
//    System.out.println("****** RESULT 2*********\n" + csvalueobjectRet2.toString());
//
//    AddressValue address3 = new AddressValue();
//    System.out.println("*********** RESULT 3 *********** ");
//    try
//    {
//      CSValueObject valueObject3=  address3;
//      CSValueObject csvalueobjectRet3 = (CSValueObject)offencetransformer.transformVO(valueObject3);
//      System.out.println(csvalueobjectRet3.toString());
//    }catch(CSConfigurationException e)
//    {
//      System.out.println("error : " + e.getMessage());
//    }
//
//      AddressValue address4 = null;
//      System.out.println("*********** RESULT 4 *********** ");
//    try
//    {
//        CSValueObject valueObject4=  address4;
//        CSValueObject csvalueobjectRet4 = (CSValueObject)offencetransformer.transformVO(valueObject4);
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
//  }
//}
//