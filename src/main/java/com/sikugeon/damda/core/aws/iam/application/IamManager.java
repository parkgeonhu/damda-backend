package com.sikugeon.damda.core.aws.iam.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;
import software.amazon.awssdk.services.iam.waiters.IamWaiter;

import java.util.HashMap;
import java.util.Map;

@Service
public class IamManager implements IamEditor{

    Logger log = LoggerFactory.getLogger(IamManager.class);

    IamClient iamClient;

    public IamManager(IamClient iamClient){
        this.iamClient = iamClient;
    }

    @Override
    public String createIAMUser(String username) {
        try {
            // Create an IamWaiter object
            IamWaiter iamWaiter = iamClient.waiter();

            CreateUserRequest request = CreateUserRequest.builder()
                                        .userName(username)
                                        .build();

            CreateUserResponse response = iamClient.createUser(request);

            // Wait until the user is created
            GetUserRequest userRequest = GetUserRequest.builder()
                                        .userName(response.user().userName())
                                        .build();

            WaiterResponse<GetUserResponse> waitUntilUserExists = iamWaiter.waitUntilUserExists(userRequest);
            waitUntilUserExists.matched().response().ifPresent(System.out::println);

            return response.user().userName();

        } catch (IamException e) {
            throw e;
        }
    }

    @Override
    public Map createIAMAccessKey(String username) {
        Map<String, String> map= new HashMap<>();

        try {
            CreateAccessKeyRequest request = CreateAccessKeyRequest.builder()
                    .userName(username)
                    .build();

            CreateAccessKeyResponse response = iamClient.createAccessKey(request);
            map.put("accessKeyId", response.accessKey().accessKeyId());
            map.put("secretAccessKey", response.accessKey().secretAccessKey());
            return map;

        } catch (IamException e) {
            throw e;
        }
    }

    public boolean addUsertoGroup(String username, String groupname){
        AddUserToGroupRequest request = AddUserToGroupRequest.builder()
                                        .userName(username)
                                        .groupName(groupname)
                                        .build();

        AddUserToGroupResponse response = iamClient.addUserToGroup(request);
        int statusCode = response.sdkHttpResponse().statusCode();

        if(statusCode== HttpStatus.OK.value()) {
            return true;
        }

        return false;
    }
}
