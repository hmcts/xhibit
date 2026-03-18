package datamigration1745.database;

import datamigration1745.database.processor.CourtRowProcessor;
import datamigration1745.database.processor.HOPoliceForceRowProcessor;
import datamigration1745.database.processor.DefendantsByCourtRowProcessor;
import datamigration1745.database.processor.OffencesByDefendantRowProcessor;
import javax.sql.DataSource;

import datamigration1745.vos.CourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.OffenceByDefendantVO;

/**
 * A utility class used to extract all of the database actions performed by
 * the Data Migration process for RFC1745.
 *
 */
public class FixedConnectionDataMigrationDatabase extends DataMigrationDatabase implements DataMigrationDatabaseInterface  {

    StandAlonePerformantDataSource standAlonePerformantDataSource = null;
    
    private static Integer nbrRetries = 0;
    
    private final static Integer maxRetries = new Integer(System.getProperty("datamigration.maxretries","10"));
    
    /**
     * Get the list of courts.
     *
     * @return A collection containing <code>CourtVO</code> objects
     *         corresponding to all the
     */
    public CourtVO[] getCourts() {
        CourtVO[] courts = null;
        try
        {
            courts = getCourtsResults();
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed getCourts so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
            
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                courts = getCourtsResults();
            }
            else
            {
                throw r;
            }
        }  
        return courts;
    }
    
    private CourtVO[] getCourtsResults() {
        final CourtRowProcessor rp = new CourtRowProcessor();
        final DataMigrationStoredProcedure sp = createStoredProcedure(GET_COURTS);
        sp.registerInTypes(GET_COURTS_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {});
        return rp.getCourts();
    }

    /**
     * Get a specific court.
     *
     * @return A <code>CourtVO</code> object corresponding to a specific court
     */
    public CourtVO getCourt(Integer courtId) {
        CourtVO court = null;
        try
        {
            court = getCourtResults(courtId);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed getCourt so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                court = getCourtResults(courtId);
            }
            else
            {
                throw r;
            }
        }  
        return court;
    }
       
    private CourtVO getCourtResults(Integer courtId) {
        final CourtRowProcessor rp = new CourtRowProcessor();
        final DataMigrationStoredProcedure sp = createStoredProcedure(GET_COURT);
        sp.registerInTypes(GET_COURT_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {courtId});
        CourtVO[] courts = rp.getCourts();
        if (courts.length > 0) {
            return courts[0];
        } else {
            return null;
        }
    }

    /**
     * Get the list of ref data entries of type HO Police Force for a court.
     *
     * @return A collection containing <code>HOPoliceForceVO</code> objects
     *         corresponding to all the HO Police Force ref data entries
     *         for the court_id
     */
    public HOPoliceForceVO[] getPoliceForceData (Integer courtId) {
        HOPoliceForceVO[] hoPoliceForce = null;
        try
        {
            hoPoliceForce = getPoliceForceDataResults(courtId);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed getPoliceForceData so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                hoPoliceForce = getPoliceForceDataResults(courtId);
            }
            else
            {
                throw r;
            }
        }  
        return hoPoliceForce;
    }
    
    private HOPoliceForceVO[] getPoliceForceDataResults (Integer courtId) {
        final HOPoliceForceRowProcessor rp = new HOPoliceForceRowProcessor();
        final DataMigrationStoredProcedure sp = createStoredProcedure(GET_HO_POLICE_FORCE);
        sp.registerInTypes(GET_HO_POLICE_FORCE_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {courtId});
        return rp.getHoPoliceForceList();
    }

    /*
     *  Returns defendants for a particular court
     */
    public DefendantByCourtVO[] getDefendantsByCourt(Integer courtId) {
        DefendantByCourtVO[] defendantByCourtVO = null;
        try
        {
            defendantByCourtVO = getDefendantsByCourtResults(courtId);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed getDefendantsByCourtResults so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                defendantByCourtVO = getDefendantsByCourtResults(courtId);
            }
            else
            {
                throw r;
            }
        }  
        return defendantByCourtVO;
    }
    
    private DefendantByCourtVO[] getDefendantsByCourtResults(Integer courtId) {
        final DefendantsByCourtRowProcessor rp = new DefendantsByCourtRowProcessor();
        final DataMigrationStoredProcedure sp = createStoredProcedure(GET_DEFENDANTS_BY_COURT);
        sp.registerInTypes(GET_DEFENDANTS_BY_COURT_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {courtId});
        return rp.getDefendantsByCourtList();
    }

    /**
     * Updates single defendant on case information with asn and ptiurn.
     * @param defendantOnCaseId
     * @param asn
     * @param ptiurn
     */
    public void updateDefendantOnCase(Integer defendantOnCaseId, String asn, String ptiurn) {
        try
        {
            updateDefendantOnCaseResults(defendantOnCaseId,asn,ptiurn);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed updateDefendantOnCase so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                updateDefendantOnCaseResults(defendantOnCaseId,asn,ptiurn);
            }
            else
            {
                throw r;
            }
        } 
    }
    
    private void updateDefendantOnCaseResults(Integer defendantOnCaseId, String asn, String ptiurn) {
        validateParameterNotNull("Defendant on Case Id", defendantOnCaseId);
        final DataMigrationStoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_ON_CASE);
        sf.registerInTypes(UPDATE_DEFENDANT_ON_CASE_TYPES);
        sf.executeFunction(new Object[] {defendantOnCaseId, asn, ptiurn });
    }

    /**
     * Updates single defendant on offence information with seq no.
     * @param defendantOnOffenceId
     * @param seqNo
     */
    public void updateDefendantOnOffence(Integer defendantOnOffenceId, String seqNo) {
        try
        {
            updateDefendantOnOffenceResults(defendantOnOffenceId,seqNo);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed updateDefendantOnOffence so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                updateDefendantOnOffenceResults(defendantOnOffenceId,seqNo);
            }
            else
            {
                throw r;
            }
        } 
    }
    
    private void updateDefendantOnOffenceResults(Integer defendantOnOffenceId, String seqNo) {
        validateParameterNotNull("Defendant on Offence Id", defendantOnOffenceId);
        final DataMigrationStoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_ON_OFFENCE);
        //final StoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_ON_OFFENCE);
        sf.registerInTypes(UPDATE_DEFENDANT_ON_OFFENCE_TYPES);
        sf.executeFunction(new Object[] {defendantOnOffenceId, seqNo });
    }

    /**
     * Updates the data migration totals for a defendants on case and offence for a given court.
     * @param courtId
     * @param totalDefendantsOnCase
     * @param totalDefendantsOnOffence
     */
    public void updateDataMigrationTotals(Integer courtId, Integer totalDefendantsOnCase, Integer totalDefendantsOnOffence) {
        try
        {
            updateDataMigrationTotalsResults(courtId,totalDefendantsOnCase,totalDefendantsOnOffence);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed updateDataMigrationTotals so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
            
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                updateDataMigrationTotalsResults(courtId,totalDefendantsOnCase,totalDefendantsOnOffence);
            }
            else
            {
                throw r;
            }
        } 
    }
    
    private void updateDataMigrationTotalsResults(Integer courtId, Integer totalDefendantsOnCase, Integer totalDefendantsOnOffence) {
        validateParameterNotNull("Court Id", courtId);
        final DataMigrationStoredFunction sf = createStoredFunction(UPDATE_DATA_MIGRATION_TOTALS);
        sf.registerInTypes(UPDATE_DATA_MIGRATION_TOTALS_TYPES);
        sf.executeFunction(new Object[] {courtId, totalDefendantsOnCase, totalDefendantsOnOffence });
    }

    /**
     * Updates the status of the data migration process to a given value for a given court.
     *
     */
    public void updateStatus(int courtId, String status) {    
        try
        {
            updateStatusResults(courtId,status);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed updateDefendantOnOffence so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
            
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                updateStatusResults(courtId,status);
            }
            else
            {
                throw r;
            }
        } 
    }
    private void updateStatusResults(int courtId, String status) {
        final DataMigrationStoredFunction sf = createStoredFunction(UPDATE_STATUS);

        sf.registerInTypes(UPDATE_STATUS_TYPES);
        sf.executeFunction(new Object[] {courtId, status});
    }

    /*
    *  Returns defendant on offence data by defendant on case ID.
    *  The records are ordered using the rules of precedence specified in the Data Migration design
    */
    public OffenceByDefendantVO[] getOffencesByDefendant(Integer docId) {
        OffenceByDefendantVO[] offenceByDefendantVOs = null;
        try
        {
            offenceByDefendantVOs = getOffencesByDefendantResults(docId);
            nbrRetries = 0;
        }
        catch(RuntimeException r)
        {            
            nbrRetries=nbrRetries+1;
            
            System.out.println("Failed getCourt so retry. Current retry count is:" + nbrRetries + " max consecutive retry count is:" + maxRetries);
                        
            if(nbrRetries<maxRetries)
            {
                standAlonePerformantDataSource=null;
                offenceByDefendantVOs = getOffencesByDefendantResults(docId);
            }
            else
            {
                throw r;
            }
        }  
        return offenceByDefendantVOs;
    }
    
    private OffenceByDefendantVO[] getOffencesByDefendantResults(Integer docId) {
        final OffencesByDefendantRowProcessor rp = new OffencesByDefendantRowProcessor();
        final DataMigrationStoredProcedure sp = createStoredProcedure(GET_OFFENCES_BY_DEFENDANT);
        sp.registerInTypes(GET_OFFENCES_BY_DEFENDANT_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {docId});
        return rp.getOffencesByDefendant();
    }


    protected DataMigrationStoredProcedure createStoredProcedure(final String name) {
        return new DataMigrationStoredProcedure(getDataSource(), name);
    }

    protected DataMigrationStoredFunction createStoredFunction(final String name) {
        return new DataMigrationStoredFunction(getDataSource(), name);
    }
    
    protected DataSource getDataSource() {
        
        if(standAlonePerformantDataSource==null)
        {
            System.out.println("FixedConnectionDataMigrationDatabase:create standAlonePerformantDataSource");
        
            standAlonePerformantDataSource = new StandAlonePerformantDataSource();
            standAlonePerformantDataSource.resetConnectionPool();
        }
        return standAlonePerformantDataSource;
    }
}
