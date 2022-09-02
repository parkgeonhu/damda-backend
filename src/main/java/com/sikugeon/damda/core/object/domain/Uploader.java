package com.sikugeon.damda.core.object.domain;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface Uploader {
    Map<String, String> upload(Map<String, String> userSession, List<MultipartFile> multipartFiles, String bucketName, String dirName);
}
