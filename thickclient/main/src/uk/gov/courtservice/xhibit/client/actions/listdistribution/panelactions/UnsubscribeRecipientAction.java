package uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTableModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * <p>
 * Title: UnsubscribeRecipientAction
 * </p>
 * <p>
 * Description: The action performed when the unsubscribeBtn is pressed. Removes
 * a recipient from the distribution.
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
public class UnsubscribeRecipientAction extends XAction {
    private final ManageListTablePanel parent;

    private final ManageListTableModel model;

    /**
     * Stores a refrence to the parent and calls <code>populateFromBundle</code>
     * on the super class which gets the name, short description and long
     * description from the resource bundle and sets them for this action. Also
     * gets the mnemonic and icon if they exist.
     * 
     * @param myParent
     *            The parent of this action.
     */
    public UnsubscribeRecipientAction(ManageListTablePanel myParent, ManageListTableModel model) {
        this.parent = myParent;
        this.model = model;

        populateFromBundle("unsubscribeBtn");
    }

    /**
     * Unsubscribes the selected recipient from the distribution. Refreshes the
     * parent.
     * 
     * @param ae
     *            The <code>ActionEvent</code>
     * @throws CSRecoverableException
     */
    public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
        if (parent.getListTable().getSelectedRowCount() < 0) {
            // No recipient selected
        } else {
            if (isWarnedListLetter()) {
                unsubscribeWLLRecipient();
            } else {
                unsubscribeRecipient();
            }

            // fire table changed event to refresh table view
            parent.getListTable().tableChanged(new TableModelEvent(parent.getListTable().getModel()));
            parent.stepUpdateViewState();
        }
    }

    private void unsubscribeRecipient() throws CSRecoverableException {
        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) parent.getListTable().getModel();
        int[] selection = parent.getListTable().getSelectedRows();
        Collection collect = new Vector();
        for (int i = 0; i < selection.length; i++) {
            RecipientComplexValue rcv = (RecipientComplexValue) tableModel.getDataAt(selection[i]);
            collect.add(rcv);
        }
        // Make delegate call
        RecipientComplexValue[] recipientsToRemove = new RecipientComplexValue[collect.size()];
        collect.toArray(recipientsToRemove);
        XhibitDelegateHelper.getMaintainRecipientDelegate().removeRecipientsFromList(recipientsToRemove,
                model.getDocumentType());

        // remove from table model
        java.util.Iterator iter = collect.iterator();
        while (iter.hasNext()) {
            model.deleteData(iter.next());
        }
    }

    private void unsubscribeWLLRecipient() throws CSRecoverableException {
        XHIBITTableModelInterface tableModel = (XHIBITTableModelInterface) parent.getListTable().getModel();
        int[] selection = parent.getListTable().getSelectedRows();
        Collection collect = new Vector();
        for (int i = 0; i < selection.length; i++) {
            WLLRecipientComplexValue rcv = (WLLRecipientComplexValue) tableModel.getDataAt(selection[i]);
            collect.add(rcv);
        }
        // Make delegate call
        WLLRecipientComplexValue[] recipientsToRemove = new WLLRecipientComplexValue[collect.size()];
        collect.toArray(recipientsToRemove);
        XhibitDelegateHelper.getMaintainRecipientDelegate().removeWLLRecipients(recipientsToRemove);

        // remove from table model
        Iterator iter = collect.iterator();
        while (iter.hasNext()) {
            model.deleteData(iter.next());
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
