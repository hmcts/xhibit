
package uk.gov.courtservice.xhibit.test.business.services.charge;

import junit.framework.*;
import org.apache.log4j.*;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;

import java.util.*;

public class TestOffenceValue extends TestCase
{
  private static Logger log =  CSServices.getLogger(TestOffenceValue.class);

  public TestOffenceValue(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testGetChargeID() {

    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Integer integerRet = offencevalue.getChargeID();
    log.debug("TestOffenceValue.testGetChargeID() - " + integerRet);
  }

  public void testGetCrestOffenceFreeText() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);


    String stringRet = offencevalue.getCrestOffenceFreeText();
    log.debug("TestOffenceValue.getCrestOffenceFreeText() - " + stringRet);
  }

  public void testGetCrestOffenceID() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);


    Integer integerRet = offencevalue.getCrestOffenceID();
    log.debug("TestOffenceValue.getCrestOffenceID() - " + integerRet);
  }
  public void testGetCrestOffenceSeqNo() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Integer integerRet = offencevalue.getCrestOffenceSeqNo();
    log.debug("TestOffenceValue.getCrestOffenceSeqNo() - " + integerRet);
  }
  public void testGetDefendantIDs() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Collection collectionRet = offencevalue.getDefendantIDs();

    Iterator iter = collectionRet.iterator();

    while ( iter.hasNext() )
    {
      log.debug("TestOffenceValue.getDefendantIDs() - " + iter.next());
    }

  }
  public void testGetDefendantValues() {
    /**
     * This method tests getDefendantValues() & setDefendantValues()
     */
    log.debug("This method tests getDefendantValues() & setDefendantValues()");

    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Calendar lastConvDate1 = Calendar.getInstance();
    lastConvDate1.setTime(new Date(500));
    Calendar lastConvDate2 = Calendar.getInstance();
    lastConvDate2.setTime(new Date(800));
    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue1 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(10), new Integer(101), "John", "B", "Pierce", "JBP", Calendar.getInstance(), new Integer(1), lastConvDate1, new Integer(1), "N", null);
    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue2 = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(new Integer(20), new Integer(201), "Marie", "B", "Brosnan", "MBB", Calendar.getInstance(), new Integer(1), lastConvDate2, new Integer(2), "N", null);

    Vector defendantValues = new Vector();
    defendantValues.add( defValue1 );
    defendantValues.add( defValue2 );

    offencevalue.setDefendantValues( defendantValues );
    Collection collectionRet = offencevalue.getDefendantValues();

    Iterator iter = collectionRet.iterator();

    while ( iter.hasNext() )
    {
      uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defValue = (uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue) iter.next();
      log.debug("TestOffenceValue.testGetDefendantValues() - check by getting the defendant surname - " + defValue.getSurName() );
    }
  }

  public void testGetMultiple() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Integer integerRet = offencevalue.getMultiple();
    log.debug("TestOffenceValue.getMultiple() - " + integerRet);
  }

  public void testGetOffenceID() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);
    Integer offenceID = new Integer(1000);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(offenceID, chargeID, refOffenceID, defIDs);

    Integer integerRet = offencevalue.getOffenceID();
    log.debug("TestOffenceValue.getOffenceID() - " + integerRet);
  }

  public void testGetAndSetRefOffenceID() {
    /**
     * This method checks both set and getRefOffenceID
     */
    log.debug("This method checks both set and getRefOffenceID");

    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Integer refOffenceID1 = new Integer(2002);
    offencevalue.setRefOffenceID(refOffenceID1);

    Integer integerRet = offencevalue.getRefOffenceID();
    log.debug("TestOffenceValue.testGetAndSetRefOffenceID() - " + integerRet);
  }

  public void testGetRefOffenceID() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);

    Integer integerRet = offencevalue.getRefOffenceID();
    log.debug("TestOffenceValue.getRefOffenceID() - " + integerRet);
  }

  public void testSetDefendantIDs() {
    Integer chargeID = new Integer(1);
    Integer refOffenceID = new Integer(2);

    Vector defIDs = new Vector();
    defIDs.add( new Integer(10) );
    defIDs.add( new Integer(11) );
    defIDs.add( new Integer(12) );

    OffenceValue offencevalue = new OffenceValue(chargeID, refOffenceID, defIDs);


    Vector newDefIDs = new Vector();
    newDefIDs.add( new Integer(20) );
    newDefIDs.add( new Integer(21) );
    newDefIDs.add( new Integer(22) );

    offencevalue.setDefendantIDs(newDefIDs);

    Collection collectionRet = offencevalue.getDefendantIDs();

    Iterator iter = collectionRet.iterator();

    while ( iter.hasNext() )
    {
      log.debug("TestOffenceValue.setDefendantIDs() - " + iter.next());
    }
  }
}
