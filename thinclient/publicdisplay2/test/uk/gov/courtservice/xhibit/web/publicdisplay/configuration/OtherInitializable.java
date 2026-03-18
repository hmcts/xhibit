package uk.gov.courtservice.xhibit.web.publicdisplay.configuration;

import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.AbstractInitializable;
import uk.gov.courtservice.xhibit.common.publicdisplay.initialization.InitializationContext;
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
 * @version $Revision: 1.1 $
 */
class OtherInitializable extends AbstractInitializable
{
    private static OtherInitializable singleton = new OtherInitializable();

    /**
     * TODO:
     *
     * @return TODO:
     */
    public static OtherInitializable getInstance()
    {
        return singleton;
    }

    /**
     * TODO:
     *
     * @param context TODO:
     *
     * @throws common.initialization.exceptions.InitializationException TODO:
     */
    protected void doInitialize(InitializationContext context)
        throws InitializationException
    {
        //
    }
}
