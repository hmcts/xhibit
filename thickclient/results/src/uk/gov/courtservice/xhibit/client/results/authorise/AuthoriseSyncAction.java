package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.xhibit.client.util.SynchXAction;

/**
 * <p>
 * Title: Apply Action to activate authorise
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthoriseSyncAction.java,v 1.6 2006/06/05 12:32:15 bzjrnl Exp $
 */

public class AuthoriseSyncAction extends SynchXAction {

    private AuthoriseResultsPanel authorisePanel;

    public AuthoriseSyncAction(AuthoriseResultsPanel authorisePanel) {
        super("btnAuthorise");
        this.authorisePanel = authorisePanel;
    }

    public void preSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        this.setEnabled(false);
        try {
            authorisePanel.stepValidate();
            authorisePanel.stepDeactivate();
        } catch (Exception e) {
            this.setEnabled(true);
            throw e;
        }

    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        this.setEnabled(false);
        try {
            authorisePanel.stepDeinitialise(true);
        } catch (Exception e) {
            this.setEnabled(true);
            throw e;
        }
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        try {
            authorisePanel.postStepDeinitialise();
        } catch (Exception e) {
            this.setEnabled(true);
            throw e;
        }
    }
}