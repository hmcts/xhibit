package uk.gov.courtservice.xhibit.common.publicdisplay.initialization;

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
public interface Initializable {
    /**
     * TODO:
     * 
     * @param context
     *            TODO:
     * 
     * @throws InitializationException
     *             TODO:
     */
    public void initialize(InitializationContext context) throws InitializationException;

    void checkInitialized();
}
