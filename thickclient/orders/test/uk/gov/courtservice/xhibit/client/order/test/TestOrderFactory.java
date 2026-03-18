//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.order.OrderFactory;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderTransformException;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderWriterException;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.io.OrderTransform;
//import uk.gov.courtservice.xhibit.client.order.io.OrderWriter;
//
//public class TestOrderFactory extends TestCase {
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    /**
//     *
//     * @param s
//     */
//    public TestOrderFactory(String s) {
//        super(s);
//    }
//
//    /**
//     *
//     */
//    protected void setUp() {
//    }
//
//    /**
//     *
//     */
//    protected void tearDown() {
//    }
//
//    /**
//     *
//     * @throws IOException
//     */
//    public void testGetReader() throws IOException {
//        InputStream is1 = new URL(res.getString("XMLDataJSPLocation")).openStream();
//        OrderReader orderreaderRet = OrderFactory.getReader(is1);
//    }
//
//    /**
//     *
//     * @throws OrderTransformException
//     */
//    public void testGetTransform() throws OrderTransformException {
//
//        OrderTransform ordertransformRet = OrderFactory.getTransform();
//
//    }
//
//    /**
//     *
//     * @throws IOException
//     * @throws OrderTransformException
//     */
//    public void testGetTransform1() throws IOException, OrderTransformException {
//        InputStream is1 = new URL(res.getString("simpleTransform")).openStream();
//        OrderTransform ordertransformRet = OrderFactory.getTransform(is1);
//
//    }
//
//    /**
//     *
//     * @throws MalformedURLException
//     * @throws OrderTransformException
//     */
//    public void testGetTransform2() throws MalformedURLException, OrderTransformException {
//        URL url1 = new URL(res.getString("simpleTransform"));
//        OrderTransform ordertransformRet = OrderFactory.getTransform(url1);
//
//    }
//
//    /**
//     *
//     * @throws OrderWriterException
//     */
//    public void testGetWriter() throws OrderWriterException {
//        OutputStream os1 = new ByteArrayOutputStream();
//        OrderWriter orderwriterRet = OrderFactory.getWriter(os1);
//
//    }
//}
//