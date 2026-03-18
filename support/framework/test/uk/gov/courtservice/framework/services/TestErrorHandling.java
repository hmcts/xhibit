//package uk.gov.courtservice.framework.services;
//
//import java.sql.SQLException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//import uk.gov.courtservice.framework.exception.CSBusinessException;
//import uk.gov.courtservice.framework.exception.CSRecoverableException;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.services.errorhandling.DefaultErrorHandler;
//import uk.gov.courtservice.framework.services.errorhandling.ErrorHandlerTypes;
//
//public class TestErrorHandling extends TestCase {
//
//    private static Logger log = Logger.getLogger(TestErrorHandling.class.getName());
//
//    public TestErrorHandling(String s) {
//        super(s);
//    }
//
//    // run before each test
//    protected void setUp() {
//    }
//
//    // run after each test
//    protected void tearDown() throws Exception {
//    }
//
//    public void testDefaultErrorHandler() {
//        log.debug("[testDefaultErrorHandler]");
//        String msg = "Test message from testDefaultErrorHandler";
//        try {
//            throw new CSRecoverableException();
//        } catch (CSRecoverableException e) {
//            CSServices.getDefaultErrorHandler().handleError(e, TestErrorHandling.class, msg);
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//    public void testSpecificErrorHandler() {
//        log.debug("[testSpecificErrorHandler]");
//        String msg = "Test message from testSpecificErrorHandler";
//        try {
//            throw new CSRecoverableException();
//        } catch (CSRecoverableException e) {
//            CSServices.getErrorHandler(ErrorHandlerTypes.DEFAULT).handleError(e, TestErrorHandling.class, msg);
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//    public void testCSRecoverableExceptionHandling() {
//        log.debug("[testCSRecoverableExceptionHandling]");
//        ErrorHandler h = CSServices.getDefaultErrorHandler();
//
//        try {
//            try {
//                throw new CSRecoverableException();
//            } catch (CSRecoverableException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 1");
//                throw e;
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSRecoverableException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 1 secondtime: SHOULD NOT PRINT");
//            // could test this but would require errorhandler to return
//            // boolean:
//            // don't consider this test important enough at this stage to
//            // make such a change
//            // fail( "error already logged " + e2 );
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//
//        try {
//            try {
//                throw new CSRecoverableException();
//            } catch (CSRecoverableException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 2");
//                throw new CSRecoverableException();
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSRecoverableException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 2 secondtime: SHOULD print OK");
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//    public void testCSBusinessExceptionHandling() {
//        log.debug("[testCSBusinessExceptionHandling]");
//        ErrorHandler h = CSServices.getDefaultErrorHandler();
//
//        try {
//            try {
//                throw new CSBusinessException();
//            } catch (CSBusinessException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 1");
//                throw e;
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSBusinessException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 1 secondtime: SHOULD NOT PRINT");
//            // could test this but would require errorhandler to return
//            // boolean:
//            // don't consider this test important enough at this stage to
//            // make such a change
//            // fail( "error already logged " + e2 );
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//
//        try {
//            try {
//                throw new CSBusinessException();
//            } catch (CSBusinessException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 2");
//                throw new CSBusinessException();
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSBusinessException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 2 secondtime: SHOULD print OK");
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//    public void testCSUnrecoverableExceptionHandling() {
//        log.debug("[testCSUnrecoverableExceptionHandling]");
//        ErrorHandler h = CSServices.getDefaultErrorHandler();
//
//        try {
//            try {
//                throw new CSUnrecoverableException();
//            } catch (CSUnrecoverableException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 1");
//                throw e;
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSUnrecoverableException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 1 secondtime: SHOULD NOT PRINT");
//            // could test this but would require errorhandler to return
//            // boolean:
//            // don't consider this test important enough at this stage to
//            // make such a change
//            // fail( "error already logged " + e2 );
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//
//        try {
//            try {
//                throw new CSUnrecoverableException();
//            } catch (CSUnrecoverableException e) {
//                h.handleError(e, DefaultErrorHandler.class, "test 2");
//                throw new CSUnrecoverableException();
//            } catch (Exception e) {
//                fail("wrong exception: " + e.getClass().toString());
//            }
//        } catch (CSUnrecoverableException e2) {
//            h.handleError(e2, DefaultErrorHandler.class, "test 2 secondtime: SHOULD print OK");
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//
//            throw new CSUnrecoverableException("This exception need not be caught");
//        }
//    }
//
//    public void testExceptionChaining() {
//        try {
//            try {
//                throw new SQLException("Nothing found here!");
//            } catch (SQLException e) {
//                ErrorHandler h = CSServices.getDefaultErrorHandler();
//                CSUnrecoverableException e2 = new CSUnrecoverableException("Wrapped SQLException", e);
//                CSRecoverableException e3 = new CSRecoverableException("test", "Wrapped CSUnrecoverableException", e2);
//                h.handleError(e3, TestErrorHandling.class, "There should be THREE exception traces printed out here");
//            }
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//    public void testUserErrorMessages() {
//        log.debug("[testUserErrorMessages]");
//        try {
//            throw new CSRecoverableException("test", "test error message");
//        } catch (CSRecoverableException e) {
//            ErrorHandler h = CSServices.getDefaultErrorHandler();
//            String msg = h.handleError(e, TestErrorHandling.class, "testUserErrorMessages");
//
//            // message returned by error handler will always be null, since
//            // the code :
//            // do
//            // { t = logCause(t, log);
//            // } while (t != null);
//            // .. always leaves 't' as null
//            // i'm not convinced the logic in handleError is correct so ...
//            assertNotNull("message returned by error handler was null", msg);
//        } catch (Exception e) {
//            fail("wrong exception: " + e.getClass().toString());
//        }
//    }
//
//}