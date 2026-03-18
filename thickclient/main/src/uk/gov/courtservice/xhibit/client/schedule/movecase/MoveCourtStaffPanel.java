package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT Prototype
 * </p>
 * <p>
 * Description: Move Court Staff selection panel
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

public class MoveCourtStaffPanel extends JPanel {
    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JCheckBox shortWriterCbx;

    private JCheckBox courtClerkCbx;

    private JCheckBox judgeCbx;

    private TitledBorder titledBorder1;

    private ResourceBundle myResource;

    public MoveCourtStaffPanel(ResourceBundle resource) {

        myResource = resource;
        jbInit();

    }

    void jbInit() {
        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)),
                XHIBITConstant.getResource(myResource, "moveCourtStaff"));
        this.setLayout(gridBagLayout1);
        this.setBorder(titledBorder1);
        this.add(getJudgeCbx(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 1, 1));
        this.add(getShortWriterCbx(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 1, 1));
        this.add(getCourtClerkCbx(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 1, 1));
    }

    public JCheckBox getShortWriterCbx() {
        if (shortWriterCbx == null) {
            shortWriterCbx = new JCheckBox(XHIBITConstant.getResource(myResource, "shorthandWriter2"));
            shortWriterCbx.setToolTipText(XHIBITConstant.getResource(myResource, "ttShorthandWriterCbx"));
        }
        return shortWriterCbx;
    }

    public JCheckBox getCourtClerkCbx() {
        if (courtClerkCbx == null) {
            courtClerkCbx = new JCheckBox(XHIBITConstant.getResource(myResource, "courtClerk"));
            courtClerkCbx.setToolTipText(XHIBITConstant.getResource(myResource, "ttCourtClerkCbx"));
        }
        return courtClerkCbx;
    }

    public JCheckBox getJudgeCbx() {
        if (judgeCbx == null) {
            judgeCbx = new JCheckBox(XHIBITConstant.getResource(myResource, "judgeColumn"));
            judgeCbx.setToolTipText(XHIBITConstant.getResource(myResource, "ttJudgeCbx"));
        }
        return judgeCbx;
    }
}