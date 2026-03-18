package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.business.cf.services.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: CounselSignInHelper
 * </p>
 * <p>
 * Description: Helper to CounselSignIn and similiar classes
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe
 * @version 1.0
 */

public class CounselSignInHelper {
    private static final Logger log = CSServices.getLogger(CounselSignInHelper.class);

    public CounselSignInHelper() {
    }

    public void perform(ActionEnvironment actionEnvironment, Integer courtId, Integer courtSiteId,
            FindLegalRepresentativeTableRowModel legRep) throws FrameworkException {
        try {
            Collection collection = CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                    .getAssignRepresentatives(courtId, new Date());

            final PartyOnCaseSorter sorter;
            final CounselSignInRowGenerator helper;

            int courtRoomId = 0;
            String onlyRoom = "0";
            int sortCol = PartyOnCaseSorter.COURTNAME; // default sort
            String trxCode = "";

            // get the legal rep details
            try {
                if (legRep != null) {
                    actionEnvironment.setSessionParameter(CounselSignInAction.Key_LegalRepresentative, legRep);
                }

                try {
                    trxCode = (String) actionEnvironment.getRequestParameter("trxCode");
                } catch (ParameterNotFoundException pe) {
                    try {
                        trxCode = (String) actionEnvironment.getSessionParameter("trxCode");
                    } catch (ParameterNotFoundException pex) { // ignore it
                        log.warn(pex, pex);
                    }

                }

                actionEnvironment.setSessionParameter("trxCode", trxCode);
            } catch (ParameterNotFoundException e) {
                log.error("Could not set up legal rep details: " + actionEnvironment, e);
                actionEnvironment.setSessionParameter("trxCode", "");
            }

            // get column to sort on
            try {
                String sortColStg = (String) actionEnvironment.getSessionParameter("sortColStg");
                sortCol = Integer.parseInt(sortColStg);
            } catch (NumberFormatException e) { // not a problem. Ignore it and
                // continue
                log.info(e);
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
                helper = new SignInByDefendant();
            } else // default sort and display
            {
                // set to sort by court room
                sorter = new PartyOnCaseSorter(PartyOnCaseSorter.COURTNAME);
                helper = new SignInByCourtroom();
            }

            // filter court rooms for site
            CounselFacilitiesHelper cfhelper = new CounselFacilitiesHelper();
            collection = cfhelper.getPartiesOnCaseForSite(collection, courtSiteId);

            // get court room names in this collection
            Collection courtRoomsInCourt = PartyOnCaseSorter.getCourtRoomsInCourt(collection);

            // look for room filter in session
            try {
                onlyRoom = (String) actionEnvironment.getSessionParameter("onlyRoom");
                courtRoomId = Integer.parseInt(onlyRoom);
            } catch (NumberFormatException e) {
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
            actionEnvironment.setSessionParameter("col1", colNames[0]);
            actionEnvironment.setSessionParameter("col2", colNames[1]);
            actionEnvironment.setSessionParameter("col3", colNames[2]);
            actionEnvironment.setSessionParameter("col4", colNames[3]);
            actionEnvironment.setSessionParameter("col5", colNames[4]);

            // get table rows
            Collection toDisplay = helper.getAllTableRows(collection);
            actionEnvironment.setSessionParameter("displayList", toDisplay);

            // set the room number filter
            actionEnvironment.setSessionParameter("onlyRoom", onlyRoom);

            // set drop down court room selector
            actionEnvironment.setSessionParameter(CounselSignInAction.Key_CourtRooms_Collection, courtRoomsInCourt);

            // set sorted list in session for AssignLegRepAction
            actionEnvironment.setSessionParameter(CounselSignInAction.Key_PartiesOnCase_Collection, collection);
        } catch (CounselFacilitiesControllerException cfe) {
            throw new FrameworkException(cfe);
        }
    }

}