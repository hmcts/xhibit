package uk.gov.courtservice.xhibit.business.entities.refcourtreporterfirm;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface RefCourtReporterFirmHome extends javax.ejb.EJBLocalHome {
    public RefCourtReporterFirm create(String obsInd, String displayFirst, String dxRef, String vatNo, String firmName,
            Integer courtId, Integer addressId, Integer crestCourtReporterFirmId, String userDisplayName) throws CreateException;

    public RefCourtReporterFirm findByPrimaryKey(Integer refCourtReporterFirmId) throws FinderException;

    public Collection findByCourtId(Integer courtId) throws FinderException;
}