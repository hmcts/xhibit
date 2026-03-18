package uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTableModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: DeleteRecipientAction
 * </p>
 * <p>
 * Description: The action performed when the deleteRecipientBtn is pressed.
 * Deletes the selected recipient from the system.
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
public class DeleteRecipientAction extends XAction {
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
    public DeleteRecipientAction(ManageListTablePanel myParent, ManageListTableModel model) {
        parent = myParent;
        this.model = model;
        populateFromBundle("deleteRecipientBtn");
    }

    /**
     * Prompts the user to confirm they want to delete the recipient from the
     * system. If the user confirms the recipient is deleted from the system and
     * the parent is refreshed.
     * 
     * @param ae
     *            The <code>ActionEvent</code>
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
        if ((parent.getListTable().getSelectedRowCount() < 0)
                || (model.getListType().equals(ResourceBundleHelper.getResource(XhibitBundles.ManageLists,
                        "warnedListLetter")))) {
            // No recipient selected
        } else {
            Frame f = XSwingUtilities.getUltimateFrameAncestor(parent);
            if (f == null)
                f = new XFrame();
            boolean reply = XMessageBox.alert(f, ResourceBundleHelper.getResource(resources, "deletePanelTitleLabel"),
                    true, XMessageBox.ICONQUESTION, ResourceBundleHelper.getResource(resources, "deleteQuestion1")
                            + "\n" + ResourceBundleHelper.getResource(resources, "deleteQuestion2"), XMessageBox.YESNO,
                    XMessageBox.DEFAULTNO);

            if (reply) {
                int[] selection = parent.getListTable().getSelectedRows();
                Collection collect = new Vector();
                for (int i = 0; i < selection.length; i++) {
                    RecipientBasicValue rbv = (RecipientBasicValue) ((XHIBITTableModelInterface) parent.getListTable()
                            .getModel()).getDataAt(selection[i]);
                    collect.add(rbv);
                }
                // Make delegate call
                RecipientBasicValue[] recipientsToRemove = new RecipientBasicValue[collect.size()];
                collect.toArray(recipientsToRemove);
                XhibitDelegateHelper.getMaintainRecipientDelegate().removeRecipients(recipientsToRemove);

                // remove from table model
                java.util.Iterator iter = collect.iterator();
                while (iter.hasNext()) {
                    model.deleteData(iter.next());
                }

                // fire table changed event to refresh table view
                parent.getListTable().tableChanged(new TableModelEvent(parent.getListTable().getModel()));
                parent.getListTable().validate();
                parent.getListTable().repaint();

                parent.stepUpdateViewState();
            }
        }
    }
}
