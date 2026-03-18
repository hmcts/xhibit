package uk.gov.courtservice.xhibit.client.results;

/**
 * <p>
 * Title: AssentingDissenting
 * </p>
 * <p>
 * Description: Represents an Assenting Dissenting value pair, -1 is used for
 * the null value
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version 1.0
 */
public interface AssentingDissenting {
    /**
     * Return the number of assenting jurors or -1 if not set
     */
    public int getAsscenting();

    /**
     * Return the number of dissenting jurors or -1 if not set
     */
    public int getDissenting();

    /**
     * Return true if null value
     */
    public boolean isNull();
}
