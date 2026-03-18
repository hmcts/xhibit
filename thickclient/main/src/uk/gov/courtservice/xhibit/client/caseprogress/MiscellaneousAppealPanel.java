package uk.gov.courtservice.xhibit.client.caseprogress;

// Java
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
 * @author unascribed
 * @version 1.0
 */
public class MiscellaneousAppealPanel extends JPanel {
    private JLabel appealTitleLabel = null;

    private JLabel appealTypeLabel = null;

    private JLabel appellantLabel = null;

    private JLabel resultsLabel = null;

    private JLabel resultsDateLabel = null;

    private JLabel hearingStartLabel = null;

    private JLabel hearingDurationLabel = null;

    private JLabel transferredCourtLabel = null;

    private JLabel appealTitleTextField = null;

    private JLabel appealTypeTextField = null;

    private JLabel appellantTextField = null;

    private JLabel resultsTextArea = null;

    private JLabel resultDateTextField = null;

    private JLabel hearingStartTextField = null;

    private JLabel hearingDurationTextField = null;

    private JLabel transferredCourtTextField = null;

    private String appTitle;

    private String appType;

    private String appName;

    private String appResult;

    private String appResultDate;

    private String appHearingStartDate;

    private Long appHearingDuration;

    private String transferredCourtDetails;

    private ResourceBundle resources;

    /**
     * Default Constructor.
     * 
     * @param appealTitle
     * @param appealType
     * @param appellant
     * @param result
     */
    public MiscellaneousAppealPanel(String appealTitle, String appealType, String appellant, String result,
            String resultDate, String hearingStartDate, Long hearingDuration, String transCourtDetails) {
        super();
        appTitle = appealTitle;
        appType = appealType;
        appName = appellant;
        appResult = result;
        appResultDate = resultDate;
        appHearingStartDate = hearingStartDate;
        appHearingDuration = hearingDuration;
        transferredCourtDetails = transCourtDetails;
        init();
    }

    /**
     * Initialise the gui components.
     */
    private void init() {
        // GUI instantiations.
        appealTitleTextField = new JLabel();
        appealTypeTextField = new JLabel();
        appellantTextField = new JLabel();
        resultsTextArea = new JLabel();
        resultDateTextField = new JLabel();
        hearingStartTextField = new JLabel();
        hearingDurationTextField = new JLabel();
        transferredCourtTextField = new JLabel();

        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);
        appealTitleLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.APPEAL_TITLE_TXT));
        appealTypeLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.APPEAL_TYPE_TXT));
        appellantLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.APPELLANT_TXT));
        resultsLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.CASE_RESULT_TXT));
        resultsDateLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.RESULT_DATE_TXT));
        hearingStartLabel = new JLabel(XHIBITConstant.getResource(resources,
                CaseProgressConstants.HEARING_START_DATE_TXT));
        hearingDurationLabel = new JLabel(XHIBITConstant.getResource(resources,
                CaseProgressConstants.HEARING_DURATION_TXT));
        transferredCourtLabel = new JLabel(XHIBITConstant.getResource(resources,
                CaseProgressConstants.TRANSFERRED_TO_TXT));

        // Add the components.
        GridBagLayout gbLayout = new GridBagLayout();
        GridBagConstraints gbConstraints = null;
        this.setLayout(gbLayout);

        // Appeal Title Label
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(appealTitleLabel, gbConstraints);

        // Appeal Title text field
        gbConstraints = new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        appealTitleTextField.setText(appTitle);
        this.add(appealTitleTextField, gbConstraints);

        // Appeal Type label.
        gbConstraints = new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(appealTypeLabel, gbConstraints);

        // Appeal Type text field
        gbConstraints = new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        appealTypeTextField.setText(appType);
        this.add(appealTypeTextField, gbConstraints);

        // Appellant label.
        gbConstraints = new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(appellantLabel, gbConstraints);

        // Appellant text field
        gbConstraints = new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        appellantTextField.setText(appName);
        this.add(appellantTextField, gbConstraints);

        // Results label.
        gbConstraints = new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(resultsLabel, gbConstraints);

        // Results text field
        gbConstraints = new GridBagConstraints(1, 3, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        resultsTextArea.setText(appResult);
        this.add(resultsTextArea, gbConstraints);

        // Results Date label
        gbConstraints = new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(resultsDateLabel, gbConstraints);

        // Results Date text field
        gbConstraints = new GridBagConstraints(1, 4, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        resultDateTextField.setText(appResultDate);
        this.add(resultDateTextField, gbConstraints);

        // Hearing Date label
        gbConstraints = new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(hearingStartLabel, gbConstraints);

        // Hearing Date text field
        gbConstraints = new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        hearingStartTextField.setText(appHearingStartDate);
        this.add(hearingStartTextField, gbConstraints);

        // Hearing Duration label
        gbConstraints = new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(hearingDurationLabel, gbConstraints);

        // Hearing Duration text field
        gbConstraints = new GridBagConstraints(1, 6, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        hearingDurationTextField.setText(getHearingDuration());
        this.add(hearingDurationTextField, gbConstraints);

        // Transferred to court label
        gbConstraints = new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE,
                new Insets(10, 10, 10, 10), 0, 0);
        this.add(transferredCourtLabel, gbConstraints);

        // Transferred to court text field
        gbConstraints = new GridBagConstraints(1, 7, 1, 1, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.HORIZONTAL, new Insets(10, 10, 10, 10), 0, 0);
        transferredCourtTextField.setText(transferredCourtDetails);
        this.add(transferredCourtTextField, gbConstraints);
    }

    /**
     * breaks down milliseconds into hours and minutes.
     */
    private String getHearingDuration() {
        StringBuffer durationText = new StringBuffer();
        if (appHearingDuration != null) {
            long durationMilliseconds = appHearingDuration.longValue();
            long durationMinutes = durationMilliseconds / (60 * 1000);
            long hours = durationMinutes / 60;
            long minutes = durationMinutes % 60;

            if (hours > 0) {
                durationText.append(hours + " " + XHIBITConstant.getResource(resources, CaseProgressConstants.HOURS)
                        + " ");
            }
            if (minutes > 0) {
                durationText.append(minutes + " "
                        + XHIBITConstant.getResource(resources, CaseProgressConstants.MINUTES));
            }
        }
        return durationText.toString();
    }
}