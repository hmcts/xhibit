//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.awt.Frame;
//import java.sql.Connection;
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
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersController;
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
//public class TestOrdersThickClient extends TestCase {
//    private static final Logger log = CSServices.getLogger(TestOrdersThickClient.class);
//
//    private static final String ERROR_NULL_REMOTE = "Remote interface reference is null.  "
//            + "It must be created by calling one of the Home interface methods first.";
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
//    Connection con;
//
//    /**
//     *
//     * @param s
//     */
//    public TestOrdersThickClient(String s) {
//        super(s);
//    }
//
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
//        // int i = stmt.executeUpdate("insert into XHB_ORDER_TYPE
//        // values (123, 'Bail Order', 0, 'TEST', 'TEST','03-MAR-03',
//        // '03-MAR-03', NULL)");
//        // int ii = stmt.executeUpdate("insert into XHB_ORDER_TYPE
//        // values (456, 'CMPO', 0, 'TEST', 'TEST','03-MAR-03', '03-MAR-03',
//        // NULL)");
//        // int j = stmt.executeUpdate("insert into XHB_ORDER_TEMPLATE
//        // values (123, 'file:/d:/projects/XHIBIT/thickclient/orders/xml/" +
//        // "documentTransforms/Bail Order.xsl', " +
//        // "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/Bail " +
//        // "Order.xml', 'file:/d:/projects/XHIBIT/thickclient/orders/xml/" +
//        // "exampleXML/DataEntryTemplate.xml', 123, 'JUNIT', 'JUNIT', " +
//        // "'25-FEB-03', '25-FEB-03', 123, null)");
//        // int jj = stmt.executeUpdate("insert into XHB_ORDER_TEMPLATE
//        // values (456, 'file:/d:/projects/XHIBIT/thickclient/orders/xml/" +
//        // "documentTransforms/Bail Order.xsl', " +
//        // "'file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/CP " +
//        // "Order.xml', 'file:/d:/projects/XHIBIT/thickclient/orders/xml/" +
//        // "exampleXML/CPOrderTemplate.xml', 456, 'JUNIT', 'JUNIT', " +
//        // "'25-FEB-03', '25-FEB-03', 456, null)");
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
//        // int m = stmt.executeUpdate("insert into xhb_order
//        // values (123, '25-FEB-03', 'JUNIT', '25-FEB-03', 'xml stuff', 2, 1,
//        // 123,
//        // 1, '25-FEB-03', 1, '25-FEB-03', 'JUNIT', 'JUNIT', 'JUNIT')");
//        // int mm = stmt.executeUpdate("insert into xhb_order
//        // values (456, '25-FEB-03', 'JUNIT', '25-FEB-03', 'xml stuff', 2, 1,
//        // 456, 1, '25-FEB-03', 1, '25-FEB-03', 'JUNIT', 'JUNIT', 'JUNIT')");
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
//     * @throws Exception
//     */
//    public void tearDown() throws Exception {
//        super.tearDown();
//
//        Statement stmt = con.createStatement();
//        // int n = stmt.executeUpdate("delete from XHB_ORDER where
//        // ORDER_TEMPLATE_ID IN (123, 456)");
//        // int m = stmt.executeUpdate("delete from XHB_ORDER where ORDER_ID =
//        // 123");
//        // int mm = stmt.executeUpdate("delete from XHB_ORDER where ORDER_ID =
//        // 456");
//        // int l = stmt.executeUpdate("delete from XHB_ORDER_STATUS where
//        // ORDER_STATUS_ID = 123");
//        // int ll = stmt.executeUpdate("delete from XHB_ORDER_STATUS where
//        // ORDER_STATUS_ID = 456");
//        // int k = stmt.executeUpdate("delete from XHB_ORDER_DELIVERY_STATUS
//        // where ORDER_DELIVERY_STATUS_ID = 123");
//        // int kk = stmt.executeUpdate("delete from XHB_ORDER_DELIVERY_STATUS
//        // where ORDER_DELIVERY_STATUS_ID = 456");
//        // int j = stmt.executeUpdate("delete from XHB_ORDER_TEMPLATE where
//        // ORDER_TEMPLATE_ID = 123");
//        // int jj = stmt.executeUpdate("delete from XHB_ORDER_TEMPLATE where
//        // ORDER_TEMPLATE_ID = 456");
//        // int i = stmt.executeUpdate("delete from XHB_ORDER_TYPE where
//        // ORDER_TYPE_ID = 123");
//        // int ii = stmt.executeUpdate("delete from XHB_ORDER_TYPE where
//        // ORDER_TYPE_ID = 456");
//
//        stmt.close();
//        con.close();
//        con = null;
//    }
//
//    /**
//     *
//     * @throws Exception
//     */
//    public void testThickClient() throws Exception {
//        OrdersWizardTest test = new OrdersWizardTest(new Frame());
//        // test.displayButtons(test);
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