package uk.gov.courtservice.xhibit.common.publicdisplay.events.test;

import junit.framework.TestCase;
import uk.gov.courtservice.xhibit.common.publicdisplay.events.types.EventType;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.3 $
 */
public class TestEventType extends TestCase {
    /**
     * Creates a new TestEventType object.
     * 
     * @param s
     *            TODO:
     */
    public TestEventType(String s) {
        super(s);
    }

    /**
     * TODO:
     */
    public void testDelegatedMethods() {
        EventType e1 = EventType.getEventType(EventType.PUBLIC_NOTICE_EVENT);
        EventType e2 = EventType.getEventType(EventType.PUBLIC_NOTICE_EVENT);
        assertTrue(e1.equals(e2));
        assertEquals("Events are not same", e1, e2);
        assertEquals("Hashcode not same", e1.hashCode(), e2.hashCode());
    }

    /**
     * TODO:
     */
    public void testGetEventType() {
        EventType e = EventType.getEventType(EventType.ADD_CASE_EVENT);
        assertNotNull("no event type returned", e);
        assertEquals("Wrong event returned", "AddCaseEvent", e.toString());
    }
}
