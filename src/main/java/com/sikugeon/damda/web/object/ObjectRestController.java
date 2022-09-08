package com.sikugeon.damda.web.object;

import com.sikugeon.damda.core.aws.s3.application.S3Manager;
import com.sikugeon.damda.core.object.domain.Uploader;
import com.sikugeon.damda.core.user.domain.User;
import com.sikugeon.damda.web.object.dto.UploadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class ObjectRestController {

    Uploader uploader;

    public ObjectRestController(Uploader uploader) {
        this.uploader = uploader;
    }

    @PostMapping("/api/objects")
    public ResponseEntity<Map<String, String>> uploadObject(@ModelAttribute UploadRequest uploadRequest, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<MultipartFile> multipartFiles = uploadRequest.getImages();
        String bucketName = user.getBucketName();
        Map<String, String> awsKey = user.getAwsKey();
        Map<String, String> uploadInfos = uploader.upload(awsKey, multipartFiles, bucketName, "");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(uploadInfos);
    }
}
