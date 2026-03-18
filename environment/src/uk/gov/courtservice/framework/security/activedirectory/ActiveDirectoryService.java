package uk.gov.courtservice.framework.security.activedirectory;

public interface ActiveDirectoryService {
    public void initialize(ActiveDirectoryServiceConfig config);

    public ActiveDirectoryUser getUser(String principalName);

    public boolean authenticateUser(ActiveDirectoryUser user, String credentials);

    public ActiveDirectoryGroup[] getGroups(ActiveDirectoryUser user);

    public ActiveDirectoryGroup getRootGroup();

    public ActiveDirectoryGroupHierarchy getGroupHierarchy();

    public String[] getSupportedControls();

}
