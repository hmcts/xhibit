///**
// * Created by IntelliJ IDEA.
// * User: hzf3bb
// * Date: Mar 7, 2003
// * Time: 3:38:55 PM
// * To change this template use Options | File Templates.
// */
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
//import uk.gov.courtservice.xhibit.xmlbinding.orders.BailXmlHelper;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.DomConvertor;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.VerifierFactoryUtilities;
//
//public class TestBailXmlHelper extends TransactionTestCase
//{
//    private XmlHelperFactory factory;
//    private Schema schema;
//    private static Logger log = CSServices.getLogger(TestXMLHelpers.class);
//    private BailXmlHelper bailHelper;
//    private String bailOrder;
//    private String bailXml;
//    private DomConvertor domConvertor;
//    private VerifierFactoryUtilities util = new VerifierFactoryUtilities();
//
//    public TestBailXmlHelper(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//
//        factory = util.getXmlHelperFactory();
//
//        this.bailXml = factory.getDataXmlForOrder(new Integer(1), "BC");
//        this.bailHelper = new BailXmlHelper("BC");
//        //populate with data and get order
//        this.bailOrder = bailHelper.getOrderXML(new Integer(1));
//        this.domConvertor = new DomConvertor(bailOrder);
//    }
//
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
//        Assert.assertNotNull("Could not obtain an empty schema.", this.bailXml);
//
//        Assert.assertTrue("Empty schema is not valid.", bailXml.length() > 0);
//
//        Assert.assertNotNull("Could not obtain a imprisonment helper.", bailHelper);
//
//        Assert.assertNotNull("Could not obtain a populated imprisonment order.", bailOrder);
//
//    }
//
//    public void testEnumeration()
//    {
//        assertNotNull("Order could not be parsed to a DOM", domConvertor.getDom());
//
//        Object o = domConvertor.getValue("ord:Order/ord:OrderData/ord:BailOrder/ord:OrderHeader/ord:CourtHouse/ord:CourtHouseType");
//        assertNotNull("CourtHouseType was not found", o);
//        boolean courtName = false;
//
//        if ((o.toString().equals("magistrates court")) || (o.toString().equals("crown court")))
//        {
//            courtName = true;
//        }
//
//        assertTrue("Court Name is invalid", courtName);
//    }
//
//    public void testDateFormat()
//    {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
//        boolean isValidDate = true;
//
//        try
//        {
//            format.parse(domConvertor.getValue("ord:Order/ord:OrderData/ord:BailOrder/ord:OrderHeader/ord:OrderDate").toString());
//        } catch (ParseException pe)
//        {
//            //not a valid date format.
//            isValidDate = false;
//        }
//
//        assertTrue("OrderDate is not a valid date format", isValidDate);
//    }
//
//}
//