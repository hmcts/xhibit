package uk.gov.courtservice.xhibit.client.maintaincharges;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.framework.util.Sorter;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.client.courtlog.CourtLogEventsTableModel;
import uk.gov.courtservice.xhibit.client.models.ApplicationCaseModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;

/**
 * <p>
 * Title: XHIBIT2
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
 * @author Simon Gilmore
 * @version 1.0
 */

public class ChargesControllerModel {
    private CourtLogEventsTableModel courtLogEventsTableModel = null;

    private ApplicationCaseModel acm = null;

    private uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue ccv = null;

    private uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue = null;

    private OffenceValue offenceValue = null;

    private uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breachValue = null;

    private uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue = null;

    private int selectionType = -1;

    private int selectedChargeType = -1;

    private int courtId = -1;

    private int courtRoomId = -1;

    private boolean section41ChargeCreated = false;

    private boolean committalChargeCreated = false;
    
    private boolean appealChargeCreated = false;

    private boolean cancelClicked = false; 
    
    private HashMap<Integer, List> allDefOnCaseSeqNosMap;

    public ChargesControllerModel() {
    }

    public ChargeControllerBeanBusinessDelegate getDelegate() throws CSRecoverableException {
        return XhibitDelegateHelper.getChargeDelegate();
    }

    public ApplicationCaseModel getACM() {
        return acm;
    }

    public void setACM(ApplicationCaseModel acm) {
        this.acm = acm;
    }

    public void setSelectionModel(ChargesSelectionModel csm) {
        if (csm != null) {
            this.chargeValue = csm.getChargeValue();
            this.offenceValue = csm.getOffenceValue();
            this.defendantValue = csm.getDefendantValue();
            this.selectionType = csm.getSelectionType();

            if (chargeValue != null) {
                if (chargeValue.getChargeType().equals(ChargeTypes.BREACH.getChargeType())) {
                    this.breachValue = chargeValue.getBreachValue();
                }
            }
        } else {
            XHIBITConstant.debug("ChargesControllerModel: setSelectionModel: csm = null");
        }
    }

    public uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue getCCV() {
        return ccv;
    }

    public void setCCV(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue ccv) {
        this.ccv = ccv;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue chargeValue) {
        this.chargeValue = chargeValue;
    }

    public OffenceValue getOffenceValue() {
        return offenceValue;
    }

    public void setOffenceValue(OffenceValue offenceValue) {
        this.offenceValue = offenceValue;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue getBreachValue() {
        return breachValue;
    }

    public void setBreachValue(uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue breachValue) {
        this.breachValue = breachValue;
    }

    public uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue getDefendantValue() {
        return defendantValue;
    }

    public void setDefendantValue(
            uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue defendantValue) {
        this.defendantValue = defendantValue;
    }

    public int getSelectedChargeType() {
        return selectedChargeType;
    }

    public void setSelectedChargeType(int selectedChargeType) {
        this.selectedChargeType = selectedChargeType;
    }

    public int getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(int selectionType) {
        this.selectionType = selectionType;
    }

    public int getCourtId() {
        return courtId;
    }

    public void setCourtId(int courtId) {
        this.courtId = courtId;
    }

    public int getCourtRoomId() {
        return courtRoomId;
    }

    public void setCourtRoomId(int courtRoomId) {
        this.courtRoomId = courtRoomId;
    }

    public CourtLogEventsTableModel getCourtLogEventsTableModel() {
        return courtLogEventsTableModel;
    }

    public void setCourtLogEventsTableModel(CourtLogEventsTableModel courtLogEventsTableModel) {
        this.courtLogEventsTableModel = courtLogEventsTableModel;
    }

    public boolean isSection41ChargeCreated() {
        return section41ChargeCreated;
    }

    public void setSection41ChargeCreated(boolean section41ChargeCreated) {
        this.section41ChargeCreated = section41ChargeCreated;
    }

    public boolean isCommittalChargeCreated() {
        return committalChargeCreated;
    }

    public void setCommittalChargeCreated(boolean committalChargeCreated) {
        this.committalChargeCreated = committalChargeCreated;
    }
    
    public boolean isAppealChargeCreated() {
    	return appealChargeCreated;
    }
    
    public void setAppealChargeCreated(boolean appealChargeCreated) {
    	this.appealChargeCreated = appealChargeCreated;
    }

    public boolean isCancelClicked() {
        return cancelClicked;
    }

    public void setCancelClicked(boolean cancelClicked) {
        this.cancelClicked = cancelClicked;
    }

    /**
     * Convenience method to determine if the user is in a court room, or is,
     * for example, in a Court Clerk's Room
     * 
     * @return true if user is in a court room
     */
    public boolean isUserInCourtRoom() {
        return XhibitSingleton.getInstance().isUserInCourtroom();
    }
    
    
    public void setAllDefOnCaseSeqNosMap(HashMap<Integer,List> allDefOnCaseSeqNosMap){
        this.allDefOnCaseSeqNosMap = allDefOnCaseSeqNosMap;
    }
    
    public HashMap<Integer,List> getAllDefOnCaseSeqNosMap(){
        return allDefOnCaseSeqNosMap;
    }
    
    public HashMap<Integer,List> getAllDefendantsOnCaseSeqNosSortedMap(){
        if(allDefOnCaseSeqNosMap==null)
        {
            allDefOnCaseSeqNosMap = new HashMap<Integer,List>();
        }
        else
        {
            Iterator valuesIter =  allDefOnCaseSeqNosMap.values().iterator();
            while (valuesIter.hasNext()){
                List seqNoList = (List)valuesIter.next();
                Collections.sort(seqNoList);
            }
        }
        
        return allDefOnCaseSeqNosMap;
    }
    public void sortOffences(Collection offenceValues) {
        if (offenceValues instanceof List) {
            Sorter.sort((List) offenceValues, new String[] { "crestOffenceSeqNo" });
        }
    }

}