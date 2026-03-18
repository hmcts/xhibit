
package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.linkhearing;

import junit.framework.*;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import uk.gov.courtservice.framework.services.CSServices;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.*;


/**
 * <p>Title: CaseHearingValueTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Marie Holmberg
 * @version 1.0
 */
public class CaseHearingValueTest extends TestCase
{

  private Logger log =  CSServices.getLogger(CaseHearingValueTest.class);
  //hearing values
  Integer hearingID = new Integer(999);
  Date startDate = new Date(123456789);
  Date endDate = new Date();
  //refHearingType values
  Integer refHearingTypeID = new Integer(123);
  String hearingTypeCode = "TRI";
  String hearingTypeDesc = "Trial Hearing";

  //hearing values
  Integer hearingID2 = new Integer(111);
  Date startDate2 = new Date(23456);
  Date endDate2 = new Date(567890);
  //refHearingType values
  Integer refHearingTypeID2 = new Integer(456);
  String hearingTypeCode2 = "SENT";
  String hearingTypeDesc2 = "Sentence Hearing";

  HearingValue value = new HearingValue();
  HearingValue value2 = new HearingValue();



  public CaseHearingValueTest(String s)
  {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

public void testCaseHearingValue()
{
   Integer caseID = new Integer(99);
   Integer caseNumber = new Integer(20029876);
   String caseType = "A";
   String caseSubType = "O";
   Integer courtID = new Integer(3);
   Collection hearingValues = this.getHearingValues();

   CaseHearingValue caseHrgValue = new CaseHearingValue();
   caseHrgValue.setCaseID(caseID);
   caseHrgValue.setCaseNumber(caseNumber);
   caseHrgValue.setCaseSubType(caseSubType);
   caseHrgValue.setCaseType(caseType);
   caseHrgValue.setCourtID(courtID);
   caseHrgValue.setHearingValues(hearingValues);

   log.debug("caseID :" + caseHrgValue.getCaseID() +", "+ caseID);
   this.assertEquals(caseHrgValue.getCaseID(), caseID);

   log.debug("caseNumber :" + caseHrgValue.getCaseNumber() +", "+ caseNumber);
   this.assertEquals(caseHrgValue.getCaseNumber(), caseNumber);

   log.debug("caseType :" + caseHrgValue.getCaseType() +", "+ caseType);
   this.assertEquals(caseHrgValue.getCaseType(), caseType);

   log.debug("caseSubType :" + caseHrgValue.getCaseSubType() +", "+ caseSubType);
   this.assertEquals(caseHrgValue.getCaseSubType(), caseSubType);

   log.debug("courtID :" + caseHrgValue.getCourtID() +", "+ courtID);
   this.assertEquals(caseHrgValue.getCourtID(), courtID);

   Iterator it = caseHrgValue.getHearingValues().iterator();
   while(it.hasNext())
   {
     HearingValue value = (HearingValue)it.next();

     if(value.getHearingID().equals(hearingID))
     {
       log.debug("First value");
       log.debug("End date : " + value.getEndDate().toString() +", "+ endDate.toString());
       this.assertEquals(value.getEndDate(), endDate);

       log.debug("Hearing ID :" + value.getHearingID() +", "+ hearingID);
       this.assertEquals(value.getHearingID(), hearingID);

       log.debug("hearingTypeCode :" + value.getHearingTypeCode() +", "+ hearingTypeCode);
       this.assertEquals(value.getHearingTypeCode(), hearingTypeCode);

       log.debug("hearingTypeDesc :" + value.getHearingTypeDesc() +", "+ hearingTypeDesc);
       this.assertEquals(value.getHearingTypeDesc(), hearingTypeDesc);

       log.debug("refHearingTypeID :" + value.getRefHearingTypeID() +", "+ refHearingTypeID);
       this.assertEquals(value.getRefHearingTypeID(), refHearingTypeID);

       log.debug("Start date : " + value.getStartDate().toString() +", "+ startDate.toString());
       this.assertEquals(value.getStartDate(), startDate);
     }
     else if(value.getHearingID().equals(hearingID2))
     {
       log.debug("Second value");
       log.debug("End date : " + value.getEndDate().toString() +", "+ endDate2.toString());
       this.assertEquals(value.getEndDate(), endDate2);

       log.debug("Hearing ID :" + value.getHearingID() +", "+ hearingID2);
       this.assertEquals(value.getHearingID(), hearingID2);

       log.debug("hearingTypeCode :" + value.getHearingTypeCode() +", "+ hearingTypeCode2);
       this.assertEquals(value.getHearingTypeCode(), hearingTypeCode2);

       log.debug("hearingTypeDesc :" + value.getHearingTypeDesc() +", "+ hearingTypeDesc2);
       this.assertEquals(value.getHearingTypeDesc(), hearingTypeDesc2);

       log.debug("refHearingTypeID :" + value.getRefHearingTypeID() +", "+ refHearingTypeID2);
       this.assertEquals(value.getRefHearingTypeID(), refHearingTypeID2);

       log.debug("Start date : " + value.getStartDate().toString() +", "+ startDate2.toString());
       this.assertEquals(value.getStartDate(), startDate2);
     }
   }

}


private Collection getHearingValues()
{
  value.setEndDate(endDate);
  value.setHearingID(hearingID);
  value.setHearingTypeCode(hearingTypeCode);
  value.setHearingTypeDesc(hearingTypeDesc);
  value.setRefHearingTypeID(refHearingTypeID);
  value.setStartDate(startDate);


  value2.setEndDate(endDate2);
  value2.setHearingID(hearingID2);
  value2.setHearingTypeCode(hearingTypeCode2);
  value2.setHearingTypeDesc(hearingTypeDesc2);
  value2.setRefHearingTypeID(refHearingTypeID2);
  value2.setStartDate(startDate2);

  Vector values = new Vector();
  values.add(value);
  values.add(value2);

  return values;
}


}
