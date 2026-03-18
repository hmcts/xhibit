package uk.gov.courtservice.xhibit.business.services.results.authorise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.ResultsStoredProcDatabase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCase;
import uk.gov.courtservice.xhibit.business.entities.xhb_case.XhbCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendant;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant.XhbDefendantBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_defendant_on_case.XhbDefendantOnCaseBeanHelper2;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.charge.ChargeControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantHelper;
import uk.gov.courtservice.xhibit.business.services.results.Results2WorkFlow;
import uk.gov.courtservice.xhibit.business.services.results.ResultsControllerException;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.originalcharge.DefendantChargesCompositeVO;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.AuthorisationValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.CaseAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.D20OffenceLinkReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantAuthorisationReturnValue;
import uk.gov.courtservice.xhibit.common.results.vos.authorise.DefendantOnCaseAuthorisationReturnValue;

/**
 * <p>
 * Title: Helper
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rakesh Lakhani
 * @version $Id: AuthorisationHelper.java,v 1.14 2011/06/15 10:09:47 atwells Exp $
 * @history Kelvin Davies - CCN1263 - 21/04/2009 - Updated to include a new method that saves defendant records via mercator 
 */
public class AuthorisationHelper {
    private final XhbCase caseValue;

    private final HashMap<Integer, DefendantAuthorisationReturnValue> authReturnMap = 
        new HashMap<Integer, DefendantAuthorisationReturnValue>();

    private final HashMap<Integer, DefendantOnCaseAuthorisationReturnValue> defOnCaseAuthReturnMap = 
        new HashMap<Integer, DefendantOnCaseAuthorisationReturnValue>();
    
    private ArrayList<Integer> offencesNotToDisplay = new ArrayList<Integer>();
    
    private final CaseAuthorisationReturnValue caseAuth = new CaseAuthorisationReturnValue();
    
    private final D20OffenceLinkReturnValue offenceLink = new D20OffenceLinkReturnValue();

    private final ResultsCompositeValue rcv;
    
    private Map<Integer,DefendantChargesCompositeVO> defChargesCompVOMap = new HashMap<Integer,DefendantChargesCompositeVO>();

    private static final Logger log = CSServices.getLogger(AuthorisationHelper.class);
    
    private ResultsStoredProcDatabase resultsDb = new ResultsStoredProcDatabase();
    
    public AuthorisationHelper(Integer caseId, Integer scheduledHearingId, AuthorisationValue[] authValues)
            throws ResultsControllerException {
        caseValue = XhbCaseBeanHelper2.findByPrimaryKey(caseId);

        List<DefendantChargesCompositeVO>  tempDefChargesCompVOList = new ArrayList<DefendantChargesCompositeVO>();
        
        try
        {
            DefendantChargesCompositeVO[] defChargesCompVO = getChargesDelegate().getDefendantChargesByCaseId(caseValue.getCaseId(),null);
            tempDefChargesCompVOList = Arrays.asList(defChargesCompVO);
        }
        catch (ChargeControllerException cce) {
            CSServices.getDefaultErrorHandler().handleError(cce, AuthorisationHelper.class);
            throw new ResultsControllerException(cce.getUserMessageAsMessage().getKey(), cce.getMessage(), cce);
        } 
        
        for (int i = 0; i < authValues.length; i++) {
            authReturnMap.put(authValues[i].getDefendantOnCaseId(), getDefAuthReturnValue(authValues[i]));
            defOnCaseAuthReturnMap
                    .put(authValues[i].getDefendantOnCaseId(), getDefOnCaseAuthReturnValue(authValues[i]));
        }

        for(DefendantChargesCompositeVO defChargesCompVOEntry:tempDefChargesCompVOList)
        {
            if(authReturnMap.containsKey(defChargesCompVOEntry.getDefendantOnCase().getDefendantOnCaseId()))
            {
                log.debug("Found defOnCaseId match: " + defChargesCompVOEntry.getDefendantOnCase().getDefendantOnCaseId());
                defChargesCompVOMap.put(defChargesCompVOEntry.getDefendantOnCase().getDefendantOnCaseId(),defChargesCompVOEntry);
            }
        }
        
        rcv = Results2WorkFlow.getResults(caseId, scheduledHearingId);
    }

    public DefendantAuthorisationReturnValue[] getDefAuthReturnValues() {
        Collection<DefendantAuthorisationReturnValue> c = authReturnMap.values();
        return c.toArray(new DefendantAuthorisationReturnValue[c.size()]);
    }

    public DefendantOnCaseAuthorisationReturnValue[] getDefOnCaseAuthReturnValues() {
        Collection<DefendantOnCaseAuthorisationReturnValue> c = defOnCaseAuthReturnMap.values();
        return c.toArray(new DefendantOnCaseAuthorisationReturnValue[c.size()]);
    }

    public CaseAuthorisationReturnValue getCaseAuthorisationReturnValue() {
        return caseAuth;
    }

    public boolean processDefendant(final Integer defendantOnCaseId) {
        return authReturnMap.containsKey(defendantOnCaseId);
    }

    public boolean processDefOnCase(final Integer defendantOnCaseId) {
        return defOnCaseAuthReturnMap.containsKey(defendantOnCaseId);
    }

    public ResultsCompositeValue getResultsCompositeValue() {
        return rcv;
    }

    public D20OffenceLinkReturnValue getD20OffenceLinkReturnValue() {
        return offenceLink;
    }
    
    
    
    private DefendantAuthorisationReturnValue getDefAuthReturnValue(AuthorisationValue authValue) {
        return new DefendantAuthorisationReturnValue(authValue.getDefendant(), authValue.getDefendantOnCaseId(),
                authValue.getResultsAuthorised(), authValue.getAmendedReason());
    }

    private DefendantOnCaseAuthorisationReturnValue getDefOnCaseAuthReturnValue(AuthorisationValue authValue) {
        return new DefendantOnCaseAuthorisationReturnValue(authValue.getDefendant(), authValue.getDefendantOnCaseId(),
                authValue.getResultsAuthorised(), authValue.getAmendedReason());
    }

    public String getCaseType() {
        return caseValue.getCaseType();
    }

    public Integer getCaseId() {
        return caseValue.getCaseId();
    }
    
    public String getCaseSubType() {
        return caseValue.getCaseSubType();
    }

    public XhbCase getXhbCase() {
        return caseValue;
    }

    public Map <Integer,DefendantChargesCompositeVO> getDefendantChargesOnCase() {
        return defChargesCompVOMap;
    } 

    public DefendantChargesCompositeVO getDefendantChargeOnCase(Integer docId) {
        return defChargesCompVOMap.get(docId);
    }
    
    public DefendantAuthorisationReturnValue getDefAuthReturnValue(Integer defendantOnCaseId) {
        if (processDefendant(defendantOnCaseId)) {
            return authReturnMap.get(defendantOnCaseId);
        } else {
            throw new CSUnrecoverableException("Defendant Id passed in is invalid");
        }
    }

    public DefendantOnCaseAuthorisationReturnValue getDefOnCaseAuthReturnValue(final Integer defendantOnCaseId) {
        if (processDefOnCase(defendantOnCaseId)) {
            return defOnCaseAuthReturnMap.get(defendantOnCaseId);
        } else {
            throw new CSUnrecoverableException("Defendant Id passed in is invalid");
        }
    }
    
    private ChargeControllerBeanBusinessDelegate getChargesDelegate(){
        return ChargeControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }
    
    /**
     * Determines whether there any unrelated disposals for the Defendant On Case
     * 
     * @param defendantId
     * @return boolean
     */
    public boolean isUnrelatedDisposalsOnCase(final Integer defendantId) {
        
        log.debug("defendantId:" + defendantId + " caseId: " + getCaseId());
        
        Integer defOnCaseId = null;
        boolean isUnrelatedDisposalsOnCase = false;
        
        if(defendantId==null)
        {
            throw new CSUnrecoverableException("isUnrelatedDisposalsOnCase: Defendant Id passed in is invalid");
        }
        
        Collection<DefendantChargesCompositeVO> defChargesCompVOCollection = defChargesCompVOMap.values();
        
        for(DefendantChargesCompositeVO defChargesCompVOEntry:defChargesCompVOCollection)
        {
            if(defChargesCompVOEntry.getDefendantOnCase().getCaseId().equals(getCaseId()) &&
               defChargesCompVOEntry.getDefendantOnCase().getDefendantId().equals(defendantId))
            {
                defOnCaseId = defChargesCompVOEntry.getDefendantOnCase().getDefendantOnCaseId();
                log.debug("Found defOnCaseId: " + defOnCaseId + " for defendantId:" + defendantId + " caseId: " + getCaseId());
                break;
            }
        }
        
        if(defOnCaseId!=null)
        {
            int numberOfUnrelatedDisposals = resultsDb.getNumberOfUnrelatedDisposals(defOnCaseId);
            log.debug("numberOfUnrelatedDisposals: " + numberOfUnrelatedDisposals + " for defOnCaseId: " + defOnCaseId + ",defendantId:" + defendantId + ",caseId: " + getCaseId());
            isUnrelatedDisposalsOnCase = numberOfUnrelatedDisposals>0;
        }
        else
        {
            log.info("No defOnCaseId found for defendantId:" + defendantId + " caseId: " + getCaseId());
        }
        
        log.debug("isUnrelatedDisposalsOnCase: " + isUnrelatedDisposalsOnCase + " for defOnCaseId: " + defOnCaseId + ",defendantId:" + defendantId + ",caseId: " + getCaseId());
        
        return isUnrelatedDisposalsOnCase;
    }
    
    /**
     * Description: This method takes a caseId and defId and updates the record.  This method is used by the AuthorisationWorkFlow.java 
     * @param authorisationValue
     * @param caseId
     * @param defId
     */
    public void saveDefendant(String resultsVerified, Integer caseId, Integer defId, String amendedReason){
        DefendantHelper temp = new DefendantHelper();
        try{
            DefendantValue defV = temp.getDefendantDetailsWithAddress(defId, caseId);
            //Update Values
            defV.getDefOnCaseBasicValue().setResultsVerified(resultsVerified);
            Calendar cal = Calendar.getInstance();
            //RFC2878 -Only set DateExported if it has never previously been set
            //amendedDateExported now to be set each time
            if(defV.getDefOnCaseBasicValue().getDateExported() == null){
                defV.getDefOnCaseBasicValue().setDateExported(cal);
            }
            defV.getDefOnCaseBasicValue().setAmendedDateExported(cal);
            log.debug("Setting amendedReason to " +defV.getDefOnCaseBasicValue().getAmendedReason());
            defV.getDefOnCaseBasicValue().setAmendedReason(amendedReason);
            
           // IntegrationFacade intFacade = IntegrationFacadeFactory.getInstance().getIntegrationFacade();
            try {
               // intFacade.updateDefendant(defV);
             	XhbDefendantOnCaseBasicValue defOnCaseBV = new XhbDefendantOnCaseBasicValue();
             	DefendantOnCaseBasicValue defOnCaseBasicValue = defV.getDefOnCaseBasicValue();
             	
             	defOnCaseBV.setDefendantOnCaseId(defOnCaseBasicValue.getDefendantOnCaseId());
             	if (defOnCaseBasicValue.getNoOfTICs() != null) {
             		defOnCaseBV.setNoOfTics(defOnCaseBasicValue.getNoOfTICs().shortValue());
             	}
             	if (defOnCaseBasicValue.getFinalDrivingLicenceStatus() != null) {
             		defOnCaseBV.setFinalDrivingLicenceStatus(defOnCaseBasicValue.getFinalDrivingLicenceStatus().byteValue());
             	}
             	defOnCaseBV.setPtiurn(defOnCaseBasicValue.getPtiurn());
             	defOnCaseBV.setIsJuvenile(defOnCaseBasicValue.getIsJuvenile());
             	defOnCaseBV.setIsMasked(defOnCaseBasicValue.getIsMasked());
             	defOnCaseBV.setMaskedName(defOnCaseBasicValue.getMaskedName());
             	
             	defOnCaseBV.setCaseId(defOnCaseBasicValue.getCaseID());
             	defOnCaseBV.setDefendantId(defOnCaseBasicValue.getDefendantID());
             	defOnCaseBV.setVersion(defOnCaseBasicValue.getVersion());
             	defOnCaseBV.setObsInd(defOnCaseBasicValue.getObsInd());
             	
             	if (defOnCaseBasicValue.getDateOfCommittal() != null) {
             		defOnCaseBV.setDateOfCommittal(defOnCaseBasicValue.getDateOfCommittal().getTime());
             	}
             	defOnCaseBV.setPncId(defOnCaseBasicValue.getPncId());
             	defOnCaseBV.setCollectMagistrateCourtId(defOnCaseBasicValue.getCollectMagistrateCourtId());
             	defOnCaseBV.setCurrentBcStatus(defOnCaseBasicValue.getCurrentBcStatus());
             	
             	defOnCaseBV.setAsn(defOnCaseBasicValue.getAsn());
             	defOnCaseBV.setBenchWarrantExecDate(defOnCaseBasicValue.getBenchWarrantExecDate());
             	defOnCaseBV.setCommBcStatus(defOnCaseBasicValue.getCommBcStatus());
             	defOnCaseBV.setBcStatusBwExecuted(defOnCaseBasicValue.getBcStatusBwExecuted());
             	
             	if (defOnCaseBasicValue.getDateExported() != null) {
             		log.debug("saveDefendant Date Exported will be set to " + defOnCaseBasicValue.getDateExported().getTime());
             		defOnCaseBV.setDateExported(defOnCaseBasicValue.getDateExported().getTime());
             	}
             	defOnCaseBV.setCustodial(defOnCaseBasicValue.getCustodial());
             	defOnCaseBV.setSuspended(defOnCaseBasicValue.getSuspended());
             	defOnCaseBV.setSeriousDrugOffence(defOnCaseBasicValue.getSeriousDrugOffence());
             	defOnCaseBV.setRecommendedDeportation(defOnCaseBasicValue.getRecommendedDeportation());
             	
             	defOnCaseBV.setNationality(defOnCaseBasicValue.getNationality());
             	defOnCaseBV.setPublicDisplayHide(defOnCaseBasicValue.getPublicDisplayHide());
             	if (defOnCaseBasicValue.getAmendedDateExported() != null) {
             		defOnCaseBV.setAmendedDateExported(defOnCaseBasicValue.getAmendedDateExported().getTime());
             	}
             	defOnCaseBV.setHateInd(defOnCaseBasicValue.getHateIndicator());
             	defOnCaseBV.setHateSentInd(defOnCaseBasicValue.getHateSentIndicator());
             	
             	defOnCaseBV.setDrivingDisqSuspendedDate(defOnCaseBasicValue.getDrivingDisqSuspendedDate());
             	defOnCaseBV.setMagCourtFirstHearingDate(defOnCaseBasicValue.getMagCourtFirstHearingDate());
             	defOnCaseBV.setMagCourtFinalHearingDate(defOnCaseBasicValue.getMagCourtFinalHearingDate());
             	
             	defOnCaseBV.setCustodyTimeLimit(defOnCaseBasicValue.getCustodyTimeLimit());
             	defOnCaseBV.setSection28Name1(defOnCaseBasicValue.getSection28Name1());
             	defOnCaseBV.setSection28Name2(defOnCaseBasicValue.getSection28Name2());
             	defOnCaseBV.setSection28Phone1(defOnCaseBasicValue.getSection28Phone1());
             	defOnCaseBV.setSection28Phone2(defOnCaseBasicValue.getSection28Phone2());
             	defOnCaseBV.setDefendantNumber(defOnCaseBasicValue.getDefendantNumber());
             	
               	defOnCaseBV.setAmendedReason(amendedReason);
             	defOnCaseBV.setResultsVerified(resultsVerified);
             	
             	if(defV.getDefOnCaseBasicValue().getDateExported() == null) {
             		log.debug("saveDefendant Date Exported will be set to " + cal.getTime());
             		defOnCaseBV.setDateExported(cal.getTime());
                }
             	XhbDefendant defBean = XhbDefendantBeanHelper2.findByPrimaryKey(defV.getDefendantID());    	
            	defOnCaseBV.setCreationDate(defBean.getCreationDate());
            	defOnCaseBV.setCreatedBy(defBean.getCreatedBy());
                
               	if(defV.getDefendantID() != null && defV.getDefendantID() != 0) {
    				XhbDefendantOnCaseBeanHelper2.updateLocal(defOnCaseBV);
    			} else {
    				XhbDefendantOnCaseBeanHelper2.createLocal(defOnCaseBV);
    			}
               	
            }  catch(Exception e) {
            	log.debug("Exception caught >>>" + e.getMessage());
            }
            
        } catch (DefendantControllerException e) {
            throw new CSUnrecoverableException("Unable to retrieve defendant details.");
        }
        
    }

	public ArrayList<Integer> getOffencesNotToDisplay() {
		return offencesNotToDisplay;
	}

	public void setOffencesNotToDisplay(ArrayList<Integer> offencesNotToDisplay) {
		this.offencesNotToDisplay = offencesNotToDisplay;
	}
	
	public void addOffenceNotToDisplay(Integer offenceID) {
		offencesNotToDisplay.add(offenceID);
	}
        
}