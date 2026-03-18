package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset.AddEditRotationSetDialog;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels.PagesInRotationSetTableModel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels.RotationSetTableModel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels.ScreenRotationSetTableModel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.listeners.DeleteListener;
import uk.gov.courtservice.xhibit.client.util.listeners.DoubleClickListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TablePopupListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TableRowListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.model.sortable.XSortableTableModel;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetComplexValue;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description: Panel to modify rotation sets (add/edit/delete) To be notified
 * of a change add a propertyChangeListener and listen for one of the change
 * events (NOTIFY_ADD/NOTIFY_EDIT/NOTIFY_DELETE).
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: MaintainRotationSetPanel.java,v 1.7 2006/05/10 08:01:42 bzjrnl
 *          Exp $
 */

public class MaintainRotationSetPanel extends XPanel {

    /**
     * This string is passed in the property change when a rotation set is added
     * "NOTIFY_ADD"
     */
    public static final String NOTIFY_ADD = "NOTIFY_ADD";

    /**
     * This string is passed in the property change when a rotation set is
     * editted "NOTIFY_EDIT"
     */
    public static final String NOTIFY_EDIT = "NOTIFY_EDIT";

    /**
     * This string is passed in the property change when a rotation set is
     * deleted "NOTIFY_DELETE"
     */
    public static final String NOTIFY_DELETE = "NOTIFY_DELETE";

    /**
     * Resource key for the border title. "pd.title.maintainrs"
     */
    private static final String BORDER_TITLE = "pd.title.maintainrs";

    private static final Color defaultListForegroundColor = (Color) UIManager.getDefaults().get("List.foreground");

    private static final int defaultAnchor = GridBagConstraints.CENTER;

    private static final int defaultFill = GridBagConstraints.HORIZONTAL;

    private static final Insets defaultInsets = XHIBITConstant.nonContainerInsets;

    private static final Dimension defaultTableSize = new Dimension(300, 120);

    /**
     * Database value to indicate the rotation set is a system defined one "Y"
     */
    private static final String SYSTEM_RS = "Y";

    private Object[] _data = null;

    private JButton deleteButton;

    private JButton editButton;

    private JButton addButton;

    private XAction deleteAction;

    private XAction editAction;

    private XAction addAction;

    // Table models
    // Note: Do not access rotationSetTableModel directly. Use get model
    // from table
    // private RotationSetTableModel rotationSetTableModel = null;
    // Note: Only access page and screen table model directly to call
    // setSelectedRow.
    private PagesInRotationSetTableModel pageTableModel = null;

    private ScreenRotationSetTableModel screenTableModel = null;

    // Tables
    private XTable rotationSetTable;

    private XTable pageTable;

    private XTable screenTable;

    /**
     * Create an instance of the Rotation Set Panel Gets the data and draws the
     * tables. Uses the inner class to listen for Rotation Set changes
     * 
     * @throws CSRecoverableException
     *             This exception is thrown if there was an error getting the
     *             data from the midtier
     */
    public MaintainRotationSetPanel() throws CSRecoverableException {
        super(new GridBagLayout());
        stepInitialise();
        init();
        this.setBorder(PublicDisplayUtils.createBorder(PublicDisplayUtils.getResource(BORDER_TITLE)));
        this.addPropertyChangeListener(new RotationSetChangeListener(this));
    }

    /**
     * Draw the GUI components
     */
    private void init() {
        this.add(PublicDisplayUtils.getDefaultScrollPane(getRotationSetTable(), defaultTableSize),
                new GridBagConstraints(0, 0, 4, 1, 1.0, 0.4, defaultAnchor, GridBagConstraints.BOTH, defaultInsets, 0,
                        0));

        this.add(new JLabel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, defaultAnchor, defaultFill, defaultInsets,
                0, 0));

        // Buttons
        this.add(getDeleteButton(), new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, defaultAnchor,
                GridBagConstraints.NONE, defaultInsets, 0, 0));
        this.add(getEditButton(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0, defaultAnchor, GridBagConstraints.NONE,
                defaultInsets, 0, 0));
        this.add(getAddButton(), new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, defaultAnchor, GridBagConstraints.NONE,
                defaultInsets, 0, 0));

        this.add(PublicDisplayUtils.getDefaultScrollPane(getPagesTable(), defaultTableSize), new GridBagConstraints(0,
                2, 4, 1, 1.0, 0.3, defaultAnchor, GridBagConstraints.BOTH, defaultInsets, 0, 0));
        this.add(PublicDisplayUtils.getDefaultScrollPane(getScreenTable(), defaultTableSize), new GridBagConstraints(0,
                3, 4, 1, 1.0, 0.3, defaultAnchor, GridBagConstraints.BOTH, defaultInsets, 0, 0));
    }

    /**
     * Get a popup with copy, add, edit and delete
     * 
     * @return Popup menu to be used on the Rotation Set table
     */
    private JPopupMenu getPopup() {
        JPopupMenu popup = new DefaultPopup(DefaultPopup.COPY);
        popup.addSeparator();
        popup.add(getAddAction());
        popup.add(getEditAction());
        popup.add(getDeleteAction());
        return popup;
    }

    /**
     * Gets the rotation set table with a right click popup, double click
     * listener and a delete key listener.
     * 
     * @return
     */
    protected XTable getRotationSetTable() {
        if (rotationSetTable == null) {
            XTable _temp = XTableFactory.getInstance().createDefaultTable(new RotationSetTableModel(getData()));
            _temp.makeSortable();
            _temp.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            _temp.getSelectionModel().addListSelectionListener(new TableRowListener(this));
            _temp.addMouseListener(new TablePopupListener(getPopup(), _temp));
            _temp.addMouseListener(new DoubleClickListener(getEditAction()));
            _temp.addKeyListener(new DeleteListener(getDeleteAction()));
            rotationSetTable = _temp;
        }
        return rotationSetTable;
    }

    /**
     * Prepares the table that shows the lists in a rotation set. The table is
     * disabled, but the foreground color is set to the system foreground so
     * that it is still easy to read. Column index 1 is hidden. This is the sort
     * order
     * 
     * @return Table for displaying Display Documents
     */
    private XTable getPagesTable() {
        if (pageTable == null) {
            pageTableModel = new PagesInRotationSetTableModel(getRotationSetTable());
            XTable _temp = XTableFactory.getInstance().createDefaultTable(pageTableModel);
            _temp.makeSortable();
            _temp.setEnabled(false);
            _temp.setForeground(defaultListForegroundColor);
            _temp.getColumnModel().removeColumn(_temp.getColumnModel().getColumn(1));
            pageTable = _temp;
        }
        return pageTable;
    }

    /**
     * Prepares the table that shows the display the rotation set is currently
     * displayed on. The table is disabled, but the foreground color is set to
     * the system foreground so that it is still easy to read.
     * 
     * @return A table for displaying Displays.
     */
    private XTable getScreenTable() {
        if (screenTable == null) {
            screenTableModel = new ScreenRotationSetTableModel(getRotationSetTable());
            XTable _temp = XTableFactory.getInstance().createDefaultTable(screenTableModel);
            _temp.makeSortable();
            _temp.setEnabled(false);
            _temp.setForeground(defaultListForegroundColor);
            screenTable = _temp;
        }
        return screenTable;
    }

    // Get instances of the buttons
    private JButton getDeleteButton() {
        if (deleteButton == null) {
            deleteButton = JButtonFactory.getButton(getDeleteAction());
        }
        return deleteButton;
    }

    private JButton getEditButton() {
        if (editButton == null) {
            editButton = JButtonFactory.getButton(getEditAction());
        }
        return editButton;
    }

    private JButton getAddButton() {
        if (addButton == null) {
            addButton = JButtonFactory.getButton(getAddAction());
        }
        return addButton;
    }

    // Get instances of the actions
    private XAction getDeleteAction() {
        if (deleteAction == null) {
            deleteAction = new DeleteAction(this);
        }
        return deleteAction;
    }

    private XAction getEditAction() {
        if (editAction == null) {
            editAction = new EditAction(this);
        }
        return editAction;
    }

    private XAction getAddAction() {
        if (addAction == null) {
            addAction = new AddAction(this);
        }
        return addAction;
    }

    /**
     * @return The array of RotationSetComplexValues retrieved from the midtier.
     */
    protected Object[] getData() {
        if (_data == null)
            return new Object[] {};
        else
            return _data;
    }

    /**
     * Gets the array of RotationSetComplexValues from the midtier.
     * 
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        PDConfigurationControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getPDConfigurationDelegate();
        _data = delegate.getRotationSetsDetailForCourt(PublicDisplayUtils.getCourtId(), Locale.getDefault());
    }

    /**
     * Calls stepUpdateViewState
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * When a table row is selected, enable appropriate buttons and feed the new
     * selected rows to pagesInRotationSetTableModel and
     * screensDisplayingRotationSetTableModel so they refresh
     */
    public void stepUpdateViewState() {
        int selectedRow = rotationSetTable.getSelectedRow();
        if (selectedRow >= 0) {
            boolean isEdit = !isSystemRotationSet(selectedRow);
            getDeleteAction().setEnabled(isEdit);
            getEditAction().setEnabled(isEdit);
        } else {
            getDeleteAction().setEnabled(false);
            getEditAction().setEnabled(false);
        }
        pageTableModel.setSelectedRow(selectedRow);
        if (getPagesTable().getModel() instanceof XSortableTableModel) {
            ((XSortableTableModel) getPagesTable().getModel()).sortByColumn(1, true);
        }

        screenTableModel.setSelectedRow(selectedRow);
        if (getScreenTable().getModel() instanceof XSortableTableModel) {
            ((XSortableTableModel) getScreenTable().getModel()).sortByColumn(0, true);
        }
    }

    private boolean isSystemRotationSet(int row) {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) getRotationSetTable().getModel();
        RotationSetComplexValue cv = (RotationSetComplexValue) model.getDataAt(row);
        return cv.getRotationSetBasicValue().getDefaultYn().equals(SYSTEM_RS);
    }

    /**
     * Empty Implementation
     */
    public void stepDeactivate() {
    }

    /**
     * Empty Implementation
     */
    public void stepValidate() {
    }

    /**
     * Empty Implementation
     * 
     * @param parm1
     */
    public void stepDeinitialise(boolean parm1) {
    }

    /**
     * Used to notify listeners that the rotation set list has been modified in
     * some way.
     * 
     * @param updateType
     *            static NOTIFY_ADD/NOTIFY_EDIT/NOTIFY_DELETE
     * @param oldObj
     *            The object prior to update - can be null
     * @param newObj
     *            The object after update - can be null
     */
    protected void notifyUpdate(String updateType, Object oldObj, Object newObj) {
        firePropertyChange(updateType, oldObj, newObj);
    }

    // Inner Classes for Actions

    /**
     * <p>
     * Title: Maintain Rotation Set Delete Action
     * </p>
     * <p>
     * Description: Prompts the user to delete the selected rotation set
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Rakesh Lakhani
     * @version $Id: MaintainRotationSetPanel.java,v 1.7 2006/05/10 08:01:42
     *          bzjrnl Exp $
     */
    class DeleteAction extends XAction {
        private static final String LABEL = "btnDelete";

        private static final String TITLE = "pd.deleteRs.title";

        private static final String MESSAGE = "pd.deleteRs.message";

        private MaintainRotationSetPanel _parent;

        /**
         * Sets up the action name
         * 
         * @param parent
         *            The Maintain Rotation set panel
         */
        public DeleteAction(MaintainRotationSetPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        /**
         * If the user confirms to delete the rotation set, the midtier call is
         * made. If successful, a PropertyChange event is fired to notify of the
         * delete
         * 
         * @param ae
         * @throws CSRecoverableException
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            XHIBITTableModelInterface model = (XHIBITTableModelInterface) _parent.getRotationSetTable().getModel();
            RotationSetComplexValue rs = (RotationSetComplexValue) model.getDataAt(_parent.getRotationSetTable()
                    .getSelectedRow());

            boolean rc = XMessageBox.alert((Dialog) SwingUtilities.getWindowAncestor(_parent), PublicDisplayUtils
                    .getResource(TITLE), true, XMessageBox.ICONQUESTION, PublicDisplayUtils.getResource(MESSAGE)
                    + rs.getRotationSetBasicValue().getDescription(), XMessageBox.YESNO, XMessageBox.DEFAULTNO);
            if (rc) {
                XhibitDelegateHelper.getPDConfigurationDelegate().deleteRotationSets(rs);
                _parent.notifyUpdate(_parent.NOTIFY_DELETE, rs, null);
            }
        }
    }

    /**
     * <p>
     * Title: Maintain Rotation Set Edit Action
     * </p>
     * <p>
     * Description: Launches the edit rotation set window. If closed
     * successfully, a PropertyChange event is fired to notify of the AddEdit
     * with the original rotation set and modified rotation set
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Rakesh Lakhani
     * @version $Id: MaintainRotationSetPanel.java,v 1.7 2006/05/10 08:01:42
     *          bzjrnl Exp $
     */
    class EditAction extends XAction {
        private static final String LABEL = "btnEdit";

        private MaintainRotationSetPanel _parent;

        /**
         * Sets up the action name
         * 
         * @param parent
         *            The Maintain Rotation set panel
         */
        public EditAction(MaintainRotationSetPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        /**
         * Launches the AddEdit dialog. Upon successful closure of the dialog, a
         * notify message is sent
         * 
         * @param parent
         *            The Maintain Rotation set panel
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            XHIBITTableModelInterface model = (XHIBITTableModelInterface) _parent.getRotationSetTable().getModel();
            RotationSetComplexValue originalRs = (RotationSetComplexValue) model.getDataAt(_parent
                    .getRotationSetTable().getSelectedRow());

            AddEditRotationSetDialog dialog = new AddEditRotationSetDialog((Dialog) SwingUtilities
                    .getWindowAncestor(_parent), originalRs.getRotationSetId());
            dialog.setVisible(true);
            if (dialog.isOkClicked()) {
                RotationSetComplexValue newRs = dialog.getRotationSet();
                _parent.notifyUpdate(_parent.NOTIFY_EDIT, originalRs, newRs);
            }
        }
    }

    /**
     * <p>
     * Title: Maintain Rotation Set Add Action
     * </p>
     * <p>
     * Description: Launches the AddEdit dialog for the user to create a new
     * rotation set
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Rakesh Lakhani
     * @version $Id: MaintainRotationSetPanel.java,v 1.7 2006/05/10 08:01:42
     *          bzjrnl Exp $
     */
    class AddAction extends XAction {
        private static final String LABEL = "btnAdd";

        private MaintainRotationSetPanel _parent;

        /**
         * Sets up the action name
         * 
         * @param parent
         *            The Maintain Rotation set panel
         */
        public AddAction(MaintainRotationSetPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        /**
         * Launches the AddEdit dialog. Upon successful closure of the dialog, a
         * notify message is sent The old object is null, the new object is the
         * newly create rotation set (without the ID)
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            AddEditRotationSetDialog dialog = new AddEditRotationSetDialog((Dialog) SwingUtilities
                    .getWindowAncestor(_parent), null);
            dialog.setVisible(true);
            if (dialog.isOkClicked()) {
                RotationSetComplexValue newRs = dialog.getRotationSet();
                _parent.notifyUpdate(_parent.NOTIFY_ADD, null, newRs);
            }
        }
    }

    /**
     * <p>
     * Title: Rotation Set Change Listener
     * </p>
     * <p>
     * Description: Listens for property change events. In all instances,
     * remembers the currently selected row, re-gets the data. Sets the get in
     * the rotation set model. Reselects the row to refresh the other two
     * tables.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: EDS
     * </p>
     * 
     * @author Rakesh Lakhani
     * @version $Id: MaintainRotationSetPanel.java,v 1.7 2006/05/10 08:01:42
     *          bzjrnl Exp $
     */
    class RotationSetChangeListener implements PropertyChangeListener {
        MaintainRotationSetPanel _parent;

        public RotationSetChangeListener(MaintainRotationSetPanel parent) {
            _parent = parent;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            try {
                XHIBITTableModelInterface model = (XHIBITTableModelInterface) _parent.getRotationSetTable().getModel();
                // get selected row.
                int currentRow = _parent.getRotationSetTable().getSelectedRow();
                // Re-get the data
                _parent.stepInitialise();
                // apply it to the table
                model.setData(_parent.getData());
                // refresh display and buttons
                // get row count.
                int newRow = getNewRowToSelect(evt, model, currentRow);

                if (newRow >= 0) {
                    _parent.getRotationSetTable().setRowSelectionInterval(newRow, newRow);
                } else {
                    _parent.stepUpdateViewState();
                }
            } catch (CSRecoverableException ex) {
                XHIBITErrorHandler.handleError(ex);
            }
        }

        /**
         * If ADD select the last row If EDIT reselect row If DELETE select
         * first row
         * 
         * @param evt
         * @param model
         * @return
         */
        private int getNewRowToSelect(PropertyChangeEvent evt, XHIBITTableModelInterface model, int originalRow) {
            int newRow = 0;
            if (model.getRowCount() <= 0)
                newRow = -1;
            if (evt.getPropertyName().equals(_parent.NOTIFY_ADD)) {
                newRow = model.getRowCount() - 1;
            } else if (evt.getPropertyName().equals(_parent.NOTIFY_EDIT)) {
                if (originalRow < model.getRowCount()) {
                    newRow = originalRow;
                } else {
                    newRow = 0;
                }
            } else if (evt.getPropertyName().equals(_parent.NOTIFY_DELETE)) {
                newRow = 0;
            }
            return newRow;
        }
    }
}