package uk.gov.courtservice.xhibit.client.actions.charges;

//import uk.gov.courtservice.xhibit.client.search.court.XHIBITSearchCourt;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description: An EDS - Court Service Application
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Frederik Vandendriessche
 * @version 1.0
 * @deprecated
 */

public class ChangeCourtAction extends XAction {

    public ChangeCourtAction() {
        populateFromBundle("ChangeCourtAction");
    }

    public void xActionPerformed(ActionEvent e) {
        XHIBITConstant.debug("OpenAmendDefendantAction: opening amend defendant dialog.");

        // XHIBITSearchCourt xsCourt = new XHIBITSearchCourt();

        XHIBITConstant.debug("OpenAmendDefendantAction: closed amend defendant dialog.");
    }
}