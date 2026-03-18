package uk.gov.courtservice.xhibit.business.services.validation; 

import java.io.File;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.log4j.Logger;

import com.bea.common.security.service.SAML2PublishException.FileNotFoundException;

import uk.gov.courtservice.xhibit.business.database.ValidationDatabase;
import uk.gov.courtservice.xhibit.business.services.validation.sax.FileEntityResolver;
import uk.gov.courtservice.xhibit.business.services.validation.sax.SAXValidationService;



/**
 * EJB for validating XML documents.
 * 
 * @author fardellwi
 */
public class ValidationMessageBean implements MessageDrivenBean, MessageListener {

    /**
     * The default location of the schema dir.
     */
    //private static final String SCHEMA_DIR_DEFAULT = "/software/apps/xhibit_2/common/schema";
    
    // Specify location of schemas in the startup properties - we cannot set a default as it changes from environment to environment
    private static final String SCHEMA_DIR_DEFAULT = System.getProperty("ValidationMessageBean.schemaDir");

    // TODO: Lookup schema dir!
    // private static final String SCHEMA_DIR_PROPERTY =
    // "ValidationMessageBean.schemaDir";

    /**
     * Serialization id, increment if class structure changes.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The class's logger.
     */
    private static final Logger log = Logger.getLogger(ValidationMessageBean.class);

    /**
     * Used to access the database.
     */
    private ValidationDatabase database;

    /**
     * Used to validate the xml.
     */
    private ValidationService service;

    /**
     * @see MessageDrivenBean#setMessageDrivenContext(MessageDrivenContext)
     */
    public void setMessageDrivenContext(MessageDrivenContext context) {
    }

    /**
     * Factory method.
     */
    public void ejbCreate() throws CreateException {
        database = new ValidationDatabase();
        // Check the location of the schemas has been configured and is valid
        if (SCHEMA_DIR_DEFAULT == null) {
            System.out.println("The  value for the schema location has not been set in the startup args");
            throw new NullPointerException("Schema location not set");
        }
        System.out.println("Schema location has been defined as "+SCHEMA_DIR_DEFAULT);
        File f = new File(SCHEMA_DIR_DEFAULT);
        if (f.exists() && f.isDirectory()) {
            System.out.println("Schema location has been confirmed as being a valid directory.");
        } else {
            System.out.println("Schema location is not a valid directory.");
        }

        service = new SAXValidationService(new FileEntityResolver(SCHEMA_DIR_DEFAULT));
    }

    /**
     * @see MessageDrivenBean#ejbRemove()
     */
    public void ejbRemove() {
        database = null;
        service = null;
    }

    /**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(final Message message) {
    	long starttime = System.currentTimeMillis();
        try{
        		long validationId = message.getLongProperty("validation_id");
        		String schemaName = message.getStringProperty("schema_name");
        		String xml = ((TextMessage) message).getText();
        		
        		ValidationResult result = service.validate(xml, schemaName);
                
                database.updateValidation(validationId, result.isValid() ? "V" : "F", result.toString());

                if (log.isDebugEnabled()) {
                    log.debug("Validating record " + validationId + " against \"" + schemaName + "\" " + (result.isValid() ? "succeded" : "failed")+ " and took "
                            + (System.currentTimeMillis() - starttime) + "ms.");
                }
        }catch (Exception jmse) {
            log.error("An error occurred validating message ("+
                    jmse);
            jmse.printStackTrace();
            throw new EJBException(jmse);
        }
    }

}
