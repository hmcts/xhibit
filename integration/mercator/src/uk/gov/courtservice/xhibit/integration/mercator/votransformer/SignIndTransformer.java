package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.SignIndMVO;

/**
 * <p>
 * Title: SignIndTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (SignIndValue) and
 * convert it to SignIndMVO
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

public class SignIndTransformer implements VOTransformer {
    public SignIndTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instanceof the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type SignIndValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        SignIndMVO signIndMVO = new SignIndMVO();
        SignIndValue signIndValue = (SignIndValue) valueObject;

        String signedDate = null;

        // Check if date is null and if not convert to String
        if (signIndValue.getIndSignedDate() != null) {
            signedDate = signIndValue.getIndSignedDate().getTime().toString();
        }

        // Set signIndMVO values

        if (signIndValue.getChargeID() != null) {
            signIndMVO.setChargeID(signIndValue.getChargeID().intValue());
        }
        if (signIndValue.getNumberOfDays() != null) {
            signIndMVO.setNumberOfDays(signIndValue.getNumberOfDays().intValue());
        }
        if (signIndValue.getCourtID() != null) {
            signIndMVO.setCourtID(signIndValue.getCourtID().intValue());
        }
        if (signIndValue.getCaseID() != null) {
            signIndMVO.setCaseID(signIndValue.getCaseID().intValue());
        }
        signIndMVO.setIndSignedDate(signedDate);
        signIndMVO.setInCourt(signIndValue.isInCourt());
        signIndMVO.setSignOutOfTime(signIndValue.isSignOutOfTime());

        if (signIndValue.getVersion() != null) {
            signIndMVO.setVersion(signIndValue.getVersion().intValue());
        }
        if (signIndValue.getId() != null) {
            signIndMVO.setId(signIndValue.getId().intValue());
        }

        return signIndMVO;
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