package uk.gov.courtservice.xhibit.client.util;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import uk.gov.courtservice.xhibit.client.actions.common.CopyAction;
import uk.gov.courtservice.xhibit.client.actions.common.CutAction;
import uk.gov.courtservice.xhibit.client.actions.common.PasteAction;

/**
 * <p>
 * Title: Standard Popup that has cut copy paste
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

public class DefaultPopup extends JPopupMenu {
    public static final int NONE = 0;

    public static final int CUT = 1;

    public static final int COPY = 2;

    public static final int PASTE = 4;

    private int saveX = 0;

    private int saveY = 0;

    public DefaultPopup() {
        this.add(getCutAction());
        this.add(getCopyAction());
        this.add(getPasteAction());
    }

    public DefaultPopup(int options) {
        if ((options & CUT) == CUT)
            this.add(getCutAction());
        if ((options & COPY) == COPY)
            this.add(getCopyAction());
        if ((options & PASTE) == PASTE)
            this.add(getPasteAction());
    }

    private JMenuItem getCutAction() {
        return new JMenuItem(CutAction.getInstance());
    }

    private JMenuItem getCopyAction() {
        return new JMenuItem(CopyAction.getInstance());
    }

    private JMenuItem getPasteAction() {
        return new JMenuItem(PasteAction.getInstance());
    }
}