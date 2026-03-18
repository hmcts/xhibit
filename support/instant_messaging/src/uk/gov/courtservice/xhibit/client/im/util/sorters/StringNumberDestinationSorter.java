package uk.gov.courtservice.xhibit.client.im.util.sorters;

// jdk
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: StringNumberDestinationSorter
 * </p>
 * <p>
 * Description: Sorter to sort destinations which are in the form <string><number>.
 * The destinations will be sorted alphabetically by the string, then
 * numerically by the number. E.g:
 * </p>
 * <p>
 * Court 1, Court 12, Court 3 , Court 33, court 4
 * </p>
 * Would be sorted:
 * <p>
 * Court 1, Court 3, Court 4, Court 12, Court 33
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: StringNumberDestinationSorter.java,v 1.1 2003/07/23 13:50:23
 *          rzgbyh Exp $
 */

public class StringNumberDestinationSorter implements DestinationSorter {
    private static Logger log = CSServices.getLogger(StringNumberDestinationSorter.class);

    Integer court;

    public StringNumberDestinationSorter() {
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
                s1 = (String) n1.getUserObject().toString().trim();
                s2 = (String) n2.getUserObject().toString().trim();

                int s1NumberIndex = s1.length();
                int s2NumberIndex = s2.length();

                while (s1NumberIndex > 0 && Character.isDigit(s1.charAt(s1NumberIndex - 1))) { // check
                    // the
                    // character
                    // before
                    // is a
                    // digit,
                    // if
                    // so
                    // take
                    // one
                    // off
                    // the
                    // index
                    s1NumberIndex--;
                }
                while (s2NumberIndex > 0 && Character.isDigit(s2.charAt(s2NumberIndex - 1))) {
                    s2NumberIndex--;
                }

                String s1StringPart = s1.substring(0, s1NumberIndex);
                String s2StringPart = s2.substring(0, s2NumberIndex);

                if (s1StringPart.equals(s2StringPart)) { // then we check
                    // the numbers
                    if (s1.substring(s1NumberIndex).length() == 0 && s2.substring(s2NumberIndex).length() == 0) {
                        return 0;
                    }
                    if (s1.substring(s1NumberIndex).length() == 0) {
                        return 1;
                    }
                    if (s2.substring(s2NumberIndex).length() == 0) {
                        return -1;
                    }
                    int s1NumberPart = Integer.parseInt(s1.substring(s1NumberIndex));
                    int s2NumberPart = Integer.parseInt(s2.substring(s2NumberIndex));
                    return s1NumberPart - s2NumberPart;
                } else {
                    return s1StringPart.compareTo(s2StringPart);
                }
            }
        };

        return TreeSorter.sortTree(curNode, comp);
    }
}