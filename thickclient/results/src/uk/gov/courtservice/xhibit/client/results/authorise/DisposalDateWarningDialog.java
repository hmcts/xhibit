package uk.gov.courtservice.xhibit.client.results.authorise;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import java.awt.Frame;
import java.awt.Dimension;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthoriseWarning;

/**
 * <p>
 * Title: DisposalDateWarningDialog
 * </p>
 * <p>
 * Description: Dialog to display a list of disposal date warnings.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2010
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class DisposalDateWarningDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    public DisposalDateWarningDialog(
            Frame owner,
            String title,
            boolean modal,
            AuthoriseWarning[] values)
    throws CSRecoverableException {
        super(owner, title, modal, XDialog.CANCEL, XDialog.CANCEL);
        this.setMinimumSize(new Dimension(600, 400));
        this.addBodyPanel(new DisposalDateWarningPanel(values));
        pack();
        setResizable(false);
    }
}
