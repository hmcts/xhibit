package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT 2 -
 * </p>
 * <p>
 * Description:
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
 * 
 * @author Frederik Vandendriessche
 * @version 1.1 applied bug_fix 52
 */
public class TodaysScheduleController extends XPanel {
    private XhibitApplicationController parent = null;

    private ScheduleHelper sh = null;

    private Object[] allCourtsArray = null;

    // Set the text temporarily incase it fails to be overriden in the
    // constructor
    private String treeRootName = "All Courts";

    private JPopupMenu tsPopup = null;

    private JSplitPane jSplitPane1 = new JSplitPane();

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    protected JTree courtScheduleTree = null;

    private DefaultMutableTreeNode dailyListTreeRoot = null;

    private JPanel rightPane = new JPanel();

    private CourtPanel courtPanel = null;

    private HearingPanel hearingPanel = null;

    private DefaultTreeModel treeModel = null;

    private uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue selectedShv = null;

    private ApplicationCaseModel appCaseModel = new ApplicationCaseModel();

    private ArrayList availableActions = new ArrayList();

    private boolean userCanView = false;

    private boolean userCanUpdate = false;

    private boolean userCanUpdateOutOfCourt = false;

    private boolean reload = false;

    private int currentCourtRoom;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private Logger log = CSServices.getLogger(TodaysScheduleController.class);

    // Actions
    XAction view;

    XAction update;

    XAction caseProps;

    XAction movecase;

    public TodaysScheduleController(XhibitApplicationController parent) {
        try {
            this.parent = parent;
            reload = false;
            stepInitialise();

            jbInit();
        } catch (CSRecoverableException e) {
            XHIBITConstant.handleError(e);
        }
    }

    protected XhibitApplicationController getParentController() {
        return parent;
    }

    private XAction getViewAction() {
        if (view == null)
            view = XhibitActions.getAction(parent, XhibitActions.ViewCase);
        return view;
    }

    private XAction getUpdateAction() {
        if (update == null)
            update = XhibitActions.getAction(parent, XhibitActions.UpdateCase);
        return update;
    }

    private XAction getCasePropsAction() {
        if (caseProps == null)
            caseProps = XhibitActions.getAction(parent, XhibitActions.CaseProps);
        return caseProps;
    }

    private XAction getMoveCaseAction() {
        if (movecase == null)
            movecase = XhibitActions.getAction(parent, XhibitActions.MoveCase);
        return movecase;
    }

    private void setUpActions() {
        log.debug("[setUpActions]");
        view = getViewAction();
        update = getUpdateAction();
        caseProps = getCasePropsAction();
        movecase = getMoveCaseAction();

        // default all to disabled
        view.setEnabled(false);
        update.setEnabled(false);
        caseProps.setEnabled(false);
        movecase.setEnabled(false);

        if (view.hasReadAccess()) {
            availableActions.add(view);
            setUserCanView(true);
        }
        if (update.hasReadAccess()) {
            availableActions.add(update);
            setUserCanUpdate(true);
            if (FunctionList.hasAccess(FunctionList.ECourtLogOut))
                setUserCanUpdateOutOfCourt(true);
        }

        if (caseProps.hasReadAccess())
            availableActions.add(caseProps);
        if (movecase.hasReadAccess())
            availableActions.add(movecase);

        // Get the current court if available
        if (XhibitSingleton.getInstance().isUserInCourtroom()) {
            currentCourtRoom = XhibitSingleton.getInstance().getCourtRoomId().intValue();
        } else {
            currentCourtRoom = -1;
        }
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

    protected boolean hasUpdateOutOfCourtAccess() {
        return userCanUpdateOutOfCourt;
    }

    protected void setUserCanUpdateOutOfCourt(boolean hasAccess) {
        userCanUpdateOutOfCourt = hasAccess;
    }

    protected int getCurrentCourtRoom() {
        return currentCourtRoom;
    }

    private Object[] getAllCourtsArray() {
        return allCourtsArray;
    }

    private void jbInit() throws CSRecoverableException {
        this.setLayout(gridBagLayout1);
        jSplitPane1.setResizeWeight(0.2);
        this.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // creating a JScrollPane to put the CourtScheduleTree in. Then, this
        // JScrollPane is added to the splitPanel.
        JScrollPane jsp = new JScrollPane(getCourtScheduleTree(), JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        jSplitPane1.add(jsp, JSplitPane.LEFT);

        rightPane.setLayout(new GridBagLayout());

        rightPane.add(getHearingPanel(), new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));

        rightPane.add(getCourtPanel(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(4, 4, 4, 4), 0, 0));

        jSplitPane1.add(rightPane, JSplitPane.RIGHT);

    }

    private CourtPanel getCourtPanel() {
        if (courtPanel == null) {
            courtPanel = new CourtPanel(this);
            courtPanel.setVisible(true, treeRootName);
        }
        return courtPanel;
    }

    private HearingPanel getHearingPanel() {
        if (hearingPanel == null) {
            hearingPanel = new HearingPanel();
            hearingPanel.setVisible(false);
        }
        return hearingPanel;
    }

    private JTree getCourtScheduleTree() throws CSRecoverableException {
        if (courtScheduleTree == null) {
            courtScheduleTree = new JTree();
            treeModel = new DefaultTreeModel(getTreeRoot());

            courtScheduleTree.setModel(treeModel);
            courtScheduleTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    actionTreeSelectionChanged(e);
                }
            });
            JPopupMenu jp = getPopup();
            if (jp != null) {
                courtScheduleTree.add(jp);
                MouseListener popupListener = new PopupListener(jp);
                courtScheduleTree.addMouseListener(popupListener);
            }
            reload = false;
        }
        return courtScheduleTree;
    }

    private void actionTreeSelectionChanged(TreeSelectionEvent e) {

        uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue shv = null;

        if (courtScheduleTree.getLastSelectedPathComponent() instanceof DefaultMutableTreeNode) // Bal
        // 100603
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) courtScheduleTree.getLastSelectedPathComponent();

            if (node == null) {
                return;
            } else {
                Object nodeInfo = node.getUserObject();

                if (nodeInfo instanceof ScheduledHearingValueHelper) {
                    shv = ((ScheduledHearingValueHelper) nodeInfo).getModel();

                    getCourtPanel().setVisible(false);
                    getHearingPanel().setSHV(shv);
                    getHearingPanel().setVisible(true);
                } else if (nodeInfo instanceof AllCourtValueHelper) {
                    getCourtPanel().setVisible(false);
                    getHearingPanel().setVisible(false);

                    getCourtPanel().loadCourtTable(getAllCourtsArray(), CourtPanel.ALLCOURTS);

                    getCourtPanel().setVisible(true, nodeInfo.toString());
                } else if (nodeInfo instanceof CourtSiteValueHelper) {
                    getCourtPanel().setVisible(false);
                    getHearingPanel().setVisible(false);

                    Object[] o = sh.getCourtScheduleData(node).toArray();
                    getCourtPanel().loadCourtTable(o, CourtPanel.ALLCOURTS);

                    getCourtPanel().setVisible(true, nodeInfo.toString());
                } else if (nodeInfo instanceof CourtRoomValueHelper) {
                    getCourtPanel().setVisible(false);
                    getHearingPanel().setVisible(false);

                    Object[] o = sh.getCourtScheduleData(node).toArray();
                    getCourtPanel().loadCourtTable(o, CourtPanel.ONECOURT);

                    getCourtPanel().setVisible(true, nodeInfo.toString());

                } else if (nodeInfo instanceof String) {
                    // Defendant node, do nothing
                } else {
                    // Do nothing
                    uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("other node selected: "
                            + nodeInfo.getClass());
                }
            }
        }
        setShv(shv);
    }

    protected void setShv(uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue shv) {
        selectedShv = shv;
        if (shv == null) {
            appCaseModel = null;
        } else {
            appCaseModel = new ApplicationCaseModel();
            appCaseModel.setScheduledHearingValue(shv);
        }
        enableActions();
    }

    public uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue getShv() {
        return selectedShv;
    }

    public ApplicationCaseModel getAppCaseModel() {
        return appCaseModel;
    }

    protected void enableActions() {
        boolean actionState = false;
        boolean isFloating = true;

        if (selectedShv != null) {
            actionState = true;
            if (selectedShv.getIsFloating() != null) {
                isFloating = selectedShv.getIsFloating().booleanValue();
            }
        }

        Iterator iter = availableActions.iterator();
        while (iter.hasNext()) {
            XAction item = (XAction) iter.next();
            item.setModel(getAppCaseModel());

            if (item == view) {
                // view enabled if not floating
                if (!isFloating)
                    item.setEnabled(actionState);
                else
                    item.setEnabled(false);
            }
            if (item == update) {
                // update enabled if not floating and outofcourt access
                if (!isFloating) {
                    if (currentCourtRoom == selectedShv.getCourtRoomId().intValue() || userCanUpdateOutOfCourt) {
                        item.setEnabled(actionState);
                    } else {
                        item.setEnabled(false);
                    }
                } else {
                    item.setEnabled(false);
                }
                
                if (selectedShv != null) {
                    // XDMX7 disable the update option if case is migrated
                    int caseId = selectedShv.getCaseId();
                    
                    if (XhibitDelegateHelper.getMigrateCaseDelegate().isCaseMigrated(caseId)) {
                    	item.setEnabled(false);
                    }	
                }
            }
            if (item == movecase || item == caseProps) {
                // move case always enabled
                // case props always enabled
                item.setEnabled(actionState);
            }
        }
    }

    protected JPopupMenu getPopup() {
        JPopupMenu tempPopup;
        JMenuItem menuItem;
        try {
            if (tsPopup == null) {
                tempPopup = new JPopupMenu();
                if (getViewAction().hasReadAccess()) {
                    menuItem = new JMenuItem();
                    menuItem.setAction(getViewAction());
                    tempPopup.add(menuItem);
                }

                if (getUpdateAction().hasReadAccess()) {
                    menuItem = new JMenuItem();
                    menuItem.setAction(getUpdateAction());
                    tempPopup.add(menuItem);
                }

                if (tempPopup.getComponentCount() > 0)
                    tempPopup.addSeparator();

                if (getMoveCaseAction().hasReadAccess()) {
                    menuItem = new JMenuItem();
                    menuItem.setAction(getMoveCaseAction());
                    tempPopup.add(menuItem);
                }

                if (tempPopup.getComponentCount() > 0)
                    tempPopup.addSeparator();

                if (getCasePropsAction().hasReadAccess()) {
                    menuItem = new JMenuItem();
                    menuItem.setAction(getCasePropsAction());
                    tempPopup.add(menuItem);
                }

                tsPopup = tempPopup;
            }
        } catch (ActionNotFoundException ex) {
            XHIBITConstant.handleError(ex);
        }
        return tsPopup;
    }

    private DefaultMutableTreeNode getTreeRoot() throws CSRecoverableException {
        if (dailyListTreeRoot == null || reload == true) {
            dailyListTreeRoot = sh.getTreeRoot();
            reload = false;
        }
        return dailyListTreeRoot;
    }

    public void stepInitialise() throws CSRecoverableException {
        // Reset the status label by default
        parent.setStatusLabel("");

        sh = new ScheduleHelper();
        allCourtsArray = ((List) sh.getCourtScheduleData(getTreeRoot())).toArray();
        treeRootName = getTreeRoot().getUserObject().toString();
    }

    public void stepActivate() throws CSRecoverableException {
        setUpActions();

        getCourtPanel().loadCourtTable(getAllCourtsArray(), CourtPanel.ALLCOURTS);

        // enable bench warrant
        XhibitActions.getAction(parent, XhibitActions.AddHearing).setEnabled(true);
    }

    public void stepUpdateViewState() {
        // This may be used to activate/deactivate the popup
        enableActions();
    }

    public void stepValidate() throws uk.gov.courtservice.framework.services.validation.CSValidationException {
        // Nothing is editable, therefore no validation
    }

    public void stepDeactivate() {
        // Deactivate caseProperties, View and Update actions
        setShv(null);
        // disable bench warrant
        XhibitActions.getAction(parent, XhibitActions.AddHearing).setEnabled(false);
    }

    public void stepDeinitialise(boolean update) {
        // Nothing to clean up
    }

    public void reloadView() throws CSRecoverableException {
        reload = true;
        stepInitialise();

        treeModel = new DefaultTreeModel(getTreeRoot());
        // DefaultMutableTreeNode node =
        // sh.findCourtInTree(selectedShv.getCourtRoomId());

        getCourtScheduleTree().setModel(treeModel);
        getCourtScheduleTree().revalidate();
        // getCourtScheduleTree().expandPath(new TreePath(node));
        // getCourtScheduleTree().setSelectionPath(new TreePath(node));

        int row = 0;
        if (getCourtScheduleTree().getSelectionRows() != null) {
            row = getCourtScheduleTree().getSelectionRows()[0];
        }
        getCourtScheduleTree().expandRow(0);
        getCourtScheduleTree().expandRow(1);
        getCourtScheduleTree().setSelectionRow(row);

        stepUpdateViewState();
    }

    class PopupListener extends MouseAdapter {
        private JPopupMenu thisPopup = null;

        public PopupListener(JPopupMenu pMenu) {
            thisPopup = pMenu;
        }

        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                getNode(e);
                // only show the popup menu options if a case has been selected
                // from the tree
                if (selectedShv != null && thisPopup != null && validOptionSelected()) {
                    // Show the popup menu. Need to do this first to
                    // establish its size
                    thisPopup.show(e.getComponent(), e.getX(), e.getY());
                    // Now reposition it if it drops off the viewable screen
                    int xCoord = e.getComponent().getLocationOnScreen().getX() + e.getX() + thisPopup.getSize().width > screenSize.width ? e
                            .getX()
                            - thisPopup.getSize().width
                            : e.getX();
                    int yCoord = e.getComponent().getLocationOnScreen().getY() + e.getY() + thisPopup.getSize().height > screenSize.height ? e
                            .getY()
                            - thisPopup.getSize().height
                            : e.getY();
                    thisPopup.show(e.getComponent(), xCoord, yCoord);
                }
            }
        }

        private void getNode(MouseEvent e) {
            // get point where user right clicked.
            javax.swing.tree.TreePath path = courtScheduleTree.getPathForLocation(e.getX(), e.getY());
            courtScheduleTree.setSelectionPath(path);
        }

        /**
         * Method to check if a valid option (Case) has been selected from the
         * Court Tree
         * 
         * @return boolean to indicate valid option selected
         */
        private boolean validOptionSelected() {
            boolean isValidOption = false; // return value
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) courtScheduleTree.getLastSelectedPathComponent();

            if (node != null) {
                Object nodeInfo = node.getUserObject();
                if (nodeInfo instanceof ScheduledHearingValueHelper) {
                    isValidOption = true; // only set to true if Case has
                    // been selected
                }
            }
            return isValidOption;
        }

    }
}