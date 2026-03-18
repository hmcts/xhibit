package uk.gov.courtservice.xhibit.web.publicdisplay.imaging;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p/> Title: THe abstract superclass for any servlet that modifies images.
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
 */
public abstract class AbstractImageServlet extends AbstractGraphicsServlet {
    private static final Logger log = CSServices.getLogger(AbstractImageServlet.class);

    public final void doImage(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        try {

            log.debug(getServletContext().getRealPath(request.getPathInfo()));
            URL url = this.getServletContext().getResource("/WEB-INF" + request.getPathInfo());
            Image sourceImage = obtainImage(url);

            // Do our custim processing on the image
            Image resultImage = processImage(sourceImage, request);

            // Output the finished image straight to the response as a JPEG!
            response.setHeader("Cache-Control", "must-revalidate");
            response.setContentType("image/jpeg");
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(response.getOutputStream());
            encoder.encode((BufferedImage) resultImage);
        } catch (Exception e) {
            log.fatal(e);
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * The business method.
     */
    protected abstract Image processImage(Image sourceImage, HttpServletRequest req);

}
