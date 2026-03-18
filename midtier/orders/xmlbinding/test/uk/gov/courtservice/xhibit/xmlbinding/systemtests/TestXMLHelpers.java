//package uk.gov.courtservice.xhibit.xmlbinding.systemtests;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.StringReader;
//import java.net.URL;
//import java.util.Properties;
//
//import javax.transaction.UserTransaction;
//
//import org.apache.log4j.Logger;
//import org.iso_relax.verifier.Schema;
//import org.iso_relax.verifier.VerifierConfigurationException;
//import org.iso_relax.verifier.VerifierFactory;
//import org.xml.sax.InputSource;
//import org.xml.sax.SAXException;
//
//import junit.framework.Assert;
//
//import uk.gov.courtservice.framework.services.CSServices;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderNotSupportedException;
//import uk.gov.courtservice.xhibit.business.exceptions.orders.OrderXMLException;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//
//
///**
// * <p>
// * Title: Test case for testing XmlHelpers
// * </p>
// * <p>
// * Description: This test case uses the XmlHelperFactory and XmlHelpers to
// * generate and validate prepopulated XML for orders.
// * </p>
// */
//public class TestXMLHelpers  extends TransactionTestCase
//{
//    private XmlHelperFactory factory;
//
//    private Schema schema;
//
//    private static Logger log = CSServices.getLogger(TestXMLHelpers.class);
//
//    private UserTransaction transaction;
//
//    /**
//     * Default constructor for this TestCase.
//     * @param s TestCase name.
//     */
//    public TestXMLHelpers(String s) throws javax.naming.NamingException
//    {
//        super(s, true);
//     }
//
//    /**
//     * Setup the XmlHelperFactory for the TestCase.
//     */
//    protected void setUp() throws java.lang.Exception
//    {
//        super.setUp();
//
//        VerifierFactory vFactory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
//        try
//        {
//            URL myURL = this.getClass().getResource("/orders/Orders.xsd");
//            schema = vFactory.compileSchema(myURL.toString());
//        }
//        catch (VerifierConfigurationException e)
//        {
//            log.error("Failed to initialise Verifier/Schema.", e);  //To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Failed to initialise Verifier/Schema.");
//        }
//        catch (SAXException e)
//        {
//            log.error("Problem in SAX.", e);//To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Problem in SAX.");
//        }
//        catch (IOException e)
//        {
//            log.error("Problem in IO.", e);  //To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Problem in IO.");
//        }
//
//        Properties props = new Properties();
//        InputStream is = this.getClass().getResourceAsStream("/testxmlbindings.properties");
//        if(is!=null)
//        {
//            try
//            {
//                props.load(is);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//            }
//        }
//        factory = new XmlHelperFactory(props);
//    }
//
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a remand order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testRemandOrderUnit() throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String remandXML = factory.getDataXmlForOrder(new Integer(1), "RC");
//        Assert.assertNotNull(remandXML);
//        System.err.println(remandXML);
//        log.info(remandXML);
//        Assert.assertTrue("Generated Remand Order XML not valid.",
//                schema.newVerifier().verify(new InputSource(new StringReader(remandXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for an imprisonment order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testImprisonmentOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String imprisonmentXML = factory.getDataXmlForOrder(new Integer(1), "IMPO");
//        Assert.assertNotNull(imprisonmentXML);
//        Assert.assertTrue("Generated Imprisonment Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(imprisonmentXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a YoungOffenders order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testYoungOffendersOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String youngOffendersXML = factory.getDataXmlForOrder(new Integer(1), "COMY");
//        Assert.assertNotNull(youngOffendersXML);
//        Assert.assertTrue("Generated YoungOffenders Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(youngOffendersXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a CPO order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testCPOOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String CPOXML = factory.getDataXmlForOrder(new Integer(1), "CMPO");
//        Assert.assertNotNull(CPOXML);
//        Assert.assertTrue("Generated CPO Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(CPOXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a CPRO order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testCPROOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String CPROXML = factory.getDataXmlForOrder(new Integer(1), "CMPRO");
//        Assert.assertNotNull(CPROXML);
//        Assert.assertTrue("Generated CPRO Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(CPROXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a CRO order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testCROOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String CROXML = factory.getDataXmlForOrder(new Integer(1), "CRO");
//        Assert.assertNotNull(CROXML);
//        Assert.assertTrue("Generated CRO Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(CROXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a BenchWarrant order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testBenchWarrantOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String benchWarrantXML = factory.getDataXmlForOrder(new Integer(1), "BW");
//        Assert.assertNotNull(benchWarrantXML);
//        Assert.assertTrue("Generated BenchWarrant Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(benchWarrantXML))));
//    }
//
//    /**
//     * Simple unit test that for now will just try to get the XML for a BailConditions order.
//     * We will in future add a regression test that will validate against an existing
//     * 'correct' generated piece of XML.
//     * @throws OrderXMLException When there is a problem in the order XML
//     * @throws OrderNotSupportedException When the order requested is not supported.
//     */
//    public void testBailConditionsOrderUnit()
//        throws OrderXMLException, OrderNotSupportedException, VerifierConfigurationException, IOException, SAXException
//    {
//        Assert.assertNotNull(factory);
//        String bailConditionsXML = factory.getDataXmlForOrder(new Integer(1), "BC");
//        Assert.assertNotNull(bailConditionsXML);
//        Assert.assertTrue("Generated BailConditions Order XML not valid.",
//                          schema.newVerifier().verify(new InputSource(new StringReader(bailConditionsXML))));
//    }
//
//}
//