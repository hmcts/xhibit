package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.*;
import uk.gov.courtservice.xhibit.business.services.dartsstartup.DartsStartupClass;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.documentum.*;
import uk.gov.courtservice.xhibit.business.services.dartswebserviceclient.DARTSService.*;

import com.sun.xml.rpc.client.ClientTransportException;



/**
 * <p>
 * Title: DartsSenderControllerBean
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * As of the DARTS NRO release and the requirement of a normal and a priority flow of messages
 * this class has become abstract and the working implementations as DartsPrioritySenderControllerBean
 * and DartsNormalSenderControllerBean which service priority and normal messages from two different
 * database sources.
 * </p>
 * <p>
 * Session bean for sending DARTS messages.  This class is called regularly by the scheduler class
 * (uk.gov.courtservice.framework.scheduler.Scheduler timing in properties file with scheduler package )
 * and is responsible for picking new messages from the DAR_NEW_MESSAGES table and sending them 
 * via the darts webservice and reporting their status back to the DAR_MESSAGE_STORE.  Failed messages due
 * transport errors are re-entered into the DAR_NEW_MESSAGES table for retry.
 * </p>
 * <p>
 * Before messages can be sent across the using the addDocument webservice, a session needs to be 
 * created with the DARTS system using the contextRegitry webservice.  This class handles the management
 * of this  session and refereshes it upon expiry.  The _connectionToken string holds the current 
 * session token.
 * </p>
 * 
 * <p>
 * NOTE: The client code provided by the DARTS project has been modified so it 
 * works correctly with the 9.2 version of weblogic. The reason being that the service 
 * supplied is implemented as a JAW-WS2.0 web service and the current version of weblogic (9.2) 
 * is only able to support JAX-RPC web service clients.
 * 
 * This has resulted in the following issues;
 * The end point for the DARTSService is explicitly defined in DARTSServicePort_Stub
 * and the end point for the ContextRegistryService is explicitly defined in the 
 * ContextRegistryServicePort_Stub.
 * 
 * Also a hack to inject the security token required by DARTS is implemented in 
 * DMAuthStubBase to override the normal generated code since clientgen could 
 * not handle this.
 * 
 * ~~~ The client cannot be regenerated using client gen because of these manual modifications. ~~~
 * </p>
 * <p>
 * Company: logica
 * </p>
 * 
 * @author Luis Valenzuela
 * @version 1.1 20090105 
 * 
 */
public abstract class DartsSenderControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;
    protected static final Logger log = CSServices.getLogger(DartsSenderControllerBean.class);

    protected static final int RET_CODE_SUCCESS      = 200;
    protected static final int RET_CODE_INVALID_XML  = 400;
    protected static final int RET_CODE_NOT_FOUND    = 404;
    protected static final int RET_CODE_ERROR        = 500;
    protected static final int RET_CODE_SEND_ERROR   = 666;
    protected static final int RET_CODE_TOKEN_ERROR  = 999;
    
    protected static long WAIT_BETWEEN_CONNECTION_RETRIES; // 30 seconds default
    protected static long _nextRetry;
    
    protected DARTSServicePort _dartsService = null;
    
    protected DartsConfiguration config;
    protected CommonDartsNewMessageDB dartsNewMessageDatabase;
    protected DartsMessageStoreDatabase _dartMessageDatabase;
    protected boolean dump;
    protected boolean _connected = false;
    
    private static String CLASS_NAME = "DartsSenderControllerBean";    
    

    /**
     * Initialises all of the instance variables for this session bean.
     * 
     * @see uk.gov.courtservice.framework.business.services.CSSessionBean
     *      #ejbCreate()
     */
    public void ejbCreate() throws CreateException 
    {
        super.ejbCreate();

        try {
            log.debug("************** Creating DartsSender EJB **************");
            dartsNewMessageDatabase = getNewMessageDB();
            _dartMessageDatabase = new DartsMessageStoreDatabase();

            config = DartsConfiguration.getInstance();
            String dumpAsString = config.getProperty("darts.dump");
            if(dumpAsString == null)
            {
                throw new DartsException("DARTS configuration object not refreshed from DB,"+
                        "sender bean cannot instantiate.");
            } else {
                dump = Boolean.valueOf(dumpAsString).booleanValue();
            }    
            String retryWaitAsString = config.getProperty("darts.conn_retry_wait");
            WAIT_BETWEEN_CONNECTION_RETRIES = (retryWaitAsString!=null) ? Long.decode(retryWaitAsString).longValue() : 30000L; 
            if (dump) {
                log.debug("************** Creating dummy DARTS service **************");
                /* Don't set up a real repository, just dump messages to the log */
                _dartsService      = new MockDartsService();                
                _connected = true;
            } else {
                log.debug("************** Creating real DARTS service **************");
                _dartsService = new DARTSService_Impl().getDARTSServicePort();
            }
            
            // Setup the global JAXM message factory
            System.setProperty("javax.xml.soap.MessageFactory", "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl");
            // Setup the global JAX-RPC service factory
            System.setProperty("javax.xml.rpc.ServiceFactory", "weblogic.webservice.core.rpc.ServiceFactoryImpl");
            
        } 
        catch (DartsException de) 
        {
            log.error("Error creating DartsSenderSessionBean - see stack trace :" + de.getMessage());
            de.printStackTrace();
            _connected = false;
            throw new CreateException();
        }
        log.debug("************** DartsSender EJB successfully created **************");
    }// end of ejbCreate()

    
    /** 
     * Method to return the correct DB connection.
     * 
     * @return CommonDartsNewMessageDB
     */
    protected abstract CommonDartsNewMessageDB getNewMessageDB();
    // this is to be overriden in the subclasses
    
    /**
     * Implementation of RemoteTask so that this process is called by the timer
     * process. This method must have the same transactional behaviour as
     * processFormattingDocument. This method is called by the Scheduler class to create
     * a unit of work for the bean.
     * 
     * @ejb.interface-method view-type="remote"
     * @ejb.transaction type="Required"
     */
    public void doTask(String taskName) 
    {
    	String METHOD_NAME = "doTask";
    	if (log.isDebugEnabled()) {
    		log.debug("Entering method: "+CLASS_NAME+"."+METHOD_NAME);
    	}
    	
    	DartsStartupClass dsc = null;
    	try{
        	Class dartsStartupClass = Class.forName("uk.gov.courtservice.xhibit.business.services.dartsstartup.DartsStartupClass");
        	Method method = dartsStartupClass.getMethod("getInstance", new Class[]{});
        	dsc = (DartsStartupClass) method.invoke(null, new Object[]{});
    	}catch(ClassNotFoundException cnfe){
    		log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + cnfe.getMessage());
    		cnfe.printStackTrace();
    	}catch(NoSuchMethodException nsme){
    		log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + nsme.getMessage());
    		nsme.printStackTrace();
    	}catch(InvocationTargetException ite){
    		log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + ite.getMessage());
    		ite.printStackTrace();
    	}catch(IllegalAccessException iae){
    		log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + iae.getMessage());
    		iae.printStackTrace();
    	}
    	
    	if(dump && dsc.getToken(taskName) == null){
    		//dump property is true and this is the first time to run so construct new random connection token
    		java.util.Random rand = new java.util.Random ();
    		String token = String.valueOf(rand.nextInt()); 
    		dsc.setToken(taskName,token);
    		log.debug("Allocated random connectionToken of: "+token+" for task "+taskName);
    	}
    	
    	/* Make sure a valid connection has been established */
        if(_connected)
        {
        	log.debug("Started task "+taskName);
            processMessages(dsc.getToken(taskName));
        }
        /* If not connected then try to connect to Context Registry Service, bean will not 
         * process messages untill a valid connection has been established. */ 
        else 
        {
            if ( _nextRetry < System.currentTimeMillis()) 
            {
                try {
                    if (log.isDebugEnabled()) {
                        log.debug("Attempting to establish DARTS context registry service.");
                    }
                    dsc.setToken(taskName,getDartsConnection());
                    _connected = true;
                    processMessages(dsc.getToken(taskName));
                } 
                catch (DartsException de) 
                {
                	log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + de.getMessage());
                	de.printStackTrace();
                    /* should not enter here as EJB already instantiated. */
                    _connected = false;
                }
                catch (RemoteException re) 
                {
                	log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + re.getMessage());
                	re.printStackTrace();
                    /* Test to see if exception thrown by transport. */
                    Throwable t = re.getCause();
                    if( t instanceof ClientTransportException )
                    {
                        log.error("Error registering context - connection to DARTS could not be established. \n" +
                                "Reason is : " + t.getMessage());
                        t.printStackTrace();
                        _connected = false;
                        doWait();
                    }
                    /* RemoteException wraps unexpected exception. */
                    else
                    {
                        log.error("Error registering context - see stack trace." );
                        re.printStackTrace();
                        _connected = false;
                        doWait();
                    }
                }// end of trying to establish connection
            }// end of if statement             
        }// end of else clause : connection established.
    }// end of doTask()


    
    /**
     * This method processes messages recieved from the DAR_NEW_MESSAGES database if present, the transactional 
     * state of the calling method should be carried through to this.
     * 
     */
    protected void processMessages(String connectionToken) 
    {
    	String METHOD_NAME = "processMessages";
    	if (log.isDebugEnabled()) {
    		log.debug("Entering method: "+CLASS_NAME+"."+METHOD_NAME);
    	}
    	
        int successCount = 0;
        int failCount = 0;     
        ArrayList<Integer> successIds = new ArrayList<Integer>();
        ArrayList<Integer> failIds = new ArrayList<Integer>();
        ArrayList<String> failDetails = new ArrayList<String>();
        
        if (log.isDebugEnabled()) {
            log.debug( connectionToken + " : Checking for DARTS messages to process.");
        }
        DartsMessageVO[] messages = dartsNewMessageDatabase.getMessages();        
        
        if (messages.length  < 1){
            // There are no DARTS messages returned from the DB
            // log.debug("No DARTS messages to send");
        }
        else
        {
            for (DartsMessageVO message : messages) {
               
                String messageId = Integer.toString(message.getId());
                DARTSResponse response;
                /* 
                 * Call addDocument() web service with DARTS provided libs. 
                 */
                try {
                    _dartsService.setToken(connectionToken);
                    if(log.isDebugEnabled()){
                         log.debug( connectionToken + " : DARTS: Calling addDocument web service." );
                    }  
                    response = _dartsService.addDocument(messageId, message.get_xhibitMessageCode(), message
                            .get_exissMessageCode(), message.get_payload());
                } 
                /*  This is an exception thrown by the DARTS server and should result in a new session being established. */
                catch (Exception_Exception ee) 
                {
                    log.error("Connection not valid, resetting DARTS session token.");
                    
                    log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + ee.getMessage());
                	ee.printStackTrace();
                    
                    _connected = false;
                    response = new DARTSResponse();
                    response.setCode(String.valueOf(RET_CODE_TOKEN_ERROR));
                }
                /* This exception can be thrown from http errors or network issues.  Do not reset the DARTS connection. */
                catch (Exception e) 
                {
                    log.error("Error on sending DARTS message - web service call failure");
                    log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + e.getMessage());
                	e.printStackTrace();
                    /* Try restablishing the DARTS Service connection. */
                    _dartsService = new DARTSService_Impl().getDARTSServicePort();
                    response = new DARTSResponse();
                    response.setCode(String.valueOf(RET_CODE_SEND_ERROR));
                }  // end of  _dartsService.addDocument() webservice call                 
                
                /*
                 * The addDocument webService must have completed without exception to arrive here.  Now
                 * the result is processed and the appropriate actions taken.
                 */                
                int responseInt = Integer.parseInt(response.getCode());
                switch(responseInt){
    
                    /* Successful */
                    case RET_CODE_SUCCESS: 
                        successIds.add( new Integer(message.getId()) ); 
                        successCount++;
                        log.debug("Sucessfully sent DARTS message - messageId= " + message.getId());
                        break;
    
                    /* Invalid XML found by DARTS */                        
                    case RET_CODE_INVALID_XML:
                        failIds.add( new Integer(message.getId()) ); 
                        failDetails.add( responseInt + " : " + response.getMessage() ); 
                        failCount++;
                        log.debug("Error on sending DARTS message - Invalid XML - messageId= " + message.getId());
                        break;
    
                    /* No Matching event Type or Court found */
                    case RET_CODE_NOT_FOUND:                                    
                        failIds.add( new Integer(message.getId()) ); 
                        failDetails.add( responseInt + " : " + response.getMessage()); 
                        failCount++;
                        log.debug("Error on sending DARTS message - Event/Court not found - messageId= " + message.getId());
                        break;
    
                    /* Error within DARTS during processing of event or document */
                    case RET_CODE_ERROR:                                    
                        failIds.add( new Integer(message.getId()) ); 
                        failDetails.add( responseInt + " : " + response.getMessage()); 
                        failCount++;
                        log.debug("Error on sending DARTS message - Error processing - messageId= " + message.getId());
                        break;
                        
                    /* Internal Error caused by transport issue, e.g Http failure */
                    case RET_CODE_SEND_ERROR:    
                        if( message.getRetryCount() < Integer.parseInt(config.getProperty("darts.retry.max.times"))) {
                            dartsNewMessageDatabase.reportMessageToBeRetried(message);
                        } else {
                            failIds.add( new Integer(message.getId()) ); 
                            failDetails.add( "Retries exceeded." ); 
                            failCount++;
                            log.error("Error on sending DARTS message - web service call failure - messageId=" + message.getId());
                        }
                        break;
                        
                    case RET_CODE_TOKEN_ERROR:
                        /* Messages that fail to be transfered because of token expiry should be retried instantly 
                         * (in the StoreProcedure and should not have the retry count incremented so that they can 
                         * fail becuase of number of retries exceeding limit. 
                         **/
                        message.setRetryCount(0);
                        dartsNewMessageDatabase.reportMessageToBeRetried(message);
                        break;
                        
                    /* Return code not recognised, fail message and store return message */            
                    default:  {
                        failIds.add( new Integer(message.getId()) );
                        failDetails.add( responseInt + " : " + response.getMessage()); 
                        failCount++;
                        log.error("Error on sending DARTS message - Unknown error - messageId : " + message.getId() +
                                  ", Return code : " + responseInt + ", Return Message : " + response.getMessage() );
                        break;
                        
                    }// end of default statement
                }// end of switch statement 
            }// end of for loop

             

            /* Report Successes to the DAR_MESSAGE_STORE table */
            if (successCount > 0) {
                Integer[] successIdArray = new Integer[successIds.size()];
                successIdArray = successIds.toArray(successIdArray);
                _dartMessageDatabase.reportSuccesses(successIdArray);
            }// end of success test             
            
            /* Report Failures to the DAR_MESSAGE_STORE table */
            if (failCount > 0) {
                Integer[] failIdArray = new Integer[failIds.size()];
                failIdArray = failIds.toArray(failIdArray);
                String[] failDetailArray = new String[failDetails.size()];
                failDetailArray = failDetails.toArray(failDetailArray);
                _dartMessageDatabase.reportFailures(failIdArray, failDetailArray);
            }// end of failure test    

        }// end of enclosing if - else
    }// end of processMessages()


    /**
     * Internal Method to establish connection with the DARTS interface using config params from the DB.
     */
    protected String getDartsConnection() throws DartsException, RemoteException{
    	
    	String METHOD_NAME = "getDartsConnection";
    	if (log.isDebugEnabled()) {
    		log.debug("Entering method: "+CLASS_NAME+"."+METHOD_NAME);
    	}

        ContextRegistryServicePort contextRegistryService = null;
        contextRegistryService = new ContextRegistryService_Impl().getContextRegistryServicePort();
                
        RepositoryIdentity repositoryIdentity = new RepositoryIdentity();
        repositoryIdentity.setUserName(config.getProperty("darts.user"));
        repositoryIdentity.setPassword(config.getProperty("darts.password"));
        repositoryIdentity.setRepositoryName(config.getProperty("darts.repository"));
        
        ServiceContext serviceContext = new ServiceContext();
        serviceContext.setIdentities(new RepositoryIdentity[] { repositoryIdentity });

        String connectionToken = contextRegistryService.register(serviceContext, "");
        if (log.isDebugEnabled()) {
            log.debug("DARTS connection established. Token is : " + connectionToken );
        }
        
        return connectionToken;
    }//  end of getDartsConnection()

    
    /**
     * Internal Method to wait the bean for WAIT_BETWEEN_CONNECTION_RETRIES time until trying to reconnect to service.
     * NOTE: This is not a wait on the thread, the time delay is accomplished through a comparison on _nextRetry and
     * the system time.
     */
    protected void doWait(){
        log.error("DARTS connection cannot be established. Waiting for : " + 
                (WAIT_BETWEEN_CONNECTION_RETRIES / 1000)  + " seconds before retry."  );
        _nextRetry = System.currentTimeMillis() + WAIT_BETWEEN_CONNECTION_RETRIES;
    }// end of doWait()

}// end of Class

