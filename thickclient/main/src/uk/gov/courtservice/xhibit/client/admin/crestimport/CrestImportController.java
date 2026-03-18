package uk.gov.courtservice.xhibit.client.admin.crestimport;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.crestimport.CrestImportException;
import uk.gov.courtservice.xhibit.business.vos.services.crestimport.CrestImportStatus;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CrestImportControllerntroller
 * </p>
 * <p>
 * Description: This is the mediator for crest import
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
 */

public class CrestImportController extends XPanel {
    /**
     * Crest import status model
     */
    private CrestImportTableModel crestImportModel;

    /**
     * Default court id
     */
    private Integer defaultCourtId = XhibitSingleton.getInstance().getCourtId();

    /**
     * Controller window
     */
    private XhibitApplicationController xac;

    /**
     * Initializes the application controller
     * 
     * @param xac
     */
    public CrestImportController(XhibitApplicationController controller) throws CSRecoverableException {
        xac = controller;
        // Initialize
        stepInitialise();
    }

    /**
     * Called before the Window is initailzed
     * 
     * @throws CSRecoverableException
     */
    public void stepInitialise() throws CSRecoverableException {

        // Initialize the delegate
        initDelegate();

        // Initialize the model
        initModel();

        // Initialize the user interface
        initGUI();
    }

    /**
     * This method is called when the window is activated
     * 
     * @throws CSRecoverableException
     */
    public void stepActivate() throws CSRecoverableException {
    }

    /**
     * Called before view state is updated
     * 
     * @throws CSRecoverableException
     */
    public void stepUpdateViewState() throws CSRecoverableException {
    }

    /**
     * This method is called for validation
     * 
     * @throws CSValidationException
     * @throws CSRecoverableException
     */
    public void stepValidate() throws CSValidationException, CSRecoverableException {
    }

    /**
     * Called before de-activation
     * 
     * @throws CSRecoverableException
     */
    public void stepDeactivate() throws CSRecoverableException {
    }

    /**
     * Called before de-initialization
     * 
     * @param update
     * @throws CSRecoverableException
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
    }

    /**
     * Initializes the delegate
     */
    private void initDelegate() {
        XhibitSingleton single = XhibitSingleton.getInstance();
    }

    /**
     * Initializes the model
     */
    private void initModel() throws CrestImportException {
        crestImportModel = new CrestImportTableModel(XhibitDelegateHelper.getCrestImportDelegate()
                .getCrestImportStatus(defaultCourtId));
    }

    /**
     * Refreshes the model
     */
    private void refreshModel() throws CrestImportException {
        crestImportModel.setModel(XhibitDelegateHelper.getCrestImportDelegate().getCrestImportStatus(defaultCourtId));
    }

    /**
     * Initializes the delegate
     */
    private void initGUI() {
        // Arrange the components
        setLayout(new BorderLayout());

        add(getImportPane(), BorderLayout.CENTER);
        add(getBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the bottom panel
     * 
     * @return
     */
    private JPanel getBottomPanel() {
        // Create the bottom panel
        JPanel bottomPanel = new JPanel();
        JButton importButton = new JButton();
        importButton.setAction(new ImportButtonAction());
        bottomPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(importButton);
        return bottomPanel;
    }

    /**
     * Creates the table pane
     * 
     * @return
     */
    private JScrollPane getImportPane() {
        // Create the table
        XTable statusTable = XTableFactory.getInstance().createDefaultTable(crestImportModel);
        statusTable.initColumnSizes(new Object[] { new Boolean(false), "Reference Data Name XXXXXXXXXXx",
                "StatusXXXXXXX" }, (int) (xac.getSize().getWidth() * 0.7));
        JScrollPane pane = new JScrollPane(statusTable);
        pane.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED), XHIBITConstant.getResource(
                XhibitBundles.XhibitAdminResources, "ImportType")));
        return pane;
    }

    class ImportButtonAction extends XAction {
        public ImportButtonAction() {
            populateFromBundle("Import");
        }

        public void xActionPerformed(ActionEvent ae) {
            // Initiate transfer
            try {
                CrestImportStatus[] statusList = crestImportModel.getSelectedImportList();

                if (statusList.length == 0)
                    return;

                XhibitDelegateHelper.getCrestImportDelegate().initiateTransfer(statusList);
                refreshModel();

                JOptionPane.showMessageDialog(xac, XHIBITConstant.getResource(XhibitBundles.XhibitAdminResources,
                        "dataRequested"));

            } catch (CrestImportException e) {
                XHIBITConstant.handleError(e);
            }
        }
    }
}