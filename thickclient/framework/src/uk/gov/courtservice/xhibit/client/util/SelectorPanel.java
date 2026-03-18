package uk.gov.courtservice.xhibit.client.util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

import uk.gov.courtservice.framework.exception.CSRecoverableException;

/**
 * <p>
 * Title: XHIBIT 2
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
 * @author Bal Bhamra
 * @version 1.0
 * 
 */

public class SelectorPanel extends JPanel {
    // Button Size
    private final int BUTTON_HEIGHT = 25;

    private final int BUTTON_WIDTH = 72;

    private Insets defaultInset = XHIBITConstant.nonContainerInsets;

    private GridBagLayout gridBagLayout1 = new GridBagLayout();

    // List of available items
    private JList allList;

    // List of selected items
    private JList targetList;

    private JScrollPane allScrollPane;

    private JScrollPane targetScrollPane;

    private JButton addAllBtn;

    private JButton addBtn;

    private JButton removeBtn;

    private JButton removeAllBtn;

    private DefaultListModel allListModel;

    private DefaultListModel targetListModel;

    private JLabel allLabel;

    private JLabel targetLabel;

    private String allDescription;

    private String targetDescription;

    public XPanel parentPanel;

    public SelectorPanel() {

    }

    public SelectorPanel(XPanel parentPanel, DefaultListModel allListModel, DefaultListModel targetListModel) {
        try {
            this.parentPanel = parentPanel;
            setAllListModel(allListModel);
            setTargetListModel(targetListModel);
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(gridBagLayout1);
        this.setPreferredSize(new Dimension(500, 200));

        this.add(getAllLabel(), new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getTargetLabel(), new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getAllScrollPane(), new GridBagConstraints(1, 1, 1, 4, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInset, 0, 0));
        this.add(getAddAllBtn(), new GridBagConstraints(2, 1, 1, 1, 0.0, 0.25, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getAddBtn(), new GridBagConstraints(2, 2, 1, 1, 0.0, 0.25, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getRemoveBtn(), new GridBagConstraints(2, 3, 1, 1, 0.0, 0.25, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getRemoveAllBtn(), new GridBagConstraints(2, 4, 1, 1, 0.0, 0.25, GridBagConstraints.CENTER,
                GridBagConstraints.NONE, defaultInset, 0, 0));
        this.add(getTargetScrollPane(), new GridBagConstraints(3, 1, 1, 5, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInset, 0, 0));
    }

    public void setButtonEnable() {
        if (targetListModel.getSize() <= 0) {
            removeBtn.setEnabled(false);
            removeAllBtn.setEnabled(false);
        } else {
            removeBtn.setEnabled(targetList.getSelectedIndex() > -1);
            removeAllBtn.setEnabled(true);
        }

        if (allListModel.getSize() <= 0) {
            addBtn.setEnabled(false);
            addAllBtn.setEnabled(false);
        } else {
            addBtn.setEnabled(allList.getSelectedIndex() > -1);
            addAllBtn.setEnabled(true);
        }

        try {
            if (parentPanel != null) {
                parentPanel.stepUpdateViewState();
            }
        } catch (CSRecoverableException re) {
            XHIBITConstant.debug("Exception thrown in SelectorPanel's  public void setButtonEnable()");
            XHIBITConstant.handleError(re);
        }
    }

    class LeftAllAction extends XAction {
        public LeftAllAction() {
            setIcon(XHIBITConstant.imageRoot + "leftAll.gif");
            putValue(Action.SHORT_DESCRIPTION, "Remove All");
        }

        public void xActionPerformed(ActionEvent ae) {
            int size = targetList.getModel().getSize();
            for (int i = 0; i < size; i++) {
                allListModel.addElement(targetListModel.getElementAt(i));
            }
            targetListModel.removeAllElements();
            setButtonEnable();
        }
    }

    class LeftAction extends XAction {
        public LeftAction() {
            setIcon(XHIBITConstant.imageRoot + "left.gif");
            putValue(Action.SHORT_DESCRIPTION, "Remove");
        }

        public void xActionPerformed(ActionEvent ae) {
            int selectedItem;

            int[] selectedItems = targetList.getSelectedIndices();
            for (int i = selectedItems.length - 1; i >= 0; i--) {
                selectedItem = selectedItems[i];
                Object selectedObject = targetListModel.getElementAt(selectedItem);
                allListModel.addElement(selectedObject);
                targetListModel.remove(selectedItem);
            }
            setButtonEnable();
        }
    }

    class RightAction extends XAction {
        public RightAction() {
            setIcon(XHIBITConstant.imageRoot + "right.gif");
            putValue(Action.SHORT_DESCRIPTION, "Add");
        }

        public void xActionPerformed(ActionEvent ae) {
            int selectedItem;
            int[] selectedItems = allList.getSelectedIndices();
            for (int i = selectedItems.length - 1; i >= 0; i--) {
                selectedItem = selectedItems[i];
                Object selectedObject = allListModel.getElementAt(selectedItem);
                targetListModel.addElement(selectedObject);
                allListModel.remove(selectedItem);
            }
            setButtonEnable();
        }
    }

    class RightAllAction extends XAction {
        public RightAllAction() {
            setIcon(XHIBITConstant.imageRoot + "rightAll.gif");
            putValue(Action.SHORT_DESCRIPTION, "Add All");
        }

        public void xActionPerformed(ActionEvent ae) {
            int size = allList.getModel().getSize();
            for (int i = 0; i < size; i++) {
                targetListModel.addElement(allListModel.getElementAt(i));
            }
            allListModel.removeAllElements();
            setButtonEnable();
        }
    }

    void allList_mouseClicked(MouseEvent e) {
        if (allList.getSelectedIndex() > -1) {
            addBtn.setEnabled(true);
        }
    }

    void targetList_mouseClicked(MouseEvent e) {
        if (targetList.getSelectedIndex() > -1) {
            removeBtn.setEnabled(true);
        }
    }

    void targetList_ListEvent() {
        if (targetList.getSelectedIndex() > -1) {
            removeBtn.setEnabled(true);
        }
    }

    void allList_ListEvent() {
        if (allList.getSelectedIndex() > -1) {
            addBtn.setEnabled(true);
        }
    }

    public void setAllListRenderer(ListCellRenderer cellRenderer) {
        getAllList().setCellRenderer(cellRenderer);
    }

    public void setTargetListRenderer(ListCellRenderer cellRenderer) {
        getTargetList().setCellRenderer(cellRenderer);
    }

    public void setAllDescription(String allDescription) {
        this.allDescription = allDescription;
        allLabel.setText(allDescription);
    }

    private String getAllDescription() {
        if (allDescription == null) {
            allDescription = "";
        }
        return allDescription;
    }

    public void setTargetDescription(String targetDescription) {
        this.targetDescription = targetDescription;
        targetLabel.setText(targetDescription);
    }

    private String getTargetDescription() {
        if (targetDescription == null) {
            targetDescription = "";
        }
        return targetDescription;
    }

    private JLabel getAllLabel() {
        if (allLabel == null) {
            allLabel = new JLabel(getAllDescription());
        }
        return allLabel;
    }

    private JLabel getTargetLabel() {
        if (targetLabel == null) {
            targetLabel = new JLabel(getTargetDescription());
        }
        return targetLabel;
    }

    public void setAllListModel(DefaultListModel lm) {
        allListModel = lm;
    }

    public DefaultListModel getAllListModel() {
        return allListModel;
    }

    public void setTargetListModel(DefaultListModel lm) {
        targetListModel = lm;
    }

    public DefaultListModel getTargetListModel() {
        return targetListModel;
    }

    public JList getAllList() {
        if (allList == null) {
            allList = new JList(getAllListModel());

            allList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    allList_ListEvent();
                }
            });
        }
        return allList;
    }

    public JList getTargetList() {
        if (targetList == null) {
            targetList = new JList(getTargetListModel());

            targetList.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
                public void valueChanged(javax.swing.event.ListSelectionEvent e) {
                    targetList_ListEvent();
                }
            });
        }

        return targetList;
    }

    private JScrollPane getAllScrollPane() {
        if (allScrollPane == null) {
            allScrollPane = new JScrollPane(getAllList());
            allScrollPane.setMinimumSize(new Dimension(25, 21));
            allScrollPane.setPreferredSize(new Dimension(25, 21));
        }
        return allScrollPane;
    }

    private JScrollPane getTargetScrollPane() {
        if (targetScrollPane == null) {
            targetScrollPane = new JScrollPane(getTargetList());
            targetScrollPane.setMinimumSize(new Dimension(25, 21));
            targetScrollPane.setPreferredSize(new Dimension(25, 21));
        }
        return targetScrollPane;
    }

    private JButton getAddBtn() {
        if (addBtn == null) {
            addBtn = new JButton();
            addBtn.setAction(new RightAction());
            addBtn.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            addBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        }
        return addBtn;
    }

    private JButton getAddAllBtn() {
        if (addAllBtn == null) {
            addAllBtn = new JButton();
            addAllBtn.setAction(new RightAllAction());
            addAllBtn.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            addAllBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        }
        return addAllBtn;
    }

    private JButton getRemoveAllBtn() {
        if (removeAllBtn == null) {
            removeAllBtn = new JButton();
            removeAllBtn.setAction(new LeftAllAction());
            addBtn.setEnabled(false);
            removeAllBtn.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            removeAllBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        }
        return removeAllBtn;
    }

    private JButton getRemoveBtn() {
        if (removeBtn == null) {
            removeBtn = new JButton();
            removeBtn.setAction(new LeftAction());
            removeBtn.setMinimumSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            removeBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        }
        return removeBtn;
    }
}
