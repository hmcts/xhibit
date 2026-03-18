package uk.gov.courtservice.xhibit.web.publicdisplay.imaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.SingleThreadModel;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.web.publicdisplay.imaging.exceptions.ImageLoadingException;
import uk.gov.courtservice.xhibit.web.publicdisplay.initialization.servlet.InitializationService;

/**
 * <p/> Title: Abstract Graphics Servlet
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.24 $
 */
public abstract class AbstractGraphicsServlet extends HttpServlet implements SingleThreadModel, ImageObserver {

    static final int AUTOMATIC_WIDTH = -1;

    private static final Logger log = CSServices.getLogger(AbstractGraphicsServlet.class);

    private static final int IMAGE_DRAW_TIMEOUT = 500;

    private static long lastModifiedTime;

    /**
     * This method is used by the standard servlet service() method to ascertain
     * whether the doGet() method should be called or not on a GET request. If
     * the lastModified time is more recent thn the If-Modified-Since header
     * attribute then doGet() will be called, otherwise the servlet will return
     * a 304 Not Modified response to the browser.<br/> <p/> In the case of our
     * graphics our default behaviour is to return a last modified time of 0
     * (Jan 1970) while starting up, so we avoid doing any heavy work during
     * system initialization. If however the browser does not have a cached copy
     * then doGet() will still get called. Once the system is initialized we
     * return the current time and then always return that time for the life of
     * the JVM. This means that we only serve an image to a given browser once
     * in the JVMs lifetime. Our images are not dynamically changing so this
     * suffices.
     * 
     * @param request
     *            the HttpServletRequest
     * 
     * @return the modified date as specifed above in milliseconds since 1970 as
     *         a long value.
     */
    protected long getLastModified(HttpServletRequest request) {
        // This is a hack to make sure that images are not serverd before
        // the service is initialized. This is to reduce load on the servers.
        if (lastModifiedTime == 0 && InitializationService.getInstance().isInitialized()) {
            lastModifiedTime = System.currentTimeMillis();
        }

        if (log.isDebugEnabled()) {
            String header = request.getHeader("If-Modified-Since");
            log.debug("If modified since " + header);
            log.debug("Returing fake last modified time of " + new Date(lastModifiedTime));
        }

        return lastModifiedTime;
    }

    /**
     * Standard support for doGet(), we only support the GET method. This calls
     * the abstract method doImage() which does all the work, exceptions are
     * caught and rethrown as a ServletException.
     * 
     * @see super#doGet(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public final void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException {
        try {
            doImage(req, res);
        } catch (Throwable t) {
            log.error("Exception thrown in Image Servlet.", t);
            throw new ServletException(t);
        }
    }

    /**
     * The abstract business method.
     * 
     * @param req
     *            Servlet Request
     * @param res
     *            Servlet Response
     * 
     * @throws ServletException
     * @throws IOException
     */
    protected abstract void doImage(HttpServletRequest req, HttpServletResponse res) throws ServletException,
            IOException;

    /**
     * Load an Image from the given url, waiting until the entire image is
     * loaded.
     * 
     * @param url
     *            the URL to use to find the image.
     * 
     * @return the image at the URL
     * 
     * @throws ImageLoadingException
     *             if the image could not be loaded.
     */
    Image obtainImage(final URL url) throws ImageLoadingException {
        ImageLoader loader = new ImageLoader(url, true);
        if (loader.isImageLoaded() != true) {
            log
                    .fatal("Image could not be loaded, please make sure the frame buffer is running and xhost + has been executed, ie this process can access the local x windows process.");
            throw new ImageLoadingException(url);
        }
        return loader.getImage();
    }

    /**
     * Write text on to an image.
     * 
     * @param image
     *            the image to write on top of.
     * @param text
     *            the text to write.
     * @param x
     *            offset from the left.
     * @param y
     *            offset from the top.
     * @param size
     *            the font size.
     * @param width
     *            the width of the produced image.
     * @param fontColor
     *            the color of the text.
     * 
     * @return the produced image.
     */
    BufferedImage writeTextOnImage(final Image image, final String text, final int x, final int y, final int size,
            int width, Color fontColor) {
        width = (width == AUTOMATIC_WIDTH) ? image.getWidth(null) : width;
        int height = image.getHeight(null);

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();

        // Draw Background
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, image.getHeight(null));

        // Draw Image
        drawImageWithWait(g, image);

        Font font = new Font("Arial", Font.PLAIN, size);
        FontMetrics metrics = g.getFontMetrics(font);
        g.setFont(font);

        // Reposition text to center if position is negative
        int sx;

        if (x < 0) {
            int textWidth = metrics.stringWidth(text);
            log.debug("writeTextOnImage textWidth: " + textWidth);
            sx = (int) ((width - textWidth) / 2.0);
            log.debug("writeTextOnImage sx: " + sx);
        } else {
            sx = x;
        }

        int sy;
        if (y < 0) {
            int textHeight = metrics.getAscent();
            log.debug("writeTextOnImage textHeight: " + textHeight);
            sy = (int) (height - ((height - textHeight) / 2.0));
            log.debug("writeTextOnImage sy: " + sy);
        } else {
            sy = y;
        }

        // Draw Text
        g.setColor(Colors.TRANSLUCENT_WHITE);
        g.drawString(text, sx - 2, sy + 2);
        g.setColor(fontColor);
        g.drawString(text, sx, sy);
        g.dispose();

        return bufferedImage;
    }

    /**
     * Draws an Image onto a Graphics object at the top left corner and waits
     * until the image is drawn before returning.
     * 
     * @param g
     *            the Graphics object.
     * @param image
     *            the Image to be drawn.
     */
    private void drawImageWithWait(Graphics g, final Image image) {
        for (boolean done = false; !done; done = g.drawImage(image, 0, 0, this)) {
            try {
                log.info("Waiting on an image to be drawn.");
                synchronized (this) {
                    wait(IMAGE_DRAW_TIMEOUT);
                }
            } catch (InterruptedException e) {
                log.warn(e);
            }
        }
    }

    /**
     * Callback to let us know when an image has been completely drawn.
     */
    public synchronized boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
        if ((infoflags & ALLBITS) != 0) {
            log.info("Image update received and all bits are loaded.");
            notifyAll();
        }
        return true;
    }
}
