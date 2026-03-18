package uk.gov.courtservice.xhibit.client.results.verdicts;

import java.util.Collection;
import java.util.Observable;

public class VerdictFilterSelectionModel extends Observable {
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

    private Collection defendants = null;
    private String defendantFilter = null;
    
    public VerdictFilterSelectionModel() {}
    
    private void updateObservers() {
        setChanged();
        notifyObservers();
    }

    public void setDefendmentFilter(String s) {
        defendantFilter = s;
        updateObservers();
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

    public Collection getDefendants() {
        return defendants;
    }

    public void setDefendants(Collection c) {
        this.defendants = c;
    }
}
