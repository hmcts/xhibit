package uk.gov.courtservice.xhibit.business.entities.refprosecutoragency;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefProsecutorAgencyHome extends javax.ejb.EJBLocalHome {
	// public RefProsecutorAgency create() throws CreateException;
	public RefProsecutorAgency create(Integer refProsecutorAgencyId, String title, String prosecutorName1,
			String prosecutorName2, String prosecutorName3, String initials, Integer addressId, String crestOpposerId,
			Integer courtId, String cpsCode, String dxRef, String obsInd, Date lastUpdateDate, Date creationDate,
			String createdBy, String lastUpdatedBy, Integer version) throws CreateException;

	public RefProsecutorAgency findByPrimaryKey(Integer refProsecutorAgencyId) throws FinderException;

	public RefProsecutorAgency findByRefProsecutorAgencyId(Integer refProsecutorAgencyId) throws FinderException;

	public Collection findByCourtId(Integer courtId) throws FinderException;

	public Collection findByCourtIdAndProsecutorName(Integer courtId, String prosecutorName3) throws FinderException;

	public Collection findByCourtIdAndCpsCode(Integer courtId, String cpsCode) throws FinderException;

	public Collection findByCourtIdProsecutorNameAndCpsCode(Integer courtId, String prosecutorName3, String cpsCode)
			throws FinderException;
}
