package uk.gov.courtservice.xhibit.client.listdistribution;

// jdk
import java.util.Collections;
import java.util.Enumeration;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.JComboBox;

import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: ListDistributionUtilities
 * </p>
 * <p>
 * Description: Provides utility methods common to the List Distribution
 * functionality.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Sarah Tong
 * @version $Id: ListDistributionUtilities.java,v 1.2 2003/08/15 10:01:33 bzw8gp
 *          Exp $
 */

public class ListDistributionUtilities {
    public ListDistributionUtilities() {
    }

    /**
     * <p>
     * Reads values from the ListDistributionResources properties file and
     * builds the contents for a combo box as a <code>Vector</code> of
     * <code>ComboContentsValue</code> objects. The code is the part of the
     * key after the keyStart <code>String</code> parameter, the value is the
     * value form the properties file. E.g.
     * </p>
     * <p>
     * keyStart = 'lddf_'<br>
     * properties file entry: lddf_htm=HTML<br>
     * Would result in a code of 'htm' and value 'HTML'
     * </p>
     * 
     * @param keyStart
     *            The <code>String</code> which identifies the keys in the
     *            properties file to use to build the combo box contents.
     * @return
     */
    public static Vector getComboBoxContents(String keyStart) {
        int length = keyStart.length();

        ResourceBundle manageListRsc = XHIBITConstant.getResourceBundle(XhibitBundles.ManageLists);
        Vector contents = new Vector();
        Enumeration manageListEnum = manageListRsc.getKeys();
        while (manageListEnum.hasMoreElements()) {
            String manageListKey = (String) manageListEnum.nextElement();
            if (manageListKey.startsWith(keyStart)) {
                ComboContentsValue dfv = new ComboContentsValue();
                dfv.setValue(manageListRsc.getString(manageListKey));
                dfv.setCode(manageListKey.substring(length));
                contents.add(dfv);
            }
        }
        // Sort using default comparator
        Collections.sort(contents);
        return contents;
    }

    /**
     * Sets the selected item for a combo box built using
     * <code>ComboContentsValue</code> objects. The object with a code value
     * matching the String passed will be set as the selected item.
     * 
     * @param comboBox
     *            The combo box to work on
     * @param code
     *            The code of the item to set to selected
     */
    public static void setSelectedItemByCode(JComboBox comboBox, String code) {
        int length = comboBox.getItemCount();

        for (int i = 0; i < length; i++) {
            if (((ComboContentsValue) comboBox.getItemAt(i)).getCode().equals(code)) {
                comboBox.setSelectedIndex(i);
            }
        }
    }
}