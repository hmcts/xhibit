package uk.gov.courtservice.xhibit.client.courtlog.directions;

import java.awt.Color;
import java.awt.Insets;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

import uk.gov.courtservice.xhibit.business.vos.entities.DefendantBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;

/**
 * <p>
 * Title: PDH Constants
 * </p>
 * <p>
 * Description: Constants and helpers for the plea and directions screen
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version 1.0 $Log: PDHConstants.java,v $
 * @version 1.0 Revision 1.19  2006/06/05 12:31:14  bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting tab fix
 * @version 1.0
 * @version 1.0 Revision 1.18 2006/05/31 14:25:19 bzjrnl
 * @version 1.0 Change: TI901
 * @version 1.0 Comment: Weblogic Upgrade - Standadise code formatting
 * @version 1.0 Revision 1.17 2006/04/12 13:51:33 bzjrnl Change: TI901 Comment:
 *          Build Rationalisation
 * 
 * Revision 1.16 2004/10/01 09:48:59 sz0t7n CR63 (merge from 6.4)
 * 
 * Revision 1.15.10.1 2004/09/29 22:34:46 sz0t7n CR63
 * 
 * Revision 1.15 2004/02/18 16:06:41 rzgbyh Merge with End_Hearing_Changes
 * 
 * Revision 1.14.42.2 2004/02/05 14:50:38 szfnvt End Hearing - Added Jdoc for
 * public methods (excluding life cycle) and class.
 * 
 * Revision 1.14.42.1 2004/01/27 18:55:30 szfnvt End Hearing - P&D Changes.
 * 
 * Revision 1.14 2003/10/09 13:04:08 szn20z PRE00093 - Added method to build a
 * full name similar in format to the way PersonValue.fullName is populated
 * 
 * Revision 1.13 2003/09/03 16:26:51 sz0t7n bug 53988 & 54063 - editting &
 * deleting directions
 * 
 * Revision 1.12 2003/08/15 09:50:04 bzw8gp Jon Powell
 * 
 * organise imports (remove unused) unused imports cause misleading dependencies
 * 
 * Revision 1.11 2003/07/25 06:36:09 rz3jq5 Added element of Sarah's fix that
 * addressed the empty string issue.
 * 
 * Revision 1.10 2003/07/24 13:59:50 rzgbyh Rolled back to defendant name fix
 * which avoids spurious spaces
 * 
 * Revision 1.9 2003/06/12 14:09:26 rzgbyh Bug fix 53162 - modified
 * buildDefendantName() method to ignore any parts of the name which are null.
 * 
 * Revision 1.8 2003/06/03 12:59:41 rz3jq5 Fixed defendant name presentation to
 * remove spurious spaces.
 * 
 * Revision 1.7 2003/04/30 10:42:50 nz5zpz addressing 52595
 * 
 * Revision 1.6 2003/04/27 15:10:30 nz5zpz fixing X3.0-52595 introduced
 * internalDebug and changed the getCB() method to store the checkboxes created
 * against the DefendantOnCase ID (not the defendant ID) as the DefendantsPanel
 * expects this when updating an Directions by Case event.
 * 
 */
public class PDHConstants {
    public static final Color DISABLED_COLOR = UIManager.getColor("control");

    public static final Color TEXT_AREA_BACKGROUND = UIManager.getColor("TextArea.background");

    public static boolean internalDebug = false;

    public static final Insets PDH_INSETS = new Insets(1, 2, 1, 2);

    // Event Types
    /** Event 40703 */
    public static final Integer DEF_IDENTIFICATION = new Integer(40704);

    /** Event 40704 */
    public static final Integer DEF_ARRAIGNMENT = new Integer(40705);

    /** Event 40705 */
    public static final Integer DEF_BAIL = new Integer(40706);

    /** Event 40706 */
    public static final Integer DEF_CERTATTENDANCE = new Integer(40707);

    /** Event 40707 */
    public static final Integer DEF_FORMB = new Integer(40708);

    /** Event 30200 */
    public static final Integer DEF_LONG_ADJOURNMENT = new Integer(30200);

    /** Event 40710 */
    public static final Integer CASE_PDFORM = new Integer(40710);

    /** Event 40711 */
    public static final Integer CASE_TRIALTIME = new Integer(40711);

    /** Event 40712 */
    public static final Integer CASE_DIRECTIONS = new Integer(40712);

    /** Event 40715 */
    public static final Integer FREETEXT = new Integer(21300);

    /** XMLCODE */
    public static final String xmlCode = "XMLCODE";

    /** DBCODE */
    public static final String dbCode = "DBCODE";

    /** ID */
    public static final String id = "ID";

    /** BASICVALUE */
    public static final String basicValue = "BASICVALUE";

    // If you don't need someone to instantiate a class of static methods,
    // just
    // make the constructor private.
    private PDHConstants() {
    }

    /**
     * Returns a boolean value indicating if event argument passed in is a
     * directions for case event.
     * 
     * @param eventType
     * @return boolean
     */
    public static boolean isDirectionForCaseEvent(Integer eventType) {
        if (eventType.equals(CASE_DIRECTIONS) || eventType.equals(CASE_PDFORM) || eventType.equals(CASE_TRIALTIME)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns a boolean value indicating if event argument passed in is a
     * directions for defendant event.
     * 
     * @param eventType
     * @return
     */
    public static boolean isDirectionForDefendantEvent(Integer eventType) {
        if (eventType.equals(DEF_ARRAIGNMENT) || eventType.equals(DEF_BAIL) || eventType.equals(DEF_CERTATTENDANCE)
                || eventType.equals(DEF_FORMB) || eventType.equals(DEF_IDENTIFICATION)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Pass back the name of the schema, which is used for retrieving the
     * restriction values for the radio buttons
     * 
     * @param caseType
     * @return
     */
    public static String getSchema(Integer caseType) {
        return caseType.toString() + ".xsd";
    }

    /**
     * Utility method to construct the name of a defendant from a
     * DefendantBasicValue
     * 
     * @param dbv
     *            The DefendantBasicValue to create the name from.
     * @return A well formatted defendant name as a string.
     */
    public static String buildDefendantName(DefendantBasicValue dbv) {
        StringBuffer sb = new StringBuffer();
        appendName(sb, dbv.getFirstName());
        appendName(sb, dbv.getMiddleName());
        appendName(sb, dbv.getSurname());

        return sb.toString();
    }

    /**
     * Utility method to construct a person's name from it's constituent parts.
     * The format mirrors that used to build PersonValue.fullname in that the
     * initials are used when no firstName and middleName is available.
     * 
     * @param firstName
     * @param middleName
     * @param initials
     * @param surname
     * @return A well formatted defendant name as a string.
     */
    public static String buildFullName(String firstName, String middleName, String initials, String surname) {
        StringBuffer sb = new StringBuffer();
        appendName(sb, firstName);
        appendName(sb, middleName);
        if (sb.length() == 0) {
            appendName(sb, initials);
        }
        appendName(sb, surname);

        return sb.toString();
    }

    /**
     * Slightly neater way of appending names and doing spaces so that no
     * unnecessary spaces are included... all names to be added should use this
     * method
     * 
     * @param sb
     *            StringBuffer to append to.
     * @param candidateName
     *            name to attempt to add, if there is something in sb and
     *            candidateName is not null then a space will be appended before
     *            the name is appended.
     */
    private static void appendName(StringBuffer sb, String candidateName) {
        if (candidateName != null && !candidateName.equals("")) {
            if (sb.length() > 0)
                sb.append(" ");
            sb.append(candidateName);
        }
    }

    /**
     * Returns xmlKey String by using dbValue to search on the collection passed
     * in.
     * 
     * @param dbValue
     * @param restrictions
     * @return String
     */
    public static String getKeyForDb(String dbValue, Collection restrictions) {
        if (internalDebug) {
            StringBuffer s = new StringBuffer(PDHConstants.class.getName() + ".getKeyForDb(" + dbValue
                    + ", restrictions)");

            s.append("\n\tThe Restrictions collection contains:");
            if (restrictions != null) {
                Iterator it = restrictions.iterator();
                while (it.hasNext()) {
                    String restriction = (String) it.next();
                    s.append("\n\t\t- " + restriction);
                }
            }
            XHIBITConstant.debug(s.toString());
        }
        String xmlKey = "";
        if (dbValue == null || restrictions == null) {
            if (internalDebug)
                XHIBITConstant.debug("getKeyForDb(" + dbValue + ", restrictions) returning " + xmlKey);
            return xmlKey;
        }
        Iterator iter = restrictions.iterator();
        while (iter.hasNext()) {
            Object item = iter.next();
            if (dbValue.equals(XHIBITConstant.getResource(XhibitBundles.Directions, ((String) item) + "_DB"))) {
                xmlKey = (String) item;
                break;
            }
        }
        if (internalDebug)
            XHIBITConstant.debug("getKeyForDb(" + dbValue + ", restrictions) returning " + xmlKey);
        return xmlKey;
    }

    /**
     * Creates and returns a JRadioButton by setting properties using xml String
     * passed in. The JRadioButton is added to the HashMap parameter.
     * 
     * @param xml
     * @param map
     * @return JRadioButton
     */
    public static JRadioButton getRb(String xml, HashMap map) {
        JRadioButton thisRb = new JRadioButton();
        thisRb.putClientProperty(PDHConstants.xmlCode, xml);
        thisRb
                .putClientProperty(PDHConstants.dbCode, XHIBITConstant.getResource(XhibitBundles.Directions, xml
                        + "_DB"));
        thisRb.setText(XHIBITConstant.getResource(XhibitBundles.Directions, xml));
        map.put(xml, thisRb);
        if (internalDebug) {
            StringBuffer strbuf = new StringBuffer();
            strbuf.append(PDHConstants.class.getName() + ": \nPDHConstant.getRB(" + xml + ", aHashMap)");
            strbuf.append("\n\tputClientProperty(" + PDHConstants.xmlCode + ", " + xml + ")");
            strbuf.append("\n\tputClientProperty(" + PDHConstants.dbCode + ", "
                    + XHIBITConstant.getResource(XhibitBundles.Directions, xml + "_DB") + ")");
            strbuf.append("\n\tsetText(" + XHIBITConstant.getResource(XhibitBundles.Directions, xml) + ")");
            strbuf.append("\n\tput the rb in the map with key " + xml + ", returning the rb.");
            XHIBITConstant.debug(strbuf.toString());
        }
        return thisRb;
    }

    public static final String notselected = "notselected";

    /**
     * Creates and returns a JRadioButton which is not selected. The
     * JRadioButton is added to the HashMap parameter.
     * 
     * @param map
     * @return JRadioButton
     */
    public static JRadioButton getNotSelectRb(HashMap map) {
        JRadioButton thisRb = new JRadioButton();
        thisRb.putClientProperty(PDHConstants.xmlCode, null);
        thisRb.putClientProperty(PDHConstants.dbCode, null);
        thisRb.setText(notselected);
        map.put(notselected, thisRb);
        return thisRb;
    }

    /**
     * Creates and returns a JCheckBox by setting properties using
     * DefendantBasicValue and DefendantOnCaseBasicValue arguments passed in.
     * The JCheckBox is added to the HashMap parameter.
     * 
     * @param dbv
     * @param docbv
     * @param map
     * @return JCheckBox
     */
    public static JCheckBox getCb(DefendantBasicValue dbv, DefendantOnCaseBasicValue docbv, HashMap map) {
        JCheckBox thisCb = new JCheckBox();
        thisCb.putClientProperty(id, docbv.getId());
        thisCb.putClientProperty(basicValue, docbv);
        thisCb.setText(buildDefendantName(dbv));

        if (internalDebug) {
            XHIBITConstant.debug(PDHConstants.class.getName() + ": Putting (" + docbv.getId() + ", " + thisCb
                    + ") in map");
        }

        // nz5zpz: replaced
        // map.put(dbv.getId(), thisCb);
        // with
        map.put(docbv.getId(), thisCb);
        // 'cause the DefendantsPanel seems to expect this. (fixing 52595)
        return thisCb;
    }

    /**
     * Returns a JScrollPane containing the JPanel passed in as an argument
     * 
     * @param pnl
     * @return JScrollPane
     */
    public static JScrollPane getScroll(javax.swing.JPanel pnl) {
        JScrollPane scroll = new JScrollPane(pnl);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scroll;
    }

    /**
     * Returns the JRadioButton which is selected out of the ButtonGroup passed
     * in as a parameter.
     * 
     * @param bGroup
     * @return JRadioButton
     */
    public static JRadioButton getSelectedRadio(ButtonGroup bGroup) {
        if (bGroup.isSelected(bGroup.getSelection())) {
            JRadioButton selectedRB = null;
            Enumeration enumeration = bGroup.getElements();
            while (enumeration.hasMoreElements()) {
                JRadioButton item = (JRadioButton) enumeration.nextElement();
                if (item.isSelected()) {
                    selectedRB = item;
                    break;
                }
            }
            return selectedRB;
        }
        return null;
    }
}