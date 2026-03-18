package uk.gov.courtservice.xhibit.client.actions.results;

import uk.gov.courtservice.framework.exception.CSRecoverableException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantOnCaseValue;
import uk.gov.courtservice.xhibit.client.results.disposals.AggravatingReasonsModel;
import uk.gov.courtservice.xhibit.client.results.disposals.DeportationModel;
import uk.gov.courtservice.xhibit.client.results.disposals.HateCrimeModel;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;

/**
 * Description: This class is used to contain methods/functions that are used within the 
 *              DisposalAction classes
 * @author davieskl
 * @version 1.0
 */
public class DisposalActionHelper  {

	private DefendantOnCaseValue getDefendantOnCaseValue(Integer defendantId, Integer caseId) throws DefendantControllerException {
		DefendantControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getDefendantDelegate();
        return delegate.getDefendantOnCaseDetails(defendantId, caseId);
	}
	
    /**
     * Description: Retreives the deportation details from the defendant on case record.
     * @return DeportationModel: deportation reason model
     */
    public DeportationModel getDeportationReasonFromDefendantOnCase(Integer defendantId, Integer caseId)
    throws CSRecoverableException {
        DeportationModel deportationReasons = new DeportationModel();
        
         try {
             DefendantOnCaseValue doc = getDefendantOnCaseValue(defendantId, caseId);
             DefendantOnCaseBasicValue docBasicValue = doc.getDefendantOnCaseBVO();
            
             deportationReasons.setCustodial(docBasicValue.getCustodial());
             deportationReasons.setSuspended(docBasicValue.getSuspended());
             deportationReasons.setSeriousDrugOffence(docBasicValue.getSeriousDrugOffence());
             deportationReasons.setRecommendedDeportation(docBasicValue.getRecommendedDeportation());
               
        } catch (DefendantControllerException e) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getDeportationReason", 
                    "Error in getDeportationReasonFromDefendantOnCase", e);
        }catch (Exception dce) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getDeportationReason", 
                    "Error in getDeportationReasonFromDefendantOnCase", dce);
        }
         
         return deportationReasons;
    }
    
    /**
     * Description: Retrieves the hate crime details from the defendant on case record.
     * @return HateCrimeModel: hate crime model
     */
    public HateCrimeModel getHateCrimeReasons(Integer defendantId, Integer caseId)
    throws CSRecoverableException {
        HateCrimeModel hateCrimeReasons = new HateCrimeModel();
        
         try {
        	 DefendantOnCaseValue doc = getDefendantOnCaseValue(defendantId, caseId);
    
             boolean hateCrimeFlag = false;
             if (doc != null) {
             	 
                 if (doc.getDefendantOnCaseBVO().getHateSentIndicator() != null) {
            		 if (doc.getDefendantOnCaseBVO().getHateSentIndicator().equals("Y")) {
            			 hateCrimeFlag = true;
            		 }
            	 }
                 
                 if (doc.getGeneralDisability() != null) {
                     hateCrimeReasons.setGeneralDisability(doc.getGeneralDisability());
                     hateCrimeFlag = true;
                 }
                 if (doc.getGeneralSexual() != null) {
                     hateCrimeReasons.setGeneralSexual(doc.getGeneralSexual());
                     hateCrimeFlag = true;
                 }
                 if (doc.getGeneralTransgender() != null) {
                     hateCrimeReasons.setGeneralTransgender(doc.getGeneralTransgender());
                     hateCrimeFlag = true;
                 }
                 if (doc.getRaceAndReligionAggravated() != null) {
                     hateCrimeReasons.setRaceAndReligionAggravated(doc.getRaceAndReligionAggravated());
                     hateCrimeFlag = true;
                 }
                 if (doc.getRacialAggravated() != null) {
                     hateCrimeReasons.setRacialAggravated(doc.getRacialAggravated());
                     hateCrimeFlag = true;
                 }
                 if (doc.getReligionAggravated() != null) {
                     hateCrimeReasons.setReligionAggravated(doc.getReligionAggravated());
                     hateCrimeFlag = true;
                 }
                 if (doc.getVictimDisability() != null) {
                     hateCrimeReasons.setVictimDisability(doc.getVictimDisability());
                     hateCrimeFlag = true;
                 }
                 if (doc.getVictimSexual() != null) {
                     hateCrimeReasons.setVictimSexual(doc.getVictimSexual());
                     hateCrimeFlag = true;
                 }
                 if (doc.getVictimTransgender() != null) {
                     hateCrimeReasons.setVictimTransgender(doc.getVictimTransgender());
                     hateCrimeFlag = true;
                 }
             }
             hateCrimeReasons.setHateCrimeFlag(hateCrimeFlag);
      
             
        } catch (DefendantControllerException e) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getHateCrimeReasons", 
                    "Error in getHateCrimeReasonsFromDefendantOnCase", e);
        }catch (Exception dce) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getHateCrimeReasons", 
                    "Error in getHateCrimeReasonsFromDefendantOnCase", dce);
        }
         
         return hateCrimeReasons;
    }
    
    /**
     * Description: Retrieves the aggravating reasons from the defendant on case record.
     * @return AggravatingReasonsModel: aggravating reason model
     */
    public AggravatingReasonsModel getAggravatingReasons(Integer defendantId, Integer caseId)
    throws CSRecoverableException {
        AggravatingReasonsModel model = new AggravatingReasonsModel();
        
         try {
        	 DefendantOnCaseValue doc = getDefendantOnCaseValue(defendantId, caseId);
    
             if (doc != null) {
                 if (doc.getAggravatingAssaultOnWorkers() != null) {
                     model.setAssaultOnWorkers(doc.getAggravatingAssaultOnWorkers());
                 }
                 if (doc.getAggravatingTerroristConnection() != null) {
                     model.setTerroristConnection(doc.getAggravatingTerroristConnection());
                 }
                 if (doc.getAggravatingEmergencyWorkers() != null) {
                     model.setEmergencyWorkers(doc.getAggravatingEmergencyWorkers());
                 }
                 if (doc.getAggravatingHostility() != null) {
                     model.setHostility(doc.getAggravatingHostility());
                 }
                 if (doc.getAggravatingSexualOrientation() != null) {
                     model.setSexualOrientation(doc.getAggravatingSexualOrientation());
                 }
                 if (doc.getAggravatingSexualOrientationOfVictim() != null) {
                     model.setSexualOrientationOfVictim(doc.getAggravatingSexualOrientationOfVictim());
                 }
                 if (doc.getAggravatingTransgender() != null) {
                     model.setTransgender(doc.getAggravatingTransgender());
                 }
                 if (doc.getAggravatingTransgenderOfVictim() != null) {
                     model.setTransgenderOfVictim(doc.getAggravatingTransgenderOfVictim());
                 }
             }
      
             
        } catch (DefendantControllerException e) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getAggravatingReasons", 
                    "Error in getAggravatingReasonsFromDefendantOnCase", e);
        }catch (Exception dce) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getAggravatingReasons", 
                    "Error in getAggravatingReasonsFromDefendantOnCase", dce);
        }
         
         return model;
    }
    
    /**
     * Description: Retrieves the hate indicator and this will determine whether user is able to select the panel in the disposal
     *              Id the case type is "A" for Appeal then this overrides it and disables the hate crime panel
     * @return boolean
     */
    public boolean getHateIndicator(String caseType, Integer defendantId, Integer caseId) throws CSRecoverableException {
        boolean hateIndicator = false;
        
         try {
             if (!caseType.equals("A")) {
    
            	 DefendantOnCaseValue doc = getDefendantOnCaseValue(defendantId, caseId);
                 DefendantOnCaseBasicValue docBasicValue = doc.getDefendantOnCaseBVO();
                if (docBasicValue.getHateIndicator() != null) {
                    hateIndicator = docBasicValue.getHateIndicator().equals("Y");
                }
             } else {
                 // Its an appeal case so will be false
             }
    
        } catch (DefendantControllerException e) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getHateIndicator", 
                    "Error in getHateIndicatorFromDefendantOnCase", e);
        }catch (Exception dce) {
            throw new CSRecoverableException(
                    "DisposalActionHelper.getHateIndicator", 
                    "Error in getHateIndicatorFromDefendantOnCase", dce);
        }
         
         return hateIndicator;
    }
    
    /**
     * Description: Determine whether user is able to select the panel in the disposal
     * @return boolean
     */
    public boolean getAggravatingIndicator() throws CSRecoverableException {
    	  // Always available
       	  return true; 
    }
}
