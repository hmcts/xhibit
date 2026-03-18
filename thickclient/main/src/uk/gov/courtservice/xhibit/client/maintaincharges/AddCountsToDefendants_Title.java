package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Bal Bhamra
 * @version 1.0
 */
/*
 * Ref Date Author Description
 * 
 * 06-08-2003 AW Daley DefendantSelectorPanel replaced with
 * DefendantsCountsTablePanel. Now handles input of CRN when adding a defendant
 * to a count.
 */
public class AddCountsToDefendants_Title extends JPanel {
    private ResourceBundle myResources = XHIBITConstant.getResourceBundle(XhibitBundles.AddCountsDefendantsResources);

    private Insets defaultInset = new Insets(4, 4, 4, 4);

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel titleLabel;

    private JLabel defendantNameLabel;

    private JLabel selectIndictmentLabel;

    private JComboBox indictmentCb;

    private DefaultComboBoxModel indictmentsModel;

    private String defendantName;

    private AddCountsToDefendantsPanel parentPanel;

    private ChargesControllerModel model;

    private Integer chargeID;

    private uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue;

    public AddCountsToDefendants_Title(AddCountsToDefendantsPanel parentPanel, String defendantName,
            Collection indictmentNames) {

        this.model = parentPanel.model;
        this.parentPanel = parentPanel;
        this.defendantName = defendantName; // this can probably be got from
        // model no??
        setIndictmentNames(indictmentNames);

        // **@todo Need to set combo based on ChargeID.*/
        this.chargeID = model.getChargeValue().getChargeID();
        // TEMP fo above
        // chargeID = new Integer(2);

        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < indictmentsModel.getSize(); i++) {
            chargeValue = (uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue) indictmentsModel
                    .getElementAt(i);
            if (chargeID.intValue() == chargeValue.getChargeID().intValue()) {
                GetIndictmentCb().setSelectedItem(chargeValue);
                break;
            }
        }
        RepopulateLists(chargeID);
    }

    private void jbInit() throws Exception {
        this.setPreferredSize(new Dimension(400, 80));
        this.setLayout(gridBagLayout1);
        this.add(getTitleLabel(), new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getDefendantNameLabel(), new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 10, 2), 0, 0));
        this.add(getSelectIndictmentLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(GetIndictmentCb(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));

    }

    public JLabel getTitleLabel() {
        if (titleLabel == null) {
            titleLabel = new JLabel(XHIBITConstant.getResource(myResources, "addCountsToDefendantLabel"));
        }

        return titleLabel;
    }

    public JLabel getDefendantNameLabel() {
        if (defendantNameLabel == null) {
            defendantNameLabel = new JLabel(defendantName);
        }
        return defendantNameLabel;
    }

    public JLabel getSelectIndictmentLabel() {
        if (selectIndictmentLabel == null) {
            selectIndictmentLabel = new JLabel(XHIBITConstant.getResource(myResources, "selectIndictmentLabel"));
        }
        return selectIndictmentLabel;
    }

    public void setIndictmentNames(Collection indictments) {
        indictmentsModel = new DefaultComboBoxModel(new Vector(indictments));
    }

    public JComboBox GetIndictmentCb() {
        if (indictmentCb == null) {
            // get vector of all indictments
            indictmentCb = new JComboBox(indictmentsModel);
            ComboBoxRenderer renderer = new ComboBoxRenderer();
            indictmentCb.setPreferredSize(new Dimension(250, 20));
            indictmentCb.setRenderer(renderer);
        }

        indictmentCb.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // repopulate listboxes
                chargeID = ((uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue) indictmentsModel
                        .getSelectedItem()).getChargeID();
                RepopulateLists(chargeID);
            }
        });

        return indictmentCb;
    }

    private void RepopulateLists(Integer selectedChargeID) {
        // DefaultListModel notOnCountModel;
        Vector allCounts;

        // TEMP CODE
        // DummyChargesBD DummyBG;
        // DummyBG= new DummyChargesBD();
        // allCounts = new Vector(DummyBG.getOffenceValues());
        // notOnCountModel = parentPanel.populateNotOnCountModel(allCounts);
        // **@todo ACTUAL CODE*/
        // Get indicments using chargeId
        allCounts = new Vector(model.getCCV().getOffenceValues(selectedChargeID));
        /* notOnCountModel = */parentPanel.populateNotOnCountModel(allCounts);

        // parentPanel.getMiddlePanel().setAllListModel(notOnCountModel);
        // parentPanel.getMiddlePanel().setTargetListModel(new
        // DefaultListModel());
        // parentPanel.getMiddlePanel().getAllList().setModel(notOnCountModel);
        // parentPanel.getMiddlePanel().getTargetList().setModel(parentPanel.getMiddlePanel().getTargetListModel());
        OffenceValue[] offencesNotOnCount = new OffenceValue[allCounts.size()];

        parentPanel.getMiddlePanel().getModel().setOffences((OffenceValue[]) allCounts.toArray(offencesNotOnCount));
        parentPanel.stepUpdateViewState();
    }

    // Renderer for combobox
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

            uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue = (uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue) value;
            setHorizontalAlignment(LEFT);

            if (list.getSelectedValue() != null) {
                /** @todo Need routine to convert the sequence no into a letter. */
                setText(XHIBITConstant.getResource(myResources, "indictmentText") + " "
                        + chargeValue.getCrestChargeSeqNo());
            }
            return this;
        }
    }

}