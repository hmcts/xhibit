//
//package uk.gov.courtservice.xhibit.test.business.services.counselfacilities;
//
//import junit.framework.*;
//import java.util.*;
//import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.*;
//import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesHelper;
//
///**
// * <p>Title: TestCounselFacilitiesHelperTest</p>
// * <p>Description: Test class to test the counselfacility. </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// * @author Marie Holmberg
// *
// * @todo - When we have a test database we need to set up setUp() scripts (if
// * necessary) and the assertEquals().
// *
// * @version 1.0
// */
//public class TestCounselFacilitiesHelperTest extends TestCase {
//
//  public TestCounselFacilitiesHelperTest(String s) {
//    super(s);
//  }
//
//  protected void setUp() {
//      /** @todo do we need specific data or will everything be in the database. */
//  }
//
//  protected void tearDown() {
//  }
//
//
//  /**
//   * testGetAssignRepresentatives()
//   * This will return a list of all the listed cases, defendants and their legal
//   * representatives (if any) for the given date.
//   */
//  public void testGetAssignRepresentatives() {
//    CounselFacilitiesHelper counselfacilitieshelper = new CounselFacilitiesHelper();
//    Integer courtId1=  null  /** @todo fill in non-null value */;
//    Date scheduleDate2=  null  /** @todo fill in non-null value */;
//    Integer courtRoomId3=  null  /** @todo fill in non-null value */;
//    try {
//      Collection collectionRet = counselfacilitieshelper.getAssignRepresentatives(courtId1, scheduleDate2, courtRoomId3);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//
//  /**
//   * testSearchForCounsel()
//   * This will return a list of counsels listed for that day that matches the
//   * search criteria.
//   *
//   * IMPORTANT NOTE: the search is only for counsel firstname and/or surname
//   * NOT any of the other criterias the SearchCounselFacilitiesCriteria contains
//   * such as courtId, courtroomId, partyoncase.
//   */
//  public void testSearchForCounsel() {
//    CounselFacilitiesHelper counselfacilitieshelper = new CounselFacilitiesHelper();
//    SearchCounselFacilitiesCriteria criteria1=  null  /** @todo fill in non-null value */;
//    try {
//      Collection collectionRet = counselfacilitieshelper.searchForCounsel(criteria1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//
//  /**
//   * testSearchForDefendant()
//   * This will return a list of all the defendants and their representatives
//   * listed for that day that matches the search criteria.
//   *
//   * IMPORTANT NOTE: the search is only for defendant firstname and/or surname
//   * NOT any of the other criterias the SearchCounselFacilitiesCriteria contains
//   * such as courtId, courtroomId, partyoncase.
//   */
//  public void testSearchForDefendant() {
//    CounselFacilitiesHelper counselfacilitieshelper = new CounselFacilitiesHelper();
//    SearchCounselFacilitiesCriteria criteria1=  null  /** @todo fill in non-null value */;
//    try {
//      Collection collectionRet = counselfacilitieshelper.searchForDefendant(criteria1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//
//  /**
//   * testSetAssignRepresentatives()
//   * This will create new entries in XHB_SH_LEG_REP table. It will sign in the
//   * legal representative for a case if for prosecution and for a defendant if
//   * for defence.
//   *
//   * Note: several sign ins can be done at the same time so therefore a collection
//   * of shLegRepBasicValues will be used.
//   */
//  public void testSetAssignRepresentatives() {
//    CounselFacilitiesHelper counselfacilitieshelper = new CounselFacilitiesHelper();
//
//    //prosecutor - solicitor
//    SHLegRepBasicValue value1 = new SHLegRepBasicValue();
//    value1.setCcInfoID(new Integer(0)); /** @todo change this id to what ever is in the db */
//    value1.setLegalRole("P");
//    value1.setRefLegalRepID(new Integer(0)); /** @todo add id here */
//    value1.setCrestSequenceNo( null ); //not required.
//    value1.setIsSignIn( "Y" );
//    value1.setRefDefenceCategoryID( null ); //not required.
//    value1.setSchedHearDefID( null );//not required for prosecution
//    value1.setScheduledHearingID( new Integer(0)); /** @todo add id here */
//    value1.setRefSolicitorFirmID(new Integer(0)); /** @todo add id here */
//    value1.setSolFirmOrRefLegalRep("S");
//
//    //defence - advocate
//    SHLegRepBasicValue value2 = new SHLegRepBasicValue();
//    value2.setCcInfoID(new Integer(0)); /** @todo change this id to what ever is in the db */
//    value2.setLegalRole("D");
//    value2.setRefLegalRepID(new Integer(0)); /** @todo add id here */
//    value2.setCrestSequenceNo( null ); //not required.
//    value2.setIsSignIn( "Y" );
//    value2.setRefDefenceCategoryID( null ); //not required.
//    value2.setSchedHearDefID(new Integer(0)); /** @todo add id here */
//    value2.setScheduledHearingID( new Integer(0)); /** @todo add id here */
//    value2.setRefSolicitorFirmID(new Integer(0)); /** @todo add id here */
//    value2.setSolFirmOrRefLegalRep("L");
//
//    ArrayList values = new ArrayList();
//    values.add(value1);
//    values.add(value2);
//
//    Collection shLegRepBasicValues1 = values;
//
//    try {
//      counselfacilitieshelper.setAssignRepresentatives(shLegRepBasicValues1);
//  /** @todo:  Insert test code here.  Use assertEquals(), for example. */
//    }
//    catch(Exception e) {
//      System.err.println("Exception thrown:  "+e);
//    }
//  }
//}
//