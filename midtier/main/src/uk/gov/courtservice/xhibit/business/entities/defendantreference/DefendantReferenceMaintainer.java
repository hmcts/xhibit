package uk.gov.courtservice.xhibit.business.entities.defendantreference;

import java.util.ArrayList;
import java.util.Collection;
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
import uk.gov.courtservice.xhibit.business.entities.defendant.Defendant;
import uk.gov.courtservice.xhibit.business.vos.entities.DefendantReferenceBasicValue;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;

public class DefendantReferenceMaintainer extends AbstractEntityMaintainer {
	private DefendantReferenceHome home = null;

	private static Logger log = CSServices.getLogger(DefendantReferenceHome.class);

	public DefendantReferenceMaintainer() {
		if (home == null) {
			home = (DefendantReferenceHome) CSServices.getServiceLocator().getLocalHome(DefendantReferenceHome.class);
		}
	}

	public DefendantReferenceBasicValue getDefendantReferenceBasicValue(DefendantReference local) {
		DefendantReferenceBasicValue value = new DefendantReferenceBasicValue(local.getDefRefId(), local.getVersion());
		setDefendantReferenceBasicValue(value, local);
		return value;
	}

	public Collection getDefendantReferenceBasicValue(Collection locals) {
		if (locals == null)
			return null;
		List values = new ArrayList();
		Iterator it = locals.iterator();
		while (it.hasNext()) {
			values.add(getDefendantReferenceBasicValue((DefendantReference) it.next()));
		}
		return values;
	}

	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		// Must use the 3 parm create in order to build the CMR
		throw new java.lang.UnsupportedOperationException();
	}

	public CSEntityLocal create(CSAbstractValue value, Defendant defendant, String userDisplayName) {
		log.debug("create(CSAbstractValue value) called");

		if (!(value instanceof DefendantReferenceBasicValue)) {
			throw new IllegalArgumentException(
					"Unexpected type: Expected instance of DefendantReferenceBasicValue got " + value.getClass());
		}

		try {
			DefendantReferenceBasicValue defRefBVO = (DefendantReferenceBasicValue) value;

			DefendantReference defRef = home.create(defRefBVO.getReferenceValue(), defRefBVO.getReferenceName(),
					defRefBVO.getCategory(), defRefBVO.getDefendantID(), defendant, userDisplayName);
			log.debug("created new DefendantReference entity using BVO[" + defRefBVO + "]");
			log.debug("returning create(CSAbstractValue value)");
			return defRef;

		} catch (CreateException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {

		log.debug("update(CSAbstractValue value) called");

		if (!(value instanceof DefendantReferenceBasicValue)) {
			throw new IllegalArgumentException(
					"Unexpected type: Expected instance of DefendantReferenceBasicValue got " + value.getClass());
		}

		try {
			DefendantReferenceBasicValue defRefBVO = (DefendantReferenceBasicValue) value;
			Integer key = defRefBVO.getId();
			Integer version = defRefBVO.getVersion();

			// call to internal method to find entity

			DefendantReference defRef = home.findByPrimaryKey(key);
			if (!defRef.getVersion().equals(defRefBVO.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			} else {

				// setters to set values from input parameter
				defRef.setReferenceValue(defRefBVO.getReferenceValue());
				defRef.setUpdated(userDisplayName);

				log.debug("completed update of entity [key: " + key + " version:" + version + "]");
				log.debug("ending update(CSAbstractValue value)");
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw (ObjectNotFoundException) ex;
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, this.getClass());
			throw new EJBException(ex);
		}
	}

	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}

	public DefendantReference findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		try {
			return home.findByPrimaryKey(id);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw (ObjectNotFoundException) e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public DefendantReference findByKeyAndVersion(Integer id, Integer version) throws ObjectNotFoundException {
		try {
			return home.findByKeyAndVersion(id, version);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw (ObjectNotFoundException) e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public DefendantReference findByDefendantIdAndReferenceName(Integer defendantId, String refName)
			throws ObjectNotFoundException {
		try {
			// This has been changed to expect a collection due to a refresh /
			// resync issue
			// causing duplicate records in XHB_DEFENDANT_REFERENCE which was
			// causing an
			// error here where the code was expecting a single value to be
			// returned. The
			// Refresh/Resync issue is still being investigated with slow
			// progress
			Collection defRefCollection = home.findByDefendantIdAndReferenceName(defendantId, refName);
			if (defRefCollection != null && defRefCollection.size() > 0) {
				return (DefendantReference) defRefCollection.toArray()[0];
			} else {
				throw new ObjectNotFoundException();
			}
		} catch (ObjectNotFoundException e) {
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public String findAllByDefendantIdAndReferenceName(Integer defendantId, String refName)
			throws ObjectNotFoundException {
		try {
			// This has been changed to expect a collection due to a refresh /
			// resync issue
			// causing duplicate records in XHB_DEFENDANT_REFERENCE which was
			// causing an
			// error here where the code was expecting a single value to be
			// returned. The
			// Refresh/Resync issue is still being investigated with slow
			// progress
			Collection defRefCollection = home.findByDefendantIdAndReferenceName(defendantId, refName);
			if (defRefCollection != null && defRefCollection.size() > 0) {
				DefendantReference df = (DefendantReference) defRefCollection.toArray()[0];
				return df.getReferenceValue();

			} else {
				throw new ObjectNotFoundException();
			}
		} catch (ObjectNotFoundException e) {
			log.warn("findByDefendantIdAndReferenceName " + e);
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

	public Collection findByRefNameValue(String referenceName, String referenceValue) {
		try {
			return home.findByRefNameValue(referenceName, referenceValue);
		}
		catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}

	}

	private void setDefendantReferenceBasicValue(DefendantReferenceBasicValue value, DefendantReference local) {
		value.setDefendantID(local.getDefendantId());
		value.setReferenceValue(local.getReferenceValue());
		value.setReferenceName(local.getReferenceName());
		value.setCategory(local.getCategory());
	}

	public Integer findReferenceNameIdByDefendantId(Integer defendantId, String refName)
			throws ObjectNotFoundException {
		try {
			// This has been changed to expect a collection due to a refresh /
			// resync issue
			// causing duplicate records in XHB_DEFENDANT_REFERENCE which was
			// causing an
			// error here where the code was expecting a single value to be
			// returned. The
			// Refresh/Resync issue is still being investigated with slow
			// progress
			Collection defRefCollection = home.findByDefendantIdAndReferenceName(defendantId, refName);
			if (defRefCollection != null && defRefCollection.size() > 0) {
				DefendantReference df = (DefendantReference) defRefCollection.toArray()[0];
				return df.getDefRefId();
			} else {
				throw new ObjectNotFoundException();
			}
		} catch (ObjectNotFoundException e) {
			log.warn("findByDefendantIdAndReferenceName " + e);
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}

}
