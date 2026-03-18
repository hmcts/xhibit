package uk.gov.courtservice.framework.security.providers.authorization;

import org.apache.log4j.Logger;

/**
 * This class provides read-write lock functionality
 * 
 * @author Meeraj
 * @version $Id: RWLock.java,v 1.4 2006/06/05 12:30:05 bzjrnl Exp $
 */
public class RWLock {
    private static final Logger log = Logger.getLogger(RWLock.class);

    private final Object mutex = new Object();

    private int givenLocks = 0;

    private int waitingWriters = 0;

    private int waitingReaders = 0;

    /**
     * Gets a read lock
     * 
     * @throws NestedException
     *             DOCUMENT ME!
     */
    public void getReadLock() {
        synchronized (mutex) {
            while ((givenLocks == -1) || (waitingWriters != 0)) {
                try {
                    mutex.wait();
                } catch (InterruptedException ex) {
                    throw new NestedException(ex);
                }
            }

            givenLocks++;
        }

        if (log.isDebugEnabled()) {
            log.debug("Read lock given:" + givenLocks);
        }
    }

    /**
     * Gets a write lock
     */
    public void getWriteLock() {
        synchronized (mutex) {
            waitingWriters++;

            while (givenLocks != 0) {
                try {
                    mutex.wait();
                } catch (InterruptedException ex) {
                    log.warn(ex.getMessage(), ex);
                }
            }

            waitingWriters--;
            givenLocks = -1;
        }
        if (log.isDebugEnabled()) {
            log.debug("Write lock given:" + givenLocks);
        }
    }

    /**
     * Release read lock
     */
    public void releaseReadLock() {
        releaseLock();
        if (log.isDebugEnabled()) {
            log.debug("Read lock released:" + givenLocks);
        }
    }

    /**
     * Release read lock
     */
    public void releaseWriteLock() {
        releaseLock();
        if (log.isDebugEnabled()) {
            log.debug("Write lock released:" + givenLocks);
        }
    }

    /**
     * Releases a lock
     */
    private void releaseLock() {
        synchronized (mutex) {
            if (givenLocks == 0) {
                return;
            }

            if (givenLocks == -1) {
                givenLocks = 0;
            } else {
                givenLocks--;
            }

            mutex.notifyAll();
        }
    }

    public int getWaitingWriters() {
        return waitingWriters;
    }

    public int getWaitingReaders() {
        return waitingReaders;
    }

    public int getGivenLocks() {
        return givenLocks;
    }
}
