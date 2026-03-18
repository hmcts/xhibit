package uk.gov.courtservice.xhibit.client.caseprogress;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 - Criminal Appeal Panel
 * </p>
 * <p>
 * Description: Panel that holds information for criminal appeals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Joseph Antoniou
 * @version 1.0
 */

public class CriminalAppealPanel extends JPanel {
    /** Toggling button for displaying/undisplaying appeals detailed data. */
    private JToggleButton appealDetailsButton = null;

    /** Toggling button for displaying/undisplaying defendant detailed data. */
    private JToggleButton defendantDetailsButton = null;

    /** The controller responsible for calling this panel. */
    private XhibitApplicationController appController = null;

    /** The logger for debug. */
    private Logger logger = CSServices.getLogger(CriminalAppealPanel.class);

    private String caseLevelResultText = null;

    private String caseLevelResultDate = null;

    private JPanel caseAppealPanel = null;

    private JPanel appealPanel = null;

    private JPanel judgePanel = null;

    private XTable criminalTable = null;

    private XTable defDisposalTable = null;

    private String judgesComments = null;

    private TableModel criminalTableModel = null;

    private TableModel defDisposalTableModel = null;

    private JLabel resultsTextArea = null;

    private JLabel resultDateTextField = null;

    /**
     * This is the key value used for the client property stored in each toggle
     * button instance. If its corresponding value is true, then the table
     * models must be set with their data in detailed mode. If false, then table
     * models should model their data normally.
     */
    public static final String DETAILS_TOGGLE = "showDetails";

    public static final String CHARGE_TABLE = "chargeTable";

    /**
     * Creates a criminal appeal panel.
     * 
     * @param appController
     *            the main application.
     * @param criminalTableModel
     *            the table model for criminal appeals.
     * @param defDisposalTableModel
     *            the table model for unrelated disposals.
     * @param judgesComments
     *            the judges comments.
     */
    public CriminalAppealPanel(XhibitApplicationController appController, String caseLevelResultText,
            String caseLevelResultDate, TableModel criminalTableModel, TableModel defDisposalTableModel,
            String judgesComments) {
        super();
        this.appController = appController;
        this.caseLevelResultText = caseLevelResultText;
        this.caseLevelResultDate = caseLevelResultDate;
        this.criminalTableModel = criminalTableModel;
        this.defDisposalTableModel = defDisposalTableModel;
        this.judgesComments = judgesComments;
        init();
    }

    /**
     * Initialise the gui components.
     */
    private void init() {
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel mainPanel = new JPanel(gbl);

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridx = 0;
        gbc.gridy = 0;

        gbc.insets = XHIBITConstant.nonContainerInsets;
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.05;
        mainPanel.add(createCaseAppealPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.5;
        mainPanel.add(createAppealPanel(), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        mainPanel.add(createJudgePanel(), gbc);

        setLayout(new GridBagLayout());

        JScrollPane mainScroller = new JScrollPane(mainPanel);
        mainScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(mainScroller, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        EventQueue.invokeLater(new ResizeTable(criminalTable));
        EventQueue.invokeLater(new ResizeTable(defDisposalTable));
    }

    /**
     * Creates the case level criminal appeal panel.
     * 
     * @return the case level criminal appeal panel.
     */
    private JPanel createCaseAppealPanel() {
        if (caseAppealPanel == null) {
            resultsTextArea = new JLabel();
            resultDateTextField = new JLabel();

            GridBagLayout gbLayout = new GridBagLayout();
            caseAppealPanel = new JPanel(gbLayout);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = XHIBITConstant.nonContainerInsets;
            gbc.weightx = 0.0;
            gbc.weighty = 0.0;
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 2;
            caseAppealPanel.add(createJLabel(CaseProgressConstants.CASE_RESULT_TXT), gbc);
            gbc.weightx = 0.9;
            gbc.gridx = 1;
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            resultsTextArea.setText(caseLevelResultText);
            caseAppealPanel.add(resultsTextArea, gbc);
            gbc.gridwidth = 1;
            gbc.weightx = 0.0;
            gbc.gridx = 2;
            gbc.gridy = 2;
            resultDateTextField.setText(caseLevelResultDate);
            caseAppealPanel.add(createJLabel(CaseProgressConstants.RESULT_DATE_TXT), gbc);
            gbc.weightx = 0.1;
            gbc.gridx = 3;
            gbc.gridy = 2;
            caseAppealPanel.add(resultDateTextField, gbc);

            // Add titled borders to the panels with position
            caseAppealPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    getResource(CaseProgressConstants.APPEAL_CASE_RESULT_TITLE), TitledBorder.DEFAULT_JUSTIFICATION, // Title
                    // Horizontal
                    // justification
                    TitledBorder.DEFAULT_POSITION, // Title
                    // border
                    // alignment
                    XHIBITConstant.getCurrentFont(), Color.black));
        }
        return caseAppealPanel;
    }

    /**
     * Creates the offence level criminal appeal panel.
     * 
     * @return the offence level criminal appeal panel.
     */
    private JPanel createAppealPanel() {
        if (appealPanel == null) {
            GridBagLayout gbLayout = new GridBagLayout();
            appealPanel = new JPanel(gbLayout);

            JScrollPane criminalScroller = new JScrollPane(getCriminalTable());
            criminalScroller.setBorder(null);

            JScrollPane defDisposalScroller = new JScrollPane(getDefendantTable());
            defDisposalScroller.setBorder(null);

            appealPanel.add(createJLabel(CaseProgressConstants.CRIMINAL_APPEALS), new GridBagConstraints(0, 0, 1, 1,
                    0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0));

            appealDetailsButton = createButton(criminalTable);
            appealPanel.add(appealDetailsButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

            appealPanel.add(criminalScroller, new GridBagConstraints(0, 1, 2, 1, 1.0, 0.5, GridBagConstraints.CENTER,
                    GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));

            appealPanel.add(createJLabel(CaseProgressConstants.DEF_DISPOSALS_TXT), new GridBagConstraints(0, 2, 1, 1,
                    0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0));

            defendantDetailsButton = createButton(defDisposalTable);
            appealPanel.add(defendantDetailsButton, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));

            appealPanel.add(defDisposalScroller, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.5,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 10, 10, 10), 0, 0));

            // Add titled borders to the panels with position
            appealPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    getResource(CaseProgressConstants.STATUS_CHARGE_TXT), // Title
                    TitledBorder.DEFAULT_JUSTIFICATION, // Title Horizontal
                    // justification
                    TitledBorder.DEFAULT_POSITION, // Title border
                    // alignment
                    XHIBITConstant.getCurrentFont(), Color.black));
        }
        return appealPanel;
    }

    /**
     * Lazy instantiates the main criminal appeal table.
     * 
     * @return the criminal appeal XTable.
     */
    private XTable getCriminalTable() {
        if (criminalTable == null) {
            criminalTable = XTableFactory.getInstance().createMultiLineTable(criminalTableModel);
            criminalTable.setPreferredScrollableViewportSize(getTableDimension(criminalTable));

            criminalTable.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
                public void ancestorMoved(HierarchyEvent e) {
                    layoutTable(criminalTable);
                }

                public void ancestorResized(HierarchyEvent e) {
                    layoutTable(criminalTable);
                }
            });
        }
        return criminalTable;
    }

    /**
     * Lazy instantiates the defendant disposal table.
     * 
     * @return the defendant disposal XTable.
     */
    private XTable getDefendantTable() {
        if (defDisposalTable == null) {
            defDisposalTable = XTableFactory.getInstance().createMultiLineTable(defDisposalTableModel);
            Object[] longValues = new Object[] { XTable.COLUMN_WIDTH_UNDEFINED, "Type", XTable.COLUMN_WIDTH_UNDEFINED };
            defDisposalTable.initColumnSizes(longValues, 400);
            defDisposalTable.setPreferredScrollableViewportSize(getTableDimension(defDisposalTable));

            defDisposalTable.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
                public void ancestorMoved(HierarchyEvent e) {
                    layoutTable(defDisposalTable);
                }

                public void ancestorResized(HierarchyEvent e) {
                    layoutTable(defDisposalTable);
                }
            });
        }
        return defDisposalTable;
    }

    private void layoutTable(JTable table) {
        table.setPreferredScrollableViewportSize(getTableDimension(table));
        table.revalidate();
        revalidate();
    }

    private JPanel createJudgePanel() {
        if (judgePanel == null) {
            GridBagLayout gbLayout = new GridBagLayout();
            judgePanel = new JPanel(gbLayout);

            JTextArea judgesCommentsTextArea = new JTextArea(judgesComments);
            judgesCommentsTextArea.setFont(XHIBITConstant.getCurrentFont());
            judgesCommentsTextArea.setEditable(false);

            JScrollPane judgesCommentScroller = new JScrollPane(judgesCommentsTextArea);
            // judgesCommentScroller.setBorder(null);

            judgePanel.add(judgesCommentScroller, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 0, 0));

            judgePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
                    getResource(CaseProgressConstants.EDIT_JUDGE_COMMENTS_TXT), // Title
                    TitledBorder.DEFAULT_JUSTIFICATION, // Title Horizontal
                    // justification
                    TitledBorder.DEFAULT_POSITION, // Title border
                    // alignment
                    XHIBITConstant.getCurrentFont(), Color.black));
        }
        return judgePanel;
    }

    private JLabel createJLabel(String resourceKey) {
        return new JLabel(getResource(resourceKey));
    }

    private String getResource(final String resourceKey) {
        return ResourceBundleHelper.getResource(XhibitBundles.CaseProgressResources, resourceKey);
    }

    /**
     * Will return the 'packed' dimension of the table when it contains its data
     * to view.
     * 
     * @param table
     *            the table to find its dimension.
     * 
     * @return the dimension of the table.
     */
    private Dimension getTableDimension(JTable table) {
        // Calculate the full height of the table by traversing each row, and
        // getting each row's height.
        int height = 0;
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            height += table.getRowHeight(i) + table.getRowMargin();
        }

        // Return the dimension.
        return new Dimension(0, height);
    }

    /**
     * Utility method for creating a toggle button used to control how much data
     * in the given table is displayed.
     * 
     * @param resources
     *            the resources bundle for screen text.
     * @param table
     *            the table whose display data is to be controlled.
     * 
     * @return the newly created toggle button.
     */
    private JToggleButton createButton(XTable table) {
        JToggleButton button = new JToggleButton();
        try {
            button.setAction(XhibitActions.getAction(appController, XhibitActions.ViewChargeDetailsAction, this));
            button.putClientProperty(DETAILS_TOGGLE, new Boolean(false));
            button.putClientProperty(CHARGE_TABLE, table);
            button.setText(getResource(CaseProgressConstants.DETAILS_ON_TXT));
        } catch (ActionNotFoundException e) {
            logger.error("Unable to find toggle button actions for CaseProgressChargesPanel");
        }
        return button;
    }

    private class ResizeTable implements Runnable {
        private XTable _table;

        public ResizeTable(XTable table) {
            _table = table;
        }

        public void run() {
            _table.setPreferredScrollableViewportSize(getTableDimension(_table));
            appController.getBodyPanel().invalidate();
            appController.getBodyPanel().validate();
        }
    }
}