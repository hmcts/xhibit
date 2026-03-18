package uk.gov.courtservice.xhibit.client.counselfacilities;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.swing.event.TableModelEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.query.CourtList;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XDateFormat;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.util.table.XTable;
import uk.gov.courtservice.xhibit.client.util.table.model.XHIBITTableModelInterface;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: CounselFacilitiesHelper
 * </p>
 * <p>
 * Description: The helper class for the thick GUI functionality for Counsel
 * Facility.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully, Sherie de Silva
 * @author Marie Holmberg
 * @version 1.0
 */
public class CounselFacilitiesHelper {
    private static final String[] COURT_ROOM_SEQUENCE = new String[] { "courtSiteCode", "isFloating",
            "crestCourtRoomNumber", "sittingSequenceNo", "timeListed", "shSequenceNo" };

    private static final String[] DEFENDANT_NAME_SEQUENCE = new String[] { "party", "courtSiteCode", "isFloating",
            "crestCourtRoomNumber", "sittingSequenceNo", "timeListed", "shSequenceNo" };

    public static final String BARRADIO = "BARRADIO";

    public static final String COURADIO = "COURADIO";

    public static final String DEFRADIO = "DEFRADIO";

    public static final String INPRADIO = "INPRADIO";

    public static final String SOLRADIO = "SOLRADIO";
    
    public static final String NONATTRADIO = "NONATTRADIO";

    public static final String CURRENT = "CURRENT";

    public static final String ALL = "ALL";

    public static final String CASE_TYPE_A = "A";

    public static final String CASE_TYPE_B = "B";

    public static final String CASE_TYPE_S = "S";

    public static final String CASE_TYPE_T = "T";

    public static final String CASE_TYPE_U = "U";

    public static final String ADVOCATE = "A";

    public static final String UNASSIGNED = "1";

    public static final String LEGAL_REP_TYPE_IN_PERSON = "I";
    
    public static final String LEGAL_REP_TYPE_NON_ATTENDANCE = "N";

    public static final String LEGAL_REP_TYPE_LAWYER = "L";

    public static final String LEGAL_REP_TYPE_SOLICITOR = "S";

    public static final int MINIMUM_SEARCHABLE_CHARACTERS = 1;

    public static final String ROLE_APPELLANT = "A";

    public static final String ROLE_DEFENDANT = "D";

    public static final String ROLE_PROSECUTION = "P";

    public static final String ROLE_RESPONDENT = "R";

    public static final String ROLE_OBJECTOR = "O";

    /**
     * This will build the full name, e.g. "firstname surname"
     * 
     * @param fullName
     * @param firstName
     * @param surname
     * @return String
     * @deprecated - use getFullName( String, String[] )
     */
    public static String getFullName(String fullName, String firstName, String surname) {
        StringBuffer buf = new StringBuffer();

        if (tidyUp(fullName).length() == 0) {
            buf.append(tidyUp(firstName));
            if (buf.length() > 0) {
                buf.append(' ');
            }
            buf.append(tidyUp(surname));
        } else {
            buf.append(tidyUp(fullName));
        }

        return buf.toString();
    }

    /**
     * This will build the full name, e.g. "title firstname surname"
     * 
     * @param String
     *            fullName
     * @param String[]
     *            nameElements
     * @return String
     */
    public static String getFullName(String fullName, String[] nameElements) {
        StringBuffer buf = new StringBuffer();

        if (tidyUp(fullName).length() == 0) {
            for (int x = 0; x < nameElements.length; x++) {
                if (nameElements[x] != null && nameElements[x].length() > 0) {
                    buf.append(" ");
                    buf.append(nameElements[x]);
                }
            }
        } else {
            buf.append(tidyUp(fullName));
        }

        return buf.toString().trim();
    }

    /**
     * Build the name where surname is before firstname, eg. "surname,
     * firstname"
     * 
     * @param firstName
     * @param surname
     * @return
     */
    public static String getSurnameFirstName(String firstName, String surname) {
        StringBuffer buf = new StringBuffer();

        buf.append(tidyUp(surname));

        if (tidyUp(firstName).length() > 0) {
            if (buf.length() > 0) {
                buf.append(", ");
            }

            buf.append(tidyUp(firstName));
        }

        return buf.toString();
    }

    /**
     * Method to make sure nulls are not displayed.
     * 
     * @param param
     * @return String
     */
    public static String tidyUp(String param) {
        return (param == null ? "" : param);
    }

    /**
     * Concatenate the case type with the number.
     * 
     * @param type
     * @param number
     * @return String
     */
    public static String getCaseTypeAndNumber(String type, Integer number) {
        StringBuffer buf = new StringBuffer();

        buf.append(tidyUp(type));
        if (number != null) {
            buf.append(tidyUp(number.toString()));
        }

        return buf.toString();
    }

    /**
     * Get the names and the chambers
     * 
     * @param param
     * @return String
     */
    public static String getFullNamesAndChambers(Collection param) {
        StringBuffer buf = new StringBuffer();
        boolean firstName = true;

        if (param != null) {
            Iterator iter = param.iterator();
            while (iter.hasNext()) {
                LegalRepSignInValue item = (LegalRepSignInValue) iter.next();

                if (firstName) {
                    firstName = false;
                } else {
                    buf.append("\n\n");
                }

                if (item.getSolFirmOrRefLegalRep().equalsIgnoreCase(LEGAL_REP_TYPE_IN_PERSON)) {
                    buf.append(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "dcdI"));
                } else if (item.getSolFirmOrRefLegalRep().equalsIgnoreCase(LEGAL_REP_TYPE_NON_ATTENDANCE)) {
                    buf.append(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "dcdN"));
                } else {
                    buf.append(getFullName("", new String[] { item.getLegRepTitle(), item.getLegRepFirstName(),
                            item.getLegRepSurname() }));

                    if (item.getLegalRepType().equalsIgnoreCase(ADVOCATE)) {
                        if (item.getChamberFirmName() != null && item.getChamberFirmName().length() > 0) {
                            buf.append("\n" + item.getChamberFirmName());
                        }
                    } else {
                        if (item.getSolicitorFirmName() != null && item.getSolicitorFirmName().length() > 0) {
                            buf.append("\n" + item.getSolicitorFirmName());
                        }
                    }
                }
            }
        }
        return buf.toString();
    }

    /**
     * The passed in collection is a list of LegalRepSignInValues that holds the
     * legal rep data. All the legal reps will be returned in a readable format.
     * 
     * @param param
     * @param personType
     * @return String
     */
    public static String getFullNamesAndChamberAndAddress(Collection param) {
        StringBuffer buf = new StringBuffer();
        boolean firstRow = true;

        if (param != null) {
            Iterator iter = param.iterator();
            while (iter.hasNext()) {
                // get each legal rep that is assigned.
                LegalRepSignInValue item = (LegalRepSignInValue) iter.next();

                // if there already is something in the buffer then add a new
                // line
                if (firstRow) {
                    firstRow = false;
                } else {
                    buf.append("\n\n");
                }

                // add the legal rep name
                if (item.getSolFirmOrRefLegalRep().equalsIgnoreCase(LEGAL_REP_TYPE_IN_PERSON) || (item.getSolFirmOrRefLegalRep().equalsIgnoreCase(LEGAL_REP_TYPE_NON_ATTENDANCE))) {
                    buf.append(XHIBITConstant.getResource(XhibitBundles.CounselFacilities, "dcdI"));
                } else {
                    buf.append(getFullName("", new String[] { item.getLegRepTitle(), item.getLegRepFirstName(),
                            item.getLegRepSurname() }));

                    if (item.getLegalRepType().equalsIgnoreCase(ADVOCATE)) {
                        if (item.getChamberFirmName() != null && item.getChamberFirmName().length() > 0) {
                            buf.append("\n" + item.getChamberFirmName());
                        }
                    } else {
                        if (item.getSolicitorFirmName() != null && item.getSolicitorFirmName().length() > 0) {
                            buf.append("\n" + item.getSolicitorFirmName());
                        }
                    }

                    // Append all the address details.
                    buf.append(getLegalRepAddress(item));
                }
            }
        }
        return buf.toString();
    }

    /**
     * This will take a LegalRepSignInValue as an argument and take out the
     * address data and add it to a String in a readable format.
     * 
     * @param item
     *            LegalRepSignInValue
     * @return String - the formatted address
     */
    public static String getLegalRepAddress(LegalRepSignInValue item) {
        StringBuffer buf = new StringBuffer();

        if ((item.getAddress1() != null) && (!item.getAddress1().equals(""))) {
            buf.append("\n" + item.getAddress1());
        }
        if ((item.getAddress2() != null) && (!item.getAddress2().equals(""))) {
            buf.append("\n" + item.getAddress2());
        }
        if ((item.getAddress3() != null) && (!item.getAddress3().equals(""))) {
            buf.append("\n" + item.getAddress3());
        }
        if ((item.getAddress4() != null) && (!item.getAddress4().equals(""))) {
            buf.append("\n" + item.getAddress4());
        }
        if ((item.getTown() != null) && (!item.getTown().equals(""))) {
            buf.append("\n" + item.getTown());
        }
        if ((item.getCounty() != null) && (!item.getCounty().equals(""))) {
            buf.append("\n" + item.getCounty());
        }
        if ((item.getPostcode() != null) && (!item.getPostcode().equals(""))) {
            buf.append("\n" + item.getPostcode());
        }
        return buf.toString();
    }

    /**
     * Return a collection of defendant names as a string
     * 
     * @param param
     * @return String
     */
    public static String getDefendantNames(Collection param) {
        StringBuffer buf = new StringBuffer();
        boolean firstName = true;

        Iterator iter = param.iterator();
        while (iter.hasNext()) {
            DefendantValue item = (DefendantValue) iter.next();

            if (firstName) {
                firstName = false;
            } else {
                buf.append('\n');
            }

            buf.append(getSurnameFirstName(item.getFirstName(), item.getSurName()));
        }

        return buf.toString();
    }

    /**
     * Method to redisplay the table
     * 
     * @param table
     * @param data
     */
    public static void redisplayTable(XTable table, Vector data) {
        XHIBITTableModelInterface model = (XHIBITTableModelInterface) table.getModel();
        model.setData(data);
        // RL: The following line is commented out as there is not benefit to
        // assigning the model back to itself !
        // table.setModel(model);
        table.tableChanged(new TableModelEvent(model));
    }

    /**
     * Get the first defendant from a collection
     * 
     * @param param
     * @return DefendantValue
     */
    public static DefendantValue getFirstDefendant(Collection param) {
        return ((DefendantValue) param.iterator().next());
    }

    /**
     * Get the date and time in a certain format
     * 
     * @param param
     * @param format
     * @return String
     */
    public static String getDateTime(Date param, int format) {
        return (XDateFormat.format(param, format));
    }

    /**
     * Sort collection of AssignRepresentativesTableRowModels The sort order is : -
     * court site code - is the case floating (all floaters will appear in the
     * end) - CREST court room number - sitting sequence number - timeListed -
     * Date - scheduled hearing sequence number
     * 
     * @param partyOnCaseList
     */
    public static void sortByCourtRoom(Collection partyOnCaseList) {
        Sorter.sort((List) partyOnCaseList, COURT_ROOM_SEQUENCE);
    }

    /**
     * Sort collection of AssignRepresentativesTableRowModels by defendant name.
     * The sort order is : - party - the defendant - court site code - is the
     * case floating (all floaters will appear in the end) - CREST court room
     * number - sitting sequence number - timeListed - Date - scheduled hearing
     * sequence number
     * 
     * @param partyOnCaseList
     */
    public static void sortByDefendant(Collection partyOnCaseList) {
        Sorter.sort((List) partyOnCaseList, DEFENDANT_NAME_SEQUENCE);
    }

    /**
     * Sort PartyOnCaseValues by - is the case floating (all floaters will
     * appear in the end) - by crest courtrooom number - timeListed - String
     * representation of the date.
     * 
     * @param partyOnCaseList
     * @deprecated use #sortByCourtRoom(Collection partyOnCaseList)
     */
    public static void sortByCourtRoomAssignLegalRep(Collection partyOnCaseList) {
        sortByCourtRoom(partyOnCaseList);
    }

    /**
     * Method to format the printed list. If a list should be printed for a
     * specific court room or for the entire court.
     * 
     * @param model
     * @return String - xml/xsl string
     * @throws UserCancelException
     * @throws CSRecoverableException
     */
    protected String formatAssignedRepsForPrinting(AssignRepresentativesModel model) throws UserCancelException,
            CSRecoverableException {

        // Get the user's print options
        PrintCounselSignInModel pcsim = new PrintCounselSignInModel();
        pcsim.setXac(model.getXac());

        try {
            PrintCounselSignInDialog pcsid = new PrintCounselSignInDialog(model.getXac(), pcsim);
            pcsid.setVisible(true);

            if (pcsid.isCancelClicked()) {
                throw new UserCancelException();
            }
        } catch (UserCancelException uce) {
            throw uce;
        } catch (Exception e) {
            throw new CSRecoverableException("gui.counselSignIn.printDialog", null,
                    "An error occurred in the PrintCounselSignInDialog", e);
        }

        Integer courtRoomId = null;
        if (!pcsim.getSelectedOption().equalsIgnoreCase(CounselFacilitiesHelper.ALL)) {
            courtRoomId = XhibitSingleton.getInstance().getCourtRoomId();
        }

        CourtList cl = XhibitDelegateHelper.getCounselFacilitiesDelegate().getCourtRoomList(
                XhibitSingleton.getInstance().getCourtId(), new Date(), courtRoomId);

        // Return the XSL formatted object
        return getFormattedDocument(cl);
    }

    // extracted from formatAssignedRepsForPrinting
    // made it protected so it can be tested
    protected static String getFormattedDocument(CourtList courtList) throws CSRecoverableException {
        try {
            // Pass data to Print Service to create XSL formatting object
            return CSServices.getPrintServices().getFormattedDocument(courtList, Locale.getDefault());
        } catch (Exception e) {
            throw new CSRecoverableException("gui.counselSignIn.formatting", null,
                    "An error whilst formatting data to print the counsel sign in details", e);
        }
    }
}
