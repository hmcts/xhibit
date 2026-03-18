package uk.gov.courtservice.xhibit.web.framework.util;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

public class KeyFactoryTest extends TestCase
{
    // The number of keys to generate for a test
    private static final int KEY_GEN_COUNT = 100;

    public KeyFactoryTest(String testName)
    {
        super(testName);
    }

    public static Test suite()
    {
        return new TestSuite(KeyFactoryTest.class);
    }

    public static void main(String args[])
    {
        TestRunner.run(suite());
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testNextKey()
    {
        Map keys = new HashMap();
        for(int i = 0; i < KEY_GEN_COUNT; i++) {
            String key = KeyFactory.getInstance().nextKey();
            assertNull(keys.get(key));
            keys.put(key, key);
        }
    }
}

