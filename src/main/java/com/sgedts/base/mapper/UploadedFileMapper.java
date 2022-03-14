package com.sgedts.base.mapper;

import com.google.cloud.storage.Blob;
import com.sgedts.base.bean.UploadedFileBean;
import com.sgedts.base.model.UploadedFile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UploadedFileMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "storageFilePath", source = "blob.blobId.name")
    @Mapping(target = "contentType", source = "blob.contentType")
    @Mapping(target = "bucketName", source = "blob.blobId.bucket")
    UploadedFileBean toUploadedFileBean(String id, String filename, Blob blob);

    UploadedFileBean toUploadedFileBean(UploadedFile uploadedFile);

    UploadedFile toUploadedFile(UploadedFileBean uploadedFileBean);

}

