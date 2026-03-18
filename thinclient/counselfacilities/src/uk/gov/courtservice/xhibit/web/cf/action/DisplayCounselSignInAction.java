package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: Thin Client Counsel Facilities
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version $Revision: 1.21 $
 * 
 */

public class DisplayCounselSignInAction extends TerminalCookieAction {
    private static final Logger log = CSServices.getLogger(DisplayCounselSignInAction.class);

    // environment data refence keys

    // key to reference the collection of Court Rooms in the Court (to
    // filter
    // the view)
    public static String Key_CourtRooms_Collection = "Key_CourtRooms_Collection";

    // key to reference the legal reprentative (table row model) (if 'assign
    // mode', not overview)
    public static String Key_LegalRepresentative = "Key_LegalRepresentative";

    // key to reference the collection in the session parameters collection
    public static String Key_PartiesOnCase_Collection = "Key_PartiesOnCase_Collection";

    // key to reference the collection's index in the session parameters
    // collection
    public static String Key_PartiesOnCase_Index = "Key_PartiesOnCase_Index";

    // key to reference the collection's columns' order in the session
    // parameters collection
    public static String Key_PartiesOnCase_ColumnOrder = "Key_PartiesOnCase_ColumnOrder";

    // key to reference the screen mode
    public static String Key_ScreenMode = "Key_ScreenMode";

    // key to reference the screen column sort mode
    public static String Key_ScreenColumnSortMode = "Key_ScreenColumnSortMode";

    public static String Key_TransactionCode = "trxCode";

    public static String Key_OnlyRooms = "onlyRoom";

    // http request data refence keys

    // key/values to determine the screen mode from the http request
    public static String ReqParamKey_ScreenMode = "assignlegalrepScreenMode";

    public static String ReqParamValue_ScreenMode_Assign = "DISPLAY4INPUT";

    public static String ReqParamValue_ScreenMode_Overview = "overview";

    // key/values to determine the screen column sort mode from the http
    // request
    public static String ReqParamKey_ScreenColumnSort = "assignlegalrepColumnSort";

    public static String ReqParamValue_ScreenColumnSort_Court = "1";

    public static String ReqParamValue_ScreenColumnSort_Defendant = "4";

    // private fields populates according to the http request parameters or
    // defaults.
    // used to determine the screen mode and screen column sort mode
    private boolean screen_AllowsInput = true;

    private String screen_SortColumns = ReqParamValue_ScreenColumnSort_Court;

    // the Vector (to keep order) to hold the mid tier PartiesOnCase
    // Collection
    private Vector partiesOnCase;

    // the collection to hold the mid tier CourtRoomsInCourt Collection
    private Collection courtRooms;

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * This method uses following Session Parameters: -
     * SearchLegRepAction.COLLECTION_NAME : to scan for counsel member matching
     * the legalRepId passed in the request - Key_LegalRepresentative : to store
     * the counsel member from the collection above matching the legalRepId -
     * courtId - onlyRoom
     * 
     * This method uses following Request Parameters: - legalRepId ; using the
     * request's legalRepId to locate the counsel member in the session-stored
     * SearchLegRepAction.COLLECTION_NAME collection if the matching counsel is
     * found in the session collection then a
     * FindLegalRepresentativeTableRowModel will be stored in the session, named
     * Key_LegalRepresentative - onlyRoom - this.ReqParamKey_ScreenMode
     * 
     * 
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */

    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("home");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }
            try {
                try {
                    // get the ID from the Leg Rep Identified, then load
                    String legRepId = (String) actionEnvironment.getRequestParameter("legalRepId");
                    log.debug("request's legRepId = " + legRepId);
                    // Now, get the corresponding value object and put it in
                    // session memory.
                    Collection coll = (Collection) actionEnvironment
                            .getSessionParameter(SearchLegRepAction.COLLECTION_NAME);
                    Iterator it = coll.iterator();
                    boolean legRepFound = false;
                    while ((it.hasNext()) && (!legRepFound)) {
                        FindLegalRepresentativeTableRowModel f = (FindLegalRepresentativeTableRowModel) it.next();
                        if (legRepId.equals(f.getLegalRepId())) {
                            actionEnvironment.setSessionParameter(Key_LegalRepresentative, f);
                            legRepFound = true;
                        }
                    }
                    if (!legRepFound) {
                        log.error("Did not find the legrep with id " + legRepId + " in the collection");
                    }
                } catch (ParameterNotFoundException pne) {
                    log.error("Could not find parameter legalRepId in " + actionEnvironment);
                    // actionEnvironment.setSessionParameter(Key_LegalRepresentative,
                    // new PersonValue());
                }

                try {
                    String trxCode = (String) actionEnvironment.getRequestParameter("trxCode");
                    // Now, get the corresponding value object and put it in
                    // session memory.
                    log.debug("request's trxCode = " + trxCode);
                    actionEnvironment.setRequestParameter(Key_TransactionCode, trxCode);
                } catch (ParameterNotFoundException pne) {
                    log.debug("Could not find parameter trxCode in " + actionEnvironment);
                    actionEnvironment.setRequestParameter(Key_TransactionCode, "");
                }

                Integer onlyRoomId = new Integer(0);
                try {
                    onlyRoomId = new Integer((String) actionEnvironment.getRequestParameter("onlyRoom"));
                    log.debug("Specific court room id " + onlyRoomId + "- (from req) will be shown only");
                } catch (Exception e) {
                    try {
                        onlyRoomId = new Integer((String) actionEnvironment.getSessionParameter("onlyRoom"));
                        log.debug("Specific court room id " + onlyRoomId + "- from sess will be shown only");
                    } catch (Exception ee) {
                        log.debug("Could not retrieve a specific court room id - all will be shown");
                        log.debug(ee);
                    }
                }

                // now, continue, find court and date, and call the mid tier to
                // get the overview
                // of defendants/cases on could sign on for.

                // if (((Vector)getPartiesOnCase()).size() < 1)
                // {
                setPartiesOnCase(CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                        .getAssignRepresentatives(getCourtId(actionEnvironment), new Date()));
                // }

                log.debug("retrieved Parties on Case for court id :" + getCourtId(actionEnvironment) + " and date "
                        + new Date());
                log.debug("there are " + this.getPartiesOnCase().size() + "PartiesOnCase");
                // Collection c = this.getPartiesOnCase();
                // Iterator i = c.iterator();
                // while (i.hasNext())
                // {
                // log.debug("party on case : " + i.next());
                // }
                // log.debug("end of retrieved Parties on Case:");

                // Vector rooms = new Vector();

                setCourtRooms(getCourtRoomsInCourt(getPartiesOnCase()));

                // find out the sort mode 'court or defendant'
                String mode = "";
                try {
                    mode = (String) actionEnvironment
                            .getRequestParameter(DisplayCounselSignInAction.ReqParamKey_ScreenMode);
                } catch (Exception e) {
                    log.debug("No Screen Mode parameter (" + DisplayCounselSignInAction.ReqParamKey_ScreenMode
                            + ") found in request.");
                    // trouble getting the parameter
                }
                if (DisplayCounselSignInAction.ReqParamValue_ScreenMode_Assign.equals(mode)) {
                    this.screen_AllowsInput = true;
                    // find out the sorting of columns mode (by court or
                    // defendant)
                } else if (DisplayCounselSignInAction.ReqParamValue_ScreenMode_Overview.equals(mode)) {
                    this.screen_AllowsInput = false;

                } else {
                    this.screen_AllowsInput = true;
                }

                String sortCols = "";
                try {
                    sortCols = (String) actionEnvironment
                            .getRequestParameter(DisplayCounselSignInAction.ReqParamKey_ScreenColumnSort);
                    log.debug("Sort Column parameter (" + DisplayCounselSignInAction.ReqParamKey_ScreenColumnSort
                            + ") = " + sortCols);
                } catch (Exception e) {
                    log.debug("No Sort Column parameter (" + DisplayCounselSignInAction.ReqParamKey_ScreenColumnSort
                            + ") found in request.");
                }

                if (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court.equals(sortCols)) {
                    this.screen_SortColumns = DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court;
                } else if (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Defendant.equals(sortCols)) {
                    this.screen_SortColumns = DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Defendant;
                } else {
                    this.screen_SortColumns = DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court;
                }
                log.debug("this.screen_AllowsInput=" + this.screen_AllowsInput);
                log.debug("this.screen_SortColumns=" + this.screen_SortColumns);

                // now, put the columns in the right order.
                Vector columnOrder = new Vector();
                if (DisplayCounselSignInAction.ReqParamValue_ScreenColumnSort_Court.equals(this.screen_SortColumns)) {
                    columnOrder.add(ColumnDefinition.COURT);
                    columnOrder.add(ColumnDefinition.TIME);
                    columnOrder.add(ColumnDefinition.ROLE);
                    columnOrder.add(ColumnDefinition.PARTY);
                    if (this.screen_AllowsInput)
                        columnOrder.add(ColumnDefinition.SELECT);
                    columnOrder.add(ColumnDefinition.CASELEGALREPS);
                } else {
                    columnOrder.add(ColumnDefinition.PARTY);
                    columnOrder.add(ColumnDefinition.ROLE);
                    columnOrder.add(ColumnDefinition.COURT);
                    columnOrder.add(ColumnDefinition.TIME);
                    if (this.screen_AllowsInput)
                        columnOrder.add(ColumnDefinition.SELECT);
                    columnOrder.add(ColumnDefinition.CASELEGALREPS);
                }

                Vector v = (Vector) this.getPartiesOnCase();
                log.debug("There are " + v.size() + " PartiesOnCase to sort");
                log.debug("The screen_SortColumns are " + this.screen_SortColumns);
                Sorter sorter = new Sorter(v);
                Vector index = sorter.sortByColumn(this.screen_SortColumns, true);
                /** @todo: ASC vs. DESC * */

                // set all the data in the session parameter collection for the
                // jsp/jstl to process.
                actionEnvironment.setRequestParameter(Key_ScreenMode, new Boolean(screen_AllowsInput));
                actionEnvironment.setRequestParameter(Key_ScreenColumnSortMode, screen_SortColumns);
                actionEnvironment.setRequestParameter(Key_LegalRepresentative, "Mr Legal Rep's Value Object");
                actionEnvironment.setSessionParameter(Key_CourtRooms_Collection, getCourtRooms());
                actionEnvironment.setSessionParameter(Key_PartiesOnCase_Collection, getPartiesOnCase());
                actionEnvironment.setRequestParameter(Key_PartiesOnCase_ColumnOrder, columnOrder);
                actionEnvironment.setRequestParameter(Key_PartiesOnCase_Index, index);
                actionEnvironment.setSessionParameter(Key_OnlyRooms, onlyRoomId.toString());
                actionEnvironment.setResponseName("displaycounselsignin");
            } catch (CounselFacilitiesControllerException e) {
                throw new FrameworkException(e);
            }
        }
    }

    public Collection getCourtRooms() {
        if (this.courtRooms == null)
            setCourtRooms(new Vector());
        return this.courtRooms;
    }

    public void setCourtRooms(Collection col) {
        this.courtRooms = col;
    }

    public Collection getPartiesOnCase() {
        if (this.partiesOnCase == null)
            setPartiesOnCase(new Vector());
        return this.partiesOnCase;
    }

    public void setPartiesOnCase(Collection col) {
        this.partiesOnCase = new Vector(col);
    }

    public static interface ColumnDefinition {
        public static String COURT = "1";

        public static String TIME = "2";

        public static String ROLE = "3";

        public static String PARTY = "4";

        public static String SELECT = "5";

        public static String CASELEGALREPS = "6"; // combination of case,
        // legal
        // reps and poss ccinfo cols
    }

    private Vector getCourtRoomsInCourt(Collection col) {
        Vector v = new Vector();
        Iterator i = col.iterator();
        while (i.hasNext()) {
            PartyOnCaseValue p = (PartyOnCaseValue) i.next();
            Vector courtRoom = new Vector();
            courtRoom.add(p.getCourtRoomId());
            courtRoom.add(p.getCourtRoomName());
            courtRoom.add(p.getCourtRoomDisplayName());
            courtRoom.add(p.getCrestCourtRoomNumber());
            v = addCourtRoom(courtRoom, v);
        }

        // Sort by crestCourtRoomNumber
        Collections.sort(v, new Comparator() {
            public int compare(Object o1, Object o2) {
                return compare((Vector) o1, (Vector) o2);
            }

            public int compare(Vector v1, Vector v2) {
                return compare((Integer) v1.get(3), (Integer) v2.get(3));
            }

            public int compare(Integer i1, Integer i2) {
                return i1.compareTo(i2);
            }
        });

        return v;
    }

    private Vector addCourtRoom(Vector aRoom, Vector rooms) {
        Integer newRoomId = (Integer) aRoom.elementAt(0);
        boolean alreadyInThere = false;
        Iterator i = rooms.iterator();
        while (i.hasNext() && (!(alreadyInThere)))
            if (newRoomId.equals((Integer) ((Vector) i.next()).elementAt(0)))
                alreadyInThere = true;
        if (!alreadyInThere)
            rooms.add(aRoom);
        return rooms;
    }
}
