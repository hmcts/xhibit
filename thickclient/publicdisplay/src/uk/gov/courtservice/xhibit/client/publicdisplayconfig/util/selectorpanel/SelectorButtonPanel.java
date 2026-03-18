package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util.selectorpanel;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XHIBITErrorHandler;
import uk.gov.courtservice.xhibit.client.util.XPanel;
import uk.gov.courtservice.xhibit.client.widgetfactory.JButtonFactory;

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
 * @version $Id: SelectorButtonPanel.java,v 1.4 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class SelectorButtonPanel extends JPanel {
    private static final Insets defaultInset = XHIBITConstant.nonContainerInsets;

    private final XPanel _parent;

    private final SelectorPanelModelInterface _fromModel;

    private final SelectorPanelModelInterface _toModel;

    private XAction addAction;

    private XAction addAllAction;

    private XAction removeAction;

    private XAction removeAllAction;

    public SelectorButtonPanel(XPanel parent, SelectorPanelModelInterface fromModel, SelectorPanelModelInterface toModel)
            throws CSRecoverableException {
        _parent = parent;
        _fromModel = fromModel;
        _toModel = toModel;
        jbInit();
        setButtonEnable();
    }

    private void jbInit() {
        this.setLayout(new GridBagLayout());
        this.setPreferredSize(new Dimension(50, 200));

        this.add(new JLabel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInset, 0, 0));

        this.add(JButtonFactory.getButton(getAddAllAction()), new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInset, 0, 5));
        this.add(JButtonFactory.getButton(getAddAction()), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInset, 0, 5));
        this.add(JButtonFactory.getButton(getRemoveAction()), new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInset, 0, 5));
        this.add(JButtonFactory.getButton(getRemoveAllAction()), new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, defaultInset, 0, 5));

        this.add(new JLabel(), new GridBagConstraints(0, 5, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, defaultInset, 0, 0));

        _fromModel.addListSelectionListener(new ListSelection(this));
        _toModel.addListSelectionListener(new ListSelection(this));
    }

    public void setButtonEnable() throws CSRecoverableException {
        if (getToModel().getModelSize() <= 0) {
            getRemoveAction().setEnabled(false);
            getRemoveAllAction().setEnabled(false);
        } else {
            getRemoveAction().setEnabled(getToModel().getSelectedIndeces().length > 0);
            getRemoveAllAction().setEnabled(true);
        }

        if (getFromModel().getModelSize() <= 0) {
            getAddAction().setEnabled(false);
            getAddAllAction().setEnabled(false);
        } else {
            getAddAction().setEnabled(getFromModel().getSelectedIndeces().length > 0);
            getAddAllAction().setEnabled(true);
        }

        if (_parent != null) {
            _parent.stepUpdateViewState();
        }
    }

    protected SelectorPanelModelInterface getFromModel() {
        return _fromModel;
    }

    protected SelectorPanelModelInterface getToModel() {
        return _toModel;
    }

    private XAction getAddAction() {
        if (addAction == null) {
            addAction = new AddAction(this);
        }
        return addAction;
    }

    private XAction getAddAllAction() {
        if (addAllAction == null) {
            addAllAction = new AddAllAction(this);
        }
        return addAllAction;
    }

    private XAction getRemoveAction() {
        if (removeAction == null) {
            removeAction = new RemoveAction(this);
        }
        return removeAction;
    }

    private XAction getRemoveAllAction() {
        if (removeAllAction == null) {
            removeAllAction = new RemoveAllAction(this);
        }
        return removeAllAction;
    }

    class RemoveAllAction extends XAction {
        private SelectorButtonPanel _parent;

        public RemoveAllAction(SelectorButtonPanel parent) {
            _parent = parent;
            setIcon(XHIBITConstant.imageRoot + "leftAll.gif");
            setShortDescription("Remove All");
        }

        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            int size = _parent.getToModel().getModelSize();
            for (int i = 0; i < size; i++) {
                _parent.getFromModel().addElement(_parent.getToModel().getElementAt(i));
            }
            _parent.getToModel().removeAllElements();
            _parent.setButtonEnable();
        }
    }

    class RemoveAction extends XAction {
        private SelectorButtonPanel _parent;

        public RemoveAction(SelectorButtonPanel parent) {
            _parent = parent;
            setIcon(XHIBITConstant.imageRoot + "left.gif");
            setShortDescription("Remove");
        }

        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            int selectedItem;

            int[] selectedItems = _parent.getToModel().getSelectedIndeces();
            for (int i = selectedItems.length - 1; i >= 0; i--) {
                selectedItem = selectedItems[i];
                Object selectedObject = _parent.getToModel().getElementAt(selectedItem);
                _parent.getFromModel().addElement(selectedObject);
                _parent.getToModel().removeElementAt(selectedItem);
            }
            _parent.setButtonEnable();
        }
    }

    class AddAction extends XAction {
        private SelectorButtonPanel _parent;

        public AddAction(SelectorButtonPanel parent) {
            _parent = parent;
            setIcon(XHIBITConstant.imageRoot + "right.gif");
            setShortDescription("Add");
        }

        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            int selectedItem;
            int[] selectedItems = _parent.getFromModel().getSelectedIndeces();
            for (int i = selectedItems.length - 1; i >= 0; i--) {
                selectedItem = selectedItems[i];
                Object selectedObject = _parent.getFromModel().getElementAt(selectedItem);
                _parent.getToModel().addElement(selectedObject);
                _parent.getFromModel().removeElementAt(selectedItem);
            }
            _parent.setButtonEnable();
        }
    }

    class AddAllAction extends XAction {
        private SelectorButtonPanel _parent;

        public AddAllAction(SelectorButtonPanel parent) {
            _parent = parent;
            setIcon(XHIBITConstant.imageRoot + "rightAll.gif");
            setShortDescription("Add All");
        }

        public void xActionPerformed(ActionEvent ae) throws CSRecoverableException {
            int size = _parent.getFromModel().getModelSize();
            for (int i = 0; i < size; i++) {
                _parent.getToModel().addElement(_parent.getFromModel().getElementAt(i));
            }
            _parent.getFromModel().removeAllElements();
            _parent.setButtonEnable();
        }
    }

    class ListSelection implements ListSelectionListener {
        private SelectorButtonPanel _parent;

        public ListSelection(SelectorButtonPanel parent) {
            _parent = parent;
        }

        public void valueChanged(ListSelectionEvent event) {
            try {
                _parent.setButtonEnable();
            } catch (CSRecoverableException ex) {
                XHIBITErrorHandler.handleError(ex);
            }
        }
    }
}