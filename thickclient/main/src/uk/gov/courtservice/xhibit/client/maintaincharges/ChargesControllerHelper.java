package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.PrintChargesValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.client.actions.ActionNotFoundException;
import uk.gov.courtservice.xhibit.client.actions.XhibitActions;
import uk.gov.courtservice.xhibit.client.util.UserCancelException;
import uk.gov.courtservice.xhibit.client.util.XAction;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitApplicationController;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version $Id: ChargesControllerHelper.java,v 1.30 2006/01/05 15:29:54 bzjrnl
 *          Exp $ <p/> This class has a Unit Test. Please ensure it is up to
 *          date.
 */
public class ChargesControllerHelper {
    public enum MODE {ADD, EDIT,REMOVE}
    
    private static final String MERCATOR_READY_FOR_EXPORT = "R";

    private static final String MERCATOR_START_PROCESS_UNAVAILABLE = "U";

    private static final Logger log = CSServices.getLogger(ChargesControllerHelper.class);

    /**
     * Create a ChargesControllerHelper
     */
    private ChargesControllerHelper() {
        // ensure no-one can instantiate this class.
    }

    protected static List getChargesActions(XhibitApplicationController xac) {
        List<XAction> chargesActions = new ArrayList<XAction>(50);

        try {
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RemoveIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RenumberCounts));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.StayIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.SignIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.SignIndictmentRefused));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.JoinIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddS41Offence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddC4SOffence));

            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.C4SBringBack));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.C4SNotAdmitted));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.C4SPutAndAdmitted));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.SOProsecutionNoEvidence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.DefendantSummaryOffences));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.ApplicationToSever));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.VoluntaryBillPreferred));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.LateBillOfIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.BillOfIndictment));

            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddBreach));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddBreachOffence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.EditBreachProps));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AdditionalCountInfo));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnCountInfo));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddDefendantsToCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RemoveDefendantsOnCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.ChangeCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AdditionalBreachOffenceDefendantInfo));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RemoveCount));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.QuashIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.StayCount));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.LieOnFileCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.CountParticularsAmended));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.ChangeDefendant));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddCountsToDefendant));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.StayDefendantOnCount));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.LieOnFileDefendantOnCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.StayDefendantOnIndictment));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.LieOnFileDefendantOnIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AdditionalOffenceInfo));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AdditionalDefendantOnOffenceInfo));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.AddDefendantsToOffence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RemoveDefendantsOnCount));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.ChangeOffence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.RemoveOffence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbAdd));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbAddCharge));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbAddOffence));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbAddDefendant));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbChange));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.tbStay));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.tbLieOnFile));
            // chargesActions.add(XhibitActions.getAction(xac,
            // XhibitActions.tbQuash));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.JoinIndictment));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.CrestIndictmentLog));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.ExportCharges));
            chargesActions.add(XhibitActions.getAction(xac, XhibitActions.OriginalCharges));
        } catch (ActionNotFoundException ex) {
            log.warn(ex, ex);
        }
        return chargesActions;
    }

    public static String formatCharges(ChargeCompositeValue ccv, XhibitApplicationController xac)
            throws UserCancelException, CSRecoverableException {
        PrintChargesModel pcm = new PrintChargesModel();
        String xslfo = "";

        try {
            PrintChargesDialog pcd = new PrintChargesDialog(xac, pcm);
            pcd.setVisible(true);
            if (pcd.isCancelClicked()) {
                throw new UserCancelException();
            }
        } catch (UserCancelException uce) {
            throw uce;
        } catch (Exception e) {
            Object[] params = null;
            CSRecoverableException csre = new CSRecoverableException(
                    "gui.user.ChargesControllerHelper.formatCharges.PrintChargesDialog", params,
                    "An error occurred in the Print Charges Dialog", e);
            throw csre;
        }

        XhibitSingleton xs = XhibitSingleton.getInstance();
        Integer courtSiteId = xs.getCourtId();
        CourtBasicValue cbv = xs.getCourtBasicValue(courtSiteId);
        String courtName = cbv.getCourtName();

        PrintChargesValue pcv = new PrintChargesValue(ccv);

        pcv.setHeaderLine1(courtName);
        Integer printSelection = new Integer(pcm.getPrintSelection());
        pcv.setPrintSelection(printSelection);

        try {
            xslfo = getFormattedDocument(pcv);
        } catch (Exception e) {
            Object[] params = null;
            CSRecoverableException csre = new CSRecoverableException(
                    "gui.user.ChargesControllerHelper.formatCharges.format", params,
                    "An error whilst formatting data for printing", e);
            throw csre;
        }
        return xslfo;
    }

    // extracted from formatCharges - made it protected so it can be tested,
    // even
    // though only called from within this class
    protected static String getFormattedDocument(PrintChargesValue value) throws CSRecoverableException {
        // Pass the returned data to the Print Controller to create an XSL
        // formatting object
        return CSServices.getPrintServices().getFormattedDocument(value, Locale.getDefault());
    }

    public static boolean canCaseBeUpdated(CaseBasicValue cbv) {
        if (cbv.getIndChangeStatus() != null
                && (cbv.getIndChangeStatus().equals(MERCATOR_READY_FOR_EXPORT) || cbv.getIndChangeStatus().equals(
                        MERCATOR_START_PROCESS_UNAVAILABLE))) {
            return false;
        }
        return true;
    }

    public static String buildDefendantName(DefendantValue dbv) {
        StringBuffer sb = new StringBuffer();
        sb.append(checkNull(dbv.getFirstName()));
        if (sb.length() > 0)
            sb.append(" ");
        sb.append(checkNull(dbv.getMiddleName()));
        if (sb.length() > 0)
            sb.append(" ");
        sb.append(checkNull(dbv.getSurName()));
        return sb.toString();
    }

    public static String checkNull(String toCheck) {
        return (toCheck == null ? "" : toCheck);
    }
    
    /**
     * Gets the defendant values that are on the case but not on the
     * offence/count.
     * 
     * @return a collection of defendant values that are on the case but not on
     *         the offence/count.
     */
    @SuppressWarnings("unchecked")
	public static Collection getDefendantsNotOnOffence(ChargesControllerModel ccm) {
            ArrayList<DefendantValue> notOnOffence = new ArrayList<DefendantValue>();            
            
            Collection<DefendantValue> allDefendants = ccm.getCCV().getAllDefendants();
            List<Integer> defendantIds = null;
            Integer chargeId = ccm.getOffenceValue().getChargeID();
	        Integer refOffenceId = ccm.getOffenceValue().getRefOffenceID();
	        Integer addressId = ccm.getOffenceValue().getAddressId();
	        // Only check if this is an existing charge, otherwise all defendants are valid 
	        if (chargeId != null) {
				defendantIds = XhibitDelegateHelper.getChargeDelegate().findDefendantIdsByChargeIdAndRefOffenceId(chargeId, refOffenceId, addressId);
	        }
            
            if (defendantIds == null || defendantIds.size() == 0) {
                notOnOffence.addAll(allDefendants);
            } else {
            	for (DefendantValue defendant : allDefendants) {
            		if (!defendantIds.contains(defendant.getDefendantID())) {
                        notOnOffence.add(defendant);
                    }
                }
            }
        return notOnOffence;
    }
    
    /**
     * Gets the defendant values that are on the case but not on the
     * offence/count
     * 
     * @return a collection of defendant values that are on the case but not on
     *         the offence/count.
     */
    public static Collection getDefendantsNotOnOffenceICases(ChargesControllerModel ccm) {
            ArrayList<DefendantValue> notOnOffence = new ArrayList<DefendantValue>();
            DefendantValue allDefendantValue;
            DefendantValue offenceDefendantValue;
    
            Collection<DefendantValue> allDefendants = ccm.getCCV().getAllDefendants();
            Collection offenceDefendants = ccm.getOffenceValue().getDefendantValues();
            
            if (offenceDefendants == null || offenceDefendants.size() == 0) {
                notOnOffence.addAll(allDefendants);
            } else {
                Iterator allIterator = allDefendants.iterator();
                while (allIterator.hasNext()) {
                    allDefendantValue = (DefendantValue) allIterator.next();
                    boolean alreadyOnCount = false;
    
                    Iterator offenceIterator = offenceDefendants.iterator();
                    while (offenceIterator.hasNext()) {
                        offenceDefendantValue = (DefendantValue) offenceIterator.next();
                        
                        if (allDefendantValue.getDefendantID().intValue() == offenceDefendantValue.getDefendantID().intValue() ) {
                            //also chck if the obs ind is N
                            DefendantOnOffenceComplexValue defOnOffComplexValue = ccm.getOffenceValue().getDefendantOnOffence(offenceDefendantValue.getDefendantID());
                            if(defOnOffComplexValue.getObsInd()!=null && defOnOffComplexValue.getObsInd().equals("Y"))
                            {
                                alreadyOnCount = false;
                                break;
                            }
                            else
                            {
                                alreadyOnCount = true;
                                break;
                                
                            }
                        }
                    }
    
                    if (alreadyOnCount == false) {
                        notOnOffence.add(allDefendantValue);
                    }
                }
            }
        return notOnOffence;
    }
    
    /**
     * Gets the defendant values that are on the case and on the
     * offence/count.
     * 
     * @return a collection of defendant values that are on the case and on
     *         the offence/count.
     */
    public static Collection getDefendantsOnOffence(ChargesControllerModel ccm) {
            DefendantValue defendantValue;            
            Collection offenceDefendants = ccm.getOffenceValue().getDefendantValues();
            Collection<DefendantValue> uniqueDefendants=new ArrayList<DefendantValue>();
            Collection<Integer> defendantIds=new ArrayList<Integer>();
            Iterator iter = offenceDefendants.iterator();
            while (iter.hasNext()) {
                    defendantValue = (DefendantValue) iter.next();
                    if(!defendantIds.contains(defendantValue.getDefendantID())                          )
                    {
                        DefendantOnOffenceComplexValue defOnOffComplexValue = ccm.getOffenceValue().getDefendantOnOffence(defendantValue.getDefendantID());
                        if((defOnOffComplexValue.getObsInd()==null) || !(defOnOffComplexValue.getObsInd()!=null && defOnOffComplexValue.getObsInd().equals("Y")))
                        {
                            uniqueDefendants.add(defendantValue);
                            defendantIds.add(defendantValue.getDefendantID());
                        }
                    }
                }            
        return uniqueDefendants;
    }
    
    //TODO - Called from AddDefendantOnOffencePanel & AddBreachDefendantOnOffencePanel
    public int getNextSequenceNoForDefendantOnCase(){
        /**
         * SEq No. Rules:
         * Get last sequence number for the defendant on the case
         * Get the defendant id, defendant on case id, look through defendant/offence sequence no.s for
         * caseid, defendantid, defendantoncaseid? get all defendant ids on defendant on offence table for 
         *  find max seq no by defendant_on_case_id where charge type is not 'O' 
         */
        //TODO ask ST if not working.  Need to contain all def on cases in the drop down.
        /*
         * SELECT MAX(dof.seq_no) 
            FROM xhb_charge ch, xhb_offence o, xhb_defendant_on_offence dof 
            WHERE ch.charge_type != 'O'
            AND ch.charge_id = o.charge_id
            AND o.offence_id = dof.offence_id
            AND o.defendant_case_id = 1 
         */
        //select distinct ca.case_id, ch.charge_id, ch.charge_type from xhb_case ca, xhb_charge ch where ch.charge_type != 'O' and ch.case_id= 1
//        model.getCCV().getCharges().iterator()
        return 0;
    }
    
    //TODO - Called from AddDefendantOnOffencePanel & AddBreachDefendantOnOffencePanel
    public void validateSeqNo(int seqNo, int maxSeqNo){
        
    }
}
