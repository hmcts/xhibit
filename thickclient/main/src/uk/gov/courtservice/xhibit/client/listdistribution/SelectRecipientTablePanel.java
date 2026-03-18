package uk.gov.courtservice.xhibit.client.listdistribution;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.listdistribution.MaintainRecipientException;
import uk.gov.courtservice.xhibit.business.vos.entities.DocumentDistributionBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.WLLRecipientComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.util.DefaultPopup;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XMessageBox;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XSwingUtilities;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.listeners.DeleteListener;
import uk.gov.courtservice.xhibit.client.util.listeners.DoubleClickListener;
import uk.gov.courtservice.xhibit.client.util.listeners.ReturnListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TablePopupListener;
import uk.gov.courtservice.xhibit.client.util.listeners.TableRowListener;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: SelectRecipientTablePanel
 * </p>
 * <p>
 * Description: Builds the screen which displays the list of recipients in the
 * system who are not currently subscribed to the List in question. Provides the
 * ability to add new recipients to the system, subscribe a recipient to the
 * List in question or delete a recipient from the system.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @version $Id: SelectRecipientTablePanel.java,v 1.29 2005/01/26 08:11:53
 *          tz0d5m Exp $
 * @author G S Rajasekaran
 * @author Sarah Tong
 */
public class SelectRecipientTablePanel extends XPanel {
    private final SelectRecipientTableModel model;

    private JButton addNewRecipientBtn;

    private JButton editRecipientBtn;

    private JButton deleteRecipientBtn;

    private JScrollPane listScrollPane;

    private XTable recipientTable;

    private XAction addAction = null;

    private XAction deleteAction = null;

    private XAction editAction = null;

    /**
     * Sets the model and resources for this panel. Stores a reference to the
     * parents OK button. Calls the <code>stepInitialise()</code> method to
     * load the data required for this panel, calls <code>jbInit()</code> to
     * build the panel components and finally <code>setActivate()</code> to
     * perform any actions necessary before displaying the panel.
     * 
     * @param model
     *            Class to store the data required to build this panel.
     * @param btn
     *            The parent's OK button
     * @throws CSRecoverableException
     *             If there is a problem loading the data.
     */
    public SelectRecipientTablePanel(SelectRecipientTableModel model) throws CSRecoverableException {
        super(new GridBagLayout());
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    private void jbInit() {
        // The recipients in the system not already subscribed to the list
        this.add(getListScrollPane(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        // If it is not warned list recipients add the buttons
        // otherwise not much point as they are never enabled!
        if (!model.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            JPanel buttonPanel = new JPanel(new GridBagLayout());
            // Button to edit an existing recipient
            buttonPanel.add(getEditRecipientBtn(), new GridBagConstraints(0, 1, 1, 1, 0.2, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            // Button to add a new recipient to the system
            buttonPanel.add(getAddNewRecipientBtn(), new GridBagConstraints(1, 1, 1, 1, 0.2, 0.0,
                    GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            // Button to delete a recipient from the system
            buttonPanel.add(getDeleteRecipientBtn(), new GridBagConstraints(2, 1, 1, 1, 0.2, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
            this.add(buttonPanel, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, GridBagConstraints.CENTER,
                    GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        }
    }

    /**
     * Builds the component to display the recipients in the system not already
     * subscribed to the list
     * 
     * @return The JScrollPane component contining the relevant data
     */
    private JScrollPane getListScrollPane() {
        if (listScrollPane == null) {
            listScrollPane = new JScrollPane(getRecipientTable());
            listScrollPane.setMinimumSize(new Dimension(440, 330));
        }
        return listScrollPane;
    }

    /**
     * Builds a table containing the recipients in the system not already
     * subscribed to the list
     * 
     * @return The XTable component contining the relevant data
     */
    protected XTable getRecipientTable() {
        if (recipientTable == null) {
            recipientTable = XTableFactory.getInstance().createDefaultTable(model);
            recipientTable.setPreferredScrollableViewportSize(new Dimension(440, 330));
            recipientTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            recipientTable.getTableHeader().setReorderingAllowed(false);
            recipientTable.makeSortable();
            recipientTable.addKeyListener(new ReturnListener(getEditAction()));
            recipientTable.addMouseListener(new DoubleClickListener(getEditAction()));
            recipientTable.addKeyListener(new DeleteListener(getDeleteAction()));
            recipientTable.addMouseListener(new TablePopupListener(getPopup(), recipientTable));
            ListSelectionModel rowSM = recipientTable.getSelectionModel();
            rowSM.addListSelectionListener(new TableRowListener(this));
        }
        return recipientTable;
    }

    private JPopupMenu myPopup = null;

    /**
     * The right-click context menu
     * 
     * @return The JPopupMenu component with the relevant options
     */
    private JPopupMenu getPopup() {
        if (myPopup == null) {
            myPopup = new DefaultPopup(DefaultPopup.COPY);
            myPopup.addSeparator();
            myPopup.add(getAddAction());
            myPopup.add(getEditAction());
            myPopup.add(getDeleteAction());
        }
        return myPopup;
    }

    /**
     * @return The XAction to add a new recipient to the system
     */
    private XAction getAddAction() {
        if (addAction == null) {
            addAction = new AddRecipientAction(this, model);
        }
        return addAction;
    }

    /**
     * @return The XAction to delete a recipient from the system
     */
    private XAction getDeleteAction() {
        if (deleteAction == null) {
            deleteAction = new DeleteRecipientAction(this, model);
        }
        return deleteAction;
    }

    /**
     * @return The XAction to edit a recipient
     */
    private XAction getEditAction() {
        if (editAction == null) {
            editAction = new EditRecipientAction(this, model);
        }
        return editAction;
    }

    /**
     * @return The button which triggers the add receipent to system action
     */
    private JButton getAddNewRecipientBtn() {
        if (addNewRecipientBtn == null) {
            addNewRecipientBtn = new JButton();
            addNewRecipientBtn.setAction(getAddAction());
        }
        return addNewRecipientBtn;
    }

    /**
     * @return The button which triggers the edit recipient action
     */
    private JButton getEditRecipientBtn() {
        if (editRecipientBtn == null) {
            editRecipientBtn = new JButton();
            editRecipientBtn.setAction(getEditAction());
        }

        // can't edit recipients for WLL as these are ref data from CREST
        if (model.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            getEditAction().setEnabled(false);
        }

        return editRecipientBtn;
    }

    /**
     * @return The button which triggers the delete recipient action
     */
    private JButton getDeleteRecipientBtn() {
        if (deleteRecipientBtn == null) {
            deleteRecipientBtn = new JButton();
            deleteRecipientBtn.setAction(getDeleteAction());

            // can't add new recipients for WLL as these are ref data from
            // CREST
            if (model.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
                getDeleteAction().setEnabled(false);
            }
        }
        return deleteRecipientBtn;
    }

    /**
     * Find the data we need for this panel and stores in the model.
     * 
     * @throws CSRecoverableException
     *             If there is a problem finding the data.
     */
    public void stepInitialise() throws CSRecoverableException {
        Vector v;
        if (model.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            // this provides us with an alphabetically sorted list so no
            // need
            // to sort here
            v = (Vector) XhibitDelegateHelper.getMaintainRecipientDelegate().findWLLRecipientsNotOnList(
                    model.getCourtId());
        } else {
            v = (Vector) XhibitDelegateHelper.getMaintainRecipientDelegate().findRecipientsNotOnList(
                    model.getDocumentType(), model.getCourtId());
        }

        // if there are no suitable recipients set an empty Vector instead of
        // null
        // so that new recipients may be added to this list if the
        // AddRecipientAction is performed
        if (v == null)
            v = new Vector();
        model.setData(v);
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
     * Calls the <code>stepUpdateViewState()</code> method.
     */
    public void stepActivate() {
        stepUpdateViewState();
    }

    /**
     * Enables \ disables the buttons as required.
     */
    public void stepUpdateViewState() {
        if (model.getListType().equals(XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
            getAddAction().setEnabled(false);
            getDeleteAction().setEnabled(false);
            getEditAction().setEnabled(false);
        } else {
            int rowsSelected = recipientTable.getSelectedRows().length;
            if (rowsSelected <= 0) {
                getEditAction().setEnabled(false);
                getDeleteAction().setEnabled(false);
            } else {
                if (rowsSelected == 1) {
                    getEditAction().setEnabled(true);
                } else {
                    getEditAction().setEnabled(false);
                }
                getDeleteAction().setEnabled(true);
            }
        }
    }

    /**
     * Adds the selected item(s) to the chosen list
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        if (update) {
            // This method needs the physical row as getDataAt will
            // translate
            // to virtual row.
            int x = getRecipientTable().getSelectedRow();
            if (x < 0) {
                // No recipient selected
            } else {
                if (model.getListType().equals(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
                    processWllRecipients();
                } else {
                    processOtherRecipients();
                }
            }
        }
    }

    private void processOtherRecipients() throws MaintainRecipientException {
        // build a list of selected recipients
        Collection collect = new Vector();
        int[] selection = getRecipientTable().getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            // retrieve the basic value from the table model.
            RecipientBasicValue basicRecipient = (RecipientBasicValue) ((XHIBITTableModelInterface) getRecipientTable()
                    .getModel()).getDataAt(selection[i]);
            // prepare a new complex object to be populated.
            RecipientComplexValue complexRecipient = new RecipientComplexValue();
            // prepare a new document distribution value to be populated.
            DocumentDistributionBasicValue docDist = new DocumentDistributionBasicValue();

            populateComplexValue(basicRecipient, complexRecipient);

            // add a document distribution value with the default\preferred
            // values
            docDist.setCourtId(complexRecipient.getCourtID());
            docDist.setDocumentType(model.getDocumentType());
            docDist.setDistributionType(complexRecipient.getPrefDistributionType());
            docDist.setMimeType(complexRecipient.getPrefMimeType());
            docDist.setRecipientID(complexRecipient.getRecipientId());
            docDist.setUsePrefDistType("Y");

            // the addRecipientToList midtier service expects ONLY the
            // distribution we are adding in the RecipientComplexValue,
            // therfore
            // overwirte any existing distributions with this new one
            Vector newDistribution = new Vector();
            newDistribution.add(docDist);
            complexRecipient.setDocumentDistribution(newDistribution);

            collect.add(complexRecipient);
        }

        RecipientComplexValue[] recipientsToAdd = new RecipientComplexValue[collect.size()];
        collect.toArray(recipientsToAdd);
        XhibitDelegateHelper.getMaintainRecipientDelegate().addRecipientsToList(recipientsToAdd,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    private void populateComplexValue(RecipientBasicValue basicRecipient, RecipientComplexValue complexRecipient) {
        complexRecipient.setCourtID(basicRecipient.getCourtID());
        complexRecipient.setEmailAddress(basicRecipient.getEmailAddress());
        complexRecipient.setFaxNumber(basicRecipient.getFaxNumber());
        complexRecipient.setId(basicRecipient.getId());
        complexRecipient.setPrefDistributionType(basicRecipient.getPrefDistributionType());
        complexRecipient.setPrefMimeType(basicRecipient.getPrefMimeType());
        complexRecipient.setRecipientId(basicRecipient.getRecipientId());
        complexRecipient.setRecipientName(basicRecipient.getRecipientName());
        complexRecipient.setVersion(basicRecipient.getVersion());
    }

    private void processWllRecipients() throws CSRecoverableException {
        // build a list of selected recipients
        Collection collect = new Vector();
        int[] selection = getRecipientTable().getSelectedRows();
        for (int i = 0; i < selection.length; i++) {
            // retrieve the basic value from the table model.
            WLLRecipientBasicValue basicWLLRecipient = (WLLRecipientBasicValue) ((XHIBITTableModelInterface) getRecipientTable()
                    .getModel()).getDataAt(selection[i]);
            // prepare a new complex object to be populated.
            WLLRecipientComplexValue complexWLLRecipient = new WLLRecipientComplexValue();
            // prepare a new document distribution value to be populated.
            DocumentDistributionBasicValue docDistribution = new DocumentDistributionBasicValue();
            complexWLLRecipient.setDocumentDistribution(docDistribution);

            populateWllComplexValue(basicWLLRecipient, complexWLLRecipient);
            docDistribution.setCourtId(model.getCourtId());
            docDistribution.setDocumentType(model.getDocumentType());
            // Default MIME Type to HTML
            docDistribution.setMimeType("HTM");

            /**
             * @todo The strings below should either be statics or read from
             *       drop down list or props file
             */
            // if they have a primary key, this record may have been
            // retrived via the
            // stored procedure so would have 0 pk if none present, check
            // for 0 also
            if (complexWLLRecipient.getWllRecipientId() != null
                    && complexWLLRecipient.getWllRecipientId().intValue() != 0) {
                // check if they have an email or fax
                if (complexWLLRecipient.getSolicitorFirmEmail() != null
                        && complexWLLRecipient.getSolicitorFirmEmail().length() > 0) {
                    docDistribution.setDistributionType("EMAIL");
                } else if (complexWLLRecipient.getSolicitorFirmFax() != null
                        && complexWLLRecipient.getSolicitorFirmFax().length() > 0) {
                    // If FAX must default MIME TYPE to PDF
                    docDistribution.setDistributionType("FAX");
                    docDistribution.setMimeType("PDF");
                } else if (complexWLLRecipient.getSolicitorFirmAddress() != null
                        && complexWLLRecipient.getSolicitorFirmAddress().length() > 0) {
                    docDistribution.setDistributionType("POST");
                } else {
                    // No address or other distribution specified
                    // fire the edit screen so the user can enter email or
                    // fax
                    // and select an appropriate distribution method.
                    complexWLLRecipient = populateDistributionType(complexWLLRecipient, "COMPLEX");
                    docDistribution = complexWLLRecipient.getDocumentDistribution();
                }
            } else {
                // it is a solicitor firm that has never been added before
                if (complexWLLRecipient.getSolicitorFirmAddress() != null
                        && complexWLLRecipient.getSolicitorFirmAddress().length() > 0) {
                    // if they have an address, default to use it
                    docDistribution.setDistributionType("POST");
                } else {
                    // No address or other distribution specified
                    // fire the edit screen so the user can enter email or
                    // fax
                    // and select an appropriate distribution method.
                    complexWLLRecipient = populateDistributionType(complexWLLRecipient, "BASIC");
                    docDistribution = complexWLLRecipient.getDocumentDistribution();
                    // need to reapply document type as it gets cleared out
                    // by
                    // the manage letter recipient dialog
                    docDistribution.setDocumentType(model.getDocumentType());
                }
            }

            // usePrefDistType is a not null field, however there is no
            // concept of
            // preferred distribution type in relation to WLL's so set this
            // to 'N'
            docDistribution.setUsePrefDistType("N");
            complexWLLRecipient.setDocumentDistribution(docDistribution);

            // If a distribution type exists add to the collection
            if (docDistribution.getDistributionType() != null && docDistribution.getDistributionType().length() > 0) {
                collect.add(complexWLLRecipient);
            }
        }

        // Make delegate call
        WLLRecipientComplexValue[] recipientsToAdd = new WLLRecipientComplexValue[collect.size()];
        collect.toArray(recipientsToAdd);
        XhibitDelegateHelper.getMaintainRecipientDelegate().addWLLRecipients(recipientsToAdd,
                XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
    }

    private WLLRecipientComplexValue populateDistributionType(WLLRecipientComplexValue complexWLLRecipient,
            String callType) throws CSRecoverableException {
        ManageLetterRecipientModel mlrm = new ManageLetterRecipientModel();
        mlrm.setCallType(callType);
        mlrm.setTransactionType(ManageLetterRecipientModel.POPULATECOMPLEX);
        mlrm.setPanelTitle("Please Choose A Distribution Method");
        mlrm.setCourtId(model.getCourtId());
        mlrm.setListType(model.getListType());
        mlrm.setDocumentType(model.getDocumentType());
        mlrm.setComplexWLLRecipient(complexWLLRecipient);
        ManageLetterRecipientDialog mld = new ManageLetterRecipientDialog(XSwingUtilities
                .getUltimateFrameAncestor(this), mlrm);
        mld.show();
        if (mld.isOkClicked()) {
            // If the user clicked OK return the WLLRecipient from the model
            return mlrm.getComplexWLLRecipient();
        } else {
            // Otherwise just pass a reference back to the original.
            return complexWLLRecipient;
        }
    }

    private void populateWllComplexValue(WLLRecipientBasicValue basicWLLRecipient,
            WLLRecipientComplexValue complexWLLRecipient) {
        complexWLLRecipient.setCourtID(basicWLLRecipient.getCourtID());
        complexWLLRecipient.setCrestSolicitorFirmID(basicWLLRecipient.getCrestSolicitorFirmID());
        complexWLLRecipient.setId(basicWLLRecipient.getId());
        complexWLLRecipient.setSolicitorFirmAddress(basicWLLRecipient.getSolicitorFirmAddress());
        complexWLLRecipient.setSolicitorFirmEmail(basicWLLRecipient.getSolicitorFirmEmail());
        complexWLLRecipient.setSolicitorFirmFax(basicWLLRecipient.getSolicitorFirmFax());
        complexWLLRecipient.setSolicitorFirmName(basicWLLRecipient.getSolicitorFirmName());
        complexWLLRecipient.setVersion(basicWLLRecipient.getVersion());
        complexWLLRecipient.setWllRecipientId(basicWLLRecipient.getWllRecipientId());
    }

    /**
     * <p>
     * Title: AddRecipientAction
     * </p>
     * <p>
     * Description: The action performed when the addNewRecipientBtn is pressed.
     * Generates the dialog in which the new recipient details are added. Only
     * relevant for List recipients, Warned List Letter recipients are reference
     * data (RefSolicitorFirm) and can be added via CREST only.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author unascribed
     * @version 1.0
     * @editor Sarah Tong
     */
    class AddRecipientAction extends XAction {
        private SelectRecipientTablePanel myParent;

        private SelectRecipientTableModel myModel;

        /**
         * Stores a refrence to the parent and the model then and calls
         * <code>populateFromBundle</code> on the super class which gets the
         * name, short description and long description from the resource bundle
         * and sets them for this action. Also gets the mnemonic and icon if
         * they exist.
         * 
         * @param parent
         *            The parent of this action.
         * @param model
         *            The parent's model
         */
        public AddRecipientAction(SelectRecipientTablePanel parent, SelectRecipientTableModel model) {
            myParent = parent;
            myModel = model;
            populateFromBundle("createNewRecipientBtn");
        }

        /**
         * Generates a <code>ManageListRecipientDialog</code> in which the
         * user can provide the details to create a new recipient. When the
         * <code>ManageListRecipientDialog</code> is closed refreshes the
         * parent if a new recipient has been added.
         * 
         * @param ae
         *            The action event.
         * @throws CSRecoverableException
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            // create and populate ManageListRecipientModel
            ManageListRecipientModel mlrModel = new ManageListRecipientModel();
            mlrModel.setPanelTitle(XHIBITConstant.getResource(XhibitBundles.ManageLists, "addPanelTitleLabel"));
            mlrModel.setListType(myModel.getListType());
            mlrModel.setCallType("BASIC");
            mlrModel.setDocumentType(myModel.getDocumentType());
            mlrModel.setCourtId(myModel.getCourtId());

            // create ManageListRecipientDialog which collects the details
            // of the
            // new recipient
            ManageListRecipientDialog mlr = null;
            Window w = XSwingUtilities.getWindowAncestor(myParent);
            if (w != null && w instanceof Frame) {
                mlr = new ManageListRecipientDialog((Frame) w, mlrModel);
            } else if (w != null && w instanceof Dialog) {
                mlr = new ManageListRecipientDialog((Dialog) w, mlrModel);
            } else {
                mlr = new ManageListRecipientDialog(new XFrame(), mlrModel);
            }

            // display the dialog
            mlr.show();

            if (mlr.isOkClicked()) // add the recipient
            {
                myModel.addData(mlrModel.getBasicRecipient());
                myParent.getRecipientTable().tableChanged(new TableModelEvent(myParent.getRecipientTable().getModel()));
            }
        }
    }

    /**
     * <p>
     * Title: DeleteRecipientAction
     * </p>
     * <p>
     * Description: The action performed when the deleteRecipientBtn is pressed.
     * Deletes the selected recipient from the system.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author unascribed
     * @version 1.0
     * @editor Sarah Tong
     */
    class DeleteRecipientAction extends XAction {
        private SelectRecipientTablePanel myParent;

        private SelectRecipientTableModel myModel;

        /**
         * Stores a refrence to the parent and the model then and calls
         * <code>populateFromBundle</code> on the super class which gets the
         * name, short description and long description from the resource bundle
         * and sets them for this action. Also gets the mnemonic and icon if
         * they exist.
         * 
         * @param parent
         *            The parent of this action.
         * @param model
         *            The parent's model
         */
        public DeleteRecipientAction(SelectRecipientTablePanel parent, SelectRecipientTableModel model) {
            myParent = parent;
            myModel = model;
            populateFromBundle("deleteRecipientBtn");
        }

        /**
         * Prompts the user to confirm they want to delete the recipient from
         * the system. If the user confirms the recipient is deleted from the
         * system and the parent is refreshed.
         * 
         * @param ae
         *            The <code>ActionEvent</code>
         * @throws CSRecoverableException
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            if (myParent.getRecipientTable().getSelectedRow() < 0) {
                // No recipient selected
            } else {
                // can't delete WLL recipients as these are reference data in
                // CREST
                if (!myModel.getListType().equalsIgnoreCase(
                        XHIBITConstant.getResource(XhibitBundles.ManageLists, "warnedListLetter"))) {
                    Frame f = XSwingUtilities.getUltimateFrameAncestor(myParent);
                    if (f == null)
                        f = new XFrame();
                    boolean reply = XMessageBox.alert(f, XHIBITConstant.getResource(XhibitBundles.ManageLists,
                            "deletePanelTitleLabel"), true, XMessageBox.ICONQUESTION, XHIBITConstant.getResource(
                            XhibitBundles.ManageLists, "deleteQuestion1")
                            + "\n" + XHIBITConstant.getResource(XhibitBundles.ManageLists, "deleteQuestion2"),
                            XDialog.YESNO, XDialog.DEFAULTNO);

                    if (reply) {
                        // build a list of selected recipients
                        Collection collect = new Vector();
                        int[] selection = myParent.getRecipientTable().getSelectedRows();
                        for (int i = 0; i < selection.length; i++) {
                            RecipientBasicValue rbv = (RecipientBasicValue) ((XHIBITTableModelInterface) myParent
                                    .getRecipientTable().getModel()).getDataAt(selection[i]);
                            collect.add(rbv);
                        }
                        // Make delegate call
                        RecipientBasicValue[] recipientsToRemove = new RecipientBasicValue[collect.size()];
                        collect.toArray(recipientsToRemove);
                        XhibitDelegateHelper.getMaintainRecipientDelegate().removeRecipients(recipientsToRemove);

                        // remove from table model
                        java.util.Iterator iter = collect.iterator();
                        while (iter.hasNext()) {
                            model.deleteData(iter.next());
                        }

                        // fire table changed event to refresh table view
                        myParent.getRecipientTable().tableChanged(
                                new TableModelEvent(myParent.getRecipientTable().getModel()));
                        myParent.getRecipientTable().validate();
                        myParent.getRecipientTable().repaint();
                    }
                }
            }
        }
    }

    /**
     * <p>
     * Title: EditRecipientAction
     * </p>
     * <p>
     * Description: Edits a recipient, not in the context of a list - i.e. only
     * preferred distribution type can be modified. This action is only valid
     * for List recipients as Warned List Letter recipients are Solicitor Firms -
     * these are reference data form CREST and therefore cannot be modified as
     * there is no feedback mechanism.
     * </p>
     * <p>
     * Copyright: Copyright (c) 2003
     * </p>
     * <p>
     * Company: Electronic Data Systems
     * </p>
     * 
     * @author unascribed
     * @version 1.0
     * @editor Sarah Tong
     */
    class EditRecipientAction extends XAction {
        private SelectRecipientTablePanel myParent;

        private SelectRecipientTableModel myModel;

        /**
         * Stores a refrence to the parent and the model then and calls
         * <code>populateFromBundle</code> on the super class which gets the
         * name, short description and long description from the resource bundle
         * and sets them for this action. Also gets the mnemonic and icon if
         * they exist.
         * 
         * @param parent
         *            The parent of this action.
         * @param model
         *            The parent's model.
         */
        public EditRecipientAction(SelectRecipientTablePanel parent, SelectRecipientTableModel model) {
            myParent = parent;
            myModel = model;
            populateFromBundle("editRecipientBtn");
        }

        /**
         * Displays a <code>ManageListRecipientDialog</code> in whch the user
         * can edit the recipient details. When the dialog is closed refreshes
         * the parent if the recipient has been updated.
         * 
         * @param ae
         *            The <code>ActionEvent</code>
         * @throws CSRecoverableException
         */
        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            int selectedRow = myParent.getRecipientTable().getSelectedRow();
            if (selectedRow < 0) {
                // No recipient selected
            } else {
                Object currentRecipient = ((XHIBITTableModelInterface) myParent.getRecipientTable().getModel())
                        .getDataAt(selectedRow);

                // list recipients only, see class documentation for further
                // details
                ManageListRecipientModel mlrModel = new ManageListRecipientModel();
                mlrModel.setPanelTitle(XHIBITConstant.getResource(XhibitBundles.ManageLists, "editPanelTitleLabel"));
                mlrModel.setCourtId(model.getCourtId());
                mlrModel.setCallType("BASIC");
                mlrModel.setListType(myModel.getListType());
                mlrModel.setDocumentType(myModel.getDocumentType());
                mlrModel.setRecipientId(((RecipientBasicValue) currentRecipient).getId());

                ManageListRecipientDialog mlr = null;
                Window w = XSwingUtilities.getWindowAncestor(myParent);
                if (w != null && w instanceof Frame) {
                    mlr = new ManageListRecipientDialog((Frame) w, mlrModel);
                } else if (w != null && w instanceof Dialog) {
                    mlr = new ManageListRecipientDialog((Dialog) w, mlrModel);
                } else {
                    mlr = new ManageListRecipientDialog(new XFrame(), mlrModel);
                }

                if (mlr != null) {
                    mlr.show();
                    if (mlr.isOkClicked()) {
                        // Update the element with the updated version
                        /**
                         * @todo Try to do this in the model without accessing
                         *       the data as virtual row may be unavailble
                         */
                        myModel.updateElement(currentRecipient, mlrModel.getBasicRecipient());
                        // Notify the table that the row has changed so the view
                        // is refreshed.
                        // Note the table is told about the physical row and not
                        // the indexed row.
                        myParent.getRecipientTable().tableChanged(
                                new TableModelEvent(myParent.getRecipientTable().getModel(), selectedRow));
                    }
                }
            }
        }
    }
}
