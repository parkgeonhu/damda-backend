package com.sikugeon.damda.core.aws.iam.application;

import java.util.Map;

public interface IamEditor {

    String createIAMUser(String username);
    Map<String, String> createIAMAccessKey(String username);
    boolean addUserToGroup(String username, String groupname);

}
