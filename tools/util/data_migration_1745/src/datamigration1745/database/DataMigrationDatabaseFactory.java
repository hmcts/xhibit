package datamigration1745.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import uk.gov.courtservice.framework.services.CSServices;

/**
 * <p>
 * Title: DataMigrationDatabaseFactory
 * </p>
 * <p>
 * Description: DataMigrationDatabaseFactory generates implementations for the
 * DataMigrationDatabase .
 * </p>
 * <p>
 * Company: EDS
 * </p>
 *
 * @author GJS
 * @version 1.0
 */
public class DataMigrationDatabaseFactory {


    private static final String FIXED_CONNECTION_DATABASE = "FIXED";

    private static final String RECREATE_CONNECTION_DATABASE = "RECREATE";

    private static final String DB_TYPE = "datamigration.databasetype";

	public static final String DEFAULT_DB_TYPE = FIXED_CONNECTION_DATABASE;
    
    public static final Integer DEFAULT_MAX_POOL_SIZE = 1;

    public static String DEFAULT_REPORT_FORMAT = "COURT";
    
    private static DataMigrationDatabaseFactory dataMigrationDatabaseFactory = new DataMigrationDatabaseFactory();

    private static Map<String,String> DB_MIGRATION_CLASSES;


    static {

        DB_MIGRATION_CLASSES = new HashMap<String,String>();
        DB_MIGRATION_CLASSES.put(FIXED_CONNECTION_DATABASE,
                "datamigration1745.database.FixedConnectionDataMigrationDatabase");
        DB_MIGRATION_CLASSES.put(RECREATE_CONNECTION_DATABASE,
                "datamigration1745.database.RecreateConnectionDataMigrationDatabase");
    }

    /**
     * Zero parm constructor for Factory
     *
     * @roseuid 3DDBB1460363
     */
    private DataMigrationDatabaseFactory() {
    }

    /**
     * Returns the singleton factory object.
     *
     * @return DataMigrationDatabaseFactory
     */
    public static DataMigrationDatabaseFactory getInstance() {
        if (dataMigrationDatabaseFactory == null) {
            createDataMigrationDatabaseFactory();
        }
        return dataMigrationDatabaseFactory;
    }

    /**
     * Creates the singleton factory object. This method is synchronized as it
     * can be accessed from multiple threads but we only ever want to create one
     * factory 
     *
     * @return void
     */
    private synchronized static void createDataMigrationDatabaseFactory() {
        dataMigrationDatabaseFactory = new DataMigrationDatabaseFactory();
    }

    /**
     * Returns a DataMigrationDatabaseInterface object
     *
     *
     * @return DataMigrationDatabaseInterface
     */
    public DataMigrationDatabaseInterface getDataMigrationDatabase() {
        return createDataMigrationDatabase();
    }

    /**
     * Creates an DataMigrationDatabaseInterface.
     * @return DataMigrationDatabaseInterface
     */
    protected DataMigrationDatabaseInterface createDataMigrationDatabase() {
        try {
            String className = DB_MIGRATION_CLASSES.get(getDatabaseMigrationType());
            if(className==null)
            {
                System.out.println("createDataMigrationDatabase className was null - returning FixedConnectionDataMigrationDatabase");
                return new FixedConnectionDataMigrationDatabase();
            }
            System.out.println("createDataMigrationDatabase className:" + className);
            return (DataMigrationDatabaseInterface) Class.forName(className).newInstance();
        } catch (Throwable ex) {
            System.out.println("createDataMigrationDatabase failed returning FixedConnectionDataMigrationDatabase. Error was: " + ex);
            return new FixedConnectionDataMigrationDatabase();
        }
    }

    /**
     * Looks up the DatabaseMigrationType from properties
     *
     * @return String
     */
    private String getDatabaseMigrationType() {
        String dbMigrationType = System.getProperty(DB_TYPE,DEFAULT_DB_TYPE).toUpperCase();
        System.out.println("dbMigrationType: " + dbMigrationType);

        return dbMigrationType;
    }
}
