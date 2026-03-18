package uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub;

import uk.gov.courtservice.xhibit.web.publicdisplay.storage.priv.impl.FileStorer;

/**
 * <p>
 * Title: The Storer Factory provides us with instances of the current Storer.
 * </p>
 * 
 * <p>
 * Description: The Storer Factory is used to provide an instance of the current
 * Storer. This allows for the switching of the Storer in future implementations
 * with the least code changes outside of the Storer itself it also encourages
 * the good habit of working to an interface/contract.
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
 * @version $Revision: 1.5 $
 */
public class StorerFactory {
    /**
     * Returns an instance of a Storer.
     * 
     * @return an instance of a Storer.
     */
    public static Storer getInstance() {
        return FileStorer.getInstance();
    }
}
