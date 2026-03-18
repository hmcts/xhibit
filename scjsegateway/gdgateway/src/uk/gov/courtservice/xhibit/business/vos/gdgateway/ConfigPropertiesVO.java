package uk.gov.courtservice.xhibit.business.vos.gdgateway;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * Title: Value Object for the Config Properties.
 * </p>
 * <p>
 * Description: Value Object for the properties that get stored in
 * GDG_CONFIG_PROPERTIES.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Rob Sumner
 * @version $Id: ConfigPropertiesVO.java,v 1.2 2006/09/06 09:39:56 jzj6wd Exp $
 */

public class ConfigPropertiesVO implements Serializable{
    
    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * The unique id of the property.
     */
    private Long propertyId;

    /**
     * The name of the associated property.
     */
    private String propertyName;

    /**
     * The value of the associated property
     */
    private String propertyValue;
    
    /**
     * The code value of the property
     */
    private String propertyCode;

    /**
     * The timestamp of the property
     */
    private Date propertyTimestamp;

    /**
     * @return the propertyId
     */
    public Long getPropertyId() {
        return propertyId;
    }

    /**
     * @param propertyId the propertyId to set
     */
    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    /**
     * @return the propertyName
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * @return the propertyTimestamp
     */
    public Date getPropertyTimestamp() {
        return propertyTimestamp;
    }

    /**
     * @param propertyTimestamp the propertyTimestamp to set
     */
    public void setPropertyTimestamp(Date propertyTimestamp) {
        this.propertyTimestamp = propertyTimestamp;
    }

    /**
     * @return the propertyValue
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * @param propertyValue the propertyValue to set
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    /**
     * @return the propertyCode
     */
    public String getPropertyCode() {
        return propertyCode;
    }

    /**
     * @param propertyCode the propertyCode to set
     */
    public void setPropertyCode(String propertyCode) {
        this.propertyCode = propertyCode;
    }

}
