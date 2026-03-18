package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;

import javax.swing.JLabel;
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

public class PanelTitleLabel extends JLabel {

    private int fontInc = 5;

    private Dimension panelTitleDim = new Dimension(200, XHIBITConstant.getLineHeight() + fontInc);

    public PanelTitleLabel() {
        java.awt.Font f = this.getFont();
        this.setFont(f.deriveFont(java.awt.Font.BOLD, f.getSize2D() + fontInc));
        this.setMinimumSize(panelTitleDim);
        this.setPreferredSize(panelTitleDim);
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setVerticalAlignment(SwingConstants.TOP);
    }

    public PanelTitleLabel(String titleText) {
        this();
        this.setText(titleText);
    }
}