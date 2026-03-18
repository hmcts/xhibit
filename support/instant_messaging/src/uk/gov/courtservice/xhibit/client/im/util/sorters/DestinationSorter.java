package uk.gov.courtservice.xhibit.client.im.util.sorters;

// jdk
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

/**
 * <p>
 * Title:
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
 * @author unascribed
 * @version 1.0
 */

public interface DestinationSorter {

    /**
     * Used at configuration time to record the court of the destinations which
     * are to be sorted.
     * 
     * @param court
     *            the court associated with the destinations.
     */
    public void setCourt(Integer court);

    public DefaultMutableTreeNode sortDestinations(TreeNode curNode);
}