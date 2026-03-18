//package uk.gov.courtservice.framework.exception;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
///**
// * <p>
// * Title:
// * </p>
// * <p>
// * Description:
// * </p>
// * <p>
// * Copyright: Copyright (c) 2002
// * </p>
// * <p>
// * Company: EDS
// * </p>
// *
// * @author Pete Raymond
// * @version 1.0
// */
//public class ExceptionTest extends TestCase {
//
//    private static Logger log = Logger.getLogger(ExceptionTest.class.getName());
//
//    public ExceptionTest(String name) {
//        super(name);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testMessageFacility() {
//        log.debug("[testMessageFacility]");
//        Message m = new Message("test");
//        assertEquals("test message", m.getMessage());
//
//        m = new Message("test2", new String[] { "1", "2" });
//        assertEquals("first parameter is the number one: 1 and second paramater is the number two 2", m.getMessage());
//
//    }
//
//    public void testMessageParameters() {
//        log.debug("[testMessageParameters]");
//        Message m = new Message("test");
//        assertEquals("test message", m.getMessage());
//        Object[] tmpParams = m.getParameters();
//
//        assertEquals(0, tmpParams.length);
//
//        m = new Message("test2", new Integer[] { new Integer(1), new Integer(2) });
//        tmpParams = m.getParameters();
//
//        assertEquals(2, tmpParams.length);
//        assertEquals(new Integer(1), tmpParams[0]);
//        assertEquals(new Integer(2), tmpParams[1]);
//
//    }
//
//    public void testExceptionMessageConstructor() {
//        log.debug("[testExceptionMessageConstructor]");
//        CSRecoverableException e = new CSRecoverableException("test", "test error message");
//        assertEquals(true, e.getUserMessage().endsWith("test message"));
//    }
//
//    // what is error number ??
//    // is this actually trying to test errorId?
//    // since errorId is generated from the currentTimeMills, it's a bit
//    // tricky
//    // to test for equality - for now just tests that 2 exceptions have
//    // unique
//    // ids - see below
//    /*
//     * public void testErrorNumber() {
//     *
//     * log.debug("[testErrorNumber]"); CSRecoverableException csRecEx = new
//     * CSRecoverableException(); String csreMsg = csRecEx.getMessage();
//     * assertNull("got non-null message from CSRecoverableException", csreMsg);
//     *
//     * csRecEx = new CSRecoverableException("test", "a test log message");
//     * String idNum2 = csRecEx.getMessage(); assertNotNull("got null message
//     * from CSRecoverableException"); assertTrue("got unexpected user message",
//     * csRecEx.getUserMessage().startsWith("test"));
//     *
//     * assertTrue(!csreMsg.equals(idNum2)); log.debug(csreMsg + " " + idNum2);
//     *
//     * CSUnrecoverableException csUnrecEx = new CSUnrecoverableException();
//     * csreMsg = csUnrecEx.getMessage(); assertNotNull("got null message from
//     * CSUnrecoverableException", csreMsg);
//     *
//     * csUnrecEx = new CSUnrecoverableException(""); idNum2 =
//     * csUnrecEx.getMessage(); log.debug("idNum2 = " + idNum2);
//     * assertEquals(csUnrecEx.getErrorID(), idNum2);
//     *
//     * assertTrue(!csreMsg.equals(idNum2)); }
//     */
//
//    // check that 2 otherwise identical error messages have unique ID's
//    public void testErrorId() {
//        log.debug("[testErrorId]");
//        // recoverable exception
//        CSRecoverableException csRecEx1 = new CSRecoverableException("test", "test exception message");
//        CSRecoverableException csRecEx2 = new CSRecoverableException("test", "test exception message");
//        assertTrue("uniqie id's are identical", !csRecEx1.getErrorID().equals(csRecEx2.getErrorID()));
//        // unrecoverable exception
//        CSUnrecoverableException csUnrecEx1 = new CSUnrecoverableException(new Message("test"),
//                "test exception message");
//        CSUnrecoverableException csUnrecEx2 = new CSUnrecoverableException(new Message("test"),
//                "test exception message");
//        assertTrue("uniqie id's are identical", !csUnrecEx1.getErrorID().equals(csUnrecEx2.getErrorID()));
//    }
//
//}