package uk.gov.courtservice.xhibit.common.publicdisplay.initialization;

import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions.FailedToInitializeException;
import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.exceptions.InitializationException;

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
 * @version $Revision: 1.3 $
 */
public abstract class AbstractInitializable implements Initializable {
    private boolean initialized;

    /**
     * TODO:
     */
    public void checkInitialized() {
        if (!initialized) {
            throw new InitializationException("Not initialised.");
        }
    }

    /**
     * TODO:
     * 
     * @param context
     *            TODO:
     * 
     * @throws InitializationException
     *             TODO:
     * @throws FailedToInitializeException
     *             TODO:
     */
    public void initialize(InitializationContext context) throws InitializationException {
        try {
            doInitialize(context);
        } catch (Exception e) {
            throw new FailedToInitializeException(this, e);
        }

        initialized = true;
    }

    /**
     * TODO:
     * 
     * @param context
     *            TODO:
     * 
     * @throws Exception
     *             TODO:
     */
    protected abstract void doInitialize(InitializationContext context) throws Exception;
}
