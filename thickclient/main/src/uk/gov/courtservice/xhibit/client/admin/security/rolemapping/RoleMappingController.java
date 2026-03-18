package uk.gov.courtservice.xhibit.client.admin.security.rolemapping;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.SystemColor;
import java.util.TreeMap;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.SaveFunction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.GroupHierarchy;
import uk.gov.courtservice.xhibit.common.rolemapping.vos.RoleMappingCompositeValue;
import uk.gov.courtservice.xhibit.rolemapping.services.RoleMappingControllerBeanBusinessDelegate;

/**
 * <p>
 * Title: RoleMappingController
 * </p>
 * <p>
 * Description: This is the mediator for role mapping controllers
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @version $Revision: 1.17 $
 */
public class RoleMappingController extends XPanel implements SaveFunction, TreeSelectionListener, TableModelListener {
    private static final Logger logger = Logger.getLogger(RoleMappingController.class);

    private XhbSecurityGroupRoleBasicValue[] groupRoles;

    private RoleMappingCompositeValue rmcv = null;

    // Underlying model
    private GroupRoleMapping mapping;

    // Model for assigned roles
    private AssignedRolesModel assModel;

    // Model for derived roles
    private final DerivedRolesModel derModel = new DerivedRolesModel(new GroupRoleValue[0]);

    // Model for derived roles
    private GroupTreeModel treeModel;

    // Group tree
    private JTree groupTree;

    public RoleMappingController() throws CSRecoverableException {
        stepInitialise();
    }

    /**
     * Called before the Window is initailzed
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        final RoleMappingCompositeValue cv = XhibitDelegateHelper.getRoleMapperDelegate()
                .getRoleMappingCompositeValue();
        mapping = new GroupRoleMapping(cv.getGroupHierarchy(), cv.getSecurityRoles(), cv.getSecurityGroupRoles());
        assModel = new AssignedRolesModel(mapping);

        // Initialize user interface
        guiInit();
    }

    /**
     * This method is called when the window is activated
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() {
    }

    /**
     * Called before view state is updated
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() {
    }

    /**
     * This method is called for validation
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() {
    }

    /**
     * Called before de-activation
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() {
    }

    /**
     * Called before de-initialization
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) {
    }

    /**
     * Save functionality
     */
    public void save() throws CSRecoverableException {
        try {
            savePreSynchAction();
            saveSynchAction();
            savePostSynchAction();
        } catch (CSRecoverableException e) {
            throw e;
        } catch (CSUnrecoverableException e) {
            throw e;
        } catch (Exception ex) {
            throw new CSUnrecoverableException(ex);
        }
    }

    public void savePreSynchAction() throws Exception {
        logger.debug("savePreSynchAction - BEGIN");

        groupRoles = mapping.getChangedGroupRoles();

        if (logger.isDebugEnabled()) {
            for (int i = 0; i < groupRoles.length; i++) {
                logger.debug("i = " + i + " " + groupRoles[i].getGroupName() + " " + groupRoles[i].getRoleName()
                        + " enabled = " + groupRoles[i].getIsEnabled());
            }
        }
        logger.debug("savePreSynchAction - END");
    }

    public void saveSynchAction() throws Exception {
        final RoleMappingControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getRoleMapperDelegate();

        delegate.setGroupRoles(groupRoles);
        rmcv = delegate.getRoleMappingCompositeValue();
    }

    public void savePostSynchAction() throws Exception {
        if (rmcv != null) {
            mapping = new GroupRoleMapping(rmcv.getGroupHierarchy(), rmcv.getSecurityRoles(), rmcv
                    .getSecurityGroupRoles());

            GroupHierarchy hierarchy = mapping.getGroupHierarchy();
            TreeMap treeData = hierarchy.getGroupTree();
            String rootGroup = hierarchy.getRootGroup();

            treeModel.refreshData(treeData, rootGroup);
            assModel.refreshData(rootGroup, mapping, new GroupRoleValue[] {});
            derModel.refreshData(new GroupRoleValue[] {});

            revalidate();

            // Select the root group
            groupTree.setSelectionRow(0);

            setModified(false);
        }
    }

    /**
     * Tree selection changed
     * 
     * @param e
     */
    public void valueChanged(TreeSelectionEvent e) {
        String group = (String) e.getNewLeadSelectionPath().getLastPathComponent();
        GroupRoleValue[] derivedRoles = mapping.getDerivedRoles(group);
        assModel.refreshData(group, mapping, derivedRoles);
        derModel.refreshData(derivedRoles);
    }

    /**
     * Table model changed
     * 
     * @param e
     */
    public void tableChanged(TableModelEvent e) {
        // Set the modified flag only if the checkbox is checked
        if (e.getColumn() == 1) {
            logger.debug("Modified: " + getModified());
            setModified(true);
        }
    }

    /**
     * Assembles the user interface
     */
    private void guiInit() {
        // Get the group hierarchy and root group
        GroupHierarchy hierarchy = mapping.getGroupHierarchy();
        TreeMap treeData = hierarchy.getGroupTree();
        String rootGroup = hierarchy.getRootGroup();

        // Create the group hierarchy tree
        treeModel = new GroupTreeModel(treeData, rootGroup);
        groupTree = new JTree(treeModel);
        groupTree.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Role Hierarchy"));
        groupTree.setShowsRootHandles(true);
        groupTree.addTreeSelectionListener(this);
        groupTree.setCellRenderer(new GroupRenderer());
        JScrollPane groupPane = new JScrollPane(groupTree);

        // Create the assigned functionality table
        JTable assTable = new JTable(assModel);
        assModel.addTableModelListener(this);
        assTable.getTableHeader().setReorderingAllowed(false);
        assTable.getTableHeader().setVisible(false);

        StripedTableCellRenderer.installInTable(assTable, SystemColor.activeCaption, SystemColor.activeCaptionText,
                SystemColor.inactiveCaption,
                // SystemColor.control,
                SystemColor.inactiveCaptionText);

        JScrollPane assPane = new JScrollPane(assTable);
        assPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Assigned Fuctionality"));
        assPane.setPreferredSize(new Dimension(200, 100));

        // Create the derived functionality table
        JTable derTable = new JTable(derModel);
        derTable.getTableHeader().setReorderingAllowed(false);
        derTable.getTableHeader().setVisible(false);
        JScrollPane derPane = new JScrollPane(derTable);
        derPane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), "Derived Fuctionality"));

        final JSplitPane rolePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, assPane, derPane);

        final JSplitPane mainPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, groupPane, rolePane);
        setLayout(new BorderLayout());
        add(mainPane, BorderLayout.CENTER);

        // Select the root group
        groupTree.setSelectionRow(0);

        // Set the properties for the splitpanes
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainPane.setDividerLocation(0.30);
                mainPane.setOneTouchExpandable(true);
                mainPane.setDividerSize(10);
                rolePane.setDividerLocation(0.50);
                rolePane.setOneTouchExpandable(true);
                rolePane.setDividerSize(10);
            }
        });
    }
}
