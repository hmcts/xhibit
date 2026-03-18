package uk.gov.courtservice.xhibit.client.results.authorise;

import java.awt.event.ActionEvent;
import uk.gov.courtservice.xhibit.client.util.XAction;



/**
 * Title:           AuthoriseUpdateVictimIndicatorAction
 * Description:     Updates the Vulnerable Victim Indicator field within the XHB_Case Table.
 * Company:         Logica
 * 
 * @author davieskl
 * @version 1.0
 */
public class AuthoriseUpdateVictimIndicatorAction  extends XAction {

    private static final long serialVersionUID = 1L;
    private AuthoriseResultsPanel authorisePanel;

 
    public AuthoriseUpdateVictimIndicatorAction(AuthoriseResultsPanel authorisePanel){
        populateFromBundle("AuthoriseResultsApply");
        this.authorisePanel = authorisePanel;
        this.setEnabled(true);
    }
    
    @Override
    public void xActionPerformed(ActionEvent e) throws Exception {
      authorisePanel.updateCaseWithVulnerableVictimIndicator();
    }

}
