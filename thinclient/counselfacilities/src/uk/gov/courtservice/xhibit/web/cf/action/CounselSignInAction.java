package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: CounselSignInAction
 * </p>
 * <p>
 * Description: Action for counsel sign in
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
public class CounselSignInAction extends TerminalCookieAction {
    private static final Logger log = CSServices.getLogger(DisplayCounselSignInAction.class);

    public static String Key_LegalRepresentative = "Key_LegalRepresentative";

    public static String Key_PartiesOnCase_Collection = "Key_PartiesOnCase_Collection";

    // key to reference the collection of Court Rooms in the Court (to
    // filter the view)
    public static String Key_CourtRooms_Collection = "Key_CourtRooms_Collection";

    public CounselSignInAction() {
    }

    /**
     * <p>
     * This method is invoked by the framework to perform the requested action.
     * </p>
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

            CounselSignInHelper helper = new CounselSignInHelper();
            helper.perform(actionEnvironment, getCourtId(actionEnvironment), getCourtSiteId(actionEnvironment),
                    getLegalRep(actionEnvironment));

            actionEnvironment.setSessionParameter("mode", "signin");
            actionEnvironment.setResponseName("displaycounselsignin");
        }
    }

    /**
     * original code base for this method is taken from
     * DisplayCounselSignInAction
     * 
     * @author Frederik Vandendriessche
     * @param actionEnvironment
     * @return
     */
    private FindLegalRepresentativeTableRowModel getLegalRep(ActionEnvironment actionEnvironment) {
        String legRepId = (String) actionEnvironment.getRequestParameter("legalRepId");
        actionEnvironment.setSessionParameter("legalRepId", legRepId);
        Collection coll = (Collection) actionEnvironment.getSessionParameter(SearchLegRepAction.COLLECTION_NAME);
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            FindLegalRepresentativeTableRowModel f = (FindLegalRepresentativeTableRowModel) it.next();
            if (legRepId.equals(f.getLegalRepId())) {
                return f;
            }
        }

        // code should never reach this point
        throw new ParameterNotFoundException("Unable to find legal representative with id " + legRepId);
    }
}
