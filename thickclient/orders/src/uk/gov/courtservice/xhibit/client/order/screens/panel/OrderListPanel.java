package uk.gov.courtservice.xhibit.client.order.screens.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.orders.XhbOrderValue;
import uk.gov.courtservice.xhibit.client.order.exceptions.OrderNotSelectedException;
import uk.gov.courtservice.xhibit.client.order.screens.dialog.OrdersWizard;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderCheckBoxListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.OrderTableSelectionListener;
import uk.gov.courtservice.xhibit.client.order.screens.helper.ResourceHelper;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrderSorter;
import uk.gov.courtservice.xhibit.client.order.screens.util.OrdersListTableModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.security.FunctionList;

/**
 * <p>
 * Title: Xhibit2 OrderListPanel
 * </p>
 * <p>
 * Description: Displays a table of existing orders
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Neil Entwistle
 * @version 1.0
 */

public class OrderListPanel extends AbstractOrdersPanel {
    private static final Logger log = CSServices.getLogger(OrderListPanel.class);

    private static final String ORDER_LIST_PANEL_TITLE = "OrderListTitle";

    private static final String ORDER_LIST_PANEL_CREATE_OPTION = "OrderListOption";

    private static final String ORDER_LIST_ORDER_ID_COL = "order.list.order.id.col";

    private static final String ORDER_LIST_ORDER_STAT_COL = "order.list.order.stat.col";

    private static final String ORDER_LIST_ORDER_TYPE_COL = "order.list.order.type.col";

    private static final String ORDER_LIST_ORDER_DESC_COL = "order.list.order.desc.col";

    private static final int DESCRIPTION_COL = 2;

    private static final char CHAR_NEWLINE = '\n';

    private static final char CHAR_SPACE = ' ';

    private JTable tblOrderList = null;

    private JCheckBox cbCreateOption = null;

    private boolean cancelled = false;

    private OrderTableSelectionListener table_cbListener;

    private JButton dlgButton;

    /**
     * Constructor
     */
    public OrderListPanel() {
        super();
    }

    /**
     * Constructor
     * 
     * @param odm
     *            the model
     * @param button
     *            the button to enable/disable
     * @throws CSRecoverableException
     */
    public OrderListPanel(OrderInitialDataVO model, JButton button, boolean replace, boolean copy) throws CSRecoverableException {
        super(model);
        this.model = model;
        this.dlgButton = button;
        initialiseListPanel(replace, copy);
    }

    /**
     * Initialise the panel
     * 
     * @throws CSRecoverableException
     */
    private void initialiseListPanel(boolean replace, boolean copy) throws CSRecoverableException {
        super.initialisePanel();

        getConstraints().gridx++;
        getConstraints().weightx = 1.0;
        getConstraints().weighty = 1.0;
        getConstraints().fill = GridBagConstraints.BOTH;

        add(new JScrollPane(getOrderListTable(replace, copy)));

        getConstraints().gridy++;

        JCheckBox cbCreateOption = getCreateCheckBox();
        cbCreateOption.addActionListener(table_cbListener);

        add(cbCreateOption, getConstraints());

        setBorder(new TitledBorder(BorderFactory.createEtchedBorder(), ResourceHelper
                .getResourceString(ORDER_LIST_PANEL_TITLE)));

    }

    /**
     * Sets the data on the panel from the supplied model
     * 
     * @param odm
     *            the model
     * @throws CSRecoverableException
     */
    public void setPanelFromModel(OrderInitialDataVO model) throws CSRecoverableException {
        this.getCreateCheckBox().setSelected(model.isCreateOrder());
        this.getOrderListTable(false, false).setModel(new OrdersListTableModel());
    }

    /**
     * Update the model and show a message dialog if any missing data
     * 
     * @param parm1
     * @throws uk.gov.courtservice.framework.exception.CSRecoverableException
     * 
     */
    public void stepDeinitialise(boolean parm1) throws uk.gov.courtservice.framework.exception.CSRecoverableException {
        log.debug("OrderListPanel:stepDeinitialise" + parm1);
        if (parm1 == true && this.model.isCreateOrder() == false) {
            try {
                Integer orderID = this.model.getSelectedOrder();
            } catch (OrderNotSelectedException onse) {
                JOptionPane.showMessageDialog(this, ResourceHelper.getResourceString(OrdersWizard.WIZ_WARN_MESSAGE),
                        ResourceHelper.getResourceString(OrdersWizard.WIZ_WARN_TITLE), JOptionPane.WARNING_MESSAGE);
                log.debug(onse);
                throw new UserCancelException();
            }
        }
        setCancelled(!parm1);
    }

    /**
     * Returns an array of XhbOrderValue objects representing the orders for the
     * defendant
     * 
     * @return XhbOrderValue[]
     */
    private XhbOrderValue[] getOrderList(boolean replace, boolean copy) {
        log.debug("Start - getOrderList");
        log.debug("replace: " + replace);
        XhbOrderValue[] returnedOrderList;
        if (replace) {
            returnedOrderList = getReplaceableOrderList();
        } else if (copy) {
            returnedOrderList = this.model.getHelper().getNonObsOrdersForDefendant();
        } else {
            returnedOrderList = this.model.getHelper().getOrdersForDefendant();
        }
        
        if (this.model.isMonetaryOrder()) {
            returnedOrderList = filterReturnedOrderList(returnedOrderList, "MO");
        } else if (this.model.isD20Order()) {
            returnedOrderList = filterReturnedOrderList(returnedOrderList, "D20");
        } else {
            returnedOrderList = filterReturnedOrderList(returnedOrderList, "Normal");
        }
        
        return returnedOrderList;
    }
    
    /**
     * Ensure that we only list in the order panel orders of the type we are supposed to.
     * 
     * @param inList
     * @param type
     * @return
     */
    private XhbOrderValue[] filterReturnedOrderList(XhbOrderValue[] inList, String type) {
        ArrayList newListAL = new ArrayList<XhbOrderValue>();
        
        int newListCount = 0;
        for (int i=0; i< inList.length; i++) {
            XhbOrderValue thisOrder = inList[i];
            if (type.equals("MO")) {
                if (thisOrder.getXhbOrderTemplate().getXhbOrderType().getCode().equals("MO")) {
                    newListAL.add(thisOrder);
                }
            } else if (type.equals("D20")) {
                if (thisOrder.getXhbOrderTemplate().getXhbOrderType().getCode().equals("D20")) {
                    newListAL.add(thisOrder);
                }
            } else {
                if (!thisOrder.getXhbOrderTemplate().getXhbOrderType().getCode().equals("MO") &&
                        !thisOrder.getXhbOrderTemplate().getXhbOrderType().getCode().equals("D20")) {
                    newListAL.add(thisOrder);
                }
            }
        }
        
        XhbOrderValue[] newListArray = new XhbOrderValue[newListAL.size()];
        return (XhbOrderValue[]) newListAL.toArray(newListArray);
    }

    /**
     * Returns an array of XhbOrderValue objects representing the orders that
     * can be replaced for the defendant and order type
     * 
     * @return XhbOrderValue[]
     */
    private XhbOrderValue[] getReplaceableOrderList() {
        return this.model.getHelper().getReplaceableOrdersForDefendantAndOrderType();
    }

    /**
     * Returns the list of column names
     * 
     * @return the columns
     */
    private String[] getColumns() {
        return new String[] { ResourceHelper.getResourceString(ORDER_LIST_ORDER_STAT_COL),
                ResourceHelper.getResourceString(ORDER_LIST_ORDER_TYPE_COL),
                ResourceHelper.getResourceString(ORDER_LIST_ORDER_DESC_COL) };
    }

    /**
     * Creates the table to display the order list
     * 
     * boolean replace - is it a replacement
     * boolean copy - is it a copy (as if so we dont want any old orders of an obsolete order template included)
     * 
     * @return the table
     * @throws CSRecoverableException
     */
    private JTable getOrderListTable(boolean replace, boolean copy) throws CSRecoverableException {
        log.debug("Start - getOrderListTable");
        log.debug("replace: " + replace);
        if (tblOrderList == null) {
            OrdersListTableModel tblModel = new OrdersListTableModel();
            tblModel.setData(OrderSorter.sort(getOrderList(replace, copy)));
            tblModel.setColumnNames(getColumns());
            tblOrderList = new JTable(tblModel);

            // Install the custom renderer on the description column
            TableColumn col = tblOrderList.getColumnModel().getColumn(DESCRIPTION_COL);
            col.setCellRenderer(new OrderListTableCellRenderer());

            ListSelectionModel lsm = tblOrderList.getSelectionModel();
            lsm.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table_cbListener = new OrderTableSelectionListener(tblOrderList, this.model, dlgButton);
            lsm.addListSelectionListener(table_cbListener);
        }

        return tblOrderList;
    }

    /**
     * Creates cbCreateOption to record if a new order should be created from
     * this panel. If selected the action will return to the wizard
     * 
     * @return JCheckBox
     * @throws CSRecoverableException
     */
    public JCheckBox getCreateCheckBox() throws CSRecoverableException {
        if (cbCreateOption == null) {
            cbCreateOption = new JCheckBox(ResourceHelper.getResourceString(ORDER_LIST_PANEL_CREATE_OPTION));
            cbCreateOption.addActionListener(new OrderCheckBoxListener(this.model, getOrderListTable(false, false)));
        }
        // Only enable checkbox if in CREATE mode, and check whether its a monetary order or not
        if (this.model.isMonetaryOrder()) {
        	boolean hasMonetaryDisposals = false;
        	String defName = this.model.getDefendantName();
        	if ((this.model.getHelper().getOrderDisposalData() != null) && 
        			(this.model.getHelper().getOrderDisposalData().get(defName) != null) &&
        			(((HashMap<String, Object>)this.model.getHelper().getOrderDisposalData().get(defName)).get("hasMonetaryDisposals") != null)) {
        		hasMonetaryDisposals = new Boolean(((HashMap<String, Object>)this.model.getHelper().getOrderDisposalData().get(defName)).get("hasMonetaryDisposals")+"").booleanValue();
        	}
        	boolean enabled = hasMonetaryDisposals && 
        			this.model.getHelper().getXAppController().getApplicationCaseModel().isInEditMode(FunctionList.EMonetaryCreateOrder);
            cbCreateOption.setEnabled(enabled);
        } else if (this.model.isD20Order()) {
            cbCreateOption.setEnabled(this.model.getHelper().getXAppController().getApplicationCaseModel().isInEditMode(FunctionList.ED20CreateOrder));
        } else {
            cbCreateOption.setEnabled(this.model.getHelper().getXAppController().getApplicationCaseModel().isInEditMode(FunctionList.ECreateOrder));
        }
        return cbCreateOption;

    }

    /**
     * Returns the cancelled indicator
     * 
     * @return boolean true if cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the cancelled indicator
     * 
     * @param can
     *            boolean
     */
    public void setCancelled(boolean can) {
        cancelled = can;
    }

    // This renderer extends a component. It is used each time a
    // cell must be displayed.
    public class OrderListTableCellRenderer extends JLabel implements TableCellRenderer {
        public OrderListTableCellRenderer() {
            super();
            // Set opaque to true to enable the JLabel background/foreground
            // to be set
            setOpaque(true);
        }

        // This method is called each time a cell in a column
        // using this renderer needs to be rendered.
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at
            // (rowIndex, vColIndex)

            if (isSelected) {
                // cell (and perhaps other cells) are selected
                // set background to the same selected colour that the table
                // displays
                setBackground(table.getSelectionBackground());
                // set foreground to the same selected colour that the table
                // displays
                setForeground(table.getSelectionForeground());
            } else {
                // cell is deselected
                // set background to the same unselected colour that the table
                // displays
                setBackground(table.getBackground());
                // set foreground to the same unselected colour that the table
                // displays
                setForeground(table.getForeground());
                setBorder(null);
            }

            if (hasFocus) {
                // this cell is the anchor and the table has the focus
                // set the border to yellow to reproduce the behaviour of the
                // table
                setBorder(new LineBorder(Color.yellow));
            }

            // Replace the carriage return with a space on the rendered
            // table only.
            // The original data is untouched
            setText(value.toString().replace(CHAR_NEWLINE, CHAR_SPACE));

            // Since the renderer is a component, return itself
            return this;
        }

        // The following methods override the defaults for performance reasons
        public void validate() {
        }

        public void revalidate() {
        }

        protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        }

        public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
        }

    }

}