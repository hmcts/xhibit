package uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv;

import java.io.File;

import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.AbstractURI;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.StoreException;

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
 * @version $Revision: 1.4 $
 */
public class FileStoreException extends StoreException {
    // --Recycle Bin (03/12/03 17:45): private static final
    // org.apache.log4j.Logger log =
    // CSServices.getLogger(FileStoreException.class);

    /**
     * Could not store an object described by the uri as the file supplied with
     * a root cause.
     * 
     * @param uri
     *            the URI identifying the object being stored.
     * @param documentFile
     *            the file describing the destination of the object being
     *            stored.
     * @param cause
     *            the nested Throwable cause.
     */
    public FileStoreException(final AbstractURI uri, final File documentFile, final Throwable cause) {
        super("Could not store " + uri + " as file '" + documentFile.getName() + " at "
                + documentFile.getAbsolutePath(), cause);
    }

    /**
     * Could not store an object described by the uri, supplied with a root
     * cause.
     * 
     * @param uri
     *            the URI identifying the object being stored.
     * @param cause
     *            the nested Throwable cause.
     */
    public FileStoreException(final AbstractURI uri, final Throwable cause) {
        super("Could not store " + uri + ".", cause);
    }

    /**
     * Could not store an object supplied with a root cause.
     * 
     * @param message
     *            an associated messgage.
     * @param cause
     *            the nested Throwable cause.
     */
    public FileStoreException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
