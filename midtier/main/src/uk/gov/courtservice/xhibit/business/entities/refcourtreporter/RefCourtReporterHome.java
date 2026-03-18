package uk.gov.courtservice.xhibit.business.entities.refcourtreporter;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefCourtReporterHome extends javax.ejb.EJBLocalHome {
    public RefCourtReporter create(String firstName, String middleName, String surname, String initials,
            String reportMethod, String obsInd, Integer courtId, Integer refCourtReporterFirmId,
            Integer crestCourtReporterId, String userDisplayName) throws CreateException;

    public RefCourtReporter findByPrimaryKey(Integer refCourtReporterId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}