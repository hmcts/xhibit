package uk.gov.courtservice.xhibit.business.database.results.processor;

// JDK
import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.common.results.vos.DisposalMenuReferenceValue;

/**
 * <p>
 * Title: RefDisposalMenuProcessor
 * </p>
 * <p>
 * Description: Process the disposal menu reference data
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
public class RefDisposalMenusProcessor extends AbstractRowProcessor {

    /**
     * Map of of menu items keyed by menuItemId
     */
    private final Map menuMap = new HashMap();

    /**
     * Process the row storing the DispsoalMenuReferenceValue in the map
     * 
     * @param row
     *            the row to process
     */
    public void processRow(Row row) {
        Integer menuItemId = row.getInteger("menu_item_id");
        menuMap.put(menuItemId, new DisposalMenuReferenceValue(menuItemId, row.getString("title"), row
                .getString("abbrev"), row.getString("disposal_code"), row.getInteger("parent"), row
                .getInteger("seq_no")));
    }

    /**
     * Access the Menu Map
     * 
     * @return the menu map
     */
    public Map getMenuMap() {
        return menuMap;
    }
}