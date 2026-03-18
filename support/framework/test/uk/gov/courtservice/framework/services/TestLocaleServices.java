//package uk.gov.courtservice.framework.services;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.Iterator;
//import java.util.Locale;
//
//import junit.framework.Test;
//import junit.framework.TestCase;
//import junit.framework.TestSuite;
//import junit.textui.TestRunner;
//
//public class TestLocaleServices extends TestCase {
//
//    public static Test suite() {
//        return new TestSuite(TestLocaleServices.class);
//    }
//
//    public static void main(String args[]) {
//        TestRunner.run(suite());
//    }
//
//    public TestLocaleServices(String s) {
//        super(s);
//    }
//
//    protected void setUp() {
//    }
//
//    protected void tearDown() {
//    }
//
//    public void testGetCandidates() {
//        try {
//            LocaleServices service = LocaleServices.getInstance();
//            System.out.println("\nRunning testGetCandidates\n");
//            System.out.println("Candidates for foo.txt");
//            Iterator names = service.getCandidates(Locale.getDefault(), "foo.txt");
//            while (names.hasNext()) {
//                System.out.println("Found candidate ==> " + names.next());
//            }
//            System.out.println("Candidates for bar");
//            names = service.getCandidates(Locale.getDefault(), "bar");
//            while (names.hasNext()) {
//                System.out.println("Found candidate ==> " + names.next());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//
//    public void testBaseName() {
//        try {
//            LocaleServices service = LocaleServices.getInstance();
//            System.out.println("\nRunning getBaseName\n");
//            System.out.println("Get the base name for foo_en.txt");
//            System.out.println("Base Name ==> " + service.getBaseName(Locale.getDefault(), "foo_en.txt"));
//            System.out.println("Get the base name for bar_en_GB");
//            System.out.println("Base Name ==> " + service.getBaseName(Locale.getDefault(), "bar_en_GB"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//
//    public void testGetResource() {
//        try {
//            LocaleServices service = LocaleServices.getInstance();
//            System.out.println("\nRunning getResource\n");
//            System.out.println("Looking for config/xsl/crestformsbf/crestFormsFO.xsl");
//            System.out.println("Found Resource ==> "
//                    + service.getResource(Locale.getDefault(), "config/xsl/crestformsbf/crestFormsFO.xsl"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//
//    public void testGetResourceAndOpenStream() {
//        try {
//            LocaleServices service = LocaleServices.getInstance();
//            String resourceName = "config/xsl/crestformsbf/crestFormsFO.xsl";
//            System.out.println("\nRunning testGetResourceAndOpenStream\n");
//            System.out.println("Looking for " + resourceName);
//            String resource = service.getResource(Locale.getDefault(), resourceName);
//            if (resource != null) {
//                System.out.println("Found Resource ==> " + resource);
//                String resourceToLookup = service.getBaseName(Locale.getDefault(), resource);
//                System.out.println("Resource to look up : " + resourceToLookup);
//                System.out.println("Trying to open stream for " + resourceToLookup);
//                InputStream is = service.openStream(Locale.getDefault(), resourceToLookup);
//                BufferedReader br = new BufferedReader(new InputStreamReader(is));
//                String line = br.readLine();
//                while (line != null) {
//                    System.out.println(line);
//                    line = br.readLine();
//                }
//            } else {
//                fail("Could not find resource for " + resourceName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            fail(e.toString());
//        }
//    }
//
//}
//