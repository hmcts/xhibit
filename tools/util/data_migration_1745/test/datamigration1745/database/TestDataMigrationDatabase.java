package datamigration1745.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.testutils.StandAloneDataSource;
import datamigration1745.vos.CourtVO;
import datamigration1745.vos.HOPoliceForceVO;
import datamigration1745.vos.DefendantByCourtVO;
import datamigration1745.vos.OffenceByDefendantVO;


public class TestDataMigrationDatabase {

    DataMigrationDatabaseInterface database = null;

    private static Connection conn;
    private Statement statement;
    private ResultSet rs;

    static {
        // Initialise Log4j For Testing
        //BasicConfigurator.configure();

        // Initialise Database Properties
        System.setProperty("database.driver", "oracle.jdbc.xa.client.OracleXADataSource");
        System.setProperty("database.url", "jdbc:oracle:thin:@130.177.4.44:1521:CSDBDEV3");
        System.setProperty("database.user", "xhibit");
        System.setProperty("database.password", "xhibit");
    }

    public static void main(String[] args) {
        try {
            TestDataMigrationDatabase db = new TestDataMigrationDatabase();

            Integer courtId = 3;
            Integer docId = 1;

            db.getCourts();
            db.getCourt(courtId);
            db.getPoliceForceData(courtId);

            if(args.length!=0)
            {
                courtId = Integer.valueOf(args[0]);
            }
            System.out.println("***** courtId for getDefendantsByCourt test is: " + courtId);
            db.getDefendantsByCourt(courtId);

            db.updateDefendantOnCase();
            db.updateDefendantOnOffence();
            db.updateDataMigrationTotals();
            db.updateStatus(1, "S");


            if(args.length!=0)
            {
                if(args.length==1)
                {
                    docId = Integer.valueOf(args[0]);
                }
                else
                {
                    docId = Integer.valueOf(args[1]);
                }
            }
            System.out.println("***** docId for getOffencesByDefendant test is: " + docId);
            db.getOffencesByDefendant(docId);

            getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TestDataMigrationDatabase() {
        // Initialise database, override to create standalone datasource
        database = DataMigrationDatabaseFactory.getInstance().getDataMigrationDatabase();

    }

    private void getCourts()
    {
        System.out.println("TestDataMigrationDatabase getCourts() - Begin");

        CourtVO[] courts = database.getCourts();
        for (CourtVO court : courts) {
            System.out.println(court.toString());
        }

        System.out.println("TestDataMigrationDatabase getCourts() - End");
    }

    private void getCourt(Integer courtId)
    {
        System.out.println("TestDataMigrationDatabase getCourt() - Begin");

        CourtVO court = database.getCourt(courtId);
        System.out.println(court.toString());

        System.out.println("TestDataMigrationDatabase getCourt() - End");
    }

    private void getPoliceForceData(Integer courtId)
    {
        System.out.println("TestDataMigrationDatabase getPoliceForceData() - Begin, courtId: " + courtId);

        HOPoliceForceVO[] hoPoliceForceList = database.getPoliceForceData(courtId);

        for (HOPoliceForceVO hoPoliceForce : hoPoliceForceList) {
            System.out.println(hoPoliceForce.toString());
        }

        System.out.println("TestDataMigrationDatabase getPoliceForceData() - End, courtId: " + courtId);
    }

    private void getDefendantsByCourt(Integer courtId)
    {
        System.out.println("TestDataMigrationDatabase getDefendantsByCourt() - Begin, courtId: " + courtId);

        DefendantByCourtVO[] defendantsByCourtList = database.getDefendantsByCourt(courtId);

        for (DefendantByCourtVO defendantByCourt : defendantsByCourtList) {
            System.out.println(defendantByCourt.toString());
        }

        System.out.println("TestDataMigrationDatabase getDefendantsByCourt() - End, courtId: " + courtId);
    }

    private void updateDefendantOnCase()
    {
        System.out.println("TestDataMigrationDatabase updateDefendantOnCase() - Begin");

        try {
            database.updateDefendantOnCase(1,"222","333");
            //Select from DB to determine if works
            Statement statement = getConnection().createStatement();
            ResultSet rs = statement.executeQuery("Select ASN, PTIURN from xhb_defendant_on_case " +
                    "where defendant_on_case_id = 1");
            if (rs.next())
            {
               System.out.println("Defendant on Case table updated with following information:");
               System.out.println("ASN - "+ rs.getString("asn"));
               System.out.println("PTIURN - "+ rs.getString("ptiurn"));
            }else{
                System.out.println("Defendant on Case table has NOT been updated!");
            }
            getConnection().close();
        } catch (SQLException e) {
            System.err.print("SQLException: " + e);
        }

        System.out.println("TestDataMigrationDatabase updateDefendantOnCase() - End");
    }

    private void updateDefendantOnOffence()
    {
        System.out.println("TestDataMigrationDatabase updateDefendantOnOffence() - Begin");

        try {
            database.updateDefendantOnOffence(1,"111");
            //Select from DB to determine if works
            statement = getConnection().createStatement();
            rs = statement.executeQuery("Select seq_no from xhb_defendant_on_offence " +
                    "where defendant_on_offence_id = 1");
            if (rs.next())
            {
               System.out.println("Defendant on Offence table updated with following information:");
               System.out.println("ASN - "+ rs.getString("seq_no"));
            }else{
                System.out.println("Defendant on Offence table has NOT been updated!");
            }

        } catch (SQLException e) {
            System.err.print("SQLException: " + e);
        }

        System.out.println("TestDataMigrationDatabase updateDefendantOnOffence() - End");
    }

    private void updateDataMigrationTotals()
    {
        System.out.println("TestDataMigrationDatabase updateDataMigrationTotals() - Begin");

        try {
            database.updateDataMigrationTotals(1,111,222);
            //Select from DB to determine if works
            statement = getConnection().createStatement();
            rs = statement.executeQuery("Select total_defendants_on_case, total_defendants_on_offence from xhb_1745_data_migration_totals " +
                    "where court_id = 1");
            if (rs.next())
            {
               System.out.println("The xhb_1745_data_migration_totals table updated with following information:");
               System.out.println("total_defendants_on_case - "+ rs.getString("total_defendants_on_case"));
               System.out.println("total_defendants_on_offence - "+ rs.getString("total_defendants_on_offence"));
            }else{
                System.out.println("The xhb_1745_data_migration_totals table has NOT been updated!");
            }

        } catch (SQLException e) {
            System.err.print("SQLException: " + e);
        }

        System.out.println("TestDataMigrationDatabase updateDataMigrationTotals() - End");
    }
    private void updateStatus(int courtId, String status)
    {
        System.out.println("TestDataMigrationDatabase updateStatus() - Begin");

        try {
            database.updateStatus(courtId, status);

            //Select from DB to determine if works
            statement = getConnection().createStatement();
            rs = statement.executeQuery("Select status from xhb_1745_data_migration_totals " +
                    "where court_id = " + courtId);
            if (rs.next())
            {
               System.out.println("xhb_1745_data_migration_totals table updated with following information:");
               System.out.println("status - "+ rs.getString("status"));
            }else{
                System.out.println("xhb_1745_data_migration_totals table has NOT been updated!");
            }

        } catch (SQLException e) {
            System.err.print("SQLException: " + e);
        }

        System.out.println("TestDataMigrationDatabase updateStatus() - End");

    }
    private static Connection getConnection(){
        try {
            if (conn == null || conn.isClosed()){
                conn = DriverManager.getConnection(System.getProperty("database.url"),
                System.getProperty("database.user"),
                System.getProperty("database.password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public OffenceByDefendantVO[] getOffencesByDefendant(Integer docId) {

        System.out.println("TestDataMigrationDatabase getOffencesByDefendant() - Begin, docId: " + docId);

        OffenceByDefendantVO[] offencesByDefList = database.getOffencesByDefendant(docId);

        for (OffenceByDefendantVO offencesByDef : offencesByDefList) {
            System.out.println(offencesByDef.toString());
        }

        System.out.println("TestDataMigrationDatabase getOffencesByDefendant() - End, docId: " + docId);

        return offencesByDefList;
    }
}