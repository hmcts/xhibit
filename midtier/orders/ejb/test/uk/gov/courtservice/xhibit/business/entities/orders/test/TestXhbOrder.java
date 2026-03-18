//package uk.gov.courtservice.xhibit.business.entities.orders.test;
//
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrder;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatus;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatus;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
//import uk.gov.courtservice.framework.services.CSServices;
//
//import javax.ejb.FinderException;
//import java.util.Collection;
//import java.util.Date;
//import java.util.Iterator;
//import java.sql.Timestamp;
//
//public class TestXhbOrder extends junit.framework.TestCase
//{
//    private XhbOrderHome home;
//    private XhbOrderTemplateHome templateHome;
//    private XhbOrderStatusHome statusHome;
//    private XhbOrderDeliveryStatusHome deliveryHome;
//    private XhbOrderTypeHome typeHome;
//    private static final String DATA_XML = "<?xml version='1.0'?>";
//    private static final String COMMUNITY_PUNISHMENT_ORDER_CODE = "CPO";
//
//
//    public TestXhbOrder(String s)
//    {
//        super(s);
//        home = (XhbOrderHome) CSServices.getServiceLocator().getLocalHome(XhbOrderHome.class);
//        deliveryHome = (XhbOrderDeliveryStatusHome) CSServices.getServiceLocator().getLocalHome(XhbOrderDeliveryStatusHome.class);
//        statusHome = (XhbOrderStatusHome) CSServices.getServiceLocator().getLocalHome(XhbOrderStatusHome.class);
//        templateHome = (XhbOrderTemplateHome) CSServices.getServiceLocator().getLocalHome(XhbOrderTemplateHome.class);
//        typeHome = (XhbOrderTypeHome) CSServices.getServiceLocator().getLocalHome(XhbOrderTypeHome.class);
//
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//
//        XhbOrderDeliveryStatus deliveryOrderStatus= deliveryHome.findByCodeUniquely("DELIVERED");
//        XhbOrderStatus orderStatus= statusHome.findByCodeUniquely("SAVED");
//
//        XhbOrderType orderType=  typeHome.create(COMMUNITY_PUNISHMENT_ORDER_CODE, "Blahhh", new Integer(0), "JUnit", "JUnit", new Timestamp(new Date().getTime()),new Timestamp(new Date().getTime()),new Integer(236));
////             typeHome.findByCodeUniquely(COMMUNITY_PUNISHMENT_ORDER_CODE);
//        XhbOrderTemplate orderTemplate= templateHome.create( "test", "test","test", new Integer(-1),   "JUNIT", "JUNIT", new Timestamp(new Date().getTime()), new Timestamp(new Date().getTime()), orderType);
//
//        home.create(DATA_XML, new Integer(0), new Timestamp(new Date().getTime()), new Integer(1), new Timestamp(new Date().getTime()),"JUnit", "JUnit",
//                deliveryOrderStatus,
//                orderStatus,
//                orderTemplate);
//
//
//
//    }
//
//
//    public void tearDown() throws Exception
//    {
//        super.tearDown();
//        Collection allBeans= home.findAll();
//        for (Iterator iterator = allBeans.iterator(); iterator.hasNext();)
//        {
//            XhbOrder order = (XhbOrder) iterator.next();
//            order.remove();
//        }
//
//        allBeans= templateHome.findAll();
//        for (Iterator iterator = allBeans.iterator(); iterator.hasNext();)
//        {
//            XhbOrderTemplate order = (XhbOrderTemplate) iterator.next();
//            order.remove();
//        }
//        allBeans= typeHome.findAll();
//        for (Iterator iterator = allBeans.iterator(); iterator.hasNext();)
//        {
//            XhbOrderType order = (XhbOrderType) iterator.next();
//            order.remove();
//        }
//    }
//
//
//    public void testFindAll() throws FinderException
//    {
//        Collection result = home.findAll();
//    }
//}
//