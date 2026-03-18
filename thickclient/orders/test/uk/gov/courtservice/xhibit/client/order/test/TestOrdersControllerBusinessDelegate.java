//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Hashtable;
//import java.util.Properties;
//
//import javax.naming.Context;
//import javax.naming.InitialContext;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
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
//import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
//
///**
// * <p>
// * Title: TestOrdersControllerBusinessDelegate
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
//public class TestOrdersControllerBusinessDelegate extends TestCase {
//    private static final Logger log = CSServices.getLogger(TestOrdersControllerBusinessDelegate.class);
//
//    private static final String ERROR_NULL_REMOTE = "Remote interface reference is null.  "
//            + "It must be created by calling one of the Home interface " + "methods first.";
//
//    private static final int MAX_OUTPUT_LINE_LENGTH = 100;
//
//    private static final String DATA_XML = "<?xml version='1.0'?>";
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
//    public TestOrdersControllerBusinessDelegate(String s) {
//        super(s);
//    }
//
//    /**
//     *
//     * @throws Exception
//     */
//    public void setUp() throws Exception {
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
//        int i = stmt.executeUpdate("insert into XHB_ORDER_TYPE values (123, 'Bail Order', 0, "
//                + "'TEST', 'TEST','03-MAR-03', '03-MAR-03', NULL)");
//        int ii = stmt.executeUpdate("insert into XHB_ORDER_TYPE values (456, "
//                + "'CMPO', 0, 'TEST', 'TEST','03-MAR-03', '03-MAR-03', NULL)");
//        int j = stmt.executeUpdate("insert into XHB_ORDER_TEMPLATE values (123, "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/" + "documentTransforms/Bail Order.xsl', "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/Bail " + "Order.xml', "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/"
//                + "DataEntryTemplate.xml', 123, 'JUNIT', 'JUNIT', '25-FEB-03', " + "'25-FEB-03', 123, null)");
//        int jj = stmt.executeUpdate("insert into XHB_ORDER_TEMPLATE values (456, "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/" + "documentTransforms/Bail Order.xsl', "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/CP " + "Order.xml', "
//                + "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/"
//                + "CPOrderTemplate.xml', 456, 'JUNIT', 'JUNIT', '25-FEB-03', " + "'25-FEB-03', 456, null)");
//        // int k = stmt.executeUpdate("insert into XHB_ORDER_DELIVERY_STATUS
//        // values(123, 'JUNIT', 1, 'JUNIT', 'JUNIT', '07-MAR-03',
//        // '07-MAR-03')");
//        // int kk = stmt.executeUpdate("insert into XHB_ORDER_DELIVERY_STATUS
//        // values(456, 'JUNIT', 1, 'JUNIT', 'JUNIT', '07-MAR-03',
//        // '07-MAR-03')");
//        // int l = stmt.executeUpdate("insert into XHB_ORDER_STATUS
//        // values(123, 'NEW', 1, 'JUNIT', 'JUNIT', '07-MAR-03', '07-MAR-03')");
//        // int ll = stmt.executeUpdate("insert into XHB_ORDER_STATUS
//        // values(456, 'NEW', 1, 'JUNIT', 'JUNIT', '07-MAR-03', '07-MAR-03')");
//        int m = stmt.executeUpdate("insert into xhb_order values (123, '25-FEB-03', 'JUNIT', "
//                + "'25-FEB-03', 'xml stuff', 2, 1, 123, 1, '25-FEB-03', 1, "
//                + "'25-FEB-03', 'JUNIT', 'JUNIT', 'JUNIT')");
//        int mm = stmt.executeUpdate("insert into xhb_order values (456, '25-FEB-03', 'JUNIT', "
//                + "'25-FEB-03', 'xml stuff', 2, 1, 456, 1, '25-FEB-03', 1, "
//                + "'25-FEB-03', 'JUNIT', 'JUNIT', 'JUNIT')");
//
//        stmt.close();
//
//        // orderTemplate = orderTemplateHome.create( "test", "test","test",
//        // new Integer(-1), "JUNIT", "JUNIT", new Timestamp(new
//        // Date().getTime()),
//        // new Timestamp(new Date().getTime()), orderType);
//    }
//
//    /**
//     *
//     * @throws SQLException
//     * @throws Exception
//     */
//    public void tearDown() throws SQLException, Exception {
//        super.tearDown();
//
//        Statement stmt = con.createStatement();
//        int m = stmt.executeUpdate("delete from XHB_ORDER where ORDER_ID = 123");
//        int mm = stmt.executeUpdate("delete from XHB_ORDER where ORDER_ID = 456");
//        int l = stmt.executeUpdate("delete from XHB_ORDER_STATUS where ORDER_STATUS_ID = 123");
//        int ll = stmt.executeUpdate("delete from XHB_ORDER_STATUS where ORDER_STATUS_ID = 456");
//        int k = stmt.executeUpdate("delete from XHB_ORDER_DELIVERY_STATUS where " + "ORDER_DELIVERY_STATUS_ID = 123");
//        int kk = stmt.executeUpdate("delete from XHB_ORDER_DELIVERY_STATUS where " + "ORDER_DELIVERY_STATUS_ID = 456");
//        int j = stmt.executeUpdate("delete from XHB_ORDER_TEMPLATE where ORDER_TEMPLATE_ID = 123");
//        int jj = stmt.executeUpdate("delete from XHB_ORDER_TEMPLATE where ORDER_TEMPLATE_ID = 456");
//        int i = stmt.executeUpdate("delete from XHB_ORDER_TYPE where ORDER_TYPE_ID = 123");
//        int ii = stmt.executeUpdate("delete from XHB_ORDER_TYPE where ORDER_TYPE_ID = 456");
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
//     */
//    public void testCreateUnknownOrder() throws OrderException, OrderXMLException {
//        Integer[] disposals = new Integer[] { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
//        XhbOrderValue order = null;
//        try {
//            disposals = null;
//            order = XhibitDelegateHelper.getOrdersDelegate().createOrder(new Integer(123), new Integer(2), disposals,
//                    false);
//        } catch (OrderException oe) {
//            assertNull(order);
//            if (logging) {
//                log("testCreateOrder.");
//            }
//        }
//
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     */
//    public void testCreateBailOrder() throws OrderException, OrderXMLException {
//        Integer[] disposals = new Integer[] { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
//        XhbOrderValue order = null;
//        disposals = null;
//        order = XhibitDelegateHelper.getOrdersDelegate().createOrder(new Integer(123), new Integer(123), disposals,
//                false);
//        assertNotNull(order);
//        if (logging) {
//            log("testCreateBailOrder.");
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     */
//    public void testCreateCMPOrder() throws OrderException, OrderXMLException {
//        Integer[] disposals = new Integer[] { new Integer(1), new Integer(2), new Integer(3), new Integer(4) };
//        XhbOrderValue order = null;
//        disposals = null;
//        order = XhibitDelegateHelper.getOrdersDelegate().createOrder(new Integer(456), new Integer(456), null, false);
//        assertNotNull(order);
//        if (logging) {
//            log("testCreateCMPOrder.");
//        }
//        XhbOrderValue sOrder = XhibitDelegateHelper.getOrdersDelegate().saveOrder(order);
//        assertNotNull(sOrder);
//
//    }
//
//    /**
//     *
//     * @throws OrderException
//     * @throws OrderXMLException
//     * @throws SQLException
//     */
//    public void testGetStatusFromOrder() throws OrderException, OrderXMLException, SQLException {
//        Statement stmt = con.createStatement();
//        ResultSet rs = stmt.executeQuery("select order_id from XHB_ORDER where rownum = 1");
//        rs.next();
//        log("Attempting to rs.getInt(1);");
//        Integer pkey = new Integer(rs.getInt(1));
//
//        log("Attempting to controller.getOrder(" + pkey + ");");
//        XhbOrderValue order = null;
//        order = XhibitDelegateHelper.getOrdersDelegate().getOrder(pkey);
//        assertNotNull(order);
//        XhbOrderStatusValue status = order.getXhbOrderStatus();
//        String code = status.getCode();
//        assertEquals("Comparing status for the first Order with that which it " + "wsa created.", code, "NEW");
//        log("Status retrieved from order " + code);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testGetOrder() throws OrderException {
//        XhbOrderValue order = null;
//        order = XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123));
//        assertNotNull(order);
//        if (logging) {
//            log("testGetOrder.");
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testGetOrdersForDefendantOnCase() throws OrderException {
//        XhbOrderValue[] orders = null;
//        orders = XhibitDelegateHelper.getOrdersDelegate().getOrdersForDefendantOnCase(new Integer(1));
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
//     * @throws OrderException
//     */
//    public void testGetOrderTemplates() throws OrderException {
//        XhbOrderTemplateValue[] templates = null;
//        templates = XhibitDelegateHelper.getOrdersDelegate().getValidTemplates();
//        assertTrue(templates.length > 0);
//
//        for (int i = 0; i < templates.length; i++) {
//            assertNotNull(templates[i]);
//            /*
//             * log.debug("getCreatedBy " + templates[i].getCreatedBy());
//             * log.debug("getCreationDate " + templates[i].getCreationDate());
//             * log.debug("getDisplayTransformName " +
//             * templates[i].getDisplayTransformName());
//             * log.debug("getEditorTemplateName " +
//             * templates[i].getEditorTemplateName());
//             * log.debug("getLastUpdateDate " +
//             * templates[i].getLastUpdateDate()); log.debug("getLastUpdatedBy " +
//             * templates[i].getLastUpdatedBy());
//             * log.debug("getNarrativeTemplateName " +
//             * templates[i].getNarrativeTemplateName()); log.debug("getObsInd " +
//             * templates[i].getObsInd()); log.debug("getOrderTemplateId " +
//             * templates[i].getOrderTemplateId()); log.debug("getOrderTypeId " +
//             * templates[i].getOrderTypeId()); log.debug("getPrimaryKey " +
//             * templates[i].getPrimaryKey()); log.debug("getVersion " +
//             * templates[i].getVersion());
//             */
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testGetOrderTypes() throws OrderException {
//        XhbOrderTypeValue[] orderType = null;
//        orderType = XhibitDelegateHelper.getOrdersDelegate().getOrderTypes();
//        for (int i = 0; i < orderType.length; i++) {
//            log.debug("OrderType: " + i + " " + orderType[i].getCode());
//        }
//        assertNotNull(orderType);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testGetValidTemplates() throws OrderException {
//        XhbOrderTemplateValue[] templates = null;
//        templates = XhibitDelegateHelper.getOrdersDelegate().getValidTemplates();
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
//     */
//    public void testPrintOrder() throws OrderException, OrderXMLException {
//        try {
//            XhbOrderValue orderValue = null;
//            orderValue = XhibitDelegateHelper.getOrdersDelegate().saveOrder(
//                    XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123)));
//            assertNotNull("Save", orderValue);
//            orderValue = XhibitDelegateHelper.getOrdersDelegate().printOrder(
//                    XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123)));
//            assertNotNull("Print", orderValue);
//        } catch (CSUnrecoverableException ex) {
//            if (logging) {
//                log("testGetOrder.");
//            }
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testSaveOrder() throws OrderException, OrderXMLException {
//        XhbOrderValue orderValue = XhibitDelegateHelper.getOrdersDelegate().saveOrder(
//                XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123)));
//        assertNotNull(orderValue);
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testSignOrderNotPrinted() throws OrderException, OrderXMLException {
//        XhbOrderValue orderValue = null;
//        try {
//            orderValue = XhibitDelegateHelper.getOrdersDelegate().signOrder(
//                    XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123)));
//        } catch (OrderStateException ose) {
//            if (logging) {
//                log("testGetOrder.");
//            }
//        } finally {
//            assertNull(orderValue);
//        }
//    }
//
//    /**
//     *
//     * @throws OrderException
//     */
//    public void testSignOrderPrinted() throws OrderException, OrderXMLException {
//        XhbOrderValue orderValue = null;
//        try {
//            orderValue = XhibitDelegateHelper.getOrdersDelegate().getOrder(new Integer(123));
//            orderValue.setOrderStatusId(new Integer(3));
//            orderValue = XhibitDelegateHelper.getOrdersDelegate().signOrder(orderValue);
//            assertNotNull(orderValue);
//        } catch (OrderStateException ose) {
//            if (logging) {
//                log("testGetOrder.");
//            }
//        } finally {
//            assertNotNull(orderValue);
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