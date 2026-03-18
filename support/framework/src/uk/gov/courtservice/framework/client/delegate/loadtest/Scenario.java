package uk.gov.courtservice.framework.client.delegate.loadtest;

/**
 * User: vz1q6h Date: 20-May-2004 Time: 17:40:17
 */

public class Scenario {

    public Scenario() {
    }

    /*
     * public Scenario(String fileName, int numberConcurrent, int sleepPeriod,
     * int repititions, int timeout) { this.filename = fileName;
     * this.numberConcurrent = numberConcurrent; this.staggerPeriod =
     * sleepPeriod; this.repititions = repititions; this.timeout = timeout; }
     */
    public int getNumberConcurrent() {
        return numberConcurrent;
    }

    public void setNumberConcurrent(int numberConcurrent) {
        this.numberConcurrent = numberConcurrent;
    }

    public int getStaggerPeriod() {
        return staggerPeriod;
    }

    public void setStaggerPeriod(int staggerPeriod) {
        this.staggerPeriod = staggerPeriod;
    }

    public int getRepititions() {
        return repititions;
    }

    public void setRepititions(int repititions) {
        this.repititions = repititions;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String toString() {
        String string = "Scenario:" + filename + " con:" + numberConcurrent + " sleep" + staggerPeriod + " reps:"
                + repititions + "timeout:" + timeout;
        return string;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getSleepPeriodBetweenReps() {
        return sleepPeriodBetweenReps;
    }

    public void setSleepPeriodBetweenReps(int sleepPeriodBetweenReps) {
        this.sleepPeriodBetweenReps = sleepPeriodBetweenReps;
    }

    private int sleepPeriodBetweenReps;

    private String filename;

    private int numberConcurrent;

    private int repititions;

    private int staggerPeriod;

    private int timeout;

}
