package uk.gov.courtservice.framework.security.providers.authorization;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Data access object for role mappings
 * 
 * @author Meeraj
 */
public class RoleMappingsDAO {
    /**
     * File contains the descriptions for roles that can be maintained by users
     */
    private static final String ROLE_PROPERTIES_FILE = "role.properties";

    /** Role cache */
    private static Properties roleProperties = new Properties();

    static {
        try {
            ClassLoader cl = RoleMappingsDAO.class.getClassLoader();
            roleProperties.load(cl.getResourceAsStream(ROLE_PROPERTIES_FILE));
        } catch (IOException ex) {
            throw new NestedException(ex);
        }
    }

    /**
     * Returns the current list of roles
     * 
     * @return
     * 
     * @throws SQLException
     */
    public Groups getGroups() throws SQLException {
        // Database resources
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Get the connection from the datasource
            con = ConnectionUtil.getConnection();

            // Execute the SQL to get all the role mappings
            // ps = con.prepareStatement(SQLUtil.SELECT_GROUP_ROLE_ALL);
            ps = con.prepareStatement(SQLUtil.SELECT_GROUP_ROLE_ENABLED);

            // Set request timeout
            ps.setQueryTimeout(0);

            rs = ps.executeQuery();

            // Create a list of roles
            Groups groups = new Groups();

            while (rs.next()) {
                // Get the role name and group name
                String roleName = rs.getString(1).trim();
                String groupName = rs.getString(2).trim();

                // System.out.println(roleName + "::" + groupName);
                Group group = groups.getGroup(groupName);
                group.addRole(roleName);
                // System.err.println("Loaded mapping:" + groupName + "->" +
                // roleName);
            }

            return groups;
        } finally {
            ConnectionUtil.closeResultSet(rs);
            ConnectionUtil.closeStatement(ps);
        }
    }

    /**
     * Adds the mapping to the database
     * 
     * @param group
     * @param role
     * 
     * @throws SQLException
     */
    public void addMapping(String group, String role) throws SQLException {
        // Database resources
        Connection con = ConnectionUtil.getConnection();

        // If the role doesn't exist create
        if (!doesExist(con, SQLUtil.SELECT_ROLE_PK, new String[] { role })) {
            String systemRole = (roleProperties.getProperty(role) == null ? "Y" : "N");
            create(con, SQLUtil.INSERT_ROLE, new String[] { role, systemRole });
        }

        // If the group doesn't exist create
        if (!doesExist(con, SQLUtil.SELECT_GROUP_PK, new String[] { group })) {
            create(con, SQLUtil.INSERT_GROUP, new String[] { group, group });
        }

        // If the mapping doesn't create
        if (!doesExist(con, SQLUtil.SELECT_GROUP_ROLE_PK, new String[] { group, role })) {
            // create(con, SQLUtil.INSERT_GROUP_ROLE, new String[] { group,
            // role });
            String[] arguments = new String[] { group, role };
            create(con, SQLUtil.INSERT_GROUP_ROLE, arguments);
        }

        // Commit the transaction
        con.commit();
    }

    /**
     * Utility method for checking the primary key already exists
     * 
     * @param con
     * @param sql
     * @param args
     *            statement arguments
     * 
     * @return A flag indicating whether the PK exists
     * 
     * @throws SQLException
     */
    private boolean doesExist(Connection con, String sql, String[] args) throws SQLException {
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = con.prepareStatement(sql);

            // Set request timeout
            ps.setQueryTimeout(0);

            for (int i = 0; i < args.length; i++) {
                ps.setString(i + 1, args[i]);
            }

            rs = ps.executeQuery();

            return rs.next();
        } finally {
            ConnectionUtil.closeResultSet(rs);
            ConnectionUtil.closeStatement(ps);
        }
    }

    /**
     * Utility method for issuing SQL statements
     * 
     * @param con
     * @param sql
     * @param args
     *            statement arguments
     * 
     * @throws SQLException
     */
    private void create(Connection con, String sql, String[] args) throws SQLException {
        PreparedStatement ps = null;

        try {
            ps = con.prepareStatement(sql);

            // Set request timeout
            ps.setQueryTimeout(0);

            for (int i = 0; i < args.length; i++) {
                ps.setString(i + 1, args[i]);
            }

            ps.executeUpdate();
        } finally {
            ConnectionUtil.closeStatement(ps);
        }
    }
}
