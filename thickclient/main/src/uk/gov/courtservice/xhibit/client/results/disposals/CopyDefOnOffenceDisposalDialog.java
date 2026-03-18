package uk.gov.courtservice.xhibit.client.results.disposals;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Action that initiates copying disposals
 * </p>
 * <p>
 * Description: Variation disposal not supported
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: CopyDefOnOffenceDisposalDialog.java,v 1.1 2004/06/15 15:34:44
 *          sz0t7n Exp $
 */
public class CopyDefOnOffenceDisposalDialog extends XDialog {
    private static final String resources = XhibitBundles.Disposals;

    public XhibitApplicationController xac;

    public CopyDefOnOffenceDisposalDialog(XhibitApplicationController xac, OffencePanelModel model)
            throws CSRecoverableException {
        super(xac, ResourceBundleHelper.getResource(resources, "defOnOffenceDisposalTitle"), true);
        this.xac = xac;

        CopyDefOnOffenceDisposalPanel defOnOffenceDisposalPanel = new CopyDefOnOffenceDisposalPanel(this, model);
        addBodyPanel(defOnOffenceDisposalPanel);
        pack();
    }
}
