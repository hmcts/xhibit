package uk.gov.courtservice.xhibit.business.entities.migration;

import java.util.Collection;
import java.util.Date;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface MigrateCaseHome extends javax.ejb.EJBLocalHome {
        
    public MigrateCase create(Integer migrateCaseId, Integer caseId,
			String migrated, String migrationTo, String migrationToUrn, Date migrationDate,
			String userDisplayName) throws CreateException;

    public MigrateCase findByPrimaryKey(Integer id) throws FinderException;
    public Collection<MigrateCase> findByCaseId(Integer caseId) throws FinderException;
}