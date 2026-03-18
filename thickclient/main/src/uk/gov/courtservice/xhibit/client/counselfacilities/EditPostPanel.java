package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;



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

public class EditPostPanel extends XPanel {

    private static final long serialVersionUID = 1L;

    private TitledBorder tb;

    private EditPostModel model;

    private ResourceBundle resources;

    private JButton addAdvocateBtn;
    private JButton removeAdvocateBtn;
    
    private JLabel statusMessage;

    private JScrollPane resultsScrollPane;

    private XTable resultsTable;

    private EditPostTableModel resultsTableModel;
    
    private XDialog parent;
    

    public EditPostPanel(
            XDialog parent, 
            EditPostModel model) 
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
        panel.add(getAddAdvocateBtn());
        panel.add(getRemoveAdvocateBtn());

        this.add(panel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));

        this.add(getResultsScrollPane(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
        this.add(getStatusMessage(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JLabel getStatusMessage() {
        if (statusMessage == null) {
            statusMessage = new JLabel(" "); 
            // Initialise to a space or the interface corrupts 
            // the first time a message is written.
        }
        return statusMessage;
    }
    
    private EditPostTableModel getEditPostTableModel() {
        if (resultsTableModel == null) {
            resultsTableModel = new EditPostTableModel(model.getInstructedAdvocates());
        }
        return resultsTableModel;
    }

    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createMultiLineTable(getEditPostTableModel());
            resultsTable.getTableHeader().setReorderingAllowed(false);
            //
            //resultsTable.makeSortable();
        
            resultsTable.getColumnModel().getColumn(EditPostTableModel.UNAVAILABLE_FLAG).setCellRenderer(
                    new CheckBoxTableCellRenderer());
            resultsTable.getColumnModel().getColumn(EditPostTableModel.UNAVAILABLE_FLAG).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));
            resultsTable.getColumnModel().getColumn(EditPostTableModel.WITHDRAWN_FLAG).setCellRenderer(
                    new CheckBoxTableCellRenderer());
            resultsTable.getColumnModel().getColumn(EditPostTableModel.WITHDRAWN_FLAG).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));

            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    
            final int selectBoxWidth = 180;
            resultsTable.getColumnModel().getColumn(EditPostTableModel.UNAVAILABLE_FLAG).setMaxWidth(selectBoxWidth);
            resultsTable.getColumnModel().getColumn(EditPostTableModel.WITHDRAWN_FLAG).setMaxWidth(selectBoxWidth);

            
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
    
    private JButton getRemoveAdvocateBtn() {
        if (removeAdvocateBtn == null) {
            removeAdvocateBtn = new JButton();
            removeAdvocateBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttRemoveFromPost"));
            removeAdvocateBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmRemoveFromPost").charAt(0));
            removeAdvocateBtn.setEnabled(false);
            removeAdvocateBtn.setActionCommand("REMOVE");
            removeAdvocateBtn.setText(XHIBITConstant.getResource(resources, "lblRemoveFromPost"));
        
            removeAdvocateBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {

                    final int x = getResultsTable().getSelectedRow();

                    if (x != -1) {
                        getResultsTable().clearSelection();
                        getEditPostTableModel().getData().remove(x);
                        getEditPostTableModel().fireTableDataChanged();
                        CounselFacilitiesHelper.redisplayTable(
                                getResultsTable(), 
                                getEditPostTableModel().getData());
                    }
                }
            });
        }
        
        return removeAdvocateBtn;
    }

    private JButton getAddAdvocateBtn() {
        if (addAdvocateBtn == null) {
            addAdvocateBtn = new JButton();
            addAdvocateBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttAddToPost"));
            addAdvocateBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmAddToPost").charAt(0));
            addAdvocateBtn.setEnabled(true);
            addAdvocateBtn.setActionCommand("ADD");
            addAdvocateBtn.setText(XHIBITConstant.getResource(resources, "lblAddToPost"));

            addAdvocateBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;

                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {

                    synchronized (model) {
                        getResultsTable().clearSelection();

                        AddInstructedAdvocateModel alrModel = new AddInstructedAdvocateModel();

                        // Using the Xac as the frame removes the "parentFrame
                        // not instance"
                        // debug message written by XDialog, but has the side
                        // effect that
                        // status message is blanked on the main screen.
                        // java.awt.Frame frame = model.getXac()
                        // java.awt.Frame frame = parent.getParentFrame();
                        java.awt.Frame frame = model.getXac();
                        AddInstructedAdvocateDialog alrDialog = new AddInstructedAdvocateDialog(frame, alrModel);

                        alrDialog.setVisible(true);

                        if (alrDialog.isOkClicked()) {
                            FindInstructedAdvocateTableRowModel trm = alrModel.getFindInstructedAdvocateTableRowModel();

                            if (trm != null) {
                                trm.setCrestPostNumber(model.getPostNumber());
                                getEditPostTableModel().add(trm);

                                CounselFacilitiesHelper.redisplayTable(
                                        getResultsTable(), 
                                        getEditPostTableModel().getData());
                            }
                        }

                        stepUpdateViewState();
                    }
                }
            });

        }

        return addAdvocateBtn;
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
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[EditPostPanel] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
        tb = new TitledBorder(
                BorderFactory.createEtchedBorder(SystemColor.controlHighlight, SystemColor.controlShadow),
                XHIBITConstant.getResource(resources, "lblInstructedBarristers"));

        getResultsTable().clearSelection();
        CounselFacilitiesHelper.redisplayTable(
                getResultsTable(), 
                this.getEditPostTableModel().getData());
    }

    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditPostPanel] stepDeactivate");
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
        XHIBITConstant.debug("[EditPostPanel] stepValidate");
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
        XHIBITConstant.debug("[EditPostPanel] stepUpdateViewState");
        
        this.getRemoveAdvocateBtn().setEnabled(this.getResultsTable().getSelectedRow() != -1);
        
        if (parent.getButtonPanel() instanceof OkCancelPanel) {
            OkCancelPanel okCancelPanel = (OkCancelPanel)parent.getButtonPanel();
            
            if (getEditPostTableModel().noMoreThanOneAvailableAdvocate()) {
                okCancelPanel.okButton.setEnabled(true);
                // Set to a space or the display re-sizes causing an ugly jump
                this.getStatusMessage().setText(" ");
            } else {
                okCancelPanel.okButton.setEnabled(false);
                this.getStatusMessage().setText(
                        XHIBITConstant.getResource(resources, "lblThereCanOnlyBeOneAvailable"));
            }
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
        XHIBITConstant.debug("[EditPostPanel] stepDeinitialise");
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditPostPanel] stepActivate");
        stepUpdateViewState();
    }
}