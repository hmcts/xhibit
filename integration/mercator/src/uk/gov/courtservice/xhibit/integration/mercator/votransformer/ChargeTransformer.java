package uk.gov.courtservice.xhibit.integration.mercator.votransformer;

import java.util.Vector;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSConfigurationException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.BreachMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ChargeBatchMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.ChargeMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.OffenceMVO;
import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.mercator.PrimaryKeysMVO;

/**
 * <p>
 * Title: ChargeTransformer
 * </p>
 * <p>
 * Description: This class is taking in a CSValueObject (ChargeValue) and
 * convert it to ChargeMVO
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

public class ChargeTransformer implements VOTransformer {
    // the logger
    private static Logger log = CSServices.getLogger(ChargeTransformer.class);

    public ChargeTransformer() {
        // Empty
    }

    public Object transformVO(Object valueObject) {
        if (valueObject == null) {
            return null;
        }

        if (valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue) {
            return transformSingleVO((ChargeValue) valueObject);
        } else if (valueObject instanceof uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue[]) {
            return transformBatchVOs((ChargeValue[]) valueObject);
        } else {
            throw new CSConfigurationException(
                    "The CSValueObject is not of the expected type ChargeValue. Class is of type :"
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

    private ChargeBatchMVO transformBatchVOs(ChargeValue[] chargeValues) {
        ChargeMVO[] chargeMVOs = new ChargeMVO[chargeValues.length];
        ChargeBatchMVO chargeBatchMVO = new ChargeBatchMVO();

        for (int i = 0; i < chargeValues.length; i++) {
            chargeMVOs[i] = transformSingleVO(chargeValues[i]);
        }

        chargeBatchMVO.setChargeMVO(chargeMVOs);

        return chargeBatchMVO;
    }

    private ChargeMVO transformSingleVO(ChargeValue chargeValue) {
        ChargeMVO chargeMVO = new ChargeMVO();

        OffenceTransformer offenceTransformer = null;
        BreachTransformer breachTransformer = null;

        BreachMVO breachMVO = null;
        OffenceMVO[] offenceMVOs = null;

        int numberOfOffences = 0;
        Vector vOffences = null;

        String prosPaperServedDate = null;
        String dateIndRec = null;
        String indSignedDate = null;

        // transform all the OffenceValues in a Collection to OffenceMVOs in
        // array
        if (chargeValue.getOffenceValues() != null) {
            // get the class transformer for Defendant
            offenceTransformer = new OffenceTransformer();

            // collection of defendants, cast to Vector
            vOffences = (Vector) chargeValue.getOffenceValues();

            // set the array to number of defendants in the collection
            numberOfOffences = vOffences.size();
            offenceMVOs = new OffenceMVO[numberOfOffences];

            // Loop through the vector and add the defendantMVO to the array
            for (int i = 0; i < numberOfOffences; i++) {
                offenceMVOs[i] = (OffenceMVO) offenceTransformer.transformVO(vOffences.get(i));
            }
            vOffences = null;
            numberOfOffences = 0;
        } else {
            log.debug("Charge Transform: ChargeValue contains no OffenceValues");
        }

        // get and transform the BreachValue
        if (chargeValue.getBreachValue() != null) {
            breachTransformer = new BreachTransformer();
            breachMVO = (BreachMVO) breachTransformer.transformVO(chargeValue.getBreachValue());
        } else {
            log.debug("Charge Transform: ChargeValue contains no BreachValue");
        }

        // convert the dates to Strings if they are not null
        if (chargeValue.getProsPaperServedDate() != null) {
            prosPaperServedDate = chargeValue.getProsPaperServedDate().getTime().toString();
        }

        if (chargeValue.getDateIndRec() != null) {
            dateIndRec = chargeValue.getDateIndRec().getTime().toString();
        }

        if (chargeValue.getIndSignedDate() != null) {
            indSignedDate = chargeValue.getIndSignedDate().getTime().toString();
        }

        // Set chargeMVO values

        if (chargeValue.getChargeID() != null) {
            chargeMVO.setChargeID(chargeValue.getChargeID().intValue());
        }
        if (chargeValue.getCaseID() != null) {
            chargeMVO.setCaseID(chargeValue.getCaseID().intValue());
        }
        chargeMVO.setChargeType(chargeValue.getChargeType());
        chargeMVO.setChargeTypeDescription(chargeValue.getChargeTypeDescription());
        if (chargeValue.getCrestChargeID() != null) {
            chargeMVO.setCrestChargeID(chargeValue.getCrestChargeID().intValue());
        }
        if (chargeValue.getCrestChargeSeqNo() != null) {
            chargeMVO.setCrestChargeSeqNo(chargeValue.getCrestChargeSeqNo().intValue());
        }
        chargeMVO.setProsPaperServedDate(prosPaperServedDate);
        chargeMVO.setBreachMVO(breachMVO);

        if (offenceMVOs != null) {
            chargeMVO.setOffenceMVO(offenceMVOs);
        }
        if (chargeValue.getDefendantID() != null) {
            chargeMVO.setDefendantID(chargeValue.getDefendantID().intValue());
        }
        if (chargeValue.getCourtID() != null) {
            chargeMVO.setCourtID(chargeValue.getCourtID().intValue());
        }
        chargeMVO.setDateIndRec(dateIndRec);
        chargeMVO.setIndSignedDate(indSignedDate);
        chargeMVO.setIndResp(chargeValue.getIndResp());
        chargeMVO.setDirty(chargeValue.isDirty());

        if (chargeValue.getVersion() != null) {
            chargeMVO.setVersion(chargeValue.getVersion().intValue());
        }
        if (chargeValue.getId() != null) {
            chargeMVO.setId(chargeValue.getId().intValue());
        }

        return chargeMVO;
    }
}