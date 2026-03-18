//package uk.gov.courtservice.framework.services;
//
//import java.util.Enumeration;
//
//import javax.jms.JMSException;
//import javax.jms.Message;
//import javax.jms.Queue;
//import javax.jms.QueueBrowser;
//import javax.jms.QueueConnection;
//import javax.jms.QueueConnectionFactory;
//import javax.jms.QueueReceiver;
//import javax.jms.QueueSender;
//import javax.jms.QueueSession;
//import javax.jms.Session;
//import javax.jms.TextMessage;
//import javax.naming.Context;
//import javax.naming.InitialContext;
//import javax.naming.NamingException;
//
//import junit.framework.TestCase;
//
//import org.apache.log4j.Logger;
//
//public class TestLoggingBean extends TestCase {
//
//    private Context jndiContext = null;
//
//    private QueueConnectionFactory queueConnectionFactory = null;
//
//    private QueueConnection queueConnection = null;
//
//    private QueueSession queueSession = null;
//
//    private QueueBrowser queueBrowser = null;
//
//    private Queue queue = null;
//
//    private QueueSender queueSender = null;
//
//    private TextMessage message = null;
//
//    private Logger log = null;
//
//    private final int NUM_MSGS = 1;
//
//    // How long to wait for the messages to get onto the JMS queue.
//    private final long WAIT_TIME = 10000;
//
//    public TestLoggingBean(String s) {
//        super(s);
//
//        // Create a logger...
//        log = CSServices.getLogger(TestLoggingBean.class);
//
//        /*
//         * Create a JNDI API InitialContext object.
//         */
//        jndiContext = CSServices.getServiceLocator().getInitialContext();// TestUtils.getInitialContext();
//
//        /*
//         * Look up connection factory and queue. If either does not exist, exit.
//         */
//        try {
//            jndiContext = new InitialContext();
//            queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("CSJMSConnectionFactory");
//            queue = (Queue) jndiContext.lookup("CSJMSLoggingQueue");
//        } catch (NamingException e) {
//            System.out.println("JNDI API lookup failed: " + e.toString());
//            System.exit(1);
//        }
//    }
//
//    /**
//     * Drains the queue of any messages...
//     *
//     * @throws Exception
//     */
//    protected void setUp() throws Exception {
//        createConnection();
//        drainQueue();
//        closeConnection();
//    }
//
//    /**
//     * Drains the queue of any messages...
//     *
//     * @throws Exception
//     */
//    protected void tearDown() throws Exception {
//        createConnection();
//        drainQueue();
//        int num = getMessageCount();
//        System.out.println(num + " messages on queue");
//        closeConnection();
//    }
//
//    /**
//     * Test that we can create a queue connection.
//     */
//    public void testJMSConnect() {
//        int retValue = 1;
//        try {
//            createConnection();
//        } catch (JMSException e) {
//            retValue = 0;
//            System.out.println("Exception occurred: " + e.toString());
//        } finally {
//            assertEquals(1, retValue);
//        }
//    }
//
//    /**
//     * Test that we can close a connection.
//     */
//    public void testJMSCloseConnection() {
//        int retValue = 1;
//        try {
//            createConnection();
//            closeConnection();
//        } catch (JMSException e) {
//            retValue = 0;
//            System.out.println("Exception occurred: " + e.toString());
//        } finally {
//            assertEquals(1, retValue);
//        }
//    }
//
//    /**
//     * Send a number of messages to the queue.
//     */
//    public void testSendMessages() {
//        int numMessages = 0;
//
//        try {
//            createConnection();
//            queueSender = queueSession.createSender(queue);
//            message = queueSession.createTextMessage();
//            for (int i = 0; i < NUM_MSGS; i++) {
//                message.setText("This is message " + (i + 1));
//                System.out.println("Sending non-logging message: " + message.getText());
//                queueSender.send(message);
//            }
//
//            // Wait so that the queue can drain...
//            Thread.sleep(this.WAIT_TIME);
//            numMessages = getMessageCount();
//        } catch (JMSException e) {
//            numMessages = -1;
//        } catch (InterruptedException ie) {
//            System.err.println("Test Case failed: " + ie.toString());
//            numMessages = -1;
//        } finally {
//            assertEquals(0, numMessages);
//        }
//    }
//
//    /**
//     * Send a Log message via JMS.
//     */
//    public void testSendLogMessage() {
//        int numMessages = 0;
//        try {
//            System.out.println("Sending a debug log message");
//            log.debug("Test debug message");
//
//            // Wait so that the queue can drain...
//            Thread.sleep(this.WAIT_TIME);
//            this.createConnection();
//            numMessages = getMessageCount();
//        } catch (JMSException e) {
//            numMessages = -1;
//        } catch (InterruptedException ie) {
//            System.err.println("Test Case failed: " + ie.toString());
//            numMessages = -1;
//        } finally {
//            assertEquals(0, numMessages);
//        }
//    }
//
//    /**
//     * Create a conection to a JMS server.
//     *
//     * @throws JMSException
//     */
//    private void createConnection() throws JMSException {
//        /*
//         * Create connection. Create session from connection; false means
//         * session is not transacted.
//         */
//        queueConnection = queueConnectionFactory.createQueueConnection();
//        queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//    }
//
//    /**
//     * Close a queue connection.
//     *
//     * @throws JMSException
//     */
//    private void closeConnection() throws JMSException {
//        queueConnection.close();
//    }
//
//    /**
//     * Drains the queue of Messages.
//     */
//    private void drainQueue() {
//        boolean moreMessages = true;
//        Message msg = null;
//        QueueReceiver queueReceiver = null;
//
//        try {
//            this.createConnection();
//            queueReceiver = queueSession.createReceiver(queue);
//            queueConnection.start();
//
//            while (moreMessages) {
//                msg = queueReceiver.receiveNoWait();
//                if (msg == null)
//                    moreMessages = false;
//                else
//                    System.out.println("Drained a message");
//            }
//            queueReceiver.close();
//        } catch (Exception e) {
//            System.out.println("Exception: " + e.toString());
//        }
//    }
//
//    /**
//     * Create a QueueBrowser and get the number of messages on the queue.
//     *
//     * @throws JMSException
//     * @return int Number of messages in the queue.
//     */
//    private int getMessageCount() {
//        Enumeration enumeration = null;
//        int counter = 0;
//
//        try {
//            queueBrowser = queueSession.createBrowser(queue);
//            enumeration = queueBrowser.getEnumeration();
//            // Count the queue messages...
//            while (enumeration.hasMoreElements()) {
//                counter++;
//                enumeration.nextElement();
//            }
//        } catch (JMSException e) {
//            System.out.println(e.toString());
//            counter = -1;
//        }
//        return counter;
//    }
//}
//