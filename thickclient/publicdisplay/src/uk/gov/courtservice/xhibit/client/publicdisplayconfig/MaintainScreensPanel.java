package uk.gov.courtservice.xhibit.client.publicdisplayconfig;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.business.services.publicdisplay.PDConfigurationControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.editscreen.EditScreenDialog;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.DisplayTreeModel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.DisplayAdapter;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.listeners.ScreenTreeListener;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.listeners.PopupListener;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.CourtSitePDComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayConfiguration;

/**
 * <p>
 * Title: XHIBIT 2 - Public Display
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: MaintainScreensPanel.java,v 1.9 2006/06/05 12:32:07 bzjrnl Exp $
 */

public class MaintainScreensPanel extends XPanel {
    public static final String NOTIFY_SCREEN_CHANGE = "NOTIFY_SCREEN_CHANGE";

    private static final String BORDER_TITLE = "pd.title.maintainscreen";

    private static final String LABEL_SCREEN = "pd.title.screen";

    private static final String LABEL_SCREENINFO = "pd.title.screeninformation";

    private static final Dimension treeDim = new Dimension(130, 175);

    private CourtSitePDComplexValue[] _data;

    private XAction initScreenAction;

    private XAction initAllScreensAction;

    private XAction editAction;

    public MaintainScreensPanel() throws CSRecoverableException {
        super(new GridBagLayout());
        stepInitialise();
        init();
        this.setBorder(PublicDisplayUtils.createBorder(PublicDisplayUtils.getResource(BORDER_TITLE)));
        this.addPropertyChangeListener(NOTIFY_SCREEN_CHANGE, new ScreenChangeListener(this));
    }

    private void init() throws CSRecoverableException {
        this.add(new JLabel(PublicDisplayUtils.getResource(LABEL_SCREEN)), new GridBagConstraints(0, 0, 3, 1, 1.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(PublicDisplayUtils.getDefaultScrollPane(getScreenTree(), treeDim), new GridBagConstraints(0, 1, 3, 1,
                1.0, 0.5, GridBagConstraints.WEST, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(new JLabel(PublicDisplayUtils.getResource(LABEL_SCREENINFO)), new GridBagConstraints(0, 2, 3, 1, 1.0,
                0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(getScreenInfo(), new GridBagConstraints(0, 3, 3, 1, 1.0, 0.3, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        // Buttons
        this.add(JButtonFactory.getButton(getInitScreenAction()), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(JButtonFactory.getButton(getInitAllScreensAction()), new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(new JLabel(), new GridBagConstraints(1, 4, 1, 2, 1.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(JButtonFactory.getButton(getEditAction()), new GridBagConstraints(2, 4, 1, 2, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

    }

    private ScreenInformationPanel screenInfo = null;

    private JTree screenTree = null;

    // Create screen objects
    private ScreenInformationPanel getScreenInfo() {
        if (screenInfo == null) {
            screenInfo = new ScreenInformationPanel();
            // screenInfo.setEditable(false);
        }
        return screenInfo;
    }

    private JTree getScreenTree() throws CSRecoverableException {
        if (screenTree == null) {
            DisplayTreeModel model = new DisplayTreeModel(getData());
            screenTree = new JTree(model.getTreeModel());
            screenTree.addTreeSelectionListener(new ScreenTreeListener(this, getScreenInfo()));
            screenTree.addMouseListener(new PopupListener(getPopup()));
        }
        return screenTree;
    }

    // Get actions
    private XAction getEditAction() {
        if (editAction == null) {
            editAction = new EditAction(this);
        }
        return editAction;
    }

    private XAction getInitScreenAction() {
        if (initScreenAction == null) {
            initScreenAction = new InitScreenAction(this);
        }
        return initScreenAction;
    }

    private XAction getInitAllScreensAction() {
        if (initAllScreensAction == null) {
            initAllScreensAction = new InitAllScreensAction(this);
        }
        return initAllScreensAction;
    }

    private JPopupMenu getPopup() {
        JPopupMenu pop = new DefaultPopup(DefaultPopup.NONE);
        pop.add(getEditAction());
        return pop;
    }

    // Utility Methods
    /**
     * Used to notify listeners that the rotation set list has been modified in
     * some way.
     * 
     * @param updateType
     *            static NOTIFY_SCREEN_CHANGE
     * @param oldObj
     *            The object prior to update - can be null
     * @param newObj
     *            The object after update - can be null
     */
    protected void notifyUpdate(String updateType, Object oldObj, Object newObj) {
        firePropertyChange(updateType, oldObj, newObj);
    }

    /**
     * @return The array of RotationSetComplexValues retrieved from the
     *         uk.gov.courtservice.xhibit.business.services.publicdisplay.
     */
    private CourtSitePDComplexValue[] getData() {
        if (_data == null)
            return new CourtSitePDComplexValue[] {};
        else
            return _data;
    }

    // Lifecycle methods
    public void stepInitialise() throws CSRecoverableException {
        PDConfigurationControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getPDConfigurationDelegate();
        _data = delegate.getDisplaysForCourt(PublicDisplayUtils.getCourtId());
    }

    public void stepActivate() {
        stepUpdateViewState();
    }

    public void stepUpdateViewState() {
        boolean isEdit = false;
        if (screenTree.getSelectionCount() > 0) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) screenTree.getLastSelectedPathComponent();
            if (node.getUserObject() instanceof DisplayAdapter)
                isEdit = true;
        }
        getEditAction().setEnabled(isEdit);
        getInitScreenAction().setEnabled(isEdit);
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
     * Returns the display selected in the tree
     * 
     * @return XhbDisplayBasicValue. Null if display not selected
     */
    public XhbDisplayBasicValue getSelectedDisplay() {
        try {
            DisplayAdapter da = (DisplayAdapter) ((DefaultMutableTreeNode) getScreenTree()
                    .getLastSelectedPathComponent()).getUserObject();
            return da.getBasicValue();
        } catch (CSRecoverableException ex) {
            return null;
        }
    }

    // Actions

    class EditAction extends XAction {
        private static final String LABEL = "btnEdit";

        private MaintainScreensPanel _parent;

        public EditAction(MaintainScreensPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        public void xActionPerformed(ActionEvent ae) {
            if (_parent.getSelectedDisplay() != null) {
                EditScreenDialog dialog = new EditScreenDialog((Dialog) SwingUtilities.getWindowAncestor(_parent),
                        _parent.getSelectedDisplay().getPrimaryKey());
                dialog.setVisible(true);
                if (dialog.isOkClicked()) {
                    DisplayConfiguration originalConfig = _parent.getScreenInfo().getCurrentDisplayConfiguration();
                    DisplayConfiguration newConfig = dialog.getDisplayConfiguration();
                    _parent.notifyUpdate(_parent.NOTIFY_SCREEN_CHANGE, originalConfig, newConfig);
                }
            }
        }
    }

    class InitScreenAction extends XAction {
        private static final String LABEL = "pdInitScreen";

        private MaintainScreensPanel _parent;

        public InitScreenAction(MaintainScreensPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        public void xActionPerformed(ActionEvent ae) {
            XhbDisplayBasicValue display = _parent.getSelectedDisplay();

            boolean rc = XMessageBox.alert((Dialog) SwingUtilities.getWindowAncestor(_parent), PublicDisplayUtils
                    .getResource("pd.initialiseDisplay.title"), true, XMessageBox.ICONQUESTION, PublicDisplayUtils
                    .getResource("pd.initialiseDisplay.message")
                    + PublicDisplayUtils.getResource("pd.displaydescription." + display.getDescriptionCode()),
                    XMessageBox.YESNO, XMessageBox.DEFAULTNO);
            if (rc) {
                XhibitDelegateHelper.getPDConfigurationDelegate().initialiseDisplay(PublicDisplayUtils.getCourtId(),
                        display.getPrimaryKey(),XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
        }
    }

    class InitAllScreensAction extends XAction {
        private static final String LABEL = "pdInitAllScreens";

        private static final String TITLE = "pd.initialiseCourt.title";

        private static final String MESSAGE = "pd.initialiseCourt.message";

        private MaintainScreensPanel _parent;

        public InitAllScreensAction(MaintainScreensPanel parent) {
            populateFromBundle(LABEL);
            _parent = parent;
        }

        public void xActionPerformed(ActionEvent ae) {
            boolean rc = XMessageBox.alert((Dialog) SwingUtilities.getWindowAncestor(_parent), PublicDisplayUtils
                    .getResource(TITLE), true, XMessageBox.ICONQUESTION, PublicDisplayUtils.getResource(MESSAGE),
                    XMessageBox.YESNO, XMessageBox.DEFAULTNO);
            if (rc) {
                XhibitDelegateHelper.getPDConfigurationDelegate().initialiseCourt(PublicDisplayUtils.getCourtId(),
                		XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            }
        }
    }

    class ScreenChangeListener implements PropertyChangeListener {
        MaintainScreensPanel _parent;

        public ScreenChangeListener(MaintainScreensPanel parent) {
            _parent = parent;
        }

        public void propertyChange(PropertyChangeEvent evt) {
            if (_parent.getSelectedDisplay() != null) {
                // Refresh display
                _parent.getScreenInfo().setNewDisplay(_parent.getSelectedDisplay().getPrimaryKey());
            }
        }
    }
}