package uk.gov.courtservice.xhibit.client.order.test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.client.order.gui.entry.DataEntryPanel;
import uk.gov.courtservice.xhibit.client.order.gui.entry.OrderComponent;

public class TestDataEntryPanel extends TestCase {

    public TestDataEntryPanel(String s) {
        super(s);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testAddComponent() {
        DataEntryPanel dataentrypanel = new DataEntryPanel();
        OrderComponent oc1 = null /** @todo fill in non-null value */
        ;
        try {
            dataentrypanel.addComponent(oc1);
            /**
             * @todo: Insert test code here. Use assertEquals(), for example.
             */
        } catch (Exception e) {
            System.err.println("Exception thrown:  " + e);
        }
    }

    public void testGetComponent() {
        DataEntryPanel dataentrypanel = new DataEntryPanel();
        String name1 = "STRING0";
        OrderComponent ordercomponentRet = dataentrypanel.getComponent(name1);
        /** @todo: Insert test code here. Use assertEquals(), for example. */
    }
}
