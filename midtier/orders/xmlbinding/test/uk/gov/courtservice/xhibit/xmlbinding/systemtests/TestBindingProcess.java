///**
// * Created by IntelliJ IDEA.
// * User: hzf3bb
// * Date: Mar 10, 2003
// * Time: 2:21:09 PM
// * To change this template use Options | File Templates.
// */
//package uk.gov.courtservice.xhibit.xmlbinding.systemtests;
//
//import java.io.BufferedWriter;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.util.Properties;
//
//import javax.naming.NamingException;
//
//import junit.framework.Assert;
//import junit.framework.Test;
//import junit.framework.TestSuite;
//
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import uk.gov.courtservice.framework.util.StringUtil;
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.util.VerifierFactoryUtilities;
//
//public class TestBindingProcess extends TransactionTestCase
//{
//    private Properties props;
//    private String defOnCaseId;
//    private String typeCode;
//    private String previousOrder;
//    private VerifierFactoryUtilities util = new VerifierFactoryUtilities();
//    private XmlHelperFactory factory;
//    String order;
//
//    public TestBindingProcess(String def, String type, String prev)
//            throws NamingException
//    {
//        super("TestOrder", true);
//        this.defOnCaseId = def;
//        this.typeCode = type;
//        this.previousOrder = prev;
//    }
//
//    protected void setUp() throws Exception
//    {
//        super.setUp();
//        this.factory = util.getXmlHelperFactory();
//        order = factory.getDataXmlForOrder(new Integer(Integer.parseInt(defOnCaseId)), typeCode);
//    }
//
//    protected void tearDown() throws Exception
//    {
//        super.tearDown();
//    }
//
//    public void TestOrder() throws IOException
//    {
//        //Create order
//        Assert.assertNotNull("Order could not be created", this.order);
//
//        //Check to see if order already exists.
//        File order = new File(previousOrder);
//        if (order.exists())
//        {
//            FileInputStream fis = new FileInputStream(order);
//            InputStreamReader r = new InputStreamReader(fis);
//            String newOrder = StringUtil.readString(fis);
//            newOrder.trim();
//            boolean orderUnchanged = false;
//
//            if (newOrder.trim().toString().compareTo(this.order.trim()) == 0)
//            {
//                //order is unchanged.
//                orderUnchanged = true;
//            }
//
//            Assert.assertTrue("Comparison of order has failed.", orderUnchanged);
//
//        }else{
//            FileWriter writer = new FileWriter(previousOrder);
//            BufferedWriter bw = new BufferedWriter(writer);
//            PrintWriter outFile = new PrintWriter(bw);
//            outFile.println(this.order);
//            outFile.close();
//        }
//
//    }
//
//    public static Test suite() throws NamingException
//    {
//        //@todo place test data in a properties file.
//        //String test data
//        String[] defId = new String[]{"1","2","3","4","5","6","7","8","9"};
//        String[] type = new String[]{"RC","IMPO","COMY","CMPO","CMPRO","CRO","BW","BC"};
//
//        TestSuite suite = new TestSuite();
//
//        for (int j=0; j<type.length; j++)
//        {
//            for (int i=0; i<defId.length; i++)
//            {
//                suite.addTest(new TestBindingProcess(defId[i], type[j], type[j]+"_"+defId[i]+".xml"));
//            }
//        }
//        return (suite);
//    }
//
//    public static void main(String[] args) throws NamingException
//    {
//        junit.textui.TestRunner.run (suite());
//    }
//}
//