package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
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

public class SingleItemSelectionPanel extends XPanel {

    private Dimension standardDimension = new Dimension(400, XHIBITConstant.getLineHeight());

    private JLabel lblHelp = null;

    private JComboBox cmbItems = null;

    private boolean userSaves = true;

    private boolean required = true;

    private XDialog parentDialog;
    private XPanel parentPanel;

    public SingleItemSelectionPanel(){
    }
    
    public SingleItemSelectionPanel(XDialog parent, Collection itemList, Object defaultSelection, String helpText) {
        parentDialog = parent;
        stepInitialise();
        init(itemList, helpText);
        setDefaultSelection(defaultSelection);
        try {
            stepUpdateViewState();
        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }
    }

    public SingleItemSelectionPanel(XDialog parent, Collection itemList, String helpText) {
        this(parent, itemList, null, helpText);
    }
        
    public SingleItemSelectionPanel(XPanel parentPanel, Collection itemList, Object defaultSelection, String helpText)
    {
        this.parentPanel = parentPanel;
        stepInitialise();
        init(itemList, helpText, true);
        setDefaultSelection(defaultSelection);
        try {
            stepUpdateViewState();
        } catch (CSRecoverableException csre) {
            XHIBITErrorHandler.handleError(csre);
        }
    }
    
    private void init(Collection itemList, String helpText) {
        init(itemList, helpText, false);
    }
    
    private void init(Collection itemList, String helpText, boolean title) {
        GridBagConstraints gbc1 = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0);

        GridBagConstraints gbc2 = new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0);

        this.setLayout(new GridBagLayout());
        if (title){
            TitledBorder titledBorder = new TitledBorder(BorderFactory.createEtchedBorder(SystemColor.controlLtHighlight,
                SystemColor.controlShadow), ResourceBundleHelper
                .getResource(XhibitBundles.MaintainCharges, "titleProcCode"));
            this.setBorder(titledBorder);
        }
        this.add(getHelpText(helpText), gbc1);
        this.add(getDropDown(itemList), gbc2);
    }

    public void stepInitialise() {
    }

    public void stepActivate() {
    }

    public void stepValidate() throws CSValidationException {
        if (required) {
            if (cmbItems.getSelectedIndex() < 0) {
                throw new CSValidationException("gui.user.selectOne",
                        "The user did not select an item from the HO Proc Code list");
            }
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException{
        if (parentDialog != null) {
            if (required) {
                if (cmbItems.getSelectedIndex() < 0) {
                    enableOk(false);
                } else {
                    enableOk(true);
                }
            } else {
                enableOk(true);
            }
        }
        if (parentPanel != null){
            parentPanel.stepUpdateViewState();
        }
    }

    public void stepDeactivate() {
    }

    public void stepDeinitialise(boolean update) {
        userSaves = update;
    }

    private void enableOk(boolean enable) {
        ((OkCancelPanel) parentDialog.getButtonPanel()).okButton.setEnabled(enable);
    }

    private JComponent getHelpText(String helpText) {
        if (lblHelp == null) {
            lblHelp = new JLabel(helpText);
            lblHelp.setMinimumSize(standardDimension);
            lblHelp.setPreferredSize(standardDimension);
        }
        return lblHelp;
    }

    private JComponent getDropDown(Collection itemList) {
        if (cmbItems == null) {
            cmbItems = new JComboBox(itemList.toArray());
            cmbItems.setMinimumSize(standardDimension);
            cmbItems.setPreferredSize(standardDimension);
            cmbItems.setEditable(false);
            cmbItems.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent ie) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITErrorHandler.handleError(csre);
                    }
                }
            });
            cmbItems.setRenderer(new SystemRefComboBoxRenderer(true));
            ToolTipManager.sharedInstance().registerComponent(cmbItems);
        }
        return cmbItems;
    }

    public void setDefaultSelection(Object item) {
        if (item != null)
            cmbItems.setSelectedItem(item);
    }

    public Object getSelectedItem() {
        if (userSaves)
            return cmbItems.getSelectedItem();
        else
            return null;
    }

    /**
     * Set whether selection from the list is required
     * 
     * @param newValue
     */
    public void setRequired(boolean newValue) {
        required = newValue;
    }
}