package uk.gov.courtservice.xhibit.client.courtlog;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import javax.ejb.FinderException;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.services.validation.CSValidationException;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.hearingschedule.HearingScheduleException;
import uk.gov.courtservice.xhibit.business.services.shjustice.SHJusticeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.HearingBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.SHJusticeBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.hearingheader.HearingHeaderValue;
import uk.gov.courtservice.xhibit.business.vos.services.hearingschedule.linkhearing.CaseSchedHearingValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.HearingHeaderValueHelper;
import uk.gov.courtservice.xhibit.client.util.UnknownCaseTypeException;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.commonfunctions.PrintFunction;
import uk.gov.courtservice.xhibit.client.util.helpers.CaseTypeHelper;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;
import uk.gov.courtservice.xhibit.client.util.table.ScrollTableRowToView;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogViewValue;

/**
 * <p>
 * Title: XHIBIT
 * </p>
 * <p>
 * Description: Court services
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Stephen Tully
 */
public class CourtLogController extends XPanel implements PrintFunction {
    private static final Logger LOG = CSServices.getLogger(CourtLogController.class);

    private static final int PIXELS_PER_CHAR = 7;

    private static final int PREFERRED_CASENO_COL_WIDTH = 70;

    private static final int PREFERRED_TIME_COL_WIDTH = 35;

    private static final int SCREEN_WIDTH = 800;

    private final CourtLogControllerHelper clch = new CourtLogControllerHelper();

    final CourtLogControllerModel model;

    private XTable courtLogHeaderTable;

    private JScrollPane eventsScrollPane = new JScrollPane();

    private XTable eventsTable;

    int lastSelectedRow = 0;

    private JPopupMenu eventsTablePopup;

    private JPopupMenu courtLogHeaderTablePopup;

    private XAction clEditAction = null;

    private XAction clDeleteAction = null;

    private HearingHeaderValueHelper myHelper = null;

    private int maxDefendantNameLength;

    private HearingHeaderValue hearingHeaderValue;

    public CourtLogController(CourtLogControllerModel model) throws CSRecoverableException {
        this.model = model;

        stepInitialise();
        jbInit();
        stepActivate();
    }

    public void stepInitialise() throws CSRecoverableException {
        LOG.debug("stepInitialise - Begin");

        loadCourtLogHeaderTableModel();

        loadCourtLogEventsTableModel();

        // Save list of editable events in the model
        model.setEditableEvents(model.getAcm().getXhibitApplicationController().getCourtLogActionMap().keys());

        setEditDeleteEnable(false);
    }

    public boolean isLinked() {
        // If there are CaseSchedHearingValues in the headerValue,
        // this means the case is linked to other cases.
        return (hearingHeaderValue.getCaseSchedHearingValues() != null && hearingHeaderValue
                .getCaseSchedHearingValues().length > 0);
    }

    public void loadCourtLogHeaderTableModel() {
        Object clhArray[] = null;

        try {
            hearingHeaderValue = XhibitDelegateHelper.getHearingDelegate().getHearingHeader(
                    model.getAcm().getScheduledHearingId(),
                    XhibitSingleton.getInstance().getUserSession().getSessionProperty(UserTerminalProperties.DISPLAY_NAME));
            
            Integer hearingId = getHearingScheduleBD().getHearingIdFromScheduleHearing(model.getAcm().getScheduledHearingId());
            SHJusticeControllerBeanBusinessDelegate shDel = XhibitDelegateHelper.getSHJusticeDelegate();
            hearingHeaderValue.setShJusticeValues(shDel.findByHearingId(hearingId));
            
            if (LOG.isDebugEnabled()) {
                LOG.debug("loadCourtLogHeaderTableModel() loaded hhv with linked cases :");
                CaseSchedHearingValue[] chvs = hearingHeaderValue.getCaseSchedHearingValues();
                for (int x = 0; x < chvs.length; x++) {
                    String s = chvs[x].getCaseType() + chvs[x].getCaseNumber().toString();
                    LOG.debug("getLinkedCases() found '" + s + "'");
                }
            }

            /*
             * CourtLogHeaderValue BD returned data? Y. instantiate clhArray
             * with 1 element store the CourtLogHeaderValue VO in clhArray[0] N.
             * instantiate clhArray with 0 elements
             */
            clhArray = new Object[1];
            clhArray[0] = hearingHeaderValue;

            // create instance of HearingHeaderValueHelper
            myHelper = new HearingHeaderValueHelper(hearingHeaderValue);
        } catch (HearingScheduleException hse) {
            // Initialise an empty clhArray
            clhArray = new Object[0];
        } catch (FinderException e) {
        	XHIBITConstant.handleError(e, this.getClass());
		} finally {
            // Save the value object returned in the model
            model.setHearingHeaderValue(hearingHeaderValue);

            // Save the court log header table model in the model
            Collection defendants = new ArrayList();
            String[] defStringArray = model.getAcm().getScheduledHearingValue().getDefendants();
            for (int i = 0; i < defStringArray.length; i++) {
                defendants.add(defStringArray[i]);
            }

            if (model.getCourtLogHeaderTableModel() == null) {
                model.setCourtLogHeaderTableModel(new CourtLogHeaderTableModel(clhArray, defendants));
            } else {
                model.getCourtLogHeaderTableModel().setData(clhArray);
                model.getCourtLogHeaderTableModel().setDefendants(defendants);
            }
        }
    }

    private void loadCourtLogEventsTableModel() {
        final CourtLogViewValue[] courtLogEvents;

        if (model.getAcm().isForAllDaysLogs()) {
            courtLogEvents = XhibitDelegateHelper.getCourtLogDelegate2().getCourtLog(model.getAcm().getCaseId());
        } else {
            courtLogEvents = XhibitDelegateHelper.getCourtLogDelegate2().getCourtLog(model.getAcm().getCaseId(),
                    model.getAcm().getScheduledHearingDateFrom(), model.getAcm().getScheduledHearingDateTo());
        }
        LOG.debug("courtLogEventsCollection size: " + courtLogEvents.length);

        for (int i = 0; i < courtLogEvents.length; i++) {
            clch.translateCourtLogViewValue(courtLogEvents[i]);
        }

        model.setListOfEvents(courtLogEvents);

        // Save the court log events table model in the model
        if (model.getCourtLogEventsTableModel() == null) {
            model.setCourtLogEventsTableModel(new CourtLogEventsTableModel(courtLogEvents));
        } else {
            model.getCourtLogEventsTableModel().setData(courtLogEvents);
        }
    }

    private void jbInit() {
        final JPanel courtLogHeaderPanel = new JPanel();
        final JPanel mainPanel = new JPanel();
        final JPanel eventsPanel = new JPanel();

        final JScrollPane courtLogHeaderScrollPane = new JScrollPane();

        this.setLayout(new GridBagLayout());
        mainPanel.setLayout(new GridBagLayout());
        this.setMinimumSize(new Dimension(800, 600));
        this.setPreferredSize(new Dimension(800, 600));
        mainPanel.setMinimumSize(new Dimension(600, 800));
        mainPanel.setPreferredSize(new Dimension(600, 800));
        courtLogHeaderPanel.setLayout(new GridBagLayout());
        courtLogHeaderScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        courtLogHeaderScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        courtLogHeaderScrollPane.setBorder(null);
        courtLogHeaderScrollPane.setMaximumSize(new Dimension(600, 92));
        courtLogHeaderScrollPane.setMinimumSize(new Dimension(600, 92));
        courtLogHeaderScrollPane.setPreferredSize(new Dimension(600, 92));
        eventsPanel.setLayout(new GridBagLayout());

        this.add(mainPanel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        mainPanel.add(courtLogHeaderPanel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));
        courtLogHeaderPanel.add(courtLogHeaderScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
        courtLogHeaderScrollPane.getViewport().add(getCourtLogHeaderTable(), null);
        mainPanel.add(eventsPanel, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0));

        eventsScrollPane = new JScrollPane(getEventsTable());
        eventsPanel.add(eventsScrollPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

        // Configure table to delete an event when the DELETE key is pressed
        String deleteActionName = getDeleteAction().getName();
        getEventsTable().getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), deleteActionName);
        getEventsTable().getActionMap().put(deleteActionName, getDeleteAction());

        MouseListener eventsTablePopupListener = new EventsTablePopupListener();
        getEventsTable().addMouseListener(eventsTablePopupListener);
        MouseListener courtLogHeaderTablePopupListener = new CourtLogHeaderTablePopupListener();
        getCourtLogHeaderTable().addMouseListener(courtLogHeaderTablePopupListener);
    }

    public JTable getCourtLogHeaderTable() {
        if (courtLogHeaderTable == null) {
            courtLogHeaderTable = XTableFactory.getInstance().createDefaultTable(model.getCourtLogHeaderTableModel());
            courtLogHeaderTable.setRowHeight(3 * XHIBITConstant.getLineHeight());
            courtLogHeaderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            courtLogHeaderTable.getTableHeader().setReorderingAllowed(false);

            // Set the cell renderer to MultiLineCellRenderer for all
            // columns
            for (int x = 0; x < courtLogHeaderTable.getColumnCount(); x++) {
                courtLogHeaderTable.getColumnModel().getColumn(x).setCellRenderer(
                        XTableFactory.getInstance().getMultiLineCellRenderer(courtLogHeaderTable));
            }

            int remainingWidth = SCREEN_WIDTH;
            TableColumn column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.T_DEFENDANT);
            column.setPreferredWidth(getDefendantColumnSize());
            remainingWidth -= column.getPreferredWidth();

            // Set Case Column (Applies to all case types)
            column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.T_CASE);
            column.setPreferredWidth(PREFERRED_CASENO_COL_WIDTH);
            remainingWidth -= column.getPreferredWidth();

            try {
                if (CaseTypeHelper.isCriminalAppeal_CaseType(hearingHeaderValue)) {
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_TIME);
                    column.setPreferredWidth(PREFERRED_TIME_COL_WIDTH);
                    remainingWidth -= column.getPreferredWidth();

                    remainingWidth = remainingWidth / 9;
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_JUDGE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_JUSTICE_1);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_JUSTICE_2);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_JUSTICE_3);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_JUSTICE_4);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_RESP_ADVOCATE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_APP_ADVOCATE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.A_HEARING);
                    column.setPreferredWidth(remainingWidth);
                } else if (CaseTypeHelper.isTrial_CaseType(hearingHeaderValue)) {
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.T_TIME);
                    column.setPreferredWidth(PREFERRED_TIME_COL_WIDTH);
                } else if (CaseTypeHelper.isMiscelleanousAppeal_CaseType(hearingHeaderValue)) {
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_TIME);
                    column.setPreferredWidth(PREFERRED_TIME_COL_WIDTH);
                    remainingWidth -= column.getPreferredWidth();
                    remainingWidth = remainingWidth / 10;
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_OBJ_ADVOCATE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_JUDGE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_JUSTICE_1);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_JUSTICE_2);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_JUSTICE_3);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_JUSTICE_4);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_RESP_ADVOCATE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_APP_ADVOCATE);
                    column.setPreferredWidth(remainingWidth);
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.M_HEARING);
                    column.setPreferredWidth(remainingWidth);
                    
                } else {
                    column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.T_TIME);
                    column.setPreferredWidth(PREFERRED_TIME_COL_WIDTH);
                }
            } catch (UnknownCaseTypeException ex) {
                column = courtLogHeaderTable.getColumnModel().getColumn(CourtLogHeaderTableModel.T_TIME);
                column.setPreferredWidth(PREFERRED_TIME_COL_WIDTH);
            }
            courtLogHeaderTable.add(getCourtLogHeaderTablePopup());
        }

        return courtLogHeaderTable;
    }

    protected JPopupMenu getCourtLogHeaderTablePopup() {
        if (courtLogHeaderTablePopup == null) {
            courtLogHeaderTablePopup = new JPopupMenu();

            try {
                JMenuItem menuItem = new JMenuItem();
                menuItem.setAction(XhibitActions.getAction(model.getAcm().getXhibitApplicationController(),
                        XhibitActions.CaseProps));
                courtLogHeaderTablePopup.add(menuItem);
            } catch (ActionNotFoundException ex) {
                LOG.info("@todo: Do what if this exception is raised ", ex);
            }
        }

        return courtLogHeaderTablePopup;
    }

    public XTable getEventsTable() {
        if (eventsTable == null) {
            eventsTable = XTableFactory.getInstance().createMultiLineTable(model.getCourtLogEventsTableModel());
            eventsTable.getColumnModel().getColumn(CourtLogEventsTableModel.EVENT_COLUMN).setCellRenderer(
                    XTableFactory.getInstance().getStyleTableRenderer(eventsTable));
            eventsTable.initColumnSizes(new Object[] { "DD-MON-YYYY", "HH:MM", XTableFactory.COLUMN_WIDTH_UNDEFINED },
                    model.getAcm().getXhibitApplicationController().getSize().width);

            eventsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            eventsTable.setFont(XHIBITConstant.getCurrentFont());
            ListSelectionModel rowSM = eventsTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting())
                        return;

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (!lsm.isSelectionEmpty()) {
                        setSelectedRow(lsm.getMinSelectionIndex());
                    }
                }
            });

            eventsTable.getTableHeader().setReorderingAllowed(false);

            eventsTable.add(getEventsTablePopup());
        }

        return eventsTable;
    }

    protected void setSelectedRow(int row) {
        setEditDeleteEnable(false);

        if (model.getAcm().isInEditMode(FunctionList.ECourtLog) && isEditableEvent(row)) {
            model.setSelectedEventsTableRow(row);
            getEditAction().setModel(model.getListOfEvents()[model.getSelectedEventsTableRow()]);
            getDeleteAction().setModel(model.getListOfEvents()[model.getSelectedEventsTableRow()]);
            setEditDeleteEnable(true);
        }
    }

    private void setEditDeleteEnable(boolean enable) {
        getEditAction().setEnabled(enable);
        getDeleteAction().setEnabled(enable);
    }

    public JPopupMenu getEventsTablePopup() {
        if (eventsTablePopup == null) {
            eventsTablePopup = new JPopupMenu();

            JMenuItem menuItem = new JMenuItem();
            menuItem.setAction(getEditAction());
            eventsTablePopup.add(menuItem);

            menuItem = new JMenuItem();
            menuItem.setAction(getDeleteAction());
            eventsTablePopup.add(menuItem);
        }

        return eventsTablePopup;
    }

    protected XAction getEditAction() {
        if (clEditAction == null) {
            clEditAction = XhibitActions.getAction(model.getAcm().getXhibitApplicationController(),
                    XhibitActions.EditClEvent);
        }
        return clEditAction;
    }

    private XAction getDeleteAction() {
        if (clDeleteAction == null) {
            clDeleteAction = XhibitActions.getAction(model.getAcm().getXhibitApplicationController(),
                    XhibitActions.DeleteClEvent);
        }
        return clDeleteAction;
    }

    public void stepActivate() throws CSRecoverableException {
        LOG.debug("Inside stepActivate");

        moveModelToScreen();

        stepUpdateViewState();
    }

    public void stepUpdateViewState() {
    }

    class EventsTablePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            int row = getEventsTable().rowAtPoint(e.getPoint());
            lastSelectedRow = row;

            getEventsTable().setRowSelectionInterval(row, row);

            if (model.getAcm().isInEditMode(FunctionList.ECourtLog) && isEditableEvent(row)) {
                if (e.isPopupTrigger()) {
                    setSelectedRow(row);
                    getEventsTablePopup().show(e.getComponent(), e.getX(), e.getY());
                } else if (e.getClickCount() == 2) {
                    if (isEditableEvent(row)) {
                        getEditAction().actionPerformed(new ActionEvent(getEventsTable(), 0, "EditAction"));
                        e.consume();
                    }
                }
            }
        }
    }

    class CourtLogHeaderTablePopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            int row = getCourtLogHeaderTable().rowAtPoint(e.getPoint());
            int column = getCourtLogHeaderTable().columnAtPoint(e.getPoint());

            getCourtLogHeaderTable().setRowSelectionInterval(row, row);

            if (e.isPopupTrigger() && FunctionList.hasAccess(FunctionList.VCaseProperty)) {
                model.setSelectedHeaderTableColumn(column);

                getCourtLogHeaderTablePopup().show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    public void stepValidate() throws CSValidationException, CSRecoverableException {
        // Empty implementation
    }

    public void stepDeactivate() throws CSRecoverableException {
        // Empty implementation
    }

    public void stepDeinitialise(boolean update) throws CSRecoverableException {
        setEditDeleteEnable(false);
    }

    private void moveModelToScreen() {
        // CourtLogHeaderTable
        getCourtLogHeaderTable().tableChanged(new TableModelEvent(getCourtLogHeaderTable().getModel()));
        getCourtLogHeaderTable().revalidate();
        getCourtLogHeaderTable().repaint();

        // CourtLogEventsTable
        getEventsTable().tableChanged(new TableModelEvent(getEventsTable().getModel()));
        getEventsTable().revalidate();
        getEventsTable().repaint();
    }

    protected boolean isEditableEvent(int param) {
        boolean returnCode = false;

        if (model.getEditableEvents() != null) {
            CourtLogViewValue courtLogViewValue = model.getListOfEvents()[param];

            // Determine if the selected event is editable
            for (int x = 0; x < model.getEditableEvents().length && !returnCode; x++) {
                String myString = (String) model.getEditableEvents()[x];

                // Is the event in the list of editable court log events
                if (myString.equalsIgnoreCase(courtLogViewValue.getEventType().toString())) {
                    // It is so set the return code to allow editing
                    returnCode = true;
                }
            }
        }

        return returnCode;
    }

    public void refreshEvents() {
        refreshEvents(false);
    }

    public void refreshEvents(boolean reselectLastEvent) {
        final boolean reselect = (reselectLastEvent && (lastSelectedRow >= 0) && (lastSelectedRow < getEventsTable()
                .getRowCount()));

        loadCourtLogEventsTableModel();
        moveModelToScreen();

        if (getEventsTable().getRowCount() > 0) {
            if (reselect) {
                getEventsTable().clearSelection();
                // pass the focus to the scroll pane so that
                // removes the yellow border from the cell
                eventsScrollPane.requestFocus();
            } else {
                // get the row count and subtract 1 to zero base.
                int x = getEventsTable().getRowCount() - 1;
                getEventsTable().setRowSelectionInterval(x, x);
                setSelectedRow(x);
            }
        } else {
            getEventsTable().clearSelection();
        }

        eventsScrollPane.revalidate();
        eventsScrollPane.repaint();
        SwingUtilities.invokeLater(new ScrollTableRowToView(getEventsTable(), eventsScrollPane));

        // Only enable the Edit/Delete actions if there is a selected event row
        setEditDeleteEnable(getEventsTable().getSelectedRow() > -1);
    }

    /**
     * @return true if the print action should automatically create a PDF
     *         document
     */
    public boolean autoSaveToFile() {
        return false;
    }

    public String[] print() throws UserCancelException, CSRecoverableException {
        return clch.formatCourtLogForPrinting(model.getAcm().getXhibitApplicationController(), model);
    }

    private int getDefendantColumnSize() {
        Iterator defIter = myHelper.getDefendantNames().iterator();

        // loop through collection to get max defendant name length
        while (defIter.hasNext()) {
            String fullname = (String) defIter.next();
            if (fullname.length() > maxDefendantNameLength) {
                maxDefendantNameLength = fullname.length();
            }
        }

        return PIXELS_PER_CHAR * maxDefendantNameLength;
    }
    

	/**
	 * Iteration 2 Access to the Mid Tier. Loads (if necessary) and returns the
	 * HearingScheduleControllerBusinessDelegate
	 * 
	 * @return the HearingScheduleControllerBusinessDelegate
	 */
	protected HearingScheduleControllerBeanBusinessDelegate getHearingScheduleBD() {
		return XhibitDelegateHelper.getHearingDelegate();
	}
}
