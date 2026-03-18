package uk.gov.courtservice.xhibit.business.services.message_of_the_day;

import java.util.TreeMap;
import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerLocal;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerLocalHome;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.PollingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;


/**
 * <p>
 * Title: MessageOfTheDayControllerBean
 * </p>
 * <p>
 * Description: System Admin message of the day handling
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @ejb.bean name="MessageOfTheDayController" description="MessageOfTheDay Controller Bean"
 *           type="Stateless" view-type="remote"
 *           jndi-name="MessageOfTheDayControllerHome"
 * @ejb.transaction type="Required"
 * 
 * @version $Id: MessageOfTheDayControllerBean.java,v 1.5 2015/01/23 14:25:02 atwells Exp $
 */
public class MessageOfTheDayControllerBean implements SessionBean {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(MessageOfTheDayControllerBean.class);

    private static final String FILE_BASE = System.getProperty("MessageOfTheDay.fileLocation");
    
    private TreeMap<String,String> fileNameAndPath = new TreeMap<String, String>();
    
    protected SessionContext ctx;

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbActivate() {
        log.debug("ejbActivate()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbRemove() {
        log.debug("ejbRemove()");
    }

    /**
     * This method is required by the EJB Specification, but is not used by this
     * example.
     */
    public void ejbPassivate() {
        log.debug("ejbPassivate()");
    }

    /**
     * Sets the session context.
     * 
     * @param ctx
     *            SessionContext Context for session
     */
    public void setSessionContext(SessionContext ctx) {
        log.debug("setSessionContext(" + ctx + ")");
        this.ctx = ctx;
    }

    public void ejbCreate() throws CreateException {
        log.debug("ejbCreate()");
    }

    private String getFileNameAndPath(String filename) {
        log.debug("Getting filename and path");
        String str = fileNameAndPath.get(filename);
        if (str != null)
            return str;
        
        if ((FILE_BASE == null) || (FILE_BASE.length() == 0)) {
            log.error("Folder location of messages has not been set");
        } else {
            log.debug("Folder location of file has been set to : " + FILE_BASE);
        }
        str = FILE_BASE + filename;
        log.debug("Getting filename and path");
        fileNameAndPath.put(filename, str);
        return str;
    }

    private String getMOTDFilename() {
        return getFileNameAndPath("message_of_the_day.txt");
    }
    
    private String getLockoutFilename() {
        return getFileNameAndPath("lockout_message.txt");
    }
    
    private String getLogoutFilename() {
        return getFileNameAndPath("logout_message.txt");
    }
    
    private String readFile(String fileName) throws IOException {
        BufferedReader inputStream = null; 
        StringBuffer message = new StringBuffer();
        try {
            inputStream = 
                new BufferedReader(new FileReader(fileName));
            
            int c;
            while ((c = inputStream.read()) != -1) {
                message.append((char)c);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return message.toString().trim();
    }
    
    private String getMessage(String filename) {
        try {
            String message = readFile(filename);
            return message;
        }
        catch (IOException e) {
            if (e instanceof java.io.FileNotFoundException) {
                // normal running
            } else {
                log.error("IOException: " + e.toString());
            }
        }
        return null;
    }
    
    /**
     * Return the message that will be shown if the system is about to be shutdown.
     * e.g. "Please log off.  The system is about to shutdown" 
     * 
     * @return the logout message
     * @ejb.interface-method view-type="both"
     */
    public String getLogoutMessage(long userLoginId) {
    	UserTerminalControllerLocal utcl = (UserTerminalControllerLocal) CSServices.getEJBServices().createLocalSession(UserTerminalControllerLocalHome.class);
    	utcl.updateUserLoginSession(userLoginId);
        return getMessage(getLogoutFilename());
    }
    
    
    /**
     * Returns the currentpolling interval value which is stored
     * in the database.
     * 
     * @return the polling interval
     * @ejb.interface-method view-type="both"
     */
    public PollingValue getPollingInterval() {
        log.debug("Getting polling interval");
        PollingQuery pollingQuery = new PollingQuery();
        PollingValue pv = pollingQuery.getData();
        log.debug("Getting polling interval: "+pv.getPollingInterval());
        log.debug("Getting check polling interval: "+pv.getCheckPollingInterval());
    	return pv;
    }
    
    /**
     * Return the message that will be shown to the user at login time.
     * e.g. "The system will be shut down at 3pm today"
     * 
     * @return the message of the day
     * @ejb.interface-method view-type="both"
     */
    public String getMessageOfTheDay() {
        return getMessage(getMOTDFilename());
    }

    /**
     * Return the message which will be shown to the user if login has been disabled.
     * e.g. "The system is currently down - please await further notice"
     * 
     * @return the lockout message.
     * @ejb.interface-method view-type="both"
     */
    public String getLockoutMessage() {
        return getMessage(getLockoutFilename());
    }

}