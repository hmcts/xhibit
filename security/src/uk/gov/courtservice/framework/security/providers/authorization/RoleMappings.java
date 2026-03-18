package uk.gov.courtservice.framework.security.providers.authorization;

import java.util.Arrays;
import java.util.HashSet;

import javax.security.auth.Subject;

import org.apache.log4j.Logger;

import weblogic.security.spi.Resource;

/**
 * @author $author$
 * 
 */
public class RoleMappings {
    private static final Logger logger = Logger.getLogger(RoleMappings.class);

    private final Groups groups = new Groups();

    private final RWLock lock = new RWLock();

    /**
     * Load the data from the database
     */
    public void loadData() {
        if (logger.isDebugEnabled()) {
            logger.debug("Refreshing cache");
            System.out.println("Refreshing cache");
        }

        // Refresh
        lock.getWriteLock();

        try {
            groups.refresh();
        } finally {
            lock.releaseWriteLock();
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Cache refreshed: " + groups);
            System.out.println("Cache refreshed: " + groups);
        }
    }

    /**
     * Adds a new role mapping
     * 
     * @param resource
     *            resource for the role
     * @param role
     *            of the role
     * @param grps
     *            that have the role
     */
    public void addMapping(Resource resource, String role, String[] grps) {
        if (logger.isDebugEnabled()) {
            logger.debug("Adding mappings for resource:" + resource + ", role: " + role);
            System.out.println("Adding mappings for resource:" + resource + ", role: " + role);
        }

        for (int i = 0; (grps != null) && (i < grps.length); i++) {
            if (groups.hasMapping(grps[i], role)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Mapping already exists" + role + ":" + grps[i]);
                }

                continue;
            }

            lock.getWriteLock();

            try {
                if (logger.isInfoEnabled()) {
                    logger.info("Write lock acquired");
                    logger.info("Adding mapping, resource = " + resource + ", role: " + role + ", group: " + grps[i]);
                    System.out.println("Adding mapping, resource = " + resource + ", role: " + role + ", group: " + grps[i]);
                }

                groups.addMapping(grps[i], role);

                if (logger.isDebugEnabled()) {
                    logger.debug("Added mapping" + role + ":" + grps[i]);
                    System.out.println("Added mapping" + role + ":" + grps[i]);
                }
            } finally {
                lock.releaseWriteLock();
                if (logger.isDebugEnabled()) {
                    logger.debug("Write lock released");
                }
            }
        }
    }

    /**
     * Gets the roles for the subject and resource
     * 
     * @param res
     *            for which roles are requested
     * @param sub
     *            for which roles are requested
     * 
     * @return
     */
    public String[] getRoles(Resource res, Subject sub) {
        //System.out.println("RoleMappings.getRoles");
        HashSet ret = new HashSet();

        // Get read lock
        lock.getReadLock();
        if (logger.isDebugEnabled()) {
            logger.debug("Read lock acquired");
            System.out.println("Read lock acquired");
        }

        try {
            // Get all the roles
            Group[] grps = groups.getGroups();

            for (int i = 0; i < grps.length; i++) {
                if (grps[i].isMember(sub)) {
                    ret.addAll(Arrays.asList(grps[i].getRoles()));
                }
            }
        } finally {
            // Release lock
            lock.releaseReadLock();
            if (logger.isDebugEnabled()) {
                logger.debug("Read lock released");
            }
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Resource:" + res + ",subject:" + sub + ",roles:" + ret);
            System.out.println("Resource:" + res + ",subject:" + sub + ",roles:" + ret);
        }

        // Return the iterator to the roles
        return (String[]) ret.toArray(new String[ret.size()]);
    }

    public int getWaitingWriters() {
        return lock.getWaitingWriters();
    }

    public int getWaitingReaders() {
        return lock.getWaitingReaders();
    }

    public int getGivenLocks() {
        return lock.getGivenLocks();
    }

    public Groups getGroups() {
        return groups;
    }
}
