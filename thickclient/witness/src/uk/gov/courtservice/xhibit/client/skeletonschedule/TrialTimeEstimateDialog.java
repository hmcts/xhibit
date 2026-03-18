package uk.gov.courtservice.xhibit.client.skeletonschedule;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.DurationLessThanMinimumException;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ControllerUtil;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Trial Time Estimate Dialog
 * </p>
 * <p>
 * Description: The trial time estimate dialog
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Will Fardell, Xdevelopment
 * @version 1.0
 */
public class TrialTimeEstimateDialog extends XDialog {

    /*
     * Resource Key Constants
     */

    private static final String TRIAL_ESTIMATE_LESS_THAN_LAST_WITNESS_KEY = "skeletonschedule.trialtimeestimatedialog.trialtimelessthanlastwitness";

    private static final String TRIALTIMEESTIMATE_DIALOG_TITLE_KEY = "trialtimeestimate.dialog";

    private static final String TRIALTIMEESTIMATE_TRIALTIME_LABEL_KEY = "trialtimeestimate.trialtime.label";

    private static final String TRIALTIMEESTIMATE_UNIT_LABEL_KEY = "trialtimeestimate.unit.label";

    // private static final String TRIALTIMEESTIMATE_FORMAT_KEY =
    // "trialtimeestimate.trialtime.format";

    /*
     * Logger
     */

    static final Logger log = CSServices.getLogger(TrialTimeEstimateDialog.class);

    /*
     * Implementation
     */

    public TrialTimeEstimateDialog(XhibitApplicationController newXac) throws CSRecoverableException {
        super(newXac, getResource(TRIALTIMEESTIMATE_DIALOG_TITLE_KEY), true, OKCANCEL, DEFAULTOK);

        log.debug("TrialTimeEstimateDialog(" + newXac + ")");

        addBodyPanel(new TrialTimeEstimatePanel(newXac));

        pack();
        setVisible(true);
    }

    /*
     * Body Panel Class
     */

    private static class TrialTimeEstimatePanel extends XPanel {

        /*
         * Constraints
         */

        private static final GridBagConstraints trialTimeLabelConstraints = new GridBagConstraints();
        static {
            trialTimeLabelConstraints.gridx = 0;
            trialTimeLabelConstraints.gridy = 0;
            trialTimeLabelConstraints.anchor = GridBagConstraints.EAST;
            trialTimeLabelConstraints.weightx = 1.0;
            trialTimeLabelConstraints.insets = new Insets(4, 4, 4, 2);
        }

        private static final GridBagConstraints trialTimeFieldConstraints = new GridBagConstraints();
        static {
            trialTimeFieldConstraints.gridx = 1;
            trialTimeFieldConstraints.gridy = 0;
            trialTimeFieldConstraints.insets = new Insets(4, 2, 4, 2);
        }

        private static final GridBagConstraints unitLabelConstraints = new GridBagConstraints();
        static {
            unitLabelConstraints.gridx = 2;
            unitLabelConstraints.gridy = 0;
            unitLabelConstraints.anchor = GridBagConstraints.WEST;
            unitLabelConstraints.weightx = 1.0;
            unitLabelConstraints.insets = new Insets(4, 2, 4, 4);
        }

        /*
         * Format
         */
        // private static DecimalFormat trialTimeEstimateFormat;
        /*
         * Implementation
         */

        private final XhibitApplicationController xac;

        private JTextField trialTimeEstimateValue;

        public TrialTimeEstimatePanel(XhibitApplicationController newXac) throws CSRecoverableException {
            super(new GridBagLayout());

            log.debug("TrialTimeEstimatePanel(" + newXac + ")");

            xac = newXac;

            add(new JLabel(getResource(TRIALTIMEESTIMATE_TRIALTIME_LABEL_KEY)), trialTimeLabelConstraints);

            KeyAdapter timeListener = new KeyAdapter() {
                public void KeyPressed(KeyEvent e) {
                    try {
                        stepUpdateViewState();
                    } catch (CSRecoverableException ex) {
                        XHIBITConstant.handleError(ex);
                    }
                }
            };

            // make consistent with rest of system, only allow 5 numeric
            // values
            // to be entered, no decimals
            final Capability[] capabilities = new Capability[] { Capability.longNumeric(), Capability.limitedText(3) };

            trialTimeEstimateValue = JTextFieldFactory.getTextField("", timeListener, 8, null, null, null, true, "",
                    DocumentFactory.newDocument(capabilities));

            add(trialTimeEstimateValue, trialTimeFieldConstraints);
            add(new JLabel(getResource(TRIALTIMEESTIMATE_UNIT_LABEL_KEY)), unitLabelConstraints);

            stepInitialise();
        }

        /*
         * Life Cycle Methods
         */

        public void stepInitialise() throws CSRecoverableException {
            log.debug("stepInitialise()");
        }

        public void stepActivate() throws CSRecoverableException {
            log.debug("stepActivate()");
            setTrialTimeEstimate(ControllerUtil.getSkeletonSchedulePanel(xac).getTrialTimeEstimate());

            stepUpdateViewState();
        }

        public void stepUpdateViewState() throws CSRecoverableException {
            log.debug("stepUpdateViewState()");
            Window w = XSwingUtilities.getWindowAncestor(this);
            if (w instanceof XDialog) {
                boolean enableOK = (trialTimeEstimateValue.getText().trim().length() > 0 && getTrialTimeEstimate() > 0);
                XDialog xd = (XDialog) w;
                ((OkCancelPanel) xd.getButtonPanel()).getOkAction().setEnabled(enableOK);
            }
        }

        public void stepValidate() throws CSValidationException, CSRecoverableException {
            log.debug("stepValidate()");
            boolean validated = (trialTimeEstimateValue.getText().trim().length() > 0 && getTrialTimeEstimate() > 0);
            if (!validated) {
                throw new CSValidationException("validation.minexclusive", new Object[] {
                        trialTimeEstimateValue.getText(), "0" }, "Trial time not entered of less than zero");
            }
        }

        public void stepDeactivate() throws CSRecoverableException {
            log.debug("stepDeactivate()");

            try {
                int trialEstimate = getTrialTimeEstimate();
                ControllerUtil.getCaseDetail(xac).setEstimatedCaseDuration(trialEstimate, false);
                ControllerUtil.getSkeletonSchedulePanel(xac).setTrialTimeEstimate(trialEstimate);
            } catch (DurationLessThanMinimumException dltm) {
                // Push back into DurationLessThanMinimumException
                String trialEstimateText = trialTimeEstimateValue.getText();
                throw new CSRecoverableException(TRIAL_ESTIMATE_LESS_THAN_LAST_WITNESS_KEY,
                        new Object[] { trialEstimateText }, "Trial time " + trialEstimateText
                                + " less than last witness session.", dltm);
            }
        }

        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            log.debug("stepDeinitialise(" + update + ")");
        }

        /*
         * Helper
         */
        public void setTrialTimeEstimate(float trialTimeEstimate) {
            log.debug("setTrialTimeEstimate(" + trialTimeEstimate + ")");
            // trialTimeEstimateValue.setText(getTrialTimeEstimateFormat().format(trialTimeEstimate));
            trialTimeEstimateValue.setText(String.valueOf((long) trialTimeEstimate));
        }

        public int getTrialTimeEstimate() {
            return Integer.parseInt(trialTimeEstimateValue.getText());
        }

        // /*
        // * Utility Methods
        // */
        // private static DecimalFormat getTrialTimeEstimateFormat()
        // {
        // if (trialTimeEstimateFormat == null)
        // {
        // trialTimeEstimateFormat = new
        // DecimalFormat(getResource(TRIALTIMEESTIMATE_FORMAT_KEY));
        // }
        // return trialTimeEstimateFormat;
        // }
    }

    /*
     * Utility Methods
     */
    public static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }
}
