package uk.gov.courtservice.xhibit.business.entities.orders;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.EJBLocalHome;
import javax.ejb.FinderException;

public interface RefAggravatingReasonsHome extends EJBLocalHome {
	public RefAggravatingReasons create(Integer refAggravatingReasonsId, String reasonDescription, 
			String obsInd, String userDisplayName) throws CreateException;

	public RefAggravatingReasons findByPrimaryKey(Integer refAggravatingReasonsId) throws FinderException;
	public Collection<RefAggravatingReasons> findAllNonObsolete() throws FinderException;
}