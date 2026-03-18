package uk.gov.courtservice.xhibit.business.entities.indictmentlog;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface IndictmentLogHome extends javax.ejb.EJBLocalHome {

    /**
     * Create method - creates 1 new Indictment Log record
     * 
     * @param caseId
     * @param SequenceNo
     * @param IndictmentInfo
     * @return IndictmentLog
     * @throws CreateException
     */
    public IndictmentLog create(Integer caseId, Integer SequenceNo, String indictmentInfo, String userDisplayName)
            throws CreateException;

    /**
     * Find by primary key and version - to be used for updates and deletes.
     * 
     * @param indictmentLogId
     * @param version
     * @return indictmentLog
     * @throws FinderException
     */

    public IndictmentLog findByKeyAndVersion(Integer indictmentLogId, Integer version) throws FinderException;

    /**
     * Find by the unique primary key.
     * 
     * @param indictmentLogId
     * @return indictmentLog
     * @throws FinderException
     */
    public IndictmentLog findByPrimaryKey(Integer indictmentLogId) throws FinderException;

    /**
     * Find 0 or more IndictmentLogs's for a caseID.
     * 
     * @param caseID
     * @return Collection IndictmentLog
     * @throws FinderException
     */
    public Collection findByCaseID(Integer caseID) throws FinderException;

}