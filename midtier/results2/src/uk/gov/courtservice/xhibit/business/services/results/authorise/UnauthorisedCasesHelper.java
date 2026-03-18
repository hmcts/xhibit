package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;

/**
 * <p>
 * Title: UnauthorisedCasesHelper
 * </p>
 * <p>
 * Description: This class provides methods to establish whether a case requires authorisation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class UnauthorisedCasesHelper {
    private static final Logger LOG = CSServices.getLogger(UnauthorisedCasesHelper.class);
    
    private Integer caseId;    
    private ResultsCompositeValue rcv = null;
    
    public UnauthorisedCasesHelper(Integer caseId){
        this.caseId = caseId;
        LOG.debug("constructor"+caseId.toString());
    }
    
    /**
     * This accessor method only creates a ResultsCompositeValue when it is required to avoid
     * unnecassary processing.
     * @return ResultCompositeValue
     * @throws ResultsControllerException
     */
    private ResultsCompositeValue getRcv() throws ResultsControllerException{
        if(rcv == null){
            //new rcv
            rcv = Results2WorkFlow.getResults(caseId, null);
        }
        
        return rcv;             
    }
    
    /**
     * This method takes a defendantId and calculates whether it has a disposal against all offences.
     * If an offence is found without a disposal then the method returns false immediately to avoid 
     * any unnecassary processing. True is only returned if every offence for this defendant has a disposal
     * 
     * @param defId
     * @return boolean
     * @throws ResultsControllerException
     */
    public boolean deftHasDisposalAgainstAllCounts(Integer defId)throws ResultsControllerException{
        LOG.debug("Processing: caseId:"+caseId.toString()+". defId:"+defId.toString());
        boolean defOkToShow = false;
        ChargeCompositeValue ccv = getRcv().getChargeCompositeValue();
        Collection charges = ccv.getCharges();
        Iterator chargeIter = charges.iterator();
        while (chargeIter.hasNext()) {
            ChargeValue charge = (ChargeValue) chargeIter.next();
            if(chargeTypeMustHaveDisposals(charge.getChargeType())){
                Collection offences = charge.getOffenceValues();
                Iterator offenceIter = offences.iterator();
                while (offenceIter.hasNext()) {
                    OffenceValue offence = (OffenceValue) offenceIter.next();
                    Collection defendantIds = offence.getDefendantIDs();
                    Iterator defendantIter = defendantIds.iterator();
                    while (defendantIter.hasNext()) {
                        Integer defendantId = (Integer) defendantIter.next();
                        if(defendantId.equals(defId)){
                            Integer dofId = offence.getDefendantOnOffence(defendantId).getDefendantOnOffenceId();
                            boolean returnValue = (rcv.getDisposalCount(dofId) > 0);
                            if (LOG.isDebugEnabled()) {
                                LOG.debug("CaseID: "+caseId+". ChargeType: "+charge.getChargeType()+"DefendantOnOffence: "+dofId+", has disposals:"+returnValue);
                            }
                            if(!returnValue){
                                return false;
                            }else{
                                defOkToShow=true;
                            }                        
                        }                
                    }
                }
            }
        }
        return defOkToShow;
    }
    
    private boolean chargeTypeMustHaveDisposals(String chargeType){
        return (chargeType.equals(ChargeTypes.INDICTMENT.getChargeType())||
                chargeType.equals(ChargeTypes.COMMITAL_FOR_SENTENCE.getChargeType())||
                chargeType.equals(ChargeTypes.CRIMINAL_APPEAL.getChargeType())||
                chargeType.equals(ChargeTypes.SECTION_41.getChargeType())||
                chargeType.equals(ChargeTypes.BREACH.getChargeType())||
                chargeType.equals(ChargeTypes.FAIL2APPEAR.getChargeType()));
        }    
}
