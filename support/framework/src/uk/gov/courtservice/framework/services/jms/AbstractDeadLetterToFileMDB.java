package uk.gov.courtservice.framework.services.jms;

import javax.ejb.MessageDrivenBean;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.HashMap;
import java.util.Enumeration;

import uk.gov.courtservice.framework.business.services.CSTextMessageBean;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: Abstract class for listening to dead letter queues and dumping the message to file
 * </p>
 * <p>
 * Description: Dumps the message to file
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version $Id: AbstractDeadLetterToFileMDB.java,v 1.4 2014/06/20 18:03:31 atwells Exp $
 */
public abstract class AbstractDeadLetterToFileMDB extends CSTextMessageBean implements MessageDrivenBean,
        MessageListener {

    private static final long serialVersionUID = 1L;

    private static final String JMS_FAILED_FOLDER = "." + System.getProperty("file.separator", "/") + "sjcse_jms_fail";
    private static final String JMS_ERROR_FOLDER  = "." + System.getProperty("file.separator", "/") + "sjcse_jms_error";

    protected final Logger log = CSServices.getLogger(getClass());

    /**
     * Logs failed JMS messages to file
     * Any errors will be logged to file and processing will end
     *
     * @param msg
     *            a message containing the queues to which an ExISS message has
     *            been sent
     * @ejb.interface-method view-type="local"
     */
    public void onTextMessage(final TextMessage textMessage) throws Exception {
        
        String messageIdString = null;
        String payload = null;
        String messages = null;
        String errorString = null;
        
        try
        {
            log.info("Start AbstractDeadLetterToFileMDB: onTextMessage");
            
            String propertyName = null;
            String propertyValue = null;

            Enumeration e = textMessage.getPropertyNames();

            HashMap<String, String> propertiesMap = new HashMap<String, String>();

            while (e.hasMoreElements()) {
                propertyName = (String) e.nextElement();
                log.debug("propertyName:" + propertyName);
 
                propertyValue = textMessage.getStringProperty(propertyName);
                log.debug("propertyValue:" + propertyValue);

                propertiesMap.put(propertyName, propertyValue);
                
                if (propertyName.equalsIgnoreCase("XHBMessageIdentifier") ||
                    propertyName.equalsIgnoreCase("RequestId") ||
                    propertyName.equalsIgnoreCase("GdgItemId"))
                {
                    log.debug("Set messageIdString:" + propertyValue);
                    messageIdString = propertyValue;
                }
            }

            log.debug("Start Logging the JMS Body to file");
            
            payload = "<JmsFailureMessage xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/jmsfailures\">";
            payload = payload + textMessage.getText();
            payload = payload + ("</JmsFailureMessage>");
            
            FileHelper.logDataToXmlFile(payload,
                                        getTransactionName().toLowerCase() + "_jms_body",
                                        messageIdString,
                                        null,
                                        JMS_FAILED_FOLDER);
            
            log.debug("End Logging the JMS Body to file");

            log.debug("Start Logging the JMS Properties to file");
            
            messages = "<JmsFailureProperties xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/jmsfailures\">";
            messages = messages + FileHelper.getXmlPropertiesData(propertiesMap);
            messages = messages + ("</JmsFailureProperties>");

            FileHelper.logDataToXmlFile(messages,
                    getTransactionName().toLowerCase() + "_jms_properties",
                    messageIdString,
                    null,
                    JMS_FAILED_FOLDER);
             
            log.debug("End Logging the JMS Properties to file");
        }
        catch(Exception e)
        {
            log.warn("Exception dumping failed JMS message to file: " + e.getMessage());
            e.printStackTrace(System.out);

            errorString = "<JmsErrors xmlns=\"http://www.courtservice.gov.uk/schemas/courtservice/xhibit/jmserrors\">";
            errorString = errorString + FileHelper.getXmlErrorData(e);
            errorString = errorString + ("</JmsErrors>");
            
            FileHelper.logDataToXmlFile(errorString,
                    getTransactionName().toLowerCase()+ "_jms_error",
                    messageIdString,
                    null,
                    JMS_ERROR_FOLDER);
            
            if(payload!=null)
            {
                log.warn("Exception dumping failed JMS message to file, failed payload was: " + payload);
            }
        }
        finally
        {
            log.debug("performAdditionalAuditing where required");
            
            performAdditionalAuditing(messageIdString,payload,messages);
            
            log.info("End AbstractDeadLetterToFileMDB: onTextMessage");
        }
    }

    /**
     * Implemented by concrete sub classes to return the transaction name
     *
     * @return the transaction name
     */
    public abstract String getTransactionName();
    
    /**
     * Overridden by concrete sub classes if further auditing required
     *
     * @param the transaction id
     * @param the message itself
     * @param the message properties
     */
    public void performAdditionalAuditing(String messageIdString, String payload, String properties) {}
}