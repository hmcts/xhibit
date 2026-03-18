//package uk.gov.courtservice.xhibit.business.ps.services;
//
//import java.rmi.RemoteException;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.sql.Timestamp;
//import java.util.HashMap;
//import java.util.Hashtable;
//import java.util.List;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//import javax.rmi.PortableRemoteObject;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.entities.xhb_address.XhbAddressBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_contact_detail.XhbContactDetailBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_court.XhbCourtBasicValue;
//import uk.gov.courtservice.xhibit.business.entities.xhb_psr_recipient.XhbPsrRecipientBasicValue;
//import uk.gov.courtservice.xhibit.business.ps.values.PSRProbationValueSet;
//import uk.gov.courtservice.xhibit.business.ps.values.PSRRecipientValueSet;
//import uk.gov.courtservice.xhibit.business.ps.values.PSRRequestValueSet;
//
///**
// * @author  Jon Powell (Electronic Data Systems)
// * @author  Sarah Tong
// *
// * Empty test created for setting up JUnitEE
// *
// * ST - Added test for creatPSRRequest following updates to this method in
// *      the controller
// */
//
//public class TestPSControllerBean
//    extends TransactionTestCase
//{
//  private PSController controller = null;
//
//  private static final Integer CASE_ID = new Integer(22);
//  private static final Integer COURT_ID = new Integer(3);
//  private static final Integer DEF_ON_CASE_ID = new Integer(18); //4);
//  private static final String COURT_ROOM = "Court Room 1";
//  private static final Integer RECIPIENT_ID = new Integer(20);
//  private static final Integer SCHED_HEARING_ID = new Integer(200);
//
//  private static String selectAll = "SELECT * FROM XHB_PSR_REQUEST";
//  private static String deletePSRRequests = "DELETE FROM XHB_PSR_REQUEST";
//  private static String selectMaxPSR = "SELECT PSR_REQUEST_ID FROM XHB_PSR_REQUEST WHERE CREATION_DATE = (SELECT  MAX(CREATION_DATE) FROM XHB_PSR_REQUEST)";
//  private static String selectTerminal =
//      "SELECT TERMINAL_NAME FROM XHB_TERMINAL WHERE COURT_SITE_ID =" +
//      COURT_ID.intValue();
//  private static String deletePSRRecipient =
//      "DELETE FROM XHB_PSR_RECIPIENT WHERE RECIPIENT_ID = " +
//      RECIPIENT_ID.intValue();
//  private static String selectMaxAddress = "SELECT ADDRESS_ID FROM XHB_ADDRESS WHERE ADDRESS_ID =(SELECT MAX(ADDRESS_ID) FROM XHB_ADDRESS)";
//
//  private static String selectMaxContact = "SELECT CONTACT_ID FROM XHB_CONTACT_DETAIL WHERE CONTACT_ID =(SELECT MAX(CONTACT_ID) FROM XHB_CONTACT_DETAIL)";
//
//  private static String selectMaxRecipient = "SELECT RECIPIENT_ID FROM XHB_PSR_RECIPIENT WHERE CREATION_DATE = (SELECT  MAX(CREATION_DATE) FROM XHB_PSR_RECIPIENT)";
//
//  private Logger log = CSServices.getLogger(TestPSControllerBean.class);
//
//  /**
//   * Create new test class
//   * @param testName  will be passed in by JUnit runner
//   */
//  public TestPSControllerBean(String testName) throws NamingException
//  {
//    super(testName, true);
//  }
//
//  /**
//   * Initialise variables common to each test. Re-run before each test.
//   */
//  protected void setUp() throws Exception
//  {
//    super.setUp();
//
//    Statement stmt = connection.createStatement();
//    stmt.executeUpdate(deletePSRRequests);
//
//    try
//    {
//      Hashtable props = new Hashtable();
//      props.put(Context.INITIAL_CONTEXT_FACTORY,
//                "weblogic.jndi.WLInitialContextFactory");
//      props.put(Context.PROVIDER_URL, "t3://midtier:7001");
//      props.put(Context.SECURITY_PRINCIPAL, "xhibit_internal");
//      props.put(Context.SECURITY_CREDENTIALS, "password");
//
//      InitialContext initialContext = new InitialContext(props);
//      PSControllerHome home =
//          (PSControllerHome) PortableRemoteObject.narrow(
//          initialContext.lookup("PSControllerHome"), PSControllerHome.class);
//      controller = home.create();
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//
//  }
//
//  /**
//   * Create a PSR request and check the record is created correctly
//   */
//  public void testCreatePSRRequest()
//  {
//    try
//    {
//      // create the PSR Request record
//      controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      // retrieve the record and ensure the correct details have been set
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(selectAll);
//
//      boolean foundRecord = false;
//      while (rs.next())
//      {
//        if (foundRecord)
//        {
//          fail("Multiple PSR Request records found");
//        }
//        assertEquals("Incorrect PSR Request Status",
//                     PSControllerBean.NEW_PSR_REQUEST_STATUS,
//                     rs.getString("PSR_STATUS"));
//        assertEquals("Incorrect defendantOnCaseId",
//                     DEF_ON_CASE_ID.intValue(),
//                     rs.getInt("DEFENDANT_ON_CASE_ID"));
//        foundRecord = true;
//      }
//
//      if (!foundRecord)
//      {
//        fail("PSR Request record not created.");
//
//      }
//      rs.close();
//      stmt.close();
//    }
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//  }
//
//  public void testFindAllIssuedRequests() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//      // create the PSR Request record
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      //get id of new PSR request
//      int id = 0;
//      Statement stmt = connection.createStatement();
//
//      ResultSet rs = stmt.executeQuery(selectMaxPSR);
//      if (rs.next())
//      {
//        id = rs.getInt("PSR_REQUEST_ID");
//      }
//      rs.close();
//      String query =
//          "UPDATE XHB_PSR_REQUEST SET PSR_STATUS = " + "'" +
//          PSControllerBean.ISSUED_PSR_REQUEST_STATUS + "'" +
//          " WHERE PSR_REQUEST_ID=" +
//          id;
//      stmt.executeUpdate(query);
//
//      rs.close();
//
//      String terminal = "";
//
//      rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString("TERMINAL_NAME");
//      }
//
//      List list = controller.findAllIssuedRequests(terminal);
//      assertNotNull(
//          "Error - The list is null but there is at least one Issued Request",
//          list);
//      assertTrue(
//          "Error - The list length is zero but there is at least one Issued Request",
//          list.size() > 0);
//
//      for (int i = 0; i > list.size(); i++)
//      {
//        assertEquals("Error - The list should contain only Issued PSRs",
//                     ( (PSRRequestValueSet) list.get(i)).getStatus(),
//                     PSControllerBean.ISSUED_PSR_REQUEST_STATUS);
//      }
//
//      rs.close();
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testFindAllUnissuedRequests() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//      // create the PSR Request record this should set the status to new on the PSR
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      //get id of new PSR request
//      int id = 0;
//      Statement stmt = connection.createStatement();
//
//      ResultSet rs = stmt.executeQuery(selectMaxPSR);
//      if (rs.next())
//      {
//        id = rs.getInt("PSR_REQUEST_ID");
//      }
//
//      rs.close();
//
//      String query =
//          "UPDATE XHB_PSR_REQUEST SET PSR_STATUS = " + "'" +
//          PSControllerBean.NEW_PSR_REQUEST_STATUS + "'" +
//          " WHERE PSR_REQUEST_ID=" +
//          id;
//
//      stmt.executeUpdate(query);
//
//      String terminal = "";
//
//      rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString("TERMINAL_NAME");
//      }
//
//      List list = controller.findAllUnissuedRequests(terminal);
//      assertNotNull(
//          "Error - There list is null but there is at least one unissued request",
//          list);
//      assertTrue(
//          "Error - There list size is zero but there is at least one unissued request",
//          list.size() > 0);
//
//      rs.close();
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testFindProbation() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//      // create the PSR Request record this should set the status to new on the PSR
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      Statement stmt = connection.createStatement();
//
//      String terminal = "";
//      ResultSet rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString("TERMINAL_NAME");
//      }
//
//      rs.close();
//
//      PSRProbationValueSet pSRProbationValueSet = controller.findProbation(
//          terminal);
//
//      assertNotNull("PSRProbationValueSet is null", pSRProbationValueSet);
//
//      XhbAddressBasicValue psXhbAddressValue = pSRProbationValueSet.getAddress();
//
//      String address_id = "";
//      String query = "SELECT ADDRESS_ID FROM XHB_COURT WHERE COURT_ID = " +
//          COURT_ID.intValue();
//
//      rs = stmt.executeQuery(query);
//      if (rs.next())
//      {
//        address_id = rs.getString("ADDRESS_ID");
//      }
//      rs.close();
//
//      query = "SELECT * FROM XHB_ADDRESS WHERE ADDRESS_ID = " +
//          address_id;
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("AddressId is incorrect",
//                     psXhbAddressValue.getAddressId().intValue(),
//                     rs.getInt("ADDRESS_ID"));
//
//        assertEquals("First Line of Address is incorrect",
//                     psXhbAddressValue.getAddress1(), rs.getString("ADDRESS_1"));
//
//        assertEquals("Second Line of Address is incorrect",
//                     psXhbAddressValue.getAddress2(), rs.getString("ADDRESS_2"));
//
//        assertEquals("Post Code is incorrect",
//                     psXhbAddressValue.getPostcode(), rs.getString("POSTCODE"));
//
//        assertEquals("Country is incorrect",
//                     psXhbAddressValue.getCountry(), rs.getString("COUNTRY"));
//
//        assertEquals("County is incorrect",
//                     psXhbAddressValue.getCounty(), rs.getString("COUNTY"));
//      }
//      rs.close();
//
//      XhbCourtBasicValue pSXhbCourtValue = pSRProbationValueSet.getCourt();
//
//      assertNotNull("PSXhbCourtValue is null", pSXhbCourtValue);
//
//      query = "SELECT * FROM XHB_COURT WHERE COURT_ID = " +
//          COURT_ID.intValue();
//
//      rs = stmt.executeQuery(query);
//      if (rs.next())
//      {
//
//        assertEquals("Court Id is incorrect",
//                     pSXhbCourtValue.getCourtId().intValue(),
//                     rs.getInt("COURT_ID"));
//
//        assertEquals("CrestIpAddress is incorrect",
//                     pSXhbCourtValue.getCrestIpAddress(),
//                     rs.getString("CREST_IP_ADDRESS"));
//
//        assertEquals("Court type is incorrect", pSXhbCourtValue.getCourtType(),
//                     rs.getString("COURT_TYPE"));
//
//        assertEquals("Crest Court Id incorrect",
//                     pSXhbCourtValue.getCrestCourtId(),
//                     rs.getString("CREST_COURT_ID"));
//
//        assertEquals("Crest Prefix incorrect", pSXhbCourtValue.getCourtPrefix(),
//                     rs.getString("COURT_PREFIX"));
//
//      }
//      rs.close();
//      // Should also look at contacts if get time
//      //pSRProbationValueSet.getContacts()
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testFindRecipientByPrimaryKey() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(deletePSRRecipient);
//      ResultSet rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//
//      String query = "INSERT INTO XHB_PSR_RECIPIENT VALUES(" +
//          RECIPIENT_ID.intValue() +
//          ",'Bill Gates','EMAIL'," + addressId + ',' +
//          "sysdate, sysdate, user, user,1)";
//
//      stmt.executeUpdate(query);
//
//      PSRRecipientValueSet pSRRecipientValueSet = controller.
//          findRecipientByPrimaryKey(RECIPIENT_ID);
//
//      assertNotNull("PSRRecipientValueSet is null", pSRRecipientValueSet);
//
//      XhbPsrRecipientBasicValue psXhbPsrRecipientValue = pSRRecipientValueSet.
//          getRecipient();
//
//      assertNotNull("PsXhbPsrRecipientValue is null", psXhbPsrRecipientValue);
//
//      assertEquals("Recipient Id is incorrect",
//                   psXhbPsrRecipientValue.getRecipientId(), RECIPIENT_ID);
//
//      assertEquals("Recipient name is incorrect",
//                   psXhbPsrRecipientValue.getRecipientName(), "Bill Gates");
//
//      assertEquals("Recipient Address Id is incorrect",
//                   psXhbPsrRecipientValue.getRecipientAddressId().intValue(),
//                   addressId);
//
//      assertEquals("Recipient method of contact is incorrect",
//                   psXhbPsrRecipientValue.getRecipientMethodOfContact(),
//                   "EMAIL");
//
//      stmt.close();
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testFindRequestByPrimaryKey() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//      // create the PSR Request record this should set the status to new on the PSR
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      //get id of new PSR request
//      int id = 0;
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(selectMaxPSR);
//      if (rs.next())
//      {
//        id = rs.getInt("PSR_REQUEST_ID");
//      }
//      rs.close();
//
//      String terminal = "";
//      stmt = connection.createStatement();
//      rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString(1);
//      }
//      rs.close();
//      stmt.close();
//
//      PSRRequestValueSet pSRRequestValueSet = controller.
//          findRequestByPrimaryKey(new Integer(id), terminal);
//
//      assertNotNull("PSRRecipientValueSet is null", pSRRequestValueSet);
//
//      assertEquals("Status Id is incorrect", pSRRequestValueSet.getStatus(),
//                   PSControllerBean.NEW_PSR_REQUEST_STATUS);
//
//      assertNotNull("Case number is null", pSRRequestValueSet.getCaseNumber());
//
//      assertNotNull("Court name is null", pSRRequestValueSet.getCourtName());
//
//      assertNotNull("Defendant is null", pSRRequestValueSet.getDefendant());
//
//      assertNotNull("Court Room is null", pSRRequestValueSet.getCourtRoom());
//
//      assertNotNull("Solicitor is null", pSRRequestValueSet.getSolicitor());
//
//      assertEquals("Court Site Id is incorrect",
//                   pSRRequestValueSet.getCourtRoom().getCourtSiteId(),
//                   COURT_ID);
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testGetAllRecipients() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(deletePSRRecipient);
//      ResultSet rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//      String query = "INSERT INTO XHB_PSR_RECIPIENT VALUES(" +
//          RECIPIENT_ID.intValue() +
//          ",'Bill Gates','EMAIL'," + addressId + ',' +
//          "sysdate, sysdate, user, user,1)";
//
//      stmt.executeUpdate(query);
//
//      rs.close();
//      stmt.close();
//
//      List lsit = controller.getAllRecipients();
//
//      assertNotNull(
//          "Error - list is null but there is a least 1 recipient in the database",
//          lsit);
//      assertTrue(
//          "Error - list is 0 length but there is a least 1 recipient in the database",
//          lsit.size() > 0);
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testInsertRecipientValues() throws
//      CSUnrecoverableException
//  {
//
//    try
//    {
//
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(deletePSRRecipient);
//      ResultSet rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//
//      rs = stmt.executeQuery(selectMaxContact);
//      int contactId = 0;
//      if (rs.next())
//      {
//        contactId = rs.getInt("CONTACT_ID");
//      }
//      rs.close();
//
//      String PS_USER = "ps user";
//      Timestamp date = new Timestamp(new java.util.Date().getTime());
//      XhbPsrRecipientBasicValue newRecipient = new XhbPsrRecipientBasicValue();
//      newRecipient.setRecipientName("William Gates");
//      newRecipient.setRecipientMethodOfContact("EMAIL");
//      newRecipient.setCreatedBy(PS_USER);
//      newRecipient.setCreationDate(date);
//      newRecipient.setRecipientId(new Integer(RECIPIENT_ID.intValue()));
//      newRecipient.setLastUpdateDate(date);
//      newRecipient.setLastUpdatedBy(PS_USER);
//      newRecipient.setVersion(new Integer(1));
//
//      XhbAddressBasicValue newAddress = new XhbAddressBasicValue();
//      newAddress.setAddress1("4 Roundwood Avenue");
//      newAddress.setAddress2("Stockley Park");
//      newAddress.setAddress3("Uxbridge");
//      newAddress.setAddress4("London");
//      newAddress.setAddressId(new Integer(addressId + 1));
//      newAddress.setCountry("England");
//      newAddress.setCounty("Berkshire");
//      newAddress.setCreatedBy(PS_USER);
//      newAddress.setCreationDate(date);
//      newAddress.setLastUpdateDate(date);
//      newAddress.setLastUpdatedBy(PS_USER);
//      newAddress.setVersion(new Integer(1));
//
//      HashMap newContacts = new HashMap();
//      XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();
//      contact.setContactId(new Integer(contactId + 1));
//      contact.setContactValue("020 8993 6292");
//      contact.setContactType("Fax");
//      contact.setCreatedBy(PS_USER);
//      contact.setCreationDate(date);
//      contact.setLastUpdateDate(date);
//      contact.setLastUpdatedBy(PS_USER);
//      contact.setVersion(new Integer(1));
//
//      newContacts.put(contact.getContactType(), contact);
//
//      PSRRecipientValueSet pSRRecipientValueSet = new PSRRecipientValueSet(
//          newRecipient, newAddress, newContacts);
//
//      controller.insertRecipientValues(pSRRecipientValueSet);
//
//      // retrieve the record and ensure the correct details have been set
//
//      String query = "SELECT * FROM XHB_PSR_RECIPIENT WHERE RECIPIENT_ID = " +
//          RECIPIENT_ID.intValue();
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect PSR Recipient Name",
//                     "William Gates",
//                     rs.getString("RECIPIENT_NAME"));
//
//        assertEquals("Incorrect Recipient method of contact",
//                     "EMAIL",
//                     rs.getString("RECIPIENT_METHOD_OF_CONTACT"));
//      }
//
//      rs.close();
//
//      query = "SELECT * FROM XHB_ADDRESS WHERE ADDRESS_ID = " + addressId + 1;
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect Address_1",
//                     "4 Roundwood Avenue",
//                     rs.getString("ADDRESS_1"));
//
//        assertEquals("Incorrect Adrress_2",
//                     "Stockley Park",
//                     rs.getString("ADDRESS_2"));
//
//        assertEquals("Incorrect Adrress_3",
//                     "Uxbridge",
//                     rs.getString("ADDRESS_3"));
//
//        assertEquals("Incorrect Adrress_4",
//                     "London",
//                     rs.getString("ADDRESS_4"));
//
//        assertEquals("Incorrect Country",
//                     "England",
//                     rs.getString("COUNTRY"));
//
//        assertEquals("Incorrect County",
//                     "Berkshire",
//                     rs.getString("COUNTY"));
//      }
//
//      rs.close();
//
//      query = "SELECT * FROM XHB_CONTACT_DETAIL WHERE CONTACT_ID = " +
//          contactId + 1;
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect Contact Value",
//                     "020 8993 6292",
//                     rs.getString("Contact_Value"));
//
//        assertEquals("Incorrect Contact Type ",
//                     "Fax",
//                     rs.getString("CONTACT_TYPE"));
//      }
//
//      rs.close();
//
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testIssuePSRRequest() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//
//      // create the PSR Request record this should set the status to new on the PSR
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      //get id of new PSR request
//      int id = 0;
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(selectMaxPSR);
//      if (rs.next())
//      {
//        id = rs.getInt("PSR_REQUEST_ID");
//      }
//
//      rs.close();
//
//      String terminal = "";
//      rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString("TERMINAL_NAME");
//      }
//
//      rs.close();
//
//      stmt.executeUpdate(deletePSRRecipient);
//      rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//
//      rs = stmt.executeQuery(selectMaxContact);
//      int contactId = 0;
//      if (rs.next())
//      {
//        contactId = rs.getInt("CONTACT_ID");
//      }
//      rs.close();
//
//      PSRRequestValueSet pSRRequestValueSet = controller.
//          findRequestByPrimaryKey(new Integer(id), terminal);
//
//      String PS_USER = "ps user";
//      Timestamp date = new Timestamp(new java.util.Date().getTime());
//      XhbPsrRecipientBasicValue newRecipient = new XhbPsrRecipientBasicValue();
//      newRecipient.setRecipientName("William Gates");
//      newRecipient.setRecipientMethodOfContact("EMAIL");
//      newRecipient.setCreatedBy(PS_USER);
//      newRecipient.setCreationDate(date);
//      newRecipient.setRecipientId(new Integer(RECIPIENT_ID.intValue()));
//      newRecipient.setLastUpdateDate(date);
//      newRecipient.setLastUpdatedBy(PS_USER);
//      newRecipient.setVersion(new Integer(1));
//
//      XhbAddressBasicValue newAddress = new XhbAddressBasicValue();
//      newAddress.setAddress1("4 Roundwood Avenue");
//      newAddress.setAddress2("Stockley Park");
//      newAddress.setAddress3("Uxbridge");
//      newAddress.setAddress4("London");
//      newAddress.setAddressId(new Integer(addressId + 1));
//      newAddress.setCountry("England");
//      newAddress.setCounty("Berkshire");
//      newAddress.setCreatedBy(PS_USER);
//      newAddress.setCreationDate(date);
//      newAddress.setLastUpdateDate(date);
//      newAddress.setLastUpdatedBy(PS_USER);
//      newAddress.setVersion(new Integer(1));
//
//      HashMap newContacts = new HashMap();
//      XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();
//      contact.setContactId(new Integer(contactId + 1));
//      contact.setContactValue("020 8993 6292");
//      contact.setContactType("Fax");
//      contact.setCreatedBy(PS_USER);
//      contact.setCreationDate(date);
//      contact.setLastUpdateDate(date);
//      contact.setLastUpdatedBy(PS_USER);
//      contact.setVersion(new Integer(1));
//
//      newContacts.put(contact.getContactType(), contact);
//
//      PSRRecipientValueSet pSRRecipientValueSet = new PSRRecipientValueSet(
//          newRecipient, newAddress, newContacts);
//
//      pSRRequestValueSet.setRecipient(pSRRecipientValueSet);
//      //pSRRequestValueSet.get
//
//      controller.issuePSRRequest(pSRRequestValueSet);
//
//      stmt = connection.createStatement();
//      rs = stmt.executeQuery(selectMaxPSR);
//
//      if (rs.next())
//      {
//          PSRRequestValueSet pSRRequest = controller.
//          findRequestByPrimaryKey(new Integer(rs.getString("PSR_REQUEST_ID")), terminal);
//
//    	  assertEquals("Error - the PSR has been issued",
//    			  	pSRRequest.getStatus(),
//    			  	PSControllerBean.ISSUED_PSR_REQUEST_STATUS);
//      }
//      rs.close();
//
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testRemoveRecipientValues() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(deletePSRRecipient);
//      ResultSet rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//
//      rs = stmt.executeQuery(selectMaxContact);
//      int contactId = 0;
//      if (rs.next())
//      {
//        contactId = rs.getInt("CONTACT_ID");
//      }
//      rs.close();
//
//      String PS_USER = "ps user";
//      Timestamp date = new Timestamp(new java.util.Date().getTime());
//      XhbPsrRecipientBasicValue newRecipient = new XhbPsrRecipientBasicValue();
//      newRecipient.setRecipientName("William Gates");
//      newRecipient.setRecipientMethodOfContact("EMAIL");
//      newRecipient.setCreatedBy(PS_USER);
//      newRecipient.setCreationDate(date);
//      newRecipient.setRecipientId(new Integer(RECIPIENT_ID.intValue()));
//      newRecipient.setLastUpdateDate(date);
//      newRecipient.setLastUpdatedBy(PS_USER);
//      newRecipient.setVersion(new Integer(1));
//
//      XhbAddressBasicValue newAddress = new XhbAddressBasicValue();
//      newAddress.setAddress1("4 Roundwood Avenue");
//      newAddress.setAddress2("Stockley Park");
//      newAddress.setAddress3("Uxbridge");
//      newAddress.setAddress4("London");
//      newAddress.setAddressId(new Integer(addressId + 1));
//      newAddress.setCountry("England");
//      newAddress.setCounty("Berkshire");
//      newAddress.setCreatedBy(PS_USER);
//      newAddress.setCreationDate(date);
//      newAddress.setLastUpdateDate(date);
//      newAddress.setLastUpdatedBy(PS_USER);
//      newAddress.setVersion(new Integer(1));
//
//      HashMap newContacts = new HashMap();
//      XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();
//      contact.setContactId(new Integer(contactId + 1));
//      contact.setContactValue("020 8993 6292");
//      contact.setContactType("Fax");
//      contact.setCreatedBy(PS_USER);
//      contact.setCreationDate(date);
//      contact.setLastUpdateDate(date);
//      contact.setLastUpdatedBy(PS_USER);
//      contact.setVersion(new Integer(1));
//
//      newContacts.put(contact.getContactType(), contact);
//
//      PSRRecipientValueSet pSRRecipientValueSet = new PSRRecipientValueSet(
//          newRecipient, newAddress, newContacts);
//
//      controller.insertRecipientValues(pSRRecipientValueSet);
//
//      rs = stmt.executeQuery(selectMaxRecipient);
//
//      int id = 0;
//      if (rs.next())
//      {
//        id = rs.getInt("RECIPIENT_ID");
//
//      }
//
//      rs.close();
//
//      PSRRecipientValueSet foundPSRRecipientValueSet = controller.
//          findRecipientByPrimaryKey(new Integer(id));
//
//      controller.removeRecipientValues(foundPSRRecipientValueSet);
//
//      String query =
//          "SELECT RECIPIENT_ID FROM XHB_PSR_RECIPIENT WHERE RECIPIENT_ID = " +
//          RECIPIENT_ID.intValue();
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//        fail("Error - the Recipient Value should have been removed");
//
//      }
//
//      rs.close();
//
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testUpdatdeRecipientValues() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//
//      Statement stmt = connection.createStatement();
//      stmt.executeUpdate(deletePSRRecipient);
//      ResultSet rs = stmt.executeQuery(selectMaxAddress);
//      int addressId = 0;
//      if (rs.next())
//      {
//        addressId = rs.getInt("ADDRESS_ID");
//      }
//      rs.close();
//
//      rs = stmt.executeQuery(selectMaxContact);
//      int contactId = 0;
//      if (rs.next())
//      {
//        contactId = rs.getInt("CONTACT_ID");
//      }
//      rs.close();
//
//      String PS_USER = "ps user";
//      Timestamp date = new Timestamp(new java.util.Date().getTime());
//      XhbPsrRecipientBasicValue newRecipient = new XhbPsrRecipientBasicValue();
//      newRecipient.setRecipientName("William Gates");
//      newRecipient.setRecipientMethodOfContact("EMAIL");
//      newRecipient.setCreatedBy(PS_USER);
//      newRecipient.setCreationDate(date);
//      newRecipient.setRecipientId(new Integer(RECIPIENT_ID.intValue()));
//      newRecipient.setLastUpdateDate(date);
//      newRecipient.setLastUpdatedBy(PS_USER);
//      newRecipient.setVersion(new Integer(1));
//
//      XhbAddressBasicValue newAddress = new XhbAddressBasicValue();
//      newAddress.setAddress1("4 Roundwood Avenue");
//      newAddress.setAddress2("Stockley Park");
//      newAddress.setAddress3("Uxbridge");
//      newAddress.setAddress4("London");
//      newAddress.setAddressId(new Integer(addressId + 1));
//      newAddress.setCountry("England");
//      newAddress.setCounty("Berkshire");
//      newAddress.setCreatedBy(PS_USER);
//      newAddress.setCreationDate(date);
//      newAddress.setLastUpdateDate(date);
//      newAddress.setLastUpdatedBy(PS_USER);
//      newAddress.setVersion(new Integer(1));
//
//      HashMap newContacts = new HashMap();
//      XhbContactDetailBasicValue contact = new XhbContactDetailBasicValue();
//      contact.setContactId(new Integer(contactId + 1));
//      contact.setContactValue("020 8993 6292");
//      contact.setContactType("Fax");
//      contact.setCreatedBy(PS_USER);
//      contact.setCreationDate(date);
//      contact.setLastUpdateDate(date);
//      contact.setLastUpdatedBy(PS_USER);
//      contact.setVersion(new Integer(1));
//
//      newContacts.put(contact.getContactType(), contact);
//
//      PSRRecipientValueSet pSRRecipientValueSet = new PSRRecipientValueSet(
//          newRecipient, newAddress, newContacts);
//
//      controller.insertRecipientValues(pSRRecipientValueSet);
//
//      // select the recipient id / primary key just inserted
//
//      try {
//		rs = stmt.executeQuery(selectMaxRecipient);
//	} catch (Exception e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//
//      int id = 0;
//      if (rs.next())
//      {
//        id = rs.getInt("RECIPIENT_ID");
//
//      }
//
//      rs.close();
//
//      PSRRecipientValueSet foundPSRRecipientValueSet = controller.
//          findRecipientByPrimaryKey(new Integer(id));
//
//      // update some of the recipient address
//      foundPSRRecipientValueSet.getAddress().setAddress2("Stockley Park 2");
//      foundPSRRecipientValueSet.getAddress().setAddress3("Uxbridge 2");
//
//      // update some of the recipient details
//      foundPSRRecipientValueSet.getRecipient().setRecipientName(
//          "William Gates 2");
//      foundPSRRecipientValueSet.getRecipient().setRecipientMethodOfContact(
//          "Fax");
//
//      // update some of the contact details
//      contact.setContactValue("020 8993 9999");
//      foundPSRRecipientValueSet.getContacts().put(contact.getContactType(),
//                                                  contact);
//
//      log.debug("****************************************** Fails Here ******************************************");
//
//      controller.updateRecipientValues(foundPSRRecipientValueSet);
//
//      log.debug("****************************************** Gets Here 3 ******************************************");
//
//      // retrieve the record and ensure the correct details have been set
//
//      String query = "SELECT * FROM XHB_PSR_RECIPIENT WHERE RECIPIENT_ID = " +
//          id;
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect PSR Recipient Name",
//                     "William Gates 2",
//                     rs.getString("RECIPIENT_NAME"));
//
//        assertEquals("Incorrect Recipient method of contact",
//                     "Fax",
//                     rs.getString("RECIPIENT_METHOD_OF_CONTACT"));
//      }
//
//      rs.close();
//
//      query = "SELECT * FROM XHB_ADDRESS WHERE ADDRESS_ID = " + addressId + 1;
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect Address_1",
//                     "4 Roundwood Avenue",
//                     rs.getString("ADDRESS_1"));
//
//        assertEquals("Incorrect Adrress_2",
//                     "Stockley Park 2",
//                     rs.getString("ADDRESS_2"));
//
//        assertEquals("Incorrect Adrress_3",
//                     "Uxbridge 2",
//                     rs.getString("ADDRESS_3"));
//
//        assertEquals("Incorrect Adrress_4",
//                     "London",
//                     rs.getString("ADDRESS_4"));
//
//        assertEquals("Incorrect Country",
//                     "England",
//                     rs.getString("COUNTRY"));
//
//        assertEquals("Incorrect County",
//                     "Berkshire",
//                     rs.getString("COUNTY"));
//      }
//
//      rs.close();
//
//      query = "SELECT * FROM XHB_CONTACT_DETAIL WHERE CONTACT_ID = " +
//          contactId + 1;
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        assertEquals("Incorrect Contact Value",
//                     "020 8993 9999",
//                     rs.getString("Contact_Value"));
//      }
//
//      rs.close();
//
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//  public void testUpdateProbation() throws
//      CSUnrecoverableException
//  {
//    try
//    {
//
//      // create the PSR Request record this should set the status to new on the PSR
//        controller.createPSRRequest(CASE_ID, COURT_ID, DEF_ON_CASE_ID, COURT_ROOM, SCHED_HEARING_ID);
//
//      //get id of new PSR request
//      int id = 0;
//      Statement stmt = connection.createStatement();
//      ResultSet rs = stmt.executeQuery(selectMaxPSR);
//      if (rs.next())
//      {
//        id = rs.getInt("PSR_REQUEST_ID");
//      }
//
//      rs.close();
//
//      String terminal = "";
//      rs = stmt.executeQuery(selectTerminal);
//
//      if (rs.next())
//      {
//        terminal = rs.getString("TERMINAL_NAME");
//      }
//
//      rs.close();
//
//      PSRProbationValueSet pSRProbationValueSet = controller.findProbation(
//          terminal);
//
//      // update adddress
//      XhbAddressBasicValue address = pSRProbationValueSet.getAddress();
//
//      address.setAddress3("Address 3");
//
//      address.setCountry("New York");
//
//      address.setPostcode("RG30 1BS");
//
//      pSRProbationValueSet.setAddress(address);
//
//      int addressId = address.getAddressId().intValue();
//
//      // update contact
//
//      controller.updateProbationValues(pSRProbationValueSet);
//
//      // retrieve the address record and ensure the correct details have been set
//
//      String query = "SELECT * FROM XHB_ADDRESS WHERE ADDRESS_ID = " +
//          addressId;
//
//      log.debug("***********************************************************************************" +
//                query);
//
//      rs = stmt.executeQuery(query);
//
//      if (rs.next())
//      {
//
//        log.debug("***********************************************************************************" +
//                  rs.getString("ADDRESS_3"));
//
//        assertEquals("Incorrect Address 3",
//                     "Address 3",
//                     rs.getString("ADDRESS_3"));
//
//        assertEquals("Incorrect Country",
//                     "New York",
//                     rs.getString("COUNTRY"));
//
//        assertEquals("Incorrect Post Code",
//                     "RG30 1BS",
//                     rs.getString("POST_CODE"));
//      }
//
//      rs.close();
//      stmt.close();
//
//    }
//
//    catch (CSUnrecoverableException ex)
//    {
//      ex.printStackTrace();
//      fail("CSUnrecoverableException : " + ex.getMessage() + " " +
//           ex.getUserMessage());
//    }
//    catch (RemoteException ex)
//    {
//      ex.printStackTrace();
//      fail("RemoteException : " + ex.getMessage());
//    }
//
//    catch (SQLException ex)
//    {
//      ex.printStackTrace();
//      fail("SQLException : " + ex.getMessage());
//    }
//
//  }
//
//}
//