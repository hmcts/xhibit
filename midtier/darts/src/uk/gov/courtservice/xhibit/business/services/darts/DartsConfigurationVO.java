package uk.gov.courtservice.xhibit.business.services.darts;

import java.io.Serializable;

/**
 * <p>
 * Title: DartsConfigurationVO
 * </p>
 * <p>
 * Description: A VO that holds the config status for the Darts system
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @version 1.0
 */
public class DartsConfigurationVO implements Serializable {
    
    /**
     * This should be updated whenever the non transient fields are changed.
     */
    private static final long serialVersionUID = 1L;    
    
    private boolean dartsActive;
    private String doctypes;
    private int cacheTime;

    public DartsConfigurationVO() {
    }

    public DartsConfigurationVO(boolean dartsActive, String doctypes, int cacheTime) {
        this.dartsActive = dartsActive;
        this.doctypes = doctypes;
        this.cacheTime = cacheTime;
    }

    /**
     * Return a debug string containing info about the String
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("DartsConfiguration[dartsActive=");
        builder.append(dartsActive);
        builder.append(",cacheTime=");
        builder.append(cacheTime);
        builder.append(",doctypes=");
        builder.append(doctypes);
        builder.append("]");
        return builder.toString();
    }

    public boolean isDartsActive() {
        return dartsActive;
    }

    public void setDartsActive(boolean dartsActive) {
        this.dartsActive = dartsActive;
    }

    public String getDoctypes() {
        return doctypes;
    }

    public void setDoctypes(String doctypes) {
        this.doctypes = doctypes;
    }

    public int getCacheTime() {
        return cacheTime;
    }

    public void setCacheTime(int cacheTime) {
        this.cacheTime = cacheTime;
    }
    
}
