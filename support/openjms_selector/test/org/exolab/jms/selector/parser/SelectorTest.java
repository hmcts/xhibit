package org.exolab.jms.selector.parser;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.exolab.jms.selector.Selector;

public class SelectorTest extends TestCase {

    //
    // The Framework!
    //

    /**
     * Execution entry point. Allows the test to be run in stand alone mode.
     * 
     * @param args
     *            String array of command line arguments
     */
    public static void main(String args[]) {
        TestRunner.run(suite());
    }

    /**
     * Create a Test useing reflection to determine tests
     */
    public static Test suite() {
        return new TestSuite(SelectorTest.class);
    }

    Message dailyListMessage;

    Message warnedListMessage;

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        dailyListMessage = createMockMessage(new Object[] { "MessageType", "ExISS", "MessageSubType", "DL", "temp",
                "true" });

        warnedListMessage = createMockMessage(new Object[] { "MessageType", "ExISS", "MessageSubType", "WL" });

    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        dailyListMessage = null;
        warnedListMessage = null;
    }

    //
    // The Tests!
    //	  

    public void testSelector() throws Exception {
        Selector selector = new Selector("MessageType = 'ExISS'");
        assertTrue(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorExcludeDailyList() throws Exception {
        Selector selector = new Selector("MessageType = 'ExISS' and (not MessageSubType = 'DL')");
        assertFalse(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorIn() throws Exception {
        Selector selector = new Selector("MessageType = 'ExISS' and MessageSubType in ('DL', 'WL')");
        assertTrue(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorLike() throws Exception {
        Selector selector = new Selector("MessageType Like 'E_I%'");
        assertTrue(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorEmtpy() throws Exception {
        Selector selector = new Selector("");
        assertTrue(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorTrue() throws Exception {
        Selector selector = new Selector("true");
        assertTrue(selector.selects(dailyListMessage));
        assertTrue(selector.selects(warnedListMessage));
    }

    public void testSelectorFalse() throws Exception {
        Selector selector = new Selector("false");
        assertFalse(selector.selects(dailyListMessage));
        assertFalse(selector.selects(warnedListMessage));
    }
        
    //
    // The Utils
    //	

    private static Message createMockMessage(Object[] headerNameValuePairs) {
        return createMockMessage(createMap(headerNameValuePairs));
    }

    private static Message createMockMessage(final Map propertyMap) {
        return (Message) Proxy.newProxyInstance(SelectorTest.class.getClassLoader(), new Class[] { Message.class },
                new InvocationHandler() {
                    final Method getObjectPropertyMethod = getMethod(Message.class, "getObjectProperty",
                            new Class[] { String.class });

                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (getObjectPropertyMethod.equals(method)) {
                            return propertyMap.get(0 < args.length ? args[0] : null);
                        }
                        throw new UnsupportedOperationException();
                    }

                });
    }

    private static Map createMap(Object[] nameValuePairs) {
        Map map = new HashMap();
        if (nameValuePairs != null) {
            for (int i = 1; i < nameValuePairs.length; i += 2) {
                map.put(nameValuePairs[i - 1], nameValuePairs[i]);
            }
        }
        return map;
    }

    private static Method getMethod(Class clazz, String name, Class[] argTypes) {
        try {
            return clazz.getMethod(name, argTypes);
        } catch (Exception e) {
            throw new IllegalStateException("Could not find method.", e);
        }
    }
}
