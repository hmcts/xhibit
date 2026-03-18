package uk.gov.courtservice.xhibit.client.schedule;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

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
 * 
 * Notes: Fred Vandendriessche changed JTable courtTable to XTable. IF required,
 * this courtTable may be made sortable using the .makeSortable() method. (and
 * make your courtTableModel a subclass of XSortableTableModel) Whether or not
 * sorting is required, an XTable must be used (always, from now on)
 */
public class CourtPanel extends JPanel {

    public static final int ALLCOURTS = 1;

    public static final int ONECOURT = 2;

    private TodaysScheduleController myParent = null;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    private JLabel courtNameLabel = null;

    private XTable courtTable = null;

    private JScrollPane courtTableScrollPane = null;

    private CourtTableModel courtTableModel = new CourtTableModel(new Object[] {});

    private TableColumn courtColumn;

    private JPopupMenu courtTablePopup = null;

    private int oldCourtWidth = 0;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public CourtPanel(TodaysScheduleController parent) {
        myParent = parent;
        jbInit();
    }

    private void jbInit() {
        this.setLayout(gridBagLayout1);

        this.add(getCourtNameLabel(), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getCourtTableScrollPane(), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));
        this.setPreferredSize(new Dimension(1000, 500));
    }

    private JScrollPane getCourtTableScrollPane() {
        if (courtTableScrollPane == null) {
            courtTableScrollPane = new JScrollPane(getCourtTable());
        }
        return courtTableScrollPane;
    }

    protected JTable getCourtTable() {
        if (courtTable == null) {
            XTableFactory xtf = XTableFactory.getInstance();
            courtTable = xtf.createMultiLineTable(courtTableModel); // new
            // JTable(courtTableModel);
            String[] longValues = { "Court 999", "A99999999 ", "His Honour Judge Rakesh Lakhani",
                    "Firstname Middlename Surname", "Mention & Application", "99:99 ", "To be heard." };
            courtTable.initColumnSizes(longValues, 500);
            // xtf.initColumnSizes(courtTable, longValues, 500);
            oldCourtWidth = courtTable.getColumnModel().getColumn(CourtTableModel.COURT_COLUMN).getWidth();
            courtTable.setPreferredScrollableViewportSize(new Dimension(500, 100));

            courtTablePopup = myParent.getPopup();
            if (courtTablePopup != null)
                courtTable.add(courtTablePopup);

            courtTable.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);

            // Add Listeners
            courtTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ListSelectionModel rowSM = courtTable.getSelectionModel();
            rowSM.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    // Ignore extra messages.
                    if (e.getValueIsAdjusting())
                        return;

                    ListSelectionModel lsm = (ListSelectionModel) e.getSource();
                    if (lsm.isSelectionEmpty()) {
                        // no rows are selected
                    } else {
                        int selectedRow = lsm.getMinSelectionIndex();
                        setRow(selectedRow);
                    }
                }
            });

            MouseListener tableListener = new TableListener();
            courtTable.addMouseListener(tableListener);
            if (courtTablePopup != null) {
                MouseListener popupListener = new PopupListener(courtTablePopup);
                courtTable.addMouseListener(popupListener);
            }

        }
        return courtTable;
    }

    private JLabel getCourtNameLabel() {
        if (courtNameLabel == null) {
            courtNameLabel = new JLabel();
            courtNameLabel.setFont(new java.awt.Font("SansSerif", 0, 18));
            courtNameLabel.setText("");
        }
        return courtNameLabel;
    }

    protected void loadCourtTable(Object[] ctm, int tableType) {

        // if (tableType==ALLCOURTS) {
        // getCourtColumn().setMinWidth(oldCourtWidth-1);
        // getCourtColumn().setMaxWidth(oldCourtWidth+1);
        // getCourtColumn().setPreferredWidth(oldCourtWidth);
        // getCourtColumn().setPreferredWidth(oldCourtWidth);
        // } else {
        // oldCourtWidth = getCourtColumn().getMaxWidth()-1;
        // uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("MAX
        // WIDTH="+ oldCourtWidth);
        // getCourtColumn().setWidth(0);
        // getCourtColumn().setPreferredWidth(0);
        // getCourtColumn().setMinWidth(0);
        // getCourtColumn().setMaxWidth(1);
        // }

        getCourtTable().getSelectionModel().clearSelection();

        getCourtTableModel().setData(ctm);

        getCourtTable().tableChanged(new TableModelEvent(getCourtTableModel()));
        getCourtTable().revalidate();
        getCourtTable().repaint();

    }

    public TableColumn getCourtColumn() {
        if (courtColumn == null) {
            courtColumn = getCourtTable().getColumnModel().getColumn(CourtTableModel.COURT_COLUMN);
        }
        return courtColumn;
    }

    public CourtTableModel getCourtTableModel() {
        return (CourtTableModel) getCourtTable().getModel();
    }

    public void setVisible(boolean isVisible, String title) {
        if (isVisible) {
            getCourtNameLabel().setText(title);
        }
        super.setVisible(isVisible);
    }

    // Returns just the class name -- no package info.
    // method for testing.
    protected String getClassName(Object o) {
        String classString = o.getClass().getName();
        int dotIndex = classString.lastIndexOf(".");
        return classString.substring(dotIndex + 1);
    }

    protected void setRow(int i) {
        uk.gov.courtservice.xhibit.client.util.XHIBITConstant.debug("ROW:" + i);
        ScheduledHearingValue tempShv = (ScheduledHearingValue) ((XHIBITTableModelInterface) getCourtTable().getModel())
                .getDataAt(i);
        myParent.setShv((ScheduledHearingValue) (tempShv));
    }

    class TableListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                getRow(e);

                XhibitApplicationController xac = (XhibitApplicationController) myParent.getParentController();

                XAction updateAction = XhibitActions.getAction(xac, XhibitActions.UpdateCase);
                ActionEvent ae = new ActionEvent(e.getSource(), e.getID(), "OpenCase");
                ScheduledHearingValue shv = myParent.getShv();
                if (myParent.getCurrentCourtRoom() == shv.getCourtRoomId().intValue() && updateAction.isEnabled()) {
                    updateAction.actionPerformed(ae);
                } else {
                    XAction viewAction = XhibitActions.getAction(xac, XhibitActions.ViewCase);
                    if (viewAction.isEnabled())
                        viewAction.actionPerformed(ae);
                }

                // if (myParent.hasViewAccess()) {
                // boolean isEdit = false;
                //
                // uk.gov.courtservice.xhibit.business.vos.services.todaysschedule.ScheduledHearingValue
                // shv = myParent.getShv();
                //
                // if
                // (myParent.getCurrentCourtRoom()==shv.getCourtRoomId().intValue())
                // {
                // if (myParent.hasUpdateAccess()) {
                // isEdit = true;
                // }
                // }
                // try
                // {
                // xac.openCase(shv, isEdit);
                // }
                // catch (CSRecoverableException ex)
                // {
                // XHIBITConstant.handleError(ex);
                // }
                // }
            } else {
                setRow(getRow(e));
            }
        }

        private int getRow(MouseEvent e) {
            // get point where user right clicked.
            int row = getCourtTable().rowAtPoint(e.getPoint());
            // Select row in table where user clicked.
            getCourtTable().setRowSelectionInterval(row, row);
            int i = getCourtTable().getSelectedRow();
            return i;
        }

    }

    class PopupListener extends MouseAdapter {
        private JPopupMenu thisPopup = null;

        public PopupListener(JPopupMenu pMenu) {
            thisPopup = pMenu;
        }

        public void mousePressed(MouseEvent e) {
            // maybeShowPopup(e);
        }

        public void mouseReleased(MouseEvent e) {
            maybeShowPopup(e);
        }

        private void maybeShowPopup(MouseEvent e) {
            if (e.isPopupTrigger()) {
                getRow(e);
                if (thisPopup != null) {
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

        private void getRow(MouseEvent e) {
            // get point where user right clicked.
            int row = getCourtTable().rowAtPoint(e.getPoint());
            // Select row in table where user clicked.
            getCourtTable().setRowSelectionInterval(row, row);
            int i = getCourtTable().getSelectedRow();
            ScheduledHearingValue tempShv = (ScheduledHearingValue) ((XHIBITTableModelInterface) getCourtTable()
                    .getModel()).getDataAt(i);
            myParent.setShv((ScheduledHearingValue) (tempShv));
        }
    }
}