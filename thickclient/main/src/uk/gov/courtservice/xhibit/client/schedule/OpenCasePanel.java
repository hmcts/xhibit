package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_room.XhbCourtRoomBasicValue;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationDetail;
import uk.gov.courtservice.xhibit.business.services.migration.MigrationMessageType;
import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseMigratedPopup;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XToolbarButton;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: This is the panel that allows the user to search for a case and
 * open it.
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
public class OpenCasePanel extends XPanel {
    // Schedule variables
    static ResourceBundle res = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");

    private uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue selectedShv = null;

    private ScheduleHelper sh = null;

    private DefaultMutableTreeNode currentNode = null;

    // Security Variables
    private boolean userCanView = false;

    private boolean userCanUpdate = false;

    private int currentCourtRoom;

    // Widgets
    private GridBagLayout gbLayout = new GridBagLayout();

    private JLabel lblCourt = new JLabel();

    private JTextField txtSelectedCourt = new JTextField();

    private JPanel jpSpacer = XHIBITConstant.getSpacer();

    private OpenCaseCourtPanel courtPanel = new OpenCaseCourtPanel();

    private OpenCaseHearingPanel hearingPanel = new OpenCaseHearingPanel();

    private JCheckBox chkReadOnly = new JCheckBox();

    private Insets pnlInset = XHIBITConstant.nonContainerInsets;

    private OpenCaseDialog myParent;

    private OpenCasePanel_btnUpLevelAction btnUpAction = null;

    private static final Logger log = CSServices.getLogger(OpenCasePanel.class);

    public OpenCasePanel(OpenCaseDialog parent) throws CSRecoverableException {
        super();
        myParent = parent;
        stepInitialise();
        jbInit();
        stepActivate();
        stepUpdateViewState();
    }

    void jbInit() {
        lblCourt.setText(res.getString("Selected_Court_"));
        this.setLayout(gbLayout);
        this.setPreferredSize(new Dimension(500, 300));
        txtSelectedCourt.setText("");
        txtSelectedCourt.setEditable(false);
        txtSelectedCourt.setPreferredSize(new Dimension(250, 25));
        txtSelectedCourt.setMinimumSize(new Dimension(150, 20));

        chkReadOnly.setText(res.getString("Read_Only"));
        if (!userCanUpdate) {
            chkReadOnly.setSelected(true);
            chkReadOnly.setEnabled(false);
        }

        this.add(lblCourt, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, pnlInset, 0, 0));
        this.add(txtSelectedCourt, new GridBagConstraints(1, 0, 1, 1, 0.75, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, pnlInset, 0, 0));
        this.add(getUpLevelButton(), new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, pnlInset, 0, 0));
        this.add(jpSpacer, new GridBagConstraints(3, 0, 1, 1, 0.25, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, pnlInset, 0, 0));
        this.add(getCentrePanel(), new GridBagConstraints(0, 1, 4, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, pnlInset, 0, 0));
        this.add(chkReadOnly, new GridBagConstraints(0, 2, 4, 1, 0.0, 0.0, GridBagConstraints.EAST,
                GridBagConstraints.NONE, pnlInset, 0, 0));
    }

    void btnUpLevel_actionPerformed(ActionEvent e) {
        if (currentNode != null) {
            populateCourtTable((DefaultMutableTreeNode) currentNode.getParent());
            updateReadOnly();
            stepUpdateViewState();
        }
    }

    private XToolbarButton getUpLevelButton() {
        XToolbarButton xb = new XToolbarButton(XToolbarButton.xbIcon);
        xb.setAction(getBtnUpAction());
        return xb;
    }

    private JPanel getCentrePanel() {
        JPanel jp = new JPanel(new GridBagLayout());
        courtPanel.getCourtTable().addMouseListener(new TableListener());
        // Remove default action for return
        courtPanel.getCourtTable().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).getParent().remove(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        // Add a listener for return
        courtPanel.getCourtTable().addKeyListener(new ReturnListener());
        ListSelectionModel rowSM1 = courtPanel.getCourtTable().getSelectionModel();
        rowSM1.addListSelectionListener(new RowListener());

        hearingPanel.getHearingTable().addMouseListener(new TableListener());
        // Remove default action for return
        hearingPanel.getHearingTable().getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).getParent().remove(
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
        // Add a listener for return
        hearingPanel.getHearingTable().addKeyListener(new ReturnListener());
        ListSelectionModel rowSM2 = hearingPanel.getHearingTable().getSelectionModel();
        rowSM2.addListSelectionListener(new RowListener());

        jp.add(courtPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, pnlInset, 0, 0));
        jp.add(hearingPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, pnlInset, 0, 0));
        return jp;
    }

    private OpenCasePanel_btnUpLevelAction getBtnUpAction() {
        if (btnUpAction == null) {
            btnUpAction = new OpenCasePanel_btnUpLevelAction(this);
        }
        return btnUpAction;
    }

    private void populateCourtTable(DefaultMutableTreeNode currentNode) {
        chkReadOnly.setEnabled(false);
        this.currentNode = currentNode;
        ArrayList courts = (ArrayList) sh.getNodeChildren(currentNode);
        courtPanel.setVisible(false);
        hearingPanel.setVisible(false);

        OpenCaseCourtModel cm = new OpenCaseCourtModel(courts.toArray());
        courtPanel.getCourtTable().setModel(cm);

        courtPanel.setVisible(true);
    }

    private void populateHearingTable(DefaultMutableTreeNode currentNode) {
        if (userCanUpdate) {
            int selectedCourtRoomId = ((CourtRoomValueHelper) currentNode.getUserObject()).getModel().getCourtRoomId()
                    .intValue();
            if (currentCourtRoom == selectedCourtRoomId) {
                chkReadOnly.setEnabled(true);
            } else {
                chkReadOnly.setSelected(true);
                chkReadOnly.setEnabled(FunctionList.hasAccess(FunctionList.ECourtLogOut));
            }
        }
        this.currentNode = currentNode;
        ArrayList hearings = (ArrayList) sh.getNodeChildren(currentNode);
        courtPanel.setVisible(false);
        hearingPanel.setVisible(false);

        // OpenCaseHearingTableModel cm = new
        // OpenCaseHearingTableModel(hearings.toArray());
        // hearingPanel.getHearingTable().setModel(cm);
        ((XHIBITTableModelInterface) hearingPanel.getHearingTable().getModel()).setData(hearings);

        // hearingPanel.getHearingTable().getColumnModel().getColumn(0).setPreferredWidth(100);
        // hearingPanel.getHearingTable().getColumnModel().getColumn(1).setPreferredWidth(150);
        // hearingPanel.getHearingTable().getColumnModel().getColumn(2).setPreferredWidth(250);

        hearingPanel.setVisible(true);
    }

    private void drillDown(Object selectedItem) throws UserCancelException {
        if (selectedItem instanceof ScheduledHearingValueHelper)
            okClicked();
        else {
            DefaultMutableTreeNode node = sh.findObjectInChildren(currentNode, selectedItem);
            if (selectedItem instanceof CourtRoomValueHelper) {
                populateHearingTable(node);
            } else {
                populateCourtTable(node);
            }
        }
    }

    public boolean allowOpenFloating = false;

    // This is used to find out if the current row can be selected/event
    // fired on it.
    public boolean canOpenFloating(ScheduledHearingValue shv) {
        // If the current row is not an SHV, a null is passed in, therefore
        // selectable by default
        if (shv == null)
            return true;

        // if floating cases are allowed return true
        if (allowOpenFloating)
            return true;

        // If the case is a floating case
        if (shv.getIsFloating() != null && shv.getIsFloating().booleanValue()) {
            // floating case so return false
            return false;
        } else {
            // not a floating case so true
            return true;
        }
    }

    public void okClicked() throws UserCancelException {
        // either drill down or open the case.

        // get selected item
        Object selectedItem = null;
        if (getCurrentSelectedRow() < 0)
            throw new UserCancelException();
        selectedItem = getCurrentTableModel().getDataAt(getCurrentSelectedRow());

        if (selectedItem instanceof ScheduledHearingValueHelper) {
            // if ok is not enabled, cancel this
            if (!myParent.getOkAction().isEnabled())
                throw new UserCancelException();

            selectedShv = ((ScheduledHearingValueHelper) selectedItem).getModel();
            
            // XDMX7
            int caseId = selectedShv.getCaseId();
            MigrationDetail migrationDetail = XhibitDelegateHelper.getMigrateCaseDelegate().getMigrationDetails(caseId, MigrationMessageType.READONLY);
            
            if (migrationDetail != null && migrationDetail.isMigrated) {
                XhibitApplicationController xac = (XhibitApplicationController) myParent.getParentFrame();
    			new CaseMigratedPopup(xac, migrationDetail.migrationTo).setVisible(true);
    			chkReadOnly.setSelected(true);;
            }
            
            // if (selectedShv.getIsFloating() != null &&
            // !selectedShv.getIsFloating().booleanValue() ) {
            if (canOpenFloating(selectedShv)) {
                // check if opening out of court
                if (!chkReadOnly.isSelected()) {
                    if (!XhibitSingleton.getInstance().isUserInCourtroom()
                            || myParent.getShv().getCourtRoomId().intValue() != XhibitSingleton.getInstance()
                                    .getCourtRoomId().intValue()) {
                        int rc = JOptionPane.showConfirmDialog(myParent.getParentFrame(), XHIBITConstant.getResource(
                                XhibitBundles.TodaysSchedule, "OpenUpdateMessage"), XHIBITConstant.getResource(
                                XhibitBundles.TodaysSchedule, "OpenUpdateTitle"), JOptionPane.YES_NO_OPTION);
                        if (rc != JOptionPane.YES_OPTION)
                            throw new UserCancelException();
                    }
                }

                myParent.dispose();
            }
        } else {
            drillDown(selectedItem);
            updateReadOnly();
            stepUpdateViewState();
        }
    }

    private void updateReadOnly() {
        // Set the read only flag
        if (chkReadOnly.isEnabled()) {
            if (currentNode.getUserObject() instanceof CourtRoomValueHelper) {
                XhbCourtRoomBasicValue crv = ((CourtRoomValueHelper) currentNode.getUserObject()).getModel();
                if (crv.getCourtRoomId().intValue() == getCurrentCourtRoom()) {
                    chkReadOnly.setSelected(false);
                } else {
                    chkReadOnly.setSelected(true);
                }
            }
        }
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue getShv() {
        return selectedShv;
    }

    public boolean isReadOnly() {
        return chkReadOnly.isSelected();
    }

    protected boolean hasViewAccess() {
        return userCanView;
    }

    protected void setUserCanView(boolean hasAccess) {
        userCanView = hasAccess;
    }

    protected boolean hasUpdateAccess() {
        return userCanUpdate;
    }

    protected void setUserCanUpdate(boolean hasAccess) {
        userCanUpdate = hasAccess;
    }

    protected int getCurrentCourtRoom() {
        return currentCourtRoom;
    }

    public void stepInitialise() throws CSRecoverableException {
        // Get data from ScheduleController
        sh = new ScheduleHelper();

        // Get User session so that the security can be queried.
        // XhibitSingleton xs = XhibitSingleton.getInstance();
        // CSUserSession csu = xs.getUserSession();

        // Initialise access rights
        XhibitApplicationController xac = (XhibitApplicationController) myParent.getParentFrame();
        setUserCanView(XhibitActions.getAction(xac, XhibitActions.ViewCase).hasReadAccess());
        setUserCanUpdate(XhibitActions.getAction(xac, XhibitActions.UpdateCase).hasEditAccess());

        // Get the current court if available
        if (XhibitSingleton.getInstance().isUserInCourtroom()) {
            currentCourtRoom = XhibitSingleton.getInstance().getCourtRoomId().intValue();
        } else {
            currentCourtRoom = -1;
        }
    }

    public void stepActivate() throws CSRecoverableException {
        // Is in court room?
        if (XhibitSingleton.getInstance().isUserInCourtroom()) {
            // If yes, then find the court room in the tree and show the
            // children
            DefaultMutableTreeNode node = sh.findCourtInTree(new Integer(getCurrentCourtRoom()));
            if (node == null) {
                populateCourtTable(sh.getTreeRoot());
            } else {
                populateHearingTable(node);
            }
        } else {
            // Otherwise get the root and show the childern.
            populateCourtTable(sh.getTreeRoot());
        }
    }

    public void stepUpdateViewState() {
        if (currentNode != null) {
            // If the item is a leaf then enable the up button
            btnUpAction.setEnabled(!currentNode.isRoot());

            if (getCurrentSelectedRow() >= 0
                    && getCurrentTableModel().getDataAt(getCurrentSelectedRow()) instanceof ScheduledHearingValueHelper) {
                ScheduledHearingValueHelper shvh = (ScheduledHearingValueHelper) getCurrentTableModel().getDataAt(
                        getCurrentSelectedRow());
                if (shvh.getModel().getIsFloating() != null) {
                    if (shvh.getModel().getIsFloating().booleanValue()) {
                        getCurrentJTable().getSelectionModel().clearSelection();
                    }
                }
                myParent.getOkAction().populateFromBundle("btnOk");
                if (getCurrentSelectedRow() < 0)
                    myParent.getOkAction().setEnabled(false);
                else
                    myParent.getOkAction().setEnabled(true);
            } else {
                myParent.getOkAction().populateFromBundle("btnOpen");
                if (getCurrentSelectedRow() < 0)
                    myParent.getOkAction().setEnabled(false);
                else
                    myParent.getOkAction().setEnabled(true);
            }
        }

        txtSelectedCourt.setText(getParentName(currentNode) + getCurrentTableSelection());
    }

    private String getCurrentTableSelection() {
        if (getCurrentSelectedRow() >= 0) {
            return "\\" + getCurrentTableModel().getValueAt(getCurrentSelectedRow(), 0);
        } else {
            return "\\";
        }
    }

    private String getParentName(DefaultMutableTreeNode node) {
        if (node.isRoot())
            return "";
        else {
            if (!(node.getUserObject() instanceof ScheduledHearingValueHelper)) {
                return getParentName((DefaultMutableTreeNode) node.getParent()) + "\\"
                        + node.getUserObject().toString();
            } else {
                return "";
            }
        }
    }

    private int getCurrentSelectedRow() {
        if (hearingPanel.isVisible())
            return hearingPanel.getHearingTable().getSelectedRow();
        if (courtPanel.isVisible())
            return courtPanel.getCourtTable().getSelectedRow();
        return -1;
    }

    private XHIBITTableModelInterface getCurrentTableModel() {
        if (hearingPanel.isVisible())
            return (XHIBITTableModelInterface) hearingPanel.getHearingTable().getModel();
        if (courtPanel.isVisible())
            return (XHIBITTableModelInterface) courtPanel.getCourtTable().getModel();
        return null;
    }

    public void stepDeinitialise(boolean update) {
        // if cancel was clicked ensure that the selected hearing (if any) is
        // nulled out.
        if (!update)
            selectedShv = null;
    }

    public void stepDeactivate() { // nothing to deactivate
    }

    public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException { // Nothing
        // to
        // validate
    }

    public JTable getCurrentJTable() {
        if (hearingPanel.isVisible()) {
            return hearingPanel.getHearingTable();
        }
        if (courtPanel.isVisible()) {
            return courtPanel.getCourtTable();
        }
        return null;
    }

    public ScheduledHearingValue getCurrentShv() {
        if (getCurrentSelectedRow() >= 0) {
            Object o = getCurrentTableModel().getDataAt(getCurrentSelectedRow());
            if (o instanceof ScheduledHearingValueHelper) {
                return ((ScheduledHearingValueHelper) o).getModel();
            }
        }
        return null;
    }

    class TableListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                getRow(e);
                try {
                    if (canOpenFloating(getCurrentShv()))
                        okClicked();
                } catch (UserCancelException ex) {
                    log.debug(ex);
                }
            } else {
                getRow(e);
                stepUpdateViewState();
            }
        }

        private void getRow(MouseEvent e) {
            // get point where user right clicked.
            int row = getCurrentJTable().rowAtPoint(e.getPoint());
            // Select row in table where user clicked.
            getCurrentJTable().setRowSelectionInterval(row, row);
        }
    }

    class RowListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent e) {
            // Ignore extra messages.
            if (e.getValueIsAdjusting())
                return;

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
            if (lsm.isSelectionEmpty()) {
                // no rows are selected
            } else {
                // int selectedRow = lsm.getMinSelectionIndex();
                stepUpdateViewState();
            }
        }
    }

    class ReturnListener implements KeyListener {

        public void keyPressed(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                e.consume();
                // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("consume:"
                // + e.isConsumed());
                try {
                    if (canOpenFloating(getCurrentShv()))
                        okClicked();
                } catch (UserCancelException ex) {
                    log.debug(ex);
                }
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }
}

class OpenCasePanel_btnUpLevelAction extends XAction {
    static ResourceBundle res = XHIBITConstant.getResourceBundle("XhibitTodaysScheduleResources");

    private OpenCasePanel adaptee;

    public OpenCasePanel_btnUpLevelAction(OpenCasePanel adaptee) {
        this.adaptee = adaptee;
        this.setIcon(XHIBITConstant.imageRoot + "uplevel.gif");
        this.setShortDescription(res.getString("Go_back"));
    }

    public void xActionPerformed(ActionEvent e) {
        adaptee.btnUpLevel_actionPerformed(e);
    }
}
