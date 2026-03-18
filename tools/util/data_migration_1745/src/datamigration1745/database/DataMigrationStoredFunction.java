package datamigration1745.database;

import javax.sql.DataSource;

import uk.gov.courtservice.framework.jdbc.core.RowProcessor;
import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.JdbcHelper;
import uk.gov.courtservice.framework.jdbc.core.Parameter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * <p>
 * Title: DataMigrationStoredFunction
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
 * @version $Id: DataMigrationStoredFunction.java,v 1.1 2007/09/06 18:47:28 qz4rwx Exp $
 */
public class DataMigrationStoredFunction extends StoredFunction {

    DataMigrationJDBCHelper dmJDBCHelper = null;

    public DataMigrationStoredFunction(DataSource ds, String sql) {
        super(ds, sql);
        dmJDBCHelper = new DataMigrationJDBCHelper(ds, sql);
    }

    public int execute(Object[] args) {
        throw new UnsupportedOperationException("execute is not supported, "
                + "please use one of the executeFunction methods");
    }

    public Object executeFunction(Object[] args) {
        Parameter[] params = getParameters(args);
        return dmJDBCHelper.executeStoredFunction(params);
    }

    public Object executeFunction(Object[] args, int outputType) {
        Parameter[] params = getParameters(args, outputType);
        return dmJDBCHelper.executeStoredFunction(params);
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
