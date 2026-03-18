package datamigration1745.database;

import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.AbstractXhibitDatabase;

import datamigration1745.vos.CourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.OffenceByDefendantVO;

/**
 * A utility class used to extract all of the database actions performed by
 * the Data Migration process for RFC1745.
 *
 */
public abstract class DataMigrationDatabase extends AbstractXhibitDatabase {

    protected static final String GET_COURTS = "{ call xhb_1745_data_migration_pkg.get_courts(?) }";
    protected static final String GET_COURT = "{ call xhb_1745_data_migration_pkg.get_court(?,?) }";
    protected static final String GET_HO_POLICE_FORCE = "{ call xhb_1745_data_migration_pkg.get_police_force_data(?,?) }";
    protected static final String GET_DEFENDANTS_BY_COURT = "{ call xhb_1745_data_migration_pkg.get_defendants_by_court(?,?) }";    
    protected static final String GET_OFFENCES_BY_DEFENDANT = "{ call xhb_1745_data_migration_pkg.get_offences_by_defendant(?,?) }";
    
    protected static final String UPDATE_DEFENDANT_ON_CASE = "{ call xhb_1745_data_migration_pkg.update_defendant_on_case(?,?,?) }";
    protected static final String UPDATE_DEFENDANT_ON_OFFENCE = "{ call xhb_1745_data_migration_pkg.update_defendant_on_offence(?,?) }";
    protected static final String UPDATE_DATA_MIGRATION_TOTALS = "{ call xhb_1745_data_migration_pkg.update_data_migration_totals(?,?,?) }";
    protected static final String UPDATE_STATUS = "{ call xhb_1745_data_migration_pkg.update_data_migration_status(?,?) }";
    
    // parameters to be passed into the stored proc
    protected static final int[] GET_COURTS_TYPES = new int[] {};
    protected static final int[] GET_COURT_TYPES = new int[] {Types.INTEGER};
    protected static final int[] GET_HO_POLICE_FORCE_TYPES = new int[] {Types.INTEGER};
    protected static final int[] GET_OFFENCES_BY_DEFENDANT_TYPES = new int[] {Types.INTEGER};
    protected static final int[] GET_DEFENDANTS_BY_COURT_TYPES = new int[] {Types.INTEGER};
    
    protected static final int[] UPDATE_DEFENDANT_ON_CASE_TYPES = new int[] {Types.INTEGER,Types.VARCHAR,Types.VARCHAR};
    protected static final int[] UPDATE_DEFENDANT_ON_OFFENCE_TYPES = new int[] {Types.INTEGER,Types.INTEGER};
    protected static final int[] UPDATE_DATA_MIGRATION_TOTALS_TYPES = new int[] {Types.INTEGER,Types.INTEGER,Types.INTEGER};
    protected static final int[] UPDATE_STATUS_TYPES = new int[] {Types.INTEGER,Types.VARCHAR};

    /**
     * Get the list of courts.
     *
     * @return A collection containing <code>CourtVO</code> objects
     *         corresponding to all the
     */
    public abstract CourtVO[] getCourts();

    /**
     * Get a specific court.
     *
     * @return A <code>CourtVO</code> object corresponding to a specific court
     */
    public abstract CourtVO getCourt(Integer courtId);
    
    /**
     * Get the list of ref data entries of type HO Police Force for a court.
     *
     * @return A collection containing <code>HOPoliceForceVO</code> objects
     *         corresponding to all the HO Police Force ref data entries
     *         for the court_id
     */
    public abstract HOPoliceForceVO[] getPoliceForceData (Integer courtId);

    /*
     *  Returns defendants for a particular court 
     */
     public abstract DefendantByCourtVO[] getDefendantsByCourt(Integer courtId);
     
    /**
     * Updates single defendant on case information with asn and ptiurn.         
     * @param defendantOnCaseId
     * @param asn
     * @param ptiurn
     */
    public abstract void updateDefendantOnCase(Integer defendantOnCaseId, String asn, String ptiurn);
    
    /**
     * Updates single defendant on offence information with seq no.
     * @param defendantOnOffenceId
     * @param seqNo
     */
    public abstract void updateDefendantOnOffence(Integer defendantOnOffenceId, String seqNo);
    
    /**
     * Updates the data migration totals for a defendants on case and offence for a given court.
     * @param courtId
     * @param totalDefendantsOnCase
     * @param totalDefendantsOnOffence
     */
    public abstract void updateDataMigrationTotals(Integer courtId, Integer totalDefendantsOnCase, Integer totalDefendantsOnOffence);
    
    /**
     * Updates the status of the data migration process to a given value for a given court.
     *
     */
    public abstract void updateStatus(int courtId, String status);
    
    /*
    *  Returns defendant on offence data by defendant on case ID.  
    *  The records are ordered using the rules of precedence specified in the Data Migration design
    */
    public abstract OffenceByDefendantVO[] getOffencesByDefendant(Integer docId); 
    
    
}
