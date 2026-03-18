package uk.gov.courtservice.framework.jdbc.core;


/**
 * An abstract class representing Xhibit databases.
 * 
 * @author Will Fardell
 * @version $Id: AbstractXhibitDatabase.java,v 1.1 2006/07/07 08:47:07 bzjrnl Exp $
 */
public abstract class AbstractXhibitDatabase extends AbstractDatabaseCall {
	public AbstractXhibitDatabase() {
		super("xhibit");	
	}	
}
