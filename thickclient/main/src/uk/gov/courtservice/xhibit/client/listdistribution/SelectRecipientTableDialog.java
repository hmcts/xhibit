package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk
import java.awt.Frame;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;

/**
 * <p>
 * Title: SelectRecipientTableDialog
 * </p>
 * <p>
 * Description: Window which contains the <code>SelectRecipientTablePanel</code>
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
 * @version $Id: SelectRecipientTableDialog.java,v 1.10 2003/08/15 10:01:35
 *          bzw8gp Exp $
 */
public class SelectRecipientTableDialog extends XDialog {
    private SelectRecipientTableModel model;

    /**
     * Sets the title of this <code>XDialog</code>, adds the body panel and
     * sets the model.
     * 
     * @param frame
     *            The parent of this <code>XDialog</code>.
     * @param imodel
     *            The model to hold the data for this screen.
     * @throws CSRecoverableException
     *             If there is a problem initialising the
     *             <code>SelectRecipientTablePanel</code> which forms the body
     *             panel
     */
    public SelectRecipientTableDialog(Frame frame, SelectRecipientTableModel imodel) throws CSRecoverableException {
        super(frame, "", true);
        this.model = imodel;
        super.setTitle(model.getListType());
        super.addBodyPanel(new SelectRecipientTablePanel(imodel));
        super.pack();
    }

}
