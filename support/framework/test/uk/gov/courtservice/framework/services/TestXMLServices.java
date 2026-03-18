package uk.gov.courtservice.framework.services;

import java.io.InputStream;
import java.util.Properties;
import java.util.Vector;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.stream.StreamSource;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.xml.XMLServicesImpl;

/**
 * <p>
 * Title: TestXMLServices
 * </p>
 * <p>
 * Description: Test harness for XMLServices class.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author Faisal Shoukat
 * @version $Id: TestXMLServices.java,v 1.5 2006/08/31 12:32:16 gzw0qg Exp $
 *
 * <Change History/>
 *
 * <P>
 * 17/02/03 - AWD - Test to generate XML from properties modified to include XML
 * header in expected result.
 *
 * Test to check when an input file does not exist modified to catch
 * CSUnrecoverableException.
 *
 * Test to read from file modified to read from /config/xml/RemandOrder.xml
 * rather than non-existent /config/xml/import.xml
 * </P>
 *
 * <P>
 * 17/02/03 - AWD - Test added to perform XML transformations
 * </P>
 *
 * <P>
 * 05/03/03 - JB - Added tests for XML generation from collections
 * </P>
 * Improved logging.
 * </P>
 *
 * <P>
 * 08/04/03 - JB - Adjusted tests to include \r\n after the xml header
 * </P>
 *
 */
public class TestXMLServices extends TestCase implements URIResolver {
    // logging results
    private static Logger log = CSServices.getLogger(TestXMLServices.class);

    // class under test
    private XMLServices xmlServ = null;

    // all tests should use same encoding - i changed this from UTF-8 as it
    // was
    // the only thing causing many of the tests to fail - JonP
    private static final String ENCODING = "UTF-8";

    public TestXMLServices(String s) {
        super(s);
    }

    // called before every test
    protected void setUp() {
        xmlServ = XMLServicesImpl.getInstance();
    }

    // called after every test
    protected void tearDown() {
        xmlServ = null;
    }
//
//    // added 29-Sep-2003
//    public void testLoadDocument() {
//        log.debug("## [testLoadDocument]");
//
//        // load a document we know exists
//        String validFile = "/config/courtlog/transformer/public_display/10100.xsl";
//        InputStream is = xmlServ.loadDocument(validFile);
//        assertNotNull("stream returned for " + validFile + " was null", is);
//
//        // load a document we know does not exist
//        String invalidFile = "/config/courtlog/transformer/public_display/40100.xsl";
//        try {
//            // this shouldn't really throw an exception cos it's an expected
//            // condition - eg public display won't have an xsl for every
//            // event
//            is = xmlServ.loadDocument(invalidFile);
//            fail("should have thrown a CSUnrecoverableException)");
//        } catch (CSUnrecoverableException expected) {
//        }
//    }
//
//    public void testCreateDocFromFile() {
//        log.debug("## [testCreateDocFromFile]");
//
//        // check we can create document
//        String validFile = "/config/xml/RemandOrder.xml";
//        Document doc = xmlServ.createDocFromFile(validFile);
//        assertNotNull("document returned for " + validFile + " was null", doc);
//
//        // check the document contents
//        NodeList fieldList = doc.getElementsByTagName("field");
//        assertNotNull("field node list was null", fieldList);
//        assertEquals(19, fieldList.getLength());
//    }
//
//    // public static void testCreateDocFromValue()
//    // {
//    // log.debug("## [testCreateDocFromValue]");
//    //
//    // String fileToRead = "/config/xml/RemandOrder.xml";
//    // try
//    // {
//    // PrintingValueObject printValue = new PrintingValueObject("John
//    // Smith", "111111", "12/12/2001",
//    // "Liverpool", "12/12/2002", "Prison", "crown", "12/12/99", "[report]",
//    // "sent", "defendent",
//    // "12/12/1999", "Manslaughter", "12/12/02", "JSmith", "04/10/2002",
//    // "0433", "12/12/02", "commited");
//    //
//    //
//    // XMLServices xml = XMLServicesImpl.getInstance(); //new
//    // XMLServicesImpl();
//    // Document doc = xml.createDocFromValue(printValue, fileToRead);
//    //
//    // // Read exhibit connection data
//    // NodeList exhibitNodes = doc.getElementsByTagName("remand_details");
//    // Element exhibitElement = (Element)exhibitNodes.item(0);
//    // NodeList nodeList = exhibitElement.getElementsByTagName("case_no");
//    // String value = nodeList.item(0).getFirstChild().getNodeValue();
//    // log.debug("value from file = " + value);
//    // assertEquals(value, "111111");
//    // }
//    // catch(CSXMLServicesException e)
//    // {
//    // log.fatal("Exception = " + e);
//    // }
//    // }
//
//    // public static void testNoResource()
//    // {
//    // log.debug("## [testNoResource]");
//    //
//    // String fileToRead = "/RemandOrder.xml";
//    // try
//    // {
//    // PrintingValueObject printValue = new PrintingValueObject("John
//    // Smith", "111111", "12/12/2001",
//    // "Liverpool", "12/12/2002", "Prison", "crown", "12/12/99", "[report]",
//    // "sent", "defendent",
//    // "12/12/1999", "Manslaughter", "12/12/02", "JSmith", "04/10/2002",
//    // "0433", "12/12/02", "commited");
//    //
//    //
//    // XMLServices xml = XMLServicesImpl.getInstance(); //new
//    // XMLServicesImpl();
//    // Document doc = xml.createDocFromValue(printValue, fileToRead);
//    //
//    // // Read exhibit connection data
//    // NodeList exhibitNodes = doc.getElementsByTagName("remand_details");
//    // Element exhibitElement = (Element)exhibitNodes.item(0);
//    // NodeList nodeList = exhibitElement.getElementsByTagName("case_no");
//    // String value = nodeList.item(0).getFirstChild().getNodeValue();
//    // log.debug("value from file = " + value);
//    // fail();
//    // }
//    // catch(CSUnrecoverableException e)
//    // {
//    // log.fatal("Exception = " + e);
//    // }
//    // }
//
//    public void testGenerateXMLFromPropSet() {
//        log.debug("## [testgenerateXMLFromPropSet]");
//
//        Properties props = new Properties();
//        props.setProperty("courtDate", "12/12/02");
//        props.setProperty("courtCode", "0433");
//        props.setProperty("transferDate", "12/12/02");
//        String expected = "<?xml version=\"1.0\" encoding=\""
//                + ENCODING
//                + "\"?>\r\n<printingValueObject><courtDate>12/12/02</courtDate><courtCode>0433</courtCode><transferDate>12/12/02</transferDate></printingValueObject>";
//
//        try {
//            String actual = xmlServ.generateXMLFromPropSet(props, "printingValueObject");
//            log.debug("expected == " + expected);
//            log.debug("actual   == " + actual);
//
//            assertTrue(expected.equalsIgnoreCase(actual));
//            // assertEquals(xmlString, xmlActual);
//        } catch (Exception cse) {
//            System.out.println("in Catch block of generate xml");
//            log.debug("Exception = " + cse);
//        }
//    }
//
//    public static void testgenerateXMLFromCollection() {
//        log.debug("## [testgenerateXMLFromCollection]");
//        try {
//            Vector v = new Vector();
//            Properties props = new Properties();
//            props.setProperty("courtDate", "12/12/02");
//            props.setProperty("courtCode", "0433");
//            // Add 2 to vector...
//            v.add(props);
//            v.add(props);
//            Properties topLevel = new Properties();
//            topLevel.put("printingValueObject", v);
//
//            String expected = "<?xml version=\"1.0\" encoding=\""
//                    + ENCODING
//                    + "\"?>\r\n<topLevel><printingValueObject><courtDate>12/12/02</courtDate><courtCode>0433</courtCode></printingValueObject><printingValueObject><courtDate>12/12/02</courtDate><courtCode>0433</courtCode></printingValueObject></topLevel>";
//
//            XMLServicesImpl xml = XMLServicesImpl.getInstance(); // new
//            // XMLServicesImpl();
//            // String xmlString = xml.generateXMLFromCollection(v,
//            // "printingValueObject");
//            log.debug("topLevel: " + topLevel);
//            String xmlString = xml.generateXMLFromPropSet(topLevel, "topLevel");
//            log.debug("expected == " + expected);
//            log.debug("actual   == " + xmlString);
//
//            assertEquals(expected, xmlString);
//        } catch (Exception cse) {
//            fail(cse.toString());
//            System.out.println("in Catch block of generate xml");
//            log.debug("Exception = " + cse);
//        }
//    }
//
//    public static void testGenerateXMLFromComplex() {
//        log.debug("## [testGenerateXMLFromComplex]");
//        try {
//            Vector v = new Vector();
//            Properties details = new Properties();
//            details.setProperty("courtDate", "12/12/02");
//            details.setProperty("courtCode", "0433");
//            // Add 2 to vector...
//            v.add(details);
//            v.add(details);
//
//            Properties props = new Properties();
//            props.setProperty("eventType", "10100");
//            props.put("collection", v);
//
//            String expected = "<?xml version=\"1.0\" encoding=\"" + ENCODING + "\"?>\r\n"
//                    + "<printingValueObject><eventType>10100</eventType>"
//                    + "<collection><courtDate>12/12/02</courtDate><courtCode>0433</courtCode></collection>"
//                    + "<collection><courtDate>12/12/02</courtDate><courtCode>0433</courtCode></collection>"
//                    + "</printingValueObject>";
//
//            XMLServicesImpl xml = XMLServicesImpl.getInstance(); // new
//            // XMLServicesImpl();
//            String xmlString = xml.generateXMLFromPropSet(props, "printingValueObject");
//            log.debug("expected == " + expected);
//            log.debug("actual   == " + xmlString);
//
//            assertEquals(expected, xmlString);
//        } catch (Exception cse) {
//            System.out.println("in Catch block of generate xml");
//            log.debug("Exception = " + cse);
//            fail();
//        }
//    }
//
//    public static void testgenerateXMLFromVeryComplex() {
//        log.debug("## [testgenerateXMLFromVeryComplex]");
//        try {
//            Vector v = new Vector();
//            Properties subDetails = new Properties();
//            subDetails.setProperty("defendant", "Fred");
//
//            Properties details = new Properties();
//            details.setProperty("courtDate", "12/12/02");
//            details.setProperty("courtCode", "0433");
//            details.put("defendants", subDetails);
//            // Add 2 to vector...
//            v.add(details);
//            v.add(details);
//
//            Properties props = new Properties();
//            props.setProperty("eventType", "10100");
//            props.put("collection", v);
//
//            String expected = "<?xml version=\"1.0\" encoding=\""
//                    + ENCODING
//                    + "\"?>\r\n"
//                    + "<printingValueObject><eventType>10100</eventType>"
//                    + "<collection><courtDate>12/12/02</courtDate><defendants><defendant>Fred</defendant></defendants><courtCode>0433</courtCode></collection>"
//                    + "<collection><courtDate>12/12/02</courtDate><defendants><defendant>Fred</defendant></defendants><courtCode>0433</courtCode></collection>"
//                    + "</printingValueObject>";
//
//            XMLServicesImpl xml = XMLServicesImpl.getInstance(); // new
//            // XMLServicesImpl();
//            String xmlString = xml.generateXMLFromPropSet(props, "printingValueObject");
//            log.debug("expected == " + expected);
//            log.debug("actual   == " + xmlString);
//
//            assertEquals(expected, xmlString);
//        } catch (Exception cse) {
//            System.out.println("in Catch block of generate xml");
//            log.debug("Exception = " + cse);
//            fail();
//        }
//    }
//
//    public void testgenerateEmptyXMLFromPropSet() {
//        log.debug("## [testgenerateEmptyXMLFromPropSet]");
//        try {
//            Properties props = new Properties();
//            props.setProperty("courtDate", "");
//            props.setProperty("courtCode", "");
//            props.setProperty("transferDate", "12/12/02");
//            String expected = "<?xml version=\"1.0\" encoding=\""
//                    + ENCODING
//                    + "\"?>\r\n"
//                    + "<printingValueObject><courtDate/><courtCode/><transferDate>12/12/02</transferDate></printingValueObject>";
//
//            XMLServices xml = XMLServicesImpl.getInstance(); // new
//            // XMLServicesImpl();
//            String actual = xml.generateXMLFromPropSet(props, "printingValueObject");
//            log.debug("expected == " + expected);
//            log.debug("actual   == " + actual);
//
//            assertEquals(expected, actual);
//        } catch (Exception cse) {
//            System.out.println("in Catch block of generate xml");
//            log.debug("Exception = " + cse);
//        }
//    }
//
//    /*
//     * Method no longer exists as replaced by XSLServices public void
//     * testTransformXML() { log.debug("## [testTransformXML]");
//     *
//     * XMLServices xml = XMLServicesImpl.getInstance(); //new XMLServicesImpl();
//     *
//     * String expectedTransform ="<html>\r\n<body>\r\n<table>\r\n<tr>\r\n<td><b>Case
//     * Called On</b>\r\n<br>Special chars &amp; &lt; &gt; </td>\r\n</tr>\r\n</table>\r\n</body>\r\n</html>\r\n";
//     *
//     * String sourceXML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><event><free_text>" +
//     * "Special chars &amp; &lt; &gt; </free_text><type>10100</type>" + "</event>";
//     *
//     * String styleSheet = "/config/courtlog/transformer/client/10100.xsl";
//     *
//     * String xmlString = xml.transformXML(sourceXML,styleSheet, this);
//     *
//     * log.debug("Transformed XML = " + xmlString); log.debug("ExpectedTransform = " +
//     * expectedTransform);
//     *
//     * assertTrue(xmlString.equalsIgnoreCase(expectedTransform)); }
//     */
//    /**
//     * Method resolves entity lookup when transforming an XML document
//     *
//     * @param href
//     * @param base
//     * @return
//     * @throws TransformerException
//     */
    public Source resolve(java.lang.String href, java.lang.String base) {
        String methodName = "resolve() - ";
        log.debug(methodName + "entry - href : " + href + ", base : " + base);

        String reqResource = "/config/courtlog/transformer/client/" + href;
        XMLServices xml = XMLServicesImpl.getInstance(); // new
        // XMLServicesImpl();
        StreamSource resolverSource = new StreamSource(xml.loadDocument(reqResource));
        return resolverSource;

    }
    /**
     * Tests that testDecodeXML 
     * Prints out the hard-coded string of what the output should be
     * so output after the call to testDecodeXML can be
     * checked easily.  
     * @param xmlString XML to be modified
     * @return String
     */    
    public void testDecodeXML() {
    System.out.println("## [testDecodeXML]");
    try {
        // Tests the decode (replacement) of lower and upper case versions of codes below:
        // &lt; should change to <  
        // &gt; should change to >
        // &quot; should change to " 
        // &apos; should change to '
        // note may not have to encode/decode " and ' but test in case we do.
        XMLServices xml = XMLServicesImpl.getInstance(); // new
        // note the backslashes(\) in this xmlstring have been escaped ie. now we have \\
        
        // print decoded string (this is what the output from decodeXML should look like)
        String xmlString = "<Exception xmlns=\"http://schemas.cjse.gov.uk/messages/exception/2006-06\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\\" SchemaVersion=\"1.0\"><Name>String</Name'Colette'><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData><InnerException SchemaVersion=\"1.0\"><Name>String</Name><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData></InnerException></Exception>";      
        System.out.println("decoded string should look like this    " + xmlString);
              
        // test decoding lower case codes i.e. &lt; &gt; &quot; &apos;
        xmlString = "&lt;Exception xmlns=&quot;http://schemas.cjse.gov.uk/messages/exception/2006-06&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xsi:schemaLocation=&quot;http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\&quot; SchemaVersion=&quot;1.0&quot;&gt;&lt;Name&gt;String&lt;/Name&apos;Colette&apos;&gt;&lt;Code&gt;String&lt;/Code&gt;&lt;Detail&gt;String&lt;/Detail&gt;&lt;RelatesTo/&gt;&lt;OriginalExceptionData&gt;String&lt;/OriginalExceptionData&gt;&lt;InnerException SchemaVersion=&quot;1.0&quot;&gt;&lt;Name&gt;String&lt;/Name&gt;&lt;Code&gt;String&lt;/Code&gt;&lt;Detail&gt;String&lt;/Detail&gt;&lt;RelatesTo/&gt;&lt;OriginalExceptionData&gt;String&lt;/OriginalExceptionData&gt;&lt;/InnerException&gt;&lt;/Exception&gt;";         
        xmlString = xml.decodeXML(xmlString);
        System.out.println("decode of lowercase xml string produces " + xmlString);
          
        // test decoding upper case codes i.e. &LT; &GT; &QUOT; &APOS;
        xmlString = "&LT;Exception xmlns=&QUOT;http://schemas.cjse.gov.uk/messages/exception/2006-06&QUOT; xmlns:xsi=&QUOT;http://www.w3.org/2001/XMLSchema-instance&QUOT; xsi:schemaLocation=&QUOT;http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\&QUOT; SchemaVersion=&QUOT;1.0&QUOT;&GT;&LT;Name&GT;String&LT;/Name&APOS;Colette&APOS;&GT;&LT;Code&GT;String&LT;/Code&GT;&LT;Detail&GT;String&LT;/Detail&GT;&LT;RelatesTo/&GT;&LT;OriginalExceptionData&GT;String&LT;/OriginalExceptionData&GT;&LT;InnerException SchemaVersion=&QUOT;1.0&QUOT;&GT;&LT;Name&GT;String&LT;/Name&GT;&LT;Code&GT;String&LT;/Code&GT;&LT;Detail&GT;String&LT;/Detail&GT;&LT;RelatesTo/&GT;&LT;OriginalExceptionData&GT;String&LT;/OriginalExceptionData&GT;&LT;/InnerException&GT;&LT;/Exception&GT;";      
        xmlString = xml.decodeXML(xmlString);
        System.out.println("decode of lowercase xml string produces " + xmlString);
        
        // test decoding some upper and some lower case codes
        xmlString = "&LT;Exception xmlns=&quot;http://schemas.cjse.gov.uk/messages/exception/2006-06&QUOT; xmlns:xsi=&QUOT;http://www.w3.org/2001/XMLSchema-instance&QUOT; xsi:schemaLocation=&QUOT;http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\&quot; SchemaVersion=&QUOT;1.0&QUOT;&GT;&LT;Name&GT;String&lt;/Name&APOS;Colette&apos;&GT;&LT;Code&GT;String&LT;/Code&GT;&LT;Detail&GT;String&LT;/Detail&GT;&LT;RelatesTo/&gt;&lt;OriginalExceptionData&GT;String&LT;/OriginalExceptionData&gt;&LT;InnerException SchemaVersion=&QUOT;1.0&QUOT;&GT;&LT;Name&GT;String&LT;/Name&GT;&LT;Code&GT;String&LT;/Code&GT;&LT;Detail&GT;String&LT;/Detail&GT;&LT;RelatesTo/&GT;&LT;OriginalExceptionData&GT;String&LT;/OriginalExceptionData&GT;&LT;/InnerException&GT;&LT;/Exception&GT;";      
        xmlString = xml.decodeXML(xmlString);
        System.out.println("decode of bothcases xml string produces " + xmlString);
          
        } catch (Exception cse) {
              System.out.println("in Catch block of testDecodeXML");
        } finally {
              System.out.println("got to 'finally' in testDecodeXML");
        }
    }
      /**
      * Tests that addXMLHeader adds the 'xml version and encoding 
      * header' at the beginning of the <code>xmlString<code>
      * Prints out the hard-coded value of what it should be
      * so output after the call to addXMLHeader can be
      * checked easily.  
      * @param xmlString XML to be modified
      * @return String
      */
      public void testAddXMLHeader() {
      System.out.println("## [testAddXMLHeader]");
      try {
              XMLServices xml = XMLServicesImpl.getInstance(); // new
              // note the backslashes(\) in this xmlstring have been escaped ie. now we have \\
              
              // print output we would expect from this function
              String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><Exception xmlns=\"http://schemas.cjse.gov.uk/messages/exception/2006-06\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\\" SchemaVersion=\"1.0\"><Name>String</Name'Colette'><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData><InnerException SchemaVersion=\"1.0\"><Name>String</Name><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData></InnerException></Exception>";      
              System.out.println("test output should look like this    " + xmlString);
                 
              // create the String without the header
              xmlString = "<Exception xmlns=\"http://schemas.cjse.gov.uk/messages/exception/2006-06\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://schemas.cjse.gov.uk/messages/exception/2006-06S:\\$LP80-Xhibit2\\APPDEV~1\\04_Sys\\Appl_Design_Specs\\V8.1\\XSDSCH~1\\080806\\Exception-v1-0.xsd\\\" SchemaVersion=\"1.0\"><Name>String</Name'Colette'><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData><InnerException SchemaVersion=\"1.0\"><Name>String</Name><Code>String</Code><Detail>String</Detail><RelatesTo/><OriginalExceptionData>String</OriginalExceptionData></InnerException></Exception>";      
              
              xmlString = xml.addXMLHeader(xmlString);
              System.out.println("xmlstring with the header added      " + xmlString);
                   
          } catch (Exception cse) {
              System.out.println("in Catch block of testAddXMLHeader");
          } finally {
              System.out.println("got to 'finally' in testAddXMLHeader");
          }
     }
}