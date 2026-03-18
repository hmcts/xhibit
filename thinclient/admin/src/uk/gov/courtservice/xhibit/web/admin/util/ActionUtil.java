package uk.gov.courtservice.xhibit.web.admin.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import uk.gov.courtservice.framework.services.CSServices;
import uk.gov.courtservice.xhibit.business.services.userterminal.UserTerminalControllerBeanBusinessDelegate;

/**
 * <p>
 * Title: ActionUtil
 * </p>
 * <p>
 * Description: This class provides utilitys for moving data to the web beans
 * 
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: EDS
 * </p>
 * 
 * @author Edward Cawley, Xdevelopment LLP (2003) $Revision: 1.1 $
 */
public abstract class ActionUtil {
    private static final Logger log = CSServices.getLogger(ActionUtil.class);


    /**
     * 
     * @return
     */
    public static String getUserName() {
        String userName = getUserDelegate().getUser("");
        log.debug("***********getUserName ::::" + userName);
        return userName;
    }

    /**
     * 
     * @return
     */
    public static UserTerminalControllerBeanBusinessDelegate getUserDelegate() {
        return UserTerminalControllerBeanBusinessDelegate.DelegateFactory.getInstance();
    }

}
