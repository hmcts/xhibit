package datamigration1745.database;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;

import datamigration1745.database.processor.CourtRowProcessor;
import datamigration1745.database.processor.HOPoliceForceRowProcessor;
import datamigration1745.database.processor.DefendantsByCourtRowProcessor;
import datamigration1745.database.processor.OffencesByDefendantRowProcessor;

import datamigration1745.vos.CourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.OffenceByDefendantVO;
import uk.gov.courtservice.framework.testutils.StandAloneDataSource;

/**
 * A utility class used to extract all of the database actions performed by
 * the Data Migration process for RFC1745.
 *
 */
public class RecreateConnectionDataMigrationDatabase extends DataMigrationDatabase implements DataMigrationDatabaseInterface {

    /**
     * Get the list of courts.
     *
     * @return A collection containing <code>CourtVO</code> objects
     *         corresponding to all the
     */
    public CourtVO[] getCourts() {
        final CourtRowProcessor rp = new CourtRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_COURTS);
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
        final CourtRowProcessor rp = new CourtRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_COURT);
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
        final HOPoliceForceRowProcessor rp = new HOPoliceForceRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_HO_POLICE_FORCE);
        sp.registerInTypes(GET_HO_POLICE_FORCE_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {courtId});
        return rp.getHoPoliceForceList();
    }

    /*
     *  Returns defendants for a particular court
     */
     public DefendantByCourtVO[] getDefendantsByCourt(Integer courtId) {
         final DefendantsByCourtRowProcessor rp = new DefendantsByCourtRowProcessor();
         final StoredProcedure sp = createStoredProcedure(GET_DEFENDANTS_BY_COURT);
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
        validateParameterNotNull("Defendant on Case Id", defendantOnCaseId);
        final StoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_ON_CASE);
        sf.registerInTypes(UPDATE_DEFENDANT_ON_CASE_TYPES);
        sf.executeFunction(new Object[] {defendantOnCaseId, asn, ptiurn });
    }

    /**
     * Updates single defendant on offence information with seq no.
     * @param defendantOnOffenceId
     * @param seqNo
     */
    public void updateDefendantOnOffence(Integer defendantOnOffenceId, String seqNo) {
        validateParameterNotNull("Defendant on Offence Id", defendantOnOffenceId);
        final StoredFunction sf = createStoredFunction(UPDATE_DEFENDANT_ON_OFFENCE);
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
        validateParameterNotNull("Court Id", courtId);
        final StoredFunction sf = createStoredFunction(UPDATE_DATA_MIGRATION_TOTALS);
        sf.registerInTypes(UPDATE_DATA_MIGRATION_TOTALS_TYPES);
        sf.executeFunction(new Object[] {courtId, totalDefendantsOnCase, totalDefendantsOnOffence });
    }

    /**
     * Updates the status of the data migration process to a given value for a given court.
     *
     */
    public void updateStatus(int courtId, String status) {
        final StoredFunction sf = createStoredFunction(UPDATE_STATUS);

        sf.registerInTypes(UPDATE_STATUS_TYPES);
        sf.executeFunction(new Object[] {courtId, status});
    }

    /*
    *  Returns defendant on offence data by defendant on case ID.
    *  The records are ordered using the rules of precedence specified in the Data Migration design
    */
    public OffenceByDefendantVO[] getOffencesByDefendant(Integer docId) {
        final OffencesByDefendantRowProcessor rp = new OffencesByDefendantRowProcessor();
        final StoredProcedure sp = createStoredProcedure(GET_OFFENCES_BY_DEFENDANT);
        sp.registerInTypes(GET_OFFENCES_BY_DEFENDANT_TYPES);
        sp.setRowProcessor(rp);
        sp.execute(new Object[] {docId});
        return rp.getOffencesByDefendant();
    }
    
    protected DataSource getDataSource() {
        System.out.println("RecreateConnectionDataMigrationDatabase:getDataSource");
        
        return new StandAloneDataSource();
    }
}
