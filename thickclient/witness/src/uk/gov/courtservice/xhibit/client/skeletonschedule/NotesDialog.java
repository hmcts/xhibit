package uk.gov.courtservice.xhibit.client.skeletonschedule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.witness.exceptions.WitnessNotFoundException;
import uk.gov.courtservice.xhibit.business.services.witness.interfaces.StateManaged;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.SkeletonScheduleFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.WitnessFactory;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.TrialSession;
import uk.gov.courtservice.xhibit.business.services.witness.schedule.interfaces.WitnessSession;
import uk.gov.courtservice.xhibit.client.skeletonschedule.util.WitnessTextListener;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.widgetfactory.Capability;
import uk.gov.courtservice.xhibit.client.widgetfactory.DocumentFactory;
import uk.gov.courtservice.xhibit.client.widgetfactory.JTextAreaFactory;
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

public class NotesDialog extends XDialog {
    /*
     * Mode Key Constants
     */

    public static final int ADD = 0;

    public static final int EDIT = 1;

    public static final int MAXNOTESIZE = 255;

    /*
     * Resource Key Constants
     */

    private static final String INVALID_MODE_KEY = "skeletonschedule.witnessdialog.invalidmode";

    private static final String ADD_TITLE_KEY = "witnessId.dialog.notes.add";

    private static final String EDIT_TITLE_KEY = "witnessId.dialog.notes.edit";

    private static final String NOTES_LABEL_KEY = "witnessId.dialog.notes.label";

    /*
     * Logger
     */

    private static final Logger log = CSServices.getLogger(NotesDialog.class);

    /*
     * Implementation
     */

    private Integer witnessId;

    private XhibitApplicationController xac;

    public NotesDialog(XhibitApplicationController newXac, int mode) throws CSRecoverableException {
        super(newXac, getTitleResource(mode), true, OKCANCEL, DEFAULTOK);

        log.debug("NotesDialog(" + newXac + ")");
        this.xac = newXac;
        pack();
    }

    /*
     * Body Panel Class
     */

    private static class NotesPanel extends XPanel {

        /*
         * Constraints
         */

        private static final GridBagConstraints notesLabelConstraints = new GridBagConstraints();

        static {
            notesLabelConstraints.gridx = 0;
            notesLabelConstraints.gridy = 0;
            notesLabelConstraints.anchor = GridBagConstraints.CENTER;
            notesLabelConstraints.insets = XHIBITConstant.nonContainerInsets;
            notesLabelConstraints.fill = GridBagConstraints.HORIZONTAL;
            notesLabelConstraints.weightx = 1.0;
        }

        private static final GridBagConstraints notesTextAreaConstraints = new GridBagConstraints();

        static {
            notesTextAreaConstraints.gridx = 0;
            notesTextAreaConstraints.gridy = 1;
            notesTextAreaConstraints.anchor = GridBagConstraints.CENTER;
            notesTextAreaConstraints.fill = GridBagConstraints.BOTH;
            notesTextAreaConstraints.weightx = 1.0;
            notesTextAreaConstraints.weighty = 1.0;
            notesTextAreaConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        /*
         * Implementation
         */

        private final XhibitApplicationController xac;

        private JTextArea notesField;

        private WitnessSession witness = null;

        public NotesPanel(XhibitApplicationController newXac, Integer witnessId) throws WitnessNotFoundException,
                CSRecoverableException {
            super(new GridBagLayout());

            log.debug("WitnessDialogPanel(" + newXac + ")");

            xac = newXac;
            if (witnessId != null) {
                witness = WitnessFactory.getInstance().getWitnessSession(witnessId);
            }

            StringBuffer sb = new StringBuffer(getResource("witnessId.dialog.notes.label1"));
            sb.append(' ');
            sb.append(witness.getDayNumber());
            sb.append(',');
            sb.append(' ');
            sb.append(getResource("skeletonschedule.session." + witness.getSessionType()));
            sb.append(' ');
            sb.append(getResource("witnessId.dialog.notes.label2"));
            add(createLabel(sb.toString()), notesLabelConstraints);
            String notes = "";
            if (witness != null) {
                notes = witness.getTrialSession().getNotes();
            }
            notesField = JTextAreaFactory.getTextArea(DocumentFactory.newDocument(new Capability[] { Capability
                    .limitedText(MAXNOTESIZE) }));
            Dimension d = new Dimension(300, 200);
            notesField.setLineWrap(true);
            JScrollPane jsp = new JScrollPane(notesField);
            jsp.setMinimumSize(d);
            jsp.setPreferredSize(d);
            add(jsp, notesTextAreaConstraints);

            stepInitialise();
        }

        /*
         * Life Cycle Methods
         */

        public void stepInitialise() throws CSRecoverableException {
            log.debug("stepInitialise()");

            if (witness != null) {
                // Do not pass the selected item into the contructor, but set it
                // later
                notesField.setText(witness.getTrialSession().getNotes());
            } else {
                notesField.setText("");

            }
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
            if (notesField.getText() == null) {
                notesField.setBackground(Color.red);
                throw new CSValidationException("skeletonschedule.witnessdialog.invalidname", "Name cannot be null.");
            }
        }

        public void stepDeactivate() throws CSRecoverableException {
            log.debug("stepDeactivate()");
        }

        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            if (update) {
                String notes = notesField.getText();
                TrialSession session = witness.getTrialSession();
                // If the meta state is UNSAVEABLE then it can't be persisted ie
                // it is used as a temporary value holder and cannot be
                // persisted.
                Integer caseId = xac.getApplicationCaseModel().getCaseId();
                if (session.getMetaState() == StateManaged.UNSAVEABLE) {
                    // So we create a valid one.
                    session = SkeletonScheduleFactory.getInstance().getSkeletonSchedule(caseId).createTrialSession(
                            new Integer(session.getDayNumber()), session.getSessionType());
                }
                log.debug("stepDeinitialise(" + update + ")");
                session.setNotes(notes);
                session.update();
            }
        }

    }

    /*
     * Component Factory Methods
     */

    private static JLabel createLabel(String text) {
        return new JLabel(text);
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
        NotesPanel panel = new NotesPanel(xac, witnessId);
        addBodyPanel(panel);
        if (panel.notesField.getText().equals("")) {
            this.getOKButton().setEnabled(false);
        }
        panel.notesField.getDocument().addDocumentListener(new WitnessTextListener(this.getOKButton()));
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

}