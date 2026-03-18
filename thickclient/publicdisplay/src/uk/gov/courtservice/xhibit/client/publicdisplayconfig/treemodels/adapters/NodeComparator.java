package uk.gov.courtservice.xhibit.client.publicdisplayconfig.treemodels.adapters;

import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;

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
 * @version $Id: NodeComparator.java,v 1.4 2006/06/05 12:32:08 bzjrnl Exp $
 */

public class NodeComparator implements Comparator {
    private static final NodeComparator _instance = new NodeComparator();

    private NodeComparator() {
    }

    /**
     * Get a comparator to sort court rooms.
     * 
     * @return A class implementing Comparator able to sort by
     *         <code>XhbCourtSite</code>.
     */
    public static final Comparator getInstance() {
        return _instance;
    }

    public int compare(Object o1, Object o2) {
        AdapterSorterInterface sort1 = (AdapterSorterInterface) ((DefaultMutableTreeNode) o1).getUserObject();
        AdapterSorterInterface sort2 = (AdapterSorterInterface) ((DefaultMutableTreeNode) o2).getUserObject();
        return sort1.getSortKey().compareTo(sort2.getSortKey());
    }

    public boolean equals(Object obj) {
        return this == obj;
    }
}