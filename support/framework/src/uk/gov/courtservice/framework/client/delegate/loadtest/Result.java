package uk.gov.courtservice.framework.client.delegate.loadtest;

/**
 * User: vz1q6h Date: 26-May-2004 Time: 14:12:35
 */

public class Result {
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getRemoteClass() {
        return remoteClass;
    }

    public void setRemoteClass(String remoteClass) {
        this.remoteClass = remoteClass;
    }

    private String remoteClass;

    private String method;

    private long time;

    public String toString() {
        return remoteClass + " " + method + " took" + time + " milliseconds";
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Result))
            return false;

        Result res = (Result) obj;
        if (remoteClass.equals(res.getRemoteClass()) && method.equals(res.getMethod()))
            return true;
        return false;
    }

}
