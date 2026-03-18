package uk.gov.courtservice.xhibit.web.framework.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * <p>
 * Title: Key Factory
 * </p>
 * <p>
 * Used to generate uniques keys.
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author William Fardell, Xdevelopment LLP (2003) $Revision: 1.5 $ $Log:
 *         KeyFactory.java,v $ Revision 1.3 2003/03/21 11:48:29 fz0n8j Revised
 *         thinclient framework!
 * 
 * Revision 1.5 2003/03/17 11:32:07 fz0n8j Added revision cvs comments. ecawley
 * 
 * Revision 1.4 2003/03/14 20:52:49 fz0n8j Deleted pasted log comment.
 * 
 * Revision 1.3 2003/03/14 15:56:15 fz0n8j Made changes to new test and added
 * test-stdout target.
 * 
 * Revision 1.2 2003/03/14 14:28:18 fz0n8j Made method nextKey public.
 * 
 * Revision 1.1 2003/03/14 14:13:09 fz0n8j Initial version with temp unsafe
 * implementation.
 * 
 */

public class KeyFactory {
    /**
     * The format for the date
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * The format for the count
     */
    private static final DecimalFormat countFormat = new DecimalFormat("0000000000");

    /**
     * The format for the random
     */
    private static final DecimalFormat randomFormat = new DecimalFormat("0000000000000000000");

    /**
     * The random number generator
     */
    private static final Random randomGenerator = new Random();

    /**
     * The singleton instance
     */
    private static final KeyFactory instance = new KeyFactory();

    /**
     * Get an instance of the factory.
     * 
     * @return the singleton instance.
     */
    public static final KeyFactory getInstance() {
        return instance;
    }

    /**
     * The last time the key gen was used
     */
    private long time = 0;

    /**
     * The number of times this was used in a given time slice Note: this is
     * only effective up to the length of countFormat
     */
    private int count = 0;

    /**
     * Stops other instances of the factory from being created
     */
    private KeyFactory() {
    }

    /**
     * Generate a unique hard (impossible) to guess key.
     * 
     * @return the next key to use
     */
    public String nextKey() {
        StringBuffer buffer = new StringBuffer();
        synchronized (this) {
            long newTime = System.currentTimeMillis();
            if (time != newTime) {
                time = newTime;
                count = 0;
            } else {
                count++;
            }
            buffer.append(dateFormat.format(new Date(time)));
            buffer.append(countFormat.format(count));
        }
        buffer.append(randomFormat.format(Math.abs(randomGenerator.nextLong())));
        return buffer.toString();
    }
}
