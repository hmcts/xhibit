package uk.gov.courtservice.xhibit.web.publicdisplay.imaging;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p/> Title: Servlet used to generate titles for lists.
 * </p>
 * <p/> <p/> Description:
 * </p>
 * <p/> Pass the following parameters
 * <ul>
 * <li><b>text</b> - the text to be displayed on top of the image.</li>
 * <li><b>width</b> - initial sizing for the image, it will get cropped.</li>
 * <li><b>height</b> - height for the image</li>
 * <li><b>fontColor</b> - <i>optional</i>the color of the font.</li>
 * <li><b>x</b> - horizontal offset for the text.</li>
 * <li><b>y</b> - vertical offset for the textt.</li>
 * <li><b>fontSize</b> - size of the font to use.</li>
 * </ul>
 * <p/> Copyright: Copyright (c) 2003
 * </p>
 * <p/> <p/> Company: Electronic Data Systems
 * </p>
 * 
 * @author Neil Ellis
 * @version $Revision: 1.10 $
 */
public class HeadingServlet extends AbstractGraphicsServlet {
    private static final int SHADOW_OFFSET = 2;

    // TODO: Sort all this out, it's a bit messy - Neil Ellis

    public void doImage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        int width = Integer.parseInt(req.getParameter("width"));
        int height = Integer.parseInt(req.getParameter("height"));
        int x = Integer.parseInt(req.getParameter("x"));
        int y = Integer.parseInt(req.getParameter("y"));
        int fontSize = Integer.parseInt(req.getParameter("fontSize"));
        String text = req.getParameter("text");
        BufferedImage sourceImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = sourceImage.createGraphics();
        Font font = new Font("Arial", Font.BOLD, fontSize);
        g.setFont(font);
        ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);
        g.setColor(Colors.TRANSLUCENT_GREY);
        g.drawString(text, x - SHADOW_OFFSET, y + SHADOW_OFFSET);
        g.setColor(getFontColor(req));
        g.drawString(text, x, y);
        g.dispose();
        res.setContentType("image/jpeg");
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(res.getOutputStream());
        encoder.encode(sourceImage.getSubimage(0, 0, (int) font.getStringBounds(text,
                ((Graphics2D) g).getFontRenderContext()).getWidth()
                + SHADOW_OFFSET * 2, height - 1));

    }

    private Color getFontColor(HttpServletRequest req) {
        String colorStr = req.getParameter("fontColor");
        Color fontColor;
        if (colorStr != null) {
            fontColor = new Color(Integer.parseInt(colorStr, 16));
        } else {
            fontColor = Colors.TEXT_FOREGROUND;
        }
        return fontColor;
    }

}
