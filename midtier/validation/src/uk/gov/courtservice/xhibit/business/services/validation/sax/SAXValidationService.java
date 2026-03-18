package uk.gov.courtservice.xhibit.business.services.validation.sax;


import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import com.sun.org.apache.xerces.internal.jaxp.SAXParserFactoryImpl;

import uk.gov.courtservice.xhibit.business.services.validation.ValidationException;
import uk.gov.courtservice.xhibit.business.services.validation.ValidationResult;
import uk.gov.courtservice.xhibit.business.services.validation.ValidationService;

/**
 * Simple Service for Validating XML.
 * 
 * @author William Fardell
 */
public class SAXValidationService implements ValidationService {

	private static final Logger log = Logger.getLogger(SAXValidationService.class);
	
	private final EntityResolver entityResolver;
	
	public SAXValidationService(final EntityResolver pEntitiyResolver){
		this.entityResolver = pEntitiyResolver;
	}
	
    public ValidationResult validate(final String xml, final String schemaName) throws ValidationException {
    	log.debug("entered validate method");
    	
    	try {
    		SAXParserFactory factory = new SAXParserFactoryImpl();
			factory.setNamespaceAware(true);
			
			SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
			SAXParser parser = null;
			
			factory.setSchema(schemaFactory.newSchema(new SAXSource(entityResolver.resolveEntity(schemaName, schemaName))));
			parser = factory.newSAXParser();
			
			
			XMLReader reader = parser.getXMLReader();
			ErrorHandlerValidationResult result = new ErrorHandlerValidationResult();
			reader.setErrorHandler(result);
			reader.parse(new InputSource(new StringReader(xml)));
			if(log.isDebugEnabled()){
				log.debug("Valid: "+result.isValid());
				if(!result.isValid()){
					log.debug("Validation Failed: "+result.toString());
				}
			}
        	return result;
        	       	
        } catch (SAXException e) {
            throw new ValidationException("An error occurred validating.", e);
        } catch (IOException e) {
            throw new ValidationException("An error occurred validating.", e);
        } catch (ParserConfigurationException e) {
            throw new ValidationException("An error occurred validating.", e);
        }
    }
}
