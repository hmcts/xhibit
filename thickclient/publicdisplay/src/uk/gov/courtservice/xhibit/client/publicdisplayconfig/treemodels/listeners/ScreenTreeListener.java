package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.listeners;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.ScreenInformationPanel;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.DisplayAdapter;
import uk.gov.courtservice.xhibit.client.util.XPanel;

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
 * @version $Id: ScreenTreeListener.java,v 1.4 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class ScreenTreeListener implements TreeSelectionListener {
    private static final Logger log = CSServices.getLogger(ScreenTreeListener.class);

    private final XPanel _parent;

    private final ScreenInformationPanel _panel;

    public ScreenTreeListener(XPanel parent, ScreenInformationPanel panel) {
        _parent = parent;
        _panel = panel;
    }

    public void valueChanged(TreeSelectionEvent e) {
        try {
            _parent.stepUpdateViewState();
        } catch (CSRecoverableException ex) {
            log.error("This should not happen !");
        }

        JTree tree = (JTree) e.getSource();
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
        if (node.getUserObject() instanceof DisplayAdapter) {
            _panel.setNewDisplay(((DisplayAdapter) node.getUserObject()).getBasicValue().getPrimaryKey());
        } else {
            _panel.setNewDisplay(null);
        }
    }
}