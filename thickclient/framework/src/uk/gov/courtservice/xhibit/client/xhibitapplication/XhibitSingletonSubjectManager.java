package uk.gov.courtservice.xhibit.client.xhibitapplication;

import javax.security.auth.Subject;

import uk.gov.courtservice.framework.security.SubjectManager;

/**
 * <p>
 * Title: SecurityAccessInfoFactory
 * </p>
 * <p>
 * Description: Get the current subject from the XhibitSingleton. This is set
 * when the application is logged on. The use of this factory is specified as a
 * command line parameter to override the default setting.
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: Electronic Data Systems
 * </p>
 * 
 * @author William Fardell, Xdevelopment (2004)
 * @version $Id: XhibitSingletonSubjectManager.java,v 1.1 2006/05/19 08:06:04
 *          bzjrnl Exp $
 */
public class XhibitSingletonSubjectManager extends SubjectManager {

    /**
     * Get the access info from the xhibit singleton
     */
    public Subject getCurrentSubject() {
        return XhibitSingleton.getInstance().getCurrentSubject();
    }
}
