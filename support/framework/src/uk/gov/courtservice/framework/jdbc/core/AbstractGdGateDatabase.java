package uk.gov.courtservice.framework.jdbc.core;


/**
 * An abstract class representing Xhibit databases.
 * 
 * @author Will Fardell
 * @version $Id: AbstractGdGateDatabase.java,v 1.1 2006/08/22 10:02:11 bzjrnl Exp $
 */
public abstract class AbstractGdGateDatabase extends AbstractDatabaseCall {
	public AbstractGdGateDatabase() {
		super("gdgate");	
	}	
}
