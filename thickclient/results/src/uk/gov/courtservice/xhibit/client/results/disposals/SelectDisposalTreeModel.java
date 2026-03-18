package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.client.results.ResultsReferenceFactory;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.tree.AbstractXTreeModel;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;

/**
 * <p>
 * Title: SelectDisposalTreeModel
 * </p>
 * <p>
 * Description: The tree model for selecting disposals
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.6 $
 */

public class SelectDisposalTreeModel extends AbstractXTreeModel {
    private final static Icon DISPOSAL_ICON = DisposalUtil.getIcon("icon/disposal/disposal.gif");

    private final static Icon GROUP_CLOSED_ICON = DisposalUtil.getIcon("icon/disposal/group_closed.gif");

    private final static Icon GROUP_OPEN_ICON = DisposalUtil.getIcon("icon/disposal/group_open.gif");

    private final DisposalMenuReferenceValue root;

    private JMenuItem defaultDisposalMenuItem;

    /**
     * Construct a new model arround the record sheet disposal root
     */
    public SelectDisposalTreeModel() throws ResultsControllerException {
        root = ResultsReferenceFactory.getInstance().getRecordSheetDisposalMenuRoot();
    }

    /**
     * Set the default menut item
     */
    public void setDefaultDisposalAction(XAction defaultDisposalAction) {
        defaultDisposalMenuItem = new JMenuItem(defaultDisposalAction);
        defaultDisposalMenuItem.setFont(defaultDisposalMenuItem.getFont().deriveFont(Font.BOLD));
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Object getRoot() {
        return root;
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Object getParent(Object node) {
        return ((DisposalMenuReferenceValue) node).getParentValue();
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public int getIndexOfChild(Object parent, Object child) {
        return ((DisposalMenuReferenceValue) parent).getIndexOfChildValue((DisposalMenuReferenceValue) child);
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Object getChild(Object parent, int index) {
        return ((DisposalMenuReferenceValue) parent).getChildValue(index);
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public int getChildCount(Object parent) {
        return ((DisposalMenuReferenceValue) parent).getChildValueCount();
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public boolean isLeaf(Object node) {
        return ((DisposalMenuReferenceValue) node).isDisposal();
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public void valueChanged(Object node, Object value) {
        throw new UnsupportedOperationException("node: " + node + " value: " + value);
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Icon getOpenIcon(Object node) {
        return GROUP_OPEN_ICON;
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Icon getClosedIcon(Object node) {
        return GROUP_CLOSED_ICON;
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public Icon getLeafIcon(Object node) {
        return DISPOSAL_ICON;
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public String getTooltipText(Object node) {
        return ((DisposalMenuReferenceValue) node).toString();
    }

    /**
     * AbstractXTreeModel Implementation
     */
    public JMenuItem getPopupMenuDefault(Object node) {
        if (((DisposalMenuReferenceValue) node).isDisposal()) {
            return defaultDisposalMenuItem;
        } else {
            return null;
        }
    }

}
