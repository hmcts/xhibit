package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.SystemColor;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIDefaults;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitHelper;

/**
 * <p>
 * Title: XHIBIT 2 -
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
 * @author Rakesh Lakhani
 * @version 1.0
 */
public class HearingPanel extends JPanel {
    private static final int defendantRows = 5;

    private static final int defendantScrollDefaultHeight = (XHIBITConstant.getLineHeight() * defendantRows) + 10;

    private static final int maxDefSize = (XHIBITConstant.getLineHeight() * 10) + 10;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel courtLabel = new JLabel();

    private JLabel courtText = null;

    private JLabel caseLabel = new JLabel();

    private JLabel caseText = null;

    private JLabel judgeLabel = new JLabel();

    private JLabel judgeText = null;

    private JLabel hearingTypeLabel = new JLabel();

    private JLabel hearingTypeText = null;

    private JLabel defendantsLabel = new JLabel();

    private JTextArea defendantsText = null;

    private JLabel timeLabel = new JLabel();

    private JLabel timeText = null;

    private JScrollPane defendantScrollPane = null;

    private JLabel floatingLable = new JLabel();

    private JLabel floatingText = new JLabel();

    private uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue shv = null;

    private JLabel lblCaseStatus = new JLabel();

    private JLabel caseStatus = new JLabel();

    private JLabel caseMovedStatus = new JLabel();

    private ResourceBundle rb;

    private static final Logger log = CSServices.getLogger(HearingPanel.class);

    public HearingPanel() {
        rb = XHIBITConstant.getResourceBundle(XhibitBundles.TodaysSchedule);
        jbInit();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);

        courtLabel.setText(rb.getString("courtColumn") + ":");
        caseLabel.setText(rb.getString("caseColumn") + ":");
        judgeLabel.setText(rb.getString("judgeColumn") + ":");
        hearingTypeLabel.setText(rb.getString("hearingTypeColumn") + ":");
        defendantsLabel.setText(getDefendantLabelText(shv));
        timeLabel.setText(rb.getString("notBeforeColumn") + ":");
        lblCaseStatus.setText(rb.getString("caseStatusColumn") + ":");
        floatingLable.setText(rb.getString("floatingCase"));

        this.add(courtLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCourtText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(caseLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCaseText(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(judgeLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getJudgeText(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(defendantsLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        // this.add(getDefendantsText(), new GridBagConstraints(1, 3, 1, 1, 0.0,
        // 0.0
        // ,GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
        // XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getDefendantsDisplay(), new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(hearingTypeLabel, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getHearingTypeText(), new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(timeLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getTimeText(), new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(floatingLable, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(floatingText, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(lblCaseStatus, new GridBagConstraints(0, 7, 1, 2, 0.0, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(caseStatus, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(caseMovedStatus, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
    }

    private JLabel getCourtText() {

        if (courtText == null) {
            courtText = new JLabel();
            courtText.setMinimumSize(new Dimension(100, 21));
            courtText.setPreferredSize(new Dimension(100, 21));
        }
        return courtText;
    }

    private JLabel getCaseText() {

        if (caseText == null) {
            caseText = new JLabel();
            caseText.setMinimumSize(new Dimension(100, 21));
            caseText.setPreferredSize(new Dimension(100, 21));
        }
        return caseText;
    }

    private JLabel getJudgeText() {

        if (judgeText == null) {
            judgeText = new JLabel();
            judgeText.setPreferredSize(new Dimension(200, 21));
            judgeText.setMinimumSize(new Dimension(200, 21));
        }
        return judgeText;
    }

    private JScrollPane getDefendantsDisplay() {
        if (defendantScrollPane == null) {
            defendantScrollPane = new JScrollPane();
            defendantScrollPane.setPreferredSize(new Dimension(200, defendantScrollDefaultHeight));
            defendantScrollPane.add(getDefendantsText());
            defendantScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            defendantScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        }
        return defendantScrollPane;
    }

    private JTextArea getDefendantsText() {
        if (defendantsText == null) {
            defendantsText = new JTextArea();
            // defendantsText.setRows(5);
            // defendantsText.setPreferredSize(new Dimension(200,
            // XHIBITConstant.getLineHeight() * defendantRows));
            defendantsText.setMinimumSize(new Dimension(200, XHIBITConstant.getLineHeight() * defendantRows));
            defendantsText.setEditable(false);
            defendantsText.setBackground(SystemColor.control);
            UIDefaults defaults = UIManager.getDefaults();
            Font f = defaults.getFont("OptionPane.font");
            defendantsText.setFont(f);
        }
        return defendantsText;
    }

    private JLabel getHearingTypeText() {

        if (hearingTypeText == null) {
            hearingTypeText = new JLabel();
            hearingTypeText.setMinimumSize(new Dimension(200, 21));
            hearingTypeText.setPreferredSize(new Dimension(200, 21));
        }
        return hearingTypeText;
    }

    private JLabel getTimeText() {

        if (timeText == null) {
            timeText = new JLabel();
            timeText.setMinimumSize(new Dimension(100, 21));
            timeText.setPreferredSize(new Dimension(100, 21));
        }
        return timeText;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue getSHV() {
        return shv;
    }

    public void setSHV(uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue shv) {
        this.shv = shv;
    }

    private Dimension getDefendantScrollDimension() {
        int width = getDefendantsText().getPreferredSize().width
                + getDefendantsDisplay().getVerticalScrollBar().getWidth() + 10;
        int height = Math.min(Math
                .max(getDefendantsText().getPreferredSize().height + 10, defendantScrollDefaultHeight), maxDefSize);
        return new Dimension(width, height);
    }

    public void setVisible(boolean isVisible) {
        if (isVisible) {
            if (shv != null) {
                Boolean isFloating = shv.getIsFloating();
                if (isFloating != null && isFloating.booleanValue()) {
                    getCourtText().setText(XHIBITConstant.getResource(rb, "unassigned"));
                } else {
                    getCourtText().setText(checkNull(shv.getCourtRoomName()));
                }
                getCaseText().setText(checkNull(shv.getCaseType()) + checkNull(shv.getCaseNumber().toString()));
                getJudgeText().setText(checkNull(shv.getJudge()));

                // build the label again since this label can change depending
                // on
                // case type
                defendantsLabel.setText(getDefendantLabelText(shv));
                getDefendantsText().setText(
                        ScheduleHelper.getDefendantString(shv.getDefendants(), shv.getCaseBasicValue()));
                getDefendantsDisplay().getViewport().remove(getDefendantsText());
                getDefendantsDisplay().getViewport().add(getDefendantsText());
                getDefendantsText().setCaretPosition(0);
                getDefendantsDisplay().setPreferredSize(getDefendantScrollDimension());
                getDefendantsDisplay().revalidate();

                getHearingTypeText().setText(checkNull(shv.getHearingTypeDesc()));
                getTimeText().setText(XDateFormat.format(shv.getNotBeforeTime(), XDateFormat.TIMEFORMAT));
                caseStatus.setText(XhibitHelper.getHearingProgress(shv.getHearingProgress()));
                if (checkNull(shv.getMovedFromCourtRoomName()).length() > 0) {
                    caseMovedStatus.setText(rb.getString("movedCourt") + " " + shv.getMovedFromCourtRoomName());
                } else {
                    caseMovedStatus.setText("");
                }
                if (shv.getIsFloating() != null) {
                    floatingText.setText(shv.getIsFloating().booleanValue() ? rb.getString("floatingTrue") : rb
                            .getString("floatingFalse"));
                } else {
                    floatingText.setText(rb.getString("floatingTrue"));
                }
            }
        }
        super.setVisible(isVisible);
    }

    /**
     * Create a dynamic Defendant label. It will be set to case title for U & B
     * cases where no defendants exist, else defendants.
     * 
     * @param shv
     *            ScheduledHearingValue
     * @return String - the label
     */
    private String getDefendantLabelText(ScheduledHearingValue shv) {
        String label = getDefaultDefendantLabel();

        // check so ScheduledHearingValue, case and casetype are not null
        if (shv != null && shv.getCaseBasicValue() != null && shv.getCaseBasicValue().getCaseType() != null) {
            try {
                if (XHIBITConstant.isUndefined_CaseType(shv) || XHIBITConstant.isBail_CaseType(shv)) {
                    label = rb.getString("caseTitleColumn") + ":";
                } else if (XHIBITConstant.isCriminalAppeal_CaseType(shv)
                        || XHIBITConstant.isMiscelleanousAppeal_CaseType(shv)) {
                    label = rb.getString("appellantColumn") + ":";
                }
            } catch (MissingResourceException ex) { // ignore and use
                // default
                log.info(ex, ex);
            } catch (UnknownCaseTypeException ex) { // ignore and use
                // default
                log.info(ex, ex);
            }
        }
        return label;
    }

    private String getDefaultDefendantLabel() {
        return rb.getString("defendantColumn") + ":";
    }

    private String checkNull(String toCheck) {
        if (toCheck == null)
            return "";
        else
            return toCheck;
    }

}