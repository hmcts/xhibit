
package uk.gov.courtservice.xhibit.test.business.services.charge;

import junit.framework.*;
import org.apache.log4j.*;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

import java.util.*;


public class TestChargeValue extends TestCase
{
  private static Logger log =  CSServices.getLogger(TestChargeValue.class);

  public TestChargeValue(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testGetCaseID() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41);
    Integer integerRet = chargevalue.getCaseID();
    log.debug("TestChargeValue.testGetCaseID() - " + integerRet);
  }
  public void testGetChargeID() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.INDICTMENT);

    Integer integerRet = chargevalue.getChargeID();
    log.debug("TestChargeValue.testGetChargeID() - " + integerRet);
  }

  public void testGetChargeType() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";

//  ChargeType BREACH                 = new ChargeType("Breach","B");
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.BREACH );
    assertEquals("B", chargevalue.getChargeType());
    log.debug("chargeTypeDescrtipion " + chargevalue.getChargeTypeDescription());
    log.debug("chargetype = "+ chargevalue.getChargeType());
    assertEquals("Breach", chargevalue.getChargeTypeDescription());

    //ChargeType CRIMINAL_APPEAL        = new ChargeType("Criminal Appeal","C");
    chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.CRIMINAL_APPEAL );
    assertEquals("C", chargevalue.getChargeType());
    assertEquals("Criminal Appeal", chargevalue.getChargeTypeDescription());

    //ChargeType MISC_APPEAL            = new ChargeType("Miscelleanous Appeal","M");
    chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.MISC_APPEAL );
    assertEquals("M", chargevalue.getChargeType());
    assertEquals("Miscelleanous Appeal", chargevalue.getChargeTypeDescription());

//  ChargeType SECTION_41             = new ChargeType("Summary Offence (section 41)","O");
    chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41 );
    assertEquals("O", chargevalue.getChargeType());
    assertEquals("Summary Offence (section 41)", chargevalue.getChargeTypeDescription());

    //ChargeType COMMITAL_FOR_SENTANCE  = new ChargeType("Committal for Sentence","S");
    chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.COMMITAL_FOR_SENTENCE );
    log.debug("chargeType = "+ chargevalue.getChargeType());
    assertEquals("S", chargevalue.getChargeType());
    assertEquals("Committal for Sentence", chargevalue.getChargeTypeDescription());

    //ChargeType INDICTMENT             = new ChargeType("Indictment","I");
    chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.INDICTMENT );
    assertEquals("I", chargevalue.getChargeType());
    assertEquals("Indictment", chargevalue.getChargeTypeDescription());


    String stringRet = chargevalue.getChargeType();
    log.debug("TestChargeValue.testGetChargeType() - " + stringRet);
  }

  public void testGetCrestChargeID() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.COMMITAL_FOR_SENTENCE);

    Integer integerRet = chargevalue.getCrestChargeID();
    log.debug("TestChargeValue.testGetCrestChargeID() - " + integerRet);
  }

  public void testGetCrestChargeSeqNo() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.CRIMINAL_APPEAL);

    Integer integerRet = chargevalue.getCrestChargeSeqNo();
    log.debug("TestChargeValue.testGetCrestChargeSeqNo() - " + integerRet);
  }

  public void testSetAndGetOffenceValues() {
    /**
     * This methods tests both set and getOffenceValues()
     */
    log.debug("This methods tests both set and getOffenceValues()");

    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.MISC_APPEAL);

    /**
     * Build an offence value
     */
    Integer refOffenceID1 = new Integer(1111);
    Vector defIDs1 = new Vector();
    defIDs1.add( new Integer(100) );
    defIDs1.add( new Integer(110) );
    defIDs1.add( new Integer(120) );
    OffenceValue offencevalue1 = new OffenceValue(refOffenceID1, defIDs1);

    /**
     * Build another offence value
     */
    Integer refOffenceID2 = new Integer(2222);
    Vector defIDs2 = new Vector();
    defIDs2.add( new Integer(101) );
    defIDs2.add( new Integer(111) );
    defIDs2.add( new Integer(121) );
    OffenceValue offencevalue2 = new OffenceValue(refOffenceID2, defIDs2);

    /**
     * Create a collection of Offence Value Objects
     */
    Vector offValues = new Vector();
    offValues.add(offencevalue1);
    offValues.add(offencevalue2);

    chargevalue.setOffenceValues( offValues );

    Collection collectionRet = chargevalue.getOffenceValues();

    Iterator iter = collectionRet.iterator();

    while ( iter.hasNext() )
    {
      OffenceValue offValue = ( OffenceValue ) iter.next();
      log.debug( "TestChargeValue.testSetAndGetOffenceValues() - " + offValue.getRefOffenceID());
    }
  }

  public void testGetProsPaperServedDate() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41);

    Calendar dateRet = chargevalue.getProsPaperServedDate();
    log.debug( "TestChargeValue.testGetProsPaperServedDate() - " + dateRet );
  }

  public void testSetChargeType() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41);

    String chargeType1=  "new Charge Type";
    chargevalue.setChargeType(ChargeTypes.BREACH);

    log.debug( "TestChargeValue.testSetChargeType() - " + chargevalue.getChargeType() );
  }

  public void testSetProsPaperServedDate() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41 );

    //chargevalue.setProsPaperServedDate( new Date() );
    chargevalue.setProsPaperServedDate(Calendar.getInstance());
    Calendar dateRet = chargevalue.getProsPaperServedDate();

    log.debug( "TestChargeValue.testSetProsPaperServedDate() - " + dateRet );
  }

  public void testGetBreachValue() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";

    Integer refOffenceID = new Integer(1);
    String breachType = "breach type";
    Calendar datePut = Calendar.getInstance();
    datePut.setTime(new Date(100000));
    BreachValue breachValue = new BreachValue(new Integer(111),
                              "originalSentence",
                              Calendar.getInstance(),
                              "originalCourtType",
                              new Integer(999),
                              datePut,
                              "breachType",
                              "bringBack",
                              "hoCode",
                              "hoDescription",
                               true);

    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41, breachValue);

    BreachValue bValue = chargevalue.getBreachValue();

    log.debug( "TestChargeValue.testGetBreachValue() - " + bValue );

  }
  public void testSetBreachValue() {
    Integer chargeID = new Integer(1);
    Integer caseID = new Integer(2);
    String chargeType =  "Charge Type";
    ChargeValue chargevalue = new ChargeValue(chargeID, caseID, ChargeTypes.SECTION_41 );


    Integer refOffenceID = new Integer(1);
    String breachType = "breach type";
    Calendar datePut = Calendar.getInstance();
    datePut.setTime(new Date(100000));
    BreachValue breachValue = new BreachValue(new Integer(111),
                               "originalSentence",
                               Calendar.getInstance(),
                               "originalCourtType",
                               new Integer(999),
                               datePut,
                               "breachType",
                               "bringBack",
                               "hoCode",
                               "hoDescription",
                               true);

    chargevalue.setBreachValue( breachValue );
    BreachValue bValue = chargevalue.getBreachValue();

    log.debug( "TestChargeValue.testSetBreachValue() - " + bValue.getBreachType() );

  }



}
