//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.exceptions.ValueNotFoundException;
//import uk.gov.courtservice.xhibit.client.order.OrderData;
//import uk.gov.courtservice.xhibit.client.order.exceptions.MalformedOrderDataException;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
//
//public class TestXMLOrderData extends TestCase {
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    private OrderData data;
//
//    public TestXMLOrderData(String s) {
//        super(s);
//    }
//
//    protected void setUp() throws Exception {
//        OrderReader or = new XMLOrderReader(new URL(res.getString("XMLDataJSPLocation")));
//
//        data = or.read();
//
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testGetValue() throws MalformedOrderDataException, ValueNotFoundException {
//        assertNotNull(data);
//        String s = (String) data.getValue("suretyValue");
//        // assertEquals("1970-01-21", s);
//    }
//
//    public void testSetValue() throws MalformedOrderDataException, ValueNotFoundException {
//        // data.setValue("dob", "1976-05-25");
//        // String newDate = (String) data.getValue("dob");
//        // assertEquals(newDate, "1976-05-25");
//    }
//}
//