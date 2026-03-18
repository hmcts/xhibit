package uk.gov.courtservice.xhibit.client.actions.caseprogress;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JToggleButton;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import uk.gov.courtservice.xhibit.client.caseprogress.CaseProgressChargesPanel;
import uk.gov.courtservice.xhibit.client.caseprogress.CaseProgressConstants;
import uk.gov.courtservice.xhibit.client.caseprogress.CaseProgressTableModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 - View Charge Details
 * </p>
 * <p>
 * Description: Responsible for populating the case progress chargetable models
 * with more/less detailed data associated with charges.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class ViewChargeDetailsAction extends XAction {
    XhibitApplicationController xac;

    ResourceBundle resources;

    String onButtonText;

    String offButtonText;

    public ViewChargeDetailsAction() {
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);
        onButtonText = XHIBITConstant.getResource(resources, CaseProgressConstants.DETAILS_ON_TXT);
        offButtonText = XHIBITConstant.getResource(resources, CaseProgressConstants.DETAILS_OFF_TXT);
    }

    public void xActionPerformed(ActionEvent e) {
        xac = (XhibitApplicationController) getController();
        JToggleButton button = (JToggleButton) e.getSource();
        boolean isDetails = !((Boolean) button.getClientProperty(CaseProgressChargesPanel.DETAILS_TOGGLE))
                .booleanValue();
        if (isDetails) {
            button.setText(offButtonText);
        } else {
            button.setText(onButtonText);
        }
        button.putClientProperty(CaseProgressChargesPanel.DETAILS_TOGGLE, new Boolean(isDetails));

        // Refresh the table.
        XTable table = (XTable) button.getClientProperty(CaseProgressChargesPanel.CHARGE_TABLE);
        TableModel tableModel = table.getModel();
        if (tableModel instanceof CaseProgressTableModel) {
            ((CaseProgressTableModel) tableModel).setDetailsOn(isDetails);
        }
        table.tableChanged(new TableModelEvent(table.getModel()));

        EventQueue.invokeLater(new ResizeTable(table));
    }

    /**
     * Will return the 'packed' dimension of the table when it contains its data
     * to view.
     * 
     * @param table
     *            the table to find its dimension.
     * 
     * @return the dimension of the table.
     */
    private Dimension getTableDimension(XTable table) {
        // Calculate the full height of the table by traversing each row, and
        // getting each row's height.
        int height = 0;
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            height += (table.getRowHeight(i) + table.getRowMargin());
        }
        // Calculate the full width of the table.
        // int width = table.getColumnModel().getTotalColumnWidth();

        // Return the dimension.
        return new Dimension(0, height);
    }

    class ResizeTable implements Runnable {
        private XTable _table;

        public ResizeTable(XTable table) {
            _table = table;
        }

        public void run() {
            _table.setPreferredScrollableViewportSize(getTableDimension(_table));
            xac.getBodyPanel().invalidate();
            xac.getBodyPanel().validate();
        }
    }
}