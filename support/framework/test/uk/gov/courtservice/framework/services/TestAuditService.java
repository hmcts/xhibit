package uk.gov.courtservice.framework.services;

import junit.framework.TestCase;
import uk.gov.courtservice.framework.services.audit.AuditService;
import uk.gov.courtservice.framework.services.audit.EventTypes;

public class TestAuditService extends TestCase {

    public TestAuditService(String s) {
        super(s);
    }

    protected void setUp() {
    }

    protected void tearDown() {
    }

    public void testAudit() {
        try {
            AuditService auditservice = CSServices.getAuditService();
            auditservice.audit(1, EventTypes.DEFAULT);
        } catch (Exception e) {
            fail(e.toString());
        }

    }

}
