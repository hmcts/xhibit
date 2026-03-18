//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.order.OrderData;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderWriter;
//
//public class TestXMLOrderWriter extends TestCase {
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    private OrderData data;
//
//    public TestXMLOrderWriter(String s) {
//        super(s);
//    }
//
//    protected void setUp() throws OrderReaderException, MalformedURLException {
//        OrderReader or = new XMLOrderReader(new URL(res.getString("XMLDataJSPLocation")));
//        data = or.read();
//
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testWrite() throws OrderReaderException, OrderWriterException, MalformedURLException {
//        OutputStream bos = new ByteArrayOutputStream();
//        // XMLOrderWriter xmlorderwriterToURL = new XMLOrderWriter(new
//        // URL(res.getString("OrderWriterOutputURL")));
//        XMLOrderWriter xmlorderwriter = new XMLOrderWriter(bos);
//        xmlorderwriter.write(data);
//        // assertEquals(714, bos.toString().length());
//        // assertEquals(1324089855, bos.toString().hashCode());
//        XMLOrderReader reader = new XMLOrderReader(new ByteArrayInputStream(bos.toString().getBytes()));
//        assertEquals(data.hashCode(), reader.read().hashCode());
//    }
//}
//