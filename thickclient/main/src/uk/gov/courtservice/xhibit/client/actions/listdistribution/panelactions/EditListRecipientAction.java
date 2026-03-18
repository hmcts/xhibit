package uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageLetterRecipientDialog;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageLetterRecipientModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListRecipientDialog;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListRecipientModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTableModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;

/**
 * <p>
 * Title: EditListRecipientAction
 * </p>
 * <p>
 * Description: The action performed when the editListRecipientBtn is pressed.
 * Edits recipient information in the context of a specific distribution (e.g.
 * Daily List, Warned List Letter)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */
public class EditListRecipientAction extends XAction {
    private ManageListTablePanel parent;

    private ManageListTableModel model;

    private String resources = XhibitBundles.ManageLists;

    /**
     * Stores a refrence to the parent and calls <code>populateFromBundle</code>
     * on the super class which gets the name, short description and long
     * description from the resource bundle and sets them for this action. Also
     * gets the mnemonic and icon if they exist.
     * 
     * @param myParent
     *            The parent of this action.
     */
    public EditListRecipientAction(ManageListTablePanel myParent, ManageListTableModel model) {
        parent = myParent;
        this.model = model;
        populateFromBundle("editListRecipientBtn");
    }

    /**
     * Generates a <code>ManageLetterRecipientDialog</code> or a
     * <codeManageListRecipientDialog></code>from which the user can update
     * recipent and distribuiton details. When the dialog is closed refreshes
     * the parent if the recipient has been updated.
     * 
     * @param ae
     *            The <code>ActionEvent</code>
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
        int selectedRow = parent.getListTable().getSelectedRow();
        if (selectedRow < 0) {
            // No recipient selected
        } else {
            Object currentElement = ((XHIBITTableModelInterface) parent.getListTable().getModel())
                    .getDataAt(selectedRow);

            if (isWarnedListLetter()) {
                // set up the data required by the new dialog
                ManageLetterRecipientModel mlrModel = new ManageLetterRecipientModel();
                mlrModel.setPanelTitle(ResourceBundleHelper.getResource(XhibitBundles.ManageLists,
                        "editWLLPanelTitleLabel"));
                mlrModel.setCourtId(model.getCourtId());
                mlrModel.setListType(model.getListType());
                mlrModel.setDocumentType(model.getDocumentType());
                mlrModel.setWLLRecipientId(((WLLRecipientBasicValue) currentElement).getId());

                // build and display the new diaolg
                ManageLetterRecipientDialog mlr = null;
                java.awt.Window w = XSwingUtilities.getWindowAncestor(parent);
                if (w != null && w instanceof Frame) {
                    mlr = new ManageLetterRecipientDialog((Frame) w, mlrModel);
                } else if (w != null && w instanceof Dialog) {
                    mlr = new ManageLetterRecipientDialog((Dialog) w, mlrModel);
                } else {
                    mlr = new ManageLetterRecipientDialog(new XFrame(), mlrModel);
                }

                if (mlr != null) {
                    mlr.show();
                    if (mlr.isOkClicked()) {
                        // update and refresh the parent
                        // parent.model.editData(
                        // (WLLRecipientBasicValue)currentElement,
                        // mlrModel.getComplexWLLRecipient() );
                        // re-read the data and redisplay the window
                        parent.stepInitialise();
                        parent.getListTable().tableChanged(new TableModelEvent(parent.getListTable().getModel()));
                        parent.stepUpdateViewState();
                    }
                }
            } else {
                // set up the data required by the new dialog
                ManageListRecipientModel mlrModel = new ManageListRecipientModel();
                mlrModel.setPanelTitle(ResourceBundleHelper.getResource(XhibitBundles.ManageLists,
                        "editListPanelTitleLabel"));
                mlrModel.setCourtId(model.getCourtId());
                mlrModel.setListType(model.getListType());
                mlrModel.setDocumentType(model.getDocumentType());
                mlrModel.setRecipientId(((RecipientBasicValue) currentElement).getId());

                // build and display the new diaolg
                ManageListRecipientDialog mlr = null;
                java.awt.Window w = XSwingUtilities.getWindowAncestor(parent);
                if (w != null && w instanceof Frame) {
                    mlr = new ManageListRecipientDialog((Frame) w, mlrModel);
                } else if (w != null && w instanceof Dialog) {
                    mlr = new ManageListRecipientDialog((Dialog) w, mlrModel);
                } else {
                    mlr = new ManageListRecipientDialog(new XFrame(), mlrModel);
                }
                if (mlr != null) {
                    mlr.show();
                    if (mlr.isOkClicked()) {
                        // update and refresh the parent
                        parent.stepInitialise();
                        // parent.model.editData(
                        // (RecipientBasicValue)currentElement,
                        // mlrModel.getBasicRecipient() );
                        parent.getListTable().tableChanged(new TableModelEvent(parent.getListTable().getModel()));
                        parent.stepUpdateViewState();
                    }
                }
            }
        }
    }

    private boolean isWarnedListLetter() {
        return model.getListType().equals(
                ResourceBundleHelper.getResource(XhibitBundles.ManageLists, "warnedListLetter"));
    }

    /**
     * Override the default read access check to check for security roles
     * depending on screen button is being used in
     * 
     * @return
     */
    public boolean hasReadAccess() {
        if (isWarnedListLetter()) {
            return FunctionList.hasAccess(FunctionList.EWLLRecipient);
        } else {
            return FunctionList.hasAccess(FunctionList.ERecipient);
        }
    }

    /**
     * Override the default edit access check to check for security roles
     * depending on screen button is being used in
     * 
     * @return
     */
    public boolean hasEditAccess() {
        if (isWarnedListLetter()) {
            return FunctionList.hasAccess(FunctionList.EWLLRecipient);
        } else {
            return FunctionList.hasAccess(FunctionList.ERecipient);
        }
    }
}