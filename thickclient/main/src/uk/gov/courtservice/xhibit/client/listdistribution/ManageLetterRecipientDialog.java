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
 * Title: ManageLetterRecipientDialog
 * </p>
 * <p>
 * Description: Window which contains the ManageLetterRecipientPanel
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
 * @version $Id: ManageLetterRecipientDialog.java,v 1.9 2003/08/15 10:01:33
 *          bzw8gp Exp $
 */
public class ManageLetterRecipientDialog extends XDialog {
    private ManageLetterRecipientModel model;

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
     *             <code>ManageLetterRecipientPanel</code> which forms the
     *             body panel
     */
    public ManageLetterRecipientDialog(Frame frame, ManageLetterRecipientModel model) throws CSRecoverableException {
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
     *             <code>ManageLetterRecipientPanel</code> which forms the
     *             body panel
     */
    public ManageLetterRecipientDialog(Dialog dialog, ManageLetterRecipientModel model) throws CSRecoverableException {
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
    private void init(ManageLetterRecipientModel model) throws CSRecoverableException {
        super.setTitle(XHIBITConstant.getResource(XhibitBundles.ManageLists, "letterRecipientTitle"));
        this.model = model;
        this.model.setPanelTitle(model.getPanelTitle());
        super.addBodyPanel(new ManageLetterRecipientPanel(this, model));
        super.pack();
        super.setResizable(false);
    }

}
