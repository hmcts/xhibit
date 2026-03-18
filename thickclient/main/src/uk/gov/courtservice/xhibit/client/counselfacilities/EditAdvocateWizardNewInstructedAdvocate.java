package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.JPanel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.vos.entities.RefSystemCodeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefSystemCodeCriteria;
import uk.gov.courtservice.xhibit.client.comparator.RefSystemRefCodeOrderComparator;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;



/**
 * <p>
 * Title: Screen for choosing the legal representative
 * </p>
 * <p>
 * Description: Select the barrister.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Mark Hewitt
 * @version 1.0
 */

public class EditAdvocateWizardNewInstructedAdvocate extends XPanel {

    private static final long serialVersionUID = 1L;

    private TitledBorder tb;

    private EditAdvocateWizardModel model;
    
    private JLabel message;
    
    private String messageText;


    private ResourceBundle resources;

    private EditAdvocateWizardController controller;

    private JComboBox defenceCategoryCB;
    
    private DefaultComboBoxModel defenceCategoryModel;
    
    private Vector<String> defenceCategoryCrestCodes;
    
    private HashMap<String,String> defenceCategoryDisplayStrings;
    
    private JButton editPostBtn;

    private JScrollPane resultsScrollPane;

    private XTable resultsTable;

    // The changed set of CREST records
    private Vector<FindInstructedAdvocateTableRowModel> editedAdvocatesVec;
    
    // Those used in the table display
    private Vector<FindInstructedAdvocateTableRowModel> displayAdvocatesVec =
        new Vector<FindInstructedAdvocateTableRowModel>();

    private Integer legalRepId;
    
    
    public EditAdvocateWizardNewInstructedAdvocate(
            EditAdvocateWizardController controller,
            EditAdvocateWizardModel model,
            Vector<FindInstructedAdvocateTableRowModel> editedAdvocatesVec,
            Integer legalRepId)
    throws CSRecoverableException {
        super();
        this.controller = controller;
        this.model = model;
        this.editedAdvocatesVec = editedAdvocatesVec;
        this.legalRepId = legalRepId;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * Paints the controls on the screen.
     */
    private void jbInit() {
        this.setLayout(new GridBagLayout());
        
        int row = 0;
        
        this.add(getEditPostBtn(), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
        this.add(getResultsScrollPane(), new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
        JPanel panel = new JPanel();
        panel.add(getMessage());
        panel.add(getDefenceCategorySelector());
        
        this.add(panel, new GridBagConstraints(0, row++, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, new Insets(2, 2, 2, 2), 0, 0));
        
    }
    
    private DefaultComboBoxModel getComboBoxModel() {
        if (defenceCategoryModel == null) {
                defenceCategoryModel = 
                    new DefaultComboBoxModel(getDefenceCategoryCrestCodes().toArray());
        }
        return defenceCategoryModel;
    }
    
    private JComboBox getDefenceCategorySelector() {
        if (defenceCategoryCB == null) {
            defenceCategoryCB = new JComboBox(getComboBoxModel());
            ComboBoxRenderer renderer = new ComboBoxRenderer();
            defenceCategoryCB.setRenderer(renderer);
        }

        defenceCategoryCB.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    stepUpdateViewState();
                } catch (CSRecoverableException csre) {
                    XHIBITConstant.handleError(csre);
                }
            }
        });
        
        return defenceCategoryCB;
    }
    
    private JLabel getMessage() {
        if (message == null) {
            message = new JLabel(messageText);
        }
        return message;
    }
    
    private XTable getResultsTable() {
        if (resultsTable == null) {
            resultsTable = XTableFactory.getInstance().createDefaultTable(new FindInstructedAdvocateTableModel());
            resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            resultsTable.makeSortable();
            resultsTable.getColumnModel().getColumn(FindInstructedAdvocateTableModel.POST_NUMBER).setMaxWidth(50);
            
            ListSelectionModel rowSM = resultsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    try {
                        moveScreenToModel();
                        stepUpdateViewState();
                    } catch (CSRecoverableException csre) {
                        XHIBITConstant.handleError(csre);
                    }
                }
            });
        }

        return resultsTable;
    }

    private JScrollPane getResultsScrollPane() {
        if (resultsScrollPane == null) {
            resultsScrollPane = new JScrollPane();
            resultsScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            resultsScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            resultsScrollPane.setBorder(tb);
            resultsScrollPane.getViewport().add(getResultsTable(), null);
            resultsScrollPane.setPreferredSize(new Dimension(500, 312));
        }

        return resultsScrollPane;
    }

    private JButton getEditPostBtn() {
        if (editPostBtn == null) {
            editPostBtn = new JButton();
            editPostBtn.setToolTipText(XHIBITConstant.getResource(resources, "ttEditPost"));
            editPostBtn.setMnemonic(XHIBITConstant.getResource(resources, "mnmEditPost").charAt(0));
            editPostBtn.setEnabled(true);
            editPostBtn.setActionCommand("EDIT");
            editPostBtn.setText(XHIBITConstant.getResource(resources, "lblEditPost"));

            editPostBtn.addActionListener(new XAction() {
                private static final long serialVersionUID = 1L;
                
                public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
                    
                    synchronized (model) {
                        
                        final int row = getResultsTable().getSelectedRow();
                        if (row == -1) {
                            return;
                        }
                        
                        FindInstructedAdvocateTableRowModel trm =
                            displayAdvocatesVec.elementAt(row);
                        if (trm == null) {
                            return;
                        }
                        
                        getResultsTable().clearSelection();
                        EditPostModel editPostModel = 
                            new EditPostModel(model.getXac(), trm.getCrestPostNumber(), editedAdvocatesVec);
                        
                        java.awt.Frame frame = model.getXac();
                        EditPostDialog dialog = new EditPostDialog(frame, editPostModel);
                        
                        dialog.setVisible(true);
                        
                        if (dialog.isOkClicked()) {
                            mergeChanges(editPostModel);
                            displayAdvocatesVec = InstructedAdvocateHelper.populateDisplayAdvocates(editedAdvocatesVec);
                            CounselFacilitiesHelper.redisplayTable(getResultsTable(), displayAdvocatesVec);
                        }
                    }
                }
            });
        }

        return editPostBtn;
    }
    
    
    private void mergeChanges(EditPostModel editPostModel) {
        
        InstructedAdvocateHelper.mergeChanges(
                editedAdvocatesVec, 
                editPostModel.getInstructedAdvocates(),
                editPostModel.getPostNumber());
        
    }
   
    
    /**
     * Checks if all required fields have been populated.
     * 
     * @return true if all mandatory fields have been completed.
     */
    protected boolean isMandatoryFieldsCompleted() {
        return model.isBarristerTypeSet()
            && this.isDefenceCategorySet()
            && this.isEmptyPostChoosen();
    }

    /**
     * Saves the data on the screen to the FindRepresentationWizardModel.
     * 
     * @throws CSRecoverableException
     */
    private void moveScreenToModel() throws CSRecoverableException {
        XHIBITConstant.debug("moveScreenToModel");
        
        if (this.isMandatoryFieldsCompleted()) {
            final int x = getResultsTable().getSelectedRow();
            
            XHIBITTableModelInterface xstModel = (XHIBITTableModelInterface) getResultsTable().getModel();
            FindInstructedAdvocateTableRowModel item = 
                (FindInstructedAdvocateTableRowModel) xstModel.getDataAt(x);
            
            String defenceCategory = (String)getComboBoxModel().getSelectedItem();
           
            model.setNewInstructedAdvocateCrestPostNumber(item.getCrestPostNumber());
            model.setNewInstructedAdvocateDefenceCategory(defenceCategory);

        } else {
            model.setNewInstructedAdvocateCrestPostNumber(null);
            model.setNewInstructedAdvocateDefenceCategory(null);
        }
        
        model.setSubstitutedInstructedAdvocateTableRowModel(null);
    }

    public void clearSelection() throws CSRecoverableException {
        getResultsTable().clearSelection();
        moveScreenToModel();
        stepUpdateViewState();
    }
    
    /**
     * XPanel implementation of life cycle method, called when the screen is
     * first loaded.
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepInitialise");
        resources = XHIBITConstant.getResourceBundle(XhibitBundles.CounselFacilities);
        tb = new TitledBorder(
                BorderFactory.createEtchedBorder(SystemColor.controlHighlight, SystemColor.controlShadow),
                XHIBITConstant.getResource(resources, "lblInstructedBarristers"));

        messageText = XHIBITConstant.getResource(resources, "lblChooseTheAdvocateClass");
        
        getResultsTable().clearSelection();
        displayAdvocatesVec = InstructedAdvocateHelper.populateDisplayAdvocates(editedAdvocatesVec);
        CounselFacilitiesHelper.redisplayTable(getResultsTable(), displayAdvocatesVec);
    }
    
    @SuppressWarnings("unchecked")
    private HashMap<String,String> getDefenceCategoryDisplayStrings() {
        if (defenceCategoryDisplayStrings != null) {
            return defenceCategoryDisplayStrings;
        }
        
        defenceCategoryDisplayStrings = new HashMap<String,String>();
        defenceCategoryDisplayStrings.put("", "");

        try {
            RefSystemCodeCriteria criteria = new RefSystemCodeCriteria();
            criteria.setCodeType(RefSystemCodeCriteria.CodeType.ADVOCATE_TYPE);
            criteria.setCourtId(XhibitSingleton.getInstance().getCourtId().toString());
            ArrayList<RefSystemCodeBasicValue> defCatCodes = (ArrayList<RefSystemCodeBasicValue>) XhibitDelegateHelper.getBizRefDelegate().findSystemCodes(criteria);
            
            Collections.sort(defCatCodes, new RefSystemRefCodeOrderComparator());
            for (int i = 0; i < defCatCodes.size(); i++) {
                String crestCode = ((RefSystemCodeBasicValue) (defCatCodes.get(i))).getCode();
                defenceCategoryDisplayStrings.put(crestCode,
                        ((RefSystemCodeBasicValue) (defCatCodes.get(i))).getDecode());
            }
        } catch (Exception e) {
            XHIBITConstant.handleError(e);
        }
        
        return defenceCategoryDisplayStrings;
    }
    
    
    private Vector<String> getDefenceCategoryCrestCodes() {
        if (defenceCategoryCrestCodes != null) {
            return defenceCategoryCrestCodes;
        }
        
        defenceCategoryCrestCodes = new Vector<String>();
        defenceCategoryCrestCodes.add("");
        
        for (String crestCode : getDefenceCategoryDisplayStrings().keySet()) {
            defenceCategoryCrestCodes.add(crestCode);
        }
        
        return defenceCategoryCrestCodes;
    }
    
    
    /**
     * XPanel implementation of life cycle method, called when (or each time)
     * the screen is left.
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepDeactivate");
    }

    /**
     * XPanel implementation of life cycle method, called when leaving this
     * screen to validate the data.
     * 
     * @throws CSRecoverableException
     * @throws CSValidationException
     *             if an invalid date is entered.
     */
    public void stepValidate() throws CSRecoverableException, CSValidationException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepValidate");
        // Nothing to validate
        moveScreenToModel();
    }

    /**
     * XPanel implementation of life cycle method, called when the state of a
     * widget changes.
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepUpdateViewState");

        getEditPostBtn().setEnabled(getResultsTable().getSelectedRow() != -1);
        
        controller.stepUpdateViewState();
    }
    
    private boolean isDefenceCategorySet() {
        String defenceCategory = (String)getComboBoxModel().getSelectedItem();
        return defenceCategory != null && !defenceCategory.equals("");
    }
    
    private boolean isEmptyPostChoosen() {
        final int row = getResultsTable().getSelectedRow();
        
        if (row == -1) {
            return false;
        }
        
        FindInstructedAdvocateTableRowModel trm =
            displayAdvocatesVec.elementAt(row);
        
        return trm.getLegalRepId() == null // empty post choosen
            || 
            // or the user has edited a post and added the
            // new instructed advocate themselves.
            trm.getLegalRepId().equals(legalRepId);
    }

    /**
     * XPanel implementation of life cycle method, called when the screen is
     * closed. Note - This is only called if the user has been to the other
     * screens in the wizard, populated the necessary data and then come back to
     * this screen.
     * 
     * @param update
     *            true if the data on the screen is being saved.
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepDeinitialise");

        if (update) {
            controller.stepDeinitialise();
        }
    }

    /**
     * XPanel implementation of life cycle method, called each time the screen
     * is displayed.
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
        XHIBITConstant.debug("[EditAdvocateWizardNewInstructedAdvocate] stepActivate");
        stepUpdateViewState();
    }
    
    // Renderer for combobox
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {

        private static final long serialVersionUID = 1L;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            String crestCode = (String) value;
            setHorizontalAlignment(LEFT);

            if (list.getSelectedValue() != null) {
                setText(getDefenceCategoryDisplayStrings().get(crestCode));
            }
            return this;
        }
    }
}
