package uk.gov.courtservice.xhibit.client.actions.results.authorise;

import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JOptionPane;

import uk.gov.courtservice.xhibit.client.results.authorise.AuthoriseResultsDialog;
import uk.gov.courtservice.xhibit.client.util.SynchXAction;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;

/**
 * <p>
 * Title:
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
 * @version $Id: AuthoriseResultsAction.java,v 1.1 2004/05/26 07:49:56 sz0t7n
 *          Exp $
 * @history James Powell 18/03/2009 - Moved sequence number validation to the dialog class
 */

public class AuthoriseResultsAction extends SynchXAction {

    private AuthoriseResultsDialog dialog = null;

    public AuthoriseResultsAction() {
        populateFromBundle("AuthoriseResults");
    }

    public void synchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        
        dialog = new AuthoriseResultsDialog(xac);
    }

    public void postSynchActionPerformed(ActionEvent parm1) throws java.lang.Exception {
        if (dialog != null)
            dialog.setVisible(true);
    }
          
}