package uk.gov.courtservice.xhibit.client.order.test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponentFactory;

public class TestOrderComponentFactory extends TestCase {

    public TestOrderComponentFactory(String s) {
        super(s);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testCreateComponent() {
        String type1 = "STRING0";
        try {
            OrderComponent ordercomponentRet = OrderComponentFactory.createComponent(type1);
            /**
             * @todo: Insert test code here. Use assertEquals(), for example.
             */
        } catch (Exception e) {
            System.err.println("Exception thrown:  " + e);
        }
    }
}
