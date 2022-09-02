package com.sikugeon.damda.core.iam.application;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.iam.IamClient;
import software.amazon.awssdk.services.iam.model.*;
import software.amazon.awssdk.services.iam.waiters.IamWaiter;

import java.util.HashMap;
import java.util.Map;

import static com.sikugeon.damda.common.util.JsonUtils.readJsonSimpleDemo;

@Service
public class IamManager implements IamEditor{

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

    public String createIAMRole(String rolename) throws Exception {
        String fileLocation="";

        try {
            JSONObject jsonObject = (JSONObject) readJsonSimpleDemo(fileLocation);
            CreateRoleRequest request = CreateRoleRequest.builder()
                    .roleName(rolename)
                    .assumeRolePolicyDocument(jsonObject.toJSONString())
                    .description("Created using the AWS SDK for Java")
                    .build();

            CreateRoleResponse response = iamClient.createRole(request);
            System.out.println("The ARN of the role is "+response.role().arn());

        } catch (IamException e) {
            throw e;
        }
        return "";
    }

}
