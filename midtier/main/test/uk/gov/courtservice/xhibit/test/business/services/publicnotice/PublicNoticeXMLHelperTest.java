package uk.gov.courtservice.xhibit.test.business.services.publicnotice;

import java.util.HashMap;


import junit.framework.TestCase;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeXmlHelper;



/**
 * <p>Title: Tests the XMLHelper </p>
 * <p>Description: see title</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: Electronic Data Systems</p>
 * @author Pat Fox
 *
 */
public class PublicNoticeXMLHelperTest extends TestCase {
    /**
   * PublicNoticeXmlHelper m_publicNoticeXMLHelper
   */
  PublicNoticeXmlHelper m_publicNoticeXMLHelper = null;
    /**
   * Logger log
   */
  Logger log = CSServices.getLogger(PublicNoticeXmlHelper.class);



    /**
   * Constructor for the PublicNoticeControllerWorkFlowTest object
   * @param s Description of the Parameter
   */
    public PublicNoticeXMLHelperTest(String s) {
        super(s);
    }


    /**
   * The JUnit setup method
   * @throws Exception Description of the Exception
   */
    protected void setUp() throws Exception {



    }


    /**
   * The teardown method for JUnit
   * @throws Exception Description of the Exception
   */
    protected void tearDown() throws Exception {


    }


    /**
   * Basically cheks to Make sure the XML file is loaded into the Map correctly
   * @throws Exception Description of the Exception
   */
    public void testGetManipulaterMap() throws Exception {


        log.debug("Test Method testGetManipulaterMap() ");

	m_publicNoticeXMLHelper = PublicNoticeXmlHelper.getInstance();

        HashMap manipulatorMap =
                m_publicNoticeXMLHelper.getManipulatorMap();

        // check the number returned
        assertEquals(7, manipulatorMap.size());
        log.debug("size of the Maps are :" + manipulatorMap.size() );
        // check the Map
        assertEquals(true, manipulatorMap.containsKey(new Integer(21200)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(21201)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(20911)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(2090301)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(2090302)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(2060201)));
        assertEquals(true, manipulatorMap.containsKey(new Integer(2060202)));


    }



}
