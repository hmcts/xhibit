//package uk.gov.courtservice.xhibit.test.business.database.dailylist;
//
//import junit.framework.*;
//
//import org.apache.log4j.Logger;
//
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//
//import uk.gov.courtservice.xhibit.business.vos.services.dailylist.*;
//
//import uk.gov.courtservice.xhibit.business.database.query.dailylist.DailyListQuery;
//
//import uk.gov.courtservice.framework.services.CSServices;
///**
// * <p>Title: Import Export Notification Statuses Test Case</p>
// * <p>Description: The Test Case for the methods contained in the ImportExportStatusHelper.</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// * @version 1.0
// */
//
//public class TestDailyList extends TestCase
//{
//
//  private static Logger log = CSServices.getLogger(TestDailyList.class);
//
//
//  public TestDailyList(String s) {
//    super(s);
//  }
//
//  protected void setUp() {
//  }
//
//  protected void tearDown() {
//  }
//
//  public void testGetDailyList()
//  {
//    log.debug("testGetDailyList() - Will call the daily list query");
//
//    //Set up the input parameters to the query
//    Calendar cal = Calendar.getInstance();
//    cal.set(2003, 07, 07);
//    Integer courtId = new Integer(3);
//    Date date = cal.getTime();
//
//    //Call the daily list query to get the result.
//    DailyListQuery dlQuery = new DailyListQuery();
//    DailyListValue dlValue = dlQuery.getDailyList( courtId, date);
//
//    /** @todo Add the CASTOR call here..... */
//    //Castor .....
//
//    log.debug("CourtValue : " + dlValue.getCourtValue().toString());
//    log.debug("ListValue : " + dlValue.getListValue().toString());
//
//    int numberofrecords = 0;
//
//
//    ArrayList sittingIDList = new ArrayList();
//    ArrayList shIDList = new ArrayList();
//
//    //get the court site values and loop through them.
//    ArrayList csList = dlValue.getCourtSiteValues();
//    for(int i = 0; i < csList.size(); i++)
//    {
//      CourtSiteValue courtSite = (CourtSiteValue)csList.get(i);
//      //log.debug("CourtSiteValue : " + courtSite.toString());
//      //log.debug("CourtSite value has number of sittings: " + courtSite.getSittingValues().size());
//
//      ArrayList sitList = courtSite.getSittingValues();
//      for(int j = 0; j < sitList.size(); j++)
//      {
//        SittingValue sittingValue = (SittingValue)sitList.get(j);
//        sittingIDList.add(sittingValue.getSittingId());
//        //log.debug("SittingValue : " + sittingValue.toString());
//        //log.debug("Sitting value has number of scheduledhearings : " + sittingValue.getScheduledValues().size());
//        //log.debug("Sitting judge : " + sittingValue.getXhbRefJudgeBasicValue().toString());
//
//        numberofrecords = numberofrecords+sittingValue.getScheduledValues().size();
//        //log.debug("numberofrecords is now : " + numberofrecords);
//
//        ArrayList shList = sittingValue.getScheduledValues();
//        for(int k = 0; k < shList.size(); k++)
//        {
//          ScheduledValue shValue = (ScheduledValue)shList.get(k);
//          shIDList.add(shValue.getScheduledHearingId());
//
//          if(k > 2) //don't print all....
//          {
//            //log.debug("shValue : " + shValue.toString());
//            //log.debug("getRefCourtBasicValue : "+shValue.getRefCourtBasicValue().toString());
//            //log.debug("getScheduledHearingValue : "+shValue.getScheduledHearingValue().toString());
//          }
//        }
//      }
//    }
//
//    for(int i = 0; i < sittingIDList.size(); i++)
//    {
//      log.debug("SittingID is : " + (Integer)sittingIDList.get(i) + ", at : " + i );
//    }
//    log.debug(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<");
//    log.debug(">>>>>>>>>>>>>>>>>><<<<<<<<<<<<");
//
//    for(int i = 0; i < shIDList.size(); i++)
//    {
//      log.debug("SH ID is : " + (Integer)shIDList.get(i) + ", at : " + i );
//    }
//
//    log.debug("numberofrecords : "+numberofrecords);
//    if(numberofrecords == 129)
//    {
//      assertEquals(true, true);
//    }
//    else
//    {
//      assertEquals(true, false);
//    }
//    log.debug("After Daily list query");
//  }
//
//}