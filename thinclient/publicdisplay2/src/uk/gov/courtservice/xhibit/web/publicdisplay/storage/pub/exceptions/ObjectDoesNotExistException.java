package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions;

import java.io.File;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.PublicDisplayRuntimeException;
import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Warning;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.5 $
 */
public class ObjectDoesNotExistException extends PublicDisplayRuntimeException implements Warning {
    /**
     * Creates a new ObjectDoesNotExistException object.
     * 
     * @param uri
     *            the uri of the object we could not file.
     * @param file
     *            the FIle we were using when trying to obtain the object.
     */
    public ObjectDoesNotExistException(AbstractURI uri, File file) {
        super("Could not find the file described by the uri '" + uri
                + "' as it does not exist. The file name used was '" + file.getAbsolutePath() + "'.");
    }
}
