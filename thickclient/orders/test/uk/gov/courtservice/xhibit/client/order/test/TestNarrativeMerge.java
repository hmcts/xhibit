//package uk.gov.courtservice.xhibit.client.order.test;
//
//import java.net.URL;
//
//import javax.xml.transform.TransformerException;
//
//import junit.framework.Assert;
//import junit.framework.TestCase;
//
//import org.apache.xpath.XPathAPI;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//
//import uk.gov.courtservice.xhibit.client.order.OrderFactory;
//import uk.gov.courtservice.xhibit.client.order.exceptions.OrderReaderException;
//import uk.gov.courtservice.xhibit.client.order.io.OrderReader;
//import uk.gov.courtservice.xhibit.client.order.xml.XMLOrderData;
//
///**
// * <p>
// * Title: Test case for testing the narrative merge process.
// * </p>
// * <p>
// * Description: Ensures that the narrative for each order can be succesfully
// * merged with order xml.
// * </p>
// * <p>
// * Copyright: Copyright (c) 2003
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author David Duncan
// * @version 1.0
// */
//public class TestNarrativeMerge extends TestCase {
//    private OrderReader orderReader, narrativeReader;
//
//    private String dataURL = "file:/d:/projects/XHIBIT/thickclient/orders/xml/exampleXML/BC_1.xml";
//
//    private String narrativeURL = "file:/d:/projects/XHIBIT/thickclient/orders/xml/narrativeXML/BailOrder_Narrative.xml";
//
//    public TestNarrativeMerge(String s) {
//        super(s);
//    }
//
//    /**
//     * Set up all objects needed for the merge test.
//     *
//     * @throws Exception
//     */
//    protected void setUp() throws Exception {
//        super.setUp();
//        orderReader = OrderFactory.getReader(new URL(dataURL).openStream());
//        narrativeReader = OrderFactory.getReader(new URL(narrativeURL).openStream());
//    }
//
//    /**
//     * Tear down all objects after test is run.
//     *
//     * @throws Exception
//     */
//    protected void tearDown() throws Exception {
//        super.tearDown();
//    }
//
//    /**
//     * Tests that the narrative xml has been successfully merged into the order
//     * xml.
//     */
//    public void testMerge() throws TransformerException {
//        Assert.assertNotNull("OrderReader could not be instantiated", orderReader);
//        Assert.assertNotNull("Narrative OrderReader could not be instantiated", narrativeReader);
//
//        XMLOrderData orderData = null;
//        XMLOrderData narrativeData = null;
//        try {
//            orderData = (XMLOrderData) orderReader.read();
//            narrativeData = (XMLOrderData) narrativeReader.read();
//        } catch (OrderReaderException e) {
//            e.printStackTrace(); // To change body of catch statement use
//            // Options | File Templates.
//        }
//        Assert.assertNotNull("orderData is null", orderData);
//        Assert.assertNotNull("narrativeData is null", narrativeData);
//
//        Document orderDom = orderData.getDom();
//        Document narrativeDom = narrativeData.getDom();
//
//        Assert.assertNotNull("orderData dom is null", orderDom);
//        Assert.assertNotNull("narrativeData dom is null", narrativeDom);
//
//        // perform simple tests on OrderData for Order and Narrative
//        Assert.assertEquals("Order Type should be 'BC'", "BC", orderData
//                .getValue("/ord:Order/ord:OrderData/ord:BailOrder/ord:OrderHeader/ord:OrderType"));
//        Assert.assertEquals("OrderHeader should include 'Text'", "Text", narrativeData
//                .getValue("/nar:Narrative/nar:Header"));
//
//        // get root nodes.
//        Element orderRoot = orderDom.getDocumentElement();
//        Node narrativeNode = XPathAPI.selectSingleNode(narrativeData.getDom(), "/nar:Narrative");
//
//        Element narrativeRoot = narrativeDom.getDocumentElement();
//
//        Node narToAdd = orderDom.importNode(narrativeRoot, true);
//        orderRoot.appendChild(narToAdd);
//
//        Assert.assertNotNull("No narrative tag was found.", orderData.getValue("//nar:Narrative"));
//    }
//}
//