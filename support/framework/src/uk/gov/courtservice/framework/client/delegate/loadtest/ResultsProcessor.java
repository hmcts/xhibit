package uk.gov.courtservice.framework.client.delegate.loadtest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import uk.gov.courtservice.framework.exception.CSUnrecoverableException;

/**
 * User: vz1q6h Date: 26-May-2004 Time: 16:51:55
 */

public class ResultsProcessor {

    private Set set;

    private Scenario scenario;

    private Vector threadResults;

    private PrintStream stream;

    public ResultsProcessor(Set set, Scenario scenario, Vector threadResults) {
        this.set = set;
        this.scenario = scenario;
        this.threadResults = threadResults;
    }

    public void process(String fileName) {
        System.out.println("Processing Results");
        try {
            stream = new PrintStream(new FileOutputStream(fileName + ".html"));
        } catch (FileNotFoundException fe) {
            throw new CSUnrecoverableException(fe); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        } catch (IOException e) {
            throw new CSUnrecoverableException(e); // To change body of
            // catch statement use
            // File | Settings |
            // File Templates.
        }

        System.out.println("Results size:" + set.size());
        List list = new Vector(set);
        Collections.sort(list, new ResultComparator());

        StringBuffer header = new StringBuffer();
        header.append("<html><h2>Scenario Results: " + scenario.getFilename() + "</h2>");
        header.append("<h3>Concurrent:" + scenario.getNumberConcurrent() + " ");
        header.append("Repititiona:" + scenario.getRepititions() + " ");
        header.append("Stagger Period:" + scenario.getStaggerPeriod() + " ");
        header.append("Sleep Period:" + scenario.getSleepPeriodBetweenReps() + " ");
        header.append("Timeout:" + scenario.getTimeout() + "</h3>");

        stream.println(header);
        stream
                .println("<BODY><table cellpadding=\"5\" width=\"%\" bgcolor=\"lightblue\"><tr bgcolor='#669999'><td><b>Class</b></td><td><b>method</b></td><td><b>min</b></td><td><b>average</b></td><td><b>max</b></td><td><b>count</b></td></tr>");
        process(list);
        stream.println("</table><br/><br/>");
        stream.println("<h2>Thread Report</h2>");
        processThreadResults();
        stream.println("</table></body</html>");
        stream.flush();
        stream.close();
    }

    private String truncate(String str) {
        String string = "uk.gov.courtservice.xhibit";
        int index = str.indexOf(string);
        if (index != -1)
            return str.substring(0, index) + str.substring(index + string.length(), str.length());
        return str;
    }

    private void processThreadResults() {
        stream.println("<table cellpadding=\"5\" width=\"%\" bgcolor=\"lightblue\"><tr bgcolor='#669999'>");
        stream.println("<td><b>Status</b></td>");
        stream.println("<td><b>Repitition</b></td>");
        stream.println("<td><b>Thread Number</b></td>");
        stream.println("<td><b>Message</b></td></tr>");

        for (int i = 0; i < threadResults.size(); i++) {
            ThreadResult res = (ThreadResult) threadResults.get(i);
            stream.println("<tr><td>" + res.getStatus() + "</td>");
            stream.println("<td>" + res.getRepitition() + "</td>");
            stream.println("<td>" + res.getThreadNo() + "</td>");
            stream.println("<td>" + res.getMessage() + "</td></tr>");
        }
    }

    private void process(List list) {
        if (list.size() == 0) {
            stream.println("<tr><td>No successfull Results</td></tr>");
            return;
        }
        Result result = (Result) list.get(0);
        int lastIndex = list.lastIndexOf(result);
        List subList = list.subList(0, lastIndex + 1);
        Collections.sort(subList, new ResultTimeComparator());

        long min = ((Result) (subList.get(0))).getTime();
        long max = ((Result) (subList.get(subList.size() - 1))).getTime();
        float ave = 0;
        for (int i = 0; i < subList.size(); i++) {
            Result aresult = (Result) (subList.get(i));
            ave += (aresult.getTime()) / ((subList.size() / 1.0f));
        }

        stream.println("<tr><td>" + truncate(result.getRemoteClass()) + "</td>" + "<td>" + truncate(result.getMethod())
                + "</td>" + "<td>" + min + "</td>" + "<td>" + (long) ave + "</td>" + "<td>" + max + "</td>" + "<td>"
                + subList.size() + "</td>" + "</tr>");

        List endList = list.subList(lastIndex + 1, list.size());
        if (endList.size() > 0)
            process(endList);

    }
}

class ResultComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
        Result res1 = (Result) obj1;
        Result res2 = (Result) obj2;

        String str1 = res1.getClass() + res1.getMethod();
        String str2 = res2.getClass() + res2.getMethod();

        return str1.compareTo(str2);
    }
}

class ResultTimeComparator implements Comparator {
    public int compare(Object obj1, Object obj2) {
        Result res1 = (Result) obj1;
        Result res2 = (Result) obj2;
        return (int) (res1.getTime() - res2.getTime());
    }
}