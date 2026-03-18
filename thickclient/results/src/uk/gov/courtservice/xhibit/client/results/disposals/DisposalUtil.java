package uk.gov.courtservice.xhibit.client.results.disposals;

import java.awt.Color;
import java.awt.Component;
import java.net.URL;
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import uk.gov.courtservice.xhibit.client.results.ResultsUtil;
import uk.gov.courtservice.xhibit.client.util.XButton;
import uk.gov.courtservice.xhibit.client.util.XCheckBox;
import uk.gov.courtservice.xhibit.client.util.XRadioButton;
import uk.gov.courtservice.xhibit.client.util.XDialog;
import uk.gov.courtservice.xhibit.client.util.XFrame;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: DisposalUtil
 * </p>
 * <p>
 * Description: Utility methods used by disposals.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Revision: 1.17 $
 */
public class DisposalUtil {
    /**
     * Cache the disposal resource bundle
     */
    private static final ResourceBundle RESOURCES = XHIBITConstant.getResourceBundle(XhibitBundles.Disposals);

    /**
     * Stop construction of utility (all static) class
     */
    private DisposalUtil() {
        // Change permisions of default constructor
    }
    
    /**
     * Returns a Resource
     * 
     * @param s
     * @return
     */
    public static String getResourceString(String s) {
        try {
        	
            return RESOURCES.getString(s);
        } catch (MissingResourceException ex) {
            return s;
        }
    }

    /**
     * Get the specified resource
     */
    public static String getResource(String name) {
        return XHIBITConstant.getResource(RESOURCES, name);
    }

    /**
     * Get the specified resource
     */
    public static String getResource(String name, Object parameter) {
        return getResource(name, new Object[] { parameter });
    }

    /**
     * Get the specified resource
     */
    public static String getResource(String name, Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return getResource(name);
        } else {
            return MessageFormat.format(getResource(name), parameters);
        }
    }

    /**
     * Get the tooltip resource, return null if not found or trimed is 0 length
     * (stops empty tooltip popup)
     */
    public static String getToolTipText(String name) {
        String tooltip = getResource(name);
        if (tooltip == null) {
            return null;
        }
        tooltip = tooltip.trim();
        if (tooltip.length() == 0) {
            return null;
        }
        return tooltip;
    }

    /**
     * Get the tooltip resource, return null if not found or trimed is 0 length
     * (stops empty tooltip popup)
     */
    public static String getToolTipText(String name, Object obj1) {
        return getToolTipText(name, new Object[] { obj1 });
    }

    public static String getToolTipText(String name, Object obj1, Object obj2) {
        return getToolTipText(name, new Object[] { obj1, obj2 });
    }

    public static String getToolTipText(String name, Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return getResource(name);
        } else {
            String resource = getResource(name);
            if (resource == null) {
                return null;
            }
            return MessageFormat.format(resource, parameters);
        }
    }
    
    /**
     * Return a new RadioButton
     */
    public static JRadioButton createRadioButton(String textKey){
        return createRadioButton(textKey, false);
    }

    /**
     * Return a new RadioButton
     */
    public static JRadioButton createRadioButton(String textKey, boolean selected){
        return new XRadioButton(getResource(textKey), selected);
    }
    
    /**
     * Return a new checkbox
     */
    public static JCheckBox createCheckBox(String textKey) {
        return createCheckBox(textKey, false);
    }

    /**
     * Return a new checkbox
     */
    public static JCheckBox createCheckBox(String textKey, boolean selected) {
        return new XCheckBox(getResource(textKey), selected);
    }

    /**
     * Return an empty text field
     */
    public static JTextField createTextField() {
        return new JTextField();
    }

    /**
     * Return an Button which wont change size
     */
    public static JButton createFixedButton(String textKey, String iconName) {
        XButton button = new XButton(getResource(textKey), getIcon(iconName));
        button.fixSize();
        return button;
    }

    /**
     * Return a label for displaying a title
     */
    public static JLabel createTitleLabel(String textKey) {
        JLabel label = new JLabel(getResource(textKey));
        return label;
    }

    /**
     * Return a label for displaying data
     */
    public static JLabel createDataLabel() {
        return createDataLabel(null);
    }

    /**
     * Return a label for displaying data
     */
    public static JLabel createDataLabel(String data) {
        JLabel label = new JLabel(data);
        label.setForeground(new Color(0, 0, 99));
        return label;
    }

    /**
     * Show a message box to alert the user
     */
    public static void alert(Component child, String titleKey, String textKey) {
        JOptionPane.showMessageDialog(child, getResource(textKey), getResource(titleKey),
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Find the parent XDialog, return null if it does not have one.
     */
    public static XDialog getParentXDialog(Component child) {
        for (Component component = child; component != null; component = component.getParent()) {
            if (component instanceof XDialog) {
                return (XDialog) component;
            }
        }
        return null;
    }

    /**
     * Find the parent XFrame, return null if it does not have one.
     */
    public static XFrame getParentXFrame(Component child) {
        for (Component component = child; component != null; component = component.getParent()) {
            if (component instanceof XFrame) {
                return (XFrame) component;
            }
        }
        return null;
    }

    /**
     * Utility method to load the named icon from the class path
     */
    public static Icon getIcon(String iconName) {
        URL iconUrl = DisposalUtil.class.getClassLoader().getResource(iconName);
        if (iconUrl != null) {
            return new ImageIcon(iconUrl);
        }
        return null;
    }

    /**
     * Return the index of criteria in data ignoring case differences or -1 if
     * not found
     */
    public static int indexOfIgnoreCase(String data, String criteria) {
        return ResultsUtil.indexOfIgnoreCase(data, criteria);
    }

    /**
     * Return the given score if the if the values are equal
     */
    public static boolean equals(Object value1, Object value2) {
        return ResultsUtil.equals(value1, value2);
    }

    /**
     * Return an array of lines split from the source string
     */

    public static String[] getLines(String input) {
        return ResultsUtil.getLines(input);
    }

    /**
     * Return the lines formated as text
     */
    public static String getText(String[] lines) {
        return ResultsUtil.getText(lines);
    }

}
