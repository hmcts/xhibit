package uk.gov.courtservice.framework.security.providers.authorization;

public class LDAPContextUtil {

    private String userPrincipalAttribute;
    private String userDisplayAttribute;
    private String userForenameAttribute;
    private String userSurnameAttribute;
    private String groupPrincipalAttribute;
    private String groupMemberOfAttribute;
    private String groupRootPrincipal;
    
    public static String getGroupMemberOfAttribute() {
        return "memberOf";
    }
    public void setGroupMemberOfAttribute(String groupMemberOfAttribute) {
        this.groupMemberOfAttribute = groupMemberOfAttribute;
    }
    public static String getGroupPrincipalAttribute() {
        return "cn";
    }
    public void setGroupPrincipalAttribute(String groupPrincipalAttribute) {
        this.groupPrincipalAttribute = groupPrincipalAttribute;
    }
    public static String getGroupRootPrincipal() {
        return "XHIBIT User";
    }
    public void setGroupRootPrincipal(String groupRootPrincipal) {
        this.groupRootPrincipal = groupRootPrincipal;
    }
    public static String getUserPrincipalAttribute() {
        return "samAccountName";
    }
    public void setUserPrincipalAttribute(String userPrincipalAttribute) {
        this.userPrincipalAttribute = userPrincipalAttribute;
    }
    public static String getUserDisplayAttribute() {
        return "displayName";
    }
    public void setUserDisplayAttribute(String userDisplayAttribute) {
        this.userDisplayAttribute = userDisplayAttribute;
    }
    public static String getUserForenameAttribute() {
        return "givenName";
    }
    public void setUserForenameAttribute(String userForenameAttribute) {
        this.userForenameAttribute = userForenameAttribute;
    }
    public static String getUserSurnameAttribute() {
        return "sn";
    }
    public void setUserSurnameAttribute(String userSurnameAttribute) {
        this.userSurnameAttribute = userSurnameAttribute;
    }
}
