package uk.gov.courtservice.xhibit.integration.services.prehearing;

// JDK
import java.util.Collection;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.vos.services.charge.BreachValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.CaseUpdateValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelChargeValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.DelOffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.LinkCountDefValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.OffenceValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.SignIndValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.OriginalChargeVO;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.OutputTransformationException;
import uk.gov.courtservice.xhibit.integration.mercator.votransformer.TransformationException;
import uk.gov.courtservice.xhibit.integration.services.IntController;
import uk.gov.courtservice.xhibit.integration.services.MercatorException;

/**
 * <p>
 * Title: PreHearingUpdateIntController
 * </p>
 * <p>
 * Description: This interface is managing the databases updates (CREST and
 * XHBIT) for pre-hearing related data.
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Abdul Rahim Hussain
 * @author Cag Onganer
 * @version 1.1
 */

public class PreHearingUpdateIntController extends IntController {
    private static Logger log = CSServices.getLogger(PreHearingUpdateIntController.class);

    /**
     * @roseuid 3DDBB301039C
     */
    public PreHearingUpdateIntController() {
        super();
    }

    /**
     * This method is used to add an offence to the current charge.
     * 
     * @param offenceValue
     * @roseuid 3DDB6AA1002E
     */
    public Integer addOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("addOffence Input (offenceValue):" + offenceValue.toString());
        String extraInfo = (String) executeUpdate("addOffence", offenceValue);
        Integer id = null;

        if (extraInfo != null) {
            try {
                id = new Integer(extraInfo);
            } catch (NumberFormatException ex) {
                // extraInfo contains something other than an integer
                log.debug("examineReturnValue: (NumberFormatException) extraInfo= " + extraInfo);
            }
        }
        return id;

    }

    /**
     * This method is used to add an offence to the joinder
     * 
     * @param offenceValue
     * @roseuid 3DDB6AA1002E
     */
    public Integer addJoinderOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("addJoinderOffence Input (offenceValue):" + offenceValue.toString());
        String extraInfo = (String) executeUpdate("addJoinderOffence", offenceValue);
        Integer id = null;

        if (extraInfo != null) {
            try {
                id = new Integer(extraInfo);
            } catch (NumberFormatException ex) {
                // extraInfo contains something other than an integer
                log.debug("examineReturnValue: (NumberFormatException) extraInfo= " + extraInfo);
            }
        }
        return id;

    }

    /**
     * This method deletes the offence corresponding to the offence ID passed as
     * parameter.
     * 
     * @param offenceChargeID
     * @roseuid 3DDB6AA10042
     */
    public void deleteOffence(DelOffenceValue delOffenceVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("delOffenceVal Input (delOffenceVal):" + delOffenceVal.toString());
        executeUpdate("deleteOffence", delOffenceVal);
    }

    /**
     * This method signs the indictment passed as parameter.
     * 
     * @roseuid 3DDB6AA10056
     */
    public void signIndictment(SignIndValue signIndVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("signIndictment Input (SignIndValue):" + signIndVal.toString());
        executeUpdate("signIndictment", signIndVal);
    }

    /**
     * This method updates the breach passed as parameter.
     * 
     * @param breachValue
     * @roseuid 3DDB6AA1006A
     */
    public void updateBreach(BreachValue breachValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("updateBreach Input (breachValue):" + breachValue.toString());
        executeUpdate("updateBreach", breachValue);
    }
    
    /**
     * This method updates the case passed as parameter.
     * 
     * @param caseValue
     */
    public void updateCase(CaseUpdateValue caseUpdateValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("updateCase Input (caseUpdateValue):" + caseUpdateValue.toString());
        executeUpdate("updateCase", caseUpdateValue);
    }

    /**
     * This method updates the offence passed as parameter.
     * 
     * @param offenceValue
     * @roseuid 3DDB6AA1007E
     */
    public void updateOffence(OffenceValue offenceValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("updateOffence Input (offenceValue):" + offenceValue.toString());
        executeUpdate("updateOffence", offenceValue);
    }

    /**
     * This method is used to add a charge to the current case.
     * 
     * @param chargeValue
     * @roseuid 3DDB6AA10092
     */
    public Integer addChargeToCase(ChargeValue chargeValue) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("addChargeToCase Input (chargeValue):" + chargeValue.toString());
        String extraInfo = (String) executeUpdate("addChargeToCase", chargeValue);
        Integer id = null;

        if (extraInfo != null) {
            try {
                id = new Integer(extraInfo);
            } catch (NumberFormatException ex) {
                // extraInfo contains something other than an integer, but this
                // is valid
                // for iteration 2a so just log it for now
                log.debug("examineReturnValue: (NumberFormatException) extraInfo= " + extraInfo);
            }
        }
        return id;

    }

    /**
     * This method is used to add a joinder charge to the current case.
     * 
     * @param chargeValue
     * @roseuid 3DDB6AA10092
     */
    public Integer[] addJoinderChargeToCase(ChargeValue[] chargeValue) throws MercatorException,
            TransformationException, OutputTransformationException {
        log.debug("addJoinderChargeToCase Input (chargeValue):" + chargeValue.toString());
        Integer[] chargeIds = null;

        Object chargeObjId = executeUpdate("addJoinderChargeToCase", chargeValue);

        if (chargeObjId != null) {
            if (chargeObjId instanceof String) {

                try {
                    chargeIds[0] = new Integer((String) chargeObjId);
                } catch (NumberFormatException ex) {
                    // extraInfo contains something other than an integer,
                    // but this is valid
                    // for iteration 2a so just log it for now
                    log.debug("examineReturnValue: (NumberFormatException) extraInfo= " + chargeObjId);
                }
            } else {
                if (chargeObjId instanceof Integer[]) {
                    chargeIds = (Integer[]) chargeObjId;
                }
            }
        }
        return chargeIds;

    }

    /**
     * This method deletes the charge corresponding to the charge ID passed as
     * parameter.
     * 
     * @roseuid 3DDB6AA100A6
     */
    public void deleteCharge(DelChargeValue delChargeVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("deleteCharge Input (delChargeVal):" + delChargeVal.toString());
        executeUpdate("deleteCharge", delChargeVal);
    }

    public void exportCharges(Integer caseId) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("exportCharges Input (caseId):" + caseId.toString());
        // executeUpdate("exportCharges",caseId);
    }

    public void linkCountsAndDefendants(LinkCountDefValue linkVal) throws MercatorException, TransformationException,
            OutputTransformationException {
        log.debug("linkCountsAndDefendants Input (linkVal):" + linkVal.toString());
        executeUpdate("linkCountsAndDefendants", linkVal);
    }

    public void updateDefendantOnCountStatus(Collection defendantCountList, String newStatus) throws MercatorException,
            TransformationException, OutputTransformationException {
        log.debug("updateDefendantOnCountStatus Input (defendantCountList):" + defendantCountList.toString());
        // executeUpdate("updateDefendantOnCountStatus",defendantCountList);
    }
    
    /**
     * This method is used to maintain a batch of Original Charges.
     * 
     * @param originalChargeValues
     * @roseuid 3DDB6AA10092
     */
    public Integer[] maintainOriginalCharge(OriginalChargeVO[] originalChargeValues) 
        throws MercatorException, TransformationException, OutputTransformationException    
    {
        log.debug("maintainOriginalCharge Input (originalChargeValues):" + originalChargeValues.toString());
        Integer[] defOnOffenceIds = null;

        Object origChargeObj = executeUpdate("maintainOriginalCharge", originalChargeValues);

        if (origChargeObj != null) {
            if (origChargeObj instanceof Integer[]) {
                defOnOffenceIds = (Integer[]) origChargeObj;
            }
        }
        
        if(defOnOffenceIds!=null && defOnOffenceIds.length>0)
        {
            for(int i=0;i<defOnOffenceIds.length;i++)
            {
                log.debug("maintainOriginalCharge: defOnOffenceId:" + defOnOffenceIds[i]);
            }
        }
        else
        {
            log.debug("maintainOriginalCharge: No defOnOffenceIds returned");
        }
        
        return defOnOffenceIds;
    }  

    /**
     * This method is used to update a Defendant On Offence
     * 
     * @param linkCountDefValue
     * @roseuid 3DDB6AA10092
     */
    public void updateDefendantOnOffence(LinkCountDefValue linkCountDefValue) 
        throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("updateDefendantOnOffence Input (linkCountDefValue):" + linkCountDefValue.toString());
        executeUpdate("updateDefendantOnOffence", linkCountDefValue);    
    }
    
    
    /**
     * This method is used to update a Defendant On Offence and Offence
     * 
     * @param offenceValue
     * @roseuid 3DDB6AA10092
     */
    public void updateOffenceAndDefOnOffence(OffenceValue offenceValue) 
        throws MercatorException, TransformationException, OutputTransformationException
    {
        log.debug("updateOffenceAndDefOnOffence Input (offenceValue):" + offenceValue.toString());
        executeUpdate("updateOffenceAndDefOnOffence", offenceValue);    
    }    
}
