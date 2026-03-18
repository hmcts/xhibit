package uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper;

// java
import java.io.IOException;
import java.io.StringReader;

// j2ee
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

// third party
import org.apache.log4j.Logger;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import junit.framework.TestCase;

// framework
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

// xhibit
import uk.gov.courtservice.xhibit.courtlog.cjse.CjseEventDetail;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapper;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapperHandler;
import uk.gov.courtservice.xhibit.courtlog.cjse.eventmapper.CjseEventMapperHandlerFactory;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogSubscriptionValue;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;


/**
 * <p>Title: Unit test class that tests the CjseEventMapperHandlerFactory,
 * the CjseEventMapperHandlers and the CjseEventMappers.</p>
 * <p>Description:</p>
 * <p>
 * This class provides a set of tests that should fully work out the contents
 * of the eventmapper package.
 * </p>
 * <p>
 * Note: the unit test is written in such a way that it takes advantage
 * of the abstraction of the framework, so if the implementing classes are
 * ever changed, the unit test should not read a rewrite.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Eds</p>
 * @author Bob Boothby
 * @version 1.0
 */
public class TestCjseEventMapperHandlers extends TestCase
{
    private static final Logger log =
                           CSServices.getLogger(TestCjseEventMapperHandlers.class);

    // Basic test constants defined.
    public static final Integer CJSE_ID_1 = new Integer(11112);
    public static final Integer CJSE_ID_2 = new Integer(11113);
    public static final String CJSE_MESSAGECODE_1 = "ABCDEF";
    public static final String CJSE_MESSAGECODE_2 = "GHIJKL";
    public static final String ONETOMANY_XPATH = "XXX/YYY/ZZZ/text()";
    public static final String ONETOMANY_FIELDVALUE_1 = "ABC";
    public static final String ONETOMANY_FIELDVALUE_2 = "DEF";
    public static final String ONETOMANY_STRING =
            "<TEST>\n" +
            "<DIFFERENTIATING_FIELD xpath=\""+ ONETOMANY_XPATH +"\"/>\n" +
            "<EVENT fieldvalue=\"" + ONETOMANY_FIELDVALUE_1 + "\" cjseid=\"" +
            CJSE_ID_1 + "\" cjsemessagecode=\"" + CJSE_MESSAGECODE_1 + "\"/>" +
            "<EVENT fieldvalue=\"" + ONETOMANY_FIELDVALUE_2 + "\" cjseid=\"" +
            CJSE_ID_2 + "\" cjsemessagecode=\"" + CJSE_MESSAGECODE_2 + "\"/>" +
            "</TEST>";
    public static final String ONETOONE_STRING =
            "<TEST>\n" +
            "<EVENT cjseid=\"" + CJSE_ID_1 + "\" cjsemessagecode=\"" +
            CJSE_MESSAGECODE_1 + "\"/>" +
            "</TEST>";
    public static final String ONETOMANY_EVENT_XML_1 =
            "<XXX><YYY><ZZZ>"+ONETOMANY_FIELDVALUE_1+"</ZZZ></YYY></XXX>";
    public static final String ONETOMANY_EVENT_XML_2 =
            "<XXX><YYY><ZZZ>"+ONETOMANY_FIELDVALUE_2+"</ZZZ></YYY></XXX>";
    public static final String ONETOMANY_EVENT_XML_3 =
            "<XXX><YYY><ZZZ>GIBBERISH</ZZZ></YYY></XXX>";

    //Instance level variables, configured by the setUp() method.
    protected SAXParser parser;
    protected CourtLogSubscriptionValue eventObject = new CourtLogSubscriptionValue();
    protected CourtLogViewValue courtLogView = new CourtLogViewValue();

    /**
     * Public constructor for this test case.
     * @param name The name ascribed to this test.
     */
    public TestCjseEventMapperHandlers(String name)
    {
        super(name);
    }

    /**
     * Sets up the necessary SAX parsers and makes minor adjustment to value objects.
     * @throws java.lang.Exception When there is a problem with the SAX configuration.
     */
    protected void setUp() throws java.lang.Exception
    {
        super.setUp();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);
        parser = factory.newSAXParser();
        eventObject.setCourtLogViewValue(courtLogView);
    }

    /**
     * Tests the One to Many mapping functionality.
     * @throws Exception When there is a problem.
     */
    public void testOneToManyCjseEventMapperHandler() throws Exception
    {
        String methodName="testOneToManyCjseEventMapperHandler - ";
        log.debug(methodName + "entry");

        //Get the mapper.
        CjseEventMapper configuredMapper = getMapper("ONETOMANY", ONETOMANY_STRING);

        //Check the behaviour
        courtLogView.setLogEntry(ONETOMANY_EVENT_XML_1);
        CjseEventDetail eventDetail = configuredMapper.getCjseEventDetail(eventObject);
        checkEventDetail(eventDetail, CJSE_ID_1, CJSE_MESSAGECODE_1);

        courtLogView.setLogEntry(ONETOMANY_EVENT_XML_2);
        eventDetail = configuredMapper.getCjseEventDetail(eventObject);
        checkEventDetail(eventDetail, CJSE_ID_2, CJSE_MESSAGECODE_2);

        //What behaviour are we going to get here in the event of a failure?
        try
        {
            courtLogView.setLogEntry(ONETOMANY_EVENT_XML_3);
            eventDetail = configuredMapper.getCjseEventDetail(eventObject);
            assertNull(
                  "There is no CJSE event for this value, should return null",
                  eventDetail);
        }
        catch(CSUnrecoverableException e)
        {
            fail("Shouldn't throw an exception here anymore, should return null");
        }
    }

    /**
     * Tests the One to One mapping functionality.
     * @throws Exception When there is a problem.
     */
    public void testOneToOneCjseEventMapperHandler() throws Exception
    {
        String methodName="testOneToOneCjseEventMapperHandler - ";
        log.debug(methodName + "entry");

        //Get the mapper.
        CjseEventMapper configuredMapper = getMapper("ONETOONE", ONETOONE_STRING);

        //Check the behaviour
        CjseEventDetail eventDetail = configuredMapper.getCjseEventDetail(eventObject);
        checkEventDetail(eventDetail, CJSE_ID_1, CJSE_MESSAGECODE_1);
    }

    /**
     * Gets the appropriate mapper using the factory and handler classes.
     * @param mapperType The String 'name' for the mapper.
     * @return a concrete implementation of the mapper.
     * @throws IOException Should never be thrown as using strings, however is
     * a requirement of the Reader interface.
     * @throws SAXException When there is a problem reading the XML.
     */
    private CjseEventMapper getMapper(String mapperType, String typeXML) throws IOException, SAXException
    {
        CjseEventMapper configuredMapper;
        CjseEventMapperHandlerFactory factory =
                CjseEventMapperHandlerFactory.getInstance();
        CjseEventMapperHandler mapperHandler =
                factory.getCjseEventMapperHandler(mapperType);

        parser.parse(
                new InputSource(new StringReader(typeXML)),
                new ATestSwitchingHandler(mapperHandler));

        configuredMapper = mapperHandler.getMapper();
        return configuredMapper;
    }

    /**
     * This method provide basic checking for the event detail class,
     * while we could use the equals() method directly, it is not very
     * 'verbose' in reporting what the differences are.
     * @param eventDetail The eventDetail to check.
     * @param expectedCjseId The expected CjseId.
     * @param expectedCjseMessageCode The expected CjseMessageCode.
     */
    private void checkEventDetail(CjseEventDetail eventDetail,
                                  Integer expectedCjseId,
                                  String expectedCjseMessageCode)
    {
        String methodName="checkEventDetail - ";
        log.debug(methodName + "entry :: expectedCjseId= " + expectedCjseId +
                  ", expectedCjseMessageCode= " + expectedCjseMessageCode +
                  ", eventDetail= " + eventDetail );

        log.debug(methodName + "Checking correct CJSE event id");
        assertEquals("Expected CJSE event id: " + expectedCjseId +
                     " got: " + eventDetail.getCjseId(),
                     eventDetail.getCjseId(),
                     expectedCjseId.toString());

        log.debug(methodName + "Checking correct message code");
        assertEquals("Expected messageCode: " + expectedCjseMessageCode +
                     " got: " + eventDetail.getCjseMessageCode(),
                     eventDetail.getCjseMessageCode(),
                     expectedCjseMessageCode);
    }

    /**
     * <p>Title: Lightweight switchin handler to provide for unit testing.</p>
     * <p>Description:</p>
     * <p>
     * This class is used to provide a simple switching handler for use when
     * testing the CjseEventMapperHandlers.
     * </p>
     * <p>Copyright: Copyright (c) 2003</p>
     * <p>Company: Eds</p>
     * @author Bob Boothby
     * @version 1.0
     */
    private class ATestSwitchingHandler extends DefaultHandler
    {
        // The CjseEventMapperHandler that this test class
        // is wrapping/switching.
        private CjseEventMapperHandler _handler;

        /**
         * Simple constructor that takes the CjseEventMapperHandler that it will
         * switch to.
         * @param handler The CjseEventMapperHandler.
         */
        private ATestSwitchingHandler(CjseEventMapperHandler handler)
        {
            _handler = handler;
        }

        /**
         * Set the document locator instance so the handler can report effectively.
         * @param locator the locator for the CjseEventMapperHandler to use.
         */
        public void setDocumentLocator(Locator locator)
        {
            _handler.setDocumentLocator(locator);
        }

        /**
         * Will receive all 'start element' calls and will defer the non-root
         * element (<TEST>) calls to the wrapped CjseEventMapperHandler.
         * @param namespaceURI The namespace URI of the element.
         * @param localName The local name of the element.
         * @param qName The qualified name of the element.
         * @param atts The attributes of the element.
         * @throws SAXException If thrown by the CjseEventMapperHandler
         */
        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts) throws SAXException
        {
            if(!localName.equalsIgnoreCase("TEST"))
            {
                _handler.startElement(namespaceURI,localName,qName,atts);
            }
        }
    }
}