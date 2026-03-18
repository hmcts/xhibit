package uk.gov.courtservice.xhibit.business.services.dartsmessagesender;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.xml.ws.BindingProvider;

import org.apache.log4j.Logger;

import com.sun.xml.ws.client.ClientTransportException;

import uk.gov.courtservice.framework.business.services.CSSessionBean;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.darts.CommonDartsNewMessageDB;
import uk.gov.courtservice.xhibit.business.services.darts.DartsConfiguration;
import uk.gov.courtservice.xhibit.business.services.darts.DartsException;
import uk.gov.courtservice.xhibit.business.services.darts.DartsMessageStoreDatabase;
import uk.gov.courtservice.xhibit.business.services.darts.DartsMessageVO;
import uk.gov.courtservice.xhibit.business.services.darts.MockDartsServiceWS;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.DARTSService;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.DARTSServicePort;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.service.Exception_Exception;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.DARTSService.synapps.DARTSResponse;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.datamodel.core.context.RepositoryIdentity;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.datamodel.core.context.ServiceContext;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.rt.services.ContextRegistryService;
import uk.gov.courtservice.xhibit.business.services.dartsjaxwswebserviceclient.documentum.rt.services.ContextRegistryServicePort;
import uk.gov.courtservice.xhibit.business.services.dartsstartup.DartsStartupClass;



/**
 * <p>
 * Title: WSDartsSenderControllerBean
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
 * NOTE: The client code provided by the DARTS project has been modified slightly so that the WSDL locations 
 * are provided in DARTSService and ContextRegistryService.
 * 
 * Also a SoapHandler sits between Client and Server to inject the security token required by DARTS into the 
 * request.
 * 
 * 
 * ~~~ The client cannot be regenerated using client gen without adding in these changes post creation. ~~~
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Nia Walters & Pete Twibill
 * @version 1.1 20160820 
 * 
 */
public abstract class WSDartsSenderControllerBean extends CSSessionBean implements SessionBean {

    private static final long serialVersionUID = 1L;
    protected static final Logger log = CSServices.getLogger(WSDartsSenderControllerBean.class);

    protected static final int RET_CODE_SUCCESS      = 200;
    protected static final int RET_CODE_INVALID_XML  = 400;
    protected static final int RET_CODE_NOT_FOUND    = 404;
    protected static final int RET_CODE_ERROR        = 500;
    protected static final int RET_CODE_SEND_ERROR   = 666;
    protected static final int RET_CODE_TOKEN_ERROR  = 999;
    
    protected static long WAIT_BETWEEN_CONNECTION_RETRIES; // 30 seconds default
    protected static long _nextRetry;
    protected static String NAMEOFTASK ;
    protected DARTSServicePort _dartsService = null;
    
    protected DartsConfiguration config;
    protected CommonDartsNewMessageDB dartsNewMessageDatabase;
    protected DartsMessageStoreDatabase _dartMessageDatabase;
    protected boolean dump;
    protected boolean _connected = false;
    protected DartsStartupClass dsc;
    
    private static String CLASS_NAME = "DartsSenderControllerBeanWS";    
    
    public static String dartsWsdlLocation, contextRegWsdlLocation ;
    
    /**
     * For access in Darts Server.
     * @return wsdl location
     */
    public static String getDartsWsdlLocation () {
    	return dartsWsdlLocation;
    }
    
    /**
     * For access in Context Registry.
     * @return wsdl location
     */
    public static String getContextRegWsdlLocation () {
    	return contextRegWsdlLocation;
    }
    
    
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
            dartsWsdlLocation = config.getProperty("darts.wsdl.service");
            contextRegWsdlLocation = config.getProperty("darts.wsdl.register");
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
                _dartsService      = new MockDartsServiceWS();                
                _connected = true;
            } else {
                log.debug("************** Creating real DARTS service **************");
                _dartsService = new DARTSService().getDARTSServicePort();
            }
            
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
        NAMEOFTASK = taskName;
    	String METHOD_NAME = "doTask";
    	if (log.isDebugEnabled()) {
    		log.debug("Entering method: "+CLASS_NAME+"."+METHOD_NAME+ " in WS bean");
    	}
    	
    	dsc = null;
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
    		log.debug("Allocated random connectionToken of: "+token+" for task "+taskName+" in WS bean");
    	}
    	
    	/* Make sure a valid connection has been established */
        if(_connected)
        {
        	log.debug("Started task "+taskName);
            processMessages(dsc.getToken(taskName));
        }
        /* If not connected then try to connect to Context Registry Service, bean will not 
         * process messages until a valid connection has been established. */ 
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
     * @param connectionToken used to authenticate the request
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
            log.debug( connectionToken + " : Checking for DARTS messages to process in WS bean.");
        }
        DartsMessageVO[] messages = dartsNewMessageDatabase.getMessages();        
        
        if (messages.length  < 1){
            // There are no DARTS messages returned from the DB
             log.debug("No DARTS messages to send");
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
                  //Setting the context token
                    BindingProvider binding =  (BindingProvider) _dartsService;
            		binding.getRequestContext().put("token", connectionToken);
            		
                    if(log.isDebugEnabled()){
                         log.debug( connectionToken + " : DARTS: Calling addDocument web service (WS)." );
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
                	
                	//Try and get a new token and send doc again
                    response = secondAttemptDueToTokenFailure(message);
            		//overwrite conenctionToken for next message in array
              		connectionToken = dsc.getToken(NAMEOFTASK);
                }
                /* This exception can be thrown from http errors or network issues.  Do not reset the DARTS connection. */
                catch (java.lang.Exception e) 
                {
                    //if the token is incorrect then set connected to false so that context reg is called the next time
                    if(e.getMessage().indexOf("Please make sure you are using the right registry and the token is not expired.")>0) {
                    	log.error("Connection not valid, token not found in DARTS session token.");                        
                        log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + e.getMessage());
                    	e.printStackTrace();
                    	
                    	//Try and get a new token and send doc again
                        response = secondAttemptDueToTokenFailure(message);
                		//overwrite conenctionToken for next message in array
                  		connectionToken = dsc.getToken(NAMEOFTASK);
                                           	
                    }
                    else {
                    	log.error("Error on sending DARTS message - web service call failure");
                        log.error("Error in " +CLASS_NAME+"."+METHOD_NAME+" - see stack trace :" + e.getMessage());
                    	e.printStackTrace();
                        /* Try restablishing the DARTS Service connection. */
                        _dartsService = new DARTSService().getDARTSServicePort(); //TODO not getting called when retrying messages?
                        response = new DARTSResponse();
                        response.setCode(String.valueOf(RET_CODE_SEND_ERROR));
                    }
                    
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
     * 
     * @return String representation of the Darts connection
     */
    protected String getDartsConnection() throws DartsException, RemoteException{
    	
    	String METHOD_NAME = "getDartsConnection";
    	if (log.isDebugEnabled()) {
    		log.debug("Entering method: "+CLASS_NAME+"."+METHOD_NAME+" of WS");
    	}

        ContextRegistryServicePort contextRegistryService = null;
        contextRegistryService = new ContextRegistryService().getContextRegistryServicePort();
                
        RepositoryIdentity repositoryIdentity = new RepositoryIdentity();
        repositoryIdentity.setUserName(config.getProperty("darts.user"));
        repositoryIdentity.setPassword(config.getProperty("darts.password"));
        repositoryIdentity.setRepositoryName(config.getProperty("darts.repository"));
        
        ServiceContext serviceContext = new ServiceContext();
        //as per ServiceContext.java to adding a new item instead of setting identities.
        serviceContext.getIdentities().add(repositoryIdentity);
        

        String connectionToken = contextRegistryService.register(serviceContext, "");
        
       
        if (log.isDebugEnabled()) {
            log.debug("DARTS connection established with WS. Token is : " + connectionToken );
        }
        
        return connectionToken;
    }//  end of getDartsConnection()

    
    /**
     * Internal Method to wait the bean for WAIT_BETWEEN_CONNECTION_RETRIES time until trying to reconnect to service.
     * NOTE: This is not a wait on the thread, the time delay is accomplished through a comparison on _nextRetry and
     * the system time.
     */
    protected void doWait(){
        log.error("DARTS connection cannot be established in WS. Waiting for : " + 
                (WAIT_BETWEEN_CONNECTION_RETRIES / 1000)  + " seconds before retry."  );
        _nextRetry = System.currentTimeMillis() + WAIT_BETWEEN_CONNECTION_RETRIES;
    }// end of doWait()

    /**
     * Called when token fails.  Goes to get a new token straight away and then try and resend the doc
     */
    protected DARTSResponse secondAttemptDueToTokenFailure(DartsMessageVO message) {
    	DARTSResponse response;
    	
    	  try {
         	 dsc.setToken(NAMEOFTASK, getDartsConnection());
         	 
          	//try to reset the connection again and send to addDocument
          	 BindingProvider binding =  (BindingProvider) _dartsService;
       		binding.getRequestContext().put("token", dsc.getToken(NAMEOFTASK));
       		      		
               if(log.isDebugEnabled()){
                    log.debug( dsc.getToken(NAMEOFTASK) + " : DARTS: Calling addDocument again as token is invalid for message "+ Integer.toString(message.getId()));
               }  
               
				response = _dartsService.addDocument(Integer.toString(message.getId()), message.get_xhibitMessageCode(), message
				         .get_exissMessageCode(), message.get_payload());
				return response;
			} catch (Exception error) {
				 _connected = false;
                 response = new DARTSResponse();
                 response.setCode(String.valueOf(RET_CODE_TOKEN_ERROR));
                 return response;
			}
     
    	
    }
}// end of Class

