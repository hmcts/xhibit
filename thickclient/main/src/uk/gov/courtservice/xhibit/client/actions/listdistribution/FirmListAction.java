package uk.gov.courtservice.xhibit.client.actions.listdistribution;

import java.awt.event.ActionEvent;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTableModel;
import uk.gov.courtservice.xhibit.client.listdistribution.ManageListTablePanel;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

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
 * Company:
 * </p>
 * 
 * @author Gajan Rajasekaran
 * @version 1.0
 */

public class FirmListAction extends XAction {

    public FirmListAction() {
        populateFromBundle("FirmList");
    }

    public void xActionPerformed(ActionEvent e) throws CSRecoverableException {
        XhibitApplicationController xac;
        xac = (XhibitApplicationController) getController();
        Integer courtId = XhibitSingleton.getInstance().getCourtId();
        ManageListTableModel model = new ManageListTableModel();
        model.setCourtId(courtId);
        model.setListType(XHIBITConstant.getResource(XhibitBundles.ManageLists, "criminalFirmList"));
        model.setDocumentType(model.getListType());
        model.getTableColumnNames();

        // ManageListTableDialog myDialog = new ManageListTableDialog( ( Frame
        // )getController( ), model );
        // myDialog.setVisible( true );
        xac.open(new ManageListTablePanel(model));
    }
}
