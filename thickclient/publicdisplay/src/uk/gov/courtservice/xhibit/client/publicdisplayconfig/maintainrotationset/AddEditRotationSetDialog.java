package uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description: This class allows creating and editting or rotation sets.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AddEditRotationSetDialog.java,v 1.4 2004/05/07 07:28:13 sz0t7n
 *          Exp $
 */

public class AddEditRotationSetDialog extends XDialog {
    private static final String TITLE_ADD = "pd.title.addrotationset";

    private static final String TITLE_EDIT = "pd.title.editrotationset";

    private Integer _rotationSetId = null;

    private AddEditRotationSetPanel maintainRotationSetPanel;

    public AddEditRotationSetDialog(java.awt.Dialog parent, Integer rotationSetId) throws CSRecoverableException {
        super(parent, "", true);
        _rotationSetId = rotationSetId;
        setTitle();
        maintainRotationSetPanel = new AddEditRotationSetPanel(this, rotationSetId);
        addBodyPanel(maintainRotationSetPanel);
        pack();
    }

    private void setTitle() {
        if (isEdit()) {
            super.setTitle(PublicDisplayUtils.getResource(TITLE_EDIT));
        } else {
            super.setTitle(PublicDisplayUtils.getResource(TITLE_ADD));
        }
    }

    private boolean isEdit() {
        return _rotationSetId != null;
    }

    public RotationSetComplexValue getRotationSet() {
        return maintainRotationSetPanel.getRotationSet();
    }
}