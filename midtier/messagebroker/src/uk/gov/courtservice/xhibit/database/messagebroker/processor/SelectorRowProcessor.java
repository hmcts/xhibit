package uk.gov.courtservice.xhibit.database.messagebroker.processor;

import java.util.ArrayList;
import java.util.List;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.messagebroker.SelectorVO;

/**
 * <p>
 * Title: SelectorRowProcessor
 * </p>
 * <p>
 * Description: Class to process the Selector rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Steve Tully
 * @version 1.0
 */
public class SelectorRowProcessor extends AbstractRowProcessor {

    // Instance cache
    private final List<SelectorVO> selectorList = new ArrayList<SelectorVO>();

    /**
     * Returns the data
     * 
     * @return Collection
     */
    public SelectorVO[] getSelectors() {
        return (SelectorVO[]) selectorList.toArray(new SelectorVO[selectorList.size()]);
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            from the resultset
     */
    public void processRow(Row row) {
        SelectorVO item = new SelectorVO();

        // Put the data into a VO
        item.setId(row.getInteger("selector_id"));
        item.setSelectorQuery(row.getString("selector"));
        item.setDescription(row.getString("description"));
        item.setEnabled(row.getString("enabled"));
        item.setPrecedence(row.getInteger("precedence"));

        // Add the VO to the ArrayList
        selectorList.add(item);
    }

}
