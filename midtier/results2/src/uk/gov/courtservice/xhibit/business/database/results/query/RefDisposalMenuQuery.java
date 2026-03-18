package uk.gov.courtservice.xhibit.business.database.results.query;

// JDK
import java.sql.Types;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.QueryOperation;
import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.database.results.processor.RefDisposalMenusProcessor;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;

/**
 * <p>
 * Title: RefDisposalMenuQuery
 * </p>
 * <p>
 * Description: Query the database for disposal menu reference data
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public class RefDisposalMenuQuery extends QueryOperation {

    // The file that contains the SQL for this query, uses case id.
    private static final String SQL_FILE = "config/database/query/results/RefDisposalMenu.sql";

    // SQL used by this query
    private static final String SQL = readSql(SQL_FILE);

    /**
     * Constructor initializes the datasource and SQL
     */
    public RefDisposalMenuQuery() {
        super(CSServices.getServiceLocator().getDataSource(), SQL);
        registerInTypes(new int[] { Types.NUMERIC, Types.VARCHAR });
    }

    /**
     * Get the menu map (keyed against menu_item_id) for the specified menu
     * group
     * 
     * @return the root of the requested menu group root
     */
    public Map getMenuMap(Integer courtId, String menuGroup) {
        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }
        if (!DisposalMenuReferenceValue.isMenuGroup(menuGroup)) {
            throw new IllegalArgumentException("menuGroup: " + menuGroup);
        }
        RefDisposalMenusProcessor processor = new RefDisposalMenusProcessor();
        setRowProcessor(processor);
        execute(new Object[] { courtId, menuGroup });
        return processor.getMenuMap();
    }

}