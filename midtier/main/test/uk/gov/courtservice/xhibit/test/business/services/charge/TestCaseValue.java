package uk.gov.courtservice.xhibit.test.business.services.charge;

import junit.framework.*;
import org.apache.log4j.*;
import uk.gov.courtservice.framework.services.CSServices;

import java.util.Date;

public class TestCaseValue extends TestCase
{
  private static Logger log =  CSServices.getLogger(TestCaseValue.class);

  public TestCaseValue(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testGetCaseDescription() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String stringRet = casevalue.getCaseDescription();
    log.debug("TestCaseValue.testGetCaseDescription() - " +  stringRet );
  }

  public void testGetCaseID() {
    Integer caseID = new Integer(101);
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseID, caseNumber, caseType );

    Integer integerRet = casevalue.getCaseID();
    log.debug("TestCaseValue.testGetCaseID() - " +  integerRet );
  }

  public void testGetCaseNumber() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    Integer integerRet = casevalue.getCaseNumber();
    log.debug("TestCaseValue.testGetCaseNumber() - " +  integerRet );
  }

  public void testGetCaseSubType() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String stringRet = casevalue.getCaseSubType();
    log.debug("TestCaseValue.testGetCaseSubType() - " +  stringRet );
  }

  public void testGetCaseTitle() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String stringRet = casevalue.getCaseTitle();
    log.debug("TestCaseValue.testGetCaseTitle() - " +  stringRet );
  }

  public void testGetCaseType() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String stringRet = casevalue.getCaseType();
    log.debug("TestCaseValue.testGetCaseType() - " +  stringRet );
  }

  public void testGetLinkedCaseID() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    Integer integerRet = casevalue.getLinkedCaseID();
    log.debug("TestCaseValue.testGetLinkedCaseID() - " +  integerRet );
  }

  public void testGetMagConvictionDate() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    Date dateRet = casevalue.getMagConvictionDate();
    log.debug("TestCaseValue.testGetMagConvictionDate() - " +  dateRet );
  }

  public void testSetCaseDescription() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String caseDescription1=  "Case description";
    casevalue.setCaseDescription(caseDescription1);

    log.debug("TestCaseValue.testSetCaseDescription() - " +  casevalue.getCaseDescription() );
  }

  public void testSetCaseSubType() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String caseSubType1=  "Case Sub Type";
    casevalue.setCaseSubType(caseSubType1);

    log.debug("TestCaseValue.testSetCaseSubType() - " +  casevalue.getCaseSubType() );
  }
  public void testSetCaseTitle() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    String caseTitle1 = "Case Title";
    casevalue.setCaseTitle(caseTitle1);
    log.debug("TestCaseValue.testSetCaseTitle() - " +  casevalue.getCaseTitle() );
  }

  public void testSetLinkedCaseID() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    Integer linkedCaseID1 = new Integer(555);
    casevalue.setLinkedCaseID(linkedCaseID1);
    log.debug("TestCaseValue.testSetLinkedCaseID() - " +  casevalue.getLinkedCaseID() );
  }
  public void testSetMagConvictionDate() {
    Integer caseNumber = new Integer(20025678);
    String caseType = "T";
    uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue casevalue = new uk.gov.courtservice.xhibit.business.vos.services.charge.CaseValue(caseNumber, caseType);

    Date magConvictionDate1 = new Date();
    casevalue.setMagConvictionDate(magConvictionDate1);
    log.debug("TestCaseValue.testSetMagConvictionDate() - " + casevalue.getMagConvictionDate() );
  }
}
