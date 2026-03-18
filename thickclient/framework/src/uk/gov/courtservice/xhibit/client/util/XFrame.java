package uk.gov.courtservice.xhibit.client.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import uk.gov.courtservice.xhibit.client.util.helpers.ShieldHelper;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class XFrame extends JFrame implements ShieldInterface {
    private final String icon = XHIBITConstant.imageRoot + "XhibitCornerLogo.gif";

    private final ShieldHelper shieldHelper = new ShieldHelper(this);
    
    private final Image transparentIcon = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB_PRE);

    public XFrame() {
        super();
        this.setIconImage(getIcon().getImage());
    }

    /**
     * Raise a glass pane infront of the frame so nothing can be clicked on
     * allowing multi threaded call without worrying about the user clicking on
     * anything
     */
    public void shield() {
        shieldHelper.shield();
    }

    /**
     * Remove glass pane allowing application to continue as normal
     */
    public void unshield() {
        shieldHelper.unshield();
    }

    private ImageIcon getIcon() {
        // java.net.URL url =
        // getClass().getClassLoader().getResource(XHIBITConstant.imageRoot +
        // "XhibitCornerLogo.gif");
        java.net.URL url = getClass().getClassLoader().getResource(icon);
        ImageIcon i = null;

        if (url != null)
            i = new ImageIcon(url);
        else
            i = new ImageIcon(icon);
        return i;
    }
    
    public void setTransparentIcon() {
    	setIconImage(transparentIcon);
    }
 
}