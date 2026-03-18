package uk.gov.courtservice.xhibit.business.entities.exporta;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface ExportAHome extends javax.ejb.EJBLocalHome {

    /**
     * Create method - creates 1 new ExportA record
     * 
     * @param courtClerkExport
     * @param statusFlag
     * @param linkedHearingID
     * @param hearingId
     * @return ExportA
     * @throws CreateException
     */
    public ExportA create(String courtClerkExport, String statusFlag, Integer linkedHearingID, Integer hearingId, String userDisplayName)
            throws CreateException;

    /**
     * Find by primary key and version - to be used for updates and deletes.
     * 
     * @param exportAId
     * @param version
     * @return ExportA
     * @throws FinderException
     */

    public ExportA findByKeyAndVersion(Integer exportAId, Integer version) throws FinderException;

    /**
     * Find by the unique primary key.
     * 
     * @param exportAId
     * @return ExportA
     * @throws FinderException
     */
    public ExportA findByPrimaryKey(Integer exportAId) throws FinderException;

    /**
     * Find 0 or more ExportA's for a hearingID and/or linkedHearingID.
     * 
     * @param hearingID
     * @param linkedHearingID
     * @return Collection ExportA
     * @throws FinderException
     */
    public Collection findByHearingLinkedHearingID(Integer hearingID, Integer linkedHearingID) throws FinderException;

}