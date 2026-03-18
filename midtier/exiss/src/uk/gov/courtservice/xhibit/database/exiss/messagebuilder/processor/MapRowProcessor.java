package uk.gov.courtservice.xhibit.database.exiss.messagebuilder.processor;

import java.util.HashMap;
import java.util.Map;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;

/**
 * <p>
 * Title: PropertyRowProcessor
 * </p>
 * <p>
 * Description: Class to process the JMS Property rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully, Jeremy Shields
 * @version 1.0
 */
public class MapRowProcessor extends AbstractRowProcessor {

    private final String keyColumn;

    private final String valueColumn;

    // Instance cache
    private final Map<String, String> map = new HashMap<String, String>();

    /**
     * Default constructor
     */
    public MapRowProcessor(String keyColumn, String valueColumn) {
        if (keyColumn == null) {
            throw new IllegalArgumentException("keyColumn: null");
        }
        if (valueColumn == null) {
            throw new IllegalArgumentException("valueColumn: null");
        }
        this.keyColumn = keyColumn;
        this.valueColumn = valueColumn;
    }

    /**
     * Get the map containing the data
     * 
     * @return
     */
    public Map<String, String> getMap() {
        return map;
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        map.put(row.getString(keyColumn), row.getString(valueColumn));
    }

}
