package uk.gov.courtservice.xhibit.business.services.userterminal;

import java.util.ArrayList;
import java.util.Iterator;

import javax.security.auth.Subject;

import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroup;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group.XhbSecurityGroupBeanHelper2;
import uk.gov.courtservice.xhibit.business.entities.xhb_security_group_role.XhbSecurityGroupRole;
import weblogic.security.Security;
import weblogic.security.SubjectUtils;
import weblogic.security.spi.WLSGroup;
import weblogic.security.spi.WLSUser;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2004
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version $Id: UserHelper.java,v 1.5 2014/06/20 17:43:01 atwells Exp $
 */

public class UserHelper {

    private UserHelper() {
        // Reduce Access Permisions
    }

    /**
     * This method returns the functionalities available for the passed subject
     * 
     * @param subject
     * @return A string array with the list of functionalities
     */
    public static String[] getFunctionalities(Subject subject) {
        // List of functionalities
        ArrayList rls = new ArrayList();

        XhbSecurityGroup group = null;
        // Iterate through the list of groups
        for (Iterator groups = XhbSecurityGroupBeanHelper2.findAll().iterator(); groups.hasNext();) {
            // Get the next group in the list
            group = (XhbSecurityGroup) groups.next();

            // If the user is not a member ignore
            if (!isUserInGroup(group.getGroupName(), subject))
                continue;

            // Get the roles for the group
            for (Iterator groupRoles = group.getXhbSecurityGroupRoles().iterator(); groupRoles.hasNext();) {
                XhbSecurityGroupRole groupRole = (XhbSecurityGroupRole) groupRoles.next();
                if (groupRole.getIsEnabled().equals("Y")) {
                    rls.add(groupRole.getXhbSecurityRole().getRoleName());
                }
            }
        }

        // Return the list as a string array
        return (String[]) rls.toArray(new String[rls.size()]);
    }

    /**
     * Checks whether the current subject is in the group
     * 
     * @param group
     * @return true if the current user is in the specified group
     */
    public static boolean isUserInGroup(String group, Subject subject) {
        //Subject subject = Security.getCurrentSubject();
        return subject != null && SubjectUtils.isUserInGroup(subject, group);
    }

    /**
     * Checks whether the current subjects principals is in the group
     * 
     * @param group
     * @return true if the current user is in the specified group
     */
    public static String getSubjectDetails(Subject subject) {
        //Subject subject = Security.getCurrentSubject();
        if (subject == null) {
            return "anonyomous";
        }

        StringBuilder builder = new StringBuilder();

        // Append Users
        Iterator<WLSUser> users = subject.getPrincipals(WLSUser.class).iterator();
        if (users.hasNext()) {
            builder.append(users.next().getName());
            while (users.hasNext()) {
                builder.append(',');
                builder.append(users.next().getName());
            }
        }
        // Append Groups
        builder.append('(');
        Iterator<WLSGroup> groups = subject.getPrincipals(WLSGroup.class).iterator();
        if (groups.hasNext()) {
            builder.append(groups.next().getName());
            while (groups.hasNext()) {
                builder.append(',');
                builder.append(groups.next().getName());
            }
        }
        builder.append(')');

        return builder.toString();
    }
}