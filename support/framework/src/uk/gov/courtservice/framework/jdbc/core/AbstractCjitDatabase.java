package uk.gov.courtservice.framework.jdbc.core;


/**
 * An abstract class representing Exiss databases.
 * 
 * @author Will Fardell
 * @version $Id: AbstractCjitDatabase.java,v 1.2 2006/08/22 10:02:11 bzjrnl Exp $
 */
public abstract class AbstractCjitDatabase extends AbstractDatabaseCall {
	public AbstractCjitDatabase() {
		super("cjit");	
	}	
}
