///**
// * Created by IntelliJ IDEA.
// * User: hzf3bb
// * Date: Mar 10, 2003
// * Time: 10:46:26 AM
// * To change this template use Options | File Templates.
// */
//package uk.gov.courtservice.xhibit.xmlbinding.systemtests.util;
//
//import uk.gov.courtservice.xhibit.xmlbinding.orders.factories.XmlHelperFactory;
//import uk.gov.courtservice.xhibit.xmlbinding.systemtests.TestXMLHelpers;
//import uk.gov.courtservice.framework.services.CSServices;
//import org.iso_relax.verifier.VerifierFactory;
//import org.iso_relax.verifier.VerifierConfigurationException;
//import org.iso_relax.verifier.Schema;
//import org.xml.sax.SAXException;
//import org.apache.log4j.Logger;
//
//import java.net.URL;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.Properties;
//
//public class VerifierFactoryUtilities
//{
//    private Schema schema;
//    private static Logger log = CSServices.getLogger(TestXMLHelpers.class);
//
//    public  XmlHelperFactory getXmlHelperFactory()
//    {
//         VerifierFactory vFactory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
//        try
//        {
//            URL myURL = this.getClass().getResource("/orders/Orders.xsd");
//            System.err.println(myURL.toString());
//            //!********!!**!*!*!* Need to sort out location.
//            schema = vFactory.compileSchema(myURL.toString());
//        } catch (VerifierConfigurationException e)
//        {
//            log.error("Failed to initialise Verifier/Schema.", e);  //To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Failed to initialise Verifier/Schema.");
//        } catch (SAXException e)
//        {
//            log.error("Problem in SAX.", e);//To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Problem in SAX.");
//        } catch (IOException e)
//        {
//            log.error("Problem in IO.", e);  //To change body of catch statement use Options | File Templates.
//            throw new RuntimeException("Problem in IO.");
//        }
//
//        Properties props = new Properties();
//        InputStream is = this.getClass().getResourceAsStream("/testxmlbindings.properties");
//        if (is != null)
//        {
//            try
//            {
//                props.load(is);
//            } catch (IOException e)
//            {
//                e.printStackTrace();  //To change body of catch statement use Options | File Templates.
//            }
//        }
//
//        return new XmlHelperFactory(props);
//    }
//}
//