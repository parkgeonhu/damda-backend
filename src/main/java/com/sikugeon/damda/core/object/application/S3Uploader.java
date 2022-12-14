package com.sikugeon.damda.core.object.application;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sikugeon.damda.core.object.domain.ObjectStorage;
import com.sikugeon.damda.core.object.domain.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class S3Uploader implements Uploader {

    ObjectStorage objectStorage;

    public S3Uploader(ObjectStorage objectStorage){
        this.objectStorage = objectStorage;
    }

    public AmazonS3Client amazonS3Client(String accessKey, String secretKey){
        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        String region = "ap-northeast-2";
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    public File multipartFileToFile(MultipartFile multipartFile){
        try {
            File file = new File(System.getProperty("user.dir")+ UUID.randomUUID()+multipartFile.getOriginalFilename());
            multipartFile.transferTo(file);

            return file;
        } catch (Exception error) {
            throw new RuntimeException(error);
        }
    }

    @Override
    public Map<String, String> upload(Map<String, String> userSession, List<MultipartFile> multipartFiles, String bucketName, String dirName) {
        // [TO-DO] UserSession ???????????? accesskey ????????????
        String accessKeyId = userSession.get("accessKeyId");
        String secretAccessKey = userSession.get("secretAccessKey");
        AmazonS3Client amazonS3Client = amazonS3Client(accessKeyId, secretAccessKey);

        Map<String, String> uploadInfos = new HashMap<>();
        for(MultipartFile multipartFile:multipartFiles){
            File uploadFile = multipartFileToFile(multipartFile);

            String key = Path.of(dirName, uploadFile.getName()).toString();
            String url = objectStorage.save(amazonS3Client, uploadFile, bucketName, key);

            uploadInfos.put(key, url);
        }

        return uploadInfos;
    }
}
