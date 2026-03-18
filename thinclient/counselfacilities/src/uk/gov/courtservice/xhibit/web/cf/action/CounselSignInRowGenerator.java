package uk.gov.courtservice.xhibit.web.cf.action;

/**
 * <p>Title: CounselSignInRowGenerator</p>
 * <p>Description: Utility class for formating the table data for cousel facilities sign in and overview.
 * Subclasses must implement the getTableRow and getColumnNames methods.
 * </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: EDS</p>
 * @author Kevin Buckthorpe
 * @version 1.0
 */
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.web.framework.util.ResourceUtil;

public abstract class CounselSignInRowGenerator {
    private static HashMap specialCharacters;

    public void init() {
        specialCharacters = new HashMap();
        specialCharacters.put(new String("\""), "&quot;");
        // add new characters as requirement arises;
        // key = character to replace
        // value = character replacement value

    }

    /**
     * Intendend to be called by getAllTableRows() method, with a row being
     * represented by a javascript method.
     * 
     * @param index
     *            row number of the table (stating from 0)
     * @param party
     *            provides the data for formatting
     * @return formatted data for the table row.
     */
    public abstract String getTableRow(int index, PartyOnCaseValue party);

    /**
     * Gets the column names for the table being constructed
     * 
     * @return column names in the order in which they appear
     */
    public abstract String[] getColumnNames();

    /**
     * Constructs the table rows. Subclasses implement the getTableRow for how
     * each row is constructed. It is intended that each row is represented by a
     * javascript method
     * 
     * @param toFormat
     *            a collection of PartyOnCaseValues
     * @return collection of formatted table rows
     */
    public Vector getAllTableRows(Collection toFormat) {
        Iterator it = toFormat.iterator();
        Vector retRows = new Vector();
        int count = 0;

        while (it.hasNext()) {
            if (count % 50 == 0) {
                retRows.add("</SCRIPT><SCRIPT LANGUAGE=\"JavaScript\">");
            }
            PartyOnCaseValue party = (PartyOnCaseValue) it.next();
            retRows.add(getTableRow(count, party));
            count++;
        }

        return retRows;
    }

    /**
     * Formated the defendant name for this set of tables
     * 
     * @param defVal
     * @return formatted defendant name
     */
    protected static String formatDefendantName(DefendantValue defVal) {
        StringBuffer defName = new StringBuffer();
        if (defVal != null) { /*
                                 * counsel sign in don't want masked names see
                                 * PR 55487
                                 * if(defVal.getIsMasked().equalsIgnoreCase("y")) {
                                 * defName.append(defVal.getMaskedName()); }
                                 * else {
                                 */
            String surname = defVal.getSurName();
            String firstName = defVal.getFirstName();
            String initials = defVal.getInitials();
            if (surname != null) {
                defName.append(surname);
                if (firstName != null) {
                    defName.append(" ");
                    defName.append(firstName);
                    if (initials != null && !initials.trim().equals("")) {
                        defName.append(", ");
                        defName.append(initials);
                    }
                }
            } else if (firstName != null) {
                defName.append(firstName);
                if (initials != null && !initials.trim().equals("")) {
                    defName.append(", ");
                    defName.append(initials);
                }
            } else if (initials != null) {
                defName.append(initials);
            }
            // }
            defName.append("<br>");
        }
        return filterCharacters(defName.toString());
    }

    /**
     * Format the case title to remove any special character as per the
     * defendant names
     */
    protected static String formatCaseTitle(String caseTitle) {
        return filterCharacters(caseTitle);
    }

    /**
     * Formats the presentation of the legal representatives for this set of
     * tables
     * 
     * @param repVal
     * @return formatted string
     */
    protected static String formatLegalRepName(LegalRepSignInValue repVal) {
        String first = repVal.getLegRepFirstName();
        String mid = repVal.getLegRepMiddleName();
        String sur = repVal.getLegRepSurname();
        StringBuffer buf = new StringBuffer();
        boolean hasName = false;
        if (first != null) {
            buf.append(first);
            if (mid != null) {
                buf.append(" ");
                buf.append(mid);
            }
            if (sur != null) {
                buf.append(" ");
                buf.append(sur);
            }
            hasName = true;
        } else if (mid != null) {
            buf.append(mid);
            if (sur != null) {
                buf.append(" ");
                buf.append(sur);
            }
            hasName = true;
        } else if (sur != null) {
            buf.append(sur);
            hasName = true;
        }
        if (hasName) {
            buf.append("<br>");
        }

        String type = repVal.getLegalRepType();
        String chambers = repVal.getChamberFirmName();
        String solfirm = repVal.getSolicitorFirmName();
        if (type != null) {
            type = type.trim();
            if (type.equalsIgnoreCase("S") && solfirm != null) {
                buf.append(solfirm);
                buf.append("<BR>");
            } else if (type.equalsIgnoreCase("A") && chambers != null) {
                buf.append(chambers);
                buf.append("<br>");
            }
        }
        buf.append("<br>");

        return filterCharacters(buf.toString());
    }

    /**
     * Translates the role represented by a character into the required string
     * 
     * @param role
     * @return description of the role
     */
    protected static String roleConverter(String role) {
        String retName = "";
        if (role != null) {
            if (role.equals("D")) {
                retName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.D");
            } else if (role.equals("P")) {
                retName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.P");
            } else if (role.equals("A")) {
                retName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.A");
            } else if (role.equals("R")) {
                retName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.R");
            } else if (role.equals("O")) {
                retName = ResourceUtil.getPropertyForResourceAndLocale("Messages", Locale.getDefault(),
                        "assignlegalrep.O");
            }
        }
        return retName;
    }

    private static String filterCharacters(String in) {
        Iterator keys = specialCharacters.keySet().iterator();
        StringBuffer out = new StringBuffer();

        while (keys.hasNext()) {
            out = new StringBuffer();
            String charVal = (String) keys.next();
            String repVal = specialCharacters.get(charVal).toString();
            int index1 = 0;
            int index2 = in.indexOf(charVal);

            if (index2 > -1) {
                if (in.startsWith(charVal)) {
                    in = in.substring(charVal.length());
                    out.append(repVal);
                }

                while (index2 >= 0) {
                    if (index2 > 0) {
                        out.append(in.substring(index1, index2));
                        out.append(repVal);
                        index1 = index2 + 1;
                    }
                    index2 = in.indexOf(charVal, index2 + 1);
                }
                out.append(in.substring(index1));
            } else {
                out = new StringBuffer(in);
            }

            in = out.toString();
        }
        return out.toString();
    }

    /**
     * Check whether cousel sign in check box should be disabled
     * 
     * @param caseType
     * @param role
     * @param shDefId
     * @return true if checkbox should be disabled for counsel sign in
     */
    public boolean isCheckboxDisabled(String caseType, String role, Integer shDefId) {
        boolean isDisabled = false;

        if (caseType != null && role != null) {
            if ((!caseType.equals("A") && !caseType.equals("S") && !caseType.equals("T"))
                    || ((role.equals("A") || role.equals("D")) && (shDefId == null || shDefId.intValue() == 0))) {
                isDisabled = true;
            }
        }

        return isDisabled;
    }

}