package uk.gov.courtservice.xhibit.client.actions.charges;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import uk.gov.courtservice.xhibit.client.originalcharges.OriginalChargesDialog;
import uk.gov.courtservice.xhibit.client.originalcharges.OriginalChargesModel;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.util.helpers.SeqNoHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.maintaincharges.ChargesController;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.framework.exception.CSRecoverableException;

public class OriginalChargesAction extends XAction {

    public OriginalChargesAction() {
        populateFromBundle("OriginalCharges");
    }
    
    public void xActionPerformed(ActionEvent e) throws Exception {
        OriginalChargesModel model = new OriginalChargesModel();
        XhibitApplicationController xac = (XhibitApplicationController) getController();
        model.setXac(xac);
        
        OriginalChargesDialog myDialog = new OriginalChargesDialog((Frame)getController(), model);
        myDialog.setVisible(true);

        if (myDialog.isCancelClicked()) {
            throw new UserCancelException();
        }else{            
            //Reload defOnCaseSeqNosMap which is stored in the xac model            
            reloadDefOnCaseSeqNosMaps(xac);            
        }
    }
    
    /**
     * This method recreates the defOnCaseSeqNosMap after original charges have been modified.  
     * @param xac
     * @throws CSRecoverableException
     */
    private void reloadDefOnCaseSeqNosMaps(XhibitApplicationController xac) throws CSRecoverableException{
        Integer caseID = xac.getApplicationCaseModel().getCaseId();
        ChargeCompositeValue ccv = null;
        
        //Get Charges
        try{
            ccv = XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
        }catch (Exception e){
           throw new CSRecoverableException("gui.OriginalChargesAction.reloadDefOnCaseSeqNosMap",
                   "Exception whilst getting the charge composite value object from the mid tier", e);
        }
       
        //Create active charge hashmap and store in applicationCaseModel
        HashMap<Integer,List> defOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(ccv.getCharges(), ccv.getAllDefendants());
        xac.getApplicationCaseModel().setDefOnCaseSeqNosMap(defOnCaseSeqNosMap);
    
        if (xac.getBodyPanel() instanceof ChargesController) {
            //If we are in 'View charges' then refresh that
            Collection charges = null;
            try{
                charges = XhibitDelegateHelper.getChargeDelegate().getChargesList(caseID);
            }catch (Exception e){
                throw new CSRecoverableException("gui.OriginalChargesAction.reloadDefOnCaseSeqNosMap",
                        "Exception whilst getting the charge composite value object from the mid tier", e);
            }
    
            //Create all charges (including obsolete) map and store in ChargesControllerModel
            defOnCaseSeqNosMap = SeqNoHelper.constructSequenceNumberMap(charges, ccv.getAllDefendants());
            ((ChargesController)xac.getBodyPanel()).getModel().setAllDefOnCaseSeqNosMap(defOnCaseSeqNosMap);
        }
        
        
    }
}
