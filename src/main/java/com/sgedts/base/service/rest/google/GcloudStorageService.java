package com.sgedts.base.service.rest.google;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.sgedts.base.bean.UploadedFileBean;
import com.sgedts.base.constant.StorageConstant;
import com.sgedts.base.exception.BadRequestException;
import com.sgedts.base.mapper.UploadedFileMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class GcloudStorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UploadedFileMapper uploadedFileMapper;
    @Value("${services.google.cloud.storage.key-file-path}")
    private String JSON_RESOURCE_FILE_PATH;
    @Value("${services.google.cloud.storage.private.bucket-name}")
    private String PRIVATE_BUCKET_NAME;

    @Autowired
    public GcloudStorageService(UploadedFileMapper uploadedFileMapper) {
        this.uploadedFileMapper = uploadedFileMapper;
    }

    public Blob getFileBlob(String storageFilePath) {
        try {
            Bucket bucket = getPrivateBucket();
            return bucket.get(storageFilePath);
        } catch (Exception e) {
            logger.error("GcloudStorageService.getImageBlob : {}", storageFilePath, e);
            throw new BadRequestException("Image not found", false);
        }
    }

    public UploadedFileBean uploadFile(MultipartFile multipartFile) {
        if (null == multipartFile) {
            throw new BadRequestException("Not valid uploaded file");
        }
        try {
            String filename = multipartFile.getOriginalFilename();
            byte[] data = multipartFile.getBytes();
            String contentType = multipartFile.getContentType();
            String generatedFileName = UUID.randomUUID().toString();

            String storageFilePath = StringUtils.joinWith("/", StorageConstant.UPLOADED_FILE_FOLDER, generatedFileName);

            Bucket bucket = getPrivateBucket();
            InputStream inputStream = new ByteArrayInputStream(data);
            Blob blob = bucket.create(storageFilePath, inputStream, contentType);
            return uploadedFileMapper.toUploadedFileBean(generatedFileName, filename, blob);
        } catch (IOException e) {
            throw new BadRequestException("Failed get uploaded file data");
        }
    }

    private Bucket getPrivateBucket() {
        Storage storage = getStorage();
        Bucket bucket = storage.get(PRIVATE_BUCKET_NAME);
        if (bucket == null) {
            throw new BadRequestException("Gcloud storage bucket not found:" + PRIVATE_BUCKET_NAME);
        }
        return bucket;
    }

    private Storage getStorage() {
        try {
            ClassPathResource resource = new ClassPathResource(JSON_RESOURCE_FILE_PATH);
            if (resource.exists()) {
                GoogleCredentials googleCredentials = GoogleCredentials.fromStream(resource.getInputStream());
                return StorageOptions.newBuilder().setCredentials(googleCredentials).build().getService();
            } else {
                throw new BadRequestException("Gcloud credential not valid");
            }
        } catch (Exception e) {
            throw new BadRequestException("Gcloud credential not valid");
        }
    }
}
