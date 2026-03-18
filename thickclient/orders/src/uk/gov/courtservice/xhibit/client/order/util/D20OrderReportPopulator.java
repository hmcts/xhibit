package uk.gov.courtservice.xhibit.client.order.util;

import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.ejb.EJBException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.framework.util.DateTimeUtilities;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal_line.XhbDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_ref_disposal_line.XhbRefDisposalLineBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdictBasicValue;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantControllerException;
import uk.gov.courtservice.xhibit.business.services.systemadmin.BisRefControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.CourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnOffenceComplexValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResD20MapBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefAppResultBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefCourtBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.RefOffenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.charge.ChargeCompositeValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DisposalValue;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefAppResultCriteria;
import uk.gov.courtservice.xhibit.business.vos.services.systemadmin.criteria.RefOffenceCriteria;
import uk.gov.courtservice.xhibit.client.casemanagement.CaseType;
import uk.gov.courtservice.xhibit.client.order.OrderData;
import uk.gov.courtservice.xhibit.client.order.screens.model.OffenceModel;
import uk.gov.courtservice.xhibit.client.order.screens.model.OffencePair;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderInitialDataVO;
import uk.gov.courtservice.xhibit.client.order.screens.model.OrderOffenceModel;
import uk.gov.courtservice.xhibit.client.util.XHIBITConstant;
import uk.gov.courtservice.xhibit.client.util.XhibitBundles;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitDelegateHelper;
import uk.gov.courtservice.xhibit.client.xhibitapplication.XhibitSingleton;
import uk.gov.courtservice.xhibit.common.results.vos.ResultsCompositeValue;
import uk.gov.courtservice.xhibit.common.results.vos.VerdictValue;


/**
 * This class is used to populate the LHS of the D20 order with legitimate values to stop the user having to enter as little
 * data as possible.
 * 
 * @author atwells
 *
 */
public class D20OrderReportPopulator {
	
    private static final String EMPTYSTRING = "";
    private static final Logger log = CSServices.getLogger(D20OrderReportPopulator.class);	
	private CaseType caseType;
	public static final String DELETED = "DELETED";
	private OrderData data;
	private Integer defendant;
	private Integer caseID;
	private Integer defOnCaseID=0;
	private OrderOffenceModel offenceModel;
	private Integer scheduledHearingId;
	private String courtName;
	private Integer courtCrestId;
	private boolean isCreateOrder;
	
	private final List<String> allowedCodes = new ArrayList<String>();
	private final List<String> dismissedCodes = new ArrayList<String>();
	private final List<String> variedCodes = new ArrayList<String>();
	private final List<String> abandonedCodes = new ArrayList<String>();
	private final List<String> remittedCodes = new ArrayList<String>();
	
	/**
	 * Constructor
	 * 
	 * @param casetype
	 * @param data
	 * @param dataModel
	 * @param orderInputStream
	 */
	public D20OrderReportPopulator(String caseType, OrderData data, OrderInitialDataVO dataModel, InputStream orderInputStream, boolean isCreateOrder) {
		log.debug("Entry:: D20OrderReportPopulator - Begin populating D20 order");
		if(caseType.startsWith("A"))
			this.caseType = CaseType.APPEAL;
		else if (caseType.startsWith("T"))
			this.caseType = CaseType.TRIAL;
		else if (caseType.startsWith("S"))
			this.caseType= CaseType.SENTENCE;
		offenceModel =dataModel.getOffenceData();
		this.data = data;
		this.defendant = dataModel.getDefendantID(); 
		this.caseID= dataModel.getXhibitCaseId();
		this.defOnCaseID =  dataModel.getDefendantOnCaseID();
		this.scheduledHearingId = dataModel.getScheduledHearingId();
		this.courtName = dataModel.getCourtName();
		this.courtCrestId = dataModel.getCrestCourtID();
		this.isCreateOrder = isCreateOrder;
		populateRefAppCodes();
	}
	
	/**
	 * Add conviction details for D20 order being displayed
	 */
	public void addConvictionDetails(OrderInitialDataVO orderModel) {
		log.debug("Entry:: addConvictionDetails - About to add conviction details to D20 order to display");
		
		XhbOrderConvictionData conData = getConvictData();
		
		conData.setConvictionDate(getConvictionDate(orderModel));
		
		if (conData.getConvictionDate() != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = date.format(conData.getConvictionDate()).trim(); 
			data.setValue("//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:Date", dateTime);
        }
		data.setValue("//ord:D20/ord:D20PersonalDetails/ord:Gender", conData.getGender());
		
		if(conData.getLicenceType() != null && conData.getLicenceType().charAt(0) != '0'){
        	data.setValue("//ord:D20/ord:DriverNumber", conData.getDriverNo());
        }
		
        data.setValue("//ord:D20/ord:LicenceIssueNo", conData.getLicenceIssueNo());
        data.setValue("//ord:D20/ord:LicenceType", conData.getLicenceType());
        data.setValue("//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseName", conData.getConvictCourtID());
        try {
        	data.setValue("//ord:D20/ord:D20CommittingCourtSection/ord:D20CommittingCourt/ord:CourtHouse/ord:CourtHouseType", "Magistrates Court");
        } catch(NumberFormatException ex) {
			log.error("Error getting Conviction Date");
			ex.printStackTrace();
        }
        
        log.debug("Exit:: addConvictionDetails - Conviction details have been added to D20 order to display");
	}
	
	/**
	 * Get the conviction date
	 * 
	 * @param 	orderModel					The model
	 * 
	 * @return	The date (or null)
	 */
    private Date getConvictionDate(OrderInitialDataVO orderModel) {
    	if (orderModel != null && orderModel.getOffenceData() != null) {
		    for(OffenceModel model : orderModel.getOffenceData().getOffences()){
			    if(model.isChecked() && model.getDateOfConviction() != null){
				    return model.getDateOfConviction();
			    }
		    }
    	}
	    return null;
    }

	/**
	 * Add offence details for D20 order being displayed
	 * 
	 * @param isInterim
	 */
	public void addOffenceDetails(boolean isInterim) {
		log.debug("Entry:: addOffenceDetails - About to add offence details to D20 order to display: isInterim="+isInterim);
		ArrayList<XhbOrderOffenceData> d20Offences = getD20Offences(isInterim);
		
		int maxOffences = 4;
		String offenceNumber;
		for (int i = 0; i < maxOffences; i++) {	
			offenceNumber = Integer.toString(i+1);
			if (i<d20Offences.size()) {
				XhbOrderOffenceData offence  = d20Offences.get(i);
				addSingleOffenceDetails(offenceNumber, offence);
			} else {
				setDateOfSentenceIfDifferent(new Date(), offenceNumber);
			}
		}
		
		log.debug("Exit:: addOffenceDetails - Offence details have been added to D20 order to display");
	}
	
	/**
     * Gets reference offence code based on its ref offence ID
     * 
     * @param	offenceID					The offence ID
     * 
     * @return the found reference offence, otherwise null
     */
    private RefOffenceBasicValue getRefOffence(Integer offenceID) {
    	log.debug("Entry:: getRefOffence - Get the ref offence for offenceID: "+offenceID);
    	
    	if(offenceID!=null) {
	    	try {
		    	RefOffenceCriteria criteria = new RefOffenceCriteria();
		    	criteria.setPrimaryKey(offenceID);
		    	RefOffenceBasicValue refOffenceBasicValue;
		        ArrayList<RefOffenceBasicValue> refOffences = new ArrayList<RefOffenceBasicValue>(XhibitDelegateHelper.getBizRefDelegate().findOffences(criteria));
		        
		        refOffenceBasicValue = (RefOffenceBasicValue)refOffences.get(0);
		        
		        log.debug("Exit:: getRefOffence - About to return ref offence");
		        
		        return refOffenceBasicValue;
	    	} catch(Exception e) {
	    		log.error("Error getting D20 offences "+e);
				e.printStackTrace();
	    	}
    	}
    	
    	return null;
    }
	
    /**
     * Get D20 Offences
     * 
     * @param isInterim
     * 
     * @return
     */
	private ArrayList<XhbOrderOffenceData> getD20Offences(boolean isInterim) {
		log.debug("Entry:: getD20Offences - About to get getD20Offences to D20 order to display: isInterim="+isInterim);
		
		ArrayList<XhbOrderOffenceData> offenceData = new ArrayList<XhbOrderOffenceData>();
	
		try {
			
			if (offenceModel != null) {
				for(OffencePair offencePair : offenceModel.getSelectedOffences()) {
					Date verdictDate = null;
					String appealType = EMPTYSTRING;
					String refAppResultCode = EMPTYSTRING;
					Date disqRemovedDate = null;
					Date disqReimposedDate = null;
					Timestamp disqSuspendedDate = null;
							
					String dvlc = offencePair.getModel().getDvlaOffence();
					log.debug("getD20Offences:: dvlc="+dvlc);
					
					DefendantOnOffenceComplexValue defendantOnOffence = offencePair.getOffence().getDefendantOnOffence(defendant);
					if (defendantOnOffence == null) {
						// Defendant not on offence so skip it
						continue;
					}
					Integer defOnOff = defendantOnOffence.getDefendantOnOffenceId();
					
					DisposalValue[] disVal = XhibitDelegateHelper.getDefendantDelegate()
							.getDisposalsForDefendantOnOffence(defOnOff,defOnCaseID, caseID);
					
					boolean hasDisint=false;
					boolean hasDisqualification=false;
					ArrayList<DisposalValue> disposalOnOffence = new ArrayList<DisposalValue>();
					
					for(DisposalValue disp : disVal) {
						if(disp.getDefendantOnOffenceId().equals(defOnOff)) {
							if("DISINT".equals(disp.getRefDisposalType().getDisposalCode())) {
								hasDisint = true;
							}
							if (isDisqualified(disp.getRefDisposalType().getDisposalCode())) {
								hasDisqualification=true;
							}
							
							disposalOnOffence.add(disp);
						}
					}
					
					if(this.caseType== CaseType.APPEAL) {
						ResultsCompositeValue value = XhibitDelegateHelper.getResults2Delegate().getResults(caseID, scheduledHearingId);
						
						VerdictValue verdict = value.getVerdict(defendantOnOffence.getDefendantOnOffenceId());	
						
						if(verdict.getRefAppResultId()!=null) {
							log.debug("getD20Offences:: verdict.getRefAppResultId()!=null");
							XhbVerdictBasicValue basicVerdictValue = verdict.getXhbVerdictBasicValue();
							
							if ( basicVerdictValue != null ){
								verdictDate = basicVerdictValue.getVerdictDate();
								
								try{
									BisRefControllerBeanBusinessDelegate delegate = XhibitDelegateHelper.getBizRefDelegate();
									
									RefAppResultBasicValue refAppResult = delegate.findRefAppByPrimaryKey(basicVerdictValue.getRefAppResultId());
	
									if ( refAppResult != null ){
										refAppResultCode = refAppResult.getCode();
										
										String description = refAppResult.getDescription1();
										
										log.debug("Description: " + description);
										if (description.toLowerCase().contains("allowed") && hasDisqualification){
											disqRemovedDate = verdictDate;
										} else if ( description.toLowerCase().contains("dismissed")){
											disqReimposedDate = verdictDate;
										}
									}
								}
								catch( Exception ex){
									ex.printStackTrace();
								}
							}
							
							appealType = offencePair.getOffence().getAppealType();
							
							RefAppResultCriteria criteria = new RefAppResultCriteria();
							criteria.setPrimaryKey(verdict.getOriginalRefVerdictId());
							ArrayList<RefAppResultBasicValue> appealRes=  new ArrayList<RefAppResultBasicValue>(XhibitDelegateHelper.getBizRefDelegate().findAppResults(criteria));
							boolean isLesser = false;
							
							//Should really only be one but just in case ...
							for(RefAppResultBasicValue appeal: appealRes ) {
								if(!isLesser) {
									if("ACALO".equals(appeal.getCode())) {
										isLesser=true;
									}
								}
							}
							
							log.debug("getD20Offences:: isLesser="+isLesser);
							
							RefOffenceBasicValue item = getRefOffence(verdict.getAltRefOffenceId());
							
							if(item!=null) {
								if(item.getDvlcCode()!=null) {
									dvlc = isLesser ? item.getDvlcCode(): null;
								}
							}
							
							log.debug("getD20Offences:: dvlc now="+dvlc);
						}
					}
					
					String offenceCode = dvlc!=null? dvlc: "NE98";
					Integer refOffenceId = offencePair.getOffence().getRefOffenceID();
					
					
					//check if drivingdisqsuspended date exists, if it does, set it
					DefendantControllerBeanBusinessDelegate defCtrlBeanDelegate = XhibitDelegateHelper.getDefendantDelegate();
					DefendantOnCaseBasicValue defBV = defCtrlBeanDelegate.getDefendantOnCaseDetails(defOnCaseID);
	
					if (defBV != null){
						disqSuspendedDate = defBV.getDrivingDisqSuspendedDate();
					}
					
					XhbOrderOffenceData data = new XhbOrderOffenceData(	refOffenceId, offenceCode, offencePair.getOffence().getOffenceStartDateTime().getTime(),isInterim, hasDisint, 
																		true, disposalOnOffence, verdictDate, appealType, refAppResultCode,
																		disqRemovedDate, disqReimposedDate, disqSuspendedDate, defOnOff);
			
					offenceData.add(data);
				}
			}
		} catch(Exception e) {
			log.error("Error getting D20 offences "+e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		
		log.debug("Exit:: getD20Offences"); 
		
		return offenceData; 
	}

	/**
	 * Add Single offence details
	 * 
	 * @param 	offenceNumber				The offence number
	 * @param 	record						The offence data
	 */
	public void addSingleOffenceDetails(String offenceNumber, XhbOrderOffenceData record) {
		log.debug("Entry:: addSingleOffenceDetails - offenceNumber="+offenceNumber);
		
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		String dateTime = date.format(record.getConvictionStartDate()).trim();  
		
		//	Don't show the offence date if the offence code is "TT99"
		String offenceId = record.getOffenceId() != null ? record.getOffenceId().toString() : "";
		String offenceCode = record.getOffCode();
		String dateSelected = "TT99".equals(offenceCode.trim()) ? "false" : "true";
		
		String offence = "//ord:D20/ord:Offence"+offenceNumber;
		data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/@selected", "true");
		data.setValue(offence+"/ord:OffenceCode"+offenceNumber, offenceCode);
		data.setValue(offence+"/ord:XhibitOffenceId"+offenceNumber, offenceId);
		data.setValue(offence+"/ord:OffenceDate"+offenceNumber+"Section/ord:OffenceDate"+offenceNumber, dateTime);
		data.setValue(offence+"/ord:OffenceDate"+offenceNumber+"Section/@selected", dateSelected);
		
		if ( this.caseType == CaseType.APPEAL){
			// Make the court number section visible for this offence
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceSentencingCourt"+offenceNumber+"Section/@selected", "true");
			setCourtId(offenceNumber);
			
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/@selected", "true");
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:OffenceDateOfAppeal"+offenceNumber+"Section/@selected", "true");
			dateTime = date.format(record.getVerdictDate()).trim();
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:OffenceDateOfAppeal"+offenceNumber+"Section/ord:OffenceDateOfAppeal"+offenceNumber, dateTime);
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:OffenceAppealCourt"+offenceNumber+"/ord:CourtHouseName", String.format("%s (%d)", courtName.toUpperCase(), courtCrestId));
			
			//	Appeal type
			String appealType = record.getAppealType();
			
			if ("C".equals(appealType) || "B".equals(appealType)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasAgainstConviction"+offenceNumber, "true");
			}
			else if ("S".equals(appealType)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasAgainstSentenceOnly"+offenceNumber, "true");
			}
			
			//	Appeal allowed?
			if (isAppealAllowed(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasAllowed"+offenceNumber, "true");
			}
			
			//	Appeal dismissed:
			if ( isAppealDismissed(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealDismissed"+offenceNumber+"Section/@selected", "true");
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealDismissed"+offenceNumber+"Section/ord:AppealDateDismissed"+offenceNumber, dateTime);
			}
			
			//	Appeal abandoned?
			if ( isAppealAbandoned(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasAbandoned"+offenceNumber+"Section/@selected", "true");
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasAbandoned"+offenceNumber+"Section/ord:AppealDateAbandoned"+offenceNumber, dateTime);
			}
			
			//	Appeal varied?
			if ( isAppealVaried(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:SentenceWasVaried"+offenceNumber, "true");
			}
			
			//	Appeal Remitted:
			if ( isAppealRemitted(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasRemitted"+offenceNumber+"Section/@selected", "true");
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceAppeal"+offenceNumber+"Section/ord:AppealWasRemitted"+offenceNumber+"Section/ord:AppealDateRemitted"+offenceNumber, dateTime);
			}
			
			//	Appeal: Disqualification removed:
			Date disqRemovedDate = record.getDisqRemovedDate();
			
			if ( disqRemovedDate != null){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationRemoved"+offenceNumber+"Section/@selected", "true");

				String dateAsString = date.format(disqRemovedDate).trim();
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationRemoved"+offenceNumber+"Section/ord:OffenceDisqualificationRemoved"+offenceNumber, dateAsString);
			}
			
			//	Appeal: Disqualification reimposed.
			Date disqReimposedDate = record.getDisqReimposedDate();
			
			if ( disqReimposedDate != null && isAppealDismissed(record)){
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationReimposed"+offenceNumber+"Section/@selected", "true");

				String dateAsString = date.format(disqReimposedDate).trim();
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationReimposed"+offenceNumber+"Section/ord:OffenceDisqualificationReimposed"+offenceNumber, dateAsString);
			}
		}
		
		// Set Interim/Final data - if it is an interim set the value to 1, final sentence will either be: 2 or blank/null
		if (record.isInterim()) {
			setInterimFinal(offenceNumber, true, false);
		} else { // Set it to either 2 or blank/null
			if ( caseType == CaseType.APPEAL ) {
				data.setValue(offence+"/ord:OffenceInterimFinal"+offenceNumber, "Not Applicable");
				
			} else if ( caseType == CaseType.TRIAL ) {
				boolean prevInterim = isDisqDisposalWithInterimChecked(record);
				setInterimFinal(offenceNumber, record.isInterim(), prevInterim);
				
			} else if ( caseType == CaseType.SENTENCE ) {
				boolean prevInterim = isDefOnOffenceInterimD20FieldSet(record.getDefendantOnOffenceId());
				setInterimFinal(offenceNumber, record.isInterim(), prevInterim);
			}
		}
		
		
		//check for a disqsuspendeddate
		Timestamp disqSuspendedDate = record.getDisqSuspendedDate();
		if(disqSuspendedDate != null) {
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationSuspended"+offenceNumber+"Section/@selected", "true");
		
			String dateAsString = date.format(disqSuspendedDate).trim();
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceDisqualificationSuspended"+offenceNumber+"Section/ord:OffenceDisqualificationSuspended"+offenceNumber, dateAsString);
		}
		
		handleDataFromDisposals(getUniqueDisposals(record.getOffenceDisposal()), offenceNumber, record.isInterim(), isAppealVaried(record));
		
		if (( caseType == CaseType.SENTENCE ) || ( caseType == CaseType.APPEAL )) {
			handleDataForSentencing(record.getOffenceDisposal(), offenceNumber, caseType);
		}
		
		log.debug("Exit:: addSingleOffenceDetails");
	}

	
	/* 
	 * Method for checking disposal is a disqualification
	 * first checks to see if any of the disposals start with DIS to narrow down the checks
	 * Then it checks if those disposals that start with DIS are in fact disqualification
	 * if none of the checks come back true, it just returns false
	 */
	private boolean isDisqDisposalWithInterimChecked(XhbOrderOffenceData record) {
	
		for(DisposalValue disposal : record.getOffenceDisposal()) {
			if((disposal.getRefDisposalType().getDisposalCode()).startsWith("DIS")) { // We only want DIS disposals
				XhbRefDisposalLineBasicValue[] disRefLine = disposal.getRefDisposalLines();	
				
				// Check the disposal to see if the "Prev Interim" checkbox has been checked
				for(int i=0; i < disRefLine.length; i++) {
					XhbRefDisposalLineBasicValue refLine = disRefLine[i];
					if ((refLine.getObsInd()!="Y") && (refLine.getDbdestin()!=null)) {
						if(refLine.getDbdestin().equals("D22")) {
							XhbDisposalLineBasicValue lineData = disposal.getDisposalLines()[i];
							if(lineData.getLineData()!=null) {
								if(lineData.getLineData().equals("Y"))
									return true; 
							}
						}
					}
				}
			} 
		}
		return false;
	}
	
	/**
	 * Set the court ID for the offence
	 * 
	 * @param 	offenceNumber					The offence number (1 - 4)
	 */
	private void setCourtId(String offenceNumber) {
		try {
			CaseBasicValue caseItem = XhibitDelegateHelper.getCaseDelegate().getCase(caseID);
            String receiptType = caseItem.getReceiptType();

            String refCourtID = EMPTYSTRING;
            
			if(receiptType != null && "BB".equals(receiptType)){
				CourtBasicValue refCourt= XhibitSingleton.getInstance().getCourtBasicValue(caseItem.getCourtID());
				String courtID = refCourt.getCrestCourtId();
				refCourtID = refCourt.getCourtName().toUpperCase()+" ("+courtID+")";
			} else {
			    RefCourtBasicValue refCourt= XhibitSingleton.getInstance().getRefCourtByCourtID(caseItem.getRefCourtID());
				refCourtID = refCourt.getCourtFullName()+" ("+refCourt.getCrestCode()+")";
			}
			
			setDateOfSentenceIfDifferentLocation(refCourtID, offenceNumber);
		} catch (Exception e) {
			log.error("Error in handleDataForSentencing "+e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param offenceDisposal
	 * @return
	 */
	private List<DisposalValue> getUniqueDisposals(ArrayList<DisposalValue> offenceDisposal) {
		List<DisposalValue>uniqueDisposals = new ArrayList<DisposalValue>();
		for(int i=0 ; i <offenceDisposal.size(); i++){
			boolean flag = true;
			for(int j= i+1 ; j < offenceDisposal.size(); j++){
			   if(offenceDisposal.get(i).getDisposalId().equals(offenceDisposal.get(j).getDisposalId())){
			    	flag=false;
			     }
			}   
			if(flag){
			 	 uniqueDisposals.add(offenceDisposal.get(i));
			}
		}
		return uniqueDisposals;
	}
	

	/**
	 * 
	 * @return
	 */
	private XhbOrderConvictionData getConvictData()  {

		switch(caseType) {
			case TRIAL: 
				return getConvictTrialData();
			case APPEAL:
				
			case SENTENCE: 
				return getConvictAppealSentData();
			default:
				return new XhbOrderConvictionData();
		}
	}
	
	/**
	 * 
	 * @param caseType 
	 * @return
	 */
	private XhbOrderConvictionData getConvictAppealSentData()  {
		log.debug("Entry:: getConvictAppealSentData");
		
		XhbOrderConvictionData conData =  new XhbOrderConvictionData();

		try {
			//Defendant Reference values 
			getDefRefVals(conData);
			getCommonInputData(conData);
			//Get court data 
			CaseBasicValue caseItem = XhibitDelegateHelper.getCaseDelegate().getCase(caseID);
			String crestCODE = null;
			
			if (this.caseType == CaseType.SENTENCE) {
				if ("BB".equals(caseItem.getReceiptType())) {
					CourtBasicValue refCourt = XhibitSingleton.getInstance().getCourtBasicValue(caseItem.getCourtID());
					String courtID = refCourt.getCrestCourtId();
					crestCODE = refCourt.getCourtName().toUpperCase()+" ("+courtID+")";
					conData.setCourtCode(refCourt.getCrestCourtId());
				} else{
					RefCourtBasicValue refCourt= XhibitSingleton.getInstance().getRefCourtByCourtID(caseItem.getRefCourtID());
					crestCODE = refCourt.getCourtFullName()+" ("+refCourt.getCrestCode()+")";
					conData.setCourtCode(refCourt.getCrestCode());				
				}
			} else if (this.caseType == CaseType.APPEAL) {
				if (caseItem.getRefCourtID()!=null) {
					RefCourtBasicValue refCourt = XhibitSingleton.getInstance().getRefCourtByCourtID(caseItem.getRefCourtID());
					crestCODE = refCourt.getCourtFullName()+" ("+refCourt.getCrestCode()+")";
					conData.setCourtCode(refCourt.getCrestCode());
			    }
			}
		
			conData.setConvictCourtID(crestCODE);
			
		} catch (Exception e) {
			log.error("Error getting convict appeal sent data: "+e);
			e.printStackTrace();
		}
		
		log.debug("Exit:: getConvictAppealSentData");
		
		return conData;
	}

	/**
	 * @param conData
	 * @throws DefendantControllerException
	 */
	private void getCommonInputData(XhbOrderConvictionData conData) throws DefendantControllerException {
		// If viewing an order then we should use the value from XML
		String gender = "";
		if (isCreateOrder) {
			gender = XhibitDelegateHelper.getDefendantDelegate().getDefendantDetails(defendant, caseID).getGenderString();
		} else {
			Vector<String> nodes = data.getChildReferences("//ord:D20/ord:D20PersonalDetails/ord:Gender");
			if (nodes.size() == 1) {
				gender = nodes.get(0);
			}
		}
		conData.setGender(gender);
	}

	/**
	 * @param conData
	 * @param defendant
	 */
	private void getDefRefVals(XhbOrderConvictionData conData) {
		log.debug("Entry:: getDefRefVals");
		
		DefendantReferenceBasicValue driverNO= XhibitDelegateHelper.getDefendantReferenceDelegate().findByDefendantIdAndReferenceName(defendant, "DRIVER_NO"); 
		DefendantReferenceBasicValue licenceType= XhibitDelegateHelper.getDefendantReferenceDelegate().findByDefendantIdAndReferenceName(defendant, "LICENCE_TYPE"); 
		DefendantReferenceBasicValue licenseIssue =XhibitDelegateHelper.getDefendantReferenceDelegate().findByDefendantIdAndReferenceName(defendant, "LICENCE_ISSUE_NUMBER"); 
		
		if(driverNO!=null) {
			conData.setDriverNo(driverNO.getReferenceValue());
		}
		
		if ((licenceType != null) && (licenceType.getReferenceValue() != null) && (licenceType.getReferenceValue().length() > 0)) {
			conData.setLicenceType(convertLicenceType(licenceType.getReferenceValue()));
		} else {
			conData.setLicenceType(XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone")); // Needs a default of 0 (Never held a licence) to populate XML correctly - this happens if the Form A licence details are not entered
		}
		
		if(licenseIssue!=null) {
			conData.setLicenceIssueNo(licenseIssue.getReferenceValue());
		}
		
		log.debug("Exit:: getDefRefVals");
	}
	
	/**
	 * Need the true value for the XML, otherwise it will fail to parse when saving 
	 * @param licenceType
	 * @return
	 */
	private String convertLicenceType(String licenceType) {
		log.debug("Entry:: convertLicenceType - licenceType="+licenceType);
		int licenceTypeInt = 0;
		try {
			licenceTypeInt = new Integer(licenceType.substring(0,1)).intValue();
		} catch (NumberFormatException nfe) {
			return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone");
		}
		
		// Using if/else as can't switch on Strings and not guaranteed to to be an int
		switch (licenceTypeInt) {
			case 1: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeProv");
			case 2: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeFull");
			case 3: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNonUk");
			case 5: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeDvla");
			case 0: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone");
			default: return XHIBITConstant.getResource(XhibitBundles.HearingRecord, "typeNone");
		}
		 
	}
	
	/**
	 * Handles the population of the report fields when a Trial is taking p
	 * @return
	 */
	private XhbOrderConvictionData getConvictTrialData() {
		log.debug("Entry:: getConvictTrialData");
		
		XhbOrderConvictionData conData =  new XhbOrderConvictionData();
		try {
			
			//Defendant Refernce values 
			getDefRefVals(conData);
			//Get court data 
			getCommonInputData(conData);
			CaseBasicValue caseItem = XhibitDelegateHelper.getCaseDelegate().getCase(caseID);
			CourtBasicValue refCourt= XhibitSingleton.getInstance().getCourtBasicValue(caseItem.getCourtID());
			String courtID = refCourt.getCrestCourtId();
			String crestCODE = refCourt.getCourtName().toUpperCase()+" ("+courtID+")";
			
			conData.setConvictCourtID(crestCODE);
			conData.setCourtCode(courtID);
		
		} catch(Exception e) {
			log.error("Error getting convict trial data: "+e);
			e.printStackTrace();
		}
		
		log.debug("Exit:: getConvictTrialData");
		return conData;
	}

	
	/**
	 * 
	 * @param disposals
	 * @param offenceNumber
	 */
	private void handleDataForSentencing(ArrayList<DisposalValue>disposals, String offenceNumber, CaseType caseType) {
		log.debug("Entry:: handleDataForSentencing - offenceNumber="+offenceNumber);
		
		Date earliest = null;
		String refCourt = EMPTYSTRING;
		ResultsCompositeValue results;
		
		try {
			CaseBasicValue caseItem = XhibitDelegateHelper.getCaseDelegate().getCase(caseID);
			
			if (caseType == CaseType.SENTENCE) { // Use crown court
				CourtBasicValue court = XhibitSingleton.getInstance().getCourtBasicValue(caseItem.getCourtID());
				String courtId = court.getCrestCourtId();
				refCourt = court.getCourtName().toUpperCase()+" ("+courtId+")";
			} else { // Must be appeal case; use mags court if we can, otherwise null
				if (caseItem.getRefCourtID() != null) {
					RefCourtBasicValue refCourtBV = XhibitSingleton.getInstance().getRefCourtByCourtID(caseItem.getRefCourtID());
					refCourt = refCourtBV.getCourtFullName()+" ("+refCourtBV.getCrestCode()+")";
				}
			}
            
			if (disposals.size() > 0) {
				results = XhibitDelegateHelper.getResults2Delegate().getResults(caseID);
				for(DisposalValue disp: disposals) {
					if(!"DISINT".equals(disp.getRefDisposalType().getDisposalCode())) {
						VerdictValue vv = results.getVerdictForDisposal(disp.getDisposalId());
						if (vv != null) {
							Date verdictDate = vv.getOriginalVerdictDate();
							earliest = earliest.after(verdictDate)? verdictDate: new Date(System.currentTimeMillis());
						} else {
							log.error("Cannot get the verdict from the disposal: disposalid="+disp.getDisposalId());
						}
					}
				}
			}
			//	Make the sentencing field visible on the D2O report. This ensures that the sentencing date and the sentencing court are made visible.
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceSentencingCourt"+offenceNumber+"Section/@selected", "true");

			this.setDateOfSentenceIfDifferent(earliest, offenceNumber);
			this.setDateOfSentenceIfDifferentLocation(refCourt, offenceNumber);
			
			log.debug("Exit:: handleDataForSentencing");
			
		} catch (Exception e) {
			log.error("Error in handleDataForSentencing "+e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Populate fields from an offences disposals
	 * 
	 * @param 	disposals					The list of disposals
	 * @param 	offenceNumber				The offence number (1-4)
	 * @param 	isInterim					Is this an interim order?
	 */
	private void handleDataFromDisposals(List<DisposalValue>disposals, String offenceNumber, boolean isInterim, boolean appealVaried) {
		log.debug("Entry:: handleDataFromDisposals - offenceNumber="+offenceNumber);
		
		boolean distotDisqualificationDataObtained = false;

		for(DisposalValue disp: disposals) {
			// If the disposal is magistrate and the appeal is Varied then skip it
			if (isDisposalMagistrateVaried(disp, appealVaried)) {
				continue;
			}
			String dispCode = disp.getRefDisposalType().getDisposalCode();
		
			if(dispCode.matches("FINE||FD||FDTIME||FDINST")) {
				boolean loadFine = true;					//	Assume we are populating the fine.
				
				if ( caseType == CaseType.APPEAL ){		//	Only check further for appeal cases.
					if ( "M".equals(disp.getCourtType())){	//	Only allowed if the disposal was not a magistrates' disposal.
						loadFine = false;
					}
				}

				if ( loadFine ){							//	Only if 'allowed'
					getFineData(disp, offenceNumber);
				}
			}
			//Get Disqualification data 
			else if(isDisqualified(dispCode)) {
				getDisqData(disp, offenceNumber);
				
				if (!distotDisqualificationDataObtained) {
					setDisqualificationValues(disp, offenceNumber, disposals);
					if ("DISTOT".equals(dispCode)) {
						distotDisqualificationDataObtained = true;
					}
				}
				//Get substance abuse 
				if("DISOBLG".equals(dispCode)) {
					getSubstanceData(disp, offenceNumber);
				}
			}
			//Get penalty points
			else if("LENDPP".equals(dispCode)) {
				getPenaltyData(disp, offenceNumber);
			}
			// Mitigating Circumstances
			else if (!isInterim && "MITCIRC".equals(dispCode)) {
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:NoDisqualificationMitigatingCircumstances"+offenceNumber, "true");
			}
			// Special Reasons
			else if (!isInterim && "SPECREA".equals(dispCode)) {
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:NoDisqualificationSpecialReasons"+offenceNumber, "true");
			}
			// Notification of Disability to Licencing Authority
			else if (!isInterim && "NOTLA".equals(dispCode)) {
				data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:NotificationOfDisability"+offenceNumber, "true");
			}
			else {
				handleOtherD20Data(disp, offenceNumber, isInterim);
			}
		}
		
		log.debug("Entry:: handleDataFromDisposals");
	}


	/**
	 * 
	 * @param dispCode
	 * @return
	 */
	private boolean isDisqualified(String dispCode) {
		return dispCode.matches("DISTOT||DISOBLG||DISINT||DISDISC");
	}
	
	/**
	 * Checks whether the value of XHB_DEFENDANT_ON_OFFENCE.INTERIM_D20 has been set to Y
	 * @param defendantOnOffenceId
	 * @return
	 */
	private boolean isDefOnOffenceInterimD20FieldSet(Integer defendantOnOffenceId) {
		if(CaseType.SENTENCE.equals(caseType)) {		//	Sentencing cases only
			try {
				ChargeCompositeValue charge =  XhibitDelegateHelper.getChargeDelegate().getCharges(caseID, true);
				DefendantOnOffenceComplexValue val = charge.getDefendantOnOffence(defendantOnOffenceId, defendant);
				if ( val!=null && val.getInterimD20()!=null ) {
					return "Y".equals(val.getInterimD20());
				}
			} catch(Exception e) {
				log.error("Error in isDefOnOffenceInterimD20FieldSet "+e);
				e.printStackTrace();
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param offenceNumber
	 * @param isInterim
	 * @param prevInterim
	 */
	private void setInterimFinal(String offenceNumber, boolean isInterim, boolean prevInterim) {
		
		if (isInterim) {
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceInterimFinal"+offenceNumber, "1 - Interim Imposed");
		} else if(prevInterim) {
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceInterimFinal"+offenceNumber, "2 - Final Sentence");
		} else {
			data.setValue("//ord:D20/ord:Offence"+offenceNumber+"/ord:OffenceInterimFinal"+offenceNumber, "Not Applicable");
		}
	}
	
	/**
	 * 
	 * @param disp
	 * @param offenceCode
	 * @param isInterim
	 */
	private void handleOtherD20Data(DisposalValue disp, String offenceCode, boolean isInterim) {
		log.debug("Entry:: handleOtherD20Data - offenceNumber="+offenceCode);
		
		if(disp.getRefDisposalType().getD20OtherSentence()!=null) {
			
			String value=getOtherSentenceCode(disp);
			
			data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/ord:OffenceOtherSentence"+offenceCode+"/ord:D20OffenceOtherSentenceDropdown",value);
			data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/@selected", "true");
			data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceSentencingCourt"+offenceCode+"Section/@selected", "true");
			
			if(disp.getRefDisposalType().getD20OtherSentence().matches("A|P|Imprisonment|Youth Custody|C|E")) {
				DurationAndUnit durationAndUnit = getDurationAndUnit(disp);
				if (!EMPTYSTRING.equals(durationAndUnit.duration)) {
					data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/ord:OffenceOtherSentence"+offenceCode+"/ord:D20OffenceOtherSentenceDurationType", durationAndUnit.duration);
				}
				if (!EMPTYSTRING.equals(durationAndUnit.unit)) {
					data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/ord:OffenceOtherSentence"+offenceCode+"/ord:D20OffenceOtherSentenceValue", durationAndUnit.unit);
				}
				// Suspended sentence
				if (!isInterim && "C".equals(disp.getRefDisposalType().getD20OtherSentence()))
				{
					setPeriodPrisonSection(offenceCode, durationAndUnit);
				}
			} else if(disp.getRefDisposalType().getD20OtherSentence().matches("J|M|Absalute discharge|Community order"))  {
				data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/ord:OffenceOtherSentence"+offenceCode+"/ord:D20OffenceOtherSentenceDurationType", "Not Applicable");
				data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffenceOtherSentence"+offenceCode+"Section/ord:OffenceOtherSentence"+offenceCode+"/ord:D20OffenceOtherSentenceValue", "00");
			
				// Community Order
				if (!isInterim && "M".equals(disp.getRefDisposalType().getD20OtherSentence()))
				{
					DurationAndUnit durationAndUnit = getDurationAndUnit(disp);
					setPeriodPrisonSection(offenceCode, durationAndUnit);
				}
			}
		}
		
		log.debug("Entry:: handleOtherD20Data");
	}
	
	/**
	 * 
	 * @param disp
	 * @return
	 */
	private DurationAndUnit getDurationAndUnit(DisposalValue disp) {
		DurationAndUnit durationAndUnit = new DurationAndUnit();
		XhbDisposalLineBasicValue[] lines  = disp.getDisposalLines();
		
		for(int i=0; i < lines.length; i++) {
			
			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];
			if(ref.getDbdestin()!=null) {
				if("D6".equals(ref.getDbdestin())) {
					XhbDisposalLineBasicValue item = lines[i];
					
					if ("hours".equals(item.getLineData()) || "hour".equals(item.getLineData())) durationAndUnit.duration="hour(s)";
					else if("days".equals(item.getLineData()) || "day".equals(item.getLineData())) durationAndUnit.duration="day(s)";
					else if ("weeks".equals(item.getLineData()) || "week".equals(item.getLineData())) durationAndUnit.duration="week(s)";
					else if ("months".equals(item.getLineData()) || "month".equals(item.getLineData())) durationAndUnit.duration="month(s)";
					else if("years".equals(item.getLineData()) || "year".equals(item.getLineData())) durationAndUnit.duration="year(s)";

				} else if("D5".equals(ref.getDbdestin())) {
					XhbDisposalLineBasicValue item = lines[i];
					if(!DELETED.equals(item.getLineData())) {
						durationAndUnit.unit = String.format("%02d", Integer.parseInt(item.getLineData()));
					}
				}
			}
		}
		return durationAndUnit;
	}
	
	/**
	 * 
	 * @param offenceCode
	 * @param durationAndUnit
	 */
	private void setPeriodPrisonSection(final String offenceCode, final DurationAndUnit durationAndUnit) {
		// Tick the Period Prison
		data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffencePPSCO"+offenceCode+"Section/@selected", "true");
		// Set the duration type
		if(durationAndUnit.getDurationType() != null && !EMPTYSTRING.equals(durationAndUnit.unit)) {
			data.setValue("//ord:D20/ord:Offence"+offenceCode+"/ord:OffencePPSCO"+offenceCode+"Section/ord:OffencePPSCO"+offenceCode+"/ord:D20OffencePPSCODurationType/ord:"+durationAndUnit.getDurationType(), durationAndUnit.unit);
		}
	}
	
	/**
	 * @param disp
	 * @param value
	 * @return
	 */
	private String getOtherSentenceCode(DisposalValue disp) {
		log.debug("Entry:: getOtherSentenceCode");
		
		String value = EMPTYSTRING;
		if("A".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="A - Imprisonment";
		}
		else if("P".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="P - Young Offenders Institution (Criminal Justice Act 1988 - England and Wales)";
		}
		else if("C".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="C - Suspended Prison Sentence";
		}
		else if("J".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="J - Absolute Discharge";
		}
		else if("M".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="M - Community Order";
		}
		else if("E".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="E - Conditional Discharge";
		}
		else if("I".equals(disp.getRefDisposalType().getD20OtherSentence()))
		{
			value="I - No Separate Penalty";
		}
		
		log.debug("Exit:: getOtherSentenceCode - value="+value);
		
		return value;
	}
	
	/**
	 * 
	 * @param courtCode
	 * @param offenceNo
	 */
	private void setDateOfSentenceIfDifferentLocation(String courtCode, String offenceNo) {
		data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceSentencingCourt"+offenceNo+"Section/ord:OffenceSentencingCourt"+offenceNo+"/ord:CourtHouseName", courtCode );
	}
	
	/**
	 * Applies to all case types
	 * If there is no date then set to null/empty
	 * 
	 * @param sentencingDate
	 * @param offenceNo
	 */
	private void setDateOfSentenceIfDifferent(Date sentencingDate, String offenceNo) {
		if (sentencingDate != null) {
			SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
			String dateTime = date.format(sentencingDate).trim(); 
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceSentencingCourt"+offenceNo+"Section/ord:OffenceDateOfSentence"+offenceNo, dateTime );
		}
	}
	
	/**
	 * 
	 * @param disp
	 * @param offenceNo
	 */
	private void getSubstanceData(DisposalValue disp, String offenceNo) {
		log.debug("Entry:: getSubstanceData - offenceNo="+offenceNo);
		
		XhbDisposalLineBasicValue[] lines  = disp.getDisposalLines();
		
		for(int i=0; i < lines.length; i++) {

			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];
			if(ref.getDbdestin()!=null) {
				XhbDisposalLineBasicValue item = lines[i];
				
				if("D27".equals(ref.getDbdestin())) {
					if(!DELETED.equals(item.getLineData())) {
						String alcValue = item.getLineData();
						if (item.getLineData().length()>4) { // Maximum value of alc level is 4 characters; if more then trunc to 4
							alcValue = item.getLineData().substring(0,4);
						}
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceAlcoholLevel"+offenceNo+"/ord:D20AlcoholLevelValue", alcValue);
					}
				} else if("D21".equals(ref.getDbdestin())) {
					if(!DELETED.equals(item.getLineData())) {
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceAlcoholLevel"+offenceNo+"/ord:D20AlcoholLevelDropdown", item.getLineData());
					}
				}
			}
		}
		
		log.debug("Exit:: getSubstanceData");
	}

	/**
	 * 
	 * @param disp
	 * @param offenceNo
	 */
	private void getPenaltyData(DisposalValue disp, String offenceNo) {
		log.debug("Entry:: getPenaltyData - offenceNo="+offenceNo);
		
		XhbDisposalLineBasicValue[] lines  = disp.getDisposalLines();
		
		for(int i=0; i < lines.length; i++) {
		
			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];
			if(ref.getDbdestin()!=null) {
				if("D14".equals(ref.getDbdestin())) {
					XhbDisposalLineBasicValue item = lines[i];
					data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffencePenaltyPoints"+offenceNo, item.getLineData());
				}
			}
		}
		
		log.debug("Exit:: getPenaltyData");
	}

	private boolean isDisposalMagistrateVaried(DisposalValue disp, boolean appealVaried) {
		// Do not process magistrate disposals when a Varied exists 
		if ( caseType == CaseType.APPEAL && appealVaried){
			if ( !"C".equals(disp.getCourtType())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param disp
	 * @param offenceNo
	 */
	private void getDisqData(DisposalValue disp, String offenceNo) {
		log.debug("Entry:: getDisqData - offenceNo="+offenceNo);
		
		XhbDisposalLineBasicValue[] lines = disp.getDisposalLines();
		
		boolean disqDefined = true;
		boolean lifeDefined = true;

		for(int i=0; i < lines.length; i++) {
			XhbDisposalLineBasicValue item = lines[i];
			
			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];

			log.debug(String.format("DBDestin: %s, item: %s", ref.getDbdestin(), item.getLineData()));
			
			if(ref.getDbdestin()!=null) {	
				if ("D2".equals(ref.getDbdestin()) && caseType != CaseType.APPEAL){
					String sentencingDate = item.getLineData();
					Date theDate;
					try {
						SimpleDateFormat deFormat = new SimpleDateFormat("dd-MMM-yyyy");
						theDate = deFormat.parse( sentencingDate );
					} catch (ParseException e) {
						theDate = null;
					}
					setDateOfSentenceIfDifferent(theDate != null ? theDate : new Date(), offenceNo);
				} else if( "D5".equals(ref.getDbdestin())) { // Duration
					if(!DELETED.equals(item.getLineData())) {
						Integer duration =  Integer.parseInt(item.getLineData());
						
						if ( duration == 999999 ){
							data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Days", "99");
							data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Years", "99");
							data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Months", "99");
						}
					} else{
						disqDefined = false;
					}
					
				} else if("D11".equals(ref.getDbdestin())) {
					if(item.getLineData()!=null) {
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Days", "99");
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Years", "99");
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Months", "99");
					}
				} else if("D20".equals(ref.getDbdestin())) {
					if(item.getLineData()!=null) {
						if("Y".equals(item.getLineData()))
							data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqTestPassed"+offenceNo, "DTETP - 4");
					}
				} else if("D23".equals(ref.getDbdestin())) {
					if(item.getLineData()!=null) {
						if("Y".equals(item.getLineData()))
							data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqTestPassed"+offenceNo, "DTTP - 1" );
					}
				} else if("D26".equals(ref.getDbdestin())) {
					if(!DELETED.equals(item.getLineData())) {
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Days", "99");
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Years", "99");
						data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Months", "99");
					} else {						
						lifeDefined = false;
					}
				}
			}
		}
		
		//	For some reason, the D26 dbDestin isn't included, we have to assume 'Life' by the absence of D26, but only if D5 was deleted.
		if ( !disqDefined && lifeDefined ){
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Days", "99");
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Years", "99");
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Months", "99");		
		}
		
		log.debug("Exit:: getDisqData - offenceNo="+offenceNo);
	}

	/**
	 * Populate disqualification for DIS* disposals
	 * DISTOT is a special case
	 * 
	 * @param disp
	 * @param offenceNo
	 */
	private void setDisqualificationValues(DisposalValue disp, String offenceNo, List<DisposalValue> disposals) {
		Integer[] daysMonthsYears = new Integer[] {0,0,0};
		boolean settingValues = false;
		
		if ("DISTOT".equals(disp.getRefDisposalType().getDisposalCode())) {
			// We need to use the longest duration from the disposals on this offence for a DISTOT
			ArrayList<Integer[]> disposalDurations = new ArrayList<Integer[]>();
			
			// using all disposals on this offence loop through each disposal and check for a duration and unit
			for (DisposalValue thisDisp: disposals) {
				Integer[] disposalDuration = getDisposalDuration(thisDisp);
				if (disposalDuration.length > 0) {
					settingValues = true;
					disposalDurations.add(disposalDuration);
				}
			}
				
			daysMonthsYears = calculateLongestDuration(disposalDurations);
			
		} else {
			daysMonthsYears = getDisposalDuration(disp);
			if (daysMonthsYears.length > 0) {
				settingValues = true;
			}
		}
		
		log.debug("Setting disqualification values for display in D20 for offence no:"+offenceNo );
		if (settingValues) {
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Days",formatToString(daysMonthsYears[0]));
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Months", formatToString(daysMonthsYears[1]));
			data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceDisqualifiedPeriod"+offenceNo+"/ord:Years",formatToString(daysMonthsYears[2]));
		}
	}
	
	
	/**
	 * Get the disposal duration for a given disposal
	 * 
	 * @param disp
	 * @return
	 */
	private Integer[] getDisposalDuration(DisposalValue disp) {
		Integer [] daysMonthsYears = {0,0,0};
		XhbDisposalLineBasicValue[] lines = disp.getDisposalLines();
		
		for(int i=0; i < lines.length; i++) {
			
			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];
			if(ref.getDbdestin()!=null) {
				
				if("D5".equals(ref.getDbdestin()) || "D24".equals(ref.getDbdestin())) {
					XhbDisposalLineBasicValue item = lines[i];
					XhbDisposalLineBasicValue duration = lines[i+1];
					if(!DELETED.equals(item.getLineData())) {
						daysMonthsYears = calculateDuration(item.getLineData(), duration.getLineData(), daysMonthsYears);
					}
				}
				if("D26".equals(ref.getDbdestin())) { // Life so set to 99 years, 99 months and 99 days
					XhbDisposalLineBasicValue item = lines[i];
					if(!DELETED.equals(item.getLineData())) {
						daysMonthsYears = new Integer[] {99, 99, 99}; 
					}
				}
		    }
	    }
		
		return daysMonthsYears;
	}
	
	
	/**
	 * Given a list of disposalDurations return the longest
	 * 
	 * @param disposalDurations
	 * @return
	 */
	private Integer[] calculateLongestDuration(ArrayList<Integer[]> disposalDurations) {
		Integer[] longest = disposalDurations.get(0);
		int highestNumDays = 0;
		
		for (Integer[] thisDuration : disposalDurations) {
			int currNumDays = convertToDays(thisDuration);
			if (currNumDays > highestNumDays) {
				highestNumDays = currNumDays;
				longest = thisDuration;
			}
		}
		
		return longest;
	}
	
	
	/**
	 * Given an array of years, months and days convert it all to days to allow comparison
	 * Note: 1 month = 365 / 12 days
	 * @return
	 */
	private int convertToDays(Integer[] period) {
		int numDays = (int) period[0] + 
				(period[1] * (365 / 12)) + 
				(period[2] * 365);
		
		return numDays;
	}

	/**
	 * 
	 * @param unit
	 * @param duration
	 * @param daysMonthsYears
	 * @return
	 */
	private Integer[] calculateDuration(final String unit, final String duration, Integer[] daysMonthsYears) {
	    return DateTimeUtilities.getDuration(Integer.valueOf(unit), duration, daysMonthsYears);
    }

	private String formatToString(int number) {
		return String.format("%02d", number);
	}
	
	
	
	/**
	 * Extracts fine data from disposal and set form for it
	 * @param data
	 */
	private void getFineData(DisposalValue disp,  String offenceNo) {
		log.debug("Entry:: getFineData - offenceNo="+offenceNo);
		
		XhbDisposalLineBasicValue[] lines  = disp.getDisposalLines();
		
		for(int i=0; i < lines.length; i++) {
			XhbRefDisposalLineBasicValue ref = disp.getRefDisposalLines()[i];
			
			if("D12".equals(ref.getDbdestin())) {
				XhbDisposalLineBasicValue item = lines[i];
				if(!DELETED.equals(item.getLineData())) {
					data.setValue("//ord:D20/ord:Offence"+offenceNo+"/ord:OffenceFine"+offenceNo+"/ord:MonetaryValue/ord:Amount", item.getLineData());
				}
			}
		}
		
		log.debug("Exit:: getFineData");
	}

	/**
	 * 
	 *
	 */
	private static class DurationAndUnit {
		String duration = EMPTYSTRING;
		String unit = EMPTYSTRING;
		public DurationAndUnit() {
		}
		private String getDurationType() {
			if (!EMPTYSTRING.equals(duration)) {
				if ("day(s)".equals(duration)) {
					return "Days";
				} else if ("month(s)".equals(duration)) {
					return "Months";
				} else if ("year(s)".equals(duration)) {
					return "Years";
				}
			}
			return null;
		}
	}
	
	
	
	private boolean isAppealAllowed(XhbOrderOffenceData record) {
		return allowedCodes.contains(record.getRefAppCode());
	}
	
	private boolean isAppealDismissed(XhbOrderOffenceData record) {
		return dismissedCodes.contains(record.getRefAppCode());
	}
	
	private boolean isAppealVaried(XhbOrderOffenceData record) {
		return variedCodes.contains(record.getRefAppCode());
	}
	
	private boolean isAppealAbandoned(XhbOrderOffenceData record) {
		return abandonedCodes.contains(record.getRefAppCode());
	}
	
	private boolean isAppealRemitted(XhbOrderOffenceData record) {
		return remittedCodes.contains(record.getRefAppCode());
	}

    private void populateRefAppCodes() {
		try {
			Collection<RefAppResD20MapBasicValue> mappings = XhibitDelegateHelper.getBizRefDelegate().getAppealResultD20Mappings();
	    	if (mappings != null) {
	    		for (RefAppResD20MapBasicValue mapping : mappings) {
	    			if ("Allowed".equals(mapping.getD20Result())) {
	    				allowedCodes.add(mapping.getAppResultCode());
	    			} else if ("Dismissed".equals(mapping.getD20Result())) {
	    				dismissedCodes.add(mapping.getAppResultCode());
	    			} else if ("Varied".equals(mapping.getD20Result())) {
	    				variedCodes.add(mapping.getAppResultCode());
	    			} else if ("Abandoned".equals(mapping.getD20Result())) {
	    				abandonedCodes.add(mapping.getAppResultCode());
	    			} else if ("Remitted".equals(mapping.getD20Result())) {
	    				remittedCodes.add(mapping.getAppResultCode());
	    			} 
	    		}
	    	}
		} catch (ObjectNotFoundException e) {
			log.error("Error getting D20 mappings "+e);
			e.printStackTrace();
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
    }
}
