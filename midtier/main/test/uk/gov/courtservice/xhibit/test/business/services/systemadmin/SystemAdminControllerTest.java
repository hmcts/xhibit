//package uk.gov.courtservice.xhibit.test.business.services.systemadmin;
//
//import java.util.Collection;
//import java.util.Iterator;
//import java.util.Properties;
//
//import javax.naming.Context;
//import javax.security.auth.Subject;
//import javax.security.auth.login.LoginException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.security.login.JAASLoginHelper;
//import uk.gov.courtservice.framework.security.login.UserNamePasswordURLCallbackHandler;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefController;
//import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerHome;
//import uk.gov.courtservice.xhibit.business.services.systemadmin.helper.RefCourtHelper;
//import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.CourtRoomBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.CourtSiteBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefAdvocateComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtReporterComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefJudgeBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefJusticeBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefSolicitorFirmComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorBasicValue;
//import uk.gov.courtservice.xhibit.business.vos.entities.SolicitorComplexValue;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.AbstractSearchCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtRoomCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.CourtSiteCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAdvocateCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefCourtReporterCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJudgeCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefJusticeCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSolicitorFirmCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
//import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.SolicitorCriteria;
//
///**
// * This tests the System Admin Controller - which comprises BisRefController & SysRefController.
// * @author Jem Marsh
// */
//public class SystemAdminControllerTest extends TestCase
//{
//  private static final Logger logger = CSServices.getLogger(SystemAdminControllerTest.class);
//  private BisRefController bisRefController = null;
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public SystemAdminControllerTest(String aString) {
//    super(aString);
//    // login...
//
//    try
//    {
//      System.setProperty("java.security.auth.login.config", "file:c:/xhibit_jaas.config");
//      UserNamePasswordURLCallbackHandler user = new UserNamePasswordURLCallbackHandler("barney", "password", "t3://localhost:7001");
//      JAASLoginHelper jl = new JAASLoginHelper(user);
//      Subject s = jl.login();
//      Properties p = new Properties();
//      p.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
//      p.put(Context.PROVIDER_URL, "t3://localhost:7001");
//    }
//    catch (LoginException ex)
//    {
//      ex.printStackTrace();
//      fail();
//    }
//  }
//
//  private void debug(String message) {
//
//    if( logger.isDebugEnabled() ) {
//      logger.debug( message );
//    }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  private BisRefController getBisRefController() {
//
//    if (this.bisRefController == null ) {
//      this.bisRefController = (BisRefController) CSServices.getEJBServices().createRemoteSession(BisRefControllerHome.class);
//    }
//    return this.bisRefController;
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourts() {
//
//    this.debug("START: testFindCourts() by Primary Key == 1");
//    try {
//      Integer key = new Integer(1);
//      CourtCriteria criteria = new CourtCriteria();
//      criteria.setPrimaryKey(key);
//      //criteria.setCourtId(key);
//      Collection data = this.getBisRefController().findCourts(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Courts found");
//      } else {
//        this.debug("*** " + data.size() + " Courts found...");
//        Iterator allData = data.iterator();
//        CourtBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (CourtBasicValue) allData.next();
//          this.debug("name" + eachValue.getCourtName() + ".");
//          this.debug("display name" + eachValue.getDisplayName() + ".");
//        }
//      }
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtsByQuery() {
//
//    this.debug("START: testFindCourts() by Name = 'OLD*' ");
//    try {
//      CourtCriteria criteria = new CourtCriteria();
//      criteria.setCourtName("OLD*");
//      Collection data = this.getBisRefController().findCourts(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Courts found");
//      } else {
//        this.debug("*** " + data.size() + " Courts found...");
//        Iterator allData = data.iterator();
//        CourtBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (CourtBasicValue) allData.next();
//          this.debug("name" + eachValue.getCourtName());
//        }
//      }
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtRooms() {
//
//    this.debug("START: testFindCourtRooms() by Primary Key == 6 ");
//    try {
//      Integer key = new Integer(6);
//      CourtRoomCriteria criteria = new CourtRoomCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findCourtRooms(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Court Rooms found");
//      } else {
//        this.debug("*** " + data.size() + " Court Rooms found...");
//        Iterator allData = data.iterator();
//        CourtRoomBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (CourtRoomBasicValue) allData.next();
//          this.debug("name = " + eachValue.getCourtRoomName() + ".");
//          this.debug("display name = " + eachValue.getDisplayName() + ".");
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtRoomsByCourt() {
//
//    this.debug("START: testFindCourtRooms() by Court ID == 1. We expect more than one, otherwise the test fails. ");
//    try {
//      CourtRoomCriteria criteria = new CourtRoomCriteria();
//      criteria.setCourtSiteId("1");
//      Collection data = this.getBisRefController().findCourtRooms(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Court Rooms found");
//      } else {
//        int numFound = data.size();
//        if (numFound > 1) {
//          this.debug("*** " + data.size() + " Court Rooms found...");
//          Iterator allData = data.iterator();
//          CourtRoomBasicValue eachValue = null;
//          while (allData.hasNext()) {
//            eachValue = (CourtRoomBasicValue) allData.next();
//            this.debug("name = " + eachValue.getCourtRoomName() + ".");
//          }
//        } else {
//          this.debug("*** " + data.size() + " Court Rooms found. Expected MORE than this.");
//          fail();
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtSites() {
//
//    this.debug("START: testFindCourtSites() by Primary Key == 1");
//    try {
//      Integer key = new Integer(1);
//      CourtSiteCriteria criteria = new CourtSiteCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findCourtSites(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Court Sites found");
//      } else {
//        this.debug("*** " + data.size() + " Court Sites found...");
//        Iterator allData = data.iterator();
//        CourtSiteBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (CourtSiteBasicValue) allData.next();
//          this.debug("name = " + eachValue.getCourtSiteName() + ".");
//          this.debug("display name = " + eachValue.getDisplayName() + ".");
//          this.debug("courtID = " + eachValue.getCourtId() + ".");
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindCourtSitesByCourtId() {
//
//    this.debug("START: testFindCourtSites with CourtId == 1 ");
//    try {
//      Integer courtId = new Integer(1);
//      CourtSiteCriteria criteria = new CourtSiteCriteria();
//      criteria.setCourtId(courtId.toString());
//      Collection data = this.getBisRefController().findCourtSites(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Court Sites found");
//      } else {
//        this.debug("*** " + data.size() + " Court Sites found...");
//        Iterator allData = data.iterator();
//        Object each = null;
//        CourtSiteBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          each = allData.next();
//          this.debug("Data type: " + each.getClass().toString());
//          eachValue = (CourtSiteBasicValue) each;
//          this.debug("name = " + eachValue.getCourtSiteName() + ".");
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
///**
// * Test functionality of RefSolicitorFirm
// */
//  public void testFindSolicitorFirms()
//  {
//    this.debug("START: testFindSolicitorFirms  ");
//    try{
//      RefSolicitorFirmCriteria criteria = new RefSolicitorFirmCriteria();
//      criteria.setSolicitorFirmName("ASLAM CO");
//      Collection results = this.getBisRefController().findSolicitorFirms(criteria);
//      this.debug("found firms#:"+results.size());
//
//      Iterator data = results.iterator();
//      RefSolicitorFirmComplexValue eachValue = null;
//      while(data.hasNext())
//      {
//        eachValue=(RefSolicitorFirmComplexValue) data.next();
//        this.debug("AddressId: "+ eachValue.getAddressId() +"Address is null:"+ (eachValue.getAddressId() == null));
//        this.debug("Address postcode: "+eachValue.getPostcode());
//      }
//    }
//    catch(Exception e)
//    {
//      this.debug("An Exception was thrown..." + e.getMessage());
//      e.printStackTrace();
//      fail();
//    }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindJudges() {
//
//    this.debug("START: testFindJudges() by Primary Key == 389");
//    try {
//      Integer key = new Integer(389);
//      RefJudgeCriteria criteria = new RefJudgeCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findJudges(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Judges found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " Judges found...");
//        Iterator allData = data.iterator();
//        RefJudgeBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefJudgeBasicValue) allData.next();
//          this.debug("Full Title (1) = " + eachValue.getFullListTitle1() + ".");
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindJustices() {
//
//    this.debug("START: testFindJustices() by Primary Key == 324");
//    try {
//      Integer key = new Integer(324);
//      RefJusticeCriteria criteria = new RefJusticeCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findJustices(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Justices found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " Justices found...");
//        Iterator allData = data.iterator();
//        RefJusticeBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefJusticeBasicValue) allData.next();
//          this.debug("CourtId = " + eachValue.getCourtId() + ".");
//          this.debug("CrestJusticeId = " + eachValue.getCrestJusticeId() + ".");
//          this.debug("Initials = " + eachValue.getInitials() + ".");
//          this.debug("Justice name = " + eachValue.getJusticeName() + ".");
//          this.debug("PsdCourtCode= " + eachValue.getPsdCourtCode() + ".");
//          this.debug("Ttile= " + eachValue.getTitle() + ".");
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindAdvocates() {
//
//    this.debug("START: testFindAdvocates() by Primary Key == 440");
//    try {
//      Integer key = new Integer(440);
//      RefAdvocateCriteria criteria = new RefAdvocateCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findAdvocates(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Advocates found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " Advocate found...");
//        Iterator allData = data.iterator();
//        RefAdvocateBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefAdvocateBasicValue) allData.next();
//          this.debug("Year called = " + eachValue.getyearOfCall() + "."); /** @todo getyearOfCall should be getYearOfCall */
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindComplexAdvocates() {
//
//    this.debug("START: testFindComplexAdvocates()");
//    try {
//      Integer key = new Integer(440);
//      RefAdvocateCriteria criteria = new RefAdvocateCriteria();
//
//  /* Search using primary key */
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setPrimaryKey(key);
//      performComplexAdvocateSearch(criteria);
//
//  /* Search using first name */
//      criteria = new RefAdvocateCriteria();
//      criteria.setFirstName("ELLIOTT");
//      criteria.setDetailIndicator("key");
//      performComplexAdvocateSearch(criteria);
//
//  /* Search using primary key but return address also */
//      criteria = new RefAdvocateCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setPrimaryKey(key);
//      performComplexAdvocateSearch(criteria);
//
//                /* Search using chamber firm name but return address also */
//      criteria = new RefAdvocateCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setChamberFirmName("BELL%");
//      performComplexAdvocateSearch(criteria);
//
//
//
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  private void performComplexAdvocateSearch(RefAdvocateCriteria criteria) throws Exception
//  {
//    Collection data = this.getBisRefController().findAdvocates(criteria);
//
//    if (data.isEmpty())
//    {
//      this.debug("No Advocates found");
//      fail();
//    }
//    else
//    {
//      this.debug("*** " + data.size() + " Advocate found...");
//      Iterator allData = data.iterator();
//      RefAdvocateComplexValue eachValue = null;
//      while (allData.hasNext())
//      {
//        eachValue = (RefAdvocateComplexValue) allData.next();
//        this.debug("Advocate ID = " + eachValue.getId() + ".");
//        this.debug("Year called = " + eachValue.getyearOfCall() + "."); /** @todo getyearOfCall should be getYearOfCall */
//
//
//        this.debug("Chamber Firm name"+eachValue.getFirmName());
//        //this.debug("Chamber Address ID"+eachValue.getAddressId());
//        this.debug("Chamber Court ID"+eachValue.getCourtId());
//        if(criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS))
//        {
//          if(eachValue.getAddress1() == null) fail("Address object not included");
//          this.debug("Address, County:"+eachValue.getCounty());
//          this.debug("Address, address1:"+eachValue.getAddress1());
//        }
//      }
//    }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindSolicitors() {
//
//    this.debug("START: testFindSolicitors() by Primary Key == 4 (Douglas Adams)");
//    try {
//      Integer key = new Integer(4);
//      SolicitorCriteria criteria = new SolicitorCriteria();
//      criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findSolicitors(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Solicitors found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " Solicitor found...");
//        Iterator allData = data.iterator();
//        SolicitorBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (SolicitorBasicValue) allData.next();
//          this.debug("Name = " + eachValue.getCrestSolicitorName() + ".");
//          this.debug("Legal Rep Id = " + eachValue.getLegalRepId());
//          this.debug("Firm Id = " + eachValue.getFirmId());
//        }
//      }
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindComplexSolicitors() {
//
//    this.debug("START: testFindComplexSolicitors() by Primary Key == 4 (Douglas Adams)");
//    try {
//      Integer key = new Integer(4);
//      SolicitorCriteria criteria = new SolicitorCriteria();
//
//  /* search using primary key */
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setPrimaryKey(key);
//      performComplexSolicitorSearch(criteria);
//
//  /* search using firstname */
//      criteria = new SolicitorCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setFirstName("Marie");
//      performComplexSolicitorSearch(criteria);
//
//  /* search using initials */
//      criteria = new SolicitorCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setInitials("U");
//      performComplexSolicitorSearch(criteria);
//
//  /* search using surname */
//      criteria = new SolicitorCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setSurname("Holmberg");
//      performComplexSolicitorSearch(criteria);
//
//  /* search using crestsolicitorname */
//      debug("\nSearch using crest solicitor name\n");
//      criteria = new SolicitorCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setCrestSolicitorName("Marie Solicitor");
//      performComplexSolicitorSearch(criteria);
//
//  /* search using solicitor firm name */
//      debug("\nSearch using solicitor firm name\n");
//      criteria = new SolicitorCriteria();
//      criteria.setDetailIndicator(AbstractSearchCriteria.ADDRESS);
//      criteria.setSolicitorFirmName("S.K. NOEL & CO.");
//      debug("\nperforming search\n");
//      performComplexSolicitorSearch(criteria);
//
//
//  /* search using solicitor firm name */
////		debug("\nSearch using solicitor firm name\n");
////		criteria = new SolicitorCriteria();
////		criteria.setDetailIndicator(criteria.ADDRESS);
////		debug("\nSetting solicitor firm name\n");
//      //criteria.setSolicitorFirmName("HANCOCK QUINS SOLICITORS");
////		criteria.setSolicitorFirmName("SIMMONS");
////		debug("\nperforming search\n");
////		performComplexSolicitorSearch(criteria);
//
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  private void performComplexSolicitorSearch(SolicitorCriteria criteria) throws Exception
//  {
//    Collection data = this.getBisRefController().findSolicitors(criteria);
//
//    if (data.isEmpty()) {
//      this.debug("No Solicitors found");
//      fail();
//    } else {
//      this.debug("*** " + data.size() + " Solicitor found...");
//      Iterator allData = data.iterator();
//      SolicitorComplexValue eachValue = null;
//      while (allData.hasNext()) {
//        eachValue = (SolicitorComplexValue) allData.next();
//        this.debug("Name = " + eachValue.getCrestSolicitorName() + ".");
//        if(eachValue.getFirm() == null) fail();
//        this.debug("Solicitor.solitiorFirm.Name = " + eachValue.getFirm().getSolicitorFirmName() + ".");
//        if(criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS))
//        {
//          if(eachValue.getFirm().getAddressId() == null) fail("Address not specified");
//          this.debug("Solicitor.solitiorFirm.address.line1 = " + eachValue.getFirm().getAddress1());
//
//        }
//      }
//    }
//
//  }
//
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindOffences() {
//
//    this.debug("START: testFindOffences() by Primary Key == 9238 (Failure to assist...)");
//    try
//    {
//      RefOffenceCriteria criteria = new RefOffenceCriteria();
//      //criteria.setCourtId("3");
//      //performFindOffences(criteria);
//      // returns all records (currently 9576) - too slow!
//
//      criteria = new RefOffenceCriteria();
//      criteria.setPrimaryKey(new Integer(9238));
//      performFindOffences(criteria);
//
//      criteria = new RefOffenceCriteria();
//      criteria.setOffenceCode("WC81281");
//      performFindOffences(criteria);
//
//      criteria = new RefOffenceCriteria();
//      criteria.setOffenceDescription("Permitting%");
//      performFindOffences(criteria);
//
//      criteria = new RefOffenceCriteria();
//      criteria.setStatute("Transport Act 1968");
//      performFindOffences(criteria);
//    }
//    catch (Exception anException)
//    {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  public void performFindOffences(RefOffenceCriteria criteria) throws Exception
//  {
//    Collection data = this.getBisRefController().findOffences(criteria);
//
//    if (data.isEmpty())
//    {
//      this.debug("No Offence found");
//      fail();
//    }
//    else
//    {
//      this.debug("*** " + data.size() + " Offence found...");
//      Iterator allData = data.iterator();
//      RefOffenceBasicValue eachValue = null;
//      while (allData.hasNext())
//      {
//        eachValue = (RefOffenceBasicValue) allData.next();
//        this.debug("Offence Description = " + eachValue.getOffenceDesc() + ".");
//        this.debug("Offence Code = " + eachValue.getOffenceCode() + ".");
//        this.debug("Offence Statute = " + eachValue.getStatute() + ".");
//      }
//    }
//
//
//
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
///**
// * This test WAS to read back ALL codes for a Court, but WL didn't like it - it failed with a cache full exception; not surprisingly because
// * it read over 1000 rows from the table.
// * So, now the test catches that exception AND if we get that one, we don't fail.
// */
//  public void testFindSystemCodes() {
//
//    this.debug("START: testFindSystemCodes() by Court ID (1) ");
//    try
//    {
//      RefSystemCodeCriteria criteria  = new RefSystemCodeCriteria();
//      //Search using code
//      criteria.setCode("AA");
//      performSystemCodeSearch(criteria);
//
//      //search using courtId
////      criteria  = new RefSystemCodeCriteria();
////      criteria.setCourtId("1");
////      performSystemCodeSearch(criteria);
//
//      //search using Decode
//      criteria  = new RefSystemCodeCriteria();
//      criteria.setDecode("46");
//      performSystemCodeSearch(criteria);
//
//      //search using Plea Code
//      criteria  = new RefSystemCodeCriteria();
//      criteria.setCodeTitle("PLEA CODES");
//      performSystemCodeSearch(criteria);
//
//      //search using code Type
//      criteria  = new RefSystemCodeCriteria();
//      criteria.setCodeType(RefSystemCodeCriteria.CodeType.PB_TYPE);
//      performSystemCodeSearch(criteria);
//
//    }
//    catch (Exception anException)
//    {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//        fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  private void performSystemCodeSearch(RefSystemCodeCriteria criteria) throws Exception
//  {
//      Collection data = this.getBisRefController().findSystemCodes(criteria);
//      if (data.isEmpty())
//      {
//        this.debug("No System Codes found");
//        fail();
//      }
//      else
//      {
//        this.debug("*** " + data.size() + " System Code(s) found...");
//        Iterator allData = data.iterator();
//        RefSystemCodeBasicValue eachValue = null;
//        while (allData.hasNext())
//        {
//          eachValue = (RefSystemCodeBasicValue) allData.next();
//          this.debug("Code title: " + eachValue.getCodeTitle() + ".");
//        }
//      }
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindPleas() {
//
//    this.debug("START: testFindPleas() (System Codes by Type == 'PLEA') ");
//    try {
//      RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
//      criteria.setCodeType(RefSystemCodeCriteria.CodeType.PLEA);
//      Collection data = this.getBisRefController().findSystemCodes(criteria);
//      if (data.isEmpty()) {
//        this.debug("No System Codes of type 'PLEA' found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " System Code(s) found...");
//        Iterator allData = data.iterator();
//        RefSystemCodeBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefSystemCodeBasicValue) allData.next();
//          this.debug("Code title & description: " + eachValue.getCodeTitle() + " - " + eachValue.getDecode() + ".");
//        }
//      }
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
///* ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testFindVerdicts() {
//
//    this.debug("START: testFindVerdicts() (System Codes by Type == 'VERDICT') for Court ID = 1 ");
//    try {
//      RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
//      criteria.setCodeType(RefSystemCodeCriteria.CodeType.VERDICT);
//      criteria.setCourtId("3");
//      Collection data = this.getBisRefController().findSystemCodes(criteria);
//      if (data.isEmpty()) {
//        this.debug("No System Codes of type 'VERDICT' found");
//        fail();
//      } else {
//        this.debug("*** " + data.size() + " Verdict System Code(s) found for Court 1 ...");
//        Iterator allData = data.iterator();
//        RefSystemCodeBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefSystemCodeBasicValue) allData.next();
//          this.debug("Code title & description: " + eachValue.getCodeTitle() + " - " + eachValue.getDecode() + ".");
//        }
//      }
//    } catch (Exception anException) {
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      anException.printStackTrace();
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  public void testFindRefCourts() {
//
//    this.debug("START: testFindCourts() by Primary Key == 1");
//    try {
//      RefCourtCriteria criteria = new RefCourtCriteria();
//      //criteria.setCourtName("A%");
//      criteria.setCourtId("1");
//      //criteria.setShortName("BIGGY");
//      //criteria.setPrimaryKey(key);
//      Collection data = this.getBisRefController().findCourts(criteria);
//
//      if (data.isEmpty()) {
//        this.debug("No Courts found");
//      } else {
//        this.debug("*** " + data.size() + " Courts found...");
//        Iterator allData = data.iterator();
//        RefCourtBasicValue eachValue = null;
//        while (allData.hasNext()) {
//          eachValue = (RefCourtBasicValue) allData.next();
//          this.debug("name" + eachValue.getCourtShortName() + ".");
//          this.debug("addressId" + eachValue.getAddressId() + ".");
//        }
//      }
//    } catch (Exception anException) {
//      anException.printStackTrace();
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//
//  }
//
//  public void testFindComplexRefCourts() {
//
//    this.debug("START: testFindComplexRefCourts()");
//    try {
//      Integer key = new Integer(1);
//      RefCourtCriteria criteria = new RefCourtCriteria();
//
//      debug("complex refCourt search using court Id");
//      criteria.setDetailIndicator("complex");
//      criteria.setCourtId("1");
//      performComplexRefCourtSearch(criteria);
//
//      debug("complex refCourt search using court Name");
//      criteria = new RefCourtCriteria();
//      criteria.setDetailIndicator("complex");
//      criteria.setCourtName("A%");
//      performComplexRefCourtSearch(criteria);
//
//      debug("complex refCourt search using short Name");
//      criteria = new RefCourtCriteria();
//      criteria.setDetailIndicator("complex");
//      criteria.setShortName("BIGGY");
//      performComplexRefCourtSearch(criteria);
//
//      debug("complex refCourt search using primary key");
//      criteria = new RefCourtCriteria();
//      criteria.setDetailIndicator("complex");
//      criteria.setPrimaryKey(key);
//      performComplexRefCourtSearch(criteria);
//
//
//    } catch (Exception anException) {
//      anException.printStackTrace();
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//
//  }
//
//  private void performComplexRefCourtSearch(RefCourtCriteria criteria) throws Exception
//  {
//    Collection data = this.getBisRefController().findCourts(criteria);
//
//    if (data.isEmpty())
//    {
//      this.debug("No Courts found");
//    }
//    else
//    {
//      this.debug("*** " + data.size() + " Courts found...");
//      Iterator allData = data.iterator();
//      RefCourtComplexValue eachValue = null;
//      while (allData.hasNext())
//      {
//        eachValue = (RefCourtComplexValue) allData.next();
//        this.debug("name" + eachValue.getCourtShortName() + ".");
//        this.debug("addressId" + eachValue.getAddressId() + ".");
//        if(! criteria.isBasicRequest())
//        {
//          if(eachValue.getAddress() == null) fail();
//          this.debug("refcourt.address.addressline1" + eachValue.getAddress().getAddress1() + ".");
//        }
//      }
//    }
//  }
//
//  public void testFindRefCourtReporters()
//  {
//    this.debug("START: testFindContactDetail()");
//    try {
//      RefCourtReporterCriteria criteria = new RefCourtReporterCriteria();
//
//      debug("complex refcourtReporter search using primary key:712");
//      criteria.setDetailIndicator("complex");
//      criteria.setPrimaryKey(new Integer(712));
//      performComplexRefCourtReporterSearch(criteria);
//
//      debug("complex refcourtReporter search using surname: DYSON");
//      criteria = new RefCourtReporterCriteria();
//      criteria.setDetailIndicator("complex");
//      criteria.setSurname("DYSON");
//      performComplexRefCourtReporterSearch(criteria);
//
//      debug("nested complex refcourtReporter search using primary key:712");
//      criteria = new RefCourtReporterCriteria();
//      criteria.setDetailIndicator("ADDRESS");
//      criteria.setSurname("DYSON");
//      performComplexRefCourtReporterSearch(criteria);
//
//
//
//    }
//    catch (Exception anException)
//    {
//      anException.printStackTrace();
//      this.debug("An Exception was thrown..." + anException.getMessage());
//      fail();
//    }
//    this.debug("END: Test complete.");
//    this.debug("");
//  }
//
//  private void performComplexRefCourtReporterSearch(RefCourtReporterCriteria criteria) throws Exception
//  {
//    RefCourtHelper helper = new RefCourtHelper();
//    Collection data = helper.findCourtReporters(criteria);
//
//    if (data.isEmpty())
//    {
//      this.debug("No Courts found");
//    }
//    else
//    {
//      this.debug("*** " + data.size() + " Reporters found...");
//      Iterator allData = data.iterator();
//      RefCourtReporterComplexValue eachValue = null;
//      while (allData.hasNext())
//      {
//        eachValue = (RefCourtReporterComplexValue) allData.next();
//        this.debug("ID:" + eachValue.getId() + ".");
//        this.debug("Surname" + eachValue.getSurname() + ".");
//        if(! criteria.isBasicRequest())
//        {
//          if(eachValue.getRefCourtReporterFirm() == null) fail("Firm not set");
//          this.debug("refCourtReporter.refCourtReporterFirm.ID" + eachValue.getRefCourtReporterFirm().getId() + ".");
//          this.debug("refCourtReporter.refCourtReporterFirm.FirmName" + eachValue.getRefCourtReporterFirm().getFirmName() + ".");
//          if(criteria.getDetailIndicator().equals(AbstractSearchCriteria.ADDRESS))
//          {
//            if(eachValue.getRefCourtReporterFirm().getAddress() == null) fail("Address not set");
//            this.debug("refCourtReporter.refCourtReporterFirm.FirmName.address" + eachValue.getRefCourtReporterFirm().getAddress().getAddress1() + ".");
//          }
//        }
//      }
//    }
//  }
//
//
//
///* -----------------------------------------------------------------------------------------------------------------------------------------------------
//                             TEST CREATE METHODS
// ----------------------------------------------------------------------------------------------------------------------------------------------------- */
//
//  public void testCreateSolicitor() {
//
//    this.debug("START: testCreateSolicitor()");
//    try{
//      SolicitorBasicValue basicValue = new SolicitorBasicValue();
//      basicValue.setCourtId(new Integer(1));
//      basicValue.setInCrest("Y");
//      basicValue.setFirmId(new Integer(1154));
//      basicValue.setFirstName("John");
//      basicValue.setMiddleName("Albert");
//      basicValue.setSurname("Doe");
//      basicValue.setInitials("JAD");
//      basicValue.setTitle("Sir");
//      basicValue.setCrestSolicitorName("Crest Name");
//      debug("calling createSolicitor with initials:"+basicValue.getInitials());
//      SolicitorBasicValue result = this.getBisRefController().createSolicitor(basicValue);
//      debug("Created solicitor with id:" + result.getId());
//      debug("And with Legal Rep id:" + result.getLegalRepId());
//    }
//    catch(Exception e)
//    {
//      debug("Exception caught during testCreateSolicitor");
//      e.printStackTrace();
//      fail();
//     }
//
//
//    this.debug("END: Test complete.");
//    this.debug("");
//
//  }
//
//
//}