package uk.gov.courtservice.framework.client.delegate.loadtest;

/**
 * User: vz1q6h Date: 24-May-2004 Time: 12:32:46
 */

public class PlayBackEntry {
    private Class homeClass;

    private String remoteMethodName;

    private Class parameterTypes[];

    private Object args[];

    public String getRemoteMethodName() {
        return remoteMethodName;
    }

    public void setRemoteMethodName(String remoteMethodName) {
        this.remoteMethodName = remoteMethodName;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Class getHomeClass() {
        return homeClass;
    }

    public void setHomeClass(Class homeClass) {
        this.homeClass = homeClass;
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(homeClass.getName());
        buffer.append(" ");
        buffer.append(remoteMethodName);
        buffer.append("( ");
        for (int i = 0; i < args.length; i++) {
            buffer.append(args[i]);
            if (i != (args.length - 1))
                buffer.append(", ");
        }
        buffer.append(" )");
        return buffer.toString();
    }
}
