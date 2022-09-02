package com.sikugeon.damda.core.object.infrastructure;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sikugeon.damda.core.object.domain.ObjectStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.nio.file.Path;
import java.util.Objects;
import java.util.UUID;

/**
 * 로컬 저장소 기반으로 사용자 프로필 이미지를 저장하고, 불러오는 {@link ProfilePictureStorage} 구현체
 *
 * @author springrunner.kr@gmail.com
 */
@Repository
public class AmazonS3ObjectStorage implements ObjectStorage, ResourceLoaderAware, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(AmazonS3ObjectStorage.class);

    private ResourceLoader resourceLoader;
    private Path basePath;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Value("${site.userProfilePicture.basePath:./files/user-profile-picture}")
    public void setBasePath(Path basePath) {
        this.basePath = basePath;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Objects.requireNonNull(resourceLoader, "resourceLoader is required");
        if (!Objects.requireNonNull(basePath, "basePath is required").toFile().exists()) {
            basePath.toFile().mkdirs();
            log.debug("create a directory: {}", basePath.toAbsolutePath().toUri());
        }
    }

    @Override
    public String save(AmazonS3Client amazonS3Client, File uploadFile, String bucketName, String key) {
        amazonS3Client.putObject(new PutObjectRequest(bucketName, key, uploadFile));
        String url = amazonS3Client.getUrl(bucketName, key).toString();

        return url;
    }
}
