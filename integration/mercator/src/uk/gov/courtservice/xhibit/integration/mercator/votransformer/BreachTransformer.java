package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.BreachMVO;

/**
 * <p>
 * Title: BreachTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (BreachValue) and
 * convert it to BreachMVO
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Marie Holmberg
 * @version 1.0
 * @author GJS - updated for generated MVOs
 * @version 1.1
 */

public class BreachTransformer implements VOTransformer {
    public BreachTransformer() {
        // empty
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instance of the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type BreachValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        BreachMVO breachMVO = new BreachMVO();

        BreachValue breachValue = (BreachValue) valueObject;

        String originalSentenceDate = null;
        String datePut = null;

        // Check if the Calendar Objects are null before taking out the time
        if (breachValue.getOriginalSentenceDate() != null) {
            originalSentenceDate = breachValue.getOriginalSentenceDate().getTime().toString();
        }

        if (breachValue.getDatePut() != null) {
            datePut = breachValue.getDatePut().getTime().toString();
        }

        // Set breachMVO values

        if (breachValue.getBreachID() != null) {
            breachMVO.setBreachID(breachValue.getBreachID().intValue());
        }
        if (breachValue.getCaseID() != null) {
            breachMVO.setCaseID(breachValue.getCaseID().intValue());
        }
        breachMVO.setOriginalSentence(breachValue.getOriginalSentence());
        breachMVO.setOriginalSentenceDate(originalSentenceDate);
        breachMVO.setOriginalCourtType(breachValue.getOriginalCourtType());
        if (breachValue.getOriginalCourtID() != null) {
            breachMVO.setOriginalCourtID(breachValue.getOriginalCourtID().intValue());
        }
        breachMVO.setDatePut(datePut);
        breachMVO.setBreachType(breachValue.getBreachType());
        breachMVO.setBringBack(breachValue.getBringBack());
        breachMVO.setHoCode(breachValue.getHoCode());
        breachMVO.setHoDescription(breachValue.getHoDescription());
        breachMVO.setInCourt(breachValue.isInCourt());
        breachMVO.setPlea(breachValue.getPlea());
        if (breachValue.getChargeID() != null) {
            breachMVO.setChargeID(breachValue.getChargeID().intValue());
        }
        if (breachValue.getRefSystemCodeID() != null) {
            breachMVO.setRefSystemCodeID(breachValue.getRefSystemCodeID().intValue());
        }
        breachMVO.setDirty(breachValue.isDirty());

        if (breachValue.getVersion() != null) {
            breachMVO.setVersion(breachValue.getVersion().intValue());
        }
        if (breachValue.getId() != null) {
            breachMVO.setId(breachValue.getId().intValue());
        }

        return breachMVO;
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