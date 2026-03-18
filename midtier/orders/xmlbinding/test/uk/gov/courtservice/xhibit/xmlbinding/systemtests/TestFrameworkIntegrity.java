//package uk.gov.courtservice.xhibit.xmlbinding.systemtests;
//
//import java.io.InputStream;
//import java.io.InputStreamReader;
//
//import javax.naming.NamingException;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.xhibit.xmlbinding.generated.orders.Order;
//
//public class TestFrameworkIntegrity extends TransactionTestCase
//{
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//    }
//
//
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//
//    }
//
//    public void testSimpleValue() throws Exception
//    {
//        InputStream inputStream = this.getClass().getResourceAsStream("/simple.xml");
//        InputStreamReader reader= new InputStreamReader(inputStream);
//        uk.gov.courtservice.xhibit.xmlbinding.generated.orders.OrderType o= Order.unmarshal(reader);
//        assertEquals("Passport should be surrendered.", true, o.getOrderData().getBailOrder().getBailPreConditions().getPassport().getSurrendered());
//
//    }
//
//    public TestFrameworkIntegrity(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//}
//