package datamigration1745.database;

import javax.sql.DataSource;

import oracle.jdbc.OracleTypes;
import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;
import uk.gov.courtservice.framework.jdbc.core.RowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredProcedure;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.jdbc.core.Parameter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <p>
 * Title: DataMigrationStoredProcedure
 * </p>
 * <p>
 * Description: Select operation
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 *
 * @author Meeraj Kunnumpurath
 * @version $Id: DataMigrationStoredProcedure.java,v 1.1 2007/09/06 18:47:28 qz4rwx Exp $
 */
public class DataMigrationStoredProcedure extends StoredProcedure {
    
    DataMigrationJDBCHelper dmJDBCHelper = null;
    
    /**
     * Initializes the object
     *
     * @param Datasource
     *            to use
     * @param SQL
     *            to use
     */
    public DataMigrationStoredProcedure(DataSource ds, String sql) {
        super(ds, sql);
        dmJDBCHelper = new DataMigrationJDBCHelper(ds, sql);
    }

    /**
     * Executes the SQL operation
     *
     * @param Arguments
     *            to the operation
     * @throws DataAccessException
     */
    public int execute(Object args[]) throws DataAccessException {
        Parameter[] params = getParameters(args, OracleTypes.CURSOR);
        return dmJDBCHelper.executeStoredProcedure(params);
    }

    public void setRowProcessor(RowProcessor rowProcessor) {
        dmJDBCHelper.setRowProcessor(rowProcessor);
    }

    public void setMaxRowCount(int rowCount) {
        dmJDBCHelper.setMaxRowCount(rowCount);
    }
    
    private class DataMigrationJDBCHelper extends JdbcHelper  {
        protected DataMigrationJDBCHelper(){this(null,null);}
        public DataMigrationJDBCHelper(DataSource newDs, String newSql) {
            super(newDs,newSql);
        }
  
        public void closeResources(ResultSet rset, Statement stmt, Connection con) {
            closeResultSet(rset);
            closeStatement(stmt);
            
            System.out.println("Resources released but Connection not closed as this is a single app executing consecutive discrete database operations");
        }
    }
}
