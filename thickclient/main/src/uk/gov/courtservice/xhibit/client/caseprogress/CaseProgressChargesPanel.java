package uk.gov.courtservice.xhibit.client.caseprogress;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.HierarchyBoundsAdapter;
import java.awt.event.HierarchyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToggleButton;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: XHIBIT 2 - Case Progress Charges
 * </p>
 * <p>
 * Description: Display Read-Only charge information for defendants within a
 * specific case.
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

public class CaseProgressChargesPanel extends JPanel {
    /** Toggling button for displaying/undisplaying detailed data. */
    private JToggleButton indictmentDetailsButton = null;

    /** Toggling button for displaying/undisplaying detailed data. */
    private JToggleButton breachesDetailsButton = null;

    /** Toggling button for displaying/undisplaying detailed data. */
    private JToggleButton section41DetailsButton = null;

    /** Toggling button for displaying/undisplaying detailed data. */
    private JToggleButton commitalsDetailsButton = null;

    private JToggleButton defDisposalsDetailsButton = null;
    
    private JToggleButton fail2AppearsDetailsButton = null;

    /** The controller responsible for calling this panel. */
    private XhibitApplicationController appController = null;

    /** The logger for debug. */
    private Logger logger = null;

    /**
     * This is the key value used for the client property stored in each toggle
     * button instance. If its corresponding value is true, then the table
     * models must be set with their data in detailed mode. If false, then table
     * models should model their data normally.
     */
    public static final String DETAILS_TOGGLE = "showDetails";

    public static final String CHARGE_TABLE = "chargeTable";

    /**
     * Constructor used for creating all the charge tables displaying the given
     * data.
     * 
     * @param indictmentTableModel
     *            tableModel for the Indictment table.
     * @param section41sTableModel
     *            tableModel for the Section 41s table.
     * @param commitalsTableModel
     *            tableModel for the Commitals For Sentence table.
     * @param breachesTableModel
     *            tableModel for the Breaches table.
     */
    public CaseProgressChargesPanel(XhibitApplicationController controller, TableModel indictmentTableModel,
            TableModel section41sTableModel, TableModel commitalsTableModel, TableModel breachesTableModel,
            TableModel fail2AppearsTableModel, TableModel defendantDisposalTableModel) {
        super();
        this.appController = controller;
        this.logger = CSServices.getLogger(CaseProgressChargesPanel.class);
        init(indictmentTableModel, section41sTableModel, commitalsTableModel, breachesTableModel, fail2AppearsTableModel,
                defendantDisposalTableModel);
    }

    /**
     * Initialise the status of the gui components.
     */
    private void init(TableModel indictmentTableModel, TableModel section41sTableModel, TableModel commitalsTableModel,
            TableModel breachesTableModel, TableModel fail2AppearsTableModel, TableModel defDisposalsTableModel) {
        ResourceBundle resources = XHIBITConstant.getResourceBundle(XhibitBundles.CaseProgressResources);

        // Create a panel to put all the components and set its layout to
        // GridBagLayout.
        JPanel dataPanel = new JPanel();
        GridBagConstraints gbConstraints = null;
        GridBagLayout gbLayout = new GridBagLayout();
        dataPanel.setLayout(gbLayout);

        int rowCounter = 0;
        XTable chargeTable = null;
        if (indictmentTableModel.getRowCount() > 0) {
            chargeTable = XTableFactory.getInstance().createMultiLineTable(indictmentTableModel);
            // Indictments
            JScrollPane indictmentScroller = this.createTableScroller(chargeTable);
            indictmentDetailsButton = this.createButton(resources, chargeTable);
            JLabel indictmentsLabel = new JLabel(XHIBITConstant.getResource(resources,
                    CaseProgressConstants.INDICTMENTS_TXT));

            // Add the indictments Label.
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 0, 0);
            dataPanel.add(indictmentsLabel, gbConstraints);

            // Add the button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(indictmentDetailsButton, gbConstraints);

            rowCounter++;
            // Add the indictment table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(indictmentScroller, gbConstraints);

            rowCounter++;
        }

        if (section41sTableModel.getRowCount() > 0) {
            // Summary Offences
            chargeTable = XTableFactory.getInstance().createMultiLineTable(section41sTableModel);
            JScrollPane section41sScroller = this.createTableScroller(chargeTable);
            section41DetailsButton = this.createButton(resources, chargeTable);
            JLabel section41sLabel = new JLabel(XHIBITConstant.getResource(resources,
                    CaseProgressConstants.SECTION41S_TXT));

            // Add the section41s Label.
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0);
            dataPanel.add(section41sLabel, gbConstraints);

            // Add the section41 button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(section41DetailsButton, gbConstraints);

            rowCounter++;
            // Add the section 41s table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(section41sScroller, gbConstraints);
            rowCounter++;
        }

        if (commitalsTableModel.getRowCount() > 0) {
            // Committal
            chargeTable = XTableFactory.getInstance().createMultiLineTable(commitalsTableModel);
            JScrollPane commitalsScroller = this.createTableScroller(chargeTable);
            commitalsDetailsButton = this.createButton(resources, chargeTable);
            JLabel commitalsLabel = new JLabel(XHIBITConstant.getResource(resources,
                    CaseProgressConstants.COMMITALS_TXT));

            // Add commitals for sentence label
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0);
            dataPanel.add(commitalsLabel, gbConstraints);

            // Add the commitals button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(commitalsDetailsButton, gbConstraints);

            rowCounter++;
            // Add the Commitals For Sentence table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(commitalsScroller, gbConstraints);
            rowCounter++;
        }

        if (fail2AppearsTableModel.getRowCount() > 0){
            //Fail2Appear
            chargeTable = XTableFactory.getInstance().createMultiLineTable(fail2AppearsTableModel);
            JScrollPane fail2AppearsScroller = this.createTableScroller(chargeTable);
            fail2AppearsDetailsButton = this.createButton(resources, chargeTable);
            JLabel fail2AppearsLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.FAIL2APPEARS_TXT));

            // Add the Label.
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0);
            dataPanel.add(fail2AppearsLabel, gbConstraints);

            // Add the details button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(fail2AppearsDetailsButton, gbConstraints);

            rowCounter++;
            // Add the breaches table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(fail2AppearsScroller, gbConstraints);
            rowCounter++;
            
        }
        
        if (breachesTableModel.getRowCount() > 0) {
            // Breach
            chargeTable = XTableFactory.getInstance().createMultiLineTable(breachesTableModel);
            JScrollPane breachesScroller = this.createTableScroller(chargeTable);
            breachesDetailsButton = this.createButton(resources, chargeTable);
            JLabel breachesLabel = new JLabel(XHIBITConstant.getResource(resources, CaseProgressConstants.BREACHES_TXT));

            // Add the breaches Label.
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0);
            dataPanel.add(breachesLabel, gbConstraints);

            // Add the breaches details button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(breachesDetailsButton, gbConstraints);

            rowCounter++;
            // Add the breaches table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(breachesScroller, gbConstraints);
            rowCounter++;
        }                

        if (defDisposalsTableModel != null && (defDisposalsTableModel.getRowCount() > 0)) {
            // Breach
            chargeTable = XTableFactory.getInstance().createMultiLineTable(defDisposalsTableModel);
            JScrollPane defDisposalsScroller = this.createTableScroller(chargeTable);
            defDisposalsDetailsButton = this.createButton(resources, chargeTable);
            JLabel defDisposalsLabel = new JLabel(XHIBITConstant.getResource(resources,
                    CaseProgressConstants.DEF_DISPOSALS_TXT));

            // Add the breaches Label.
            gbConstraints = new GridBagConstraints(0, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                    GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 0, 0);
            dataPanel.add(defDisposalsLabel, gbConstraints);

            // Add the breaches details button
            gbConstraints = new GridBagConstraints(1, rowCounter, 1, 1, 0.0, 0.0, GridBagConstraints.EAST,
                    GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0);
            dataPanel.add(defDisposalsDetailsButton, gbConstraints);

            rowCounter++;
            // Add the breaches table.
            gbConstraints = new GridBagConstraints(0, rowCounter, 2, 1, 1.0, 0.0, GridBagConstraints.NORTH,
                    GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0);
            dataPanel.add(defDisposalsScroller, gbConstraints);
            rowCounter++;
        }

        // used to anchor everything north
        dataPanel.add(new JPanel(), new GridBagConstraints(0, rowCounter, 3, 1, 1.0, 1.0, GridBagConstraints.NORTH,
                GridBagConstraints.HORIZONTAL, new Insets(0, 10, 10, 10), 0, 0));

        JScrollPane dataScroller = new JScrollPane(dataPanel);
        dataScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new GridBagLayout());
        gbConstraints = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0);
        add(dataScroller, gbConstraints);
    }

    /**
     * Utility method for creating a table.
     * 
     * @param tablemodel
     *            the model the table uses for its data.
     */
    private JScrollPane createTableScroller(final XTable table) {
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scroller = new JScrollPane(table);
        scroller.setBorder(null);
        table.setPreferredScrollableViewportSize(getTableDimension(table));
        table.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
            public void ancestorMoved(HierarchyEvent e) {
                table.setPreferredScrollableViewportSize(CaseProgressChargesPanel.this.getTableDimension(table));
                table.revalidate();
                CaseProgressChargesPanel.this.revalidate();
            }

            public void ancestorResized(HierarchyEvent e) {
                table.setPreferredScrollableViewportSize(CaseProgressChargesPanel.this.getTableDimension(table));
                table.revalidate();
                CaseProgressChargesPanel.this.revalidate();
            }
        });
        table.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                table.setPreferredScrollableViewportSize(CaseProgressChargesPanel.this.getTableDimension(table));
                table.revalidate();
                CaseProgressChargesPanel.this.revalidate();
            }
        });
        return scroller;
    }

    /**
     * Utility method for creating a toggle button.
     * 
     * @param resources
     *            the resources responsible for holding the tooltip text of the
     *            button.
     * 
     * @return the newly created toggle button.
     */
    private JToggleButton createButton(ResourceBundle resources, XTable table) {
        JToggleButton button = new JToggleButton();
        try {
            button.setAction(XhibitActions.getAction(this.appController, XhibitActions.ViewChargeDetailsAction, this));
            button.putClientProperty(DETAILS_TOGGLE, new Boolean(false));
            button.putClientProperty(CHARGE_TABLE, table);
            button.setText(XHIBITConstant.getResource(resources, CaseProgressConstants.DETAILS_ON_TXT));
        } catch (ActionNotFoundException e) {
            logger.error("Unable to find toggle button actions for CaseProgressChargesPanel");
        }
        return button;
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
            height += (table.getRowHeight(i) + table.getRowMargin());
        }

        // Return the dimension.
        return new Dimension(0, height);
    }
}