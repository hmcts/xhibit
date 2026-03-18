package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public class XToolbarToggleButton extends JToggleButton {
    private final Dimension xbIconDimension = new Dimension(26, 26);

    private final Dimension xbTextDimension = new Dimension(100, 20);

    private final Insets xbuttonInsets = new Insets(0, 0, 0, 0);

    public static final int xbIcon = 1;

    public static final int xbText = 2;

    private Dimension requiredDimension;

    private int requestedButton;

    public XToolbarToggleButton() {
        this(xbIcon);
    }

    public XToolbarToggleButton(int buttonType) {
        super();
        requestedButton = buttonType;
        switch (buttonType) {
        case (xbIcon):
            requiredDimension = xbIconDimension;
            break;
        case (xbText):
            requiredDimension = xbTextDimension;
            this.setHorizontalAlignment(SwingConstants.LEFT);
            break;
        case (xbIcon + xbText):
            requiredDimension = new Dimension(xbIconDimension.width + xbTextDimension.width,
                    xbIconDimension.height > xbTextDimension.height ? xbIconDimension.height : xbTextDimension.height);
            break;
        }
        setupButton();
    }

    private void setupButton() {
        setPreferredSize(requiredDimension);
        setMinimumSize(requiredDimension);
        setMaximumSize(requiredDimension);
        setMargin(xbuttonInsets);
        // setBorder(BorderFactory.createEmptyBorder());
        setFocusPainted(false);
        // addMouseListener(this);
    }

    /**
     * Overrides setAction in JButton so that no text is displayed on the button
     * in a toolbar.
     * 
     * @param action
     *            A toolbar action (that implements javax.swing.Action) that is
     *            performed when the button is clicked.
     */
    public void setAction(Action action) {
        super.setAction(action);
        switch (requestedButton) {
        case (xbIcon):
            setText("");
            break;
        case (xbText):
            setIcon(null);
            break;
        case (xbIcon + xbText):
            // Nothing
            break;
        }
    }

    /**
     * Overrides isFocusTraversable in AbstractButton so that this
     * XToolbarButton can not receive the focus by the user with the TAB key.
     * 
     * @return false.
     */
    public boolean isFocusTraversable() {
        return false;
    }

}