package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk
import java.awt.Dialog;
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: ManageListRecipientDialog
 * </p>
 * <p>
 * Description: Window which contains the ManageListRecipientPanel
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
 * 
 * @editor Sarah Tong
 * @version $Id: ManageListRecipientDialog.java,v 1.13 2005/06/02 13:49:04
 *          szfnvt Exp $
 */
public class ManageListRecipientDialog extends XDialog {
    private ManageListRecipientModel model;

    /**
     * Used when the parent window is a <code>Frame</code>. Calls the
     * <code>init()</code> method.
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
    public ManageListRecipientDialog(Frame frame, ManageListRecipientModel model) throws CSRecoverableException {
        super(frame, "", true);
        init(model);
    }

    /**
     * Used when the parent window is a <code>Dialog</code>. Calls the
     * <code>init()</code> method.
     * 
     * @param dialog
     *            The parent of this <code>XDialog</code>.
     * @param model
     *            The model to hold the data for this screen.
     * @throws CSRecoverableException
     *             If there is a problem initialising the
     *             <code>ManageListRecipientPanel</code> which forms the body
     *             panel
     */
    public ManageListRecipientDialog(Dialog dialog, ManageListRecipientModel model) throws CSRecoverableException {
        super(dialog, "", true);
        init(model);
    }

    /**
     * Sets the title of this <code>XDialog</code>, adds the body panel and
     * sets the model.
     * 
     * @param model
     *            The model to hold the data for this screen.
     * @throws CSRecoverableException
     */
    private void init(ManageListRecipientModel model) throws CSRecoverableException {
        super.setTitle(XHIBITConstant.getResource(XhibitBundles.ManageLists, "listRecipientTitle"));
        this.model = model;
        this.model.setPanelTitle(model.getPanelTitle());
        super.addBodyPanel(new ManageListRecipientPanel(this, model));
        pack();
        centreDialog();
    }

}
