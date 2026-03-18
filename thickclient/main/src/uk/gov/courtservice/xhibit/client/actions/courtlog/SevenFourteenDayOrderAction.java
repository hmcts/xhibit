package uk.gov.courtservice.xhibit.client.actions.courtlog;

import java.awt.Frame;
import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.courtlog.SevenFourteenDayOrderDialog;
import uk.gov.courtservice.xhibit.client.courtlog.SevenFourteenDayOrderModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;

/**
 * <p>
 * Title: XHIBIT 2
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Unknown
 * @version $Revision: 1.7 $
 */
public class SevenFourteenDayOrderAction extends CourtLogAction {
    protected void displayScreen(ActionEvent e) throws UserCancelException, CSRecoverableException {
        log.debug("SevenFourteenDayOrder - displayScreen(e)");

        final SevenFourteenDayOrderModel model = (SevenFourteenDayOrderModel) cloneModel();
        final SevenFourteenDayOrderDialog myDialog = new SevenFourteenDayOrderDialog((Frame) getController(), model);

        displayDialog(myDialog);
    }
}
