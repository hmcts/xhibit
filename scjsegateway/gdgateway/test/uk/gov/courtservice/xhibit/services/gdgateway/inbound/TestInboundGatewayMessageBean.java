package uk.gov.courtservice.xhibit.services.gdgateway.inbound;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

public class TestInboundGatewayMessageBean extends TestCase {
   
private InboundGatewayMessageBean messageBean;  
private static final Logger log = CSServices.getLogger(InboundGatewayMessageBean.class);
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        //
    }
    
    // Setup Log4j configuration
    static {
        BasicConfigurator.configure();
    }
    public static Test suite() {
        return new TestSuite(TestInboundGatewayMessageBean.class);
    }
    protected void setUp() {
        log.debug("###########TestInboundGatewayMessageBean:setUp##############");
        messageBean = new InboundGatewayMessageBean();
    }
    protected void tearDown() {
        //
    }
    public void testTest(){
        //
    }

}
