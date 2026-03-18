package uk.gov.courtservice.xhibit.web.publicdisplay.rendering.impl;

import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.compiled.RotationSetCompiledRendererDelegate;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.exceptions.RenderingException;
import uk.gov.courtservice.xhibit.web.publicdisplay.types.rotationset.DisplayRotationSet;

/**
 * <p/> Title: RotationSetCompiledRenderer is the renderer supplied by the
 * RenderFactory for a rotation set if template rendering is NOT being used.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell
 * @version $Revision: 1.3 $
 * @see uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer
 * @see uk.gov.courtservice.xhibit.web.publicdisplay.rendering.RendererFactory#getInstanceForType(uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable)
 */
public class RotationSetCompiledRenderer implements Renderer {
    private final RotationSetCompiledRendererDelegate delegate = new RotationSetCompiledRendererDelegate();

    /**
     * Render the renderable document
     * 
     * @param renderable
     *            the rotation set (cast to Renderable) to render.
     * 
     * @throws RenderingException
     *             if the renderer fails to render.
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer#render(uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable)
     */
    public void render(final Renderable renderable) throws RenderingException {
        renderRotationSet((DisplayRotationSet) renderable);
    }

    /**
     * Render the rotation set document
     * 
     * @param renderable
     *            the rotation set to render.
     * 
     * @throws RenderingException
     *             if the renderer fails to render.
     */
    public void renderRotationSet(final DisplayRotationSet rotationSet) throws RenderingException {
        final String result = delegate.getDisplayRotationSetHtml(rotationSet);
        rotationSet.setRenderedString(result);
    }
}
