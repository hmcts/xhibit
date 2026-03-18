//
//package uk.gov.courtservice.xhibit.test.business.services.charge;
//
//import java.util.Calendar;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.Vector;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
//import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
//import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
//
//public class TestChargeCompositeValue extends TestCase
//{
//  private static Logger log =  CSServices.getLogger(TestChargeCompositeValue.class);
//
//  public TestChargeCompositeValue(String s)
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
//  /**
//   * This method creates the Case Value Object used to create the ChargeCompositeValue object
//   * @return CaseValue
//   */
//  private CaseBasicValue getCaseValue()
//  {
//    Integer caseID = new Integer(101);
//    Integer version = new Integer(1);
//    Integer caseNumber = new Integer(20025678);
//    String caseType = "T";
//    CaseBasicValue caseBasicValue = new CaseBasicValue(caseID, version);
//    caseBasicValue.setCaseNumber(caseNumber);
//    caseBasicValue.setCaseType(caseType);
//
//    return caseBasicValue;
//  }
//
//  /**
//   * This method creates the collection of charge value objects used to create the ChargeCompositeValue object
//   * @return CaseValue
//   */
//  private Collection getCharges()
//  {
//    /**
//     * First ChargeValue object
//     */
//    Integer chargeID1 = new Integer(1);
//    Integer caseID1 = new Integer(2);
//    String chargeType1 =  "Charge Type 1";
//    ChargeValue chargevalue1 = new ChargeValue(chargeID1, caseID1, ChargeTypes.BREACH);
//
//    /**
//     * Build an offence value
//     */
//    Integer offenceID1 = new Integer(789);
//    Integer refOffenceID1 = new Integer(1111);
//    Vector defIDs1 = new Vector();
//    defIDs1.add( new Integer(100) );
//    defIDs1.add( new Integer(110) );
//    defIDs1.add( new Integer(120) );
//    OffenceValue offencevalue1 = new OffenceValue(offenceID1, chargeID1, refOffenceID1, defIDs1);
//
//    /**
//     * Build another offence value
//     */
//    Integer offenceID2 = new Integer(790);
//    Integer refOffenceID2 = new Integer(2222);
//    Vector defIDs2 = new Vector();
//    defIDs2.add( new Integer(101) );
//    defIDs2.add( new Integer(111) );
//    defIDs2.add( new Integer(121) );
//    OffenceValue offencevalue2 = new OffenceValue(offenceID2, chargeID1, refOffenceID2, defIDs2);
//
//    /**
//     * Build def value objects
//     */
//    Calendar lastConvDate1 = Calendar.getInstance();
//    lastConvDate1.setTime(new Date(500));
//    Calendar lastConvDate2 = Calendar.getInstance();
//    lastConvDate2.setTime(new Date(800));
//    Calendar lastConvDate3 = Calendar.getInstance();
//    lastConvDate3.setTime(new Date(1000));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue11 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(100), new Integer(101), "John1", "B", "Pierce", "JBP", Calendar.getInstance(), new Integer(1), lastConvDate1, new Integer(1));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue21 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(110), new Integer(201), "Marie1", "B", "Brosnan", "MBB", Calendar.getInstance(), new Integer(2), lastConvDate2, new Integer(2));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue31 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(120), new Integer(301), "Pierre1", "B", "Moscovisci", "PBM", Calendar.getInstance(), new Integer(1), lastConvDate2, new Integer(3));
//
//    Vector defendantValues1 = new Vector();
//    defendantValues1.add( defValue11 );
//    defendantValues1.add( defValue21 );
//    defendantValues1.add( defValue31 );
//
//    offencevalue1.setDefendantValues( defendantValues1 );
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue12 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(101), new Integer(101), "John2", "B", "Pierce", "JBP", Calendar.getInstance(), new Integer(1), lastConvDate1, new Integer(1));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue22 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(111), new Integer(201), "Marie2", "B", "Brosnan", "MBB", Calendar.getInstance(), new Integer(2), lastConvDate2, new Integer(2));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue32 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(121), new Integer(301), "Pierre2", "B", "Moscovisci", "PBM", Calendar.getInstance(), new Integer(1), lastConvDate3, new Integer(3));
//
//    Vector defendantValues2 = new Vector();
//    defendantValues2.add( defValue12 );
//    defendantValues2.add( defValue22 );
//    defendantValues2.add( defValue32 );
//
//    offencevalue2.setDefendantValues( defendantValues2 );
//    /**
//     * Create a collection of Offence Value Objects
//     */
//    Vector offValues = new Vector();
//    offValues.add(offencevalue1);
//    offValues.add(offencevalue2);
//
//    chargevalue1.setOffenceValues( offValues );
//
//    /**
//     * Second ChargeValue object
//     */
//    Integer chargeID2 = new Integer(3);
//    Integer caseID2 = new Integer(4);
//    String chargeType2 =  "Charge Type 2";
//    ChargeValue chargevalue2 = new ChargeValue(chargeID2, caseID2, ChargeTypes.COMMITAL_FOR_SENTENCE);
//
//    /**
//     * Build an offence value
//     */
//    Integer offenceID3 = new Integer(800);
//    Integer refOffenceID3 = new Integer(3333);
//    Vector defIDs3 = new Vector();
//    defIDs3.add( new Integer(300) );
//    defIDs3.add( new Integer(310) );
//    defIDs3.add( new Integer(320) );
//    OffenceValue offencevalue3 = new OffenceValue(offenceID3, chargeID2, refOffenceID3, defIDs3);
//
//    /**
//     * Build another offence value
//     */
//    Integer offenceID4 = new Integer(801);
//    Integer refOffenceID4 = new Integer(4444);
//    Vector defIDs4 = new Vector();
//    defIDs4.add( new Integer(301) );
//    defIDs4.add( new Integer(311) );
//    defIDs4.add( new Integer(321) );
//    OffenceValue offencevalue4 = new OffenceValue(offenceID4, chargeID2, refOffenceID4, defIDs4);
//
//    /**
//     * Build def value objects
//     */
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue13 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(300), new Integer(101), "John3", "B", "Pierce", "JBP", Calendar.getInstance(), new Integer(1), lastConvDate1, new Integer(1));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue23 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(310), new Integer(201), "Marie3", "B", "Brosnan", "MBB", Calendar.getInstance(), new Integer(2), lastConvDate2, new Integer(2));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue33 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(320), new Integer(301), "Pierre3", "B", "Moscovisci", "PBM", Calendar.getInstance(), new Integer(1), lastConvDate3, new Integer(3));
//
//    Vector defendantValues3 = new Vector();
//    defendantValues3.add( defValue13 );
//    defendantValues3.add( defValue23 );
//    defendantValues3.add( defValue33 );
//
//    offencevalue3.setDefendantValues( defendantValues3 );
//
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue14 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(301), new Integer(101), "John4", "B", "Pierce", "JBP", Calendar.getInstance(), new Integer(1), lastConvDate1, new Integer(1));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue24 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(311), new Integer(201), "Marie4", "B", "Brosnan", "MBB", Calendar.getInstance(), new Integer(2), lastConvDate2, new Integer(2));
//    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue34 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(321), new Integer(301), "Pierre4", "B", "Moscovisci", "PBM", Calendar.getInstance(), new Integer(1), lastConvDate3, new Integer(3));
//
//    Vector defendantValues4 = new Vector();
//    defendantValues4.add( defValue14 );
//    defendantValues4.add( defValue24 );
//    defendantValues4.add( defValue34 );
//
//    offencevalue4.setDefendantValues( defendantValues4 );
//
//    /**
//     * Create a collection of Offence Value Objects
//     */
//    Vector offValues2 = new Vector();
//    offValues2.add(offencevalue3);
//    offValues2.add(offencevalue4);
//
//    chargevalue2.setOffenceValues( offValues2 );
//
//    /**
//     * Create a Collection of ChargeValue objects
//     */
//    Vector charges = new Vector();
//    charges.add(chargevalue1);
//    charges.add(chargevalue2);
//
//    return charges;
//  }
//
//  public void testGetCase() {
//    CaseBasicValue caseValue = getCaseValue();
//    Collection charges = getCharges();
//
//    ChargeCompositeValue chargecompositevalue = new ChargeCompositeValue(caseValue, charges);
//
//    CaseBasicValue casevalueRet = chargecompositevalue.getCaseBasicValue();
//    log.debug("TestCaseValue.testGetCase() - " +  casevalueRet.getCaseNumber() );
//  }
//
//  public void testGetCharges() {
//    CaseBasicValue caseValue = getCaseValue();
//    Collection charges = getCharges();
//
//    ChargeCompositeValue chargecompositevalue = new ChargeCompositeValue(caseValue, charges);
//
//    Collection collectionRet = chargecompositevalue.getCharges();
//    Iterator iter = collectionRet.iterator();
//    while ( iter.hasNext() )
//    {
//      ChargeValue chg = (ChargeValue) iter.next();
//      log.debug("TestCaseValue.testGetCharges() - " + chg.getChargeType() );
//    }
//  }
//
//  public void testGetDefendants() {
//
//    CaseBasicValue caseValue = getCaseValue();
//    Collection charges = getCharges();
//
//    ChargeCompositeValue chargecompositevalue = new ChargeCompositeValue(caseValue, charges);
//
//    Collection collectionRet = chargecompositevalue.getDefendants( new Integer(800) );
//
//    Iterator iter = collectionRet.iterator();
//
//    while ( iter.hasNext() )
//    {
//      uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defvalue = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) iter.next();
//      log.debug("TestCaseValue.testGetDefendants() - " +  defvalue.getFirstName() );
//    }
//
//  }
//
//  public void testGetOffenceValues() {
//
//    CaseBasicValue caseValue = getCaseValue();
//    Collection charges = getCharges();
//
//    ChargeCompositeValue chargecompositevalue = new ChargeCompositeValue(caseValue, charges);
//
//    Collection offValues = chargecompositevalue.getOffenceValues( new Integer(1) );
//
//    Iterator iter = offValues.iterator();
//
//    while ( iter.hasNext() )
//    {
//      OffenceValue ov = (OffenceValue) iter.next();
//      log.debug("TestCaseValue.testGetOffenceValues() - " +  ov.getRefOffenceID() );
//    }
//
//  }
//
//  public void testGetAllDefendants() {
//
//    CaseBasicValue caseValue = getCaseValue();
//    Collection charges = getCharges();
//
//    ChargeCompositeValue chargecompositevalue = new ChargeCompositeValue(caseValue, charges);
//
//    Collection defendants = chargecompositevalue.getAllDefendants();
//
//    Iterator iter = defendants.iterator();
//
//    while ( iter.hasNext() )
//    {
//      uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) iter.next();
//      log.debug("TestCaseValue.testGetAllDefendants() - " + defValue.getFirstName() );
//    }
//  }
//}
//