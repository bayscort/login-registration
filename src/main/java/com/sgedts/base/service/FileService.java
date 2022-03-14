package com.sgedts.base.service;

import com.google.cloud.storage.Blob;
import com.monitorjbl.xlsx.StreamingReader;
import com.sgedts.base.bean.UploadFileBean;
import com.sgedts.base.bean.UploadedFileBean;
import com.sgedts.base.constant.ExtensionTypeConstant;
import com.sgedts.base.constant.enums.MessageResourceEnum;
import com.sgedts.base.exception.BadRequestException;
import com.sgedts.base.mapper.UploadedFileMapper;
import com.sgedts.base.model.UploadedFile;
import com.sgedts.base.repository.UploadedFileRepo;
import com.sgedts.base.service.rest.google.GcloudStorageService;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final GcloudStorageService gcloudStorageService;
    private final UploadedFileMapper uploadedFileMapper;
    private final UploadedFileRepo uploadedFileRepo;

    @Autowired
    public FileService(GcloudStorageService gcloudStorageService, UploadedFileMapper uploadedFileMapper, UploadedFileRepo uploadedFileRepo) {
        this.gcloudStorageService = gcloudStorageService;
        this.uploadedFileMapper = uploadedFileMapper;
        this.uploadedFileRepo = uploadedFileRepo;
    }

    @Transactional
    public UploadFileBean createUploadedFile(MultipartFile multipartFile) {
        long totalRow = 0L;
        try (InputStream is = multipartFile.getInputStream()) {
            if (StringUtils.endsWith(multipartFile.getOriginalFilename(), ExtensionTypeConstant.XLSX)) {
                totalRow = countXlsxQuota(is);
            } else if (StringUtils.endsWith(multipartFile.getOriginalFilename(), ExtensionTypeConstant.CSV)) {
                totalRow = countCsvQuota(is);
            }
        } catch (IOException e) {
            throw new BadRequestException(MessageResourceEnum.INVALID_FILE);
        }

        UploadedFileBean uploadedFileBean = gcloudStorageService.uploadFile(multipartFile);
        uploadedFileBean.setTotalRow(totalRow);
        UploadedFile uploadedFile = uploadedFileMapper.toUploadedFile(uploadedFileBean);
        uploadedFile = uploadedFileRepo.save(uploadedFile);
        return new UploadFileBean(uploadedFile.getId());
    }

    public UploadedFileBean getUploadedFile(String fileId) {
        return uploadedFileRepo.findById(fileId)
                .map(uploadedFileMapper::toUploadedFileBean)
                .orElseThrow(() -> new BadRequestException(MessageResourceEnum.FILE_NOT_FOUND));
    }

    public int countXlsxQuota(InputStream is) throws IOException {
        int quota;
        try (Workbook workbook = StreamingReader.builder()
                .rowCacheSize(100)
                .bufferSize(4096)
                .open(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            quota = sheet.getLastRowNum() + 1;
        }
        return quota;
    }

    public int countCsvQuota(InputStream is) throws IOException {
        byte[] c = new byte[4096];
        int quota = 0;
        int readChars;
        boolean endsWithoutNewLine = false;
        while ((readChars = is.read(c)) != -1) {
            for (int i = 0; i < readChars; ++i) {
                if (c[i] == '\n')
                    ++quota;
            }
            endsWithoutNewLine = (c[readChars - 1] != '\n');
        }
        if (endsWithoutNewLine) {
            ++quota;
        }
        return quota;
    }

    public byte[] getBlob(String storageFilePath) {
        Blob blob = gcloudStorageService.getFileBlob(storageFilePath);
        return blob.getContent();
    }

}
