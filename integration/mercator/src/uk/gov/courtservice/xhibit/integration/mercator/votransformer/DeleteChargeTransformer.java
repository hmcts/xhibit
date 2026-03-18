package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DeleteChargeMVO;

/**
 * <p>
 * Title: DeleteChargeTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (DelChargeValue) and
 * convert it to DeleteChargeMVO
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

public class DeleteChargeTransformer implements VOTransformer {
    public DeleteChargeTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instanceof the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type DelChargeValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        DeleteChargeMVO deleteChargeMVO = new DeleteChargeMVO();
        DelChargeValue delChargeValue = (DelChargeValue) valueObject;

        // Set deleteChargeMVO values

        if (delChargeValue.getChargeID() != null) {
            deleteChargeMVO.setChargeID(delChargeValue.getChargeID().intValue());
        }
        if (delChargeValue.getCourtID() != null) {
            deleteChargeMVO.setCourtID(delChargeValue.getCourtID().intValue());
        }
        if (delChargeValue.getCaseID() != null) {
            deleteChargeMVO.setCaseID(delChargeValue.getCaseID().intValue());
        }
        if (delChargeValue.getCrestChargeSeqNo() != null) {
            deleteChargeMVO.setCrestChargeSeqNo(delChargeValue.getCrestChargeSeqNo().intValue());
        }
        if (delChargeValue.getCrestChargeID() != null) {
            deleteChargeMVO.setCrestChargeID(delChargeValue.getCrestChargeID().intValue());
        }
        deleteChargeMVO.setInCourt(delChargeValue.isInCourt());
        deleteChargeMVO.setDeleteResults(delChargeValue.isDeleteResults());

        if (delChargeValue.getVersion() != null) {
            deleteChargeMVO.setVersion(delChargeValue.getVersion().intValue());
        }
        if (delChargeValue.getId() != null) {
            deleteChargeMVO.setId(delChargeValue.getId().intValue());
        }

        return deleteChargeMVO;
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