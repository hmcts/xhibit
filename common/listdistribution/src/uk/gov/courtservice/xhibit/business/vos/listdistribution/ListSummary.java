package uk.gov.courtservice.xhibit.business.vos.listdistribution;

import uk.gov.courtservice.xhibit.business.xmlbinding.courtservice.xhibit.List;
import uk.gov.courtservice.xhibit.business.xmlbinding.listdistribution.ListProcessor;

/**
 * <p>
 * Title: ListSummary
 * </p>
 * <p>
 * Description: A summary of the list.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: ListSummary.java,v 1.4 2006/06/05 12:28:21 bzjrnl Exp $
 */
public class ListSummary {
    // The wll control primary key
    private final Integer listId;

    // The list read from the associated xml_document data
    private final List list;

    // The processor determined from the associated xml_document type
    private final ListProcessor listProcessor;

    // The court id of the associated xml_document
    private final Integer courtId;

    // Data to propogate from list to letter for use when formatting
    private final Integer majorVersion;

    // Data to propogate from list to letter for use when formatting
    private final Integer minorVersion;

    // Data to propogate from list to letter for use when formatting
    private final String language;

    // Data to propogate from list to letter for use when formatting
    private final String country;

    /**
     * Construct a new ListSummary
     * 
     * @throws IllegalArgumentException
     *             if any of the parameters are null
     */
    public ListSummary(Integer listId, List list, ListProcessor listProcessor, Integer courtId, Integer majorVersion,
            Integer minorVersion, String language, String country) {
        if (listId == null) {
            throw new IllegalArgumentException("listId: null");
        }

        if (list == null) {
            throw new IllegalArgumentException("list: null");
        }

        if (listProcessor == null) {
            throw new IllegalArgumentException("listProcessor: null");
        }

        if (courtId == null) {
            throw new IllegalArgumentException("courtId: null");
        }

        this.listId = listId;
        this.list = list;
        this.listProcessor = listProcessor;
        this.courtId = courtId;
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
        this.language = language;
        this.country = country;
    }

    /**
     * Get the wll control summary identifier
     */
    public Integer getListId() {
        return listId;
    }

    /**
     * Get the list
     */
    public List getList() {
        return list;
    }

    /**
     * Get the list processor
     */
    public ListProcessor getListProcessor() {
        return listProcessor;
    }

    /**
     * Get the court id
     */
    public Integer getCourtId() {
        return courtId;
    }

    /**
     * Get the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Get the major version
     */
    public Integer getMajorVersion() {
        return majorVersion;
    }

    /**
     * Get the minor version
     */
    public Integer getMinorVersion() {
        return minorVersion;
    }

}
