package uk.gov.courtservice.xhibit.business.entities.refappresd20map;

import java.util.Date;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefAppResD20MapHome extends javax.ejb.EJBLocalHome {
	public RefAppResD20Map create(Integer refAppResD20MapId, String appResultCode, String d20Result,
			Date lastUpdateDate, Date creationDate, String createdBy, String lastUpdatedBy, Integer version)
			throws CreateException;

	public RefAppResD20Map findByPrimaryKey(Integer pk) throws FinderException;
	
	public Collection findByAppResultCode(String appResultCode) throws FinderException;

	public Collection<RefAppResD20Map> findAllMappings() throws FinderException;
}
