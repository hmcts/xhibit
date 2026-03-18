package uk.gov.courtservice.xhibit.web.cf.action;

import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.cf.services.FindLegalRepresentativeTableRowModel;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.counselfacilities.CounselFacilitiesControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.SHLegRepBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.counselfacilities.PartyOnCaseValue;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.action.TerminalCookieAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.DuplicateFormSubmissionException;
import uk.gov.courtservice.xhibit.web.framework.util.FrameworkException;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: Default Action
 * </p>
 * <p>
 * Description: The default action for the application. <p/>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 */
public class AssignLegRepAction extends TerminalCookieAction {
    private static final Logger log = CSServices.getLogger(AssignLegRepAction.class);

    /**
     * Empty default constructor
     */
    public AssignLegRepAction() {
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
            Collection legalRepsCollection = new Vector();
            List partyOnCaseList = (List) actionEnvironment
                    .getSessionParameter(CounselSignInAction.Key_PartiesOnCase_Collection);
            Vector partyOnCaseCollection = new Vector(partyOnCaseList);
            FindLegalRepresentativeTableRowModel legalRepValue = (FindLegalRepresentativeTableRowModel) actionEnvironment
                    .getSessionParameter(DisplayCounselSignInAction.Key_LegalRepresentative);
            Integer legalRepId = new Integer(legalRepValue.getLegalRepId());

            for (int x = 0; x < partyOnCaseCollection.size(); x++) {
                try {
                    String select = (String) actionEnvironment.getRequestParameter("select_" + x);
                    PartyOnCaseValue item = (PartyOnCaseValue) partyOnCaseCollection.elementAt(x);

                    SHLegRepBasicValue shLegRepBV = new SHLegRepBasicValue();

                    String ccInfo = (String) actionEnvironment.getRequestParameter("ccInfo_" + x);

                    if ("0".equalsIgnoreCase(ccInfo)) {
                        shLegRepBV.setCcInfoID(null);
                    } else {
                        shLegRepBV.setCcInfoID(new Integer(ccInfo));
                    }

                    shLegRepBV.setCrestSequenceNo(null);
                    shLegRepBV.setIsSignIn("Y");
                    shLegRepBV.setLegalRole(determineLegalRole(item.getPartyRole()));
                    shLegRepBV.setRefDefenceCategoryID(null);
                    shLegRepBV.setRefLegalRepID(legalRepId);
                    shLegRepBV.setSchedHearDefID(item.getScheduledHearingDefendantId());
                    shLegRepBV.setScheduledHearingID(item.getScheduledHearingId());
                    if ((legalRepValue.getLegalRepType()).equalsIgnoreCase("BARRADIO")) {
                        shLegRepBV.setSolFirmOrRefLegalRep("L");
                        shLegRepBV.setRefSolicitorFirmID(null);
                    } else {
                        shLegRepBV.setSolFirmOrRefLegalRep("S");
                        shLegRepBV.setRefSolicitorFirmID(new Integer(legalRepValue.getChambersId()));
                    }

                    legalRepsCollection.add(shLegRepBV);
                } catch (ParameterNotFoundException pnfe) {
                    // pnfe.printStackTrace();
                    // NoAction
                    // log.warn(pnfe, pnfe); This is expected, but not a
                    // problem. There is no need to notify this one
                }
            }

            // If any rows were selected, make the appropriate BusOp call to
            // save the data
            try {
                // log.debug(" the collection :::::::::::::::::::::::: " +
                // legalRepsCollection.size( ));
                if (legalRepsCollection.size() > 0) {
                    CounselFacilitiesControllerBeanBusinessDelegate.DelegateFactory.getInstance()
                            .setAssignRepresentativesUsingBasicVO(legalRepsCollection,
                                    (actionEnvironment.getSessionParameter(UserTerminalProperties.DISPLAY_NAME.toString())).toString());
                }
            } catch (CounselFacilitiesControllerException e) {
                log.fatal(e, e);
                throw new FrameworkException(e);
            }
            actionEnvironment.setResponseName("assignlegrep");
        }
    }

    private String determineLegalRole(String legalRole) {
        if ("A".equalsIgnoreCase(legalRole) || "D".equalsIgnoreCase(legalRole)) {
            return "D";
        } else if ("O".equalsIgnoreCase(legalRole)) {
            return "O";
        } else if ("R".equalsIgnoreCase(legalRole)) {
            return "R";
        } else {
            return "P";
        }
    }
}
