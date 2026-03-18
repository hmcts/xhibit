package uk.gov.courtservice.ant.taskdefs;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

/**
 * Ant task for determining a unique number for the host. The specified property
 * is set to the value. This is a good way of getting a number unique to the
 * current machine. The max and min field can be used to control the range of
 * the number returned. Smaller ranges have more chance of overlap. The offset
 * can be used if more than 1 unique number is required.
 * 
 * @author Will Fardell
 */
public class HostNumber extends Task {

    private String property;

    private String host;

    private Integer offset;

    private Integer min;

    private Integer max;

    public void setProperty(String property) {
        this.property = property;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    /*
     * Execute the command
     */
    @Override
    public void execute() throws BuildException {
        if (property == null) {
            throw new BuildException("property attribute is required", getLocation());
        }
        if (getProject().getProperty(property) == null) {
            // determin number
            long number = getHostNumber(host);

            // offset number
            if (offset != null) {
                number += (long) offset;
            }
            // adjust number to required range
            if (max != null) {
                if (min != null) {
                    number = (number % ((long) max - (long) min)) + (long) min;
                } else {
                    number = number % ((long) max);
                }
            }
            // set property
            getProject().setProperty(property, String.valueOf(number));
            getProject().log("Set property \"" + property + "\" to \"" + number + "\" for host \"" + host + "\".",
                    Project.MSG_VERBOSE);
        } else {
            getProject().log("Property \"" + property + "\" already set.", Project.MSG_VERBOSE);
        }

    }

    private long getHostNumber(String host) {
        byte[] buffer = getAddress(host);

        long address = buffer[3] & 0xFFl;
        address |= ((buffer[2] << 8) & 0xFF00l);
        address |= ((buffer[1] << 16) & 0xFF0000l);
        address |= ((buffer[0] << 24) & 0xFF000000l);

        return address;
    }

    /*
     * Protected so can be stubed by test
     */
    protected byte[] getAddress(String host) {
        try {
            InetAddress inetAddress = (host == null) ? InetAddress.getLocalHost() : InetAddress.getByName(host);
            return inetAddress.getAddress();
        } catch (UnknownHostException uhe) {
            throw new BuildException(uhe);
        }
    }
}
