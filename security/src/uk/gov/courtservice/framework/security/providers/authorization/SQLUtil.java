package uk.gov.courtservice.framework.security.providers.authorization;

import java.io.IOException;
import java.util.Properties;

/**
 * This is a utility class for reading the SQL statements
 * 
 * @author Meeraj
 * @version $Id: SQLUtil.java,v 1.3 2006/06/05 12:30:05 bzjrnl Exp $
 */
public class SQLUtil {
    /** Name of the SQL file to load from */
    private static final String SQL_FILE = "sql.properties";

    /** SQL to select all enabled group-role associations */
    public static final String SELECT_GROUP_ROLE_ENABLED;

    /** SQL to select a group by primary key */
    public static final String SELECT_GROUP_PK;

    /** SQL to select a role by primary key */
    public static final String SELECT_ROLE_PK;

    /** SQL to select a role by primary key */
    public static final String SELECT_GROUP_ROLE_PK;

    /** SQL to insert a group */
    public static final String INSERT_GROUP;

    /** SQL to insert a role */
    public static final String INSERT_ROLE;

    /** SQL to insert a group-role associations */
    public static final String INSERT_GROUP_ROLE;

    /**
     * Static initializer loads the SQL
     */
    static {
        try {
            final Properties sql = new Properties();
            sql.load(SQLUtil.class.getClassLoader().getResourceAsStream(SQL_FILE));

            // SQL to select all enabled group-role associations
            SELECT_GROUP_ROLE_ENABLED = sql.getProperty("SELECT_GROUP_ROLE_ENABLED");

            // SQL to select a group by primary key
            SELECT_GROUP_PK = sql.getProperty("SELECT_GROUP_PK");

            // SQL to select a role by primary key
            SELECT_ROLE_PK = sql.getProperty("SELECT_ROLE_PK");

            // SQL to select a role by primary key
            SELECT_GROUP_ROLE_PK = sql.getProperty("SELECT_GROUP_ROLE_PK");

            // SQL to insert a group
            INSERT_GROUP = sql.getProperty("INSERT_GROUP");

            // SQL to insert a role
            INSERT_ROLE = sql.getProperty("INSERT_ROLE");

            // SQL to insert a group-role associations
            INSERT_GROUP_ROLE = sql.getProperty("INSERT_GROUP_ROLE");
        } catch (IOException ex) {
            ex.printStackTrace();
            throw new NestedException(ex);
        }
    }
}
