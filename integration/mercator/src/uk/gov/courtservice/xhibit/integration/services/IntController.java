package uk.gov.courtservice.xhibit.integration.services;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.integration.mercator.MercatorWrapper;
import uk.gov.courtservice.xhibit.integration.mercator.MercatorWrapperFactory;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformerFactory;

/**
 * 
 * <p>
 * Title: IntController
 * </p>
 * <p>
 * Description: Abstract Parent class for Mercator related Integration
 * Controllers
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @author Marie Holmberg
 * @version 1.0
 * @version 1.1 (GJS) Updated creation of mercatorWrapper to use the factory
 *          which will create the HTTP, EJB or STUB versions depending on the
 *          mercator.wrapper.type property
 */
public abstract class IntController {

    private MercatorWrapper mercator = null;

    private static Logger log = CSServices.getLogger(IntController.class);

    /**
     * @roseuid 3DDBAEC6025F
     */
    public IntController() {
        mercator = MercatorWrapperFactory.getInstance().getMercatorWrapper();
    }

    /**
     * This method executes a mercator map. It should be used when a value
     * object will be passed in to mercator since transformation is required
     * before.
     * 
     * @param mapName
     *            The Mercator map to execute.
     * @param vo
     *            The value object to pass to Mercator
     * @return Mercator return object.
     * @throws MercatorException
     * @throws TransformationException
     * @throws OutputTransformationException
     * @roseuid 3DDB69D90225
     */
    public Object executeUpdate(String mapName, Object vo) throws MercatorException, TransformationException,
            OutputTransformationException {
        VOTransformer vot = VOTransformerFactory.getInstance().createVOTransformer(vo.getClass());

        if (log.isDebugEnabled()) {
            log.debug("executeUpdate entered. Map Name (String mapName, CSValueObject vo) = " + mapName
                    + " VO Class = " + vo.getClass().getName() + " VOT Class = " + vot.getClass().getName());
        }

        Object mvo = vot.transformVO(vo);

        if (log.isDebugEnabled()) {
            log.debug("transformed vo = " + mvo.toString());
        }

        Object o = mercator.runMap(mapName, mvo);

        log.debug("executeUpdate exited.");
        return vot.transformOutput(o);
    }

    /**
     * This will be used to execute a Mercator map. This method should be used
     * when an object needs to be passed in to Mercator and it is not a
     * ValueObject. The object that will be passed in can be of any wrapper
     * number type as long as Mercator recognises the type.
     * 
     * @param mapName
     *            The Mercator map to execute.
     * @param obj
     *            The object to passed to Mercator
     * @return Mercator return object
     * @throws MercatorException
     */
    public Object executeUpdate(String mapName, Number obj) throws MercatorException {
        log.debug("executeUpdate entered. Map Name (String mapName, Object obj) = " + mapName + " Object Class ="
                + obj.getClass().getName());

        Object o = mercator.runMap(mapName, obj);

        log.debug("executeUpdate exited.");
        return o;
    }
}