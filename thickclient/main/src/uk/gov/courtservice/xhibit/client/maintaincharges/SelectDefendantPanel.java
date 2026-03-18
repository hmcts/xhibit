package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

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
 * @author Bal Bhamra
 * @version 1.0
 */
public class SelectDefendantPanel extends XPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel defendantLabel = null;

    private DefaultComboBoxModel defendantsModel;

    private JComboBox defendantsCb = null;

    private ChargesControllerModel ccm;

    private BreachController controller;

    private BreachWizardModel model;

    private DefendantValue defendantValue;

    private Vector defendantsCol;

    private ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.Breaches);

    public SelectDefendantPanel(ChargesControllerModel ccm, BreachController controller, BreachWizardModel model)
            throws CSRecoverableException {
        
        if (ccm == null || controller == null || model == null) {
            throw new IllegalArgumentException(
                    "SelectDefendantPanel - Must have values for ccm, controller, " +
                    "and model");
        }
        
        this.ccm = ccm;
        this.controller = controller;
        this.model = model;
        stepInitialise();
        jbInit();
        stepActivate();
        stepUpdateViewState();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);
        this.setMinimumSize(new Dimension(230, 263));
        this.setPreferredSize(new Dimension(230, 263));
        this.add(getDefendantLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDefendantCb(), new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
    }

    public JLabel getDefendantLabel() {
        if (defendantLabel == null) {
            defendantLabel = new JLabel(XHIBITConstant.getResource(resources, "defendantLabel"));
        }
        return defendantLabel;
    }

    public JComboBox getDefendantCb() {
        if (defendantsCb == null) {
            defendantsCb = new JComboBox(defendantsModel);
            defendantsCb.setToolTipText(XHIBITConstant.getResource(resources, "ttDefendantCombo"));
            ComboBoxRenderer renderer = new ComboBoxRenderer();
            renderer.setPreferredSize(new Dimension(250, 20));
            defendantsCb.setRenderer(renderer);
            defendantsCb.setMinimumSize(new Dimension(250, 20));
            defendantsCb.setPreferredSize(new Dimension(250, 20));
        }
        return defendantsCb;
    }

    public void stepInitialise() {
        // get all defendants
        defendantsCol = new Vector(ccm.getCCV().getAllDefendants());
        sortDefendants(defendantsCol);
        defendantsModel = new DefaultComboBoxModel(defendantsCol);
    }

    public void stepActivate() throws CSRecoverableException {
        // H/O Proceeding Code need to use Code perhaps to set item in combo
        if (model.getDefendantID() != null) {
            Iterator it = defendantsCol.iterator();
            while (it.hasNext()) {
                defendantValue = (DefendantValue) it.next();
                if (model.getDefendantID().equals(defendantValue.getDefendantID())) {
                    getDefendantCb().setSelectedItem(defendantValue);
                    break;
                }
            }
        }
    }

    public void stepUpdateViewState() throws CSRecoverableException {
        controller.stepUpdateViewState();
    }

    public void stepDeactivate() {
        model.setDefendantID(((DefendantValue) getDefendantCb().getSelectedItem()).getDefendantID());
        model.setDefendantOnCaseID(((DefendantValue) getDefendantCb().getSelectedItem())
                .getDefOnCaseBasicValue().getId());
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            controller.stepDeinitialise();
        }
    }

    public void stepValidate() {

    }

    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            defendantValue = (DefendantValue) value;
            setHorizontalAlignment(LEFT);
            if (list.getSelectedValue() != null) {
                setText(defendantValue.getFirstName() + " " + defendantValue.getSurName());
            }
            return this;
        }
    }

    public void sortDefendants(Collection defendantsCol) {
        if (defendantsCol instanceof java.util.List) {
            uk.gov.courtservice.framework.util.Sorter.sort((java.util.List) defendantsCol, new String[] { "surName" });
        }
    }
}