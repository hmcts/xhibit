package uk.gov.courtservice.xhibit.business.entities.defendantoncase;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.caze.Case;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseHome;
import uk.gov.courtservice.xhibit.business.entities.caze.CaseMaintainer;
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.entities.defendant.DefendantHome;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirm;
import uk.gov.courtservice.xhibit.business.entities.defoncaserefsolfirm.DefOnCaseRefSolFirmHome;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrderMaintainer;
import uk.gov.courtservice.xhibit.business.services.createcase.GenerateCaseGroupControllerBeanBusinessDelegate;
import uk.gov.courtservice.xhibit.business.services.defendant.DefendantOnCaseDatabaseManager;
import uk.gov.courtservice.xhibit.business.vos.entities.CaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseBasicValue;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantOnCaseComplexValue;
import uk.gov.courtservice.xhibit.business.vos.services.caselinking.CaseLinkingValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author unascribed
 * @author Abdul Rahim Hussain
 * @author Ian Hannaford
 * @version 2.1
 * @history 24/02/03 Ian Hannaford Need to change update, instead of throwing
 *          <code>UnsupportedOperationException</code> Exception, it needed to
 *          be able to update isMasked and maskedName
 * @history 21/04/09 Kelvin Davies - CCN1263 - Updated to include the
 *          Date_Exported field.
 * @history 17/05/11 Brian Hingston - RFC2878 - Updated to include
 *          Amended_Date_Exported and Amended_Reason fields
 * @history 21/07/14 Brian Hingston - RFS4224 - Updated to include Hate Crime
 *          fields
 * 
 * @history 04/09/17 Nia Walters - adding find by defendant id for case creation
 *          purposes
 * @history 15/09/17 Nia Walters - adding create method for insertion into the
 *          table
 * 
 */
public class DefendantOnCaseMaintainer extends AbstractEntityMaintainer {
	private static DefendantOnCaseMaintainer instance;

	// @todo - shouldn't this be static?
	private DefendantOnCaseHome home = null;
	private CaseHome caseHome = null;
	private DefendantHome defHome = null;
	private DefOnCaseRefSolFirmHome docRSFH = null;
	private static Logger log = CSServices.getLogger(DefendantOnCaseMaintainer.class);
	private LegalAidOrderMaintainer maintainer = null;
	private static final String PACKAGE_NAME = "uk.gov.courtservice.xhibit.business.entities.defendantoncase";
	private static final String CLASS_NAME = "DefendantOnCaseMaintainer";
	private static final String ERROR_IN = "ERROR IN :";


	public DefendantOnCaseMaintainer() {
		if (home == null) {
			home = (DefendantOnCaseHome) CSServices.getServiceLocator().getLocalHome(DefendantOnCaseHome.class);
		}
		if (caseHome == null) {
			caseHome = (CaseHome) CSServices.getServiceLocator().getLocalHome(CaseHome.class);
		}
		if (defHome == null) {
			defHome = (DefendantHome) CSServices.getServiceLocator().getLocalHome(DefendantHome.class);
		}
		if (docRSFH == null) {
			docRSFH = (DefOnCaseRefSolFirmHome) CSServices.getServiceLocator()
					.getLocalHome(DefOnCaseRefSolFirmHome.class);
		}
		if(maintainer == null) {
			maintainer = new LegalAidOrderMaintainer();
		}
	}

	/**
	 * Created singleton access point for this maintainer. Constructor is still
	 * public to allow the gradual change to use this.
	 * 
	 * @return
	 */
	public static DefendantOnCaseMaintainer getInstance() {
		if (instance == null) {
			synchronized (DefendantOnCaseMaintainer.class) {
				if (instance == null) {
					instance = new DefendantOnCaseMaintainer();
				}
			}
		}

		return instance;
	}

	public DefendantOnCaseBasicValue getDefendantOnCaseBasicValue(DefendantOnCase local) {
		DefendantOnCaseBasicValue value = new DefendantOnCaseBasicValue(local.getDefendantOnCaseId(),
				local.getVersion());
		setDefendantOnCaseBasicValue(value, local);
		return value;
	}

	public DefendantOnCaseComplexValue getDefendantOnCaseComplexValue(DefendantOnCase local) {
		DefendantOnCaseComplexValue value = new DefendantOnCaseComplexValue(local.getDefendantOnCaseId(),
				local.getVersion());
		setDefendantOnCaseBasicValue(value, local);
		return value;
	}

	public Collection getDefendantOnCaseBasicValues(Collection locals) {
		if (locals == null)
			return null;

		Collection<DefendantOnCaseBasicValue> values = new ArrayList<DefendantOnCaseBasicValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getDefendantOnCaseBasicValue((DefendantOnCase) it.next()));
		}
		return values;
	}

	public Collection getDefendantOnCaseComplexValues(Collection locals) {
		if (locals == null)
			return null;

		Collection<DefendantOnCaseComplexValue> values = new ArrayList<DefendantOnCaseComplexValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getDefendantOnCaseComplexValue((DefendantOnCase) it.next()));
		}
		return values;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new UnsupportedOperationException();
	}

	public Integer create(Case thisCase, Defendant defendant, DefendantOnCaseBasicValue defOnCaseBasicValue,
			String userDisplayName) {
		try {
			if(thisCase.getCaseType().equals("T") && 
					(defOnCaseBasicValue.getCurrentBcStatus().equals("J")||defOnCaseBasicValue.getCurrentBcStatus().equals("C"))) {
				defOnCaseBasicValue.setCtlApplies("Y");
			}
				
			DefendantOnCase d = home.create(thisCase, defendant, defOnCaseBasicValue.getAsn(),
					defOnCaseBasicValue.getCurrentBcStatus(), defOnCaseBasicValue.getIsJuvenile(),
					defOnCaseBasicValue.getDrivingDisqSuspendedDate(), defOnCaseBasicValue.getPncId(),
					defOnCaseBasicValue.getPtiurn(), defOnCaseBasicValue.getIsMasked(),
					defOnCaseBasicValue.getMaskedName(), defOnCaseBasicValue.getHateIndicator(),
					defOnCaseBasicValue.getHateType(), defOnCaseBasicValue.getBenchWarrantExecDate(),
					defOnCaseBasicValue.getBcStatusBwExecuted(), defOnCaseBasicValue.getMagCourtFirstHearingDate(),
					defOnCaseBasicValue.getMagCourtFinalHearingDate(), defOnCaseBasicValue.getNationality(),
					defOnCaseBasicValue.getSection28Name1(), defOnCaseBasicValue.getSection28Name2(),
					defOnCaseBasicValue.getSection28Phone1(), defOnCaseBasicValue.getSection28Phone2(),
					defOnCaseBasicValue.getCustodyTimeLimit(), defOnCaseBasicValue.getCommBcStatus(), userDisplayName,
					defOnCaseBasicValue.getDefendantNumber(),defOnCaseBasicValue.getDateOfCommittal()==null? null: new Timestamp(defOnCaseBasicValue.getDateOfCommittal().getTimeInMillis()),
					defOnCaseBasicValue.getCtlApplies(), defOnCaseBasicValue.getDarRetentionPolicyId());
			return d.getDefendantOnCaseId();
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	public void amendDefendantsOnCase(List<DefendantOnCaseBasicValue> defOnCaseBasicValue, String userDisplayName,
			Integer caseId, CaseBasicValue cbv) {
		final String METHOD_NAME = "amendDefendantsOnCase";
		log.debug("Entered "+METHOD_NAME+" for case "+caseId);
		try {
			if (defOnCaseBasicValue.size() > 0) {
				// get current defendants on case in a list
				ArrayList<DefendantOnCase> currentDefendantsOnCaseColl = (ArrayList<DefendantOnCase>) findByCaseId(
						caseId);
				ArrayList<DefendantOnCaseBasicValue> currentDefendantsOnCase = new ArrayList<DefendantOnCaseBasicValue>();
				// convert to basic value
				for (DefendantOnCase defs : currentDefendantsOnCaseColl) {
					currentDefendantsOnCase.add(getDefendantOnCaseBasicValue(defs));
				}

				// for each defendant in current list, if not in new list delete
				// it
				for (DefendantOnCaseBasicValue defCurrent : currentDefendantsOnCase) {
					Boolean toKeep = false;
					for (DefendantOnCaseBasicValue defNew : defOnCaseBasicValue) {
						if (defNew.getId() != null && (defNew.getId()).equals((defCurrent.getId()))) {
							toKeep = true;
							update(defNew, userDisplayName);
							break;
						}
					}
					if (!toKeep) {
						delete(defCurrent.getId(), defCurrent.getVersion(), userDisplayName);
					}
				}
				
				int groupNum = 0;
				ArrayList<CaseLinkingValue> casesToUpdate = new ArrayList<CaseLinkingValue>();
				
				// for each defendant in new list, if in current list update
				for (DefendantOnCaseBasicValue defNew : defOnCaseBasicValue) {
					Boolean toAdd = true;
					for (DefendantOnCaseBasicValue defCurrent : currentDefendantsOnCase) {
						if (defNew.getId() != null && (defNew.getId().equals(defCurrent.getId()))) {
							toAdd = false;
							break;
						}
					}
					Integer defOnCaseId = null;
					if (toAdd) {
						// add
						Case currentCase = caseHome.findByPrimaryKey(caseId);
						Defendant def = defHome.findByPrimaryKey(defNew.getDefendantID());
						
						// Moved these from case controller bean as this only needs to be done when a 
						// defendant is added to the case, better to do here as less complex than trying 
						// to re-implement business logic in CaseControllerBean. 
						if (defNew.getCommBcStatus() != null) {
							defNew.setCurrentBcStatus(defNew.getCommBcStatus());
							
							if (defNew.getCurrentBcStatus().equals("J") || defNew.getCurrentBcStatus().equals("C")) {
								if (def != null) {
									def.setCurrentPrisonStatus("Y");
									def.setUpdated(userDisplayName);
								}
								
								// sentence case has no mag first hearing date
								if (defNew.getMagCourtFirstHearingDate() != null) {
									int noOfDays = (26 * 7) - 1;	// 26 weeks minus 1 day
									Date modifiedDate = new Date(defNew.getMagCourtFirstHearingDate().getTime());
									
									// Add the number of days to the date
									Calendar modifiedCalendar = Calendar.getInstance();
									modifiedCalendar.setTime(modifiedDate);
									modifiedCalendar.add(Calendar.DAY_OF_YEAR, noOfDays);
									
									defNew.setCustodyTimeLimit(new Timestamp(modifiedCalendar.getTimeInMillis()));
								}
							}
						}
												
						// This returns cases where defendant id given is on an active case
						ArrayList<CaseLinkingValue> clvListGroupNull = (ArrayList<CaseLinkingValue>) findLinkedCases(cbv.getCourtID(), 
								defNew.getDefendantID(), null);
						
						Integer oldGroupNum = cbv.getCaseGroupNumber();
						
						// Is the defendant on other active cases?
						if (clvListGroupNull.size() > 0) {
							groupNum = 0;
							
							// Yes: are any of these cases linked? ie groupNum != null
							for (CaseLinkingValue clvLinked : clvListGroupNull) {								
								if (clvLinked.getCaseGroupNumber() != null) {
									groupNum = GenerateCaseGroupControllerBeanBusinessDelegate.DelegateFactory.getInstance()
											.generateCaseGroupNumber(cbv.getCourtID());
									
									break;	// break out of for loop as no need to go any further
								} 
							}
							
							// if no linked cases were found
							if (groupNum <= 0) {
								// if cbv caseGroupNum is not set then need to create new one
								if (cbv.getCaseGroupNumber() == null) {
									groupNum = GenerateCaseGroupControllerBeanBusinessDelegate.DelegateFactory.getInstance()
											.generateCaseGroupNumber(cbv.getCourtID());
									
									cbv.setCaseGroupNumber(groupNum);
								} else {
									groupNum = cbv.getCaseGroupNumber();
								}
							} else {
								cbv.setCaseGroupNumber(groupNum);
							}
							
							ArrayList<Integer> caseGroupNumberUpdated = new ArrayList<Integer>();
							
							// Iterate through all active cases to be linked
							// If group num is already set then update all linked ones too
							for (CaseLinkingValue clvLinked : clvListGroupNull) {								
								if (clvLinked.getCaseGroupNumber() != null) {
									if (!caseGroupNumberUpdated.contains(clvLinked.getCaseGroupNumber())) {
										// find all grouped via this case num and update with new number
										@SuppressWarnings("unchecked")
										ArrayList<CaseLinkingValue> clvListGroupActive = (ArrayList<CaseLinkingValue>) 
												findActiveCasesWithGroupNumber(cbv.getCourtID(), clvLinked.getCaseGroupNumber());
										
										casesToUpdate.addAll(clvListGroupActive);
										
										caseGroupNumberUpdated.add(clvLinked.getCaseGroupNumber());
									}
								} else {
									// in an else statement as the findActiveCasesWithGroupNumber
									// query will also return the case being used as group number source
									casesToUpdate.add(clvLinked);
								}
							}
							
							// CTX-1999
							if (oldGroupNum != null && oldGroupNum > 0) {
								// find all grouped via this case num and update with new number
								@SuppressWarnings("unchecked")
								ArrayList<CaseLinkingValue> clvGroupThisCase = (ArrayList<CaseLinkingValue>) 
										findActiveCasesWithGroupNumber(cbv.getCourtID(), oldGroupNum);
								
								casesToUpdate.addAll(clvGroupThisCase);
							}
						} 
						
						defOnCaseId = create(currentCase, def, defNew, userDisplayName);
					}
				}
				
				for (CaseLinkingValue clv : casesToUpdate) {
					CaseMaintainer caseMaintainer = new CaseMaintainer();
					Case cs = caseMaintainer.findByPrimaryKey(clv.getCaseId());
					CaseBasicValue caseToLink = caseMaintainer.getCaseBasicValue(cs);
					caseToLink.setCaseGroupNumber(groupNum);
					caseMaintainer.amendCase(caseToLink, userDisplayName);
				}
			}
			// otherwise if there is no new values - check if there are existing
			// and if so mark them as obsolete
			else {
				ArrayList<DefendantOnCase> currentDefendantsOnCaseColl = (ArrayList<DefendantOnCase>) findByCaseId(
						caseId);
				ArrayList<DefendantOnCaseBasicValue> currentDefendantsOnCase = new ArrayList<DefendantOnCaseBasicValue>();
				// convert to basic value
				for (DefendantOnCase defs : currentDefendantsOnCaseColl) {
					currentDefendantsOnCase.add(getDefendantOnCaseBasicValue(defs));
				}

				// for each defendant in current list, if not in new list delete
				// it
				for (DefendantOnCaseBasicValue defCurrent : currentDefendantsOnCase) {
					delete(defCurrent.getId(), defCurrent.getVersion(), userDisplayName);
				}
			}
		} catch (ObjectNotFoundException ex) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+ex);
			throw new EJBException(ex);
		} catch (FinderException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			throw new EJBException(e);
		}
		
		log.debug("Exit "+METHOD_NAME+" for case "+caseId);

	}

	/**
	 * Updates the DefendantOnCase entity
	 * 
	 * @param value
	 *            CSAbsractValue of type DefendantValue
	 */
	public void update(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) {
		final String METHOD_NAME = "update";
		try {
			log.debug("Entered "+METHOD_NAME+" for "+defOnCaseBasicValue.getDefendantOnCaseId());
			Integer key = defOnCaseBasicValue.getId();
			Integer version = defOnCaseBasicValue.getVersion();
			DefendantOnCase defOnCase = findByPrimaryKey(key);
			if (!defOnCase.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				
				//ctx-1990 set the receipt date for DefOnCase
				defOnCase.setDateReceiptNoticeAppeal(defOnCaseBasicValue.getDateReceiptNoticeAppeal());
				defOnCase.setCacdAppealResult(defOnCaseBasicValue.getCacdAppealResult());
				defOnCase.setCacdAppealResultDate(defOnCaseBasicValue.getCacdAppealResultDate());
				defOnCase.setFormNgSentDate(defOnCaseBasicValue.getFormNgSentDate());
				defOnCase.setCoaStatus(defOnCaseBasicValue.getCoaStatus());
				
				// setters to set values from input parameter
				defOnCase.setIsMasked(defOnCaseBasicValue.getIsMasked());
				defOnCase.setMaskedName(defOnCaseBasicValue.getMaskedName());

				defOnCase.setFinalDrivingLicenceStatus(defOnCaseBasicValue.getFinalDrivingLicenceStatus());
				defOnCase.setNoOfTics(defOnCaseBasicValue.getNoOfTICs());
				defOnCase.setResultsVerified(defOnCaseBasicValue.getResultsVerified());
				defOnCase.setCollectMagistrateCourtId(defOnCaseBasicValue.getCollectMagistrateCourtId());

				// MH - added
				log.debug("Set the juvenile flag to : " + defOnCaseBasicValue.getIsJuvenile());
				defOnCase.setIsJuvenile(defOnCaseBasicValue.getIsJuvenile());

				// KD CCN0400 - START
				log.debug("Set the custodial flag to : " + defOnCaseBasicValue.getCustodial());
				defOnCase.setCustodial(defOnCaseBasicValue.getCustodial());

				log.debug("Set the suspended flag to : " + defOnCaseBasicValue.getSuspended());
				defOnCase.setSuspended(defOnCaseBasicValue.getSuspended());

				log.debug("Set the serious drug flag to : " + defOnCaseBasicValue.getSeriousDrugOffence());
				defOnCase.setSeriousDrugOffence(defOnCaseBasicValue.getSeriousDrugOffence());

				log.debug(
						"Set the recommended deportation flag to : " + defOnCaseBasicValue.getRecommendedDeportation());
				defOnCase.setRecommendedDeportation(defOnCaseBasicValue.getRecommendedDeportation());

				log.debug("Set the nationality to : " + defOnCaseBasicValue.getNationality());
				defOnCase.setNationality(defOnCaseBasicValue.getNationality());
				// KD CCN0400 - END

				// KD- CCN12163 - START
				// Guard against Date Exported being reset to Null
				if ( null != defOnCaseBasicValue.getDateExported() && !"".equals(defOnCaseBasicValue.getDateExported())) {
					log.debug("Set Date Exported to: " + convertToTimestamp(defOnCaseBasicValue.getDateExported()));
					defOnCase.setDateExported(convertToTimestamp(defOnCaseBasicValue.getDateExported()));
				}
				// KD- CCN12163 - END

				// RFC2878 -START
				log.debug("Set Amended Date Exported to: " + defOnCaseBasicValue.getAmendedDateExported());
				defOnCase.setAmendedDateExported(convertToTimestamp(defOnCaseBasicValue.getAmendedDateExported()));

				log.debug("Set the Amended Reason to : " + defOnCaseBasicValue.getAmendedReason());
				defOnCase.setAmendedReason(defOnCaseBasicValue.getAmendedReason());
				// RFC2878 -STOP

				// RFS4224 -START
				log.debug("Set Hate Ind: " + defOnCaseBasicValue.getHateIndicator());
				defOnCase.setHateIndicator(defOnCaseBasicValue.getHateIndicator());

				log.debug("Set the Hate Type to : " + defOnCaseBasicValue.getHateType());
				defOnCase.setHateType(defOnCaseBasicValue.getHateType());

				log.debug("Set Hate Sent Ind: " + defOnCaseBasicValue.getHateSentIndicator());
				defOnCase.setHateSentIndicator(defOnCaseBasicValue.getHateSentIndicator());
				// RFC4224 -STOP

				log.debug("Set public display hide to: " + defOnCaseBasicValue.getPublicDisplayHide());
				defOnCase.setPublicDisplayHide(defOnCaseBasicValue.getPublicDisplayHide());

				log.debug("Defendant on case before update : " + defOnCase);

				defOnCase.setUpdated(userDisplayName);

					defOnCase.setDrivingDisqSuspendedDate(defOnCaseBasicValue.getDrivingDisqSuspendedDate());

				if (defOnCaseBasicValue.getMagCourtFirstHearingDate() != null) {
					defOnCase.setMagCourtFirstHearingDate(defOnCaseBasicValue.getMagCourtFirstHearingDate());
				}

				if (defOnCaseBasicValue.getMagCourtFinalHearingDate() != null) {
					defOnCase.setMagCourtFinalHearingDate(defOnCaseBasicValue.getMagCourtFinalHearingDate());
				}

				// add missing fields
				defOnCase.setAsn(defOnCaseBasicValue.getAsn());
				defOnCase.setPncId(defOnCaseBasicValue.getPncId());
				defOnCase.setPtiurn(defOnCaseBasicValue.getPtiurn());
				//added this because during case update (CM) you shouldn't update BCStatus
				if(defOnCaseBasicValue.getCurrentBcStatus()!=null) {
					defOnCase.setCurrentBcStatus(defOnCaseBasicValue.getCurrentBcStatus());
				}
				defOnCase.setBenchWarrantExecDate(defOnCaseBasicValue.getBenchWarrantExecDate());
				defOnCase.setBcStatusBwExecuted(defOnCaseBasicValue.getBcStatusBwExecuted());
				defOnCase.setCommBcStatus(defOnCaseBasicValue.getCommBcStatus());
	
				if (defOnCaseBasicValue.getDateOfCommittal() != null) { 
					defOnCase.setDateOfCommittal(new Timestamp(defOnCaseBasicValue.getDateOfCommittal().getTimeInMillis()));
				}
				defOnCase.setCtlApplies(defOnCaseBasicValue.getCtlApplies());
				
				Defendant dVal = defHome.findByPrimaryKey(defOnCaseBasicValue.getDefendantID());
				defOnCase.setDefendant(dVal);
				defOnCase.setDefendantNumber(defOnCaseBasicValue.getDefendantNumber());

				defOnCase.setDifferenceReport(defOnCaseBasicValue.getDifferenceReport());

				log.debug("Defendant on case after update : " + defOnCase);

				log.debug("completed update of entity [key: " + key + " version:" + version + "]");
				log.debug("ending update(CSAbstractValue value)");

			}
		} catch (ObjectNotFoundException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			throw new OptimisticLockException(e);
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Updates the DefendantOnCase entity with the values from Form A
	 * 
	 * @param defendantOnCaseId
	 *            Integer
	 * @param bcStatus
	 *            String
	 * @param userDisplayName
	 *            String
	 */
	public void updateFormA(final Integer defendantOnCaseId, final DefendantOnCaseBasicValue defOnCase, final String bcStatus, final String userDisplayName) throws ObjectNotFoundException {

		DefendantOnCase defendantOnCase = findByPrimaryKey(defendantOnCaseId);
		if (bcStatus != null) {
			defendantOnCase.setCurrentBcStatus(bcStatus);
			defendantOnCase.setCustodyTimeLimit(defOnCase.getCustodyTimeLimit());
			defendantOnCase.setCtlApplies(defOnCase.getCtlApplies());
			defendantOnCase.setUpdated(userDisplayName);
		}
		
	}

	/**
	 * Updates the DefendantOnCase entity
	 * 
	 * @param value
	 *            CSAbsractValue of type DefendantValue
	 */
	public void updateDefCourtOfAppealValues(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) {
		final String METHOD_NAME = "update";
		try {
			log.debug("Entered "+METHOD_NAME+" for "+defOnCaseBasicValue.getDefendantOnCaseId());
			Integer key = defOnCaseBasicValue.getId();
			Integer version = defOnCaseBasicValue.getVersion();
			DefendantOnCase defOnCase = findByPrimaryKey(key);
			if (!defOnCase.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				
				//ctx-2996 set the receipt date for DefOnCase
				defOnCase.setDateReceiptNoticeAppeal(defOnCaseBasicValue.getDateReceiptNoticeAppeal());
				defOnCase.setCacdAppealResult(defOnCaseBasicValue.getCacdAppealResult());
				defOnCase.setCacdAppealResultDate(defOnCaseBasicValue.getCacdAppealResultDate());
				defOnCase.setFormNgSentDate(defOnCaseBasicValue.getFormNgSentDate());
	 		}
		} catch (ObjectNotFoundException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			throw new OptimisticLockException(e);
		} catch (FinderException e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Updates the DefendantOnCase entity
	 * 
	 * @param value
	 *            CSAbsractValue of type DefendantValue
	 */
	public void updateDefOnCaseCOAStatus(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) {
		final String METHOD_NAME = "update";
		try {
			log.debug("Entered "+METHOD_NAME+" for "+defOnCaseBasicValue.getDefendantOnCaseId());
			Integer key = defOnCaseBasicValue.getId();
 			DefendantOnCase defOnCase = findByPrimaryKey(key);
 			
 			//ctx-2996 set the receipt date for DefOnCase
	 		defOnCase.setCoaStatus(defOnCaseBasicValue.getCoaStatus());
 			 
		} catch (ObjectNotFoundException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			throw new OptimisticLockException(e);
		} 
	}

	public void updateListing(DefendantOnCaseBasicValue defOnCaseBasicValue, String userDisplayName) throws ObjectNotFoundException {
		try {
			Integer key = defOnCaseBasicValue.getId();
			Integer version = defOnCaseBasicValue.getVersion();
			DefendantOnCase defOnCase = home.findByPrimaryKey(key);
			if (!defOnCase.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
			defOnCase.setSection28Name1(defOnCaseBasicValue.getSection28Name1());
			defOnCase.setSection28Name2(defOnCaseBasicValue.getSection28Name2());
			defOnCase.setSection28Phone1(defOnCaseBasicValue.getSection28Phone1());
			defOnCase.setSection28Phone2(defOnCaseBasicValue.getSection28Phone2());
			defOnCase.setCurrentBcStatus(defOnCaseBasicValue.getCurrentBcStatus());
			defOnCase.setCustodyTimeLimit(defOnCaseBasicValue.getCustodyTimeLimit());
			defOnCase.setCtlApplies(defOnCaseBasicValue.getCtlApplies());
			defOnCase.setUpdated(userDisplayName);
	    } catch (ObjectNotFoundException e) {
	        CSServices.getDefaultErrorHandler().handleError(e, getClass());
	        throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
	    }
	}

	/**
	 * Updates the DefendantOnCase entity
	 * 
	 * @param defendantOnCaseId
	 * @param darRetentionPolicyId
	 * @param userDisplayName
	 */
	public void updateDarRetentionPolicy(final Integer defendantOnCaseId, 
			final Integer darRetentionPolicyId, final String userDisplayName) throws FinderException {
		final String METHOD_NAME = "updateDarRetentionPolicy";
		log.debug(METHOD_NAME+"("+defendantOnCaseId+","+darRetentionPolicyId+")");
		try {
			// Get latest version of the record
			DefendantOnCase local = home.findByPrimaryKey(defendantOnCaseId);
			
			// Requires update
			if (darRetentionPolicyId != null && 
					!darRetentionPolicyId.equals(local.getDarRetentionPolicyId())) {
				// Update retention policy
				local.setDarRetentionPolicyId(darRetentionPolicyId);
				if (userDisplayName != null) {
					local.setUpdated(userDisplayName);
				}
			}
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
	    }
	} 
	
	/**
	 * Updates the DefendantOnCase entity
	 * 
	 * @param value
	 *            CSAbsractValue of type DefendantValue
	 */
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof DefendantValue)) {
			throw new IllegalArgumentException(
					"Unexpected type: Expected instance of DefendantValue got " + value.getClass());
		}

		DefendantValue dv = (DefendantValue) value;
		DefendantOnCaseBasicValue defOnCaseBasicValue = dv.getDefOnCaseBasicValue();

		this.update(defOnCaseBasicValue, userDisplayName);
	}

	public void delete(Integer id, Integer version, String userDisplayName) throws ObjectNotFoundException {
		final String METHOD_NAME = "delete";
		log.debug("entered "+METHOD_NAME+" for id "+id);
		DefendantOnCase defOnCase = findByPrimaryKey(id);
		if (!defOnCase.getVersion().equals(version)) {
			throw new OptimisticLockException("Optimistic Lock Error");
		} else {
			defOnCase.setObsInd("Y");
			defOnCase.setUpdated(userDisplayName);
		}

		// check if it has private representation and if so set end date to
		// current date
		try {
			Date cDate = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yy");
			String t = ft.format(cDate);
			Date currentDate = ft.parse(t);
			ArrayList<DefOnCaseRefSolFirm> docRSF = 
			(ArrayList<DefOnCaseRefSolFirm>)docRSFH.findPrivateRepByDefendantOnCaseId(id);
			if (!docRSF.isEmpty()) {
          docRSF.get(0).setRepEndDate(currentDate);
          docRSF.get(0).setUpdated(userDisplayName);
			}
		} catch (ParseException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch (FinderException e) {
			log.error(ERROR_IN+PACKAGE_NAME+CLASS_NAME+METHOD_NAME+e);
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
		log.debug(METHOD_NAME+" exited");
	}

	public DefendantOnCase findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		try {
			return home.findByPrimaryKey(id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public DefendantOnCase findByKeyAndVersion(Integer id, Integer version) throws ObjectNotFoundException {
		try {
			return home.findByKeyAndVersion(id, version);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findByCaseId(Integer caseId) {
		try {
			return home.findByCaseId(caseId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findByDefendantId(Integer defendantId) throws ObjectNotFoundException {
		try {
			return home.findByDefendantId(defendantId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public DefendantOnCase findByDefendantAndCase(Integer defId, Integer caseId) throws ObjectNotFoundException {
		try {
			if (home.findByDefendantAndCase(defId, caseId) != null) {
				return home.findByDefendantAndCase(defId, caseId);
			} else {
				return null;
			}
		} catch (ObjectNotFoundException e) {
			return null;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findByDefendantAndCaseIncludeObsolete(Integer defId, Integer caseId)
			throws ObjectNotFoundException {
		try {
			return home.findByDefendantAndCaseIncludeObsolete(defId, caseId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	private void setDefendantOnCaseBasicValue(DefendantOnCaseBasicValue value, DefendantOnCase local) {
		value.setDefendantID(local.getDefendantId());
		value.setCaseID(local.getCaseId());
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		// null is not a meaningful value for number of tics, should default to
		// 0 this saves coding round this in all the vo's 'clients'.
		Integer noOfTICs = local.getNoOfTics();
		value.setNoOfTICs(noOfTICs == null ? new Integer(0) : noOfTICs);
		value.setDefendantOnCaseId(local.getDefendantOnCaseId());
		value.setFinalDrivingLicenceStatus(local.getFinalDrivingLicenceStatus());
		value.setPncId(local.getPncId());
		value.setPtiurn(local.getPtiurn());
		value.setIsJuvenile(local.getIsJuvenile());
		value.setIsMasked(local.getIsMasked());
		value.setMaskedName(local.getMaskedName());
		value.setObsInd(local.getObsInd());
		value.setDefendantNumber(local.getDefendantNumber());
		value.setDateOfCommittal(convertToCalendar(local.getDateOfCommittal()));
		value.setResultsVerified(local.getResultsVerified());
		value.setCollectMagistrateCourtId(local.getCollectMagistrateCourtId());
		value.setAsn(local.getAsn());
		value.setMagCourtFirstHearingDate(local.getMagCourtFirstHearingDate());
		value.setMagCourtFinalHearingDate(local.getMagCourtFinalHearingDate());
		value.setDrivingDisqSuspendedDate(local.getDrivingDisqSuspendedDate());
		value.setBcStatusBwExecuted(local.getBcStatusBwExecuted());
		value.setBenchWarrantExecDate(local.getBenchWarrantExecDate());
		value.setCustodyTimeLimit(local.getCustodyTimeLimit());
		value.setCurrentBcStatus(local.getCurrentBcStatus());

		// CCN1263 - KD
		value.setDateExported(convertToCalendar(local.getDateExported()));

		// CCN0400 - KD
		value.setCustodial(local.getCustodial());
		value.setSuspended(local.getSuspended());
		value.setSeriousDrugOffence(local.getSeriousDrugOffence());
		value.setRecommendedDeportation(local.getRecommendedDeportation());
		value.setNationality(local.getNationality());

		// RFC2878 - BH
		value.setAmendedDateExported(convertToCalendar(local.getAmendedDateExported()));
		value.setAmendedReason(local.getAmendedReason());

		// RFS4224 - BH
		value.setHateIndicator(local.getHateIndicator());
		value.setHateType(local.getHateType());
		value.setHateSentIndicator(local.getHateSentIndicator());

		value.setPublicDisplayHide(local.getPublicDisplayHide());
		value.setCurrentBcStatus(local.getCurrentBcStatus());
		value.setCommBcStatus(local.getCommBcStatus());

		// new case create dates
		value.setDrivingDisqSuspendedDate(local.getDrivingDisqSuspendedDate());
		value.setMagCourtFirstHearingDate(local.getMagCourtFirstHearingDate());
		value.setMagCourtFinalHearingDate(local.getMagCourtFinalHearingDate());
		
		value.setSection28Name1(local.getSection28Name1());
		value.setSection28Phone1(local.getSection28Phone1());
		value.setSection28Name2(local.getSection28Name2());
		value.setSection28Phone2(local.getSection28Phone2());
		
		value.setDifferenceReport(local.getDifferenceReport());
		value.setVersion(local.getVersion());
		
		value.setCoaStatus(local.getCoaStatus());
		value.setFormNgSentDate(local.getFormNgSentDate());
		value.setDateReceiptNoticeAppeal(local.getDateReceiptNoticeAppeal());
		value.setCacdAppealResultDate(local.getCacdAppealResultDate());
		value.setCacdAppealResult(local.getCacdAppealResult());
		value.setCtlApplies(local.getCtlApplies());
 	}

	// Added for CaseCreation
	public Collection findByDefendantIdInCustody(Integer defendantId) throws ObjectNotFoundException {
		try {
			return home.findByDefendantIdInCustody(defendantId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public Collection findDefendantOnActiveCases(Integer defendantId) throws ObjectNotFoundException {
		try {
			return home.findDefendantOnActiveCases(defendantId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	

	public Collection findDefendantOnActiveCasesAndCourt(Integer defendantId, Integer courtId) {
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findDefendantsOnActiveCases(defendantId, courtId);
	}

	public Collection findLinkedCases(Integer courtId, Integer defendantId, Integer caseGroupNumber) {
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findLinkedCases(courtId, defendantId, caseGroupNumber);
	}
	
	public Integer returnCountActiveCases(Integer caseId) {
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.returnCountActiveCases(caseId);
	}
	
	public Collection findActiveCasesWithGroupNumber(Integer courtId, Integer groupNumber) {
		DefendantOnCaseDatabaseManager docDBManager = new DefendantOnCaseDatabaseManager();
		return docDBManager.findActiveCasesWithGroupNumber(courtId, groupNumber);
	}
	
	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new UnsupportedOperationException();
	}
}