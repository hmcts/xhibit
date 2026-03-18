package uk.gov.courtservice.framework.client.delegate;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Framework Team
 * @version 1.0
 */
public class CSBusinessDelegateInfo {
    private Class delegateClass;

    private Class homeClass;

    public CSBusinessDelegateInfo(Class delegateClass, Class homeClass) {
        this.delegateClass = delegateClass;
        this.homeClass = homeClass;
    }

    public Class getDelegateClass() {
        return delegateClass;
    }

    public Class getHomeClass() {
        return homeClass;
    }
}