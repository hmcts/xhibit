package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: CounselSigninOverviewAction
 * </p>
 * <p>
 * Description: Action resulting in the display of cousel signin overview page
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */
public class CounselSigninOverviewAction extends TerminalCookieAction {
    // key to reference the collection of Court Rooms in the Court (to
    // filter the view)
    public static String Key_CourtRooms_Collection = "Key_CourtRooms_Collection";

    private static final Logger log = CSServices.getLogger(CounselSigninOverviewAction.class);

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
     * 
     * @param actionEnvironment
     *            the environment to evaluate the action in
     */
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        try { // collection of partyOnCaseVo
            Collection collection = CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                    .getAssignRepresentatives(getCourtId(actionEnvironment), new Date());

            // two action classes direct to the same jsp. jsp toggles on
            // trxcode value and mode
            actionEnvironment.setSessionParameter("trxCode", "");
            actionEnvironment.setSessionParameter("mode", "overview");

            final PartyOnCaseSorter sorter;
            final CounselSignInRowGenerator helper;

            int courtRoomId = 0;
            String onlyRoom = "0";
            int sortCol = PartyOnCaseSorter.COURTNAME; // default sort

            // get column to sort on
            try {
                String sortColStg = (String) actionEnvironment.getSessionParameter("sortColStg");
                sortCol = Integer.parseInt(sortColStg);
            } catch (NumberFormatException e) { // not a problem. Ignore it and
                // continue
                log.debug(e);
            } catch (ParameterNotFoundException pnfe) {
                log.debug(pnfe);
            } // not a problem. Ignore it and continue
            catch (IllegalArgumentException iae) {
                log.debug(iae);
            } // not a problem. Ignore it and continue

            try {
                String sortColStg = (String) actionEnvironment.getRequestParameter("assignlegalrepColumnSort");
                sortCol = Integer.parseInt(sortColStg);
                actionEnvironment.setSessionParameter("sortColStg", sortColStg);
            } catch (NumberFormatException e) { // ignore it and use defaults
                log.debug(e);
            } catch (ParameterNotFoundException pnfe) {
                log.debug(pnfe);
            } // not a problem. Ignore it and continue
            catch (IllegalArgumentException iae) {
                log.debug(iae);
            } // not a problem. Ignore it and continue

            if (sortCol == PartyOnCaseSorter.DEFENDANT) {
                // set to sort by defendant
                sorter = new PartyOnCaseSorter(PartyOnCaseSorter.DEFENDANT);
                helper = new OverviewByDefendant();
            } else // default sort and display
            {
                // set to sort by court room
                sorter = new PartyOnCaseSorter(PartyOnCaseSorter.COURTNAME);
                helper = new OverviewByCourtroom();
            }

            // filter court rooms for site
            CounselFacilitiesHelper cfhelper = new CounselFacilitiesHelper();
            collection = cfhelper.getPartiesOnCaseForSite(collection, getCourtSiteId(actionEnvironment));

            // get court room names in this collection
            Collection courtRoomsInCourt = PartyOnCaseSorter.getCourtRoomsInCourt(collection);

            // look for room filter in session
            try {
                onlyRoom = (String) actionEnvironment.getSessionParameter("onlyRoom");
                courtRoomId = Integer.parseInt(onlyRoom);
            } catch (NumberFormatException e) { // not a problem. Ignore it and
                // continue
                log.debug(e);
            } catch (ParameterNotFoundException pnfe) {
                log.debug(pnfe);
            } // not a problem. Ignore it and continue
            catch (IllegalArgumentException iae) {
                log.debug(iae);
            } // not a problem. Ignore it and continue

            // look for room filter in request. If found this takes priority
            // over session value.
            try {
                String room = (String) actionEnvironment.getRequestParameter("onlyRoom");
                courtRoomId = Integer.parseInt(room);
                onlyRoom = room;
            } catch (NumberFormatException e) { // ignore it and use default ALL
                log.debug(e);
            } catch (ParameterNotFoundException pnfe) {
                log.debug(pnfe);
            } // not a problem. Ignore it and continue
            catch (IllegalArgumentException iae) {
                log.debug(iae);
            } // not a problem. Ignore it and continue

            // filter cases for court room if requested
            if (courtRoomId > 0) {
                collection = PartyOnCaseSorter.filterCollection(new Integer(courtRoomId), collection);
            }

            // do the sort
            Collections.sort((List) collection, sorter);

            // get the column headings
            String[] colNames = helper.getColumnNames();
            actionEnvironment.setRequestParameter("col1", colNames[0]);
            actionEnvironment.setRequestParameter("col2", colNames[1]);
            actionEnvironment.setRequestParameter("col3", colNames[2]);
            actionEnvironment.setRequestParameter("col4", colNames[3]);

            // get table rows
            Collection toDisplay = helper.getAllTableRows(collection);
            actionEnvironment.setRequestParameter("displayList", toDisplay);

            // set the room number filter
            actionEnvironment.setSessionParameter("onlyRoom", onlyRoom);

            // set drop down court room selector
            actionEnvironment.setSessionParameter(Key_CourtRooms_Collection, courtRoomsInCourt);
        } catch (CounselFacilitiesControllerException e) {
            throw new FrameworkException(e);
        }

        actionEnvironment.setResponseName("displaycounselsignin");
    }

}
