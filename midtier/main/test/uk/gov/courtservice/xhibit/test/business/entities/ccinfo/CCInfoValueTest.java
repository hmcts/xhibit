package uk.gov.courtservice.xhibit.test.business.entities.ccinfo;

import junit.framework.*;
import org.apache.log4j.Logger;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CCInfoBasicValue;

public class CCInfoValueTest extends TestCase
{
    private Logger log =  CSServices.getLogger(CCInfoValueTest.class);

    public CCInfoValueTest(String s)
    {
        super(s);
    }

    protected void setUp()
    {
    }

    protected void tearDown()
    {
    }

    public void testBasic()
    {
        try
        {
            Integer ccInfoID = new Integer(1);
            String ccInfoText = "ccInfoText";
            Integer version = new Integer(1);

            CCInfoBasicValue basic = new CCInfoBasicValue(ccInfoID, version);

            basic.setCcInfoText(ccInfoText);

            log.debug("ccInfoText");
            assertEquals(ccInfoText, basic.getCcInfoText());
            log.debug("ccInfoID");
            assertEquals(ccInfoID, basic.getId());
            log.debug("version");
            assertEquals(version, basic.getVersion());
        }
        catch(Exception e)
        {
            log.debug("testBasic() is failed");
            e.printStackTrace();
            fail();
        }
    }
}