package com.leyou.upload.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.commom.enums.ExceptionEnum;
import com.leyou.commom.exception.LyException;
import com.leyou.upload.config.UploadProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * @Author ssqswyf
 * @Date 2021/4/21
 */

@Service
@Slf4j
@EnableConfigurationProperties(UploadProperties.class)
public class UploadService {

    private final UploadProperties prop;

    private final FastFileStorageClient storageClient;


    public UploadService(FastFileStorageClient storageClient, UploadProperties prop) {
        this.storageClient = storageClient;
        this.prop = prop;
    }

    public String uploadImage(MultipartFile file) {

        try {

            String contentType = file.getContentType();
            if (!prop.getAllowTypes().contains(contentType)) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            BufferedImage image = ImageIO.read(file.getInputStream());
            if (image == null) {
                throw new LyException(ExceptionEnum.INVALID_FILE_TYPE);
            }

            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            StorePath storePath = storageClient.uploadFile(file.getInputStream(), file.getSize(), extension, null);


            return prop.getBaseUrl() + storePath.getFullPath();

        } catch (IOException e) {
            log.error("【文件上传】 上传文件失败",e);
            throw new LyException(ExceptionEnum.UPLOAD_FILE_ERROR);
        }


    }
}
