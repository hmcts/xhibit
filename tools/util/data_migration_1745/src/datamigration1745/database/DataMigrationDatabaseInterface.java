package datamigration1745.database;

import datamigration1745.vos.CourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.OffenceByDefendantVO;

/**
 * Interface for DataMigrationDatabase
 *
 */
public interface DataMigrationDatabaseInterface {

    /**
     * Get the list of courts.
     *
     * @return A collection containing <code>CourtVO</code> objects
     *         corresponding to all the
     */
    public CourtVO[] getCourts();

    /**
     * Get a specific court.
     *
     * @return A <code>CourtVO</code> object corresponding to a specific court
     */
    public CourtVO getCourt(Integer courtId);

    /**
     * Get the list of ref data entries of type HO Police Force for a court.
     *
     * @return A collection containing <code>HOPoliceForceVO</code> objects
     *         corresponding to all the HO Police Force ref data entries
     *         for the court_id
     */
    public HOPoliceForceVO[] getPoliceForceData (Integer courtId);

    /*
     *  Returns defendants for a particular court
     */
     public DefendantByCourtVO[] getDefendantsByCourt(Integer courtId);

    /**
     * Updates single defendant on case information with asn and ptiurn.
     * @param defendantOnCaseId
     * @param asn
     * @param ptiurn
     */
    public void updateDefendantOnCase(Integer defendantOnCaseId, String asn, String ptiurn);

    /**
     * Updates single defendant on offence information with seq no.
     * @param defendantOnOffenceId
     * @param seqNo
     */
    public void updateDefendantOnOffence(Integer defendantOnOffenceId, String seqNo);

    /**
     * Updates the data migration totals for a defendants on case and offence for a given court.
     * @param courtId
     * @param totalDefendantsOnCase
     * @param totalDefendantsOnOffence
     */
    public void updateDataMigrationTotals(Integer courtId, Integer totalDefendantsOnCase, Integer totalDefendantsOnOffence);

    /**
     * Updates the status of the data migration process to a given value for a given court.
     *
     */
    public void updateStatus(int courtId, String status);

    /*
    *  Returns defendant on offence data by defendant on case ID.
    *  The records are ordered using the rules of precedence specified in the Data Migration design
    */
    public OffenceByDefendantVO[] getOffencesByDefendant(Integer docId);

}
