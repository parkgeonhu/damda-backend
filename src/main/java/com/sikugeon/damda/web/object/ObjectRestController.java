package com.sikugeon.damda.web.object;

import com.sikugeon.damda.core.object.domain.UploadInfo;
import com.sikugeon.damda.core.object.domain.Uploader;
import com.sikugeon.damda.web.object.dto.UploadRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ObjectRestController {

    Uploader uploader;

    Logger log = LoggerFactory.getLogger(ObjectRestController.class);

    public ObjectRestController(Uploader uploader) {
        this.uploader = uploader;
    }


    @PostMapping("/api/object")
    public ResponseEntity<Map<String, String>> uploadObject(@ModelAttribute UploadRequest uploadRequest) {

        List<MultipartFile> multipartFiles = uploadRequest.getImages();
        Map<String, String> uploadInfos = uploader.upload(uploadRequest.getMap(), multipartFiles, "");


//        for(MultipartFile multipartFile:multipartFiles){
//            log.debug("profilePicture: {}, {}", multipartFile.getOriginalFilename(), multipartFile.getContentType());
//
//            // multipartfile을 s3에 업로드하기
//
//            UploadInfo uploadInfo = new UploadInfo();
//            uploadInfo.setName(multipartFile.getName());
//            uploadInfo.setUri(uploadedObjectUrl);
//            uploadInfo.setUploaded_at(new Date());
//
//            uploadInfos.add(uploadInfo);
//        }


        return ResponseEntity.status(HttpStatus.ACCEPTED).body(uploadInfos);
    }

    @PostMapping("/api/object1")
    public ResponseEntity<List<UploadInfo>> uploadObject1(@RequestParam("images") List<MultipartFile> objects) throws IOException {

        List<UploadInfo> uploadInfos = new ArrayList<>();

        for (MultipartFile object : objects) {
            log.debug("profilePicture: {}, {}", object.getOriginalFilename(), object.getContentType());

            // multipartfile을 s3에 업로드하기

            File file = object.getResource().getFile();

//            URI uploadedObjectUri = objectStorage.save(object);
//
//            UploadInfo uploadInfo = new UploadInfo();
//            uploadInfo.setName(object.getName());
//            uploadInfo.setUri(uploadedObjectUri);
//            uploadInfo.setUploaded_at(new Date());
//
//            uploadInfos.add(uploadInfo);
        }


        return new ResponseEntity<>(uploadInfos, HttpStatus.ACCEPTED);
    }

}
