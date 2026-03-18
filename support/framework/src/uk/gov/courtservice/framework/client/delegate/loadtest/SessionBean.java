package uk.gov.courtservice.framework.client.delegate.loadtest;

import java.util.ArrayList;

/**
 * User: vz1q6h Date: 24-May-2004 Time: 12:15:57
 */

public class SessionBean {
    public ArrayList getScenarios() {
        return scenarios;
    }

    public void setScenarios(ArrayList scenarios) {
        this.scenarios = scenarios;
    }

    private ArrayList scenarios;
}