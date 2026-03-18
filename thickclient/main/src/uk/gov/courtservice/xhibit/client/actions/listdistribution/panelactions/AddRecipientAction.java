package uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTableModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.listdistribution.SelectRecipientTableDialog;
import uk.gov.courtservice.xhibit.client.listdistribution.SelectRecipientTableModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title: AddRecipientAction
 * </p>
 * <p>
 * Description: The action performed when the addRecipientBtn is pressed. Builds
 * a <code>SelectRecipientTableDialog</code> for the user to select recipients
 * to add to the distirbution.
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
 * @editor Sarah Tong
 */
public class AddRecipientAction extends XAction {
    private ManageListTablePanel parent;

    private ManageListTableModel model;

    private String resources = XhibitBundles.ManageLists;

    /**
     * Stores a reference to the parent and calls
     * <code>populateFromBundle</code> on the super class which gets the name,
     * short description and long description from the resource bundle and sets
     * them for this action. Also gets the mnemonic and icon if they exist.
     * 
     * @param myParent
     *            The parent of this action.
     */
    public AddRecipientAction(ManageListTablePanel myParent, ManageListTableModel model) {
        parent = myParent;
        this.model = model;
        populateFromBundle("addListRecipientBtn");
    }

    /**
     * Generates a <code>SelectRecipientTableDialog</code> from which the user
     * can select the recipient to add to the distribution. When the
     * <code>SelectRecipientTableDialog</code> is closed refreshes the parent
     * if a new recipient has been selected.
     * 
     * @param ae
     *            The action event.
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
        SelectRecipientTableModel srtModel = new SelectRecipientTableModel();
        srtModel.setListType(model.getListType());
        srtModel.setDocumentType(model.getDocumentType());
        srtModel.setCourtId(model.getCourtId());
        srtModel.getTableColumnNames();
        Frame f = XSwingUtilities.getUltimateFrameAncestor(parent);
        if (f == null)
            f = new XFrame();

        // build and display the window from which to select the new recipient
        SelectRecipientTableDialog srt = new SelectRecipientTableDialog(f, srtModel);
        srt.show();
        if (srt.isOkClicked()) {
            // re-read the data and redisplay the window
            parent.stepInitialise();
            parent.getListTable().tableChanged(new TableModelEvent(parent.getListTable().getModel()));
            parent.stepUpdateViewState();
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