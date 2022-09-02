package com.sikugeon.damda.web.object;

import com.sikugeon.damda.core.object.domain.Uploader;
import com.sikugeon.damda.web.object.dto.UploadRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
public class ObjectRestController {

    Uploader uploader;

    public ObjectRestController(Uploader uploader) {
        this.uploader = uploader;
    }

    @PostMapping("/api/objects")
    public ResponseEntity<Map<String, String>> uploadObject(@ModelAttribute UploadRequest uploadRequest) {

        List<MultipartFile> multipartFiles = uploadRequest.getImages();
        Map<String, String> uploadInfos = uploader.upload(uploadRequest.getMap(), multipartFiles, "");

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(uploadInfos);
    }
}
