package uk.gov.courtservice.xhibit.client.publicdisplayconfig.maintainrotationset.views;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import uk.gov.courtservice.xhibit.client.publicdisplayconfig.tablemodels.SelectListTableModel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.PublicDisplayUtils;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel.AbstractSelectorPanel;
import uk.gov.courtservice.xhibit.client.util.RightJustifyTextRenderer;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XTableFactory;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.util.table.renderers.XNumericLimitedLengthCellEditor;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.RotationSetDDComplexValue;

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
 * @version $Id: SelectedListTableView.java,v 1.7 2006/06/05 12:32:07 bzjrnl Exp $
 */

public class SelectedListTableView extends AbstractSelectorPanel {
    private static final String LABEL_SELECTEDLIST = "pd.label.selectedlist";

    private static final Dimension dim = new Dimension(250, 350);

    private static final Dimension buttonDim = new Dimension(30, 30);

    SelectListTableModel tableModel;

    XTable table;

    Integer _rotationSetId;

    public SelectedListTableView(Integer rotationSetId, Object[] data) {
        _rotationSetId = rotationSetId;

        this.setLayout(new GridBagLayout());
        tableModel = new SelectListTableModel(getDataArrayList(data));

        String title = PublicDisplayUtils.getResource(LABEL_SELECTEDLIST);

        this.add(new JLabel(title), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(PublicDisplayUtils.getDefaultScrollPane(getListTable(), dim), new GridBagConstraints(0, 1, 1, 4, 1.0,
                1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, XHIBITConstant.nonContainerInsets, 0, 0));

        this.add(new JLabel(), new GridBagConstraints(1, 1, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getButton(getUpAction()), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(getButton(getDownAction()), new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, XHIBITConstant.nonContainerInsets, 0, 0));
        this.add(new JLabel(), new GridBagConstraints(1, 4, 1, 1, 0.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.HORIZONTAL, XHIBITConstant.nonContainerInsets, 0, 0));

        stepUpdateViewState();
    }

    private JButton getButton(XAction action) {
        JButton button = JButtonFactory.getButton(action);
        button.setMinimumSize(buttonDim);
        button.setPreferredSize(buttonDim);
        button.setMaximumSize(buttonDim);
        return button;
    }

    private ArrayList getDataArrayList(Object[] data) {
        ArrayList al = new ArrayList();

        for (int i = 0; i < data.length; i++) {
            al.add(data[i]);
        }
        return al;
    }

    public void removeElementAt(int objectToRemoveLocationId) {
        Object o = getModelInterface().getDataAt(objectToRemoveLocationId);
        tableModel.removeElement((RotationSetDDComplexValue) o);

        // reset ordering
        resetOrder(objectToRemoveLocationId);
    }

    public int[] getSelectedIndeces() {
        return table.getSelectedRows();
    }

    /**
     * 
     * @return Array of RotationSetDDComplexValue
     */
    public Object[] getData() {
        setEditingValue();
        return tableModel.getData();
    }

    // This method checks if the table is still in edit mode and forces it
    // to
    // stop editting
    private void setEditingValue() {
        JTable table = getListTable();
        if (table.isEditing()) {
            table.getCellEditor().stopCellEditing();
        }
    }

    /**
     * 
     * @param index
     * @return RotationSetDDComplexValue
     */
    public Object getElementAt(int index) {
        return (RotationSetDDComplexValue) getModelInterface().getDataAt(index);
    }

    public int getModelSize() {
        return getModelInterface().getRowCount();
    }

    public void addListSelectionListener(ListSelectionListener listen) {
        table.getSelectionModel().addListSelectionListener(listen);
    }

    public void removeAllElements() {
        tableModel.removeAllElements();
    }

    public void addElement(Object objectToAdd) {
        RotationSetDDComplexValue complex = (RotationSetDDComplexValue) objectToAdd;
        complex.getRotationSetDDBasicValue().setOrdering(new Integer(getModelSize() + 1));
        tableModel.addElement(complex);
    }

    private XHIBITTableModelInterface getModelInterface() {
        return (XHIBITTableModelInterface) table.getModel();
    }

    private void resetOrder(int startRow) {
        for (int i = startRow; i < getModelSize(); i++) {
            RotationSetDDComplexValue complex = (RotationSetDDComplexValue) getModelInterface().getDataAt(i);
            complex.getRotationSetDDBasicValue().setOrdering(new Integer(i + 1));
        }
    }

    private XTable getListTable() {
        if (table == null) {
            XTable temp = XTableFactory.getInstance().createDefaultTable(tableModel);
            temp.removeColumn(temp.getColumnModel().getColumn(0));
            temp.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            temp.getSelectionModel().addListSelectionListener(new ListListener());
            temp.initColumnSizes(tableModel.getLongValues(), (int) dim.getWidth());
            XNumericLimitedLengthCellEditor cellEditor = new XNumericLimitedLengthCellEditor(9);
            RightJustifyTextRenderer cellRenderer = new RightJustifyTextRenderer();
            TableColumn delayCol = temp.getColumnModel().getColumn(1);
            delayCol.setCellEditor(cellEditor);
            delayCol.setCellRenderer(cellRenderer);
            table = temp;
        }
        return table;
    }

    private void sortByOrder() {
        tableModel.sort();
    }

    XAction upAction;

    XAction downAction;

    private XAction getUpAction() {
        if (upAction == null) {
            upAction = new UpAction();
        }
        return upAction;
    }

    private XAction getDownAction() {
        if (downAction == null) {
            downAction = new DownAction();
        }
        return downAction;
    }

    private void stepUpdateViewState() {
        if (getListTable().getSelectedRowCount() == 1) {
            if (getListTable().getSelectedRow() > 0) {
                getUpAction().setEnabled(true);
            } else {
                getUpAction().setEnabled(false);
            }
            if (getListTable().getSelectedRow() < getListTable().getModel().getRowCount() - 1) {
                getDownAction().setEnabled(true);
            } else {
                getDownAction().setEnabled(false);
            }
        } else {
            getUpAction().setEnabled(false);
            getDownAction().setEnabled(false);
        }
    }

    private void moveSelection(boolean up) {
        int currRow = getListTable().getSelectedRow();
        int moveToRow;
        if (up) {
            moveToRow = currRow - 1;
        } else {
            moveToRow = currRow + 1;
        }
        Integer currRowOrder = getDataAt(currRow).getRotationSetDDBasicValue().getOrdering();
        Integer moveToRowOrder = getDataAt(moveToRow).getRotationSetDDBasicValue().getOrdering();
        getDataAt(currRow).getRotationSetDDBasicValue().setOrdering(moveToRowOrder);
        getDataAt(moveToRow).getRotationSetDDBasicValue().setOrdering(currRowOrder);

        // Refresh View
        sortByOrder();
        // stepUpdateViewState();
        getListTable().setRowSelectionInterval(moveToRow, moveToRow);
    }

    private RotationSetDDComplexValue getDataAt(int row) {
        return (RotationSetDDComplexValue) ((XHIBITTableModelInterface) getListTable().getModel()).getDataAt(row);
    }

    class ListListener implements ListSelectionListener {
        public void valueChanged(ListSelectionEvent lse) {
            stepUpdateViewState();
        }
    }

    class UpAction extends XAction {
        public UpAction() {
            setIcon(XHIBITConstant.imageRoot + "moveup.gif");
            setShortDescription("Move Up");
        }

        public void xActionPerformed(ActionEvent ae) {
            moveSelection(true);
        }
    }

    class DownAction extends XAction {
        public DownAction() {
            setIcon(XHIBITConstant.imageRoot + "movedown.gif");
            setShortDescription("Move Down");
        }

        public void xActionPerformed(ActionEvent ae) {
            moveSelection(false);
        }
    }
}