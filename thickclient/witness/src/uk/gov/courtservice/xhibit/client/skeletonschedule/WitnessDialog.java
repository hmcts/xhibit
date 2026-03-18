package uk.gov.courtservice.xhibit.client.skeletonschedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged;
import uk.gov.courtservice.xhibit.business.services.witness.reference.WitnessReferenceDataFactory;
import uk.gov.courtservice.xhibit.business.services.witness.reference.interfaces.WitnessReferenceData;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.client.skeletonschedule.combo.ArrayComboBoxModel;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.ControllerUtil;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.TrialSessionComparator;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.WitnessTextListener;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTimePanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextFieldFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: Witness Dialog
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
public class WitnessDialog extends XDialog {
    /*
     * Mode Key Constants
     */

    public static final int ADD = 0;

    public static final int EDIT = 1;

    /*
     * Resource Key Constants
     */

    private static final String INVALID_MODE_KEY = "skeletonschedule.witnessdialog.invalidmode";

    private static final String ADD_TITLE_KEY = "witnessId.dialog.add";

    private static final String EDIT_TITLE_KEY = "witnessId.dialog.edit";

    private static final String SESSION_LABEL_KEY = "witnessId.dialog.session.label";

    private static final String NAME_LABEL_KEY = "witnessId.dialog.name.label";

    private static final String TYPE_LABEL_KEY = "witnessId.dialog.type.label";

    private static final String STATUS_LABEL_KEY = "witnessId.dialog.status.label";

    private static final String TIME_LABEL_KEY = "witnessId.dialog.arrival.label";

    private static final String SESSION_AM_BEGIN = "witnessId.session.am.begin";

    private static final String SESSION_AM_END = "witnessId.session.am.end";

    private static final String SESSION_PM_BEGIN = "witnessId.session.pm.begin";

    private static final String SESSION_PM_END = "witnessId.session.pm.end";

    private static final String WITNESS_TITLE_KEY = "witnessId.warning.title";

    private static final String WITNESS_AM_MESSAGE_KEY = "witnessId.warning.message.am";

    private static final String WITNESS_PM_MESSAGE_KEY = "witnessId.warning.message.pm";

    private static final String MORNING_SESSION = "witnessId.session.morning";

    private static final String WITNESS_DELIM = " ";

    /*
     * Logger
     */

    private static final Logger log = CSServices.getLogger(WitnessDialog.class);

    /*
     * Implementation
     */

    private Integer witnessId;

    private XhibitApplicationController xac;

    public WitnessDialog(XhibitApplicationController newXac, int mode) throws CSRecoverableException {
        super(newXac, getTitleResource(mode), true, OKCANCEL, DEFAULTOK);

        log.debug("WitnessDialog(" + newXac + ")");
        this.xac = newXac;

    }

    /*
     * Body Panel Class
     */

    private static class WitnessPanel extends XPanel {

        /*
         * Constraints
         */

        private static final GridBagConstraints sessionLabelConstraints = new GridBagConstraints();

        static {
            sessionLabelConstraints.gridx = 0;
            sessionLabelConstraints.gridy = 0;
            sessionLabelConstraints.anchor = GridBagConstraints.WEST;
            sessionLabelConstraints.insets = new Insets(4, 4, 2, 2);
        }

        private static final GridBagConstraints sessionComboConstraints = new GridBagConstraints();

        static {
            sessionComboConstraints.gridx = 1;
            sessionComboConstraints.gridy = 0;
            sessionComboConstraints.anchor = GridBagConstraints.WEST;
            sessionComboConstraints.weightx = 1.0;
            sessionComboConstraints.insets = new Insets(4, 2, 2, 4);
        }

        private static final GridBagConstraints nameLabelConstraints = new GridBagConstraints();

        static {
            nameLabelConstraints.gridx = 0;
            nameLabelConstraints.gridy = 1;
            nameLabelConstraints.anchor = GridBagConstraints.WEST;
            nameLabelConstraints.insets = new Insets(2, 4, 2, 2);
        }

        private static final GridBagConstraints nameFieldConstraints = new GridBagConstraints();

        static {
            nameFieldConstraints.gridx = 1;
            nameFieldConstraints.gridy = 1;
            nameFieldConstraints.anchor = GridBagConstraints.WEST;
            nameFieldConstraints.weightx = 1.0;
            nameFieldConstraints.insets = new Insets(2, 2, 2, 4);
        }

        private static final GridBagConstraints typeLabelConstraints = new GridBagConstraints();

        static {
            typeLabelConstraints.gridx = 0;
            typeLabelConstraints.gridy = 2;
            typeLabelConstraints.anchor = GridBagConstraints.WEST;
            typeLabelConstraints.insets = new Insets(2, 4, 2, 2);
        }

        private static final GridBagConstraints typeComboConstraints = new GridBagConstraints();

        static {
            typeComboConstraints.gridx = 1;
            typeComboConstraints.gridy = 2;
            typeComboConstraints.anchor = GridBagConstraints.WEST;
            typeComboConstraints.weightx = 1.0;
            typeComboConstraints.insets = new Insets(2, 2, 2, 4);
        }

        private static final GridBagConstraints statusLabelConstraints = new GridBagConstraints();

        static {
            statusLabelConstraints.gridx = 0;
            statusLabelConstraints.gridy = 3;
            statusLabelConstraints.anchor = GridBagConstraints.WEST;
            statusLabelConstraints.insets = new Insets(2, 4, 2, 2);
        }

        private static final GridBagConstraints statusComboConstraints = new GridBagConstraints();

        static {
            statusComboConstraints.gridx = 1;
            statusComboConstraints.gridy = 3;
            statusComboConstraints.anchor = GridBagConstraints.WEST;
            statusComboConstraints.weightx = 1.0;
            statusComboConstraints.insets = new Insets(2, 2, 2, 4);
        }

        private static final GridBagConstraints timeLabelConstraints = new GridBagConstraints();

        static {
            timeLabelConstraints.gridx = 0;
            timeLabelConstraints.gridy = 4;
            timeLabelConstraints.anchor = GridBagConstraints.WEST;
            timeLabelConstraints.insets = new Insets(2, 4, 4, 2);
        }

        private static final GridBagConstraints timeFieldConstraints = new GridBagConstraints();

        static {
            timeFieldConstraints.gridx = 1;
            timeFieldConstraints.gridy = 4;
            timeFieldConstraints.anchor = GridBagConstraints.WEST;
            timeFieldConstraints.weightx = 1.0;
            timeFieldConstraints.insets = new Insets(2, 2, 4, 4);
        }

        /*
         * Implementation
         */

        private final XhibitApplicationController xac;

        private JComboBox sessionCombo;

        private JTextField nameField;

        private JComboBox typeCombo;

        private JComboBox statusCombo;

        private XTimePanel timeField;

        private WitnessSession witness = null;

        private Time expectedTime;

        // private static final String NOTES = "Default Note";
        private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        public WitnessPanel(XhibitApplicationController newXac, Integer witnessId) throws WitnessNotFoundException,
                CSRecoverableException {
            super(new GridBagLayout());

            log.debug("WitnessDialogPanel(" + newXac + ")");

            xac = newXac;
            if (witnessId != null) {
                witness = WitnessFactory.getInstance().getWitnessSession(witnessId);
            }

            add(createLabel(getResource(SESSION_LABEL_KEY)), sessionLabelConstraints);
            sessionCombo = createComboBox();

            add(sessionCombo, sessionComboConstraints);

            add(createLabel(getResource(NAME_LABEL_KEY)), nameLabelConstraints);
            String witnessName = "";
            if (witness != null) {
                witnessName = witness.getName();
            }

            Capability[] caps = new Capability[] {
            // Capability.alphaNumeric(),
            Capability.limitedText(70) };

            nameField = JTextFieldFactory.getTextField("", null, 35, null, null, null, true, witnessName,
                    DocumentFactory.newDocument(caps));
            nameField.setText(witnessName);

            add(nameField, nameFieldConstraints);

            add(createLabel(getResource(TYPE_LABEL_KEY)), typeLabelConstraints);
            typeCombo = createComboBox();
            add(typeCombo, typeComboConstraints);

            add(createLabel(getResource(STATUS_LABEL_KEY)), statusLabelConstraints);
            statusCombo = createComboBox();
            add(statusCombo, statusComboConstraints);

            add(createLabel(getResource(TIME_LABEL_KEY)), timeLabelConstraints);
            Calendar c = Calendar.getInstance();
            c.set(Calendar.HOUR_OF_DAY, 10);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            timeField = new XTimePanel(this, c);
            add(timeField, timeFieldConstraints);

            stepInitialise();
        }

        /*
         * Life Cycle Methods
         */

        public void stepInitialise() throws CSRecoverableException {
            log.debug("stepInitialise()");

            float trialEstimate = ControllerUtil.getSkeletonSchedulePanel(xac).getTrialTimeEstimate();
            TrialSession[] trialSessions = ControllerUtil.getSkeletonSchedule(xac)
                    .getTrialSessionsIncludingDummyValuesForDuration(trialEstimate);

            // sort the sessions before passing them to combo box model (Bug
            // X53677)
            Arrays.sort(trialSessions, new TrialSessionComparator());

            if (witness != null) {
                // Do not pass the selected item into the contructor, but set it
                // later
                sessionCombo.setModel(new TrialSessionComboBoxModel(trialSessions)); // ,
                // witness.getTrialSession()));
            } else {
                sessionCombo.setModel(new TrialSessionComboBoxModel(trialSessions));

            }

            if (witness != null) {
                // timeField.setText(timeFormat.format(witness.getExpected()));
                timeField.setTime(witness.getExpected());
            }
            WitnessReferenceData refData = WitnessReferenceDataFactory.getWitnessReferenceData();
            typeCombo.setModel(new ArrayComboBoxModel(refData.getWitnessTypes()));
            if (witness != null) {
                typeCombo.setSelectedItem(witness.getType());
            }
            String[] witnessStatuses = refData.getWitnessStatuses();
            statusCombo.setModel(new ArrayComboBoxModel(witnessStatuses));
            if (witness != null) {
                statusCombo.setSelectedItem(witness.getStatus());
            }
            typeCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (typeCombo != null
                            && typeCombo.getSelectedItem() != null
                            && ((String) typeCombo.getSelectedItem())
                                    .equalsIgnoreCase("Defence"/* "*WitnessDetail.DEFENCE_TYPE */)) {
                        statusCombo.setEnabled(false);
                        // statusCombo.setSelectedIndex(0);
                    } else {
                        statusCombo.setEnabled(true);

                    }
                }
            });
            typeCombo.setSelectedIndex(0);
            // Set the selected item once the dialog has been contructed
            if (witness != null) {
                sessionCombo.setSelectedItem(witness.getTrialSession());
                typeCombo.setSelectedItem(formatDisplayName(witness.getType()));
            }

            /**
             * PR 56352: Neil Entwistle - add a listener to the sessionCombo to
             * set the default time to 10:00 if Morning and 14:00 if Afternoon
             */
            sessionCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (sessionCombo != null && sessionCombo.getSelectedItem() != null) {
                        Calendar c = null;
                        try {
                            c = timeField.getDate();
                        } catch (CSValidationException ex) {
                            // Calendar on timeField is not valid so create
                            // a new one.
                            c = Calendar.getInstance();
                        }
                        c.set(Calendar.MINUTE, 0);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);

                        if (sessionCombo.getSelectedItem().toString().indexOf(getResource(MORNING_SESSION)) > 0) {
                            c.set(Calendar.HOUR_OF_DAY, 10);
                        } else {
                            c.set(Calendar.HOUR_OF_DAY, 14);
                        }
                        timeField.setTime(c);
                    }
                }
            });
        }

        public void stepActivate() throws CSRecoverableException {
            log.debug("stepActivate()");
            stepUpdateViewState();
        }

        public void stepUpdateViewState() throws CSRecoverableException {
            log.debug("stepUpdateViewState()");
        }

        public void stepValidate() throws CSValidationException, CSRecoverableException {
            log.debug("stepValidate()");
            updateTime();
            if (nameField.getText() == null) {
                nameField.setBackground(Color.red);
                throw new CSValidationException("skeletonschedule.witnessdialog.invalidname", "Name cannot be null.");
            }
        }

        public void stepDeactivate() throws CSRecoverableException {
            log.debug("stepDeactivate()");
        }

        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            if (update) {
                // set the age to -1 (default value)
                int age = -1;
                String name = nameField.getText();
                String type = (String) typeCombo.getSelectedItem();
                String status = null;
                if (statusCombo.isEnabled()) {
                    status = (String) statusCombo.getSelectedItem();
                }
                TrialSession session = ((TrialSessionComboBoxModel) sessionCombo.getModel()).getSelectedTrialSession();
                // If the meta state is UNSAVEABLE then it can't be persisted ie
                // it is used as a temporary value holder and cannot be
                // persisted.
                Integer caseId = xac.getApplicationCaseModel().getCaseId();
                if (session.getMetaState() == StateManaged.UNSAVEABLE) {
                    // So we create a valid one.
                    session = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseId).createTrialSession(
                            new Integer(session.getDayNumber()), session.getSessionType());
                }

                if (witness == null) {
                    witness = WitnessFactory.getInstance().createWitnessSession(caseId, session.getId(), name, type,
                            status, age, expectedTime, null);
                    log.debug("createWitnessSession(" + caseId + ", " + session.getId() + ", " + name + ", " + type
                            + ", " + age + ", " + expectedTime + ", " + ")");
                    witness.update();
                } else {
                    log.debug("stepDeinitialise(" + update + ")");
                    witness.setName(name);
                    witness.setType(type);
                    witness.setStatus(status);
                    witness.setTrialSession(session);
                    if (expectedTime != null) {
                        witness.setExpected(expectedTime);
                    }
                    witness.update();

                }
            }
        }

        private void updateTime() throws CSValidationException, CSRecoverableException {
            String text = timeField.getText();
            TrialSession session = ((TrialSessionComboBoxModel) sessionCombo.getModel()).getSelectedTrialSession();
            try {
                expectedTime = new Time(timeFormat.parse(text).getTime());
                timeField.getTimeComponent().setBackground(Color.white);
            } catch (ParseException e) {
                log.debug(e);
                timeField.getTimeComponent().setBackground(Color.red);
                throw new CSValidationException("WITNESS_XXX", "Time format incorrect.");
            }

            validateTimeWithSession(session);
        }

        // Bug X53677
        // changed this method (and updateTime above) to throw
        // CSRecoverableException
        // so that we can give the user the choice of whether to proceed if they
        // enter
        // a time which is outside of the normal sitting hours.
        // used to be just a message but now give Yes/No option
        private void validateTimeWithSession(TrialSession session) throws CSValidationException, CSRecoverableException {
            try {
                Time amBegin = new Time(timeFormat.parse(getResource(SESSION_AM_BEGIN)).getTime());
                Time amEnd = new Time(timeFormat.parse(getResource(SESSION_AM_END)).getTime());
                Time pmBegin = new Time(timeFormat.parse(getResource(SESSION_PM_BEGIN)).getTime());
                Time pmEnd = new Time(timeFormat.parse(getResource(SESSION_PM_END)).getTime());

                // PR 56353 - warning message removed
                // if (session.getSessionType().equals("M")) {
                // if (expectedTime.after(amEnd) ||
                // expectedTime.before(amBegin)) {
                // int response = JOptionPane.showConfirmDialog(this,
                // getResource(WITNESS_AM_MESSAGE_KEY),
                // getResource(WITNESS_TITLE_KEY),
                // JOptionPane.YES_NO_OPTION);
                // if (response == JOptionPane.NO_OPTION) {
                // throw new UserCancelException();
                // }
                // }
                // } else {
                // if (expectedTime.after(pmEnd) ||
                // expectedTime.before(pmBegin)) {
                // int response = JOptionPane.showConfirmDialog(this,
                // getResource(WITNESS_PM_MESSAGE_KEY),
                // getResource(WITNESS_TITLE_KEY),
                // JOptionPane.YES_NO_OPTION);
                // if (response == JOptionPane.NO_OPTION) {
                // throw new UserCancelException();
                // }
                // }
                // }
            } catch (ParseException e) {
                timeField.getTimeComponent().setBackground(Color.red);
                throw new CSValidationException("WITNESS_XXX", "Time format incorrect.");
            }
        }
    }

    /*
     * Component Factory Methods
     */

    private static JLabel createLabel(String text) {
        return new JLabel(text);
    }

    // private static JTextField createField(String text, int length)
    // {
    // return new JTextField(text, length);
    // }

    private static JComboBox createComboBox() {
        return new JComboBox();
    }

    /*
     * Utility Methods
     */

    private static String getTitleResource(int mode) throws CSRecoverableException {
        switch (mode) {
        case ADD:
            return getResource(ADD_TITLE_KEY);
        case EDIT:
            return getResource(EDIT_TITLE_KEY);
        default:
            throw new CSRecoverableException(INVALID_MODE_KEY, new Object[] { new Integer(mode) }, "Unrecognised mode "
                    + mode + ".");
        }

    }

    private static String getResource(String resourceName) {
        log.debug("getResource(" + resourceName + ")");
        return XHIBITConstant.getResource(XhibitBundles.SkeletonSchedule, resourceName);
    }

    public Integer getWitnessId() {
        return witnessId;
    }

    public void setWitnessId(Integer witnessId) {
        this.witnessId = witnessId;
    }

    public void init(Integer witnessId) throws CSRecoverableException, WitnessNotFoundException {
        setWitnessId(witnessId);
        WitnessPanel panel = new WitnessPanel(xac, witnessId);
        addBodyPanel(panel);
        if (panel.nameField.getText().equals("")) {
            this.getOKButton().setEnabled(false);
        }
        panel.nameField.getDocument().addDocumentListener(new WitnessTextListener(this.getOKButton()));
        pack();
        setVisible(true);

    }

    /**
     * Returns the OK button from the XDialog
     * 
     * @return JButton
     */
    private JButton getOKButton() {
        Component[] components = this.getButtonPanel().getComponents();
        JButton btn = null;

        for (int i = 0; i < components.length; i++) {
            if ((components[i] instanceof JButton) && (((JButton) components[i]).getText().equals("OK"))) {
                btn = (JButton) this.getButtonPanel().getComponent(i);
                break;
            }
        }
        return btn;
    }

    /**
     * Formats name for display by capitalising the first character
     * 
     * @param name
     * @return
     */
    public static String formatDisplayName(String name) {
        log.debug("$$$ formatDisplayName: " + name + " $$$");
        name = name.toLowerCase();
        StringTokenizer nameToken = new StringTokenizer(name, WITNESS_DELIM);
        StringBuffer buf = new StringBuffer();
        String tokResult = null;

        log.debug("$$$ name.indexOf(WITNESS_DELIM): " + name.indexOf(WITNESS_DELIM) + " $$$");
        if (name.indexOf(WITNESS_DELIM) > 0) {
            while (nameToken.hasMoreTokens()) {
                tokResult = nameToken.nextToken();
                buf.append(tokResult.substring(0, 1).toUpperCase());
                buf.append(tokResult.substring(1));
                buf.append(" ");
            }
        } else {
            buf.append(name.substring(0, 1).toUpperCase());
            buf.append(name.substring(1));
        }

        return buf.toString();
    }

}
