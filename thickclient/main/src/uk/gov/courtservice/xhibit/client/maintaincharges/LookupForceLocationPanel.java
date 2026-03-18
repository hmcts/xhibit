package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.client.counselfacilities.CounselFacilitiesHelper;
import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;



/**
 * <p>
 * Title: Screen for editing the instructed advocates for a post
 * </p>
 * <p>
 * Description: Edit the post.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class LookupForceLocationPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private TitledBorder tb;

    private LookupForceLocationModel model;

    private JScrollPane resultsScrollPane;

    private XTable resultsTable;

    private LookupForceLocationTableModel tableModel;
    
    private XDialog parent;
    

    public LookupForceLocationPanel(
            XDialog parent, 
            LookupForceLocationModel model) 
    throws CSRecoverableException {
        super();
        this.model = model;
        this.parent = parent;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Paints the controls on the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        
        JPanel panel = new JPanel();

        this.add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        this.add(getResultsScrollPane(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    
    private LookupForceLocationTableModel getTableModel() {
        if (tableModel == null) {
            tableModel = new LookupForceLocationTableModel(model.getLocationCodes());
        }
        return tableModel;
    }

    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createMultiLineTable(getTableModel());
            resultsTable.getTableHeader().setReorderingAllowed(false);
            //
            //resultsTable.makeSortable();
        
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
            resultsTable.getColumnModel().getColumn(
                    LookupForceLocationTableModel.LOCATION_CODE_COLUMN).setMaxWidth(60);

            ListSelectionModel rowSM = resultsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    try {
                        moveScreenToModel();
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return resultsTable;
    }

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            resultsScrollPane.setBorder(tb);
            resultsScrollPane.getViewport().add(getResultsTable(), null);
            resultsScrollPane.setPreferredSize(new Dimension(700, 312));
        }

        return resultsScrollPane;
    }

    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return true;
    }

    /**
     * Saves the data on the screen to the FindRepresentationWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() throws CSRecoverableException {
        XHIBITConstant.debug("moveScreenToModel");
        
        RefSystemCodeBasicValue item = null;

        int x = getResultsTable().getSelectedRow();

        if (x != -1) {
            XHIBITTableModelInterface xstModel = (XHIBITTableModelInterface) getResultsTable().getModel();
            item = (RefSystemCodeBasicValue) xstModel.getDataAt(x);
            model.setForceLocationCode(item.getCode());
        }
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepInitialise");
        getResultsTable().clearSelection();
        CounselFacilitiesHelper.redisplayTable(
                getResultsTable(), 
                this.getTableModel().getData());
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepDeactivate");
    }

    /**
     * XPanel implementation of life cycle method, called when leaving this
     * screen to validate the data.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     *             if an invalid date is entered.
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepValidate");
        // Nothing to validate
        moveScreenToModel();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepUpdateViewState");
        
        if (parent.getButtonPanel() instanceof OkCancelPanel) {
            OkCancelPanel okCancelPanel = (OkCancelPanel)parent.getButtonPanel();
            
            okCancelPanel.okButton.setEnabled(getResultsTable().getSelectedRow() != -1);
        }
    }
    

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * closed. Note - This is only called if the user has been to the other
     * screens in the wizard, populated the necessary data and then come back to
     * this screen.
     * 
     * @param update
     *            true if the data on the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepDeinitialise");
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[LookupForceLocationPanel] stepActivate");
        stepUpdateViewState();
    }
}
