package uk.gov.courtservice.framework.jdbc.core;


/**
 * An abstract class representing Darts databases.
 * 
 * @author Luis Valenzuela
 * @version 1.0
 */
public abstract class AbstractDartsDatabase extends AbstractDatabaseCall {
	public AbstractDartsDatabase() {
		super("darts");	
	}	
}
