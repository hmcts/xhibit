//package uk.gov.courtservice.business.services.witness.test;
//
//import junit.framework.Assert;
//import org.apache.log4j.Logger;
//import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
//import uk.gov.courtservice.framework.services.CSServices;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//import uk.gov.courtservice.framework.testutils.junit.TransactionTestCase;
//import javax.naming.NamingException;
//
///**
// * <p>Title: </p>
// * <p>Description: .</p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: Electronic Data Systems</p>
// *
// * @author Neil Ellis
// * @version $Revision: 1.8 $
// *
// */
//public class AbstractTestClass extends TransactionTestCase
//{
//    protected static final Logger log = CSServices.getLogger(AbstractTestClass.class);
//    protected static final Integer COURT_ID_FOR_TERMINAL = new Integer(1);
//    protected static final String terminalName;
//    protected static final Integer CASE_ID = new Integer(-1);
//    protected static final Integer NEW_CASE_ID = new Integer(-2);
//    protected static final Integer CASE_ID_TWO = new Integer(-2);
//    protected static final Integer CASE_ID_THREE = new Integer(-3);
//    protected static final Integer DAY = new Integer(1);
//    protected static final Integer WITNESS_ID = new Integer(1);
//    protected static final short WITNESS_SESSION_DAY_NUMBER = (short) 1;
//    protected static final Integer COURT_ID = new Integer(1);
//    protected static final Integer COURT_ID_2 = new Integer(2);
//    protected static final Integer COURT_ID_3 = new Integer(3);
//    protected static final Integer SCHEDULED_HEARING_ID = new Integer(1);
//
//
//    public AbstractTestClass(String s) throws NamingException
//    {
//        super(s, true);
//    }
//
//    public void testInitialise()
//    { assertNotNull(log);
//
//    }
//
//
//    public void setUp() throws Exception
//    {
//        super.setUp();
////        WitnessTestHelper.initializeTerminalAndDate(terminalName, connection);
//        WitnessTestHelper.setUpWitnessData(null,
//                                           CASE_ID.intValue(),
//                                           CASE_ID_TWO.intValue(),
//                                           CASE_ID_THREE.intValue(),
//                                           SCHEDULED_HEARING_ID.intValue(),
//                                           connection);
//      }
//
//
//    public void handleException(Exception e)
//    {
//        e.printStackTrace();
//        log.fatal(e);
//        Assert.fail(e.getMessage());
//    }
//
//
//    static
//    {
//        try
//        {
//            terminalName = InetAddress.getLocalHost().getHostName();
//        }
//        catch (UnknownHostException e)
//        {
//
//            throw new CSUnrecoverableException(e);
//        }
//    }
//
//}
//