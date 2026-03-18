package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.UpdateCaseMVO;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: Transforms the UpdateCaseValue
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author Kelvin Davies
 * @version 1.0
 */

public class UpdateCaseTransform implements VOTransformer {
    public UpdateCaseTransform() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof CaseUpdateValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type UpdateCaseValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        UpdateCaseMVO updateCaseMVO = new UpdateCaseMVO();
        CaseUpdateValue updateCase = (CaseUpdateValue) valueObject;

        // Set UpdateCaseMVO values

        if (updateCase.getCaseNumber() != null) {
            updateCaseMVO.setCaseNumber(updateCase.getCaseNumber().intValue());
        }
        updateCaseMVO.setCaseType(updateCase.getCaseType());
        
        if (updateCase.getCourtID() != null) {
            updateCaseMVO.setCourtID(updateCase.getCourtID().intValue());
        }

        if (updateCase.getVersion() != null) {
            if(updateCase.getVersion().intValue() >= 0){
                updateCaseMVO.setVersion(updateCase.getVersion().intValue());
            }
        }
        if (updateCase.getId() != null) {
            updateCaseMVO.setId(updateCase.getId().intValue());
        }
        
        if(updateCase.getVulnerableVictimIndicator() != null){
            updateCaseMVO.setVulnerableVictimIndicator(updateCase.getVulnerableVictimIndicator());
        }

        return updateCaseMVO;
    }

    public Object transformOutput(Object result) {
        /**
         * @todo Implement this
         *       uk.gov.courtservice.xhibit.integration.mercator.votransformer.VOTransformer
         *       method
         */
        return result;
    }
}
