package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels;

import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.entities.xhb_display.XhbDisplayBasicValue;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.DisplayAdapter;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.DisplayLocationAdapter;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.DisplaySiteAdapter;
import uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters.NodeComparator;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.CourtSitePDComplexValue;
import uk.gov.courtservice.xhibit.common.publicdisplay.vos.publicdisplay.DisplayLocationComplexValue;

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
 * @version $Id: DisplayTreeModel.java,v 1.7 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class DisplayTreeModel {
    private final DefaultTreeModel treeModel;

    private DefaultMutableTreeNode root;

    public DisplayTreeModel(CourtSitePDComplexValue[] modelData) throws CSRecoverableException {
        String courtName = XhibitSingleton.getInstance().getCourtBasicValue().getDisplayName();
        root = new DefaultMutableTreeNode(courtName);
        treeModel = new DefaultTreeModel(root);
        loadTreeData(modelData);
    }

    public TreeModel getTreeModel() {
        return treeModel;
    }

    private void loadTreeData(CourtSitePDComplexValue[] modelData) {
        ArrayList arrSite = new ArrayList();
        for (int i = 0; i < modelData.length; i++) {
            CourtSitePDComplexValue site = modelData[i];
            DefaultMutableTreeNode siteNode = new DefaultMutableTreeNode(new DisplaySiteAdapter(site
                    .getCourtSiteBasicValue()));
            arrSite.add(siteNode);

            DisplayLocationComplexValue[] locations = site.getDisplayLocationComplexValue();

            ArrayList arrLocation = new ArrayList();
            for (int j = 0; j < locations.length; j++) {
                DisplayLocationComplexValue location = locations[j];
                DefaultMutableTreeNode locationNode = new DefaultMutableTreeNode(new DisplayLocationAdapter(location
                        .getDisplayLocationBasicValue()));
                arrLocation.add(locationNode);

                XhbDisplayBasicValue[] displays = location.getDisplayBasicValue();
                ArrayList arrDisplays = new ArrayList();
                for (int k = 0; k < displays.length; k++) {
                    XhbDisplayBasicValue display = displays[k];
                    arrDisplays.add(new DefaultMutableTreeNode(new DisplayAdapter(display)));
                }
                sortAndAddNodes(arrDisplays, locationNode);
            }
            sortAndAddNodes(arrLocation, siteNode);
        }
        sortAndAddNodes(arrSite, root);
    }

    private void sortAndAddNodes(ArrayList list, DefaultMutableTreeNode node) {
        Object[] nodes = list.toArray();
        Arrays.sort(nodes, NodeComparator.getInstance());

        for (int i = 0; i < nodes.length; i++) {
            DefaultMutableTreeNode item = (DefaultMutableTreeNode) nodes[i];
            node.add(item);
        }
    }
}