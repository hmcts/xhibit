//package uk.gov.courtservice.xhibit.business.services.orders.test;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersControllerHome;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersController;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceControllerHome;
//import uk.gov.courtservice.xhibit.business.services.orders.OrdersReferenceController;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplateHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderType;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTemplate;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrder;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatus;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatus;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderDeliveryStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderStatusHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderHome;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
//import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderTypeValue;
//import uk.gov.courtservice.xhibit.business.entities.ordersref.XhbRefCourtValue;
//
//import javax.ejb.FinderException;
//import javax.ejb.RemoveException;
//import javax.ejb.EJBException;
//import java.sql.Timestamp;
//import java.util.Date;
//import java.util.Collection;
//import java.util.Iterator;
//import java.rmi.RemoteException;
//
///**
// * This JUnit test is designed to be deployed to the same container as the Entity EJBs.
// */
//public class TestOrdersReferenceController extends junit.framework.TestCase
//{
//
//    private OrdersReferenceControllerHome home;
//    private OrdersReferenceController controller;
//
//
//    public TestOrdersReferenceController(String s)
//    {
//        super(s);
//        home = (OrdersReferenceControllerHome) CSServices.getServiceLocator().getRemoteHome(OrdersReferenceControllerHome.class);
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
//        controller = home.create();
//    }
//
//
//    public void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//
//    public void testGetCourts() throws Exception
//    {
//        XhbRefCourtValue[] validCourts;
//        validCourts = controller.getCourts(new Integer(1));
//        for (int i = 0; i < validCourts.length; i++)
//        {
//            XhbRefCourtValue validCourt = validCourts[i];
//        }
//    }
//
//}
//
//