package com.sikugeon.damda.core.object.application;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sikugeon.damda.core.object.domain.ObjectStorage;
import com.sikugeon.damda.core.object.domain.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.util.*;

@Service
@Slf4j
public class S3Uploader implements Uploader {
    ObjectStorage objectStorage;

    @Value("${basePath:/home/ec2-user/}")
    private String PATH;

    public S3Uploader(ObjectStorage objectStorage) {
        this.objectStorage = objectStorage;
    }

    public AmazonS3Client amazonS3Client(String accessKey, String secretKey) {
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = "ap-northeast-2";
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public File multipartFileToFile(MultipartFile multipartFile) {
        File file = new File(PATH + multipartFile.getOriginalFilename());
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    @Override
    public Map<String, String> upload(Map<String, String> awsKey, List<MultipartFile> multipartFiles, String bucketName, String dirName) {
        String accessKeyId = awsKey.get("accessKeyId");
        String secretAccessKey = awsKey.get("secretAccessKey");
        AmazonS3Client amazonS3Client = amazonS3Client(accessKeyId, secretAccessKey);

        Map<String, String> uploadInfos = new HashMap<>();
        for (MultipartFile multipartFile : multipartFiles) {
            File uploadFile = multipartFileToFile(multipartFile);

            String key = Path.of(dirName, uploadFile.getName()).toString();
            String url = objectStorage.save(amazonS3Client, uploadFile, bucketName, key);

            uploadInfos.put(key, url);
        }

        return uploadInfos;
    }
}
