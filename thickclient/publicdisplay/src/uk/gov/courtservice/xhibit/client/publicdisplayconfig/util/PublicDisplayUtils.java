package uk.gov.courtservice.xhibit.client.publicdisplayconfig.util;

import java.awt.Dimension;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.helpers.ResourceBundleHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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
 * @version $Id: PublicDisplayUtils.java,v 1.10 2006/06/05 12:32:08 bzjrnl Exp $
 */
public class PublicDisplayUtils {
    public static final String PRE_DISPLAY = "pd.displaydescription.";

    public static final String PRE_DISPLAYDOCUMENT = "pd.displaydocument.";

    public static final String PRE_DISPLAY_LOCATION = "pd.displaylocation.";

    public static final String PRE_LOCALE = "pd.locale.";

    public static final String PRE_LABEL = "pd.label.";

    public static final String SORT_KEY_ROTATION_SET = "description";

    public static final String SORT_KEY_COURTROOM = "crestCourtRoomNo";

    private PublicDisplayUtils() {
        // prevent external instantiation...
    }

    /**
     * Indirection to get courtId. May be useful if in the future multi-court pd
     * maintenance is required.
     * 
     * @return Court id the user is logged into
     */
    public static Integer getCourtId() {
        return XhibitSingleton.getInstance().getCourtId();
    }

    /**
     * Indirection to get courtSiteId. May be useful if in the future
     * multi-court pd maintenance is required.
     * 
     * @return Court Site id the user is logged into
     */
    public static Integer getCourtSiteId() {
        return XhibitSingleton.getInstance().getCourtSiteId();
    }

    /**
     * Short cuts the call to the resource bundle helper. Looks up the property
     * in the PublicDisplayConfiguration resource bundle
     * 
     * @param key
     *            the key in the Resource bundle
     * @return value associated with key in PublicDisplayConfiguration resource
     *         bundle
     */
    public static String getResource(String key) {
        String value;
        try {
            value = getResourceBundle().getString(key);
        } catch (MissingResourceException ex) {
            String temp = key.substring(key.lastIndexOf('.') + 1);
            temp = temp.replace('_', ' ');
            char[] tempChars = temp.toCharArray();
            tempChars[0] = Character.toUpperCase(tempChars[0]);
            int i = temp.indexOf(' ', 0);
            while (i >= 0 && i < temp.length()) {
                if (i + 1 <= tempChars.length) {
                    tempChars[i + 1] = Character.toUpperCase(tempChars[i + 1]);
                    i = temp.indexOf(' ', i + 1);
                }
            }
            value = new String(tempChars);
        }
        return value;
    }

    private static ResourceBundle pdResourceBundle = null;

    private static ResourceBundle getResourceBundle() {
        if (pdResourceBundle == null) {
            pdResourceBundle = ResourceBundleHelper.getResourceBundle(XhibitBundles.PublicDisplayConfiguration);
        }
        return pdResourceBundle;
    }

    /**
     * Wraps a table in a JScrollPane and sets the minimum and preferred sizes.
     * 
     * @param comp
     *            The component to be scrolled
     * @param tableSize
     *            The minimum size (also the preferred). Can be null
     * @return Scroll Pane
     */
    public static JScrollPane getDefaultScrollPane(JComponent comp, Dimension tableSize) {
        if (comp == null)
            throw new IllegalArgumentException("Component can not be null");

        JScrollPane pane = new JScrollPane(comp);
        if (tableSize != null) {
            pane.setMinimumSize(tableSize);
            pane.setPreferredSize(tableSize);
        }
        return pane;
    }

    public static TitledBorder createBorder(String title) {
        return BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), title,
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION);
    }
}
