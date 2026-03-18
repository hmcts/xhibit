package uk.gov.courtservice.framework.xml.transform;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.URIResolver;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Allows the values of a template to be adapted.
 * 
 * @author Will Fardell
 */
public class URIResolverTemplatesAdapter extends TemplatesAdapter {
    private static Logger log = CSServices.getLogger(URIResolverTemplatesAdapter.class);

    private final URIResolver resolver;

    /**
     * Construct a new adapter
     * 
     * @author Will Fardell
     */
    public URIResolverTemplatesAdapter(Templates delegate, URIResolver resolver) {
        super(delegate);
        this.resolver = resolver;
    }

    /**
     * Overriden by child classes to configure the transformer
     */
    protected void configureTransformer(Transformer transformer) throws TransformerConfigurationException {
        if (log.isDebugEnabled()) {
            log.debug("Changeing resolver from " + transformer.getURIResolver() + " to revolver " + resolver
                    + " for transformer " + transformer + " .");
        }
        transformer.setURIResolver(resolver);
    }
}
