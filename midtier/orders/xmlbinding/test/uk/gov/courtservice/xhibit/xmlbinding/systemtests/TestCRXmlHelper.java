//package uk.gov.courtservice.xhibit.xmlbinding.systemtests;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//import javax.naming.NamingException;
//
//import org.apache.log4j.Logger;
//import org.iso_relax.verifier.Schema;
//
//import junit.framework.Assert;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.CRXmlHelper;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.DomConvertor;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.VerifierFactoryUtilities;
//
///**
// * <p>
// * Title: Test case for testing CRXmlHelper
// * </p>
// * <p>
// * Description: This test case uses the XmlHelperFactory and XmlHelpers to
// * generate and validate prepopulated XML for a CR order.
// * </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: EDS</p>
// * @author David Duncan
// * @version 1.0
// */
//public class TestCRXmlHelper extends TransactionTestCase
//{
//    private XmlHelperFactory factory;
//    private Schema schema;
//    private static Logger log = CSServices.getLogger(TestXMLHelpers.class);
//    private CRXmlHelper CRHelper;
//    private String CROrder;
//    private String CRXml;
//    private DomConvertor dom;
//    private VerifierFactoryUtilities util = new VerifierFactoryUtilities();
//
//    /**
//     * Default constructor for this TestCase.
//     * @param s TestCase name.
//     */
//    public TestCRXmlHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    /**
//     * Setup the XmlHelperFactory, and create a dom
//     * from produced CR Order.
//     */
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        factory = util.getXmlHelperFactory();
//
//        this.CRXml = factory.getDataXmlForOrder(new Integer(1), "CRO");
//        this.CRHelper = new CRXmlHelper("CRO");
//        //populate with data and get order
//        this.CROrder = CRHelper.getOrderXML(new Integer(1));
//        this.dom = new DomConvertor(CROrder);
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
//            throws OrderNotSupportedException, OrderXMLException
//    {
//        Assert.assertNotNull("Could not obtain an empty schema.", this.CRXml);
//
//        Assert.assertTrue("Empty schema is not valid.", CRXml.length() > 0);
//
//        Assert.assertNotNull("Could not obtain a imprisonment helper.", CRHelper);
//
//        Assert.assertNotNull("Could not obtain a populated imprisonment order.", CROrder);
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
//		Object o = dom.getValue("ord:Order/ord:OrderData/ord:CROrder/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType");
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
//			format.parse(dom.getValue("ord:Order/ord:OrderData/ord:CROrder/ord:OrderHeader/ord:OrderDate").toString());
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
//        String typeElement = dom.getValue("ord:Order/ord:OrderData/ord:CROrder/ord:OrderHeader/ord:OrderType").toString();
//        Assert.assertNotNull("Order type was not set", typeElement);
//
//        //Order Type code for a CR Order is "CRO"
//        Assert.assertEquals("OrderType is not set to the correct " +
//                            "code for CR Order", typeElement, "CRO");
//    }
//
//}
//