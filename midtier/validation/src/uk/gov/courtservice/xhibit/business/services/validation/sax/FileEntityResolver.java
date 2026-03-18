package uk.gov.courtservice.xhibit.business.services.validation.sax;

import java.io.File;
import java.net.MalformedURLException;

import org.apache.log4j.Logger;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * Resolves the entities name to a file in the entities dir. This uses the last
 * part of the url following a / or a space! The later is to support the current
 * names of schemas.
 */
public class FileEntityResolver implements EntityResolver {
    /**
     * The class's logger.
     */
    private static final Logger log = Logger.getLogger(FileEntityResolver.class);
    
	/**
	 * The dir containing the entitites.
	 */
	public final File entityDir;
	
	/**
	 * Construct a new instance to resolve entities to files in the specified
	 * dir.
	 * 
	 * @param pEntityDir
	 *            the dir containing the entitites.
	 */
	public FileEntityResolver(final String pEntityDir) {
		this(pEntityDir == null ? (File)null : new File(pEntityDir));
	}
	
	/**
	 * Construct a new instance to resolve entities to files in the specified
	 * dir.
	 * 
	 * @param pEntityDir
	 *            the dir containing the entitites.
	 */
	public FileEntityResolver(final File pEntityDir) {
		if(pEntityDir == null) {
			throw new IllegalArgumentException("pEntityDir: null");
		}
		entityDir = pEntityDir;
	}
	
	
    /**
	 * Resolve the entity.
	 * 
	 * @param publicId
	 *            Public id
	 * @param systemId
	 *            System id
	 */
    public InputSource resolveEntity(String publicId, String systemId) {
        String newSystemId = resolveEntityUrl(systemId);
        if(newSystemId != null) {
            InputSource inputSource = new InputSource(newSystemId);
            inputSource.setPublicId(publicId);
            return inputSource;            
        }
        return null;
    }
    
    /**
     * 
     * Resolve the entity URL.
     * 
     * @param publicId
     * 
     */
    protected String resolveEntityUrl(final String publicId) {
        File entityFile = resolveEntityFile(publicId);
        if(entityFile != null) {
            try {                
                String entityUrl = entityFile.toURL().toExternalForm();
                if(log.isDebugEnabled()) {
                    log.debug("Resolved \"" + publicId + "\" to url \"" + entityUrl + "\".");
                }
                return entityUrl;
            } catch (final MalformedURLException e) {
                log.warn("An error ocured creating URL for file \"" + entityFile.getAbsolutePath() + "\".", e);
                return null;
            }
        }
        return null;
    }   
    
    /**
     * 
     * Resolve the entity.
     * 
     * @param publicId
     * 
     */
    protected File resolveEntityFile(final String publicId) {
        if(publicId == null) {
            throw new IllegalArgumentException("systemId: null");
        }
        // Determine the seperator index
        int spaceIndex = publicId.lastIndexOf(' ');        
        int forwardSlashIndex = publicId.lastIndexOf('/');            
        int backwardSlashIndex = publicId.lastIndexOf('\\');        
        int seperatorIndex = Math.max(Math.max(spaceIndex, forwardSlashIndex), backwardSlashIndex);
        
        // Determine the name
        String entityName;
        if(seperatorIndex > 0) {
            entityName = publicId.substring(seperatorIndex + 1);
        } else {
            entityName = publicId;
        }
        
        // Create and return the file
        File entityFile = new File(entityDir, entityName).getAbsoluteFile();
        if(log.isDebugEnabled()) {
            log.debug("Resolved \"" + publicId + "\" to file \"" + entityFile + "\".");
        }
    	return entityFile;
    }
}
