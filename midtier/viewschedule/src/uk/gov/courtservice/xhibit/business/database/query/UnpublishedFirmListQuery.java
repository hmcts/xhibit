package uk.gov.courtservice.xhibit.business.database.query;

import java.sql.Clob;
import java.sql.Types;

import uk.gov.courtservice.framework.jdbc.core.StoredFunction;
import uk.gov.courtservice.framework.jdbc.core.columneditor.StringStrategy;
import uk.gov.courtservice.framework.services.CSServices;

public class UnpublishedFirmListQuery extends StoredFunction {
	private static final String SQL_FUNCTION = "{? = call XHB_GET_XML_REPORTS.GET_FIRM_LIST_DATA(?, ?) }";
	
	/** Buffer size for reading from a Clob */
    private static final int PARAMETER_TYPES[] = { Types.INTEGER, Types.VARCHAR };
    
    /**
     * Initialize the datasource and register the in parameter types
     */
    public UnpublishedFirmListQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL_FUNCTION);
        registerInTypes(PARAMETER_TYPES);
    }
    
    /**
     * Gets the Firm List schedule for the specified list id
     * 
     * @param listId  Integer
     * @return String - Firm list XML for the specified List ID
     */
    public String getFirmList(final Integer listId) {
    
        if (listId == null)
            throw new IllegalArgumentException("listId");

        String firmListXml = StringStrategy.getValue((Clob) executeFunction(new Object[] { listId, "" }, Types.CLOB));
        // Execute the query
        return firmListXml ;
        
    }   
}
