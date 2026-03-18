package uk.gov.courtservice.xhibit.business.entities.hatesentencing;

import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;

public interface HateSentencingHome extends javax.ejb.EJBLocalHome {

    /**
     * Create method - creates 1 new Hate Sentencing record
     * 
     * @param DefOnCaseId
     * @param RefHateSentenceTypeId
     * @return HateSentence
     * @throws CreateException
     */
    public HateSentencing create(Integer defOnCaseId, Integer refHateSentenceTypeId, String userDisplayName)
            throws CreateException;

    /**
     * Find by primary key and version - to be used for updates and deletes.
     * 
     * @param hateSentencingId
     * @param version
     * @return hateSentencing
     * @throws FinderException
     */

    public HateSentencing findByKeyAndVersion(Integer hateSentencingId, Integer version) throws FinderException;

    /**
     * Find by the unique primary key.
     * 
     * @param hateSentencingId
     * @return hateSentencing
     * @throws FinderException
     */
    public HateSentencing findByPrimaryKey(Integer hateSentencingId) throws FinderException;

    /**
     * Find 0 or more Hate Sentencing rows for a defOnCaseId.
     * 
     * @param defOnCaseId
     * @return Collection IndictmentLog
     * @throws FinderException
     */
    public Collection findByDefendantOnCaseId(Integer defOnCaseID) throws FinderException;
    
    /**
     * Find 0 or more Hate Sentencing rows for a defOnCaseId.
     * 
     * @param defOnCaseId
     * @return Collection IndictmentLog
     * @throws FinderException
     */
    public Collection findNonObsoleteByDefendantOnCaseId(Integer defOnCaseID) throws FinderException;

}
