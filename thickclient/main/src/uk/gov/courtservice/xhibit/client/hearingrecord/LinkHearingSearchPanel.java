package uk.gov.courtservice.xhibit.client.hearingrecord;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.HearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.text.LimitedTextValidatingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.util.text.UpperCaseTransformingDocumentDecorator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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

public class LinkHearingSearchPanel extends XPanel {
    HearingRecordModel model;

    private JTextField caseText;

    private JButton searchBtn;

    private XTable resultsTable;

    private static final int LENGTH_OF_CASE_TEXT = 9;

    private Vector hearingsVec = null;

    public LinkHearingSearchPanel(HearingRecordModel model) throws CSRecoverableException {
        this.model = model;
        this.setMaximumSize(new Dimension(500, 200));
        this.setMinimumSize(new Dimension(500, 200));
        this.setPreferredSize(new Dimension(500, 200));
        stepInitialise();
        jbInit();
        stepActivate();
    }

    public void jbInit() {
        this.setLayout(new GridBagLayout());

        this.add(new JLabel(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "case")), new GridBagConstraints(0,
                0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        this.add(getCaseText(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
        this.add(getSearchBtn(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));

        this.resultsTable = XTableFactory.getInstance().createDefaultTable(new LinkSearchResultsTableModel(this.model));
        this.resultsTable.setPreferredScrollableViewportSize(new Dimension(450, 75));
        JScrollPane scrollPane = new JScrollPane(this.resultsTable);
        scrollPane.setMinimumSize(new Dimension(450, 75));
        this.add(scrollPane, new GridBagConstraints(0, 1, GridBagConstraints.REMAINDER, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(4, 4, 4, 4), 0, 0));
    }

    /**
     * Lazy instantiate the caseText screen widget
     * 
     * @return - JTextField
     */
    private JTextField getCaseText() {
        if (caseText == null) {
            caseText = new JTextField();
            caseText.setColumns(10);
            UpperCaseTransformingDocumentDecorator uCase = new UpperCaseTransformingDocumentDecorator();
            LimitedTextValidatingDocumentDecorator limit = new LimitedTextValidatingDocumentDecorator(uCase,
                    LENGTH_OF_CASE_TEXT);
            caseText.setDocument(limit);
            caseText.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    stepUpdateViewState();
                }
            });
        }

        return caseText;
    }

    /**
     * Lazy instantiate the searchBtn screen widget
     * 
     * @return - JButton
     */
    private JButton getSearchBtn() {
        if (searchBtn == null) {
            searchBtn = new JButton();
            searchBtn.setToolTipText(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "ttpSearch"));
            searchBtn.setAction(XhibitActions.getAction(model.getXac(), XhibitActions.LinkHearingSearch));
            XhibitActions.getAction(model.getXac(), XhibitActions.LinkHearingSearch).setCaller(this);
        }

        return searchBtn;
    }

    public void stepInitialise() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepDeactivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        if (this.resultsTable.getSelectedRow() != -1) // if at least one row
        // is selected
        {
            Collection hearingIDs = new Vector(); // creating collection
            // of selected hearing
            // Ids to be passed into
            // link hearings BD
            // method.

            for (int i = 0; i < this.resultsTable.getSelectedRowCount(); i++) {
                int x = this.resultsTable.getSelectedRows()[i];
                hearingIDs.add((Integer) ((Vector) this.hearingsVec).get(x));
            }

            XhibitDelegateHelper.getHearingDelegate().linkHearings(model.getLeadHearingId(), hearingIDs,
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME)); // BD
                                                                                                            // call
                                                                                                            // to
            // link selected
            // hearings with
            // lead hearing
        }
    }

    public void stepValidate() throws uk.gov.courtservice.framework.exception.CSRecoverableException,
            uk.gov.courtservice.framework.services.validation.CSValidationException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepUpdateViewState() {
        getSearchBtn().setEnabled(isMandatoryFieldsComplete());
    }

    private boolean isMandatoryFieldsComplete() {
        return getCaseText().getText().length() == LENGTH_OF_CASE_TEXT;
    }

    public void stepDeinitialise(boolean update) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        /**
         * @todo Implement this uk.gov.courtservice.xhibit.client.util.XPanel
         *       abstract method
         */
    }

    public void stepActivate() throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        stepUpdateViewState();
    }

    public HearingRecordModel getModel() {
        return this.model;
    }

    public void search() throws CSValidationException {
        validateCaseNumber();

        try {
            CaseHearingValue chv = new CaseHearingValue();

            chv = XhibitDelegateHelper.getHearingDelegate().listCaseWithHearings(this.caseText.getText(),
                    XhibitSingleton.getInstance().getCourtId(), model.getLeadHearingId());

            Vector data = new Vector();
            Vector v = new Vector();
            hearingsVec = new Vector();

            if (chv != null) {
                if (chv.getHearingValues() != null) {
                    for (int i = 0; i < chv.getHearingValues().size(); i++) {
                        HearingValue hv = ((HearingValue) ((Vector) chv.getHearingValues()).get(i));
                        hearingsVec.add(hv.getHearingID());
                        v.add(hv.getHearingTypeCode() + hv.getHearingTypeDesc());
                        String stDate = XDateFormat.format(hv.getStartDate(), XDateFormat.DATEFORMAT);
                        v.add(stDate);
                        String endDate = XDateFormat.format(hv.getEndDate(), XDateFormat.DATEFORMAT);
                        v.add(endDate);
                        data.add(v);
                    }
                }
            }

            model.setLinkResultsTableData(data);
            this.resultsTable.setModel(new LinkSearchResultsTableModel(this.model));
            this.resultsTable.repaint();

            if (data.size() == 0) {
                JOptionPane.showMessageDialog(this, XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                        "xs.gen.nomatchestext"), XHIBITConstant.getResource(XhibitBundles.XhibitSearch,
                        "xs.gen.nomatchestitle"), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (HearingScheduleException hse) {
            getCaseText().requestFocus();
            throw new CSValidationException("hearingrecord.search.caseNumberNotFound", "Case number not found");
        } catch (CSUnrecoverableException ex) {
            XHIBITConstant.handleError(ex);
        }
    }

    /**
     * Checks that the case number is valid
     * 
     * @throws CSValidationException
     */
    private void validateCaseNumber() throws CSValidationException {
        Object[] params = new Object[] { new Integer(LENGTH_OF_CASE_TEXT - 1) };

        // Case text field must be 9 characters long
        if (getCaseText().getText().length() < LENGTH_OF_CASE_TEXT) {
            getCaseText().requestFocus();
            throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                    "Too few characters entered");
        }

        try {
            // The first character must be alphabetic
            char caseType = getCaseText().getText().charAt(0);
            String caseNumber = getCaseText().getText().substring(1);

            if (!Character.isLetter(caseType)) {
                getCaseText().requestFocus();
                throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                        "Invalid case type");
            }

            // The remaining characters must be numeric
            Long.parseLong(caseNumber);
        } catch (NumberFormatException nfe) {
            getCaseText().requestFocus();
            throw new CSValidationException("hearingrecord.search.invalidCaseNumberFormat", params,
                    "Invalid case number");
        }
    }
}
