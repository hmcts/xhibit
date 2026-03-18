//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.net.URL;
//import java.util.ResourceBundle;
//
//import javax.swing.JComponent;
//
//import junit.framework.TestCase;
//import uk.gov.courtservice.xhibit.client.order.OrderData;
//import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
//import uk.gov.courtservice.xhibit.client.order.io.DataEntryFormReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderReader;
//
//public class TestDataEntryForm extends TestCase {
//    private DataEntryFormReader entrytemplatereader = null;
//
//    private DataEntryPanel entrytemplateRet = null;
//
//    static ResourceBundle res = ResourceBundle.getBundle("uk.gov.courtservice.xhibit.client.order.test.Resource");
//
//    private XMLOrderReader or;
//
//    private OrderData data;
//
//    public TestDataEntryForm(String s) {
//        super(s);
//    }
//
//    protected void setUp() throws Exception {
//        or = new XMLOrderReader(new URL(res.getString("XMLDataJSPLocation")));
//        data = or.read();
//        entrytemplatereader = new DataEntryFormReader(data);
//        URL url1 = new URL(res.getString("DataEntryTemplate"));
//        entrytemplateRet = entrytemplatereader.read(url1);
//
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testAddComponent() {
//        JComponent jcomponentRet = entrytemplateRet.getRootContainer();
//        assertNotNull(jcomponentRet);
//    }
//
//    public void testGetRootContainer() {
//        DataEntryPanel dataentryform = new DataEntryPanel();
//        JComponent jcomponentRet = dataentryform.getRootContainer();
//        assertNotNull(jcomponentRet);
//    }
//}
//