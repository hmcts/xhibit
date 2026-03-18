package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * @deprecated
 * <p>
 * Title: ManageListTableDialog
 * </p>
 * <p>
 * Description: Window which contains the <code>ManageListTablePanel</code>
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: G S Rajasekaran
 * </p>
 * @editor Sarah Tong
 * @version $Id: ManageListTableDialog.java,v 1.10 2006/05/31 14:25:36 bzjrnl
 *          Exp $
 */
public class ManageListTableDialog extends XDialog {
    private ManageListTableModel model;

    private ManageListTablePanel bodyPanel;

    /**
     * Sets the title of this <code>XDialog</code>, adds the body panel and
     * sets the model.
     * 
     * @param frame
     *            The parent of this <code>XDialog</code>.
     * @param model
     *            The model to hold the data for this screen.
     * @throws CSRecoverableException
     *             If there is a problem initialising the
     *             <code>ManageListRecipientPanel</code> which forms the body
     *             panel
     */
    public ManageListTableDialog(Frame frame, ManageListTableModel model) throws CSRecoverableException {
        super(frame, "", true);
        this.model = model;
        super.setTitle(model.getListType());
        this.bodyPanel = new ManageListTablePanel(model);
        super.addBodyPanel(bodyPanel);
        super.pack();
    }
}
