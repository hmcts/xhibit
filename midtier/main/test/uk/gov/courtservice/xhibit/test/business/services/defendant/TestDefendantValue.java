
package uk.gov.courtservice.xhibit.test.business.services.defendant;

import junit.framework.*;
import org.apache.log4j.*;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.AddressValue;

import java.util.Calendar;
import java.util.Date;

public class TestDefendantValue extends TestCase
{
  private static Logger log =  CSServices.getLogger(TestDefendantValue.class);
  public TestDefendantValue(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testSetAndGetAddressValue() {
    /**
     * This method checks both the set and getAddressValue() methods
     */

    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    /**
     * Build the address value object
     */
    String val1=  "address1";
    String val2=  "address2";
    String val3=  "address3";
    String val4=  "address4";
    String val5=  "town";
    String val6=  "county";
    String val7=  "postcode";
    String val8=  "country";

    AddressValue addressvalue = new AddressValue(val1, val2, val3, val4, val5, val6, val7, val8);

    /**
     * Set the defendant value object with the new address value object
     */
    defendantvalue.setAddressValue( addressvalue );

    /**
     * Get the address value object
     */
    AddressValue addressvalueRet = defendantvalue.getAddressValue();
    log.debug("TestDefendantValue.testSetAndGetAddressValue() - " +  addressvalueRet.getAddress1() );
  }

  public void testGetCrestDefendantID() {
   Integer crestDefendantID = new Integer(1);
   String firstName = "Laurent";
   String middleName = "M";
   String surName = "Bossard";
   String initials = "LB";
   Calendar dateOfBirth = Calendar.getInstance();
   dateOfBirth.setTime(new Date(500));
   Integer gender = new Integer(1);
   Calendar lastConvictionDate = Calendar.getInstance();
   lastConvictionDate.setTime(new Date(1000));

   uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

   Integer integerRet = defendantvalue.getCrestDefendantID();
   log.debug("TestDefendantValue.testGetCrestDefendantID() - " +  integerRet );
  }

  public void testGetDateOfBirth() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Calendar dateRet = defendantvalue.getDateOfBirth();
    log.debug("TestDefendantValue.testGetDateOfBirth() - " +  dateRet.getTime() );
  }
  public void testGetDefendantID() {
    Integer defendantID = new Integer(100);
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(defendantID, crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Integer integerRet = defendantvalue.getDefendantID();
    log.debug("TestDefendantValue.testGetDefendantID() - " +  integerRet );
  }
  public void testGetGender() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Integer intRet = defendantvalue.getGender();
    log.debug("TestDefendantValue.testGetGender() - " +  intRet );
  }
  public void testGetInitials() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String stringRet = defendantvalue.getInitials();
    log.debug("TestDefendantValue.testGetInitials() - " +  stringRet );
  }
  public void testGetLastConvictionDate() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Calendar dateRet = defendantvalue.getLastConvictionDate();
    log.debug("TestDefendantValue.testGetLastConvictionDate() - " +  dateRet.getTime() );
  }
  public void testGetMiddleName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String stringRet = defendantvalue.getMiddleName();
    log.debug("TestDefendantValue.testGetMiddleName() - " +  stringRet );
  }
  public void testGetSurName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String stringRet = defendantvalue.getSurName();
    log.debug("TestDefendantValue.testGetSurName() - " +  stringRet );
  }
  public void testGetfirstName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String stringRet = defendantvalue.getFirstName();
    log.debug("TestDefendantValue.testGetfirstName() - " +  stringRet );
  }


  public void testSetDateOfBirth() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Date dateOfBirth1=  null ;
    defendantvalue.setDateOfBirth( Calendar.getInstance() );
    log.debug("TestDefendantValue.testSetDateOfBirth() - " +  defendantvalue.getDateOfBirth() );
  }
  public void testSetFirstName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String firstName1=  "New First Name";
    defendantvalue.setFirstName(firstName1);
    log.debug("TestDefendantValue.testSetFirstName() - " +  defendantvalue.getFirstName() );
  }
  public void testSetGender() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    Integer gender1 = new Integer(2);
    defendantvalue.setGender(gender1);
    log.debug("TestDefendantValue.testSetGender() - " +  defendantvalue.getGender() );
  }
  public void testSetInitials() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String initials1=  "LMM";
    defendantvalue.setInitials(initials1);
    log.debug("TestDefendantValue.testSetInitials() - " +  defendantvalue.getInitials() );
  }
  public void testSetLastConvictionDate() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    defendantvalue.setLastConvictionDate( Calendar.getInstance() );
    log.debug("TestDefendantValue.testSetLastConvictionDate() - " + defendantvalue.getLastConvictionDate() );
  }
  public void testSetMiddleName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String middleName1=  "new middle name";
    defendantvalue.setMiddleName(middleName1);
    log.debug("TestDefendantValue.testSetMiddleName() - " +  defendantvalue.getMiddleName() );
  }
  public void testSetSurName() {
    Integer crestDefendantID = new Integer(1);
    String firstName = "Laurent";
    String middleName = "M";
    String surName = "Bossard";
    String initials = "LB";
    Calendar dateOfBirth = Calendar.getInstance();
    dateOfBirth.setTime(new Date(500));
    Integer gender = new Integer(1);
    Calendar lastConvictionDate = Calendar.getInstance();
    lastConvictionDate.setTime(new Date(1000));

    uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantvalue = new uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue(crestDefendantID, firstName, middleName, surName, initials, dateOfBirth, gender, lastConvictionDate, new Integer(1), "N", null);

    String surName1=  "new surname";
    defendantvalue.setSurName(surName1);
    log.debug("TestDefendantValue.testSetSurName() - " +  defendantvalue.getSurName() );
  }
}
