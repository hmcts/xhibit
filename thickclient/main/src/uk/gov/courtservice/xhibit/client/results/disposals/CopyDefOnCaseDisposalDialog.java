package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Dialog to show defendants for copying unrelated disposals
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CopyDefOnCaseDisposalDialog.java,v 1.1 2004/06/15 15:34:43
 *          sz0t7n Exp $
 */
public class CopyDefOnCaseDisposalDialog extends XDialog {
    private static final String resources = XhibitBundles.Disposals;

    public CopyDefOnCaseDisposalDialog(XhibitApplicationController xac, OffencePanelModel model)
            throws CSRecoverableException {
        super(xac, ResourceBundleHelper.getResource(resources, "defOnCaseDisposalTitle"), true);

        CopyDefOnCaseDisposalPanel defOnCaseDisposalPanel = new CopyDefOnCaseDisposalPanel(this, model);
        addBodyPanel(defOnCaseDisposalPanel);
        pack();
    }
}