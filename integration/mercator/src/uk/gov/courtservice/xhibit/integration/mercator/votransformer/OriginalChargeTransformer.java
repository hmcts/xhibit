package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.Vector;
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeTypes;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OriginalChargeMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OriginalChargeBatchMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ChargeMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PrimaryKeysMVO;

/**
 * <p>
 * Title: OriginalChargeTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (OriginalChargeValue) and
 * convert it to OriginalChargeBatchMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2007
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version 1.0
 */

public class OriginalChargeTransformer implements VOTransformer {
    // the logger
    private static Logger log = CSServices.getLogger(OriginalChargeTransformer.class);

    public OriginalChargeTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        if (valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO[]) {
            return transformBatchVOs((OriginalChargeVO[]) valueObject);
        } else {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type OriginalChargeVO. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        if (result instanceof PrimaryKeysMVO) {
            return transformBatchOutput((PrimaryKeysMVO) result);
        } else {
            return result;
        }
    }

    private Integer[] transformBatchOutput(PrimaryKeysMVO primaryKeys) {
        int[] intArray = primaryKeys.getPrimaryKeys();
        Integer[] integerArray = new Integer[intArray.length];

        for (int i = 0; i < intArray.length; i++) {
            integerArray[i] = new Integer(intArray[i]);
        }
        return integerArray;
    }

    private OriginalChargeBatchMVO transformBatchVOs(OriginalChargeVO[] originalChargeValues) {
        OriginalChargeMVO[] originalChargeMVOs = new OriginalChargeMVO[originalChargeValues.length];
        OriginalChargeBatchMVO originalChargeBatchMVO = new OriginalChargeBatchMVO();

        for (int i = 0; i < originalChargeValues.length; i++) {
            originalChargeMVOs[i] = transformSingleVO(originalChargeValues[i]);
        }

        originalChargeBatchMVO.setOriginalChargeMVO(originalChargeMVOs);

        return originalChargeBatchMVO;
    }

    private OriginalChargeMVO transformSingleVO(OriginalChargeVO originalChargeValue) {
        
        log.info("Start Original Charge Transformer for Defendant: " + originalChargeValue.getDefendantId());  
        
        log.debug("Original Charge Transaction Type: " + originalChargeValue.getTrxCode());
        log.debug("Original Charge Case ID: " + originalChargeValue.getCaseId());
        log.debug("Original Charge Seq No: " + originalChargeValue.getSeqNo());
        log.debug("Original Charge Text: " + originalChargeValue.getOriginalCharge());
        log.debug("Original Charge DOO ID: " + originalChargeValue.getDefendantOnOffenceId());
        log.debug("Original Charge DOC ID: " + originalChargeValue.getDefendantOnCaseId());
        log.debug("Original Charge Offence ID: " + originalChargeValue.getOffenceId());
        log.debug("Original Charge ID: " + originalChargeValue.getChargeId());
        log.debug("Original Court ID: " + originalChargeValue.getCourtId());
        
        OriginalChargeMVO originalChargeMVO = new OriginalChargeMVO();
        
        ChargeTransformer chargeTransformer = new ChargeTransformer();
        
        Collection <OffenceValue>offenceValues = new Vector<OffenceValue>();
        Collection <Integer>defendantIDs       = new Vector<Integer>();
      
        ChargeValue charge = null;
        ChargeMVO chargeMVO = null;
        OffenceValue offence = null;
        
        DefendantOnOffenceComplexValue defendantOnOffenceValue = null;
        DefendantOnCaseBasicValue defendantOnCaseValue = null;
        
        chargeMVO = new ChargeMVO();

        if((originalChargeValue.getCaseId()!=null &&
           (ChargeTypes.getChargeType(originalChargeValue.getChargeType())!=null)))
        {
            log.debug("Create Original Charge: Case ID and Charge Type Available");
            charge = new ChargeValue(originalChargeValue.getCaseId(),ChargeTypes.getChargeType(originalChargeValue.getChargeType()));
        }
        else
        {
            log.debug("Update or Delete Original Charge: Case ID and Charge Type Not Available");
            charge = new ChargeValue(null,null);
        }
        
        charge.setDefendantID(originalChargeValue.getDefendantId());
 
        if(originalChargeValue.getCourtId()!=null)
        {
            charge.setCourtID(originalChargeValue.getCourtId());
        }
        
        if(originalChargeValue.getChargeId()!=null)
        {
            charge.setChargeID(originalChargeValue.getChargeId());
        }
        
        offence = new OffenceValue();
        
        if(originalChargeValue.getOffenceId()!=null)
        {
            offence.setOffenceID(originalChargeValue.getOffenceId()); 
        }
        if(originalChargeValue.getChargeId()!=null)
        {
            offence.setChargeID(originalChargeValue.getChargeId()); 
        }
        offence.setCrestOffenceFreeText(originalChargeValue.getOriginalCharge());
        
        if(originalChargeValue.getRefOffenceId()!=null)
        {
            offence.setRefOffenceID(originalChargeValue.getRefOffenceId());
        }
         
        defendantOnCaseValue = new DefendantOnCaseBasicValue();
        if(originalChargeValue.getDefendantOnCaseId()!=null)
        {
            defendantOnCaseValue.setId(originalChargeValue.getDefendantOnCaseId()); 
        }
        defendantOnCaseValue.setCaseID(originalChargeValue.getCaseId());
        defendantOnCaseValue.setDefendantID(originalChargeValue.getDefendantId());
           
        defendantOnOffenceValue = new DefendantOnOffenceComplexValue();  
        if(originalChargeValue.getDefendantOnOffenceId()!=null)
        {
            defendantOnOffenceValue.setDefendantOnOffenceId(originalChargeValue.getDefendantOnOffenceId());
        }
        defendantOnOffenceValue.setDefendantOnCaseId(originalChargeValue.getDefendantOnCaseId());
        defendantOnOffenceValue.setSeqNo(originalChargeValue.getSeqNo());
        
        defendantOnOffenceValue.setDefendantOnCase(defendantOnCaseValue);
                
        offence.addDefOnOffenceComplexValue(originalChargeValue.getDefendantId(), defendantOnOffenceValue);
        
        if(originalChargeValue.getDefendantId()!=null)
        {
            defendantIDs.add(originalChargeValue.getDefendantId());
            offence.setDefendantIDs(defendantIDs);
        }
        
        offenceValues.add(offence);
        
        charge.setOffenceValues(offenceValues);
        
        chargeMVO = (ChargeMVO)chargeTransformer.transformVO(charge);
        
        originalChargeMVO.setChargeMVO(chargeMVO);
        originalChargeMVO.setTrxCode(originalChargeValue.getTrxCode());
        
        if (originalChargeValue.getVersion() != null) {
            originalChargeMVO.setVersion(originalChargeValue.getVersion().intValue());
        }
        if (originalChargeValue.getId() != null) {
            originalChargeMVO.setId(originalChargeValue.getId().intValue());
        }
        
        log.info("End Original Charge Transformer for Defendant: " + originalChargeValue.getDefendantId());  
        
        return originalChargeMVO;
    }
}