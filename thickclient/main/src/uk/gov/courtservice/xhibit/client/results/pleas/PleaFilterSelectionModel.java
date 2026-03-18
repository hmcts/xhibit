package uk.gov.courtservice.xhibit.client.results.pleas;

import java.util.Collection;
import java.util.Observable;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author Simon Gilmore
 * @version 1.0
 */
public class PleaFilterSelectionModel extends Observable {
    public static final int FILTER_NONE = 0;

    public static final int FILTER_INDICTMENT = 1;

    public static final int FILTER_INDICTMENT_COUNT = 2;

    public static final int FILTER_COUNT = 4;

    public static final int FILTER_DEFENDANT = 8;

    public static final int FILTER_INDICTMENT_COUNT_DEFENDANT = 16;

    public static final int FILTER_INDICTMENT_DEFENDANT = 32;

    public static final int FILTER_COUNT_DEFENDANT = 64;

    private int filter = FILTER_NONE;

    private int previousFilter = FILTER_NONE;

    private Collection indictments = null;

    private Collection counts = null;

    private Collection defendants = null;

    private String indictmentFilter = null;

    private String countFilter = null;

    private String defendantFilter = null;

    public PleaFilterSelectionModel() {
    }

    private void updateObservers() {
        setChanged();
        notifyObservers();
    }

    public void setIndictmentFilter(String s) {
        indictmentFilter = s;
        updateObservers();
    }

    public void setCountFilter(String s) {
        countFilter = s;
        updateObservers();
    }

    public void setDefendmentFilter(String s) {
        defendantFilter = s;
        updateObservers();
    }

    public String getIndictmentFilter() {
        return indictmentFilter;
    }

    public String getCountFilter() {
        return countFilter;
    }

    public String getDefendantFilter() {
        return defendantFilter;
    }

    public int getFilter() {
        return filter;
    }

    public void setFilter(int newFilter) {
        previousFilter = filter;
        filter = newFilter;
    }

    public int getPreviousFilter() {
        return previousFilter;
    }

    public Collection getIndictments() {
        return indictments;
    }

    public Collection getCounts() {
        return counts;
    }

    public Collection getDefendants() {
        return defendants;
    }

    public void setIndictments(Collection c) {
        this.indictments = c;
    }

    public void setCounts(Collection c) {
        this.counts = c;
    }

    public void setDefendants(Collection c) {
        this.defendants = c;
    }
}