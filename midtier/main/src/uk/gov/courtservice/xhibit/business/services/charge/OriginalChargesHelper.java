package uk.gov.courtservice.xhibit.business.services.charge;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.originalcharges.OriginalChargesDatabaseManager;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseHelper;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.ChargeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantOnCaseVO;

public class OriginalChargesHelper {
    private static final Logger log = CSServices.getLogger(OriginalChargesHelper.class);
    
    private OriginalChargesDatabaseManager originalChargesDBM;
    private DefendantOnCaseHelper defendantOnCaseHelper = new DefendantOnCaseHelper();

    public OriginalChargesHelper() {
        originalChargesDBM = new OriginalChargesDatabaseManager();
    }

    public DefendantChargesCompositeVO[] getDefendantChargesByCaseId(Integer caseId, String chargeType) {
        log.info("***** DefendantChargesCompositeVO[] getDefendantChargesByCaseId(" + caseId + ", " + chargeType + ")");
        
        DefendantOnCaseVO[] defendantOnCaseVOArray = 
            defendantOnCaseHelper.getDefendantsOnCaseByCaseId(originalChargesDBM, caseId);

        DefendantChargesCompositeVO[] defendantChargesCVOArray = new DefendantChargesCompositeVO[defendantOnCaseVOArray.length];
        
        for( int x = 0; x < defendantOnCaseVOArray.length; x++ ) {
            defendantChargesCVOArray[x] = 
                new DefendantChargesCompositeVO(
                    defendantOnCaseVOArray[x],
                    getChargesByDefendantOnCaseId(
                        defendantOnCaseVOArray[x].getDefendantOnCaseId(),
                        chargeType
                    ),
                    getObsoleteChargesByDefendantOnCaseId(
                        defendantOnCaseVOArray[x].getDefendantOnCaseId(),
                        chargeType
                    )
                );
        }
        
        return defendantChargesCVOArray;
    }

    public ChargeVO[] getChargesByDefendantOnCaseId(Integer defendantOnCaseId, String chargeType) {
        log.info("***** ChargeVO[] getChargesByDefendantOnCaseId(" + defendantOnCaseId + ", " + chargeType + ")");
        
        return originalChargesDBM.getCharges(
            defendantOnCaseId,
            chargeType
        );
    }    

    public ChargeVO[] getObsoleteChargesByDefendantOnCaseId(Integer defendantOnCaseId, String chargeType) {
        log.info("***** ChargeVO[] getObsoleteChargesByDefendantOnCaseId(" + defendantOnCaseId + ", " + chargeType + ")");
        
        return originalChargesDBM.getObsoleteCharges(
            defendantOnCaseId,
            chargeType
        );
    }    
}
