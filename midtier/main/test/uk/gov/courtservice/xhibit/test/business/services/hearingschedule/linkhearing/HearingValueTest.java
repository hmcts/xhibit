
package uk.gov.courtservice.xhibit.test.business.services.hearingschedule.linkhearing;

import junit.framework.*;

import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import uk.gov.courtservice.framework.services.CSServices;

import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.*;


/**
 * <p>Title: HearingValueTest</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Marie Holmberg
 * @version 1.0
 */
public class HearingValueTest extends TestCase {

  private Logger log =  CSServices.getLogger(HearingValueTest.class);

  public HearingValueTest(String s) {
    super(s);
  }

  protected void setUp()
  {
  }

  protected void tearDown()
  {
  }

  public void testHearingValueTest()
  {
    //hearing values
    Integer hearingID = new Integer(999);
    Date startDate = new Date(123456789);
    Date endDate = new Date();
    //refHearingType values
    Integer refHearingTypeID = new Integer(123);
    String hearingTypeCode = "TRI";
    String hearingTypeDesc = "Trial Hearing";

    HearingValue value = new HearingValue();
    value.setEndDate(endDate);
    value.setHearingID(hearingID);
    value.setHearingTypeCode(hearingTypeCode);
    value.setHearingTypeDesc(hearingTypeDesc);
    value.setRefHearingTypeID(refHearingTypeID);
    value.setStartDate(startDate);

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
}
