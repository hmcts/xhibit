package uk.gov.courtservice.xhibit.client.publicdisplayconfig.editscreen;

import java.awt.Dialog;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: EditScreenDialog.java,v 1.6 2006/06/05 12:32:07 bzjrnl Exp $
 */
public class EditScreenDialog extends XDialog {
    private static final String TITLE = "pd.title.editscreen";

    private EditScreenPanel editScreenPanel;

    public EditScreenDialog(Dialog _parent, Integer displayId) {
        super(_parent, PublicDisplayUtils.getResource(TITLE), true);
        editScreenPanel = new EditScreenPanel(displayId);
        addBodyPanel(editScreenPanel);
        pack();
    }

    /**
     * @return Returns the display configuration being changed.
     */
    public DisplayConfiguration getDisplayConfiguration() {
        return editScreenPanel.getDisplayConfiguration();
    }
}