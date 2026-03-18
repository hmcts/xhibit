package uk.gov.courtservice.framework.services.config;

/**
 * <p>
 * Title: WlsEncFilter
 * </p>
 * <p>
 * Description: This class filters the name wls-connector-resref
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

public class WlsEncFilter implements NameFilter {

    // Name by which WL context is bound
    private static final String WLS_REF = "wls-connector-resref";

    /**
     * This method filters the name by which WL context is bound
     * 
     * @param name
     * @return Returns false if the name is wls-connector-resref
     */
    public boolean accept(String name) {
        return !name.equals(WLS_REF);
    }

}