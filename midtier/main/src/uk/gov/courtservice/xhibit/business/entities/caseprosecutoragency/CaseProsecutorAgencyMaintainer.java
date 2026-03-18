package uk.gov.courtservice.xhibit.business.entities.caseprosecutoragency;

import java.text.ParseException;
import java.text.SimpleDateFormat;

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
 * @author Abdul Rahim Hussain
 * @version 1.0
 * 
 * <Change History/>
 * <P>
 * 17/02/03 - JB - Corrected findbyNumbeTypeAndCourtId to call the correct
 * method on the home
 * </P>
 */

// jdk
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;


import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.exception.OptimisticLockException;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.entities.legalaidorder.LegalAidOrderMaintainer;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirm;
import uk.gov.courtservice.xhibit.business.entities.prosecutorrefsolfirm.ProsecutorRefSolFirmHome;
import uk.gov.courtservice.xhibit.business.vos.services.caseprosecutoragency.CaseProsecutorAgencyValue;

public class CaseProsecutorAgencyMaintainer extends AbstractEntityMaintainer {
	private CaseProsecutorAgencyHome home = null;
	private ProsecutorRefSolFirmHome prosRSFH = null;
	private LegalAidOrderMaintainer maintainer = null;

	public CaseProsecutorAgencyMaintainer() {
		if (home == null) {
			home = (CaseProsecutorAgencyHome) CSServices.getServiceLocator()
					.getLocalHome(CaseProsecutorAgencyHome.class);
		}
		if (prosRSFH == null) {
			prosRSFH = (ProsecutorRefSolFirmHome) CSServices.getServiceLocator()
					.getLocalHome(ProsecutorRefSolFirmHome.class);
		}
		if (maintainer == null) {
			maintainer = new LegalAidOrderMaintainer();
		}
	}
	
	private CaseProsecutorAgencyValue getCaseProsValue(CaseProsecutorAgency next) {
		CaseProsecutorAgencyValue val = new CaseProsecutorAgencyValue();
		val.setCaseProsAgencyID(next.getCaseProsAgencyId());
		val.setCreatedBy(next.getCreatedBy());
		val.setCaseID(next.getCaseId());
		val.setProsecutorType(next.getProsecutorType());
		val.setLastUpdatedBy(next.getLastUpdatedBy());
		val.setRefProsecutorAgencyID(next.getRefProsecutorAgencyId());
		val.setVersion(next.getVersion());
		val.setObsInd(next.getObsInd());
		val.setRespondentStatus(next.getRespondentStatus());
		val.setId(next.getCaseProsAgencyId());
		return val;
	}

	public List<CaseProsecutorAgencyValue> getCaseProsValues(Collection locals) {
		if (locals == null)
			return null;
		ArrayList<CaseProsecutorAgencyValue> values = new ArrayList<CaseProsecutorAgencyValue>();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getCaseProsValue((CaseProsecutorAgency) it.next()));
		}
		return values;
	}

	public CaseProsecutorAgency findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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

	public Collection findByCaseId(Integer caseId)  {
		try {
			return home.findByCaseId(caseId);
		}  catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findByRefProsecutorAgencyId(Integer refProsecutorAgencyId) {
		try {
			return home.findByRefProsecutorAgencyId(refProsecutorAgencyId);
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new java.lang.UnsupportedOperationException();
	}

	public void amendProsecutor(List<CaseProsecutorAgencyValue> prosRepBasicValue, String userDisplayName,
			Integer caseId) {

		try {
			if (!prosRepBasicValue.isEmpty()) {
				// get current prosecutors on case in a list
				List<CaseProsecutorAgencyValue> currentProsecutorsOnCase = getCaseProsValues(findByCaseId(
						caseId));

				// for each prosecutor in current list, if not in new list
				// delete it
				for (CaseProsecutorAgencyValue prosecutorCurrent : currentProsecutorsOnCase) {
					boolean toKeep = false;
					for (CaseProsecutorAgencyValue prosecutorNew : prosRepBasicValue) {
						if (prosecutorNew.getCaseProsAgencyID() != null && (prosecutorNew.getCaseProsAgencyID())
								.equals((prosecutorCurrent.getCaseProsAgencyID()))) {
							toKeep = true;
							// only update if the value has changed
							if (hasChanged(prosecutorCurrent, prosecutorNew)) {
								update(prosecutorNew, userDisplayName);
							}
							break;
						}
					}
					if (!toKeep) {
						delete(prosecutorCurrent.getCaseProsAgencyID(), prosecutorCurrent.getVersion(),
								userDisplayName);
					}
				}
				// for each prosecutor in new list, if in current list update
				for (CaseProsecutorAgencyValue prosecutorNew : prosRepBasicValue) {
					boolean toAdd = true;
					for (CaseProsecutorAgencyValue prosecutorCurrent : currentProsecutorsOnCase) {
						if (prosecutorNew.getCaseProsAgencyID() != null && (prosecutorNew.getCaseProsAgencyID()
								.equals(prosecutorCurrent.getCaseProsAgencyID()))) {
							toAdd = false;
							break;
						}
					}
					if (toAdd) {
						// add
						createCaseProsecutorAgency(prosecutorNew, userDisplayName);
					}
				}
			}
			// otherwise if there is no new values - check if there are existing
			// and if so mark them as obsolete
			else {
				ArrayList<CaseProsecutorAgency> currentProsecutorsOnCaseColl = (ArrayList<CaseProsecutorAgency>) findByCaseId(
						caseId);
				ArrayList<CaseProsecutorAgencyValue> currentProsecutorsOnCase = new ArrayList<CaseProsecutorAgencyValue>();
				// convert to basic value
				for (CaseProsecutorAgency pros : currentProsecutorsOnCaseColl) {
					currentProsecutorsOnCase.add(getCaseProsValue(pros));
				}

				// for each prosecutor in current list, if not in new list
				// delete it
				for (CaseProsecutorAgencyValue prosecutorCurrent : currentProsecutorsOnCase) {
					delete(prosecutorCurrent.getCaseProsAgencyID(), prosecutorCurrent.getVersion(), userDisplayName);
				}
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw new EJBException(ex);
		}
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		CaseProsecutorAgencyValue val = (CaseProsecutorAgencyValue) value;
			Integer key = val.getCaseProsAgencyID();
			Integer version = val.getVersion();
			CaseProsecutorAgency prosAgency = findByPrimaryKey(key);
			if (!prosAgency.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {
				prosAgency.setUpdated(userDisplayName);
				prosAgency.setRefProsecutorAgencyId(val.getRefProsecutorAgencyID());
				prosAgency.setRespondentStatus(val.getRespondentStatus());
				prosAgency.setProsecutorType(val.getProsecutorType());
			}
	}

	public void delete(Integer id, Integer version, String userDisplayName) throws ObjectNotFoundException {
		CaseProsecutorAgency caseProsAgency = findByPrimaryKey(id);
		if (!caseProsAgency.getVersion().equals(version)) {
			throw new OptimisticLockException("Optimistic Lock Error");
		} else {
			caseProsAgency.setObsInd("Y");
			caseProsAgency.setUpdated(userDisplayName);
		}

		// check if it has private representation and if so set end date to
		// current date
		try {
			Date cDate = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("dd-MMM-yy");
			String t = ft.format(cDate);
			Date currentDate = ft.parse(t);
			ArrayList<ProsecutorRefSolFirm> prosRSF = (ArrayList<ProsecutorRefSolFirm>) prosRSFH.findPrivateRepByCaseProsAgency(id);
			
			if (!prosRSF.isEmpty()) {
				prosRSF.get(0).setRepEndDate(currentDate);
				prosRSF.get(0).setUpdated(userDisplayName);
			}
		} catch (ParseException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		} catch(ObjectNotFoundException e) {
			// No private rep found. Do not throw exception
		} 
		catch (FinderException e) {
			throw new EJBException(e);
		} 
	}

	public CaseProsecutorAgency createCaseProsecutorAgency(CaseProsecutorAgencyValue value, String userDisplayName) {
		try {
			return home.create(value.getProsecutorType(), value.getCaseID(), value.getRefProsecutorAgencyID(),
					value.getRespondentStatus(), userDisplayName);
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		// Not currently implemented , use delete method above

	}

	/**
	 * Returns true if any of the 3 main properties have changed.
	 * 
	 * @param oldValue
	 *            (the value in the database)
	 * @param newValue
	 *            (the value to change to)
	 * @return true/false depending on whether the values have changed.
	 */
	public boolean hasChanged(CaseProsecutorAgencyValue oldValue, CaseProsecutorAgencyValue newValue) {
		if (!(oldValue.getCaseProsAgencyID().equals(newValue.getCaseProsAgencyID()))) {
			return true;
		}
		if (!(oldValue.getCaseID().equals(newValue.getCaseID()))) {
			return true;
		}
		if (!(oldValue.getProsecutorType().equals(newValue.getProsecutorType()))) {
			return true;
		}
		if (!(oldValue.getRefProsecutorAgencyID().equals(newValue.getRefProsecutorAgencyID()))) {
			return true;
		}
		// if it's a respondent then check this value as well
		return (oldValue.getRespondentStatus() != null
				&& !(oldValue.getRespondentStatus().equals(newValue.getRespondentStatus()))) ;

	}
}
