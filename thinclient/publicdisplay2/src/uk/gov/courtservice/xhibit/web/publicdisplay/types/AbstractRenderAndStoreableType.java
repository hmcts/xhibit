package uk.gov.courtservice.xhibit.web.publicdisplay.types;

import java.io.Serializable;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.common.publicdisplay.data.Data;
import uk.gov.courtservice.xhibit.common.publicdisplay.types.uri.Identifiable;
import uk.gov.courtservice.xhibit.common.publicdisplay.util.Debuggable;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderable;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.Renderer;
import uk.gov.courtservice.xhibit.web.publicdisplay.rendering.RendererFactory;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storeable;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StoredObject;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.Storer;
import uk.gov.courtservice.xhibit.web.publicdisplay.storage.pub.StorerFactory;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.Createable;
import uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.Removeable;

/**
 * <p/> Title: An abstract convenience class for objects that can be both
 * rendererd and stored.
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
public abstract class AbstractRenderAndStoreableType implements Renderable, Storeable, Identifiable, Serializable,
        Debuggable, Createable, Removeable {
    private static final Logger log = CSServices.getLogger(AbstractRenderAndStoreableType.class);

    protected Data data;

    private Renderer renderer = RendererFactory.getInstanceForType(this);

    private Storer storer = StorerFactory.getInstance();

    private String renderedString;

    private boolean beenStored;

    private static final int MAXIMUM_DEBUG_TEXT = 80;

    /**
     * Sets the rendered string.
     * 
     * @param renderedString
     *            the rendered version of the document as a String.
     * @pre renderedString != null
     */
    public void setRenderedString(final String renderedString) {
        this.renderedString = renderedString;
    }

    /**
     * Gets the string containing the rendered version of this object.
     * 
     * @return the rendered version as a String.
     */
    public String getRenderedString() {
        return renderedString;
    }

    /**
     * @pre storer != null
     * @see uk.gov.courtservice.xhibit.web.publicdisplay.workflow.pub.Removeable#remove()
     */
    public void remove() {
        log.info("remove()");
        debug(log);
        storer.remove(this);
        debug(log);
        log.info("remove() finished.");
    }

    /**
     * Render this object in the context supplied.
     * 
     * @pre renderer != null
     */
    public void render() {
        log.info("render().");
        debug(log);
        renderer.render(this);
        debug(log);
        log.info("render() finished.");
    }

    /**
     * Obtain a refrence to the stored version of this object.
     * 
     * @return the stored reference.
     * @pre storer != null
     */
    public StoredObject retrieve() {
        log.info("retrieve()");
        debug(log);
        StoredObject storedObject = storer.retrieve(getUri());
        debug(log);
        log.info("retrieve() finished.");

        return storedObject;
    }

    /**
     * Store this object in the context supplied.
     * 
     * @pre storer != null
     */
    public void store() {
        log.info("store()");
        debug(log);
        storer.store(this);
        beenStored = true;
        debug(log);
        log.info("store() finished.");
    }

    /**
     * Log debugging info for this object.
     */
    public void debug(Logger logger) {
        if (!logger.isDebugEnabled()) {
            return;
        }

        logger.debug("Debugging: " + this.getClass());

        if (data != null) {
            logger.debug("This render and storeable has data.");
        }

        if (renderedString != null) {
            logger.debug("This renderable and storeable object has been rendered.");

            if (renderedString.length() < MAXIMUM_DEBUG_TEXT) {
                logger.debug("renderedString= '" + renderedString.length() + "'");
            } else {
                logger.debug("renderedString= '" + renderedString.substring(0, MAXIMUM_DEBUG_TEXT) + "...'");
            }
        }

        if (beenStored) {
            logger.debug("This renderable and storeable object has been stored.");
        }
    }
}
