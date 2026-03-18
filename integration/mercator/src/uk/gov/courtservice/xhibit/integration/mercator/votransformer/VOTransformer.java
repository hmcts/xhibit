package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

// Court Service

/**
 * 
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: This interface defines what the VOTransformer can do.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Cag Onganer
 * @version 1.0
 */

public interface VOTransformer {

    public Object transformVO(Object valueObject) throws TransformationException;

    /**
     * @param result
     * @return Object
     * @roseuid 3DD38CCE01B1
     */
    public Object transformOutput(Object result) throws OutputTransformationException;

}
