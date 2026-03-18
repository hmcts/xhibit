package uk.gov.courtservice.xhibit.client.courtlog.directions.defendant;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.text.Document;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_directions_for_defendant.XhbDirectionsForDefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.directions.DirectionsForDefendantValue;
import uk.gov.courtservice.xhibit.client.courtlog.directions.ItemChangeListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.PDHConstants;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateRadioButtonListener;
import uk.gov.courtservice.xhibit.client.courtlog.directions.XDirectionsForDefendantPanel;
import uk.gov.courtservice.xhibit.client.util.RestrictionFinder;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;

/**
 * <p>
 * Title: P&D Form Panel
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
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class BailCustodyPanel extends XDirectionsForDefendantPanel {
    /**
     * Constant used to represent the maximum number of characters that can be
     * entered into the directions text field
     * 
     * @since Bug X55066
     */
    private static final int DIRECTIONS_TEXT_LIMIT = 255;

    private final String pdLookup = "E" + getEventType() + "_Bail_Or_Custody_Options";

    private final String listenButton = "E" + getEventType() + "_Bail_Varied";

    private final ButtonGroup padGroup = new ButtonGroup();

    private final HashMap buttonMap = new HashMap();

    private Collection thisRestriction = null;

    private DirectionsForDefendantValue[] model;

    // private DirectionsForDefendantBasicValue dfv;

    private JLabel bailConditionsLabel = null;

    private JTextArea bailConditionsText = null;

    private JScrollPane bailConditionsScroll = null;

    public BailCustodyPanel(DirectionsForDefendantValue[] model) throws CSRecoverableException {
        setModel(model);
        init();
        // stepActivate();
    }

    public void stepActivate() throws CSRecoverableException {
        // reset warning view first
        clearWarning(padGroup);
        super.stepActivate();
    }

    public Integer getEventType() {
        return PDHConstants.DEF_BAIL;
    }

    public void moveModelToScreen() {
        if (model != null) {
            if (!isModelsIdentical()) {
                showWarning(padGroup);
            }
            // Regardless of whether the models are identical or not, use
            // the first
            // defendant for displaying on the screen
            XhbDirectionsForDefendantBasicValue dfv = model[0].getDirectionsForDefendantBasicValue();
            if (dfv != null) {
                String xmlKey = PDHConstants.getKeyForDb(dfv.getBailStatus(), getRestrictions());

                JRadioButton toSelect = (JRadioButton) buttonMap.get(xmlKey);
                if (toSelect != null) {
                    toSelect.setSelected(true);
                } else {
                    JRadioButton toNotSelect = (JRadioButton) buttonMap.get(PDHConstants.notselected);
                    toNotSelect.setSelected(true);
                }

                if (dfv.getNewBailConditions() != null) {
                    getBailConditionText().setText(dfv.getNewBailConditions());
                } else {
                    getBailConditionText().setText("");
                }
            }
        }
    }

    public void moveScreenToModel() {
        if (model != null && getModified()) {
            JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
            if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.dbCode) != null) {
                for (int i = 0; i < model.length; i++) {
                    XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();
                    dfv.setBailStatus((String) selectedRB.getClientProperty(PDHConstants.dbCode));

                    if (getBailConditionText().isEnabled()) {
                        dfv.setNewBailConditions(getBailConditionText().getText());
                    } else {
                        dfv.setNewBailConditions(null);
                    }
                }
            }
        }
    }

    public void populateCRUD(HashMap crud, Integer defOnCaseId) {
        if (model != null) {
            String eventCode = "E" + getEventType().toString();

            for (int i = 0; i < model.length; i++) {
                XhbDirectionsForDefendantBasicValue dfv = model[i].getDirectionsForDefendantBasicValue();

                if (dfv.getDefendantOnCaseId().equals(defOnCaseId) && dfv.getBailStatus() != null) {
                    HashMap bailConditionMap = new HashMap();
                    bailConditionMap.put(pdLookup, PDHConstants.getKeyForDb(dfv.getBailStatus(), getRestrictions())); // selectedRB.getClientProperty(PDHConstants.xmlCode));
                    bailConditionMap.put(eventCode + "_Bail_Or_Custody_Conditions", dfv.getNewBailConditions());
                    crud.put(eventCode + "_Bail_Or_Custody", bailConditionMap);
                    break;
                }
            }
        }
    }

    private Collection getRestrictions() {
        if (thisRestriction == null) {
            thisRestriction = RestrictionFinder.getRestrictingValues(PDHConstants.DEF_BAIL.toString() + ".xsd",
                    pdLookup);
        }

        return thisRestriction;
    }

    private void init() {
        this.setLayout(new GridBagLayout());
        TitledBorder padBorder = new TitledBorder(XHIBITConstant.getResource(XhibitBundles.Directions,
                "BailOrCustodyBorder"));
        Border border2 = BorderFactory.createCompoundBorder(padBorder, BorderFactory.createEmptyBorder(4, 4, 4, 4));
        this.setBorder(border2);

        this.add(getWarningLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.VERTICAL, PDHConstants.PDH_INSETS, 0, 0));
        JPanel bcp = new JPanel(new GridBagLayout());
        this.add(bcp, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, PDHConstants.PDH_INSETS, 0, 0));

        ActionListener rbListner = new UpdateStateRadioButtonListener(this);
        Object[] rb = getRestrictions().toArray();

        int i = 0;
        for (; i < rb.length; i++) {
            JRadioButton thisRadio = PDHConstants.getRb((String) rb[i], buttonMap);
            thisRadio.addActionListener(rbListner);
            thisRadio.addItemListener(new ItemChangeListener(this));
            thisRadio.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED && !isActivating())
                        clearWarning(padGroup);
                }
            });
            padGroup.add(thisRadio);
            bcp.add(thisRadio, new GridBagConstraints(0, i, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        }
        padGroup.add(PDHConstants.getNotSelectRb(buttonMap));

        bcp.add(getBailConditionLabel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, PDHConstants.PDH_INSETS, 0, 0));
        bcp.add(getBailConditionsScroll(), new GridBagConstraints(1, 1, 1, i - 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, PDHConstants.PDH_INSETS, 0, 0));
    }

    private JScrollPane getBailConditionsScroll() {
        if (bailConditionsScroll == null) {
            Dimension defaultSize = new Dimension(100, XHIBITConstant.getLineHeight() * 2);

            bailConditionsScroll = new JScrollPane(getBailConditionText());
            bailConditionsScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            bailConditionsScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            bailConditionsScroll.setMinimumSize(defaultSize);
            bailConditionsScroll.setPreferredSize(defaultSize);
        }

        return bailConditionsScroll;
    }

    public JTextArea getBailConditionText() {
        if (bailConditionsText == null) {
            bailConditionsText = JTextAreaFactory.getTextArea();
            Document doc = DocumentFactory.newDocument(new Capability[] { Capability
                    .utf8LimitedTextCapability(DIRECTIONS_TEXT_LIMIT) });
            bailConditionsText.setDocument(doc);
            bailConditionsText.addKeyListener(new UpdateStateKeyListener(this));
            bailConditionsText.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if (!isActivating())
                        clearWarning(padGroup);
                }
            });
        }

        return bailConditionsText;
    }

    public JLabel getBailConditionLabel() {
        if (bailConditionsLabel == null) {
            JLabel jl = new JLabel();
            jl.setText(XHIBITConstant.getResource(XhibitBundles.Directions, "BailNewConditions"));
            bailConditionsLabel = jl;
        }

        return bailConditionsLabel;
    }

    public void setModel(DirectionsForDefendantValue[] newModel) {
        model = newModel;
        // dfv = newModel.getDirectionsForDefendantBasicValue();
    }

    public void stepValidate() {
    }

    public void stepUpdateViewState() {
        JRadioButton selectedRB = PDHConstants.getSelectedRadio(padGroup);
        boolean enable = false;
        if (selectedRB != null && selectedRB.getClientProperty(PDHConstants.dbCode) != null) {
            if (selectedRB.getClientProperty(PDHConstants.xmlCode).equals(listenButton)) {
                enable = true;
            }
        }

        getBailConditionLabel().setEnabled(enable);
        getBailConditionText().setEnabled(enable);
    }

    private boolean isModelsIdentical() {
        if (model.length <= 1)
            return true;

        for (int i = 0; i < model.length - 1; i++) {
            XhbDirectionsForDefendantBasicValue dfd1 = model[i].getDirectionsForDefendantBasicValue();
            XhbDirectionsForDefendantBasicValue dfd2 = model[i + 1].getDirectionsForDefendantBasicValue();
            if ((dfd1.getBailStatus() == null && dfd2.getBailStatus() != null)
                    || (dfd1.getBailStatus() != null && !dfd1.getBailStatus().equals(dfd2.getBailStatus()))) {
                return false;
            } else {
                if ((dfd1.getNewBailConditions() == null && dfd2.getNewBailConditions() != null)
                        || (dfd1.getNewBailConditions() != null && !dfd1.getNewBailConditions().equals(
                                dfd2.getNewBailConditions()))) {
                    return false;
                }
            }
        }
        return true;
    }

    protected void setWarningVisible(ButtonGroup buttonGroup, boolean visible) {
        super.setWarningVisible(buttonGroup, visible);
        getBailConditionText().setBackground(visible ? PDHConstants.DISABLED_COLOR : PDHConstants.TEXT_AREA_BACKGROUND);
    }
}
