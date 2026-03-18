package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * <p>
 * Title: Button to be used in toolbars in the XHIBIT 2 application.
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Stopped the mouse over for disabled buttons. <br>
 * Allowed for icon/text or both.
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */

public class XToolbarButton extends JButton implements MouseListener {
    private final Dimension xbIconDimension = new Dimension(26, 26);

    private final Dimension xbTextDimension = new Dimension(100, 20);

    private final Insets xbuttonInsets = new Insets(0, 0, 0, 0);

    public static final int xbIcon = 1;

    public static final int xbText = 2;

    protected Border borderRaised;

    protected Border borderLowered;

    protected Border borderNone;

    private Dimension requiredDimension;

    private int requestedButton;

    /**
     * Creates a default icon button for a toolbar.
     */
    public XToolbarButton() {
        this(xbIcon);
    }

    public XToolbarButton(int buttonType) {
        super();

        borderRaised = BorderFactory.createBevelBorder(BevelBorder.RAISED, SystemColor.controlLtHighlight,
                SystemColor.controlHighlight, SystemColor.controlShadow, SystemColor.control);
        borderLowered = BorderFactory.createBevelBorder(BevelBorder.LOWERED, SystemColor.controlLtHighlight,
                SystemColor.controlHighlight, SystemColor.controlShadow, SystemColor.control);
        borderNone = BorderFactory.createEmptyBorder();

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
        setBorder(borderNone);
        setFocusPainted(false);
        addMouseListener(this);
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

    /**
     * MouseListener implementaion not used here.
     * 
     * @param e
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * MouseListener implementaion that sets a raised border for this
     * XToolbarButton when the user moves the mouse over it.
     * 
     * @param e
     */
    public void mouseEntered(MouseEvent e) {
        if (getAction().isEnabled()) {
            setBorder(borderRaised);
        }
    }

    /**
     * MouseListener implementaion that sets an empty border for this
     * XToolbarButton when the user moves the mouse off it.
     * 
     * @param e
     */
    public void mouseExited(MouseEvent e) {
        setBorder(borderNone);
    }

    /**
     * MouseListener implementaion that sets a lowered border for this
     * XToolbarButton when the user presses a mouse button over it.
     * 
     * @param e
     */
    public void mousePressed(MouseEvent e) {
        if (getAction().isEnabled()) {
            setBorder(borderLowered);
        }
    }

    /**
     * MouseListener implementaion that sets an empty border for this
     * XToolbarButton when the user releases a mouse button.
     * 
     * @param e
     */
    public void mouseReleased(MouseEvent e) {
        setBorder(borderNone);
    }

}