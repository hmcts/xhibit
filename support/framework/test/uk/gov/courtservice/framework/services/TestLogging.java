package uk.gov.courtservice.framework.services;

// Import log4j classes.
import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Pete Raymond
 * @version 1.0
 */

public class TestLogging extends TestCase {

    public TestLogging(String s) {
        super(s);
    }

    protected void setUp() {

    }

    protected void tearDown() {
    }

    public static void testWeblogicLogging() throws Exception {

    }

    public static void testLoggingFromFactory() throws Exception {
        Logger log = CSServices.getLogger(TestLogging.class);
        assertTrue(log != null);
        if (log.isDebugEnabled()) {
            // not an eay way to check this automatcially - an suggestions
            // anyone ? Pete
            log.debug("debug message");
            log.warn("warn message");
            log.info("info message");
        }
    }

}