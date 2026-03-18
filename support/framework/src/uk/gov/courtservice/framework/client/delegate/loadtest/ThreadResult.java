package uk.gov.courtservice.framework.client.delegate.loadtest;

/**
 * Created by IntelliJ IDEA. User: vz1q6h Date: 27-May-2004 Time: 15:03:07 To
 * change this template use File | Settings | File Templates.
 */
public class ThreadResult {

    public int getRepitition() {
        return repitition;
    }

    public void setRepitition(int repitition) {
        this.repitition = repitition;
    }

    public int getThreadNo() {
        return threadNo;
    }

    public void setThreadNo(int threadNo) {
        this.threadNo = threadNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private String status;

    private int repitition;

    private int threadNo;

    private String message = "";

    public String toString() {
        return "Status:" + status + " rep:" + repitition + " threadNumber:" + threadNo + " Message:" + message;
    }

}