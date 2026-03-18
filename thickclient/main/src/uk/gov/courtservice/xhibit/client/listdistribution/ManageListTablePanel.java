package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions.AddRecipientAction;
import uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions.DeleteRecipientAction;
import uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions.EditListRecipientAction;
import uk.gov.courtservice.xhibit.client.actions.listdistribution.panelactions.UnsubscribeRecipientAction;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.util.listeners.DeleteListener;
import uk.gov.courtservice.xhibit.client.util.listeners.DoubleClickListener;
import uk.gov.courtservice.xhibit.client.util.listeners.PopupListener;
import uk.gov.courtservice.xhibit.client.util.listeners.ReturnListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TablePopupListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TableRowListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: ManageListTablePanel
 * </p>
 * <p>
 * Description: Builds the screen which displays the recipients subscribed to a
 * given List (Daily, Warned etc) and provides funtionality to add and removed
 * recipients from the list as well as editing recipient details.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * <p>
 * Author: G S Rajasekaran
 * </p>
 * 
 * @version $Id: ManageListTablePanel.java,v 1.33 2014/06/20 18:34:45 atwells Exp $
 *          R. Lakhani - Modified to follow code standards. The following was
 *          changed: - Removed sizes on panel and set on components - Max size
 *          never used - Actions changed to XAction - Title border changed to
 *          incorporate list title - Removed try catch around initialise - Moved
 *          Relevant methods into step methods. - Enabled buttons where
 *          appropriate
 * @editor R. Lakhani
 * @editor Sarah Tong
 */
public class ManageListTablePanel extends XPanel {
    private final ManageListTableModel model;

    private XTable listTable;

    private JScrollPane listScrollPane;

    private JButton addRecipientBtn = null;

    private JButton editListRecipientBtn = null;

    private JButton deleteRecipientBtn = null;

    private JButton unsubscribeBtn = null;

    /**
     * Sets the model and resources for this panel. Calls the
     * <code>stepInitialise()</code> method to load the data required for this
     * panel, calls <code>jbInit()</code> to build the panel components and
     * finally <code>setActivate()</code> to perform any actions necessary
     * before displaying the panel.
     * 
     * @param imodel
     *            Class to store the data required to build this panel.
     * @throws CSRecoverableException
     *             If there is a problem loading the data.
     */
    public ManageListTablePanel(ManageListTableModel imodel) throws CSRecoverableException {
        super(new GridBagLayout());
        this.model = imodel;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        // border round the panel
        this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(), model.getListType()
                + " " + ResourceBundleHelper.getResource(XhibitBundles.ManageLists, "listTableBorderTitle")));
        // the main table structure
        this.add(getListScrollPane(), new GridBagConstraints(0, 0, 1, 4, 1.0, 1.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        // add recipients button
        this.add(getAddRecipientBtn(), new GridBagConstraints(1, 0, 1, 1, 0.1, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // edit recipient button
        this.add(getEditListRecipientBtn(), new GridBagConstraints(1, 1, 1, 1, 0.1, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // unsubscribe from list button
        this.add(getUnsubscribeBtn(), new GridBagConstraints(1, 2, 1, 1, 0.1, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        // delete recipient button
        this.add(getDeleteRecipientBtn(), new GridBagConstraints(1, 3, 1, 1, 0.1, 0.0, GridBagConstraints.NORTHWEST,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.addMouseListener(new PopupListener(getPopup()));
    }

    private JScrollPane getListScrollPane() {
        if (listScrollPane == null) {
            listScrollPane = new JScrollPane(getListTable());
            listScrollPane.setMinimumSize(new Dimension(400, 400));
            listScrollPane.setPreferredSize(new Dimension(400, 400));
        }
        return listScrollPane;
    }

    public XTable getListTable() {
        if (listTable == null) {
            // listTable =
            // XTableFactory.getInstance().createDefaultTable(model);
            listTable = XTableFactory.getInstance().createMultiLineTable(model);
            listTable.makeSortable();
            listTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            // remove the return default functionality from the table..
            listTable.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).getParent().remove(
                    KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));

            listTable.getTableHeader().setReorderingAllowed(false);
            listTable.addKeyListener(new ReturnListener(getEditListAction()));
            listTable.addKeyListener(new DeleteListener(getDeleteAction()));
            listTable.addMouseListener(new TablePopupListener(getPopup(), listTable));
            listTable.addMouseListener(new DoubleClickListener(getEditListAction()));
            ListSelectionModel rowSM = listTable.getSelectionModel();
            rowSM.addListSelectionListener(new TableRowListener(this));
        }
        return listTable;
    }

    private JPopupMenu myPopup = null;

    private JPopupMenu getPopup() {
        if (myPopup == null) {
            myPopup = new DefaultPopup(DefaultPopup.COPY);
            myPopup.addSeparator();
            myPopup.add(getAddAction());
            myPopup.add(getEditListAction());
            myPopup.add(getUnsubscribeAction());
            myPopup.add(getDeleteAction());
        }
        return myPopup;
    }

    private XAction addAction = null;

    private XAction deleteAction = null;

    private XAction editListAction = null;

    private XAction unsubscribeAction = null;

    private XAction getAddAction() {
        if (addAction == null) {
            addAction = new AddRecipientAction(this, model);
        }
        return addAction;
    }

    private XAction getDeleteAction() {
        if (deleteAction == null) {
            deleteAction = new DeleteRecipientAction(this, model);
        }
        return deleteAction;
    }

    private XAction getEditListAction() {
        if (editListAction == null) {
            editListAction = new EditListRecipientAction(this, model);
        }
        return editListAction;
    }

    private XAction getUnsubscribeAction() {
        if (unsubscribeAction == null) {
            unsubscribeAction = new UnsubscribeRecipientAction(this, model);
        }
        return unsubscribeAction;
    }

    private JButton getAddRecipientBtn() {
        if (addRecipientBtn == null) {
            addRecipientBtn = new JButton();
            addRecipientBtn.setAction(getAddAction());
        }
        return addRecipientBtn;
    }

    private JButton getEditListRecipientBtn() {
        if (editListRecipientBtn == null) {
            editListRecipientBtn = new JButton();
            editListRecipientBtn.setAction(getEditListAction());
        }
        return editListRecipientBtn;
    }

    private JButton getDeleteRecipientBtn() {
        if (deleteRecipientBtn == null) {
            deleteRecipientBtn = new JButton();
            deleteRecipientBtn.setAction(getDeleteAction());
        }
        return deleteRecipientBtn;
    }

    private JButton getUnsubscribeBtn() {
        if (unsubscribeBtn == null) {
            unsubscribeBtn = new JButton();
            unsubscribeBtn.setAction(getUnsubscribeAction());
        }
        return unsubscribeBtn;
    }

    /**
     * Find the data we need for this panel and stores in the model.
     * 
     * @throws CSRecoverableException
     *             If there is a problem finding the data.
     */
    public void stepInitialise() throws CSRecoverableException {
        Vector v = null;
        if (model.getListType().equals(ResourceBundleHelper.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            v = (Vector) XhibitDelegateHelper.getMaintainRecipientDelegate().findWLLRecipients(model.getCourtId(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        } else {
            v = (Vector) XhibitDelegateHelper.getMaintainRecipientDelegate().findRecipientsByDocumentType(
                    model.getDocumentType(), model.getCourtId(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            if (v != null)
                Sorter.sort(v, new String[] { "recipientName" });
        }

        if (v == null)
            v = new Vector();

        if (listTable != null) {
            ((XHIBITTableModelInterface) getListTable().getModel()).setData(v);
        } else {
            model.setData(v);
        }
    }

    /**
     * Empty implementation of abstract class method.
     */
    public void stepDeactivate() {
    }

    /**
     * Empty implementation of abstract class method.
     */
    public void stepValidate() {
    }

    /**
     * Empty implementation of abstract class method.
     * 
     * @param update
     *            true if an action is to be performed on closing the window,
     *            false otherwise (e.g. if cancel pressed)
     */
    public void stepDeinitialise(boolean update) {
    }

    /**
     * Calls the <code>stepUpdateViewState()</code> method.
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * Enables \ disables the buttons as required.
     */
    public void stepUpdateViewState() {
        int x = getListTable().getSelectedRows().length; // getSelectedRow();
        getAddAction().setEnabled(true);
        if (x <= 0) {
            getEditListAction().setEnabled(false);
            getDeleteAction().setEnabled(false);
            getUnsubscribeAction().setEnabled(false);
        } else {
            // only enable edit if one row is selected
            if (x == 1)
                getEditListAction().setEnabled(true);
            else
                getEditListAction().setEnabled(false);

            getUnsubscribeAction().setEnabled(true);
            if (model.getListType().equals(
                    ResourceBundleHelper.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
                getDeleteAction().setEnabled(false);
            } else {
                getDeleteAction().setEnabled(true);
            }
        }
    }
}