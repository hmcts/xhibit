package uk.gov.courtservice.xhibit.web.action;

import uk.gov.courtservice.framework.client.SessionPropertiesMap;
import uk.gov.courtservice.xhibit.business.vos.services.userterminal.UserTerminalProperties;
import uk.gov.courtservice.xhibit.web.framework.action.AbstractAction;
import uk.gov.courtservice.xhibit.web.framework.action.ActionEnvironment;
import uk.gov.courtservice.xhibit.web.framework.util.ParameterNotFoundException;

/**
 * <p>
 * Title: CourtCookieAction
 * </p>
 * <p>
 * Description: This abstract action is extended by actions which ensure the
 * court id is set before proceeding. The functionality for capturing the court
 * id is not implemented here as it can be captured at the same time as other
 * data.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Will Fardell, Xdevelopment LLP (2004)
 * @version $Revision: 1.7 $
 */
public abstract class CourtCookieAction extends AbstractAction {
    /**
     * @return true if the court id has been stored in the court cookie.
     */
    public boolean hasCourtId(ActionEnvironment actionEnvironment) {
        return getCourtId(actionEnvironment) != null;
    }

    /**
     * Get the court id from the court cookie
     * 
     * @return the court id from the cookie or null if it has not been set
     */
    public Integer getCourtId(ActionEnvironment actionEnvironment) {
        try {
            return (Integer) actionEnvironment.getSessionParameter(UserTerminalProperties.COURT_ID.toString());
        } catch (IllegalArgumentException ex) {
            return null;
        } catch (ParameterNotFoundException ex) {
            return null;
        }
    }

    protected void initialiseSession(ActionEnvironment actionEnvironment, SessionPropertiesMap propertyMap) {
        actionEnvironment.setSessionParameter(UserTerminalProperties.COURT_ID.toString(), (Integer) propertyMap
                .get(UserTerminalProperties.COURT_ID));
    }

}
