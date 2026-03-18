package uk.gov.courtservice.xhibit.web.publicdisplay.rendering;

import java.util.HashMap;

import org.apache.log4j.Logger;

import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.impl.DisplayDocumentCompiledRenderer;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.impl.RotationSetCompiledRenderer;

/**
 * <p/> Title: The Factory class used to obtain a Renderer.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.7 $
 * @see Renderer
 */
public class RendererFactory {
    private static Logger log = Logger.getLogger(RendererFactory.class);

    private static HashMap _rendererInstances = new HashMap();

    // Initialisation
    static {
        _rendererInstances.put("document", new DisplayDocumentCompiledRenderer());
        _rendererInstances.put("rotationset", new RotationSetCompiledRenderer());

    }

    /**
     * Obtain a renderer for a particular Renderable.
     * 
     * @param type
     *            a Renderable document.
     * 
     * @return a renderer.
     * 
     * @pre type instanceof DisplayDocument || type instanceof
     *      DisplayRotationSet
     * @post type instanceof DisplayDocument implies return instanceof
     *       DisplayDocumentRenderer
     * @post type instanceof DisplayRotationSet implies return instanceof
     *       RotationSetRenderer
     * @post return != null
     */
    public static Renderer getInstanceForType(final Renderable type) {
        final Renderer renderer = (Renderer) _rendererInstances.get(type.getRenderableType());
        return renderer;
    }
}
