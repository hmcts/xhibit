//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.order.OrderData;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
//
//public class TestXMLOrderReader extends TestCase {
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    public TestXMLOrderReader(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testRead() throws OrderReaderException, MalformedURLException {
//        OrderData result = null;
//        OrderReader or = new XMLOrderReader(new URL(res.getString("XMLDataJSPLocation")));
//        result = or.read();
//        assertNotNull(result);
//    }
//}
//