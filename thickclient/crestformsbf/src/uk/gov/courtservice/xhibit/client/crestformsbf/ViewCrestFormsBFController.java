package uk.gov.courtservice.xhibit.client.crestformsbf;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.client.crestformsbf.swing.XTable;
import uk.gov.courtservice.xhibit.client.crestformsbf.swing.table.SortTableModel;
import uk.gov.courtservice.xhibit.client.models.crestformsbf.CrestFormsBFModel;
import uk.gov.courtservice.xhibit.client.print.FOPInterface;
import uk.gov.courtservice.xhibit.client.print.factory.FOPFactory;
import uk.gov.courtservice.xhibit.client.util.OkCancelPanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title: The Crest Form B - F View Controller
 * </p>
 * <p>
 * Description: A dialog for selecting which Crest Forms to view. This dialog is
 * made up of two panels the body panel and the button panel, comunication
 * bettween the panels is handled by the dialog.
 * </p>
 * <p>
 * The body panel contains a panel with a tabular representation of the forms
 * model. When a selection is made the print and preview buttons become active
 * (see selectionChanged on the controller).
 * </p>
 * <p>
 * The button panel contains a view and a print (also a close but this is
 * handled by the framework) these get the formated xml data from the model and
 * pass it to the print framework.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment 2003
 */

public class ViewCrestFormsBFController extends XDialog {

    /*
     * Logger
     */
    private static final Logger log = CSServices.getLogger(ViewCrestFormsBFController.class);

    /*
     * Resource bundle keys
     */
    private static final String CONTROLLER_TITLE_KEY = "controller.title";

    private static final String CONTROLLER_CLOSE_TEXT_KEY = "controller.close.text";

    private static final String CONTROLLER_TABLE_CASE_HEADER_KEY = "controller.table.case.header";

    private static final String CONTROLLER_TABLE_LINKED_HEADER_KEY = "controller.table.linked.header";

    private static final String CONTROLLER_TABLE_DEFENDANT_HEADER_KEY = "controller.table.defendant.header";

    private static final String CONTROLLER_TABLE_FORM_HEADER_KEY = "controller.table.form.header";

    /*
     * Resource bundle key prefixs (used for actions!)
     */
    private static final String PRINT_KEY_PREFIX = "PrintCrestFormsBF";

    private static final String PREVIEW_KEY_PREFIX = "PreviewCrestFormsBF";

    private static final String CLOSE_KEY_PREFIX = "CloseCrestFormsBF";

    private static final String SELECT_ALL_KEY_PREFIX = "SelectAllCrestFormsBF";

    private static final String DESELECT_KEY_PREFIX = "DeselectCrestFormsBF";

    /**
     * The resource bundle cache, do not access directly use getResourceBundle()
     */
    private static ResourceBundle resourceBundle = null;

    /**
     * The crest forms b - f model
     */
    private final CrestFormsBFModel model;

    /**
     * Construct a new instance with the frame as its controller
     * 
     * @param frame
     *            the parent class
     * @param title
     *            the title of this dialog
     * @throws CSRecoverableException
     *             wherever needed
     */
    public ViewCrestFormsBFController(XhibitApplicationController xac) throws CSRecoverableException {
        super(xac, getResource(CONTROLLER_TITLE_KEY), false);
        model = new CrestFormsBFModel(xac.getApplicationCaseModel().getCaseId(), xac.getApplicationCaseModel()
                .getScheduledHearingId());
        selectionChanged(model.getSelectedIndicies());
        addBodyPanel(new BodyPanel(this));
        pack();
    }

    /**
     * Get the controllers model
     * 
     * @return the crest forms b - f model
     */
    public CrestFormsBFModel getModel() {
        return model;
    }

    /**
     * Overridden button creation to get the custom button panel we want
     * 
     * @param panelType
     *            Note this is ignored as we create our own custom panel type
     * @param defaultButton
     *            Note this is ignored as we create our own custom panel type
     */
    protected OkCancelPanel createButtonPanel(int panelType, int defaultButton) {
        return new ViewPrintClosePanel(this);
    }

    /**
     * Get the ViewPrintClosePanel
     * 
     * @return the current ViewPrintClosePanel
     */
    private ViewPrintClosePanel getViewPrintClosePanel() {
        return (ViewPrintClosePanel) buttonPanel;
    }

    /**
     * Get the BodyPanel
     * 
     * @return the current body panel
     */
    private BodyPanel getBodyPanel() {
        return (BodyPanel) bodyPanel;
    }

    /**
     * Called when the table selection changes (used to update the model)
     * 
     * @oaram indicies the newly selected indicies
     */
    private void selectionChanged(int[] indicies) {
        if (indicies.length == 0) {
            getViewPrintClosePanel().setNoFormsSelected();
        } else if (indicies.length == model.getFormCount()) {
            getViewPrintClosePanel().setAllFormsSelected();
        } else {
            getViewPrintClosePanel().setSomeFormsSelected();
        }
        model.setSelectedIndicies(indicies);
    }

    /**
     * Called when the print button is clicked, print the selected forms
     * 
     * @param e
     *            the event that trigered this method call
     * @throws Exception
     *             if an error occures
     */
    private void printClicked(ActionEvent e) throws Exception {
        renderXml(model.getFormatedXml(), false);
    }

    /**
     * Called when the preview button is clicked, preview the selected forms
     * 
     * @param e
     *            the event that trigered this method call
     * @throws Exception
     *             if an error occures
     */
    private void previewClicked(ActionEvent e) throws Exception {
        renderXml(model.getFormatedXml(), true);
    }

    /**
     * Called when the select all button is clicked, select all the forms
     * 
     * @param e
     *            the event that trigered this method call
     * @throws Exception
     *             if an error occures
     */
    private void selectAllClicked(ActionEvent e) throws Exception {
        getBodyPanel().selectAll();
    }

    /**
     * Called when the deselect button is clicked, deselect all the forms
     * 
     * @param e
     *            the event that trigered this method call
     * @throws Exception
     *             if an error occures
     */
    private void deselectClicked(ActionEvent e) throws Exception {
        getBodyPanel().deselect();
    }

    /**
     * Render the formated xml using the standard FOP print components
     * 
     * @param formatedXml
     *            the fop xml to print
     * @param preview
     *            true if a preview is required, false otherwise
     * @throws CSRecoverableException
     *             if an error occures while printing
     */
    private void renderXml(String formatedXml, boolean preview) throws CSRecoverableException {
        log.debug("Rendering (" + preview + "): " + formatedXml);
        try {
            FOPInterface fop = FOPFactory.getFOPRenderer(preview);
            fop.printDocument(formatedXml, preview);
        } catch (Exception e) {
            throw new CSRecoverableException("gui.crestformsbf.print", "An error occurred whilst printing.", e);
        }
    }

    /**
     * Get the keyed resource
     * 
     * @param key
     *            the key of the resource to look up
     * @return a <code>String</code> containing the resource keyed by the
     *         parameter
     */
    private static String getResource(String key) {
        return getResourceBundle().getString(key);
    }

    /**
     * Get the resource bundle (this cached to improve performance)
     * 
     * @return a <code>ResourceBundle</code> object containing the resources
     *         for this controller
     */
    private static ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = XHIBITConstant.getResourceBundle(XhibitBundles.ViewCrestFormsBF);
        }
        return resourceBundle;
    }

    /**
     * <p>
     * Title: The Crest Form B - F View Controller Main Panel
     * </p>
     * <p>
     * Description: The panel for selecting which Crest Forms to view.
     * </p>
     * 
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author William Fardell, Xdevelopment 2003
     */
    private static class BodyPanel extends XPanel {

        /*
         * Single constraint to have the table fill the available area (with
         * standard indentation)
         */
        private static final GridBagConstraints tableConstraints = new GridBagConstraints();
        static {
            tableConstraints.gridx = 0;
            tableConstraints.gridy = 0;
            tableConstraints.fill = GridBagConstraints.BOTH;
            tableConstraints.weightx = 1.0;
            tableConstraints.weighty = 1.0;
            tableConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        private final SortTableModel model;

        private final XTable table;

        /**
         * Construct an instance of the panel
         */
        public BodyPanel(final ViewCrestFormsBFController controller) {
            super(new GridBagLayout());
            model = new SortTableModel(new TableModel(controller));
            table = new XTable(model);
            table.getTableHeader().setReorderingAllowed(false);
            table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        controller.selectionChanged(model.mapRowIndicies(table.getSelectedRows()));
                    }
                }
            });
            add(new JScrollPane(table), tableConstraints);
        }

        /**
         * Select All Rows
         */
        public void selectAll() {
            table.getSelectionModel().setSelectionInterval(0, table.getRowCount() - 1);
        }

        /**
         * Deselect All Rows
         */
        public void deselect() {
            table.getSelectionModel().clearSelection();
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepInitialise()
         *      XPanel
         */
        public void stepInitialise() throws CSRecoverableException {
            log.debug("stepInitialise");
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepActivate()
         *      XPanel
         */
        public void stepActivate() throws CSRecoverableException {
            log.debug("stepActivate");
            stepUpdateViewState();
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepUpdateViewState()
         *      XPanel
         */
        public void stepUpdateViewState() throws CSRecoverableException {
            log.debug("stepUpdateViewState");
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepValidate()
         *      XPanel
         */
        public void stepValidate() throws CSValidationException, CSRecoverableException {
            log.debug("stepValidate");
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeactivate()
         *      XPanel
         */
        public void stepDeactivate() throws CSRecoverableException {
            log.debug("stepDeactivate");
        }

        /**
         * XPanel Implemenation
         * 
         * @see uk.gov.courtservice.xhibit.client.util.XPanel#stepDeinitialise()
         *      XPanel
         */
        public void stepDeinitialise(boolean update) throws CSRecoverableException {
            log.debug("stepDeinitialise");
        }

        /**
         * <p>
         * Title: The Crest Form B - F View Controller Table Model
         * </p>
         * <p>
         * Description: This table model wraps the crest forms b-f table model
         * </p>
         * 
         * <p>
         * Copyright: Copyright (c) 2003
         * </p>
         * <p>
         * Company: EDS
         * </p>
         * 
         * @author William Fardell, Xdevelopment 2003
         */
        private static class TableModel extends AbstractTableModel {

            /**
             * Cache the column names from the resource bundle
             */
            private static final String[] COLUMN_NAMES = new String[] { getResource(CONTROLLER_TABLE_CASE_HEADER_KEY),
            // getResource(CONTROLLER_TABLE_LINKED_HEADER_KEY),
                    getResource(CONTROLLER_TABLE_DEFENDANT_HEADER_KEY), getResource(CONTROLLER_TABLE_FORM_HEADER_KEY) };

            /**
             * The column classes
             */
            private static final Class[] COLUMN_CLASSES = new Class[] { String.class,
            // Boolean.class,
                    String.class, String.class };

            /**
             * Store the model as it is used frequently and doesnt change!
             */
            private final CrestFormsBFModel model;

            /**
             * Construct a TableModel for the given controller
             */
            public TableModel(ViewCrestFormsBFController controller) {
                model = controller.getModel();
            }

            /**
             * This model contains 3 columns, Case - case reference Linked -
             * true if the case is linked, otherwise false (removed) Defendant -
             * defendants full name Form - form name
             * 
             * @param columnIndex
             *            the index of the column we are looking for
             * @return the indexed column name
             * @throws IllegalArgumentException
             *             if the columnIndex is out of range
             */
            public String getColumnName(int columnIndex) throws IllegalArgumentException {
                if (columnIndex < 0 || columnIndex >= getColumnCount()) {
                    throw new IllegalArgumentException("columnIndex: " + columnIndex);
                }
                return COLUMN_NAMES[columnIndex];
            }

            /**
             * This model contains 3 columns of the following types Case -
             * String Linked - Boolean (removed) Defendant - String Form -
             * String
             * 
             * @param columnIndex
             *            the index of the column we are looking for
             * @return the class of the column
             * @throws IllegalArgumentException
             *             if the columnIndex is out of range
             */
            public Class getColumnClass(int columnIndex) throws IllegalArgumentException {
                if (columnIndex < 0 || columnIndex >= getColumnCount()) {
                    throw new IllegalArgumentException("columnIndex: " + columnIndex);
                }
                return COLUMN_CLASSES[columnIndex];
            }

            /**
             * This model contains 4 columns
             * 
             * @return the number of columns
             * @see getColumnName(int) getColumnName
             */
            public int getColumnCount() {
                return COLUMN_NAMES.length;
            }

            /**
             * Get the number of forms in the model
             * 
             * @return the number of forms in the model
             */
            public int getRowCount() {
                return model.getFormCount();
            }

            /**
             * Return the form values in a tabuler form the columnIndex is
             * mapped to form object fields as follows:
             * 
             * <ol>
             * <li>Form -> Case -> Reference</li>
             * <li>Form -> Case -> Linked</li>
             * (removed)
             * <li>Form -> Defendant -> Full Name</li>
             * <li>Form -> Type -> Name</li>
             * <ol>
             * 
             * @param rowIndex
             *            the index of the form to return
             * @param columnIndex
             *            the field to return (see above)
             * @return the indexed form value
             * @throws IllegalArgumentException
             *             if either index is out of range
             */
            public Object getValueAt(int rowIndex, int columnIndex) throws IllegalArgumentException {
                switch (columnIndex) {
                case 0:
                    return model.getForm(rowIndex).getCase().getReference();
                    // case 1:
                    // return new
                    // Boolean(model.getForm(rowIndex).getCase().isLinked());
                case 1:
                    return model.getForm(rowIndex).getDefendant().getFullName();
                case 2:
                    return model.getForm(rowIndex).getType().getDisplayName();

                default:
                    throw new IllegalArgumentException("columnIndex: " + columnIndex);
                }
            }
        }
    }

    /**
     * <p>
     * Title: The Crest Form B - F View Controller Button Panel
     * </p>
     * <p>
     * Description: This is a nasty fudge for working arround the limitations of
     * the XDialog's button panel implemenation. The current implementation is
     * not very flexible (for evidence see cast in setApplyAction()). We create
     * our own implementaion of OKCancelPanel where Cancel is changed to Close,
     * Ok is removed and View and Print buttons have been added.
     * </p>
     * 
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author William Fardell, Xdevelopment 2003
     */
    private static class ViewPrintClosePanel extends OkCancelPanel {

        /*
         * Note the underlying panel is cleared before the components are added
         * again
         */
        private static final GridBagConstraints selectAllButtonConstraints = new GridBagConstraints();
        static {
            selectAllButtonConstraints.gridx = 0;
            selectAllButtonConstraints.gridy = 0;
            selectAllButtonConstraints.anchor = GridBagConstraints.EAST;
            selectAllButtonConstraints.weightx = 1.0;
            selectAllButtonConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        private static final GridBagConstraints deselectButtonConstraints = new GridBagConstraints();
        static {
            deselectButtonConstraints.gridx = 1;
            deselectButtonConstraints.gridy = 0;
            deselectButtonConstraints.anchor = GridBagConstraints.EAST;
            deselectButtonConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        private static final GridBagConstraints previewButtonConstraints = new GridBagConstraints();
        static {
            previewButtonConstraints.gridx = 2;
            previewButtonConstraints.gridy = 0;
            previewButtonConstraints.anchor = GridBagConstraints.EAST;
            previewButtonConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        private static final GridBagConstraints printButtonConstraints = new GridBagConstraints();
        static {
            printButtonConstraints.gridx = 3;
            printButtonConstraints.gridy = 0;
            printButtonConstraints.anchor = GridBagConstraints.EAST;
            printButtonConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        private static final GridBagConstraints closeButtonConstraints = new GridBagConstraints();
        static {
            closeButtonConstraints.gridx = 4;
            closeButtonConstraints.gridy = 0;
            closeButtonConstraints.anchor = GridBagConstraints.EAST;
            closeButtonConstraints.insets = XHIBITConstant.nonContainerInsets;
        }

        /*
         * Control references
         */

        private final JButton selectAllButton;

        private final JButton deselectButton;

        private final JButton previewButton;

        private final JButton printButton;

        /**
         * Construct an instance of the ViewPrintClosePanel panel
         */
        public ViewPrintClosePanel(final ViewCrestFormsBFController controller) {
            super(controller, DEFAULTCANCEL);
            removeAll();

            selectAllButton = createSelectAllButton(controller);
            add(selectAllButton, selectAllButtonConstraints);

            deselectButton = createDeselectButton(controller);
            add(deselectButton, deselectButtonConstraints);

            previewButton = createPreviewButton(controller);
            add(previewButton, previewButtonConstraints);

            printButton = createPrintButton(controller);
            add(printButton, printButtonConstraints);

            ((XAction) cancelButton.getAction()).populateFromBundle(CLOSE_KEY_PREFIX);
            add(cancelButton, closeButtonConstraints);
        }

        /**
         * Set the buttons if all the forms are selected
         */
        public void setAllFormsSelected() {
            previewButton.setEnabled(true);
            printButton.setEnabled(true);
            selectAllButton.setEnabled(false);
            deselectButton.setEnabled(true);
        }

        /**
         * Set the buttons if some the forms are selected
         */
        public void setSomeFormsSelected() {
            previewButton.setEnabled(true);
            printButton.setEnabled(true);
            selectAllButton.setEnabled(true);
            deselectButton.setEnabled(true);
        }

        /**
         * Set the buttons if no forms are selected
         */
        public void setNoFormsSelected() {
            previewButton.setEnabled(false);
            printButton.setEnabled(false);
            selectAllButton.setEnabled(true);
            deselectButton.setEnabled(false);
        }

        /**
         * Create a new select all button that delegates the select all
         * functionality to the controller
         * 
         * @return the new select all button
         */
        private JButton createSelectAllButton(final ViewCrestFormsBFController controller) {
            return new JButton(new XAction(SELECT_ALL_KEY_PREFIX) {
                public void xActionPerformed(ActionEvent e) throws Exception {
                    controller.selectAllClicked(e);
                }
            });
        }

        /**
         * Create a new deselect button that delegates the deselect
         * functionality to the controller
         * 
         * @return the new deselect button
         */
        private JButton createDeselectButton(final ViewCrestFormsBFController controller) {
            return new JButton(new XAction(DESELECT_KEY_PREFIX) {
                public void xActionPerformed(ActionEvent e) throws Exception {
                    controller.deselectClicked(e);
                }
            });
        }

        /**
         * Create a new print button that delegates the print functionality to
         * the controller
         * 
         * @return the new print button
         */
        private JButton createPrintButton(final ViewCrestFormsBFController controller) {
            return new JButton(new XAction(PRINT_KEY_PREFIX) {
                public void xActionPerformed(ActionEvent e) throws Exception {
                    controller.printClicked(e);
                }
            });
        }

        /**
         * Create a new preview button that delegates the preview functionality
         * to the controller
         * 
         * @return the new preview button
         */
        private JButton createPreviewButton(final ViewCrestFormsBFController controller) {
            return new JButton(new XAction(PREVIEW_KEY_PREFIX) {
                public void xActionPerformed(ActionEvent e) throws Exception {
                    controller.previewClicked(e);
                }
            });
        }
    }
}
