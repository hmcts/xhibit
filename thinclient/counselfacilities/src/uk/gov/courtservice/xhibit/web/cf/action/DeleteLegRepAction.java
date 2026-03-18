package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.gov.courtservice.xhibit.business.cf.services.CounselFacilitiesHelper;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.LegalRepSignInValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;

/**
 * <p>
 * Title: Delete a Legal Representative from counsel sign in
 * </p>
 * <p>
 * Description: deletes a legal rep that has previously signed in. (i.e.
 * un-signs in the legal rep)
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Kevin Buckthorpe 18 Dec 2003
 * @version 1.0
 */

public class DeleteLegRepAction extends TerminalCookieAction {
    public void terminalPerformAction(ActionEnvironment actionEnvironment) throws FrameworkException {
        if (!isTokenInSession()) {
            actionEnvironment.logout();
            actionEnvironment.setResponseName("home");
        } else {
            if (!checkToken()) {
                throw new DuplicateFormSubmissionException();
            }

            String delColIndex = (String) actionEnvironment.getRequestParameter("delColId");
            List partyOnCaseList = (List) actionEnvironment
                    .getSessionParameter(CounselSignInAction.Key_PartiesOnCase_Collection);
            PartyOnCaseValue pocVal = (PartyOnCaseValue) partyOnCaseList.get(Integer.parseInt(delColIndex));

            String displayName = pocVal.getCourtRoomDisplayName();
            Collection defCol = pocVal.getDefendants();
            Iterator defIt = defCol.iterator();
            String defName = "";

            while (defIt.hasNext()) {
                DefendantValue def = (DefendantValue) defIt.next();
                defName += CounselSignInRowGenerator.formatDefendantName(def);
            }

            // set table values
            actionEnvironment.setRequestParameter("courtname", displayName);
            actionEnvironment.setRequestParameter("time", CounselFacilitiesHelper.dateToTimeString(pocVal
                    .getTimeListed()));
            actionEnvironment.setRequestParameter("role", CounselSignInRowGenerator
                    .roleConverter(pocVal.getPartyRole()));
            actionEnvironment.setRequestParameter("defendant", defName);
            actionEnvironment.setRequestParameter("case", pocVal.getCaseType() + pocVal.getCaseNumber());

            // collection of legalRepSignInValue
            Collection legRepCol = pocVal.getRepresentatives();
            Vector legRepVect = new Vector();
            Iterator legRepIt = legRepCol.iterator();

            while (legRepIt.hasNext()) {
                legRepVect.add(CounselSignInRowGenerator.formatLegalRepName((LegalRepSignInValue) legRepIt.next()));
            }

            // add legal rep sign in value collection
            actionEnvironment.setRequestParameter("removereps", legRepVect);
            actionEnvironment.setSessionParameter("repCollection", legRepCol);

            actionEnvironment.setResponseName("deletelegrep");
        }
    }

}