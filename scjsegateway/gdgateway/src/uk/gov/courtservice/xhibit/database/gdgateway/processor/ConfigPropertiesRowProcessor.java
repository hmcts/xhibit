package uk.gov.courtservice.xhibit.database.gdgateway.processor;

import java.util.ArrayList;
import java.util.Collection;

import uk.gov.courtservice.framework.jdbc.core.AbstractRowProcessor;
import uk.gov.courtservice.framework.jdbc.core.Row;
import uk.gov.courtservice.xhibit.business.vos.gdgateway.ConfigPropertiesVO;

/**
 * <p>
 * Title: ConfigPropertiesRowProcessor
 * </p>
 * <p>
 * Description: Class to process the gdg_config_properties rows.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2006
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Rob Sumner
 * @version $Id: ConfigPropertiesRowProcessor.java,v 1.4 2006/09/15 14:20:53 rzvddy Exp $
 */
public class ConfigPropertiesRowProcessor extends AbstractRowProcessor{
    
    // declare the return VO
    //private ConfigPropertiesVO configProperties = new ConfigPropertiesVO();

    private Collection<ConfigPropertiesVO> configProperties = new ArrayList<ConfigPropertiesVO>();
    
    /**
     * Returns the data
     * 
     * @return ConfigPropertiesVO
     *           the property
     */
    public Collection<ConfigPropertiesVO> getConfigProperties() {
        return configProperties;
    }

    /**
     * Implementation of row processor
     * 
     * @param row
     *            the result set will only contain one row
     */
    public void processRow(Row row) {
        ConfigPropertiesVO item = new ConfigPropertiesVO();
        item.setPropertyId(row.getLong("id"));
        item.setPropertyName(row.getString("property_name"));
        item.setPropertyValue(row.getString("property_value"));
        item.setPropertyCode(row.getString("property_code"));
        item.setPropertyTimestamp(row.getDate("property_timestamp"));
    
        configProperties.add(item);
    }
}

