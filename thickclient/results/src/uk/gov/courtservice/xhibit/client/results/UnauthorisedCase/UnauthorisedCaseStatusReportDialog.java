package uk.gov.courtservice.xhibit.client.results.UnauthorisedCase;

import uk.gov.courtservice.xhibit.client.util.ApplyOkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

import javax.swing.ImageIcon;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: UnauthorisedCaseStatusReportDialog
 * </p>
 * <p>
 * Description: The dialog which displays the Print Preview screen when the user
 * chooses to generate a report of the Unauthorised Cases
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */
public class UnauthorisedCaseStatusReportDialog extends XDialog{
    private static final long serialVersionUID = 1L;
    
    private UnauthorisedCaseStatusReportPanel panel;
    private final String icon = XHIBITConstant.imageRoot + "XhibitCornerLogo.gif";
            
    public UnauthorisedCaseStatusReportDialog(XhibitApplicationController xac,UnauthorisedCaseStatusPanel parentPanel)throws CSRecoverableException{
        super(xac,XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources, "UnauthorisedCaseStatusReport.title"),
                true,XDialog.APPLYOKCANCEL,XDialog.DEFAULTCANCEL);
        super.setIconImage(getIcon().getImage());
        panel = new UnauthorisedCaseStatusReportPanel(parentPanel);
        ApplyOkCancelPanel buttonPanel = (ApplyOkCancelPanel) getButtonPanel();
        buttonPanel.okButton.setVisible(false);
        buttonPanel.applyButton.setVisible(false);
        buttonPanel.getCancelAction().populateFromBundle("Close");
        addBodyPanel(panel);
        pack();
        
        
    }
    private ImageIcon getIcon() {
        java.net.URL url = getClass().getClassLoader().getResource(icon);
        ImageIcon i = null;

        if (url != null)
            i = new ImageIcon(url);
        else
            i = new ImageIcon(icon);
        return i;
    }
}
