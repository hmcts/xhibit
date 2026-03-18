package uk.gov.courtservice.xhibit.web.publicdisplay.rendering;

import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.exceptions.RenderingException;

/**
 * <p/> Title: A class that is capable of rendering implemnets this interface.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.6 $
 */
public interface Renderer {

    public static final String BASE_URL_PROPERTY = "publicdisplay.web.base_url";

    /**
     * Renders the renderable object passed to it.
     * 
     * @pre renderable != null
     * @pre renderable.getUri() != null
     */
    void render(Renderable renderable) throws RenderingException;

}
