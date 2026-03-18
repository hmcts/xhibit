package uk.gov.courtservice.framework.services.config;

/**
 * <p>
 * Title: NameFilter
 * </p>
 * <p>
 * Description: This interface should be implemented by the classes that filter
 * the names that are cached by the EncHelper
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

public interface NameFilter {

    /**
     * This method should contain the logic to decide whether a name should be
     * cached.
     * 
     * @param name
     * @return True indicating the name should be cached.
     */
    public boolean accept(String name);

}