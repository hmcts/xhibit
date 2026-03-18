package uk.gov.courtservice.framework.business.entities;

import javax.ejb.EJBLocalObject;

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
 * @author Meeraj Kunnumpurath
 * @version 1.0
 */

public interface CSEntityLocal extends EJBLocalObject {
    public Integer getVersion();

    public void setUpdated(String userDisplayName);
}