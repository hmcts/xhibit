package uk.gov.courtservice.framework.jdbc.core;

import java.util.Arrays;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.jdbc.exception.DataAccessException;

public class LoggedStoredProcedure extends StoredProcedure {

	// Logger
    private static final Logger log = Logger.getLogger(LoggedStoredProcedure.class);
	private String sql;
	
	public LoggedStoredProcedure(DataSource ds, String sql) {
		super(ds, sql);
		
		//Keep a reference to the SQL so we can log it later
		this.sql = sql;
	}
	
	
	/**
     * Executes the SQL operation and logs any inputs and response
     * 
     * @param Arguments
     *            to the operation
     * @throws DataAccessException
     */
	@Override
    public int execute(Object args[]) throws DataAccessException {
        
		long startTime = System.currentTimeMillis();
		
		log.debug(String.format("Calling %s with parameters %s", sql, Arrays.toString(args)));
		int numberOfRowsReturned = super.execute(args);
		log.debug(String.format("%s Returned with Number of Rows: %d in %d milliseconds", sql, numberOfRowsReturned, System.currentTimeMillis() - startTime ));		
		
		return numberOfRowsReturned;
    }
}
