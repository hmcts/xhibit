package uk.gov.courtservice.framework.services.config;

/**
 * <p>
 * Title: WlsEncFilter
 * </p>
 * <p>
 * Description: This class accepts all names
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

public class DefaultNameFilter implements NameFilter {

    /**
     * This method filters nothing
     * 
     * @param name
     * @return Always returns true
     */
    public boolean accept(String name) {
        return true;
    }

}