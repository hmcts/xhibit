//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.rmi.RemoteException;
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Hashtable;
//import java.util.Properties;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.client.delegate.CSBusinessDelegateFactory;
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderException;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderStateException;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersController;
//import uk.gov.courtservice.xhibit.client.order.delegate.OrdersControllerBusinessDelegate;
//
///**
// * <p>
// * Title: TestOrdersControllerBusinessDelegateEJB
// * </p>
// * <p>
// * Description: Tests the Orders Business Delegate calls
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Neil Entwistle
// * @version 1.0
// */
//
//public class TestOrdersControllerBusinessDelegateEJB extends TestCase {
//    private static final Logger log = CSServices.getLogger(TestOrdersControllerBusinessDelegateEJB.class);
//
//    private static final String ERROR_NULL_REMOTE = "Remote interface reference is null.  "
//            + "It must be created by calling one of the Home interface " + "methods first.";
//
//    private static final int MAX_OUTPUT_LINE_LENGTH = 100;
//
//    private static final String DATA_XML = "<?xml version='1.0'?>";
//
//    private CSBusinessDelegateFactory factory = null;
//
//    private OrdersControllerBusinessDelegate ocbDelegate = null;
//
//    private boolean logging = true;
//
//    // private OrdersControllerHome ordersControllerHome = null;
//    private OrdersController ordersController = null;
//
//    private XhbOrderTemplate orderTemplate = null;
//
//    private XhbOrderTemplateHome orderTemplateHome = null;
//
//    private XhbOrderStatusHome statusHome;
//
//    private XhbOrderDeliveryStatusHome deliveryHome;
//
//    private XhbOrderTypeHome orderTypeHome;
//
//    private XhbOrderType orderType;
//
//    private Connection con;
//
//    /**
//     *
//     * @param s
//     */
//    public TestOrdersControllerBusinessDelegateEJB(String s) {
//        super(s);
//        long startTime = 0;
//        if (logging) {
//            log("Initializing bean access.");
//            startTime = System.currentTimeMillis();
//        }
//
//        try {
//            // get naming context
//            Context ctx = getInitialContext();
//
//            // look up jndi name
//            Object ref = ctx.lookup("OrdersControllerHome");
//
//            // cast to Home interface
//            // ordersControllerHome =
//            // (OrdersControllerHome) PortableRemoteObject.narrow(
//            // ref, OrdersControllerHome.class);
//            // ordersController = ordersControllerHome.create();
//
//            // look up jndi name for type
//            // ref = ctx.lookup("XhbOrderTypeHome");
//            //
//            // cast to Home interface
//            // orderTypeHome = (XhbOrderTypeHome)
//            // PortableRemoteObject.narrow(
//            // ref, XhbOrderTypeHome.class);
//            // orderType = orderTypeHome.findByCodeUniquely("BAIL ORDER");
//
//            // look up jndi name for template
//            // ref = ctx.lookup("XhbOrderTemplateHome");
//
//            // cast to Home interface
//            // orderTemplateHome =
//            // (XhbOrderTemplateHome) PortableRemoteObject.narrow(
//            // ref, XhbOrderTemplateHome.class);
//
//            if (logging) {
//                long endTime = System.currentTimeMillis();
//                log("Succeeded initializing bean access.");
//                log("Execution time: " + (endTime - startTime) + " ms.");
//            }
//        } catch (Exception e) {
//            if (logging) {
//                log("Failed initializing bean access.");
//            }
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws NamingException
//     * @throws SQLException
//     * @throws Exception
//     */
//    public void setUp() throws OrderException, NamingException, SQLException, Exception {
//        super.setUp();
//
//        Context ctx = null;
//        Hashtable env = new Hashtable();
//
//        env.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
//        env.put(Context.PROVIDER_URL, "t3://localhost:7001");
//        log.debug("Loading Initial Context...");
//        ctx = new InitialContext(env);
//        log.debug("Done loading Initial Context");
//        javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup("XhibitOracleTxDataSource");
//        con = ds.getConnection();
//        Statement stmt = con.createStatement();
//        log.debug("Got connection and statement");
//        int i = stmt.executeUpdate("select 1 from dual");
//        int j = stmt.executeUpdate("insert into XHB_ORDER_TEMPLATE values (123, 'test', 'test',"
//                + "'test', 123, 'JUNIT', 'JUNIT', '25-FEB-03', " + "'25-FEB-03', 1, null)");
//        int k = stmt.executeUpdate("insert into xhb_order values (123, '25-FEB-03', 'JUNIT', "
//                + "'25-FEB-03', 'xml stuff', 1, 1, 123, 1, '25-FEB-03', 1, " + "'25-FEB-03', 'JUNIT', 'JUNIT')");
//        stmt.close();
//
//        // orderTemplate = orderTemplateHome.create( "test", "test","test",
//        // new Integer(-1), "JUNIT", "JUNIT",
//        // new Timestamp(new Date().getTime()),
//        // new Timestamp(new Date().getTime()), orderType);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws SQLException
//     * @throws Exception
//     */
//    public void tearDown() throws OrderException, SQLException, Exception {
//        super.tearDown();
//
//        // Collection allBeans= orderTemplateHome.findAll();
//        // for (Iterator iterator = allBeans.iterator(); iterator.hasNext();)
//        // {
//        // XhbOrderTemplate order = (XhbOrderTemplate) iterator.next();
//        // order.remove();
//        // }
//
//        Statement stmt = con.createStatement();
//        stmt.executeUpdate("select 1 from dual");
//        int k = stmt.executeUpdate("delete from XHB_ORDER where ORDER_ID = 123");
//        int j = stmt.executeUpdate("delete from XHB_ORDER_TEMPLATE where ORDER_TEMPLATE_ID = 123");
//
//        stmt.close();
//        con.close();
//        con = null;
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     * @throws RemoteException
//     */
//    public void testCreateOrder() throws OrderException, OrderXMLException, RemoteException {
//        Integer[] disposals = new Integer[] { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
//        XhbOrderValue order = null;
//        order = ordersController.createOrder(new Integer(1), new Integer(1), disposals, false);
//        assertNotNull(order);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws SQLException
//     * @throws RemoteException
//     */
//    public void testGetStatusFromOrder() throws OrderException, SQLException, RemoteException {
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("select order_id from XHB_ORDER where rownum = 1");
//        rs.next();
//        log("Attempting to rs.getInt(1);");
//        Integer pkey = new Integer(rs.getInt(1));
//
//        log("Attempting to controller.getOrder(" + pkey + ");");
//        XhbOrderValue order = null;
//        order = ordersController.getOrder(pkey);
//        assertNotNull(order);
//        XhbOrderStatusValue status = order.getXhbOrderStatus();
//        String code = status.getCode();
//        assertEquals("Comparing status for the first Order with that " + "which it wsa created.", code, "NEW");
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws RemoteException
//     */
//    public void testGetOrder() throws OrderException, RemoteException {
//        XhbOrderValue order = null;
//        order = ordersController.getOrder(new Integer(123));
//        assertNotNull(order);
//        if (logging) {
//            log("testGetOrder.");
//        }
//    }
//
//    /**
//     *
//     */
//    public void testGetOrdersForDefendantOnCase() {
//        XhbOrderValue[] orders = null;
//        try {
//            orders = ordersController.getOrdersForDefendantOnCase(new Integer(1));
//        } catch (RemoteException ex) {
//            if (logging) {
//                log("testGetOrdersForDefendantOnCase.");
//            }
//        }
//        assertTrue(orders.length > 0);
//
//        for (int i = 0; i < orders.length; i++) {
//            assertNotNull(orders[i]);
//        }
//
//    }
//
//    /**
//     *
//     */
//    public void testGetOrderTemplates() {
//        XhbOrderTemplateValue[] templates = null;
//        try {
//            // templates = ordersController.getOrderTemplates(new
//            // Integer(1));
//            templates = ordersController.getValidTemplates();
//        } catch (RemoteException ex) {
//            if (logging) {
//                log("testGetOrdersForDefendantOnCase.");
//            }
//        }
//        assertTrue(templates.length > 0);
//
//        for (int i = 0; i < templates.length; i++) {
//            assertNotNull(templates[i]);
//            /*
//             * log.debug( "getCreatedBy " + templates[i].getCreatedBy());
//             * log.debug( "getCreationDate " + templates[i].getCreationDate());
//             * log.debug( "getDisplayTransformName " +
//             * templates[i].getDisplayTransformName()); log.debug(
//             * "getEditorTemplateName " + templates[i].getEditorTemplateName());
//             * log.debug( "getLastUpdateDate " +
//             * templates[i].getLastUpdateDate()); log.debug( "getLastUpdatedBy " +
//             * templates[i].getLastUpdatedBy()); log.debug(
//             * "getNarrativeTemplateName " +
//             * templates[i].getNarrativeTemplateName()); log.debug( "getObsInd " +
//             * templates[i].getObsInd()); log.debug( "getOrderTemplateId " +
//             * templates[i].getOrderTemplateId()); log.debug( "getOrderTypeId " +
//             * templates[i].getOrderTypeId()); log.debug( "getPrimaryKey " +
//             * templates[i].getPrimaryKey()); log.debug( "getVersion " +
//             * templates[i].getVersion());
//             */
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws RemoteException
//     */
//    public void testGetOrderTypes() throws OrderException, RemoteException {
//        XhbOrderTypeValue[] orderType = null;
//        orderType = ordersController.getOrderTypes();
//        assertNotNull(orderType);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws RemoteException
//     */
//    public void testGetValidTemplates() throws OrderException, RemoteException {
//        XhbOrderTemplateValue[] templates = null;
//        templates = ordersController.getValidTemplates();
//        assertTrue(templates.length > 0);
//
//        for (int i = 0; i < templates.length; i++) {
//            assertNotNull(templates[i]);
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     * @throws RemoteException
//     */
//    public void testSignOrderNotPrinted() throws OrderException, OrderXMLException, RemoteException {
//        XhbOrderValue orderValue = null;
//        try {
//            orderValue = ordersController.signOrder(ordersController.getOrder(new Integer(123)));
//        } catch (OrderStateException ose) {
//            if (logging) {
//                log("testSignOrderNotPrinted.");
//            }
//        } finally {
//            assertNull(orderValue);
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     * @throws RemoteException
//     */
//    public void testSignOrderPrinted() throws OrderException, OrderXMLException, RemoteException {
//        XhbOrderValue orderValue = null;
//        try {
//            orderValue = ordersController.getOrder(new Integer(123));
//            orderValue.setOrderStatusId(new Integer(3));
//            orderValue = ordersController.signOrder(orderValue);
//            assertNotNull(orderValue);
//        } catch (OrderStateException ose) {
//            if (logging) {
//                log("testSignOrderNotPrinted.");
//            }
//        } finally {
//            assertNotNull(orderValue);
//        }
//    }
//
//    /**
//     *
//     */
//    public void testSaveOrder() {
//        try {
//            XhbOrderValue orderValue = ordersController.saveOrder(new XhbOrderValue());
//            assertNotNull(orderValue);
//        } catch (Exception ex) {
//            if (logging) {
//                log("testSignOrderNotPrinted.");
//            }
//        }
//    }
//
//    /**
//     *
//     */
//    public void testSignOrder() {
//        try {
//            XhbOrderValue orderValue = ordersController.signOrder(new XhbOrderValue());
//            assertNotNull(orderValue);
//        } catch (Exception ex) {
//            if (logging) {
//                log("testSignOrderNotPrinted.");
//            }
//        }
//    }
//
//    // ----------------------------------------------------------------------------
//    // Utility Methods
//    // ----------------------------------------------------------------------------
//
//    private void log(String message) {
//        if (message == null) {
//            log.debug("-- null");
//            return;
//        }
//        if (message.length() > MAX_OUTPUT_LINE_LENGTH) {
//            log.debug("-- " + message.substring(0, MAX_OUTPUT_LINE_LENGTH) + " ...");
//        } else {
//            log.debug("-- " + message);
//        }
//    }
//
//    private Context getInitialContext() throws Exception {
//        String url = "t3://localhost:7001";
//        String user = null;
//        String password = null;
//        Properties properties = null;
//        try {
//            properties = new Properties();
//            properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
//            properties.put(Context.PROVIDER_URL, url);
//            if (user != null) {
//                properties.put(Context.SECURITY_PRINCIPAL, user);
//                properties.put(Context.SECURITY_CREDENTIALS, password == null ? "" : password);
//            }
//
//            return new InitialContext(properties);
//        } catch (Exception e) {
//            log("Unable to connect to WebLogic server at " + url);
//            log("Please make sure that the server is running.");
//            throw e;
//        }
//    }
//
//}