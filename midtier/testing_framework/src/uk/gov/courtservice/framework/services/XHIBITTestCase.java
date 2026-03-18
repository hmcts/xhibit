package uk.gov.courtservice.framework.services;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import junit.framework.TestCase;


/**
 * @author Meeraj
 * @title LazyLoadingServiceLocatorImpl
 */
public class XHIBITTestCase extends TestCase 
{
    private static String DEFAULT_SERVER_URL="PROVIDER_URL";
    private static String DEFAULT_SERVER_FACTORY="INITIAL_CONTEXT_FACTORY";
    private static String XHIBIT_DATASOURCE="xhibit.datasourcename";
    protected static Properties junitProps = new Properties();
    
    private Map jndiCache = Collections.synchronizedMap(new HashMap());
    private Properties applicationProperties;
    private Context context;
    private Hashtable env;
    private XHIBITDatabaseHarness database;

    static 
    {
        try
        {
            String filepath = System.getenv("XHIBIT_HOME");
            if (filepath == null )
            {
                // No XHIBIT_HOME set, so we are probably running from eclipse, set 
                // path relative to the project folders
                filepath = "..\\..\\..";
            }
            junitProps.load(new FileInputStream(filepath + "\\junit.properties"));
        }
        catch (IOException e)
        {
           // throw e;
        }      
    }

    
    public XHIBITTestCase(String testName) 
    {
    	super(testName);
    	applicationProperties = this.getProperties();
        env = new Hashtable();
        
  
    }
    
    public void setUp() throws Exception 
    {
    	super.setUp();        
    }
    
    public void tearDown() throws Exception {
    	database.close();
    }
    
    /**
     * Provides a name/value properties file as a properties object to minimise
     * required changed to to other parts of the application while ensuring that
     * configuration data is loaded from a central location.
     * 
     * @param componentName
     *            the String for the component type e.g. listimport
     * @return Properties object based on the file loaded
     * @throws CSconfigurationException
     *             if configuration files cannot be correctly read
     */
    public Properties getProperties()
    {
    	Properties componentProps=null;
        String errMsg = null;
        componentProps = new Properties();
        componentProps.put(XHIBIT_DATASOURCE, "XhibitOracleTxDataSource");
        componentProps.put(DEFAULT_SERVER_URL,junitProps.getProperty("junit.xhibit.midtierserver"));
        componentProps.put(DEFAULT_SERVER_FACTORY,"weblogic.jndi.WLInitialContextFactory");
       return componentProps;
    }

    public XHIBITDatabaseHarness getDatabase() {
    	return database;
    }
    
    
    public void setContext(String username, String password) {
    	env.put(Context.SECURITY_PRINCIPAL, username);
    	env.put(Context.SECURITY_CREDENTIALS, password);
    	context = this.getInitialContext();
        DataSource xhibitDataSource = this.getXhibitDataSource();
        database= new XHIBITDatabaseHarness(xhibitDataSource);
    	database.setContext(username,password);
    }
    
    /**
     * Utility method to lookup objects
     * 
     * @param serverName
     * @return
     */
    protected Object lookup(String jndiName) 
    {
    	Object ejbObject=null;
        if (!jndiCache.containsKey(jndiName)) {
            
            try {
                ejbObject = context.lookup(jndiName);
                jndiCache.put(jndiName, ejbObject);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return jndiCache.get(jndiName);
    }

    /**
     * Gets the initail context
     * 
     * @return
     */
    public Context getInitialContext() 
    {
    	Context ctx=null;
        try {
        	  this.setEnv();
              ctx = new InitialContext(env);
        } catch (Exception e) {
            e.printStackTrace();
           }
        return ctx;
    }

    /**
     * Gets the default datasource
     * 
     * @return
     */
    public DataSource getXhibitDataSource() 
    {
        String dsJndiName;
        Object objDataSource;
         dsJndiName = (String)applicationProperties.getProperty(XHIBIT_DATASOURCE);
         objDataSource = this.lookup(dsJndiName);
        return (javax.sql.DataSource) lookup(dsJndiName);
    }

    /**
     * Gets a remote EJB
     * 
     * @param homeClass
     * @return
     */
    public EJBHome getRemoteHome(Class homeClass) 
    {
        Object homeObject = lookup(getHomeJndiName(homeClass));
        return (EJBHome) PortableRemoteObject.narrow(homeObject, homeClass);
    }

    /**
     * Gets a local EJB
     * 
     * @param homeClass
     * @return
     */
    public EJBLocalHome getLocalHome(Class homeClass) 
    {
    	  String ejbName="";
    	  ejbName = getHomeJndiName(homeClass);
    	  return (EJBLocalHome) lookup(ejbName);
    }

    /**
     * Gets the queue connection factory
     * 
     * @param qcfBindingName
     * @return
     */
    public QueueConnectionFactory getQueueConnectionFactory(String qcfBindingName) 
    {
        return (QueueConnectionFactory) lookup(qcfBindingName);
    }

    /**
     * Gets the queue
     * 
     * @param queueBindingName
     * @return
     */
    public Queue getQueue(String queueBindingName) 
    {
        return (Queue) lookup(queueBindingName);
    }

    /**
     * Gets the topic connection factory
     * 
     * @param tcfBindingName
     * @return
     */
    public TopicConnectionFactory getTopicConnectionFactory(String tcfBindingName) 
    {
        return (TopicConnectionFactory) lookup(tcfBindingName);
    }

    /**
     * Gets the topic
     * 
     * @param topicBindingName
     * @return
     */
    public Topic getTopic(String topicBindingName) 
    {
        return (Topic) lookup(topicBindingName);
    }
    
    /**
     * Gets the connection factory
     * 
     * @param cfBindingName
     * @return
     */
    public ConnectionFactory getConnectionFactory(String cfBindingName) 
    {
        return (ConnectionFactory) lookup(cfBindingName);
    }

    /**
     * Gets the destination
     * 
     * @param dBindingName
     * @return
     */
    public Destination getDestination(String dBindingName) 
    {
        return (Destination) lookup(dBindingName);
    } 

    /**
     * Utility method to populate the JNDI properties
     * 
     * @return
     */
    protected void setEnv() 
    {
        env.put(Context.INITIAL_CONTEXT_FACTORY, (String)applicationProperties.getProperty(DEFAULT_SERVER_FACTORY));
        env.put(Context.PROVIDER_URL, (String)applicationProperties.getProperty(DEFAULT_SERVER_URL));
    }
    
    
    /**
     * Gets the home JNDI name
     * 
     * @param clazz
     * @return
     */
    private static String getHomeJndiName(Class clazz) 
    {
        String fullClassName = clazz.getName();
        String jndiName="";
        int lastDotIndex = fullClassName.lastIndexOf('.');

        jndiName = fullClassName.substring(lastDotIndex + 1, fullClassName.length());
        return jndiName;
    }
    
    
}
