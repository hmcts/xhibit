package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.net.URL;

import javax.swing.GrayFilter;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: Factory for creating objects
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0
 */

public class DirectionsFactory {
    public static ImageIcon rbDisabled = DirectionsFactory.getIcon("rbDisabled.gif");

    public static ImageIcon rbDisabledSelected = DirectionsFactory.getIcon("rbDisabledSelected.gif");

    public static ImageIcon chkDisabled = DirectionsFactory.getIcon("chkDisabled.gif");

    public static ImageIcon chkDisabledSelected = DirectionsFactory.getIcon("chkDisabledSelected.gif");

    private DirectionsFactory() {
    }

    public static JLabel createWarningLabel(String toolTipText) {
        JLabel warningLabel = new JLabel(getIcon("twarning.gif"));
        if (toolTipText != null)
            warningLabel.setToolTipText(toolTipText);
        warningLabel.setVisible(false);
        return warningLabel;
    }

    public static ImageIcon getIcon(String iconName) {
        URL u = DirectionsFactory.class.getClassLoader().getResource(XHIBITConstant.imageRoot + iconName);
        ImageIcon icon = new ImageIcon(u);
        return icon;
    }

    public static ImageIcon createDisabledIcon(ImageIcon icon) {
        return new ImageIcon(GrayFilter.createDisabledImage(((ImageIcon) icon).getImage()));
    }
}