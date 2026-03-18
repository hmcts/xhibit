package uk.gov.courtservice.xhibit.integration.mercator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;

/**
 * Class whose methods perform useful calculations regarding performance
 * statistics This includes a getSizeOf method that will determine the size of
 * the Serializable object passed to it
 */

public class MercatorTransactionStatistics {
    private static final int DEFAULT_SIZE = 0;

    private static final int DEFAULT_TIME = 0;

    private static Logger log = CSServices.getLogger(MercatorTransactionStatistics.class);

    /**
     * Determines the size of the Serializable object passed to it
     * 
     * Exception handling: All IOExceptions handled internally, method will
     * return 0 The object passed in is checked to ensure it is not null and
     * isSerializable - if these conditions are not met 0 is returned
     * 
     * @param object
     *            Mandatory. This is the object we are going to determine the
     *            size of which must be Serializable or Externalizable
     * @return The size in bytes of the object
     */
    public static int getSizeOf(final Serializable object) {
        if (!checkSerializable(object)) {
            log.debug("Check serializable failed");
            return DEFAULT_SIZE;
        }

        ObjectOutputStream oos = null;
        ByteArrayOutputStream baos = null;
        int objectSize = 0;

        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);

            oos.writeObject(object);

            objectSize = baos.size();
        } catch (final IOException io) {
            log.debug("getSizeOf IOException manipulating oos and baos, error is: " + io.toString());
            return DEFAULT_SIZE;
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                    baos = null;
                }
            } catch (final IOException e) {
                log.debug("getSizeOf IOException closing baos, error is: " + e.toString());
                return DEFAULT_SIZE;
            } finally {
                try {
                    if (oos != null) {
                        oos.close();
                        oos = null;
                    }
                } catch (final IOException e) {
                    log.debug("getSizeOf IOException closing oos, error is: " + e.toString());
                    return DEFAULT_SIZE;
                }
            }
        }

        log.debug("***** Sizeof object is: " + objectSize);
        return objectSize;
    } // sizeof

    /**
     * Determines the difference in milliseconds between two times. The
     * startTime must be before the endTime
     * 
     * @param startTime:
     *            Mandatory: this is the start time
     * @param endTime:
     *            Mandatory: this is the end time
     * @return The diffence in milliseconds between the start and end time
     */
    public static long getElapsedTime(Calendar startTime, Calendar endTime) {
        long elapsedTime = 0;

        if (startTime == null || endTime == null) {
            log.debug("getElapsedTime: null calendar passed in ");
            return DEFAULT_TIME;
        }

        if (endTime.getTime().getTime() >= startTime.getTime().getTime()) {
            elapsedTime = endTime.getTime().getTime() - startTime.getTime().getTime();
        } else {
            log.debug("getElapsedTime: endTime before startTime");
            return DEFAULT_TIME;
        }

        log.debug("***** Elapsed Time: " + elapsedTime);

        return elapsedTime;
    }

    /**
     * Checks that an object is not null and is serializable
     * 
     * @param object:
     *            the object we are going check to see if it is serializable
     * @return boolean
     */
    private static boolean checkSerializable(final Object object) {
        if (object == null) {
            log.debug("checkSerializable object was null");
            return false;
        }

        if (!(object instanceof Serializable)) {
            log.debug("checkSerializable object was not Serializable");
            return false;
        }

        return true;
    }
}