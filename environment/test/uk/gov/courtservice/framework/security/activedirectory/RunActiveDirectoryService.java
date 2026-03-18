package uk.gov.courtservice.framework.security.activedirectory;

public class RunActiveDirectoryService {
    public static void main(String[] args) throws Throwable {
        test(ActiveDirectoryServiceFactory.getActiveDirectoryService(PropertiesActiveDirectoryServiceConfig
                .getResourceAsConfig("service.properties")));
    }

    private static synchronized void test(ActiveDirectoryService service) {
        service.getUser("xhibit_internal");
        
//        for (int i = 0; i < 1000; i++) {
//            try {
//                long starttime = System.currentTimeMillis();
//                ActiveDirectoryGroupHierarchy hierarchy = service.getGroupHierarchy();
//                System.out.println("Getting hierarchy took " + (System.currentTimeMillis() - starttime) + "ms.");
//            } catch (Throwable t) {
//                t.printStackTrace(System.out);
//                try {
//                    System.out.println("Sleeping for 5 seconds.");
//                    Thread.sleep(5000);
//                } catch (InterruptedException ie) {
//                    System.out.println("Sleep interrupted.");
//                    ie.printStackTrace(System.out);
//                }
//            }
//        }
    }
}
