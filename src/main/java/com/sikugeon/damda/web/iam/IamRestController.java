package com.sikugeon.damda.web.iam;

import com.sikugeon.damda.core.iam.application.IamEditor;
import com.sikugeon.damda.web.iam.dto.IamRegisterRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class IamRestController {
    IamEditor iamEditor;
    final String GROUP_NAME = "damda";

    public IamRestController(IamEditor iamEditor){
        this.iamEditor = iamEditor;
    }

    @PostMapping("api/iam")
    public ResponseEntity<Map> register(@RequestBody IamRegisterRequest iamRegisterRequest){
        final String username = iamRegisterRequest.getUsername();
        iamEditor.createIAMUser(username);
        Map map = iamEditor.createIAMAccessKey(username);
        iamEditor.addUsertoGroup(username, GROUP_NAME);
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

}
