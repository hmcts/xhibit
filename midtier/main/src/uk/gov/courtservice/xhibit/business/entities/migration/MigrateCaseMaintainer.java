package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Collection;

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
import uk.gov.courtservice.xhibit.business.vos.entities.MigrateCaseBasicValue;

public class MigrateCaseMaintainer extends AbstractEntityMaintainer {
	
	private static Logger log = CSServices.getLogger(MigrateCaseMaintainer.class);

	private final static String YES = "Y";
	
	private MigrateCaseHome home = null;

	public MigrateCaseMaintainer() {
		if (home == null) {
			home = (MigrateCaseHome) CSServices.getServiceLocator().getLocalHome(MigrateCaseHome.class);
		}
	}

	public MigrateCase findByPrimaryKey(Integer id) throws ObjectNotFoundException {
		log.debug("*** entered into findByPrimaryKey ***");
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

	public Collection<MigrateCase> findByCaseId(Integer caseId) throws ObjectNotFoundException {
		log.debug("*** entered into findByCaseId ***");
		try {
			return home.findByCaseId(caseId);
		} catch (ObjectNotFoundException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw e;
		} catch (FinderException e) {
			CSServices.getDefaultErrorHandler().handleError(e, this.getClass());
			throw new EJBException(e);
		}
	}
	
	public MigrateCaseBasicValue getBasicValue(MigrateCase local) {
		MigrateCaseBasicValue basicValue = new MigrateCaseBasicValue(local.getMigrateCaseId(), local.getVersion());
		loadValue(basicValue, local);
		return basicValue;
	}

	protected void loadValue(MigrateCaseBasicValue basicValue, MigrateCase local) {
		basicValue.setMigrateCaseId(local.getMigrateCaseId());
		basicValue.setCaseId(local.getCaseId());
		basicValue.setMigrated(local.getMigrated());
		basicValue.setMigrationTo(local.getMigrationTo());
		basicValue.setMigrationToUrn(local.getMigrationToUrn());
		basicValue.setMigrationDate(local.getMigrationDate());
	}
	
	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof MigrateCaseBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			MigrateCaseBasicValue basicValue = (MigrateCaseBasicValue) value;
			MigrateCase local = home.create(
					basicValue.getMigrateCaseId(), basicValue.getCaseId(),
					basicValue.getMigrated(), basicValue.getMigrationTo(),
					basicValue.getMigrationToUrn(), basicValue.getMigrationDate(),
					userDisplayName);
			return local;
		} catch (CreateException e) {
			CSServices.getDefaultErrorHandler().handleError(e, getClass());
			throw new EJBException(e);
		}
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) {
		if (!(value instanceof MigrateCaseBasicValue)) {
			throw new IllegalArgumentException("Unexpected type:" + value.getClass());
		}
		try {
			// Get the current db values
			MigrateCase local = home.findByPrimaryKey(value.getId());

			// Check the version
			if (local.getVersion() == null || value.getVersion() == null
					|| !local.getVersion().equals(value.getVersion())) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			MigrateCaseBasicValue basicValue = (MigrateCaseBasicValue) value;

			// Update the record
			local.setCaseId(basicValue.getCaseId());
			local.setMigrated(basicValue.getMigrated());
			local.setMigrationTo(basicValue.getMigrationTo());
			local.setMigrationToUrn(basicValue.getMigrationToUrn());
			local.setMigrationDate(basicValue.getMigrationDate());
			
			if (userDisplayName != null) {
				local.setUpdated(userDisplayName);
			}
		} catch (ObjectNotFoundException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		} catch (FinderException ex) {
			CSServices.getDefaultErrorHandler().handleError(ex, getClass());
			throw new EJBException(ex);
		}
	}

	@Override
	public void delete(Integer id, Integer version) {
		throw new java.lang.UnsupportedOperationException();
	}
	
    public void delete(Integer id, Integer version, String userDisplayName) {
    	try {
			// Get the current db values
			MigrateCase local = home.findByPrimaryKey(id);

			// Check the version
			if (local.getVersion() == null || version == null
					|| !local.getVersion().equals(version)) {
				throw new OptimisticLockException("Optimistic Lock Error");
			}
			
	    	// Update the record
			if (userDisplayName != null) {
				local.setUpdated(userDisplayName);
			}
			
	    } catch (ObjectNotFoundException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    } catch (FinderException ex) {
	        CSServices.getDefaultErrorHandler().handleError(ex, getClass());
	        throw new EJBException(ex);
	    }		

    }
}