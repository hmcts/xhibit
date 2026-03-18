package uk.gov.courtservice.xhibit.business.entities.shlegrep;

import java.sql.Timestamp;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ShLegRepHome extends javax.ejb.EJBLocalHome {
    public ShLegRep create(
            Integer crestSequenceNo, 
            String legalRole, 
            String isSignedIn, 
            String solFirmOrRefLegalRep,
            Integer refLegalRepId, 
            Integer refDefenceCategoryId, 
            Integer refSolicitorFirmId,
            Integer substitutedRefLegalRepId,
            String subInst, String userDisplayName) throws CreateException;

    public ShLegRep findByPrimaryKey(Integer shLegRepId) throws FinderException;

    /**
     * Search by scheduled hearing Id on shLegRep Bean
     * 
     * @param scheduledHearingId
     *            Scheduled Hearing Id.
     * @return Collection
     * @throws FinderException
     */
    public Collection findByScheduledHearingId(Integer scheduledHearingId) throws FinderException;

    public Collection findBySHIdRoleAndSolFirmOrRefLegRep(Integer scheduledHearingId, String legalRole,
            String solFirmOrRefLegRep) throws FinderException;

    public Collection findByRefLegRepIDForDateHearingID(Integer refLegRepId, Timestamp startDate, Timestamp endDate,
            Integer hearingId) throws FinderException;
}