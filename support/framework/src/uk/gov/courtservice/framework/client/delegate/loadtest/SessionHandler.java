package uk.gov.courtservice.framework.client.delegate.loadtest;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;
import bsh.Interpreter;

/**
 * User: vz1q6h Date: 20-May-2004 Time: 17:40:02
 */

public class SessionHandler {

    private Vector threadResults;

    public SessionHandler(Vector threadResults) {
        this.threadResults = threadResults;
    }

    public Set runScenario(Scenario scenario) {
        System.out.println("Running scenario:" + scenario.getFilename());
        Set set = Collections.synchronizedSet(new HashSet());
        // ScenarioThread[] threads = new
        // ScenarioThread[scenario.getNumberConcurrent() *
        // scenario.getRepititions()];

        Vector threadMonitors = threadMonitors = new Vector();
        for (int i = 0; i < scenario.getRepititions(); i++) {
            // Create concurrent threads
            Vector threads = new Vector();
            for (int j = 0; j < scenario.getNumberConcurrent(); j++) {
                ThreadResult threadResult = new ThreadResult();
                threadResult.setThreadNo(j);
                threadResult.setRepitition(i);
                threads.add(new ScenarioThread(scenario.getFilename(), set, threadResult));
                threadResults.add(threadResult);

            }
            ThreadMonitor monitor = new ThreadMonitor(threads, scenario, threadResults, i);
            monitor.start();
            threadMonitors.add(monitor);

            try {
                Thread.sleep(scenario.getSleepPeriodBetweenReps());
            } catch (InterruptedException e) {
                throw new CSUnrecoverableException(e);
            }
        }

        System.out.println("Waiting for thread monitors to complete");

        while (true) {
            boolean finished = true;
            // System.out.println("Thread Monitors:" +
            // threadMonitors.size());
            for (int j = 0; j < threadMonitors.size(); j++) {
                ThreadMonitor thread = (ThreadMonitor) threadMonitors.get(j);
                finished = !thread.isAlive() && finished;
            }
            if (finished)
                break;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new CSUnrecoverableException(e);
            }
        }
        return set;
    }
}

class ThreadMonitor extends Thread {
    private Vector threads;

    private int timeout;

    private Vector threadResults;

    private int rep;

    private int staggerPeriod;

    public ThreadMonitor(Vector threads, Scenario scenario, Vector threadResults, int rep) {
        this.threads = threads;
        this.timeout = scenario.getTimeout();
        this.threadResults = threadResults;
        this.rep = rep;
        this.staggerPeriod = scenario.getStaggerPeriod();

    }

    public void run() {
        Date startTime = new Date();
        for (int i = 0; i < threads.size(); i++) {
            ScenarioThread thread = (ScenarioThread) threads.get(i);
            thread.start();
            sleep(staggerPeriod);
        }

        while (true) {
            boolean finished = true;
            for (int i = 0; i < threads.size(); i++) {
                ScenarioThread thread = (ScenarioThread) threads.get(i);
                finished = !thread.isAlive() && finished;
            }
            if (finished)
                break;
            Date now = new Date();
            long elapsedTime = now.getTime() - startTime.getTime();

            /*
             * for (int i = 0; i < threads.size(); i++) { ScenarioThread thread =
             * (ScenarioThread) threads.get(i); ThreadResult threadResult =
             * (ThreadResult) threadResults.get(i + rep * threads.size());
             * System.out.println("Elapsed time:" + elapsedTime);
             * System.out.println("LHS:" + (elapsedTime + i * staggerPeriod));
             * System.out.println("RHS:" + (timeout * 1000)); if (elapsedTime +
             * i * staggerPeriod * 1000 > timeout * 1000) { if
             * (thread.isAlive()) { thread.interrupt();
             * threadResult.setStatus("Timed Out"); } } }
             */
            if (elapsedTime > timeout) {
                for (int i = 0; i < threads.size(); i++) {
                    ScenarioThread thread = (ScenarioThread) threads.get(i);
                    ThreadResult threadResult = (ThreadResult) threadResults.get(i + rep * threads.size());
                    if (thread.isAlive()) {
                        thread.interrupt();
                        threadResult.setStatus("Timed Out");
                    }
                }
                break;
            }

            try {
                sleep(100l);
            } catch (InterruptedException e) {
                throw new CSUnrecoverableException(e);
            }
        }
    }

    public void sleep(int secs) {
        try {
            // System.out.println("Thread Monitor: Sleeping for " + secs + "
            // seconds");
            Thread.sleep(secs * 1000);
        } catch (InterruptedException e) {
            throw new CSUnrecoverableException(e);
        }
    }

}

class ScenarioThread extends Thread {
    private String m_fileName;

    private Interpreter bshInterpreter;

    private Set results;

    private ThreadResult threadResult;

    public ScenarioThread(String fileName, Set results, ThreadResult threadResult) {
        // System.out.println("Creating thread");
        m_fileName = fileName;
        bshInterpreter = new Interpreter();
        this.results = results;
        this.threadResult = threadResult;
    }

    public void run() {
        try {
            // System.out.println("running beanshell:" + m_fileName +
            // ".bsh");
            Set result = (Set) bshInterpreter.source(m_fileName + ".bsh");
            threadResult.setStatus("Success");
            results.addAll(result);
        } catch (Throwable e) {
            // e.printStackTrace();
            threadResult.setMessage(e.toString());
            threadResult.setStatus("Failed");
        }
    }
}
