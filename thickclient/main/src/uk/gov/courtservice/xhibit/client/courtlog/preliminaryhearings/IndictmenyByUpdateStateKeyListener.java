package uk.gov.courtservice.xhibit.client.courtlog.preliminaryhearings;

import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import uk.gov.courtservice.xhibit.client.courtlog.directions.UpdateStateKeyListener;

/**
 * <p>
 * Title: IndictmenyByUpdateStateKeyListener
 * </p>
 * <p>
 * Description: UpdateStateKeyListener for the IndictmentByPanel in Preliminary
 * Hearings. If the user has NULLed the date, the modified switch on the
 * IndictmentBy panel is set to false as it is deemed that the user does not
 * want to generate an IndictmentBy court log event.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Stephen Tully
 * @version 1.0
 */

public class IndictmenyByUpdateStateKeyListener extends UpdateStateKeyListener {
    public IndictmenyByUpdateStateKeyListener(JPanel component) {
        super(component);
    }

    public void keyReleased(KeyEvent e) {
        if ((component instanceof IndictmentByPanel)
                && (((IndictmentByPanel) component).getIndictmentBy().getDateComponent().getText().equalsIgnoreCase(""))) {
            // The Indictment By date has been initialised so set the
            // modified
            // flag for the panel to false as it is the only field on the
            // panel
            ((IndictmentByPanel) component).setModified(false);
            super.maybeUpdateViewState();
        } else {
            super.keyReleased(e);
        }
    }
}
