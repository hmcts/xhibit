package uk.gov.courtservice.xhibit.business.audittrail;

import java.util.Hashtable;

import uk.gov.courtservice.framework.services.audittrail.AuditTrailEvent;
import uk.gov.courtservice.framework.services.audittrail.AuditTrailEventFactory;

import org.apache.log4j.Logger;


import uk.gov.courtservice.xhibit.business.entities.aud_user_logins.AudUserLoginsBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_court_log_entry.XhbCourtLogEntry;
import uk.gov.courtservice.xhibit.business.entities.xhb_disposal2.XhbDisposal2BasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_plea.XhbPlea;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRoleBasicValue;
import uk.gov.courtservice.xhibit.business.entities.xhb_verdict.XhbVerdict;
import uk.gov.courtservice.xhibit.business.vos.services.defendant.DefendantValue;
import uk.gov.courtservice.xhibit.integration.vos.services.caseretrieval.CaseAccessValue;

/**
 * <p>
 * Title: AuditTrailEventFactory
 * </p>
 * <p>
 * Description: This class is the event factory responsible for creating AuditTrailEvents
 * </p>
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * <p>
 * Company: Logica
 * </p>
 * 
 * @author James Powell
 * @version 1.0
 */

public class AuditTrailEventFactoryImpl implements AuditTrailEventFactory{
    private static final Logger log = Logger.getLogger(AuditTrailEventFactoryImpl.class);          
    
    /**
     * This method accepts an aribtrary argument and then creates and returns the relevant
     * Audit Trail Event. If there is no matching clause, then an event is not created and the 
     * metthod returns null
     */
    public AuditTrailEvent getAuditTrailEvent(Object argument){
        if(argument instanceof Hashtable){
            log.debug("Found Hashtable");
            Hashtable ht = (Hashtable) argument;
            
            String key = XhbCourtLogEntry.class.getName();
            if(ht.containsKey(key)){
                log.debug("Found Court Log class, creating event");
                return new AuditTrailCourtEvent(ht.get(key));
            }
            key = XhbVerdict.class.getName();
            if(ht.containsKey(key)){
                log.debug("Found Verdict class, creating event");
                return new AuditTrailVerdictEvent(ht.get(key));
            }
            key = XhbPlea.class.getName();
            if(ht.containsKey(key)){
                log.debug("Found Plea class, creating event");
                return new AuditTrailPleaEvent(ht.get(key));
            }
            key = XhbSecurityGroupRoleBasicValue.class.getName();
            if(ht.containsKey(key)){
                log.debug("found RoleMapping Event, creating event");
                return new AuditTrailRoleChangeEvent(ht.get(key));
            }
            
            key = XhbDisposal2BasicValue.class.getName(); 
            if(ht.containsKey(key)){
                log.debug("found disposal event, creating event");
                return new AuditTrailDisposalEvent(ht.get(key));
            }
            
            key = DefendantValue.class.getName();
            if(ht.containsKey(key)){
                log.debug("found defendant event, creating event");
                return new AuditTrailDefendantEvent(ht.get(key));
            }
            
            key = CaseAccessValue.class.getName();
            if(ht.containsKey(key)){
                log.debug("found caseAccessValue, creating event");
                return new AuditTrailSystemEvent(ht.get(key));
            }
            
            key = AuditTrailEvent.USER_LOGON;
            if(ht.containsKey(key)){
                log.debug("found Logon event, creating event");
                return new AuditTrailLoginEvent(ht.get(key));
            }
            key = AuditTrailEvent.USER_LOGOFF;
            if(ht.containsKey(key)){
                log.debug("found logoff event, creating event");
                return new AuditTrailLogoffEvent(ht.get(key));
            }
            key = AudUserLoginsBasicValue.class.getName();
            String key2 = Exception.class.getName();
            if(ht.containsKey(key) && ht.containsKey(key2)){
                log.debug("Found ubsuccessful login attempt");
                return new AuditTrailLoginEvent(ht.get(key),ht.get(key2));
            }
            
        }
        return null;
    }
} 