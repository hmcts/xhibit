package uk.gov.courtservice.xhibit.client.publicnotice;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.publicnotice.PublicNoticeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellEditor;
import uk.gov.courtservice.xhibit.client.courtlog.CheckBoxTableCellRenderer;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: Panel displays all public notices available for display
 * </p>
 * <p>
 * Description: The Public notices can be selected or deselected using the check
 * boxes. The panel does not a have a model for the screen but there is a
 * PublicNoticeTableModel which contains data to populate the table on the
 * screem which contains all displayed dynamic data
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
 * 
 */
public class PublicNoticesPanel extends XPanel {
    /**
     * PublicNoticesTableModel publicNoticeTableModel
     */
    private PublicNoticesTableModel publicNoticeTableModel;

    /**
     * JTable publicNoticeTable
     */
    private JTable publicNoticeTable;

    /**
     * boolean[] selected
     */
    private boolean[] selected;

    /**
     * int MAX_NOTICES
     */
    private static final int MAX_NOTICES = 5;

    /**
     * <init>
     * 
     * @param model
     *            parameter for <init>
     * @throws CSRecoverableException -
     */
    public PublicNoticesPanel() throws CSRecoverableException {
        super();
        stepInitialise();
        jbInit();
        stepActivate();
    }

    /**
     * jbInit
     */
    private void jbInit() {
        this.setMaximumSize(new Dimension(360, 270));
        this.setMinimumSize(new Dimension(360, 270));
        this.setPreferredSize(new Dimension(360, 270));
        this.setLayout(new BorderLayout());
        this.add(new JScrollPane(getPublicNoticeTable()), BorderLayout.CENTER);
    }

    /**
     * getPublicNoticeTable
     * 
     * @return the returned JTable
     */
    private JTable getPublicNoticeTable() {
        publicNoticeTable = XTableFactory.getInstance().createMultiLineTable(publicNoticeTableModel);
        publicNoticeTable.getColumnModel().getColumn(PublicNoticesTableModel.IS_ACTIVE).setCellRenderer(
                new CheckBoxTableCellRenderer());
        publicNoticeTable.getColumnModel().getColumn(PublicNoticesTableModel.IS_ACTIVE).setCellEditor(
                new CheckBoxTableCellEditor(new JCheckBox()));
        publicNoticeTable.getColumnModel().getColumn(PublicNoticesTableModel.IS_ACTIVE).setMaxWidth(50);

        return publicNoticeTable;
    }

    /**
     * stepInitialise
     * 
     * @throws CSRecoverableException -
     */
    public void stepInitialise() throws CSRecoverableException {
        int courtRoomID = XhibitSingleton.getInstance().getCourtRoomId().intValue();

        // Create instance of table model passing in available public notices
        publicNoticeTableModel = new PublicNoticesTableModel(getDelegate().getAllPublicNoticesForCourtRoom(courtRoomID));

        // Intitialise array to hold selection status of each public notice
        selected = new boolean[publicNoticeTableModel.getPublicNoticeValues().length];

        // Using public notices values set selection status in array for each
        // notice
        for (int i = publicNoticeTableModel.getPublicNoticeValues().length - 1; i >= 0; i--) {
            selected[i] = publicNoticeTableModel.getPublicNoticeValues()[i].getIsActive();
        }
    }

    /**
     * stepDeactivate
     * 
     * @throws CSRecoverableException -
     */
    public void stepDeactivate() throws CSRecoverableException {
        // no implementation required...
    }

    /**
     * stepValidate
     * 
     * @throws CSValidationException -
     */
    public void stepValidate() throws CSValidationException {
        // Validation check to ensure that no more than MAX_NOTICES are
        // selected.
        int count = 0;
        for (int i = publicNoticeTableModel.getPublicNoticeValues().length - 1; i >= 0; i--) {
            if (publicNoticeTableModel.getPublicNoticeValues()[i].getIsActive()) {
                count++;
            }

            // setting dirty flag in same loop
            publicNoticeTableModel.getPublicNoticeValues()[i]
                    .setDirty(publicNoticeTableModel.getPublicNoticeValues()[i].getIsActive() != selected[i]);
        }

        if (count > MAX_NOTICES) {
            Object[] max = new Object[1];
            max[0] = new Integer(MAX_NOTICES);

            throw new CSValidationException("validation.maxallowed", max,
                    "validation.maxallowed: Maximum allowed selections");
        }
    }

    /**
     * stepUpdateViewState
     */
    public void stepUpdateViewState() {
        // Does nothing
    }

    /**
     * stepDeinitialise
     * 
     * @param update
     *            parameter for stepDeinitialise
     * @throws CSRecoverableException -
     */
    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        // Setting public notices on delegate
        if (update) {
            getDelegate().setAllPublicNoticesForCourtRoom(publicNoticeTableModel.getPublicNoticeValues(),
                    XhibitSingleton.getInstance().getCourtRoomId().intValue(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
        }
    }

    /**
     * stepActivate
     */
    public void stepActivate() {
        // Does nothing
    }

    /**
     * getDelegate
     * 
     * @return the returned PublicNoticeControllerBusinessDelegate
     */
    private PublicNoticeControllerBeanBusinessDelegate getDelegate() {
        return XhibitDelegateHelper.getPublicNoticeDelegate();
    }
}
