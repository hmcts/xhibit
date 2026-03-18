package uk.gov.courtservice.xhibit.business.services.messagebroker;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;

import org.apache.log4j.BasicConfigurator;

import uk.gov.courtservice.xhibit.business.vos.messagebroker.QueueVO;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.RuleVO;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.SelectorVO;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class RulesManagerTest extends TestCase {

    //
    // The Framework!
    //

    static {
        // Initialise Log4j For Testing
        BasicConfigurator.configure();
    }

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
        return new TestSuite(RulesManagerTest.class);
    }

    private Message dailyListMessage;

    private Message warnedListMessage;

    private RulesManager rulesManager1;

    private RulesManager rulesManager2;

    /**
     * TestCase implementation.
     * 
     * @see TestCase#setUp() TestCase
     */
    public void setUp() {
        dailyListMessage = createMockMessage(new Object[] { "XHBTarget", "EXISS", "XHBItemType", "DL", "Foo", 1, "bar", true });

        warnedListMessage = createMockMessage(new Object[] { "XHBTarget", "EXISS", "XHBItemType", "WL", "Foo", -1, "bar", true });

        rulesManager1 = new RulesManager(createTestRules1());
        rulesManager2 = new RulesManager(createTestRules2());
    }

    /**
     * TestCase implementation.
     * 
     * @see TestCase#tearDown() TestCase
     */
    public void tearDown() {
        dailyListMessage = null;
        warnedListMessage = null;
        rulesManager1 = null;
        rulesManager2 = null;
    }

    //
    // Tests
    //

    public void testRulesManager1DailyList() throws Exception {
        RuleVO dailyListMatchedRule = rulesManager1.findMatchingRule(dailyListMessage);
        assertNotNull(dailyListMatchedRule);
        assertEquals(new Integer(-1), dailyListMatchedRule.getSelector().getId());
    }

    public void testRulesManager1WarnedList() throws Exception  {
        RuleVO warnedListMatchedRule = rulesManager1.findMatchingRule(warnedListMessage);
        assertNotNull(warnedListMatchedRule);
        assertEquals(new Integer(-3), warnedListMatchedRule.getSelector().getId());
    }

    public void testRulesManager2DailyList() throws Exception {
        RuleVO dailyListMatchedRule = rulesManager2.findMatchingRule(dailyListMessage);
        assertNotNull(dailyListMatchedRule);
        assertEquals(new Integer(-1), dailyListMatchedRule.getSelector().getId());
    }

    public void testRulesManager2WarnedList() throws Exception  {
        RuleVO warnedListMatchedRule = rulesManager2.findMatchingRule(warnedListMessage);
        assertNotNull(warnedListMatchedRule);
        assertEquals(new Integer(-2), warnedListMatchedRule.getSelector().getId());
    }

    //
    // Utilites
    //    

    private static RuleVO[] createTestRules1() {
        RuleVO[] rules = new RuleVO[3];
        rules[0] = new RuleVO(new SelectorVO(-1, "XHBTarget='EXISS' AND XHBItemType='DL'", null, "Y", 0),
                new QueueVO[] { new QueueVO(-1, "test/jms/Queue1", null) });
        rules[1] = new RuleVO(new SelectorVO(-2, "XHBTarget='EXISS' AND XHBItemType='EVENT'", null, "Y", 0),
                new QueueVO[] { new QueueVO(-2, "test/jms/Queue2", null) });
        rules[2] = new RuleVO(new SelectorVO(-3, "XHBTarget='EXISS'", null, "Y", 0), new QueueVO[] { new QueueVO(-3,
                "test/jms/Queue3", null) });
        return rules;
    }

    private static RuleVO[] createTestRules2() {
        RuleVO[] rules = new RuleVO[2];
        rules[0] = new RuleVO(new SelectorVO(-1, "Foo > 0 and bar", null, "Y", 0), new QueueVO[] { new QueueVO(-1,
                "test/jms/Queue1", null) });
        rules[1] = new RuleVO(new SelectorVO(-2, "Foo < 0 and bar", null, "Y", 0), new QueueVO[] { new QueueVO(-2,
                "test/jms/Queue2", null) });
        return rules;
    }

    private static Message createMockMessage(Object[] headerNameValuePairs) {
        return createMockMessage(createMap(headerNameValuePairs));
    }

    private static Message createMockMessage(final Map<Object, Object> propertyMap) {
        return (Message) Proxy.newProxyInstance(RulesManagerTest.class.getClassLoader(), new Class[] { Message.class },
                new InvocationHandler() {
                    final Method getObjectPropertyMethod = getMethod(Message.class, "getObjectProperty",
                            new Class[] { String.class });

                    final Method getPropertyNamesMethod = getMethod(Message.class, "getPropertyNames", null);

                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (getObjectPropertyMethod.equals(method)) {
                            return propertyMap.get(0 < args.length ? args[0] : null);
                        } else if (getPropertyNamesMethod.equals(method)) {
                            return Collections.enumeration(propertyMap.keySet());
                        }
                        throw new UnsupportedOperationException();
                    }

                });
    }

    private static Map<Object, Object> createMap(Object[] nameValuePairs) {
        Map<Object, Object> map = new HashMap<Object, Object>();
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
