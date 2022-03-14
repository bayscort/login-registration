package com.sgedts.base.repository;

import com.sgedts.base.model.UploadedFile;
import org.springframework.data.repository.CrudRepository;

public interface UploadedFileRepo extends CrudRepository<UploadedFile, String> {
}
