package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRDefendantValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingrecord.HRLinkedCaseListValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
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
 * @author Sherie De Silva
 * @version 1.0
 */

public class CaseGeneral extends JPanel {

    private static final long serialVersionUID = 1L;

    private HearingRecordModel model;

    private JPanel defPanel;

    private JPanel casePanel;

    private JPanel ccPanel;

    private JTextField firstNameText;

    private JTextField middleInitialText;

    private JTextField surnameText;

    private JTextField caseNumberText;

    // private JTextField linkedText;
    private JList linkedList;

    private JTextField ccNameText;

    public CaseGeneral(HearingRecordModel model) {
        this.model = model;
        jbInit();
    }

    public void jbInit() {
        // this.setLayout(new GridBagLayout());

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new GridBagLayout());

        defPanel = new JPanel();
        defPanel.setLayout(new GridBagLayout());
        if (model.isAppealType()) {
            defPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "appellant")));
        } else {
            defPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),
                    XHIBITConstant.getResource(XhibitBundles.HearingRecord, "defendant")));
        }

        final int textFieldWidth = 40;
        firstNameText = new JTextField(textFieldWidth);
        firstNameText.setEditable(false);
        middleInitialText = new JTextField(textFieldWidth);
        middleInitialText.setEditable(false);
        surnameText = new JTextField(textFieldWidth);
        surnameText.setEditable(false);

        defPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "firstName")),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        defPanel.add(firstNameText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        defPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "middleName")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        defPanel.add(middleInitialText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        defPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "lastName")),
                new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        defPanel.add(surnameText, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        containerPanel.add(defPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        casePanel = new JPanel();
        casePanel.setLayout(new GridBagLayout());
        casePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), XHIBITConstant
                .getResource(XhibitBundles.HearingRecord, "case")));

        caseNumberText = new JTextField(12);
        caseNumberText.setEditable(false);
        // linkedText = new JTextField(30);
        // linkedText.setEditable(false);
        linkedList = new JList();
        linkedList.setMaximumSize(new Dimension(120, 70));
        linkedList.setMinimumSize(new Dimension(120, 70));
        linkedList.setPreferredSize(new Dimension(120, 70));
        linkedList.setVisible(true);
        linkedList.setEnabled(false);

        casePanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "number")),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        casePanel.add(caseNumberText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        casePanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "linked")),
                new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHEAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        casePanel.add(linkedList, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

        containerPanel.add(casePanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

        ccPanel = new JPanel();
        ccPanel.setLayout(new GridBagLayout());
        ccPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), XHIBITConstant
                .getResource(XhibitBundles.HearingRecord, "courtClerk")));

        ccNameText = new JTextField(textFieldWidth);
        ccNameText.setEditable(false);

        ccPanel.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "name")),
                new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE,
                        new Insets(4, 4, 4, 4), 0, 0));
        ccPanel.add(ccNameText, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

        containerPanel.add(ccPanel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));

        // this.add(containerPanel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
        // GridBagConstraints.NORTH, GridBagConstraints.NONE, new Insets(4, 4,
        // 4, 4), 0, 0));
        this.add(containerPanel);
    }

    public void populateScreen() {
        HRDefendantValue defendantVal = model.getHearingRecordVal().getHearingRecordDisplayValue()
                .getHrDefendantValue();

        this.firstNameText.setText(defendantVal.getFirstName());
        this.middleInitialText.setText(defendantVal.getMiddleName());
        this.surnameText.setText(defendantVal.getSurname());

        String caseNo = model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCaseValue().getCaseType()
                + model.getHearingRecordVal().getHearingRecordDisplayValue().getHrCaseValue().getCaseNumber();
        this.caseNumberText.setText(caseNo);

        Vector linkedCasesVec = (Vector) model.getHearingRecordVal().getHearingRecordDisplayValue()
                .getHrLinkedCaseListValue();
        Vector<String> lcData = new Vector<String>();
        if (linkedCasesVec != null) {
            for (int i = 0; i < linkedCasesVec.size(); i++) {
                String s = ((HRLinkedCaseListValue) (linkedCasesVec.get(i))).getCaseType()
                        + ((HRLinkedCaseListValue) (linkedCasesVec.get(i))).getCaseNumber();
                lcData.add(s);
            }
        }
        this.linkedList.setListData(lcData);
        this.linkedList.repaint();

        this.ccNameText.setText(model.getCourtClerk());
    }

}