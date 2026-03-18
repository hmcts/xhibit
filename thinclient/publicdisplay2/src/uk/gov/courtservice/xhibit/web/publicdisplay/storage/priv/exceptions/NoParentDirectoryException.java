package uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.exceptions;

import java.io.File;

import uk.gov.courtservice.xhibit.common.publicdisplay.exceptions.Fatal;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.exceptions.StoreException;

/**
 * <p>
 * Title:
 * </p>
 * 
 * <p>
 * Description:
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.4 $
 */
public class NoParentDirectoryException extends StoreException implements Fatal {
    /**
     * Creates a new NoParentDirectoryException object.
     * 
     * @param file
     *            the File which we could not obtain a parent directory for.
     */
    public NoParentDirectoryException(File file) {
        super("Could not obtain parent directory for '" + file.getAbsolutePath() + "'.");
    }
}
