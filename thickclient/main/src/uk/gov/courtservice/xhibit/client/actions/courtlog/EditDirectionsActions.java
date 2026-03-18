package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.FreeTextModel;
import uk.gov.courtservice.xhibit.client.courtlog.directions.DirectionsSingleEventDialog;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.courtlog.vos.CourtLogCRUDValue;

/**
 * <p>
 * Title: Edit Directions Action
 * </p>
 * <p>
 * Description: Directions are 11 events that are created on one big screen,
 * however they are editted individually
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Revision: 1.8 $
 */
public class EditDirectionsActions extends CourtLogAction {
    public void displayScreen(ActionEvent e) throws CSRecoverableException {
        FreeTextModel ftm = (FreeTextModel) getModel();
        if (ftm.isInEditMode()) {
            XhibitApplicationController xac = (XhibitApplicationController) getController();
            CourtLogCRUDValue crud = XhibitDelegateHelper.getCourtLogDelegate2().getEntry(ftm.getEventId());
            final DirectionsSingleEventDialog dsed = new DirectionsSingleEventDialog(xac, xac, crud);

            displayDialog(dsed);
        }
    }
}
