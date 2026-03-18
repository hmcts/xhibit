package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.DeleteOffenceMVO;

/**
 * <p>
 * Title: DeleteOffenceTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (DelOffenceValue) and
 * convert it to DeleteOffenceMVO
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

public class DeleteOffenceTransformer implements VOTransformer {
    // the logger
    private static Logger log = CSServices.getLogger(DeleteOffenceTransformer.class);

    public DeleteOffenceTransformer() {
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        // check the instanceof the CSValueObject before trying to cast it.
        if (!(valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue)) {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type DelOffenceValue. Class is of type :"
                            + valueObject.getClass().toString() + " with vos :" + valueObject.toString());
        }

        DeleteOffenceMVO deleteOffenceMVO = new DeleteOffenceMVO();
        DelOffenceValue delOffenceValue = (DelOffenceValue) valueObject;

        int[] defendantIDs = null;
        Vector vDefs = null;
        int numberOfDefs = 0;

        // check if there are any defendants - if so convert from Collection to
        // array.
        if (delOffenceValue.getDefendantIDs() != null) {
            vDefs = (Vector) delOffenceValue.getDefendantIDs();

            numberOfDefs = vDefs.size();

            defendantIDs = new int[numberOfDefs];

            for (int i = 0; i < numberOfDefs; i++) {
                defendantIDs[i] = ((Integer) vDefs.elementAt(i)).intValue();
            }

            vDefs = null;
            numberOfDefs = 0;
        } else {
            log.debug("DeleteOffence Transform: DelOffenceValue contains no DefendantIDs");
        }

        // Set deleteOffenceMVO values

        if (delOffenceValue.getOffenceID() != null) {
            deleteOffenceMVO.setOffenceID(delOffenceValue.getOffenceID().intValue());
        }
        if (delOffenceValue.getCourtID() != null) {
            deleteOffenceMVO.setCourtID(delOffenceValue.getCourtID().intValue());
        }
        if (delOffenceValue.getCaseID() != null) {
            deleteOffenceMVO.setCaseID(delOffenceValue.getCaseID().intValue());
        }
        if (delOffenceValue.getChargeID() != null) {
            deleteOffenceMVO.setChargeID(delOffenceValue.getChargeID().intValue());
        }
        if (defendantIDs != null) {
            deleteOffenceMVO.setDefendantIDs(defendantIDs);
        }

        deleteOffenceMVO.setInCourt(delOffenceValue.isInCourt());
        deleteOffenceMVO.setDeleteResults(delOffenceValue.isDeleteResults());

        if (delOffenceValue.getVersion() != null) {
            deleteOffenceMVO.setVersion(delOffenceValue.getVersion().intValue());
        }
        if (delOffenceValue.getId() != null) {
            deleteOffenceMVO.setId(delOffenceValue.getId().intValue());
        }

        return deleteOffenceMVO;
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