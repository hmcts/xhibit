package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.maintaincharges.joinder.DefendantOffenceTableModel;
import uk.gov.courtservice.xhibit.client.maintaincharges.joinder.JoinderConstants;
import uk.gov.courtservice.xhibit.client.maintaincharges.log.CrestIndictmentLog;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2 - RenumberCountsListPanel
 * </p>
 * <p>
 * Description: This panel is part of the RenumberCountsListDialog
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Krishna Pokala
 * @version 1.0
 */

public class RenumberCountsListPanel extends XPanel implements ListSelectionListener {
    
    private static final long serialVersionUID = 1L;

    ChargesControllerModel model;
    protected GridBagLayout gbLayout = new GridBagLayout();
    protected GridBagConstraints gbConstraints;
    protected ResourceBundle resources;
    protected static Logger log;
    public static final int SELECTED_INDICTMENT = 0;

    public static final int JOINDER_INDICTMENT = 1;
    protected XTable indictmentTable;
    protected JButton moveUpButton;
    protected JButton moveDownButton;
    private String panelTitle;
    private boolean initialised = false;
    private Map<Integer, Integer> startState;
    private XDialog parent;

    
    public RenumberCountsListPanel(ChargesControllerModel model, XDialog parent) {
        this.parent = parent;
        this.model = model;

        this.stepInitialise();
        try {
            this.stepActivate();
            this.stepUpdateViewState();
        } catch (CSRecoverableException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise the GUI components and store them on the panel.
     */
    public void stepInitialise() {
        // Create the components.
        PanelTitleLabel panelTitleLabel = new PanelTitleLabel(this.panelTitle);
        this.indictmentTable = XTableFactory.getInstance().createMultiLineTable(new DefendantOffenceTableModel());
        this.indictmentTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.indictmentTable.getTableHeader().setReorderingAllowed(false);

        this.setLayout(gbLayout);

        // Initialise buttons here..
        JPanel buttonPanel = new JPanel(gbLayout);
        this.moveUpButton = new JButton(new MoveUpAction());
        this.moveUpButton.setIcon(this.getImage("moveup.gif"));
        this.moveDownButton = new JButton(new MoveDownAction());
        this.moveDownButton.setIcon(this.getImage("movedown.gif"));

        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 0, 0);
        buttonPanel.add(moveUpButton, gbConstraints);

        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new java.awt.Insets(0, 0, 0, 0), 0, 0);
        buttonPanel.add(moveDownButton, gbConstraints);

        // PANEL TITLE LABEL
        add(panelTitleLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // TABLE
        add(new JScrollPane(this.indictmentTable), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // BUTTON PANEL
        add(buttonPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.VERTICAL, XHIBITConstant.nonContainerInsets, 0, 0));
        buttonPanel.setVisible(true);
        this.indictmentTable.getSelectionModel().addListSelectionListener(this);
    }

    private ImageIcon getImage(String imageName) {
        ImageIcon image = null;
        java.net.URL url = getClass().getClassLoader().getResource(XHIBITConstant.imageRoot + imageName);
        if (url == null) {
            image = new ImageIcon(XHIBITConstant.imageRoot + imageName);
        } else {
            image = new ImageIcon(url);
        }
        return image;
    }

    /**
     * This panel contains only read-only information, despite the re-ordering
     * functionality.
     */
    public void stepDeactivateOnNext() {
        //Empty
    }

    /**
     * Sets reentrancy variables
     */
    public void stepDeactivateOnAll() {
        //Empty
    }

    /**
     * No validation necessary,no implementation.
     */
    public void stepValidate() {
        //Empty
    }

    /**
     * Overrides method of JoinderIndictment panel, to display/not display the
     * next button, depending on the setting.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        if (parent != null) {
            parent.getButtonPanel().okButton.setEnabled(false);
        }
        this.moveDownButton.setEnabled(false);
        this.moveUpButton.setEnabled(false);
    }
    
    /**
     * Overrides method in JoinderInditmentPanel to load the indictment
     * information in the table contained in this panel.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {

        // Take a copy of the charge so if the Cancel button is pressed 
        // the model passed in will remain unchanged.
        ChargeValue indictment = new ChargeValue(model.getChargeValue());
        model.sortOffences(indictment.getOffenceValues());
        
        ((DefendantOffenceTableModel) this.indictmentTable.getModel()).setData(indictment);
        
        startState = getState();
        
        if (!initialised) {
            this.indictmentTable.initColumnSizes(new Object[] {
                    ResourceBundleHelper.getResource(resources, JoinderConstants.COUNT),
                    ResourceBundleHelper.getResource(resources, JoinderConstants.OFFENCE_DESC),
                    ResourceBundleHelper.getResource(resources, JoinderConstants.DEFENDANT) }, 750);
        }
        this.indictmentTable.tableChanged(new TableModelEvent(this.indictmentTable.getModel()));
        this.revalidate();
        this.repaint();
    }

    private HashMap<Integer, Integer> getState() {
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        Collection offences = getTableModelOffences();
        Iterator itr = offences.iterator();
        while (itr.hasNext()) {
            OffenceValue offenceValue = (OffenceValue)itr.next();
            map.put(offenceValue.getOffenceID(), offenceValue.getCrestOffenceSeqNo());
        }
        return map;
    }
    
    private HashMap<Integer, Integer> getChangedState() {
        HashMap<Integer, Integer> changedState = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> currentState = getState();
        if (startState != null) {
            for (Integer key : startState.keySet()) {
                if (!startState.get(key).equals(currentState.get(key))) {
                    changedState.put(key, currentState.get(key));
                }
            }
        }
        return changedState;
    }
    
    private boolean isStateChanged() {
        return !getChangedState().isEmpty();
    }
    
    public void valueChanged(ListSelectionEvent e) {
        int selectionIdx = this.indictmentTable.getSelectionModel().getMaxSelectionIndex();
        if (selectionIdx == -1) {
            this.moveDownButton.setEnabled(false);
            this.moveUpButton.setEnabled(false);
        } else {
            int rowCount = this.indictmentTable.getModel().getRowCount();
            this.moveDownButton.setEnabled(selectionIdx < (rowCount - 1));
            this.moveUpButton.setEnabled(selectionIdx > 0);
        }
        if (parent != null) {
            parent.getButtonPanel().okButton.setEnabled(isStateChanged());
        }
    }

    public void reset() {
        this.initialised = false;
    }

    /**
     
     * Description: Moves an offence up one row in the table, decrementing its
     * offence seq number by 1.     
     */
    private final class MoveUpAction extends XAction {
        private static final long serialVersionUID = 1L;

        public void xActionPerformed(ActionEvent e) {
            JTable table = RenumberCountsListPanel.this.indictmentTable;
            int rowIndex = table.getSelectionModel().getMaxSelectionIndex();
            DefendantOffenceTableModel tableModel = (DefendantOffenceTableModel) table.getModel();
            tableModel.moveUp(rowIndex);
            table.tableChanged(new TableModelEvent(tableModel));
            table.getSelectionModel().setSelectionInterval(rowIndex - 1, rowIndex - 1);
        }
    }

    /**
    
     * Description: Moves an Offence down one row in the table incrementing its
     * offence seq number by 1.     
     */
    private final class MoveDownAction extends XAction {
        private static final long serialVersionUID = 1L;

        public void xActionPerformed(ActionEvent e) {
            JTable table = RenumberCountsListPanel.this.indictmentTable;
            int rowIndex = table.getSelectionModel().getMaxSelectionIndex();
            DefendantOffenceTableModel tableModel = (DefendantOffenceTableModel) table.getModel();
            tableModel.moveDown(rowIndex);            
            table.tableChanged(new TableModelEvent(tableModel));
            table.getSelectionModel().setSelectionInterval(rowIndex + 1, rowIndex + 1);
        }
    }

    public void stepDeactivate() throws CSRecoverableException {
        if (isStateChanged()) {
            Collection offences = getTableModelOffences();
            ChargeValue indictment = model.getChargeValue();
            indictment.setOffenceValues(offences);
            XhibitDelegateHelper.getChargeDelegate().renumberCounts(indictment, getChangedState(),
            		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        }
    }
    
    private Collection getTableModelOffences() {
        Collection offences = 
            ((DefendantOffenceTableModel) this.indictmentTable.getModel()).getOffences();
        return offences;
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if(update){
            //Write to CRESTIndictmentLog (RFC2867)
            if (isStateChanged()) {
                CaseBasicValue caseBasicValue = model.getACM().getScheduledHearingValue().getCaseBasicValue();
                CrestIndictmentLog.getInstance().renumberCountLog(caseBasicValue, getChangedState(), 
                        startState, model.getChargeValue());
                
            }
        }
    }
}