package uk.gov.courtservice.xhibit.business.entities.defendantonoffence;

import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.ObjectNotFoundException;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.business.entities.AbstractEntityMaintainer;
import uk.gov.courtservice.framework.business.entities.CSEntityLocal;
import uk.gov.courtservice.framework.business.vos.CSAbstractValue;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DefendantOnOffenceMaintainer
 * </p>
 * <p>
 * Description: 
 * </p>
 * <p>
 * Copyright: Copyright (c) 2022
 * </p>
 * <p>
 * Company: CGI
 * </p>
 * 
 * @author Mark Harris
 * @version 1.0
 */
public class DefendantOnOffenceMaintainer extends AbstractEntityMaintainer {

	private static Logger log = CSServices.getLogger(DefendantOnOffenceMaintainer.class);
	
	private DefendantOnOffenceHome home = null;

	public DefendantOnOffenceMaintainer() {
		if (home == null) {
			home = (DefendantOnOffenceHome) CSServices.getServiceLocator().getLocalHome(DefendantOnOffenceHome.class);
		}
	}

	public DefendantOnOffence findByPrimaryKey(Integer id) throws ObjectNotFoundException {
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
	
	/**
	 * Updates the DefendantOnOffence entity
	 * 
	 * @param defendantOnOffenceId
	 * @param darRetentionPolicyId
	 * @param userDisplayName
	 */
	public void updateDarRetentionPolicy(final Integer defendantOnOffenceId, 
			final Integer darRetentionPolicyId, final String userDisplayName) throws FinderException {
		final String METHOD_NAME = "updateDarRetentionPolicy";
		log.debug(METHOD_NAME+"("+defendantOnOffenceId+","+darRetentionPolicyId+")");
		try {
			// Get latest version of the record
			DefendantOnOffence local = home.findByPrimaryKey(defendantOnOffenceId);
			
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
	
	@Override
	public void delete(Integer id, Integer version) throws ObjectNotFoundException {
		throw new UnsupportedOperationException();
	}

	@Override
	public CSEntityLocal create(CSAbstractValue value, String userDisplayName) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void update(CSAbstractValue value, String userDisplayName) throws ObjectNotFoundException {
		throw new UnsupportedOperationException();
	}
}