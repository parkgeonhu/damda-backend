package com.sikugeon.damda.core.object.domain;

import com.amazonaws.services.s3.AmazonS3Client;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;

/**
 * 프로필 이미지 보관소 인터페이스
 *
 * @author springrunner.kr@gmail.com
 */
public interface ObjectStorage {

    String save(AmazonS3Client amazonS3Client, File uploadFile, String bucketName, String key);

}
