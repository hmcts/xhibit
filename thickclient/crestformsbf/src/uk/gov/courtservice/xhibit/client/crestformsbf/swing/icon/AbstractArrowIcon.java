package uk.gov.courtservice.xhibit.client.crestformsbf.swing.icon;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * Abstract icon holds common code for arrow icons
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public abstract class AbstractArrowIcon implements Icon {

    private static final int DEFAULT_SIZE = 7;

    protected final Color edge1;

    protected final Color edge2;

    protected final Color fill;

    protected final int size;

    public AbstractArrowIcon(boolean isPressedView) {
        if (isPressedView) {
            this.edge1 = UIManager.getColor("controlDkShadow");
            this.edge2 = UIManager.getColor("controlLtHighlight");
            this.fill = UIManager.getColor("controlShadow");
        } else {
            this.edge1 = UIManager.getColor("controlShadow");
            this.edge2 = UIManager.getColor("controlHighlight");
            this.fill = UIManager.getColor("control");
        }
        this.size = DEFAULT_SIZE;
    }

    public int getIconWidth() {
        return size;
    }

    public int getIconHeight() {
        return size;
    }
}
