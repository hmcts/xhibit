package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.PanelTitleLabel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: Xhibit2
 * </p>
 * <p>
 * Description: Court Services Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author David Crossland
 * @version 1.0
 */
public class LegalArgumentOptionsPanel extends CourtLogEventPanel {
    private LegalArgumentOptionsModel model;

    private JLabel panelTitleLabel;

    private JTextArea pajTextArea;

    private JTextArea dajTextArea;

    private JTextArea jrTextArea;

    private JCheckBox publicNotPermittedCbx;

    private final ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.LegalArgumentOptions);

    private Dimension labelDim = new Dimension(175, XHIBITConstant.getLineHeight());

    private Dimension panelDim = new Dimension(260, 40);

    private JLabel pajLabel;

    private JLabel dajLabel;

    private JLabel jrLabel;

    private String lblLAO_PAJ = "lblLAO_PAJ";

    private String lblLAO_DAJ = "lblLAO_DAJ";

    private String ttLAO_PAJText = "ttLAO_PAJText";

    private String ttLAO_DAJText = "ttLAO_DAJText";

    public LegalArgumentOptionsPanel(XDialog parent, LegalArgumentOptionsModel model) throws CSRecoverableException {
        super(parent, model);
        this.model = model;

        // alternative labels and tool tips for Options when case type is an
        // appeal
        if (model.getEventType().equals("20611")) {
            lblLAO_PAJ = "lblLAO_PAJ_A";
            lblLAO_DAJ = "lblLAO_DAJ_A";
            ttLAO_PAJText = "ttLAO_PAJText_A";
            ttLAO_DAJText = "ttLAO_DAJText_A";
        }

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        this.setMinimumSize(new Dimension(440, 280));

        final JScrollPane jrScrollPane = new JScrollPane();
        final JScrollPane dajScrollPane = new JScrollPane();
        final JScrollPane pajScrollPane = new JScrollPane();

        jrScrollPane.setMinimumSize(panelDim);
        jrScrollPane.setPreferredSize(panelDim);
        jrScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jrScrollPane.setAutoscrolls(true);

        dajScrollPane.setMinimumSize(panelDim);
        dajScrollPane.setPreferredSize(panelDim);
        dajScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        dajScrollPane.setAutoscrolls(true);

        pajScrollPane.setMinimumSize(panelDim);
        pajScrollPane.setPreferredSize(panelDim);
        pajScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        pajScrollPane.setAutoscrolls(true);

        pajScrollPane.getViewport().add(getPAJTextArea(), null);
        jrScrollPane.getViewport().add(getJRTextArea(), null);
        dajScrollPane.getViewport().add(getDAJTextArea(), null);

        this.add(getPanelTitleLabel(), new GridBagConstraints(0, 0, 2, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 20));
        this.add(getPAJLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(pajScrollPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getDAJLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(dajScrollPane, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getJRLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(jrScrollPane, new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getPublicNotPermittedCbx(), new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getLogAuditPanel(), new GridBagConstraints(0, 5, 2, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    protected void populateModelProperties(Map propertyMap) throws CSRecoverableException {
        // Save the data in the model
        if (model.getEventType().equals("20611")) {
            TreeMap treeMap = (TreeMap) propertyMap.get("E20611_Legal_Argument_Options");
            model
                    .setPublicNotPermitted(((String) treeMap.get("E20611_Opt1_Public_Not_Permitted_To_Enter"))
                            .equals("1"));
            model.setJRText((String) treeMap.get("E20611_Opt2_Judges_Ruling"));
            model.setDAJText((String) treeMap.get("E20611_Opt3_Appellant_Addresses_Judge"));
            model.setPAJText((String) treeMap.get("E20611_Opt4_Respondent_Addresses_Judge"));
        } else if (model.getEventType().equals("20916")) {
            TreeMap treeMap = (TreeMap) propertyMap.get("E20916_Legal_Argument_Options");
            model
                    .setPublicNotPermitted(((String) treeMap.get("E20916_Opt1_Public_Not_Permitted_To_Enter"))
                            .equals("1"));
            model.setJRText((String) treeMap.get("E20916_Opt2_Judges_Ruling"));
            model.setDAJText((String) treeMap.get("E20916_Opt3_Defence_Addresses_Judge"));
            model.setPAJText((String) treeMap.get("E20916_Opt4_Prosecution_Addresses_Judge"));
        } else if (model.getEventType().equals("20611")) {
            TreeMap treeMap = (TreeMap) propertyMap.get("E21100_Legal_Argument_Options");
            model
                    .setPublicNotPermitted(((String) treeMap.get("E21100_Opt1_Public_Not_Permitted_To_Enter"))
                            .equals("1"));
            model.setJRText((String) treeMap.get("E21100_Opt2_Judges_Ruling"));
            model.setDAJText((String) treeMap.get("E21100_Opt3_Defence_Addresses_Judge"));
            model.setPAJText((String) treeMap.get("E21100_Opt4_Prosecution_Addresses_Judge"));
        }
    }

    protected void populateCRUDProperties(Map propertyMap) throws CSRecoverableException {
        TreeMap laoOptionsType = new TreeMap();

        String pNP = model.getPublicNotPermitted() ? "1" : "0";
        log.debug(" pNP is : " + pNP.toString());

        if (model.getEventType().equals("20611")) {
            laoOptionsType.put("E20611_Opt1_Public_Not_Permitted_To_Enter", pNP);
            laoOptionsType.put("E20611_Opt2_Judges_Ruling", model.getJRText());
            laoOptionsType.put("E20611_Opt3_Appellant_Addresses_Judge", model.getDAJText());
            laoOptionsType.put("E20611_Opt4_Respondent_Addresses_Judge", model.getPAJText());
            propertyMap.put("E20611_Legal_Argument_Options", laoOptionsType);
        } else if (model.getEventType().equals("20916")) {
            laoOptionsType.put("E20916_Opt1_Public_Not_Permitted_To_Enter", pNP);
            laoOptionsType.put("E20916_Opt2_Judges_Ruling", model.getJRText());
            laoOptionsType.put("E20916_Opt3_Defence_Addresses_Judge", model.getDAJText());
            laoOptionsType.put("E20916_Opt4_Prosecution_Addresses_Judge", model.getPAJText());
            propertyMap.put("E20916_Legal_Argument_Options", laoOptionsType);
        } else if (model.getEventType().equals("21100")) {
            laoOptionsType.put("E21100_Opt1_Public_Not_Permitted_To_Enter", pNP);
            laoOptionsType.put("E21100_Opt2_Judges_Ruling", model.getJRText());
            laoOptionsType.put("E21100_Opt3_Defence_Addresses_Judge", model.getDAJText());
            laoOptionsType.put("E21100_Opt4_Prosecution_Addresses_Judge", model.getPAJText());
            propertyMap.put("E21100_Legal_Argument_Options", laoOptionsType);
        }
    }

    protected boolean isMandatoryFieldsCompleted() {
        return !(pajTextArea.getText().equals("")) || !(dajTextArea.getText().equals(""))
                || !(jrTextArea.getText().equals(""))
                // || !(freeTextPanel.getFreeTextArea().getText().equals(""))
                // to do - or Cbx is changed ?????
                || publicNotPermittedCbx.isSelected();
    }

    private JLabel getPanelTitleLabel() {
        if (panelTitleLabel == null) {
            panelTitleLabel = new PanelTitleLabel(model.getPanelText());
        }
        return panelTitleLabel;
    }

    private JTextArea getStandardTextArea() {
        JTextArea jta = new JTextArea();
        jta.setPreferredSize(new Dimension(260, 40));
        jta.setMinimumSize(new Dimension(260, 40));
        jta.setBorder(null);
        jta.setLineWrap(true);
        jta.setWrapStyleWord(true);
        jta.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                stepUpdateViewStateHandleExceptions();
            }
        });
        return jta;
    }

    private JTextArea getPAJTextArea() {
        if (pajTextArea == null) {
            pajTextArea = getStandardTextArea();
            pajTextArea.setToolTipText(XHIBITConstant.getResource(resources, ttLAO_PAJText));
        }
        return pajTextArea;
    }

    private JTextArea getDAJTextArea() {
        if (dajTextArea == null) {
            dajTextArea = getStandardTextArea();
            dajTextArea.setToolTipText(XHIBITConstant.getResource(resources, ttLAO_DAJText));
        }
        return dajTextArea;
    }

    private JTextArea getJRTextArea() {
        if (jrTextArea == null) {
            jrTextArea = getStandardTextArea();
            jrTextArea.setToolTipText(XHIBITConstant.getResource(resources, "ttLAO_JRText"));
        }
        return jrTextArea;
    }

    private JCheckBox getPublicNotPermittedCbx() {
        if (publicNotPermittedCbx == null) {
            publicNotPermittedCbx = new JCheckBox();
            publicNotPermittedCbx.setSelected(model.getPublicNotPermitted());
            publicNotPermittedCbx.setText(XHIBITConstant.getResource(resources, "lblLAO_pnpCbx"));
            publicNotPermittedCbx.setToolTipText(XHIBITConstant.getResource(resources, "ttLAO_pnpCbx"));
            publicNotPermittedCbx.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    stepUpdateViewStateHandleExceptions();
                }
            });
        }
        return publicNotPermittedCbx;
    }

    private JLabel getPAJLabel() {
        if (pajLabel == null) {
            pajLabel = new JLabel();
            pajLabel.setText(XHIBITConstant.getResource(resources, lblLAO_PAJ));
            pajLabel.setPreferredSize(labelDim);
        }
        return pajLabel;
    }

    private JLabel getDAJLabel() {
        if (dajLabel == null) {
            dajLabel = new JLabel();
            dajLabel.setText(XHIBITConstant.getResource(resources, lblLAO_DAJ));
            dajLabel.setPreferredSize(labelDim);
        }
        return dajLabel;
    }

    private JLabel getJRLabel() {
        if (jrLabel == null) {
            jrLabel = new JLabel();
            jrLabel.setText(XHIBITConstant.getResource(resources, "lblLAO_JR"));
            jrLabel.setPreferredSize(labelDim);
        }
        return jrLabel;
    }

    protected void moveModelToScreen() {
        super.moveModelToScreen();
        if (model.isInEditMode()) {

            pajTextArea.setText(model.getPAJText());
            dajTextArea.setText(model.getDAJText());
            jrTextArea.setText(model.getJRText());
            publicNotPermittedCbx.setSelected(model.getPublicNotPermitted());
        }
    }

    protected void moveScreenToModel() throws CSRecoverableException {
        super.moveScreenToModel();
        model.setPAJText(pajTextArea.getText());
        model.setDAJText(dajTextArea.getText());
        model.setJRText(jrTextArea.getText());
        model.setPublicNotPermitted(publicNotPermittedCbx.isSelected());
    }
}
