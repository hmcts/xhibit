package uk.gov.courtservice.xhibit.client.schedule.movecase;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT Iteration 2
 * </p>
 * <p>
 * Description: Show Case Detail non-editable fields
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
public class CaseDetailsPanel extends JPanel {
    private JLabel shortWriterLabel;

    private JLabel defendantLabel;

    private JLabel timeListedLabel;

    private JLabel judgeLabel;

    private JLabel prosAdvocLabel;

    private JLabel currCaseLabel;

    private JLabel hearingTypeLabel;

    private JLabel defAdvocLabel;

    public JTextField hearingTypeText;

    public JTextField shortWriterText;

    public JTextField timeListedText;

    public JTextField currCaseText;

    public JTextField judgeText;

    // public JList judgeList;
    public JList prosAdvocList;

    public JList defendantList;

    public JList defAdvocList;

    // private JScrollPane judgeScrollPane;
    private JScrollPane prosAdvocScrollPane;

    private JScrollPane defendantScrollPane;

    private JScrollPane defAdvocScrollPane;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private TitledBorder titledBorder1;

    private ResourceBundle myResource;

    private boolean isAppeal;

    public CaseDetailsPanel(ResourceBundle resource, boolean isAppeal) {

        this.myResource = resource;
        this.isAppeal = isAppeal;
        jbInit();
        // setTemporaryInformation();
    }

    void jbInit() {
        this.setLayout(gridBagLayout1);

        titledBorder1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)),
                XHIBITConstant.getResource(myResource, "caseDetails"));

        this.setBorder(titledBorder1);

        this.add(getCurrCaseLabel(), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 0), 0, 0));
        this.add(getJudgeLabel(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 0), 0, 0));
        this.add(getProsAdvocLabel(), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.35, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 0), 0, 0));
        this.add(getDefendantLabel(), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.65, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        this.add(getCurrCaseText(), new GridBagConstraints(1, 1, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getJudgeText(), new GridBagConstraints(1, 2, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getProsAdvocScrollPane(), new GridBagConstraints(1, 3, 1, 1, 0.5, 0.35, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDefendantScrollPane(), new GridBagConstraints(1, 4, 1, 1, 0.5, 0.65, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));

        this.add(getHearingTypeLabel(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 8, 4, 4), 0, 0));
        this.add(getTimeListedLabel(), new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 8, 4, 4), 0, 0));
        this.add(getShortWriterLabel(), new GridBagConstraints(3, 3, 1, 1, 0.0, 0.35, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 8, 4, 4), 0, 0));
        this.add(getDefAdvocLabel(), new GridBagConstraints(3, 4, 1, 1, 0.0, 0.65, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, new Insets(4, 8, 4, 0), 0, 0));

        this.add(getHearingTypeText(), new GridBagConstraints(4, 1, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getTimeListedText(), new GridBagConstraints(4, 2, 1, 1, 0.5, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getShortWriterText(), new GridBagConstraints(4, 3, 1, 1, 0.5, 0.35, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getDefAdvocScrollPane(), new GridBagConstraints(4, 4, 1, 1, 0.5, 0.65, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));
    }

    private JLabel getCurrCaseLabel() {
        if (currCaseLabel == null) {
            currCaseLabel = new JLabel(XHIBITConstant.getResource(myResource, "currentCase"));
            currCaseLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            currCaseLabel.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
        }
        return currCaseLabel;
    }

    private JLabel getHearingTypeLabel() {
        if (hearingTypeLabel == null) {
            hearingTypeLabel = new JLabel(XHIBITConstant.getResource(myResource, "hearingType"));
            hearingTypeLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeLabel.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
        }
        return hearingTypeLabel;
    }

    private JLabel getJudgeLabel() {
        if (judgeLabel == null) {
            judgeLabel = new JLabel(XHIBITConstant.getResource(myResource, "judge"));
            judgeLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            judgeLabel.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
        }
        return judgeLabel;
    }

    private JLabel getTimeListedLabel() {
        if (timeListedLabel == null) {
            timeListedLabel = new JLabel(XHIBITConstant.getResource(myResource, "timeListed"));
            timeListedLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            timeListedLabel.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
        }
        return timeListedLabel;
    }

    private JLabel getProsAdvocLabel() {
        if (prosAdvocLabel == null) {
            String prosAdvocLabelText;
            if (isAppeal) {
                // prosAdvocLabelText = XHIBITConstant.getResource(myResource,
                // "appellantAdvocate");
                prosAdvocLabelText = XHIBITConstant.getResource(myResource, "respondentAdvocate");
            } else {
                prosAdvocLabelText = XHIBITConstant.getResource(myResource, "prosecutionAdvocate");
            }
            prosAdvocLabel = new JLabel(prosAdvocLabelText);
            prosAdvocLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            prosAdvocLabel.setPreferredSize(new Dimension(130, XHIBITConstant.getLineHeight()));
        }
        return prosAdvocLabel;
    }

    private JLabel getShortWriterLabel() {
        if (shortWriterLabel == null) {
            shortWriterLabel = new JLabel(XHIBITConstant.getResource(myResource, "shorthandWriter1"));
            shortWriterLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            shortWriterLabel.setPreferredSize(new Dimension(110, XHIBITConstant.getLineHeight()));
        }
        return shortWriterLabel;
    }

    private JLabel getDefendantLabel() {
        if (defendantLabel == null) {
            String defendantLabelText;
            if (isAppeal) {
                defendantLabelText = XHIBITConstant.getResource(myResource, "appellant");
            } else {
                defendantLabelText = XHIBITConstant.getResource(myResource, "defendants");
            }
            defendantLabel = new JLabel(defendantLabelText);
            defendantLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            defendantLabel.setPreferredSize(new Dimension(100, XHIBITConstant.getLineHeight()));
        }
        return defendantLabel;
    }

    private JLabel getDefAdvocLabel() {
        if (defAdvocLabel == null) {
            String defAdvocLabelText;
            if (isAppeal) {
                // defAdvocLabelText = XHIBITConstant.getResource(myResource,
                // "respondentAdvocate");
                defAdvocLabelText = XHIBITConstant.getResource(myResource, "appellantAdvocate");
            } else {
                defAdvocLabelText = XHIBITConstant.getResource(myResource, "defenceAdvocate");
            }
            defAdvocLabel = new JLabel(defAdvocLabelText);
            defAdvocLabel.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            defAdvocLabel.setPreferredSize(new Dimension(130, XHIBITConstant.getLineHeight()));
        }
        return defAdvocLabel;
    }

    public JTextField getCurrCaseText() {
        if (currCaseText == null) {
            currCaseText = new JTextField();
            currCaseText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            currCaseText.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight()));
        }
        return currCaseText;
    }

    public JTextField getHearingTypeText() {
        if (hearingTypeText == null) {
            hearingTypeText = new JTextField();
            hearingTypeText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            hearingTypeText.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight()));
        }
        return hearingTypeText;
    }

    public JTextField getTimeListedText() {
        if (timeListedText == null) {
            timeListedText = new JTextField();
            timeListedText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            timeListedText.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight()));
        }
        return timeListedText;
    }

    public JTextField getShortWriterText() {
        if (shortWriterText == null) {
            shortWriterText = new JTextField();
            shortWriterText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            shortWriterText.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight()));
        }
        return shortWriterText;
    }

    public JTextField getJudgeText() {
        if (judgeText == null) {
            judgeText = new JTextField();
            judgeText.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            judgeText.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight()));
        }
        return judgeText;
    }

    /** @todo FOR LISTS NEED MODEL */
    // = new JList(new DefaultListModel());?????
    /**
     * @todo Set number of rows visible in each list before scrollbar appears
     */
    /*
     * lstProsAdvoc.setVisibleRowCount(2); lstDefendant.setVisibleRowCount(6);
     * lstDefAdvoc.setVisibleRowCount(2);
     */

    // public JList getJudgeList()
    // {
    // if( judgeList == null)
    // {
    // judgeList= new JList();
    // }
    // return judgeList;
    // }
    public JList getProsAdvocList() {
        if (prosAdvocList == null) {
            prosAdvocList = new JList();
        }
        return prosAdvocList;
    }

    public JList getDefendantList() {
        if (defendantList == null) {
            defendantList = new JList();
        }
        return defendantList;
    }

    public JList getDefAdvocList() {
        if (defAdvocList == null) {
            defAdvocList = new JList();
        }
        return defAdvocList;
    }

    // public JScrollPane getJudgeScrollPane()
    // {
    // if(judgeScrollPane == null)
    // {
    // judgeScrollPane = new JScrollPane(getJudgeList());
    // judgeScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    // judgeScrollPane.setMinimumSize(new Dimension(50,
    // XHIBITConstant.getLineHeight()));
    // judgeScrollPane.setPreferredSize(new Dimension(70,
    // XHIBITConstant.getLineHeight() *2));
    // judgeScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
    // }
    // return judgeScrollPane;
    // }

    public JScrollPane getProsAdvocScrollPane() {
        if (prosAdvocScrollPane == null) {
            prosAdvocScrollPane = new JScrollPane(getProsAdvocList());
            prosAdvocScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            prosAdvocScrollPane.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            prosAdvocScrollPane.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight() * 2));
            // prosAdvocScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return prosAdvocScrollPane;
    }

    public JScrollPane getDefendantScrollPane() {
        if (defendantScrollPane == null) {
            defendantScrollPane = new JScrollPane(getDefendantList());
            // defendantScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            defendantScrollPane.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            defendantScrollPane.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight() * 6));
            // defendantScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return defendantScrollPane;
    }

    public JScrollPane getDefAdvocScrollPane() {
        if (defAdvocScrollPane == null) {
            defAdvocScrollPane = new JScrollPane(getDefAdvocList());
            defAdvocScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            defAdvocScrollPane.setMinimumSize(new Dimension(50, XHIBITConstant.getLineHeight()));
            defAdvocScrollPane.setPreferredSize(new Dimension(70, XHIBITConstant.getLineHeight() * 6));
            // defAdvocScrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        }
        return defAdvocScrollPane;
    }

    public void enableFields(boolean enable) {
        getHearingTypeText().setEnabled(enable);
        getShortWriterText().setEnabled(enable);
        getTimeListedText().setEnabled(enable);
        getCurrCaseText().setEnabled(enable);
        getJudgeText().setEnabled(enable);
        // getJudgeList().setEnabled(enable);
        getProsAdvocList().setEnabled(enable);
        getDefendantList().setEnabled(enable);
        getDefAdvocList().setEnabled(enable);
    }
}