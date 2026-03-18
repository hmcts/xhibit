package uk.gov.courtservice.framework.util;

import java.io.File;
import java.io.IOException;

/**
 * <p>
 * Title: FileUtil
 * </p>
 * <p>
 * Description: This class is used as a repositiry for common functions and
 * utilities relating to file
 * </p>
 * <p>
 * Copyright: Copyright (c) 2005
 * </p>
 * <p>
 * Company: Electronic Dathreads Systems
 * </p>
 * 
 * @author Will Fardel
 * @version $Id: FileUtil.java,v 1.3 2006/06/05 12:30:21 bzjrnl Exp $
 */
public class FileUtil {
    /**
     * Stop creation of this utility class.
     */
    private FileUtil() {
        // Change Permisions
    }

    /**
     * Make this dir and all the dirs above this path. Use this instead of
     * File.mkdirs to work around 4742723.
     * 
     * @param path
     *            the dir to create
     * @throws IOException
     *             if cant create the dir for any reason
     */
    public static boolean mkdirs(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path: null");
        }
        return mkdirs(new File(path));
    }

    /**
     * Make this dir and all the dirs above this path. Use this instead of
     * File.mkdirs to work around 4742723.del
     * 
     * @param path
     *            the dir to create
     * @throws IOException
     *             if cant create the dir for any reason
     */
    public static synchronized boolean mkdirs(File dir) {
        if (dir == null) {
            throw new IllegalArgumentException("dir: null");
        }
        return dir.mkdirs();
    }

    /**
     * Test method
     * 
     * @param args
     *            the command line arguments, these are not used.
     */
    public static void main(final String[] args) throws Exception {
        final Object LOCK = new Object();

        final String ROOT_DIR = getStringProperty("fileutil.root", "./FileUtilTest");
        final int THREAD_COUNT = getIntProperty("fileutil.threadcount", 10);
        final int PARENT_COUNT = getIntProperty("fileutil.parentcount", 20);
        final int DIR_COUNT = getIntProperty("fileutil.dircount", 20);

        ThreadGroup tg = new ThreadGroup("runners");

        final Thread[] threads = new Thread[THREAD_COUNT];
        final int makeErrors[] = new int[THREAD_COUNT];

        for (int i = 0; i < THREAD_COUNT; i++) {
            final Object localLock = new Object();
            final int id = i;
            threads[id] = new Thread(tg, "racer-" + id) {
                public void run() {
                    synchronized (LOCK) {
                        synchronized (localLock) {
                            localLock.notifyAll();
                        }
                        try {
                            LOCK.wait();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    for (int l = 0; l < PARENT_COUNT; l++) {
                        for (int f = 0; f < DIR_COUNT; f++) {
                            String fname = ROOT_DIR + "\\b\\c\\dir_" + l + "\\" + f + "\\" + id;

                            File file1 = new File(fname);
                            File file2 = new File(fname);

                            if (!FileUtil.mkdirs(file1) & !file2.exists()) {
                                makeErrors[id]++;
                            }
                        }
                    }

                    threads[id] = null;
                }
            };

            synchronized (localLock) {
                threads[id].start();
                localLock.wait();
            }
        }

        // Start all threads
        synchronized (LOCK) {
            out("3");
            LOCK.wait(1000);
            out("2");
            LOCK.wait(1000);
            out("1");
            LOCK.wait(1000);
            out("GO!");
            LOCK.notifyAll();
        }

        // Wait for all threads to complete
        while (tg.activeCount() > 0) {
            Thread.sleep(2000);
        }
        tg.destroy();

        // Report any Errors
        int totalErrors = 0;
        for (int i = 0; i < makeErrors.length; i++) {
            if (makeErrors[i] != 0) {
                err("mkdirs errors thread #" + i + " = " + makeErrors[i]);
                totalErrors += makeErrors[i];
            }
        }
        out("mkdirs errors total #" + totalErrors);

        // Tidy up
        deltree(new File(ROOT_DIR));
    }

    private static void deltree(File dir) {
        File[] sub = dir.listFiles();
        for (int i = 0; i < sub.length; i++) {
            if (sub[i].isDirectory())
                deltree(sub[i]);
            else if (!sub[i].delete())
                err("cannot delete " + sub[i]);
        }

        if (!dir.delete())
            err("cannot delete " + dir);
    }

    private static void out(Object o) {
        System.out.println("[" + Thread.currentThread().getName() + "] " + o);
    }

    private static void err(Object o) {
        System.err.println("[" + Thread.currentThread().getName() + "] " + o);
    }

    private static String getStringProperty(String name, String defaultValue) {
        return System.getProperty(name, defaultValue);
    }

    private static int getIntProperty(String name, int defaultValue) {
        String value = System.getProperty(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException nfe) {
                // Do nothing
            }
        }
        return defaultValue;
    }

}