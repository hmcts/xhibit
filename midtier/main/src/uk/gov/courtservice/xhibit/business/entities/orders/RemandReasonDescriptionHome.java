package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RemandReasonDescriptionHome extends EJBLocalHome {
	public RemandReasonDescription create(Integer remandReasonDescriptionId, String reasonCategory,
			String reasonDescription, 
			String obsInd, String userDisplayName) throws CreateException;

	public RemandReasonDescription findByPrimaryKey(Integer remandReasonDescriptionId) throws FinderException;

	public Collection<RemandReasonDescription> findAllReasonDescriptions() throws FinderException;
}