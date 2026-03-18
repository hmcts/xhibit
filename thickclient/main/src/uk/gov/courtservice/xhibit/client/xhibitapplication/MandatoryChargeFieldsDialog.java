package uk.gov.courtservice.xhibit.client.xhibitapplication;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

import java.awt.Frame;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

import java.awt.Dimension;

import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Dialog to display charges which have missing mandatory values.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 */

public class MandatoryChargeFieldsDialog extends XDialog {

    private static final long serialVersionUID = 1L;

    public MandatoryChargeFieldsDialog(
            Frame owner,
            String title,
            boolean modal,
            ApplicationCaseModel acm)
    throws CSRecoverableException{
        super(owner,title,modal,XDialog.CANCEL,XDialog.CANCEL);
        this.setMinimumSize(new Dimension(400,400));
        
        String reviewBeforeClosing = 
            ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, "reviewBeforeClosing");
        
        //Add panel with grid etc.
        this.addBodyPanel(new MandatoryChargeFieldsPanel(acm, reviewBeforeClosing));
        ((OkCancelPanel)this.getButtonPanel()).okButton.setVisible(false);
        
        //Show dialog
        pack();
        setResizable(false);
        setVisible(true);
    }

    public MandatoryChargeFieldsDialog(
            Frame owner,
            String title,
            boolean modal,
            ApplicationCaseModel acm,
            String context)
    throws CSRecoverableException {
        super(owner,title,modal,XDialog.CANCEL,XDialog.CANCEL);
        this.setMinimumSize(new Dimension(400,400));
        this.addBodyPanel(new MandatoryChargeFieldsPanel(acm, context));
        pack();
        setResizable(false);
    }
}
