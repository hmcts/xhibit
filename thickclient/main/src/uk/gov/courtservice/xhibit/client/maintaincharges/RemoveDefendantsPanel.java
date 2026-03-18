package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.DefendantHelper;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.CheckBoxTableCellRenderer;

/**
 * <p>
 * Title: RemoveDefendantsPanel
 * </p>
 * <p>
 * Description:Remove Defendants Panel
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Krishna Pokala
 * @version $Id: Remove|DefendantsPanel.java
 */

public class RemoveDefendantsPanel extends XPanel implements TableModelListener {

    private static final long serialVersionUID = 1L;

    private static final Logger log = CSServices.getLogger(RemoveDefendantsPanel.class);

    private RemoveDefendantsFromCountPanel parent;

    private ChargesControllerModel model;

    Collection<DefendantValue> defendants;

    JButton selectAllButton;

    private JScrollPane defendantsScrollPane;

    private Vector<SelectDefendantsToRemoveRowModel> defendantDetails = new Vector<SelectDefendantsToRemoveRowModel>();

    private XTable defendantsTable;

    private boolean atleastOneDefendantSelected = false;

    public boolean isAtleastOneDefendantSelected() {
        return atleastOneDefendantSelected;
    }

    public void setAtleastOneDefendantSelected(boolean atleastOneDefendantSelected) {
        this.atleastOneDefendantSelected = atleastOneDefendantSelected;
    }

    public RemoveDefendantsPanel(RemoveDefendantsFromCountPanel parent, ChargesControllerModel model,
            Collection defendants) {
        this.parent = parent;
        this.model = model;
        this.defendants = defendants;
        stepInitialise();
        jbInit();
        try {
            selectAllDefendants(Boolean.TRUE);
            selectAllDefendants(Boolean.FALSE);
        } catch (Exception e) {
            XHIBITErrorHandler.handleError(e);
        }
    }

    /**
     * Initialise GUI components
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        String defendantsDetailsBorder = ResourceBundleHelper.getResource(XhibitBundles.RemoveFromCountResources,
                "defendantsDetailsBorderTitle.value");
        this.setBorder(createdTitledBorder(defendantsDetailsBorder));

        this.add(getDefendantsScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.containerInsets, 0, 0));
        // get SelectAll Button
        this.add(getSelectAllButton(), new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0, GridBagConstraints.NORTHEAST,
                GridBagConstraints.EAST, new Insets(2, 2, 2, 2), 0, 0));
    }

    private JScrollPane getDefendantsScrollPane() {
        if (defendantsScrollPane == null) {
            defendantsScrollPane = new JScrollPane();
            defendantsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            defendantsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            defendantsScrollPane.getViewport().add(getDefendantsTable(), null);
            defendantsScrollPane.setPreferredSize(new Dimension(600, XHIBITConstant.getLineHeight() * 5));
        }

        return defendantsScrollPane;
    }

    private JButton getSelectAllButton() {
        if (selectAllButton == null) {
            selectAllButton = new JButton("Select All");
            selectAllButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        selectAllDefendants(Boolean.TRUE);
                        moveDetailsToScreen();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }

                }
            });
        }
        return selectAllButton;
    }

    private XTable getDefendantsTable() {
        if (defendantsTable == null) {
            SelectDefendantsToRemoveModel sdrm = new SelectDefendantsToRemoveModel();
            defendantsTable = XTableFactory.getInstance().createMultiLineTable(sdrm);

            defendantsTable.getTableHeader().setReorderingAllowed(false);
            defendantsTable.getColumnModel().getColumn(SelectDefendantsToRemoveModel.REMOVE_FROM_COUNT)
                    .setCellRenderer(new CheckBoxTableCellRenderer());
            defendantsTable.getColumnModel().getColumn(SelectDefendantsToRemoveModel.REMOVE_FROM_COUNT).setCellEditor(
                    new CheckBoxTableCellEditor(new JCheckBox()));

            TableColumn column = null;
            column = defendantsTable.getColumnModel().getColumn(SelectDefendantsToRemoveModel.DEFENDANT_NAME);
            column.setPreferredWidth(300);
            column = defendantsTable.getColumnModel().getColumn(SelectDefendantsToRemoveModel.REMOVE_FROM_COUNT);
            column.setPreferredWidth(100);

            defendantsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = defendantsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting())
                        return;
                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (!lsm.isSelectionEmpty()) {
                        stepUpdateViewState();
                    }
                }
            });
            sdrm.addTableModelListener(this);
        }

        return defendantsTable;
    }

    public void tableChanged(TableModelEvent tme) {
        stepUpdateViewState();
    }

    /**
     * Life-cycle method to retrieve non-volatile data or data to be shown in
     * its initial state. In this instance, invoke a call to get a list of
     * defendants.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() {
        getDefendantsData();
    }

    /**
     * Retrieve the defendants on case and build a table model with the results.
     * 
     * @throws CSRecoverableException
     */
    private void getDefendantsData() {
        defendantDetails = new Vector<SelectDefendantsToRemoveRowModel>();
        Iterator iter = defendants.iterator();
        while (iter.hasNext()) {
            SelectDefendantsToRemoveRowModel item = new SelectDefendantsToRemoveRowModel();

            DefendantValue defendantValue = (DefendantValue) iter.next();
            DefendantOnOffenceComplexValue defOnOffComplexValue = model.getOffenceValue().getDefendantOnOffence(
                    defendantValue.getDefendantID());

            item.setDefendantId(defendantValue.getDefendantID());
            item.setDefendantOnOffenceId(defOnOffComplexValue.getDefendantOnOffenceId());
            item.setDefendantName(DefendantHelper.getDefendantFullName(defendantValue));
            if (!(defOnOffComplexValue.getObsInd() != null && defOnOffComplexValue.getObsInd().equals("Y")))
                defendantDetails.add(item);
        }
    }

    /**
     * Retrieve the defendants on case and build a table model with the results.
     * 
     * @throws CSRecoverableException
     */
    private void selectAllDefendants(Boolean value) throws CSRecoverableException {

        defendantDetails = new Vector<SelectDefendantsToRemoveRowModel>();
        atleastOneDefendantSelected = true;
        Iterator iter = defendants.iterator();
        while (iter.hasNext()) {
            SelectDefendantsToRemoveRowModel item = new SelectDefendantsToRemoveRowModel();
            DefendantValue defendantValue = (DefendantValue) iter.next();
            DefendantOnOffenceComplexValue defOnOffComplexValue = model.getOffenceValue().getDefendantOnOffence(
                    defendantValue.getDefendantID());
            item.setDefendantId(defendantValue.getDefendantID());
            item.setDefendantOnOffenceId(defOnOffComplexValue.getDefendantOnOffenceId());
            item.setDefendantName(DefendantHelper.getDefendantFullName(defendantValue));
            item.setRemoveFromCount(value);
            if (!(defOnOffComplexValue.getObsInd() != null && defOnOffComplexValue.getObsInd().equals("Y")))
                defendantDetails.add(item);
        }
    }

    /**
     * Life-cycle method executed when the screen is made visible. In this
     * instance, move details to the screen.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        moveDetailsToScreen();

    }

    /**
     * Pseudo life-cycle method to refresh the screen.
     * 
     * @throws CSRecoverableException
     */
    private void moveDetailsToScreen() throws CSRecoverableException {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) getDefendantsTable().getModel();
        model.setData(defendantDetails);
        defendantsTable.tableChanged(new TableModelEvent(model));

    }

    /**
     * Life-cycle method to manage the enabled state of screen widgets
     */
    public void stepUpdateViewState() {
        boolean noSelectionMade = false;
        Iterator it = ((SelectDefendantsToRemoveModel) getDefendantsTable().getModel()).getData().iterator();
        while (it.hasNext()) {
            SelectDefendantsToRemoveRowModel row = (SelectDefendantsToRemoveRowModel) it.next();
            if (row.isRemoveFromCount() != null && row.isRemoveFromCount().booleanValue()) {
                noSelectionMade = true;
            }
        }
        if (noSelectionMade)
            atleastOneDefendantSelected = true;
        else
            atleastOneDefendantSelected = false;
        log.debug("****  atleastOneDefendantSelected " + atleastOneDefendantSelected);
        parent.stepUpdateViewState();

    }

    /**
     * Life-cycle method to perform screen validation. In this instance, at
     * least one defendant must be selected for authorisation to take place.
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // Empty
    }

    /**
     * Life-cycle method executed when the screen is made invisible
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        // Empty
    }

    /**
     * Life-cycle method executed when an either the OK or Cancel button is
     * clicked. In this instance, if OK( Authorise ) is clicked, call authorise
     * on the selected defendants. NOTE: Do not perform any GUI actions in this
     * method
     * 
     * @param save -
     *            true if the OK button was clicked
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean save) throws CSRecoverableException {
        // Empty
    }

    /**
     * Creates a lowered etched titled border with the title specified
     * 
     * @param title
     *            Border title
     * @retuns a lowered etched titled border
     */
    private Border createdTitledBorder(String title) {
        return BorderFactory.createTitledBorder(new EtchedBorder(EtchedBorder.LOWERED), title);
    }

    public Vector<SelectDefendantsToRemoveRowModel> getDefendantDetails() {
        return defendantDetails;
    }

    public void setDefendantDetails(Vector<SelectDefendantsToRemoveRowModel> defendantDetails) {
        this.defendantDetails = defendantDetails;
    }

}