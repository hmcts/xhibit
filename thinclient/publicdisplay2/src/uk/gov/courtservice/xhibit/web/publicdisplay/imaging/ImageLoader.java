package uk.gov.courtservice.xhibit.web.publicdisplay.imaging;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p/> Title:
 * </p>
 * <p/> <p/> Description: <p/> The ImageLoader class is responsible for loading
 * images from a supplied URL, optionally retrieving them from the cache. The
 * ImageLoader will effectively block the curent thread until the image has been
 * fully loaded. If the image is not loaded within
 * <code>IMAGE_LOAD_TIMEOUT</code> milliseconds then the thread continues
 * execution and the return value of isImageLoaded() will be false until the
 * image completes loading. If isImageLoaded() is false then the return value of
 * getImage() is undefined.
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.7 $
 */
public class ImageLoader implements ImageObserver {
    private static final Logger log = CSServices.getLogger(ImageLoader.class);

    private static final int IMAGE_LOAD_TIMEOUT = 500; // (milliseconds that

    // is)

    private static final int MAX_IMAGES_IN_CACHE = 50;

    // Our internal image cache that is potentially subject to garbage
    // collection.
    private static Map imageCache = Collections.synchronizedMap(new HashMap(MAX_IMAGES_IN_CACHE));

    private Image image;

    private boolean imageLoaded;

    /**
     * Attempt to load an image at the URL supplied, if the image can't be
     * loaded within IMAGE_LOAD_TIMEOUT then return null otherwise return the
     * loaded image. If the useCaching parameter is set then use the internal
     * cache were appropriate.
     * 
     * @param url
     *            the URL at which the image can be found.
     * @param useCaching
     *            should we use the in built caching.
     */
    public ImageLoader(URL url, boolean useCaching) {
        Image resultImage = load(url, useCaching);
        this.image = resultImage;
    }

    /**
     * Attempt to load an image at the URL supplied, if the image can't be
     * loaded within IMAGE_LOAD_TIMEOUT then return null otherwise return the
     * loaded image. If the useCaching parameter is set then use the internal
     * cache were appropriate.
     * 
     * @param url
     *            the URL at which the image can be found.
     * @param useCaching
     *            should we use the in built caching.
     * @return the loaded image or null if it wasn't loaded.
     */
    private Image load(URL url, boolean useCaching) {
        // Note the use of soft references to help reduce memory consumption
        // before an out of memory error occurs the GC will collect all
        // SoftReferences
        SoftReference cachedImageReference = (SoftReference) imageCache.get(url);
        Image cachedImage = null;
        if (cachedImageReference != null) {
            cachedImage = (Image) cachedImageReference.get();
        }
        Image resultImage = null;
        if (cachedImage == null || useCaching == false) {
            if (log.isDebugEnabled()) {
                log.debug("Retrieving image for '" + url + "'.");
            }
            resultImage = Toolkit.getDefaultToolkit().createImage(url);
            if (resultImage.getHeight(this) == -1) {
                if (log.isDebugEnabled()) {
                    log.debug("Waiting to load image for ... " + url);
                }
                synchronized (this) {
                    try {
                        wait(IMAGE_LOAD_TIMEOUT);
                    } catch (InterruptedException e) {
                        log.warn(e);
                    }
                }
            } else {
                setImageLoaded(true);
            }
            if (imageCache.size() < MAX_IMAGES_IN_CACHE && useCaching) {
                log.debug("Image placed in cache.");
                imageCache.put(url, new SoftReference(resultImage));
            }
        } else {
            if (log.isDebugEnabled()) {
                log.debug("Using cached image for '" + url + "'.");
            }
            setImageLoaded(true);
            resultImage = cachedImage;
        }
        return resultImage;
    }

    /**
     * @see java.awt.image.ImageObserver#imageUpdate(java.awt.Image, int, int,
     *      int, int, int)
     */
    public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        log.debug("Image update infoFlags= " + infoflags);
        if ((infoflags & ALLBITS) != 0) {
            log.info("Image update received and all bits are loaded.");
            notifyAll();
            setImageLoaded(true);
        }
        return true;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setImageLoaded(boolean imageLoaded) {
        this.imageLoaded = imageLoaded;
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }
}
