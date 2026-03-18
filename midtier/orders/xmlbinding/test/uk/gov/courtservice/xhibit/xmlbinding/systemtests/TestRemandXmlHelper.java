//package uk.gov.courtservice.xhibit.xmlbinding.systemtests;
//
//import java.io.BufferedWriter;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//import javax.naming.NamingException;
//import javax.xml.parsers.ParserConfigurationException;
//
//import org.apache.log4j.Logger;
//import org.iso_relax.verifier.Schema;
//import org.xml.sax.SAXException;
//
//import junit.framework.Assert;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.RemandXmlHelper;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.DomConvertor;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.VerifierFactoryUtilities;
//
///**
// * <p>
// * Title: Test case for testing RemandXmlHelper
// * </p>
// * <p>
// * Description: This test case uses the XmlHelperFactory and XmlHelpers to
// * generate and validate prepopulated XML for a Remand Order.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author David Duncan
// * @version 1.0
// */
//public class TestRemandXmlHelper extends TransactionTestCase
//{
//    private XmlHelperFactory factory;
//    private Schema schema;
//    private static Logger log = CSServices.getLogger(TestXMLHelpers.class);
//    private RemandXmlHelper remandHelper;
//    private String remandOrder;
//    private String remandXml;
//    private DomConvertor dom;
//    private VerifierFactoryUtilities util = new VerifierFactoryUtilities();
//
//    /**
//     * Default constructor for this TestCase.
//     * @param s TestCase name.
//     */
//    public TestRemandXmlHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    /**
//     * Setup the XmlHelperFactory, and create a dom
//     * from produced Remand Order.
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        factory = util.getXmlHelperFactory();
//
//        this.remandXml = factory.getDataXmlForOrder(new Integer(1), "RC");
//        this.remandHelper = new RemandXmlHelper("RC");
//        //populate with data and get order
//        this.remandOrder = remandHelper.getOrderXML(new Integer(1));
//
//        this.dom = new DomConvertor(remandOrder);
//        FileWriter writer = new FileWriter("TestXML.xml");
//        BufferedWriter bw = new BufferedWriter(writer);
//        PrintWriter outFile = new PrintWriter(bw);
//        outFile.println(this.remandOrder);
//        outFile.close();
//    }
//
//    /**
//     * Tear down objects.
//     * @throws Exception
//     */
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//    /**
//     * Tests presence of values in produced order. Checks to
//     * perform will be enumerations, date formats, presence of
//     * mandatory values...
//     */
//    public void testGetOrderXML()
//            throws OrderNotSupportedException, OrderXMLException,
//            ParserConfigurationException, SAXException, IOException
//    {
//        Assert.assertNotNull("Could not obtain an empty schema.", this.remandXml);
//
//        Assert.assertTrue("Empty schema is not valid.", remandXml.length() > 0);
//
//        Assert.assertNotNull("Could not obtain a remand helper.", remandHelper);
//
//        Assert.assertNotNull("Could not obtain a populated remand order.", remandOrder);
//
//    }
//
//    /**
//     * Test enumeration values.
//     */
//    public void testEnumeration()
//	{
//		assertNotNull("Order could not be parsed to a DOM", dom.getDom());
//
//		Object o = dom.getValue("ord:Order/ord:OrderData/ord:RemandOrder/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType");
//		assertNotNull("CourtHouseType was not found", o);
//		boolean courtName = false;
//
//		if ((o.toString().equals("magistrates court")) || (o.toString().equals("crown court")))
//		{
//			courtName = true;
//		}
//
//		assertTrue("Court Name is invalid", courtName);
//	}
//
//    /**
//     * Test the populated date is a correct format.
//     */
//	public void testDateFormat()
//	{
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
//		boolean isValidDate = true;
//
//		try{
//			format.parse(dom.getValue("ord:Order/ord:OrderData/ord:RemandOrder/ord:OrderHeader/ord:OrderDate").toString());
//		}catch(ParseException pe)
//		{
//			//not a valid date format.
//			isValidDate = false;
//		}
//
//		assertTrue("OrderDate is not a valid date format", isValidDate);
//	}
//
//    /**
//     * Tests the returned order is of the correct type
//     */
//    public void testOrderType()
//    {
//        String typeElement = dom.getValue("ord:Order/ord:OrderData/ord:RemandOrder/ord:OrderHeader/ord:OrderType").toString();
//        Assert.assertNotNull("Order type was not set", typeElement);
//
//        //Order Type code for a remand order is "RC"
//        Assert.assertEquals("OrderType is not set to the correct " +
//                            "code for Remand Order", typeElement, "RC");
//    }
//
//
//
//}
//