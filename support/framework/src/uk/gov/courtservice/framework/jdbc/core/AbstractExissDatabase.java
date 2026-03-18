package uk.gov.courtservice.framework.jdbc.core;


/**
 * An abstract class representing Exiss databases.
 * 
 * @author Will Fardell
 * @version $Id: AbstractExissDatabase.java,v 1.2 2006/07/14 15:33:33 bzjrnl Exp $
 */
public abstract class AbstractExissDatabase extends AbstractDatabaseCall {
	public AbstractExissDatabase() {
		super("exiss");	
	}	
}
