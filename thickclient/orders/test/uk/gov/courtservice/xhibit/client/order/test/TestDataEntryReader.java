//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.xhibit.client.order.OrderData;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
//import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
//import uk.gov.courtservice.xhibit.client.order.io.DataEntryFormReader;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
//
//public class TestDataEntryReader extends TestCase {
//    private static final Logger log = CSServices.getLogger(TestDataEntryReader.class);
//
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    private OrderReader or;
//
//    private OrderData data;
//
//    public TestDataEntryReader(String s) {
//        super(s);
//    }
//
//    protected void setUp() throws MalformedURLException, OrderReaderException {
//        or = new XMLOrderReader(new URL(res.getString("XMLDataJSPLocation")));
//
//        data = or.read();
//
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testRead() throws Exception {
//        DataEntryFormReader entrytemplatereader = new DataEntryFormReader(data);
//        DataEntryPanel entrytemplateRet = null;
//
//        URL url1 = new URL(res.getString("DataEntryTemplate"));
//        entrytemplateRet = entrytemplatereader.read(url1);
//        log.debug(entrytemplateRet);
//
//        assertNotNull(entrytemplateRet);
//    }
//}
//