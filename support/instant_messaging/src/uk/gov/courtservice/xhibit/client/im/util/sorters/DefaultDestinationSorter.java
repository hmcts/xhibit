package uk.gov.courtservice.xhibit.client.im.util.sorters;

// jdk
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DefaultDestinationSorter
 * </p>
 * <p>
 * Description: The sorter to use to sort the courtrooms when no specific sorter
 * is specified in the properties file. Will sort the court rooms
 * alphabetically.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: DefaultDestinationSorter.java,v 1.1 2003/07/23 13:50:23 rzgbyh
 *          Exp $
 */

public class DefaultDestinationSorter implements DestinationSorter {
    private static Logger log = CSServices.getLogger(DefaultDestinationSorter.class);

    Integer court;

    public DefaultDestinationSorter() {
    }

    public void setCourt(Integer court) {
        this.court = court;
    }

    public DefaultMutableTreeNode sortDestinations(TreeNode curNode) {
        log.debug("sortDestinations() start with curNode " + curNode.toString());

        // set up comparator
        Comparator comp = new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((DefaultMutableTreeNode) o1, (DefaultMutableTreeNode) o2);
            }

            public int compare(DefaultMutableTreeNode n1, DefaultMutableTreeNode n2) {
                String s1, s2;
                s1 = (String) n1.getUserObject().toString();
                s2 = (String) n2.getUserObject().toString();
                return (s1.compareTo(s2));
            }
        };

        return TreeSorter.sortTree(curNode, comp);
    }
}